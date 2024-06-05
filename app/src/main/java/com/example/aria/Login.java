package com.example.aria;
import android.Manifest;
import android.content.pm.PackageManager;
import android.telephony.SmsManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.example.aria.RetroFitClasses.TokensAPI;
import okhttp3.MediaType;

public class Login extends AppCompatActivity {
    public static final MediaType JSON = MediaType.get("application/json");
    private static final int PERMISSION_REQUEST_CODE = 1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SharedPreferences sharedPreferences = getSharedPreferences("shp", Context.MODE_PRIVATE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login2);
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
                username.setText("");
                password.setText("");
            }
        });

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

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