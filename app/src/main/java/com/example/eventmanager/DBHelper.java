package com.example.eventmanager;


import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
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
                        " (id integer primary key,type text,name text,time text,description text, city text,date text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ACTIVITY_TABLE_NAME);
        onCreate(db);
    }

    public boolean insertUser(String type,String name, String time, String description,String city,String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("type", type);
        contentValues.put("name", name);
        contentValues.put("time", time);
        contentValues.put("description", description);
        contentValues.put("city", city);
        contentValues.put("date", date);

        db.insert(ACTIVITY_TABLE_NAME, null, contentValues);
        return true;
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

    public boolean updateUser (Integer id,String type, String name, String time, String description,City city,String date) {
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

    public Integer deleteUser (Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(ACTIVITY_TABLE_NAME,
                "id = ? ",
                new String[] { Integer.toString(id) });
    }

    @SuppressLint("Range")
    public ArrayList<String> getAllUser() {
        ArrayList<String> array_list = new ArrayList<String>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+ ACTIVITY_TABLE_NAME, null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex(ACTIVITY_COLUMN_NAME)));
            res.moveToNext();
        }
        return array_list;
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
