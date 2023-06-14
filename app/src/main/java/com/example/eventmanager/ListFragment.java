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

        // Create and add Activity objects to the list
        Activity activity1 = new Activity("Work", "9:00 AM - 5:00 PM", "Office work", "Company HQ");
        Activity activity2 = new Activity("Leisure", "6:00 PM - 8:00 PM", "Watch a movie", "Local cinema");
        Activity activity3 = new Activity("Travel", "10:00 AM - 6:00 PM", "Visit a tourist attraction", "City center");

        activityList.add(activity1);
        activityList.add(activity2);
        activityList.add(activity3);

        // Add your logic to populate the activityList with data
        activityAdapter = new ActivityAdapter(activityList);
        recyclerView.setAdapter(activityAdapter);
        return view;
    }
}
