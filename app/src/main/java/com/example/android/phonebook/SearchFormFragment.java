package com.example.android.phonebook;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.android.phonebook.sqlite.DBHelper;
import com.example.android.phonebook.sqlite.SQLiteContactsManager;

public class SearchFormFragment extends Fragment {

    public static SearchFormFragment newInstance() {
        SearchFormFragment fragment = new SearchFormFragment();

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search_form, container, false);

        AppCompatButton search_button = view.findViewById(R.id.search_button);
        search_button.setOnClickListener(v -> handleSearchButton());

        return view;
    }

    //Обработчик для кнопки искать
    private void handleSearchButton() {
        EditText name_view = getActivity().findViewById(R.id.name);
        String name = name_view.getText().toString().trim();

        TextView notification_view = getActivity().findViewById(R.id.notification);

        //Заполнено ли поле поиска
        if (name.isEmpty()) {
            notification_view.setText(R.string.empty_name_field);
            return;
        }

        //Затирание букв в поле поиска
        name_view.setText(R.string.space);

        //Поиск записи в телефонной книге
        if (SQLiteContactsManager.getInstance().findEntries(name).isEmpty()) {
            notification_view.setText(getString(R.string.zero_person_phones, name));
        } else {
            notification_view.setText(R.string.space);

            //Запуск RecyclerViewActivity
            Intent intent = new Intent(getActivity(), RecyclerViewActivity.class);
            intent.putExtra(RecyclerViewActivity.REQUEST, name);
            startActivity(intent);
        }
    }
}