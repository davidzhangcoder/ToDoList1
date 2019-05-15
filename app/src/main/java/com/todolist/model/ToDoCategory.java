package com.todolist.model;

import com.todolist.context.ContextHolder;
import com.todolist.db.GenericDao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ToDoCategory implements Serializable , IToDoItemType , IToDoCategory
{
    public enum WORKFLOW { LIST , EDIT };

    private WORKFLOW workflow;


    public static final int CATEGORY_DEFAULT_ID = 0;

    public static final int CATEGORY_ADD_NEW_ID = -1;
    public static final String CATEGORY_ADD_NEW_NAME = "Add New Category";

    public static final int CATEGORY_ALL_ID = -2;
    public static final String CATEGORY_ALL_NAME = "All Lists";



    public static final String TABLE_NAME = "todo_category";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_NAME + " TEXT"
                    + ")";

    private boolean selected = false;

    private long id;

    private String name;

    public ToDoCategory() {
    }

    public ToDoCategory(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public WORKFLOW getWorkflow() {
        return workflow;
    }

    public void setWorkflow(WORKFLOW workflow) {
        this.workflow = workflow;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public static ToDoCategory getToDoCategory(long id )
    {
        GenericDao db = new GenericDao(ContextHolder.getContext());

        Map<String, String> result = db.getRowData( TABLE_NAME , ToDoItem.COLUMN_ID + "=?" , new String[]{String.valueOf(id)}  );

        ToDoCategory toDoCategory = new ToDoCategory();
        toDoCategory.setId( Long.parseLong(result.get(COLUMN_ID)) );
        toDoCategory.setName( result.get(COLUMN_NAME) );

        return toDoCategory;
    }

    public static List<ToDoCategory> getToDoCategorys()
    {
        List<ToDoCategory> toDoCategoryList = new ArrayList<ToDoCategory>();
        GenericDao db = new GenericDao(ContextHolder.getContext());

        List<Map<String, String>> resultList = db.getListData( TABLE_NAME , null , null , COLUMN_ID + " ASC " );

        for(Iterator<Map<String, String>> it = resultList.iterator(); it.hasNext() ; ) {
            Map<String, String> result = it.next();
            ToDoCategory toDoCategory = new ToDoCategory();
            toDoCategory.setId(Long.parseLong(result.get(COLUMN_ID)));
            toDoCategory.setName(result.get(COLUMN_NAME));

            toDoCategoryList.add( toDoCategory );
        }

        return toDoCategoryList;
    }

    @Override
    public int type(ITypeFactory typeFactory) {
        return typeFactory.getType( this );
    }

}
