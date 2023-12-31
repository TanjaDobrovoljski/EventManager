package com.example.eventmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class WelcomeActivity extends AppCompatActivity  {

    private static final String SELECTED_LANGUAGE = "Locale.Helper.Selected.Language";
    BottomNavigationView navigationView;
    DBHelper dbHelper=new DBHelper(this);
    DBHelperCity dbHelperCity=new DBHelperCity(this);
    FloatingActionButton fab;
    private ListFragment listFragment;
    private AutoCompleteTextView searchBox;
    private List<Activity> allActivities; // Replace this with your list of activities
    private ArrayAdapter<Activity> adapter;
    private CalendarFragment calendarFragment;
    private boolean isItemClicked = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

       // LanguageHelper.applyLanguage(this);

        searchBox=findViewById(R.id.searchBox);
        searchBox.setVisibility(View.GONE);

        allActivities = dbHelper.getAllActivities(); // Initialize your list here
        // ...

        // Setup the adapter for the AutoCompleteTextView
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, allActivities);
        searchBox.setAdapter(adapter);


       /* searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Not used
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Filter the list based on the current search query
                filterList(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Not used
            }
        });

        */

        searchBox.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    // User clicked the enter key, so hide the soft keyboard
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);

                    // Perform filtering on the list based on the current search query
                    filterList(searchBox.getText().toString());

                    return true;
                }
                return false;
            }
        });

      //dbHelper.deleteAll();
        dbHelperCity.deleteAllCities();

       // dbHelper.insertActivity("nesto","nesto,","nesto","nesto","nesto","nesto",null,null,4362784);

        dbHelperCity.insertCity("London",51.509865,-0.118092);
        dbHelperCity.insertCity("Banja Luka",44.772182,17.191000);
        dbHelperCity.insertCity("Belgrade",44.786568,20.448922);
        dbHelperCity.insertCity("Zagreb",45.815011,15.981919);
        dbHelperCity.insertCity("Paris",48.864716,2.349014);
        dbHelperCity.insertCity("Prague",50.075538,14.437800);
        dbHelperCity.insertCity("Sarajevo",43.856430,18.413029);
        dbHelperCity.insertCity("Rome",41.902782,12.496366);
        dbHelperCity.insertCity("Madrid",40.416775,-3.703790);
        dbHelperCity.insertCity("Los Angeles",34.052235,-118.243683);
        dbHelperCity.insertCity("Mykonos",37.450001,25.350000);
        dbHelperCity.insertCity("St Petersburg",59.937500,30.308611);
        dbHelperCity.insertCity("Innsbruck",47.259659,11.400375);
        dbHelperCity.insertCity("Salzburg",47.811195,13.033229);

        setSupportActionBar(findViewById(R.id.toolbar));
        getSupportActionBar().setTitle(getString(R.string.app_name));


        ActivityHolder.setActivityA(this);

        navigateToListFragment();
        fab=findViewById(R.id.fab);
        FloatingActionButton fab = findViewById(R.id.fab);

        if(fab.getVisibility()==View.GONE)
            fab.setVisibility(View.VISIBLE);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(WelcomeActivity.this, fab);
                popupMenu.getMenuInflater().inflate(R.menu.fab_main, popupMenu.getMenu());
                if (searchBox.getVisibility() == View.VISIBLE)
                    searchBox.setVisibility(View.GONE);

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
        if(navigationView.getVisibility()==View.GONE)
            navigationView.setVisibility(View.VISIBLE);
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (searchBox.getVisibility() == View.VISIBLE)
                    searchBox.setVisibility(View.GONE);
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

    private void filterList(String query) {
        List<Activity> filteredList = new ArrayList<>();
        for (Activity activity : allActivities) {
            if (activity.getName().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(activity);
            }
        }

        if (filteredList.isEmpty()) {
            // Perform partial search on activity names
            for (Activity activity : allActivities) {
                String[] activityNameParts = activity.getName().toLowerCase().split(" ");
                for (String part : activityNameParts) {
                    if (part.contains(query.toLowerCase())) {
                        filteredList.add(activity);

                    }
                }
            }
        }

        adapter.clear();
        adapter.addAll(filteredList);
        adapter.notifyDataSetChanged();

        if (!filteredList.isEmpty()) {
            listFragment = new ListFragment(filteredList);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.nav_host_fragment, listFragment)
                    .commit();
        }
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();

        if(id==R.id.app_settings)
        {
            setTitle(getString(R.string.menu_settings));
            Intent intent=new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        else if(id==R.id.about_app)
        {
            setTitle(getString(R.string.menu_aboutApp));
            Intent intent=new Intent(this, AboutAppActivity.class);
            startActivity(intent);
            return true;
        }
        else if(id==R.id.app_search)
        {
            if (searchBox.getVisibility() == View.VISIBLE) {
                searchBox.setVisibility(View.GONE);
                navigateToListFragment();
            } else {
                searchBox.setVisibility(View.VISIBLE);
            }
            return true;
        }
        return  super.onOptionsItemSelected(item);
    }

    public void navigateToListFragment() {
         listFragment = new ListFragment(dbHelper);
        if (searchBox.getVisibility() == View.VISIBLE)
            searchBox.setVisibility(View.GONE);
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
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // Disable menu options if an item is clicked
        menu.findItem(R.id.app_settings).setVisible(!isItemClicked);
        menu.findItem(R.id.app_settings).setTitle(getString(R.string.menu_settings));
        menu.findItem(R.id.about_app).setVisible(!isItemClicked);
        menu.findItem(R.id.about_app).setTitle(getString(R.string.menu_aboutApp));


        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        if (currentFragment instanceof CalendarFragment) {
            menu.findItem(R.id.app_search).setVisible(false);
        } else {
            menu.findItem(R.id.app_search).setVisible(!isItemClicked);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    // Method to reset the item clicked flag
    public void setItemClicked(boolean clicked) {
        isItemClicked = clicked;
        invalidateOptionsMenu(); // Update the options menu to reflect the change
    }
    public void setSearchBoxVisibility(boolean isVisible) {
        if (isVisible) {
            searchBox.setVisibility(View.VISIBLE);
        } else {
            searchBox.setVisibility(View.GONE);
        }
    }
    private void navigateToCalendarFragment() {
        if (searchBox.getVisibility() == View.VISIBLE)
            searchBox.setVisibility(View.GONE);

         calendarFragment = new CalendarFragment(dbHelper);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.nav_host_fragment, calendarFragment)
                .commit();
    }


    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }


    @Override
    protected void attachBaseContext(Context base) {
        // uzimamo trenutni jezik iz SharedPreferences-a
        SharedPreferences shPreferences = PreferenceManager.getDefaultSharedPreferences(base);
        String lang = shPreferences.getString(SELECTED_LANGUAGE, Locale.getDefault().getLanguage());

        // sacuvamo promjene u konfiguraciji aplikacije
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        base.getResources().updateConfiguration(config,
                base.getResources().getDisplayMetrics());
        super.attachBaseContext(base);

    }


    @Override
    protected void onResume() {
        super.onResume();

        LanguageHelper.applyLanguage(this);
        if (searchBox.getVisibility() == View.VISIBLE)
            searchBox.setVisibility(View.GONE);
        getSupportActionBar().setTitle(getString(R.string.app_name));
        searchBox.setHint(getString(R.string.search_activities));
        Menu menu = navigationView.getMenu();
        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
            switch (item.getItemId()) {
                case R.id.activitiesList:
                    item.setTitle(R.string.list_view);
                    break;
                case R.id.activitiesCalendar:
                    item.setTitle(R.string.calendar_view);
                    break;
                // Add more items as needed
            }
        }


        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
            switch (item.getItemId()) {
                case R.id.freeTime:
                    item.setTitle(R.string.add_freetime);
                    break;
                case R.id.work:
                    item.setTitle(R.string.add_work);
                    break;
                case R.id.travel:
                    item.setTitle(R.string.add_travel);
                    break;
            }
        }


        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
            switch (item.getItemId()) {
                case R.id.app_settings:
                    item.setTitle(R.string.menu_settings);
                    break;
                case R.id.about_app:
                    item.setTitle(R.string.menu_aboutApp);
                    break;

            }
        }


    }

}