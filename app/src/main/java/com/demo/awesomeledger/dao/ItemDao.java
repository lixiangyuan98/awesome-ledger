package com.demo.awesomeledger.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import com.demo.awesomeledger.bean.Item;
import com.demo.awesomeledger.helper.ItemHelper;
import com.demo.awesomeledger.util.ItemKind;
import com.demo.awesomeledger.util.ItemType;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ItemDao extends BaseDao<Item> {

    private static ItemHelper itemHelper;
    private static volatile ItemDao instance = null;

    private ItemDao(Context context) {
        itemHelper = ItemHelper.getInstance(context);
        SQLiteDatabase db = itemHelper.getWritableDatabase();
        db.close();
    }

    public static ItemDao getInstance(Context context) {
        if (instance == null) {
            synchronized (ItemDao.class) {
                if (instance == null) {
                    instance = new ItemDao(context);
                }
            }
        }
        return instance;
    }

    private ContentValues getContentValues(@NonNull Item item) {
        ContentValues values = new ContentValues();
        values.put("date", item.getDate().getTime());
        values.put("type", item.getItemType().name());
        values.put("kind", item.getItemKind().name());
        values.put("address", item.getAddress());
        values.put("money", item.getMoney());
        values.put("comment", item.getComment());
        return values;
    }

    private Item createItemFromCursor(Cursor cursor) {
        return new Item(cursor.getInt(0), new Date(cursor.getLong(1)),
                ItemType.valueOf(cursor.getString(2)),
                ItemKind.valueOf(cursor.getString(3)),
                cursor.getString(4),
                cursor.getDouble(5),
                cursor.getString(6));
    }

    @Override
    @Nullable
    public Item get(long id) {
        SQLiteDatabase db = itemHelper.getReadableDatabase();
        String[] args = new String[1];
        args[0] = String.valueOf(id);
        Cursor cursor = db.query(true, ItemHelper.name, null, "id=?",
                args, null, null, null, null);
        Item item;
        if (cursor.moveToFirst()) {
            item = createItemFromCursor(cursor);
        } else {
            item = null;
        }
        cursor.close();
        db.close();
        return item;
    }

    @Override
    public List<Item> getAll() {
        return query(true, null, null, null, null, "-date");
    }

    @Override
    @Nullable
    public List<Item> query(boolean distinct, String selection, String[] selectionArgs,
                            String groupBy, String having, String orderBy) {
        SQLiteDatabase db = itemHelper.getReadableDatabase();
        Cursor cursor = db.query(distinct, ItemHelper.name, null, selection, selectionArgs,
                groupBy, having, orderBy, null);
        List<Item> queryResults = null;
        if (cursor.moveToFirst()) {
            queryResults = new ArrayList<>(cursor.getCount());
            while (cursor.moveToNext()) {
                Item item = createItemFromCursor(cursor);
                queryResults.add(item);
            }
        }
        cursor.close();
        db.close();
        return queryResults;
    }

    @Override
    public long insert(@NonNull Item item) {
        SQLiteDatabase db = itemHelper.getWritableDatabase();
        long id = db.insert(ItemHelper.name, null, getContentValues(item));
        db.close();
        return id;
    }

    @Override
    public void update(@NonNull Item item) {
        SQLiteDatabase db = itemHelper.getWritableDatabase();
        String[] whereArgs = new String[1];
        whereArgs[0] = String.valueOf(item.getId());
        db.update(ItemHelper.name, getContentValues(item), "id=?", whereArgs);
        db.close();
    }

    @Override
    public void delete(@NonNull Item item) {
        SQLiteDatabase db = itemHelper.getWritableDatabase();
        String[] whereArgs = new String[1];
        whereArgs[0] = String.valueOf(item.getId());
        db.delete(ItemHelper.name, "id=?", whereArgs);
        db.close();
    }
}
