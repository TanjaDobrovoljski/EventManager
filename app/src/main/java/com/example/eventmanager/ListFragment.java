package com.example.eventmanager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ListFragment extends Fragment {
    private RecyclerView recyclerView;
    private ArrayList<Activity> activityList;
    private List<Activity> filteredActivityList;
    private ActivityAdapter activityAdapter;
    private DBHelper dbHelper;

    public ListFragment(DBHelper dbHelper) {
        this.dbHelper = dbHelper;
    }
    public ListFragment(List<Activity> list) {
        this.filteredActivityList=list;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        recyclerView = view.findViewById(R.id.list_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        WelcomeActivity welcomeActivity = (WelcomeActivity) requireActivity();
        welcomeActivity.setItemClicked(false);

        if (filteredActivityList != null) {
            // If the filtered list is not null, use it as the data source for the adapter
            activityAdapter = new ActivityAdapter(filteredActivityList);
        } else {
            // Otherwise, use the activities from the dbHelper
            activityList = dbHelper.getAllActivitiesSortedByDate();
            activityAdapter = new ActivityAdapter(activityList, dbHelper);
        }

        /*activityAdapter.setOnItemClickListener(new ActivityAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Activity activity) {
                Toast.makeText(getContext(), "Kliknuli ste na: " + activity.getName(), Toast.LENGTH_LONG).show();
            }
        });*/
        activityAdapter.setOnItemClickListener(new ActivityAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Activity activity) {
                // Open NewsArticleFragment on item click
                ActivityDetails detailsArticleFragment = new ActivityDetails();
                Bundle bundle = new Bundle();

                WelcomeActivity welcomeActivity = (WelcomeActivity) requireActivity();
                welcomeActivity.setItemClicked(true);
                welcomeActivity.setSearchBoxVisibility(false);




                bundle.putParcelable("activity", activity);

                bundle.putString("fragmentType", "FreeTimeFragment");

                detailsArticleFragment.setArguments(bundle);
                getParentFragmentManager()
                        .beginTransaction()
                        .replace(R.id.nav_host_fragment, detailsArticleFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
        recyclerView.setAdapter(activityAdapter);
        return view;
    }

    public void refreshList()
    {
        activityList = dbHelper.getAllActivitiesSortedByDate();
        activityAdapter = new ActivityAdapter(activityList,dbHelper);
        recyclerView.setAdapter(activityAdapter);
    }

}
