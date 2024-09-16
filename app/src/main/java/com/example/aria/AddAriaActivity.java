package com.example.aria;
import static java.lang.Character.isDigit;
import android.Manifest;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import com.example.aria.RetroFitClasses.AriaTimes;
import com.example.aria.RetroFitClasses.ChatsAPI;
import com.example.aria.RetroFitClasses.EventsAPI;
import com.example.aria.RetroFitClasses.UsersAPI;
import com.example.aria.adapters.MeetingTimeAdapter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AddAriaActivity extends AppCompatActivity {
    public static final MediaType JSON = MediaType.get("application/json");
    private static final int PERMISSIONS_REQUEST_CODE_CALENDAR = 100;
    private static final int PERMISSIONS_REQUEST_CODE_NOTIFICATIONS = 1001;
    private static final int PERMISSIONS_REQUEST_CODE_SMS = 1010;
    private int countertimes = 2;
    private final String DEFAULT_START_TIME="00:01";
    private final String DEFAULT_DATE="01/01/2024";
    private final String WITH_GOOGLE_CALENDAR="1";
    private final String WITHOUT_GOOGLE_CALENDAR="0";
    private final String PADDING_ZERO="0";
    private final int ONE_DIGIT=10;
    OkHttpClient client = new OkHttpClient();
    int id;
    String token,titleStr,descriptionStr,endTime,alertStr,username,fullName, phone, msg;
    ViewGroup header;
    EditText phoneNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        List<TimeMeeting> meetingTimeList=new ArrayList<>();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_aria3);

        final int LENGTH_PHONE=9;





        username = getIntent().getExtras().getString("username");
        token = getIntent().getExtras().getString("token");
        fullName = getIntent().getExtras().getString("fullName");
        ListView lstTimeMeeting=findViewById(R.id.lstTimeMeeting);
        LayoutInflater inflater = getLayoutInflater();
        header = (ViewGroup) inflater.inflate(R.layout.header, lstTimeMeeting, false);
        lstTimeMeeting.addHeaderView(header, null, false);
        TextView durationTextView=header.findViewById(R.id.duration);
        Duration d = parseDuration(durationTextView.getText().toString());
        MeetingTimeAdapter adapter=new MeetingTimeAdapter(meetingTimeList,d);
        lstTimeMeeting.setAdapter(adapter);
        TextView timeText=header.findViewById(R.id.time);
        TextView timeText2=header.findViewById(R.id.time2);
        TextView dateText=header.findViewById(R.id.date);
        LinearLayout linearTime1 = header.findViewById(R.id.linearTime1);
        LinearLayout linearTime1a = header.findViewById(R.id.linearTime1a);
        LinearLayout linearTime1b = header.findViewById(R.id.linearTime1b);
        TextView time1 = header.findViewById(R.id.time1);
        TextView time1a = header.findViewById(R.id.time1a);
        TextView time1b = header.findViewById(R.id.time1b);
        TextView date1 = header.findViewById(R.id.date1);
        TextView date1a = header.findViewById(R.id.date1a);
        TextView date1b = header.findViewById(R.id.date1b);
        UsersAPI usersAPI = new UsersAPI();
        List<AriaTimes> lstAriaTimes = usersAPI.getTimesAriaSort(token);
        time1.setText(lstAriaTimes.get(0).getTime());
        time1a.setText(lstAriaTimes.get(1).getTime());
        time1b.setText(lstAriaTimes.get(2).getTime());
        date1.setText(lstAriaTimes.get(0).getDate());
        date1a.setText(lstAriaTimes.get(1).getDate());
        date1b.setText(lstAriaTimes.get(2).getDate());
        linearTime1.setOnClickListener(v->{
            timeText.setText(time1.getText().toString().split("-")[0]);
            timeText2.setText(time1.getText().toString().split("-")[1]);
            dateText.setText(date1.getText().toString());
        });
        linearTime1a.setOnClickListener(v->{
            timeText.setText(time1a.getText().toString().split("-")[0]);
            timeText2.setText(time1a.getText().toString().split("-")[1]);
            dateText.setText(date1a.getText().toString());
        });
        linearTime1b.setOnClickListener(v->{
            timeText.setText(time1b.getText().toString().split("-")[0]);
            timeText2.setText(time1b.getText().toString().split("-")[1]);
            dateText.setText(date1b.getText().toString());
        });

        durationTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialogStarts=new TimePickerDialog(AddAriaActivity.this, android.R.style.Theme_Holo_Light_Dialog, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        if(minute<ONE_DIGIT&&hourOfDay<ONE_DIGIT)
                            durationTextView.setText(PADDING_ZERO+hourOfDay+":"+PADDING_ZERO+minute);
                        else if(minute<ONE_DIGIT)
                            durationTextView.setText(hourOfDay+":"+PADDING_ZERO+minute);
                        else if(hourOfDay<ONE_DIGIT)
                            durationTextView.setText(PADDING_ZERO+hourOfDay+":"+minute);
                        else
                            durationTextView.setText(hourOfDay+":"+minute);
                    }
                },23,59,true);
                timePickerDialogStarts.show();
            }
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
        SimpleDateFormat ft = new SimpleDateFormat("dd/MM/yyyy");
        String str = ft.format(new Date());
        LocalTime localTime = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            localTime = LocalTime.now();
        }
        int hour = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            hour = localTime.getHour();
        }
        int minute = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            minute = localTime.getMinute();
        }
        String hour_string=Integer.toString(hour);
        String minutes_string=Integer.toString(minute);
        if(hour<ONE_DIGIT)
            hour_string=PADDING_ZERO+hour_string;
        if(minute<ONE_DIGIT)
            minutes_string=PADDING_ZERO+minutes_string;


        dateText.setText(str);
        timeText.setText(hour_string+":"+minutes_string);
        timeText2.setText(hour_string+":"+minutes_string);
        Spinner spinner = header.findViewById(R.id.alertAria);
        ImageButton btnClose=findViewById(R.id.btnBack);
        btnClose.setOnClickListener(view->{
            onBackPressed();
        });

        ImageButton btnDone=findViewById(R.id.btnDone);
        btnDone.setOnClickListener(view->{
            phoneNumber=header.findViewById(R.id.phoneNumber);
            EditText title = header.findViewById(R.id.titleAria);
            Duration duration = parseDuration(durationTextView.getText().toString());
            adapter.updateDuration(duration);
            boolean flag=true;
            if(phoneNumber.getText().length()==LENGTH_PHONE && title.getText().toString().length()!=0 && meetingTimeList.size()!=0&&!adapter.isError()) {
                for(int i=0;i<phoneNumber.getText().length();i++)
                {
                    if((!isDigit(phoneNumber.getText().charAt(i)))||(i==0 && phoneNumber.getText().charAt(i)!='5')) {
                        flag = false;
                    }
                }
                if(flag==true)
                {
                    List<Integer> days = new ArrayList<>();
                    List<Integer> flags = new ArrayList<>();
                    List<String> times = new ArrayList<>();

                    for(int i=0;i<meetingTimeList.size();i++){
                        boolean flagAriaTimes = false;
                        DateTimeFormatter formatter3 = null;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            formatter3 = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                        }
                        LocalDate datecheck = null;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            datecheck = LocalDate.parse(meetingTimeList.get(i).getDateMeeting(), formatter3);
                        }
                        int dayOfWeekNumber = 0;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            dayOfWeekNumber = datecheck.getDayOfWeek().getValue();
                        }
                        if(dayOfWeekNumber==7)
                        {
                            dayOfWeekNumber=0;
                        }
                        for(int j=0;j<lstAriaTimes.size();j++){
                            if(meetingTimeList.get(i).getTimeMeeting().equals(lstAriaTimes.get(j).getTime().split("-")[0])&&meetingTimeList.get(i).getTimeMeeting2().equals(lstAriaTimes.get(j).getTime().split("-")[1])&&meetingTimeList.get(i).getDateMeeting().equals(lstAriaTimes.get(j).getDate())){
                                days.add(dayOfWeekNumber);
                                flags.add(1);
                                times.add(lstAriaTimes.get(j).getTime());
                                flagAriaTimes = true;
                            }
                        }
                        if(!flagAriaTimes){
                            days.add(dayOfWeekNumber);
                            flags.add(0);
                            times.add(meetingTimeList.get(i).getTimeMeeting()+"-"+meetingTimeList.get(i).getTimeMeeting2());
                        }
                    }
                    usersAPI.ariaTimes(days, times, flags, token);

                    EditText treatment=header.findViewById(R.id.treatment);

                    String treatmentStr;
                    if(treatment.getText().toString()!="")
                        treatmentStr=treatment.getText().toString();
                    else
                        treatmentStr="meeting";

                    EditText description = header.findViewById(R.id.desAria);
                    titleStr = title.getText().toString();
                    descriptionStr = description.getText().toString();
                    Spinner alert = header.findViewById(R.id.alertAria);
                    alertStr = (String) alert.getSelectedItem();

                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    String currentDate = sdf.format(new Date());
                    msg = "I want you to act as you are client and want to set a ";
                    msg=msg+treatmentStr+" in on of specific dates and time i will give you.\n"+
                            "you start to chat with the service provider right now.\n"+
                            "you need to chat and answer only like the client,don't answer me the service provider response, you will get it later\n"+
                            "do not write 'Client:', 'service provider:' in your response\n" +
                            "for example, you can start the chat with the service provider like: 'Hello! I would like to schedule an appointment for a ";
                    msg=msg+treatmentStr+" i'm available on ... at...'\n"+
                            "no matter what, pay attention that every response of you must be at maximum 159 characters include white space! you need to calculate the length of your response and send it just if it's less than 159 characters include white space!!\n"
                            +"for example: if your response is 'aaaa aa aaa' so the length of this response is 11\n"
                            +"you will provided the service provider's answer and your task is to set a ";
                    msg=msg+treatmentStr+" in one of the next times in this list:\n";

                    String treatment_duration=durationTextView.getText().toString();

                    for(int i=0;i<meetingTimeList.size();i++){
                        int j = i+1;
                        LocalTime time = null;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            time = LocalTime.parse(meetingTimeList.get(i).getTimeMeeting2());
                        }

                        String[] durationParts = treatment_duration.split(":");
                        int hours = Integer.parseInt(durationParts[0]);
                        int minutes = Integer.parseInt(durationParts[1]);

                        Duration duration2 = null;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            duration2 = Duration.ofHours(hours).plusMinutes(minutes);
                        }

                        // Subtract the duration from the time
                        LocalTime resultTime = null;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            resultTime = time.minus(duration2);
                        }

                        // Format the result as hh:mm
                        String result = resultTime.toString();
                        msg = msg + j + ". " + meetingTimeList.get(i).getDateMeeting() + " " + meetingTimeList.get(i).getTimeMeeting() + " - " + result +"\n";
                    }
                    msg = msg + "on the list above you have all the optional times for the meeting, ensure you check with the service provider all of those dates and their ranges (if there is some date that the service provider offer and it suit to one of the ranges, accept this date).\n"
                            + "if the service provider gives many options you need to check every option if the time for the treatment is in the range of some specific date and time in the list above \n" +
                            "you can set an appointment only with time that is in the list so before you write an answer check again if your answer has at least one relevant date and range at the list above, if not do not write it!!!! \n"+
                            " for example if the service provider write: 'i can on 18/08/2024 at 10:00 and 14:00' you need to check if this times 18/08/2024 at 10:00 and  18/08/2024 at 14:00 suits to the times in the list above if not ask for another dates that suits to the list above!!\n" +
                            "this is an example of how to check if time is suits for you: if  you available on 18/08/2024 at 16:20-23:30 than you can schedule on 18/08/2024 at 16:45, 17:30, 18:00, 22:07, 23:30 but not at 11:23, 16:18, 2:00 and any other time that is before the start time or after the end time!!"
                            +"If none of the above dates and their relevant ranges are available for the service provider,write \"Thank you, I will think about it. goodbye'\"\n" +
                            "if you succeed to set the appointment, write the start time and date of the appointment and write \"thank you, i will come. goodbye\"\n"
                            +"If the service provider give you options for a ";
                    msg=msg+" time, make sure it is one of the above dates and time, if it is not, ask for the dates and time above\n"
                            +"if the service provider tell you he is available on tomorrow/next day etc you can calculate this from today date:"+currentDate+"\n"
                            +"if the service provider ask for your name, your name is "+fullName+"\n"
                            +"pay attention that every response of you must be at maximum 159 characters include white space! you need to calculate the length of your response and send it just if it's less than 159 characters include white space!!\n"
                            +"for example: if your response is 'aaaa aa aaa' so the length of this response is 11\n"
                    +"the times in the conversion are according to israel times so don't use AM PM";


                    endTime=treatment_duration;

                    if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                        if(!(((String)spinner.getSelectedItem()).equals("None"))) {
                            requestPermissions(new String[]{
                                    Manifest.permission.POST_NOTIFICATIONS
                            }, PERMISSIONS_REQUEST_CODE_NOTIFICATIONS);
                        }
                        else {

                            String[] permissions2 = {Manifest.permission.READ_SMS, Manifest.permission.SEND_SMS, Manifest.permission.RECEIVE_SMS};
                            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED ||
                                    ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED ||  ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(this, permissions2, PERMISSIONS_REQUEST_CODE_SMS);

                            }
                            else{

                                showCustomPermissionDialog(token, titleStr, descriptionStr, DEFAULT_START_TIME, endTime, alertStr, DEFAULT_DATE, phoneNumber.getText().toString());
                            }

                        }
                    } else {
                        String[] permissions2 = {Manifest.permission.READ_SMS, Manifest.permission.SEND_SMS,  Manifest.permission.RECEIVE_SMS};
                        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED ||
                                ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED)  {
                            ActivityCompat.requestPermissions(this, permissions2, PERMISSIONS_REQUEST_CODE_SMS);
                        }
                        else{
                            showCustomPermissionDialog(token, titleStr, descriptionStr, DEFAULT_START_TIME, endTime, alertStr, DEFAULT_DATE, phoneNumber.getText().toString());
                        }

                    }


                }
                else {
                    phoneNumber.setText("");
                    Drawable rightDrawable = getResources().getDrawable(R.drawable.wrong); // Replace with your actual drawable resource
                    phoneNumber.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, rightDrawable, null);
                    phoneNumber.setHintTextColor(getResources().getColor(R.color.RedWarning));

                }
                LinearLayout layoutWrongDuration=header.findViewById(R.id.wrongDuration);
                layoutWrongDuration.setVisibility(View.INVISIBLE);
            }
            else
            {

                if(phoneNumber.getText().length()!=LENGTH_PHONE) {
                    phoneNumber.setText("");
                    ImageView errorIcon = header.findViewById(R.id.wrongPhone);
                    LinearLayout errorText = header.findViewById(R.id.wrongPhone2);
                    errorIcon.setVisibility(View.VISIBLE);
                    errorText.setVisibility(View.VISIBLE);

                }
                else{
                    ImageView errorIcon = header.findViewById(R.id.wrongPhone);
                    LinearLayout errorText = header.findViewById(R.id.wrongPhone2);
                    errorIcon.setVisibility(View.INVISIBLE);
                    errorText.setVisibility(View.INVISIBLE);
                }
                if(title.getText().toString().length()==0)
                {
                    ImageView errorIcon = header.findViewById(R.id.wrongTitle);
                    LinearLayout errorText = header.findViewById(R.id.wrongTitle2);
                    errorIcon.setVisibility(View.VISIBLE);
                    errorText.setVisibility(View.VISIBLE);
                }
                else{
                    ImageView errorIcon = header.findViewById(R.id.wrongTitle);
                    LinearLayout errorText = header.findViewById(R.id.wrongTitle2);
                    errorIcon.setVisibility(View.INVISIBLE);
                    errorText.setVisibility(View.INVISIBLE);
                }
                if(meetingTimeList.size()==0)
                {
                    LinearLayout errorText = header.findViewById(R.id.wrongTime2);
                    errorText.setVisibility(View.VISIBLE);
                    TextView wrongTime=findViewById(R.id.wrongTime_tv);
                    wrongTime.setText("enter at least one time you can");
                }
                else{
                    LinearLayout errorText = header.findViewById(R.id.wrongTime2);
                    errorText.setVisibility(View.INVISIBLE);

                }
                if(adapter.isError())
                {
                    LinearLayout layoutWrongDuration=header.findViewById(R.id.wrongDuration);
                    layoutWrongDuration.setVisibility(View.VISIBLE);
                }
                else {
                    LinearLayout layoutWrongDuration=header.findViewById(R.id.wrongDuration);
                    layoutWrongDuration.setVisibility(View.INVISIBLE);
                }


            }
        });


        String[] items = {"None", "hour before", "day before"};
        ArrayAdapter<String> AlertAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items);
        AlertAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(AlertAdapter);



        ImageButton addDate = header.findViewById(R.id.addDate);
        TimePicker time = header.findViewById(R.id.hourMin);
        TimePicker time2 = header.findViewById(R.id.hourMin2);
        DatePicker date = header.findViewById(R.id.DatePicker);
        time.setIs24HourView(true);
        time2.setIs24HourView(true);
        TextView time_tv=header.findViewById(R.id.time);
        TextView time_tv2=header.findViewById(R.id.time2);
        TextView date_tv=header.findViewById(R.id.date);
        LinearLayout timePicker1 = header.findViewById(R.id.timePicker1);
        LinearLayout timePicker2 = header.findViewById(R.id.timePicker2);
        date_tv.setOnClickListener(v -> {
            timePicker1.setVisibility(View.VISIBLE);
            timePicker2.setVisibility(View.GONE);

        });

        time_tv.setOnClickListener(v -> {
            timePicker2.setVisibility(View.VISIBLE);
            timePicker1.setVisibility(View.GONE);

        });
        time_tv2.setOnClickListener(v -> {
            timePicker2.setVisibility(View.VISIBLE);
            timePicker1.setVisibility(View.GONE);

        });
        time.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                String minute_string=Integer.toString(minute);
                String hour_string=Integer.toString(hourOfDay);
                if (minute<ONE_DIGIT){
                    minute_string=PADDING_ZERO+minute_string;

                }
                if(hourOfDay<ONE_DIGIT){
                    hour_string=PADDING_ZERO+hour_string;
                }
                timeText.setText(hour_string+":"+minute_string);

            }
        });
        time2.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                String minute_string=Integer.toString(minute);
                String hour_string=Integer.toString(hourOfDay);
                if (minute<ONE_DIGIT){
                    minute_string=PADDING_ZERO+minute_string;

                }
                if(hourOfDay<ONE_DIGIT){
                    hour_string=PADDING_ZERO+hour_string;
                }
                timeText2.setText(hour_string+":"+minute_string);

            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            date.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
                @Override
                public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    int month = monthOfYear+1;
                    String month_string=Integer.toString(month);
                    String day_string=Integer.toString(dayOfMonth);
                    if(month<10)
                    {
                        month_string=PADDING_ZERO+month_string;
                    }
                    if(dayOfMonth<10)
                    {
                        day_string=PADDING_ZERO+day_string;
                    }

                    dateText.setText(day_string+"/"+month_string+"/"+year);

                }
            });
        }


        addDate.setOnClickListener(v -> {
            LinearLayout errorText = header.findViewById(R.id.wrongTime2);
            errorText.setVisibility(View.INVISIBLE);
            timePicker1.setVisibility(View.GONE);
            timePicker2.setVisibility(View.GONE);
            TextView date2=findViewById(R.id.date);

            DateTimeFormatter formatter = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                formatter = DateTimeFormatter.ofPattern("HH:mm");
            }
            LocalTime timeparse= null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                timeparse = LocalTime.parse(timeText.getText().toString(), formatter);
            }

            LocalTime timeparse2= null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                timeparse2 = LocalTime.parse(timeText2.getText().toString(), formatter);
            }

            TimeMeeting t1=new TimeMeeting(date2.getText().toString(),timeText.getText().toString(),timeText2.getText().toString());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if(timeparse.isBefore(timeparse2)) {
                    Duration durationBetween = Duration.between(timeparse,timeparse2);
                    Duration specifiedDuration = parseDuration(durationTextView.getText().toString());
                    if (!(durationBetween.compareTo(specifiedDuration) > 0))
                    {
                        LinearLayout layoutWrongDuration=header.findViewById(R.id.wrongDuration);
                        layoutWrongDuration.setVisibility(View.VISIBLE);
                    }
                    else{
                        LinearLayout layoutWrongDuration=header.findViewById(R.id.wrongDuration);
                        layoutWrongDuration.setVisibility(View.INVISIBLE);

                        if(dateText.getText().toString().equals(date1.getText().toString())&&timeText.getText().toString().equals(time1.getText().toString().split("-")[0])&&timeText2.getText().toString().equals(time1.getText().toString().split("-")[1])){
                            if(countertimes<9){
                                countertimes += 1;
                                time1.setText(lstAriaTimes.get(countertimes).getTime());
                                date1.setText(lstAriaTimes.get(countertimes).getDate());
                            }
                        } else if (dateText.getText().toString().equals(date1a.getText().toString())&&timeText.getText().toString().equals(time1a.getText().toString().split("-")[0])&&timeText2.getText().toString().equals(time1a.getText().toString().split("-")[1])) {
                            if(countertimes<9){
                                countertimes += 1;
                                time1a.setText(lstAriaTimes.get(countertimes).getTime());
                                date1a.setText(lstAriaTimes.get(countertimes).getDate());

                            }
                        } else if (dateText.getText().toString().equals(date1b.getText().toString())&&timeText.getText().toString().equals(time1b.getText().toString().split("-")[0])&&timeText2.getText().toString().equals(time1b.getText().toString().split("-")[1])) {
                            if(countertimes<9){
                                countertimes += 1;
                                time1b.setText(lstAriaTimes.get(countertimes).getTime());
                                date1b.setText(lstAriaTimes.get(countertimes).getDate());
                            }
                        }

                        if (!meetingTimeList.contains(t1)) {
                            meetingTimeList.add(t1);
                            adapter.notifyDataSetChanged();
                        }
                    }
                }
                else{

                    TextView wrongTime=findViewById(R.id.wrongTime_tv);
                    wrongTime.setText("start time needs to be before end time");
                    errorText.setVisibility(View.VISIBLE);



                }
            }
        });


    }
    void callChatGptApi(String question,String sender){
        JSONArray list_messages=new JSONArray();
        JSONObject json_message=new JSONObject();


        try {
            json_message.put("role","user");
            json_message.put("content",question);

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        list_messages.put(json_message);
        JSONObject jsonBody=new JSONObject();
        try{
            jsonBody.put("model","gpt-4");
            jsonBody.put("messages",list_messages);
        }
        catch (JSONException e){
            e.printStackTrace();
        }
        RequestBody body=RequestBody.create(jsonBody.toString(),JSON);
        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/chat/completions")
                .header("Authorization","Bearer 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")
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if(response.isSuccessful()){
                    JSONObject jsonObject=null;
                    try{
                        jsonObject=new JSONObject(response.body().string());
                        JSONArray jsonArray=jsonObject.getJSONArray("choices");
                        JSONObject json_array=jsonArray.getJSONObject(0);
                        JSONObject json_msg=json_array.getJSONObject("message");
                        String result=json_msg.getString("content");
                        String response_chatgpt=result.trim();

                        ChatsAPI chatsAPI = new ChatsAPI();
                        token = getIntent().getExtras().getString("token");

                        LocalTime currentTime = null;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                            currentTime = LocalTime.now();
                        }

                        int hour = 0;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                            hour = currentTime.getHour();
                        }
                        int minute = 0;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                            minute = currentTime.getMinute();
                        }
                        String hour_string = Integer.toString(hour);
                        String minutes_string = Integer.toString(minute);
                        if (hour < ONE_DIGIT)
                            hour_string = PADDING_ZERO+ hour_string;
                        if (minute < ONE_DIGIT)
                            minutes_string = PADDING_ZERO + minutes_string;
                        String time = hour_string + ":" + minutes_string;

                        chatsAPI.addChat(id, phone, time, msg, result.trim(), token);

                        SmsManager smsManager = SmsManager.getDefault();
                        smsManager.sendTextMessage(phone, null, response_chatgpt, null, null);
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }
                else{
                }

            }
        });


    }
    private void showCustomPermissionDialog(String token, String titleStr, String descriptionStr, String start, String end, String alertStr, String date, String phoneNumber) {
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
        phone="+972"+phoneNumber;
        Intent intent = new Intent(this, CalendarActivity.class);
        intent.putExtra("token",token);
        intent.putExtra("username",username);
        intent.putExtra("fullName",fullName);
        EventsAPI eventsAPI = new EventsAPI();
        btnPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (ContextCompat.checkSelfPermission(v.getContext(), Manifest.permission.WRITE_CALENDAR) == PackageManager.PERMISSION_GRANTED){
                    String idEvent = eventsAPI.addEvent(token, titleStr, descriptionStr, start, end, alertStr, date, WITH_GOOGLE_CALENDAR);
                    id = Integer.valueOf(idEvent);
                    callChatGptApi(msg,phone);
                    startActivity(intent);
                }else {
                    requestCalendarPermissions(token);
                }

            }
        });

        btnNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                String idEvent = eventsAPI.addEvent(token, titleStr, descriptionStr, start, end, alertStr, date, WITHOUT_GOOGLE_CALENDAR);
                id = Integer.valueOf(idEvent);
                callChatGptApi(msg,phone);
                startActivity(intent);

            }
        });
        dialog.show();
    }
    private static Duration parseDuration(String durationString) {
        String[] parts = durationString.split(":");
        int hours = Integer.parseInt(parts[0]);
        int minutes = Integer.parseInt(parts[1]);
        return Duration.ofHours(hours).plusMinutes(minutes);
    }
    private void requestCalendarPermissions(String token) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR},
                    PERMISSIONS_REQUEST_CODE_CALENDAR);
        }
        else{
            EventsAPI eventsAPI=new EventsAPI();
            String idEvent = eventsAPI.addEvent(token, titleStr, descriptionStr, DEFAULT_START_TIME, endTime, alertStr, DEFAULT_DATE, WITH_GOOGLE_CALENDAR);
            id = Integer.valueOf(idEvent);
            callChatGptApi(msg,phone);
            Intent intent = new Intent(this, CalendarActivity.class);
            intent.putExtra("token", token);
            intent.putExtra("username",username);
            intent.putExtra("fullName",fullName);
            startActivity(intent);
        }
    }
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_CODE_NOTIFICATIONS) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                String[] permissions2 = {Manifest.permission.READ_SMS, Manifest.permission.SEND_SMS,Manifest.permission.RECEIVE_SMS};


                if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED ||
                        ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED)  {
                    ActivityCompat.requestPermissions(this, permissions2, PERMISSIONS_REQUEST_CODE_SMS);
                }
                else{
                    showCustomPermissionDialog(token, titleStr, descriptionStr, DEFAULT_START_TIME, endTime, alertStr, DEFAULT_DATE, phoneNumber.getText().toString());
                }



            } else {
                alertStr="None";
                Spinner spinner=header.findViewById(R.id.alertAria);
                spinner.setSelection(0);
                TextView alertmsgTextView=findViewById(R.id.alertmsg);
                alertmsgTextView.setVisibility(View.VISIBLE);


            }
        }
        if(requestCode == PERMISSIONS_REQUEST_CODE_SMS)
        {
            boolean allPermissionsGranted = true;
            for (int grantResult : grantResults) {
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    allPermissionsGranted = false;
                    break;
                }
            }

            if (allPermissionsGranted) {
                Toast.makeText(this, "Permissions granted!", Toast.LENGTH_SHORT).show();
                showCustomPermissionDialog(token, titleStr, descriptionStr, DEFAULT_START_TIME, endTime, alertStr, DEFAULT_DATE, phoneNumber.getText().toString());


            } else {
                Toast.makeText(this, "Permissions denied! aria cannot make you events", Toast.LENGTH_SHORT).show();

            }
        }
        if(requestCode==PERMISSIONS_REQUEST_CODE_CALENDAR)
        {
            EventsAPI eventsAPI=new EventsAPI();
            Intent intent = new Intent(this, CalendarActivity.class);
            intent.putExtra("token",token);
            intent.putExtra("username",username);
            intent.putExtra("fullName",fullName);
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                String idEvent = eventsAPI.addEvent(token, titleStr, descriptionStr, DEFAULT_START_TIME, endTime, alertStr, DEFAULT_DATE, WITH_GOOGLE_CALENDAR);
                id = Integer.valueOf(idEvent);
                callChatGptApi(msg,phone);
                startActivity(intent);

            }
            else{
                Toast.makeText(this, "Can't add to google calendar", Toast.LENGTH_SHORT).show();
                String idEvent = eventsAPI.addEvent(token, titleStr, descriptionStr, DEFAULT_START_TIME, endTime, alertStr, DEFAULT_DATE, WITHOUT_GOOGLE_CALENDAR);
                id = Integer.valueOf(idEvent);
                callChatGptApi(msg,phone);
                startActivity(intent);


            }
        }

    }

}