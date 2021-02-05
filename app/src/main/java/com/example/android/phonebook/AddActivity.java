package com.example.android.phonebook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class AddActivity extends AppCompatActivity {

    AppCompatButton info_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        //Назначение слушателей кнопкам
        info_button = findViewById(R.id.info_button);
        info_button.setOnClickListener(v -> {
            startActivity(new Intent(AddActivity.this, RecyclerViewActivity.class));
        });

        AppCompatButton addButton = findViewById(R.id.add_button);
        addButton.setOnClickListener(v -> handleAddButton());

        AppCompatButton goToSearchActivityButton = findViewById(R.id.go_to_main_activity);
        goToSearchActivityButton.setOnClickListener(v -> {
            Intent intent = new Intent(AddActivity.this, MainActivity.class);
            startActivity(intent);
        });

        showPhonesQuantity();
    }

    //Отображение количества телефонов
    private void showPhonesQuantity() {
        int quantity = Contacts.getContactsAsMap().size();

        String info_string;
        if (quantity == 0) {
            info_string = getString(R.string.zero_phones_quantity);
            info_button.setEnabled(false);
        } else {
            info_string = getString(R.string.phones_quantity, quantity);
            info_button.setEnabled(true);
        }

        info_button.setText(info_string);
    }

    //Обработчик для кнопки добавить
    private void handleAddButton() {
        EditText name_view = findViewById(R.id.name);
        String name = name_view.getText().toString().trim();

        EditText phone_view = findViewById(R.id.phone);
        String phone = phone_view.getText().toString().trim();

        TextView notification_view = findViewById(R.id.notification);

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
        Toast.makeText(this, getString(R.string.record_created, name, phone),
                Toast.LENGTH_SHORT).show();

        //Обновление уведомления о количестве записей в телефонной книге
        showPhonesQuantity();
    }
}