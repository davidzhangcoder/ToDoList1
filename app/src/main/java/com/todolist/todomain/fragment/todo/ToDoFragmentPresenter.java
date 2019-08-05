package com.todolist.todomain.fragment.todo;

import android.support.annotation.NonNull;

import com.todolist.data.source.GenericDataSource;
import com.todolist.data.source.ToDoItemDataSource;
import com.todolist.data.source.ToDoItemRepository;
import com.todolist.model.ToDoCategory;
import com.todolist.model.ToDoItem;

import java.util.ArrayList;
import java.util.List;

public class ToDoFragmentPresenter implements ToDoFragmentContract.Presenter {

    private ToDoItemRepository toDoItemRepository;
    private ToDoFragmentContract.View view;

    public ToDoFragmentPresenter(@NonNull ToDoItemRepository toDoItemRepository , @NonNull ToDoFragmentContract.View view) {
        this.toDoItemRepository = toDoItemRepository;
        this.view = view;
        view.setPresenter( this );
    }

    @Override
    public void start() {
        toDoItemRepository.loadToDoItems(ToDoCategory.CATEGORY_ALL_ID, new ToDoItemDataSource.LoadToDoItemsCallBack() {
            @Override
            public void onToDoItemsLoaded(List<ToDoItem> toDos) {
                view.showToDoItems(toDos);
            }

            @Override
            public void onDataNotAvailable() {

            }
        });
    }

    @Override
    public void doneAction(ToDoItem toDoItem) {
        toDoItem.setDone( true );
        toDoItemRepository.completeToDo(toDoItem, new GenericDataSource.GenericToDoCallBack() {
            @Override
            public void onCompleted(ToDoItem toDo) {
                view.refreshTabs();
            }

            @Override
            public void onError() {

            }
        });
    }

    @Override
    public void forwardToToDoDetail() {
        view.showToDoDetail();
    }

    @Override
    public void doDisplayCategoryDialog() {
        view.showCategoryDialog();
    }

    @Override
    public void doGetToDoItemsByCategory(long categoryID) {
        toDoItemRepository.loadToDoItems(categoryID, new ToDoItemDataSource.LoadToDoItemsCallBack() {
            @Override
            public void onToDoItemsLoaded(List<ToDoItem> toDos) {
                view.showToDoItems(toDos);
            }

            @Override
            public void onDataNotAvailable() {
                view.showToDoItems( new ArrayList<ToDoItem>() );
            }
        });
    }

}
