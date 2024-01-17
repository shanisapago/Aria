package com.example.aria;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Signin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin);

        System.out.println("hiiiii");
        Button register = findViewById(R.id.buttonRegister);
        register.setOnClickListener(v->{

            EditText usernameSignin=findViewById(R.id.usernameSubmit);
            EditText passwordSignin = findViewById(R.id.passwordSignin);
            EditText confirmPassSignin = findViewById(R.id.confirmSignin);
            EditText phoneSignin = findViewById(R.id.phoneSubmit);

            String username=usernameSignin.getText().toString();
            String password=passwordSignin.getText().toString();
            String confirmPass=confirmPassSignin.getText().toString();
            String phone=phoneSignin.getText().toString();

            Boolean phoneFlag=false;
            Boolean passFlag=false;
            Boolean usernameFlag=false;

            if(username.length()!=0){
                usernameFlag=true;
            }

            if(confirmPass.equals(password)){
                passFlag=true;
            }
            if((phone.length()==10)&&(phone.charAt(0)=='0')&&(phone.charAt(1)=='5')){
                System.out.println("naama");
                phoneFlag=true;
            }
            if(passFlag&&phoneFlag&&usernameFlag){
                Intent i=new Intent(this, Login.class);
                startActivity(i);
            }
        });

        TextView clickHere = findViewById(R.id.clickHereLogin);
        clickHere.setOnClickListener(v->{
            Intent i=new Intent(this, Login.class);
            startActivity(i);
        });


        Intent i=new Intent(this, Day.class);
        startActivity(i);
    }
}
