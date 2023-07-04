package com.example.eventmanager;
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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class WorkFragment extends Fragment {

    private EditText nameEditText;

    private CalendarView calendarView;
    private EditText descriptionEditText;
    private EditText locationEditText;
    String selectedDate;
    private Spinner hourSpinner;
    private Spinner minuteSpinner;
    String selectedTime;
    private Button buttonAdd;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_work, container, false);

        nameEditText = view.findViewById(R.id.nameEditText);
        buttonAdd=view.findViewById(R.id.buttonAdd);


        buttonAdd.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View view) {

                                             if(!isFormValid())
                                                 Toast.makeText(view.getContext(), "Niste popunili sva polja!", Toast.LENGTH_SHORT).show();
                                         }
                                     }
        );

        hourSpinner = view.findViewById(R.id.hourSpinner);
        minuteSpinner = view.findViewById(R.id.minuteSpinner);

        // Set up the hour spinner

        calendarView = view.findViewById(R.id.calendarView);
        calendarView.setMinDate(System.currentTimeMillis() - 1000);

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

        calendarView = view.findViewById(R.id.calendarView);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                // Retrieve the selected date
                month=month+1;
                selectedDate = year + "-" + month + "-" + dayOfMonth;

                Calendar currentDate = Calendar.getInstance();
                int currentYear = currentDate.get(Calendar.YEAR);
                int currentMonth = currentDate.get(Calendar.MONTH)+1;
                int currentDay = currentDate.get(Calendar.DAY_OF_MONTH);


                // Check if the selected date is today
                boolean isToday = (year == currentYear && month == currentMonth && dayOfMonth == currentDay);

                // Update the hour spinner based on the selected date
                updateHourSpinner(isToday);
            }
        });

        descriptionEditText = view.findViewById(R.id.descriptionEditText);
        AutoCompleteTextView locationAutoCompleteTextView = view.findViewById(R.id.locationAutoCompleteTextView);

        City c1=new City("London",51.509865,-0.118092);
        City c2=new City("Banja Luka",44.772182,17.191000);
        City c3=new City("Belgrade",44.786568,20.448922);
        City c4=new City("Zagreb",45.815011,15.981919);
        City c5=new City("Paris",-6.889043,107.596066);
        City c6=new City("Prague",50.075538,14.437800);
        City c7=new City("Sarajevo",43.856430,18.413029);
        City c8=new City("Rome",41.902782,12.496366);
        City c9=new City("Madrid",40.416775,-3.703790);

        String[] locations = {c1.getName(),c2.getName(),c3.getName(),c4.getName(),c5.getName(), c6.getName(), c7.getName(), c8.getName(),c9.getName()}; // Replace with your list of locations
        ArrayAdapter<String> locationAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, locations);
        locationAutoCompleteTextView.setAdapter(locationAdapter);


        return view;
    }

    private void updateHourSpinner(boolean isToday) {
        int currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);

        if (isToday) {
            List<String> hours = new ArrayList<>();
            hourSpinner.setAdapter(null);
            minuteSpinner.setAdapter(null);
            // Show hours starting from the next hour
            for (int i = currentHour + 1; i <= 23; i++) {
                hours.add(String.format("%02d", i));
            }


            ArrayAdapter<String> hourAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, hours);
            hourAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            hourSpinner.setAdapter(hourAdapter);

            List<String> minutes = new ArrayList<>();
            for (int i = 0; i <= 59; i++) {
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
        }
    }

    public void showTimeDropdown(View view) {
        // Display the time dropdown when the time EditText is clicked
        hourSpinner.performClick();
        minuteSpinner.performClick();
    }

    public boolean isFormValid() {
        String name = nameEditText.getText().toString().trim();


        String description = descriptionEditText.getText().toString().trim();
        String location = locationEditText.getText().toString().trim();
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




        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(selectedTime)
                || TextUtils.isEmpty(description) || TextUtils.isEmpty(location)) {
            return false; // At least one field is empty
        }

        return true; // All fields are filled
    }

    public String getName() {
        return nameEditText.getText().toString().trim();
    }

    public String getTime() {
        return selectedTime;
    }

    public String getDate() {
        return selectedDate;
    }

    public String getDescription() {
        return descriptionEditText.getText().toString().trim();
    }

    public String getLocation() {
        return locationEditText.getText().toString().trim();
    }



}
