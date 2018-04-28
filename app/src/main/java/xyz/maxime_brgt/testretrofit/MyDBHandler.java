package xyz.maxime_brgt.testretrofit;

import android.provider.BaseColumns;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;

import java.util.ArrayList;

public class MyDBHandler extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "filepathDB.db";
    public static final String TABLE_NAME = "photos";
    public static final String COLUMN_ID = "photoID";
    public static final String COLUMN_FILEPATH = "filepath";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_DESCRIPTION = "description";

    public MyDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + COLUMN_ID +
                " INTEGER PRIMARY KEY," + COLUMN_FILEPATH + " TEXT," +
                COLUMN_NAME + " TEXT," + COLUMN_DESCRIPTION + " TEXT)";
        db.execSQL(CREATE_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {}

    //SELECT * FROM PHOTOS
    public String loadHandler() {
        String result = "";
        String query = "Select * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            int result_0 = cursor.getInt(0);
            String result_1 = cursor.getString(1);
            String result_2 = cursor.getString(2);
            String result_3 = cursor.getString(3);
            result += String.valueOf(result_0) + " " + result_1 + " " + result_2 + " " + result_3 + System.getProperty("line.separator");
        }
        cursor.close();
        db.close();
        return result;
    }

    public ArrayList<String> loadPathsHandler() {
        ArrayList<String> result = new ArrayList<>();
        String query = "Select * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            String result_1 = cursor.getString(1);
            result.add(result_1);
        }
        cursor.close();
        db.close();
        return result;
    }

    public ArrayList<String> loadNamesHandler() {
        ArrayList<String> result = new ArrayList<>();
        String query = "Select * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            String result_2 = cursor.getString(2);
            result.add(result_2);
        }
        cursor.close();
        db.close();
        return result;
    }

    public ArrayList<String> loadDescriptionsHandler() {
        ArrayList<String> result = new ArrayList<>();
        String query = "Select * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            String result_3 = cursor.getString(3);
            result.add(result_3);
        }
        cursor.close();
        db.close();
        return result;
    }

    public void addHandler(Photo photo) {
        ContentValues values = new ContentValues();
        //values.put(COLUMN_ID, photo.getID());
        values.put(COLUMN_FILEPATH, photo.getFilepath());
        values.put(COLUMN_NAME, photo.getName());
        values.put(COLUMN_DESCRIPTION, photo.getDescription());
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_NAME, null, values);
        db.close();
    }
    public Photo findHandler(String filepath) {
        String query = "Select * FROM " + TABLE_NAME + " WHERE " + COLUMN_FILEPATH + " = '" + filepath + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Photo photo = new Photo();
        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            photo.setID(Integer.parseInt(cursor.getString(0)));
            photo.setFilepath(cursor.getString(1));
            photo.setName(cursor.getString(2));
            photo.setDescription(cursor.getString(3));
            cursor.close();
        } else {
            photo = null;
        }
        db.close();
        return photo;
    }
    public boolean deleteHandler(String filepath) {
        boolean result = false;
//        String query = "Select * FROM " + TABLE_NAME + " WHERE " + COLUMN_FILEPATH + " = '" + filepath + "'";
        SQLiteDatabase db = this.getWritableDatabase();
//        Cursor cursor = db.rawQuery(query, null);
//        Photo photo = new Photo();
//        if (cursor.moveToFirst()) {
//            photo.setFilepath(cursor.getString(1));
//            db.delete(TABLE_NAME, COLUMN_FILEPATH + "=?",
//                    new String[] {
//            });
//            cursor.close();
//            result = true;
//        }
        db.execSQL("DELETE FROM " + TABLE_NAME + " WHERE " + COLUMN_FILEPATH + " = '" + filepath + "'");
        result = true;
        db.close();
        return result;
    }

    public boolean clearHandler() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME);
        db.close();
        return true;
    }

    public boolean updateHandler(Photo photo, String name, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE " + TABLE_NAME + " SET " + COLUMN_ID + " = '" + photo.getID() + "', " +
                COLUMN_FILEPATH + " = '" + photo.getFilepath() + "', " + COLUMN_NAME + " = '" + name + "', "
                + COLUMN_DESCRIPTION  + " = '" + description + "'" + " WHERE " + COLUMN_ID + "= '" + photo.getID() + "'" + ";");
        db.close();
        return true;
    }
}