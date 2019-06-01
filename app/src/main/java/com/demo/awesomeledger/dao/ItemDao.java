package com.demo.awesomeledger.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import com.demo.awesomeledger.bean.Item;
import com.demo.awesomeledger.helper.ItemHelper;
import com.demo.awesomeledger.util.ItemKind;
import com.demo.awesomeledger.util.ItemType;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ItemDao extends BaseDao<Item> {

    private ItemHelper itemHelper;

    public ItemDao(Context context) {
        itemHelper = new ItemHelper(context);
    }

    private ContentValues getContentValues(@NonNull Item item) {
        ContentValues values = new ContentValues();
        values.put("date", item.getDate().getTime());
        values.put("type", item.getItemType().getType());
        values.put("kind", item.getItemKind().getKind());
        values.put("money", item.getMoney());
        values.put("comment", item.getComment());
        return values;
    }

    @Override
    @NonNull public List<Item> query(boolean distinct, String selection, String[] selectionArgs,
                                     String groupBy, String having, String orderBy) {
        SQLiteDatabase db = itemHelper.getReadableDatabase();
        Cursor cursor = db.query(distinct, ItemHelper.name, null, selection, selectionArgs,
                groupBy, having, orderBy, null);
        List<Item> queryResults = new ArrayList<>(cursor.getCount());
        while (cursor.moveToNext()) {
            Item item = new Item(cursor.getInt(0), new Date(cursor.getLong(1)),
                    Enum.valueOf(ItemType.class, cursor.getString(2)),
                    Enum.valueOf(ItemKind.class, cursor.getString(3)),
                    cursor.getDouble(4),
                    cursor.getString(5));
            queryResults.add(item);
        }
        cursor.close();
        db.close();
        return queryResults;
    }

    @Override
    public void insert(@NonNull Item item) {
        SQLiteDatabase db = itemHelper.getWritableDatabase();
        db.insert(ItemHelper.name, null, getContentValues(item));
        db.close();
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
