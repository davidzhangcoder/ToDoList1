package com.todolist.data.source;

import android.support.annotation.NonNull;

import com.todolist.model.ToDoItem;

import java.util.List;

public interface ToDoItemDataSource extends GenericDataSource{

    interface LoadToDoItemsCallBack
    {
        void onToDoItemsLoaded(List<ToDoItem> toDos);

        void onDataNotAvailable();
    }

    public void loadToDoItems(@NonNull long categoryID , @NonNull LoadToDoItemsCallBack callBack );

    public void getToDoItem( @NonNull long toDoItemID , @NonNull GenericToDoCallBack callBack );

    public void saveToDo(@NonNull ToDoItem toDoItem , @NonNull GenericToDoCallBack callBack);

    public void updateToDo(@NonNull ToDoItem toDoItem , @NonNull GenericToDoCallBack callBack);

    public void completeToDo(@NonNull ToDoItem toDoItem , @NonNull GenericToDoCallBack callBack);

    public void reverseCompletedToDo(@NonNull ToDoItem toDoItem , @NonNull GenericToDoCallBack callBack);


    public void loadDoneItems(LoadToDoItemsCallBack callBack);

}