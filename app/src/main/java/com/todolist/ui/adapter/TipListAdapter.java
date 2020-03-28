package com.todolist.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.todolist.R;
import com.todolist.TipListItemTouchHelperCallback;
import com.todolist.model.BaseViewHolder;
import com.todolist.model.IToDoItem;
import com.todolist.model.TipHolder;
import com.todolist.model.ToDoItem;
import com.todolist.model.ToDoItemTitle;
import com.todolist.model.ToDoItemTitleHolder;
import com.todolist.model.TypeFactoryForToDoItemlist;
import com.todolist.tododetail.EditToDoItemActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class TipListAdapter extends RecyclerView.Adapter<BaseViewHolder> implements TipListItemTouchHelperCallback.onDragListener
{

    private Context mContext;

    public final static int TAG_TIPTODO = 1;

    private List<IToDoItem> mData = new ArrayList<IToDoItem>();

    public final static String CONTENT = "content";

    private ToDoItemAction doneAction;

    public interface ToDoItemAction
    {
        void doAction(RecyclerView.ViewHolder viewHolder, List<IToDoItem> mData , RecyclerView.Adapter adapter);
    }

    public TipListAdapter(Context context, List<IToDoItem> listTodo) {
        mContext = context;
        mData.addAll(listTodo);
    }

    public void notifyData(List<IToDoItem> toDoItemList) {
        if (toDoItemList != null) {
            int previousSize = mData.size();
            mData.clear();
            notifyItemRangeRemoved(0, previousSize);
            mData.addAll(toDoItemList);
            notifyItemRangeInserted(0, toDoItemList.size());
        }
    }

    public void replaceData( @NonNull List<IToDoItem> toDoItemList ) {
        mData = checkNotNull( toDoItemList );
        notifyDataSetChanged();
    }

    public View.OnClickListener getClickListener(final int position) {
        View.OnClickListener mListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, EditToDoItemActivity.class);
//                i.putExtra("content", ((TextView)v).getText());
                i.putExtra(EditToDoItemActivity.EDITTODOITEMACTIVITY_TODOITEM, (ToDoItem)mData.get(position));
                mContext.startActivity(i);
            }
        };

        return mListener;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(viewType, parent, false);
        if( viewType == R.layout.item_tiplist )
            return new TipHolder(view);
        else
            return new ToDoItemTitleHolder(view);
    }

    @Override
    public void onBindViewHolder(final BaseViewHolder holder, final int position) {
        holder.setUpView( mData.get(position) , position , this );
    }

    public void deleteItem(int position) {
        notifyItemRemoved(position);
        mData.remove(position);
    }


    public void changeToFinish(RecyclerView.ViewHolder holder) {

        doneAction.doAction( holder , mData , this );

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public int getItemViewType(int position) {
        if( mData.get(position) instanceof ToDoItem)
            return ((ToDoItem)mData.get(position)).type( new TypeFactoryForToDoItemlist() );
        else
            return ((ToDoItemTitle)mData.get(position)).type( new TypeFactoryForToDoItemlist());
    }

    @Override
    public boolean onItemDrag(int fromPosition, int toPosition) {
        Collections.swap(mData, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return false;
    }


    public ToDoItemAction getDoneAction() {
        return doneAction;
    }

    public void setDoneAction(ToDoItemAction doneAction) {
        this.doneAction = doneAction;
    }
}
