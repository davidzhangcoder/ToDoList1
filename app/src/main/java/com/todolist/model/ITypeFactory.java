package com.todolist.model;

import com.todolist.data.model.ToDoCategory;
import com.todolist.data.model.ToDoItem;

public interface ITypeFactory {

    int getType(ToDoItemTitle toDoItemTitle);

    int getType(ToDoItem toDoItem);

    int getType(ToDoCategory toDoCategory);

}
