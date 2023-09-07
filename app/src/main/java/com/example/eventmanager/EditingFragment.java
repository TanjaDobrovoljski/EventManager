package com.example.eventmanager;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
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
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class EditingFragment extends Fragment {
    private EditText nameEditText;
    private DBHelperCity dbHelperCity;
    private Button buttonSave, buttonCancel;
    private ImageView image1, image2;
    private static final int CAMERA_REQ_CODE = 1;
    private static final int GALLERY_REQ_CODE = 2;
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
    private Bitmap imageBitMap1,imageBitMap2;

    private int currentImage;
    private Spinner hourSpinner;
    private Spinner minuteSpinner;

    private DBHelper dbHelper;
    Activity activity = null;
    String activityDate="";
    WelcomeActivity w;
    BottomNavigationView navigationView;

    private static final String API_KEY = "QWbBcpE6Sb1dsPpWTIt29e7puN3daasrXHBIucDx0qEGdgwxJijP5Mrl"; //6d2c8722de764a277fe0e1de101e296e

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



       w = (WelcomeActivity) requireActivity();
         navigationView = w.findViewById(R.id.navigationView);
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
        imageBitMap1=activity.getImage1();
        imageBitMap2=activity.getImage2();

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
        int month = Integer.parseInt(dateParts[1])-1;
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
                    dbHelper.updateActivity(activityId, activity.getType(), name, selectedTime, description, location, activityDate,imageBitMap1,imageBitMap2);
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

        image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(getString(R.string.Image_source));
                builder.setItems(new CharSequence[]{getString(R.string.gallery),getString(R.string.camera),"Internet"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            if(ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                                Intent galleryIntent = new Intent(Intent.ACTION_PICK);
                                currentImage = 1;
                                galleryIntent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                                startActivityForResult(galleryIntent, GALLERY_REQ_CODE);
                            }
                            else {
                                // Request the gallery permission
                                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, GALLERY_REQ_CODE);
                            }
                        } else if(which==1){
                            // Open camera

                            if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                currentImage = 1;
                                startActivityForResult(cameraIntent, CAMERA_REQ_CODE);

                            } else {
                                // Request the camera permission
                                ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.CAMERA}, CAMERA_REQ_CODE);
                            }

                        }
                        else {
                            currentImage=1;
                            buildImageUrl(String.valueOf(nameEditText.getText()));
                        }
                    }
                });
                builder.show();
            }
        });


        image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(getString(R.string.Image_source));
                builder.setItems(new CharSequence[]{getString(R.string.gallery), getString(R.string.camera),"Internet"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            if(ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                                Intent galleryIntent = new Intent(Intent.ACTION_PICK);
                                currentImage = 1;
                                galleryIntent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                                startActivityForResult(galleryIntent, GALLERY_REQ_CODE);
                            }
                            else {
                                // Request the gallery permission
                                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, GALLERY_REQ_CODE);
                            }
                        } else if(which==1){
                            // Open camera

                            if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                currentImage = 2;
                                startActivityForResult(cameraIntent, CAMERA_REQ_CODE);

                            } else {
                                // Request the camera permission
                                ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.CAMERA}, CAMERA_REQ_CODE);
                            }

                        }
                        else {
                            currentImage=2;
                            buildImageUrl(String.valueOf(nameEditText.getText()));

                        }
                    }
                });
                builder.show();
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

        String formattedMonth = (month < 10) ? "0" + month : String.valueOf(month);
        String formattedDay = (dayOfMonth < 10) ? "0" + dayOfMonth : String.valueOf(dayOfMonth);

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

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(selectedTime) || TextUtils.isEmpty(description) || TextUtils.isEmpty(location)) {
            return false; // At least one field is empty
        }

        return true;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode,  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == GALLERY_REQ_CODE) {
                Uri selectedImageUri = data.getData();


                if (selectedImageUri != null) {

                    if (currentImage == 1) {
                        try {
                            // Retrieve the selected image as a Bitmap
                            imageBitMap1 = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), selectedImageUri);

                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        image1.setImageBitmap(imageBitMap1);
                    } else if (currentImage == 2) {
                        try {
                            // Retrieve the selected image as a Bitmap
                            imageBitMap2 = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), selectedImageUri);

                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        image2.setImageBitmap(imageBitMap2);
                    }
                }
            } else if (requestCode == CAMERA_REQ_CODE) {
                Bundle extras = data.getExtras();
                Bitmap capturedImage = (Bitmap) extras.get("data");

                if (currentImage == 1) {
                    imageBitMap1 = capturedImage;
                    image1.setImageBitmap(imageBitMap1);
                } else if (currentImage == 2) {
                    imageBitMap2 = capturedImage;
                    image2.setImageBitmap(imageBitMap2);
                }
            }
        }
        if (navigationView.getVisibility() == View.VISIBLE)
            navigationView.setVisibility(View.GONE);
        if (w.findViewById(R.id.fab).getVisibility() == View.VISIBLE) {
            (w.findViewById(R.id.fab)).setVisibility(View.GONE);
        }
    }



    private void buildImageUrl(String sightName) {
        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... params) {
                try {
                    String sightName = params[0];
                    String encodedSightName = URLEncoder.encode(sightName, "UTF-8");

                    // Construct the API URL with the sight name parameter
                    String apiUrl = "https://api.pexels.com/v1/search?query=" + encodedSightName + "&per_page=2";
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(apiUrl)
                            .addHeader("Authorization", API_KEY)
                            .build();

                    Response response = client.newCall(request).execute();
                    String json = response.body().string();

                    // Parse the JSON response
                    return parseImageUrlFromResponse(json);
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                } catch (JSONException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(String imageUrl) {
                if (imageUrl != null) {
                    loadImageFromUrl(imageUrl);
                } else {
                    buildImageUrl("freetime");
                }
            }
        }.execute(sightName);
    }


    private int flag=0;
    private String parseImageUrlFromResponse(String responseJson) throws JSONException {
        JSONObject responseObj = new JSONObject(responseJson);
        JSONArray photosArray = responseObj.getJSONArray("photos");

        if(flag==0)
            flag=1;
        else if(flag==1)
            flag=0;

        if (photosArray.length() > 0) {
            JSONObject photoObj = photosArray.getJSONObject(flag);
            JSONObject srcObj = photoObj.getJSONObject("src");
            String imageUrl = srcObj.getString("large");

            return imageUrl;
        }

        return null;
    }


    private void loadImageFromUrl(String imageUrl) {
        if (imageUrl != null) {
            if (currentImage == 1) {
                Picasso.get()
                        .load(imageUrl)
                        .resize(image1.getWidth(), image1.getHeight())
                        .centerCrop()
                        .error(R.drawable.error_image)
                        .into(new Target() {
                            @Override
                            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                imageBitMap1=bitmap;
                                image1.setImageBitmap(bitmap);

                                // Pass the bitmap to your insertUser method
                            }

                            @Override
                            public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                            }


                            @Override
                            public void onPrepareLoad(Drawable placeHolderDrawable) {
                                // Called before the image is loaded, you can show a placeholder here
                            }
                        });
            } else if (currentImage == 2) {
                Picasso.get()
                        .load(imageUrl)
                        .resize(image2.getWidth(), image2.getHeight())
                        .centerCrop()
                        .error(R.drawable.error_image)
                        .into(new Target() {
                            @Override
                            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                imageBitMap2=bitmap;
                                image2.setImageBitmap(bitmap);

                                // Pass the bitmap to your insertUser method
                            }

                            @Override
                            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                                // Handle the case where loading the bitmap failed
                            }

                            @Override
                            public void onPrepareLoad(Drawable placeHolderDrawable) {
                                // Called before the image is loaded, you can show a placeholder here
                            }
                        });
            }
        } else {
            buildImageUrl("freetime");
        }
    }



}