package com.example.aria;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.aria.adapters.DayListAdapter;

import java.util.ArrayList;
import java.util.List;

public class Day extends AppCompatActivity {

    ListView dayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.day);

        dayList=findViewById(R.id.dayList);
        List<DayListItem> l=new ArrayList<>();

        DayListItem d=new DayListItem("9:00", "title1", "description1");
        DayListItem d2=new DayListItem("10:00", "title2", "description2");
        DayListItem d3=new DayListItem("11:00", "title3", "description3");
        l.add(d);
        l.add(d2);
        l.add(d3);


        DayListAdapter adapter= new DayListAdapter(l);

        dayList.setAdapter(adapter);


        ImageView add = findViewById(R.id.addBtn);
        add.setOnClickListener(v->{
            Intent i=new Intent(this, AddCalendarActivity.class);
            startActivity(i);
        });
    }
}
