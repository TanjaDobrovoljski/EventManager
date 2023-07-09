package com.example.eventmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class WelcomeActivity extends AppCompatActivity  {

    BottomNavigationView navigationView;
    DBHelper dbHelper=new DBHelper(this);
    DBHelperCity dbHelperCity=new DBHelperCity(this);
    FloatingActionButton fab;
    private ListFragment listFragment;
    private CalendarFragment calendarFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

      //dbHelper.deleteAll();
        dbHelperCity.deleteAllCities();

        dbHelperCity.insertCity("London",51.509865,-0.118092);
        dbHelperCity.insertCity("Banja Luka",44.772182,17.191000);
        dbHelperCity.insertCity("Belgrade",44.786568,20.448922);
        dbHelperCity.insertCity("Zagreb",45.815011,15.981919);
        dbHelperCity.insertCity("Paris",-6.889043,107.596066);
        dbHelperCity.insertCity("Prague",50.075538,14.437800);
        dbHelperCity.insertCity("Sarajevo",43.856430,18.413029);
        dbHelperCity.insertCity("Rome",41.902782,12.496366);
        dbHelperCity.insertCity("Madrid",40.416775,-3.703790);

        setSupportActionBar(findViewById(R.id.toolbar));
        getSupportActionBar().setTitle(getString(R.string.app_name));

        ActivityHolder.setActivityA(this);

        navigateToListFragment();
        fab=findViewById(R.id.fab);
        FloatingActionButton fab = findViewById(R.id.fab);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(WelcomeActivity.this, fab);
                popupMenu.getMenuInflater().inflate(R.menu.fab_main, popupMenu.getMenu());

                // Set a click listener on the popup menu items
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        // Handle menu item click events here
                        switch (menuItem.getItemId()) {
                            case R.id.freeTime:
                                openAddNewActivityWithFragment(1);
                                return true;
                            case R.id.work:
                                openAddNewActivityWithFragment(2);
                                return true;
                            case R.id.travel:
                                openAddNewActivityWithFragment(3);
                                return true;
                            default:
                                return false;
                        }
                    }
                });

                // Show the popup menu
                popupMenu.show();
            }
        });


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

    private void openAddNewActivityWithFragment(int fragmentId) {
        Intent intent = new Intent(WelcomeActivity.this, AddNewActivity.class);
        intent.putExtra("fragmentId", fragmentId);
        startActivity(intent);
    }
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main,menu);
        return  true;
    }


    public void navigateToListFragment() {
         listFragment = new ListFragment(dbHelper);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.nav_host_fragment, listFragment)
                .commit();
    }

    public ListFragment getListFragment() {
        return listFragment;
    }

    public void setListFragment(ListFragment listFragment) {
        this.listFragment = listFragment;
    }

    private void navigateToCalendarFragment() {
         calendarFragment = new CalendarFragment(dbHelper);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.nav_host_fragment, calendarFragment)
                .commit();
    }


}