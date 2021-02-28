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

import com.example.android.phonebook.sqlite.SQLiteContactsManager;

import java.util.Comparator;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private SQLiteContactsManager sQLiteContactsManager;

    //результаты выборки по поиску или условию
    private List<SQLiteContactsManager.Entry> contactsRetrieval;

    private String request;
    private int actionType;

    public RecyclerViewAdapter(String request, int actionType) {
        this.request = request;
        this.actionType = actionType;
        sQLiteContactsManager = SQLiteContactsManager.getInstance();

        updateRetrieval();
    }

    //Обновление результатов выборки по поиску или условию
    public void updateRetrieval() {
        if (request != null) {
            contactsRetrieval = sQLiteContactsManager.findEntries(request);
        } else if (actionType == RecyclerViewActivity.CHOSEN_BUTTON) {
            contactsRetrieval = sQLiteContactsManager.selectByChosen(true);
        } else if (actionType == RecyclerViewActivity.INFO_BUTTON) {
            contactsRetrieval = sQLiteContactsManager.findEntries(null);
        } else if (actionType == RecyclerViewActivity.LAST_ENTRIES_BUTTON) {
            contactsRetrieval = sQLiteContactsManager.findLastEntries(10);
        }
    }

    public void updateRetrieval(int actionType) {
        this.actionType = actionType;

        updateRetrieval();
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
        return contactsRetrieval.size();
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
            SQLiteContactsManager.Entry entry = contactsRetrieval.get(position);
            String person = entry.getPerson();
            String phone = entry.getPhone();

            int id = entry.getEntryId();
            this.person.setText(person);
            this.telephone.setText(phone);
            this.choosenButton.setBackgroundResource(entry.isChoosen() ?
                    R.drawable.ic_star_on_button : R.drawable.ic_star_off_button);

            //Слушатель для кнопки редактировать
            editButton.setOnClickListener(v -> {
                EditFormFragment edit_form_fragment = EditFormFragment.newInstance();

                Bundle bundle = new Bundle();
                bundle.putInt(EditFormFragment.ID, id);
                edit_form_fragment.setArguments(bundle);
                context.getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.edit_fragment_container, edit_form_fragment)
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
                            sQLiteContactsManager.removeEntry(id);
                            Toast.makeText(context,
                                    context.getString(R.string.record_removed, person, phone),
                                    Toast.LENGTH_SHORT).show();
                            updateRetrieval();
                            notifyDataSetChanged();
                            //обновление уведомления о количестве записей в телефонной книге
                            context.getCountPhonesFragment().showQuantityButtons();
                        })
                        .setNegativeButton(R.string.no, (dialog, which) -> {
                        });
                AlertDialog dialog = builder.create();
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
            });

            //Слушатель для кнопки добавить и удалить из избранного
            choosenButton.setOnClickListener(v -> {
                sQLiteContactsManager.setChosen(!sQLiteContactsManager.getEntry(id).isChoosen(), id);
                updateRetrieval();
                notifyDataSetChanged();
            });

            //Слушатель для кнопки позвонить
            callButton.setOnClickListener(v -> {

            });
        }
    }
}