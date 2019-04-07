package com.todolist.model;

public interface ITypeFactory {

    int getType(ToDoItemTitle toDoItemTitle);

    int getType(ToDoItem toDoItem);

    int getType(ToDoCategory toDoCategory);

}
