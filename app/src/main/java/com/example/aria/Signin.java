package com.example.aria;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.aria.RetroFitClasses.UsersAPI;

public class Signin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin);

        //System.out.println("hiiiii");
        Button register = findViewById(R.id.buttonRegister);
        register.setOnClickListener(v->{

            EditText usernameSignin=findViewById(R.id.usernameSubmit);
            EditText passwordSignin = findViewById(R.id.passwordSignin);
            EditText confirmPassSignin = findViewById(R.id.confirmSignin);
            EditText phoneSignin = findViewById(R.id.phoneSubmit);

            LinearLayout wrongUser = findViewById(R.id.wrongUser);
            LinearLayout wrongPass = findViewById(R.id.wrongPass);
            LinearLayout wrongConfirm = findViewById(R.id.wrongConfirm);
            LinearLayout wrongPhone = findViewById(R.id.wrongPhone);

            String username=usernameSignin.getText().toString();
            String password=passwordSignin.getText().toString();
            String confirmPass=confirmPassSignin.getText().toString();
            String phone=phoneSignin.getText().toString();

            Boolean phoneFlag = true;
            Boolean passFlag = true;
            Boolean usernameFlag = true;
            Boolean confirmFlag = true;

            if(username.length()==0){
                wrongUser.setVisibility(View.VISIBLE);
                usernameFlag = false;
            }
            else {
                wrongUser.setVisibility(View.GONE);
            }

            if(!((confirmPass.equals(password))&&(password.length() > 7))){
                if(password.length() < 8){
                    passwordSignin.setText("");
                    confirmPassSignin.setText("");
                    wrongPass.setVisibility(View.VISIBLE);
                    passFlag = false;
                }
                else{

                    wrongConfirm.setVisibility(View.VISIBLE);
                    confirmFlag=false;
                    confirmPassSignin.setText("");
                    wrongPass.setVisibility(View.GONE);
                }

            }

            if((phone.length()==10)&&(phone.charAt(0)=='0')&&(phone.charAt(1)=='5')){
                //System.out.println("naama");
                phoneFlag=true;
            }
            if(passFlag&&phoneFlag&&usernameFlag){

                UsersAPI usersAPI=new UsersAPI();
                usersAPI.post(username, password, phone);


            else {
                wrongConfirm.setVisibility(View.GONE);
                wrongPass.setVisibility(View.GONE);
            }
            if(!((phone.length() == 10)&&(phone.charAt(0) == '0')&&(phone.charAt(1) == '5'))){
                //System.out.println("naama");
                phoneFlag = false;
                phoneSignin.setText("");
                wrongPhone.setVisibility(View.VISIBLE);
            }
            else {
                wrongPhone.setVisibility(View.GONE);
            }

            if(passFlag && phoneFlag && usernameFlag && confirmFlag){
                Intent i=new Intent(this, Login.class);
                startActivity(i);
            }

        });

        TextView clickHere = findViewById(R.id.clickHereLogin);
        clickHere.setOnClickListener(v->{
            Intent i=new Intent(this, Login.class);
            startActivity(i);
        });


        //Intent i=new Intent(this, Day.class);
        //startActivity(i);
    }
}
