package com.demo.awesomeledger.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class ItemHelper extends SQLiteOpenHelper {

    public static final String name = "user_db";
    private static final Integer version = 1;

    public ItemHelper(@Nullable Context context) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(@NonNull SQLiteDatabase db) {
        final String sql = "CREATE TABLE IF NOT EXISTS ledger(" +
                "id integer primary key autoincrement," +
                "date datetime not null," +
                "type varchar(32) not null," +
                "kind varchar(32) not null," +
                "money double not null," +
                "comment varchar(256) not null)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { }
}
