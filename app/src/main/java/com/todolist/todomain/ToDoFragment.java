package com.todolist.todomain;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.todolist.data.Injection;
import com.todolist.tododetail.EditToDoItemActivity;
import com.todolist.R;
import com.todolist.ui.adapter.TipListAdapter;
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_tiplist, container, false)
                ;
        recyclerView = view.findViewById(R.id.tip_recycler);

        floatingActionButton = view.findViewById(R.id.addToDo);
        floatingActionButton.setOnClickListener( tipEditOnEditorActionListener );

        Toolbar toolbar = (Toolbar)this.getActivity().findViewById(R.id.toolbar);
        inflater.inflate(R.layout.toolbar_layout, toolbar, true);
        ((AppCompatActivity)this.getActivity()).setSupportActionBar(toolbar);

        initRecyclerView( recyclerView );
        return view;
    }

    private void initRecyclerView( RecyclerView recyclerView )
    {
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setHasFixedSize(true);

        tipListAdapter = new TipListAdapter( this.getContext(), toDoItemList );
        tipListAdapter.setDoneAction( getDoneAction() );
        recyclerView.setAdapter( tipListAdapter );
        ItemTouchHelper.Callback callback = new TipListItemTouchHelperCallback();
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private TipListAdapter.ToDoItemAction getDoneAction()
    {
        TipListAdapter.ToDoItemAction doneAction = new TipListAdapter.ToDoItemAction() {
            @Override
            public void doAction(ToDoItem toDoItem) {
                ToDoFragment.this.mPresenter.doneAction( toDoItem );
            }
        };

        return doneAction;
    }

    private View.OnClickListener tipEditOnEditorActionListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            ToDoFragment.this.mPresenter.forwardToToDoDetail();
        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        new ToDoMainPresenter(Injection.provideToDoItemRepository(),this);

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

//        if( categoryFilterID != ToDoCategory.CATEGORY_ADD_NEW_ID ) {
//            toDoItemList.clear();
//            toDoItemList.addAll(getDisplayToDoItemList(categoryFilterID));
//
//            tipListAdapter.notifyData(toDoItemList);
//        }
    }

    @Override
    public void showToDoItems(List<ToDoItem> toDoItemList) {
        tipListAdapter.replaceData( ToDoItemUtil.getToDoItemGroupByDueTime( toDoItemList ) );
    }

    @Override
    public void refreshTabs() {
        mListener.refresh();
    }

    @Override
    public void showToDoDetail() {
        Intent intent = new Intent(ToDoFragment.this.getContext(), EditToDoItemActivity.class);
        ToDoFragment.this.getContext().startActivity(intent);
    }

    @Override
    public void setPresenter(@NonNull ToDoMainContract.Presenter presenter) {
         this.mPresenter = checkNotNull(presenter);
    }

    public interface OnFragmentInteractionListener {

        void refresh();
    }
}
