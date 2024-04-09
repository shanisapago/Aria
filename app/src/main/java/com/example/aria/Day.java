package com.example.aria;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.aria.RetroFitClasses.EventsAPI;
import com.example.aria.RetroFitClasses.NewEvent;
import com.example.aria.RetroFitClasses.TokensAPI;
import com.example.aria.RetroFitClasses.UsersAPI;
import com.example.aria.adapters.DayListAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Day extends AppCompatActivity {

    ListView dayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.day);

        dayList=findViewById(R.id.dayList);
        String token = getIntent().getExtras().getString("token");
        String date = getIntent().getExtras().getString("date");
        List<DayListItem> l=new ArrayList<>();

        UsersAPI usersAPI=new UsersAPI();
        List<NewEvent> events = usersAPI.getEvents(token);

        for(int i=0;i<events.size();i++){
            if(date.equals(events.get(i).getDate())){
                DayListItem d = new DayListItem(events.get(i).getId(), events.get(i).getStart(),events.get(i).getEnd(), events.get(i).getTitle(), events.get(i).getDescription(), events.get(i).getAlert());
                l.add(d);
            }
        }
        Collections.sort(l, new DayListComparator());




        DayListAdapter adapter= new DayListAdapter(l);

        dayList.setAdapter(adapter);


        ImageView add = findViewById(R.id.addBtn);
        add.setOnClickListener(v->{
            Intent i=new Intent(this, AddCalendarActivity.class);
            i.putExtra("token", token);
            i.putExtra("date", date);
            startActivity(i);
        });
    }
}
