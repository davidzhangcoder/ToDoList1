package com.todolist.tododetail;

import com.todolist.data.source.GenericDataSource;
import com.todolist.data.source.ToDoItemRepository;
import com.todolist.model.ToDoItem;

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

    }

    @Override
    public void createOrUpdateToDoItem(ToDoItem toDoItem) {
         GenericDataSource.GenericToDoCallBack callBack = new GenericDataSource.GenericToDoCallBack() {
                @Override
                public void onCompleted(ToDoItem toDo) {
                    view.showAfterCreateOrUpdateToDoItem();
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
