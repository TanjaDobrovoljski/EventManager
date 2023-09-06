package com.example.eventmanager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class EditingFragment extends Fragment {
    private EditText nameEditText;
    private DBHelperCity dbHelperCity;
    private Button buttonSave, buttonCancel;
    private ImageView image1, image2;
    private String name= "";
    private String selectedDate="";
    private String selectedTime="";
    private String description= "";
    private String location= "";

    private EditText descriptionEditText;

    private CalendarView calendarView;
    private AutoCompleteTextView locationAutoCompleteTextView;

    private ArrayList<City> cities;

    private TextView hour, min;


    private Spinner hourSpinner;
    private Spinner minuteSpinner;

    private DBHelper dbHelper;
    Activity activity = null;
    String activityDate="";

    public EditingFragment(Activity a, DBHelper db) {
        this.activity = a;
        this.dbHelper = db;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_editing, container, false);

        dbHelperCity = new DBHelperCity(getContext());
        dbHelper = new DBHelper(getContext());

        int activityId=dbHelper.getActivityId(activity.getType(),activity.getName(),activity.getTime(),activity.getDescription(),activity.getCity(),activity.getDate());



        WelcomeActivity w = (WelcomeActivity) requireActivity();
        BottomNavigationView navigationView = w.findViewById(R.id.navigationView);
        if (navigationView.getVisibility() == View.VISIBLE)
            navigationView.setVisibility(View.GONE);
        if (w.findViewById(R.id.fab).getVisibility() == View.VISIBLE) {
            (w.findViewById(R.id.fab)).setVisibility(View.GONE);
        }

        setHasOptionsMenu(true);

        nameEditText = view.findViewById(R.id.activity_title);
        buttonCancel=view.findViewById(R.id.buttonCancel);
        buttonSave=view.findViewById(R.id.buttonSave);
        hour=view.findViewById(R.id.hour);
        min=view.findViewById(R.id.min);
        calendarView=view.findViewById(R.id.calendarView);

        descriptionEditText = view.findViewById(R.id.description);
        locationAutoCompleteTextView = view.findViewById(R.id.locationAutoCompleteTextView);
        hourSpinner = view.findViewById(R.id.hourSpinner);
        minuteSpinner = view.findViewById(R.id.minuteSpinner);
        image1 = view.findViewById(R.id.image1);
        image2 = view.findViewById(R.id.image2);

        if ("FREE".equals((activity.getType()))) {

            if(image1.getVisibility()==View.GONE)
                image1.setVisibility(View.VISIBLE);
            if(image2.getVisibility()==View.GONE)
                image2.setVisibility(View.VISIBLE);

            if (activity.getImage1() != null)
                image1.setImageBitmap(activity.getImage1());
            if (activity.getImage2() != null)
                image2.setImageBitmap(activity.getImage2());
        } else {

            if(image1.getVisibility()==View.VISIBLE)
                image1.setVisibility(View.GONE);
            if(image2.getVisibility()==View.VISIBLE)
                image2.setVisibility(View.GONE);
        }

        nameEditText.setText(activity.getName());
        descriptionEditText.setText(activity.getDescription());
        locationAutoCompleteTextView.setText(activity.getCity());
        String[] timeParts = activity.getTime().split(":");
        calendarView.setMinDate(System.currentTimeMillis() - 1000);

        cities=dbHelperCity.getAllCities();
        String[] locations = new String[cities.size()];

        int i=0;
        for (City c:cities)
        {
            locations[i]=c.getName();
            i++;
        }

        ArrayAdapter<String> locationAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, locations);
        locationAutoCompleteTextView.setAdapter(locationAdapter);


         activityDate = activity.getDate();

        String[] dateParts = activityDate.split("-");
        int year = Integer.parseInt(dateParts[0]);
        int month = Integer.parseInt(dateParts[1])-1; // Subtract 1 because Calendar month starts from 0 (January)
        int day = Integer.parseInt(dateParts[2]);

        Calendar currentDate = Calendar.getInstance();
        int currentYear = currentDate.get(Calendar.YEAR);
        int currentMonth = currentDate.get(Calendar.MONTH)+1;
        int currentDay = currentDate.get(Calendar.DAY_OF_MONTH);

        boolean isToday = (year == currentYear && month == currentMonth && day == currentDay);
       updateHourSpinner(isToday);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                // Retrieve the selected date
                month=month+1;
                activityDate="";
                activityDate = formatDateToDB(year, month , dayOfMonth);

                Calendar currentDate = Calendar.getInstance();
                int currentYear = currentDate.get(Calendar.YEAR);
                int currentMonth = currentDate.get(Calendar.MONTH)+1;
                int currentDay = currentDate.get(Calendar.DAY_OF_MONTH);


                // Check if the selected date is today
                boolean isToday = (year == currentYear && month == currentMonth && dayOfMonth == currentDay);
                updateHourSpinner(isToday);

            }
        });

        calendarView.setDate(getTimeInMillis(year, month, day));

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goBackToPreviousActivity();
            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isFormValid()) {
                    dbHelper.updateActivity(activityId, activity.getType(), name, selectedTime, description, location, activityDate);
                    Toast.makeText(view.getContext(), getString(R.string.edit_activity_toast), Toast.LENGTH_SHORT).show();
                    if(view.getContext() instanceof AddNewActivity) {
                        ((AddNewActivity) view.getContext()).getActivityA().getListFragment().refreshList();

                    }
                    goBackToPreviousActivity();
                }
                else
                    Toast.makeText(view.getContext(), getString(R.string.add_new_activity_toast_fields), Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void updateHourSpinner(boolean isToday) {
        int currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        int currentMin=Calendar.getInstance().get(Calendar.MINUTE);
        String[] timeParts = activity.getTime().split(":");
        String h= timeParts[0];
        String m=timeParts[1];

        if (isToday) {
            List<String> hours = new ArrayList<>();
            hourSpinner.setAdapter(null);
            minuteSpinner.setAdapter(null);
            // Show hours starting from the next hour
            for (int i = currentHour+ 1; i <= 23; i++) {
                hours.add(String.format("%02d", i));
            }


            ArrayAdapter<String> hourAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, hours);
            hourAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            hourSpinner.setAdapter(hourAdapter);

            List<String> minutes = new ArrayList<>();
            for (int i =currentMin+2; i <= 59; i++) {
                minutes.add(String.format("%02d", i));
            }
            ArrayAdapter<String> minuteAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, minutes);
            minuteAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            minuteSpinner.setAdapter(minuteAdapter);

        }
        else {
            hourSpinner.setAdapter(null);
            minuteSpinner.setAdapter(null);

            List<String> hours = new ArrayList<>();
            for (int i = 0; i <= 23; i++) {
                hours.add(String.format("%02d", i));
            }
            ArrayAdapter<String> hourAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, hours);
            hourAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            hourSpinner.setAdapter(hourAdapter);

            // Set up the minute spinner
            List<String> minutes = new ArrayList<>();
            for (int i = 0; i <= 59; i++) {
                minutes.add(String.format("%02d", i));
            }
            ArrayAdapter<String> minuteAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, minutes);
            minuteAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            minuteSpinner.setAdapter(minuteAdapter);

            int spinnerPosition = hourAdapter.getPosition(h);

            // on below line we are setting selection for our spinner to spinner position.
            hourSpinner.setSelection(spinnerPosition);
            int spinnerPosition2 = minuteAdapter.getPosition(m);

            // on below line we are setting selection for our spinner to spinner position.
            minuteSpinner.setSelection(spinnerPosition2);

        }


    }

    private long getTimeInMillis(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day, 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    @Override
    public void onPause() {
        super.onPause();

        WelcomeActivity w = (WelcomeActivity) requireActivity();
        BottomNavigationView navigationView = w.findViewById(R.id.navigationView);
        if (navigationView.getVisibility() == View.GONE)
            navigationView.setVisibility(View.VISIBLE);
        if (w.findViewById(R.id.fab).getVisibility() == View.GONE) {
            (w.findViewById(R.id.fab)).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        WelcomeActivity w = (WelcomeActivity) requireActivity();
        BottomNavigationView navigationView = w.findViewById(R.id.navigationView);
        if (navigationView.getVisibility() == View.GONE)
            navigationView.setVisibility(View.VISIBLE);
        if (w.findViewById(R.id.fab).getVisibility() == View.GONE) {
            (w.findViewById(R.id.fab)).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        LanguageHelper.applyLanguage(this.getContext());

        hour.setText(getString(R.string.add_activity_hours));
        min.setText(getString(R.string.add_activity_minutes));
        if("".equals(descriptionEditText.getText()))
            descriptionEditText.setHint(getString(R.string.add_activity_description));
        if("".equals(locationAutoCompleteTextView.getText()))
            locationAutoCompleteTextView.setHint(getString(R.string.add_activity_location));
        buttonSave.setText(getString(R.string.buttonSave));
        buttonCancel.setText(getString(R.string.buttonCancel));

    }

    private String formatDateToDB(int year, int month, int dayOfMonth) {
        // Ensure that the month and day are formatted with leading zeros if needed
        String formattedMonth = (month < 10) ? "0" + month : String.valueOf(month);
        String formattedDay = (dayOfMonth < 10) ? "0" + dayOfMonth : String.valueOf(dayOfMonth);

        // Combine the formatted components to get the final "yyyy-MM-dd" format
        return year + "-" + formattedMonth + "-" + formattedDay;
    }

    private void goBackToPreviousActivity() {
        Intent intent = new Intent(this.getContext(), WelcomeActivity.class);
        startActivity(intent);
    }

    public boolean isFormValid() {
        name = nameEditText.getText().toString().trim();
        description = descriptionEditText.getText().toString().trim();
        location =  locationAutoCompleteTextView.getText().toString().trim();
        String selectedHour = "";
        String selectedMinute = "";
        if (hourSpinner.getSelectedItem() != null) {
            selectedHour = hourSpinner.getSelectedItem().toString();
            selectedTime = selectedHour + ":";
        }

        if (minuteSpinner.getSelectedItem() != null) {
            selectedMinute = minuteSpinner.getSelectedItem().toString();
            selectedTime+= selectedMinute;
        }

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(selectedTime)) {
            return false;
        }

        return true;
    }
}