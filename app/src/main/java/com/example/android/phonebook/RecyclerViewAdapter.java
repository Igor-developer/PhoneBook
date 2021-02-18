package com.example.android.phonebook;

import android.os.Bundle;
import android.util.Log;
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

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

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

    class MyViewHolder extends RecyclerView.ViewHolder {
        private View itemView;
        private TextView person;
        private TextView telephone;
        private AppCompatImageButton editButton;
        private AppCompatImageButton deleteButton;
        private AppCompatImageButton callButton;
        private AppCompatImageButton choosenButton;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            this.itemView = itemView;

            this.editButton = itemView.findViewById(R.id.edit_button);
            this.deleteButton = itemView.findViewById(R.id.delete_button);
            this.choosenButton = itemView.findViewById(R.id.choosen_button);
            this.callButton = itemView.findViewById(R.id.call_button);

            this.person = itemView.findViewById(R.id.person);
            this.telephone = itemView.findViewById(R.id.telephones);
        }

        private void bind(int position) {

            RecyclerViewActivity context = (RecyclerViewActivity) itemView.getContext();
            ContactsManager.Entry entry = contactList.get(position);
            String person = entry.getPerson();
            String phone = entry.getPhone();

            int original_index = entry.getListIndex();
            this.person.setText(person);
            this.telephone.setText(phone);

            //Слушатель для кнопки редактировать
            editButton.setOnClickListener(v -> {
                Log.d("klm", String.valueOf(position));

                AddFormFragment add_form_fragment = AddFormFragment.newInstance();

                Bundle bundle = new Bundle();
                bundle.putInt(AddFormFragment.ORIGINAL_INDEX, original_index);
                add_form_fragment.setArguments(bundle);
                context.getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.root_container, add_form_fragment)
                        .commit();
            });

            //Слушатель для кнопки удалить
            deleteButton.setOnClickListener(v -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(R.string.remove_entry_title)
                        .setMessage(context.getString(R.string.remove_entry_message,
                                person,
                                phone))
                        .setIcon(android.R.drawable.ic_delete)
                        .setPositiveButton(R.string.yes, (dialog, which) -> {
                            ContactsManager.getInstance().removeEntry(original_index);
                            Toast.makeText(context,
                                    context.getString(R.string.record_removed, person, phone),
                                    Toast.LENGTH_SHORT).show();
                            updateContactList();
                            notifyDataSetChanged();
                        })
                        .setNegativeButton(R.string.no, (dialog, which) -> {
                        });
                AlertDialog dialog = builder.create();
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
            });

            //Слушатель для кнопки добавить и удалить из избранного
            choosenButton.setOnClickListener(v -> {

            });

            //Слушатель для кнопки позвонить
            callButton.setOnClickListener(v -> {

            });
        }
    }
}