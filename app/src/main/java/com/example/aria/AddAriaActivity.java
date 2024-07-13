package com.example.aria;
import static java.lang.Character.isDigit;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
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
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.aria.RetroFitClasses.ChatsAPI;
import com.example.aria.RetroFitClasses.EventsAPI;
import com.example.aria.adapters.MeetingTimeAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
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

    OkHttpClient client = new OkHttpClient();
    int id;
    String phone;
    String msg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        List<TimeMeeting> meetingTimeList=new ArrayList<>();
        EventsAPI eventsAPI = new EventsAPI();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_aria3);

        String username = getIntent().getExtras().getString("username");

        ListView lstTimeMeeting=findViewById(R.id.lstTimeMeeting);


        //MeetingTimeAdapter2 adapter=new MeetingTimeAdapter2(meetingTimeList);
        //lstTimeMeeting.setLayoutManager(new LinearLayoutManager(this));

        //LayoutInflater inflater = getLayoutInflater();
        //header = (ViewGroup) inflater.inflate(R.layout.header, lstTimeMeeting, false);



        LayoutInflater inflater = getLayoutInflater();
        ViewGroup header = (ViewGroup) inflater.inflate(R.layout.header, lstTimeMeeting, false);
        lstTimeMeeting.addHeaderView(header, null, false);

        TextView durationTextView=header.findViewById(R.id.duration);
        Duration d = parseDuration(durationTextView.getText().toString());
        MeetingTimeAdapter adapter=new MeetingTimeAdapter(meetingTimeList,d);

        lstTimeMeeting.setAdapter(adapter);



        durationTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialogStarts=new TimePickerDialog(AddAriaActivity.this, android.R.style.Theme_Holo_Light_Dialog, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        if(minute<10&&hourOfDay<10)
                            durationTextView.setText("0"+hourOfDay+":"+"0"+minute);
                        else if(minute<10)
                            durationTextView.setText(hourOfDay+":"+"0"+minute);
                        else if(hourOfDay<10)
                            durationTextView.setText("0"+hourOfDay+":"+minute);
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
            startActivity(intent);
        });

        SimpleDateFormat ft = new SimpleDateFormat("dd/MM/yyyy");
        String str = ft.format(new Date());
        LocalTime localTime = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            localTime = LocalTime.now();
        }
        int hour = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            hour = localTime.getHour();
        }
        int minute = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            minute = localTime.getMinute();
        }
        String hour_string=Integer.toString(hour);
        String minutes_string=Integer.toString(minute);
        if(hour<10)
            hour_string="0"+hour_string;
        if(minute<10)
            minutes_string="0"+minutes_string;

        TextView timeText=header.findViewById(R.id.time);
        TextView timeText2=header.findViewById(R.id.time2);
        TextView dateText=header.findViewById(R.id.date);
        dateText.setText(str);
        timeText.setText(hour_string+":"+minutes_string);
        timeText2.setText(hour_string+":"+minutes_string);



        /*Switch toggleButton=findViewById(R.id.toggleButton);
        EditText treatmentTime=findViewById(R.id.treatmentTime);

        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // Toggle button is switched to "Yes"
                    treatmentTime.setVisibility(View.VISIBLE);
                } else {
                    // Toggle button is switched to "No"
                    treatmentTime.setText("");
                    treatmentTime.setVisibility(View.GONE);
                }
            }
        });
*/


        Spinner spinner = header.findViewById(R.id.alertAria);


        ImageButton btnClose=findViewById(R.id.btnBack);
        btnClose.setOnClickListener(view->{
            onBackPressed();
        });

        ImageButton btnDone=findViewById(R.id.btnDone);
        btnDone.setOnClickListener(view->{
            System.out.println("in done");

            EditText phoneNumber=header.findViewById(R.id.phoneNumber);
            EditText title = header.findViewById(R.id.titleAria);


            Duration duration = parseDuration(durationTextView.getText().toString());
            adapter.updateDuration(duration);


            System.out.println("after edit text");

            boolean flag=true;
            if(phoneNumber.getText().length()==9 && title.getText().toString().length()!=0 && meetingTimeList.size()!=0&&adapter.isError()) {
                for(int i=0;i<phoneNumber.getText().length();i++)
                {
                    if((!isDigit(phoneNumber.getText().charAt(i)))||(i==0 && phoneNumber.getText().charAt(i)!='5')) {
                        flag = false;
                    }
                }
                if(flag==true)
                {
                    System.out.println("in flag==true");
                    EditText treatment=header.findViewById(R.id.treatment);

                    String treatmentStr;
                    if(treatment.getText().toString()!="")
                        treatmentStr=treatment.getText().toString();
                    else
                        treatmentStr="meeting";

                    EditText description = header.findViewById(R.id.desAria);
                    String titleStr = title.getText().toString();
                    String descriptionStr = description.getText().toString();
                    Spinner alert = header.findViewById(R.id.alertAria);
                    String alertStr = (String) alert.getSelectedItem();

                    msg = "I want you to act as you are client and want to set a ";
                    msg=msg+treatmentStr+" in on of specific dates and time i will give you.\n"+
                            "you start to chat with the service provider right now.\n"+
                            "you need to chat and answer only like the client,don't answer me the service provider response, you will get it later\n"+
                            "do not write 'Client:' in your response\n" +
                            "for example, you can start the chat with the service provider like: 'Hello! I would like to schedule an appointment for a ";
                    msg=msg+treatmentStr+" i'm available on ... at...'\n"+
                            "you will provided the service provider's answer and your task is to set a ";
                    msg=msg+treatmentStr+" in one of the next dates:\n";

                    for(int i=0;i<meetingTimeList.size();i++){
                        int j = i+1;
                        msg = msg + j + ". " + meetingTimeList.get(i).getDateMeeting() + " " + meetingTimeList.get(i).getTimeMeeting() + "\n";
                        System.out.println(meetingTimeList.get(i).getDateMeeting());
                        System.out.println(meetingTimeList.get(i).getTimeMeeting());
                    }
                    msg = msg + " ensure you check with the service provider all of those dates.\n"

                            +"If none of the above dates are available for the service provider,write \"Thank you, I will think about it. goodbye'\"\n" +
                            "if you succeed to set the appointment, write \"thank you, i will come. goodbye\"\n"
                            +"If the service provider give you options for a ";
                    msg=msg+" time, make sure it is one of the above dates and time, if it is not, ask for the dates and time above\n";
                    String treatment_duration=durationTextView.getText().toString();

                    String endTime=treatment_duration;
                    System.out.println("duration treatment");



                    System.out.println("msg");
                    System.out.println(msg);
                    String token = getIntent().getExtras().getString("token");
                    System.out.println("token");
                    System.out.println(token);
                    String idEvent = eventsAPI.addEvent(token, titleStr, descriptionStr, "00:01", endTime, alertStr, "01/01/2024");
                    System.out.println(idEvent);
                    id = Integer.valueOf(idEvent);
                    phone="+972"+phoneNumber.getText().toString();
                    System.out.println("phone");
                    System.out.println(phone);


                    callChatGptApi(msg,phone);
                    Intent intent = new Intent(this, CalendarActivity.class);
                    intent.putExtra("token",token);
                    startActivity(intent);
                }
                else {
                    phoneNumber.setText("");
                    phoneNumber.setHint("wrong format! ");
                    Drawable rightDrawable = getResources().getDrawable(R.drawable.wrong); // Replace with your actual drawable resource
                    phoneNumber.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, rightDrawable, null);
                    phoneNumber.setHintTextColor(getResources().getColor(R.color.RedWarning));

                }
            }
            else
            {

                if(phoneNumber.getText().length()!=9) {
                    phoneNumber.setText("");
                    phoneNumber.setHint("wrong format!");
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
                    System.out.println("title is empty");
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
                    System.out.println("size 0");
                    ImageView errorIcon = header.findViewById(R.id.wrongTime);
                    LinearLayout errorText = header.findViewById(R.id.wrongTime2);
                    errorIcon.setVisibility(View.VISIBLE);
                    errorText.setVisibility(View.VISIBLE);
                    TextView wrongTime=findViewById(R.id.wrongTime_tv);
                    wrongTime.setText("enter at least one time you can");
                }
                else{
                    ImageView errorIcon = header.findViewById(R.id.wrongTime);
                    LinearLayout errorText = header.findViewById(R.id.wrongTime2);
                    errorIcon.setVisibility(View.INVISIBLE);
                    errorText.setVisibility(View.INVISIBLE);

                }

            }
        });


        String[] items = {"None", "hour before", "day before"};
        ArrayAdapter<String> AlertAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items);
        AlertAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(AlertAdapter);


        //RecyclerView lstTimeMeeting=findViewById(R.id.lstTimeMeeting);
