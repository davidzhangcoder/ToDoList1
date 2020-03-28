package com.todolist.model;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.todolist.R;
import com.todolist.ui.adapter.CategoryListAdapter;

public class ToDoCategoryHodler extends BaseViewHolder<IToDoCategory , CategoryListAdapter>
{
    public ToDoCategoryHodler(View itemView) {
        super(itemView);
    }

    @Override
    public void setUpView(IToDoCategory toDoCategory, int position, CategoryListAdapter adapter) {
        ((TextView)getView(R.id.categoryName)).setText(toDoCategory.getName());

        View.OnClickListener itemOnClickListener = new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                adapter.getItemCallBack().doItemClickCallBack( toDoCategory );

                getView(R.id.categoryImageCheck).setVisibility(View.VISIBLE);
            }
        };

        if( ((ToDoCategory)adapter.getSelectedToDoCategory()) != null && ((ToDoCategory)adapter.getSelectedToDoCategory()).getId() == ((ToDoCategory)toDoCategory).getId() )
            getView(R.id.categoryImageCheck).setVisibility(View.VISIBLE);

        ((RelativeLayout)getView(R.id.categoryContainer)).setOnClickListener( itemOnClickListener );
    }

}
