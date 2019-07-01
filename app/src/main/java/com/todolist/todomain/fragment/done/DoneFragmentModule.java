package com.todolist.todomain.fragment.done;

import com.todolist.data.source.ToDoItemRepository;
import com.todolist.di.FragmentScoped;

import dagger.Module;
import dagger.Provides;

@Module
public class DoneFragmentModule {

    DoneFragmentContract.View view;

    public DoneFragmentModule(DoneFragmentContract.View view) {
        this.view = view;
    }

    @Provides
    @FragmentScoped
    DoneFragmentContract.Presenter provideDoneFragmentPresenter(ToDoItemRepository toDoItemRepository){
        return new DoneFragmentPresenter(toDoItemRepository,view);
    }

}
