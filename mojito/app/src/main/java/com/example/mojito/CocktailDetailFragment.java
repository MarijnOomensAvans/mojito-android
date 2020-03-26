package com.example.mojito;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class CocktailDetailFragment extends Fragment {
    public static final String TAG = "CocktailDetailFragment";

    private int id;
    private String name;
    private String instructions;
    private String image;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cocktail_detail_fragment, container, false);

        Log.d(TAG, "onCreateView: View created");

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            id = bundle.getInt("id");
        }

        fetchCocktail(view);

        return view;
    }

    private void fetchCocktail(final View view) {
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        String url ="https://www.thecocktaildb.com/api/json/v1/1/lookup.php?i=" + id;

        JsonObjectRequest cocktailRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject cocktail = response.getJSONArray("drinks").getJSONObject(0);
                    name = cocktail.getString("strDrink");
                    instructions = cocktail.getString("strInstructions");
                    image = cocktail.getString("strDrinkThumb");

                    initialiseView(view);
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

        queue.add(cocktailRequest);
    }

    private void initialiseView(View view) {
        TextView nameText = view.findViewById(R.id.detail_name);
        nameText.setText(name);

        TextView instructionsText = view.findViewById(R.id.detail_instructions);
        instructionsText.setText(instructions);

        loadImage(view);
    }

    private void loadImage(View view) {
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());

        final ImageView mImageView;
        mImageView = view.findViewById(R.id.detail_image);

        ImageRequest request = new ImageRequest(image,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        mImageView.setImageBitmap(bitmap);
                    }
                }, 500, 500, null,
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        mImageView.setImageResource(R.drawable.ic_launcher_foreground);
                    }
                });

        queue.add(request);
    }

}
