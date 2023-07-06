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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.Toast;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "activities";
    public static final String ACTIVITY_TABLE_NAME = "activity";
    public static final String ACTIVITY_COLUMN_ID = "id";
    public static final String ACTIVITY_COLUMN_TYPE = "type";
    public static final String ACTIVITY_COLUMN_NAME = "name";
    public static final String ACTIVITY_COLUMN_TIME = "time";
    public static final String ACTIVITY_COLUMN_DESCRIPTION = "description";
    public static final String ACTIVITY_COLUMN_LOCATION = "location";
    public static final String ACTIVITY_COLUMN_DATE = "date";
    public static final String ACTIVITY_COLUMN_IMAGE1 = "image1";
    public static final String ACTIVITY_COLUMN_IMAGE2 = "image2";


    private HashMap hp;
    private Context ctx;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME , null, 1);
        ctx = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Toast.makeText(ctx, "onCreate", Toast.LENGTH_LONG).show();

        db.execSQL(
                "create table "+ACTIVITY_TABLE_NAME +
                        " (id integer primary key,type text,name text,time text,description text, city text,date text,image1 blob,image2 blob)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ACTIVITY_TABLE_NAME);
        onCreate(db);
    }

    public boolean insertActivity(String type,String name, String time, String description,String city,String date,Bitmap image1, Bitmap image2) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("type", type);
        contentValues.put("name", name);
        contentValues.put("time", time);
        contentValues.put("description", description);
        contentValues.put("city", city);
        contentValues.put("date", date);
        if (image1 != null) {
            byte[] image1ByteArray = getBitmapAsByteArray(image1);
            contentValues.put("image1", image1ByteArray);
        } else {
            contentValues.put("image1", new byte[0]);
        }

        // Convert image2 Bitmap to byte array and insert into the database
        if (image2 != null) {
            byte[] image2ByteArray = getBitmapAsByteArray(image2);
            contentValues.put("image2", image2ByteArray);
        } else {
            contentValues.put("image2", new byte[0]);
        }


        db.insert(ACTIVITY_TABLE_NAME, null, contentValues);
        return true;
    }
    private static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }

    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+ACTIVITY_TABLE_NAME+" where id="+id+"", null );
        return res;
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, ACTIVITY_TABLE_NAME);
        return numRows;
    }

    public boolean updateActivity (Integer id,String type, String name, String time, String description,City city,String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("time", time);
        contentValues.put("description", description);
        contentValues.put("location", city.getName());
        contentValues.put("date", date);
        contentValues.put("type", type);

        db.update(ACTIVITY_TABLE_NAME, contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    public Integer deleteActivity (Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(ACTIVITY_TABLE_NAME,
                "id = ? ",
                new String[] { Integer.toString(id) });
    }

    @SuppressLint("Range")
    public ArrayList<Activity> getAllActivities() {
        ArrayList<Activity> activityList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("select * from "+ACTIVITY_TABLE_NAME, null);

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String type = cursor.getString(cursor.getColumnIndex("type"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String time = cursor.getString(cursor.getColumnIndex("time"));
            String description = cursor.getString(cursor.getColumnIndex("description"));
            String city = cursor.getString(cursor.getColumnIndex("city"));
            String date = cursor.getString(cursor.getColumnIndex("date"));

            // Retrieve the image1 and image2 byte arrays
            byte[] image1ByteArray = cursor.getBlob(cursor.getColumnIndex("image1"));
            byte[] image2ByteArray = cursor.getBlob(cursor.getColumnIndex("image2"));

            // Convert the byte arrays to Bitmaps
            Bitmap image1 = BitmapFactory.decodeByteArray(image1ByteArray, 0, image1ByteArray.length);
            Bitmap image2 = BitmapFactory.decodeByteArray(image2ByteArray, 0, image2ByteArray.length);

            // Create an Activity object and add it to the activityList
            Activity activity = new Activity(id,type, name, time, description, city, date, image1, image2);
            activityList.add(activity);
        }

        cursor.close();
        db.close();

        return activityList;
    }

    @SuppressLint("Range")
    public Activity getActivity(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + ACTIVITY_TABLE_NAME + " WHERE id=?", new String[]{String.valueOf(id)});

        Activity activity = null;

        if (cursor.moveToFirst()) {
            String type = cursor.getString(cursor.getColumnIndex("type"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String time = cursor.getString(cursor.getColumnIndex("time"));
            String description = cursor.getString(cursor.getColumnIndex("description"));
            String city = cursor.getString(cursor.getColumnIndex("city"));
            String date = cursor.getString(cursor.getColumnIndex("date"));

            // Retrieve the image1 and image2 byte arrays
            byte[] image1ByteArray = cursor.getBlob(cursor.getColumnIndex("image1"));
            byte[] image2ByteArray = cursor.getBlob(cursor.getColumnIndex("image2"));

            // Convert the byte arrays to Bitmaps
            Bitmap image1 = BitmapFactory.decodeByteArray(image1ByteArray, 0, image1ByteArray.length);
            Bitmap image2 = BitmapFactory.decodeByteArray(image2ByteArray, 0, image2ByteArray.length);

            // Create an Activity object
            activity = new Activity(id, type, name, time, description, city, date, image1, image2);
        }

        cursor.close();
        db.close();

        return activity;
    }

    public void deleteAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(ACTIVITY_TABLE_NAME, null, null);
        db.close();
    }

   /* @SuppressLint("Range")
    public ArrayList<Activity> getAllUsers() {
        ArrayList<Activity> array_list = new ArrayList<Activity>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+ACTIVITY_TABLE_NAME, null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(new Activity(res.getInt(res.getColumnIndex("id")),res.getString(res.getColumnIndex(ACTIVITY_COLUMN_TYPE)),res.getString(res.getColumnIndex(ACTIVITY_COLUMN_NAME)),res.getString(res.getColumnIndex(ACTIVITY_COLUMN_TIME)),res.getString(res.getColumnIndex(ACTIVITY_COLUMN_DESCRIPTION)),res.getString(res.getColumnIndex(ACTIVITY_COLUMN_LOCATION)), res.getString(res.getColumnIndex(ACTIVITY_COLUMN_DATE))));
            res.moveToNext();
        }
        return array_list;
    }*/


}
