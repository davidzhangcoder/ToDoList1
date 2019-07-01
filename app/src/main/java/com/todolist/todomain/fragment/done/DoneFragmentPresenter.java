package com.todolist.todomain.fragment.done;

import com.todolist.data.source.GenericDataSource;
import com.todolist.data.source.ToDoItemDataSource;
import com.todolist.data.source.ToDoItemRepository;
import com.todolist.model.ToDoItem;

import java.util.List;

public class DoneFragmentPresenter implements DoneFragmentContract.Presenter {

    private ToDoItemRepository toDoItemRepository;
    private DoneFragmentContract.View view;

    public DoneFragmentPresenter(ToDoItemRepository toDoItemRepository, DoneFragmentContract.View view) {
        this.toDoItemRepository = toDoItemRepository;
        this.view = view;

        view.setPresenter( this );
    }

    @Override
    public void start() {
        toDoItemRepository.loadDoneItems(new ToDoItemDataSource.LoadToDoItemsCallBack() {
            @Override
            public void onToDoItemsLoaded(List<ToDoItem> toDos) {
                view.showToDoItems( toDos );
            }

            @Override
            public void onDataNotAvailable() {

            }
        });
    }

    @Override
    public void reverseDoneAction(ToDoItem toDoItem) {
        toDoItem.setDone( false );
        toDoItemRepository.reverseCompletedToDo(toDoItem, new GenericDataSource.GenericToDoCallBack() {
            @Override
            public void onCompleted(ToDoItem toDo) {
                view.refreshTabs();
            }

            @Override
            public void onError() {

            }
        });
    }
}
