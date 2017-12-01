package e.natasja.natasjawezel__pset5;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class CategoriesFragment extends ListFragment {
    List<String> categoriesArray;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        categoriesArray = new ArrayList<String>();

        // Instantiate the RequestQueue.
        String url ="https://resto.mprog.nl/categories";

        RequestQueue queue = Volley.newRequestQueue(getContext());

        // Request a string response from the provided URL.
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray group = response.getJSONArray("categories");

                            // loop over JsonArray group and put the words in dishesArray
                            for(int i = 0; i < group.length(); i++) {
                                categoriesArray.add(group.getString(i));
                            }

                            listStuff();
                            // notifyDataSetChanged

                        } catch (JSONException e) {

                            // show error (in case of an error)
                           Log.d("CategoriesFragment", "JSONObject to JSONArray didnt go right");
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        // show error (in case of error)
                        Log.d("CategoriesFragment", "No response on Request.");
                        Toast.makeText(getContext(), "No response on request", Toast.LENGTH_SHORT).show();
                    }
                });

        queue.add(jsObjRequest);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_categories, container, false);

        return view;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        // if item is clicked, go to menu fragment
        MenuFragment menuFragment = new MenuFragment();

        Bundle args = new Bundle();
        args.putString("categorie", l.getItemAtPosition(position).toString());
        menuFragment.setArguments(args);

        getFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, menuFragment)
                .addToBackStack(null)
                .commit();
    }

    public void listStuff(){

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, categoriesArray);

        this.setListAdapter(adapter);
    }

}
