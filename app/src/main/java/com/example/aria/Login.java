package com.example.aria;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.aria.RetroFitClasses.TokensAPI;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;

public class Login extends AppCompatActivity {
    public static final MediaType JSON = MediaType.get("application/json");
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SharedPreferences sharedPreferences = getSharedPreferences("shp", Context.MODE_PRIVATE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
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
                startActivity(i);
            } else {
                username.setText("");
                password.setText("");
            }
        });

        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{
                    Manifest.permission.RECEIVE_SMS
            }, 1000);
        }*/

    }
}