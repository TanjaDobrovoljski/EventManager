package com.example.eventmanager;

import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.InputFilter;
import android.text.Spanned;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.FileNotFoundException;
import java.io.IOException;

import java.net.URLEncoder;


import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class FreeTimeFragment extends Fragment {

    private EditText nameEditText;

    private CalendarView calendarView;
    private EditText descriptionEditText;
    private EditText locationEditText;

    private Spinner hourSpinner;
    private Spinner minuteSpinner;

    private Button buttonAdd;
    private ImageView image1,image2;
    private Bitmap imageBitMap1,imageBitMap2;
    private static final int CAMERA_REQ_CODE = 1;
    private static final int GALLERY_REQ_CODE = 2;
    private int currentImage;
    private AutoCompleteTextView locationAutoCompleteTextView;
    DBHelper dbHelper;
    private String name= "";
    private String selectedDate="";
    private String selectedTime="";
    private String description= "";
    private String location= "";
    private String selectedHour = "";
    private String selectedMinute = "";


    private static final String API_KEY = "QWbBcpE6Sb1dsPpWTIt29e7puN3daasrXHBIucDx0qEGdgwxJijP5Mrl"; //6d2c8722de764a277fe0e1de101e296e

    public FreeTimeFragment(DBHelper dbHelper) {
        this.dbHelper=dbHelper;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_free_time, container, false);

        nameEditText = view.findViewById(R.id.nameEditText);
       buttonAdd=view.findViewById(R.id.buttonAdd);
       image1=view.findViewById(R.id.image1);
       image2=view.findViewById(R.id.image2);


        image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Select Image Source");
                builder.setItems(new CharSequence[]{"Gallery", "Camera","Internet"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            // Open gallery
                            Intent galleryIntent = new Intent(Intent.ACTION_PICK);
                            currentImage = 1;
                            galleryIntent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                            startActivityForResult(galleryIntent, GALLERY_REQ_CODE);
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
                            buildImageUrl("dog");
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
                builder.setTitle("Select Image Source");
                builder.setItems(new CharSequence[]{"Gallery", "Camera","Internet"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            // Open gallery
                            Intent galleryIntent = new Intent(Intent.ACTION_PICK);
                            currentImage = 2;
                            galleryIntent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(galleryIntent, GALLERY_REQ_CODE);
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
                            buildImageUrl("dog");
                        }
                    }
                });
                builder.show();
            }
        });




        buttonAdd.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               int i;
               if(!isFormValid())
                   Toast.makeText(view.getContext(), "Niste popunili sva polja!", Toast.LENGTH_SHORT).show();
               else {

                  if( dbHelper.insertUser("FREE",name,selectedTime,description,location,selectedDate,imageBitMap1,imageBitMap2)) {
                      Toast.makeText(view.getContext(), "Uspijesno dodana aktivnost!", Toast.LENGTH_SHORT).show();

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
         locationAutoCompleteTextView = view.findViewById(R.id.locationAutoCompleteTextView);

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
         name = nameEditText.getText().toString().trim();

       
         description = descriptionEditText.getText().toString().trim();
         location =  locationAutoCompleteTextView.getText().toString().trim();
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


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
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
                // Get the captured image from the intent data
                Bundle extras = data.getExtras();


                if (currentImage == 1) {
                   imageBitMap1 = (Bitmap) extras.get("data");
                    image1.setImageBitmap(imageBitMap1);
                } else if (currentImage == 2) {
                    imageBitMap2 = (Bitmap) extras.get("data");
                    image2.setImageBitmap(imageBitMap2);
                }
            }
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
                    // Handle the error case
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
            // Handle the case where imageUrl is null or invalid
        }
    }


    public void handleBackPressed() {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        fragmentManager.popBackStack();
    }


}
