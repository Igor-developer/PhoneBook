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

    public static final String RECYCLERVIEW_POSITION = "RECYCLERVIEW_POSITION";
    public static final String ORIGINAL_INDEX = "ORIGINAL_INDEX";
    private int position;
    private int originalIndex;
    private ContactsManager contactsManager;
    private RecyclerViewAdapter adapter;
    private boolean isAttachedToRecyclerViewActivity;
    private EditText nameView;
    private EditText phoneView;
    private TextView notificationView;
    private View darkening;

    public static AddFormFragment newInstance() {

        return new AddFormFragment();
    }

    //Извлечение аргументов
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            position = getArguments().getInt(RECYCLERVIEW_POSITION);
            originalIndex = getArguments().getInt(ORIGINAL_INDEX);
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
        AppCompatButton addButton = view.findViewById(R.id.add_button);
        AppCompatButton clearButton = view.findViewById(R.id.clear_button);
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
            nameView.setText(contactsManager.getEntry(originalIndex).getPerson());
            phoneView.setText(contactsManager.getEntry(originalIndex).getPhone());

            //- перехватить нажатия, чтобы не срабатывал нижележащий слой RecyclerView
            darkening.setOnClickListener(v -> {
            });

            //- установить слушателей кнопкам в зависимости от Activity, к которой они прикреплены
            clearButton.setOnClickListener(v -> removeThisFragment());
        } else {
            clearButton.setOnClickListener(v -> clearFields());
        }

        addButton.setOnClickListener(v -> handleAddButton());


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
        }
    }

    //Обработчик для кнопки addButton
    private void handleAddButton() {
        String name = nameView.getText().toString().trim();
        String phone = phoneView.getText().toString().trim();

        //Заполнены ли все поля
        if (name.isEmpty() || phone.isEmpty()) {
            notificationView.setText(R.string.empty_fields);
            return;
        }

        ContactsManager.Entry entry = contactsManager.getEntry(name);

        //Проверка к какой активити прикреплён фрагмент
        if (isAttachedToRecyclerViewActivity) {
            //Проверка на дубликаты
            if (entry != null && entry != contactsManager.getEntry(originalIndex)) {
                notificationView.setText(
                        entry.getPhone().equals(phone) ?
                                R.string.person_phone_exist :
                                R.string.person_exist);
                return;
            }

            //Замена записи в телефонной книге
            ContactsManager.Entry replacement =
                    new ContactsManager.Entry(nameView.getText().toString(),
                            phoneView.getText().toString());
            contactsManager.replaceEntry(originalIndex, replacement);

            //Вывод Toast сообщения о изменении записи
            Toast.makeText(getActivity(), getString(R.string.record_changed, name, phone),
                    Toast.LENGTH_SHORT).show();

            //Отмена выделения элемента RecyclerView
            adapter.cancelColorHighlight(position);

            //Обновление RecyclerView
            adapter.updateContactList();
            adapter.notifyDataSetChanged();

            //Удаление фрагмента
            getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();

        } else {
            //Проверка на дубликаты
            if (entry != null) {
                notificationView.setText(
                        entry.getPhone().equals(phone) ?
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
    }

    //Очистка полей
    private void clearFields() {
        notificationView.setText(R.string.space);
        nameView.setText("");
        phoneView.setText("");
    }

    private void removeThisFragment() {
        //Отмена выделения элемента RecyclerView
        adapter.cancelColorHighlight(position);

        //Удаление фрагмента
        getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
    }

    //Интерфейс с методами для выделения цветом и снятия выделения с выбранного элемента
    interface ColorHighlighting {

        void colorHighlight(int position);

        void cancelColorHighlight(int position);
    }
}