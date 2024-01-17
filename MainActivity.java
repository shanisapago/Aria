package com.example.aria;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btnCalendar = findViewById(R.id.btnCalendar);
        btnCalendar.setOnClickListener(view -> {
            Intent intent = new Intent(this, CalendarActivity.class);
            startActivity(intent);
        });
        Button btnAddAria = findViewById(R.id.btnAddAria);
        btnAddAria.setOnClickListener(view -> {
            Intent intent = new Intent(this, AddAriaActivity.class);
            startActivity(intent);
        });
        Button btnAddCalendar = findViewById(R.id.btnAddCalnder);
        btnAddCalendar.setOnClickListener(view -> {
            Intent intent = new Intent(this, AddCalendarActivity.class);
            startActivity(intent);
        });

    }
}