package com.todolist;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.todolist.db.ToDoItemDao;
import com.todolist.model.IToDoItem;
import com.todolist.model.ToDoCategory;
import com.todolist.model.ToDoItem;
import com.todolist.util.ToDoItemUtil;

import java.util.ArrayList;
import java.util.List;

public class ToDoFragment extends Fragment {

    private RecyclerView recyclerView;

    private FloatingActionButton floatingActionButton;

    private OnFragmentInteractionListener mListener;

    private List<IToDoItem> toDoItemList = new ArrayList<IToDoItem>();

    private ToDoItemDao db;

    private TipListAdapter tipListAdapter;

    private long categoryFilterID = ToDoCategory.CATEGORY_ALL_ID;

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

        db = new ToDoItemDao(this.getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_tiplist, container, false)
                ;
//        ImageView editPlus = (ImageView) view.findViewById(R.id.edit_plus);
//        editPlus.setImageDrawable(new PlusDrawable(this.getResources().getColor(R.color.white)));

//        EditText editText = (EditText) view.findViewById(R.id.tip_edit);
//        editText.setOnEditorActionListener( tipEditOnEditorActionListener );
//        editText.clearFocus();

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

//        //Test
//        if( toDoItemList == null || toDoItemList.size() ==0 ) {
//            ToDoItem toDoItem = new ToDoItem();
//            toDoItem.setName("Test");
//            toDoItemList.add(toDoItem);
//        }

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

//                //Add item into "To Do" List
//                String name = textView.getText().toString();
//                ToDoItem toDoItem = new ToDoItem();
//                toDoItem.setName( name );
//                toDoItemList.add( 0 , toDoItem );
//
//                ((TipListAdapter)recyclerView.getAdapter()).notifyData( toDoItemList );
//
//                ContentValues values = new ContentValues();
//                values.put(ToDoItem.COLUMN_NAME, name);
//                values.put(ToDoItem.COLUMN_DONE_INDICATOR, Boolean.FALSE);


//                long id = db.addContent( ToDoItem.TABLE_NAME , values );
//                toDoItem.setId( id );

                Intent intent = new Intent(ToDoFragment.this.getContext(), EditToDoItemActivity.class);
//                intent.putExtra("content", ((TextView)textView).getText().toString());
//                intent.putExtra( EditToDoItemActivity.EDITTODOITEMACTIVITY_TODOITEM , toDoItem );
                ToDoFragment.this.getContext().startActivity(intent);
        }
    };

//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }

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

        if( categoryFilterID != ToDoCategory.CATEGORY_ADD_NEW_ID ) {
            toDoItemList.clear();
            toDoItemList.addAll(getDisplayToDoItemList(categoryFilterID));

            tipListAdapter.notifyData(toDoItemList);
        }
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);

        void refresh();
    }
}
