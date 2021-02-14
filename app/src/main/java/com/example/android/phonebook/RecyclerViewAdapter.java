package com.example.android.phonebook;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
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
                recyclerView.getResources().getColor(R.color.accent));
    }

    @Override
    public void cancelColorHighlight(int position) {
        recyclerView.getChildAt(position).setBackgroundColor(
                recyclerView.getResources().getColor(R.color.light_lilac));
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        View itemView;
        TextView person;
        TextView telephones;
        AppCompatImageButton edit_button;
        AppCompatImageButton delete_button;
        AppCompatImageButton call_button;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            this.itemView = itemView;

            this.edit_button = itemView.findViewById(R.id.edit_button);
            this.delete_button = itemView.findViewById(R.id.delete_button);
            this.call_button = itemView.findViewById(R.id.call_button);

            this.person = itemView.findViewById(R.id.person);
            this.telephones = itemView.findViewById(R.id.telephones);
        }

        private void bind(int position) {

            ContactsManager.Entry entry = contactList.get(position);

            person.setText(entry.getPerson());
            telephones.setText(entry.getPhone());
            int original_index = entry.getListIndex();

            edit_button.setOnClickListener(v -> {

                RecyclerViewActivity context = (RecyclerViewActivity) itemView.getContext();

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

            delete_button.setOnClickListener(v -> {
//                colorHighlight(position);
//                cancelColorHighlight(position);

            });

            call_button.setOnClickListener(v -> {
//                colorHighlight(position);
//                cancelColorHighlight(position);

            });
        }
    }
}