package com.todolist.util;

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

        boolean hasAddedCurrentCategory = false;
        boolean hasAddedNextWeekCategory = false;
        boolean hasAddedLaterCategory = false;

        Calendar currentDate = Calendar.getInstance();
        for( int i = 0 ; i < toDoItemList.size() ; i++ )
        {
            ToDoItem toDoItem = toDoItemList.get( i );

            if( toDoItem.getDueDate() == null ) {
                emptyDueDateList.add( toDoItem );
                continue;
            }

            if( toDoItem.getDueDate().before( currentDate ) && i == 0 ) {
                //Add Overdue Category on position 1
                ToDoItemTitle newToDoItem = new ToDoItemTitle();
                newToDoItem.setTitleCategory( ToDoItemTitle.TAG_TITLE_CATEGORY_OVERDUE );
                retList.add( 0 , newToDoItem );
            }
            else if( toDoItem.getDueDate().equals( currentDate ) && !hasAddedCurrentCategory )
            {
                //Add Overdue Category on position 1
                ToDoItemTitle newToDoItem = new ToDoItemTitle();
                newToDoItem.setTitleCategory( ToDoItemTitle.TAG_TITLE_CATEGORY_CURRENT );
                retList.add( i , newToDoItem );
            }
            else if( toDoItem.getDueDate().after( currentDate ) ) {
                int whichWeek = currentDate.get( Calendar.WEEK_OF_YEAR );
                int objectWhichWeek = toDoItem.getDueDate().get( Calendar.WEEK_OF_YEAR );
                if( objectWhichWeek - whichWeek == 1 && !hasAddedNextWeekCategory ) {
                    ToDoItemTitle newToDoItem = new ToDoItemTitle();
                    newToDoItem.setTitleCategory( ToDoItemTitle.TAG_TITLE_CATEGORY_NEXT_WEEK );
                    retList.add( i , newToDoItem );
                }
                else if( !hasAddedLaterCategory ){
                    ToDoItemTitle newToDoItem = new ToDoItemTitle();
                    newToDoItem.setTitleCategory( ToDoItemTitle.TAG_TITLE_CATEGORY_LATER );
                    retList.add( i , newToDoItem );
                }
            }

            retList.add( toDoItem );
        }

        if( emptyDueDateList.size() > 0 )
        {
            //retList add No Date Category first, then add emptyDueDateList
            ToDoItemTitle newToDoItem = new ToDoItemTitle();
            newToDoItem.setTitleCategory( ToDoItemTitle.TAG_TITLE_CATEGORY_NO_DATE );
            retList.addAll( retList.size() , emptyDueDateList );
        }

        return retList;
    }

}
