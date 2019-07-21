package com.todolist.util;

import android.content.Context;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.todolist.R;
import com.todolist.todomain.ToDoMainActivity;

public class AdsUtil {


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
