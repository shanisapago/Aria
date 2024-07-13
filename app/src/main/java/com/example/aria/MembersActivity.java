package com.example.aria;
import static java.lang.Character.isDigit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.CalendarContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.aria.RetroFitClasses.ChatsAPI;

import com.example.aria.RetroFitClasses.EventsAPI;
import com.example.aria.RetroFitClasses.FirebaseAPI;
import com.example.aria.RetroFitClasses.NotUser;
import com.example.aria.RetroFitClasses.PhoneUsers2;
import com.example.aria.adapters.MembersAdapter;
import com.example.aria.adapters.OpenEventsAdapter;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

public class MembersActivity extends AppCompatActivity {
    private static final int PERMISSIONS_REQUEST_CODE_CALENDAR = 100;
    String username, token, date="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.members);


        List<MemberListItem> membersList=new ArrayList<>();
        List<String>names = new ArrayList<String>();
        List<String>phones=new ArrayList<>();
        ListView lstMembers=findViewById(R.id.friendsList);
        NotUser[] notUsers = new NotUser[0];
        MembersAdapter members_adapter=new MembersAdapter(membersList,notUsers);
        lstMembers.setAdapter(members_adapter);
        lstMembers.setAdapter(members_adapter);

        String token = getIntent().getExtras().getString("token");
        System.out.println("token");
        System.out.println(token);
        String username = getIntent().getExtras().getString("username");
        String title = getIntent().getExtras().getString("title");
        String description = getIntent().getExtras().getString("description");
        String start = getIntent().getExtras().getString("start");
        String end = getIntent().getExtras().getString("end");
        String date = getIntent().getExtras().getString("date");
        String alert = getIntent().getExtras().getString("alert");
        int monthInt = getIntent().getExtras().getInt("monthInt");
        int dayInt = getIntent().getExtras().getInt("dayInt");
        int yearInt = getIntent().getExtras().getInt("yearInt");
        int h1 = getIntent().getExtras().getInt("h1");
        int m1 = getIntent().getExtras().getInt("m1");
        int h2 = getIntent().getExtras().getInt("h2");
        int m2 = getIntent().getExtras().getInt("m2");

        EditText phoneNumber = findViewById(R.id.phone);
        EditText nameI = findViewById(R.id.name);
        LinearLayout errorName = findViewById(R.id.wrongName);
        LinearLayout errorPhone = findViewById(R.id.wrongPhone);
        ImageView iconName = findViewById(R.id.wrongNameIcon);
        ImageView iconPhone = findViewById(R.id.wrongPhoneIcon);

        ImageView addBtn=findViewById(R.id.addBtn);
        addBtn.setOnClickListener(view->{
            System.out.println("click");
            boolean flagP=true;
            boolean flagN=true;
            System.out.println(phoneNumber.getText().length());
            System.out.println(phoneNumber.getText().toString());
            if(phoneNumber.getText().length()!=9){
                flagP = false;
                errorPhone.setVisibility(View.VISIBLE);
                iconPhone.setVisibility(View.VISIBLE);
                errorPhone.setVisibility(View.VISIBLE);
            }
            else {
                for(int i=0;i<phoneNumber.getText().length();i++)
                {
                    if((!isDigit(phoneNumber.getText().charAt(i)))||(i==0 && phoneNumber.getText().charAt(i)!='5')) {
                        flagP = false;
                        errorPhone.setVisibility(View.VISIBLE);
                        iconPhone.setVisibility(View.VISIBLE);
                        errorPhone.setVisibility(View.VISIBLE);
                    }
                }
            }
            if(nameI.getText().toString().length()==0){
                flagN = false;
                errorName.setVisibility(View.VISIBLE);
                iconName.setVisibility(View.VISIBLE);
                errorName.setVisibility(View.VISIBLE);
            }
            if(flagP){
                errorPhone.setVisibility(View.INVISIBLE);
                iconPhone.setVisibility(View.INVISIBLE);
                errorPhone.setVisibility(View.INVISIBLE);
            }
            if(flagN){
                errorName.setVisibility(View.INVISIBLE);
                iconName.setVisibility(View.INVISIBLE);
                errorName.setVisibility(View.INVISIBLE);
            }
            if(flagP && flagN) {
                EditText phone = findViewById(R.id.phone);
                EditText name = findViewById(R.id.name);
                String phoneString = phone.getText().toString();
                String nameString = name.getText().toString();
                MemberListItem member = new MemberListItem(phoneString, nameString);
                if (!membersList.contains(member)) {
                    membersList.add(member);
                    //phones.add(phoneString);
                    members_adapter.notifyDataSetChanged();
                    //names.add(nameString);
                }
            }
        });



        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(view -> {
            onBackPressed();
        });

