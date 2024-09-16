package com.example.aria;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.example.aria.RetroFitClasses.ChatsAPI;
import com.example.aria.adapters.ClosedEventsAdapter;
import com.example.aria.adapters.OpenEventsAdapter;
import java.util.ArrayList;
import java.util.List;

public class AriaListEventsActivity extends AppCompatActivity {
    String username,token,fullName;
    private static final int PERMISSIONS_REQUEST_CODE_SMS = 1011;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aria_listevents);

        username = getIntent().getExtras().getString("username");
        token = getIntent().getExtras().getString("token");
        fullName = getIntent().getExtras().getString("fullName");

        List<OpenEventsListItem> openEventsList=new ArrayList<>();
        ListView lstOpenEvents=findViewById(R.id.openEventslst);
        OpenEventsAdapter open_adapter=new OpenEventsAdapter(openEventsList);
        lstOpenEvents.setAdapter(open_adapter);

        List<ClosedEventsListItem> closedEventsList=new ArrayList<>();
        ListView lstClosedEvents=findViewById(R.id.closedEventslst);
        ClosedEventsAdapter closed_adapter=new ClosedEventsAdapter(closedEventsList);
        lstClosedEvents.setAdapter(closed_adapter);
        ImageView backBtn = findViewById(R.id.back);
        ImageButton btnAria = findViewById(R.id.btnAria);
        btnAria.setOnClickListener(view -> {
            String[] permissions2 = {android.Manifest.permission.READ_SMS, android.Manifest.permission.SEND_SMS, android.Manifest.permission.RECEIVE_SMS};
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, android.Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED ||  ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, permissions2, PERMISSIONS_REQUEST_CODE_SMS);

            }
            else {
                Intent intent=new Intent(this, AddAriaActivity.class);
                intent.putExtra("username",username);
                intent.putExtra("token",token);
                intent.putExtra("fullName",fullName);
                startActivity(intent);
            }
        });
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
            intent.putExtra("fullName",fullName);
            startActivity(intent);
        });


        ChatsAPI chatsAPI=new ChatsAPI();
        List<List<AriaEventsItems>> ariaEvents=chatsAPI.getAriaList(token);
        List<AriaEventsItems> openEvents=ariaEvents.get(0);
        List<AriaEventsItems> closedEvents=ariaEvents.get(1);


        for (int i=0;i<openEvents.size();i++)
        {
            String title="";
            String description="";

            if(openEvents.get(i).getTitle()!=null)
                title=openEvents.get(i).getTitle();
            if(openEvents.get(i).getDescription()!=null)
                description=openEvents.get(i).getDescription();


            OpenEventsListItem oe=new OpenEventsListItem(title,description);

            openEventsList.add(oe);

        }
        open_adapter.notifyDataSetChanged();
        for (int i=0;i<closedEvents.size();i++)
        {
            String title="";
            String description="";
            String time="";
            String date="";

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
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        TextView start_textview = findViewById(R.id.startsTextView);

        if (requestCode == PERMISSIONS_REQUEST_CODE_SMS) {


            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(this, AddAriaActivity.class);
                intent.putExtra("token", token);
                intent.putExtra("username", username);
                intent.putExtra("fullName",fullName);
                startActivity(intent);

            } else {
                Toast.makeText(this, "aria needs sms permission", Toast.LENGTH_SHORT).show();


            }
        }
    }

}