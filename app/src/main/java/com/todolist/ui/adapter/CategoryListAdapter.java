package com.todolist.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.todolist.R;
import com.todolist.data.model.ToDoCategory;
import com.todolist.model.BaseViewHolder;
import com.todolist.model.ToDoCategoryHodler;
import com.todolist.model.ToDoCategoryHodlerForEdit;
import com.todolist.model.TypeFactoryForToDoItemlist;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import static com.google.common.base.Preconditions.checkNotNull;

public class CategoryListAdapter extends RecyclerView.Adapter<BaseViewHolder>
{
    private Context mContext;

    private List<ToDoCategory> mData = new ArrayList<ToDoCategory>();

    private ToDoCategory selectedToDoCategory;

    private ItemCallBack itemCallBack;
    
    public CategoryListAdapter(Context mContext, List<ToDoCategory> mData , ToDoCategory selectedToDoCategory ) {
        this.mContext = mContext;
        this.mData = mData;
        this.selectedToDoCategory = selectedToDoCategory;
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

    public void replaceData( @NonNull List<ToDoCategory> toDoCategoryList ) {
        mData = checkNotNull( toDoCategoryList );
        notifyDataSetChanged();
    }


    public ToDoCategory getSelectedToDoCategory() {
        return selectedToDoCategory;
    }

    public void setSelectedToDoCategory(ToDoCategory selectedToDoCategory) {
        this.selectedToDoCategory = selectedToDoCategory;
    }

    public interface ItemCallBack
    {
        public void doItemClickCallBack( ToDoCategory iToDoCategory );
    }

    public ItemCallBack getItemCallBack() {
        return itemCallBack;
    }

    public void setItemCallBack(ItemCallBack itemCallBack) {
        this.itemCallBack = itemCallBack;
    }
}
