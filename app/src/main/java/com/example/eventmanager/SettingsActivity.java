package com.example.eventmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;

import org.jetbrains.annotations.NotNull;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class SettingsActivity extends AppCompatActivity {

    private Spinner languageSpinner;
    private static final String NOTIFICATION_CHANNEL_ID = "event_manager_channel";

    private Switch notificationSwitch;
    private Spinner notificationDropdown;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        dbHelper=new DBHelper(this);
        setSupportActionBar(findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Podesavanja");
        createNotificationChannel();

        // Initialize views
        languageSpinner = findViewById(R.id.languageSpinner);
        notificationSwitch = findViewById(R.id.notificationSwitch);
        SharedPreferences preferences = getSharedPreferences("save", MODE_PRIVATE);
        boolean isNotificationSwitchChecked = preferences.getBoolean("value", false);

        // Set the state of the notification switch
        notificationSwitch.setChecked(isNotificationSwitchChecked);


        notificationDropdown = findViewById(R.id.notificationDropdown);

        int selectedPosition = preferences.getInt("selectedPosition", 0);
        notificationDropdown.setSelection(selectedPosition);


        // Set up language spinner
        ArrayAdapter<CharSequence> languageAdapter = ArrayAdapter.createFromResource(this,
                R.array.languages, android.R.layout.simple_spinner_item);
        languageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        languageSpinner.setAdapter(languageAdapter);

        // Set up notification dropdown spinner
        ArrayAdapter<CharSequence> notificationAdapter = ArrayAdapter.createFromResource(this,
                R.array.notification_options, android.R.layout.simple_spinner_item);
        notificationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        notificationDropdown.setAdapter(notificationAdapter);

        notificationDropdown.setVisibility(isNotificationSwitchChecked ? View.VISIBLE : View.GONE);

       /* notificationSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                notificationDropdown.setVisibility(View.VISIBLE);

                if (notificationDropdown.getSelectedItemPosition() > 0) {
                    showNotification();
                }
            } else {
                notificationDropdown.setVisibility(View.GONE);
            }
        });*/

        notificationDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedOption = parent.getItemAtPosition(position).toString();
                if (notificationSwitch.isChecked() && position >= 0) {
                   // ArrayList<Activity> activities = dbHelper.getActivitiesForDateRange(getCurrentDate(),7);
                    //showNotification(activities);
                    handleNotificationOption();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle when nothing is selected
            }
        });

        notificationSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                notificationDropdown.setVisibility(View.VISIBLE);
            } else {
                notificationDropdown.setVisibility(View.GONE);
            }
            saveNotificationSwitchState(isChecked);
        });

     /*  notificationDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (notificationSwitch.isChecked()) {
                    handleNotificationOption();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle when nothing is selected
            }
        });*/



        languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedLanguage = parent.getItemAtPosition(position).toString();
                // Handle language selection
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle when nothing is selected
            }
        });
    }

    private void saveNotificationSwitchState(boolean isChecked) {
        SharedPreferences preferences = getSharedPreferences("save", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("value", isChecked);

        if (isChecked) {
            int selectedPosition = notificationDropdown.getSelectedItemPosition();
            editor.putInt("selectedPosition", selectedPosition);
        }

        editor.apply();
        editor.apply();
    }


    private void handleNotificationOption() {
        int selectedPosition = notificationDropdown.getSelectedItemPosition();
        if (selectedPosition > -1) {
            // Save the selected position of the spinner in SharedPreferences
            SharedPreferences preferences = getSharedPreferences("save", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("selectedPosition", selectedPosition);
            editor.apply();

            // Show notification based on the selected option
            String selectedOption = notificationDropdown.getItemAtPosition(selectedPosition).toString();
            if (selectedOption.equals("1 hour before")) {
                ArrayList<Activity> activities = dbHelper.getActivitiesForNextHour(getCurrentDate());
                showNotification(activities);
            } else if (selectedOption.equals("1 day before")) {
                ArrayList<Activity> activities = dbHelper.getActivitiesForDateRange(getCurrentDate(), 1);
                showNotification(activities);
            } else if (selectedOption.equals("7 days before")) {
                ArrayList<Activity> activities = dbHelper.getActivitiesForDateRange(getCurrentDate(), 7);
                showNotification(activities);
            }
        }
    }



    private String getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return dateFormat.format(calendar.getTime());
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home)
        {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void showNotification(ArrayList<Activity> list)
    {
        if(list!= null) {
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            int i = 1;
            for (Activity a : list
            ) {
                NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_notification)
                        .setContentTitle("Event Manager")
                        .setContentText(a.getName() + " on:" + a.getDate() + " in " + a.getCity())
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                // Show the notification
                notificationManager.notify(i, builder.build());
                i++;
            }
        }

    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My notification", importance);
            channel.setDescription("neki opis,nesto");
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}
