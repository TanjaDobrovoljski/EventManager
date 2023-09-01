package com.example.eventmanager;
import android.graphics.Bitmap;
import android.os.Bundle;


import androidx.core.content.ContextCompat;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TravelFragment extends Fragment {

    private EditText nameEditText;
    DBHelper dbHelper;
    DBHelperCity dbHelperCity;
    private TextView hour,min;
    private CalendarView calendarView;
    private AutoCompleteTextView locationAutoCompleteTextView;
    private EditText descriptionEditText;
    private EditText locationEditText;
    private Bitmap imageBitMap1,imageBitMap2;
    private Spinner hourSpinner;
    private Spinner minuteSpinner;

    private Button buttonAdd;

    private String name= "";
    private String selectedDate="";
    private String selectedTime="";
    private String description= "";
    private String location= "";
    private String selectedHour = "";
    private String selectedMinute = "";
    private  ArrayList<City> cities;

    public DBHelperCity getDbHelperCity() {
        return dbHelperCity;
    }

    public void setDbHelperCity(DBHelperCity dbHelperCity) {
        this.dbHelperCity = dbHelperCity;
    }

    public TravelFragment(DBHelper dbHelper,DBHelperCity dbHelperCity) {
        this.dbHelper=dbHelper;
        this.dbHelperCity=dbHelperCity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_work, container, false);

        nameEditText = view.findViewById(R.id.nameEditText);
        buttonAdd=view.findViewById(R.id.buttonAdd);
        hour=view.findViewById(R.id.hour);
        min=view.findViewById(R.id.min);


        buttonAdd.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View view) {

                                             if(!isFormValid())
                                                 Toast.makeText(view.getContext(), "Niste popunili sva polja!", Toast.LENGTH_SHORT).show();
                                             else {
                                                 if( dbHelper.insertActivity("TRAVEL",name,selectedTime,description,location,selectedDate,imageBitMap1,imageBitMap2, ContextCompat.getColor(getContext(), R.color.primary_triadic_two))) {
                                                     Toast.makeText(view.getContext(), "Uspijesno dodana aktivnost!", Toast.LENGTH_SHORT).show();
                                                     if(view.getContext() instanceof AddNewActivity)
                                                         ((AddNewActivity) view.getContext()).getActivityA().getListFragment().refreshList();
                                                     goBackToPreviousActivity();

                                                 }
                                                 }


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
                selectedDate = formatDateToDB(year, month , dayOfMonth);

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
        locationAutoCompleteTextView = view.findViewById(R.id.locationAutoCompleteTextView);
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


        return view;
    }

    private String formatDateToDB(int year, int month, int dayOfMonth) {
        // Ensure that the month and day are formatted with leading zeros if needed
        String formattedMonth = (month < 10) ? "0" + month : String.valueOf(month);
        String formattedDay = (dayOfMonth < 10) ? "0" + dayOfMonth : String.valueOf(dayOfMonth);

        // Combine the formatted components to get the final "yyyy-MM-dd" format
        return year + "-" + formattedMonth + "-" + formattedDay;
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
         name = nameEditText.getText().toString().trim();


         description = descriptionEditText.getText().toString().trim();
        location = locationAutoCompleteTextView.getText().toString().trim();

        selectedHour = "";
         selectedMinute = "";
        if (hourSpinner.getSelectedItem() != null) {
            selectedHour = hourSpinner.getSelectedItem().toString();
            selectedTime = selectedHour + ":";
        }

        if (minuteSpinner.getSelectedItem() != null) {
            selectedMinute = minuteSpinner.getSelectedItem().toString();
            selectedTime+= selectedMinute;
        }

        if("".equals(selectedDate))
        {
            Calendar currentDate = Calendar.getInstance();
            int year = currentDate.get(Calendar.YEAR);
            int month = currentDate.get(Calendar.MONTH);
            int dayOfMonth = currentDate.get(Calendar.DAY_OF_MONTH);
            // Retrieve the selected date
            month = month + 1;
            selectedDate = formatDateToDB(year, month, dayOfMonth);
        }


        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(selectedTime)
                || TextUtils.isEmpty(location)) {
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

    private void goBackToPreviousActivity() {
        if (getActivity() != null) {
            getActivity().onBackPressed();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        LanguageHelper.applyLanguage(this.getContext());
        nameEditText.setHint(getString(R.string.add_activity_title));
        hour.setText(getString(R.string.add_activity_hours));
        min.setText(getString(R.string.add_activity_minutes));
        descriptionEditText.setHint(getString(R.string.add_activity_description));
        locationAutoCompleteTextView.setHint(getString(R.string.add_activity_location));
        buttonAdd.setText(getString(R.string.button_add));

    }

}
