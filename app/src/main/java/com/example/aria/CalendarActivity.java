package com.example.aria;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.Date;

public class CalendarActivity extends AppCompatActivity {

    @SuppressLint({"ResourceAsColor", "ResourceType"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar);

        String token = getIntent().getExtras().getString("token");
        CalendarView calendarView = findViewById(R.id.calendarView);
        Calendar calendar = Calendar.getInstance();
        Date today = calendar.getTime();
        calendarView.setDate(today.getTime());

        ImageButton btnExit=findViewById(R.id.btnExit);
        btnExit.setOnClickListener(view->{
            Intent intent=new Intent(this,Login.class);

            startActivity(intent);
        });

        ImageButton btnAria=findViewById(R.id.btnAria);
        btnAria.setOnClickListener(view->{
            Intent intent=new Intent(this,AddAriaActivity.class);
            intent.putExtra("token",token);
            startActivity(intent);
        });

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

                Intent intent=new Intent(CalendarActivity.this, Day.class);
                intent.putExtra("token",token);
                startActivity(intent);
            }
        });


    }
}