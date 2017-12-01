package e.natasja.natasjawezel__pset5;


import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class OrderFragment extends DialogFragment implements View.OnClickListener {

    RestoDatabase mRestoDatabase = RestoDatabase.getInstance(getContext());
    RestoAdapter adapter;
    Cursor data = mRestoDatabase.selectAll();
    ListView list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_order, container, false);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        // get all items from the database
        List<String> listData = new ArrayList<>();
        while (data.moveToNext()) {
            listData.add(data.getString(1));
        }

        // link the orderadapter to the listview
        adapter = new RestoAdapter(getContext(), data);
        list = getView().findViewById(R.id.list);
        list.setAdapter(adapter);

        Button cancel = getView().findViewById(R.id.cancelButton);
        Button submit = getView().findViewById(R.id.submitButton);

        cancel.setOnClickListener(this);
        submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cancelButton:
                Toast.makeText(getContext(), "You've cancelled your order!", Toast.LENGTH_SHORT).show();
                mRestoDatabase.deleteDatabase();
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);

                break;
            case R.id.submitButton:

                String url = "https://resto.mprog.nl/order";
                Log.d("OrderFragment", "Submitting");
                RequestQueue queue = Volley.newRequestQueue(getContext());

                // Request a string response from the provided URL.
                JsonObjectRequest jsObjRequest = new JsonObjectRequest
                        (Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    Toast.makeText(getContext(), "You've submitted your order! The expected waiting time is: " + response.getString("preparation_time") + " minutes", Toast.LENGTH_LONG).show();
                                    mRestoDatabase.deleteDatabase();
                                    Intent intent = new Intent(getContext(), MainActivity.class);
                                    startActivity(intent);

                                    // finish the activity

                                } catch (JSONException e) {
                                    e.printStackTrace();
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

                break;
        }
    }
}
