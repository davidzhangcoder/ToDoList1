package com.todolist.todomain.fragment.done;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.material.snackbar.Snackbar;
import com.todolist.R;
import com.todolist.app.App;
import com.todolist.data.model.ToDoCategory;
import com.todolist.data.model.ToDoItem;
import com.todolist.model.IToDoItem;
import com.todolist.model.TipHolder;
import com.todolist.todomain.TestA;
import com.todolist.ui.LazyFragment;
import com.todolist.ui.adapter.CategoryAdapter;
import com.todolist.ui.adapter.TipListAdapter;
import com.todolist.util.AdsUtil;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


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

    private Spinner categorySpinner;

    private RecyclerView recyclerView;

    private Snackbar snackbar;

    private AdView mAdView;


    private List<ToDoCategory> toDoCategorys = new ArrayList<ToDoCategory>();

    private ToDoCategory selectedToDoCategory;

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
        mAdView = view.findViewById(R.id.adView);

        //build ToolBar that inside at ToDoMainActivity
        Toolbar toolbar = (Toolbar)this.getActivity().findViewById(R.id.toolbar);
        inflater.inflate(R.layout.toolbar_layout, toolbar, true);
        ((AppCompatActivity)this.getActivity()).setSupportActionBar(toolbar);
        categorySpinner = (Spinner) toolbar.findViewById(R.id.categorySpinner);

        initRecyclerView( recyclerView );

        isCreated = true;
        lazyLoad();

        return view;
    }

    private void displayBannerAds(AdView mAdView) {
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        if(AdsUtil.displayBannerAds( this.getActivity() ))
            mAdView.setVisibility(View.VISIBLE);
        else
            mAdView.setVisibility(View.INVISIBLE);

    }

    @Override
    public void onAttachFragment(Fragment childFragment) {
        super.onAttachFragment(childFragment);
    }

    @Override
    protected void lazyLoad() {
        if( isCreated && isVisible ) {
            mPresenter.start();

            //Move displayBannerAds(mAdView) to onAttach(), because it needs getActivity() to return context
            //getActivity() returns null before when onAttach() invoked
            if( getActivity() != null )
                displayBannerAds(mAdView);
        }
    }

    @Override
    protected void onInvisible() {
        if( isCreated && !isVisible )
            mAdView.setVisibility(View.INVISIBLE);
    }

    public void initialCategorySpinner( List<ToDoCategory> toDoCategorys ) {
        if( categorySpinner != null && toDoCategorys != null ) {

            ToDoCategory allToDoCategory = new ToDoCategory( ToDoCategory.Companion.getCATEGORY_ALL_ID() , ToDoCategory.Companion.getAllCatrgoryName() );
            toDoCategorys.add( 0 , allToDoCategory );


            this.toDoCategorys = toDoCategorys;
            if( selectedToDoCategory == null && toDoCategorys != null && toDoCategorys.size() != 0 )
                selectedToDoCategory = toDoCategorys.get( 0 );
            SpinnerAdapter adapter = new CategoryAdapter(this.getContext(), toDoCategorys , selectedToDoCategory);
            categorySpinner.setAdapter(adapter);
            categorySpinner.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    ((CategoryAdapter)adapter).setSelectedToDoCategory(toDoCategorys.get(position));
                    selectedToDoCategory=toDoCategorys.get(position);
                    mPresenter.doGetToDoItemsByCategory(selectedToDoCategory.getId());
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
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
            public void doAction( RecyclerView.ViewHolder holder, List<IToDoItem> mData , RecyclerView.Adapter adapter ) {

                if( holder instanceof TipHolder ) {
                    CheckBox checkBox = (CheckBox) ((TipHolder) holder).getView(R.id.tip_checkbox);
                    if( checkBox != null && !checkBox.isChecked() ) {
                        snackbar = Snackbar.make(holder.itemView, "Not Finish Yet?", Snackbar.LENGTH_LONG)
                                .setAction("Yes", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        int position = holder.getAdapterPosition();

                                        ToDoItem toDoItem = (ToDoItem) mData.get(position);

                                        mData.remove(position);

                                        adapter.notifyItemRemoved(position);

                                        DoneFragment.this.mPresenter.reverseDoneAction(toDoItem);
                                    }
                                })
                                .addCallback(new Snackbar.Callback() {
                                    @Override
                                    public void onDismissed(Snackbar transientBottomBar, int event) {
                                        if (event == Snackbar.Callback.DISMISS_EVENT_TIMEOUT || event == Snackbar.Callback.DISMISS_EVENT_CONSECUTIVE) {
                                            if (holder instanceof TipHolder) {
                                                ((CheckBox) ((TipHolder) holder).getView(R.id.tip_checkbox)).setChecked(true);
                                            }
                                        }
                                    }
                                });
                        snackbar.show();
                    }
                    else {
                        if( snackbar != null )
                            snackbar.dismiss();
                    }


//                DoneFragment.this.mPresenter.reverseDoneAction( toDoItem );
                }
            }
        };

        return doneAction;
    }

    @Override
    public void onResume() {
        super.onResume();

        if( isCreated && isVisible )
            mPresenter.start();

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
