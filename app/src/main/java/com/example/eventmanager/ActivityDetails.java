package com.example.eventmanager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class ActivityDetails extends Fragment {

    private String name= "";
    private String date="";
    private String time="";
    private String description= "";
    private String location= "";

    DBHelperCity dbHelperCity;
    private TextView nameTextView,descriptionTextView,dateActivity,timeActivity;
    private MapView mapView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_activity_details, container, false);

        dbHelperCity=new DBHelperCity(getContext());
        // Retrieve the clicked activity from the arguments
        Bundle arguments = getArguments();
        if (arguments != null) {
            Activity activity = arguments.getParcelable("activity");
            if (activity != null) {
                // Update the UI with the details of the clicked activity
                 nameTextView = view.findViewById(R.id.activity_title);
                 descriptionTextView = view.findViewById(R.id.description);
                 dateActivity=view.findViewById(R.id.dateActivity);
                 timeActivity=view.findViewById(R.id.timeActivity);
                 mapView=view.findViewById(R.id.mapView);


                // Set other text views with the respective details

                nameTextView.setText(activity.getName());
                descriptionTextView.setText(activity.getDescription());
                dateActivity.setText(activity.getDate());
                timeActivity.setText(activity.getTime());
                location=activity.getCity();
                // Set other text views with the respective details
            }
        }

        return view;
    }

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        @Override
        public void onMapReady(GoogleMap googleMap) {
            LatLng city = dbHelperCity.getCoordinates(location);
            googleMap.getUiSettings().setZoomControlsEnabled(true);
            googleMap.addMarker(new MarkerOptions().position(city).title("Marker in Sydney"));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(city));
        }
    };
}
