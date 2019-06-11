package com.todolist.todomain;

import com.todolist.di.ActivityScoped;
import com.todolist.di.FragmentScoped;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ToDoMainModule {

    @Provides
    @FragmentScoped
    ToDoFragment provideToDoFragment(){
        return new ToDoFragment();
    }

    @Provides
    @FragmentScoped
    DoneFragment provideDoneFragment(){
        return new DoneFragment();
    }

//    @Provides
//    @Singleton
//    TestA provideTestA(){
//        return new TestA();
//    }

//    @Provides
//    @ActivityScoped
//    abstract ToDoMainPresenter provideToDoMainPresenter();

}
