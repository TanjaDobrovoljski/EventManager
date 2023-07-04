package com.example.eventmanager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ListFragment extends Fragment {
    private RecyclerView recyclerView;
    private List<Activity> activityList;
    private ActivityAdapter activityAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        recyclerView = view.findViewById(R.id.list_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        activityList = new ArrayList<>();


        Activity activity1 = new Activity("Hjc","Work", "9:00 AM - 5:00 PM", "Office work"," grad1","31/12/2023");
        Activity activity2 = new Activity("Hjc","Leisure", "6:00 PM - 8:00 PM", "Watch a movie"," grad1","03/07/2023");
        Activity activity3 = new Activity("Hjc","Travel", "10:00 AM - 6:00 PM", "Visit a tourist attraction", " grad1","15/02/2024");

        activityList.add(activity1);
        activityList.add(activity2);
        activityList.add(activity3);


        activityAdapter = new ActivityAdapter(activityList);


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

                bundle.putParcelable("activity", activity);
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

}
