package com.example.android.phonebook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
//Занятие 5.
//В приложении PhoneBook реализовать «master detail flow» и фрагменты.

    ContactsManager contactsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contactsManager = ContactsManager.getInstance();

        //Создание фрагментов
        AddFormFragment add_form_fragment = AddFormFragment.newInstance();
        SearchFormFragment search_form_fragment = SearchFormFragment.newInstance();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.form_fragment, search_form_fragment)
                .commit();

        //Назначение слушателей
        //кнопке перехода на форму добавления записи и кнопке перехода на форму поиска
        AppCompatButton go_to_add_form_button = findViewById(R.id.go_to_add_form);
        AppCompatButton go_to_search_form_button = findViewById(R.id.go_to_main_activity);

        go_to_add_form_button.setOnClickListener(v -> {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.form_fragment, add_form_fragment)
                    .addToBackStack(null)
                    .commit();
            go_to_add_form_button.setEnabled(false);
            go_to_search_form_button.setEnabled(true);
        });

        go_to_search_form_button.setOnClickListener(v -> {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.form_fragment, search_form_fragment)
                    .addToBackStack(null)
                    .commit();
            go_to_add_form_button.setEnabled(true);
            go_to_search_form_button.setEnabled(false);
        });

        //Обновление уведомления о количестве записей в телефонной книге
        showPhonesQuantity();
    }

    //Отображение количества телефонов
    void showPhonesQuantity() {
        //Назначение слушателя кнопке отображения количества записей
        AppCompatButton info_button = findViewById(R.id.info_button);
        info_button.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this,
                                          RecyclerViewActivity.class)));

        int quantity = contactsManager.getSize();

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
}