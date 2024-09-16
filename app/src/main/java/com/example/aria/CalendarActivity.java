package com.example.aria;
import android.Manifest;
import android.annotation.SuppressLint;
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
import com.applandeo.materialcalendarview.CalendarDay;
import com.applandeo.materialcalendarview.CalendarView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.applandeo.materialcalendarview.listeners.OnCalendarDayClickListener;
import com.example.aria.RetroFitClasses.EventsAPI;
import com.example.aria.RetroFitClasses.FirebaseAPI;
import com.example.aria.RetroFitClasses.MembersNotificationsMsg;
import com.example.aria.RetroFitClasses.NewEvent;
import com.example.aria.RetroFitClasses.NewEvent2;
import com.example.aria.RetroFitClasses.UsersAPI;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;


public class CalendarActivity extends AppCompatActivity {

    private static final int PERMISSIONS_REQUEST_CODE_CALENDAR = 100;
    private static final int PERMISSIONS_REQUEST_CODE_NOTIFICATIONS = 1001;
    private static final int PERMISSIONS_REQUEST_CODE_SMS = 1011;
    private final int MINUTES=60;
    private String username,title, des, start, end, token, date="", id,fullName;
    @SuppressLint({"ResourceAsColor", "ResourceType"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar2);
        token = getIntent().getExtras().getString("token");
        username=getIntent().getExtras().getString("username");
        fullName = getIntent().getExtras().getString("fullName");


        String activity=getIntent().getExtras().getString("activity");
        if(activity!=null)
        {
            if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(new String[]{
                        Manifest.permission.POST_NOTIFICATIONS
                }, PERMISSIONS_REQUEST_CODE_NOTIFICATIONS);
            }

        }
        else{
        }

