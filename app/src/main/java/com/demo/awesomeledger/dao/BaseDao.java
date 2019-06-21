package com.demo.awesomeledger.dao;

import java.util.List;

abstract class BaseDao<T> {

    public abstract List<T> query(boolean distinct, String selection, String[] selectionArgs,
                                  String groupBy, String having, String orderBy, boolean deleted);

    public abstract long insert(T t);

    public abstract void update(T t, boolean fromRemote);

    public abstract void delete(T t);
}
