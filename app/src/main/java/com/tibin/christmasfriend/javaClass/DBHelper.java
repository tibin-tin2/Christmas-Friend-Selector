package com.tibin.christmasfriend.javaClass;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class DBHelper extends SQLiteOpenHelper {

    final static String DATABASE_NAME_FRIEND = "friend";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME_FRIEND, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE names (id INTEGER PRIMARY KEY AUTOINCREMENT, name text, isSelected INTEGER DEFAULT 0, isSelectedBy INTEGER DEFAULT 0)");
        List<String> names = new FriendsList().getNames();
        insertNames(names, db);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS names");
        onCreate(db);
    }

    public boolean insertNames(List<String> names) {
        for (String name : names) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("name", name);
            db.insert("names", null, contentValues);
        }
        return true;
    }

    public boolean insertNames(List<String> names, SQLiteDatabase db) {
        for (String name : names) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("name", name);
            db.insert("names", null, contentValues);
        }
        return true;
    }

    public Cursor getNames() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM names", null);
        return res;
    }

    public int numberOfRowsNames() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, "names");
        return numRows;
    }

    public Integer deleteName(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("names", "id= ? ", new String[]{Integer.toString(id)});
    }

//    public List<Map<String, Boolean>> getAllNames() {
//        List<Map<String, Boolean>> names = new ArrayList<>();
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor res = db.rawQuery("SELECT * FROM names", null);
//        res.moveToFirst();
//
//        while (res.isAfterLast() == false) {
//            Map<String, Boolean> myMap = new HashMap<>();
//            if(res.getInt(res.getColumnIndex("isSelected")) == 0) {
//                myMap.put(res.getString(res.getColumnIndex("name")),Boolean.FALSE);
//            } else {
//                myMap.put(res.getString(res.getColumnIndex("name")),Boolean.TRUE);
//            }
//            names.add(myMap);
//            res.moveToNext();
//        }
//        return names;
//    }

    public List<String> getAllNames() {

        List<String> names = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM names", null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            names.add(res.getString(res.getColumnIndex("name")));
            res.moveToNext();
        }
        return names;
    }

    public List<Map<String, Boolean>> getAllNamesWithIsSelected() {
        List<Map<String, Boolean>> names = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM names", null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            Map<String, Boolean> myMap = new HashMap<>();
            if (res.getInt(res.getColumnIndex("isSelected")) == 0) {
                myMap.put(res.getString(res.getColumnIndex("name")), Boolean.FALSE);
            } else {
                myMap.put(res.getString(res.getColumnIndex("name")), Boolean.TRUE);
            }
            names.add(myMap);
            res.moveToNext();
        }
        return names;
    }

    public List<Map<String, Boolean>> getAllNamesWithIsSelectedByFalse() {
        List<Map<String, Boolean>> names = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM names", null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            Map<String, Boolean> myMap = new HashMap<>();
            if (res.getInt(res.getColumnIndex("isSelectedBy")) == 0) {
                myMap.put(res.getString(res.getColumnIndex("name")), Boolean.FALSE);
            } else {
                myMap.put(res.getString(res.getColumnIndex("name")), Boolean.TRUE);
            }
            names.add(myMap);
            res.moveToNext();
        }
        return names;
    }

    public void updateIsSelectedInNames(String name, Boolean isSelected) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("isSelected", isSelected ? 1 : 0);
        db.update("names", contentValues, "name = ? ", new String[]{name});
    }

//    public void updateIsSelectedInNames( SQLiteDatabase db) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues contentValues = new ContentValues();
//        contentValues.put("isSelected", Boolean.TRUE ? 1 : 0);
//        db.update("names", contentValues, "id = ? ", new String[]{Integer.toString(1)});
//    }

    public void clearAllIsSelected() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("isSelected", 0);
        db.update("names",contentValues,null, null);
    }

}
