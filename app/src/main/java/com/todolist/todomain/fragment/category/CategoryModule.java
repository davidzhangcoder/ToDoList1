package com.todolist.todomain.fragment.category;

import com.todolist.data.source.ToDoItemRepository;
import com.todolist.di.FragmentScoped;

import dagger.Module;
import dagger.Provides;

@Module
public class CategoryModule {

    CategoryContract.View view;

    public CategoryModule(CategoryContract.View view) {
        this.view = view;
    }

    @Provides
    @FragmentScoped
    CategoryContract.Presenter provideCategoryPresenter(ToDoItemRepository toDoItemRepository) {
        return new CategoryPresenter(toDoItemRepository,view);
    }

}
