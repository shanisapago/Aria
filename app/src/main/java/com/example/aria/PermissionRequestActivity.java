package com.example.aria;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class PermissionRequestActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 123;
    String sender;
    String response_chatgpt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Request the permission
        System.out.println("in the new activity");
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("sender")) {
            sender = intent.getStringExtra("sender");
            System.out.println("sender"+sender);
            response_chatgpt=intent.getStringExtra("msg");
            System.out.println(response_chatgpt);
        }

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.SEND_SMS},
                MY_PERMISSIONS_REQUEST_SEND_SMS);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_SEND_SMS) {
            // Check if the permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
                // Finish the activity and return to the original flow of your app
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(sender, null, response_chatgpt, null, null);
                System.out.println("finish sending sms");
                System.out.println("in permission 1");

                finish();
            } else {
                // Permission denied
                // Handle accordingly (e.g., show a message)
                System.out.println(" permission denied");
                finish();
            }
        }
    }
}
