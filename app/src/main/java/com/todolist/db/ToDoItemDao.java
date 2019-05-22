package com.todolist.db;

import android.content.ContentValues;
import android.support.annotation.NonNull;

import com.todolist.context.ContextHolder;
import com.todolist.model.ToDoCategory;
import com.todolist.model.ToDoItem;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

public class ToDoItemDao
{

    private List<ToDoItem> getToDoItems(@NonNull String selection , @NonNull String[] parameters )
    {
		checkNotNull( selection );
		checkNotNull( parameters );
		
        List<ToDoItem> toDoItemList = new ArrayList<ToDoItem>();

        GenericDao db = new GenericDao(ContextHolder.getContext());

        List<Map<String, String>> resultList = db.getListData( ToDoItem.TABLE_NAME , selection , parameters ,  ToDoItem.COLUMN_DUE_TIMESTAMP + " ASC " );

        for(Iterator<Map<String, String>> it = resultList.iterator(); it.hasNext() ; ) {
            Map<String, String> result = it.next();
            ToDoItem toDoItem = new ToDoItem();
            toDoItem.setId(Long.parseLong(result.get(ToDoItem.COLUMN_ID)));
            toDoItem.setName(result.get(ToDoItem.COLUMN_NAME));
            if( result.get(ToDoItem.COLUMN_DUE_TIMESTAMP) != null && !result.get(ToDoItem.COLUMN_DUE_TIMESTAMP).trim().equalsIgnoreCase("") ) {
                toDoItem.setDueTimestamp(Long.parseLong(result.get(ToDoItem.COLUMN_DUE_TIMESTAMP)));
                Calendar date = Calendar.getInstance();
                date.setTimeInMillis(toDoItem.getDueTimestamp());
                toDoItem.setDueDate( date );
            }
            if( result.get(ToDoItem.COLUMN_REMIND_TIMESTAMP) != null && !result.get(ToDoItem.COLUMN_REMIND_TIMESTAMP).trim().equalsIgnoreCase("") ) {
                toDoItem.setRemindTimestamp(Long.parseLong(result.get(ToDoItem.COLUMN_REMIND_TIMESTAMP)));
                Calendar date = Calendar.getInstance();
                date.setTimeInMillis(toDoItem.getRemindTimestamp());
                toDoItem.setRemindDate( date );
            }
            if( result.get(ToDoItem.COLUMN_CHILD_TASK) != null && !result.get(ToDoItem.COLUMN_CHILD_TASK).trim().equalsIgnoreCase("") )
                toDoItem.setChildTask( result.get(ToDoItem.COLUMN_CHILD_TASK) );
            if( result.get(ToDoItem.COLUMN_REMARK) != null && !result.get(ToDoItem.COLUMN_REMARK).trim().equalsIgnoreCase("") )
                toDoItem.setRemark( result.get(ToDoItem.COLUMN_REMARK) );
            if( result.get(ToDoItem.COLUMN_RECURRENCE_PERIOD) != null && !result.get(ToDoItem.COLUMN_RECURRENCE_PERIOD).trim().equalsIgnoreCase("") )
                toDoItem.setRecurrencePeriod(Integer.parseInt(result.get(ToDoItem.COLUMN_RECURRENCE_PERIOD)));
            if( result.get(ToDoItem.COLUMN_CATEGORY) != null && !result.get(ToDoItem.COLUMN_CATEGORY).trim().equalsIgnoreCase("") ) {
                toDoItem.setToDoCategory(ToDoCategory.getToDoCategory(Long.parseLong( result.get(ToDoItem.COLUMN_CATEGORY) )));
            }
            if( result.get(ToDoItem.COLUMN_DONE_INDICATOR) != null && !result.get(ToDoItem.COLUMN_DONE_INDICATOR).trim().equalsIgnoreCase("") ) {
                toDoItem.setDone(Boolean.getBoolean( result.get(ToDoItem.COLUMN_DONE_INDICATOR) ));
            }

            toDoItemList.add( toDoItem );
        }

        return toDoItemList;
    }

    public List<ToDoItem> loadToDoItems(@NonNull String selection , @NonNull String[] parameters )
    {
        checkNotNull( selection );
        checkNotNull( parameters );
        return getToDoItems( selection , parameters );
    }

    public ToDoItem getToDoItem(@NonNull long id )
    {
		List<ToDoItem> toDoItems = getToDoItems( ToDoItem.COLUMN_ID + "=?" , new String[]{String.valueOf(id)} );
		
		if( toDoItems != null && toDoItems.size() > 0 ) {
			return toDoItems.get(0);
		}
		
		return null;
    }

    public List<ToDoItem> getToDoItems()
    {
        return getToDoItems( ToDoItem.COLUMN_DONE_INDICATOR + "<=?" , new String[]{"0"} );
    }

    public List<ToDoItem> getDoneItems()
    {
		return getToDoItems( ToDoItem.COLUMN_DONE_INDICATOR + ">?" , new String[]{"0"} );
    }

    public long insertToDoItem(@NonNull ToDoItem toDoItem) {
        checkNotNull( toDoItem );
        GenericDao db = new GenericDao(ContextHolder.getContext());
        ContentValues values = new ContentValues();
        values.put(ToDoItem.COLUMN_NAME, toDoItem.getName());
        values.put(ToDoItem.COLUMN_DONE_INDICATOR, toDoItem.isDone());
        values.put(ToDoItem.COLUMN_DUE_TIMESTAMP, toDoItem.getDueTimestamp());
        values.put(ToDoItem.COLUMN_RECURRENCE_PERIOD, toDoItem.getRecurrencePeriod());
        values.put(ToDoItem.COLUMN_CATEGORY, toDoItem.getToDoCategory().getId() );
        long id = db.addContent(ToDoItem.TABLE_NAME, values);
        toDoItem.setId(id);
        return id;
    }

    public boolean updateToDoItem(@NonNull ToDoItem toDoItem) {
        checkNotNull( toDoItem );
        GenericDao db = new GenericDao(ContextHolder.getContext());
        ContentValues values = new ContentValues();
        values.put(ToDoItem.COLUMN_NAME, toDoItem.getName());
        values.put(ToDoItem.COLUMN_DONE_INDICATOR, toDoItem.isDone());
        values.put(ToDoItem.COLUMN_DUE_TIMESTAMP, toDoItem.getDueTimestamp());
        values.put(ToDoItem.COLUMN_RECURRENCE_PERIOD, toDoItem.getRecurrencePeriod());
        values.put(ToDoItem.COLUMN_CATEGORY, toDoItem.getToDoCategory().getId() );
        return db.updateContent( ToDoItem.TABLE_NAME , values , ToDoItem.COLUMN_ID + " = ?" , new String[]{String.valueOf(toDoItem.getId())} );
    }

}
