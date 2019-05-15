package com.todolist.todomain;

import com.todolist.ui.BasePresenter;
import com.todolist.ui.BaseView;

public interface ToDoMainContract {

    interface View extends BaseView<Presenter>
    {
        void showCategoryFilterDialog();

        void showAddCategoryDialog();
    }

    interface  Presenter extends BasePresenter
    {
        void addToDo();

        void updateToDo();

        void completetoDo();

        void onShowCategoryFilterDialog();
    }
}
