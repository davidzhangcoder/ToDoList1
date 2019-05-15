package com.todolist.todomain;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.todolist.EditToDoItemActivity;
import com.todolist.R;
import com.todolist.TipListAdapter;
import com.todolist.TipListItemTouchHelperCallback;
import com.todolist.db.GenericDao;
import com.todolist.model.IToDoItem;
import com.todolist.model.ToDoCategory;
import com.todolist.model.ToDoItem;
import com.todolist.util.ToDoItemUtil;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class ToDoFragment extends Fragment implements ToDoMainContract.View {

    public static final String NAME = ToDoFragment.class.getName();


    private RecyclerView recyclerView;

    private FloatingActionButton floatingActionButton;

    private OnFragmentInteractionListener mListener;

    private List<IToDoItem> toDoItemList = new ArrayList<IToDoItem>();

    private GenericDao db;

    private TipListAdapter tipListAdapter;

    private long categoryFilterID = ToDoCategory.CATEGORY_ALL_ID;

    private ToDoMainContract.Presenter mPresenter;


    public ToDoFragment() {
    }

    public static ToDoFragment newInstance() {
        ToDoFragment fragment = new ToDoFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public static ToDoFragment newInstance( long categoryID ) {
        ToDoFragment fragment = new ToDoFragment();
        Bundle args = new Bundle();
        args.putLong(ToDoCategory.TABLE_NAME+ToDoCategory.COLUMN_ID , categoryID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            if( getArguments().getLong( ToDoCategory.TABLE_NAME+ToDoCategory.COLUMN_ID , Integer.MIN_VALUE ) != Integer.MIN_VALUE )
                categoryFilterID = getArguments().getLong( ToDoCategory.TABLE_NAME+ToDoCategory.COLUMN_ID );
        }

        db = new GenericDao(this.getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_tiplist, container, false)
                ;
        recyclerView = view.findViewById(R.id.tip_recycler);

        floatingActionButton = view.findViewById(R.id.addToDo);
        floatingActionButton.setOnClickListener( tipEditOnEditorActionListener );

        initRecyclerView( recyclerView );

        return view;
    }

    private void initRecyclerView( RecyclerView recyclerView )
    {
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setHasFixedSize(true);

        toDoItemList.addAll( getDisplayToDoItemList( categoryFilterID ) );

        tipListAdapter = new TipListAdapter( this.getContext(), toDoItemList );
        tipListAdapter.setDoneAction( getDoneAction() );
        recyclerView.setAdapter( tipListAdapter );
        ItemTouchHelper.Callback callback = new TipListItemTouchHelperCallback();
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private List<IToDoItem> getDisplayToDoItemList( long categoryFilterID ) {
        List<IToDoItem> toDoItemList = new ArrayList<IToDoItem>();

        if( categoryFilterID == ToDoCategory.CATEGORY_ALL_ID )
            toDoItemList.addAll( ToDoItemUtil.getToDoItemGroupByDueTime( ToDoItem.getToDoItems() ) );
        else if( categoryFilterID != ToDoCategory.CATEGORY_ADD_NEW_ID )
        {
            String selection = ToDoItem.COLUMN_DONE_INDICATOR + "<=? and " + ToDoItem.COLUMN_CATEGORY + " == ?" ;
            String[] parameter = new String[]{ "0" , categoryFilterID+"" };
            toDoItemList.addAll( ToDoItemUtil.getToDoItemGroupByDueTime( ToDoItem.getToDoItems( selection , parameter ) ) );
        }

        return toDoItemList;
    }

    private TipListAdapter.ToDoItemAction getDoneAction()
    {
        TipListAdapter.ToDoItemAction doneAction = new TipListAdapter.ToDoItemAction() {
            @Override
            public void doAction(ToDoItem toDoItem) {
                toDoItem.setDone( true );
                ContentValues values = new ContentValues();
                values.put(ToDoItem.COLUMN_DONE_INDICATOR, toDoItem.isDone());
                db.updateContent( ToDoItem.TABLE_NAME , values , ToDoItem.COLUMN_ID + " = ?" , new String[]{String.valueOf(toDoItem.getId())} );

                mListener.refresh();
            }
        };

        return doneAction;
    }

    private View.OnClickListener tipEditOnEditorActionListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
                Intent intent = new Intent(ToDoFragment.this.getContext(), EditToDoItemActivity.class);
                ToDoFragment.this.getContext().startActivity(intent);
        }
    };

     @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onResume() {
        super.onResume();

        mPresenter.start();

        if( categoryFilterID != ToDoCategory.CATEGORY_ADD_NEW_ID ) {
            toDoItemList.clear();
            toDoItemList.addAll(getDisplayToDoItemList(categoryFilterID));

            tipListAdapter.notifyData(toDoItemList);
        }
    }

    @Override
    public void showCategoryFilterDialog() {

    }

    @Override
    public void showAddCategoryDialog() {

    }

    @Override
    public void setPresenter(@NonNull ToDoMainContract.Presenter presenter) {
         this.mPresenter = checkNotNull(presenter);
    }

    public interface OnFragmentInteractionListener {

        void refresh();
    }
}
