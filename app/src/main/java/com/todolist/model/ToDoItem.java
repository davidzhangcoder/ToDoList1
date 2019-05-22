package com.todolist.model;

import com.todolist.context.ContextHolder;
import com.todolist.db.GenericDao;

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
    public static final String COLUMN_RECURRENCE_PERIOD = "recurrence_period";
    public static final String COLUMN_CATEGORY = "category_id";

    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_NAME + " TEXT,"
                    + COLUMN_DUE_TIMESTAMP + " DATETIME, "
                    + COLUMN_REMIND_TIMESTAMP + " DATETIME ,"
                    + COLUMN_CHILD_TASK + " TEXT , "
                    + COLUMN_REMARK + " TEXT , "
                    + COLUMN_RECURRENCE_PERIOD + " INTEGER , "
                    + COLUMN_DONE_INDICATOR + " BOOLEAN , "
                    + COLUMN_CATEGORY + " INTEGER "
                    + " FOREIGN KEY( " + COLUMN_CATEGORY + ") REFERENCES " + ToDoCategory.TABLE_NAME + " ( " + ToDoCategory.COLUMN_ID + " ) "
                    + ")";

    private String name;

    private long id;
    private long dueTimestamp;
    private long remindTimestamp;
    private String childTask;
    private String remark;
    private boolean isDone;
    private int recurrencePeriod;
    private ToDoCategory toDoCategory;

    private Calendar dueDate;
    private Calendar remindDate;


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

    public int getRecurrencePeriod() {
        return recurrencePeriod;
    }

    public void setRecurrencePeriod(int recurrencePeriod) {
        this.recurrencePeriod = recurrencePeriod;
    }

    public ToDoCategory getToDoCategory() {
        return toDoCategory;
    }

    public void setToDoCategory(ToDoCategory toDoCategory) {
        this.toDoCategory = toDoCategory;
    }
}
