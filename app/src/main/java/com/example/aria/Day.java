package com.example.aria;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
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
        System.out.println("check get events");

        if(events!=null) {


            for (int i = 0; i < events.size(); i++) {
                System.out.println("event "+i);
                for(int j=0;j<events.get(i).getPhoneNumbers().size();j++)
                {
                    System.out.println(events.get(i).getPhoneNumbers().get(j));
                }


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
                DayListItem clickedDay = l.get(position);
                Intent i=new Intent(getApplicationContext(), EditEvent.class);
                i.putExtra("token",token);
                i.putExtra("id",clickedDay.getId());
                i.putExtra("title",clickedDay.getTitle());
                i.putExtra("start",clickedDay.getTime());
                i.putExtra("end",clickedDay.getEnd());
                i.putExtra("alert",clickedDay.getAlerts());
                i.putExtra("description",clickedDay.getDescription());
                i.putExtra("date",date);
                startActivity(i);
            }
        });
    }
}
