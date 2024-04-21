package com.example.aria;
import static com.example.aria.Login.JSON;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsMessage;
import androidx.annotation.NonNull;
import com.example.aria.RetroFitClasses.ChatsAPI;
import com.example.aria.RetroFitClasses.EventsAPI;
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

public class ReceiveSms extends BroadcastReceiver {
    OkHttpClient client = new OkHttpClient();

    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("shp", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            Object[] pdus = (Object[]) bundle.get("pdus");
            if (pdus != null) {
                for (Object pdu : pdus) {
                    SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdu);
                    String sender = smsMessage.getDisplayOriginatingAddress();
                    String messageBody = smsMessage.getMessageBody();
                    System.out.println("New SMS received from: " + sender + "\nMessage: " + messageBody);
                    if(messageBody.toLowerCase().contains("goodbye"))
                    {
                        JSONArray list_all_messages=new JSONArray();
                        JSONObject json_message=new JSONObject();
                        JsonObject json_message2=new JsonObject();



                        try {
                            json_message.put("role", "user");
                            json_message.put("content", "if you set an appointment write :'yes DD/MM/YYYY HH:MM' else write 'no'");
                            json_message2 = JSONObjectToJsonObjectConverter.convertToGsonJsonObject(json_message);
                        }catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
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

                        ChatsAPI chatsAPI = new ChatsAPI();
                        JsonObject result = chatsAPI.addMessage(sender, token, json_message2, hour_string + ":" + minutes_string);
                        String id=result.get("id").getAsString();

                        JsonElement array = result.get("array");
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
                                        System.out.println("result");
                                        String response_chatgpt = result.trim();
                                        if (!(response_chatgpt.equalsIgnoreCase("no")) && !(response_chatgpt.equalsIgnoreCase("no."))) //check if no is enough or No
                                        {
                                            int index_time = response_chatgpt.indexOf(":");
                                            String start_time = response_chatgpt.substring(index_time - 2, index_time + 3);
                                            int index_first_slash = response_chatgpt.indexOf("/");
                                            String date = response_chatgpt.substring(index_first_slash - 2, index_first_slash + 8);
                                            EventsAPI eventsAPI = new EventsAPI();
                                            eventsAPI.updateStart(5, start_time);
                                            eventsAPI.updateDate(5, date);
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
}