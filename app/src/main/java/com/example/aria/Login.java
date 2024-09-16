package com.example.aria;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.aria.RetroFitClasses.UserDetails;
import com.example.aria.RetroFitClasses.UsersAPI;
import com.google.firebase.iid.FirebaseInstanceId;
import com.example.aria.RetroFitClasses.TokensAPI;
import okhttp3.MediaType;

public class Login extends AppCompatActivity {
    public static final MediaType JSON = MediaType.get("application/json");



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SharedPreferences sharedPreferences = getSharedPreferences("shp", Context.MODE_PRIVATE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login2);


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
            UserDetails userDetails = tokensAPI.post(username.getText().toString(), password.getText().toString());
            if(userDetails!=null) {


                String token = userDetails.getToken();
                String fullName = userDetails.getFullName();
                if (token != null) {
                    FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(Login.this, instanceIdResult -> {
                        String newToken = "";
                        newToken = instanceIdResult.getToken();

                        UsersAPI usersAPI = new UsersAPI();
                        usersAPI.updateToken(username.getText().toString(), newToken);
                    });

                    Intent i = new Intent(this, CalendarActivity.class);
                    i.putExtra("token", token);
                    i.putExtra("username", username.getText().toString());

                    i.putExtra("fullName", fullName);
                    SharedPreferences.Editor edit = sharedPreferences.edit();
                    edit.putString("token", token);
                    edit.apply();

                    startActivity(i);
                }
            }else {
                LinearLayout errorText = findViewById(R.id.error);
                errorText.setVisibility(View.VISIBLE);
                ImageView errorpass = findViewById(R.id.wrongPassword);
                ImageView errorusername = findViewById(R.id.wrongUsername);

                errorpass.setVisibility(View.VISIBLE);
                errorusername.setVisibility(View.VISIBLE);
                username.setText("");
                password.setText("");

            }
        });
    }





}