package com.todolist.todomain;

import android.Manifest;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.todolist.R;
import com.todolist.app.App;
import com.todolist.event.BusFactory;
import com.todolist.todomain.fragment.category.CategoryFragment;
import com.todolist.todomain.fragment.done.DoneFragment;
import com.todolist.todomain.fragment.todo.ToDoFragment;
import com.todolist.todomain.fragment.todo.ToDoFragmentAdapter;
import com.todolist.ui.dialog.RewardVideoAndPurchaseDialog;
import com.todolist.util.AdsUtil;
import com.todolist.util.SharedPrefUtils;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;


import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;


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
//        if( AdsUtil.displayRewardedVideoAds( this ) )
    {
//            String rewaredVideoDate = SharedPrefUtils.getStringData(this, REWARDED_VIDEO_DATE );
        rewardVideoAndPurchaseDialog = new RewardVideoAndPurchaseDialog();
        rewardedVideoAd = AdsUtil.setupRewardedVideoAd(this , this );
//            rewardVideoAndPurchaseDialog.show(ToDoMainActivity.this.getSupportFragmentManager(), "rewardVideoAndPurchaseDialog");
    }


//        MobileAds.initialize(this, "ca-app-pub-6130191480576260~1951770609");
//
//        AdView mAdView = findViewById(R.id.adView);
////        mAdView.setAdSize(AdSize.BANNER);
//        Real ID : ca-app-pub-6130191480576260/5998826825
////        mAdView.setAdUnitId("ca-app-pub-3940256099942544/6300978111"); //Sample ID : ca-app-pub-3940256099942544/6300978111
//        AdRequest adRequest = new AdRequest.Builder().build();
//        mAdView.loadAd(adRequest);


    // test
    InterstitialAd interstitialAd = AdsUtil.setupInterstitialAd(this);
    Button button = new Button( this );
    button.setText("test button");
    button.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Rewared Video
//                if( rewardedVideoAd.isLoaded() ) {
//                    rewardedVideoAd.show();
//                }

            //Rewarded Video Dialog
//                RewardVideoAndPurchaseDialog rewardVideoAndPurchaseDialog = new RewardVideoAndPurchaseDialog();
//                rewardVideoAndPurchaseDialog.show(ToDoMainActivity.this.getSupportFragmentManager(), "rewardVideoAndPurchaseDialog");

            //Interstitial
//                if( interstitialAd != null && interstitialAd.isLoaded() ) {
//                    interstitialAd.show();
//                }

            //Matisse
//                if(ContextCompat.checkSelfPermission(ToDoMainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
//                    ActivityCompat.requestPermissions(ToDoMainActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
//                }else{
//                    //执行逻辑
//                    Matisse.from(ToDoMainActivity.this)
//                            .choose(MimeType.ofAll())
//                            .countable(true)
//                            .maxSelectable(1)//由于这里我只需要一张照片，所以最多选择设置为1
////                        .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
//                            .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
//                            .thumbnailScale(0.85f)
//                            .imageEngine(new GlideEngine())
//                            .forResult(REQUEST_CODE_CHOOSE);
//                }

        }
    });
    toolbar.addView( button );


//        rewardedVideoAd = MobileAds.getRewardedVideoAdInstance( this );
//        RewardedVideoAdListener rewardedVideoAdListener = new RewardedVideoAdListener(){
//
//            @Override
//            public void onRewardedVideoAdLoaded() {
//                Toast.makeText(ToDoMainActivity.this,"onRewardedVideoAdLoaded",Toast.LENGTH_SHORT);
//            }
//
//            @Override
//            public void onRewardedVideoAdOpened() {
//
//            }
//
//            @Override
//            public void onRewardedVideoStarted() {
//
//            }
//
//            @Override
//            public void onRewardedVideoAdClosed() {
//                // Load the next rewarded video ad.
//                loadRewardedVideoAd();
//            }
//
//            @Override
//            public void onRewarded(RewardItem rewardItem) {
//                Toast.makeText(ToDoMainActivity.this,"onRewarded",Toast.LENGTH_SHORT);
//            }
//
//            @Override
//            public void onRewardedVideoAdLeftApplication() {
//
//            }
//
//            @Override
//            public void onRewardedVideoAdFailedToLoad(int i) {
//
//            }
//
//            @Override
//            public void onRewardedVideoCompleted() {
//                Toast.makeText(ToDoMainActivity.this,"onRewardedVideoCompleted",Toast.LENGTH_SHORT);
//            }
//        };
//        rewardedVideoAd.setRewardedVideoAdListener( rewardedVideoAdListener );
//
//        loadRewardedVideoAd();

}

    @Override
    public void doRewardedVideoAd() {
        if( rewardVideoAndPurchaseDialog != null )
            rewardVideoAndPurchaseDialog.show( this.getSupportFragmentManager(),"rewardVideoAndPurchaseDialog");
    }

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

    private void loadRewardedVideoAd() {
        rewardedVideoAd.loadAd(getString(R.string.admob_adunit_rewardvideo),
                new AdRequest.Builder().build());
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

    public ToDoMainActivityComponent getToDoMainActivityComponent() {
        return toDoMainActivityComponent;
    }
}
