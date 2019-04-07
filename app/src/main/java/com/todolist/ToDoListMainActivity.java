package com.todolist;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.todolist.broadcast.ToDoListAlarmBroadCastReceiver;
import com.todolist.event.BusFactory;
import com.todolist.service.ToDoListAlarmService;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class ToDoListMainActivity extends AppCompatActivity
        implements
        ToDoFragment.OnFragmentInteractionListener ,
        CategoryFragment.OnFragmentInteractionListener ,
        DoneFragment.OnFragmentInteractionListener
{
    protected Activity context;
//    private Unbinder unbinder;

//    @BindView(R.id.toolbar)
    Toolbar toolbar;

//    @BindView(R.id.tabLayout)
    TabLayout tabLayout;

//    @BindView(R.id.viewPager)
    ViewPager viewPager;

    Button categoryButton;

    List<Fragment> fragmentList = new ArrayList<>();
    String[] titles = {"ToDo", "Done"};

    ToDoFragmentAdapter adapter;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = this;

        if (getLayoutId() > 0) {
            setContentView(getLayoutId());
        }
        setListener();

        toolbar = findViewById(R.id.toolbar);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        categoryButton = findViewById(R.id.categoryButton);

        initData(savedInstanceState);
        initService();
        initCategoryFragment();
    }

    private void initCategoryFragment()
    {
        categoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CategoryFragment categoryFragment = CategoryFragment.newInstance();
                categoryFragment.show(getSupportFragmentManager(), null);
            }
        });
    }

    private void initService()
    {
//        Intent alarmServiceIntent = new Intent( this , ToDoListAlarmService.class );
//        startService( alarmServiceIntent );

//        AlarmManager  alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//        Calendar calendar =Calendar.getInstance(Locale.getDefault());
//        calendar.setTimeInMillis(System.currentTimeMillis());
////        calendar.set(Calendar.HOUR_OF_DAY, 20);
////        calendar.set(Calendar.MINUTE, 30);
////        calendar.set(Calendar.SECOND, 0);
////        calendar.set(Calendar.MILLISECOND, 0);
//
//
//        Intent intent = new Intent(this,ToDoListAlarmBroadCastReceiver.class);
//        intent.setAction("alarmAction");
//        PendingIntent pendingIntent=PendingIntent.getBroadcast(context,0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
//        alarmManager.set( AlarmManager.RTC_WAKEUP , calendar.getTimeInMillis()+5000 , pendingIntent );
////        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);
    }



    public void initData(Bundle savedInstanceState) {
        setSupportActionBar(toolbar);

        fragmentList.clear();
        fragmentList.add(ToDoFragment.newInstance());
        fragmentList.add(DoneFragment.newInstance());

        if (adapter == null) {
            adapter = new ToDoFragmentAdapter(getSupportFragmentManager(), fragmentList);
        }
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(3);

        tabLayout.setupWithViewPager(viewPager);
    }


    public int getLayoutId() {
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

    @Override
    protected void onStart() {
        super.onStart();
        if (useEventBus()) {
            BusFactory.getBus().register(this);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
     }


    @Override
    protected void onPause() {
        super.onPause();
    }



    public boolean useEventBus() {
        return false;
    }


    public void setListener() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BusFactory.getBus().unregister(this);
    }

}
