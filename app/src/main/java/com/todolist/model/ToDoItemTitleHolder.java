package com.todolist.model;

import android.view.View;
import android.widget.TextView;

import com.todolist.R;
import com.todolist.ui.adapter.TipListAdapter;

public class ToDoItemTitleHolder extends BaseViewHolder<ToDoItemTitle , TipListAdapter> {

    public ToDoItemTitleHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setUpView(ToDoItemTitle toDoItemTitle, int position , final TipListAdapter tipListAdapter) {
        ((TextView)getView(R.id.tipTitle)).setText(toDoItemTitle.getTitle()+"");
    }

}
