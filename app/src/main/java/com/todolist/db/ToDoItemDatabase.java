package com.todolist.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.todolist.model.ToDoItem;

public class ToDoItemDatabase extends SQLiteOpenHelper {

    private final static String DB_NAME = "todo_list_db";


    public final static int DB_VERSION = 1;



    private static ToDoItemDatabase mInstance=null;
    private ToDoItemDatabase(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public static synchronized ToDoItemDatabase getInstance(Context context) {

        if (mInstance == null) {
            mInstance = new ToDoItemDatabase(context);
        }
        return mInstance;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        // create notes table
        db.execSQL(ToDoItem.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + ToDoItem.TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

}

//使用案例：
//        dao=new AdsDao(AppContext);
//private void excuteDbUpdateTask(ScheduleModel task,String sid){
//        ContentValues values=new ContentValues();
//        values.put(ToDoItemDatabase.TASK_NAME, task.getPlayerName());
//        values.put(ToDoItemDatabase.TASK_TYPE, task.getType());
//        values.put(ToDoItemDatabase.TASK_PUBLISH, task.getPublishTime());
//        values.put(ToDoItemDatabase.TASK_BEGINDATE, task.getBeginDate());
//        boolean flag=dao.updateContent(ToDoItemDatabase.TABLE_PROGRAM, values, ToDoItemDatabase.SID+" =? ", new String[]{sid});
//        LogUtils.debug(tag, "update result="+flag);
//        }
//
//private void excuteDbDeleteTask(String sid){
//        boolean flag=dao.deleteContent(ToDoItemDatabase.TABLE_PROGRAM, ToDoItemDatabase.SID+" =? ", new String[]{sid});
//        LogUtils.debug(tag, "delete result="+flag);
//        }
//
//private void excuteDbAddTask(ScheduleModel task){
//        ContentValues values=new ContentValues();
//        values.put(ToDoItemDatabase.FILE_VER, task.getPublishTime());
//        values.put(ToDoItemDatabase.FILE_SYNC, sfile.getSyncModel());
//        boolean flag=dao.addContent(ToDoItemDatabase.TABLE_SMILFILE, values);
//        LogUtils.debug(tag, "add result="+flag);
//
//        }
//
//        List<Map<String, String>> listTypeData=null;
//        //" id = ? ", new String[] { "2" }
//
//        listTypeData=dao.getListData(ToDoItemDatabase.TABLE_PROGRAM, ToDoItemDatabase.TASK_TYPE+" = ?",
//        new String[] {String.valueOf(Constance.PLAY_TYPE_INSERT)});