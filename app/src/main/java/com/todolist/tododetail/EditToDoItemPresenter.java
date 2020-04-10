package com.todolist.tododetail;

import com.todolist.data.model.ToDoCategory;
import com.todolist.data.model.ToDoItem;
import com.todolist.data.source.GenericDataSource;
import com.todolist.data.source.ToDoItemDataSource;
import com.todolist.data.source.ToDoItemRepository;

import java.util.List;

public class EditToDoItemPresenter implements EditToDoItemContract.Presenter {
    private ToDoItemRepository toDoItemRepository;
    private EditToDoItemContract.View view;

    public EditToDoItemPresenter(ToDoItemRepository toDoItemRepository, EditToDoItemContract.View view) {
        this.toDoItemRepository = toDoItemRepository;
        this.view = view;
        view.setPresenter(this);
    }

    @Override
    public void start() {
        toDoItemRepository.loadToDoCategorys(new ToDoItemDataSource.LoadToDoCategorysCallBack() {
            @Override
            public void onToDoCategorysLoaded(List<ToDoCategory> toDoCategorys) {
                view.initialToDoCategoryList( toDoCategorys );
            }

            @Override
            public void onDataNotAvailable() {

            }
        });
    }

    @Override
    public void createOrUpdateToDoItem(ToDoItem toDoItem) {
         GenericDataSource.GenericToDoCallBack callBack = new GenericDataSource.GenericToDoCallBack() {
                @Override
                public void onCompleted(ToDoItem toDo) {
                    view.showAfterCreateOrUpdateToDoItem(toDo);
                }

                @Override
                public void onError() {

                }
            };
        if( toDoItem.getId() == 0 ) {
            toDoItemRepository.saveToDo(toDoItem, callBack );
        }
        else {
            toDoItemRepository.updateToDo(toDoItem, callBack);
        }
    }
}
