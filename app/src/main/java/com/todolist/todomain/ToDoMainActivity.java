package com.todolist.todomain;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import com.todolist.R;
import com.todolist.app.App;
import com.todolist.todomain.fragment.category.CategoryFragment;
import com.todolist.todomain.fragment.done.DoneFragment;
import com.todolist.todomain.fragment.todo.ToDoFragment;
import com.todolist.todomain.fragment.todo.ToDoFragmentAdapter;
import com.todolist.ui.dialog.RewardVideoAndPurchaseDialog;
import com.todolist.util.AdsUtil;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;


public class ToDoMainActivity extends AppCompatActivity
        implements
        AdsUtil.RewardedVideoAdCallBack,
        ToDoFragment.OnFragmentInteractionListener ,
        CategoryFragment.OnFragmentInteractionListener ,
        DoneFragment.OnFragmentInteractionListener
{
    private static final String TAG = ToDoMainActivity.class.getName();


    private final int REQUEST_CODE_CHOOSE=0;

    protected Activity context;

    Toolbar toolbar;

    TabLayout tabLayout;

    ViewPager viewPager;

    AppBarLayout appBarLayout;

    List<Fragment> fragmentList = new ArrayList<>();

    ToDoFragmentAdapter adapter;

    @Inject
    ToDoFragment toDoFragment;

    @Inject
    DoneFragment doneFragment;

    ToDoMainActivityComponent toDoMainActivityComponent;

    private RewardedVideoAd rewardedVideoAd;

    private RewardVideoAndPurchaseDialog rewardVideoAndPurchaseDialog;


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
            //and stretch the viewPager
            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) viewPager.getLayoutParams();
            layoutParams.setMargins(0, 0, 0, toolbar.getMeasuredHeight() + verticalOffset);
            viewPager.requestLayout();

        }

    });

    toDoMainActivityComponent = DaggerToDoMainActivityComponent
            .builder()
            .appComponent(((App)getApplication()).getAppComponent())
            .build();
    toDoMainActivityComponent.inject(this);

    initViewPagerFragments();

    //display Reward Video
        if( AdsUtil.displayRewardedVideoAds( this ) )
    {
        rewardVideoAndPurchaseDialog = new RewardVideoAndPurchaseDialog();
        rewardedVideoAd = AdsUtil.setupRewardedVideoAd(this , this );
    }

}

    @Override
    public void doRewardedVideoAd() {
        if( rewardVideoAndPurchaseDialog != null )
            rewardVideoAndPurchaseDialog.show( this.getSupportFragmentManager(),"rewardVideoAndPurchaseDialog");
    }

    @Override
    public RewardedVideoAd getRewardedVideoAd() {
        return rewardedVideoAd;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //执行逻辑
                    Matisse.from(ToDoMainActivity.this)
                            .choose(MimeType.ofAll())
                            .countable(true)
                            .maxSelectable(1)//由于这里我只需要一张照片，所以最多选择设置为1
//                        .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                            .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                            .thumbnailScale(0.85f)
                            .imageEngine(new GlideEngine())
                            .forResult(REQUEST_CODE_CHOOSE);
                } else {
                    Toast.makeText(this, "You denied the permission", Toast.LENGTH_SHORT).show();
                }

                break;
            default:
        }
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
    }

    public boolean useEventBus() {
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public ToDoMainActivityComponent getToDoMainActivityComponent() {
        return toDoMainActivityComponent;
    }
}
