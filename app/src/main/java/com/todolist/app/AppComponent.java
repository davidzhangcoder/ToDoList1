package com.todolist.app;

import com.todolist.data.source.ToDoItemRepository;
import com.todolist.todomain.TestA;
import com.todolist.todomain.fragment.done.DoneFragment;
import com.todolist.todomain.fragment.todo.ToDoFragment;

import javax.inject.Singleton;

import dagger.Component;

@Component(modules = {AppModule.class})
@Singleton //Module的scope要和Component一致
public interface AppComponent {

    //依赖模式，依赖模式要显式的暴露，提供给子Component的注入类

    TestA getTestA();

    ToDoItemRepository provideToDoItemRepository();

    ToDoFragment provideToDoFragment();

    DoneFragment provideDoneFragment();

}
