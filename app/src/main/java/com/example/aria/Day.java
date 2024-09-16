package com.example.aria;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.aria.RetroFitClasses.NewEvent;
import com.example.aria.RetroFitClasses.UsersAPI;
import com.example.aria.adapters.DayListAdapter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Day extends AppCompatActivity {

    ListView dayList;
    TextView calendarTitle;
    Calendar currentCalendar;
    String token,username,fullName;
    List<NewEvent> events;
    private static final int PERMISSIONS_REQUEST_CODE_SMS = 1011;
    private final String PADDING_ZERO="0";
    private final int ONE_DIGIT=10;
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.day2);
        String date = getIntent().getExtras().getString("date");
        calendarTitle = findViewById(R.id.calendar_title);
        ImageView prevWeekButton = findViewById(R.id.prev_week);
        ImageView nextWeekButton = findViewById(R.id.next_week);
        ImageView backBtn = findViewById(R.id.back);
        String givenDate = date;
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        token = getIntent().getExtras().getString("token");
        username = getIntent().getExtras().getString("username");
        fullName = getIntent().getExtras().getString("fullName");

        UsersAPI usersAPI=new UsersAPI();
        events = usersAPI.getEvents(token);
        ImageButton btnAriaList=findViewById(R.id.arialstbtn);
        btnAriaList.setOnClickListener(view->{
            Intent intent=new Intent(this, AriaListEventsActivity.class);
            intent.putExtra("username",username);
            intent.putExtra("token",token);
            intent.putExtra("fullName",fullName);
            startActivity(intent);
        });
        currentCalendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        try {
            currentCalendar.setTime(sdf.parse(givenDate));
        } catch (Exception e) {
            e.printStackTrace();
        }
                //int year = Integer.parseInt(getIntent().getExtras().getString("year"));
                //int month = Integer.parseInt(getIntent().getExtras().getString("month"));
                int day = Integer.parseInt(getIntent().getExtras().getString("day"));

        currentCalendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        updateCalendar(day);

        prevWeekButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentCalendar.add(Calendar.WEEK_OF_YEAR, -1);
                int f = 0;
                updateCalendar(f);
            }
        });

        nextWeekButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentCalendar.add(Calendar.WEEK_OF_YEAR, 1);
                int f = 0;
                updateCalendar(f);
            }
        });
    }

    private void updateCalendar(int day) {

        updateCalendarTitle(day);
        updateWeekDates(day);
    }

    private void updateCalendarTitle(int day) {
        Calendar startOfWeekCalendar = (Calendar) currentCalendar.clone();
        startOfWeekCalendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        Calendar endOfWeekCalendar = (Calendar) startOfWeekCalendar.clone();
        endOfWeekCalendar.add(Calendar.DAY_OF_WEEK, 6);


        String monthYearTitle;
        SimpleDateFormat monthYearFormat = new SimpleDateFormat("MMMM, yyyy", Locale.getDefault());

        if (startOfWeekCalendar.get(Calendar.MONTH) == endOfWeekCalendar.get(Calendar.MONTH)) {

            monthYearTitle = monthYearFormat.format(startOfWeekCalendar.getTime());
        } else {

            if(day>10){
                monthYearTitle = monthYearFormat.format(startOfWeekCalendar.getTime());
            }
            else {
                monthYearTitle = monthYearFormat.format(endOfWeekCalendar.getTime());
            }
        }

        calendarTitle.setText(monthYearTitle);
    }

    private void updateWeekDates(int day) {

        int[] daysOfWeek = {
                R.id.sunday_date,
                R.id.monday_date,
                R.id.tuesday_date,
                R.id.wednesday_date,
                R.id.thursday_date,
                R.id.friday_date,
                R.id.saturday_date
        };


        Calendar weekCalendar = (Calendar) currentCalendar.clone();
        weekCalendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);


        SimpleDateFormat dayFormat = new SimpleDateFormat("d", Locale.getDefault());
        for (int i = 0; i < daysOfWeek.length; i++) {
            TextView dateView = findViewById(daysOfWeek[i]);
            dateView.setText(dayFormat.format(weekCalendar.getTime()));
            weekCalendar.add(Calendar.DAY_OF_MONTH, 1);

            int index = ((String)calendarTitle.getText()).indexOf(',');

            String month = ((String)calendarTitle.getText()).substring(0,index);



            String newMonth;
            switch (month){
                case "ינואר":
                case "January":
                    newMonth = "01";
                    break;
                case "February":
                case "פברואר":
                    newMonth = "02";
                    break;
                case "March":
                case "מרץ":
                    newMonth = "03";
                    break;
                case "April":
                case "אפריל":
                    newMonth = "04";
                    break;
                case "May":
                case "מאי":
                    newMonth = "05";
                    break;
                case "June":
                case "יוני":
                    newMonth = "06";
                    break;
                case "July":
                case "יולי":
                    newMonth = "07";
                    break;
                case "August":
                case "אוגוסט":
                    newMonth = "08";
                    break;
                case "September":
                case "ספטמבר":
                    newMonth = "09";
                    break;
                case "October":
                case "אוקטובר":
                    newMonth = "10";
                    break;
                case "November":
                case "נובמבר":
                    newMonth = "11";
                    break;
                default:
                    newMonth = "12";
            }

            String newDay = String.valueOf(day);
            String chosenDate;
            if(day/ONE_DIGIT==0)
                newDay = PADDING_ZERO.concat(String.valueOf(day));
            chosenDate = (((newDay.concat("/")).concat(newMonth)).concat("/")).concat(String.valueOf(currentCalendar.get(Calendar.YEAR)));


            if(Integer.parseInt((String) dateView.getText()) == day ){
                dateView.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.lightBlue));
            }
            else{
                dateView.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.invisible));
            }
            showEvents(chosenDate);
            dateView.setOnClickListener(v -> {
                updateCalendar(Integer.parseInt((String)dateView.getText()));
            });
        }
    }
    private void showEvents(String date){

        String token = getIntent().getExtras().getString("token");
        String username = getIntent().getExtras().getString("username");

        dayList=findViewById(R.id.dayList);
        ImageView ariaBtn = findViewById(R.id.btnAria);
        ImageView homeBtn = findViewById(R.id.btnhome);
        List<DayListItem> l=new ArrayList<>();


        if(events!=null) {



            for (int i = 0; i < events.size(); i++) {

                if (date.equals(events.get(i).getDate())) {

                    DayListItem d = new DayListItem(events.get(i).getId(), events.get(i).getStart(), events.get(i).getEnd(), events.get(i).getTitle(), events.get(i).getDescription(), events.get(i).getAlert(),events.get(i).getRequestCode());
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
            DateTimeFormatter formatter = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            }
            LocalDate datecheck = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                datecheck = LocalDate.parse(date, formatter);
            }
            int dayOfWeekNumber = 0;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                dayOfWeekNumber = datecheck.getDayOfWeek().getValue();
            }

            if(dayOfWeekNumber==7)
            {
                dayOfWeekNumber=0;
            }
            i.putExtra("token", token);
            i.putExtra("username",username);
            i.putExtra("date", date);
            i.putExtra("day",dayOfWeekNumber);
            i.putExtra("fullName",fullName);
            startActivity(i);
        });

        homeBtn.setOnClickListener(v ->  {
                Intent intent=new Intent(this, CalendarActivity.class);
                intent.putExtra("username",username);
                intent.putExtra("token",token);
                intent.putExtra("fullName",fullName);
                startActivity(intent);
        });

        ariaBtn.setOnClickListener(v ->  {
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


        ListView listview=findViewById(R.id.dayList);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DayListItem clickedDay = l.get(position);
                Intent i=new Intent(getApplicationContext(), EditEvent.class);
                i.putExtra("token",token);
                i.putExtra("username",username);
                i.putExtra("id",clickedDay.getId());
                i.putExtra("title",clickedDay.getTitle());
                i.putExtra("start",clickedDay.getTime());
                i.putExtra("end",clickedDay.getEnd());
                i.putExtra("alert",clickedDay.getAlerts());
                i.putExtra("description",clickedDay.getDescription());
                i.putExtra("date",date);
                i.putExtra("requestCode",clickedDay.getRequestCode());
                i.putExtra("fullName",fullName);
                startActivity(i);
            }
        });
    }
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


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
