package com.todolist.data.source;

import com.todolist.model.ToDoItem;

public interface GenericDataSource {

    interface GenericToDoCallBack {
        void onCompleted(ToDoItem toDo);

        void onError();
    }

}
