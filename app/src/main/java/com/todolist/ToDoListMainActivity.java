package com.todolist;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.todolist.broadcast.ToDoListAlarmBroadCastReceiver;
import com.todolist.event.BusFactory;
import com.todolist.model.ToDoCategory;
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
    private static final String TAG = ToDoListMainActivity.class.getName();

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
    String[] titles = {"ToDo", "Done"};

    ToDoFragmentAdapter adapter;
    ToDoCategory selectedToDoCategory;

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
        categoryButton = (TextView)findViewById(R.id.categoryButton);
        categoryButtonImage = (ImageView)findViewById(R.id.categoryButtonImage);;

        appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                Log.i(TAG , "verticalOffset : " + verticalOffset );
//                ViewGroup.LayoutParams toolbarLayputParam = toolbar.getLayoutParams();
//                Log.i(TAG , "toolbarLayputParam.height : " + toolbarLayputParam.height );
//                Log.i(TAG , "toolbar.getMeasuredHeight() : " + toolbar.getMeasuredHeight() );
//                Log.i(TAG , "toolbar.getTranslationY() : " + toolbar.getTranslationY() );
//                Log.i(TAG , "toolbar.getHeight() : " + toolbar.getHeight() );
//                Log.i(TAG , "toolbar.getBottom() : " + toolbar.getBottom() );
//                Log.i(TAG , "toolbar.getTop() : " + toolbar.getTop() );
//
//                ViewGroup.LayoutParams appBarLayoutLayputParam = appBarLayout.getLayoutParams();
//                Log.i(TAG , "appBarLayoutLayputParam.height : " + appBarLayoutLayputParam.height );
//                Log.i(TAG , "appBarLayout.getMeasuredHeight() : " + appBarLayout.getMeasuredHeight() );
//                Log.i(TAG , "appBarLayout.getTranslationY() : " + appBarLayout.getTranslationY() );
//                Log.i(TAG , "appBarLayout.getHeight() : " + appBarLayout.getHeight() );
//                Log.i(TAG , "appBarLayout.getBottom() : " + appBarLayout.getBottom() );
                Log.i(TAG , "appBarLayout.getTop() : " + appBarLayout.getTop() );

                ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) viewPager.getLayoutParams();
                layoutParams.setMargins(0, 0, 0, toolbar.getMeasuredHeight() + verticalOffset);
                viewPager.requestLayout();

            }

        });

        initData(savedInstanceState);
        initService();
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
//                ObjectAnimator
//                        .ofFloat(appBarLayout, "Y", 0f, -147f)
//                        .setDuration(1000)
//                        .start();

                CategoryFragment categoryFragment = CategoryFragment.newInstance( selectedToDoCategory );
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

        String[] titles = new String[]{getString(R.string.tab_title_todo) , getString(R.string.tab_title_done)};

        if (adapter == null) {
            adapter = new ToDoFragmentAdapter(getSupportFragmentManager(), fragmentList, titles);
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
