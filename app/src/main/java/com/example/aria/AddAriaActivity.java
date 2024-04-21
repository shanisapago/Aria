package com.example.aria;
import static java.lang.Character.isDigit;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
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
import java.time.LocalTime;
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
        setContentView(R.layout.add_aria);
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

        TextView timeText=findViewById(R.id.time);
        TextView dateText=findViewById(R.id.date);
        dateText.setText(str);
        timeText.setText(hour_string+":"+minutes_string);

        Spinner spinner = findViewById(R.id.alertAria);

        ImageButton btnClose=findViewById(R.id.btnClose);
        btnClose.setOnClickListener(view->{
            Intent intent=new Intent(this,CalendarActivity.class);
            startActivity(intent);
        });

        ImageButton btnDone=findViewById(R.id.btnDone);
        btnDone.setOnClickListener(view->{

            EditText phoneNumber=findViewById(R.id.phoneNumber);
            boolean flag=true;
            if(phoneNumber.getText().length()==10) {
                for(int i=0;i<phoneNumber.getText().length();i++)
                {
                    if((!isDigit(phoneNumber.getText().charAt(i)))||(i==0 && phoneNumber.getText().charAt(i)!='0')||(i==1 && phoneNumber.getText().charAt(i)!='5')) {
                        flag = false;
                    }
                }
                if(flag==true)
                {
                    EditText title = findViewById(R.id.titleAria);
                    EditText description = findViewById(R.id.desAria);
                    String titleStr = title.getText().toString();
                    String descriptionStr = description.getText().toString();
                    Spinner alert = findViewById(R.id.alertAria);
                    String alertStr = (String) alert.getSelectedItem();

                    msg = "hi, i need you to chat with me where you will be the client and i will represent a business.\n you need to talk only like the client.\n"
                            +"when the conversion is over always finish with 'goodbye'\n"
                            +"you want to set an appointment in one of the next dates:\n";
                    for(int i=0;i<meetingTimeList.size();i++){
                        int j = i+1;
                        msg = msg + j + ". " + meetingTimeList.get(i).getDateMeeting() + " " + meetingTimeList.get(i).getTimeMeeting() + "\n";
                        System.out.println(meetingTimeList.get(i).getDateMeeting());
                        System.out.println(meetingTimeList.get(i).getTimeMeeting());
                    }
                    msg = msg + "if none of this dates is available, tell me: 'thank you, i will think about it'\n and finish the conversion";
                    System.out.println("msg");
                    System.out.println(msg);
                    String token = getIntent().getExtras().getString("token");
                    String idEvent = eventsAPI.addEvent(token, titleStr, descriptionStr, "00:01", "00:02", alertStr, "01/01/2024");

                    id = Integer.valueOf(idEvent);

                    phone = phoneNumber.getText().toString();
                    callChatGptApi(msg);
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
                phoneNumber.setText("");
                phoneNumber.setHint("wrong format!");
                phoneNumber.setHintTextColor(getResources().getColor(R.color.RedWarning));
                Drawable rightDrawable = getResources().getDrawable(R.drawable.wrong); // Replace with your actual drawable resource
                phoneNumber.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, rightDrawable, null);

            }
        });


        String[] items = {"None", "hour before", "day before"};
        ArrayAdapter<String> AlertAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items);
        AlertAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(AlertAdapter);

        ListView lstTimeMeeting=findViewById(R.id.lstTimeMeeting);
        MeetingTimeAdapter adapter=new MeetingTimeAdapter(meetingTimeList);
        lstTimeMeeting.setAdapter(adapter);

          ImageButton addDate = findViewById(R.id.addDate);

        TimePicker time = findViewById(R.id.hourMin);
        DatePicker date = findViewById(R.id.DatePicker);
        time.setIs24HourView(true);
        LinearLayout chooseDate = findViewById(R.id.chooseDate);
        LinearLayout timePicker = findViewById(R.id.timePicker);
        chooseDate.setOnClickListener(v -> {
            timePicker.setVisibility(View.VISIBLE);
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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            date.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
                @Override
                public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    int month = monthOfYear+1;
                    String month_string=Integer.toString(month);
                    String day_string=Integer.toString(dayOfMonth);
                    if(monthOfYear<10)
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
            timePicker.setVisibility(View.GONE);
            TextView date2=findViewById(R.id.date);
            TextView time2=findViewById(R.id.time);

            System.out.println(date2);
            TimeMeeting t1=new TimeMeeting(date2.getText().toString(),time2.getText().toString());
            if(!meetingTimeList.contains(t1)) {
                meetingTimeList.add(t1);
                adapter.notifyDataSetChanged();
            }
        });

    }

    void callChatGptApi(String question){
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
                        System.out.println(result.trim());

                        ChatsAPI chatsAPI = new ChatsAPI();
                        String token = getIntent().getExtras().getString("token");

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
                        String time = hour + ":" + minute;

                        chatsAPI.addChat(id, phone, time, msg, result.trim(), token);

                    }catch (JSONException e){
                        System.out.println("failed "+e.getMessage());
                        e.printStackTrace();
                    }
                }
                else{
                    System.out.println("failed "+response.body().toString());
                    System.out.println(response.body().string());
                }

            }
        });
    }
}

