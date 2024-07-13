package com.example.aria;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.aria.RetroFitClasses.ChatsAPI;
import com.example.aria.RetroFitClasses.EventsAPI;
import com.example.aria.adapters.ClosedEventsAdapter;
import com.example.aria.adapters.MeetingTimeAdapter;
import com.example.aria.adapters.OpenEventsAdapter;

import java.util.ArrayList;
import java.util.List;

public class AriaListEventsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aria_listevents);

        String username = getIntent().getExtras().getString("username");
        String token = getIntent().getExtras().getString("token");

        List<OpenEventsListItem> openEventsList=new ArrayList<>();
        ListView lstOpenEvents=findViewById(R.id.openEventslst);
        OpenEventsAdapter open_adapter=new OpenEventsAdapter(openEventsList);
        lstOpenEvents.setAdapter(open_adapter);

        List<ClosedEventsListItem> closedEventsList=new ArrayList<>();
        ListView lstClosedEvents=findViewById(R.id.closedEventslst);
        ClosedEventsAdapter closed_adapter=new ClosedEventsAdapter(closedEventsList);
        lstClosedEvents.setAdapter(closed_adapter);
        ImageView backBtn = findViewById(R.id.back);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        ImageButton btnhome=findViewById(R.id.btnhome);
        btnhome.setOnClickListener(view->{
            Intent intent=new Intent(this,CalendarActivity.class);
            intent.putExtra("username",username);
            intent.putExtra("token",token);
            startActivity(intent);
        });
        ImageButton btnAria = findViewById(R.id.btnAria);
        btnAria.setOnClickListener(view -> {
            Intent intent = new Intent(this, AddAriaActivity.class);
            intent.putExtra("token",token);
            intent.putExtra("username",username);
            startActivity(intent);
        });
        System.out.println("in aria list events activity");
        ChatsAPI chatsAPI=new ChatsAPI();
        List<List<AriaEventsItems>> ariaEvents=chatsAPI.getAriaList(token);
        List<AriaEventsItems> openEvents=ariaEvents.get(0);
        List<AriaEventsItems> closedEvents=ariaEvents.get(1);
        System.out.println("1");

        for (int i=0;i<openEvents.size();i++)
        {
            String title="";
            String description="";
            System.out.println("4");
            if(openEvents.get(i).getTitle()!=null)
                title=openEvents.get(i).getTitle();
            if(openEvents.get(i).getDescription()!=null)
                description=openEvents.get(i).getDescription();


            OpenEventsListItem oe=new OpenEventsListItem(title,description);
            System.out.println("5");
            System.out.println(openEvents.get(i).getTitle());
            openEventsList.add(oe);

        }
        open_adapter.notifyDataSetChanged();
        for (int i=0;i<closedEvents.size();i++)
        {
            String title="";
            String description="";
            String time="";
            String date="";
            System.out.println("4");
            if(closedEvents.get(i).getTitle()!=null)
                title=closedEvents.get(i).getTitle();
            if(closedEvents.get(i).getDescription()!=null)
                description=closedEvents.get(i).getDescription();
            if(closedEvents.get(i).getTime()!=null)
                time=closedEvents.get(i).getTime();
            if(closedEvents.get(i).getDate()!=null)
                date=closedEvents.get(i).getDate();



            ClosedEventsListItem ce=new ClosedEventsListItem(title,description,date,time);
            closedEventsList.add(ce);

        }
        closed_adapter.notifyDataSetChanged();






    }

}