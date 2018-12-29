package com.todolist.db;

import android.content.ContentValues;

import java.util.List;
import java.util.Map;

public interface IDatabaseOperate {

    public long addContent(String tableName, ContentValues values);

    public boolean deleteContent(String tableName, String whereClause, String[] whereArgs);

    public boolean updateContent(String tableName, ContentValues values, String whereClause, String[] whereArgs);

    //查询数据库的时候返回的单条记录
    public Map<String, String> getRowData(String tableName, String selection, String[] selectionArgs);

    //查询数据库的时候返回的多条记录
    public List<Map<String, String>> getListData(String tableName, String selection, String[] selectionArgs, String orderBy);

}