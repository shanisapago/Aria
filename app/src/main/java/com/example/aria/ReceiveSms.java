package com.example.aria;
import static com.example.aria.Login.JSON;

import android.Manifest;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.aria.RetroFitClasses.ChatsAPI;
import com.example.aria.RetroFitClasses.EventsAPI;
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

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Callback;
import okhttp3.Response;


public class ReceiveSms extends BroadcastReceiver {
    OkHttpClient client = new OkHttpClient();
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 123;
    private static final int NOTIFICATION_ID = 1;
    String sender;
    String response_chatgpt;


    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "New SMS", Toast.LENGTH_LONG).show();
        System.out.println("new sms");
        sendNotification(context, sender, "title");
        SharedPreferences sharedPreferences = context.getSharedPreferences("shp", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");
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
                                                                eventsAPI.updateAriaResult(id_int,start_time,date,token);


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
}