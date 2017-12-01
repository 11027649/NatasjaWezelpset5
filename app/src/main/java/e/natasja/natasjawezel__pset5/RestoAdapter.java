package e.natasja.natasjawezel__pset5;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;

/**
 * Created by Natasja on 30-11-2017.
 */

public class RestoAdapter extends ResourceCursorAdapter {
    public RestoAdapter(Context context, Cursor cursor) {
        super(context, R.layout.row_resto, cursor, 0);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView dish = (TextView) view.findViewById(R.id.dish);
        TextView amount = (TextView) view.findViewById(R.id.orderAmount);

        Integer amount_i = cursor.getColumnIndex("amount");
        Integer dish_i = cursor.getColumnIndex("name");

        Integer amount_value = cursor.getInt(amount_i);
        String dish_value = cursor.getString(dish_i);

        dish.setText(dish_value);
        amount.setText(amount_value.toString());
    }
}
