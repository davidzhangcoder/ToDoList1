package com.todolist.data.source;

import com.todolist.data.model.ToDoCategory;
import com.todolist.data.model.ToDoItem;
import com.todolist.data.source.local.ToDoItemLocalDataSource;
import com.todolist.data.source.remote.ToDoItemRemoteDataReSource;

import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;

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

    public synchronized static ToDoItemRepository getInstance(ToDoItemLocalDataSource localDataSource ,
                                                 ToDoItemRemoteDataReSource remoteDataReSource) {
        if (INSTANCE == null) {
            INSTANCE = new ToDoItemRepository(localDataSource , remoteDataReSource);
        }
        return INSTANCE;
    }

    public void getToDoItemsForAlarm( long from , long to , long isDone , long recurrencePeriod , ToDoItemDataSource.LoadToDoItemsCallBack callBack ) {
        mLocalDataSource.getToDoItemsForAlarm( from , to , isDone , recurrencePeriod , callBack );
    }

    @Override
    public void loadToDoItems( @NonNull long categoryID , @NonNull LoadToDoItemsCallBack callBack) {
        checkNotNull( categoryID );
        checkNotNull( callBack );
        mLocalDataSource.loadToDoItems(categoryID , new LoadToDoItemsCallBack() {
            @Override
            public void onToDoItemsLoaded(List<ToDoItem> toDos) {
                callBack.onToDoItemsLoaded( toDos );
            }

            @Override
            public void onDataNotAvailable() {
                callBack.onDataNotAvailable();
                //ToDo
                //loadToDoItemsFromRemoteDataSource( categoryID , callBack );
            }
        });
    }

    private void loadToDoItemsFromRemoteDataSource(@NonNull long categoryID , @NonNull LoadToDoItemsCallBack callBack)
    {
        checkNotNull( categoryID );
        checkNotNull( callBack );
        mRemoteDataSource.loadToDoItems(categoryID , new LoadToDoItemsCallBack() {
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
    public void loadDoneItems(long categoryID , LoadToDoItemsCallBack callBack) {
        mLocalDataSource.loadDoneItems(categoryID , new LoadToDoItemsCallBack() {
            @Override
            public void onToDoItemsLoaded(List<ToDoItem> toDos) {
                callBack.onToDoItemsLoaded( toDos );
            }

            @Override
            public void onDataNotAvailable() {
                callBack.onDataNotAvailable();
                //ToDo
                //loadToDoItemsFromRemoteDataSource( categoryID , callBack );
            }
        });
    }

    @Override
    public void getToDoItem(long toDoItemID, GenericToDoCallBack callBack) {
        checkNotNull( toDoItemID );
        checkNotNull( callBack );
        mLocalDataSource.getToDoItem(toDoItemID , new GenericToDoCallBack() {
            @Override
            public void onCompleted(ToDoItem toDoItem) {
                callBack.onCompleted( toDoItem );
            }

            @Override
            public void onError() {
                //ToDo
                //loadToDoItemsFromRemoteDataSource( categoryID , callBack );
            }
        });
    }

    @Override
    public void saveToDo(@NonNull ToDoItem toDoItem , @NonNull GenericToDoCallBack callBack) {
        checkNotNull( toDoItem );
        mLocalDataSource.saveToDo(toDoItem , new GenericToDoCallBack() {
            @Override
            public void onCompleted(ToDoItem toDoItem) {
                callBack.onCompleted( toDoItem );
            }

            @Override
            public void onError() {
                //ToDo
                //loadToDoItemsFromRemoteDataSource( categoryID , callBack );
            }
        });
    }

    @Override
    public void updateToDo(@NonNull ToDoItem toDoItem , @NonNull GenericToDoCallBack callBack) {
        mLocalDataSource.updateToDo(toDoItem, new GenericToDoCallBack() {
            @Override
            public void onCompleted(ToDoItem toDo) {
                callBack.onCompleted( toDoItem );
            }

            @Override
            public void onError() {
                callBack.onError();
            }
        });
    }

    @Override
    public void completeToDo(@NonNull ToDoItem toDoItem , @NonNull GenericToDoCallBack callBack) {
        mLocalDataSource.completeToDo(toDoItem, new GenericToDoCallBack() {
            @Override
            public void onCompleted(ToDoItem toDo) {
                callBack.onCompleted( toDoItem );
            }

            @Override
            public void onError() {
                callBack.onError();
            }
        });
    }

    @Override
    public void reverseCompletedToDo(@NonNull ToDoItem toDoItem , @NonNull GenericToDoCallBack callBack) {
        mLocalDataSource.reverseCompletedToDo(toDoItem, new GenericToDoCallBack() {
            @Override
            public void onCompleted(ToDoItem toDo) {
                callBack.onCompleted( toDoItem );
            }

            @Override
            public void onError() {
                callBack.onError();
            }
        });
    }

    public void loadToDoCategorys(LoadToDoCategorysCallBack callBack) {
        mLocalDataSource.loadToDoCategorys(new LoadToDoCategorysCallBack() {
            @Override
            public void onToDoCategorysLoaded(List<ToDoCategory> toDoCategorys) {
                callBack.onToDoCategorysLoaded(toDoCategorys);
            }

            @Override
            public void onDataNotAvailable() {
                //ToDo
            }
        });
    }

    @Override
    public void saveToDoCategory(@NonNull ToDoCategory toDoCategory , @NonNull GenericToDoCategoryCallBack callBack) {
        checkNotNull( toDoCategory );
        mLocalDataSource.saveToDoCategory(toDoCategory , new GenericToDoCategoryCallBack() {
            @Override
            public void onCompleted(ToDoCategory toDoCategory) {
                callBack.onCompleted( toDoCategory );
            }

            @Override
            public void onError() {
                //ToDo
                //loadToDoItemsFromRemoteDataSource( categoryID , callBack );
            }
        });
    }
}
