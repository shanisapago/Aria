package com.example.aria;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

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

        Intent i=new Intent(this, Day.class);
        startActivity(i);
       /* new android.os.Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                point2.startAnimation(animation);
            }
        },600);
        new android.os.Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                point3.startAnimation(animation);
            }
        },1600);*/


    }
}