package com.todolist.todomain.fragment.category;

import androidx.annotation.NonNull;

import com.todolist.data.source.ToDoItemDataSource;
import com.todolist.data.source.ToDoItemRepository;
import com.todolist.model.ToDoCategory;

import java.util.List;

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
        toDoItemRepository.loadToDoCategorys(new ToDoItemDataSource.LoadToDoCategorysCallBack() {
            @Override
            public void onToDoCategorysLoaded(List<ToDoCategory> toDoCategorys) {
                view.showToDoCategorys(toDoCategorys);
            }

            @Override
            public void onDataNotAvailable() {

            }
        });
    }

    @Override
    public void saveCategory(ToDoCategory toDocategory) {

    }
}
