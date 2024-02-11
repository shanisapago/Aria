package com.example.aria;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class AddCalendarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_calendar);


        ImageButton btnClose = findViewById(R.id.btnClose);
        btnClose.setOnClickListener(view -> {
            Intent intent = new Intent(this, CalendarActivity.class);
            startActivity(intent);
        });

        ImageButton btnDone = findViewById(R.id.btnDone);
        btnDone.setOnClickListener(view -> {
            Intent intent = new Intent(this, CalendarActivity.class);
            startActivity(intent);
        });
    }
}
