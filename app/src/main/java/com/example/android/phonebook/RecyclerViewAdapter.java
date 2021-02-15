package com.example.android.phonebook;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>
        implements AddFormFragment.ColorHighlighting {

    private RecyclerView recyclerView;
    private List<ContactsManager.Entry> contactList;
    String request;

    public RecyclerViewAdapter(String request) {
        this.request = request;
        updateContactList();
    }

    public void updateContactList() {
        this.contactList = ContactsManager.getInstance().findEntries(request);
    }

    //Накачиваем лейаут и создаём ViewHolder
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        recyclerView = parent.findViewById(R.id.recyclerview);

        View inflate = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.rv_element, parent, false);

        return new MyViewHolder(inflate);
    }

    //Заносим реальные данные в TextView элемента списка
    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.MyViewHolder holder, int position) {
        holder.bind(position);
    }

    //В методе нужно указать количество элементов RecyclerView
    @Override
    public int getItemCount() {
        return contactList.size();
    }

    //Методы интерфейса AddFormFragment.ColorHighlighting по выделению выбранного элемента цветом
    @Override
    public void colorHighlight(int position) {
        recyclerView.getChildAt(position).setBackgroundColor(
                recyclerView.getResources().getColor(R.color.primary_lilac_dark));
    }

    @Override
    public void cancelColorHighlight(int position) {
        recyclerView.getChildAt(position).setBackgroundColor(
                recyclerView.getResources().getColor(R.color.primary_lilac));
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private View itemView;
        private TextView person;
        private TextView telephone;
        private AppCompatImageButton edit_button;
        private AppCompatImageButton delete_button;
        private AppCompatImageButton call_button;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            this.itemView = itemView;

            this.edit_button = itemView.findViewById(R.id.edit_button);
            this.delete_button = itemView.findViewById(R.id.delete_button);
            this.call_button = itemView.findViewById(R.id.call_button);

            this.person = itemView.findViewById(R.id.person);
            this.telephone = itemView.findViewById(R.id.telephones);
        }

        private void bind(int position) {

            ContactsManager.Entry entry = contactList.get(position);
            String person = entry.getPerson();
            String phone = entry.getPhone();

            this.person.setText(person);
            this.telephone.setText(phone);
            int original_index = entry.getListIndex();

            RecyclerViewActivity context = (RecyclerViewActivity) itemView.getContext();

            //Слушатель для кнопки редактировать
            edit_button.setOnClickListener(v -> {
                colorHighlight(position);

                AddFormFragment add_form_fragment = AddFormFragment.newInstance();

                Bundle bundle = new Bundle();
                bundle.putInt(AddFormFragment.RECYCLERVIEW_POSITION, position);
                bundle.putInt(AddFormFragment.ORIGINAL_INDEX, original_index);
                add_form_fragment.setArguments(bundle);
                context.getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.root_container, add_form_fragment)
                        .commit();
            });

            //Слушатель для кнопки удалить
            delete_button.setOnClickListener(v -> {
                colorHighlight(position);
                new AlertDialog.Builder(context)
                        .setTitle(R.string.remove_entry_title)
                        .setMessage(context.getString(R.string.remove_entry_message,
                                person,
                                phone))
                        .setIcon(android.R.drawable.ic_delete)
                        .setPositiveButton(R.string.yes, (dialog, which) -> {
                            ContactsManager.getInstance().removeEntry(original_index);
                            Toast.makeText(context,
                                    context.getString(R.string.record_removed, person, phone),
                                    Toast.LENGTH_SHORT).show();
                            cancelColorHighlight(position);
                            updateContactList();
                            notifyDataSetChanged();
                        })
                        .setNegativeButton(R.string.no, (dialog, which) -> {
                            cancelColorHighlight(position);
                        })
                        .create()
                        .show();
            });

            //Слушатель для кнопки позвонить
            call_button.setOnClickListener(v -> {
//                colorHighlight(position);
//                cancelColorHighlight(position);

            });
        }
    }
}