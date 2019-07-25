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

import java.util.Random;

public class AdsUtil {


    private final static Random BANNER_RANDOM = new Random();

    private final static Random REWARDEDVIDEO_RANDOM = new Random();

    private final static Random INTERSTITIAL_RANDOM = new Random();

    public static boolean displayBannerAds() {
        return BANNER_RANDOM.nextBoolean();
    }

    public static boolean displayRewardedVideoAds() {
        return false;
    }

    public static boolean displayInterstitialAds() {
        return false;
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
        };

        interstitialAd.setAdListener( adListener );

        return interstitialAd;
    }

    public static RewardedVideoAd setupRewardedVideoAd( Context context ) {
        RewardedVideoAd rewardedVideoAd = MobileAds.getRewardedVideoAdInstance( context );

        RewardedVideoAdListener rewardedVideoAdListener = new RewardedVideoAdListener(){

            @Override
            public void onRewardedVideoAdLoaded() {
                Toast.makeText(context,"onRewardedVideoAdLoaded",Toast.LENGTH_SHORT);
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
