package com.example.aria;
import static java.lang.Character.isDigit;
import androidx.annotation.NonNull;
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
import android.provider.CalendarContract;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.aria.RetroFitClasses.EventsAPI;
import com.example.aria.RetroFitClasses.FirebaseAPI;
import com.example.aria.RetroFitClasses.NotUser;
import com.example.aria.RetroFitClasses.PhoneUsers2;
import com.example.aria.adapters.MembersAdapter;
import com.google.firebase.iid.FirebaseInstanceId;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

public class MembersActivity extends AppCompatActivity {
    private static final int PERMISSIONS_REQUEST_CODE_CALENDAR = 100;
    private static final int PERMISSIONS_REQUEST_CODE_SMS = 1011;
    private final int MINUTES=60;
    private final String WITHOUT_GOOGLE_CALENDAR="0";
    String username, token, date="",fullName;

    String title,description,start,end;
    int monthInt,yearInt,dayInt,h1,h2,m1,m2;
    List<PhoneUsers2> pu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.members);

        final int PHONE_DIGITS = 9;
        final int EMPTY_LENGTH = 0;

        List<MemberListItem> membersList=new ArrayList<>();
        List<String>names = new ArrayList<String>();
        List<String>phones=new ArrayList<>();
        ListView lstMembers=findViewById(R.id.friendsList);
        NotUser[] notUsers = new NotUser[0];
        MembersAdapter members_adapter=new MembersAdapter(membersList,notUsers);
        lstMembers.setAdapter(members_adapter);
        lstMembers.setAdapter(members_adapter);

        token = getIntent().getExtras().getString("token");

        username = getIntent().getExtras().getString("username");
        fullName = getIntent().getExtras().getString("fullName");
        title = getIntent().getExtras().getString("title");
        description = getIntent().getExtras().getString("description");
        start = getIntent().getExtras().getString("start");
        end = getIntent().getExtras().getString("end");
        date = getIntent().getExtras().getString("date");
        String alert = getIntent().getExtras().getString("alert");
        monthInt = getIntent().getExtras().getInt("monthInt");

        dayInt = getIntent().getExtras().getInt("dayInt");
        yearInt = getIntent().getExtras().getInt("yearInt");
        h1 = getIntent().getExtras().getInt("h1");
        m1 = getIntent().getExtras().getInt("m1");
        h2 = getIntent().getExtras().getInt("h2");
        m2 = getIntent().getExtras().getInt("m2");
        int requestCode=getIntent().getExtras().getInt("code");

        EditText phoneNumber = findViewById(R.id.phone);
        EditText nameI = findViewById(R.id.name);
        LinearLayout errorName = findViewById(R.id.wrongName);
        LinearLayout errorPhone = findViewById(R.id.wrongPhone);
        ImageView iconName = findViewById(R.id.wrongNameIcon);
        ImageView iconPhone = findViewById(R.id.wrongPhoneIcon);

        ImageView addBtn=findViewById(R.id.addBtn);
        addBtn.setOnClickListener(view->{
            EditText phone = findViewById(R.id.phone);
            EditText name = findViewById(R.id.name);

            boolean flagP=true;
            boolean flagN=true;

            if(phoneNumber.getText().length()!=PHONE_DIGITS ){
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
            if(nameI.getText().toString().length()==EMPTY_LENGTH){
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
            else{
                phone.setText("");
            }
            if(flagN){
                errorName.setVisibility(View.INVISIBLE);
                iconName.setVisibility(View.INVISIBLE);
                errorName.setVisibility(View.INVISIBLE);
            }
            if(flagP && flagN) {
                String phoneString = "+972"+phone.getText().toString();
                String nameString = name.getText().toString();
                MemberListItem member = new MemberListItem(phoneString, nameString);
                if (!membersList.contains(member)) {
                    membersList.add(member);

                    members_adapter.notifyDataSetChanged();


                }
                phone.setText("");
                name.setText("");
            }
        });



        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(view -> {
            onBackPressed();
        });

        ImageButton btnAria = findViewById(R.id.btnAria);
        btnAria.setOnClickListener(view -> {
            String[] permissions2 = {Manifest.permission.READ_SMS, Manifest.permission.SEND_SMS, Manifest.permission.RECEIVE_SMS};
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED ||  ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, permissions2, PERMISSIONS_REQUEST_CODE_SMS);

            }
            else {
                Intent intent = new Intent(this, AddAriaActivity.class);
                intent.putExtra("token",token);
                intent.putExtra("username",username);
                intent.putExtra("fullName",fullName);
                startActivity(intent);

            }
        });
        ImageButton btnAriaList=findViewById(R.id.arialstbtn);
        btnAriaList.setOnClickListener(view->{

            Intent intent=new Intent(this, AriaListEventsActivity.class);
            intent.putExtra("username",username);
            intent.putExtra("token",token);
            intent.putExtra("fullName",fullName);
            startActivity(intent);
        });

        ImageButton btnhome=findViewById(R.id.btnhome);
        btnhome.setOnClickListener(view->{
            Intent intent=new Intent(this,CalendarActivity.class);
            intent.putExtra("username",username);
            intent.putExtra("token",token);
            intent.putExtra("fullName",fullName);
            startActivity(intent);
        });



        ImageView btnDone=findViewById(R.id.btnDone);
        btnDone.setOnClickListener(view->{


            EventsAPI eventsAPI=new EventsAPI();
            FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(MembersActivity.this, instanceIdResult -> {

            String tok = instanceIdResult.getToken();


            pu=eventsAPI.checkPhones(token, title, description, start, end, alert, date, membersList, tok, WITHOUT_GOOGLE_CALENDAR,requestCode);
            if (pu.get(0).getId().equals("-1")){

                members_adapter.updateNotUsers(pu.get(0).getNotUsers());

            }
            else{

                FirebaseAPI firebaseAPI = new FirebaseAPI();

                if(!pu.get(0).getTitle().equals("")) {


                    for (int i = 0; i < pu.size(); i++) {
                        String message = " you got new invitation from " + fullName;

                        firebaseAPI.sendMessage("new invitation!", message, pu.get(i).getAppToken());
                    }
                    showCustomPermissionDialog(pu.get(0).getId(), title, description, start, end, monthInt, yearInt, dayInt, h1, m1, h2, m2, token);


                }
                else{
                    showCustomPermissionDialog(pu.get(0).getId(), title, description, start, end, monthInt, yearInt, dayInt, h1, m1, h2, m2, token);
                }


            }
            });
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
        tvTitle.setText("Google calendar");
        tvMessage.setText("do you want to add this event to your google calendar?");
        Button btnPositive = dialogView.findViewById(R.id.btnPositive);
        Button btnNegative = dialogView.findViewById(R.id.btnNegative);
        Intent intent = new Intent(this, CalendarActivity.class);
        intent.putExtra("token", token);
        intent.putExtra("username", username);
        intent.putExtra("fullName",fullName);

        btnPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                addEventToCalendar(id, title, des, start, end, monthInt, yearInt, dayInt, h1, m1, h2, m2);


            }
        });

        btnNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                startActivity(intent);

            }
        });

        dialog.show();
    }

    private void addEventToCalendar(String id, String title, String des, String start, String end, int monthInt, int yearInt, int dayInt, int h1, int m1, int h2, int m2) {



        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR) == PackageManager.PERMISSION_GRANTED) {

            java.util.Calendar startCal = java.util.Calendar.getInstance();
            startCal.set(yearInt, monthInt, dayInt, h1, m1);
            long startTime = startCal.getTimeInMillis();
            int numH = h2 - h1;
            int numM = m2 - m1;
            int timeInM = (numH * MINUTES) + numM;
            long endTime = startTime + timeInM * MINUTES * 1000;


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
            Intent intent = new Intent(this, CalendarActivity.class);
            intent.putExtra("token", token);
            intent.putExtra("username", username);
            intent.putExtra("fullName",fullName);
            startActivity(intent);

        } else {

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

        return -1;
    }

    private void requestCalendarPermissions(String id, String title, String des, String start, String end, int monthInt, int yearInt, int dayInt, int h1, int m1, int h2, int m2, String date) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR},
                    PERMISSIONS_REQUEST_CODE_CALENDAR);
        }
        else{

            addEventToCalendar(id, title, des, start, end, monthInt, yearInt, dayInt, h1, m1, h2, m2);

        }
    }
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);



        if (requestCode == PERMISSIONS_REQUEST_CODE_CALENDAR) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {



                addEventToCalendar(pu.get(0).getId(), title, description, start, end, monthInt, yearInt, dayInt, h1, m1, h2, m2);
            } else {

                Toast.makeText(this, "Can't add to google calendar", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, CalendarActivity.class);
                intent.putExtra("token", token);
                intent.putExtra("username", username);
                intent.putExtra("fullName",fullName);
                startActivity(intent);

            }
        }
        if (requestCode == PERMISSIONS_REQUEST_CODE_SMS) {


            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(this, AddAriaActivity.class);
                intent.putExtra("token",token);
                intent.putExtra("username",username);
                intent.putExtra("fullName",fullName);
                startActivity(intent);

            } else {
                Toast.makeText(this, "aria needs sms permission", Toast.LENGTH_SHORT).show();



            }
        }
        }
}
