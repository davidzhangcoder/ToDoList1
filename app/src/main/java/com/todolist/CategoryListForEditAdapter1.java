package com.todolist;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.todolist.model.BaseViewHolder;
import com.todolist.model.IToDoCategory;
import com.todolist.model.ToDoCategory;
import com.todolist.model.ToDoCategoryHodler;
import com.todolist.model.TypeFactoryForToDoItemlist;

import java.util.ArrayList;
import java.util.List;

public class CategoryListForEditAdapter1 extends RecyclerView.Adapter<ToDoCategoryHodler>
{
    private Context mContext;

    private List<IToDoCategory> mData = new ArrayList<IToDoCategory>();

    private ToDoCategory selectedToDoCategory;

    public CategoryListForEditAdapter1(Context mContext, List<IToDoCategory> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public ToDoCategoryHodler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(viewType, parent, false);
        return new ToDoCategoryHodler( view );
    }

    @Override
    public void onBindViewHolder(@NonNull ToDoCategoryHodler baseViewHolder, int position) {
//        baseViewHolder.setUpView( mData.get(position) , position , this );
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public int getItemViewType(int position) {
//        if( mData.get(position) instanceof ToDoCategory)
            return ((ToDoCategory)mData.get(position)).type( new TypeFactoryForToDoItemlist() );
//        else
//            return ((ToDoCategoryAddNew)mData.get(position)).type( new TypeFactoryForToDoItemlist() );
    }

    public ToDoCategory getSelectedToDoCategory() {
        return selectedToDoCategory;
    }

    public void setSelectedToDoCategory(ToDoCategory selectedToDoCategory) {
        this.selectedToDoCategory = selectedToDoCategory;
    }
}
