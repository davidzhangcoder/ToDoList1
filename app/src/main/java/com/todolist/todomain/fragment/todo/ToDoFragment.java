package com.todolist.todomain.fragment.todo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherAdView;
import com.todolist.app.App;
import com.todolist.data.Injection;
import com.todolist.tododetail.EditToDoItemActivity;
import com.todolist.R;

import com.todolist.todomain.ToDoMainActivity;
import com.todolist.todomain.fragment.category.CategoryFragment;
import com.todolist.todomain.TestA;
import com.todolist.ui.LazyFragment;
import com.todolist.ui.adapter.TipListAdapter;
import com.todolist.TipListItemTouchHelperCallback;
import com.todolist.model.IToDoItem;
import com.todolist.model.ToDoCategory;
import com.todolist.model.ToDoItem;
import com.todolist.util.AdsUtil;
import com.todolist.util.ToDoItemUtil;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

public class ToDoFragment extends LazyFragment implements ToDoFragmentContract.View {

    public static final String NAME = ToDoFragment.class.getName();
    public static final int DISPLAY_CATEGORY_FRAGMENT_REQUEST_CODE = 1;


    private RecyclerView recyclerView;

    private FloatingActionButton floatingActionButton;

    private OnFragmentInteractionListener mListener;

    private List<IToDoItem> toDoItemList = new ArrayList<IToDoItem>();

    private TipListAdapter tipListAdapter;

    private long categoryFilterID = ToDoCategory.CATEGORY_ALL_ID;

    private TextView categoryButton;
    private ImageView categoryButtonImage;
    private ToDoCategory selectedToDoCategory;
    private PublisherAdView mAdView;


    @Inject
    ToDoFragmentContract.Presenter mPresenter;

    private boolean isCreated = false;


    @Inject
    TestA a;


//    @Inject
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

//        DaggerToDoFragmentComponent
//                .builder()
//                .appComponent(((App)getActivity().getApplication()).getAppComponent())
//                .build()
//                .inject(this);

//        DaggerAppComponent
//                .builder()
//                .build()
//                .toDoFragmentComponent()
//                .build()
//                .inject(this);

//        ((App)getActivity().getApplication())
//                .getAppComponent()
//                .toDoFragmentComponent()
//                .setToDoFragmentModule(new ToDoFragmentModule(this))
//                .build()
//                .inject(this);

        ((ToDoMainActivity)getActivity())
                .getToDoMainActivityComponent()
                .toDoFragmentComponent()
                .setToDoFragmentModule(new ToDoFragmentModule(this))
                .build()
                .inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        //Test
        Log.i("ToDoFragment", "================================" + a);


        View view = inflater.inflate(R.layout.content_tiplist, container, false);
        recyclerView = view.findViewById(R.id.tip_recycler);

        floatingActionButton = view.findViewById(R.id.addToDo);
        floatingActionButton.setOnClickListener( tipEditOnEditorActionListener );

        Toolbar toolbar = (Toolbar)this.getActivity().findViewById(R.id.toolbar);
        inflater.inflate(R.layout.toolbar_layout, toolbar, true);
        ((AppCompatActivity)this.getActivity()).setSupportActionBar(toolbar);
        categoryButton = (TextView)toolbar.findViewById(R.id.categoryButton);
        categoryButtonImage = (ImageView)toolbar.findViewById(R.id.categoryButtonImage);;
        mAdView = view.findViewById(R.id.adView);

        initRecyclerView( recyclerView );
        initCategoryFragment();

        isCreated = true;
        lazyLoad();

        return view;
    }

    private void displayBannerAds(PublisherAdView mAdView) {
        PublisherAdRequest adRequest = new PublisherAdRequest.Builder()/*.addTestDevice("B3EEABB8EE11C2BE770B684D95219ECB")*/.build();
        mAdView.loadAd(adRequest);
        if(AdsUtil.displayBannerAds( this.getActivity() ))
            mAdView.setVisibility(View.VISIBLE);
        else
            mAdView.setVisibility(View.INVISIBLE);

    }

    @Override
    protected void lazyLoad() {
        if( isCreated && isVisible ) {
            mPresenter.start();

            displayBannerAds(mAdView);
        }
    }

    @Override
    protected void onInvisible() {
        if( isCreated && !isVisible )
            mAdView.setVisibility(View.INVISIBLE);
    }

    private void initCategoryFragment()
    {
        if( selectedToDoCategory == null ) {
            ToDoCategory allToDoCategory = new ToDoCategory();
            allToDoCategory.setId( ToDoCategory.CATEGORY_ALL_ID );
            allToDoCategory.setName( ToDoCategory.CATEGORY_ALL_NAME );

            selectedToDoCategory = allToDoCategory;
        }

        categoryButtonImage.setColorFilter(getResources().getColor(R.color.white));
        categoryButton.setText( selectedToDoCategory.getName() );
        categoryButton.setTypeface(categoryButton.getTypeface(), Typeface.BOLD);
        categoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.doDisplayCategoryDialog();
            }
        });
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

//        new ToDoFragmentPresenter(Injection.provideToDoItemRepository(),this);

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

//        if( isCreated && isVisible )
//            mPresenter.start();

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
        mPresenter.start();;
    }

    @Override
    public void showToDoDetail() {
        Intent intent = new Intent(ToDoFragment.this.getContext(), EditToDoItemActivity.class);
        ToDoFragment.this.getContext().startActivity(intent);
    }

    @Override
    public void showCategoryDialog() {
        CategoryFragment categoryFragment = CategoryFragment.newInstance( selectedToDoCategory );
        categoryFragment.setTargetFragment( this , DISPLAY_CATEGORY_FRAGMENT_REQUEST_CODE );
        categoryFragment.show( this.getFragmentManager(), null);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //我们根据requestCode判断是哪个子Fragment传回的数据
        //从data中拿到传回的数据
        if(resultCode == DISPLAY_CATEGORY_FRAGMENT_REQUEST_CODE) {
            selectedToDoCategory = (ToDoCategory) data.getExtras().get(CategoryFragment.KEY_SELECTED_TODOCATEGORY);
            categoryButton.setText(selectedToDoCategory.getName());
            mPresenter.doGetToDoItemsByCategory(selectedToDoCategory.getId());
        }
    }

    @Override
    public void setPresenter(@NonNull ToDoFragmentContract.Presenter presenter) {
         this.mPresenter = checkNotNull(presenter);
    }

    public interface OnFragmentInteractionListener {

//        void refresh();
    }


}
//a
//b
//c
