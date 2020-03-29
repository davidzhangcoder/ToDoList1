package com.todolist.data.source.local;

import androidx.annotation.NonNull;

import com.todolist.data.source.ToDoItemDataSource;
import com.todolist.db.ToDoItemDao;
import com.todolist.model.ToDoCategory;
import com.todolist.model.ToDoItem;
import com.todolist.util.AppExecutors;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class ToDoItemLocalDataSource implements ToDoItemDataSource {

    private static volatile ToDoItemLocalDataSource INSTANCE;

    private AppExecutors mAppExecutors;

    private ToDoItemDao mToDoItemDao;

    // Prevent direct instantiation.
    protected ToDoItemLocalDataSource(@NonNull AppExecutors appExecutors,
                                 @NonNull ToDoItemDao toDoItemDao) {
        mAppExecutors = appExecutors;
        mToDoItemDao = toDoItemDao;
    }

    public static ToDoItemLocalDataSource getInstance(@NonNull AppExecutors appExecutors,
                                                   @NonNull ToDoItemDao toDoItemDao) {
        if (INSTANCE == null) {
            synchronized (ToDoItemLocalDataSource.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ToDoItemLocalDataSource(appExecutors, toDoItemDao);
                }
            }
        }
        return INSTANCE;
    }


    @Override
    public void loadToDoItems(@NonNull long categoryID , @NonNull LoadToDoItemsCallBack callBack) {
        checkNotNull( categoryID );
		checkNotNull( callBack );
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final List<ToDoItem> toDoItems = new ArrayList<ToDoItem>();
                if( categoryID == ToDoCategory.CATEGORY_ALL_ID )
                    toDoItems.addAll( mToDoItemDao.loadToDoItems( ToDoItem.COLUMN_DONE_INDICATOR + "<=?" , new String[]{"0"} ) );
                else {
                    String selection = ToDoItem.COLUMN_DONE_INDICATOR + "<=? and " + ToDoItem.COLUMN_CATEGORY + " == ?" ;
                    String[] parameter = new String[]{ "0" , categoryID+"" };
                    toDoItems.addAll( mToDoItemDao.loadToDoItems( selection , parameter ) );
                }
                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (toDoItems.isEmpty()) {
                            // This will be called if the table is new or just empty.
                            callBack.onDataNotAvailable();
                        } else {
                            callBack.onToDoItemsLoaded(toDoItems);
                        }
                    }
                });
            }
        };

        mAppExecutors.diskIO().execute(runnable);
    }

    @Override
    public void loadDoneItems( long categoryID , LoadToDoItemsCallBack callBack ) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final List<ToDoItem> toDoItems = new ArrayList<ToDoItem>();
                if( categoryID == ToDoCategory.CATEGORY_ALL_ID )
                    toDoItems.addAll( mToDoItemDao.loadToDoItems( ToDoItem.COLUMN_DONE_INDICATOR + ">?" , new String[]{"0"} ) );
                else
                    toDoItems.addAll( mToDoItemDao.loadToDoItems( ToDoItem.COLUMN_DONE_INDICATOR + ">? and " + ToDoItem.COLUMN_CATEGORY + " == ? " , new String[]{"0" , categoryID+""} ) );
                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (toDoItems.isEmpty()) {
                            // This will be called if the table is new or just empty.
                            callBack.onDataNotAvailable();
                        } else {
                            callBack.onToDoItemsLoaded(toDoItems);
                        }
                    }
                });
            }
        };

        mAppExecutors.diskIO().execute(runnable);
    }

    @Override
    public void getToDoItem(@NonNull long toDoItemID, @NonNull GenericToDoCallBack callBack) {
		checkNotNull( toDoItemID );
		checkNotNull( callBack );
		
		Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final ToDoItem toDoItem = mToDoItemDao.getToDoItem( toDoItemID );
                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (toDoItem != null ) {
                            // This will be called if the table is new or just empty.
                            callBack.onCompleted(toDoItem);
                        } else {
                            callBack.onError();
                        }
                    }
                });
            }
        };

        mAppExecutors.diskIO().execute(runnable);
    }

    @Override
    public void saveToDo(@NonNull ToDoItem toDoItem , @NonNull GenericToDoCallBack callBack) {
        checkNotNull( toDoItem );
        checkNotNull( callBack );

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                 long id = mToDoItemDao.insertToDoItem(toDoItem);
                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if ( id != -1 ) {
                            callBack.onCompleted(toDoItem);
                        } else {
                            callBack.onError();
                        }
                    }
                });
            }
        };

        mAppExecutors.diskIO().execute(runnable);
    }

    @Override
    public void updateToDo(@NonNull ToDoItem toDoItem , @NonNull GenericToDoCallBack callBack) {
        checkNotNull( toDoItem );
        checkNotNull( callBack );

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                boolean done = mToDoItemDao.updateToDoItem(toDoItem);
                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if ( done ) {
                            callBack.onCompleted(toDoItem);
                        } else {
                            callBack.onError();
                        }
                    }
                });
            }
        };

        mAppExecutors.diskIO().execute(runnable);
    }

    @Override
    public void completeToDo(@NonNull ToDoItem toDoItem , @NonNull GenericToDoCallBack callBack) {
        checkNotNull( toDoItem );
        checkNotNull( callBack );

        toDoItem.setDone( true );
        updateToDo( toDoItem , callBack );
    }

    @Override
    public void reverseCompletedToDo(@NonNull ToDoItem toDoItem , @NonNull GenericToDoCallBack callBack) {
        checkNotNull( toDoItem );
        checkNotNull( callBack );

        toDoItem.setDone( false );
        updateToDo( toDoItem , callBack );

    }

    @Override
    public void loadToDoCategorys(@NonNull LoadToDoCategorysCallBack callBack) {
        checkNotNull( callBack );
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final List<ToDoCategory> toDoCategorys = new ArrayList<ToDoCategory>();
                toDoCategorys.addAll( mToDoItemDao.loadToDoCategorys() );
                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (!toDoCategorys.isEmpty()) {
                            callBack.onToDoCategorysLoaded(toDoCategorys);
                        } else {
                            // This will be called if the table is new or just empty.
                            callBack.onDataNotAvailable();
                        }
                    }
                });
            }
        };

        mAppExecutors.diskIO().execute(runnable);
    }

    @Override
    public void saveToDoCategory(@NonNull ToDoCategory toDoCategory , @NonNull GenericToDoCategoryCallBack callBack) {
        checkNotNull( toDoCategory );
        checkNotNull( callBack );

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                long id = mToDoItemDao.insertToDoCategory(toDoCategory);
                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if ( id != -1 ) {
                            callBack.onCompleted(toDoCategory);
                        } else {
                            callBack.onError();
                        }
                    }
                });
            }
        };

        mAppExecutors.diskIO().execute(runnable);
    }
}
