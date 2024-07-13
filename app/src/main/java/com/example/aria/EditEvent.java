package com.example.aria;
import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintSet;

import com.example.aria.RetroFitClasses.EventsAPI;

public class EditEvent extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_event2);

        String token = getIntent().getExtras().getString("token");
        String id=getIntent().getExtras().getString("id");
        String date_now=getIntent().getExtras().getString("date");
        String title=getIntent().getExtras().getString("title");
        String start=getIntent().getExtras().getString("start");
        String end=getIntent().getExtras().getString("end");
        String alert=getIntent().getExtras().getString("alert");
        String description=getIntent().getExtras().getString("description");
        EditText title_edittext=findViewById(R.id.title);
        TextView start_textview=findViewById(R.id.startsTextView);
        TextView end_textview=findViewById(R.id.endsTextView);
        EditText description_edittext=findViewById(R.id.des);
        TextView date_textview = findViewById(R.id.date);
        title_edittext.setText(title);
        start_textview.setText(start);
        end_textview.setText(end);
        description_edittext.setText(description);
        date_textview.setText(date_now);

        String day=date_now.substring(0,2);
        String month=date_now.substring(3,5);
        String year=date_now.substring(6);
        System.out.println("day");
        System.out.println(date_now.substring(0,2));
        System.out.println("month");
        System.out.println(date_now.substring(3,5));
        System.out.println("year");
        System.out.println(date_now.substring(6));

        Spinner spinner = findViewById(R.id.alert);
        String[] items = {"None", "hour before", "day before"};
        ArrayAdapter<String> AlertAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items);
        AlertAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(AlertAdapter);
        if(alert.equals("hour before")) {
            spinner.setSelection(1);
        }
        else if(alert.equals("day before")) {
            spinner.setSelection(2);
        }

        date_textview.setOnClickListener(v -> {
            DatePicker date = findViewById(R.id.DatePicker);
            LinearLayout date_layout = findViewById(R.id.date_layout);
            if(date.getVisibility()==View.GONE){
                date.setVisibility(View.VISIBLE);
                date_layout.setVisibility(View.VISIBLE);
            }
            else if(date.getVisibility()==View.VISIBLE){
                date.setVisibility(View.GONE);
                date_layout.setVisibility(View.GONE);
            }
        });


        DatePicker date = findViewById(R.id.DatePicker);
        // TextView dateText=findViewById(R.id.dateTextView);
        // dateText.setText(date_now);
        // LinearLayout chooseDate = findViewById(R.id.chooseDate);
        // LinearLayout timePicker = findViewById(R.id.timePicker);

        //  chooseDate.setOnClickListener(v -> {
        //      timePicker.setVisibility(View.VISIBLE);
        //  });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            date.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
                @Override
                public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    String newDay = String.valueOf(dayOfMonth);
                    int month=monthOfYear+1;
                    String newMonth = String.valueOf(month);
                    String date;
                    if(dayOfMonth/10==0)
                        newDay = "0".concat(String.valueOf(dayOfMonth));
                    if(month/10==0)
                        newMonth = "0".concat(String.valueOf(month));
                    date = (((newDay.concat("/")).concat(newMonth)).concat("/")).concat(String.valueOf(year));

                    date_textview.setText(date);
                }
            });
        }

        start_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialogStarts=new TimePickerDialog(EditEvent.this, android.R.style.Theme_Holo_Light_Dialog, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        if(minute<10&&hourOfDay<10)
                            start_textview.setText("0"+hourOfDay+":"+"0"+minute);
                        else if(minute<10)
                            start_textview.setText(hourOfDay+":"+"0"+minute);
                        else if(hourOfDay<10)
                            start_textview.setText("0"+hourOfDay+":"+minute);
                        else
                            start_textview.setText(hourOfDay+":"+minute);


                    }
                },23,59,true);
                timePickerDialogStarts.show();
            }
        });

        end_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialogStarts=new TimePickerDialog(EditEvent.this, android.R.style.Theme_Holo_Light_Dialog, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        if(minute<10&&hourOfDay<10)
                            end_textview.setText("0"+hourOfDay+":"+"0"+minute);
                        else if(minute<10)
                            end_textview.setText(hourOfDay+":"+"0"+minute);
                        else if(hourOfDay<10)
                            end_textview.setText("0"+hourOfDay+":"+minute);
                        else
                            end_textview.setText(hourOfDay+":"+minute);


                    }
                },23,59,true);
                timePickerDialogStarts.show();
            }
        });

        ImageButton btnDelete=findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(view->{
            EventsAPI eventsAPI=new EventsAPI();
            eventsAPI.deleteEventById(Integer.parseInt(id),token);
            Intent i=new Intent(this,Day.class);
            i.putExtra("token",token);
            i.putExtra("day",day);
            i.putExtra("month",month);
            i.putExtra("year",year);
            i.putExtra("date",date_now);
            startActivity(i);
        });

        Button btnDone=findViewById(R.id.btnDone);
        btnDone.setOnClickListener(view->{
            EventsAPI eventsAPI=new EventsAPI();
            eventsAPI.updateAll(Integer.parseInt(id),token,title_edittext.getText().toString(),start_textview.getText().toString(),end_textview.getText().toString(),date_textview.getText().toString(),(String) spinner.getSelectedItem(),description_edittext.getText().toString());
            Intent i=new Intent(this,Day.class);
            i.putExtra("token",token);
            i.putExtra("day",day);
            i.putExtra("month",month);
            i.putExtra("year",year);
            i.putExtra("date",date_now);
            startActivity(i);
        });
    }
}