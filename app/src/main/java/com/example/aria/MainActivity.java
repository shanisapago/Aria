package com.example.aria;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.CalendarView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private CalendarView calendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        calendarView = findViewById(R.id.calendarView);

        // Wait for CalendarView to be laid out
        calendarView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // Find the button within the CalendarView by its ID or class
                Button monthNavButton = findMonthNavButton(calendarView);

                if (monthNavButton != null) {
                    // Attach touch listener to the button
                    monthNavButton.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            switch (event.getAction()) {
                                case MotionEvent.ACTION_DOWN:
                                    // Handle touch down event
                                    break;
                                case MotionEvent.ACTION_UP:
                                    // Handle touch up event
                                    break;
                            }
                            return true; // Return true to consume the event
                        }
                    });
                }

                // Remove the layout listener to prevent multiple calls
                calendarView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    // Example method to find and return the month navigation button
    private Button findMonthNavButton(CalendarView calendarView) {
        // Example: Find the button by ID
        int id = getResources().getIdentifier("android:id/next", null, null);
        return calendarView.findViewById(id);

        // Alternatively, find by traversing child views or using specific IDs/classes if known
    }
}


