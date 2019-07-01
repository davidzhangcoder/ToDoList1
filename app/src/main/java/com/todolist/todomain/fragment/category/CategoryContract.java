package com.todolist.todomain.fragment.category;

import com.todolist.model.ToDoCategory;
import com.todolist.ui.BasePresenter;
import com.todolist.ui.BaseView;

import java.util.List;

public interface CategoryContract
{
    interface View extends BaseView<Presenter> {
        void showToDoCategorys(List<ToDoCategory> toDoCategoryList);
    }

    interface Presenter extends BasePresenter {
        void saveCategory(ToDoCategory toDocategory);
    }
}
