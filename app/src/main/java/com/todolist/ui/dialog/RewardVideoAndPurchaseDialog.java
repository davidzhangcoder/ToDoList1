package com.todolist.ui.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.todolist.R;
import com.todolist.util.AdsUtil;

public class RewardVideoAndPurchaseDialog extends AppCompatDialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        RewardedVideoAd rewardedVideoAd = AdsUtil.setupRewardedVideoAd(getActivity());

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.dialog_reward_video_and_purchase, null, false);

        AlertDialog alertDialog = builder
                .setView(view)
                .setCancelable(true)
                .create();

        Button rewardVideoButton = (Button)view.findViewById(R.id.rewardVideoButton);
        rewardVideoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( rewardedVideoAd != null && rewardedVideoAd.isLoaded() )
                {
                    rewardedVideoAd.show();
                    alertDialog.dismiss();
                }
            }
        });

        return alertDialog;
    }
}
