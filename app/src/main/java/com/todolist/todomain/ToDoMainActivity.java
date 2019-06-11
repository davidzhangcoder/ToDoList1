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
import com.todolist.data.Injection;
import com.todolist.event.BusFactory;
import com.todolist.model.ToDoCategory;


import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;


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

    AppBarLayout appBarLayout;

    List<Fragment> fragmentList = new ArrayList<>();

    ToDoFragmentAdapter adapter;

    @Inject
    ToDoFragment toDoFragment;

    @Inject
    DoneFragment doneFragment;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = this;

        if (getLayoutId() > 0) {
            setContentView(getLayoutId());
        }

        toolbar = findViewById(R.id.toolbar);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

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

        DaggerToDoMainActivityComponent.builder().build().inject(this);

        initViewPagerFragments();
   }

    private void initViewPagerFragments() {

        if( getSupportFragmentManager().findFragmentByTag(ToDoFragment.NAME) == null ) {
            if(toDoFragment==null)
                toDoFragment = ToDoFragment.newInstance();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add( toDoFragment , ToDoFragment.NAME );
            fragmentTransaction.commit();
        }
        else
            toDoFragment = (ToDoFragment) getSupportFragmentManager().findFragmentByTag(ToDoFragment.NAME);

        if( getSupportFragmentManager().findFragmentByTag(DoneFragment.NAME) == null ) {
            if(doneFragment==null)
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
