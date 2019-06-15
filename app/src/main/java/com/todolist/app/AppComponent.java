package com.todolist.app;

import com.todolist.todomain.TestA;
import com.todolist.todomain.ToDoFragmentComponent;

import javax.inject.Singleton;

import dagger.Component;

@Component(modules = {AppModule.class})
@Singleton
public interface AppComponent {

    TestA getTestA();

    ToDoFragmentComponent.Builder toDoFragmentComponent();

}
