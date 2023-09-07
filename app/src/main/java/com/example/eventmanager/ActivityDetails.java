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
import com.google.android.material.bottomnavigation.BottomNavigationView;

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

        WelcomeActivity w=(WelcomeActivity) requireActivity();
        BottomNavigationView navigationView = w.findViewById(R.id.navigationView);
        if(navigationView.getVisibility()==View.VISIBLE)
            navigationView.setVisibility(View.GONE);
        if(w.findViewById(R.id.fab).getVisibility()==View.VISIBLE) {
            (w.findViewById(R.id.fab)).setVisibility(View.GONE);
        }

        setHasOptionsMenu(true);

        Bundle arguments = getArguments();
        if (arguments != null) {
           activity = arguments.getParcelable("activity");
            if (activity != null) {

                 nameTextView = view.findViewById(R.id.activity_title);
                 descriptionTextView = view.findViewById(R.id.description);
                 dateActivity=view.findViewById(R.id.dateActivity);
                 timeActivity=view.findViewById(R.id.timeActivity);
                mapView = view.findViewById(R.id.mapView);

                if ("TRAVEL".equals(activity.getType())) {
                    mapView.setVisibility(View.VISIBLE);
                    mapView.onCreate(savedInstanceState);
                    mapView.getMapAsync(this);
                    location = activity.getCity();
                }
                else {

                    mapView.setVisibility(View.GONE);
                    mapView=null;
                }

                if("FREE".equals((activity.getType())))
                {
                    image1=view.findViewById(R.id.image1);
                    image2=view.findViewById(R.id.image2);
                    if(activity.getImage1()!=null)
                    image1.setImageBitmap(activity.getImage1());
                    if(activity.getImage2()!=null)
                    image2.setImageBitmap(activity.getImage2());
                }

                nameTextView.setText(activity.getName());
                descriptionTextView.setText(activity.getDescription());
                dateActivity.setText(activity.getDate());
                timeActivity.setText(activity.getTime());
                location=activity.getCity();

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
                EditingFragment editingFragment = new EditingFragment(activity, dbHelper);

                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.nav_host_fragment, editingFragment)
                        .addToBackStack(null)
                        .commit();

                return true;
            case R.id.app_delete:
             AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                builder.setTitle(getString(R.string.delete_activity_toast_title));
                builder.setMessage(getString(R.string.delete_activity_toast));
                builder.setPositiveButton(getString(R.string.delete_activity_toast_yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       dbHelper.deleteActivity(dbHelper.getActivityId(activity.getType(),activity.getName(),activity.getTime(),activity.getDescription(),activity.getCity(),activity.getDate()));
                        Toast.makeText(getContext(),getString(R.string.delete_activity_toast_confirmation)+" "+activity.getName(),Toast.LENGTH_SHORT).show();
                       getActivity().onBackPressed();
                    }
                });
                builder.setNegativeButton(getString(R.string.delete_activity_toast_no), null);
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
                    .title(getString(R.string.location_city_not_found))
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)); // Use a red marker icon

            googleMap.addMarker(markerOptions);
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 7f));
            Toast.makeText(getContext(), R.string.location_city_not_found, Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        LanguageHelper.applyLanguage(this.getContext());
        if(mapView!=null)
            mapView.onResume();

        WelcomeActivity w=(WelcomeActivity) requireActivity();
        BottomNavigationView navigationView = w.findViewById(R.id.navigationView);
        if(navigationView.getVisibility()==View.VISIBLE)
            navigationView.setVisibility(View.GONE);
        if(w.findViewById(R.id.fab).getVisibility()==View.VISIBLE) {
            (w.findViewById(R.id.fab)).setVisibility(View.GONE);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(mapView!=null)
        mapView.onPause();

        WelcomeActivity w=(WelcomeActivity) requireActivity();
        BottomNavigationView navigationView = w.findViewById(R.id.navigationView);
        if(navigationView.getVisibility()==View.GONE)
            navigationView.setVisibility(View.VISIBLE);
        if(w.findViewById(R.id.fab).getVisibility()==View.GONE) {
            (w.findViewById(R.id.fab)).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mapView!=null)
        mapView.onDestroy();

        WelcomeActivity w=(WelcomeActivity) requireActivity();
        BottomNavigationView navigationView = w.findViewById(R.id.navigationView);
        if(navigationView.getVisibility()==View.GONE)
            navigationView.setVisibility(View.VISIBLE);
        if(w.findViewById(R.id.fab).getVisibility()==View.GONE) {
            (w.findViewById(R.id.fab)).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if(mapView!=null)
        mapView.onSaveInstanceState(outState);
    }



}
