package com.example.android.phonebook;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

public class CountPhonesFragment extends Fragment {

    View view;

    public static CountPhonesFragment newInstance() {

        return new CountPhonesFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_count_phones, container, false);

        //Обновление уведомления о количестве записей в телефонной книге
        showPhonesQuantity();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        //Обновление уведомления о количестве записей в телефонной книге
        showPhonesQuantity();
    }

    //Отображение количества телефонов
    public void showPhonesQuantity() {
        //Получение кнопок и их ViewGroup
        FrameLayout chosen_info_border = view.findViewById(R.id.chosen_info_border);
        FrameLayout last_phones_info_border = view.findViewById(R.id.last_phones_info_border);
        AppCompatButton chosen_info_button = view.findViewById(R.id.chosen_info_button);
        AppCompatButton info_button = view.findViewById(R.id.info_button);
        AppCompatButton last_phones_info_button = view.findViewById(R.id.last_phones_info_button);

        //Назначение слушателей кнопкам
        chosen_info_button.setOnClickListener(v ->
                startActivity(new Intent(getActivity(), RecyclerViewActivity.class)));

        info_button.setOnClickListener(v ->
                startActivity(new Intent(getActivity(), RecyclerViewActivity.class)));

        last_phones_info_button.setOnClickListener(v ->
                startActivity(new Intent(getActivity(), RecyclerViewActivity.class)));

        //Отображение кнопок
        int quantity = ContactsManager.getInstance().getSize();
        if (quantity == 0) {
            info_button.setText(getString(R.string.zero_phones_quantity));

            chosen_info_button.setEnabled(false);
            info_button.setEnabled(false);
            last_phones_info_button.setEnabled(false);

            chosen_info_border.setVisibility(View.GONE);
            last_phones_info_border.setVisibility(View.GONE);
        } else {
            chosen_info_button.setText(getString(R.string.chosen_quantity));
            info_button.setText(getString(R.string.phones_quantity, quantity));
            last_phones_info_button.setText(getString(R.string.last_phones));

            chosen_info_button.setEnabled(true);
            info_button.setEnabled(true);
            last_phones_info_button.setEnabled(true);

            chosen_info_border.setVisibility(View.VISIBLE);
            last_phones_info_border.setVisibility(View.VISIBLE);
        }
    }
}