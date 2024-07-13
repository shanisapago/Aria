package com.example.aria;
import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.telephony.SmsManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.aria.RetroFitClasses.ChatsAPI;
import com.example.aria.RetroFitClasses.FirebaseAPI;
import com.google.firebase.iid.FirebaseInstanceId;

import com.example.aria.RetroFitClasses.EventsAPI;
import com.example.aria.RetroFitClasses.TokensAPI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Calendar;
import java.util.TimeZone;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import okhttp3.MediaType;

public class Login extends AppCompatActivity {
    public static final MediaType JSON = MediaType.get("application/json");
    //private static final int PERMISSIONS_REQUEST_CODE_CALENDAR = 100;
    private static final int PERMISSION_REQUEST_CODE = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SharedPreferences sharedPreferences = getSharedPreferences("shp", Context.MODE_PRIVATE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login2);

        //showCustomPermissionDialog();


        /*FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            System.out.println("failed");
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();
                        System.out.println("new token");
                        System.out.println(token);
                        // Log and toast
                        //String msg = getString(R.string.msg_token_fmt, token);
                        //Log.d(TAG, msg);
                        //Toast.makeText(Login.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });*/
        /*FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(Login.this, instanceIdResult -> {
                    String newToken = instanceIdResult.getToken();
                    System.out.println("new token");
                    System.out.println(newToken);
                    FirebaseAPI firebaseAPI = new FirebaseAPI();
                    firebaseAPI.sendMessage("title", "description", newToken);
                    System.out.println("after request");
                });*/





        TextView textView = findViewById(R.id.clickHereLogin);
        String text = "Sign in";
        SpannableString content = new SpannableString(text);
        content.setSpan(new UnderlineSpan(), 0, text.length(), 0);
        textView.setText(content);



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
            if (token != null) {
                Intent i = new Intent(this, Loading.class);
                i.putExtra("token", token);
                i.putExtra("username",username.getText().toString());
                SharedPreferences.Editor edit = sharedPreferences.edit();
                edit.putString("token", token);
                edit.apply();
                String[] permissions = {Manifest.permission.READ_SMS, Manifest.permission.SEND_SMS};


                if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED ||
                        ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST_CODE);
                }

                startActivity(i);
            } else {
                LinearLayout errorText = findViewById(R.id.error);
                errorText.setVisibility(View.VISIBLE);
                ImageView errorpass = findViewById(R.id.wrongPassword);
                ImageView errorusername = findViewById(R.id.wrongUsername);

                errorpass.setVisibility(View.VISIBLE);
                errorusername.setVisibility(View.VISIBLE);
                username.setText("");
                password.setText("");
                System.out.println("there is not user like this");
            }
        });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        System.out.println("in request");
        if (requestCode == PERMISSION_REQUEST_CODE) {
            boolean allPermissionsGranted = true;
            for (int grantResult : grantResults) {
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    allPermissionsGranted = false;
                    break;
                }
            }

            if (allPermissionsGranted) {
                Toast.makeText(this, "Permissions granted!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permissions denied!", Toast.LENGTH_SHORT).show();

            }
        }
    }

        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{
                    Manifest.permission.RECEIVE_SMS
            }, 1000);
        }*/


}