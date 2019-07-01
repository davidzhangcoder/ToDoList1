package com.todolist.app;

import com.todolist.data.source.ToDoItemRepository;
import com.todolist.todomain.TestA;
import com.todolist.todomain.fragment.done.DoneFragment;
import com.todolist.todomain.fragment.todo.ToDoFragment;
import com.todolist.todomain.fragment.todo.ToDoFragmentComponent;

import javax.inject.Singleton;

import dagger.Component;
import dagger.Provides;

@Component(modules = {AppModule.class})
@Singleton
public interface AppComponent {

    TestA getTestA();

    ToDoItemRepository provideToDoItemRepository();

    ToDoFragment provideToDoFragment();

    DoneFragment provideDoneFragment();

}
