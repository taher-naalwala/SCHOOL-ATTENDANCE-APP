package com.sabaq.chat;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;


public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "sabaq.db";
    public static final String TABLE_NAME = "student_table";
    public static final String COL_1 = "NAME";
    public static final String COL_2 = "DAY";
    public static final String COL_3 = "STATE";
    public static final String COL_4 = "DATE";


    public static final String TABLE_NAME2 = "sabaq_info";
    public static final String COL_21 = "ID";
    public static final String COL_22 = "NAME";
    public static final String COL_23 = "PARHAWNAR";
    public static final String COL_24 = "COUNT";
    public static final String COL_25 = "ADMIN";
    public static final String COL_26 = "Monitor";
    public static final String COL_27 = "Participants";


    public static final String TABLE_NAME3 = "chat";
    public static final String COL_36 = "NAME";
    public static final String COL_31 = "SABAQID";
    public static final String COL_32 = "MSG";
    public static final String COL_33 = "DATE";
    public static final String COL_34 = "TIME";
    public static final String COL_35 = "STATUS";




    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME +" (NAME TEXT,DAY TEXT,STATE TEXT,DATE TEXT)");
        db.execSQL("create table " + TABLE_NAME2 +" (ID INTEGER,NAME TEXT,PARHAWNAR TEXT,COUNT INT,ADMIN INT,Monitor TEXT,Participants TEXT)");
        db.execSQL("create table " + TABLE_NAME3 +" (SABAQID INTEGER,MSG TEXT,DATE TEXT,TIME REAL,STATUS INTEGER,NAME TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME2);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME3);
        onCreate(db);
    }

    public boolean insertData(String id,String name,String surname,String marks) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1,id);
        contentValues.put(COL_2,name);
        contentValues.put(COL_3,surname);
        contentValues.put(COL_4,marks);
        long result = db.insert(TABLE_NAME,null ,contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }

    public Cursor getSabaq() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select DISTINCT NAME from "+TABLE_NAME,null);
        return res;
    }

    public Cursor getAllData(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME+" WHERE NAME = ?",new String[]{name});
        return res;
    }


    public void deleteData (String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, "NAME = ?",new String[] {name});
    }



    public void deleteAllData () {
        SQLiteDatabase db = this.getWritableDatabase();
         db.delete(TABLE_NAME, null,null);
    }


    //Table 2

    public boolean insertData2(String id,String name,String parhawnar,String date,String time,String msg,int status,String sendersname,String count,int admin,String monitor,String participants) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_21,id);
        contentValues.put(COL_22,name);
        contentValues.put(COL_23,parhawnar);
        contentValues.put(COL_24,count);
        contentValues.put(COL_25,admin);
        contentValues.put(COL_26,monitor);
        contentValues.put(COL_27,participants);
        ContentValues contentValues1 = new ContentValues();
        contentValues1.put(COL_31,id);
        contentValues1.put(COL_32,msg);
        contentValues1.put(COL_33,date);
        contentValues1.put(COL_34,time);
        contentValues1.put(COL_35,status);
        contentValues1.put(COL_36,sendersname);
        long result = db.insert(TABLE_NAME2,null ,contentValues);
        db.insert(TABLE_NAME3,null ,contentValues1);
        if(result == -1)
            return false;
        else
            return true;
    }

    public void deleteAllData2 () {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME2, null,null);
    }
    public void deleteAllData3 () {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME3, null,null);
    }

    public void SeenMessage (String statusCheck,String sabaqid) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_35,"2");
        db.update(TABLE_NAME3, contentValues, " STATUS = ? AND SABAQID = ?", new String[]{statusCheck, sabaqid});
    }

    public Cursor getSabaq2() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME2,null);
        return res;
    }
    public Cursor getParhawnar(String sabaqid) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME2+" WHERE ID = ?",new String[]{sabaqid});
        return res;
    }
    public Cursor getLastMessage(String id) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor res = db.rawQuery("select * from "+TABLE_NAME3+" WHERE SABAQID = ? ORDER BY DATE DESC,TIME DESC LIMIT 1",new String[]{id});
        return res;
    }

    public Cursor getAllMessage(String id,String status1,String status2) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor res = db.rawQuery("select * from "+TABLE_NAME3+" WHERE SABAQID = ? AND ( STATUS = ? OR STATUS = ?) ORDER BY DATE,TIME",new String[]{id,status1,status2});
        return res;
    }

    public Cursor getMonitor(String id) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor res = db.rawQuery("select * from "+TABLE_NAME2+" WHERE Monitor = ? ",new String[]{id});
        return res;
    }

    public Cursor getMessage(String id,String status1) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor res = db.rawQuery("select * from "+TABLE_NAME3+" WHERE SABAQID = ? AND STATUS = ? ORDER BY DATE,TIME",new String[]{id,status1});
        return res;
    }



    public boolean insertData3(String sabaqid,String msg,String date,String time,int status,String sendersname) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_31,sabaqid);
        contentValues.put(COL_32,msg);
        contentValues.put(COL_33,date);
        contentValues.put(COL_34,time);
        contentValues.put(COL_35,status);
        contentValues.put(COL_36,sendersname);
        long result = db.insert(TABLE_NAME3,null ,contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }
    public Cursor getMsgCount(String sabaqid,String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME3+" WHERE STATUS = ? AND SABAQID = ?",new String[]{status,sabaqid});
        return res;
    }

    public boolean refreshData(String id,String name,String parhawnar,String date,String time,String msg,int status,String sendersname,String count,int admin,String monitor,String participants) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_21,id);
        contentValues.put(COL_22,name);
        contentValues.put(COL_23,parhawnar);
        contentValues.put(COL_24,count);
        contentValues.put(COL_25,admin);
        contentValues.put(COL_26,monitor);
        contentValues.put(COL_27,participants);
        Cursor res = db.rawQuery("select * from "+TABLE_NAME3+" WHERE SABAQID = ?",new String[]{id});
        if (res.getCount()==0)
        {
            ContentValues contentValues1 = new ContentValues();
            contentValues1.put(COL_31,id);
            contentValues1.put(COL_32,msg);
            contentValues1.put(COL_33,date);
            contentValues1.put(COL_34,time);
            contentValues1.put(COL_35,status);
            contentValues1.put(COL_36,sendersname);

            db.insert(TABLE_NAME3,null ,contentValues1);

        }
        else
        {

        }

        long result = db.insert(TABLE_NAME2,null ,contentValues);

        if(result == -1)
            return false;
        else
            return true;
    }
    public boolean databaseExist()
    {
        File dbFile = new File("sabaq.db");
        return dbFile.exists();
    }
}