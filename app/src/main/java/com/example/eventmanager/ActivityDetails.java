package com.example.eventmanager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.TransitionRes;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class ActivityDetails extends Fragment implements OnMapReadyCallback {

    private String name= "";
    private String date="";
    private String time="";
    private String description= "";
   // static final LatLng Your_Location = new LatLng(23.81, 90.41); //Your LatLong
    private GoogleMap mMap;
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
                 if("TRAVEL".equals(activity.getType())) {
                     mapView = view.findViewById(R.id.mapView);
                     mapView.onCreate(savedInstanceState);
                     mapView.getMapAsync(this);
                 }



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


    @Override
    public void onMapReady(GoogleMap googleMap) {
       // mMap=googleMap;
        LatLng sydney = new LatLng(100, -32);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }


}
