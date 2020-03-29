package com.todolist.model;

import androidx.recyclerview.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;

public abstract class BaseViewHolder<T,V> extends RecyclerView.ViewHolder {

    private SparseArray<View> views;
    private View mItemView;

    public BaseViewHolder(View itemView) {
        super(itemView);
        views = new SparseArray<>();
        this.mItemView = itemView;
    }

    public View getView(int resID) {
        View view = views.get(resID);

        if (view == null) {
            view = mItemView.findViewById(resID);
            views.put(resID,view);
        }

        return view;
    }

    public abstract void setUpView(T model, int position , V adapter);
}

