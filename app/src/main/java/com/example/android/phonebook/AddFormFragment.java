package com.example.android.phonebook;

import android.os.Bundle;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.android.phonebook.room_db.Entry;
import com.example.android.phonebook.room_db.PhoneBookDao;
import com.example.android.phonebook.room_db.RoomSingleton;

public class AddFormFragment extends Fragment {

    private PhoneBookDao room;
    private EditText nameView;
    private EditText phoneView;
    private TextView notificationView;

    public static AddFormFragment newInstance() {

        return new AddFormFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_form, container, false);

        room = RoomSingleton.getRoom();

        //Получение необходимых View
        nameView = view.findViewById(R.id.name);
        phoneView = view.findViewById(R.id.phone);
        notificationView = view.findViewById(R.id.notification);

        AppCompatButton addButton = view.findViewById(R.id.add_button);
        addButton.setOnClickListener(v -> handleAddButton());

        AppCompatButton clearButton = view.findViewById(R.id.clear_button);
        clearButton.setOnClickListener(v -> clearFields());

        return view;
    }

    //Обработчик для кнопки addButton
    private void handleAddButton() {
        String name = nameView.getText().toString().trim();
        String phone = phoneView.getText().toString().trim();

        //Заполнены ли все поля
        if (name.isEmpty() || phone.isEmpty()) {
            notificationView.setText(R.string.empty_fields);
            return;
        }

        //Проверка на дубликаты
        Entry entry = room.getEntry(name);
        if (entry != null) {
            notificationView.setText(
                    entry.getPhone().equals(phone) ?
                            R.string.person_phone_exist :
                            R.string.person_exist);
            return;
        }

        //Добавление записи в телефонную книгу
        room.addEntry(new Entry(name, phone));

        //Вывод Toast сообщения о создании записи
        Toast.makeText(getActivity(), getString(R.string.record_created, name, phone),
                Toast.LENGTH_SHORT).show();

        //Обновление уведомления о количестве записей в телефонной книге
        ((MainActivity) getActivity()).getCountPhonesFragment().showQuantityButtons();

        //Очистка полей
        clearFields();

    }

    //Очистка полей
    private void clearFields() {
        notificationView.setText(R.string.space);
        nameView.setText("");
        phoneView.setText("");
    }
}