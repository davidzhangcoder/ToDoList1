package com.todolist.db;

public class AdsDao //implements IDatabaseOperate
{

//    private ToDoItemDatabase dbHelper=null;
//
//    public AdsDao(Context context) {
//        dbHelper= ToDoItemDatabase.getInstance(context);
//    }
//
//    @Override
//    public synchronized long addContent(String tableName, ContentValues values) {
//        boolean flag = false;
//        SQLiteDatabase database = null;
//        long id = -1;
//        try {
//            database = dbHelper.getWritableDatabase();
//            id = database.insert(tableName, null, values);
//            flag = (id != -1);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } finally {
//            if (database != null) {
//                database.close();
//            }
//        }
//        return id;
//    }
//
//    @Override
//    public synchronized boolean deleteContent(String tableName, String whereClause, String[] whereArgs) {
//        boolean flag = false;
//        SQLiteDatabase database = null;
//        int count = 0;
//        try {
//            database = dbHelper.getWritableDatabase();
//            count = database.delete(tableName, whereClause, whereArgs);
//            flag = (count > 0);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } finally {
//            if (database != null) {
//                database.close();
//            }
//        }
//        return flag;
//    }
//
//    @Override
//    public synchronized boolean updateContent(String tableName, ContentValues values, String whereClause,
//                                              String[] whereArgs) {
//        boolean flag = false;
//        SQLiteDatabase database = null;
//        int count = 0;
//        try {
//            database = dbHelper.getWritableDatabase();
//            count = database.update(tableName, values, whereClause, whereArgs);
//            flag = (count > 0);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } finally {
//            if (database != null) {
//                database.close();
//            }
//        }
//        return flag;
//    }
//
//    @Override
//    public synchronized Map<String, String> getRowData(String tableName, String selection,
//                                                       String[] selectionArgs) {
//        SQLiteDatabase database = null;
//        Cursor cursor = null;
//        Map<String, String> map = new HashMap<String, String>();
//
//        try {
//            database = dbHelper.getReadableDatabase();
//            cursor = database.query(true, tableName, null, selection, selectionArgs, null,
//                    null, null, null); //查询单条记录，记录是唯一的，所以第一个参数置为 true.
//            int cols_len = cursor.getColumnCount();
//            while (cursor.moveToNext()) {
//                for (int i = 0; i < cols_len; i++) {
//                    String cols_name = cursor.getColumnName(i);
//                    String cols_values = cursor.getString(cursor.getColumnIndex(cols_name));
//                    if (cols_values == null) {
//                        cols_values = "";
//                    }
//                    map.put(cols_name, cols_values);
//                }
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } finally {
//            if (database != null) {
//                database.close();
//            }
//        }
//        return map;
//    }
//
//    @Override
//    public synchronized List<Map<String, String>> getListData(String tableName, String selection,
//                                                              String[] selectionArgs , String orderBy ) {
//        SQLiteDatabase database = null;
//        Cursor cursor = null;
//        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
//        try {
//            database = dbHelper.getReadableDatabase();
//            cursor = database.query(false, tableName, null, selection, selectionArgs, null,
//                    null, null, null); //查询所有记录，所以有重复的数据也要全部检出，所以第一参数置为false.
//            int cols_len = cursor.getColumnCount();
//            while (cursor.moveToNext()) {
//                Map<String, String> map = new HashMap<String, String>();
//                for (int i = 0; i < cols_len; i++) {
//                    String cols_name = cursor.getColumnName(i);
//                    String cols_values = cursor.getString(cursor.getColumnIndex(cols_name));
//                    if (cols_values == null) {
//                        cols_values = "";
//                    }
//                    map.put(cols_name, cols_values);
//                }
//                list.add(map);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } finally {
//            if (database != null) {
//                database.close();
//            }
//        }
//        return list;
//    }

}