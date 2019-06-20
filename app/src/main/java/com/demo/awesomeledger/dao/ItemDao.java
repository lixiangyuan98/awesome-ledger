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
        values.put("uuid", item.getUuid());
        values.put("date", item.getDate().getTime());
        values.put("type", item.getItemType().name());
        values.put("kind", item.getItemKind().name());
        values.put("address", item.getAddress());
        values.put("money", item.getMoney());
        values.put("comment", item.getComment());
        values.put("created_at", item.getCreatedAt().getTime());
        values.put("updated_at", item.getUpdatedAt().getTime());
        if (item.getDeletedAt() != null) {
            values.put("deleted_at", item.getDeletedAt().getTime());
        }
        return values;
    }

    private Item createItemFromCursor(Cursor cursor, boolean deleted) {
        if (!deleted && cursor.getLong(10) != 0) {
            return null;
        }
        Date deletedAt = null;
        if (deleted && cursor.getLong(10) != 0) {
            deletedAt = new Date(cursor.getLong(10));
        }
        return new Item(cursor.getInt(0), cursor.getString(1),
                new Date(cursor.getLong(2)),
                ItemType.valueOf(cursor.getString(3)),
                ItemKind.valueOf(cursor.getString(4)),
                cursor.getString(5),
                cursor.getDouble(6),
                cursor.getString(7),
                new Date(cursor.getLong(8)),
                new Date(cursor.getLong(9)),
                deletedAt);
    }

    // 获取指定id的item, 软删除的项目会被忽略
    @Nullable
    public Item get(long id) {
        SQLiteDatabase db = itemHelper.getReadableDatabase();
        String[] args = new String[1];
        args[0] = String.valueOf(id);
        Cursor cursor = db.query(true, ItemHelper.name, null, "id=?",
                args, null, null, null, null);
        Item item = null;
        if (cursor.moveToFirst()) {
            item = createItemFromCursor(cursor, false);
        }
        cursor.close();
        db.close();
        return item;
    }

    // 获取指定uuid的item, 软删除的item也会被返回
    @Nullable
    public Item get(String uuid) {
        SQLiteDatabase db = itemHelper.getReadableDatabase();
        String[] args = new String[1];
        args[0] = uuid;
        Cursor cursor = db.query(true, ItemHelper.name, null, "uuid=?",
                args, null, null, null, null);
        Item item = null;
        if (cursor.moveToFirst()) {
            item = createItemFromCursor(cursor, true);
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

    // 获得所有item, 包含被软删除的item
    @Nullable
    public List<Item> getItems() {
        return query(true, null, null, null, null, "-date", true);
    }

    @Nullable
    public List<Item> getItems(Calendar month) {
        return query(true, "date>=? AND date<=?", getDateArgs(month), null, null, "-date", false);
    }

    @Nullable
    public List<Item> getItems(Calendar month, ItemType type) {
        return query(true, "date>=? AND date<=? AND type=?", getDateArgs(month, type),
                null, null, "-date", false);
    }

    @Nullable
    public List<Item> getItems(Calendar month, ItemType type, ItemKind kind) {
        return query(true, "date>=? AND date<=? AND type=? AND kind=\"" + kind.name() + "\"",
                getDateArgs(month, type), null, null, null, false);
    }

    @Override
    @Nullable
    public List<Item> query(boolean distinct, String selection, String[] selectionArgs,
                            String groupBy, String having, String orderBy, boolean deleted) {
        SQLiteDatabase db = itemHelper.getReadableDatabase();
        Cursor cursor = db.query(distinct, ItemHelper.name, null, selection, selectionArgs,
                groupBy, having, orderBy, null);
        List<Item> queryResults = null;
        if (cursor.moveToFirst()) {
            queryResults =  new ArrayList<>(cursor.getCount());
            do {
                Item item = createItemFromCursor(cursor, deleted);
                if (item != null) {
                    queryResults.add(item);
                }
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
        item.setUpdatedAt(new Date());
        db.update(ItemHelper.name, getContentValues(item), "id=?", whereArgs);
        db.close();
    }

    // 软删除
    @Override
    public void delete(@NonNull Item item) {
        item.setDeletedAt(new Date());
        update(item);
    }

    // 彻底删除item
    public void delete(@NonNull String uuid) {
        SQLiteDatabase db = itemHelper.getWritableDatabase();
        String[] whereArgs = new String[1];
        whereArgs[0] = uuid;
        db.delete(ItemHelper.name, "uuid=?", whereArgs);
        db.close();
    }
}
