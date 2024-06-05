package com.example.aria;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.Field;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;

public class CalendarActivity extends AppCompatActivity {

    @SuppressLint({"ResourceAsColor", "ResourceType"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar2);
        CalendarView calendarView = findViewById(R.id.calendarView);

        Calendar calendar = Calendar.getInstance();
        Date today = calendar.getTime();
        calendarView.setDate(today.getTime());
        String token = getIntent().getExtras().getString("token");
        String username=getIntent().getExtras().getString("username");
        TextView txt=findViewById(R.id.text);
        String new_txt="Hello "+username+"\n good ";
        LocalTime localTime = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            localTime = LocalTime.now();
        }
        int hour = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            hour = localTime.getHour();
        }
        System.out.println("hour");
        System.out.println(hour);
        if(hour>=6 && hour<12)
        {
            new_txt=new_txt+"morning";

        } else if (hour>=12 && hour<=18) {
            new_txt=new_txt+"afternoon";

        } else if (hour>18 && hour <=22) {
            new_txt=new_txt+"evening";

        }
        else {
            new_txt=new_txt+"night";
        }
        txt.setText(new_txt);


        ImageButton btnExit=findViewById(R.id.btnExit);
        btnExit.setOnClickListener(view->{
            Intent intent=new Intent(this,Login.class);
            startActivity(intent);
        });
        ImageButton btnAria=findViewById(R.id.ariabtn);
        btnAria.setOnClickListener(view->{
            Intent intent=new Intent(this, AddAriaActivity.class);
            intent.putExtra("username",username);
            startActivity(intent);
        });

        /*
        ImageButton btnAria=findViewById(R.id.btnAria);
        btnAria.setOnClickListener(view->{
            Intent intent=new Intent(this,AddAriaActivity.class);
            intent.putExtra("token",token);
            startActivity(intent);
        });

         */

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

                Intent intent=new Intent(CalendarActivity.this, Day.class);
                String newDay = String.valueOf(dayOfMonth);
                month+=1;
                String newMonth = String.valueOf(month);
                String date;
                if(dayOfMonth/10==0)
                    newDay = "0".concat(String.valueOf(dayOfMonth));
                if(month/10==0)
                    newMonth = "0".concat(String.valueOf(month));
                date = (((newDay.concat("/")).concat(newMonth)).concat("/")).concat(String.valueOf(year));
                intent.putExtra("token", token);
                intent.putExtra("date", date);
                intent.putExtra("username",username);
                startActivity(intent);
            }
        });
    }
}