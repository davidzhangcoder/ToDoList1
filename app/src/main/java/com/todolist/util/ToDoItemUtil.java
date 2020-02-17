package com.todolist.util;

import com.maltaisn.recurpicker.Recurrence;
import com.todolist.app.App;
import com.todolist.R;
import com.todolist.model.IToDoItem;
import com.todolist.model.ToDoItem;
import com.todolist.model.ToDoItemTitle;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

public class ToDoItemUtil {

    public static List<IToDoItem> getToDoItemGroupByDueTime(List<ToDoItem> toDoItemList )
    {
        List<IToDoItem> retList = new LinkedList<IToDoItem>();

        List<ToDoItem> emptyDueDateList = new ArrayList<ToDoItem>();
        List<ToDoItem> overdueDateList = new ArrayList<ToDoItem>();
        List<ToDoItem> currentDateList = new ArrayList<ToDoItem>();
        List<ToDoItem> nextWeekDateList = new ArrayList<ToDoItem>();
        List<ToDoItem> laterDateList = new ArrayList<ToDoItem>();
//        List<ToDoItem> noDateDateList = new ArrayList<ToDoItem>();

//        boolean hasAddedCurrentCategory = false;
//        boolean hasAddedNextWeekCategory = false;
//        boolean hasAddedLaterCategory = false;

        Calendar currentDate = Calendar.getInstance();
        for( int i = 0 ; i < toDoItemList.size() ; i++ )
        {
            ToDoItem toDoItem = toDoItemList.get( i );

            if( toDoItem.getDueDate() == null ) {
                emptyDueDateList.add( toDoItem );
//                continue;
            }

            if( DateUtil.before(toDoItem.getDueDate(),currentDate) ) {
//                //Add Overdue Category on position 1
//                ToDoItemTitle newToDoItem = new ToDoItemTitle();
//                newToDoItem.setTitleCategory( ToDoItemTitle.TAG_TITLE_CATEGORY_OVERDUE );
//                newToDoItem.setTitle( App.getContext().getResources().getString(R.string.overdue) );
//                retList.add( 0 , newToDoItem );

                Calendar toCalendar = Calendar.getInstance();
                toCalendar.add( Calendar.DATE , 1 );
                toCalendar.set( Calendar.HOUR_OF_DAY , 23 );
                toCalendar.set( Calendar.MINUTE , 59 );
                toCalendar.set( Calendar.SECOND , 59 );

                if( toDoItem.getRecurrencePeriod() == -1 )
                    overdueDateList.add( toDoItem );
                else {
                    if ( toDoItem.getRecurrencePeriod() == Recurrence.DAILY && DateUtil.isHourAndMinutesInRange( toDoItem.getDueDate() , currentDate , toCalendar ) )
                        currentDateList.add(toDoItem);
                    else if (toDoItem.getRecurrencePeriod() == Recurrence.WEEKLY
                            && DateUtil.isSameWeekDay(toDoItem.getDueDate(), Calendar.getInstance())
                            && DateUtil.isHourAndMinutesInRange( toDoItem.getDueDate() , currentDate , toCalendar )
                            )
                        currentDateList.add(toDoItem);
                    else if (toDoItem.getRecurrencePeriod() == Recurrence.MONTHLY
                            && DateUtil.isSameMonthDay(toDoItem.getDueDate(), Calendar.getInstance())
                            && DateUtil.isHourAndMinutesInRange( toDoItem.getDueDate() , currentDate , toCalendar )
                            )
                        currentDateList.add(toDoItem);
                    else if (toDoItem.getRecurrencePeriod() == Recurrence.YEARLY
                            && DateUtil.isSameYearDay(toDoItem.getDueDate(), Calendar.getInstance())
                            && DateUtil.isHourAndMinutesInRange( toDoItem.getDueDate() , currentDate , toCalendar )
                            )
                        currentDateList.add(toDoItem);
                    else
                        overdueDateList.add( toDoItem );
                }
            }
            else if( DateUtil.sameDay(toDoItem.getDueDate(),currentDate))
            {
//                //Add Overdue Category on position 1
//                ToDoItemTitle newToDoItem = new ToDoItemTitle();
//                newToDoItem.setTitleCategory( ToDoItemTitle.TAG_TITLE_CATEGORY_CURRENT );
//                retList.add( i , newToDoItem );
//                newToDoItem.setTitle( App.getContext().getResources().getString(R.string.current) );
//                hasAddedCurrentCategory = true;

                currentDateList.add( toDoItem );
            }
            else if( DateUtil.after(toDoItem.getDueDate(),currentDate) ) {
                int whichWeek = currentDate.get( Calendar.WEEK_OF_YEAR );
                int objectWhichWeek = toDoItem.getDueDate().get( Calendar.WEEK_OF_YEAR );
                if( objectWhichWeek - whichWeek == 1  ) {
//                    ToDoItemTitle newToDoItem = new ToDoItemTitle();
//                    newToDoItem.setTitleCategory( ToDoItemTitle.TAG_TITLE_CATEGORY_NEXT_WEEK );
//                    newToDoItem.setTitle( App.getContext().getResources().getString(R.string.nextweek) );
//                    retList.add( i , newToDoItem );
//                    hasAddedNextWeekCategory = true;
                    nextWeekDateList.add( toDoItem );
                }
                else {
                    laterDateList.add( toDoItem );
                }
            }

        }

        if( overdueDateList.size() > 0 )
        {
            ToDoItemTitle newToDoItem = new ToDoItemTitle();
            newToDoItem.setTitleCategory( ToDoItemTitle.TAG_TITLE_CATEGORY_OVERDUE );
            newToDoItem.setTitle( App.getContext().getResources().getString(R.string.overdue) );
            retList.add( newToDoItem );
            retList.addAll( retList.size() , overdueDateList );
        }
        if( currentDateList.size() > 0 )
        {
            ToDoItemTitle newToDoItem = new ToDoItemTitle();
            newToDoItem.setTitleCategory( ToDoItemTitle.TAG_TITLE_CATEGORY_CURRENT );
            newToDoItem.setTitle( App.getContext().getResources().getString(R.string.current) );
            retList.add( newToDoItem );
            retList.addAll( retList.size() , currentDateList );
        }
        if( nextWeekDateList.size() > 0 )
        {
            ToDoItemTitle newToDoItem = new ToDoItemTitle();
            newToDoItem.setTitleCategory( ToDoItemTitle.TAG_TITLE_CATEGORY_NEXT_WEEK );
            newToDoItem.setTitle( App.getContext().getResources().getString(R.string.nextweek) );
            retList.add( newToDoItem );
            retList.addAll( retList.size() , nextWeekDateList );
        }
        if( laterDateList.size() > 0 )
        {
            ToDoItemTitle newToDoItem = new ToDoItemTitle();
            newToDoItem.setTitleCategory( ToDoItemTitle.TAG_TITLE_CATEGORY_LATER );
            newToDoItem.setTitle( App.getContext().getResources().getString(R.string.later) );
            retList.add( newToDoItem );
            retList.addAll( retList.size() , laterDateList );
        }

        if( emptyDueDateList.size() > 0 )
        {
            //retList add No Date Category first, then add emptyDueDateList
            ToDoItemTitle newToDoItem = new ToDoItemTitle();
            newToDoItem.setTitleCategory( ToDoItemTitle.TAG_TITLE_CATEGORY_NO_DATE );
            newToDoItem.setTitle( App.getContext().getResources().getString(R.string.nodate) );
            retList.add( newToDoItem );
            retList.addAll( retList.size() , emptyDueDateList );
        }



        return retList;
    }

}
