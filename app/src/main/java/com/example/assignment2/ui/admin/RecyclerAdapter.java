package com.example.assignment2.ui.admin;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment2.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.Arrays;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private String[] enter;

    private String[] names;

    private String[] times;

    private String[] vaccineStatuses;

    public RecyclerAdapter(String[] enter, String[] names, String[] times, String[] vaccineStatuses){
        this.enter = enter.clone();
        this.names = names.clone();
        this.times = times.clone();
        this.vaccineStatuses = vaccineStatuses.clone();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(enter[position].equals("entry")) holder.statusImage.setImageResource(R.drawable.ic_baseline_login_24);
        else holder.statusImage.setImageResource(R.drawable.ic_baseline_logout_24);

        holder.name.setText(hideString(names[position]));
        holder.time.setText(times[position]);

        if(vaccineStatuses[position].equals("Vaccinated")) {
            String vaccinated = "Vaccinated";
            holder.vaccineStatus.setText(vaccinated);
            holder.cardView.setCardBackgroundColor(Color.rgb(3, 194, 74));
        }
        else {
            String unvaccinated = "Unvaccinated";
            holder.vaccineStatus.setText(unvaccinated);
            holder.cardView.setCardBackgroundColor(Color.rgb(185, 14, 10));
        }
    }

    private String hideString(String string){
        return string.charAt(0) + "xxxx" + string.substring(6,9);
    }

    @Override
    public int getItemCount() {
        return names.length;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView statusImage;
        TextView name, time, vaccineStatus;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            statusImage = itemView.findViewById(R.id.imageView);
            name = itemView.findViewById(R.id.nameTextView);
            time = itemView.findViewById(R.id.timeTextView);
            vaccineStatus = itemView.findViewById(R.id.vaccineStatusTextView);
            cardView = itemView.findViewById(R.id.cardView);

            itemView.setOnClickListener(view -> {
                int position = getAdapterPosition();

            });


        }
    }
}