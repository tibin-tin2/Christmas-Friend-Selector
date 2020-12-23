package com.tibin.christmasfriend.javaClass;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBHelper extends SQLiteOpenHelper {

    final static String DATABASE_NAME_FRIEND = "friend";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME_FRIEND, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE names (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, isSelected INTEGER DEFAULT 0, isSelectedBy INTEGER DEFAULT 0)");
        db.execSQL("CREATE TABLE friends (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, friend TEXT)");
        List<String> names = new FriendsList().getNames();
        insertNames(names, db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS names");
        db.execSQL("DROP TABLE IF EXISTS friends");
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

    public Integer deleteName(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("names", "name = ? ", new String[]{name});
    }

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

        while (!res.isAfterLast()) {
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

    public List<String> getAllNamesWithIsSelectedByFalse() {
        List<String> names = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM names WHERE isSelectedBy = '0'", null);
        res.moveToFirst();

        while (!res.isAfterLast()) {
            names.add(res.getString(res.getColumnIndex("name")));
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

    public void updateIsSelectedByInNames(String name, Boolean isSelected) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("isSelectedBy", isSelected ? 1 : 0);
        db.update("names", contentValues, "name = ? ", new String[]{name});
    }


    public void clearAllIsSelected() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("isSelected", 0);
        db.update("names", contentValues, null, null);
    }

    public void clearAllIsSelectedBy() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("isSelectedBy", 0);
        db.update("names", contentValues, null, null);
    }

    public void addNew(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("isSelected", 0);
        contentValues.put("isSelectedBy", 0);
        db.insert("names", null, contentValues);
    }

    public void clearIsSelected(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("isSelected", 0);
        db.update("names", contentValues, "name = ?", new String[]{name});
    }

    public void clearIsSelectedBy(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("isSelectedBy", 0);
        db.update("names", contentValues, "name = ?", new String[]{name});
    }

    public void insertFriendTable(String name, String friend) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("friend", friend);
        db.insert("friends", null, contentValues);
    }

    public void clearFriendsTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("friends", null, null);
    }

    public Cursor getAllFriends() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT name, friend FROM friends", null);
        return res;
    }

}
