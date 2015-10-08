package com.glitterlabs.caixia.Databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/*
    Copyright (c) 2015-2020 Glitter Technology Ventures, LLC.
    All rights reserved. Patents pending.
    Responsible: Abhay Bhusari
 */
public class DatabseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "myfriends.db";
    private static final String TABLE_NAME = "myfriends_table";
    private static final String ID = "ID";
    private static final String NAME = "NAME";
    private static final String USER_NAME = "USER_NAME";
    private static final String USER_ID = "USER_ID";
    private static final int DATABASE_VERSION = 1;

    public DatabseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        // SQLiteDatabase db= this.getWritableDatabase();
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT,USER_NAME TEXT, USER_ID TEXT NOT NULL, UNIQUE (USER_ID))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String name, String userName, String userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NAME, name);
        contentValues.put(USER_NAME, userName);
        contentValues.put(USER_ID, userId);
        long result = db.insert(TABLE_NAME, null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Cursor getAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor  cursor= db.rawQuery("select * from "+ TABLE_NAME,null);
        return cursor;
    }

    public void deleteItem(String userID)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        try
        {
            db.delete(TABLE_NAME, "USER_ID ="+userID, null);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            db.close();
        }
    }


}
