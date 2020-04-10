package com.todolist.ui.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.todolist.R;
import com.todolist.util.AdsUtil;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

public class RewardVideoAndPurchaseDialog extends AppCompatDialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.dialog_reward_video_and_purchase, null, false);

        AlertDialog alertDialog = builder
                .setView(view)
                .setCancelable(true)
                .create();

        ImageView rewardVideoImageView = (ImageView)view.findViewById(R.id.rewardVideoImage);
        rewardVideoImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( getActivity() instanceof AdsUtil.RewardedVideoAdCallBack ) {
                    RewardedVideoAd rewardedVideoAd = ((AdsUtil.RewardedVideoAdCallBack) getActivity()).getRewardedVideoAd();
                    if (rewardedVideoAd != null && rewardedVideoAd.isLoaded()) {
                        rewardedVideoAd.show();
                        alertDialog.dismiss();
                    }
                }
            }
        });

        return alertDialog;
    }

}
