package com.todolist.tododetail;

import com.todolist.data.model.ToDoCategory;
import com.todolist.data.model.ToDoItem;
import com.todolist.ui.BasePresenter;
import com.todolist.ui.BaseView;

import java.util.List;

public interface EditToDoItemContract {
    interface View extends BaseView<Presenter> {
        void showAfterCreateOrUpdateToDoItem(ToDoItem toDo);

        void initialToDoCategoryList(List<ToDoCategory> toDoCategoryList);
    }

    interface Presenter extends BasePresenter {
        void createOrUpdateToDoItem(ToDoItem toDoItem);
    }
}
