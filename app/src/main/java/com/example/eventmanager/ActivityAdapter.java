package com.example.eventmanager;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.ActivityViewHolder> {
    private List<Activity> activityList;
    private OnItemClickListener listener;
    Context context;


    public interface OnItemClickListener {
        void onItemClick(Activity activity);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public ActivityAdapter(List<Activity> activityList) {
        this.activityList = activityList;
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
        holder.bind(activity);
    }

    @Override
    public int getItemCount() {
        return activityList.size();
    }

     class ActivityViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView descriptionTextView;
        TextView timeTextView;
        TextView locationTextView;
        TextView dateTextView;

        ActivityViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);

            int backgroundColor = ContextCompat.getColor(context, R.color.primary_triadic_two); //promjena boje zavisno od kategorije
            Drawable backgroundDrawable = nameTextView.getBackground();


            if (backgroundDrawable instanceof GradientDrawable) {
                GradientDrawable gradientDrawable = (GradientDrawable) backgroundDrawable;
                gradientDrawable.setColor(backgroundColor);
            }

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
