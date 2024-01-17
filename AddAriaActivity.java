package com.example.aria;

import static java.lang.Character.isDigit;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;


public class AddAriaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_aria);

        ImageButton btnClose=findViewById(R.id.btnClose);
        btnClose.setOnClickListener(view->{
            Intent intent=new Intent(this,MainActivity.class);
            startActivity(intent);
        });

        ImageButton btnDone=findViewById(R.id.btnDone);
        btnDone.setOnClickListener(view->{
            EditText phoneNumber=findViewById(R.id.phoneNumber);
            boolean flag=true;
            if(phoneNumber.getText().length()==10) {
               for(int i=0;i<phoneNumber.getText().length();i++)
               {
                   if((!isDigit(phoneNumber.getText().charAt(i)))||(i==0 && phoneNumber.getText().charAt(i)!='0')||(i==1 && phoneNumber.getText().charAt(i)!='5')) {
                       flag = false;

                   }

               }
               if(flag==true)
               {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
               }
               else {
                   TextView wrongPhoneNumber=findViewById(R.id.wrongPhoneNumber);
                   wrongPhoneNumber.setText("wrong format of number!");
               }
            }
            else
            {
                TextView wrongPhoneNumber=findViewById(R.id.wrongPhoneNumber);
                wrongPhoneNumber.setText("wrong format of number!");
            }
        });


        ListView lstTimeMeeting=findViewById(R.id.lstTimeMeeting);

        //setlayoutmanager

        List<TimeMeeting> meetingTimeList=new ArrayList<>();
        TimeMeeting t1=new TimeMeeting("12/01/2024","13:00");
        TimeMeeting t2=new TimeMeeting("12/01/2024","16:00");
        TimeMeeting t3=new TimeMeeting("13/01/2024","17:00");
        TimeMeeting t4=new TimeMeeting("22/01/2024","19:00");
        meetingTimeList.add(t1);
        meetingTimeList.add(t2);
        meetingTimeList.add(t3);
        meetingTimeList.add(t4);
        meetingTimeAdapter adapter=new meetingTimeAdapter(meetingTimeList);
        lstTimeMeeting.setAdapter(adapter);


    }
}