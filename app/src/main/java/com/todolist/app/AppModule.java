package com.todolist.app;

import com.todolist.data.Injection;
import com.todolist.data.source.ToDoItemRepository;
import com.todolist.todomain.TestA;
import com.todolist.todomain.fragment.done.DoneFragment;
import com.todolist.todomain.fragment.todo.ToDoFragment;
import com.todolist.todomain.fragment.todo.ToDoFragmentComponent;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module//(subcomponents = {ToDoFragmentComponent.class} )
public class AppModule {

    @Provides
    @Singleton
    TestA provideTestA(){
        return new TestA();
    }

    @Provides
    @Singleton
    ToDoItemRepository provideToDoItemRepository() {
        return Injection.provideToDoItemRepository();
    }

    @Provides
    @Singleton
    ToDoFragment provideToDoFragment(){
        return ToDoFragment.newInstance();
    }

    @Provides
    @Singleton
    DoneFragment provideDoneFragment(){
        return DoneFragment.newInstance();
    }

}
