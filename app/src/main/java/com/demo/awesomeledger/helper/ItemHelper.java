package com.demo.awesomeledger.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

public class ItemHelper extends SQLiteOpenHelper {

    public static final String name = "ledger_db";
    private static volatile ItemHelper instance = null;
    private static final Integer version = 1;

    private ItemHelper(@Nullable Context context) {
        super(context, name, null, version);
    }

    public static ItemHelper getInstance(Context context) {
        if (instance == null) {
            synchronized (ItemHelper.class) {
                if (instance == null) {
                    instance = new ItemHelper(context);
                }
            }
        }
        return instance;
    }

    @Override
    public void onCreate(@NonNull SQLiteDatabase db) {
        Log.i("helper", "create table");
        final String sql = "CREATE TABLE IF NOT EXISTS " + name +
                "(id integer primary key autoincrement," +
                "date datetime not null," +
                "type varchar(32) not null," +
                "kind varchar(32) not null," +
                "address varchar(128) not null," +
                "money double not null," +
                "comment varchar(256) not null)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { }
}
