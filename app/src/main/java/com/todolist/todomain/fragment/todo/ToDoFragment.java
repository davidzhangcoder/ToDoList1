package com.todolist.todomain.fragment.todo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherAdView;
import com.todolist.model.TipHolder;
import com.todolist.tododetail.EditToDoItemActivity;
import com.todolist.R;

import com.todolist.todomain.ToDoMainActivity;
import com.todolist.todomain.fragment.category.CategoryFragment;
import com.todolist.todomain.TestA;
import com.todolist.ui.CustomRecyclerScrollViewListener;
import com.todolist.ui.LazyFragment;
import com.todolist.ui.adapter.CategoryAdapter;
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

    private Spinner categorySpinner;

    private OnFragmentInteractionListener mListener;

    private List<IToDoItem> toDoItemList = new ArrayList<IToDoItem>();

    private TipListAdapter tipListAdapter;

    private long categoryFilterID = ToDoCategory.CATEGORY_ALL_ID;
    private List<ToDoCategory> toDoCategorys = new ArrayList<ToDoCategory>();

//    private TextView categoryButton;
//    private ImageView categoryButtonImage;
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

                //build ToolBar that inside at ToDoMainActivity
        Toolbar toolbar = (Toolbar)this.getActivity().findViewById(R.id.toolbar);
        inflater.inflate(R.layout.toolbar_layout, toolbar, true);
        ((AppCompatActivity)this.getActivity()).setSupportActionBar(toolbar);
        categorySpinner = (Spinner) toolbar.findViewById(R.id.categorySpinner);
//        categoryButton = (TextView)toolbar.findViewById(R.id.categoryButton);
//        categoryButtonImage = (ImageView)toolbar.findViewById(R.id.categoryButtonImage);;

        //Ads View
        mAdView = view.findViewById(R.id.adView);

        //Initial
        initRecyclerView( recyclerView , floatingActionButton );
//        initCategoryFragment();
//        initialCategorySpinner();

        //Initial Menu ( comment out following if fragment need to append some menu item in, it is necessary to display menu in Fragment )
        //this.setHasOptionsMenu( true );

        isCreated = true;
//        lazyLoad();

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_todofragment, menu);

        MenuItem item = menu.findItem(R.id.filterBySpinner);
        Spinner spinner = (Spinner) item.getActionView();
        spinner.setPrompt("Filter By:");

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getContext(),
                R.array.spinner_list_item_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
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

    public void initialCategorySpinner( List<ToDoCategory> toDoCategorys ) {
        if( categorySpinner != null && toDoCategorys != null ) {

            ToDoCategory allToDoCategory = new ToDoCategory();
            allToDoCategory.setId( ToDoCategory.CATEGORY_ALL_ID );
            allToDoCategory.setName( ToDoCategory.CATEGORY_ALL_NAME );
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

    private void initCategoryFragment()
    {
        if( selectedToDoCategory == null ) {
            ToDoCategory allToDoCategory = new ToDoCategory();
            allToDoCategory.setId( ToDoCategory.CATEGORY_ALL_ID );
            allToDoCategory.setName( ToDoCategory.CATEGORY_ALL_NAME );

            selectedToDoCategory = allToDoCategory;
        }

//        categoryButtonImage.setColorFilter(getResources().getColor(R.color.white));
//        categoryButton.setText( selectedToDoCategory.getName() );
//        categoryButton.setTypeface(categoryButton.getTypeface(), Typeface.BOLD);
//        categoryButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mPresenter.doDisplayCategoryDialog();
//            }
//        });
    }

    private void initRecyclerView( RecyclerView recyclerView , FloatingActionButton floatingActionButton )
    {
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setHasFixedSize(true);

        tipListAdapter = new TipListAdapter( this.getContext(), toDoItemList );
        tipListAdapter.setDoneAction( getDoneAction() );
        recyclerView.setAdapter( tipListAdapter );
        ItemTouchHelper.Callback callback = new TipListItemTouchHelperCallback();
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        CustomRecyclerScrollViewListener customRecyclerScrollViewListener = new CustomRecyclerScrollViewListener() {
            @Override
            public void show() {
//                floatingActionButton.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();

                if( mAdView.getVisibility() == View.VISIBLE ) {
                    mAdView.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
                }
            }

            @Override
            public void hide() {
//                FrameLayout.LayoutParams fl = (FrameLayout.LayoutParams)floatingActionButton.getLayoutParams();
//                floatingActionButton.animate().translationY( floatingActionButton.getHeight() + fl.bottomMargin ).setInterpolator(new AccelerateInterpolator(2.0f)).start();

                if( mAdView.getVisibility() == View.VISIBLE ) {
                    CoordinatorLayout.LayoutParams mAdVieFrameLayoutw = (CoordinatorLayout.LayoutParams)mAdView.getLayoutParams();
                    mAdView.animate().translationY( mAdView.getHeight() + mAdVieFrameLayoutw.bottomMargin ).setInterpolator(new AccelerateInterpolator(2.0f)).start();
                }
            }
        };

        recyclerView.addOnScrollListener( customRecyclerScrollViewListener );
    }

    private TipListAdapter.ToDoItemAction getDoneAction()
    {
        TipListAdapter.ToDoItemAction doneAction = new TipListAdapter.ToDoItemAction() {
            @Override
            public void doAction(RecyclerView.ViewHolder holder, List<IToDoItem> mData , RecyclerView.Adapter adapter ) {
                Snackbar.make( holder.itemView , "Is Done?", Snackbar.LENGTH_LONG )
                        .setAction("Yes", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                int position = holder.getAdapterPosition();
                                ToDoItem toDoItem = (ToDoItem) mData.get(position);


                                mData.remove(position);

                                adapter.notifyItemRemoved(position);

                                ToDoFragment.this.mPresenter.doneAction( toDoItem );
                            }
                        })
                        .addCallback(new Snackbar.Callback() {
                            @Override
                            public void onDismissed(Snackbar transientBottomBar, int event) {
                                if( event == Snackbar.Callback.DISMISS_EVENT_TIMEOUT ) {
                                    if( holder instanceof TipHolder) {
                                        ((CheckBox)((TipHolder)holder).getView(R.id.tip_checkbox)).setChecked(false);
                                    }
                                }
                            }
                        })
                        .show();

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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();

        if( isCreated && isVisible )
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
//        //我们根据requestCode判断是哪个子Fragment传回的数据
//        //从data中拿到传回的数据
//        if(resultCode == DISPLAY_CATEGORY_FRAGMENT_REQUEST_CODE) {
//            selectedToDoCategory = (ToDoCategory) data.getExtras().get(CategoryFragment.KEY_SELECTED_TODOCATEGORY);
//            categoryButton.setText(selectedToDoCategory.getName());
//            mPresenter.doGetToDoItemsByCategory(selectedToDoCategory.getId());
//        }
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
