package com.todolist.model;

import com.todolist.context.ContextHolder;
import com.todolist.db.ToDoItemDao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ToDoItem implements Serializable, IToDoItemType, IToDoItem
{

    public static final String TABLE_NAME = "todo_item";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_DUE_TIMESTAMP = "due_timestamp";
    public static final String COLUMN_REMIND_TIMESTAMP = "remind_timestamp";
    public static final String COLUMN_CHILD_TASK = "child_task";
    public static final String COLUMN_REMARK = "remark";
    public static final String COLUMN_DONE_INDICATOR = "done_indicator";

    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_NAME + " TEXT,"
                    + COLUMN_DUE_TIMESTAMP + " DATETIME, "
                    + COLUMN_REMIND_TIMESTAMP + " DATETIME ,"
                    + COLUMN_CHILD_TASK + " TEXT , "
                    + COLUMN_REMARK + " TEXT , "
                    + COLUMN_DONE_INDICATOR + " BOOLEAN "
                    + ")";


    private String name;

    private long id;
    private long dueTimestamp;
    private long remindTimestamp;
    private String childTask;
    private String remark;
    private boolean isDone;

    private Calendar dueDate;
    private Calendar remindDate;

    public static ToDoItem getToDoItem(long id )
    {
        ToDoItemDao db = new ToDoItemDao(ContextHolder.getContext());

        Map<String, String> result = db.getRowData( TABLE_NAME , ToDoItem.COLUMN_ID + "=?" , new String[]{String.valueOf(id)}  );

        ToDoItem toDoItem = new ToDoItem();
        toDoItem.setId( Long.parseLong(result.get(COLUMN_ID)) );
        toDoItem.setName( result.get(COLUMN_NAME) );
        if( result.get(COLUMN_DUE_TIMESTAMP) != null && !result.get(COLUMN_DUE_TIMESTAMP).trim().equalsIgnoreCase("") ) {
            toDoItem.setDueTimestamp(Long.parseLong(result.get(COLUMN_DUE_TIMESTAMP)));
            Calendar date = Calendar.getInstance();
            date.setTimeInMillis(toDoItem.getDueTimestamp());
            toDoItem.setDueDate( date );
        }
        if( result.get(COLUMN_REMIND_TIMESTAMP) != null && !result.get(COLUMN_REMIND_TIMESTAMP).trim().equalsIgnoreCase("") ) {
            toDoItem.setRemindTimestamp(Long.parseLong(result.get(COLUMN_REMIND_TIMESTAMP)));
            Calendar date = Calendar.getInstance();
            date.setTimeInMillis(toDoItem.getRemindTimestamp());
            toDoItem.setRemindDate( date );
        }
        if( result.get(COLUMN_CHILD_TASK) != null && !result.get(COLUMN_CHILD_TASK).trim().equalsIgnoreCase("") )
            toDoItem.setChildTask( result.get(COLUMN_CHILD_TASK) );
        if( result.get(COLUMN_REMARK) != null && !result.get(COLUMN_REMARK).trim().equalsIgnoreCase("") )
            toDoItem.setRemark( result.get(COLUMN_REMARK) );


        return toDoItem;
    }

    public static List<ToDoItem> getToDoItems()
    {
        List<ToDoItem> toDoItemList = new ArrayList<ToDoItem>();
        ToDoItemDao db = new ToDoItemDao(ContextHolder.getContext());

        List<Map<String, String>> resultList = db.getListData( TABLE_NAME , ToDoItem.COLUMN_DONE_INDICATOR + "<=?" , new String[]{"0"} , COLUMN_DUE_TIMESTAMP + " ASC " );

        for(Iterator<Map<String, String>> it = resultList.iterator(); it.hasNext() ; ) {
            Map<String, String> result = it.next();
            ToDoItem toDoItem = new ToDoItem();
            toDoItem.setId(Long.parseLong(result.get(COLUMN_ID)));
            toDoItem.setName(result.get(COLUMN_NAME));
            if( result.get(COLUMN_DUE_TIMESTAMP) != null && !result.get(COLUMN_DUE_TIMESTAMP).trim().equalsIgnoreCase("") ) {
                toDoItem.setDueTimestamp(Long.parseLong(result.get(COLUMN_DUE_TIMESTAMP)));
                Calendar date = Calendar.getInstance();
                date.setTimeInMillis(toDoItem.getDueTimestamp());
                toDoItem.setDueDate( date );
            }
            if( result.get(COLUMN_REMIND_TIMESTAMP) != null && !result.get(COLUMN_REMIND_TIMESTAMP).trim().equalsIgnoreCase("") ) {
                toDoItem.setRemindTimestamp(Long.parseLong(result.get(COLUMN_REMIND_TIMESTAMP)));
                Calendar date = Calendar.getInstance();
                date.setTimeInMillis(toDoItem.getRemindTimestamp());
                toDoItem.setRemindDate( date );
            }
            if( result.get(COLUMN_CHILD_TASK) != null && !result.get(COLUMN_CHILD_TASK).trim().equalsIgnoreCase("") )
                toDoItem.setChildTask( result.get(COLUMN_CHILD_TASK) );
            if( result.get(COLUMN_REMARK) != null && !result.get(COLUMN_REMARK).trim().equalsIgnoreCase("") )
                toDoItem.setRemark( result.get(COLUMN_REMARK) );

            toDoItemList.add( toDoItem );
        }

        return toDoItemList;
    }

    public static List<ToDoItem> getDoneItems()
    {
        List<ToDoItem> doneItemList = new ArrayList<ToDoItem>();
        ToDoItemDao db = new ToDoItemDao(ContextHolder.getContext());

        List<Map<String, String>> resultList = db.getListData( TABLE_NAME , ToDoItem.COLUMN_DONE_INDICATOR + ">?" , new String[]{"0"} , null );

        for(Iterator<Map<String, String>> it = resultList.iterator(); it.hasNext() ; ) {
            Map<String, String> result = it.next();
            ToDoItem toDoItem = new ToDoItem();
            toDoItem.setId(Long.parseLong(result.get(COLUMN_ID)));
            toDoItem.setName(result.get(COLUMN_NAME));
            if( result.get(COLUMN_DUE_TIMESTAMP) != null && !result.get(COLUMN_DUE_TIMESTAMP).trim().equalsIgnoreCase("") )
                toDoItem.setDueTimestamp( Long.parseLong(result.get(COLUMN_DUE_TIMESTAMP)) );
            if( result.get(COLUMN_REMIND_TIMESTAMP) != null && !result.get(COLUMN_REMIND_TIMESTAMP).trim().equalsIgnoreCase("") )
                toDoItem.setRemindTimestamp( Long.parseLong(result.get(COLUMN_REMIND_TIMESTAMP)) );
            if( result.get(COLUMN_CHILD_TASK) != null && !result.get(COLUMN_CHILD_TASK).trim().equalsIgnoreCase("") )
                toDoItem.setChildTask( result.get(COLUMN_CHILD_TASK) );
            if( result.get(COLUMN_REMARK) != null && !result.get(COLUMN_REMARK).trim().equalsIgnoreCase("") )
                toDoItem.setRemark( result.get(COLUMN_REMARK) );

            doneItemList.add( toDoItem );
        }

        return doneItemList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getDueTimestamp() {
        return dueTimestamp;
    }

    public void setDueTimestamp(long dueTimestamp) {
        this.dueTimestamp = dueTimestamp;
    }

    public long getRemindTimestamp() {
        return remindTimestamp;
    }

    public void setRemindTimestamp(long remindTimestamp) {
        this.remindTimestamp = remindTimestamp;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    public String getChildTask() {
        return childTask;
    }

    public void setChildTask(String childTask) {
        this.childTask = childTask;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Calendar getDueDate() {
        return dueDate;
    }

    public void setDueDate(Calendar dueDate) {
        this.dueDate = dueDate;
    }

    public Calendar getRemindDate() {
        return remindDate;
    }

    public void setRemindDate(Calendar remindDate) {
        this.remindDate = remindDate;
    }

    @Override
    public int type(ITypeFactory typeFactory) {
        return typeFactory.getType( this );
    }
}
