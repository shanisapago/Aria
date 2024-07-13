package com.example.aria;

import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.HashSet;

public class CalendarActivity2 extends AppCompatActivity implements CustomCalendarView2.OnMonthChangedListener {

    private static final String TAG = "CalendarActivity2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        CustomCalendarView2 calendarView = findViewById(R.id.calendarView);
        calendarView.setOnMonthChangedListener(this);

        // Set event days
        HashSet<Integer> eventDays = new HashSet<>();
        eventDays.add(4); // Add event days here
        calendarView.setEventDays(eventDays);

    }

    @Override
    public void onMonthChanged(int newMonth, int newYear) {
        Log.d(TAG, "Month changed to: " + newMonth + " " + newYear);
        System.out.println("1");
        String monthName = getMonthName(newMonth);
        Toast.makeText(this, "Month changed to: " + monthName + " " + newYear, Toast.LENGTH_SHORT).show();
    }

    private String getMonthName(int month) {
        System.out.println("2");
        return new java.text.DateFormatSymbols().getMonths()[month];
    }

}
