package com.example.aria;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.aria.RetroFitClasses.UsersAPI;

import java.util.concurrent.atomic.AtomicReference;

public class Signin extends AppCompatActivity {
    private  Boolean usernameUnique=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin2);

        TextView textView = findViewById(R.id.clickHereLogin);
        String text = "Log in";
        SpannableString content = new SpannableString(text);
        content.setSpan(new UnderlineSpan(), 0, text.length(), 0);
        textView.setText(content);

        AtomicReference<String> username_save = new AtomicReference<>("");


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
            Boolean confirmFlag=false;
            Boolean usernameFlag=false;

            System.out.println("");
            if(!(username_save.get().equals(username)))
            {
                System.out.println("check the username unique");
                UsersAPI users=new UsersAPI();
                boolean code=users.checkUsername(username);
                System.out.println("code");
                System.out.println(code);
                username_save.set(username);
                usernameUnique=code;
            }

            if(username.length()!=0){
                usernameFlag=true;
            }

            if(password.length()>7){
                passFlag=true;
            }

            if(confirmPass.equals(password)){
                confirmFlag=true;
            }
            if((phone.length()==9)&&(phone.charAt(0)=='5')){
                phoneFlag=true;
            }
            if(passFlag&&phoneFlag&&usernameFlag&&confirmFlag&&usernameUnique){

                UsersAPI usersAPI=new UsersAPI();
                usersAPI.post(username, password, phone);
//                if(code) {
                Intent i = new Intent(this, Login.class);
                startActivity(i);
//                }
//                else{
//                    System.out.println("the username is not unique");
//                    usernameSignin.setText("");
//                }
            }
            else {
                if(passFlag==false) {
                    System.out.println("password not good");
                    ImageView errorIcon = findViewById(R.id.wrongPass);
                    LinearLayout errorText = findViewById(R.id.wrongPass2);
                    errorIcon.setVisibility(View.VISIBLE);
                    errorText.setVisibility(View.VISIBLE);
                }
                else{
                    ImageView errorIcon = findViewById(R.id.wrongPass);
                    LinearLayout errorText = findViewById(R.id.wrongPass2);
                    errorIcon.setVisibility(View.INVISIBLE);
                    errorText.setVisibility(View.INVISIBLE);
                }
                if(confirmFlag==false) {
                    ImageView errorIcon = findViewById(R.id.wrongConfirm);
                    LinearLayout errorText = findViewById(R.id.wrongConfirm2);
                    errorIcon.setVisibility(View.VISIBLE);
                    errorText.setVisibility(View.VISIBLE);
                }
                else{
                    ImageView errorIcon = findViewById(R.id.wrongConfirm);
                    LinearLayout errorText = findViewById(R.id.wrongConfirm2);
                    errorIcon.setVisibility(View.INVISIBLE);
                    errorText.setVisibility(View.INVISIBLE);
                }
                if(phoneFlag==false) {
                    ImageView errorIcon = findViewById(R.id.wrongPhone);
                    LinearLayout errorText = findViewById(R.id.wrongPhone2);
                    errorIcon.setVisibility(View.VISIBLE);
                    errorText.setVisibility(View.VISIBLE);
                }
                else{
                    ImageView errorIcon = findViewById(R.id.wrongPhone);
                    LinearLayout errorText = findViewById(R.id.wrongPhone2);
                    errorIcon.setVisibility(View.INVISIBLE);
                    errorText.setVisibility(View.INVISIBLE);
                }
                if(usernameUnique==false || usernameFlag==false)
                {
                    System.out.println("not unique username");
                    ImageView errorIcon = findViewById(R.id.wrongUsername);
                    LinearLayout errorText = findViewById(R.id.wrongUsername2);
                    errorIcon.setVisibility(View.VISIBLE);
                    errorText.setVisibility(View.VISIBLE);
                }
                else {
                    System.out.println("unique");
                    ImageView errorIcon = findViewById(R.id.wrongUsername);
                    LinearLayout errorText = findViewById(R.id.wrongUsername2);
                    errorIcon.setVisibility(View.INVISIBLE);
                    errorText.setVisibility(View.INVISIBLE);
                }


            }
        });

        TextView clickHere = findViewById(R.id.clickHereLogin);
        clickHere.setOnClickListener(v->{
            Intent i=new Intent(this, Login.class);
            startActivity(i);
        });
    }
}