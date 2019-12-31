package com.todolist.model;

import android.net.Uri;

public class ToDoImage {

    public static final String TABLE_NAME = "todo_image";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_URL = "url";
    public static final String COLUMN_TODOITEM = "todoitem_id";


    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COLUMN_URL + " TEXT ,"
                    + COLUMN_TODOITEM + " INTEGER ,"

                    + " FOREIGN KEY ( " + COLUMN_TODOITEM + ") REFERENCES " + ToDoItem.TABLE_NAME + " ( " + ToDoItem.COLUMN_ID + " ) "
                    + ")";


    private boolean isAdd;

    private Uri uri;


    public boolean isAdd() {
        return isAdd;
    }

    public void setAdd(boolean add) {
        isAdd = add;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

}
