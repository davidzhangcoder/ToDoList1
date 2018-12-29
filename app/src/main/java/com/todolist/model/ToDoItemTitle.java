package com.todolist.model;

import java.io.Serializable;

public class ToDoItemTitle implements Serializable, IToDoItemType , IToDoItem
{

    public final static int TAG_TITLE_CATEGORY_OVERDUE = 1;
    public final static int TAG_TITLE_CATEGORY_CURRENT = 2;
    public final static int TAG_TITLE_CATEGORY_NEXT_WEEK = 3;
    public final static int TAG_TITLE_CATEGORY_LATER = 4;
    public final static int TAG_TITLE_CATEGORY_NO_DATE = 5;

    private int titleCategory = 0;

    @Override
    public int type(ITypeFactory typeFactory) {
        return typeFactory.getType( this );
    }

    public int getTitleCategory() {
        return titleCategory;
    }

    public void setTitleCategory(int titleCategory) {
        this.titleCategory = titleCategory;
    }
}
