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
import java.util.Map;
import java.util.Set;

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

            Intent intent = new Intent(getActivity(), FindingsActivity.class);
            intent.putExtra("findings", findings.toString());
            name_view.setText("");
            startActivity(intent);
        } else {
            notification_view.setTextColor(getResources().getColor(R.color.red));
            notification_view.setText(getString(R.string.zero_person_phones, name));
        }
    }
}