package com.todolist.todomain.fragment;

import com.todolist.data.source.ToDoItemDataSource;
import com.todolist.data.source.ToDoItemRepository;
import com.todolist.model.ToDoItem;
import com.todolist.todomain.fragment.todo.ToDoFragmentContract;
import com.todolist.todomain.fragment.todo.ToDoFragmentPresenter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class ToDoFragmentPresenterTester {

    @Mock
    private ToDoItemRepository toDoItemRepository;

    @Mock
    ToDoFragmentContract.View view;


    private ToDoFragmentPresenter toDoFragmentPresenter;

    private List<ToDoItem> toDoItemList = new ArrayList<ToDoItem>();

    @Captor
    private ArgumentCaptor<ToDoItemDataSource.LoadToDoItemsCallBack> loadToDoItemsCallBackArgumentCaptor;

    @Captor
    private ArgumentCaptor<Long> longArgumentCaptor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        toDoFragmentPresenter = new ToDoFragmentPresenter( toDoItemRepository , view );

        ToDoItem toDoItem1 = new ToDoItem();
        ToDoItem toDoItem2 = new ToDoItem();

        toDoItemList.add( toDoItem1 );
        toDoItemList.add( toDoItem2 );
    }

    @Test
    public void loadAllTasksFromRepositoryAndLoadIntoView() {

        toDoFragmentPresenter.start();

        verify(toDoItemRepository , times(1)).loadToDoItems(longArgumentCaptor.capture(),loadToDoItemsCallBackArgumentCaptor.capture());

        ToDoItemDataSource.LoadToDoItemsCallBack loadToDoItemsCallBack = loadToDoItemsCallBackArgumentCaptor.getValue();
        loadToDoItemsCallBack.onToDoItemsLoaded( toDoItemList );

        ArgumentCaptor<List> showToDoItemListArgumentCaptor = ArgumentCaptor.forClass(List.class);
        verify( view , times(1)).showToDoItems(showToDoItemListArgumentCaptor.capture());

        assertTrue( showToDoItemListArgumentCaptor.getValue().size() == 2 );

    }
}
