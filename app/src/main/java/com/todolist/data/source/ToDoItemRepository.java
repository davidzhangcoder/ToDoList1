package com.todolist.data.source;

import android.support.annotation.NonNull;

import com.todolist.data.source.local.ToDoItemLocalDataSource;
import com.todolist.data.source.remote.ToDoItemRemoteDataReSource;
import com.todolist.model.ToDoItem;

import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

public class ToDoItemRepository implements ToDoItemDataSource
{
    private static ToDoItemRepository INSTANCE;


    private ToDoItemLocalDataSource mLocalDataSource;

    private ToDoItemRemoteDataReSource mRemoteDataSource;

    Map<String, ToDoItem> mCachedTasks;

    boolean mCacheIsDirty = false;


    private ToDoItemRepository(@NonNull ToDoItemLocalDataSource localDataSource , @NonNull ToDoItemRemoteDataReSource remoteDataReSource) {
        mLocalDataSource = checkNotNull(localDataSource);
        mRemoteDataSource = checkNotNull(remoteDataReSource);
    }

    public static ToDoItemRepository getInstance(ToDoItemLocalDataSource localDataSource ,
                                                 ToDoItemRemoteDataReSource remoteDataReSource) {
        if (INSTANCE == null) {
            INSTANCE = new ToDoItemRepository(localDataSource , remoteDataReSource);
        }
        return INSTANCE;
    }

    @Override
    public void loadToDoItems(LoadToDoItemsCallBack callBack) {
        checkNotNull( callBack );
        mLocalDataSource.loadToDoItems(new LoadToDoItemsCallBack() {
            @Override
            public void onToDoItemsLoaded(List<ToDoItem> toDos) {
                callBack.onToDoItemsLoaded( toDos );
            }

            @Override
            public void onDataNotAvailable() {
                loadToDoItemsFromRemoteDataSource( callBack );
            }
        });
    }

    private void loadToDoItemsFromRemoteDataSource(LoadToDoItemsCallBack callBack)
    {
        mRemoteDataSource.loadToDoItems(new LoadToDoItemsCallBack() {
            @Override
            public void onToDoItemsLoaded(List<ToDoItem> toDos) {
                //Because Remote DataSource API not implemented
                callBack.onDataNotAvailable();
            }

            @Override
            public void onDataNotAvailable() {
                callBack.onDataNotAvailable();
            }
        });
    }

    @Override
    public void getToDoItem(long toDoItemID, GetToDoCallBack callBack) {

    }

    @Override
    public void saveToDo() {

    }

    @Override
    public void completeToDo() {

    }

    @Override
    public void notcompleteToDo() {

    }
}
