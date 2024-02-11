package com.example.aria;

import static java.lang.Character.isDigit;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.example.aria.adapters.MeetingTimeAdapter;


import java.util.ArrayList;
import java.util.List;


public class AddAriaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_aria);

        ImageButton btnClose=findViewById(R.id.btnClose);
        btnClose.setOnClickListener(view->{
            Intent intent=new Intent(this,CalendarActivity.class);
            startActivity(intent);
        });

        ImageButton btnDone=findViewById(R.id.btnDone);
        btnDone.setOnClickListener(view->{


            EditText phoneNumber=findViewById(R.id.phoneNumber);
            boolean flag=true;
            if(phoneNumber.getText().length()==10) {
                for(int i=0;i<phoneNumber.getText().length();i++)
                {
                    if((!isDigit(phoneNumber.getText().charAt(i)))||(i==0 && phoneNumber.getText().charAt(i)!='0')||(i==1 && phoneNumber.getText().charAt(i)!='5')) {
                        flag = false;

                    }

                }
                if(flag==true)
                {
                    Intent intent = new Intent(this, CalendarActivity.class);
                    startActivity(intent);
                }
                else {
                    phoneNumber.setText("");
                    phoneNumber.setHint("wrong format! ");

                    Drawable rightDrawable = getResources().getDrawable(R.drawable.wrong); // Replace with your actual drawable resource
                    phoneNumber.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, rightDrawable, null);
                    phoneNumber.setHintTextColor(getResources().getColor(R.color.RedWarning));

                }
            }
            else
            {
                phoneNumber.setText("");
                phoneNumber.setHint("wrong format!");
                phoneNumber.setHintTextColor(getResources().getColor(R.color.RedWarning));
                Drawable rightDrawable = getResources().getDrawable(R.drawable.wrong); // Replace with your actual drawable resource
                phoneNumber.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, rightDrawable, null);

            }
        });


        Spinner spinner = findViewById(R.id.alert);
        String[] items = {"None", "hour before", "day before"};
        ArrayAdapter<String> AlertAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items);
        AlertAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(AlertAdapter);

        // Set an item selection listener for the spinner
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Handle item selection
                String selectedItem = (String) parentView.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Handle no selection
            }
        });
        ListView lstTimeMeeting=findViewById(R.id.lstTimeMeeting);

        //setlayoutmanager

        List<TimeMeeting> meetingTimeList=new ArrayList<>();
        TimeMeeting t1=new TimeMeeting("12/01/2024","13:00");
        TimeMeeting t2=new TimeMeeting("12/01/2024","16:00");
        TimeMeeting t3=new TimeMeeting("13/01/2024","17:00");
        TimeMeeting t4=new TimeMeeting("22/01/2024","19:00");
        meetingTimeList.add(t1);
        meetingTimeList.add(t2);
        meetingTimeList.add(t3);
        meetingTimeList.add(t4);
        MeetingTimeAdapter adapter=new MeetingTimeAdapter(meetingTimeList);

        lstTimeMeeting.setAdapter(adapter);

        ImageButton addDate = findViewById(R.id.addDate);
        TimePicker time = findViewById(R.id.hourMin);
        DatePicker date = findViewById(R.id.DatePicker);
        time.setIs24HourView(true);
        TextView chooseDate = findViewById(R.id.chooseDate);
        LinearLayout timePicker = findViewById(R.id.timePicker);
        chooseDate.setOnClickListener(v -> {
            timePicker.setVisibility(View.VISIBLE);
        });

        time.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                chooseDate.setText(hourOfDay+":"+minute);
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            date.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
                @Override
                public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    int month = monthOfYear+1;
                    chooseDate.setText(dayOfMonth+"/"+month+"/"+year);
                }
            });
        }
        addDate.setOnClickListener(v -> {
            timePicker.setVisibility(View.GONE);
        });

    }
}