        CalendarView calendarView = (CalendarView) findViewById(R.id.calendarView);
        calendarView.setHeaderColor(R.color.invisible);
        List<CalendarDay> calendarDays = new ArrayList<>();
        UsersAPI usersAPI=new UsersAPI();
        List<NewEvent> events = usersAPI.getEvents(token);
        if(events!=null) {

            for (int i = 0; i < events.size(); i++) {
                DateTimeFormatter formatter = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                }
                LocalDate date = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    date = LocalDate.parse(events.get(i).getDate(), formatter);
                }
                String monthString = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    monthString = date.format(DateTimeFormatter.ofPattern("MM"));
                }
                String dayString = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    dayString = date.format(DateTimeFormatter.ofPattern("dd"));
                }
                String yearString = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    yearString = date.format(DateTimeFormatter.ofPattern("yyyy"));
                }
                int day = Integer.parseInt(dayString);
                int year = Integer.parseInt(yearString);
                int month=Integer.parseInt(monthString)-1;
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, day);
                CalendarDay calendarDay = new CalendarDay(calendar);
                calendarDay.setImageResource(R.drawable.dot);
                calendarDays.add(calendarDay);
                calendarView.setCalendarDays(calendarDays);
            }
        }

        TextView txt=findViewById(R.id.text);
        EventsAPI eventsAPI=new EventsAPI();
        List<MembersNotificationsMsg> lstMembersNotifications=eventsAPI.getMembersNotificationMsg(token);
        if (lstMembersNotifications != null)
        {

            for(int i=0; i<lstMembersNotifications.size(); i++){
                id =  lstMembersNotifications.get(i).getId();
                showCustomPermissionDialog(lstMembersNotifications.get(i).getMessage(), lstMembersNotifications.get(i).getId(), lstMembersNotifications.get(i).getTokenSender(), lstMembersNotifications.get(i).getTitle(), lstMembersNotifications.get(i).getName());
            }
        }
        ImageButton btnAriaList=findViewById(R.id.arialstbtn);
        btnAriaList.setOnClickListener(view->{
            Intent intent=new Intent(this, AriaListEventsActivity.class);
            intent.putExtra("username",username);
            intent.putExtra("token",token);
            intent.putExtra("fullName",fullName);
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


        calendarView.setOnCalendarDayClickListener(new OnCalendarDayClickListener() {
            @Override
            public void onClick(CalendarDay calendarDay) {
                Calendar clickedDayCalendar = calendarDay.getCalendar();

                int year = clickedDayCalendar.get(Calendar.YEAR);
                int month = clickedDayCalendar.get(Calendar.MONTH);
                int dayOfMonth = clickedDayCalendar.get(Calendar.DAY_OF_MONTH);
                Intent intent=new Intent(CalendarActivity.this, Day.class);
                String newDay = String.valueOf(dayOfMonth);
                month+=1;
                String newMonth = String.valueOf(month);
                String date2;
                if(dayOfMonth/10==0)
                    newDay = "0".concat(String.valueOf(dayOfMonth));
                if(month/10==0)
                    newMonth = "0".concat(String.valueOf(month));
                date2 = (((newDay.concat("/")).concat(newMonth)).concat("/")).concat(String.valueOf(year));
                intent.putExtra("year", String.valueOf(year));
                intent.putExtra("month", String.valueOf(month));
                intent.putExtra("day", String.valueOf(dayOfMonth));
                intent.putExtra("token", token);
                intent.putExtra("date", date2);
                intent.putExtra("username",username);
                intent.putExtra("fullName",fullName);
                startActivity(intent);
            }
        });
    }
    private void showCustomPermissionDialog(String msg, String id, String senderToken, String title, String name) {
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

                    dialog.dismiss();
                    EventsAPI eventsAPI = new EventsAPI();
                    String [] user = new String [1];
                    user[0] = username;
                    Alert [] alert = new Alert[1];
                    alert[0] = new Alert(username, "None", -1);
                    String message = name + " has confirmed your invitation to "+ title;
                    firebaseAPI.sendMessage("invitation confirmed!", message, senderToken);
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
                    monthInt = monthInt -1;
                    showCustomPermissionDialogCalendar(j.getId(),j.getTitle(),j.getDescription(),j.getStart(),j.getEnd(),monthInt,yearInt,dayInt,h1,m1,h2,m2,token);
                    eventsAPI.deleteInvitation(Integer.parseInt(id), username);
                }
            });

            btnNegative.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String message = name + " has rejected your invitation to "+ title;
                    firebaseAPI.sendMessage("invitation rejected!", message, senderToken);
                    dialog.dismiss();
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
            tvTitle.setText("Google calendar");
            tvMessage.setText("do you want to add this event to your google calendar?");
            Button btnPositive = dialogView.findViewById(R.id.btnPositive);
            Button btnNegative = dialogView.findViewById(R.id.btnNegative);
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

                }
            });

            dialog.show();
        }
        private void addEventToCalendar(String id, String title, String des, String start, String end, int monthInt, int yearInt, int dayInt, int h1, int m1, int h2, int m2) {

            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_CALENDAR) == PackageManager.PERMISSION_GRANTED) {
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
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR},
                        PERMISSIONS_REQUEST_CODE_CALENDAR);
            }
            else{
                addEventToCalendar(id, title, des, start, end, monthInt, yearInt, dayInt, h1, m1, h2, m2);
            }
        }
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_CODE_NOTIFICATIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


            } else {
                Toast.makeText(this, "Permissions denied! you won't get notifications", Toast.LENGTH_SHORT).show();
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
        if (requestCode == PERMISSIONS_REQUEST_CODE_CALENDAR) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                TextView startView = findViewById(R.id.startsTextView);
                TextView endView = findViewById(R.id.endsTextView);
                String startTime = startView.getText().toString();
                String endTime = endView.getText().toString();
                int h1 = Integer.parseInt(startTime.substring(0, startTime.indexOf(':')));
                int m1 = Integer.parseInt(startTime.substring(startTime.indexOf(':') + 1, startTime.length()));
                int h2 = Integer.parseInt(endTime.substring(0, endTime.indexOf(':')));
                int m2 = Integer.parseInt(endTime.substring(endTime.indexOf(':') + 1, endTime.length()));
                int index_first_slash = date.indexOf("/");
                String day = date.substring(index_first_slash - 2, index_first_slash);
                String month = date.substring(index_first_slash + 1, index_first_slash + 3);
                String year = date.substring(index_first_slash + 4, date.length());
                int dayInt = Integer.valueOf(day);
                int monthInt = Integer.valueOf(month);
                int yearInt = Integer.valueOf(year);
                monthInt = monthInt - 1;

                addEventToCalendar(id, title, des, start, end, monthInt, yearInt, dayInt, h1, m1, h2, m2);
            } else {
                Toast.makeText(this, "Can't add to google calendar", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, CalendarActivity.class);
                intent.putExtra("token", token);
                intent.putExtra("username", username);
                intent.putExtra("fullName",fullName);
                startActivity(intent);

            }
        }

    }
}
