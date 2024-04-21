package com.example.aria;
import android.Manifest;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import com.example.aria.RetroFitClasses.EventsAPI;
import com.example.aria.adapters.MembersAdapter;
import java.util.ArrayList;
import java.util.List;

public class AddCalendarActivity extends AppCompatActivity {

    private EventsAPI eventsAPI = new EventsAPI();
    private String title, des, token, start, end, a, date="";

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_calendar);
        List<MemberListItem> membersList = new ArrayList<>();

        ListView lstMembers=findViewById(R.id.lstMembers);
        MembersAdapter adapter=new MembersAdapter(membersList);
        lstMembers.setAdapter(adapter);

        ImageButton addMember = findViewById(R.id.addMember);
        addMember.setOnClickListener(v -> {
            EditText phone2=findViewById(R.id.phoneNumber);
            EditText name2=findViewById(R.id.name);
            MemberListItem m1=new MemberListItem(phone2.getText().toString(),name2.getText().toString());

            if(!membersList.contains(m1)) {
                membersList.add(m1);
                adapter.notifyDataSetChanged();
            }
        });
        createNotificationChannel();
        token = getIntent().getExtras().getString("token");
        date = getIntent().getExtras().getString("date");
        ImageButton btnClose = findViewById(R.id.btnClose);
        btnClose.setOnClickListener(view -> {
            Intent intent = new Intent(this, CalendarActivity.class);
            intent.putExtra("token",token);
            startActivity(intent);
        });

        Spinner spinner = findViewById(R.id.alert);
        ImageButton btnDone = findViewById(R.id.btnDone);
        btnDone.setOnClickListener(view -> {
            EditText editTitle = findViewById(R.id.title);
            TextView startView = findViewById(R.id.startsTextView);
            TextView endView = findViewById(R.id.endsTextView);
            EditText editDes = findViewById(R.id.des);
            a = (String) spinner.getSelectedItem();
            title = editTitle.getText().toString();
            des = editDes.getText().toString();
            start = startView.getText().toString();
            end = endView.getText().toString();

            if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{
                        Manifest.permission.POST_NOTIFICATIONS
                }, 1001);
            }
            else{

                int index_time = start.indexOf(":");
                String hour = start.substring(index_time - 2, index_time);
                String minute = start.substring(index_time + 1, start.length());
                int index_first_slash = date.indexOf("/");
                String day = date.substring(index_first_slash - 2, index_first_slash);
                String month = date.substring(index_first_slash + 1, index_first_slash + 3);
                String year = date.substring(index_first_slash + 4, date.length());

                int hourInt = Integer.valueOf(hour);
                int minuteInt = Integer.valueOf(minute);
                int dayInt = Integer.valueOf(day);
                int monthInt = Integer.valueOf(month);
                int yearInt = Integer.valueOf(year);

                monthInt = monthInt - 1;
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
                        dayInt = 23;
                    }
                    else{
                        dayInt = dayInt - 1;
                    }
                    description = "tomorrow at " + start;
                }
                Intent intent = new Intent(this, CalendarActivity.class);
                Intent intent1 = new Intent(AddCalendarActivity.this, NotificationBroadcast.class);
                intent1.setAction("com.example.aria.ACTION_SHOW_NOTIFICATION");
                intent1.putExtra("title", title); // Add title string
                intent1.putExtra("description", description);
                int requestCodePending = generateUniqueRequestCode();
                PendingIntent pendingIntent = PendingIntent.getBroadcast(AddCalendarActivity.this, requestCodePending, intent1, PendingIntent.FLAG_IMMUTABLE);
                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                if(!a.equals("None")){
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.YEAR, yearInt); // Year
                    calendar.set(Calendar.MONTH, monthInt); // Month (Note: January is 0)
                    calendar.set(Calendar.DAY_OF_MONTH, dayInt); // Day of the month
                    calendar.set(Calendar.HOUR_OF_DAY, hourInt); // Hour (in 24-hour format)
                    calendar.set(Calendar.MINUTE, minuteInt); // Minute
                    calendar.set(Calendar.SECOND, 0); // Second
                    long alarmTimeMillis = calendar.getTimeInMillis();

                    alarmManager.set(AlarmManager.RTC_WAKEUP, alarmTimeMillis, pendingIntent);
                }


                eventsAPI.addEvent(token, title, des, start, end, a, date);
                intent.putExtra("token", token);
                startActivity(intent);
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

                monthInt = monthInt - 1;
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
                        if (monthInt == 0){
                            monthInt = 11;
                        }
                        else{
                            monthInt = monthInt - 1;
                        }
                        if (monthInt == 0 || monthInt == 2 || monthInt == 4 || monthInt == 6 || monthInt == 7 || monthInt == 9 || monthInt == 11){
                            dayInt = 31;
                        } else if (monthInt == 1) {
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
                    calendar.set(Calendar.MONTH, monthInt); // Month (Note: January is 0)
                    calendar.set(Calendar.DAY_OF_MONTH, dayInt); // Day of the month
                    calendar.set(Calendar.HOUR_OF_DAY, hourInt); // Hour (in 24-hour format)
                    calendar.set(Calendar.MINUTE, minuteInt); // Minute
                    calendar.set(Calendar.SECOND, 0); // Second

                    long alarmTimeMillis = calendar.getTimeInMillis();
                    alarmManager.set(AlarmManager.RTC_WAKEUP, alarmTimeMillis, pendingIntent);
                }
                eventsAPI.addEvent(token, title, des, start, end, a, date);
                intent.putExtra("token", token);
                startActivity(intent);
            } else {
                // Permission is denied
                // You may inform the user about the consequences of not granting the permission
                // Or you may disable functionality that requires this permission
            }
        }
    }
}
