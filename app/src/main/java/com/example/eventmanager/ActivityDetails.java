package com.example.eventmanager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
    Activity activity=null;

    DBHelperCity dbHelperCity;
    private TextView nameTextView,descriptionTextView,dateActivity,timeActivity;
    private MapView mapView;
    private ImageView image1,image2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_activity_details, container, false);

        dbHelperCity=new DBHelperCity(getContext());

        setHasOptionsMenu(false);
        // Retrieve the clicked activity from the arguments
        Bundle arguments = getArguments();
        if (arguments != null) {
           activity = arguments.getParcelable("activity");
            if (activity != null) {
                // Update the UI with the details of the clicked activity
                 nameTextView = view.findViewById(R.id.activity_title);
                 descriptionTextView = view.findViewById(R.id.description);
                 dateActivity=view.findViewById(R.id.dateActivity);
                 timeActivity=view.findViewById(R.id.timeActivity);
                mapView = view.findViewById(R.id.mapView);
                if ("TRAVEL".equals(activity.getType())) {
                    // If the type is "TRAVEL," show the MapView and its related views
                    mapView.setVisibility(View.VISIBLE);
                    mapView.onCreate(savedInstanceState);
                    mapView.getMapAsync(this);
                    location = activity.getCity();
                }
                else {
                    // If the type is not "TRAVEL," hide the MapView and its related views
                    mapView.setVisibility(View.GONE);
                    mapView=null;
                }

                if("FREE".equals((activity.getType())))
                {
                    image1=view.findViewById(R.id.image1);
                    image2=view.findViewById(R.id.image2);
                    image1.setImageBitmap(activity.getImage1());
                    image2.setImageBitmap(activity.getImage2());
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

        mMap = googleMap;
        LatLng location = dbHelperCity.getCoordinates(activity.getCity());
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.addMarker(new MarkerOptions().position(location).title("Marker in "+ activity.getCity()));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 7f)); // Adjust the zoom level here
    }


    @Override
    public void onResume() {
        super.onResume();
        if(mapView!=null)
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        if(mapView!=null)
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mapView!=null)
        mapView.onDestroy();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if(mapView!=null)
        mapView.onSaveInstanceState(outState);
    }


}
