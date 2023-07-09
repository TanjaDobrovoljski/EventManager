package com.example.eventmanager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.widget.CalendarView;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class CalendarFragment extends Fragment {
    private MaterialCalendarView calendarView;

    private DBHelper dbHelper;
    public CalendarFragment(DBHelper dbHelper) {
        this.dbHelper=dbHelper;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);
        calendarView = view.findViewById(R.id.calendarView);


        List<Activity> activities = dbHelper.getAllActivities();
        // Add your logic to update the UI with the activities for the selected date

        for (Activity a:activities
        ) {
            // Add events to the CalendarView
            addEventToCalendar(a.getDate(),a.getColor());

            // Add more events as needed
        }
        return view;
    }


/*
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);
        calendarView = view.findViewById(R.id.calendarView);
        calendarView.setMinDate(System.currentTimeMillis() - 1000);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                List<Activity> activities = dbHelper.getAllActivities();
                // Add your logic to update the UI with the activities for the selected date

                for (Activity a:activities
                     ) {


                    String[] parts = a.getDate().split("-");
                    int y = Integer.parseInt(parts[0]);
                    int m = Integer.parseInt(parts[1]) ; // Subtract 1 to adjust for zero-based month in CalendarView
                    int d = Integer.parseInt(parts[2]);
                    long selectedDate = Long.parseLong(a.getDate());
                    calendarView.setDate(selectedDate);


                }

            }
        });

        return view;
    }*/
    private void addEventToCalendar(String date, int color) {
        String[] parts = date.split("-");
        int year = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1])-1 ;
        int dayOfMonth = Integer.parseInt(parts[2]);

        CalendarDay eventDate = CalendarDay.from(year, month, dayOfMonth);
        calendarView.addDecorator(new EventDecorator(color, eventDate));
    }

    private static class EventDecorator implements DayViewDecorator {
        private int color;
        private CalendarDay eventDate;

        public EventDecorator(int color, CalendarDay eventDate) {
            this.color = color;
            this.eventDate = eventDate;
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            return day.equals(eventDate);
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new DotSpan(20, color));
        }
    }


}
