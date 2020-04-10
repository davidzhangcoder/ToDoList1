package com.todolist.todomain.fragment.todo;

import com.todolist.data.model.ToDoCategory;
import com.todolist.data.model.ToDoItem;
import com.todolist.data.source.GenericDataSource;
import com.todolist.data.source.ToDoItemDataSource;
import com.todolist.data.source.ToDoItemRepository;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

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
        toDoItemRepository.loadToDoItems(ToDoCategory.Companion.getCATEGORY_ALL_ID(), new ToDoItemDataSource.LoadToDoItemsCallBack() {
            @Override
            public void onToDoItemsLoaded(List<ToDoItem> toDos) {
                view.showToDoItems(toDos);
            }

            @Override
            public void onDataNotAvailable() {

            }
        });

        toDoItemRepository.loadToDoCategorys(new ToDoItemDataSource.LoadToDoCategorysCallBack() {
            @Override
            public void onToDoCategorysLoaded(List<ToDoCategory> toDoCategorys) {
                view.initialCategorySpinner( toDoCategorys );
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
