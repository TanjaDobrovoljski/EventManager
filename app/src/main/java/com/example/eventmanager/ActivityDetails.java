package com.example.eventmanager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class ActivityDetails extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_activity_details, container, false);

        // Retrieve the clicked activity from the arguments
        Bundle arguments = getArguments();
        if (arguments != null) {
            Activity activity = arguments.getParcelable("activity");
            if (activity != null) {
                // Update the UI with the details of the clicked activity
                TextView nameTextView = view.findViewById(R.id.activity_title);
                TextView descriptionTextView = view.findViewById(R.id.news_content);
                // Set other text views with the respective details

                nameTextView.setText(activity.getName());
                descriptionTextView.setText(activity.getDescription());
                // Set other text views with the respective details
            }
        }

        return view;
    }
}
