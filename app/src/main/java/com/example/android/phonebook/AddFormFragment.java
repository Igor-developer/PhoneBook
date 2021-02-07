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
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class AddFormFragment extends Fragment {

    public static AddFormFragment newInstance() {
        AddFormFragment fragment = new AddFormFragment();

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_form, container, false);

        AppCompatButton add_button = view.findViewById(R.id.add_button);
        add_button.setOnClickListener(v -> handleAddButton());

        return view;
    }

    //Обработчик для кнопки добавить
    private void handleAddButton() {
        EditText name_view = getActivity().findViewById(R.id.name);
        String name = name_view.getText().toString().trim();

        EditText phone_view = getActivity().findViewById(R.id.phone);
        String phone = phone_view.getText().toString().trim();

        TextView notification_view = getActivity().findViewById(R.id.notification);

        Map<String, Set<String>> phones = Contacts.getContactsAsMap();

        //Заполнены ли все поля и нет ли дубликатов
        if (name.isEmpty()) {
            notification_view.setTextColor(getResources().getColor(R.color.red));
            notification_view.setText(R.string.empty_name_field);
            return;
        } else if (phone.isEmpty()) {
            notification_view.setTextColor(getResources().getColor(R.color.red));
            notification_view.setText(R.string.empty_phone_field);
            return;
        } else if (phones.containsKey(name) && phones.get(name).contains(phone)) {
            notification_view.setTextColor(getResources().getColor(R.color.red));
            notification_view.setText(R.string.duplicate_phone);
            return;
        } else {
            notification_view.setTextColor(getResources().getColor(R.color.black));
            notification_view.setText(R.string.space);
        }

        //Добавление записи в телефонную книгу
        Set<String> person_telephones = phones.containsKey(name) ? phones.get(name) : new TreeSet<>();
        person_telephones.add(phone);
        phones.put(name, person_telephones);

        //Очистка полей ввода
        name_view.setText("");
        phone_view.setText("");

        //Вывод Toast сообщения о создании записи
        Toast.makeText(getActivity(), getString(R.string.record_created, name, phone),
                Toast.LENGTH_SHORT).show();

        //Обновление уведомления о количестве записей в телефонной книге
        ((MainActivity) getActivity()).showPhonesQuantity();
    }
}