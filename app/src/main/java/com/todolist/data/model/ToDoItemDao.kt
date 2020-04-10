package com.todolist.data.model

import androidx.room.*
import java.util.*

@Dao
abstract class ToDoItemDao {

    @Transaction
    open fun getToDoItemsForAlarm( from: Long , to: Long , isDone: Long , recurrencePeriod: Long ) : List<ToDoItem> {
        var toDoItemList: List<ToDoItem> = getToDoItems( from , to , isDone , recurrencePeriod );
        toDoItemList.forEach {
            it.toDoCategory = getToDoCategorybyID( it.toDoCategoryID );
        }
        return toDoItemList;
    }

    @Transaction
    open fun getToDoItemViewByID( id: Long ) : ToDoItem {
        val toDoItemView : ToDoItemView = getToDoItemByID( id );
        toDoItemView.let {
            it.toDoItem.toDoCategory = getToDoCategorybyID( it.toDoItem.toDoCategoryID );
            it.toDoItem.toDoImageList.clear();
            it.toDoItem.toDoImageList.addAll( it.toDoImageList );
        };
        return toDoItemView.toDoItem;
    }

    @Transaction
    open fun getToDoItemViewsByToDoCategory( isDone : Long , categoryIDs : List<Long> ) : List<ToDoItem>?{
        val toDoItemViewList : List<ToDoItemView> = getToDoItemsByToDoCategory( isDone , categoryIDs );
        val toDoItemList = ArrayList<ToDoItem>();
        toDoItemViewList.forEach {
            it.toDoItem.toDoCategory = getToDoCategorybyID( it.toDoItem.toDoCategoryID );
            it.toDoItem.toDoImageList.clear();
            it.toDoItem.toDoImageList.addAll( it.toDoImageList );
            toDoItemList.add( it.toDoItem );
        }
        return toDoItemList;
    }

    @Transaction
    open fun getToDoItemViews( isDone : Long ) : List<ToDoItem >{
        val toDoItemViewList : List<ToDoItemView> = getToDoItems( isDone );
        val toDoItemList = ArrayList<ToDoItem>();
        toDoItemViewList.forEach {
            it.toDoItem.toDoCategory = getToDoCategorybyID( it.toDoItem.toDoCategoryID );
            it.toDoItem.toDoImageList.clear();
            it.toDoItem.toDoImageList.addAll( it.toDoImageList );
            toDoItemList.add( it.toDoItem );
        }
        return toDoItemList;
    }


    @Query(" select * from todo_category ")
    abstract fun getToDoCategory() : List<ToDoCategory>;

    @Insert(onConflict = OnConflictStrategy.ABORT)
    abstract fun insertToDoItem( toDoItem: ToDoItem ) : Long;

    @Update
    abstract fun updateToDoItem( toDoItem: ToDoItem ) : Int;

    @Insert(onConflict = OnConflictStrategy.ABORT)
    abstract fun insertToDoCategory( toDoCategory: ToDoCategory ) : Long;


    @Transaction
    @Query(" select * from TODO_ITEM where done_indicator = :isDone order by due_timestamp ASC ")
    protected abstract fun getToDoItems( isDone : Long ) : List<ToDoItemView >;

    @Query(" select * from TODO_ITEM where ( ( due_timestamp between :from and :to )  or ( due_timestamp < :from and  recurrence_period != :recurrencePeriod ) ) and  done_indicator == :isDone order by due_timestamp ASC ")
    protected abstract fun getToDoItems( from: Long , to: Long , isDone: Long , recurrencePeriod: Long ) : List<ToDoItem>;

    @Transaction
    @Query(" select * from TODO_ITEM where done_indicator = :isDone and category_id in ( :categoryIDs ) order by due_timestamp ASC ")
    protected abstract fun getToDoItemsByToDoCategory( isDone : Long , categoryIDs : List<Long> ) : List<ToDoItemView>;

    @Query(" select * from todo_category where id = :id ")
    protected abstract fun getToDoCategorybyID( id : Long ) : ToDoCategory;

    @Transaction
    @Query(" select * from TODO_ITEM where id = :id ")
    protected abstract fun getToDoItemByID( id : Long ) : ToDoItemView;

}