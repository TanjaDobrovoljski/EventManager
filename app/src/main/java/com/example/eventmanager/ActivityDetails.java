package com.example.eventmanager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.TransitionRes;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
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
    private DBHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_activity_details, container, false);

        dbHelperCity=new DBHelperCity(getContext());
        dbHelper=new DBHelper(getContext());

        setHasOptionsMenu(true);
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.details_main,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.app_edit:
                return false;
            case R.id.app_delete:
             AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                builder.setTitle("Confirm Delete");
                builder.setMessage("Are you sure you want to delete this activity?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       dbHelper.deleteActivity(dbHelper.getActivityId(activity.getType(),activity.getName(),activity.getTime(),activity.getDescription(),activity.getCity(),activity.getDate()));
                        Toast.makeText(getContext(),"Successfully deleted "+activity.getName(),Toast.LENGTH_SHORT).show();
                       getActivity().onBackPressed();
                    }
                });
                builder.setNegativeButton("No", null);
                builder.show();
                return true;

            default:
                break;
        }

        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        if(dbHelperCity.cityExists(activity.getCity()))
        {
            LatLng location = dbHelperCity.getCoordinates(activity.getCity());
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.addMarker(new MarkerOptions().position(location).title("Marker in "+ activity.getCity()));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 7f));
        }
        else {
            LatLng defaultLocation = new LatLng(0, 0);
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(defaultLocation)
                    .title("City Not Found")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)); // Use a red marker icon

            googleMap.addMarker(markerOptions);
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 7f));
            Toast.makeText(getContext(), "City not found", Toast.LENGTH_SHORT).show();
        }
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
