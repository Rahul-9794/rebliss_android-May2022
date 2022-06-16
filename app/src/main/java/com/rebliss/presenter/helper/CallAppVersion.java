package com.rebliss.presenter.helper;

import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import com.google.gson.Gson;
import com.rebliss.R;
import com.rebliss.data.perf.MySingleton;
import com.rebliss.domain.constant.Constant;
import com.rebliss.domain.model.ErrorBody;
import com.rebliss.domain.model.appversion.AppVersionResponce;
import com.rebliss.domain.model.fospos.GetFosPos;
import com.rebliss.presenter.retrofit.ApiClient;
import com.rebliss.presenter.retrofit.ApiInterface;

import java.io.IOException;
import java.net.ConnectException;
import java.net.NoRouteToHostException;
import java.net.SocketTimeoutException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CallAppVersion {
    private Context context;
    private MySingleton mySingleton;
    private Network network;
    String currentVersion = "";

    public void CallAppVersion(Context context) {
        this.context = context;
        mySingleton = new MySingleton(context);
        network = new Network();
        try {
            currentVersion = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
            Log.e("TAG>>>>>>>>>>", "CallAppVersion: "+currentVersion );
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (network.isNetworkConnected(context)) {
            sendVersion(currentVersion);
        }
    }


    private void sendVersion(String version) {

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        final Call<AppVersionResponce> call = apiService.postAppVersion(mySingleton.getData(Constant.TOKEN_BASE_64), version);
        call.enqueue(new Callback<AppVersionResponce>() {
            @Override
            public void onResponse(Call<AppVersionResponce> call, Response<AppVersionResponce> response) {
                if (response.isSuccessful()) {
                    Log.e("TAG", "onResponse: "+response.body().toString() );
                    if (response.code() >= 200 && response.code() < 700) {
                        if (response.code() == 200) {
                            if (response.body().getStatus() == 1) {

                            }
//
                        }
                    }
                } else {

                    try {
                        ErrorBody errorBody = new ErrorBody();
                        Gson gson = new Gson();
                        errorBody = gson.fromJson(response.errorBody().string(), ErrorBody.class);
                        if (errorBody.getName().equalsIgnoreCase(context.getString(R.string.unAuthorisedUser))) {
                            Logout.Login(context);
                        }
                    } catch (Exception e) {

                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<AppVersionResponce> call, Throwable t) {
                if (t != null && (t instanceof IOException || t instanceof SocketTimeoutException
                        || t instanceof ConnectException || t instanceof NoRouteToHostException
                        || t instanceof SecurityException)) {

                }
            }
        });
    }
}
