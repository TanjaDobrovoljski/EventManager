package com.example.eventmanager;


import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
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
    private static final String TEMP_TABLE_NAME = "temp_activities";

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
    public static final String ACTIVITY_COLUMN_COLOR = "color";

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
                        " (id integer primary key,type text,name text,time text,description text, city text,date text,image1 blob,image2 blob,color real)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ACTIVITY_TABLE_NAME);
        onCreate(db);
    }

    public boolean insertActivity(String type,String name, String time, String description,String city,String date,Bitmap image1, Bitmap image2,int color) {
        SQLiteDatabase db = this.getWritableDatabase();

        if (activityExists(name)) {
            return false; // Object already exists, return false
        }

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
        contentValues.put("color", color);

        db.insert(ACTIVITY_TABLE_NAME, null, contentValues);


        return true;
    }

   /* private void sortActivitiesByDate() {
        Collections.sort(activityList, new Comparator<Activity>() {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

            @Override
            public int compare(Activity activity1, Activity activity2) {
                try {
                    Date date1 = dateFormat.parse(activity1.getDate());
                    Date date2 = dateFormat.parse(activity2.getDate());
                    return date1.compareTo(date2);
                } catch (ParseException e) {
                    e.printStackTrace();
                    return 0;
                }
            }
        });
    }
*/

    private boolean activityExists(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + ACTIVITY_TABLE_NAME + " WHERE name = ?";
        String[] selectionArgs = {name};
        Cursor cursor = db.rawQuery(query, selectionArgs);
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    @SuppressLint("Range")
    public int getActivityId(String type, String name, String time, String description, String city, String date) {
        SQLiteDatabase db = this.getReadableDatabase();

        // Define the columns to retrieve
        String[] columns = {"id"};

        // Define the WHERE clause with the given attributes
        String selection = "type = ? AND name = ? AND time = ? AND description = ? AND city = ? AND date = ?";
        String[] selectionArgs = {type, name, time, description, city, date};

        // Execute the query
        Cursor cursor = db.query(ACTIVITY_TABLE_NAME, columns, selection, selectionArgs, null, null, null);

        int activityId = -1; // Default value if activity is not found

        if (cursor.moveToFirst()) {
            // Retrieve the id from the cursor
            activityId = cursor.getInt(cursor.getColumnIndex("id"));
        }

        // Close the cursor
        cursor.close();

        return activityId;
    }


    /*public boolean deleteActivity(int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Define the WHERE clause with the id value
        String whereClause = "id = ?";
        String[] whereArgs = { String.valueOf(id) };

        // Delete the row with the given id from the database
        int rowsDeleted = db.delete(ACTIVITY_TABLE_NAME, whereClause, whereArgs);

        // Check if any row was deleted
        return rowsDeleted > 0;
    }
*/

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

    public boolean updateActivity (Integer id,String type, String name, String time, String description,String city,String date,Bitmap image1, Bitmap image2) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("time", time);
        contentValues.put("description", description);
        contentValues.put("city", city);
        contentValues.put("date", date);
        contentValues.put("type", type);

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

        if (cursor.moveToFirst()) {
            do {
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

            int color = cursor.getInt(cursor.getColumnIndex("color"));
            // Create an Activity object and add it to the activityList
            Activity activity = new Activity(id,type, name, time, description, city, date, image1, image2,color);
            activityList.add(activity);
        } while (cursor.moveToNext());
    }
        cursor.close();
        db.close();

        return activityList;
    }
    @SuppressLint("Range")
    public ArrayList<Activity> getAllActivitiesSortedByDate() {
        ArrayList<Activity> activityList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + ACTIVITY_TABLE_NAME + " ORDER BY date ASC, time ASC", null);

        if (cursor.moveToFirst()) {
            do {
            // Retrieve activity details from the cursor
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

            int color = cursor.getInt(cursor.getColumnIndex("color"));

            // Create an Activity object and add it to the activityList
            Activity activity = new Activity(id, type, name, time, description, city, date, image1, image2, color);
            activityList.add(activity);
            } while (cursor.moveToNext());
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

            int color = cursor.getInt(cursor.getColumnIndex("color"));
            // Create an Activity object
            activity = new Activity(id, type, name, time, description, city, date, image1, image2,color);
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

    @SuppressLint("Range")
    public ArrayList<Activity> getActivitiesForDateRange(String startDate, int daysAfter) {
        ArrayList<Activity> activities = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Define the date range for the query
        String endDate = calculateEndDate(startDate, daysAfter);

        // Define the format for dates in the "yyyy-M-dd" format
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            // Parse the start and end dates and format them in "yyyy-M-dd" format
            Date startDateObject = dateFormat.parse(startDate);
            Date endDateObject = dateFormat.parse(endDate);
            startDate = dateFormat.format(startDateObject);
            endDate = dateFormat.format(endDateObject);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Define the query to retrieve activities within the specified date range
        String query = "SELECT * FROM " + ACTIVITY_TABLE_NAME +
                " WHERE date =? ORDER BY date ASC";


        Cursor cursor = db.rawQuery(query, new String[]{endDate});

        // Check if the cursor has any data
        if (cursor != null && cursor.moveToFirst()) {
            do {
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

                int color = cursor.getInt(cursor.getColumnIndex("color"));

                // Create an Activity object and add it to the activityList
                Activity activity = new Activity(id, type, name, time, description, city, date, image1, image2, color);
                activities.add(activity);
            } while (cursor.moveToNext());
        }

        // Close the cursor and database connection
        if (cursor != null) {
            cursor.close();
        }
        db.close();

        return activities;
    }



    private String calculateEndDate(String startDate, int daysAfter) {
        // Convert the start date to a Date object
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date;
        try {
            date = dateFormat.parse(startDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return startDate; // Return the start date as-is in case of parsing error
        }

        // Calculate the end date by adding the specified number of days to the start date
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, daysAfter);
        return dateFormat.format(calendar.getTime());
    }


    @SuppressLint("Range")
    public ArrayList<Activity> getActivitiesForNextHour(String startDate) {
        ArrayList<Activity> activities = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            // Parse the start and end dates and format them in "yyyy-M-dd" format
            Date startDateObject = dateFormat.parse(startDate);
            startDate = dateFormat.format(startDateObject);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Get the current date and time
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        String currentDate = dateFormat.format(calendar.getTime());
        String currentTime = timeFormat.format(calendar.getTime());

        // Calculate the end time for the next hour
        calendar.add(Calendar.HOUR_OF_DAY, 1);
        String nextHour = timeFormat.format(calendar.getTime());

        // Define the query to retrieve activities within the next hour
        String query = "SELECT * FROM " + ACTIVITY_TABLE_NAME +
                " WHERE date = ? AND time > ? AND time <= ? ORDER BY date ASC, time ASC";

        Cursor cursor = db.rawQuery(query, new String[]{startDate, currentTime, nextHour});

        if (cursor != null && cursor.moveToFirst()) {
            do {
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

                int color = cursor.getInt(cursor.getColumnIndex("color"));

                // Create an Activity object and add it to the activityList
                Activity activity = new Activity(id, type, name, time, description, city, date, image1, image2, color);
                activities.add(activity);
            } while (cursor.moveToNext());
        }

        // Close the cursor and database connection
        if (cursor != null) {
            cursor.close();
        }
        db.close();

        return activities;
    }

    @SuppressLint("Range")
    public ArrayList<Activity> getActivitiesForDate(String date2) {
        ArrayList<Activity> activities = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Define the date range for the query


        // Define the format for dates in the "yyyy-M-dd" format
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            // Parse the start and end dates and format them in "yyyy-M-dd" format
            Date startDateObject = dateFormat.parse(date2);

            date2 = dateFormat.format(startDateObject);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Define the query to retrieve activities within the specified date range
        String query = "SELECT * FROM " + ACTIVITY_TABLE_NAME +
                " WHERE date =? ORDER BY time ASC";


        Cursor cursor = db.rawQuery(query, new String[]{date2});

        // Check if the cursor has any data
        if (cursor != null && cursor.moveToFirst()) {
            do {
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

                int color = cursor.getInt(cursor.getColumnIndex("color"));

                // Create an Activity object and add it to the activityList
                Activity activity = new Activity(id, type, name, time, description, city, date, image1, image2, color);
                activities.add(activity);
            } while (cursor.moveToNext());
        }

        // Close the cursor and database connection
        if (cursor != null) {
            cursor.close();
        }
        db.close();

        return activities;
    }

}
