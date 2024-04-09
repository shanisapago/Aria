package com.example.aria;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.aria.RetroFitClasses.NewEvent;
import com.example.aria.RetroFitClasses.UsersAPI;
import com.example.aria.adapters.DayListAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Day extends AppCompatActivity {

    ListView dayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.day);
        String token = getIntent().getExtras().getString("token");
        String date = getIntent().getExtras().getString("date");
        dayList=findViewById(R.id.dayList);
        List<DayListItem> l=new ArrayList<>();
        UsersAPI usersAPI=new UsersAPI();
        List<NewEvent> events = usersAPI.getEvents(token);
        if(events!=null) {
            for (int i = 0; i < events.size(); i++) {
                if (date.equals(events.get(i).getDate())) {
                    DayListItem d = new DayListItem(events.get(i).getId(), events.get(i).getStart(), events.get(i).getEnd(), events.get(i).getTitle(), events.get(i).getDescription(), events.get(i).getAlert());
                    l.add(d);
                }
            }
            Collections.sort(l, new DayListComparator());
        }
       

        DayListAdapter adapter= new DayListAdapter(l);

        dayList.setAdapter(adapter);


        ImageView add = findViewById(R.id.addBtn);
        add.setOnClickListener(v->{
            Intent i=new Intent(this, AddCalendarActivity.class);
            i.putExtra("token", token);
            i.putExtra("date", date);
            startActivity(i);
        });
        ListView listview=findViewById(R.id.dayList);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the clicked DayList object
                DayListItem clickedDay = l.get(position);
                System.out.println(clickedDay.description);
                System.out.println("in on itemclick");
                Intent i=new Intent(getApplicationContext(), EditEvent.class);
                System.out.println("1");
                i.putExtra("token",token);
                i.putExtra("id",clickedDay.getId());
                System.out.println("2");
                i.putExtra("title",clickedDay.getTitle());
                i.putExtra("start",clickedDay.getTime());
                i.putExtra("end",clickedDay.getEnd());
                System.out.println("good");
                i.putExtra("alert",clickedDay.getAlerts());
                System.out.println("good2");
                i.putExtra("description",clickedDay.getDescription());
                i.putExtra("date",date);
                startActivity(i);

            }
        });
    }
}
