package com.example.aria;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        TextView clickHere = findViewById(R.id.clickHereLogin);
        clickHere.setOnClickListener(v->{
            Intent i=new Intent(this, Signin.class);
            startActivity(i);
        });

        Button login = findViewById(R.id.buttonLogin);
        login.setOnClickListener(v->{
            EditText username = findViewById(R.id.usernameSubmit);
            EditText password = findViewById(R.id.passwordSubmit);
            Intent i=new Intent(this, Loading.class);
            System.out.println(username.getText());
            i.putExtra("username", username.getText().toString());
            Intent i=new Intent(this, CalendarActivity.class);
            startActivity(i);
        });
    }
}
