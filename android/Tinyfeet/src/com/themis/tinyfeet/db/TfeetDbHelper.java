package com.themis.tinyfeet.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.themis.tinyfeet.db.model.TfeetColumn;

/**
 * Created with IntelliJ IDEA.
 * User: sunharuka
 * Date: 13-4-9
 * Time: 下午8:35
 * To change this template use File | Settings | File Templates.
 */
public class TfeetDbHelper extends SQLiteOpenHelper {
    static final String TAG = "TfeetDbHelper";
    static final String DB_NAME = "tfeet.db";
    static final int DB_VERSION = 1;
    static final String TABLE_NAME ="tfeets";
    private Context context;

    public TfeetDbHelper(Context context) {
        super(context,DB_NAME,null,DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table "+ TABLE_NAME +" ("+ TfeetColumn.ID+" int primary key, "+TfeetColumn.UID+" varchar,"+TfeetColumn.PIC_URL+" varchar,"+TfeetColumn.TEXT+" varchar,"+TfeetColumn.SEASON+" int,"+TfeetColumn.DAYNIGHT+" int,"+TfeetColumn.LIKE_COUNT+" int,"+TfeetColumn.COMMENT_COUNT+" int,"+TfeetColumn.CREATED_AT+" datatime,"+TfeetColumn.LATITUDE+" numeric,"+TfeetColumn.LONGITUDE+" numeric)";
        db.execSQL(sql);
        Log.d(TAG,"onCreate SQL:"+sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("drop table if exists "+ TABLE_NAME);
        Log.d(TAG, TABLE_NAME +" is droped");
        onCreate(sqLiteDatabase);
    }

    public void insert(ContentValues values) {
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_NAME, null, values);
        db.close();
    }
}