//        MeetingTimeAdapter adapter=new MeetingTimeAdapter(meetingTimeList);
//        //MeetingTimeAdapter2 adapter=new MeetingTimeAdapter2(meetingTimeList);
//        //lstTimeMeeting.setLayoutManager(new LinearLayoutManager(this));
//        lstTimeMeeting.setAdapter(adapter);
//        //LayoutInflater inflater = getLayoutInflater();
//        header = (ViewGroup) inflater.inflate(R.layout.header, lstTimeMeeting, false);
//        lstTimeMeeting.addHeaderView(header, null, false);


        ImageButton addDate = header.findViewById(R.id.addDate);
        TimePicker time = header.findViewById(R.id.hourMin);
        TimePicker time2 = header.findViewById(R.id.hourMin2);
        DatePicker date = header.findViewById(R.id.DatePicker);
        time.setIs24HourView(true);
        time2.setIs24HourView(true);
        //LinearLayout chooseDate = header.findViewById(R.id.chooseDate);
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
                if (minute<10){
                    System.out.println("less than 10");
                    minute_string="0"+minute_string;

                }
                if(hourOfDay<10){
                    hour_string="0"+hour_string;
                }
                timeText.setText(hour_string+":"+minute_string);

            }
        });
        time2.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                String minute_string=Integer.toString(minute);
                String hour_string=Integer.toString(hourOfDay);
                if (minute<10){
                    System.out.println("less than 10");
                    minute_string="0"+minute_string;

                }
                if(hourOfDay<10){
                    hour_string="0"+hour_string;
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
                        month_string="0"+month_string;
                    }
                    if(dayOfMonth<10)
                    {
                        day_string="0"+day_string;
                    }

                    dateText.setText(day_string+"/"+month_string+"/"+year);

                }
            });
        }


        addDate.setOnClickListener(v -> {
            ImageView errorIcon = header.findViewById(R.id.wrongTime);
            LinearLayout errorText = header.findViewById(R.id.wrongTime2);
            errorIcon.setVisibility(View.INVISIBLE);
            errorText.setVisibility(View.INVISIBLE);
            timePicker1.setVisibility(View.GONE);
            timePicker2.setVisibility(View.GONE);
            TextView date2=findViewById(R.id.date);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            LocalTime timeparse=LocalTime.parse(timeText.getText().toString(), formatter);

            LocalTime timeparse2=LocalTime.parse(timeText2.getText().toString(), formatter);



            System.out.println(date2);
            System.out.println(timeText.getText().toString());
            System.out.println(timeText2.getText().toString());
            TimeMeeting t1=new TimeMeeting(date2.getText().toString(),timeText.getText().toString(),timeText2.getText().toString());
            System.out.println(t1.getTimeMeeting());
            System.out.println(t1.getTimeMeeting2());
            if(timeparse.isBefore(timeparse2)) {
                Duration durationBetween = Duration.between(timeparse,timeparse2);
                Duration specifiedDuration = parseDuration(durationTextView.getText().toString());
                System.out.println("Duration between timeParse1 and timeParse2: " + durationBetween);
                System.out.println("Specified duration: " + specifiedDuration);
                if (!(durationBetween.compareTo(specifiedDuration) > 0))
                {
                    errorText.setVisibility(View.VISIBLE);
                    System.out.println("bigger ");
                    errorText.setVisibility(View.VISIBLE);
                    TextView wrongTime=findViewById(R.id.wrongTime_tv);
                    wrongTime.setText("duration time need to be in the range of the start and the end time");
                }
                else{
                errorText.setVisibility(View.INVISIBLE);

                if (!meetingTimeList.contains(t1)) {
                    meetingTimeList.add(t1);
                    adapter.notifyDataSetChanged();
                }
                }
            }
            else{
                ImageView errorIconTime = header.findViewById(R.id.wrongTime);
                errorIconTime.setVisibility(View.VISIBLE);

                TextView wrongTime=findViewById(R.id.wrongTime_tv);
                wrongTime.setText("start time needs to be before end time");
                errorText.setVisibility(View.VISIBLE);



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
            jsonBody.put("model","gpt-3.5-turbo-16k");
            //jsonBody.put("prompt",question);
            jsonBody.put("messages",list_messages);
            //jsonBody.put("max_tokens",4000);
            //jsonBody.put("temperature",0);
        }
        catch (JSONException e){
            System.out.println("shani");
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
                System.out.println("shani");
                System.out.println("failed "+e.getMessage());
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
                        System.out.println(response_chatgpt);

                        ChatsAPI chatsAPI = new ChatsAPI();
                        String token = getIntent().getExtras().getString("token");

                        LocalTime currentTime = null;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                            currentTime = LocalTime.now();
                        }

                        // Extract hour and minute components
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
                        if (hour < 10)
                            hour_string = "0" + hour_string;
                        if (minute < 10)
                            minutes_string = "0" + minutes_string;
                        String time = hour_string + ":" + minutes_string;

                        chatsAPI.addChat(id, phone, time, msg, result.trim(), token);

                        System.out.println("shani");
                        System.out.println("result");
                        //SmsManager smsManager = SmsManager.getDefault();
                        //smsManager.sendTextMessage("+972549409957", null, response_chatgpt, null, null);
                    }catch (JSONException e){
                        System.out.println("shani");
                        System.out.println("failed 3 "+e.getMessage());
                        e.printStackTrace();
                    }
                }
                else{
                    System.out.println("shani");
                    System.out.println("failed 2 "+response.body().toString());
                    System.out.println(response.body().string());
                }

            }
        });


    }
    private static Duration parseDuration(String durationString) {
        String[] parts = durationString.split(":");
        int hours = Integer.parseInt(parts[0]);
        int minutes = Integer.parseInt(parts[1]);
        return Duration.ofHours(hours).plusMinutes(minutes);
    }
}