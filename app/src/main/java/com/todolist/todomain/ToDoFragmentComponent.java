package com.todolist.todomain;

import com.todolist.app.AppComponent;
import com.todolist.app.AppModule;
import com.todolist.di.FragmentScoped;

import javax.inject.Singleton;

import dagger.Component;

@FragmentScoped
@Component(modules = {ToDoMainModule.class} , dependencies = AppComponent.class)
public interface ToDoFragmentComponent {

    void inject(ToDoFragment toDoFragment);

}
