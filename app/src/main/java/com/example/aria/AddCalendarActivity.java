package com.example.aria;


import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import com.example.aria.RetroFitClasses.EventsAPI;

public class AddCalendarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_calendar);

        String token = getIntent().getExtras().getString("token");
        String date = getIntent().getExtras().getString("date");
        //String user = getIntent().getExtras().getString("username");
        ImageButton btnClose = findViewById(R.id.btnClose);
        btnClose.setOnClickListener(view -> {
            Intent intent = new Intent(this, CalendarActivity.class);

            startActivity(intent);
        });
        Spinner spinner = findViewById(R.id.alert);
        ImageButton btnDone = findViewById(R.id.btnDone);
        btnDone.setOnClickListener(view -> {
            Intent intent = new Intent(this, CalendarActivity.class);
            EditText editTitle = findViewById(R.id.title);
            TextView startView = findViewById(R.id.startsTextView);
            TextView endView = findViewById(R.id.endsTextView);
            EditText editDes = findViewById(R.id.des);
            String a = (String) spinner.getSelectedItem();
            System.out.println("alert");
            System.out.println(a);
            String title = editTitle.getText().toString();
            String des = editDes.getText().toString();
            String start = startView.getText().toString();
            String end = endView.getText().toString();
            EventsAPI eventsAPI = new EventsAPI();
            eventsAPI.addEvent(token, title, des, start, end, a, date);
            intent.putExtra("token", token);
            startActivity(intent);
        });

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
                //Toast.makeText(AddCalendarActivity.this, "Selected: " + selectedItem, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Handle no selection
            }
        });

        TextView startsTextView=findViewById(R.id.startsTextView);
        startsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialogStarts=new TimePickerDialog(AddCalendarActivity.this, android.R.style.Theme_Holo_Light_Dialog, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        if(minute<10&&hourOfDay<10)
                            startsTextView.setText("0"+hourOfDay+":"+"0"+minute);
                        else if(minute<10)
                            startsTextView.setText(hourOfDay+":"+"0"+minute);
                        else if(hourOfDay<10)
                            startsTextView.setText("0"+hourOfDay+":"+minute);
                        else
                            startsTextView.setText(hourOfDay+":"+minute);


                    }
                },23,59,true);
                timePickerDialogStarts.show();
            }
        });

        TextView endsTextView=findViewById(R.id.endsTextView);
        endsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialogStarts=new TimePickerDialog(AddCalendarActivity.this, android.R.style.Theme_Holo_Light_Dialog, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        if(minute<10&&hourOfDay<10)
                            endsTextView.setText("0"+hourOfDay+":"+"0"+minute);
                        else if(minute<10)
                            endsTextView.setText(hourOfDay+":"+"0"+minute);
                        else if(hourOfDay<10)
                            endsTextView.setText("0"+hourOfDay+":"+minute);
                        else
                            endsTextView.setText(hourOfDay+":"+minute);


                    }
                },23,59,true);
                timePickerDialogStarts.show();
            }
        });
    }
}
