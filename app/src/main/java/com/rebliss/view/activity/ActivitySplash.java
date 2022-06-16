package com.rebliss.view.activity;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.gson.Gson;
import com.rebliss.utils.App;
import com.rebliss.R;
import com.rebliss.data.perf.MySingleton;
import com.rebliss.domain.constant.Constant;
import com.rebliss.domain.model.ErrorBody;
import com.rebliss.domain.model.profile.Data;
import com.rebliss.domain.model.profile.ProfileResponce;
import com.rebliss.presenter.helper.DisplaySnackBar;
import com.rebliss.presenter.helper.Logout;
import com.rebliss.presenter.helper.Network;
import com.rebliss.presenter.helper.TextUtil;
import com.rebliss.presenter.retrofit.ApiClient;
import com.rebliss.presenter.retrofit.ApiInterface;
import com.rebliss.view.activity.employee.EmployeeAcivity;
import com.rebliss.view.activity.new_changes.LoginSignupChooserActivity;

import java.io.File;
import java.io.IOException;
import java.net.ConnectException;
import java.net.NoRouteToHostException;
import java.net.SocketTimeoutException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivitySplash extends AppCompatActivity {

    TextView text_create, text_loading;
    MySingleton mySingleton;
    private Network network;
    DisplaySnackBar displaySnackBar;
    Data profileData;
    BroadcastReceiver br;
    public static File dir = new File(new File(Environment.getExternalStorageDirectory(), "rebliss"), "rebliss");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.content_activity_splash);
        mySingleton = new MySingleton(this);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp((Application) getApplicationContext());
        logSentFriendRequestEvent();
        Log.i("Token", mySingleton.getData(Constant.TOKEN_BASE_64));
        Log.i("Token", mySingleton.getData(Constant.TOKEN));
        Log.i("test", mySingleton.getData(Constant.USER_ID));
        Log.i("test1", mySingleton.getData(Constant.USER_GROUP_ID));
        Log.i("test2", mySingleton.getData(Constant.USER_FOS_TYPE));
        text_create = findViewById(R.id.text_create);
        text_loading = findViewById(R.id.text_loading);
        text_create.setTypeface(App.ARIAL_REGULAR);
        text_loading.setTypeface(App.ARIAL_REGULAR);


        network = new Network();
        displaySnackBar = new DisplaySnackBar(ActivitySplash.this);


        if (!TextUtil.isStringNullOrBlank(mySingleton.getData(Constant.TOKEN)) && mySingleton.getData(Constant.USER_VERIFYED).equalsIgnoreCase("1")) {
            if (network.isNetworkConnected(ActivitySplash.this)) {

                if (mySingleton.getData(Constant.USER_GROUP_ID).equalsIgnoreCase(Constant.EMPLOYEE_GROUP_ID)) {
                    if (mySingleton.getData(Constant.USER_PROFILE_VERIFYED).equalsIgnoreCase("1")) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                Intent callOtp = new Intent(ActivitySplash.this, EmployeeAcivity.class);
                                startActivity(callOtp);
                                finish();
                            }
                        }, 3000);



                    }

                } else {
                    if (network.isNetworkConnected(ActivitySplash.this)) {
                        callProfileAPI();
                    }
                    else
                        {
                            displaySnackBar.DisplaySnackBar(Constant.NETWIRK_ERROR, Constant.TYPE_ERROR);
                        }
                }
            } else {




                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent dashboard = new Intent(ActivitySplash.this, LoginSignupChooserActivity.class);
                        dashboard.putExtra(Constant.UNAUTHORISE_TOKEN, "0");
                        startActivity(dashboard);
                        finish();
                    }
                }, 3000);


            }

        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent dashboard = new Intent(ActivitySplash.this, LoginSignupChooserActivity.class);
                    dashboard.putExtra(Constant.UNAUTHORISE_TOKEN, "0");
                    startActivity(dashboard);
                    finish();
                }
            }, 3000);


        }
    }

    private void callIntent() {
        if (mySingleton.getData(Constant.USER_GROUP_ID).equalsIgnoreCase("1") || mySingleton.getData(Constant.USER_GROUP_ID).equalsIgnoreCase("3")) {
            if (profileData.getProfile_verified().equalsIgnoreCase("0")) {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("splash", "callIntent to DashboardCPDetail" );
                        Intent dashboard = new Intent(ActivitySplash.this, DashboardCPDetails.class);
                        dashboard.putExtra("call_from", "0");
                        startActivity(dashboard);
                        finish();

                    }
                }, 3000);


            } else {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        Intent callDashboar = new Intent(ActivitySplash.this, ActivityDashboard.class);
                        callDashboar.putExtra("profile_varified", profileData.getProfile_verified());
                        startActivity(callDashboar);
                        finish();
                    }
                }, 3000);

            }
        } else if (mySingleton.getData(Constant.USER_GROUP_ID).equalsIgnoreCase("2")) {
            Log.e("TAG", "callIntent: "+ mySingleton.getData(Constant.USER_FOS_TYPE));
            if (profileData.getProfile_verified().equalsIgnoreCase("0")) {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        Intent dashboard = new Intent(ActivitySplash.this, ActivityDashboard.class);
                        dashboard.putExtra("profile_varified", profileData.getProfile_verified());
                        startActivity(dashboard);
                        finish();


                    }
                }, 3000);


            }
            else if(profileData.getProfile_verified().equalsIgnoreCase("2"))
            {     if(mySingleton.getData(Constant.USER_FOS_TYPE).equals("rBS")) {





                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent callDashboar = new Intent(getApplicationContext(), ActivityDashboard.class);
                        callDashboar.putExtra("profile_varified", profileData.getProfile_verified());
                        callDashboar.putExtra("directnavigation", "directnavs");
                        Log.e("TAG", "callIntentsfasfa: " );
                        startActivity(callDashboar);
                        finish();

                    }
                }, 3000);


            }
            else {




                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent callDashboar = new Intent(getApplicationContext(), ActivityDashboard.class);
                        callDashboar.putExtra("profile_varified", profileData.getProfile_verified());
                        callDashboar.putExtra("directnavigation", "directnav");
                        startActivity(callDashboar);
                        finish();

                    }
                }, 3000);


            }

            }
            else {


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        Intent callDashboar = new Intent(getApplicationContext(), ActivityDashboard.class);
                        callDashboar.putExtra("profile_varified", profileData.getProfile_verified());
                        callDashboar.putExtra("directnavigation", "directnav");
                        startActivity(callDashboar);
                        finish();

                    }
                }, 3000);



            }
        }
        else if (mySingleton.getData(Constant.USER_GROUP_ID).equalsIgnoreCase("4")) {

            if (profileData.getProfile_verified().equalsIgnoreCase("0")) {



                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {


                        Intent dashboard = new Intent(ActivitySplash.this, DashboardSuperCPDetails.class);
                        dashboard.putExtra("call_from", "0");
                        startActivity(dashboard);
                        finish();
                    }
                }, 3000);

            } else {



                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent callDashboar = new Intent(getApplicationContext(), ActivityDashboard.class);
                        callDashboar.putExtra("profile_varified", profileData.getProfile_verified());
                        startActivity(callDashboar);
                        finish();

                    }
                }, 3000);


            }
        } else {

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent dashboard = new Intent(ActivitySplash.this, LoginSignupChooserActivity.class);
                    dashboard.putExtra(Constant.UNAUTHORISE_TOKEN, "0");
                    startActivity(dashboard);
                    finish();

                }
            }, 3000);


        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void callProfileAPI() {

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        final Call<ProfileResponce> call = apiService.getProfile(mySingleton.getData(Constant.TOKEN_BASE_64));
        call.enqueue(new Callback<ProfileResponce>() {
            @Override
            public void onResponse(Call<ProfileResponce> call, Response<ProfileResponce> response) {

                if (response.isSuccessful()) {
                    if (response.code() >= 200 && response.code() < 700) {
                        if (response.code() == 200) {
                            if (response.body().getStatus() == 1) {
                                if (response.body().getData() != null) {
                                    Gson gson = new Gson();
                                    String json = gson.toJson(response.body(), ProfileResponce.class);
                                    Log.i("Jsondata ", json);
                                    profileData = response.body().getData();
                                    mySingleton.saveData(Constant.USER_GROUP_ID, response.body().getData().getGroup_id());
                                    mySingleton.saveData(Constant.USER_GROUP_DETAIL_ID, response.body().getData().getGroup_detail_id());
                                    mySingleton.saveData(Constant.USER_FOS_TYPE, response.body().getData().getFos_type());
                                    callIntent();
                                }
                            } else if (response.body().getStatus() == 0) {
                                Log.e("TAG", "onResponse: status 0 splash" );;
                            }
                        }

                    } else {
                        Log.e("TAG", "onResponse: else splash " );

                    }
                } else {
                    try {
                        ErrorBody errorBody = new ErrorBody();
                        Gson gson = new Gson();
                        errorBody = gson.fromJson(response.errorBody().string(), ErrorBody.class);
                        displaySnackBar.DisplaySnackBar(errorBody.getMessage(), Constant.TYPE_ERROR);
                        Toast.makeText(ActivitySplash.this, errorBody.getMessage(), Toast.LENGTH_SHORT).show();
                        if (errorBody.getMessage().contains("invalid")) {
                            Logout.Login(ActivitySplash.this);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ProfileResponce> call, Throwable t) {
                if (t != null && (t instanceof IOException || t instanceof SocketTimeoutException
                        || t instanceof ConnectException || t instanceof NoRouteToHostException
                        || t instanceof SecurityException)) {

                }
            }
        });
    }
    public void onPause() {
        super.onPause();

       // unregisterReceiver(br);
    }

    @Override
    protected void onStart() {
        super.onStart();


    }

    public void logSentFriendRequestEvent () {
        Log.e("TAG", "logSentFriendRequestEvent: " );
        AppEventsLogger.newLogger(this).logEvent("sentFriendRequest");
    }
}
