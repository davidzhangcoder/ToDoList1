package com.todolist.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.todolist.model.ToDoItem;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by ravi on 15/03/18.
 */

public class DatabaseHelper extends SQLiteOpenHelper {


    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "todo_list_db";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        // create notes table
        db.execSQL(ToDoItem.CREATE_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + ToDoItem.TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

    public long insertToDoItem(String name) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(ToDoItem.COLUMN_NAME, name);

        // insert row
        long id = db.insert(ToDoItem.TABLE_NAME, null, values);

        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }

    public ToDoItem getToDoItem(long id) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(ToDoItem.TABLE_NAME,
                new String[]{ToDoItem.COLUMN_ID, ToDoItem.COLUMN_NAME, ToDoItem.COLUMN_DUE_TIMESTAMP},
                ToDoItem.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        // prepare ToDoItem object
        ToDoItem toDoItem = new ToDoItem();
        toDoItem.setId( cursor.getInt(cursor.getColumnIndex(ToDoItem.COLUMN_ID)) );
        toDoItem.setName( cursor.getString(cursor.getColumnIndex(ToDoItem.COLUMN_NAME)) );
//                cursor.getString(cursor.getColumnIndex(ToDoItem.COLUMN_DUE_TIMESTAMP)));

        // close the db connection
        cursor.close();

        return null;
    }

    public List<ToDoItem> getAllToDoItems() {
        List<ToDoItem> toDoItems = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + ToDoItem.TABLE_NAME + " ORDER BY " +
                ToDoItem.COLUMN_ID + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ToDoItem toDoItem = new ToDoItem();
                toDoItem.setId(cursor.getInt(cursor.getColumnIndex(ToDoItem.COLUMN_ID)));
                toDoItem.setName(cursor.getString(cursor.getColumnIndex(ToDoItem.COLUMN_NAME)));
//                note.setTimestamp(cursor.getString(cursor.getColumnIndex(Note.COLUMN_TIMESTAMP)));

                toDoItems.add(toDoItem);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return toDoItems;
    }

    public int getToDoItemsCount() {
        String countQuery = "SELECT  * FROM " + ToDoItem.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();


        // return count
        return count;
    }

    public int updateToDoItem(ToDoItem toDoItem) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ToDoItem.COLUMN_NAME, toDoItem.getName());
        values.put(ToDoItem.COLUMN_DUE_TIMESTAMP, toDoItem.getDueTimestamp());
        values.put(ToDoItem.COLUMN_REMIND_TIMESTAMP, toDoItem.getRemindTimestamp());

        // updating row
        return db.update(ToDoItem.TABLE_NAME, values, ToDoItem.COLUMN_ID + " = ?",
                new String[]{String.valueOf(toDoItem.getId())});
    }

    public void deleteToDoItem(ToDoItem toDoItem) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(ToDoItem.TABLE_NAME, ToDoItem.COLUMN_ID + " = ?",
                new String[]{String.valueOf(toDoItem.getId())});
        db.close();
    }
}