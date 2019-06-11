package com.demo.awesomeledger.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.demo.awesomeledger.bean.Item;
import com.demo.awesomeledger.helper.ItemHelper;
import com.demo.awesomeledger.type.ItemKind;
import com.demo.awesomeledger.type.ItemType;

import java.util.ArrayList;
import java.util.Calendar;
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

    private String[] getDateArgs(Calendar month) {
        String[] args = new String[2];
        Calendar calendar = Calendar.getInstance();
        calendar.set(month.get(Calendar.YEAR), month.get(Calendar.MONTH), 1, 0, 0, 0);
        Date startDate = calendar.getTime();
        calendar.set(month.get(Calendar.YEAR), month.get(Calendar.MONTH),
                month.getActualMaximum(Calendar.DATE), 23, 59, 59);
        Date endDate = calendar.getTime();
        args[0] = String.valueOf(startDate.getTime());
        args[1] = String.valueOf(endDate.getTime());
        return args;
    }

    private String[] getDateArgs(Calendar month, ItemType type) {
        String[] args = new String[3];
        String[] res_args = getDateArgs(month);
        args[0] = res_args[0];
        args[1] = res_args[1];
        args[2] = type.name();
        return args;
    }

    public List<Item> getAllItemsOfMonth(Calendar month) {
        return query(true, "date>=? AND date<=?", getDateArgs(month), null, null, "-date");
    }

    public List<Item> getItemsOfMonth(Calendar month, ItemType type) {
        return query(true, "date>=? AND date<=? AND type=?", getDateArgs(month, type),
                null, null, "-date");
    }

    public List<Item> getItemsOfMonthAndKind(Calendar month, ItemType type, ItemKind kind) {
        return query(true, "date>=? AND date<=? AND type=? AND kind=\"" + kind.name() + "\"",
                getDateArgs(month, type), null, null, null);
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
            queryResults =  new ArrayList<>(cursor.getCount());
            do {
                Item item = createItemFromCursor(cursor);
                queryResults.add(item);
            } while (cursor.moveToNext());
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
