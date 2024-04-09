package com.example.aria;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
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

        String token = getIntent().getExtras().getString("token");

        dayList=findViewById(R.id.dayList);
        List<DayListItem> l=new ArrayList<>();
        DayListAdapter adapter= new DayListAdapter(l);
        dayList.setAdapter(adapter);


        ImageView add = findViewById(R.id.addBtn);
        add.setOnClickListener(v->{
            Intent i=new Intent(this, AddCalendarActivity.class);
            i.putExtra("token",token);
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
                i.putExtra("alert",clickedDay.getAlert().getAlert());
                System.out.println("good2");
                i.putExtra("description",clickedDay.getDescription());
                startActivity(i);

    }
    });
    }
}
