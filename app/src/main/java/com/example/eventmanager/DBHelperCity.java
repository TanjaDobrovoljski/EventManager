package com.example.eventmanager;


import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import com.google.android.gms.maps.model.LatLng;

public class DBHelperCity extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "cities";
    public static final String CITY_TABLE_NAME = "city";
    public static final String CITY_COLUMN_ID = "id";
    public static final String CITY_COLUMN_NAME = "name";
    public static final String CITY_COLUMN_X = "x";
    public static final String CITY_COLUMN_Y = "y";



    private HashMap hp;
    private Context ctx;

    public DBHelperCity(Context context) {
        super(context, DATABASE_NAME , null, 1);
        ctx = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Toast.makeText(ctx, "onCreate", Toast.LENGTH_LONG).show();

        db.execSQL(
                "create table "+CITY_TABLE_NAME +
                        " (id integer primary key,name text,x real,y real)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " +CITY_TABLE_NAME);
        onCreate(db);
    }

    public boolean insertCity(String name,double x,double y) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("name", name);
        contentValues.put("x", x);
        contentValues.put("y", y);

        db.insert(CITY_TABLE_NAME, null, contentValues);
        return true;
    }

    @SuppressLint("Range")
    public LatLng getCoordinates(String cityName) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {"x", "y"};
        String selection = "name = ?";
        String[] selectionArgs = {cityName};

        Cursor cursor = db.query(CITY_TABLE_NAME, columns, selection, selectionArgs, null, null, null);
        if (cursor.moveToFirst()) {
            Double latitude = cursor.getDouble(cursor.getColumnIndex("x"));
            Double longitude = cursor.getDouble(cursor.getColumnIndex("y"));
            return new LatLng(latitude, longitude);
        }

        return null; // City not found
    }

    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+CITY_TABLE_NAME+" where id="+id+"", null );
        return res;
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, CITY_TABLE_NAME);
        return numRows;
    }


    public Integer deleteUser (Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(CITY_TABLE_NAME,
                "id = ? ",
                new String[] { Integer.toString(id) });
    }

    @SuppressLint("Range")
    public ArrayList<City> getAllCities() {
        ArrayList<City> cityList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("select * from "+CITY_TABLE_NAME, null);

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            Double x = cursor.getDouble(cursor.getColumnIndex("x"));
            Double y = cursor.getDouble(cursor.getColumnIndex("y"));


            City city = new City(id, name, x,y);
            cityList.add(city);
        }

        cursor.close();
        db.close();

        return cityList;
    }
    public void deleteAllCities() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(CITY_TABLE_NAME, null, null);
        db.close();
    }


}
