package com.example.android.phonebook;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
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

import com.example.android.phonebook.room_db.Entry;
import com.example.android.phonebook.room_db.PhoneBookDao;
import com.example.android.phonebook.room_db.RoomSingleton;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private final PhoneBookDao room;

    //результаты выборки по поиску или условию
    private List<Entry> contactsRetrieval;

    private final String request;
    private int actionType;

    public RecyclerViewAdapter(String request, int actionType) {
        this.request = request;
        this.actionType = actionType;
        this.room = RoomSingleton.getRoom();

        updateRetrieval();
    }

    //Обновление результатов выборки по поиску или условию
    public void updateRetrieval() {
        if (request != null) {
            contactsRetrieval = room.findEntries(request);
        } else if (actionType == RecyclerViewActivity.CHOSEN_BUTTON) {
            contactsRetrieval = room.selectByChosen(true);
        } else if (actionType == RecyclerViewActivity.INFO_BUTTON) {
            contactsRetrieval = room.getAllEntries();
        } else if (actionType == RecyclerViewActivity.LAST_ENTRIES_BUTTON) {
            contactsRetrieval = room.getLastEntries(10);
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
        private final View itemView;
        private final TextView personTV;
        private final TextView telephoneTV;
        private final AppCompatImageButton editButton;
        private final AppCompatImageButton deleteButton;
        private final AppCompatImageButton callButton;
        private final AppCompatImageButton chosenButton;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            this.itemView = itemView;
            editButton = itemView.findViewById(R.id.edit_button);
            deleteButton = itemView.findViewById(R.id.delete_button);
            chosenButton = itemView.findViewById(R.id.choosen_button);
            callButton = itemView.findViewById(R.id.call_button);
            personTV = itemView.findViewById(R.id.person);
            telephoneTV = itemView.findViewById(R.id.telephones);
        }

        private void bind(int position) {
            Entry entry = contactsRetrieval.get(position);

            personTV.setText(entry.getPerson());
            telephoneTV.setText(entry.getPhone());
            chosenButton.setBackgroundResource(entry.isChosen() ?
                    R.drawable.ic_star_on_button : R.drawable.ic_star_off_button);

            RecyclerViewActivity context = (RecyclerViewActivity) itemView.getContext();

            //Слушатель для кнопки редактировать
            editButton.setOnClickListener(v -> {
                EditFormFragment edit_form_fragment = EditFormFragment.newInstance();

                Bundle bundle = new Bundle();
                bundle.putInt(EditFormFragment.ID, entry.getId());
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
                                entry.getPerson(), entry.getPhone()))
                        .setIcon(android.R.drawable.ic_delete)
                        .setPositiveButton(R.string.yes, (dialog, which) -> {
                            room.deleteEntry(entry);
                            Toast.makeText(context,
                                    context.getString(R.string.record_removed,
                                            entry.getPerson(), entry.getPhone()),
                                    Toast.LENGTH_SHORT).show();
                            updateRetrieval();
                            notifyDataSetChanged();
                            //обновление уведомления о количестве записей в телефонной книге
                            if (request == null) {
                                context.getCountPhonesFragment().showQuantityButtons();
                            }
                            //если не осталось записей для отображения
                            //- в поисковом запросе или в базе данных
                            if ((request != null && contactsRetrieval.size() == 0)
                                    || room.getEntriesCount() == 0) {
                                context.finish();
                            }
                        })
                        .setNegativeButton(R.string.no, (dialog, which) -> {
                        });
                AlertDialog dialog = builder.create();
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
            });

            //Слушатель для кнопки добавить и удалить из избранного
            chosenButton.setOnClickListener(v -> {
                entry.setChosen(!entry.isChosen());
                room.updateEntry(entry);
                updateRetrieval();
                notifyDataSetChanged();
            });

            //Слушатель для кнопки позвонить
            callButton.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + entry.getPhone()));
                try {
                    context.startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(context, context.getString(R.string.no_resolve_activity),
                            Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}