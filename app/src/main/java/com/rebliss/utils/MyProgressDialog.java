package com.rebliss.utils;

import android.app.ProgressDialog;
import android.content.Context;



public class MyProgressDialog {

    private static ProgressDialog progress;

    public static void showProgress(Context context) {

        progress = new ProgressDialog(context);
        progress.setMessage("Please Wait");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setCancelable(false);
        progress.show();
    }

    public static void show(Context context) {

        progress = new ProgressDialog(context);
        progress.setMessage("Please wait while image upload is in progress");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setCancelable(false);
        progress.show();
    }


    public static void setMsg(String msg) {

       progress.setMessage(msg);
    }
    public static void dismissProgress() {
        if (progress.isShowing())
            progress.dismiss();
    }

}
