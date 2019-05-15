package com.todolist.data.source;

import com.todolist.model.ToDoItem;

import java.util.List;

public interface ToDoItemDataSource {

    interface LoadToDoItemsCallBack
    {
        void onToDoItemsLoaded(List<ToDoItem> toDos);

        void onDataNotAvailable();
    }

    interface GetToDoCallBack {
        void onToDoItemLoaded(ToDoItem toDo);

        void onDataNotAvailable();
    }

    public void loadToDoItems( LoadToDoItemsCallBack callBack );

    public void getToDoItem( long toDoItemID , GetToDoCallBack callBack );

    public void saveToDo();

    public void completeToDo();

    public void notcompleteToDo();

}