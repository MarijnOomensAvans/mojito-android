package com.example.mojito;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.mojito.models.Cocktail;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AllCocktailOverviewFragment extends Fragment {

    private ArrayList<Cocktail> cocktails = new ArrayList<>();
    public static final String TAG = "AllCocktailFragment";

    private AllCocktailOverviewFragment.OnItemSelectedListener listener;

    public interface OnItemSelectedListener {
        void onItemSelected(Integer id);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.all_cocktail_overview_fragment, container, false);
        if (cocktails.isEmpty()) {
            fetchData(view);
        } else {
            initRecyclerView(view);
        }

        return view;
    }

    private void fetchData(final View view) {
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        String url ="https://www.thecocktaildb.com/api/json/v1/1/filter.php?c=Cocktail";

        JsonObjectRequest popularCocktailsRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray popularCocktails = response.getJSONArray("drinks");
                    for (int i = 0; i < popularCocktails.length(); i++) {
                        cocktails.add(new Cocktail(popularCocktails.getJSONObject(i).getInt("idDrink"), popularCocktails.getJSONObject(i).getString("strDrink")));
                    }
                    initRecyclerView(view);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: An error occurred while fetching popular cocktails");
            }
        });

        queue.add(popularCocktailsRequest);
    }
    private void initRecyclerView(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_all);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(cocktails);
        recyclerView.setAdapter(adapter);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
               // if(dy > 0){
              //      int visibleItemCount = layoutManager.getChildCount();
               //     int totalItemCount = layoutManager.getItemCount();
                //    int pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();
               // }
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity() instanceof AllCocktailOverviewFragment.OnItemSelectedListener) {
            listener = (AllCocktailOverviewFragment.OnItemSelectedListener) getActivity();
        }
    }

}
