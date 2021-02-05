package com.example.android.phonebook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Map;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
//Занятие 4.
//В приложение PhoneBook добавить RecyclerView, в которой будут отображаться все записи.

    AppCompatButton info_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Назначение слушателей кнопкам
        info_button = findViewById(R.id.info_button);
        info_button.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, RecyclerViewActivity.class));
        });

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

    //Обработчик для кнопки искать
    private void handleSearchButton() {
        EditText name_view = findViewById(R.id.name);
        String name = name_view.getText().toString().trim();

        TextView notification_view = findViewById(R.id.notification);

        Map<String, Set<String>> phones = Contacts.getContactsAsMap();

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

            Intent intent = new Intent(this, FindingsActivity.class);
            intent.putExtra("findings", findings.toString());
            name_view.setText("");
            startActivity(intent);
        } else {
            notification_view.setTextColor(getResources().getColor(R.color.red));
            notification_view.setText(getString(R.string.zero_person_phones, name));
        }
    }

}