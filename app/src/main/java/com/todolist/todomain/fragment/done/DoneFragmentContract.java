package com.todolist.todomain.fragment.done;

import com.todolist.model.ToDoItem;
import com.todolist.ui.BasePresenter;
import com.todolist.ui.BaseView;

import java.util.List;

public interface DoneFragmentContract {
    interface View extends BaseView<Presenter> {
        void showToDoItems(List<ToDoItem> toDoItemList);

        void refreshTabs();
    }

    interface Presenter extends BasePresenter {
        void reverseDoneAction(ToDoItem toDoItem);
    }

}