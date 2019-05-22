package com.todolist.data;

import com.todolist.data.source.ToDoItemRepository;
import com.todolist.data.source.local.ToDoItemLocalDataSource;
import com.todolist.data.source.remote.ToDoItemRemoteDataReSource;
import com.todolist.db.ToDoItemDao;
import com.todolist.util.AppExecutors;

public class Injection {

    public static ToDoItemRepository provideToDoItemRepository()
    {
        return ToDoItemRepository.getInstance( ToDoItemLocalDataSource.getInstance( new AppExecutors() , new ToDoItemDao() )
                , ToDoItemRemoteDataReSource.getInstance( new AppExecutors() ));
    }
}
