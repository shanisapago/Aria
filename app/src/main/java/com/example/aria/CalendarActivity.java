package com.example.aria;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.aria.RetroFitClasses.EventsAPI;
import com.example.aria.RetroFitClasses.FirebaseAPI;
import com.example.aria.RetroFitClasses.MembersNotificationsMsg;
import com.example.aria.RetroFitClasses.NewEvent2;

import java.lang.reflect.Field;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class CalendarActivity extends AppCompatActivity {

    private static final int PERMISSIONS_REQUEST_CODE_CALENDAR = 100;
    private String username,title, des, token, start, end, a, date="";
    @SuppressLint({"ResourceAsColor", "ResourceType"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar2);
        CalendarView calendarView = findViewById(R.id.calendarView);



        Calendar calendar = Calendar.getInstance();
        Date today = calendar.getTime();
        calendarView.setDate(today.getTime());
        token = getIntent().getExtras().getString("token");
        username=getIntent().getExtras().getString("username");
        TextView txt=findViewById(R.id.text);
        EventsAPI eventsAPI=new EventsAPI();
        List<MembersNotificationsMsg> lstMembersNotifications=eventsAPI.getMembersNotificationMsg(token);
        if (lstMembersNotifications != null)
        {
            System.out.println("shaniiiiiiiiiiiiiiiiiiiiiiiffffff");
            System.out.println(lstMembersNotifications);
            for(int i=0; i<lstMembersNotifications.size(); i++){
                showCustomPermissionDialog(lstMembersNotifications.get(i).getMessage(), lstMembersNotifications.get(i).getId(), lstMembersNotifications.get(i).getTokenSender(), lstMembersNotifications.get(i).getTitle(), lstMembersNotifications.get(i).getName());
            }
        }
        System.out.println("shaniiiiiiiiiiiiiiiiiiiiiii");
        System.out.println(lstMembersNotifications);

        ImageButton btnAriaList=findViewById(R.id.arialstbtn);
        btnAriaList.setOnClickListener(view->{

            Intent intent=new Intent(this, AriaListEventsActivity.class);
            intent.putExtra("username",username);
            intent.putExtra("token",token);
            startActivity(intent);
        });


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
                intent.putExtra("year", String.valueOf(year));
                intent.putExtra("month", String.valueOf(month));
                intent.putExtra("day", String.valueOf(dayOfMonth));
                intent.putExtra("token", token);
                intent.putExtra("date", date);
                intent.putExtra("username",username);
                startActivity(intent);
            }
        });
    }
    private void showCustomPermissionDialog(String msg, String id, String senderToken, String title, String name) {
            System.out.println("in mini message");
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            LayoutInflater inflater = getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.mini_message, null);
            builder.setView(dialogView);

            final AlertDialog dialog = builder.create();

            TextView tvTitle = dialogView.findViewById(R.id.tvTitle);
            TextView tvMessage = dialogView.findViewById(R.id.tvMessage);
            tvTitle.setText("you have new invitation");
            tvMessage.setText(msg);
            Button btnPositive = dialogView.findViewById(R.id.btnPositive);
            Button btnNegative = dialogView.findViewById(R.id.btnNegative);
            FirebaseAPI firebaseAPI = new FirebaseAPI();
            btnPositive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Handle Allow button click
                    dialog.dismiss();
                    EventsAPI eventsAPI = new EventsAPI();
                    String [] user = new String [1];
                    user[0] = username;
                    System.out.println(username);
                    Alert [] alert = new Alert[1];
                    alert[0] = new Alert(username, "None");
                    String message = name + " has confirmed your invitation to "+ title;
                    firebaseAPI.sendMessage("invitation confirmed!", message, senderToken);
                    System.out.println("after request");
                    NewEvent2 j=eventsAPI.joinEvent(Integer.parseInt(id), user, alert);
                    int index_first_slash = j.getDate().indexOf("/");
                    String day = j.getDate().substring(index_first_slash - 2, index_first_slash);
                    String month = j.getDate().substring(index_first_slash + 1, index_first_slash + 3);
                    String year = j.getDate().substring(index_first_slash + 4, j.getDate().length());
                    int dayInt = Integer.valueOf(day);
                    int monthInt = Integer.valueOf(month);
                    int yearInt = Integer.valueOf(year);

                    int h1 = Integer.parseInt(j.getStart().substring(0, j.getStart().indexOf(':')));
                    int m1 = Integer.parseInt(j.getStart().substring(j.getStart().indexOf(':') + 1, j.getStart().length()));
                    int h2 = Integer.parseInt(j.getEnd().substring(0, j.getEnd().indexOf(':')));
                    int m2 = Integer.parseInt(j.getEnd().substring(j.getEnd().indexOf(':') + 1, j.getEnd().length()));

                    //monthInt =- 1;
                    monthInt = monthInt -1;

                    showCustomPermissionDialogCalendar(j.getId(),j.getTitle(),j.getDescription(),j.getStart(),j.getEnd(),monthInt,yearInt,dayInt,h1,m1,h2,m2,token);
                    // Perform action to grant permission or proceed
                    eventsAPI.deleteInvitation(Integer.parseInt(id), username);
                }
            });

            btnNegative.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Handle Deny button click
                    String message = name + " has rejected your invitation to "+ title;
                    firebaseAPI.sendMessage("invitation rejected!", message, senderToken);
                    dialog.dismiss();
                    //התראה למי שהזמין
                    // Perform action to deny permission or exit
                    EventsAPI eventsAPI = new EventsAPI();
                    eventsAPI.deleteInvitation(Integer.parseInt(id), username);
                }
            });

            dialog.show();
        }

        private void showCustomPermissionDialogCalendar(String id, String title, String des, String start, String end, int monthInt, int yearInt, int dayInt, int h1, int m1, int h2, int m2, String token) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            LayoutInflater inflater = getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.mini_message, null);
            builder.setView(dialogView);

            final AlertDialog dialog = builder.create();

            TextView tvTitle = dialogView.findViewById(R.id.tvTitle);
            TextView tvMessage = dialogView.findViewById(R.id.tvMessage);
            Button btnPositive = dialogView.findViewById(R.id.btnPositive);
            Button btnNegative = dialogView.findViewById(R.id.btnNegative);
            btnPositive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Handle Allow button click
                    dialog.dismiss();
                    //EventsAPI eventsAPI = new EventsAPI();
                    //String [] user = new String [1];
                    //user[0] = username;
                    //Alert [] alert = new Alert[1];
                    //alert[0] = new Alert(username, "None");
                    //eventsAPI.joinEvent(Integer.parseInt(id), user, alert);
                    //showCustomPermissionDialogCalendar(pu.getId(), title, description, start, end, monthInt, yearInt, dayInt, h1, m1, h2, m2, token);
                    addEventToCalendar(id, title, des, start, end, monthInt, yearInt, dayInt, h1, m1, h2, m2);
                    //startActivity(intent);
                    // Perform action to grant permission or proceed
                }
            });

            btnNegative.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Handle Deny button click
                    dialog.dismiss();
                    //startActivity(intent);
                    // Perform action to deny permission or exit
                }
            });

            dialog.show();
        }
        private void addEventToCalendar(String id, String title, String des, String start, String end, int monthInt, int yearInt, int dayInt, int h1, int m1, int h2, int m2) {
            // Check again if permissions are granted
            System.out.println("add event");

            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_CALENDAR) == PackageManager.PERMISSION_GRANTED) {
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
                eventsAPI.addGoogleEvent(Integer.parseInt(id),(int)googleID,token);
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
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR},
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
