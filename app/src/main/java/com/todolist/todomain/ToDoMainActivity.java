package com.todolist.todomain;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.todolist.R;
import com.todolist.event.BusFactory;
import com.todolist.model.ToDoCategory;


import java.util.ArrayList;
import java.util.List;


public class ToDoMainActivity extends AppCompatActivity
        implements
        ToDoFragment.OnFragmentInteractionListener ,
        CategoryFragment.OnFragmentInteractionListener ,
        DoneFragment.OnFragmentInteractionListener
{
    private static final String TAG = ToDoMainActivity.class.getName();

    protected Activity context;
//    private Unbinder unbinder;

//    @BindView(R.id.toolbar)
    Toolbar toolbar;

//    @BindView(R.id.tabLayout)
    TabLayout tabLayout;

//    @BindView(R.id.viewPager)
    ViewPager viewPager;

    TextView categoryButton;
    ImageView categoryButtonImage;

    AppBarLayout appBarLayout;

    List<Fragment> fragmentList = new ArrayList<>();

    ToDoFragmentAdapter adapter;
    ToDoCategory selectedToDoCategory;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = this;

        if (getLayoutId() > 0) {
            setContentView(getLayoutId());
        }

        toolbar = findViewById(R.id.toolbar);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        categoryButton = (TextView)findViewById(R.id.categoryButton);
        categoryButtonImage = (ImageView)findViewById(R.id.categoryButtonImage);;

        appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                //following code make it scroll up to hide actionbar, but not hide Tab
                ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) viewPager.getLayoutParams();
                layoutParams.setMargins(0, 0, 0, toolbar.getMeasuredHeight() + verticalOffset);
                viewPager.requestLayout();

            }

        });

        initViewPagerFragments();
        initCategoryFragment();
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
                CategoryFragment categoryFragment = CategoryFragment.newInstance( selectedToDoCategory );
                categoryFragment.show(getSupportFragmentManager(), null);
            }
        });
    }

    private void initViewPagerFragments() {
        setSupportActionBar(toolbar);

        ToDoFragment toDoFragment = null;
        DoneFragment doneFragment = null;

        if( getSupportFragmentManager().findFragmentByTag(ToDoFragment.NAME) == null ) {
            toDoFragment = ToDoFragment.newInstance();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add( toDoFragment , ToDoFragment.NAME );
            fragmentTransaction.commit();
        }
        else
            toDoFragment = (ToDoFragment) getSupportFragmentManager().findFragmentByTag(ToDoFragment.NAME);

        if( getSupportFragmentManager().findFragmentByTag(DoneFragment.NAME) == null ) {
            doneFragment = DoneFragment.newInstance();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add( doneFragment , DoneFragment.NAME );
            fragmentTransaction.commit();
        }
        else
            doneFragment = (DoneFragment) getSupportFragmentManager().findFragmentByTag(DoneFragment.NAME);


        fragmentList.clear();
        fragmentList.add(toDoFragment);
        fragmentList.add(doneFragment);
//        fragmentList.add(ToDoFragment.newInstance());
//        fragmentList.add(DoneFragment.newInstance());

        String[] titles = new String[]{getString(R.string.tab_title_todo) , getString(R.string.tab_title_done)};

        if (adapter == null) {
            adapter = new ToDoFragmentAdapter(getSupportFragmentManager(), fragmentList, titles);
        }
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(3);

        tabLayout.setupWithViewPager(viewPager);
    }


    private int getLayoutId() {
        return R.layout.activity_to_do_list_main;
    }

    //implemented for ToDoFragment
    @Override
    public void refresh() {
        fragmentList.clear();
        fragmentList.add(ToDoFragment.newInstance());
        fragmentList.add(DoneFragment.newInstance());
        adapter.updateData( fragmentList );
    }

    public void refresh( long categoryID ) {
        fragmentList.clear();
        fragmentList.add(ToDoFragment.newInstance( categoryID ));
        fragmentList.add(DoneFragment.newInstance());
        adapter.updateData( fragmentList );
    }

    public void onCategorySelected( ToDoCategory toDoCategory )
    {
        this.selectedToDoCategory = toDoCategory;
        categoryButton.setText( selectedToDoCategory.getName() );
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (useEventBus()) {
            BusFactory.getBus().register(this);
        }
    }

    public boolean useEventBus() {
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BusFactory.getBus().unregister(this);
    }

}
