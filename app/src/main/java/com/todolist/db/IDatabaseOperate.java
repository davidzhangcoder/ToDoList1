package com.todolist.db;

import android.content.ContentValues;

import java.util.List;
import java.util.Map;

public interface IDatabaseOperate {

    long addContent(String tableName, ContentValues values);

    boolean deleteContent(String tableName, String whereClause, String[] whereArgs);

    boolean updateContent(String tableName, ContentValues values, String whereClause, String[] whereArgs);

    //查询数据库的时候返回的单条记录
    Map<String, String> getRowData(String tableName, String selection, String[] selectionArgs);

    //查询数据库的时候返回的多条记录
    List<Map<String, String>> getListData(String tableName, String selection, String[] selectionArgs, String orderBy);

}