package com.example.aria;

import static android.Manifest.permission.READ_PHONE_NUMBERS;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.READ_SMS;

import android.Manifest;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
//import android.net.wifi.hotspot2.pps.Credential;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

//import android.support.annotation.Nullable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.aria.RetroFitClasses.ChatsAPI;
import com.example.aria.RetroFitClasses.EventsAPI;
import com.example.aria.RetroFitClasses.TokensAPI;
//import com.google.android.gms.auth.api.Auth;
//import com.google.android.gms.auth.api.credentials.HintRequest;
//import com.google.android.gms.auth.api.credentials.Credential;
//import com.google.android.gms.common.api.GoogleApiClient;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Login extends AppCompatActivity {
    private static final int READ_SMS_PERMISSION_CODE = 1;
    public static final MediaType JSON = MediaType.get("application/json");
    private static final int PERMISSION_REQUEST_CODE = 1000;

    OkHttpClient client = new OkHttpClient();
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SharedPreferences sharedPreferences = getSharedPreferences("shp", Context.MODE_PRIVATE);


        System.out.println("before call api");
        //callChatGptApi("hi, i need you to chat with me where you will be the client and i will be a barber. you need to talk only like the client."
        //        +"always finish the conversion with 'goodbye'"
        //        +"you want to set an appointment in one of the next dates:\n"
        //        +
        //        "1.  12/04/2024 10:00\n" +
        //        "2. 12/04/2024 16:00\n" +
        //        "3. 10/04/2024 12:00\n" +
        //        "4. 11/04/2024 13:00\n"+
        //        "5. 11/04/2024 8:00\n"+
        //        "6. 11/04/2024 9:00\n"+
        //        "7. 11/04/2024 12:00\n"+
        //        "8. 19/04/2024 9:00\n"+
        //        "9. 19/04/2024 19:00\n"+
        //        "10. 19/04/2024 14:00\n"+
        //        "if none of this dates is available, tell the barber \"thank you, i will think about it\"\n" +
        //        "and finish the conversion.\n"
        //);
        System.out.println("after call api");

        //ChatsAPI chatsAPI = new ChatsAPI();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        //Toast.makeText(getApplicationContext(),"Sender:hi",Toast.LENGTH_LONG).show();
        TextView clickHere = findViewById(R.id.clickHereLogin);
        clickHere.setOnClickListener(v -> {
            Intent i = new Intent(this, Signin.class);
            startActivity(i);
        });

        Button login = findViewById(R.id.buttonLogin);
        login.setOnClickListener(v -> {
            EditText username = findViewById(R.id.usernameSubmit);
            EditText password = findViewById(R.id.passwordSubmit);

            TokensAPI tokensAPI = new TokensAPI();
            String token = tokensAPI.post(username.getText().toString(), password.getText().toString());
            System.out.println("token");
            System.out.println(token);
            //ReceiveSms receiveSms = new ReceiveSms(555);
            //receiveSms.onReceive(getApplicationContext(), getIntent());
            //chatsAPI.addChat(2, "0507776310", "17:45", "shaniiiiii", "shanikwaaaa", token);
            if (token != null) {
                Intent i = new Intent(this, Loading.class);
                System.out.println(username.getText());
                i.putExtra("token", token);
                SharedPreferences.Editor edit = sharedPreferences.edit();
                edit.putString("token", token);
                edit.apply();
                //i.putExtra("username", username.getText().toString());

                // Define your permissions
                String[] permissions = {Manifest.permission.READ_SMS, Manifest.permission.SEND_SMS};

// Check if permissions are granted, and if not, request them
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED ||
                        ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                    // Request permissions
                    ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST_CODE);
                }



                startActivity(i);
            } else {
                username.setText("");
                password.setText("");
            }
        });
        Button send = findViewById(R.id.phone);
        send.setOnClickListener(v -> {
            SmsManager smsManager = SmsManager.getDefault();
            String number = "+972549409957";
            String msg = "hi";
            smsManager.sendTextMessage(number, null, msg, null, null);
            System.out.println("finish");
        });

        /*if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.READ_SMS}, READ_SMS_PERMISSION_CODE);
            System.out.println("in if 1");
        } else {
            System.out.println("in else 1");
            readSms();
        }
    }

    private void readSms() {
        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(
                Telephony.Sms.CONTENT_URI,
                null,
                null,
                null,
                null);
        System.out.println("before if");
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String address = cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.ADDRESS));
                String body = cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.BODY));
                Toast.makeText(getApplicationContext(), "Sender: " + address + "\nMessage: " + body, Toast.LENGTH_LONG).show();
                System.out.println("Sender: " + address + "\nMessage: " + body);
            } while (cursor.moveToNext());
        }

        if (cursor != null) {
            cursor.close();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == READ_SMS_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                readSms();
            }
        }
    }*/
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED){
//            requestPermissions(new String[]{
//                    Manifest.permission.RECEIVE_SMS
//            }, 1000);
//        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_CODE) {
            // Check if all permissions were granted
            boolean allPermissionsGranted = true;
            for (int grantResult : grantResults) {
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    allPermissionsGranted = false;
                    break;
                }
            }

            if (allPermissionsGranted) {
                // All permissions granted, you can proceed with your tasks
                Toast.makeText(this, "Permissions granted!", Toast.LENGTH_SHORT).show();
                // Now you can perform actions that require these permissions
            } else {
                // One or more permissions were denied
                Toast.makeText(this, "Permissions denied!", Toast.LENGTH_SHORT).show();
                // You may inform the user about the importance of these permissions
                // or handle the lack of permissions gracefully
            }
        }
    }


    void callChatGptApi(String question){
        JSONArray list_messages=new JSONArray();
        JSONObject json_message=new JSONObject();
        JSONObject json_message2=new JSONObject();
        JSONObject json_message3=new JSONObject();
        JSONObject json_message4=new JSONObject();
        JSONObject json_message5=new JSONObject();


        try {
            json_message.put("role","user");
            json_message.put("content",question);

            json_message2.put("role","system");
            json_message2.put("content","Hey there! I'm looking to schedule a haircut appointment. Do you have any openings in the next few days? Specifically, I'm interested in either April 12th at 10:00 AM or 4:00 PM, April 11th at 1:00 PM, or April 19th at 9:00 AM, 2:00 PM, or 7:00 PM. Let me know what works for you!");
            json_message3.put("role","user");
            json_message3.put("content","i can in 10/04/2024 at 12:00");
            json_message4.put("role","system");
            json_message4.put("content","thank you so we set 10/04/2024 on 12:00.Goodbye.");
            json_message5.put("role","user");
            json_message5.put("content","if you set an appointment write :'yes DD/MM/YYYY HH:MM' else write 'no'");

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        list_messages.put(json_message);
        list_messages.put(json_message2);
        list_messages.put(json_message3);
        list_messages.put(json_message4);
        list_messages.put(json_message5);
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
                        System.out.println(result.trim());
                        System.out.println("shani");
                        System.out.println("result");
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
    /*@Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1000) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "permission granted!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "permission not granted", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }*/

    /*public void GetNumber() {

        if (ActivityCompat.checkSelfPermission(this, READ_SMS) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, READ_PHONE_NUMBERS) ==
                        PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            // Permission check

            // Create obj of TelephonyManager and ask for current telephone service
            TelephonyManager telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
            String phoneNumber = telephonyManager.getDeviceId();
            System.out.println("phone number");
            EditText username = findViewById(R.id.usernameSubmit);
            username.setText(phoneNumber);
            return;
        } else {
            // Ask for permission
            requestPermission();
        }
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{READ_SMS, READ_PHONE_NUMBERS, READ_PHONE_STATE}, 100);
        }
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 100:
                TelephonyManager telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
                if (ActivityCompat.checkSelfPermission(this, READ_SMS) !=
                        PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                        READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(this, READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                String phoneNumber = telephonyManager.getDeviceId();
                //phone_number.setText(phoneNumber);
                System.out.println("phone number");
                EditText username = findViewById(R.id.usernameSubmit);
                username.setText(phoneNumber);
                //Toast.makeText(getApplicationContext(), phoneNumber, Toast.LENGTH_LONG).show();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + requestCode);
        }
    }*/

    /*@Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 1008:
                if (resultCode == RESULT_OK) {
                    Credential credential = data.getParcelableExtra(Credential.EXTRA_KEY);
                    if (credential != null) {
                        String phoneNumber = credential.getId();
                        // Do something with the phone number
                    } else {
                        // Credential is null, handle accordingly
                    }
                } else {
                    // Result not OK, handle accordingly
                }
                break;
            // Handle other request codes if necessary
        }
    }*/
}