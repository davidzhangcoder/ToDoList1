package com.todolist.model;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.todolist.CategoryListAdapter;
import com.todolist.R;

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
            }
        };

        ((LinearLayout)getView(R.id.categoryContainer)).setOnClickListener( itemOnClickListener );
    }

}
