package com.todolist.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.todolist.app.App;
import com.todolist.R;
import com.todolist.model.ToDoCategory;
import com.todolist.model.ToDoItem;

public class ToDoItemDatabase extends SQLiteOpenHelper {

    private final static String DB_NAME = "todo_list_db";


    public final static int DB_VERSION = 3;



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

        //Version 1
        db.execSQL(ToDoItem.CREATE_TABLE);

        //Version 2
        db.execSQL(ToDoCategory.CREATE_TABLE);
        initCategory( db );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        // Drop older table if existed
//        db.execSQL("DROP TABLE IF EXISTS " + ToDoItem.TABLE_NAME);
//
//        db.execSQL("DROP TABLE IF EXISTS " + ToDoCategory.TABLE_NAME);
//
//        // Create tables again
//        onCreate(db);

        switch( oldVersion )
        {
            case 1:
                db.execSQL(ToDoCategory.CREATE_TABLE);
                initCategory( db );
                break;
            case 2:
                String addCategoryIDInToDoITemSQL = "ALTER TABLE " + ToDoItem.TABLE_NAME + " ADD COLUMN " + ToDoItem.COLUMN_CATEGORY + " INTEGER ";
                db.execSQL( addCategoryIDInToDoITemSQL );
                break;
            default: {
                try {
                    db.beginTransaction();
                    // Drop older table if existed
                    db.execSQL("DROP TABLE IF EXISTS " + ToDoItem.TABLE_NAME);

                    db.execSQL("DROP TABLE IF EXISTS " + ToDoCategory.TABLE_NAME);

                    // Create tables again
                    onCreate(db);
                    db.setTransactionSuccessful();
                }
                finally
                {
                    db.endTransaction();
                }
            }
        }
    }

    private void initCategory(SQLiteDatabase db)
    {
        //Inital category
//        db.execSQL("insert into " + ToDoCategory.TABLE_NAME + "( " + ToDoCategory.COLUMN_ID + "," + ToDoCategory.COLUMN_NAME
//                + ") Values( null ,'" + App.getContext().getString(R.string.category_all) + "')");

//        db.execSQL("insert into " + ToDoCategory.TABLE_NAME + "( " + ToDoCategory.COLUMN_ID + "," + ToDoCategory.COLUMN_NAME
//                + ") Values( " + ToDoCategory.CATEGORY_DEFAULT_ID + " ,'" + App.getContext().getString(R.string.category_default) + "')");

        db.execSQL("insert into " + ToDoCategory.TABLE_NAME + "( " + ToDoCategory.COLUMN_ID + "," + ToDoCategory.COLUMN_NAME
                + ") Values( null ,'" + App.getContext().getString(R.string.category_working) + "')");

        db.execSQL("insert into " + ToDoCategory.TABLE_NAME + "( " + ToDoCategory.COLUMN_ID + "," + ToDoCategory.COLUMN_NAME
                + ") Values( null ,'" + App.getContext().getString(R.string.category_learning) + "')");

        db.execSQL("insert into " + ToDoCategory.TABLE_NAME + "( " + ToDoCategory.COLUMN_ID + "," + ToDoCategory.COLUMN_NAME
                + ") Values( null ,'" + App.getContext().getString(R.string.category_meeting) + "')");

        db.execSQL("insert into " + ToDoCategory.TABLE_NAME + "( " + ToDoCategory.COLUMN_ID + "," + ToDoCategory.COLUMN_NAME
                + ") Values( null ,'" + App.getContext().getString(R.string.category_appointment) + "')");

        db.execSQL("insert into " + ToDoCategory.TABLE_NAME + "( " + ToDoCategory.COLUMN_ID + "," + ToDoCategory.COLUMN_NAME
                + ") Values( null ,'" + App.getContext().getString(R.string.category_shopping) + "')");

        db.execSQL("insert into " + ToDoCategory.TABLE_NAME + "( " + ToDoCategory.COLUMN_ID + "," + ToDoCategory.COLUMN_NAME
                + ") Values( null ,'" + App.getContext().getString(R.string.category_other) + "')");
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