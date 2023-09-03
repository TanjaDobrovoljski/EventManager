package com.example.eventmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import java.util.Locale;

public class SettingsActivity extends AppCompatActivity {

    private Spinner languageSpinner;
    private static final String NOTIFICATION_CHANNEL_ID = "event_manager_channel";

    private Switch notificationSwitch;
    private Spinner notificationDropdown;
    private DBHelper dbHelper;
    private static final String SELECTED_LANGUAGE = "Locale.Helper.Selected.Language";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        LanguageHelper.applyLanguage(this);



        dbHelper=new DBHelper(this);
        setSupportActionBar(findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        createNotificationChannel();

        // Initialize views
        languageSpinner = findViewById(R.id.languageSpinner);
        notificationSwitch = findViewById(R.id.notificationSwitch);
        notificationDropdown = findViewById(R.id.notificationDropdown);

        SharedPreferences preferences = getSharedPreferences("save", MODE_PRIVATE);
        SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(this);

      //  SharedPreferences preferences = getSharedPreferences("save", MODE_PRIVATE);
        boolean isNotificationSwitchChecked = preferences.getBoolean("value", false);

        // Set the state of the notification switch
        notificationSwitch.setChecked(isNotificationSwitchChecked);




        int selectedPosition = preferences.getInt("selectedPosition", -1);
        if(selectedPosition != -1) {
            // set the selected value of the spinner
            notificationDropdown.setSelection(selectedPosition);
        }

        // Set up language spinner
        ArrayAdapter<CharSequence> languageAdapter = ArrayAdapter.createFromResource(this,
                R.array.languages, android.R.layout.simple_spinner_item);
        languageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        languageSpinner.setAdapter(languageAdapter);
        int selectedPositionLang = sh.getInt("selectedLanguage", -1);
        if(selectedPositionLang != -1) {
            // set the selected value of the spinner
            languageSpinner.setSelection(selectedPositionLang);
        }

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
                SharedPreferences shPreferences = PreferenceManager.getDefaultSharedPreferences(SettingsActivity.this);
                String savedLanguage = shPreferences.getString(SELECTED_LANGUAGE, Locale.getDefault().getLanguage());
                String flag="";

                    if (selectedLanguage.equals("English") || selectedLanguage.equals("Engleski")) {
                        flag="en";
                        if (!flag.equals(savedLanguage)) {
                            setLocale( "en");
                            saveSelectedLanguage("en");


                        }
                    } else if (selectedLanguage.equals("Serbian") || selectedLanguage.equals("Srpski")) {
                        flag="sr";
                        if (!flag.equals(savedLanguage)) {
                            setLocale( "sr");
                            saveSelectedLanguage("sr");

                        }
                    }
                }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle when nothing is selected
            }
        });


    }

    private void saveNotificationSwitchState(boolean isChecked) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putBoolean("value", isChecked);

        if (isChecked) {
            int selectedPosition = notificationDropdown.getSelectedItemPosition();
            editor.putInt("selectedPosition", selectedPosition);
        }

        editor.apply();
    }

    private void saveSelectedLanguage(String language) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(SELECTED_LANGUAGE, language);
        int selected=languageSpinner.getSelectedItemPosition();
        editor.putInt("selectedLanguage",selected);
        editor.apply();
    }


    private void handleNotificationOption() {
        int selectedPosition = notificationDropdown.getSelectedItemPosition();
        if (selectedPosition > -1) {
            SharedPreferences preferences = getSharedPreferences("save", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("selectedPosition", selectedPosition);
            editor.apply();

            // Show notification based on the selected option
            String selectedOption = notificationDropdown.getItemAtPosition(selectedPosition).toString();
            if (selectedOption.equals("1 hour before") || selectedOption.equals("1 sat ranije")) {
                ArrayList<Activity> activities = dbHelper.getActivitiesForNextHour(getCurrentDate());
                showNotification(activities);
            } else if (selectedOption.equals("1 day before") || selectedOption.equals("1 dan ranije")) {
                ArrayList<Activity> activities = dbHelper.getActivitiesForDateRange(getCurrentDate(), 1);
                showNotification(activities);
            } else if (selectedOption.equals("7 days before") || selectedOption.equals("7 dana ranije")) {
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



    public void setLocale( String language) {
        // sacuvamo novi jezik u SharedPreferences
        SharedPreferences shPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = shPreferences.edit();
        editor.putString(SELECTED_LANGUAGE, language);
        editor.apply();

        // sacuvamo promjene u konfiguraciji aplikacije
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
        recreate();
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
        getSupportActionBar().setTitle(getString(R.string.settings));
        notificationSwitch.setText(getString(R.string.settings_notifications));
    }
}
