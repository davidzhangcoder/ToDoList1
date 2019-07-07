package com.todolist.todomain.fragment.done;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.todolist.R;
import com.todolist.app.App;
import com.todolist.data.Injection;
import com.todolist.todomain.TestA;
import com.todolist.ui.LazyFragment;
import com.todolist.ui.adapter.TipListAdapter;
import com.todolist.model.IToDoItem;
import com.todolist.model.ToDoItem;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DoneFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DoneFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DoneFragment extends LazyFragment implements DoneFragmentContract.View {

    public static final String NAME = DoneFragment.class.getName();


    @Inject
    DoneFragmentContract.Presenter mPresenter;

    private RecyclerView recyclerView;

    private List<IToDoItem> doneList = new ArrayList<IToDoItem>();

    private OnFragmentInteractionListener mListener;

    private TipListAdapter tipListAdapter;

    private boolean isCreated = false;


    @Inject
    TestA a;


//    @Inject
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

//        AppComponent appComponent = DaggerAppComponent
//                .builder()
//                .appModule(new AppModule())
//                .build();

        DaggerDoneFragmentComponent
                .builder()
                .appComponent(((App)getActivity().getApplication()).getAppComponent())
                .doneFragmentModule(new DoneFragmentModule(this))
                .build()
                .inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        //Test
        Log.i("DoneFragment", "================================" + a);


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_done, container, false);

        recyclerView = view.findViewById(R.id.done_recycler);

        initRecyclerView( recyclerView );

        isCreated = true;
        lazyLoad();

        return view;
    }

    @Override
    protected void lazyLoad() {
        if( isCreated && isVisible ) {
            mPresenter.start();
        }
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

//        mPresenter.start();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

//        new DoneFragmentPresenter(Injection.provideToDoItemRepository(),this);

        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void setPresenter(DoneFragmentContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void showToDoItems(List<ToDoItem> toDoItemList) {
        List<IToDoItem> tempList = new ArrayList<IToDoItem>();
        for( ToDoItem toDoItem : toDoItemList )
            tempList.add( toDoItem );
        tipListAdapter.replaceData(tempList);
    }

    @Override
    public void refreshTabs() {
        mPresenter.start();;
    }

    public interface OnFragmentInteractionListener {
    }

}