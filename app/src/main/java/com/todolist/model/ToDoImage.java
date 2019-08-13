package com.todolist.model;

import android.net.Uri;

public class ToDoImage {

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
