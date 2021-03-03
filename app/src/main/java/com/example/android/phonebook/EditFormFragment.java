package com.example.android.phonebook;

import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.android.phonebook.room_db.Entry;
import com.example.android.phonebook.room_db.PhoneBookDao;
import com.example.android.phonebook.room_db.RoomSingleton;

public class EditFormFragment extends Fragment {

    public static final String ID = "ID";
    private int id;
    private Entry entry;
    private PhoneBookDao room;
    private RecyclerViewAdapter adapter;
    private EditText nameView;
    private EditText phoneView;
    private TextView notificationView;
    private View darkening;

    public static EditFormFragment newInstance() {

        return new EditFormFragment();
    }

    //Извлечение аргументов
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            id = getArguments().getInt(ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_form, container, false);

        room = RoomSingleton.getRoom();

        adapter = (RecyclerViewAdapter)
                ((RecyclerView) getActivity().findViewById(R.id.recyclerview)).getAdapter();

        //Получение необходимых View
        nameView = view.findViewById(R.id.name);
        phoneView = view.findViewById(R.id.phone);
        notificationView = view.findViewById(R.id.notification);
        darkening = getActivity().findViewById(R.id.darkening);

        //- извлечь и отобразить значения полей
        entry = this.room.getEntry(id);
        nameView.setText(entry.getPerson());
        phoneView.setText(entry.getPhone());

        //- перехватить нажатия, чтобы не срабатывал нижележащий слой RecyclerView
        darkening.setOnClickListener(v -> {});

        //- установить слушателей кнопкам
        AppCompatButton addButton = view.findViewById(R.id.add_button);
        addButton.setOnClickListener(v -> handleAddButton());

        AppCompatButton clearButton = view.findViewById(R.id.clear_button);
        clearButton.setOnClickListener(v -> getActivity()
                .getSupportFragmentManager().beginTransaction().remove(this).commit());

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        //Анимация фрагмента (затемнение, разворачивание)
        View view = getView();

        darkening.setVisibility(View.VISIBLE);
        darkening
                .animate()
                .alpha(0.66F)
                .setDuration(150);

        view.setScaleX(0F);
        view.setScaleY(0F);
        view
                .animate()
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(200);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        //Анимация фрагмента - отмена затемнения
        darkening.setAlpha(0F);
        darkening.setVisibility(View.GONE);
    }

    //Обработчик для кнопки addButton
    private void handleAddButton() {
        String person = nameView.getText().toString().trim();
        String phone = phoneView.getText().toString().trim();

        //Заполнены ли все поля
        if (person.isEmpty() || phone.isEmpty()) {
            notificationView.setText(R.string.empty_fields);
            return;
        }

        //Проверка на дубликаты
        Entry detected_entry = room.getEntry(person);
        if (detected_entry != null && detected_entry.getPerson().equals(person)
                && id != detected_entry.getId()) {  //и не является текущей редактируемой записью
            notificationView.setText(
                    detected_entry.getPhone().equals(phone) ?
                            R.string.person_phone_exist :
                            R.string.person_exist);
            return;
        }

        //Обновление записи в телефонной книге
        entry.setPerson(person);
        entry.setPhone(phone);
        room.updateEntry(entry);

        //Вывод Toast сообщения о изменении записи
        Toast.makeText(getActivity(), getString(R.string.record_changed, person, phone),
                Toast.LENGTH_SHORT).show();

        //Обновление RecyclerView
        adapter.updateRetrieval();
        adapter.notifyDataSetChanged();

        //Удаление фрагмента
        getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
    }
}