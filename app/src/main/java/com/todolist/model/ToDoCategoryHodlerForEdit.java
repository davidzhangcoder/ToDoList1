package com.todolist.model;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.todolist.R;
import com.todolist.data.model.ToDoCategory;
import com.todolist.ui.adapter.CategoryListAdapter;

import androidx.appcompat.widget.AppCompatImageView;

public class ToDoCategoryHodlerForEdit extends BaseViewHolder<ToDoCategory, CategoryListAdapter> {
    public ToDoCategoryHodlerForEdit(View itemView) {
        super(itemView);
    }

    @Override
    public void setUpView(ToDoCategory toDoCategory, int position, CategoryListAdapter adapter) {
        ((TextView)getView(R.id.categoryName)).setText(toDoCategory.getName());

        if( toDoCategory instanceof  ToDoCategory && ((ToDoCategory)toDoCategory).getSelected() )
            ((AppCompatImageView)getView(R.id.categoryImageCheck)).setVisibility( View.VISIBLE );
        else
            ((AppCompatImageView)getView(R.id.categoryImageCheck)).setVisibility( View.INVISIBLE );

        View.OnClickListener itemOnClickListener = new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                adapter.getItemCallBack().doItemClickCallBack( toDoCategory );
            }
        };
        ((RelativeLayout)getView(R.id.categoryContainer)).setOnClickListener( itemOnClickListener );
    }
}
