package com.example.android.phonebook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class RecyclerViewActivity extends AppCompatActivity {

    CountPhonesFragment countPhonesFragment;
    public static final String REQUEST = "REQUEST";
    public static final String BUTTON = "BUTTON_TYPE";
    public static final int CHOSEN_BUTTON = 1;
    public static final int INFO_BUTTON = 2;
    public static final int LAST_ENTRIES_BUTTON = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);

        Intent intent = getIntent();
        String request = intent.getStringExtra(REQUEST);
        int button_type = intent.getIntExtra(BUTTON, -1);

        TextView request_view = findViewById(R.id.request_view);

        //Если RecyclerViewActivity вызвана в результате поиска по запросу
        if (request != null) {
            request_view.setText(getString(R.string.request_results, request));
            request_view.setVisibility(View.VISIBLE);
        }

        //Если RecyclerViewActivity вызвана в результате перехода по кнопке
        if (button_type != -1) {
            countPhonesFragment = CountPhonesFragment.newInstance();
            Bundle bundle = new Bundle();
            bundle.putInt(BUTTON, button_type);
            countPhonesFragment.setArguments(bundle);
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.count_phones_fragment, countPhonesFragment)
                    .commit();
        }

        //Создание адаптера RecyclerView
        RecyclerView recyclerview = findViewById(R.id.recyclerview);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(request, button_type);
        recyclerview.setAdapter(adapter);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
    }

    public CountPhonesFragment getCountPhonesFragment() {
        return countPhonesFragment;
    }
}
