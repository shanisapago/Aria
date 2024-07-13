package com.example.aria;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.text.Layout;
import android.util.Log;
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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.aria.RetroFitClasses.EventsAPI;

import java.util.Calendar;

public class EditEvent extends AppCompatActivity {
    private static final int PERMISSIONS_REQUEST_CODE_CALENDAR = 100;

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
            String googleId = eventsAPI.idGoogle(Integer.parseInt(id), token);
            if (!googleId.equals("-1")){
                long eventId = Integer.parseInt(googleId);
                ContentResolver contentResolver = getContentResolver();
                deleteEvent(contentResolver,eventId);
                eventsAPI.deleteGoogle(Integer.parseInt(id),token);
            }
            Toast.makeText(this, "Event deleted", Toast.LENGTH_SHORT).show();
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

            String newStart = start_textview.getText().toString();
            String newEnd = end_textview.getText().toString();
            int h1 = Integer.parseInt(newStart.substring(0, newStart.indexOf(':')));
            int m1 = Integer.parseInt(newStart.substring(newStart.indexOf(':') + 1, newStart.length()));
            int h2 = Integer.parseInt(newEnd.substring(0, newEnd.indexOf(':')));
            int m2 = Integer.parseInt(newEnd.substring(newEnd.indexOf(':') + 1, newEnd.length()));

            String newDate = date_textview.getText().toString();
            int index_first_slash = newDate.indexOf("/");
            String newDay = newDate.substring(index_first_slash - 2, index_first_slash);
            String newMonth = newDate.substring(index_first_slash + 1, index_first_slash + 3);
            String newYear = newDate.substring(index_first_slash + 4, newDate.length());


            String googleId = eventsAPI.idGoogle(Integer.parseInt(id), token);
            System.out.println("googleId");
            System.out.println(googleId);
            if (!googleId.equals("-1")){
                System.out.println("in different");
                long eventId = Integer.parseInt(googleId);
                updateEventTime(eventId, title_edittext.getText().toString(), description_edittext.getText().toString(), Integer.parseInt(newMonth) - 1, Integer.parseInt(newYear), Integer.parseInt(newDay), h1, m1, h2, m2);
            }

            Toast.makeText(this, "Event updated", Toast.LENGTH_SHORT).show();
            //בדיקה אם הid נמצא בלוח שנה של הפלאפון
            long eventId = 0;
//            System.out.println("in update");
//            System.out.println(title_edittext.getText().toString());
//            System.out.println(description_edittext.getText().toString());
//            System.out.println(date_textview.getText().toString());
//            System.out.println(Integer.parseInt(newYear));
//            System.out.println(Integer.parseInt(newMonth)-1);
//            System.out.println(Integer.parseInt(newDay));
//            System.out.println(h1);
//            System.out.println(m1);
//            System.out.println(h2);
//            System.out.println(m2);
            //updateEventTime(eventId, title_edittext.getText().toString(), description_edittext.getText().toString(), Integer.parseInt(newMonth) - 1, Integer.parseInt(newYear), Integer.parseInt(newDay), h1, m1, h2, m2);

        });
    }
    private void updateEventTime(long id, String title, String des, int monthInt, int yearInt, int dayInt, int h1, int m1, int h2, int m2) {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_CALENDAR) == PackageManager.PERMISSION_GRANTED) {
            // Set the new start and end times for the event
            System.out.println("in update");
            Calendar startCal = Calendar.getInstance();
            startCal.set(yearInt, monthInt, dayInt, h1, m1); // Year, month, day, hour, minute
            long newStartTime = startCal.getTimeInMillis();

            Calendar endCal = Calendar.getInstance();
            endCal.set(yearInt, monthInt, dayInt, h2, m2); // Year, month, day, hour, minute
            long newEndTime = endCal.getTimeInMillis();

            ContentResolver cr = getContentResolver();
            ContentValues values = new ContentValues();
            values.put(CalendarContract.Events.DTSTART, newStartTime);
            values.put(CalendarContract.Events.DTEND, newEndTime);
            values.put(CalendarContract.Events.TITLE, title);
            values.put(CalendarContract.Events.DESCRIPTION, des);

            Uri updateUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, id);

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR) == PackageManager.PERMISSION_GRANTED) {
                //Log.d("CalendarSync", "Update URI: " + updateUri.toString());
                System.out.println("CalendatSync Update URI:" + updateUri.toString());
                try {
                    int rows = cr.update(updateUri, values, null, null);
                    //Log.d("CalendarSync", "Rows updated: " + rows);
                    /*if (rows > 0) {
                        Toast.makeText(this, "Event time updated", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Failed to update event", Toast.LENGTH_SHORT).show();
                    }*/
                } catch (Exception e) {
                    //Log.e("CalendarSync", "Error updating event", e);
                    Toast.makeText(this, "Error updating event: " + e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            // Request permissions if not granted
            System.out.println("no permission");
            requestCalendarPermissions(id, title, des, monthInt, yearInt, dayInt, h1, m1, h2, m2);
        }
    }
    private void requestCalendarPermissions(long id, String title, String des, int monthInt, int yearInt, int dayInt, int h1, int m1, int h2, int m2) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR},
                    PERMISSIONS_REQUEST_CODE_CALENDAR);
        }
        else{
            System.out.println("in");
            //addEventToCalendar(id, title, des, start, end, monthInt, yearInt, dayInt, h1, m1, h2, m2, date);
            //ContentResolver contentResolver = getContentResolver();
            //deleteEvent(contentResolver,8);
            //*********************************************************88
            updateEventTime(id, title, des, monthInt, yearInt, dayInt, h1, m1, h2, m2);
        }
    }
    public void deleteEvent(ContentResolver contentResolver, long eventID) {
        Uri deleteUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, eventID);
        int rows = contentResolver.delete(deleteUri, null, null);
        if (rows > 0) {
            //Toast.makeText(this, "Event deleted", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error deleting event", Toast.LENGTH_SHORT).show();
        }
    }

}
