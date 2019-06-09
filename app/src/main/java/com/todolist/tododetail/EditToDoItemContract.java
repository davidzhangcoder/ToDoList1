package com.todolist.tododetail;

import com.todolist.model.ToDoItem;
import com.todolist.ui.BasePresenter;
import com.todolist.ui.BaseView;

public interface EditToDoItemContract {
    interface View extends BaseView<Presenter> {
        void showAfterCreateOrUpdateToDoItem();
    }

    interface Presenter extends BasePresenter {
        void createOrUpdateToDoItem(ToDoItem toDoItem);
    }
}
