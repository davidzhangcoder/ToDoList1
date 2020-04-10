package com.todolist.model;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.CheckBox;
import android.widget.TextView;

import com.maltaisn.recurpicker.Recurrence;
import com.todolist.R;
import com.todolist.TipListItemTouchHelperCallback;
import com.todolist.app.App;
import com.todolist.data.model.ToDoItem;
import com.todolist.ui.adapter.TipListAdapter;
import com.todolist.util.DateUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TipHolder extends BaseViewHolder<ToDoItem, TipListAdapter> implements TipListItemTouchHelperCallback.OnDragVHListener {

    AnimatorSet upSet, downSet;

    public TipHolder(View itemView) {
        super(itemView);

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

        Calendar currentDate = Calendar.getInstance();

        if( toDoItem.getDueDate() != null ) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(App.getContext().getResources().getString(R.string.date_format));// HH:mm:ss
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

        TextView recurrenceTextView  = (TextView)getView(R.id.tip_recurrence);
        recurrenceTextView.setVisibility(View.GONE);
        if( toDoItem.getRecurrencePeriod() == Recurrence.DAILY ) {
            recurrenceTextView.setText(R.string.everyday);
            recurrenceTextView.setVisibility(View.VISIBLE);
        }
        else if( toDoItem.getRecurrencePeriod() == Recurrence.WEEKLY ) {
            recurrenceTextView.setText(R.string.everyweek);
            recurrenceTextView.setVisibility(View.VISIBLE);
        }
        else if( toDoItem.getRecurrencePeriod() == Recurrence.MONTHLY ) {
            recurrenceTextView.setText(R.string.everymonth);
            recurrenceTextView.setVisibility(View.VISIBLE);
        }
        else if( toDoItem.getRecurrencePeriod() == Recurrence.YEARLY ) {
            recurrenceTextView.setText(R.string.everyyear);
            recurrenceTextView.setVisibility(View.VISIBLE);
        }


        getView(R.id.tipCardView).setOnClickListener( tipListAdapter.getClickListener(position) );

        CheckBox checkBox = ((CheckBox)getView(R.id.tip_checkbox));
        checkBox.setChecked( toDoItem.isDone() );

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  tipListAdapter.changeToFinish(TipHolder.this);
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
