package com.todolist;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.todolist.model.BaseViewHolder;
import com.todolist.model.IToDoCategory;
import com.todolist.model.ToDoCategoryHodler;
import com.todolist.model.ToDoCategory;
import com.todolist.model.ToDoCategoryHodlerForEdit;
import com.todolist.model.TypeFactoryForToDoItemlist;

import java.util.ArrayList;
import java.util.List;

public class CategoryListAdapter extends RecyclerView.Adapter<BaseViewHolder>
{
    private Context mContext;

    private List<IToDoCategory> mData = new ArrayList<IToDoCategory>();

    private IToDoCategory selectedToDoCategory;

    private ItemCallBack itemCallBack;

    public CategoryListAdapter(Context mContext, List<IToDoCategory> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if( viewType == R.layout.item_category ) {
            View view = LayoutInflater.from(mContext).inflate(viewType, parent, false);
            return new ToDoCategoryHodler(view);
        }
        else {
            View view = LayoutInflater.from(mContext).inflate(viewType, parent, false);
            return new ToDoCategoryHodlerForEdit(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder baseViewHolder, int position) {
        baseViewHolder.setUpView( mData.get(position) , position , this );
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public int getItemViewType(int position) {
//        if( mData.get(position) instanceof ToDoCategory)
        {
            return ((ToDoCategory) mData.get(position)).type(new TypeFactoryForToDoItemlist());
        }
    }

    public IToDoCategory getSelectedToDoCategory() {
        return selectedToDoCategory;
    }

    public void setSelectedToDoCategory(IToDoCategory selectedToDoCategory) {
        this.selectedToDoCategory = selectedToDoCategory;
    }

    public interface ItemCallBack
    {
        public void doItemClickCallBack( IToDoCategory iToDoCategory );
    }

    public ItemCallBack getItemCallBack() {
        return itemCallBack;
    }

    public void setItemCallBack(ItemCallBack itemCallBack) {
        this.itemCallBack = itemCallBack;
    }
}
