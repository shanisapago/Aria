package com.example.aria;
import android.Manifest;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.aria.RetroFitClasses.EventsAPI;
import com.example.aria.RetroFitClasses.PhoneUsers;
import com.example.aria.RetroFitClasses.PhoneUsers2;
import com.example.aria.adapters.MembersAdapter;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

public class AddCalendarActivity extends AppCompatActivity {
    private static final int PERMISSIONS_REQUEST_CODE_CALENDAR = 100;
    private EventsAPI eventsAPI = new EventsAPI();
    private String username,title, des, token, start, end, a, date="";

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_calendar2);
        List<MemberListItem> membersList = new ArrayList<>();
        username=getIntent().getExtras().getString("username");

//        ListView lstMembers=findViewById(R.id.lstMembers);
//        MembersAdapter adapter=new MembersAdapter(membersList);
//        lstMembers.setAdapter(adapter);
//
//        ImageButton addMember = findViewById(R.id.addMember);
//        addMember.setOnClickListener(v -> {
//            EditText phone2=findViewById(R.id.phoneNumber);
//            EditText name2=findViewById(R.id.name);
//            MemberListItem m1=new MemberListItem(phone2.getText().toString(),name2.getText().toString());
//
//            if(!membersList.contains(m1)) {
//                membersList.add(m1);
//                adapter.notifyDataSetChanged();
//            }
//        });
        createNotificationChannel();
        token = getIntent().getExtras().getString("token");

        date = getIntent().getExtras().getString("date");
        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(view -> {
            onBackPressed();
        });

        ImageButton btnhome=findViewById(R.id.btnhome);
        btnhome.setOnClickListener(view->{
            Intent intent=new Intent(this,CalendarActivity.class);
            intent.putExtra("username",username);
            intent.putExtra("token",token);
            startActivity(intent);
        });

        ImageButton btnAriaList=findViewById(R.id.arialstbtn);
        btnAriaList.setOnClickListener(view->{

            Intent intent=new Intent(this, AriaListEventsActivity.class);
            intent.putExtra("username",username);
            intent.putExtra("token",token);
            startActivity(intent);
        });

        ImageButton btnAria = findViewById(R.id.btnAria);
        btnAria.setOnClickListener(view -> {
            Intent intent = new Intent(this, AddAriaActivity.class);
            intent.putExtra("token",token);
            intent.putExtra("username",username);
            startActivity(intent);
        });
        Spinner spinner = findViewById(R.id.alert);
        Button btnMembers = findViewById(R.id.btnAddMembers);
        btnMembers.setOnClickListener(view -> {
            Intent intent = new Intent(this, MembersActivity.class);
            EditText editTitle = findViewById(R.id.title);
            TextView startView = findViewById(R.id.startsTextView);
            TextView endView = findViewById(R.id.endsTextView);
            String startTime = startView.getText().toString();
            String endTime = endView.getText().toString();
            int h1 = Integer.parseInt(startTime.substring(0, startTime.indexOf(':')));
            int m1 = Integer.parseInt(startTime.substring(startTime.indexOf(':') + 1, startTime.length()));
            int h2 = Integer.parseInt(endTime.substring(0, endTime.indexOf(':')));
            int m2 = Integer.parseInt(endTime.substring(endTime.indexOf(':') + 1, endTime.length()));
            Boolean titleFlag=false;
            Boolean timeFlag=false;
            if((h1 < h2 || (h1 == h2) && m1 < m2))
            {
                timeFlag=true;
            }
            if(editTitle.getText().toString().length() != 0){
                System.out.println("title is not empty");
                titleFlag=true;
            }
            if (titleFlag&&timeFlag) {
                EditText editDes = findViewById(R.id.des);
                a = (String) spinner.getSelectedItem();
                title = editTitle.getText().toString();
                des = editDes.getText().toString();
                start = startView.getText().toString();
                end = endView.getText().toString();

                int index_first_slash = date.indexOf("/");
                String day = date.substring(index_first_slash - 2, index_first_slash);
                String month = date.substring(index_first_slash + 1, index_first_slash + 3);
                String year = date.substring(index_first_slash + 4, date.length());
                int dayInt = Integer.valueOf(day);
                int monthInt = Integer.valueOf(month);
                int yearInt = Integer.valueOf(year);


                if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{
                            Manifest.permission.POST_NOTIFICATIONS
                    }, 1001);
                } else {


                    int index_time = start.indexOf(":");
                    String hour = start.substring(index_time - 2, index_time);
                    String minute = start.substring(index_time + 1, start.length());

                    int hourInt = Integer.valueOf(hour);
                    int minuteInt = Integer.valueOf(minute);

                    monthInt = monthInt - 1;
                    String description = "";

                    if (a.equals("hour before")) {
                        if (hourInt == 0) {
                            hourInt = 23;
                        } else {
                            hourInt = hourInt - 1;
                        }
                        description = "today at " + start;
                    }
                    int newDayInt = dayInt;
                    if (a.equals("day before")) {
                        if (newDayInt == 1) {
                            newDayInt = 23;
                        } else {
                            newDayInt = newDayInt - 1;
                        }
                        description = "tomorrow at " + start;
                    }

                    Intent intent1 = new Intent(AddCalendarActivity.this, NotificationBroadcast.class);
                    intent1.setAction("com.example.aria.ACTION_SHOW_NOTIFICATION");
                    intent1.putExtra("title", title); // Add title string
                    intent1.putExtra("description", description);
                    int requestCodePending = generateUniqueRequestCode();
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(AddCalendarActivity.this, requestCodePending, intent1, PendingIntent.FLAG_IMMUTABLE);
                    AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                    if (!a.equals("None")) {
                        System.out.println("alerttt");
                        System.out.println(hourInt);
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.YEAR, yearInt); // Year
                        calendar.set(Calendar.MONTH, monthInt); // Month (Note: January is 0)
                        calendar.set(Calendar.DAY_OF_MONTH, newDayInt); // Day of the month
                        calendar.set(Calendar.HOUR_OF_DAY, hourInt); // Hour (in 24-hour format)
                        calendar.set(Calendar.MINUTE, minuteInt); // Minute
                        calendar.set(Calendar.SECOND, 0); // Second
                        long alarmTimeMillis = calendar.getTimeInMillis();

                        alarmManager.set(AlarmManager.RTC_WAKEUP, alarmTimeMillis, pendingIntent);
                    }



                }
                //Intent intent = new Intent(this, CalendarActivity.class);
                List <String> list = new ArrayList<String>();

                //PhoneUsers2 pu = eventsAPI.checkPhones(token, title, des, start, end, a, date, list);
                //System.out.println("id event");
                //System.out.println(id);
                //intent.putExtra("token", token);
                //intent.putExtra("username", username);
                //startActivity(intent);
                //String id=pu.getId();


                intent.putExtra("token",token);
                intent.putExtra("username",username);
                intent.putExtra("title",title);
                intent.putExtra("description",des);
                intent.putExtra("start",start);
                intent.putExtra("end",end);
                intent.putExtra("date",date);
                intent.putExtra("alert",a);
                intent.putExtra("monthInt",monthInt);
                intent.putExtra("dayInt",dayInt);
                intent.putExtra("yearInt",yearInt);
                intent.putExtra("h1",h1);
                intent.putExtra("m1",m1);
                intent.putExtra("h2",h2);
                intent.putExtra("m2",m2);

                startActivity(intent);

                // showCustomPermissionDialog(id, title, des, start, end, monthInt, yearInt, dayInt, h1, m1, h2, m2, token);
            }
            else {

                if (timeFlag == false) {
                    System.out.println("time not good");
                    ImageView errorIcon = findViewById(R.id.wrongTime);
                    LinearLayout errorText = findViewById(R.id.wrongTime2);
                    errorIcon.setVisibility(View.VISIBLE);
                    errorText.setVisibility(View.VISIBLE);
                } else {
                    System.out.println("time good");
                    ImageView errorIcon = findViewById(R.id.wrongTime);
                    LinearLayout errorText = findViewById(R.id.wrongTime2);
                    errorIcon.setVisibility(View.INVISIBLE);
                    errorText.setVisibility(View.INVISIBLE);
                }
                if (titleFlag==false) {
                    System.out.println("title not good");
                    ImageView errorIcon = findViewById(R.id.wrongTitle);
                    LinearLayout errorText = findViewById(R.id.wrongTitle2);
                    errorIcon.setVisibility(View.VISIBLE);
                    errorText.setVisibility(View.VISIBLE);
                } else {
                    System.out.println("title good");
                    ImageView errorIcon = findViewById(R.id.wrongTitle);
                    LinearLayout errorText = findViewById(R.id.wrongTitle2);
                    errorIcon.setVisibility(View.INVISIBLE);
                    errorText.setVisibility(View.INVISIBLE);

                }

            }

        });



        Button btnDone = findViewById(R.id.btnDone);
        btnDone.setOnClickListener(view -> {
            System.out.println("done btn");
            EditText editTitle = findViewById(R.id.title);
            TextView startView = findViewById(R.id.startsTextView);
            TextView endView = findViewById(R.id.endsTextView);
            String startTime = startView.getText().toString();
            String endTime = endView.getText().toString();
            int h1 = Integer.parseInt(startTime.substring(0, startTime.indexOf(':')));
            int m1 = Integer.parseInt(startTime.substring(startTime.indexOf(':') + 1, startTime.length()));
            int h2 = Integer.parseInt(endTime.substring(0, endTime.indexOf(':')));
            int m2 = Integer.parseInt(endTime.substring(endTime.indexOf(':') + 1, endTime.length()));
            Boolean titleFlag=false;
            Boolean timeFlag=false;
            if((h1 < h2 || (h1 == h2) && m1 < m2))
            {
                timeFlag=true;
            }
            if(editTitle.getText().toString().length() != 0){
                System.out.println("title is not empty");
                titleFlag=true;
            }
            if (titleFlag&&timeFlag) {
                EditText editDes = findViewById(R.id.des);
                a = (String) spinner.getSelectedItem();
                title = editTitle.getText().toString();
                des = editDes.getText().toString();
                start = startView.getText().toString();
                end = endView.getText().toString();

                int index_first_slash = date.indexOf("/");
                String day = date.substring(index_first_slash - 2, index_first_slash);
                String month = date.substring(index_first_slash + 1, index_first_slash + 3);
                String year = date.substring(index_first_slash + 4, date.length());
                int dayInt = Integer.valueOf(day);
                int monthInt = Integer.valueOf(month);
                int yearInt = Integer.valueOf(year);


                if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{
                            Manifest.permission.POST_NOTIFICATIONS
                    }, 1001);
                } else {


                    int index_time = start.indexOf(":");
                    String hour = start.substring(index_time - 2, index_time);
                    String minute = start.substring(index_time + 1, start.length());

                    int hourInt = Integer.valueOf(hour);
                    int minuteInt = Integer.valueOf(minute);

                    monthInt = monthInt - 1;
                    String description = "";

                    if (a.equals("hour before")) {
                        if (hourInt == 0) {
                            hourInt = 23;
                        } else {
                            hourInt = hourInt - 1;
                        }
                        description = "today at " + start;
                    }
                    int newDayInt = dayInt;
                    if (a.equals("day before")) {
                        if (newDayInt == 1) {
                            newDayInt = 23;
                        } else {
                            newDayInt = newDayInt - 1;
                        }
                        description = "tomorrow at " + start;
                    }

                    Intent intent1 = new Intent(AddCalendarActivity.this, NotificationBroadcast.class);
                    intent1.setAction("com.example.aria.ACTION_SHOW_NOTIFICATION");
                    intent1.putExtra("title", title); // Add title string
                    intent1.putExtra("description", description);
                    int requestCodePending = generateUniqueRequestCode();
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(AddCalendarActivity.this, requestCodePending, intent1, PendingIntent.FLAG_IMMUTABLE);
                    AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                    if (!a.equals("None")) {
                        System.out.println("alerttt");
                        System.out.println(hourInt);
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.YEAR, yearInt); // Year
                        calendar.set(Calendar.MONTH, monthInt); // Month (Note: January is 0)
                        calendar.set(Calendar.DAY_OF_MONTH, newDayInt); // Day of the month
                        calendar.set(Calendar.HOUR_OF_DAY, hourInt); // Hour (in 24-hour format)
                        calendar.set(Calendar.MINUTE, minuteInt); // Minute
                        calendar.set(Calendar.SECOND, 0); // Second
                        long alarmTimeMillis = calendar.getTimeInMillis();

                        alarmManager.set(AlarmManager.RTC_WAKEUP, alarmTimeMillis, pendingIntent);
                    }



                }
                //Intent intent = new Intent(this, CalendarActivity.class);
                List <MemberListItem> list2 = null;
                String empty = "";
                List<PhoneUsers2> pu = eventsAPI.checkPhones(token, title, des, start, end, a, date, list2, empty);
                System.out.println("id event");
                //System.out.println(id);
                //intent.putExtra("token", token);
                //intent.putExtra("username", username);
                //startActivity(intent);
                String id=pu.get(0).getId();
                showCustomPermissionDialog(id, title, des, start, end, monthInt, yearInt, dayInt, h1, m1, h2, m2, token);
            }
            else {

                if (timeFlag == false) {
                    System.out.println("time not good");
                    ImageView errorIcon = findViewById(R.id.wrongTime);
                    LinearLayout errorText = findViewById(R.id.wrongTime2);
                    errorIcon.setVisibility(View.VISIBLE);
                    errorText.setVisibility(View.VISIBLE);
                } else {
                    System.out.println("time good");
                    ImageView errorIcon = findViewById(R.id.wrongTime);
                    LinearLayout errorText = findViewById(R.id.wrongTime2);
                    errorIcon.setVisibility(View.INVISIBLE);
                    errorText.setVisibility(View.INVISIBLE);
                }
                if (titleFlag==false) {
                    System.out.println("title not good");
                    ImageView errorIcon = findViewById(R.id.wrongTitle);
                    LinearLayout errorText = findViewById(R.id.wrongTitle2);
                    errorIcon.setVisibility(View.VISIBLE);
                    errorText.setVisibility(View.VISIBLE);
                } else {
                    System.out.println("title good");
                    ImageView errorIcon = findViewById(R.id.wrongTitle);
                    LinearLayout errorText = findViewById(R.id.wrongTitle2);
                    errorIcon.setVisibility(View.INVISIBLE);
                    errorText.setVisibility(View.INVISIBLE);

                }

            }
        });

        String[] items = {"None", "hour before", "day before"};
        ArrayAdapter<String> AlertAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items);
        AlertAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(AlertAdapter);

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

    private void createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "LemubitReminderChannel";
            String description = "Channel for Lemubit Reminder";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("notifyLemubit", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private int generateUniqueRequestCode() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        int requestCode = sharedPreferences.getInt("requestCode", 0);
        requestCode++;
        sharedPreferences.edit().putInt("requestCode", requestCode).apply();
        return requestCode;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1001) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                int index_time = start.indexOf(":");
                String hour = start.substring(index_time - 2, index_time);
                String minute = start.substring(index_time + 1);
                int index_first_slash = date.indexOf("/");
                String day = date.substring(index_first_slash - 2, index_first_slash);
                String month = date.substring(index_first_slash + 1, index_first_slash + 3);
                String year = date.substring(index_first_slash + 4);

                int hourInt = Integer.valueOf(hour);
                int minuteInt = Integer.valueOf(minute);
                int dayInt = Integer.valueOf(day);
                int monthInt = Integer.valueOf(month);
                int yearInt = Integer.valueOf(year);

                int newMonthInt = monthInt - 1;
                String description = "";

                if(a.equals("hour before")){
                    if (hourInt == 0){
                        hourInt = 23;
                    }
                    else{
                        hourInt = hourInt - 1;
                    }
                    description = "today at " + start;
                }
                if(a.equals("day before")){
                    if (dayInt == 1){
                        if (newMonthInt == 0){
                            newMonthInt = 11;
                        }
                        else{
                            newMonthInt = newMonthInt - 1;
                        }
                        if (newMonthInt == 0 || newMonthInt == 2 || newMonthInt == 4 || newMonthInt == 6 || newMonthInt == 7 || newMonthInt == 9 || newMonthInt == 11){
                            dayInt = 31;
                        } else if (newMonthInt == 1) {
                            dayInt = 29;
                        }
                        else {
                            dayInt = 30;
                        }
                    }
                    else{
                        dayInt = dayInt - 1;
                    }
                    description = "tomorrow at " + start;
                }
                Intent intent = new Intent(this, CalendarActivity.class);
                Intent intent1 = new Intent(AddCalendarActivity.this, NotificationBroadcast.class);
                intent1.setAction("com.example.aria.ACTION_SHOW_NOTIFICATION");
                intent1.putExtra("title", title);
                intent1.putExtra("description", description);
                int requestCodePending = generateUniqueRequestCode();
                PendingIntent pendingIntent = PendingIntent.getBroadcast(AddCalendarActivity.this, requestCodePending, intent1, PendingIntent.FLAG_IMMUTABLE);
                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

                if(!a.equals("None")){
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.YEAR, yearInt); // Year
                    calendar.set(Calendar.MONTH, newMonthInt); // Month (Note: January is 0)
                    calendar.set(Calendar.DAY_OF_MONTH, dayInt); // Day of the month
                    calendar.set(Calendar.HOUR_OF_DAY, hourInt); // Hour (in 24-hour format)
                    calendar.set(Calendar.MINUTE, minuteInt); // Minute
                    calendar.set(Calendar.SECOND, 0); // Second

                    long alarmTimeMillis = calendar.getTimeInMillis();
                    alarmManager.set(AlarmManager.RTC_WAKEUP, alarmTimeMillis, pendingIntent);
                }
                List <MemberListItem> list2 = null;
                String empty = "";
                eventsAPI.checkPhones(token, title, des, start, end, a, date, list2, empty);
                intent.putExtra("token", token);
                intent.putExtra("username",username);
                startActivity(intent);
            } else {
                // Permission is denied
                // You may inform the user about the consequences of not granting the permission
                // Or you may disable functionality that requires this permission
            }
        }
    }
    private void showCustomPermissionDialog(String id, String title, String des, String start, String end, int monthInt, int yearInt, int dayInt, int h1, int m1, int h2, int m2, String token) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.mini_message, null);
        builder.setView(dialogView);

        final AlertDialog dialog = builder.create();

        TextView tvTitle = dialogView.findViewById(R.id.tvTitle);
        TextView tvMessage = dialogView.findViewById(R.id.tvMessage);
        Button btnPositive = dialogView.findViewById(R.id.btnPositive);
        Button btnNegative = dialogView.findViewById(R.id.btnNegative);
        Intent intent = new Intent(this, CalendarActivity.class);
        intent.putExtra("token", token);
        intent.putExtra("username", username);
        btnPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle Allow button click
                dialog.dismiss();
                addEventToCalendar(id, title, des, start, end, monthInt, yearInt, dayInt, h1, m1, h2, m2);
                startActivity(intent);
                // Perform action to grant permission or proceed
            }
        });

        btnNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle Deny button click
                dialog.dismiss();
                startActivity(intent);
                // Perform action to deny permission or exit
            }
        });

        dialog.show();
    }
    private void addEventToCalendar(String id, String title, String des, String start, String end, int monthInt, int yearInt, int dayInt, int h1, int m1, int h2, int m2) {
        // Check again if permissions are granted
        System.out.println("add event");

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR) == PackageManager.PERMISSION_GRANTED) {
            System.out.println("permission");
            java.util.Calendar startCal = java.util.Calendar.getInstance();
            startCal.set(yearInt, monthInt, dayInt, h1, m1); // Year, month, day, hour, minute
            long startTime = startCal.getTimeInMillis();
            int numH = h2 - h1;
            int numM = m2 - m1;
            int timeInM = (numH * 60) + numM;
            long endTime = startTime + timeInM * 60 * 1000;


            System.out.println(startTime);
            System.out.println(endTime);
            System.out.println(title);
            System.out.println(des);
            ContentResolver cr = getContentResolver();
            ContentValues values = new ContentValues();
            values.put(CalendarContract.Events.DTSTART, startTime);
            values.put(CalendarContract.Events.DTEND, endTime);
            values.put(CalendarContract.Events.TITLE, title);
            values.put(CalendarContract.Events.DESCRIPTION, des);
            values.put(CalendarContract.Events.CALENDAR_ID, getPrimaryCalendarId());
            values.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().getID());

            Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);
            long googleID = Long.parseLong(uri.getLastPathSegment());

            EventsAPI eventsAPI = new EventsAPI();
            //eventsAPI.addGoogleEvent(Integer.parseInt(id),(int)googleID,token);
            //System.out.println("Event added with ID:" + eventID);
            //Log.d("CalendarSync", "Event added with ID: " + eventID);
        } else {
            // Request permissions if not granted
            System.out.println("no permission calendar");
            requestCalendarPermissions(id, title, des, start, end, monthInt, yearInt, dayInt, h1, m1, h2, m2, date);
        }
    }
    private long getPrimaryCalendarId() {
        String[] projection = new String[]{
                CalendarContract.Calendars._ID,
                CalendarContract.Calendars.IS_PRIMARY
        };

        Cursor cursor = getContentResolver().query(
                CalendarContract.Calendars.CONTENT_URI,
                projection,
                CalendarContract.Calendars.IS_PRIMARY + " = 1",
                null,
                null
        );

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                long calendarId = cursor.getLong(0);
                cursor.close();
                return calendarId;
            }
            cursor.close();
        }

        return -1; // Default to invalid ID if primary calendar is not found
    }
    private void requestCalendarPermissions(String id, String title, String des, String start, String end, int monthInt, int yearInt, int dayInt, int h1, int m1, int h2, int m2, String date) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR},
                    PERMISSIONS_REQUEST_CODE_CALENDAR);
        }
        else{
            System.out.println("in");
            addEventToCalendar(id, title, des, start, end, monthInt, yearInt, dayInt, h1, m1, h2, m2);
            //ContentResolver contentResolver = getContentResolver();
            //deleteEvent(contentResolver,8);
            //updateEventTime(8);
        }
    }
}