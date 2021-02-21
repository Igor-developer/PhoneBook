package com.example.android.phonebook;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CountPhonesFragment extends Fragment {

    View view;

    AppCompatButton chosen_info_button;
    AppCompatButton info_button;
    AppCompatButton last_entries_info_button;

    int buttonType;

    public static CountPhonesFragment newInstance() {

        return new CountPhonesFragment();
    }

    //Извлечение аргументов
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            buttonType = getArguments().getInt(RecyclerViewActivity.BUTTON);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_count_phones, container, false);

        //Получение кнопок
        chosen_info_button = view.findViewById(R.id.chosen_info_button);
        info_button = view.findViewById(R.id.info_button);
        last_entries_info_button = view.findViewById(R.id.last_phones_info_button);

        //Первоначальное раскрашивание кнопок
        ButtonsMediator buttons_mediator = new ButtonsMediator();
        switch (buttonType) {
            case RecyclerViewActivity.CHOSEN_BUTTON:
                buttons_mediator.markButton(chosen_info_button);
                break;
            case RecyclerViewActivity.INFO_BUTTON:
                buttons_mediator.markButton(info_button);
                break;
            case RecyclerViewActivity.LAST_ENTRIES_BUTTON:
                buttons_mediator.markButton(last_entries_info_button);
                break;
        }

        //Назначение слушателей кнопкам
        //в зависимости от прикрепления кнопки к MainActivity или RecyclerViewActivity
        if (getActivity().getClass() == MainActivity.class) {
            chosen_info_button.setOnClickListener(v -> {
                Intent intent = new Intent(getActivity(), RecyclerViewActivity.class);
                intent.putExtra(RecyclerViewActivity.BUTTON, RecyclerViewActivity.CHOSEN_BUTTON);
                startActivity(intent);
            });

            info_button.setOnClickListener(v -> {
                Intent intent = new Intent(getActivity(), RecyclerViewActivity.class);
                intent.putExtra(RecyclerViewActivity.BUTTON, RecyclerViewActivity.INFO_BUTTON);
                startActivity(intent);
            });

            last_entries_info_button.setOnClickListener(v -> {
                Intent intent = new Intent(getActivity(), RecyclerViewActivity.class);
                intent.putExtra(RecyclerViewActivity.BUTTON, RecyclerViewActivity.LAST_ENTRIES_BUTTON);
                startActivity(intent);
            });
        } else {
            RecyclerViewAdapter adapter = (RecyclerViewAdapter)
                    ((RecyclerView) getActivity().findViewById(R.id.recyclerview)).getAdapter();

            chosen_info_button.setOnClickListener(v -> {
                adapter.updateContactList(RecyclerViewActivity.CHOSEN_BUTTON);
                adapter.notifyDataSetChanged();
                buttons_mediator.markButton(chosen_info_button);
            });

            info_button.setOnClickListener(v -> {
                adapter.updateContactList(RecyclerViewActivity.INFO_BUTTON);
                adapter.notifyDataSetChanged();
                buttons_mediator.markButton(info_button);
            });

            last_entries_info_button.setOnClickListener(v -> {
                adapter.updateContactList(RecyclerViewActivity.LAST_ENTRIES_BUTTON);
                adapter.notifyDataSetChanged();
                buttons_mediator.markButton(last_entries_info_button);
            });
        }

        //Обновление уведомления о количестве записей в телефонной книге
        showQuantityButtons();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        //Обновление уведомления о количестве записей в телефонной книге
        showQuantityButtons();
    }

    //Отображение кнопок
    public void showQuantityButtons() {
        //Отображение кнопок
        int count = ContactsManager.getInstance().getSize();

        info_button.setText(count == 0 ? getString(R.string.zero_phones_quantity) :
                getString(R.string.phones_quantity, count));

        chosen_info_button.setEnabled(count != 0);
        info_button.setEnabled(count != 0);
        last_entries_info_button.setEnabled(count != 0);

        chosen_info_button.setVisibility(count == 0 ? View.GONE : View.VISIBLE);
        last_entries_info_button.setVisibility(count == 0 ? View.GONE : View.VISIBLE);
    }


    //Класс для эмуляции поведения кнопок, как RadioButton
    private class ButtonsMediator {

        private void markButton(AppCompatButton appCompatButton) {
            if (appCompatButton == chosen_info_button) {
                distinguishButton(chosen_info_button);
                cancelDistinguishButton(info_button);
                cancelDistinguishButton(last_entries_info_button);
            } else if (appCompatButton == info_button) {
                cancelDistinguishButton(chosen_info_button);
                distinguishButton(info_button);
                cancelDistinguishButton(last_entries_info_button);
            } else if (appCompatButton == last_entries_info_button) {
                cancelDistinguishButton(chosen_info_button);
                cancelDistinguishButton(info_button);
                distinguishButton(last_entries_info_button);
            }
        }

        //Выделение кнопки цветом
        private void distinguishButton(AppCompatButton appCompatButton) {
            appCompatButton.setBackgroundColor(getResources().getColor(R.color.primary_lilac_dark));
            appCompatButton.setTextColor(getResources().getColor(R.color.white));
            appCompatButton.setTypeface(null, Typeface.BOLD);
            appCompatButton.setEnabled(false);
        }

        //Отмена выделения кнопки цветом
        private void cancelDistinguishButton(AppCompatButton appCompatButton) {
            appCompatButton.setBackgroundColor(getResources().getColor(R.color.white));
            appCompatButton.setTextColor(getResources().getColor(R.color.black));
            appCompatButton.setTypeface(null, Typeface.NORMAL);
            appCompatButton.setEnabled(true);
        }
    }
}