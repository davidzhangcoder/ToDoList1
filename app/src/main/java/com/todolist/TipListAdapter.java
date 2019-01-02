package com.todolist;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.todolist.model.BaseViewHolder;
import com.todolist.model.IToDoItem;
import com.todolist.model.TipHolder;
import com.todolist.model.ToDoItem;
import com.todolist.model.ToDoItemTitle;
import com.todolist.model.ToDoItemTitleHolder;
import com.todolist.model.TypeFactoryForToDoItemlist;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TipListAdapter extends RecyclerView.Adapter<BaseViewHolder> implements TipListItemTouchHelperCallback.onDragListener
{

    private Context mContext;

    public final static int TAG_TIPTODO = 1;

    private List<IToDoItem> mData = new ArrayList<IToDoItem>();

    public final static String CONTENT = "content";

    private ToDoItemAction doneAction;

//    private int textPosition;
//    private RecyclerView.ViewHolder mTextHolder;

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

    public View.OnClickListener getClickListener(final int position) {
        View.OnClickListener mListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, EditToDoItemActivity.class);
                i.putExtra("content", ((TextView)v).getText());
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
//            ((TipHolder)holder).getContent().setText(((ToDoItem)mData.get(position)).getName().toString());
//            if( ((ToDoItem)mData.get(position)).getDueDate() != null ) {
//                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// HH:mm:ss
//                String dateString = simpleDateFormat.format( ((ToDoItem)mData.get(position)).getDueDate().getTime() );
//                ((TipHolder) holder).getTipDueDateAndTime().setText( dateString );
//            }
//            ((TipHolder)holder).getContent().setOnClickListener(getClickListener(position));
//            ((TipHolder)holder).getCheckbox().setChecked(false);
//            ((TipHolder)holder).getCheckbox().setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    changeToFinish(holder);
//                }
//            });
//            ((TipHolder)holder).getSwipeLayout().setListener(new SwipeLayout.OperatorListener() {
//                @Override
//                public void onDelete() {
//                    deleteItem(holder.getAdapterPosition());
//                }
//
//                @Override
//                public void onEdit() {
//
//                }
//            });
    }

    public void deleteItem(int position) {
        notifyItemRemoved(position);
        mData.remove(position);
    }


    public void changeToFinish(RecyclerView.ViewHolder holder) {
        int position = holder.getAdapterPosition();

        if( doneAction != null )
        {
            doneAction.doAction( (ToDoItem) mData.get(position) );
        }

        mData.remove(position);

        notifyItemRemoved(position);
    }

    public interface ToDoItemAction
    {
        void doAction(ToDoItem toDoItem);
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

//    public class TipHolder extends RecyclerView.ViewHolder implements TipListItemTouchHelperCallback.OnDragVHListener {
//        private TextView content;
//        private TextView tipDueDateAndTime;
//        private TextView category;
//        private CheckBox checkbox;
//        private SwipeLayout swipeLayout;
//        AnimatorSet upSet, downSet;
//        public TipHolder(View itemView) {
//            super(itemView);
//            content = (TextView) itemView.findViewById(R.id.tip_content);
//            tipDueDateAndTime = (TextView) itemView.findViewById(R.id.tip_dueDateAndTime);
//            checkbox = (CheckBox) itemView.findViewById(R.id.tip_checkbox);
//            swipeLayout = (SwipeLayout) itemView.findViewById(R.id.tip_layout);
//
//            //创建动画
//            ObjectAnimator scaleXAnim = ObjectAnimator.ofFloat(itemView, "scaleX", 1.05f);
//            ObjectAnimator scaleYAnim = ObjectAnimator.ofFloat(itemView, "scaleY", 1.05f);
//            ObjectAnimator upAnim = ObjectAnimator.ofFloat(itemView, "translationZ", 10);
//            ObjectAnimator upColor = ObjectAnimator.ofArgb(itemView, "backgroundColor", Color.LTGRAY);
//            upSet = new AnimatorSet();
//            upSet.playSequentially(scaleXAnim, scaleYAnim, upAnim, upColor);
//            upSet.setDuration(100);
//            upSet.setInterpolator(new DecelerateInterpolator());
//
//            ObjectAnimator downAnim = ObjectAnimator.ofFloat(itemView, "translationZ", 0);
//            ObjectAnimator scaleXDownAnim = ObjectAnimator.ofFloat(itemView, "scaleX", 1.0f);
//            ObjectAnimator scaleYDownAnim = ObjectAnimator.ofFloat(itemView, "scaleY", 1.0f);
//            ObjectAnimator downColor = ObjectAnimator.ofArgb(itemView, "backgroundColor", 0);
//            downSet = new AnimatorSet();
//            downSet.playSequentially(scaleXDownAnim, scaleYDownAnim, downAnim, downColor);
//            downSet.setDuration(100);
//            downSet.setInterpolator(new DecelerateInterpolator());
//
//        }
//
//        @Override
//        public void onItemFinish() {
//            itemView.clearAnimation();
//            downSet.start();
//        }
//
//        @Override
//        public void onItemSelected() {
//            itemView.clearAnimation();
//            upSet.start();
//        }
//    }

    public ToDoItemAction getDoneAction() {
        return doneAction;
    }

    public void setDoneAction(ToDoItemAction doneAction) {
        this.doneAction = doneAction;
    }
}
