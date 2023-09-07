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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import android.widget.CalendarView;
import android.widget.Toast;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CalendarFragment extends Fragment {
    private MaterialCalendarView calendarView;

    private DBHelper dbHelper;
    private RecyclerView list;
    private ActivityAdapter adapter;
    private List<Activity> allActivities;
    public CalendarFragment(DBHelper dbHelper) {
        this.dbHelper=dbHelper;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        calendarView = view.findViewById(R.id.calendarView);
        list=view.findViewById(R.id.activityListView);

        List<Activity> activities = dbHelper.getAllActivities();

        for (Activity a:activities
        ) {
            addEventToCalendar(a.getDate(),a.getColor());
        }
        adapter = new ActivityAdapter(new ArrayList<>());
        list.setLayoutManager(new LinearLayoutManager(getActivity()));

        list.setAdapter(adapter);

        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(
                    @NonNull MaterialCalendarView widget,
                    @NonNull CalendarDay date,
                    boolean selected
            ) {

                List<Activity> selectedActivities = dbHelper.getActivitiesForDate(calendarDayToDate(date));

                adapter.setActivities(selectedActivities);
                if (selectedActivities.isEmpty()) {
                    list.setVisibility(View.GONE);
                } else {
                    list.setVisibility(View.VISIBLE);
                }

            }
        });

        adapter.setOnItemClickListener(new ActivityAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Activity activity) {
                // Open NewsArticleFragment on item click
                ActivityDetails detailsArticleFragment = new ActivityDetails();
                Bundle bundle = new Bundle();

                WelcomeActivity welcomeActivity = (WelcomeActivity) requireActivity();
                welcomeActivity.setItemClicked(true);
                welcomeActivity.setSearchBoxVisibility(false);


                bundle.putParcelable("activity", activity);

                bundle.putString("fragmentType", "FreeTimeFragment");

                detailsArticleFragment.setArguments(bundle);
                getParentFragmentManager()
                        .beginTransaction()
                        .replace(R.id.nav_host_fragment, detailsArticleFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });


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

    public String calendarDayToDate(CalendarDay calendarDay) {
        int year = calendarDay.getYear();
        int month = calendarDay.getMonth() + 1; // Note: CalendarDay uses zero-based months
        int day = calendarDay.getDay();

        // Format the date as "yyyy-MM-dd"
        return String.format("%04d-%02d-%02d", year, month, day);
    }


}
