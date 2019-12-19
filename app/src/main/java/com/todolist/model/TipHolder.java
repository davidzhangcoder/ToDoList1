package com.todolist.model;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.CheckBox;
import android.widget.TextView;

import com.todolist.R;
import com.todolist.SwipeLayout;
import com.todolist.ui.adapter.TipListAdapter;
import com.todolist.TipListItemTouchHelperCallback;
import com.todolist.util.DateUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TipHolder extends BaseViewHolder<ToDoItem , TipListAdapter> implements TipListItemTouchHelperCallback.OnDragVHListener {
//    private TextView content;
//    private TextView tipDueDateAndTime;
//    private TextView category;
//    private CheckBox checkbox;
//    private SwipeLayout swipeLayout;
    AnimatorSet upSet, downSet;

    public TipHolder(View itemView) {
        super(itemView);
//        content = (TextView) itemView.findViewById(R.id.tip_content);
//        tipDueDateAndTime = (TextView) itemView.findViewById(R.id.tip_dueDateAndTime);
//        checkbox = (CheckBox) itemView.findViewById(R.id.tip_checkbox);
//        swipeLayout = (SwipeLayout) itemView.findViewById(R.id.tip_layout);

        //创建动画
        ObjectAnimator scaleXAnim = ObjectAnimator.ofFloat(itemView, "scaleX", 1.05f);
        ObjectAnimator scaleYAnim = ObjectAnimator.ofFloat(itemView, "scaleY", 1.05f);
        ObjectAnimator upAnim = ObjectAnimator.ofFloat(itemView, "translationZ", 10);
        ObjectAnimator upColor = ObjectAnimator.ofArgb(itemView, "backgroundColor", Color.LTGRAY);
        upSet = new AnimatorSet();
        upSet.playSequentially(scaleXAnim, scaleYAnim, upAnim, upColor);
        upSet.setDuration(100);
        upSet.setInterpolator(new DecelerateInterpolator());

        ObjectAnimator downAnim = ObjectAnimator.ofFloat(itemView, "translationZ", 0);
        ObjectAnimator scaleXDownAnim = ObjectAnimator.ofFloat(itemView, "scaleX", 1.0f);
        ObjectAnimator scaleYDownAnim = ObjectAnimator.ofFloat(itemView, "scaleY", 1.0f);
        ObjectAnimator downColor = ObjectAnimator.ofArgb(itemView, "backgroundColor", 0);
        downSet = new AnimatorSet();
        downSet.playSequentially(scaleXDownAnim, scaleYDownAnim, downAnim, downColor);
        downSet.setDuration(100);
        downSet.setInterpolator(new DecelerateInterpolator());

    }

    @Override
    public void setUpView(ToDoItem toDoItem, int position , final TipListAdapter tipListAdapter) {
        ((TextView)getView(R.id.tip_content)).setText(toDoItem.getName().toString() + " - " + toDoItem.getToDoCategory().getName().toString());
//        ((TextView)getView(R.id.tip_category)).setText(toDoItem.getToDoCategory().getName().toString());

        Calendar currentDate = Calendar.getInstance();

        if( toDoItem.getDueDate() != null ) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, d MMM yyyy hh:mm aaa");// HH:mm:ss
            String dateString = simpleDateFormat.format( toDoItem.getDueDate().getTime() );
            TextView tipDueDateAndTimeTextView = ((TextView)getView(R.id.tip_dueDateAndTime));
            tipDueDateAndTimeTextView.setText( dateString );

            if( toDoItem.getDueDate() != null && !toDoItem.isDone() ) {
                if (DateUtil.before(toDoItem.getDueDate(), currentDate)) {
                    int overDueColor = tipDueDateAndTimeTextView.getContext().getResources().getColor(R.color.red_secondary);
                    tipDueDateAndTimeTextView.setTextColor( overDueColor );
                } else if (DateUtil.sameDay(toDoItem.getDueDate(), currentDate)) {
                    int colorPrimaryColor = tipDueDateAndTimeTextView.getContext().getResources().getColor(R.color.colorPrimary);
                    tipDueDateAndTimeTextView.setTextColor( colorPrimaryColor );
                }
            }
        }

        getView(R.id.tipCardView).setOnClickListener( tipListAdapter.getClickListener(position) );
//        getView(R.id.tip_content).setOnClickListener(tipListAdapter.getClickListener(position));

        ((CheckBox)getView(R.id.tip_checkbox)).setChecked( toDoItem.isDone() );

        getView(R.id.tip_checkbox).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tipListAdapter.changeToFinish(TipHolder.this);
            }
        });

        ((SwipeLayout)getView(R.id.tip_layout)).setListener(new SwipeLayout.OperatorListener() {
            @Override
            public void onDelete() {
                tipListAdapter.deleteItem(TipHolder.this.getAdapterPosition());
            }

            @Override
            public void onEdit() {

            }
        });
    }

    @Override
    public void onItemFinish() {
        itemView.clearAnimation();
        downSet.start();
    }

    @Override
    public void onItemSelected() {
        itemView.clearAnimation();
        upSet.start();
    }

//    public TextView getContent() {
//        return content;
//    }
//
//    public void setContent(TextView content) {
//        this.content = content;
//    }
//
//    public TextView getTipDueDateAndTime() {
//        return tipDueDateAndTime;
//    }
//
//    public void setTipDueDateAndTime(TextView tipDueDateAndTime) {
//        this.tipDueDateAndTime = tipDueDateAndTime;
//    }
//
//    public TextView getCategory() {
//        return category;
//    }
//
//    public void setCategory(TextView category) {
//        this.category = category;
//    }
//
//    public CheckBox getCheckbox() {
//        return checkbox;
//    }
//
//    public void setCheckbox(CheckBox checkbox) {
//        this.checkbox = checkbox;
//    }
//
//    public SwipeLayout getSwipeLayout() {
//        return swipeLayout;
//    }
//
//    public void setSwipeLayout(SwipeLayout swipeLayout) {
//        this.swipeLayout = swipeLayout;
//    }

    public AnimatorSet getUpSet() {
        return upSet;
    }

    public void setUpSet(AnimatorSet upSet) {
        this.upSet = upSet;
    }

    public AnimatorSet getDownSet() {
        return downSet;
    }

    public void setDownSet(AnimatorSet downSet) {
        this.downSet = downSet;
    }
}
