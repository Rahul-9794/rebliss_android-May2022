package com.rebliss.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class UpdateReceiver extends BroadcastReceiver {
    private static UpdateListener mListener;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (mListener!=null) {
            mListener.UpdateListener("");
        }
    }

    public static void bindListener(UpdateListener listener) {
        mListener = listener;
    }
}
