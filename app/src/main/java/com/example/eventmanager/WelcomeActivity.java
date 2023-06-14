package com.example.eventmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class WelcomeActivity extends AppCompatActivity {

    BottomNavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        setSupportActionBar(findViewById(R.id.toolbar));
        getSupportActionBar().setTitle(getString(R.string.app_name));
        navigateToListFragment();
        navigationView=findViewById(R.id.navigationView);
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.activitiesList:
                        // Navigate to the ListFragment
                        navigateToListFragment();
                        return true;
                    case R.id.activitiesCalendar:
                        // Navigate to the CalendarFragment
                        navigateToCalendarFragment();
                        return true;
                }
                return false;
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main,menu);

        return  true;
    }


    private void navigateToListFragment() {
        ListFragment listFragment = new ListFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.nav_host_fragment, listFragment)
                .commit();
    }

    private void navigateToCalendarFragment() {
        CalendarFragment calendarFragment = new CalendarFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.nav_host_fragment, calendarFragment)
                .commit();
    }


}