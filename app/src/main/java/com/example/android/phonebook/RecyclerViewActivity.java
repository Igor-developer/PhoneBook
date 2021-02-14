package com.example.android.phonebook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.Surface;
import android.view.WindowManager;
import android.widget.TextView;

public class RecyclerViewActivity extends AppCompatActivity {

    public static final String REQUEST = "REQUEST";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);

        Intent intent = getIntent();
        String request = intent.getStringExtra(REQUEST);

        TextView request_view = findViewById(R.id.request_view);
        request_view.setText(
                getString(request == null ? R.string.entries : R.string.request_results, request));

        //Создание адаптера RecyclerView
        RecyclerView recyclerview = findViewById(R.id.recyclerview);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(request);
        recyclerview.setAdapter(adapter);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
    }
}
