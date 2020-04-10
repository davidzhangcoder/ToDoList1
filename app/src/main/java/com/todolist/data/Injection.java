package com.todolist.data;

import android.content.Context;

import com.todolist.data.model.ToDoItemDao;
import com.todolist.data.model.ToDoItemDatabase;
import com.todolist.data.source.ToDoItemRepository;
import com.todolist.data.source.local.ToDoItemLocalDataSource;
import com.todolist.data.source.remote.ToDoItemRemoteDataReSource;
import com.todolist.util.AppExecutors;

public class Injection {

    public static ToDoItemRepository provideToDoItemRepository( Context context )
    {
        ToDoItemDao toDoItemDao = ToDoItemDatabase.Companion.getInstance( context ).getToDoItemDao();
        return ToDoItemRepository.getInstance( ToDoItemLocalDataSource.getInstance( new AppExecutors() , toDoItemDao )
                , ToDoItemRemoteDataReSource.getInstance( new AppExecutors() ));
    }
}
