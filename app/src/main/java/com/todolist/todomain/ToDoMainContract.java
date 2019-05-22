package com.todolist.todomain;

import com.todolist.model.ToDoItem;
import com.todolist.ui.BasePresenter;
import com.todolist.ui.BaseView;

import java.util.List;

public interface ToDoMainContract {

    interface View extends BaseView<Presenter>
    {
        void showToDoItems(List<ToDoItem> toDoItemList);

        void refreshTabs();

        void showToDoDetail();
    }

    interface Presenter extends BasePresenter
    {
        void doneAction(ToDoItem toDoItem);

        void forwardToToDoDetail();
    }
}
