package com.techaj.apoorvajain.protectpassword.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.techaj.apoorvajain.protectpassword.Model.PasswordData;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "passwordManager";
    private static final String TABLE_NAME = "passwords";
    private static final String COLOUMN_ID = "id";
    private static final String COLOUMN_TITLE = "title";
    private static final String COLOUMN_USER_NAME = "user_name";
    private static final String COLOUMN_PASSWORD = "password";
    private static final String COLOUMN_NOTES = "notes";
    private static final String COLOUMN_LAST_UPDATED = "last_updated";

    private static final String TABLE_NAME1 = "mstPassword";

    private static final String COLOUMN_IV = "IV";
    private static final int FIXED_ID = 123456;


    // private static

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME + "("
                        + COLOUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + COLOUMN_TITLE + " TEXT,"
                        + COLOUMN_USER_NAME + " TEXT,"
                        + COLOUMN_PASSWORD + " TEXT,"
                        + COLOUMN_NOTES + " TEXT,"
                        + COLOUMN_LAST_UPDATED + " TEXT"//" DATETIME DEFAULT CURRENT_TIMESTAMP"
                        + ")";
        String CREATE_TABLE1 = "CREATE TABLE " + TABLE_NAME1 + "("                     // to store master password
                + COLOUMN_ID + " INTEGER PRIMARY KEY,"
                + COLOUMN_IV + " TEXT"
                + ")";

        db.execSQL(CREATE_TABLE);
        db.execSQL(CREATE_TABLE1);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME1);


        // Create tables again
        onCreate(db);
    }

    public long insertData(PasswordData data) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLOUMN_TITLE, data.getTitle());
        values.put(COLOUMN_USER_NAME, data.getUserName());
        values.put(COLOUMN_PASSWORD, data.getPassword());
        values.put(COLOUMN_NOTES, data.getNotes());
        values.put(COLOUMN_LAST_UPDATED, getDateTime());
        // insert row
        long id = db.insert(TABLE_NAME, null, values);

        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }

    public PasswordData getData(long id) {

        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT  * FROM " + TABLE_NAME + " WHERE " + COLOUMN_ID + " = " + id;
        Cursor cursor = db.rawQuery(query, null);

        PasswordData data = new PasswordData();

        if (cursor != null && cursor.moveToFirst()) {
            Log.e("AJ", cursor.getColumnIndex(COLOUMN_ID) + id + "");
            data.setId(cursor.getInt(cursor.getColumnIndex(COLOUMN_ID)));
            data.setTitle(cursor.getString(cursor.getColumnIndex(COLOUMN_TITLE)));
            data.setPassword(cursor.getString(cursor.getColumnIndex(COLOUMN_PASSWORD)));

            data.setLastUpdated(
                    convertDate(cursor.getString(cursor.getColumnIndex(COLOUMN_LAST_UPDATED))));
            data.setUserName(cursor.getString(cursor.getColumnIndex(COLOUMN_USER_NAME)));
            data.setNotes(cursor.getString(cursor.getColumnIndex(COLOUMN_NOTES)));
            cursor.close();
        }
        db.close();
        return data;


    }

    public List<PasswordData> getAllData() {
        List<PasswordData> passwordList = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NAME + " ORDER BY " +
                COLOUMN_LAST_UPDATED + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                PasswordData data = new PasswordData();
                data.setId(cursor.getInt(cursor.getColumnIndex(COLOUMN_ID)));
                data.setTitle(cursor.getString(cursor.getColumnIndex(COLOUMN_TITLE)));
                data.setPassword(cursor.getString(cursor.getColumnIndex(COLOUMN_PASSWORD)));
                data.setLastUpdated(
                        convertDate
                        (cursor.getString(cursor.getColumnIndex(COLOUMN_LAST_UPDATED))));
                data.setUserName(cursor.getString(cursor.getColumnIndex(COLOUMN_USER_NAME)));


                passwordList.add(data);
            } while (cursor.moveToNext());
        }
        cursor.close();
        // close db connection
        db.close();

        // return notes list
        return passwordList;
    }

    public int updateData(PasswordData data) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLOUMN_TITLE, data.getTitle());
        values.put(COLOUMN_USER_NAME, data.getUserName());
        values.put(COLOUMN_PASSWORD, data.getPassword());
        values.put(COLOUMN_NOTES, data.getNotes());
        values.put(COLOUMN_LAST_UPDATED, getDateTime());

        // updating row
        int i =
                db.update(TABLE_NAME, values, COLOUMN_ID + " = ?",
                        new String[]{String.valueOf(data.getId())});

        db.close();
        return i;
// check if timestamp updates or not
    }

    public int deleteData(long id) {

        SQLiteDatabase db = this.getWritableDatabase();
        int i = db.delete(TABLE_NAME, COLOUMN_ID + " = ?",
                new String[]{"" + id});

        db.close();
        return i;
    }

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
            //    "dd-MMM-yyyy\nhh:mm a", Locale.getDefault());
                 "yyyy-MM-dd\nhh:mm a", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    private String convertDate(String date){
        DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd\nhh:mm a", Locale.getDefault());
        DateFormat targetFormat = new SimpleDateFormat("dd-MMM-yyyy\nhh:mm a");

        Date date1 = null;
        try {
            date1 = originalFormat.parse(date);
            return targetFormat.format(date1);
        } catch (ParseException e) {
            e.printStackTrace();
Log.i("AJ11","catch");
            return (date);
        }
              // changes here
    }

    public void insertIV(String IV) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLOUMN_ID, FIXED_ID);
        values.put(COLOUMN_IV, IV);
        db.insert(TABLE_NAME1, null, values);

        // close db connection
        db.close();

    }

    public String matchIV() {         //get IV
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT  * FROM " + TABLE_NAME1 + " WHERE " + COLOUMN_ID + " = " + FIXED_ID;
        Cursor cursor = db.rawQuery(query, null);
        String data = null;
        if (cursor != null && cursor.moveToFirst()) {
            data = cursor.getString(cursor.getColumnIndex(COLOUMN_IV));
        }
        assert cursor != null;
        cursor.close();
        db.close();
        return data;
    }
}
