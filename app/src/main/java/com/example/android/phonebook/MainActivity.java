package com.example.android.phonebook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
//Занятие 8.
//В приложении PhoneBook интегрировать Room и хранить контакты в ней.

    CountPhonesFragment countPhonesFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Создание фрагментов
        AddFormFragment add_form_fragment = AddFormFragment.newInstance();
        SearchFormFragment search_form_fragment = SearchFormFragment.newInstance();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.form_fragment, add_form_fragment)
                .commit();
        countPhonesFragment = CountPhonesFragment.newInstance();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.count_phones_fragment, countPhonesFragment)
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
    }


    public CountPhonesFragment getCountPhonesFragment() {
        return countPhonesFragment;
    }
}