package com.todolist.todomain;

import android.support.annotation.NonNull;

import com.todolist.data.source.ToDoItemRepository;

public class CategoryPresenter implements CategoryContract.Presenter {

    private ToDoItemRepository toDoItemRepository;
    private CategoryContract.View view;

    public CategoryPresenter(@NonNull ToDoItemRepository toDoItemRepository , @NonNull CategoryContract.View view) {
        this.toDoItemRepository = toDoItemRepository;
        this.view = view;
        view.setPresenter( this );
    }

    @Override
    public void start() {

    }
}
