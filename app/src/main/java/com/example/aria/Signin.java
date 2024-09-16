package com.example.aria;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.example.aria.RetroFitClasses.TokensAPI;
import com.example.aria.RetroFitClasses.UserDetails;
import com.example.aria.RetroFitClasses.UsersAPI;
import com.google.firebase.iid.FirebaseInstanceId;
import java.util.concurrent.atomic.AtomicReference;

public class Signin extends AppCompatActivity {
    private  Boolean usernameUnique=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences sharedPreferences = getSharedPreferences("shp", Context.MODE_PRIVATE);

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

            if(!(username_save.get().equals(username)))
            {
                UsersAPI users=new UsersAPI();
                boolean code=users.checkUsername(username);

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
                FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(Signin.this, instanceIdResult -> {
                    String newToken = "";
                    EditText fullNameEdit = findViewById(R.id.fullNameSubmit);
                    String fullName = fullNameEdit.getText().toString();
                    newToken = instanceIdResult.getToken();

                    UsersAPI usersAPI=new UsersAPI();
                    usersAPI.post(username, password, phone, newToken, fullName);
                    Intent i = new Intent(this, CalendarActivity.class);
                    i.putExtra("activity","signin");
                    TokensAPI tokensAPI = new TokensAPI();

                    UserDetails userDetails = tokensAPI.post(username, password);
                    String token=userDetails.getToken();

                    if (token != null) {
                        i.putExtra("token", token);
                        i.putExtra("username", username);
                        i.putExtra("fullName",fullName);
                        SharedPreferences.Editor edit = sharedPreferences.edit();
                        edit.putString("token", token);
                        edit.apply();
                    }
                    startActivity(i);
                });

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
                if(usernameFlag==false)
                {
                    ImageView errorIcon = findViewById(R.id.wrongUsername);
                    errorIcon.setVisibility(View.VISIBLE);
                    LinearLayout errorText2 = findViewById(R.id.wrongUsername2);
                    TextView usernameTextWrong=findViewById(R.id.usernameTextWrong);
                    usernameTextWrong.setText("enter an username");
                    errorText2.setVisibility(View.VISIBLE);

                }
                else if(usernameUnique==false){
                    ImageView errorIcon = findViewById(R.id.wrongUsername);
                    LinearLayout errorText = findViewById(R.id.wrongUsername2);
                    errorIcon.setVisibility(View.VISIBLE);
                    errorText.setVisibility(View.VISIBLE);
                    TextView usernameTextWrong=findViewById(R.id.usernameTextWrong);
                    usernameTextWrong.setText("your username is already taken");

                }
                else
                {
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