package com.SmartTech.teasyNew.activity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.SmartTech.teasyNew.model.walletdataModel;

import java.util.ArrayList;


public class database {
    static myDbHelper myhelper;
    public database(Context context)
    {
        myhelper = new myDbHelper(context);
    }

    public static long insertPin(int id, String pin)
    {
        SQLiteDatabase db = myhelper.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("ID", id);
            contentValues.put("mPin", pin);

          long s = db.insertWithOnConflict("PIN", null, contentValues,SQLiteDatabase.CONFLICT_REPLACE);
          return s;

    }

    public static long insertToggle(int id, int status)
    {
        SQLiteDatabase db = myhelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ID", id);
        contentValues.put("mTOGGLE", status);
        long s = db.insertWithOnConflict("TOGGLE", null, contentValues,SQLiteDatabase.CONFLICT_REPLACE);
        return s;

    }

    public static long insertNotification(int id, String time)
    {
        SQLiteDatabase db = myhelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ID", id);
        contentValues.put("time", time);
        long s = db.insertWithOnConflict("Notification", null, contentValues,SQLiteDatabase.CONFLICT_REPLACE);
        return s;

    }
    public static long insertWallet(int id, String wallet, String type, String shortCode) {
        SQLiteDatabase db = myhelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ID", id);
        contentValues.put("wallet", wallet);
        contentValues.put("userType", type);
        contentValues.put("shortcode", shortCode);
        long s = db.insertWithOnConflict("Wallet1", null, contentValues,SQLiteDatabase.CONFLICT_REPLACE);
        return s;

    }



    static class myDbHelper extends SQLiteOpenHelper
    {
        private static final String CREATE_wallet = "CREATE TABLE Wallet1 (ID INTEGER PRIMARY KEY, wallet VARCHAR(10000), userType VARCHAR(10000),shortcode VARCHAR(10000));";
        private static final String CREATE_notification = "CREATE TABLE Notification (ID INTEGER PRIMARY KEY, time VARCHAR(10000));";
        private static final String CREATE_Pin = "CREATE TABLE PIN (ID INTEGER PRIMARY KEY, mPin VARCHAR(10000));";
        private static final String DROP_Pin ="DROP TABLE IF EXISTS PiN";
        private static final String Drop_notification ="DROP TABLE IF EXISTS Notification";
        private static final String Drop_wallet ="DROP TABLE IF EXISTS Wallet1";
        private static final String CREATE_toggle = "CREATE TABLE TOGGLE (ID INTEGER PRIMARY KEY, mTOGGLE INTEGER);";
        private static final String DROP_toggle ="DROP TABLE IF EXISTS TOGGLE";

        private final Context context;
        public myDbHelper(Context context) {
            super(context, "TeasyDB", null, 9);
            this.context=context;
        }

        public void onCreate(SQLiteDatabase db) {

            try {
                // new database schema
                db.execSQL(CREATE_Pin);
                db.execSQL(CREATE_toggle);
                db.execSQL(CREATE_wallet);
                db.execSQL(CREATE_notification);

            } catch (Exception e) {

            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            try {
                //new database schema
                db.execSQL(DROP_Pin);
                db.execSQL(DROP_toggle);
                db.execSQL(Drop_notification);
                db.execSQL(Drop_wallet);
                onCreate(db);
            }catch (Exception e) {

            }
        }
    }

    public int getLEV(){
       ArrayList mCount = new ArrayList();

        // Select All Query
        String selectQuery = "SELECT  * FROM PIN";

        SQLiteDatabase db = myhelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);//selectQuery,selectedArguments

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            mCount.add(cursor.getString(1));//adding 2nd column data
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return mCount.size();
    }

    public String getTPin(){
        String pin = null;

        // Select All Query
        String selectQuery = "SELECT  * FROM PIN";

        SQLiteDatabase db = myhelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);//selectQuery,selectedArguments

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            pin = cursor.getString(1);//adding 2nd column data
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return pin;
    }

    public int getmStatus(){
        int status =0;

        // Select All Query
        String selectQuery = "SELECT  * FROM TOGGLE";

        SQLiteDatabase db = myhelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);//selectQuery,selectedArguments

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            status = cursor.getInt(1);//adding 2nd column data
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return status;
    }

    public int getNotificationToggleStatus() {
        int status = 3;

        // Select All Query
        String selectQuery = "SELECT  * FROM TOGGLE";

        SQLiteDatabase db = myhelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);//selectQuery,selectedArguments

        // looping through all rows and adding to list
        if (cursor.moveToFirst()){
            do {
                if (cursor.getInt(0) == 2) {
                    status = cursor.getInt(1);//adding 2nd column data
                    break;
                }

            }while(cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return status;
    }


    public int getPolicyStatus() {
        int status = 3;

        // Select All Query
        String selectQuery = "SELECT  * FROM TOGGLE";

        SQLiteDatabase db = myhelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);//selectQuery,selectedArguments

        // looping through all rows and adding to list
        if (cursor.moveToFirst()){
            do {
                if (cursor.getInt(0) == 3) {
                    status = cursor.getInt(1);//adding 2nd column data
                    break;
                }

            }while(cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return status;
    }

    public String getAllNotification(){
        String pin = null;

        // Select All Query
        String selectQuery = "SELECT  * FROM Notification";

        SQLiteDatabase db = myhelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);//selectQuery,selectedArguments

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            pin = cursor.getString(1);//adding 2nd column data
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return pin;
    }

    public walletdataModel getAllWallet(){
        walletdataModel model = new walletdataModel();

        // Select All Query
        String selectQuery = "SELECT  * FROM Wallet1";

        SQLiteDatabase db = myhelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);//selectQuery,selectedArguments

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            model.setWallet(cursor.getString(1));
            model.setShortcode(cursor.getString(3));
            model.setType(cursor.getString(2));
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return model;
    }

    public int getNotificationSize(){
        ArrayList mCount = new ArrayList();

        // Select All Query
        String selectQuery = "SELECT  * FROM Notification";

        SQLiteDatabase db = myhelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);//selectQuery,selectedArguments

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            mCount.add(cursor.getString(1));//adding 2nd column data
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return mCount.size();
    }

    public int getWalletSize(){
        ArrayList mCount = new ArrayList();

        // Select All Query
        String selectQuery = "SELECT  * FROM Wallet1";

        SQLiteDatabase db = myhelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);//selectQuery,selectedArguments

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            mCount.add(cursor.getString(1));//adding 2nd column data
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return mCount.size();
    }
}