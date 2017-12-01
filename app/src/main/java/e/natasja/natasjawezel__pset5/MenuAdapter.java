package e.natasja.natasjawezel__pset5;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Natasja on 30-11-2017.
 */

public class MenuAdapter extends ArrayAdapter<MenuFragment.Dish> {
    public MenuAdapter(Context context, ArrayList<MenuFragment.Dish> dishes) {
        super(context, 0, dishes);
    }

    @NonNull
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        // get the data item for this position
        MenuFragment.Dish dish = getItem(position);

        // check if an existing view is being reused, otherwise inflate the view
        // source: https://github.com/codepath/android_guides/wiki/Using-an-ArrayAdapter-with-ListView
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.row_menu, parent, false);
        }

        // lookup views for data population
        TextView title = (TextView) view.findViewById(R.id.title);
        TextView description = (TextView) view.findViewById(R.id.description);
        TextView price = (TextView) view.findViewById(R.id.price);
        ImageView imageDish = (ImageView) view.findViewById(R.id.imageDish);

        // populate the data into template view using dish object
        title.setText(dish.name);
        description.setText(dish.description);
        price.setText("€" + dish.price + " ");
        if (dish.imageDish != null) {
            imageDish.setImageBitmap(dish.imageDish);
        }

        // return the completed view to render on screen
        return view;
    }
}
