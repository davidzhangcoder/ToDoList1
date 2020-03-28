package com.todolist.todomain.fragment.todo;

import com.todolist.model.ToDoCategory;
import com.todolist.model.ToDoItem;
import com.todolist.ui.BasePresenter;
import com.todolist.ui.BaseView;

import java.util.List;

public interface ToDoFragmentContract {

    interface View extends BaseView<Presenter>
    {
        void showToDoItems(List<ToDoItem> toDoItemList);

        void refreshTabs();

        void showToDoDetail();

        void showCategoryDialog();

        void initialCategorySpinner( List<ToDoCategory> toDoCategorys );
    }

    interface Presenter extends BasePresenter
    {
        void doneAction(ToDoItem toDoItem);

        void forwardToToDoDetail();

        void doDisplayCategoryDialog();

        void doGetToDoItemsByCategory(long categoryID);
    }
}
