package com.todolist.db;

import android.content.ContentValues;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.todolist.context.ContextHolder;
import com.todolist.model.ToDoCategory;
import com.todolist.model.ToDoImage;
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
            long toDoItemID = Long.parseLong(result.get(ToDoItem.COLUMN_ID));
            toDoItem.setId( toDoItemID );
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
                toDoItem.setDone( result.get(ToDoItem.COLUMN_DONE_INDICATOR).equalsIgnoreCase("1") );
            }

            String toDoImageSelection = ToDoImage.COLUMN_TODOITEM + "=?";
            String[] toDoImageParameter = new String[]{ String.valueOf( toDoItemID ) };
            List<Map<String, String>> toDoImageResultList = db.getListData( ToDoImage.TABLE_NAME , toDoImageSelection , toDoImageParameter ,  ToDoImage.COLUMN_ID + " ASC " );
            if( toDoImageResultList != null && toDoImageResultList.size() > 0 ) {
                for(Iterator<Map<String, String>> toDoImageResultIt = toDoImageResultList.iterator(); toDoImageResultIt.hasNext() ; ) {
                    Map<String, String> toDoImageResult = toDoImageResultIt.next();
                    ToDoImage toDoImage = new ToDoImage();

                    long toDoImageId = Long.parseLong( toDoImageResult.get( ToDoImage.COLUMN_ID ) );
                    String urlString = toDoImageResult.get( ToDoImage.COLUMN_URL );
                    long toDoItemIDFromToDoImage = Long.parseLong( toDoImageResult.get( ToDoImage.COLUMN_TODOITEM ) );

                    toDoImage.setId( toDoImageId );
                    toDoImage.setToDoItemId( toDoItemIDFromToDoImage );
                    Uri uri = Uri.parse( urlString );
                    toDoImage.setUri( uri );
                    toDoImage.setUriString( urlString );
                    toDoItem.getToDoImageList().add( toDoImage );
                }
            }

            toDoItemList.add( toDoItem );
        }

        return toDoItemList;
    }

    private List<ToDoCategory> getToDoCategorys(@NonNull String selection , @NonNull String[] parameters) {
        List<ToDoCategory> toDoCategoryList = new ArrayList<ToDoCategory>();

        GenericDao db = new GenericDao(ContextHolder.getContext());

        List<Map<String, String>> resultList = db.getListData( ToDoCategory.TABLE_NAME , selection , parameters ,  ToDoCategory.COLUMN_ID + " ASC " );

        for(Iterator<Map<String, String>> it = resultList.iterator(); it.hasNext() ; ) {
            Map<String, String> result = it.next();
            ToDoCategory toDoCategory = new ToDoCategory();
            toDoCategory.setId(Long.parseLong(result.get(ToDoCategory.COLUMN_ID)));
            toDoCategory.setName(result.get(ToDoCategory.COLUMN_NAME));

            toDoCategoryList.add( toDoCategory );
        }

        return toDoCategoryList;
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

        for( ToDoImage toDoImage : toDoItem.getToDoImageList() ) {
            ContentValues valuesToDoImage = new ContentValues();
            valuesToDoImage.put(ToDoImage.COLUMN_URL, toDoImage.getUri().toString());
            valuesToDoImage.put(ToDoImage.COLUMN_TODOITEM, id);
            db.addContent(ToDoImage.TABLE_NAME, valuesToDoImage);
        }

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
        boolean retBollean = db.updateContent( ToDoItem.TABLE_NAME , values , ToDoItem.COLUMN_ID + " = ?" , new String[]{String.valueOf(toDoItem.getId())} );

        if( toDoItem.getToDoImageList() != null && toDoItem.getToDoImageList().size() > 0 ) {
            db.deleteContent( ToDoImage.TABLE_NAME , ToDoImage.COLUMN_TODOITEM + " = ? ", new String[]{ String.valueOf(toDoItem.getId()) } );

            for( ToDoImage toDoImage : toDoItem.getToDoImageList() ) {
                ContentValues valuesToDoImage = new ContentValues();
                valuesToDoImage.put(ToDoImage.COLUMN_URL, toDoImage.getUri().toString());
                valuesToDoImage.put(ToDoImage.COLUMN_TODOITEM, toDoItem.getId());
                db.addContent(ToDoImage.TABLE_NAME, valuesToDoImage);
            }
        }

        return retBollean;
    }

    public List<ToDoCategory> loadToDoCategorys() {
        return getToDoCategorys(null,null);
    }

    public long insertToDoCategory(@NonNull ToDoCategory toDoCategory) {
        checkNotNull( toDoCategory );
        GenericDao db = new GenericDao(ContextHolder.getContext());
        ContentValues values = new ContentValues();
        values.put(ToDoItem.COLUMN_NAME, toDoCategory.getName());
        long id = db.addContent(ToDoCategory.TABLE_NAME, values);
        toDoCategory.setId(id);
        return id;
    }

}
