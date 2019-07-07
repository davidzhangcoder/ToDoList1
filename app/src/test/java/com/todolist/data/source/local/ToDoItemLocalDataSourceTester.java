package com.todolist.data.source.local;

import com.todolist.data.source.GenericDataSource;
import com.todolist.data.source.ToDoItemDataSource;
import com.todolist.db.ToDoItemDao;
import com.todolist.model.ToDoCategory;
import com.todolist.model.ToDoItem;
import com.todolist.util.AppExecutors;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static junit.framework.TestCase.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ToDoItemLocalDataSourceTester {

//    @Captor
//    private ArgumentCaptor<ToDoItem> toDoItemArgumentCaptor;

//    @Captor
//    private ArgumentCaptor<String> stringArgumentCaptor;
//
//    @Captor
//    private ArgumentCaptor<String[]> stringArrayArgumentCaptor;

    Executor executor = new Executor() {
        @Override
        public void execute(Runnable command) {
            command.run();
        }
    };

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testUpdateToDo() {
        ToDoItem toDoItem = new ToDoItem();
        toDoItem.setId( 1 );

        // mock up class
        ToDoItemDao toDoItemDaoMocked = mock(ToDoItemDao.class);
        AppExecutors appExecutorsMocked = mock(AppExecutors.class);
        GenericDataSource.GenericToDoCallBack genericToDoCallBackMocked = mock(GenericDataSource.GenericToDoCallBack.class);

        // mock up behavior
        when( appExecutorsMocked.diskIO() ).thenReturn( executor );
        when( appExecutorsMocked.mainThread() ).thenReturn( executor );
        when( toDoItemDaoMocked.updateToDoItem( toDoItem )).thenReturn( true );

        ArgumentCaptor<ToDoItem> toDoItemArgumentCaptor = ArgumentCaptor.forClass(ToDoItem.class);

        // testing
        ToDoItemLocalDataSource toDoItemLocalDataSource = new ToDoItemLocalDataSource( appExecutorsMocked , toDoItemDaoMocked );

        toDoItemLocalDataSource.updateToDo( toDoItem , genericToDoCallBackMocked );

//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        // verifying
        verify( genericToDoCallBackMocked , times( 1 )).onCompleted( toDoItemArgumentCaptor.capture() );

        ToDoItem resultToDoItem = toDoItemArgumentCaptor.getValue();

        assertTrue( resultToDoItem != null );
        assertTrue( resultToDoItem.getId() == 1 );

        reset(toDoItemDaoMocked);
        reset(appExecutorsMocked);
        reset(genericToDoCallBackMocked);
    }

    @Test
    public void testLoadToDoItems() {

        List<ToDoItem> toDoItemList = new ArrayList<ToDoItem>();
        ToDoItem toDoItem = new ToDoItem();
        toDoItem.setId( 1 );
        toDoItemList.add( toDoItem );

        // mock up class
        ToDoItemDao toDoItemDaoMocked = mock(ToDoItemDao.class);
        AppExecutors appExecutorsMocked = mock(AppExecutors.class);
        ToDoItemDataSource.LoadToDoItemsCallBack loadToDoItemsCallBackMocked = mock(ToDoItemDataSource.LoadToDoItemsCallBack.class );

        // mock up behavior
        when( appExecutorsMocked.diskIO() ).thenReturn( executor );
        when( appExecutorsMocked.mainThread() ).thenReturn( executor );
        when( toDoItemDaoMocked.loadToDoItems( anyString() , any() ) ).thenReturn( toDoItemList );

        ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String[]> stringArrayArgumentCaptor = ArgumentCaptor.forClass(String[].class);

        // testing
        ToDoItemLocalDataSource toDoItemLocalDataSource = new ToDoItemLocalDataSource( appExecutorsMocked , toDoItemDaoMocked );
        toDoItemLocalDataSource.loadToDoItems( ToDoCategory.CATEGORY_ALL_ID , loadToDoItemsCallBackMocked );

        // verify
        verify( toDoItemDaoMocked , times( 1 )).loadToDoItems( stringArgumentCaptor.capture() , stringArrayArgumentCaptor.capture() );

        String selection = stringArgumentCaptor.getValue();
        assertTrue( (ToDoItem.COLUMN_DONE_INDICATOR + "<=?").equals( selection ) );

        String[] parameter = stringArrayArgumentCaptor.getValue();
        assertTrue( parameter != null && parameter.length == 1 && parameter[0].equals("0") );

        reset(toDoItemDaoMocked);
        reset(appExecutorsMocked);
        reset(loadToDoItemsCallBackMocked);
    }

}
