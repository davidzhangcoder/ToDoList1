package com.todolist.todomain;

import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.todolist.R;
import com.todolist.data.Injection;
import com.todolist.ui.adapter.TipListAdapter;
import com.todolist.db.GenericDao;
import com.todolist.model.IToDoItem;
import com.todolist.model.ToDoItem;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DoneFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DoneFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DoneFragment extends Fragment implements DoneMainContract.View {

    public static final String NAME = DoneFragment.class.getName();


    private DoneMainContract.Presenter mPresenter;

    private RecyclerView recyclerView;

    private List<IToDoItem> doneList = new ArrayList<IToDoItem>();

    private OnFragmentInteractionListener mListener;

    private TipListAdapter tipListAdapter;


    public DoneFragment() {
        // Required empty public constructor
    }

    public static DoneFragment newInstance() {
        DoneFragment fragment = new DoneFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public static DoneFragment newInstance(String param1, String param2) {
        DoneFragment fragment = new DoneFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_done, container, false);

        recyclerView = view.findViewById(R.id.done_recycler);

        initRecyclerView( recyclerView );

        return view;
    }

    private void initRecyclerView( RecyclerView recyclerView )
    {
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setHasFixedSize(true);

        tipListAdapter = new TipListAdapter( this.getContext(), doneList );
        tipListAdapter.setDoneAction( getDoneAction() );
        recyclerView.setAdapter( tipListAdapter );
    }

    private TipListAdapter.ToDoItemAction getDoneAction()
    {
        TipListAdapter.ToDoItemAction doneAction = new TipListAdapter.ToDoItemAction() {
            @Override
            public void doAction(ToDoItem toDoItem) {
                DoneFragment.this.mPresenter.reverseDoneAction( toDoItem );
            }
        };

        return doneAction;
    }

    @Override
    public void onResume() {
        super.onResume();

        mPresenter.start();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        new DoneMainPresenter(Injection.provideToDoItemRepository(),this);

        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void setPresenter(DoneMainContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void showToDoItems(List<ToDoItem> toDoItemList) {
        List<IToDoItem> tempList = new ArrayList<IToDoItem>();
        for( ToDoItem toDoItem : toDoItemList )
            tempList.add( toDoItem );
        tipListAdapter.replaceData(tempList);
    }

    public void refreshTabs() {
        mListener.refresh();
    }

    public interface OnFragmentInteractionListener {
        void refresh();
    }

}
