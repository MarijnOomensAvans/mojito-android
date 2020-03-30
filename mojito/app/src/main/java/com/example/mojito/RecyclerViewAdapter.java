package com.example.mojito;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mojito.models.Cocktail;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private static final String TAG = "RecyclerViewAdapter";

    private ArrayList<Cocktail> cocktails;

    private Context context;

    RecyclerViewAdapter(ArrayList<Cocktail> cocktails) {
        this.cocktails = cocktails;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem, parent, false);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: onBindViewholder called");

        holder.cocktailName.setText(cocktails.get(position).getmName());

        Set<String> drunkCocktails = null;

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        if (sharedPreferences.getStringSet("drunk", null) != null) {
            drunkCocktails = sharedPreferences.getStringSet("drunk", null);
        }

        if (drunkCocktails != null) {
            if (drunkCocktails.contains(cocktails.get(position).getmName())) {
                holder.checkBox.setChecked(true);
            }
        }

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: clicked on " + cocktails.get(position).getmName());

                MainActivity mainActivity = (MainActivity) v.getContext();
                mainActivity.onItemSelected(cocktails.get(position).getmId());
            }
        });

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                final SharedPreferences.Editor mEdit1 = sharedPreferences.edit();

                Set<String> newSet = null;

                if (sharedPreferences.getStringSet("drunk", null) != null) {
                    newSet = sharedPreferences.getStringSet("drunk", null);
                }

                mEdit1.remove("drunk").apply();

                if (isChecked) {
                    if (newSet != null) {
                        newSet.add(cocktails.get(position).getmName());
                    } else {
                        newSet = new HashSet<String>() {{ add(cocktails.get(position).getmName()); }};
                    }
                    mEdit1.putStringSet("drunk", newSet);
                } else {
                    if (newSet != null) {
                        newSet.remove(cocktails.get(position).getmName());
                        mEdit1.putStringSet("drunk", newSet);
                    }
                }

                mEdit1.apply();
            }
        });
    }


    @Override
    public int getItemCount() {
        return cocktails.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        TextView cocktailName;
        CheckBox checkBox;
        RelativeLayout parentLayout;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            cocktailName = itemView.findViewById(R.id.cocktail_name);
            checkBox = itemView.findViewById(R.id.check_box);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }
}
