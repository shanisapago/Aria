package com.example.aria;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.example.aria.RetroFitClasses.ChatsAPI;
import com.example.aria.RetroFitClasses.EventsAPI;
import com.example.aria.RetroFitClasses.NewEvent;

public class Loading extends AppCompatActivity {

    TextView point1;
    TextView point2;
    TextView point3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        point1=(TextView) findViewById(R.id.point1);
        point2=(TextView) findViewById(R.id.point2);
        point3=(TextView) findViewById(R.id.point3);
        Animation animation=AnimationUtils.loadAnimation(this,R.anim.bounce);

        point1.startAnimation(animation);
        point2.startAnimation(animation);
        point3.startAnimation(animation);

        String token = getIntent().getExtras().getString("token");
        String username=getIntent().getExtras().getString("username");

        EventsAPI eventsAPI = new EventsAPI();
        NewEvent newEvent = eventsAPI.updateAriaResult(42,"11:30","08/08/2024",token);
        System.out.println("New Event");
        System.out.println(newEvent.getFlag());
        System.out.println(newEvent.getTitle());
        System.out.println(newEvent.getDescription());
        System.out.println(newEvent.getEnd());
        System.out.println(newEvent.getStart());


        new CountDownTimer(5000, 1000) {

            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                Intent i=new Intent(Loading.this, CalendarActivity.class);
                i.putExtra("token", token);
                i.putExtra("username",username);

                startActivity(i);
            }
        }.start();


    }
}