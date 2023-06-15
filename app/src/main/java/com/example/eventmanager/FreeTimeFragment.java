package com.example.eventmanager;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class FreeTimeFragment extends Fragment {

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
        View view = inflater.inflate(R.layout.fragment_free_time, container, false);

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
        List<String> hours = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
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
                 selectedDate = year + "-" + (month + 1) + "-" + dayOfMonth;

                // Do something with the selected date
                // ...
            }
        });

        descriptionEditText = view.findViewById(R.id.descriptionEditText);
        locationEditText = view.findViewById(R.id.locationEditText);

        return view;
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

        String selectedHour = hourSpinner.getSelectedItem().toString();
        String selectedMinute = minuteSpinner.getSelectedItem().toString();
         selectedTime = selectedHour + ":" + selectedMinute;



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
