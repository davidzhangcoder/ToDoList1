package com.todolist.todomain;

import com.todolist.ui.BasePresenter;
import com.todolist.ui.BaseView;

public interface CategoryContract
{
    interface View extends BaseView<Presenter> {

    }

    interface Presenter extends BasePresenter {

    }
}
