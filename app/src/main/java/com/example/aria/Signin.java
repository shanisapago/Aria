package com.example.aria;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.aria.RetroFitClasses.UsersAPI;

public class Signin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin);

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

            if(username.length()!=0){
                usernameFlag=true;
            }

            if(password.length()>7){
                passFlag=true;
            }

            if(confirmPass.equals(password)){
                confirmFlag=true;
            }
            if((phone.length()==10)&&(phone.charAt(0)=='0')&&(phone.charAt(1)=='5')){
                phoneFlag=true;
            }
            if(passFlag&&phoneFlag&&usernameFlag&&confirmFlag){

                UsersAPI usersAPI=new UsersAPI();
                usersAPI.post(username, password, phone);

                Intent i=new Intent(this, Login.class);
                startActivity(i);
            }
            else {
                if(passFlag==false) {
                    ImageView errorIcon = findViewById(R.id.wrongPass);
                    LinearLayout errorText = findViewById(R.id.wrongPass2);
                    errorIcon.setVisibility(View.VISIBLE);
                    errorText.setVisibility(View.VISIBLE);
                }
                else{
                    ImageView errorIcon = findViewById(R.id.wrongPass);
                    LinearLayout errorText = findViewById(R.id.wrongPass2);
                    errorIcon.setVisibility(View.GONE);
                    errorText.setVisibility(View.GONE);
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
                    errorIcon.setVisibility(View.GONE);
                    errorText.setVisibility(View.GONE);
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
                    errorIcon.setVisibility(View.GONE);
                    errorText.setVisibility(View.GONE);
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
