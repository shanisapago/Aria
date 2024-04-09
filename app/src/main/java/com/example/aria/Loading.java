package com.example.aria;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.example.aria.RetroFitClasses.NewEvent;
import com.example.aria.RetroFitClasses.EventsAPI;
import com.example.aria.RetroFitClasses.UsersAPI;

import java.util.List;

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

        EventsAPI eventsAPI=new EventsAPI();
        //eventsAPI.updateEnd(4,"20:00");
        //eventsAPI.updateStart(4,"15:00");
        //eventsAPI.updateDate(4,"10/05/2022");
        //eventsAPI.updateDescription(4,"shanishanishani");
        //Alert a = new Alert("dani", "yes");


        //System.out.println(username);
        //String name = "dani";
        //eventsAPI.updateTitle(4,name);
        //eventsAPI.deleteByDate("10/04/2023");

        String token = getIntent().getExtras().getString("token");
        UsersAPI usersAPI=new UsersAPI();
        //System.out.println(username);
        /*List<NewEvent> e = usersAPI.getEvents(token);
        for(int i=0;i<e.size();i++){
            System.out.println(e.get(i).getTitle());
        }*/

        new CountDownTimer(5000, 1000) {

            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                Intent i=new Intent(Loading.this, CalendarActivity.class);
                i.putExtra("token",token);
                startActivity(i);
            }
        }.start();


    }
}