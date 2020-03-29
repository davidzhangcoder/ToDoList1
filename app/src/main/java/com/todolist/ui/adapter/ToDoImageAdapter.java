package com.todolist.ui.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.todolist.R;
import com.todolist.model.BaseViewHolder;
import com.todolist.model.ToDoImage;
import com.todolist.model.ToDoImageHolder;

import java.util.ArrayList;
import java.util.List;

public class ToDoImageAdapter extends RecyclerView.Adapter<BaseViewHolder>
    implements
        IAdapterAction<ToDoImage>
{

    private Context mContext;

    private List<ToDoImage> data = new ArrayList<ToDoImage>();

    public ToDoImageAdapter(Context mContext , List<ToDoImage> data) {
        this.mContext = mContext;
        this.data = data;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_todoimage, parent, false);
        return new ToDoImageHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder baseViewHolder, int position) {
        baseViewHolder.setUpView( data.get(position) , position , this );
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public Context getContext() {
        return mContext;
    }

    public List<ToDoImage> getData() {
        return data;
    }

    @Override
    public void addElement(ToDoImage element) {
        getData().add( element );
        notifyDataSetChanged();
    }

    @Override
    public void addElement(ToDoImage element, int position) {
        getData().add( position , element );
        notifyDataSetChanged();
    }

    @Override
    public void deleteElement(int position) {
        getData().remove( position );
        notifyDataSetChanged();
    }
}
