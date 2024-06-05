package com.example.aria;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import com.example.aria.RetroFitClasses.NewEvent;
import com.example.aria.RetroFitClasses.UsersAPI;
import com.example.aria.adapters.DayListAdapter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Day extends AppCompatActivity {

    ListView dayList;
    TextView calendarTitle;
    Calendar currentCalendar;
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

        currentCalendar = Calendar.getInstance();

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        //Calendar calendar = Calendar.getInstance();

        try {
            currentCalendar.setTime(sdf.parse(givenDate));
        } catch (Exception e) {
            e.printStackTrace();
        }
                // Get current date
                int year = Integer.parseInt(getIntent().getExtras().getString("year"));
                int month = Integer.parseInt(getIntent().getExtras().getString("month"));
                int day = Integer.parseInt(getIntent().getExtras().getString("day"));

        currentCalendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY); // Ensure it starts on Sunday
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
        // Update the calendar title
        //SimpleDateFormat sdf = new SimpleDateFormat("MMMM, yyyy", Locale.getDefault());
        //calendarTitle.setText(sdf.format(currentCalendar.getTime()));

        // Update the weekly calendar dates
        updateCalendarTitle(day);
        updateWeekDates(day);
    }

    private void updateCalendarTitle(int day) {
        // Clone the current calendar and set to the start of the week (Sunday)
        Calendar startOfWeekCalendar = (Calendar) currentCalendar.clone();
        startOfWeekCalendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);

        // Calculate the end of the week
        Calendar endOfWeekCalendar = (Calendar) startOfWeekCalendar.clone();
        endOfWeekCalendar.add(Calendar.DAY_OF_WEEK, 6);

        // Determine the correct month and year for the title
        String monthYearTitle;
        SimpleDateFormat monthYearFormat = new SimpleDateFormat("MMMM, yyyy", Locale.getDefault());

        if (startOfWeekCalendar.get(Calendar.MONTH) == endOfWeekCalendar.get(Calendar.MONTH)) {
            // If the week is within the same month
            monthYearTitle = monthYearFormat.format(startOfWeekCalendar.getTime());
        } else {
            // If the week spans across two different months
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
        // Array of TextView IDs
        int[] daysOfWeek = {
                R.id.sunday_date,
                R.id.monday_date,
                R.id.tuesday_date,
                R.id.wednesday_date,
                R.id.thursday_date,
                R.id.friday_date,
                R.id.saturday_date
        };

        // Set the calendar to the start of the week (Sunday)
        Calendar weekCalendar = (Calendar) currentCalendar.clone();
        weekCalendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);

        // Loop through the days and set the date (day of month)
        SimpleDateFormat dayFormat = new SimpleDateFormat("d", Locale.getDefault());
        for (int i = 0; i < daysOfWeek.length; i++) {
            TextView dateView = findViewById(daysOfWeek[i]);
            dateView.setText(dayFormat.format(weekCalendar.getTime()));
            weekCalendar.add(Calendar.DAY_OF_MONTH, 1);

            int index = ((String)calendarTitle.getText()).indexOf(',');
            String month = ((String)calendarTitle.getText()).substring(0,index);


            String newMonth;
            switch (month){
                case "January":
                    newMonth = "01";
                    break;
                case "February":
                    newMonth = "02";
                    break;
                case "March":
                    newMonth = "03";
                    break;
                case "April":
                    newMonth = "04";
                    break;
                case "May":
                    newMonth = "05";
                    break;
                case "June":
                    newMonth = "06";
                    break;
                case "July":
                    newMonth = "07";
                    break;
                case "August":
                    newMonth = "08";
                    break;
                case "September":
                    newMonth = "09";
                    break;
                case "October":
                    newMonth = "10";
                    break;
                case "November":
                    newMonth = "11";
                    break;
                default:
                    newMonth = "12";
            }

            String newDay = String.valueOf(day);
            String chosenDate;
            if(day/10==0)
                newDay = "0".concat(String.valueOf(day));
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
        //String date = getIntent().getExtras().getString("date");
        dayList=findViewById(R.id.dayList);
        List<DayListItem> l=new ArrayList<>();
        UsersAPI usersAPI=new UsersAPI();
        List<NewEvent> events = usersAPI.getEvents(token);

        if(events!=null) {


            for (int i = 0; i < events.size(); i++) {
                //for(int j=0;j<events.get(i).getPhoneNumbers().size();j++)
                //{
                //    System.out.println(events.get(i).getPhoneNumbers().get(j));
               // }


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
            i.putExtra("username",username);
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
                i.putExtra("username",username);
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
