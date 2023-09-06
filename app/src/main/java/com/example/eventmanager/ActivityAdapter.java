package com.example.eventmanager;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.ActivityViewHolder> {
    private List<Activity> activityList;
    private OnItemClickListener listener;
    private DBHelper dbHelper;
    private String type;
    TextView nameTextView;


    Context context;


    public interface OnItemClickListener {
        void onItemClick(Activity activity);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public ActivityAdapter(List<Activity> activityList,DBHelper dbHelper) {
        this.activityList = activityList;
        this.dbHelper=dbHelper;
    }

    public ActivityAdapter(List<Activity> activityList) {
        this.activityList = activityList;

    }
    public void setActivities(List<Activity> activities) {
        this.activityList = activities;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ActivityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
         context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_activity, parent, false);

        return new ActivityViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ActivityViewHolder holder, int position) {
        Activity activity = activityList.get(position);
        if(activity!=null ) {
            type = activity.getType();

            int backgroundColor = 0;

            if ("FREE".equals(type))
                backgroundColor = ContextCompat.getColor(context, R.color.primary_triadic_one);
            else if ("TRAVEL".equals(type))
                backgroundColor = ContextCompat.getColor(context, R.color.primary_triadic_two);
            else if ("WORK".equals(type))
                backgroundColor = ContextCompat.getColor(context, R.color.primary_triadic_four);

            activity.setColor(backgroundColor);
            Drawable backgroundDrawable = nameTextView.getBackground();


            if (backgroundDrawable instanceof GradientDrawable) {
                GradientDrawable gradientDrawable = (GradientDrawable) backgroundDrawable;
                gradientDrawable.setColor(backgroundColor);
            }
        }
        holder.bind(activity);
    }

    @Override
    public int getItemCount() {
        return activityList.size();
    }

     class ActivityViewHolder extends RecyclerView.ViewHolder {

        TextView descriptionTextView;
        TextView timeTextView;
        TextView locationTextView;
        TextView dateTextView;

        ActivityViewHolder(@NonNull View itemView) {
            super(itemView);

            nameTextView = itemView.findViewById(R.id.nameTextView);
          //  descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
            //locationTextView = itemView.findViewById(R.id.locationTextView);
            dateTextView=itemView.findViewById(R.id.dateTextView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onItemClick(activityList.get(position));

                    }
                }
            });
        }

        void bind(Activity activity) {
            nameTextView.setText(activity.getName());
//            descriptionTextView.setText(activity.getDescription());
            timeTextView.setText(activity.getTime());
         //   locationTextView.setText(activity.getLocation());
            dateTextView.setText(activity.getDate());
        }
    }
}
