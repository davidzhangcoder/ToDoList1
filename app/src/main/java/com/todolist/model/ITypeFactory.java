package com.todolist.model;

public interface ITypeFactory {

    public int getType(ToDoItemTitle toDoItemTitle);

    public int getType(ToDoItem toDoItem);

}
