package e.natasja.natasjawezel__pset5;


import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;


/**
 * A simple {@link Fragment} subclass.
 */
public class MenuFragment extends ListFragment {
    RestoDatabase mRestoDatabase;
    Bitmap image;
    RequestQueue queue;
    MenuAdapter adapter;
    ArrayList<Dish> arrayOfDishes;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle arguments = this.getArguments();
        final String categorie = arguments.getString("categorie");

        Log.d("MenuFragment", "The categorie from the bundle is: " + categorie);

        // Instantiate the RequestQueue.
        String url = "https://resto.mprog.nl/menu";

        queue = Volley.newRequestQueue(getContext());

        // Request a string response from the provided URL.
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray group = response.getJSONArray("items");

                            // loop over JsonArray group and put the words in categoriesArray
                            for (int i = 0; i < group.length(); i++) {

                                JSONObject object = group.getJSONObject(i);

                                String appetizerOrEntree = object.getString("category");
                                Log.d("MenuFragment", "This is a " + appetizerOrEntree);

                                // show the items that are in the right category
                                if (appetizerOrEntree.equals(categorie)) {
                                    String name = object.getString("name");
                                    String description = object.getString("description");
                                    String imageUrl = object.getString("image_url");
                                    imageRequestFunction(name, description, imageUrl);

                                    // construct data source
                                    arrayOfDishes = new ArrayList<Dish>();

                                }

                            }
                        } catch (JSONException e) {

                            // show error (in case of an error)
                            Toast.makeText(getContext(), "JSONObject to JSONArray didn't go right", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        // show error (in case of error)
                        Toast.makeText(getContext(), "No response on Request.", Toast.LENGTH_SHORT).show();
                    }
                });

        queue.add(jsObjRequest);
    }

    public class Dish {
        public String name;
        public String description;
        public Bitmap imageDish;

        public Dish(String name, String description, Bitmap imageDish) {
            this.name = name;
            this.description = description;
            this.imageDish = imageDish;
        }
    }

    public void imageRequestFunction(final String name, final String description, String imageUrl) {

        // bron: https://www.programcreek.com/javi-api-examples/index.php?api=com.android.volley.toolbox.ImageRequest
        ImageRequest imageRequest = new ImageRequest(imageUrl, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap bitmap) {
                image = bitmap;

                Dish newDish = new Dish(name, description, image);
                Log.d("MenuFragment", "New dish = " + newDish);
                arrayOfDishes.add(newDish);

                // create the adapter to convert the array to views
                adapter = new MenuAdapter(getContext(), arrayOfDishes);
                setListAdapter(adapter);
            }

        },
                0,
                0,
                null,
                Bitmap.Config.ALPHA_8,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "Loading image failed", Toast.LENGTH_LONG).show();

                        image = null;

                        Dish newDish = new Dish(name, description, image);
                        Log.d("MenuFragment", "New dish = " + newDish);
                        arrayOfDishes.add(newDish);

                        // create the adapter to convert the array to views
                        adapter = new MenuAdapter(getContext(), arrayOfDishes);
                        setListAdapter(adapter);

                    }
                }
        );

        queue.add(imageRequest);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_menu, container, false);


        ListView listView = view.findViewById(R.id.list);
        Log.d("MenuFragment", "ListView searching + " + R.id.list + "and the listview = " + listView + arrayOfDishes);

        return view;
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        String dish = (String)l.getItemAtPosition(position);

        Log.d("RestoDatabase", "The dish is: " + dish);

        mRestoDatabase = RestoDatabase.getInstance(getContext());
        mRestoDatabase.addData(dish, 8);
    }
}