        ImageButton btnAria = findViewById(R.id.btnAria);
        btnAria.setOnClickListener(view -> {
            Intent intent = new Intent(this, AddAriaActivity.class);
            intent.putExtra("token",token);
            intent.putExtra("username",username);
            startActivity(intent);
        });
        ImageButton btnAriaList=findViewById(R.id.arialstbtn);
        btnAriaList.setOnClickListener(view->{

            Intent intent=new Intent(this, AriaListEventsActivity.class);
            intent.putExtra("username",username);
            intent.putExtra("token",token);
            startActivity(intent);
        });

        ImageButton btnhome=findViewById(R.id.btnhome);
        btnhome.setOnClickListener(view->{
            Intent intent=new Intent(this,CalendarActivity.class);
            intent.putExtra("username",username);
            intent.putExtra("token",token);
            startActivity(intent);
        });

        /*ImageView btnAdd=findViewById(R.id.addBtn);
        btnAdd.setOnClickListener(view->{
            EditText phone=findViewById(R.id.phone);
            EditText name=findViewById(R.id.name);
            String phoneString=phone.getText().toString();
            String nameString=name.getText().toString();
            MemberListItem member=new MemberListItem(phoneString,nameString);
            if (!membersList.contains(member)) {
                membersList.add(member);
                phones.add(phoneString);
                members_adapter.notifyDataSetChanged();
            }

        });*/

        ImageView btnDone=findViewById(R.id.btnDone);
        btnDone.setOnClickListener(view->{


            EventsAPI eventsAPI=new EventsAPI();
            //FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(MembersActivity.this, instanceIdResult -> {
                String tok = "";
                //String tok = instanceIdResult.getToken();
                //System.out.println("phonesss");
                //System.out.println(phones.size());
                //for(int i=0;i<phones.size();i++){
                //    System.out.println(phones.get(i));
                //}
                List<PhoneUsers2> pu=eventsAPI.checkPhones(token, title, description, start, end, alert, date, membersList, tok);
                if (pu.get(0).getId().equals("-1")){
                    System.out.println("-1");
                    members_adapter.updateNotUsers(pu.get(0).getNotUsers());
                    System.out.println("after");
                }
                else{
                    System.out.println("++++++++++++++++++++++++++++++++++");
                    System.out.println(h1);
                    System.out.println(m1);
                    System.out.println(h2);
                    System.out.println(m2);
                    System.out.println(yearInt);
                    System.out.println(monthInt);
                    System.out.println(dayInt);
                    //FirebaseAPI firebaseAPI = new FirebaseAPI();
                    for (int i=0; i<pu.size(); i++){
                        String message = " you got new invitation from "+ pu.get(i).getSender();
                        //firebaseAPI.sendMessage("new invitation!", message, pu.get(i).getAppToken());
                    }
                    showCustomPermissionDialog(pu.get(0).getId(), title, description, start, end, monthInt, yearInt, dayInt, h1, m1, h2, m2, token);
                    Intent intent=new Intent(this,CalendarActivity.class);
                    intent.putExtra("token",token);
                    intent.putExtra("username",username);
                    startActivity(intent);

                }
            //});
        });


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