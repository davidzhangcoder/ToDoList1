package com.todolist.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class ToDoImage implements Parcelable {

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
//    private String uriString;

    private long id;

    private long toDoItemId;


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

//    public String getUriString() {
//        return uriString;
//    }
//
//    public void setUriString(String uriString) {
//        this.uriString = uriString;
//    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getToDoItemId() {
        return toDoItemId;
    }

    public void setToDoItemId(long toDoItemId) {
        this.toDoItemId = toDoItemId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.isAdd ? (byte) 1 : (byte) 0);
        dest.writeParcelable(this.uri, flags);
        dest.writeLong(this.id);
        dest.writeLong(this.toDoItemId);
    }

    public ToDoImage() {
    }

    protected ToDoImage(Parcel in) {
        this.isAdd = in.readByte() != 0;
        this.uri = in.readParcelable(Uri.class.getClassLoader());
        this.id = in.readLong();
        this.toDoItemId = in.readLong();
    }

    public static final Parcelable.Creator<ToDoImage> CREATOR = new Parcelable.Creator<ToDoImage>() {
        @Override
        public ToDoImage createFromParcel(Parcel source) {
            return new ToDoImage(source);
        }

        @Override
        public ToDoImage[] newArray(int size) {
            return new ToDoImage[size];
        }
    };
}
