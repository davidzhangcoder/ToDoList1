package com.todolist.todomain.fragment;

import com.todolist.data.model.ToDoItem;
import com.todolist.data.source.GenericDataSource;
import com.todolist.data.source.ToDoItemDataSource;
import com.todolist.data.source.ToDoItemRepository;
import com.todolist.todomain.fragment.todo.ToDoFragmentContract;
import com.todolist.todomain.fragment.todo.ToDoFragmentPresenter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class ToDoFragmentPresenterTester {

    // Way 1 to mock
//    @Mock
//    private ToDoItemRepository toDoItemRepository;
//
//    @Mock
//    ToDoFragmentContract.View view;


    private ToDoFragmentPresenter toDoFragmentPresenter;

    private List<ToDoItem> toDoItemList = new ArrayList<ToDoItem>();

    // Way 1 to mock
    @Captor
    private ArgumentCaptor<ToDoItemDataSource.LoadToDoItemsCallBack> loadToDoItemsCallBackArgumentCaptor;

    @Captor
    private ArgumentCaptor<Long> longArgumentCaptor;

    @Captor
    private ArgumentCaptor<GenericDataSource.GenericToDoCallBack> genericToDoCallBackArgumentCaptor;

    @Captor
    private ArgumentCaptor<ToDoItem> toDoItemArgumentCaptor;

    @Before
    public void setup() {

        // When using Way 1 to mock, following line must be called
        MockitoAnnotations.initMocks(this);

//        //put Mocked toDoItemRepository and view into ToDoFragmentPresenter
//        toDoFragmentPresenter = new ToDoFragmentPresenter( toDoItemRepository , view );

        ToDoItem toDoItem1 = new ToDoItem();
        ToDoItem toDoItem2 = new ToDoItem();

        toDoItemList.add( toDoItem1 );
        toDoItemList.add( toDoItem2 );
    }

    @Test
    public void testStart() {

        // Way 2 to mock
        ToDoItemRepository toDoItemRepository = mock(ToDoItemRepository.class);
        ToDoFragmentContract.View view = mock(ToDoFragmentContract.View.class);

        //put Mocked toDoItemRepository and view into ToDoFragmentPresenter
        toDoFragmentPresenter = new ToDoFragmentPresenter( toDoItemRepository , view );

        toDoFragmentPresenter.start();

        verify(toDoItemRepository , times(1)).loadToDoItems(longArgumentCaptor.capture(),loadToDoItemsCallBackArgumentCaptor.capture());

        ToDoItemDataSource.LoadToDoItemsCallBack loadToDoItemsCallBack = loadToDoItemsCallBackArgumentCaptor.getValue();
        loadToDoItemsCallBack.onToDoItemsLoaded( toDoItemList );

        ArgumentCaptor<List> showToDoItemListArgumentCaptor = ArgumentCaptor.forClass(List.class);
        verify( view , times(1)).showToDoItems(showToDoItemListArgumentCaptor.capture());

        assertTrue( showToDoItemListArgumentCaptor.getValue().size() == 2 );

    }

    @Test
    public void doGetToDoItemsByCategoryTest() {

        ToDoItemRepository toDoItemRepository = mock(ToDoItemRepository.class);
        ToDoFragmentContract.View view = mock(ToDoFragmentContract.View.class);

        //put Mocked toDoItemRepository and view into ToDoFragmentPresenter
        toDoFragmentPresenter = new ToDoFragmentPresenter( toDoItemRepository , view );

//        toDoFragmentPresenter.doGetToDoItemsByCategory(ToDoCategory.CATEGORY_DEFAULT_ID);

        verify(toDoItemRepository , times(1)).loadToDoItems(longArgumentCaptor.capture(),loadToDoItemsCallBackArgumentCaptor.capture());

        ToDoItemDataSource.LoadToDoItemsCallBack loadToDoItemsCallBack = loadToDoItemsCallBackArgumentCaptor.getValue();
        loadToDoItemsCallBack.onToDoItemsLoaded( toDoItemList );

        ArgumentCaptor<List> showToDoItemListArgumentCaptor = ArgumentCaptor.forClass(List.class);
        verify( view , times(1)).showToDoItems(showToDoItemListArgumentCaptor.capture());

        assertTrue( showToDoItemListArgumentCaptor.getValue().size() == 2 );

    }

    @Test
    public void testDoneAction() {
        ToDoItem toDoItem = new ToDoItem();

        ToDoItemRepository toDoItemRepository = mock(ToDoItemRepository.class);
        ToDoFragmentContract.View view = mock(ToDoFragmentContract.View.class);

        //put Mocked toDoItemRepository and view into ToDoFragmentPresenter
        toDoFragmentPresenter = new ToDoFragmentPresenter( toDoItemRepository , view );

        toDoFragmentPresenter.doneAction( toDoItem );

        verify( toDoItemRepository , times( 1 ) ).completeToDo( toDoItemArgumentCaptor.capture() , genericToDoCallBackArgumentCaptor.capture() );

        GenericDataSource.GenericToDoCallBack genericToDoCallBackArgumentCaptorValue = genericToDoCallBackArgumentCaptor.getValue();
        ToDoItem toDoItemArgumentCaptorValue = toDoItemArgumentCaptor.getValue();
        assertTrue( genericToDoCallBackArgumentCaptorValue != null );
        assertTrue( toDoItemArgumentCaptorValue != null );
        assertTrue( toDoItemArgumentCaptorValue.isDone() == true );
    }
}
