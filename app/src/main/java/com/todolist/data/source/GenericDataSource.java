package com.todolist.data.source;

import com.todolist.data.model.ToDoCategory;
import com.todolist.data.model.ToDoItem;

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
