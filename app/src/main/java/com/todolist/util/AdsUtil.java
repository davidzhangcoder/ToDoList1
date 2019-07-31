package com.todolist.util;

import android.content.Context;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.todolist.R;
import com.todolist.todomain.ToDoMainActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class AdsUtil {


    private static final String REWARDED_VIDEO_DATE = "REWARDED_VIDEO_DATE";

    private final static Random BANNER_RANDOM = new Random();

    private final static Random REWARDEDVIDEO_RANDOM = new Random();

    private final static Random INTERSTITIAL_RANDOM = new Random();

    public interface RewardedVideoAdCallBack {
        public void doRewardedVideoAd();

        public RewardedVideoAd getRewardedVideoAd();
    }

    public static boolean displayBannerAds( Context context ) {
        return BANNER_RANDOM.nextBoolean() && isAllowAds(context);
    }

    public static boolean displayRewardedVideoAds( Context context ) {
        return REWARDEDVIDEO_RANDOM.nextBoolean() && isAllowAds(context);
    }

    public static boolean displayInterstitialAds( Context context ) {
        return INTERSTITIAL_RANDOM.nextBoolean() && isAllowAds(context);
    }

    public static boolean isAllowAds( Context context ) {
        Calendar rewardedVideoDate = getRewardedVideoDate(context);
        if( rewardedVideoDate != null && DateUtil.sameDay(rewardedVideoDate , Calendar.getInstance()) ) {
            return false;
        }
        return true;
    }

    public static Calendar getRewardedVideoDate(  Context context ) {
        String rewaredVideoDate = SharedPrefUtils.getStringData(context, REWARDED_VIDEO_DATE );
        if( rewaredVideoDate != null && !rewaredVideoDate.trim().equals("")) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date date = simpleDateFormat.parse( rewaredVideoDate );
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                return calendar;
            } catch (ParseException e) {
                return null;
            }
        }
        else
            return null;
    }

    public static InterstitialAd setupInterstitialAd( Context context ) {
        InterstitialAd interstitialAd = new InterstitialAd(context);
        interstitialAd.setAdUnitId( context.getString(R.string.admon_adunit_interstitial) );

        interstitialAd.loadAd(new AdRequest.Builder().build());

        AdListener adListener = new AdListener() {
            @Override
            public void onAdClosed() {
                interstitialAd.loadAd(new AdRequest.Builder().build());
            }

            @Override
            public void onAdLoaded() {

            }
        };

        interstitialAd.setAdListener( adListener );

        return interstitialAd;
    }

    public static RewardedVideoAd setupRewardedVideoAd( Context context , RewardedVideoAdCallBack rewardedVideoAdCallBack ) {
        RewardedVideoAd rewardedVideoAd = MobileAds.getRewardedVideoAdInstance( context );

        RewardedVideoAdListener rewardedVideoAdListener = new RewardedVideoAdListener(){

            @Override
            public void onRewardedVideoAdLoaded() {
                Toast.makeText(context,"onRewardedVideoAdLoaded",Toast.LENGTH_SHORT);
                rewardedVideoAdCallBack.doRewardedVideoAd();
            }

            @Override
            public void onRewardedVideoAdOpened() {

            }

            @Override
            public void onRewardedVideoStarted() {

            }

            @Override
            public void onRewardedVideoAdClosed() {
                // Load the next rewarded video ad.
                loadRewardedVideoAd( rewardedVideoAd , context );
            }

            @Override
            public void onRewarded(RewardItem rewardItem) {
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String dateString = simpleDateFormat.format( calendar.getTime() );
                SharedPrefUtils.saveData( context , REWARDED_VIDEO_DATE , dateString);

                Toast.makeText(context,"onRewarded",Toast.LENGTH_SHORT);
            }

            @Override
            public void onRewardedVideoAdLeftApplication() {

            }

            @Override
            public void onRewardedVideoAdFailedToLoad(int i) {

            }

            @Override
            public void onRewardedVideoCompleted() {
                Toast.makeText(context,"onRewardedVideoCompleted",Toast.LENGTH_SHORT);
            }
        };

        rewardedVideoAd.setRewardedVideoAdListener( rewardedVideoAdListener );

        loadRewardedVideoAd( rewardedVideoAd , context );

        return rewardedVideoAd;
    }

    private static void loadRewardedVideoAd( RewardedVideoAd rewardedVideoAd , Context context) {
//        context.getString(R.string.admob_adunit_rewardvideo)
        rewardedVideoAd.loadAd( context.getString(R.string.admob_adunit_rewardvideo) , new AdRequest.Builder().build() );
    }


}
