package com.todolist.data.source.remote;

import com.todolist.data.model.ToDoCategory;
import com.todolist.data.model.ToDoItem;
import com.todolist.data.source.ToDoItemDataSource;
import com.todolist.data.source.local.ToDoItemLocalDataSource;
import com.todolist.util.AppExecutors;

import androidx.annotation.NonNull;

public class ToDoItemRemoteDataReSource implements ToDoItemDataSource
{
    private static volatile ToDoItemRemoteDataReSource INSTANCE;

    private AppExecutors mAppExecutors;

    // Prevent direct instantiation.
    private ToDoItemRemoteDataReSource(@NonNull AppExecutors appExecutors) {
        mAppExecutors = appExecutors;
    }

    public static ToDoItemRemoteDataReSource getInstance(@NonNull AppExecutors appExecutors) {
        if (INSTANCE == null) {
            synchronized (ToDoItemLocalDataSource.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ToDoItemRemoteDataReSource(appExecutors);
                }
            }
        }
        return INSTANCE;
    }



    @Override
    public void loadToDoItems(@NonNull long categoryID , @NonNull LoadToDoItemsCallBack callBack) {

    }

    @Override
    public void getToDoItem(long toDoItemID, GenericToDoCallBack callBack) {

    }

    @Override
    public void saveToDo(@NonNull ToDoItem toDoItem , @NonNull GenericToDoCallBack callBack) {

    }

    public void updateToDo(@NonNull ToDoItem toDoItem , @NonNull GenericToDoCallBack callBack) {

    }

    @Override
    public void completeToDo(@NonNull ToDoItem toDoItem , @NonNull GenericToDoCallBack callBack) {

    }

    @Override
    public void reverseCompletedToDo(@NonNull ToDoItem toDoItem , @NonNull GenericToDoCallBack callBack) {

    }


    public void loadDoneItems(@NonNull long categoryID , @NonNull LoadToDoItemsCallBack callBack) {

    }


    public void loadToDoCategorys(LoadToDoCategorysCallBack callBack) {

    }

    @Override
    public void saveToDoCategory(@NonNull ToDoCategory toDoCategory , @NonNull GenericToDoCategoryCallBack callBack) {

    }
}
