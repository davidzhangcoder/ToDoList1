package com.todolist.todomain.fragment.todo;

import com.todolist.data.source.ToDoItemRepository;
import com.todolist.di.FragmentScoped;

import dagger.Module;
import dagger.Provides;

@Module
public class ToDoFragmentModule {

    ToDoFragmentContract.View view;

    public ToDoFragmentModule( ToDoFragmentContract.View view ) {
        this.view = view;
    }

//    @Provides
//    @FragmentScoped
//    ToDoFragment provideToDoFragment(){
//        return new ToDoFragment();
//    }
//
//    @Provides
//    @FragmentScoped
//    DoneFragment provideDoneFragment(){
//        return new DoneFragment();
//    }

//    @Provides
//    @Singleton
//    TestA provideTestA(){
//        return new TestA();
//    }

    @Provides
    @FragmentScoped
    ToDoFragmentContract.Presenter provideToDoFragmentPresenter(ToDoItemRepository toDoItemRepository) {
        return new ToDoFragmentPresenter(toDoItemRepository,view);
    }

}
