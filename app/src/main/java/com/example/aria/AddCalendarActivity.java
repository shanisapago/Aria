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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.example.aria.RetroFitClasses.EventsAPI;
import com.example.aria.RetroFitClasses.PhoneUsers2;
import com.example.aria.RetroFitClasses.UsersAPI;
import com.google.firebase.iid.FirebaseInstanceId;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

public class AddCalendarActivity extends AppCompatActivity {
    private static final int PERMISSIONS_REQUEST_CODE_CALENDAR = 100;
    private static final int PERMISSIONS_REQUEST_CODE_NOTIFICATIONS = 1001;
    private static final int PERMISSIONS_REQUEST_CODE_NOTIFICATIONS_MEMBERS = 1010;
    private static final int PERMISSIONS_REQUEST_CODE_SMS = 1011;

    private final String WITHOUT_GOOGLE_CALENDAR="0";
    private final String PADDING_ZERO="0";
    private final int ONE_DIGIT=10;
    private final int MINUTES=60;


    private EventsAPI eventsAPI = new EventsAPI();
    private String username,title, des, token, start, end, a, date="",fullName;

    String id;
    int requestCodePending;


    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_calendar2);

        final int EMPTY_LENGTH=0;


        List<MemberListItem> membersList = new ArrayList<>();
        username=getIntent().getExtras().getString("username");
        fullName = getIntent().getExtras().getString("fullName");
        int dayint=getIntent().getExtras().getInt("day");
        requestCodePending = generateUniqueRequestCode();
        createNotificationChannel();
        token = getIntent().getExtras().getString("token");
        date = getIntent().getExtras().getString("date");
        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(view -> {
            onBackPressed();
        });

        UsersAPI usersAPI=new UsersAPI();
        List<String> lst= usersAPI.getTimes(dayint,token);


        TextView time1=findViewById(R.id.time1);
        TextView time2=findViewById(R.id.time2);
        TextView time3=findViewById(R.id.time3);
        time1.setText(lst.get(0));
        time2.setText(lst.get(1));
        time3.setText(lst.get(2));
        time1.setOnClickListener(v->{
            String time1str=time1.getText().toString();
            TextView startView=findViewById(R.id.startsTextView);
            startView.setText(time1str.split("-")[0]);
            TextView endView=findViewById(R.id.endsTextView);
            endView.setText(time1str.split("-")[1]);

        });
        time2.setOnClickListener(v->{
            String time2str=time2.getText().toString();
            TextView startView=findViewById(R.id.startsTextView);
            startView.setText(time2str.split("-")[0]);
            TextView endView=findViewById(R.id.endsTextView);
            endView.setText(time2str.split("-")[1]);

        });
        time3.setOnClickListener(v->{
            String time3str=time3.getText().toString();
            TextView startView=findViewById(R.id.startsTextView);
            startView.setText(time3str.split("-")[0]);
            TextView endView=findViewById(R.id.endsTextView);
            endView.setText(time3str.split("-")[1]);

        });

        ImageButton btnhome=findViewById(R.id.btnhome);
        btnhome.setOnClickListener(view->{
            Intent intent=new Intent(this,CalendarActivity.class);
            intent.putExtra("username",username);
            intent.putExtra("token",token);
            intent.putExtra("fullName",fullName);
            startActivity(intent);
        });

        ImageButton btnAriaList=findViewById(R.id.arialstbtn);
        btnAriaList.setOnClickListener(view->{



            Intent intent=new Intent(this, AriaListEventsActivity.class);
            intent.putExtra("username",username);
            intent.putExtra("token",token);
            intent.putExtra("fullName",fullName);
            startActivity(intent);
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
            if(editTitle.getText().toString().length() != EMPTY_LENGTH){

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


                if (!(((String)spinner.getSelectedItem()).equals("None"))&& checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {

                        requestPermissions(new String[]{
                                Manifest.permission.POST_NOTIFICATIONS
                        }, PERMISSIONS_REQUEST_CODE_NOTIFICATIONS_MEMBERS);

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
                    intent1.putExtra("title", title);
                    intent1.putExtra("description", description);
                    final int[] timeValues = {hourInt, minuteInt, newDayInt, monthInt, yearInt};

                    FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(AddCalendarActivity.this, instanceIdResult -> {
                        String newToken = "";
                        newToken = instanceIdResult.getToken();
                        intent1.putExtra("token",newToken);
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(AddCalendarActivity.this, requestCodePending, intent1, PendingIntent.FLAG_UPDATE_CURRENT |PendingIntent.FLAG_IMMUTABLE);
                        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                        int code = -1;
                        if (!a.equals("None")) {


                            Calendar calendar = Calendar.getInstance();
                            calendar.set(Calendar.YEAR, yearInt);
                            calendar.set(Calendar.MONTH, timeValues[3]);
                            calendar.set(Calendar.DAY_OF_MONTH, timeValues[2]);
                            calendar.set(Calendar.HOUR_OF_DAY, timeValues[0]);
                            calendar.set(Calendar.MINUTE, minuteInt);
                            calendar.set(Calendar.SECOND, 0);
                            long alarmTimeMillis = calendar.getTimeInMillis();

                            alarmManager.set(AlarmManager.RTC_WAKEUP, alarmTimeMillis, pendingIntent);
                            code = requestCodePending;
                        }
                        List <String> list = new ArrayList<String>();
                        intent.putExtra("token",token);
                        intent.putExtra("username",username);
                        intent.putExtra("title",title);
                        intent.putExtra("description",des);
                        intent.putExtra("start",start);
                        intent.putExtra("end",end);
                        intent.putExtra("date",date);
                        intent.putExtra("alert",a);
                        intent.putExtra("monthInt",timeValues[3]);
                        intent.putExtra("dayInt",dayInt);
                        intent.putExtra("yearInt",yearInt);
                        intent.putExtra("h1",h1);
                        intent.putExtra("m1",m1);
                        intent.putExtra("h2",h2);
                        intent.putExtra("m2",m2);
                        intent.putExtra("code", code);
                        intent.putExtra("fullName",fullName);

                        startActivity(intent);

                    });



                }

            }
            else {

                if (timeFlag == false) {

                    ImageView errorIcon = findViewById(R.id.wrongTime);
                    LinearLayout errorText = findViewById(R.id.wrongTime2);
                    errorIcon.setVisibility(View.VISIBLE);
                    errorText.setVisibility(View.VISIBLE);
                } else {

                    ImageView errorIcon = findViewById(R.id.wrongTime);
                    LinearLayout errorText = findViewById(R.id.wrongTime2);
                    errorIcon.setVisibility(View.INVISIBLE);
                    errorText.setVisibility(View.INVISIBLE);
                }
                if (titleFlag==false) {

                    ImageView errorIcon = findViewById(R.id.wrongTitle);
                    LinearLayout errorText = findViewById(R.id.wrongTitle2);
                    errorIcon.setVisibility(View.VISIBLE);
                    errorText.setVisibility(View.VISIBLE);
                } else {

                    ImageView errorIcon = findViewById(R.id.wrongTitle);
                    LinearLayout errorText = findViewById(R.id.wrongTitle2);
                    errorIcon.setVisibility(View.INVISIBLE);
                    errorText.setVisibility(View.INVISIBLE);

                }

            }

        });



        Button btnDone = findViewById(R.id.btnDone);
        btnDone.setOnClickListener(view -> {

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
            if(editTitle.getText().toString().length() != EMPTY_LENGTH){

                titleFlag=true;
            }
            if (titleFlag&&timeFlag) {




                EditText editDes = findViewById(R.id.des);
                a = (String) spinner.getSelectedItem();
                title = editTitle.getText().toString();
                des = editDes.getText().toString();
                start = startView.getText().toString();
                end = endView.getText().toString();



                String range=start+"-"+end;
                int flag=0;
                if(range.equals(time1.getText().toString())||range.equals(time2.getText().toString())||range.equals(time3.getText().toString())){
                    flag=1;
                }
                usersAPI.calendarTimes(dayint,range,flag,token);

                int index_first_slash = date.indexOf("/");
                String day = date.substring(index_first_slash - 2, index_first_slash);
                String month = date.substring(index_first_slash + 1, index_first_slash + 3);
                String year = date.substring(index_first_slash + 4, date.length());
                int dayInt = Integer.valueOf(day);
                int monthInt = Integer.valueOf(month);
                int yearInt = Integer.valueOf(year);


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    if ((!((String)spinner.getSelectedItem()).equals("None"))&&checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.POST_NOTIFICATIONS}, PERMISSIONS_REQUEST_CODE_NOTIFICATIONS);


                    }
                 else {



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
                    intent1.putExtra("title", title);
                    intent1.putExtra("description", description);

                    int newHourInt=hourInt;
                    int newMonthInt=monthInt;
                    int newDayInt2=newDayInt;


                        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(AddCalendarActivity.this, instanceIdResult -> {
                        String newToken = "";
                        newToken = instanceIdResult.getToken();
                        intent1.putExtra("token", newToken);


                        PendingIntent pendingIntent = PendingIntent.getBroadcast(AddCalendarActivity.this, requestCodePending, intent1, PendingIntent.FLAG_UPDATE_CURRENT |PendingIntent.FLAG_IMMUTABLE);
                        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

                        int code = -1;
                        if (!a.equals("None")) {

                            Calendar calendar = Calendar.getInstance();
                            calendar.set(Calendar.YEAR, yearInt);
                            calendar.set(Calendar.MONTH, newMonthInt);
                            calendar.set(Calendar.DAY_OF_MONTH, newDayInt2);
                            calendar.set(Calendar.HOUR_OF_DAY, newHourInt);
                            calendar.set(Calendar.MINUTE, minuteInt);
                            calendar.set(Calendar.SECOND, 0);
                            long alarmTimeMillis = calendar.getTimeInMillis();

                            alarmManager.set(AlarmManager.RTC_WAKEUP, alarmTimeMillis, pendingIntent);
                            code = requestCodePending;
                        }



                            List <MemberListItem> list2 = null;
                            String empty = "";
                            List<PhoneUsers2> pu = eventsAPI.checkPhones(token, title, des, start, end, a, date, list2, empty, WITHOUT_GOOGLE_CALENDAR,code);
                            id=pu.get(0).getId();
                            showCustomPermissionDialog(id, title, des, start, end, Integer.valueOf(month)-1, yearInt, dayInt, h1, m1, h2, m2, token);
                    });






                }
            }
                else{

                    List <MemberListItem> list2 = null;
                    String empty = "";
                    List<PhoneUsers2> pu = eventsAPI.checkPhones(token, title, des, start, end, a, date, list2, empty, WITHOUT_GOOGLE_CALENDAR,-1);
                    id=pu.get(0).getId();
                    showCustomPermissionDialog(id, title, des, start, end, Integer.valueOf(month)-1, yearInt, dayInt, h1, m1, h2, m2, token);


                }

            }
            else {

                if (timeFlag == false) {

                    ImageView errorIcon = findViewById(R.id.wrongTime);
                    LinearLayout errorText = findViewById(R.id.wrongTime2);
                    errorIcon.setVisibility(View.VISIBLE);
                    errorText.setVisibility(View.VISIBLE);
                } else {

                    ImageView errorIcon = findViewById(R.id.wrongTime);
                    LinearLayout errorText = findViewById(R.id.wrongTime2);
                    errorIcon.setVisibility(View.INVISIBLE);
                    errorText.setVisibility(View.INVISIBLE);
                }
                if (titleFlag==false) {

                    ImageView errorIcon = findViewById(R.id.wrongTitle);
                    LinearLayout errorText = findViewById(R.id.wrongTitle2);
                    errorIcon.setVisibility(View.VISIBLE);
                    errorText.setVisibility(View.VISIBLE);
                } else {

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
                        if(minute<ONE_DIGIT&&hourOfDay<ONE_DIGIT)
                            startsTextView.setText(PADDING_ZERO+hourOfDay+":"+PADDING_ZERO+minute);
                        else if(minute<ONE_DIGIT)
                            startsTextView.setText(hourOfDay+":"+PADDING_ZERO+minute);
                        else if(hourOfDay<ONE_DIGIT)
                            startsTextView.setText(PADDING_ZERO+hourOfDay+":"+minute);
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
                        if(minute<ONE_DIGIT&&hourOfDay<ONE_DIGIT)
                            endsTextView.setText(PADDING_ZERO+hourOfDay+":"+PADDING_ZERO+minute);
                        else if(minute<ONE_DIGIT)
                            endsTextView.setText(hourOfDay+":"+PADDING_ZERO+minute);
                        else if(hourOfDay<ONE_DIGIT)
                            endsTextView.setText(PADDING_ZERO+hourOfDay+":"+minute);
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

        if (requestCode == PERMISSIONS_REQUEST_CODE_NOTIFICATIONS) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                EditText editTitle = findViewById(R.id.title);
                TextView startView = findViewById(R.id.startsTextView);
                TextView endView = findViewById(R.id.endsTextView);
                String startTime = startView.getText().toString();
                String endTime = endView.getText().toString();
                int h1 = Integer.parseInt(startTime.substring(0, startTime.indexOf(':')));
                int m1 = Integer.parseInt(startTime.substring(startTime.indexOf(':') + 1, startTime.length()));
                int h2 = Integer.parseInt(endTime.substring(0, endTime.indexOf(':')));
                int m2 = Integer.parseInt(endTime.substring(endTime.indexOf(':') + 1, endTime.length()));
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
                final int[] timeValues = {hourInt, minuteInt, dayInt, newMonthInt, yearInt};

                Intent intent = new Intent(this, CalendarActivity.class);
                Intent intent1 = new Intent(AddCalendarActivity.this, NotificationBroadcast.class);
                intent1.setAction("com.example.aria.ACTION_SHOW_NOTIFICATION");
                intent1.putExtra("title", title);
                intent1.putExtra("description", description);
                FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(AddCalendarActivity.this, instanceIdResult -> {
                    String newToken = "";
                    newToken = instanceIdResult.getToken();
                    intent1.putExtra("token",newToken);



                    PendingIntent pendingIntent = PendingIntent.getBroadcast(AddCalendarActivity.this, requestCodePending, intent1, PendingIntent.FLAG_UPDATE_CURRENT |PendingIntent.FLAG_IMMUTABLE);
                    AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                    int code=-1;
                    if(!a.equals("None")){

                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.YEAR, yearInt);
                        calendar.set(Calendar.MONTH, timeValues[3]);
                        calendar.set(Calendar.DAY_OF_MONTH, timeValues[2]);
                        calendar.set(Calendar.HOUR_OF_DAY, timeValues[0]);
                        calendar.set(Calendar.MINUTE, minuteInt);
                        calendar.set(Calendar.SECOND, 0);

                        long alarmTimeMillis = calendar.getTimeInMillis();
                        alarmManager.set(AlarmManager.RTC_WAKEUP, alarmTimeMillis, pendingIntent);
                        code=requestCodePending;
                    }
                    List <MemberListItem> list2 = null;
                    String empty = "";

                    List<PhoneUsers2> pu= eventsAPI.checkPhones(token, title, des, start, end, a, date, list2, empty, WITHOUT_GOOGLE_CALENDAR,code);

                    id=pu.get(0).getId();
                    showCustomPermissionDialog(id, title, des, start, end, monthInt, yearInt, timeValues[2], h1, m1, h2, m2, token);

                });


            } else {

                Spinner spinner=findViewById(R.id.alert);
                spinner.setSelection(0);
                TextView alertmsgTextView=findViewById(R.id.alertmsg);
                alertmsgTextView.setVisibility(View.VISIBLE);



            }
        }
        if(requestCode==PERMISSIONS_REQUEST_CODE_NOTIFICATIONS_MEMBERS)
        {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

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
                intent1.putExtra("title", title);
                intent1.putExtra("description", description);
                final int[] timeValues = {hourInt, minuteInt, newDayInt, monthInt, yearInt};

                FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(AddCalendarActivity.this, instanceIdResult -> {
                    String newToken = "";
                    newToken = instanceIdResult.getToken();
                    intent1.putExtra("token",newToken);

                    PendingIntent pendingIntent = PendingIntent.getBroadcast(AddCalendarActivity.this, requestCodePending, intent1, PendingIntent.FLAG_UPDATE_CURRENT |PendingIntent.FLAG_IMMUTABLE);
                    AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                    if (!a.equals("None")) {


                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.YEAR, yearInt);
                        calendar.set(Calendar.MONTH, timeValues[3]);
                        calendar.set(Calendar.DAY_OF_MONTH, timeValues[2]);
                        calendar.set(Calendar.HOUR_OF_DAY, timeValues[0]);
                        calendar.set(Calendar.MINUTE, minuteInt);
                        calendar.set(Calendar.SECOND, 0);
                        long alarmTimeMillis = calendar.getTimeInMillis();

                        alarmManager.set(AlarmManager.RTC_WAKEUP, alarmTimeMillis, pendingIntent);
                    }
                    List <String> list = new ArrayList<String>();
                    Intent intent = new Intent(this, MembersActivity.class);
                    intent.putExtra("token",token);
                    intent.putExtra("username",username);
                    intent.putExtra("title",title);
                    intent.putExtra("description",des);
                    intent.putExtra("start",start);
                    intent.putExtra("end",end);
                    intent.putExtra("date",date);
                    intent.putExtra("alert",a);
                    intent.putExtra("monthInt",timeValues[3]);
                    intent.putExtra("dayInt",dayInt);
                    intent.putExtra("yearInt",yearInt);
                    intent.putExtra("h1",h1);
                    intent.putExtra("m1",m1);
                    intent.putExtra("h2",h2);
                    intent.putExtra("m2",m2);
                    intent.putExtra("fullName",fullName);

                    startActivity(intent);
                });








            }
            else{
            Spinner spinner=findViewById(R.id.alert);
            spinner.setSelection(0);
            TextView alertmsgTextView=findViewById(R.id.alertmsg);
            alertmsgTextView.setVisibility(View.VISIBLE);

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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {


                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR},
                        PERMISSIONS_REQUEST_CODE_CALENDAR);

            } else {

                addEventToCalendar(id, title, des, start, end, monthInt, yearInt, dayInt, h1, m1, h2, m2);

            }
        }
        else{

        }
    }
}