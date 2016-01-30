package com.example.administrator.testsliding.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by jianghao on 16/1/25.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME="sendFileDatabase";//数据库名称
    private static final String TABLE_NAME="localFile";//表名称
    private static final int  version=1;
    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table ["+TABLE_NAME+"](_id INTEGER PRIMARY KEY AUTOINCREMENT,fileName VARCHAR, start INT ,end INT ,isChanged SMALLINT ,upload SMALLINT);";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
