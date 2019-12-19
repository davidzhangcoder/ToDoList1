package com.todolist.todomain.fragment.todo;

import com.todolist.di.ActivityScoped;
import com.todolist.di.FragmentScoped;

import dagger.Subcomponent;

@FragmentScoped
@Subcomponent(modules = {ToDoFragmentModule.class} )
//inherit 模式
public interface ToDoFragmentComponent {

    void inject(ToDoFragment toDoFragment);

    @Subcomponent.Builder
    interface Builder { // SubComponent 必须显式地声明 Subcomponent.Builder，parent Component 需要用 Builder 来创建 SubComponent
        ToDoFragmentComponent build();

        //因为ToDoFragmentModule的构建器有参数，所以需要setter方法
        Builder setToDoFragmentModule(ToDoFragmentModule toDoFragmentModule);
    }

}
