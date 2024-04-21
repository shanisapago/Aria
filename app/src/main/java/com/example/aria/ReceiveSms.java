package com.example.aria;

import static com.example.aria.Login.JSON;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.aria.RetroFitClasses.ChatsAPI;
import com.example.aria.RetroFitClasses.EventsAPI;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalTime;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Callback;
import okhttp3.Response;
import android.Manifest;



public class ReceiveSms extends BroadcastReceiver {
    OkHttpClient client = new OkHttpClient();
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 123;
    String sender;
    String response_chatgpt;


    @Override
    public void onReceive(Context context, Intent intent) {

        Toast.makeText(context, "New SMS", Toast.LENGTH_LONG).show();
        SharedPreferences sharedPreferences = context.getSharedPreferences("shp", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");
        System.out.println("token");
        System.out.println(token);
        //System.out.println("New SMS");
        //System.out.println(this.num);
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
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            Object[] pdus = (Object[]) bundle.get("pdus");
            if (pdus != null) {
                for (Object pdu : pdus) {
                    // Convert the message to an SmsMessage object
                    SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdu);
                    // Get the sender's phone number
                    sender = smsMessage.getDisplayOriginatingAddress();
                    // Get the message body
                    String messageBody = smsMessage.getMessageBody();
                    // Display a toast with the sender's phone number and message body
                    System.out.println("New SMS received from: " + sender + "\nMessage: " + messageBody);

                    JsonObject response_of_sms_json = new JsonObject();
                    response_of_sms_json.addProperty("role", "user");
                    response_of_sms_json.addProperty("content", messageBody);

                    ChatsAPI chatsAPI = new ChatsAPI();
                    JsonObject result = chatsAPI.addMessage(sender, token, response_of_sms_json, hour_string + ":" + minutes_string); //before if....
                    System.out.println("result!!!!!!!!");
                    System.out.println(result.get("array"));
                    String id = result.get("id").getAsString();
                    int id_int = Integer.parseInt(id);
                    System.out.println("iddddddd");
                    System.out.println(id);
                    if (id_int != -1) {

                    System.out.println("open chat");
                    JsonElement array = result.get("array");
                    JSONArray list_all_messages = new JSONArray();

                    array.getAsJsonArray().forEach(element -> {
                        // Convert each JsonElement to a JSONObject and add it to the JSONArray
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
                            System.out.println("shani");
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
                                            json_message.put("content", "If you succeeded in setting an appointment, please write the date and time in the format 'yes dd/mm/yyyy hh:mm am/pm'. If you didn't succeed in setting an appointment, please write 'no'.");
                                            //add to resultif you succeeded to set an appointment, please write the date in the format 'yes dd/mm/yyyy'.
                                            //json_message2 = JSONObjectToJsonObjectConverter.convertToGsonJsonObject(json_message);
                                            list_all_messages.put(json_message);
                                        } catch (JSONException e) {
                                            throw new RuntimeException(e);
                                        }


                                        System.out.println("2");


                                        //list_all_messages.put(json_message);
                                        JSONObject jsonBody = new JSONObject();
                                        try {
                                            jsonBody.put("model", "gpt-3.5-turbo-16k");
                                            //jsonBody.put("prompt",question);
                                            jsonBody.put("messages", list_all_messages);
                                            //jsonBody.put("max_tokens",4000);
                                            //jsonBody.put("temperature",0);
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
                                                System.out.println("shani");
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

                                                            System.out.println("in ifff");
                                                            int index_time = response_chatgpt.indexOf(":");
                                                            String start_time = response_chatgpt.substring(index_time - 2, index_time + 3);
                                                            int index_first_slash = response_chatgpt.indexOf("/");
                                                            String date = response_chatgpt.substring(index_first_slash - 2, index_first_slash + 8);
                                                            EventsAPI eventsAPI = new EventsAPI();
                                                            eventsAPI.updateAriaResult(id_int,start_time,"12:00",date);

                                                        } else {
                                                            //the event is canceled
                                                            System.out.println("the event is canceled");
                                                            EventsAPI eventsAPI=new EventsAPI();
                                                            eventsAPI.deleteEventById(id_int,token);
                                                        }
                                                    } catch (JSONException e) {
                                                        System.out.println("shani");
                                                        System.out.println("failed 3 " + e.getMessage());
                                                        e.printStackTrace();
                                                    }
                                                } else {
                                                    System.out.println("shani");
                                                    System.out.println("failed 2 " + response.body().toString());
                                                    System.out.println(response.body().string());


                                                }
                                            }
                                        });
                                    } else {
                                        //the chatgpt answer is not the final
                                        JsonObject json_response_chat_conv = JSONObjectToJsonObjectConverter.convertToGsonJsonObject(json_response_chat);
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

                                        chatsAPI.addMessage(sender, token, json_response_chat_conv, hour_string + ":" + minutes_string);
                                        //sending sms to the sender.....
                                        //sending this as sms
                                        System.out.println("go to permission");
                                        SmsManager smsManager = SmsManager.getDefault();
                                        smsManager.sendTextMessage(sender, null, response_chatgpt, null, null);
                                        System.out.println("finish sending sms");
                                        //Intent permissionIntent = new Intent(context, PermissionRequestActivity.class);
                                        //permissionIntent.putExtra("sender", sender);
                                        //permissionIntent.putExtra("msg", response_chatgpt);
                                        //permissionIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                                        //context.startActivity(permissionIntent);

//                                        if (ContextCompat.checkSelfPermission(context, Manifest.permission.SEND_SMS)
//                                                != PackageManager.PERMISSION_GRANTED) {
//                                            // Permission is not granted
//                                            // Request the permission
//                                            ActivityCompat.requestPermissions((Activity) context,
//                                                    new String[]{Manifest.permission.SEND_SMS},
//                                                    MY_PERMISSIONS_REQUEST_SEND_SMS);
//                                            System.out.println("in if111111");
//                                        } else {
//                                            // Permission already granted
//                                            // Proceed with sending SMS
//                                            SmsManager smsManager = SmsManager.getDefault();
//                                            smsManager.sendTextMessage(sender, null, response_chatgpt, null, null);
//                                            System.out.println("finish sending sms");
//                                        }

                                    }

                                } catch (JSONException e) {
                                    System.out.println("shani");
                                    System.out.println("failed 3 " + e.getMessage());
                                    e.printStackTrace();
                                }
                            } else {
                                System.out.println("shani");
                                System.out.println("failed 2 " + response.body().toString());
                                System.out.println(response.body().string());


                            }
                        }
                    });


                }
                }
            }
        }
    }
//    @Override
//    public void onRequestPermissionsResult(int requestCode,
//                                           String[] permissions, int[] grantResults) {
//        switch (requestCode) {
//            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
//                // If request is cancelled, the result arrays are empty.
//                System.out.println("in case");
//                if (grantResults.length > 0
//                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    // Permission granted
//                    // Proceed with sending SMS
//                    SmsManager smsManager = SmsManager.getDefault();
//                    smsManager.sendTextMessage(sender, null, response_chatgpt, null, null);
//                    System.out.println("finish sending sms");
//
//                } else {
//                    // Permission denied
//                    // Handle accordingly (e.g., show a message or disable features)
//                    System.out.println("in else");
//                }
//                return;
//            }
//            // Other permission requests...
//        }
//    }

}