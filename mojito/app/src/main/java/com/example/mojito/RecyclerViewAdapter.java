package com.example.mojito;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private static final String TAG = "RecyclerViewAdapter";

    private ArrayList<String> cocktailNames;
    private ArrayList<Integer> cocktailIds;

    RecyclerViewAdapter(ArrayList<String> cocktailNames, ArrayList<Integer> cocktailIds) {
        this.cocktailNames = cocktailNames;
        this.cocktailIds = cocktailIds;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: onBindViewholder called");

        holder.cocktailName.setText(cocktailNames.get(position));

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: clicked on " + cocktailNames.get(position));

                MainActivity mainActivity = (MainActivity) v.getContext();
                mainActivity.onItemSelected(cocktailIds.get(position));

            }
        });
    }

    @Override
    public int getItemCount() {
        return cocktailNames.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        TextView cocktailName;
        RelativeLayout parentLayout;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            cocktailName = itemView.findViewById(R.id.cocktail_name);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }
}
