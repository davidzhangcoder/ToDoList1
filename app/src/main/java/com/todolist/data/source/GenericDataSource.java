package com.todolist.data.source;

import com.todolist.model.ToDoCategory;
import com.todolist.model.ToDoItem;

public interface GenericDataSource {

    interface GenericToDoCallBack {
        void onCompleted(ToDoItem toDo);

        void onError();
    }

    interface GenericToDoCategoryCallBack {
        void onCompleted(ToDoCategory toDoCategory);

        void onError();
    }

}
