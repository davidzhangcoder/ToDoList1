package com.todolist.model;


import com.todolist.R;
import com.todolist.data.model.ToDoCategory;
import com.todolist.data.model.ToDoItem;

public class TypeFactoryForToDoItemlist implements ITypeFactory {
    @Override
    public int getType(ToDoItemTitle toDoItemTitle) {
        if( toDoItemTitle.getTitleCategory() == ToDoItemTitle.TAG_TITLE_CATEGORY_OVERDUE )
            return R.layout.item_tiplist_title_overdue;
        else if( toDoItemTitle.getTitleCategory() == ToDoItemTitle.TAG_TITLE_CATEGORY_CURRENT )
            return R.layout.item_tiplist_title_normal;
        else if( toDoItemTitle.getTitleCategory() == ToDoItemTitle.TAG_TITLE_CATEGORY_NEXT_WEEK )
            return R.layout.item_tiplist_title_normal;
        else if( toDoItemTitle.getTitleCategory() == ToDoItemTitle.TAG_TITLE_CATEGORY_LATER )
            return R.layout.item_tiplist_title_normal;
        else
            return R.layout.item_tiplist_title_nodata;
    }

    @Override
    public int getType(ToDoItem toDoItem) {
        return R.layout.item_tiplist;
    }

    @Override
    public int getType(ToDoCategory toDoCategory) {
            return R.layout.item_category;
    }

}
