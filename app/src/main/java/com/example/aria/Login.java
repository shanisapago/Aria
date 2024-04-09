package com.example.aria;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.aria.RetroFitClasses.TokensAPI;

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

            TokensAPI tokensAPI=new TokensAPI();
            String token=tokensAPI.post(username.getText().toString(), password.getText().toString());
            System.out.println("token");
            System.out.println(token);
            if(token!=null){
                Intent i=new Intent(this, Loading.class);
                System.out.println(username.getText());
                i.putExtra("token", token);
                //i.putExtra("username", username.getText().toString());
                startActivity(i);
            }
            else
            {
                username.setText("");
                password.setText("");
            }
        });
    }
}
