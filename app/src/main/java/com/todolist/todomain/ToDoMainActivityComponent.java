package com.todolist.todomain;

import com.todolist.app.AppComponent;
import com.todolist.di.ActivityScoped;
import com.todolist.di.FragmentScoped;
import com.todolist.todomain.fragment.todo.ToDoFragmentComponent;
import com.todolist.todomain.fragment.todo.ToDoFragmentModule;

import dagger.Component;

@Component(modules = {ToDoMainActivityModule.class} , dependencies = AppComponent.class )
@ActivityScoped
public interface ToDoMainActivityComponent {

    void inject(ToDoMainActivity toDoMainActivity);

    ToDoFragmentComponent.Builder toDoFragmentComponent();

}
