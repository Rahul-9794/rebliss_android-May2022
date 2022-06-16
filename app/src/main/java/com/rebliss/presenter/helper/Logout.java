package com.rebliss.presenter.helper;

import android.content.Context;
import android.content.Intent;

import com.rebliss.data.perf.MySingleton;
import com.rebliss.domain.constant.Constant;
import com.rebliss.view.activity.ActivityLogin;



public class Logout {
    public static void Login(Context context){
        MySingleton mySingleton =  new MySingleton(context);
        String token = mySingleton.getData(Constant.DEVICE_FCM_TOKEN);
        mySingleton.clearData();
        mySingleton.saveData(Constant.DEVICE_FCM_TOKEN, token);
        Intent newIntent = new Intent(context,ActivityLogin.class);
        newIntent.putExtra(Constant.UNAUTHORISE_TOKEN, "1");
        newIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        newIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(newIntent);
    }
}
