package e.natasja.natasjawezel__pset5;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Natasja on 30-11-2017.
 */

public class RestoDatabase extends SQLiteOpenHelper {

    // this is where the unique instance of the class is stored, once made
    private static RestoDatabase instance = null;

    // some variables that we'll use often
    private static final String TABLE_NAME = "orders";
    private static final String COL0 = "_id";
    private static final String COL1 = "name";
    private static final String COL2 = "price";
    private static final String COL3 = "amount";

    // constructor of the class
    private RestoDatabase(Context context) {
        super(context, TABLE_NAME, null, 1);
    }

    // methods of the class

    /**
     * on create, creates a database with the id, name, price and amount
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("RestoDatabase", "Here");
        String createTable = "create table " + TABLE_NAME + " (" + COL0 + " INTEGER PRIMARY KEY, " + COL1 + " TEXT, " + COL2 + " FLOAT, " + COL3 + " INT);";
        Log.d("RestoDatabase", "There");
        db.execSQL(createTable);
        Log.d("RestoDatabase", "The database is created");
    }

    /**
     * on upgrade, drops the old database; calls the function oncreate to create a new one
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String dropTable = "DROP TABLE IF EXISTS " + TABLE_NAME;
        db.execSQL(dropTable);
        onCreate(db);
    }

    /**
     * because the database is a singleton, the constructor should be private. therefore you can't
     * construct a database from the mainactivity file. this function returns to you the (only)
     * instance of this database, that is created in thís file.
     * @param context
     * @return
     */
    public static RestoDatabase getInstance(Context context) {
        if (instance == null) {
            instance = new RestoDatabase(context);
        }
        return instance;
    }

    public void addData(String dish, int price) {
        SQLiteDatabase db = this.getWritableDatabase();

        if ( field_exists(dish) == true) {
            // if string dish is already in database, increase amount by 1

            Log.d("RestoDatabase", "This column existed, increasing amount..");
            String query = "UPDATE " + TABLE_NAME + " SET " + COL3 + " = (" + COL3 + " + " + 1 + ") WHERE " + COL1 + " = ?";
            Log.d("RestoDatabase", query);
            db.execSQL(query, new String[] {dish});
        } else {
            Log.d("RestoDatabase", "This column doesn't exist yet, creating..");
            // else, put in database, with amount 1
            ContentValues contentValues = new ContentValues();
            contentValues.put(COL1, dish);
            contentValues.put(COL2, price);
            contentValues.put(COL3, 1);

            db.insert(TABLE_NAME, null, contentValues);
        }
    }

    public void deleteDatabase() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE from " + TABLE_NAME;
        db.execSQL(query);
    }

    private boolean field_exists(String dish) throws SQLException{
        SQLiteDatabase db = this.getWritableDatabase();

        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COL1 + " = ?";
        Cursor mCursor = db.rawQuery(query, new String[] {dish});

        // true if column value exists
        if ((mCursor != null) && (mCursor.moveToFirst())) {
            mCursor.close();
            Log.d("RestoDatabse", "The column did exist");
            return true;
        }

        // false if it doesn't
        mCursor.close();
        Log.d("RestoDatabse", "The column didn't exist yet");
        return false;
    }

    public Cursor selectAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor data = db.rawQuery(query, null, null);
        return data;
    }
}
