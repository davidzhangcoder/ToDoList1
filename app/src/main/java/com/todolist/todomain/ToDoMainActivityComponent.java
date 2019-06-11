package com.todolist.todomain;

import com.todolist.di.FragmentScoped;

import dagger.Component;

@Component(modules = {ToDoMainModule.class})
@FragmentScoped
public interface ToDoMainActivityComponent {

    void inject(ToDoMainActivity toDoMainActivity);

}
