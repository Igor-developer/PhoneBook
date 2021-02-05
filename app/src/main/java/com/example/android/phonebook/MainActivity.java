package com.example.android.phonebook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class MainActivity extends AppCompatActivity {
//Занятие 2.
//Создать приложение PhoneBook. В телефонную книгу можно добавлять контакт по имени и телефон.
//Искать контакт нужно по имени.
//После добавления контакта в список обязательно очищать поля ввода.
//Ещё необходимо добавить текстовое поле с количеством контактов.

    private static Map<String, Set<String>> phones;

    public static Map getPhones() {
        if (phones == null) {
            phones = new TreeMap<>();
        }

        return phones;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Назначение слушателей кнопкам
        AppCompatButton searchButton = findViewById(R.id.search_button);
        searchButton.setOnClickListener(v -> handleSearchButton());

        AppCompatButton goToAddActivityButton = findViewById(R.id.go_to_add_activity);
        goToAddActivityButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddActivity.class);
            startActivity(intent);
        });

        showPhonesQuantity();
    }

    //Отображение количества телефонов
    private void showPhonesQuantity() {
        int quantity = MainActivity.getPhones().size();

        String info_string;
        if (quantity == 0) {
            info_string = getString(R.string.zero_phones_quantity);
        } else {
            info_string = getString(R.string.phones_quantity, quantity);
        }

        TextView info_view = findViewById(R.id.info);
        info_view.setText(info_string);
    }

    //Обработчик для кнопки искать
    private void handleSearchButton() {
        EditText name_view = findViewById(R.id.name);
        String name = name_view.getText().toString().trim();

        TextView notification_view = findViewById(R.id.notification);

        Map<String, Set<String>> phones = MainActivity.getPhones();

        //Заполнено ли поле поиска
        if (name.isEmpty()) {
            notification_view.setTextColor(getResources().getColor(R.color.red));
            notification_view.setText(R.string.empty_name_field);
            return;
        } else {
            notification_view.setTextColor(getResources().getColor(R.color.black));
            notification_view.setText(R.string.space);
        }

        //Поиск записи в телефонной книге
        if (phones.containsKey(name)) {
            StringBuilder findings = new StringBuilder();
            findings.append("Телефоны человека").append(System.lineSeparator())
                    .append(name).append(":").append(System.lineSeparator())
                    .append(System.lineSeparator());

            int n = 1;
            for (String i : phones.get(name)) {
                findings.append(n++).append(") ").append(i).append(System.lineSeparator());
            }

            notification_view.setText(findings.toString());
        } else {
            notification_view.setTextColor(getResources().getColor(R.color.red));
            notification_view.setText(getString(R.string.zero_person_phones, name));
        }
    }

}