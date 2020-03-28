package com.example.mojito;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends FragmentActivity implements OverviewListFragment.OnItemSelectedListener {
    public static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_container, new OverviewListFragment());
            ft.commit();
        }
    }


    @Override
    public void onItemSelected(Integer id) {
        Bundle bundle = new Bundle();
        bundle.putInt("id", id);

        CocktailDetailFragment cocktailDetailFragment = new CocktailDetailFragment();
        cocktailDetailFragment.setArguments(bundle);

        FragmentTransaction f = getSupportFragmentManager().beginTransaction();
        f.replace(R.id.fragment_container, cocktailDetailFragment);
        f.addToBackStack("detail");
        f.commit();
    }
}
