package com.example.administrator.testsliding.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by jianghao on 16/1/25.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME="sendFileDatabase.db";//数据库名称
    private static final String TABLE_NAME="localFile";//表名称
    private static final String TABLE_NAME_IQ="iqFile";//表名称
    private static final int  version=1;
    private static DatabaseHelper mInstance;


    public static final String sql = "create table ["+TABLE_NAME+"](_id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "fileName VARCHAR UNIQUE, " +
            "start INT ," +
            "end INT ," +
            "isChanged SMALLINT ," +
            "upload SMALLINT," +
            "times SMALLINT);";
    public static final  String iqFile = "create table ["+TABLE_NAME_IQ+"](_id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "fileName VARCHAR," +
            "upload SMALLINT," +
            "_times SMALLINT);";

    private DatabaseHelper(Context context) {
        super(context, DB_NAME, null, version);
    }

    public synchronized static DatabaseHelper getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new DatabaseHelper(context);
        }
        return mInstance;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sql);
        db.execSQL(iqFile);
       //this.deleteDatabase("sendFileDatabase");删除数据库
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (oldVersion) {
            case 1:
                //db.execSQL("alter table iqFile add column _times SMALLINT");
                break;
            case 2:
                break;
            case 3:
                //db.execSQL("alter table localFile add column times SMALLINT");
                break;
            default:
        }
    }
}

