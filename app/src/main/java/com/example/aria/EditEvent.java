package com.example.aria;
import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.example.aria.RetroFitClasses.EventsAPI;
import com.example.aria.RetroFitClasses.UsersAPI;
import com.google.firebase.iid.FirebaseInstanceId;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.List;

public class EditEvent extends AppCompatActivity {
    private static final int PERMISSIONS_REQUEST_CODE_CALENDAR = 100;
    private static final int PERMISSIONS_REQUEST_CODE_NOTIFICATIONS = 1010;
    private static final int PERMISSIONS_REQUEST_CODE_SMS = 1011;
    private final String PADDING_ZERO="0";
    private final int ONE_DIGIT=10;

    String token,username,date_now,fullName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_event2);

        token = getIntent().getExtras().getString("token");
        username = getIntent().getExtras().getString("username");
        fullName = getIntent().getExtras().getString("fullName");

        String id=getIntent().getExtras().getString("id");
        date_now=getIntent().getExtras().getString("date");
        String title=getIntent().getExtras().getString("title");
        String start=getIntent().getExtras().getString("start");
        String end=getIntent().getExtras().getString("end");
        String alert=getIntent().getExtras().getString("alert");
        String description=getIntent().getExtras().getString("description");
        int requestCode=getIntent().getExtras().getInt("requestCode");

        int requestCodePending = generateUniqueRequestCode();


        DateTimeFormatter formatter = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        }
        LocalDate datecheck = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            datecheck = LocalDate.parse(date_now, formatter);
        }
        int dayOfWeekNumber = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            dayOfWeekNumber = datecheck.getDayOfWeek().getValue();
        }

        if(dayOfWeekNumber==7)
        {
            dayOfWeekNumber=0;
        }

        UsersAPI usersAPI = new UsersAPI();
        List<String> lst= usersAPI.getTimes(dayOfWeekNumber,token);
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

        ImageView homeBtn = findViewById(R.id.btnhome);
        homeBtn.setOnClickListener(v ->  {
            Intent intent=new Intent(this, CalendarActivity.class);
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
                Intent intent=new Intent(this, AddAriaActivity.class);
                intent.putExtra("username",username);
                intent.putExtra("token",token);
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

        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(view -> {
            onBackPressed();
        });


        EditText title_edittext=findViewById(R.id.title);
        TextView startView=findViewById(R.id.startsTextView);
        TextView endView=findViewById(R.id.endsTextView);
        EditText description_edittext=findViewById(R.id.des);
        TextView date_textview = findViewById(R.id.date);
        title_edittext.setText(title);
        startView.setText(start);
        endView.setText(end);
        description_edittext.setText(description);
        date_textview.setText(date_now);


        String day=date_now.substring(0,2);
        String month=date_now.substring(3,5);
        String year=date_now.substring(6);


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
            if(date.getVisibility()==View.INVISIBLE){
                date.setVisibility(View.VISIBLE);
                date_layout.setVisibility(View.VISIBLE);
            }
            else if(date.getVisibility()==View.VISIBLE){
                date.setVisibility(View.INVISIBLE);
                date_layout.setVisibility(View.INVISIBLE);
            }
        });


        DatePicker date = findViewById(R.id.DatePicker);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            date.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
                @Override
                public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    String newDay = String.valueOf(dayOfMonth);
                    int month=monthOfYear+1;
                    String newMonth = String.valueOf(month);
                    String date;
                    if(dayOfMonth/ONE_DIGIT==0)
                        newDay = PADDING_ZERO.concat(String.valueOf(dayOfMonth));
                    if(month/ONE_DIGIT==0)
                        newMonth = PADDING_ZERO.concat(String.valueOf(month));
                    date = (((newDay.concat("/")).concat(newMonth)).concat("/")).concat(String.valueOf(year));

                    date_textview.setText(date);
                }
            });
        }

        startView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialogStarts=new TimePickerDialog(EditEvent.this, android.R.style.Theme_Holo_Light_Dialog, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        if(minute<ONE_DIGIT&&hourOfDay<ONE_DIGIT)
                            startView.setText(PADDING_ZERO+hourOfDay+":"+PADDING_ZERO+minute);
                        else if(minute<ONE_DIGIT)
                            startView.setText(hourOfDay+":"+PADDING_ZERO+minute);
                        else if(hourOfDay<ONE_DIGIT)
                            startView.setText(PADDING_ZERO+hourOfDay+":"+minute);
                        else
                            startView.setText(hourOfDay+":"+minute);


                    }
                },23,59,true);
                timePickerDialogStarts.show();
            }
        });

        endView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialogStarts=new TimePickerDialog(EditEvent.this, android.R.style.Theme_Holo_Light_Dialog, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        if(minute<ONE_DIGIT&&hourOfDay<ONE_DIGIT)
                            endView.setText(PADDING_ZERO+hourOfDay+":"+PADDING_ZERO+minute);
                        else if(minute<ONE_DIGIT)
                            endView.setText(hourOfDay+":"+PADDING_ZERO+minute);
                        else if(hourOfDay<ONE_DIGIT)
                            endView.setText(PADDING_ZERO+hourOfDay+":"+minute);
                        else
                            endView.setText(hourOfDay+":"+minute);


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
            i.putExtra("username",username);
            i.putExtra("day",day);
            i.putExtra("month",month);
            i.putExtra("year",year);
            i.putExtra("date",date_now);
            i.putExtra("fullName",fullName);
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
            EditText editTitle = findViewById(R.id.title);

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

                titleFlag=true;
            }
            if (titleFlag&&timeFlag) {

            EventsAPI eventsAPI=new EventsAPI();

            if(spinner.getSelectedItem().equals("None"))
                eventsAPI.updateAll(Integer.parseInt(id),token,title_edittext.getText().toString(),startView.getText().toString(),endView.getText().toString(),date_textview.getText().toString(),(String) spinner.getSelectedItem(),description_edittext.getText().toString(),-1);
            else
                eventsAPI.updateAll(Integer.parseInt(id),token,title_edittext.getText().toString(),startView.getText().toString(),endView.getText().toString(),date_textview.getText().toString(),(String) spinner.getSelectedItem(),description_edittext.getText().toString(),requestCodePending);





            String newDate = date_textview.getText().toString();
            int index_first_slash = newDate.indexOf("/");
            String newDay = newDate.substring(index_first_slash - 2, index_first_slash);
            String newMonth = newDate.substring(index_first_slash + 1, index_first_slash + 3);
            String newYear = newDate.substring(index_first_slash + 4, newDate.length());


            String googleId = eventsAPI.idGoogle(Integer.parseInt(id), token);

            if (!googleId.equals("-1")){

                long eventId = Integer.parseInt(googleId);

                updateEventTime(eventId, title_edittext.getText().toString(), description_edittext.getText().toString(), Integer.parseInt(newMonth) - 1, Integer.parseInt(newYear), Integer.parseInt(newDay), h1, m1, h2, m2);
            }

            Toast.makeText(this, "Event updated", Toast.LENGTH_SHORT).show();

            long eventId = 0;

            if(!startView.getText().toString().equals(start)||!date_textview.getText().toString().equals(date_now)||!spinner.getSelectedItem().equals(alert)||!title_edittext.getText().toString().equals(title))
            {
                if(!alert.equals("None"))
                {

                    Intent intentCancel = new Intent(EditEvent.this, NotificationBroadcast.class);
                    intentCancel.setAction("com.example.aria.ACTION_SHOW_NOTIFICATION");
                    PendingIntent pendingIntentCancel = PendingIntent.getBroadcast(EditEvent.this, requestCode, intentCancel, PendingIntent.FLAG_UPDATE_CURRENT|PendingIntent.FLAG_IMMUTABLE);

                    AlarmManager alarmManagerCancel = (AlarmManager) EditEvent.this.getSystemService(Context.ALARM_SERVICE);
                    alarmManagerCancel.cancel(pendingIntentCancel);
                }
                if(!spinner.getSelectedItem().equals("None"))
                {
                    if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {

                        requestPermissions(new String[]{
                                Manifest.permission.POST_NOTIFICATIONS
                        }, PERMISSIONS_REQUEST_CODE_NOTIFICATIONS);

                    }
                    else{

                        TextView titleView=findViewById(R.id.title);
                        String titlenew=titleView.getText().toString();



                        TextView dateView=findViewById(R.id.date);
                        String date2=dateView.getText().toString();
                        int index_first_slashnew = date2.indexOf("/");
                        String daynew = date2.substring(index_first_slashnew - 2, index_first_slashnew);
                        String monthnew = date2.substring(index_first_slashnew + 1, index_first_slashnew + 3);
                        String yearnew = date2.substring(index_first_slashnew + 4, date2.length());
                        int dayInt = Integer.valueOf(daynew);
                        int monthInt = Integer.valueOf(monthnew);
                        int yearInt = Integer.valueOf(yearnew);


                        int index_time = startTime.indexOf(":");
                        String hour = startTime.substring(index_time - 2, index_time);
                        String minute = startTime.substring(index_time + 1, startTime.length());

                        int hourInt = Integer.valueOf(hour);
                        int minuteInt = Integer.valueOf(minute);

                        monthInt = monthInt - 1;
                        String description2 = "";
                        Spinner spinnernew=findViewById(R.id.alert);
                        if (spinnernew.getSelectedItem().equals("hour before")) {

                            if (hourInt == 0) {
                                hourInt = 23;
                            } else {
                                hourInt = hourInt - 1;
                            }
                            description2 = "today at " + startTime;
                        }
                        int newDayInt = dayInt;
                        if (spinnernew.getSelectedItem().equals("day before")) {
                            if (newDayInt == 1) {
                                newDayInt = 23;
                            } else {
                                newDayInt = newDayInt - 1;
                            }
                            description2 = "tomorrow at " + startTime;
                        }

                        Intent intent1 = new Intent(EditEvent.this, NotificationBroadcast.class);
                        intent1.setAction("com.example.aria.ACTION_SHOW_NOTIFICATION");
                        intent1.putExtra("title", titlenew);
                        intent1.putExtra("description", description2);
                        final int[] timeValues = {hourInt, minuteInt, newDayInt, monthInt, yearInt};

                        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(EditEvent.this, instanceIdResult -> {
                            String newToken = "";
                            newToken = instanceIdResult.getToken();
                            intent1.putExtra("token",newToken);

                            PendingIntent pendingIntent = PendingIntent.getBroadcast(EditEvent.this, requestCodePending, intent1, PendingIntent.FLAG_UPDATE_CURRENT |PendingIntent.FLAG_IMMUTABLE);
                            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                            if (!spinnernew.getSelectedItem().equals("None")) {


                                android.icu.util.Calendar calendar = android.icu.util.Calendar.getInstance();
                                calendar.set(android.icu.util.Calendar.YEAR, yearInt);
                                calendar.set(android.icu.util.Calendar.MONTH, timeValues[3]);
                                calendar.set(android.icu.util.Calendar.DAY_OF_MONTH, timeValues[2]);
                                calendar.set(android.icu.util.Calendar.HOUR_OF_DAY, timeValues[0]);
                                calendar.set(android.icu.util.Calendar.MINUTE, minuteInt);
                                calendar.set(android.icu.util.Calendar.SECOND, 0);
                                long alarmTimeMillis = calendar.getTimeInMillis();

                                alarmManager.set(AlarmManager.RTC_WAKEUP, alarmTimeMillis, pendingIntent);

                                Intent i=new Intent(this,Day.class);
                                i.putExtra("token",token);
                                i.putExtra("username",username);
                                i.putExtra("day",day);
                                i.putExtra("month",month);
                                i.putExtra("year",year);
                                i.putExtra("date",date_now);
                                i.putExtra("fullName",fullName);
                                startActivity(i);
                            }
                            else {
                                Intent i=new Intent(this,Day.class);
                                i.putExtra("token",token);
                                i.putExtra("username",username);
                                i.putExtra("day",day);
                                i.putExtra("month",month);
                                i.putExtra("year",year);
                                i.putExtra("date",date_now);
                                i.putExtra("fullName",fullName);
                                startActivity(i);
                            }


                        });

                    }
                }
                else{
                    Intent i=new Intent(this,Day.class);
                    i.putExtra("token",token);
                    i.putExtra("username",username);
                    i.putExtra("fullName",fullName);
                    i.putExtra("day",day);
                    i.putExtra("month",month);
                    i.putExtra("year",year);
                    i.putExtra("date",date_now);
                    startActivity(i);

                }

            }
            else{
                Intent i=new Intent(this,Day.class);
                i.putExtra("token",token);
                i.putExtra("username",username);
                i.putExtra("fullName",fullName);
                i.putExtra("day",day);
                i.putExtra("month",month);
                i.putExtra("year",year);
                i.putExtra("date",date_now);
                startActivity(i);
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
    }
    private void updateEventTime(long id, String title, String des, int monthInt, int yearInt, int dayInt, int h1, int m1, int h2, int m2) {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_CALENDAR) == PackageManager.PERMISSION_GRANTED) {


            Calendar startCal = Calendar.getInstance();
            startCal.set(yearInt, monthInt, dayInt, h1, m1);
            long newStartTime = startCal.getTimeInMillis();

            Calendar endCal = Calendar.getInstance();
            endCal.set(yearInt, monthInt, dayInt, h2, m2);
            long newEndTime = endCal.getTimeInMillis();

            ContentResolver cr = getContentResolver();
            ContentValues values = new ContentValues();
            values.put(CalendarContract.Events.DTSTART, newStartTime);
            values.put(CalendarContract.Events.DTEND, newEndTime);
            values.put(CalendarContract.Events.TITLE, title);
            values.put(CalendarContract.Events.DESCRIPTION, des);

            Uri updateUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, id);

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR) == PackageManager.PERMISSION_GRANTED) {


                try {
                    int rows = cr.update(updateUri, values, null, null);

                } catch (Exception e) {

                    Toast.makeText(this, "Error updating event: " + e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        } else {


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


            updateEventTime(id, title, des, monthInt, yearInt, dayInt, h1, m1, h2, m2);
        }
    }
    public void deleteEvent(ContentResolver contentResolver, long eventID) {
        Uri deleteUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, eventID);
        int rows = contentResolver.delete(deleteUri, null, null);
        if (rows > 0) {

        } else {
            Toast.makeText(this, "Error deleting event", Toast.LENGTH_SHORT).show();
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

        TextView start_textview=findViewById(R.id.startsTextView);


        if (requestCode == PERMISSIONS_REQUEST_CODE_NOTIFICATIONS) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                TextView titleView=findViewById(R.id.title);
                String title=titleView.getText().toString();
                TextView startView = findViewById(R.id.startsTextView);
                TextView endView = findViewById(R.id.endsTextView);
                String startTime = startView.getText().toString();
                String endTime = endView.getText().toString();
                int h1 = Integer.parseInt(startTime.substring(0, startTime.indexOf(':')));
                int m1 = Integer.parseInt(startTime.substring(startTime.indexOf(':') + 1, startTime.length()));
                int h2 = Integer.parseInt(endTime.substring(0, endTime.indexOf(':')));
                int m2 = Integer.parseInt(endTime.substring(endTime.indexOf(':') + 1, endTime.length()));
                TextView dateView=findViewById(R.id.date);
                String date2=dateView.getText().toString();
                int index_first_slash = date2.indexOf("/");
                String day = date2.substring(index_first_slash - 2, index_first_slash);
                String month = date2.substring(index_first_slash + 1, index_first_slash + 3);
                String year = date2.substring(index_first_slash + 4, date2.length());
                int dayInt = Integer.valueOf(day);
                int monthInt = Integer.valueOf(month);
                int yearInt = Integer.valueOf(year);


                int index_time = startTime.indexOf(":");
                String hour = startTime.substring(index_time - 2, index_time);
                String minute = startTime.substring(index_time + 1, startTime.length());

                int hourInt = Integer.valueOf(hour);
                int minuteInt = Integer.valueOf(minute);

                monthInt = monthInt - 1;
                String description2 = "";
                Spinner spinner=findViewById(R.id.alert);
                if (spinner.getSelectedItem().equals("hour before")) {

                    if (hourInt == 0) {
                        hourInt = 23;
                    } else {
                        hourInt = hourInt - 1;
                    }
                    description2 = "today at " + startTime;
                }
                int newDayInt = dayInt;
                if (spinner.getSelectedItem().equals("day before")) {
                    if (newDayInt == 1) {
                        newDayInt = 23;
                    } else {
                        newDayInt = newDayInt - 1;
                    }
                    description2 = "tomorrow at " + startTime;
                }

                Intent intent1 = new Intent(EditEvent.this, NotificationBroadcast.class);
                intent1.setAction("com.example.aria.ACTION_SHOW_NOTIFICATION");
                intent1.putExtra("title", title);
                intent1.putExtra("description", description2);
                final int[] timeValues = {hourInt, minuteInt, newDayInt, monthInt, yearInt};

                FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(EditEvent.this, instanceIdResult -> {
                    String newToken = "";
                    newToken = instanceIdResult.getToken();
                    intent1.putExtra("token",newToken);
                    int requestCodePending = generateUniqueRequestCode();
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(EditEvent.this, requestCodePending, intent1, PendingIntent.FLAG_UPDATE_CURRENT |PendingIntent.FLAG_IMMUTABLE);
                    AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                    if (!spinner.getSelectedItem().equals("None")) {


                        android.icu.util.Calendar calendar = android.icu.util.Calendar.getInstance();
                        calendar.set(android.icu.util.Calendar.YEAR, yearInt);
                        calendar.set(android.icu.util.Calendar.MONTH, timeValues[3]);
                        calendar.set(android.icu.util.Calendar.DAY_OF_MONTH, timeValues[2]);
                        calendar.set(android.icu.util.Calendar.HOUR_OF_DAY, timeValues[0]);
                        calendar.set(android.icu.util.Calendar.MINUTE, minuteInt);
                        calendar.set(android.icu.util.Calendar.SECOND, 0);
                        long alarmTimeMillis = calendar.getTimeInMillis();

                        alarmManager.set(AlarmManager.RTC_WAKEUP, alarmTimeMillis, pendingIntent);

                        Intent i=new Intent(this,Day.class);
                        i.putExtra("token",token);
                        i.putExtra("username",username);
                        i.putExtra("fullName",fullName);
                        i.putExtra("day",day);
                        i.putExtra("month",month);
                        i.putExtra("year",year);
                        i.putExtra("date",date_now);
                        startActivity(i);
                    }


                });

            } else {
                Spinner spinner=findViewById(R.id.alert);
                spinner.setSelection(0);
                TextView alertmsgTextView=findViewById(R.id.alertmsg);
                alertmsgTextView.setVisibility(View.VISIBLE);
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
