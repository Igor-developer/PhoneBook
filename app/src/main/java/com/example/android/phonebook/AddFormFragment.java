package com.example.android.phonebook;

import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class AddFormFragment extends Fragment {

    public static final String KEY = "KEY";
    private ContactsManager contactsManager;
    private RecyclerViewAdapter adapter;
    private int index;
    private boolean isAttachedToRecyclerViewActivity;
    private EditText nameView;
    private EditText phoneView;
    private TextView notificationView;
    private AppCompatButton addButton;
    private AppCompatButton clearButton;
    private View darkening;

    public static AddFormFragment newInstance() {
        AddFormFragment fragment = new AddFormFragment();

        return fragment;
    }

    //Извлечение аргументов
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            index = getArguments().getInt(KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_form, container, false);

        contactsManager = ContactsManager.getInstance();

        //Данный фрагмент используется в двух Activity, нужна проверка где он прикреплён
        isAttachedToRecyclerViewActivity = (getActivity().getClass() == RecyclerViewActivity.class);

        //Получение необходимых View
        nameView = view.findViewById(R.id.name);
        phoneView = view.findViewById(R.id.phone);
        notificationView = view.findViewById(R.id.notification);
        addButton = view.findViewById(R.id.add_button);
        clearButton = view.findViewById(R.id.clear_button);
        darkening = getActivity().findViewById(R.id.darkening);

        //При условии прикрепления к RecyclerViewActivity
        if (isAttachedToRecyclerViewActivity) {
            adapter = (RecyclerViewAdapter)
                    ((RecyclerView) getActivity().findViewById(R.id.recyclerview)).getAdapter();

            //- изменить надписи кнопок и размер фрагмента
            ((AppCompatButton) view.findViewById(R.id.add_button)).setText(R.string.save_person);
            ((AppCompatButton) view.findViewById(R.id.clear_button)).setText(R.string.cancel);

            LinearLayout buttons_group = view.findViewById(R.id.buttons_group);
            LinearLayout.LayoutParams buttons_group_layout_params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            buttons_group.setLayoutParams(buttons_group_layout_params);

            //- извлечь и отобразить значения полей
            nameView.setText(contactsManager.getEntry(index).getPerson());
            phoneView.setText(contactsManager.getEntry(index).getPhone());

            //- перехватить нажатия, чтобы не срабатывал нижележащий слой RecyclerView
            darkening.setOnClickListener(v -> {
            });

            //- установить слушателей кнопкам в зависимости от Activity, к которой они прикреплены
            addButton.setOnClickListener(v -> handleButtonsOnRecyclerViewActivity(v));
            clearButton.setOnClickListener(v -> startFragmentSuicide());
        } else {
            addButton.setOnClickListener(v -> handleButtonsOnMainActivity(v));
            clearButton.setOnClickListener(v -> clearFields());
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        //Анимация фрагмента (затемнение, разворачивание)
        if (isAttachedToRecyclerViewActivity) {
            View view = getView();

            darkening.setVisibility(View.VISIBLE);
            darkening
                    .animate()
                    .alpha(0.66F)
                    .setDuration(150);

            view.setScaleX(0F);
            view.setScaleY(0F);
            view
                    .animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(200);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        //Анимация фрагмента
        if (isAttachedToRecyclerViewActivity) {
            //- отмена затемнения)
            darkening.setAlpha(0F);
            darkening.setVisibility(View.GONE);

            //- отмена выделения элемента RecyclerView
            adapter.cancelColorHighlight(index);
        }
    }

    //Обработчик для кнопки addButton при прикреплении к MainActivity
    private void handleButtonsOnMainActivity(View v) {
        String name = nameView.getText().toString().trim();
        String phone = phoneView.getText().toString().trim();

        //Заполнены ли все поля и нет ли дубликатов
        if (name.isEmpty() || phone.isEmpty()) {
            notificationView.setText(R.string.empty_fields);
            return;
        } else if (contactsManager.containsPerson(name)) {
            notificationView.setText(
                    contactsManager.getEntry(name).getPhone().equals(phone) ?
                            R.string.person_phone_exist :
                            R.string.person_exist);
            return;
        }

        //Добавление записи в телефонную книгу
        contactsManager.addEntry(name, phone);

        //Вывод Toast сообщения о создании записи
        Toast.makeText(getActivity(), getString(R.string.record_created, name, phone),
                Toast.LENGTH_SHORT).show();

        //Обновление уведомления о количестве записей в телефонной книге
        ((MainActivity) getActivity()).showPhonesQuantity();

        //Очистка полей
        clearFields();
    }

    //Обработчик для кнопки addButton при прикреплении к RecyclerViewActivity
    private void handleButtonsOnRecyclerViewActivity(View v) {
        String name = nameView.getText().toString().trim();
        String phone = phoneView.getText().toString().trim();

        //Заполнены ли все поля и нет ли дубликатов
        if (name.isEmpty() || phone.isEmpty()) {
            notificationView.setText(R.string.empty_fields);
            return;
        }

        //Замена записи в телефонной книге
        ContactsManager.Entry replacement =
                new ContactsManager.Entry(nameView.getText().toString(),
                        phoneView.getText().toString());
        contactsManager.replace(index, replacement);

        //Обновление RecyclerView
        adapter.notifyItemChanged(index);

        //Удаление фрагмента
        startFragmentSuicide();
    }

    //Очистка полей
    private void clearFields() {
        notificationView.setText(R.string.space);
        nameView.setText("");
        phoneView.setText("");
    }

    //Удаление фрагмента
    private void startFragmentSuicide() {
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .remove(this)
                .commit();
    }

    //Интерфейс с методами для выделения цветом и снятия выделения с выбранного элемента
    interface ColorHighlighting {

        void colorHighlight(int index);

        void cancelColorHighlight(int index);
    }
}