package com.todolist.todomain;

import android.support.annotation.NonNull;

import com.todolist.data.source.GenericDataSource;
import com.todolist.data.source.ToDoItemDataSource;
import com.todolist.data.source.ToDoItemRepository;
import com.todolist.model.ToDoCategory;
import com.todolist.model.ToDoItem;

import java.util.List;

public class ToDoMainPresenter implements ToDoMainContract.Presenter {

    private ToDoItemRepository toDoItemRepository;
    private ToDoMainContract.View view;
    private long filterToDoCategoryID = ToDoCategory.CATEGORY_ALL_ID;

    public ToDoMainPresenter(@NonNull ToDoItemRepository toDoItemRepository , @NonNull ToDoMainContract.View view) {
        this.toDoItemRepository = toDoItemRepository;
        this.view = view;
        view.setPresenter( this );
    }

    @Override
    public void start() {
        toDoItemRepository.loadToDoItems(filterToDoCategoryID, new ToDoItemDataSource.LoadToDoItemsCallBack() {
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

    public long getFilterToDoCategoryID() {
        return filterToDoCategoryID;
    }

    public void setFilterToDoCategoryID(long filterToDoCategoryID) {
        this.filterToDoCategoryID = filterToDoCategoryID;
    }
}
