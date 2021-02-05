package com.example.android.phonebook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

public class FindingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_findings);

        TextView phone_numbers_view = findViewById(R.id.phone_numbers);
        Intent intent = getIntent();
        phone_numbers_view.setText(intent.getStringExtra("findings"));

        ImageButton backButton = findViewById(R.id.back_arrow);
        backButton.setOnClickListener(v -> finish());
    }
}