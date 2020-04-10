package com.todolist.data.source.local;

import com.todolist.data.model.ToDoCategory;
import com.todolist.data.model.ToDoItem;
import com.todolist.data.model.ToDoItemDao;
import com.todolist.data.source.ToDoItemDataSource;
import com.todolist.util.AppExecutors;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

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


    public void getToDoItemsForAlarm( long from , long to , long isDone , long recurrencePeriod , ToDoItemDataSource.LoadToDoItemsCallBack callBack ) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                final List<ToDoItem> toDoItems = new ArrayList<ToDoItem>();

                toDoItems.addAll( mToDoItemDao.getToDoItemsForAlarm( from , to , isDone , recurrencePeriod ) );

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
    public void loadToDoItems(@NonNull long categoryID , @NonNull LoadToDoItemsCallBack callBack) {
        checkNotNull( categoryID );
		checkNotNull( callBack );
        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                final List<ToDoItem> toDoItems = new ArrayList<ToDoItem>();
                if( categoryID == ToDoCategory.Companion.getCATEGORY_ALL_ID() ) {
                    toDoItems.addAll(mToDoItemDao.getToDoItemViews(0 ));
                }
                else {
                    List<Long> temp = new ArrayList<>();
                    temp.add( categoryID );
                    toDoItems.addAll( mToDoItemDao.getToDoItemViewsByToDoCategory( 0 , temp ) );
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
                if( categoryID == ToDoCategory.Companion.getCATEGORY_ALL_ID() )
                    toDoItems.addAll( mToDoItemDao.getToDoItemViews( 1 ) );
                else {
                    List<Long> temp = new ArrayList<>();
                    temp.add( categoryID );
                    toDoItems.addAll(mToDoItemDao.getToDoItemViewsByToDoCategory(1 , temp ) );
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
    public void getToDoItem(@NonNull long toDoItemID, @NonNull GenericToDoCallBack callBack) {
		checkNotNull( toDoItemID );
		checkNotNull( callBack );
		
		Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final ToDoItem toDoItem = mToDoItemDao.getToDoItemViewByID( toDoItemID );
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
                long done = mToDoItemDao.updateToDoItem(toDoItem);
                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if ( done != 0 ) {
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
                toDoCategorys.addAll( mToDoItemDao.getToDoCategory() );
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
//        checkNotNull( toDoCategory );
//        checkNotNull( callBack );
//
//        Runnable runnable = new Runnable() {
//            @Override
//            public void run() {
//                long id = mToDoItemDao.insertToDoCategory(toDoCategory);
//                mAppExecutors.mainThread().execute(new Runnable() {
//                    @Override
//                    public void run() {
//                        if ( id != -1 ) {
//                            callBack.onCompleted(toDoCategory);
//                        } else {
//                            callBack.onError();
//                        }
//                    }
//                });
//            }
//        };
//
//        mAppExecutors.diskIO().execute(runnable);
    }
}
