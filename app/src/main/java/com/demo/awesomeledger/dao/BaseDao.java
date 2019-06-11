package com.demo.awesomeledger.dao;

import java.util.List;

public abstract class BaseDao<T> {

    public abstract List<T> query(boolean distinct, String selection, String[] selectionArgs,
                                  String groupBy, String having, String orderBy);

    public abstract long insert(T t);

    public abstract void update(T t);

    public abstract void delete(T t);
}
