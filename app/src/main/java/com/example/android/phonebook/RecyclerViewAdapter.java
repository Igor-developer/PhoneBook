package com.example.android.phonebook;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private final ContactsManager contactsManager;

    public RecyclerViewAdapter() {
        this.contactsManager = ContactsManager.getInstance();
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
        holder.bind(contactsManager.getEntry(position));
    }

    //В методе нужно указать количество элементов RecyclerView
    @Override
    public int getItemCount() {
        return contactsManager.getSize();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView person;
        TextView telephones;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            person = itemView.findViewById(R.id.person);
            telephones = itemView.findViewById(R.id.telephones);

        }

        private void bind(ContactsManager.Entry entry) {
            person.setText(entry.getPerson());
            telephones.setText(entry.getPhone());
        }
    }
}