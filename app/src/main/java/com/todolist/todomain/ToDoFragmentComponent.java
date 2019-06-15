package com.todolist.todomain;

import com.todolist.app.AppComponent;
import com.todolist.app.AppModule;
import com.todolist.di.FragmentScoped;

import javax.inject.Singleton;

import dagger.Component;
import dagger.Subcomponent;

@FragmentScoped
@Subcomponent(modules = {ToDoMainModule.class} )
public interface ToDoFragmentComponent {

    void inject(ToDoFragment toDoFragment);

    @Subcomponent.Builder
    interface Builder { // SubComponent 必须显式地声明 Subcomponent.Builder，parent Component 需要用 Builder 来创建 SubComponent
        ToDoFragmentComponent build();
    }

}
