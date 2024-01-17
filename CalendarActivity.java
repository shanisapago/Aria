package com.example.aria;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.Date;

public class CalendarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar);
        CalendarView calendarView = findViewById(R.id.calendarView);
        Calendar calendar = Calendar.getInstance();
        Date today = calendar.getTime();
        calendarView.setDate(today.getTime());

        ImageButton btnExit=findViewById(R.id.btnExit);
        btnExit.setOnClickListener(view->{
            Intent intent=new Intent(this,MainActivity.class);
            startActivity(intent);
        });

        ImageButton btnAria=findViewById(R.id.btnAria);
        btnAria.setOnClickListener(view->{
            Intent intent=new Intent(this,AddAriaActivity.class);
            startActivity(intent);
        });

    }
}