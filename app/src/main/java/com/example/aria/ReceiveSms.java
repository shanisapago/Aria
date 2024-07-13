package com.example.aria;
import static com.example.aria.Login.JSON;

import android.Manifest;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.example.aria.RetroFitClasses.ChatsAPI;
import com.example.aria.RetroFitClasses.EventsAPI;
import com.example.aria.RetroFitClasses.NewEvent;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.TimeZone;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Callback;
import okhttp3.Response;


public class ReceiveSms extends BroadcastReceiver {
    OkHttpClient client = new OkHttpClient();
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 123;
    private static final int PERMISSIONS_REQUEST_CODE_CALENDAR = 100;
    private static final int NOTIFICATION_ID = 1;
    String sender, token;
    String response_chatgpt;


    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "New SMS", Toast.LENGTH_LONG).show();
        System.out.println("new sms");
        sendNotification(context, sender, "title");
        SharedPreferences sharedPreferences = context.getSharedPreferences("shp", Context.MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");
        //System.out.println("token");
        //System.out.println(token);
        //System.out.println("New SMS");
        //System.out.println(this.num);
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
        if (hour < 10)
            hour_string = "0" + hour_string;
        if (minute < 10)
            minutes_string = "0" + minutes_string;

        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            Object[] pdus = (Object[]) bundle.get("pdus");
            if (pdus != null) {
                for (Object pdu : pdus) {
                    SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdu);
                    sender = smsMessage.getDisplayOriginatingAddress();
                    String messageBody = smsMessage.getMessageBody();
                    System.out.println("New SMS received from: " + sender + "\nMessage: " + messageBody);

                    JsonObject response_of_sms_json = new JsonObject();
                    response_of_sms_json.addProperty("role", "user");
                    response_of_sms_json.addProperty("content", messageBody);

                    ChatsAPI chatsAPI = new ChatsAPI();
                    JsonObject result = chatsAPI.addMessage(sender, token, response_of_sms_json, hour_string + ":" + minutes_string); //before if....
                    String id = result.get("id").getAsString();

                    System.out.println("idddd");
                    System.out.println(id);
                    int id_int = Integer.parseInt(id);
                    if (id_int != -1) {
                        JsonElement array = result.get("array");
                        JSONArray list_all_messages = new JSONArray();

                        array.getAsJsonArray().forEach(element -> {
                            JsonObject jsonObject = element.getAsJsonObject();
                            JSONObject jsonItem = new JSONObject();

                            try {
                                jsonItem.put("role", jsonObject.get("role").getAsString());
                                jsonItem.put("content", jsonObject.get("content").getAsString());
                                list_all_messages.put(jsonItem);
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }

                        });
                        JSONObject jsonBody = new JSONObject();
                        try {
                            jsonBody.put("model", "gpt-3.5-turbo-16k");
                            jsonBody.put("messages", list_all_messages);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        RequestBody body = RequestBody.create(jsonBody.toString(), JSON);
                        Request request = new Request.Builder()
                                .url("https://api.openai.com/v1/chat/completions")
                                .header("Authorization", "Bearer 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")
                                .post(body)
                                .build();
                        client.newCall(request).enqueue(new Callback() {
                            @Override
                            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                                System.out.println("failed " + e.getMessage());
                            }

                            @Override
                            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                                if (response.isSuccessful()) {
                                    JSONObject jsonObject = null;
                                    try {
                                        jsonObject = new JSONObject(response.body().string());
                                        JSONArray jsonArray = jsonObject.getJSONArray("choices");
                                        JSONObject json_array = jsonArray.getJSONObject(0);
                                        JSONObject json_msg = json_array.getJSONObject("message");
                                        String result = json_msg.getString("content");
                                        System.out.println(result.trim());
                                        System.out.println("shani");
                                        System.out.println("result");
                                        response_chatgpt = result.trim();
                                        JSONObject json_response_chat = new JSONObject();
                                        json_response_chat.put("role", "system");
                                        json_response_chat.put("content", response_chatgpt);

                                        if (response_chatgpt.toLowerCase().contains("goodbye")) {
                                            SmsManager smsManager = SmsManager.getDefault();
                                            System.out.println("the phone that work "+"+972549409957");
                                            System.out.println("phone i put now "+sender);
                                            smsManager.sendTextMessage(sender, null, response_chatgpt, null, null);
                                            JsonArray ja = new JsonArray();
                                            JSONObject json_message = new JSONObject();
                                            JsonObject json_message2 = new JsonObject();
                                            list_all_messages.put(json_response_chat);

                                            try {
                                                System.out.println("1");

                                                json_message.put("role", "user");
                                                json_message.put("content", "If you succeeded in setting an appointment, please write the date and time in the format 'yes dd/mm/yyyy hh:mm'. If you didn't succeed in setting an appointment, please write 'no'.");
                                                list_all_messages.put(json_message);
                                            } catch (JSONException e) {
                                                throw new RuntimeException(e);
                                            }
                                            JSONObject jsonBody = new JSONObject();
                                            try {
                                                jsonBody.put("model", "gpt-3.5-turbo-16k");
                                                jsonBody.put("messages", list_all_messages);
                                            } catch (JSONException e) {
                                                System.out.println("shani");
                                                e.printStackTrace();
                                            }
                                            RequestBody body = RequestBody.create(jsonBody.toString(), JSON);
                                            Request request = new Request.Builder()
                                                    .url("https://api.openai.com/v1/chat/completions")
                                                    .header("Authorization", "Bearer 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")
                                                    .post(body)
                                                    .build();
                                            client.newCall(request).enqueue(new Callback() {
                                                @Override
                                                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                                                    System.out.println("failed " + e.getMessage());
                                                }

                                                @Override
                                                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                                                    if (response.isSuccessful()) {
                                                        JSONObject jsonObject = null;
                                                        try {
                                                            jsonObject = new JSONObject(response.body().string());
                                                            JSONArray jsonArray = jsonObject.getJSONArray("choices");
                                                            JSONObject json_array = jsonArray.getJSONObject(0);
                                                            JSONObject json_msg = json_array.getJSONObject("message");
                                                            String result = json_msg.getString("content");
                                                            System.out.println(result.trim());
                                                            System.out.println("shani");
                                                            System.out.println("result");
                                                            String response_chatgpt = result.trim();

                                                            if (!(response_chatgpt.equalsIgnoreCase("no")) && !(response_chatgpt.equalsIgnoreCase("no."))) //check if no is enough or No
                                                            {
                                                                int index_time = response_chatgpt.indexOf(":");
                                                                String start_time = response_chatgpt.substring(index_time - 2, index_time + 3);
                                                                int index_first_slash = response_chatgpt.indexOf("/");
                                                                String date = response_chatgpt.substring(index_first_slash - 2, index_first_slash + 8);


                                                                EventsAPI eventsAPI = new EventsAPI();
                                                                NewEvent ariaEvent = eventsAPI.updateAriaResult(id_int,start_time,date,token);
                                                                if (ariaEvent.getFlag().equals("1")){
                                                                    int h1 = Integer.parseInt(start_time.substring(0, start_time.indexOf(':')));
                                                                    int m1 = Integer.parseInt(start_time.substring(start_time.indexOf(':') + 1, start_time.length()));
                                                                    int h2 = Integer.parseInt(ariaEvent.getEnd().substring(0, ariaEvent.getEnd().indexOf(':')));
                                                                    int m2 = Integer.parseInt(ariaEvent.getEnd().substring(ariaEvent.getEnd().indexOf(':') + 1, ariaEvent.getEnd().length()));
                                                                    int index_first_slash2 = date.indexOf("/");
                                                                    String day = date.substring(index_first_slash2 - 2, index_first_slash2);
                                                                    String month = date.substring(index_first_slash2 + 1, index_first_slash2 + 3);
                                                                    String year = date.substring(index_first_slash2 + 4, date.length());
                                                                    int dayInt = Integer.valueOf(day);
                                                                    int monthInt = Integer.valueOf(month);
                                                                    int yearInt = Integer.valueOf(year);
                                                                    monthInt = monthInt - 1;
                                                                    addEventToCalendar(context, id, ariaEvent.getTitle(), ariaEvent.getDescription(), start_time, ariaEvent.getEnd(), monthInt, yearInt, dayInt, h1, m1, h2, m2);
                                                                }


                                                            } else {
                                                                EventsAPI eventsAPI=new EventsAPI();
                                                                eventsAPI.deleteEventById(id_int,token);

                                                            }
                                                        } catch (JSONException e) {
                                                            System.out.println("failed " + e.getMessage());
                                                            e.printStackTrace();
                                                        }
                                                    } else {
                                                        System.out.println("failed " + response.body().toString());
                                                        System.out.println(response.body().string());


                                                    }
                                                }
                                            });
                                        } else {
                                            JsonObject json_response_chat_conv = JSONObjectToJsonObjectConverter.convertToGsonJsonObject(json_response_chat);
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
                                            if (hour < 10)
                                                hour_string = "0" + hour_string;
                                            if (minute < 10)
                                                minutes_string = "0" + minutes_string;

                                            chatsAPI.addMessage(sender, token, json_response_chat_conv, hour_string + ":" + minutes_string);
                                            SmsManager smsManager = SmsManager.getDefault();
                                            smsManager.sendTextMessage(sender, null, response_chatgpt, null, null);

                                        }

                                    } catch (JSONException e) {
                                        System.out.println("failed " + e.getMessage());
                                        e.printStackTrace();
                                    }
                                } else {
                                    System.out.println("failed " + response.body().toString());
                                    System.out.println(response.body().string());


                                }
                            }
                        });


                    }
                }
            }
        }
    }
    private void sendNotification(Context context, String sender, String message) {
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        String CHANNEL_NAME="Aria";
        String CHANNEL_ID="3";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.icon)
                .setContentTitle("Aria set a new appointment!")
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }
    private void addEventToCalendar(Context context, String id, String title, String des, String start, String end, int monthInt, int yearInt, int dayInt, int h1, int m1, int h2, int m2) {
        // Check again if permissions are granted
        System.out.println("add event");

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_CALENDAR) == PackageManager.PERMISSION_GRANTED) {
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
            ContentResolver cr = context.getContentResolver();
            ContentValues values = new ContentValues();
            values.put(CalendarContract.Events.DTSTART, startTime);
            values.put(CalendarContract.Events.DTEND, endTime);
            values.put(CalendarContract.Events.TITLE, title);
            values.put(CalendarContract.Events.DESCRIPTION, des);
            values.put(CalendarContract.Events.CALENDAR_ID, getPrimaryCalendarId(context));
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
            //requestCalendarPermissions(id, title, des, start, end, monthInt, yearInt, dayInt, h1, m1, h2, m2, date);
        }
    }
    private long getPrimaryCalendarId(Context context) {
        String[] projection = new String[]{
                CalendarContract.Calendars._ID,
                CalendarContract.Calendars.IS_PRIMARY
        };

        Cursor cursor = context.getContentResolver().query(
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

}