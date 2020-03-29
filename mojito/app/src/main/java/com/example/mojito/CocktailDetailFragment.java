package com.example.mojito;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

public class CocktailDetailFragment extends Fragment {
    public static final String TAG = "CocktailDetailFragment";

    private int id;
    private String name;
    private String instructions;
    private String image;
    private String glass;
    private ArrayList<String> ingredients = new ArrayList<>();
    private ArrayList<String> amounts = new ArrayList<>();
    public static final String MY_File_NAME = "MyOwnPhoto";
    private final int REQUEST_PERMISSION_CAMERA = 1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cocktail_detail_fragment, container, false);

        Log.d(TAG, "onCreateView: View created");

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            id = bundle.getInt("id");
        }

        Button photoButton = view.findViewById(R.id.photobutton);
        photoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
            }
        });

        fetchCocktail(view);
        requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, 3);

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
                    glass = "Glass: " + cocktail.getString("strGlass");

                    for (int i = 1; i <= 15; i++) {
                        String ingredient = cocktail.getString("strIngredient" + i);
                        if (ingredient.equals("null")) break;
                        ingredients.add(ingredient);
                        amounts.add(cocktail.getString("strMeasure" + i));
                    }

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

        TextView ingredientsTitleText = view.findViewById(R.id.detail_ingredients_title);
        ingredientsTitleText.setText(R.string.ingredients);

        TextView ingredientsText = view.findViewById(R.id.detail_ingredients);

        StringBuilder measureIngredients = new StringBuilder();

        for (int i = 0; i < ingredients.size(); i++) {
            String appendAmount = "";
            String amount = amounts.get(i);
            if (!amount.equals("null")) {
                appendAmount = amount + " ";
            }
            measureIngredients.append(appendAmount).append(ingredients.get(i)).append("\n");
        }

        ingredientsText.setText(measureIngredients.toString());

        TextView glassText = view.findViewById(R.id.detail_glass);
        glassText.setText(glass);

        loadImage(view);
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            SharedPreferences prefs = getActivity().getSharedPreferences(MY_File_NAME, MODE_PRIVATE);
            String photoLocation = prefs.getString("PhotoCocktail" + name, "");
            if (!photoLocation.equals("")) {
                Bitmap bmp = BitmapFactory.decodeFile(photoLocation);
                ImageView imageView = view.findViewById(R.id.ownPhoto);
                imageView.setImageBitmap(bmp);
            }
        }
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

    public void takePhoto() {
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                startActivityForResult(intent, 1);
            }
        } else {
            requestPermissions(new String[] {Manifest.permission.CAMERA}, REQUEST_PERMISSION_CAMERA);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION_CAMERA)  {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivityForResult(intent, 1);
                }
                }else {
               return;
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            ImageView imageView = getView().findViewById(R.id.ownPhoto);
            imageView.setImageBitmap(imageBitmap);
            requestPermissions(new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
            requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, 3);
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                String fileName = "/PhotoCocktail" + name + ".png";
                File file = new File(path, fileName);
                if (file.exists ()) file.delete ();
                try {
                    path.mkdirs();
                    FileOutputStream outStream = new FileOutputStream(file.getAbsoluteFile());
                    imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
                    outStream.flush();
                    outStream.close();
                    SharedPreferences.Editor editor = getActivity().getSharedPreferences(MY_File_NAME, MODE_PRIVATE).edit();
                    editor.putString("PhotoCocktail" + name, file.getAbsolutePath());
                    editor.apply();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            else{
                return;
            }
        }
    }

}
