package com.example.mojito;

import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity implements OverviewListFragment.OnItemSelectedListener {
    public static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(myToolbar);

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
