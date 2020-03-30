package com.example.mojito;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class OverviewListFragment extends Fragment  {

    public static final String TAG = "OverviewListFragment";

    private ArrayList<Cocktail> cocktails = new ArrayList<>();

    private OnItemSelectedListener listener;

    public interface OnItemSelectedListener {
        void onItemSelected(Integer id);
    }

    public void setListener(OnItemSelectedListener listener) {
        this.listener = listener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.overview_fragment, container, false);

        Log.d(TAG, "onCreateView: View created");

        if (cocktails.isEmpty()) {
            fetchData(view);
        } else {
            initRecyclerView(view);
        }

        return view;
    }

    private void fetchData(final View view) {
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        String url ="https://www.thecocktaildb.com/api/json/v2/9973533/popular.php";

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
        Log.d(TAG, "initRecyclerView: Initialising recycler view");
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(cocktails);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getActivity() instanceof OverviewListFragment.OnItemSelectedListener) {
            listener = (OnItemSelectedListener) getActivity();
        }
    }
}
