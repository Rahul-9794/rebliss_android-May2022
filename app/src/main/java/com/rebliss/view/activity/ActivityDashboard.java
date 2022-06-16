package com.rebliss.view.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.rebliss.utils.App;
import com.rebliss.R;
import com.rebliss.data.perf.MySingleton;
import com.rebliss.domain.constant.Constant;
import com.rebliss.domain.model.ErrorBody;
import com.rebliss.domain.model.KycResponse;
import com.rebliss.domain.model.fosattendancedetail.FosAttendanceStatus;
import com.rebliss.domain.model.logout.LogoutResponce;
import com.rebliss.domain.model.notificationlist.Desc;
import com.rebliss.domain.model.notificationlist.NotificationListResponse;
import com.rebliss.domain.model.payment.SuccessResponse;
import com.rebliss.presenter.helper.DisplaySnackBar;
import com.rebliss.presenter.helper.Logout;
import com.rebliss.presenter.helper.Network;
import com.rebliss.presenter.retrofit.ApiClient;
import com.rebliss.presenter.retrofit.ApiInterface;
import com.rebliss.utils.CheckAppVersion;
import com.rebliss.utils.UpdateReceiver;
import com.rebliss.view.activity.cpdashboardforfos.dialogsmodel.CpListResponse;
import com.rebliss.view.activity.cppayment.InvoiceListingAcitivity;
import com.rebliss.view.activity.notification.NotificationListActivity;
import com.rebliss.view.fragment.ChangePassword;
import com.rebliss.view.fragment.DashboardCPDetailFragment;
import com.rebliss.view.fragment.FosDashboardFrag;
import com.rebliss.view.fragment.FragmentDashboard;
import com.rebliss.view.fragment.KycFragment;

import java.io.IOException;
import java.net.ConnectException;
import java.net.NoRouteToHostException;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;
import eu.dkaratzas.android.inapp.update.Constants;
import eu.dkaratzas.android.inapp.update.InAppUpdateManager;
import eu.dkaratzas.android.inapp.update.InAppUpdateStatus;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ActivityDashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, InAppUpdateManager.InAppUpdateHandler, BottomNavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = ActivityDashboard.class.getSimpleName();
    public RecyclerView recyclerView;
    Fragment fragment = null;
    Class fragmentClass = null;
    private MySingleton mySingleton;
    private TextView tvNotificationCount;
    private Network network;
    Intent intent;
    String status;
    private Dialog dialog;
    String profile_varified = "0";
    public static int fragmentSelected = 0;
    private KProgressHUD kProgressHUD;
    Integer value = 0;
    MenuItem item;
    ImageView imgAdd;
    String currentVersion = "";
    NavigationView navigationView;
    ImageView ivLogout;
    private DisplaySnackBar displaySnackBar;
    private BroadcastReceiver mReceiver;
    InAppUpdateManager inAppUpdateManager;
    String comefrom ="";
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard3);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.nav_home);
        Log.e(TAG, "ActivityDashboard: " );

        displaySnackBar = new DisplaySnackBar(this);
        network = new Network();
        mySingleton = new MySingleton(ActivityDashboard.this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ivLogout = findViewById(R.id.ivLogout);
        Bundle bundle = getIntent().getExtras();
       if(bundle !=null){

            comefrom = bundle.getString("directnavigation");

        }
        if(comefrom==null){
            comefrom = "";
        }
        if(comefrom!=null && comefrom.equals("directnav") && mySingleton.getData(Constant.USER_GROUP_ID).equalsIgnoreCase(Constant.FOS_GROUP_ID))
        {
            try {
                Log.e(TAG, "onCreate: "+comefrom );
                fragmentClass = FosDashboardFrag.class;
                fragment = (Fragment) fragmentClass.newInstance();
                Bundle bundles = new Bundle();
                bundles.putString("profile_varified", profile_varified);
                bundles.putString("comefrom", "directnav");
                mySingleton.saveData("navigatetodashboard","nav");
                fragment.setArguments(bundles);

                try {

                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commitAllowingStateLoss();
                }
                catch (IllegalStateException e){
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if(comefrom!=null && comefrom.equals("directnavs") && mySingleton.getData(Constant.USER_GROUP_ID).equalsIgnoreCase(Constant.FOS_GROUP_ID))
        {
            try {
                Log.e(TAG, "onCreate: "+comefrom );
                fragmentClass = FosDashboardFrag.class;
                fragment = (Fragment) fragmentClass.newInstance();
                Bundle bundles = new Bundle();
                bundles.putString("profile_varified", profile_varified);
                bundles.putString("comefrom", "directnavs");
                mySingleton.saveData("navigatetodashboard","nav");
                fragment.setArguments(bundles);

                try {

                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commitAllowingStateLoss();
                }
                catch (IllegalStateException e){
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        else if (network.isNetworkConnected(ActivityDashboard.this)) {
            callCheckKyc();
        }
        else
        {
            displaySnackBar.DisplaySnackBar(Constant.NETWIRK_ERROR, Constant.TYPE_ERROR);
        }

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w(TAG, "getInstanceId failed", task.getException());
                        return;
                    }

                    String token = task.getResult().getToken();
                    mySingleton.saveData(Constant.DEVICE_FCM_TOKEN, token);
                    Log.i("tokensssssss", token);

                });
        if (network.isNetworkConnected(ActivityDashboard.this)) {
            callAPPVersionAPI();
        }
        else
            {
                displaySnackBar.DisplaySnackBar(Constant.NETWIRK_ERROR, Constant.TYPE_ERROR);
            }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
                if (mySingleton.getData(Constant.USER_GROUP_ID).equalsIgnoreCase(Constant.FOS_GROUP_ID)) {
                    value = 5;
                    getstatus();
                }
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
            }

            @Override
            public void onDrawerStateChanged(int newState) {
            }
        });

        navigationView = findViewById(R.id.nav_view);
        Menu menu1 = navigationView.getMenu();
        menu1.findItem(R.id.nav_showstatus).setTitle("Attendance Status(" + status + ")");

        menu1.findItem(R.id.nav_invoice).setVisible(false);
        if (!mySingleton.getData(Constant.USER_GROUP_ID).equalsIgnoreCase(Constant.FOS_GROUP_ID)) {
            menu1.findItem(R.id.nav_showstatus).setVisible(false);

        }
        if (mySingleton.getData(Constant.USER_GROUP_ID).equalsIgnoreCase(Constant.FOS_GROUP_ID)) {
            menu1.findItem(R.id.nav_showstatus).setVisible(false);
            menu1.findItem(R.id.nav_camera).setVisible(false);
            ivLogout.setVisibility(View.GONE);

        }
        if (mySingleton.getData(Constant.USER_GROUP_ID).equalsIgnoreCase(Constant.CP_GROUP_ID)) {
            menu1.findItem(R.id.nav_invoice).setVisible(true);
            menu1.findItem(R.id.nav_camera).setVisible(true);
            menu1.findItem(R.id.nav_camera).setTitle("View Profie");
            ivLogout.setVisibility(View.VISIBLE);

        }
        if (mySingleton.getData(Constant.USER_GROUP_ID).equalsIgnoreCase(Constant.SUPER_CP_GROUP_ID)){
            menu1.findItem(R.id.nav_invoice).setVisible(true);
            menu1.findItem(R.id.nav_camera).setVisible(true);
            menu1.findItem(R.id.nav_camera).setTitle("View Profie");
            ivLogout.setVisibility(View.VISIBLE);

        }

        if (mySingleton.getData(Constant.USER_GROUP_ID).equalsIgnoreCase(Constant.POS_GROUP_ID)){
            menu1.findItem(R.id.nav_invoice).setVisible(true);
            menu1.findItem(R.id.nav_camera).setVisible(true);
            ivLogout.setVisibility(View.VISIBLE);

        }
        try {
            currentVersion = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            Menu menu = navigationView.getMenu();
            menu.findItem(R.id.nav_appVersion).setTitle("App version: " + currentVersion);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        ImageView imageView = headerView.findViewById(R.id.imageView);
        TextView textTitle = headerView.findViewById(R.id.textTitle);
        TextView textDetails = headerView.findViewById(R.id.textDetails);
        tvNotificationCount = findViewById(R.id.tvNotificationCount);

        textTitle.setTypeface(App.LATO_REGULAR);
        textDetails.setTypeface(App.LATO_REGULAR);

        textTitle.setText(mySingleton.getData(Constant.USER_FIRST_NAME) + " " + mySingleton.getData(Constant.USER_LAST_NAME));
        textDetails.setText(mySingleton.getData(Constant.USER_PHONE));
        intent = getIntent();
        profile_varified = intent.getStringExtra("profile_varified");
        imgAdd = findViewById(R.id.imgAdd);

        if (mySingleton.getData(Constant.USER_GROUP_ID).equalsIgnoreCase(Constant.FOS_GROUP_ID)) {

            Date c = Calendar.getInstance().getTime();
            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
            String formattedDate = df.format(c);
            if (mySingleton.getData(Constant.IS_TODAY_FIRST) == null || mySingleton.getData(Constant.IS_TODAY_FIRST).isEmpty() || !mySingleton.getData(Constant.IS_TODAY_FIRST).equals(formattedDate)) {
                mySingleton.saveData(Constant.IS_TODAY_FIRST, formattedDate);
 if(profile_varified==null)
 {
     profile_varified ="";
 }
                if (profile_varified.equalsIgnoreCase("1")&&
                        !mySingleton.getData(Constant.USER_UNIQUE_REF_CODE).contains("rBM")) {
                    opendialog();
                }
            }


        }

        if (item != null) {
            if (!mySingleton.getData(Constant.USER_PROFILE_VERIFYED).equalsIgnoreCase("1")) {
                item.setVisible(false);
            }
        }

        imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent notificationIntent = new Intent(ActivityDashboard.this, NotificationListActivity.class);
                startActivity(notificationIntent);


            }
        });

        findViewById(R.id.ivLogout).setOnClickListener(view -> {
            if (network.isNetworkConnected(ActivityDashboard.this)) {
                callLogout();
            }
            else
                {
                    displaySnackBar.DisplaySnackBar(Constant.NETWIRK_ERROR, Constant.TYPE_ERROR);
                }
        });

        //update method
        inAppUpdateManager = InAppUpdateManager.Builder(this,101).resumeUpdates(true).mode(Constants.UpdateMode.IMMEDIATE)
                .snackBarAction("An Update has just been downloaded")
                .snackBarAction("RESTART").handler(this);
        inAppUpdateManager.checkForAppUpdate();

    }


    public void setLanguage(Activity activity,String language){
        Locale locale = new Locale(language);
        Resources resources = activity.getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(locale);
        resources.updateConfiguration(configuration,resources.getDisplayMetrics());
    }

    private void callAPPVersionAPI()
    {
        try {
            currentVersion = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            Log.e(TAG, "CallAppVersion: "+currentVersion );
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        Log.i(TAG, "callAPPVersionAPI: "+currentVersion);
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            final Call<SuccessResponse> call = apiService.getAppVersion(mySingleton.getData("id"),currentVersion);
            call.enqueue(new Callback<SuccessResponse>() {
                @Override
                public void onResponse(Call<SuccessResponse> call, Response<SuccessResponse> response) {

                    if (response.isSuccessful()) {
                        Log.e(TAG, "onResponse: "+response.body().getStatus().toString() );
                        if(response.body().getStatus()==1)
                        {
                            Log.e(TAG, "Version Updated successfully");
                        }
                        else{

                        }

                    }
                }

                @Override
                public void onFailure(Call<SuccessResponse> call, Throwable t) {
                    if (t != null && (t instanceof IOException || t instanceof SocketTimeoutException
                            || t instanceof ConnectException || t instanceof NoRouteToHostException
                            || t instanceof SecurityException)) {

                    }
                }
            });
        }


    private void callCheckKyc() {
        kProgressHUD = KProgressHUD.create(ActivityDashboard.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .setWindowColor(getResources().getColor(R.color.progressbar_color))
                .show();

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        final Call<KycResponse> call = apiService.checkKyc(mySingleton.getData(Constant.USER_ID));
        call.enqueue(new Callback<KycResponse>() {
            @Override
            public void onResponse(Call<KycResponse> call, Response<KycResponse> response) {
                kProgressHUD.dismiss();
                if (response.isSuccessful()) {
                    if (response.code() == 200 && response.body() != null) {
                        Log.e(TAG, "KYC: " );
                        if (response.body().data.all_groups == 0) {
                            callSelelctedFragement(-1); //-1 for select fragment
                        } else {
                            fragmentSelected = 0;
                            callSelelctedFragement(fragmentSelected);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<KycResponse> call, Throwable t) {
                kProgressHUD.dismiss();
                t.printStackTrace();
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();

        checkAppVersionUpdateDialog();
        getNotificationUnreadCount();

        IntentFilter intentFilter = new IntentFilter(
                "android.intent.action.MAIN");

        mReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                getNotificationUnreadCount();
            }
        };
        //registering our receiver
        this.registerReceiver(mReceiver, intentFilter);

    }

    @Override
    protected void onPause() {
        super.onPause();
        this.unregisterReceiver(this.mReceiver);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            finish();

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_dashboard, menu);
        item = menu.findItem(R.id.nav_camera);


        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent productIntent = new Intent(this, RegisterFos.class);

            startActivity(productIntent);

            return true;

        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            fragmentSelected = 1;
            callSelelctedFragement(fragmentSelected);
        } else if (id == R.id.nav_password) {
            fragmentSelected = 2;
            callSelelctedFragement(fragmentSelected);
        } else if (id == R.id.nav_home) {
            fragmentSelected = 0;
            callSelelctedFragement(fragmentSelected);
        } else if (id == R.id.nav_shop) {
            fragmentSelected = 3;
            callSelelctedFragement(fragmentSelected);
        }else if (id == R.id.nav_showstatus) {
            if (profile_varified.equalsIgnoreCase("1")) {
                opendialog();
            }
        } else if (id == R.id.nav_invoice) {
            startActivity(new Intent(this, InvoiceListingAcitivity.class));
        }


        if (id == R.id.nav_account) {
            startActivity(new Intent(ActivityDashboard.this,MyTaskActivity.class));
            return true;
        }
        if (id == R.id.nav_policies) {
            startActivity(new Intent(ActivityDashboard.this,OpportunityListActivity.class));
            return true;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showSimpleAlertDialog(String message) {
        new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE).setTitleText(Constant.TITLE)
                .setContentText(message)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                        Intent login = new Intent(ActivityDashboard.this, ActivityLogin.class);
                        String device_token = mySingleton.getData(Constant.DEVICE_FCM_TOKEN);
                        mySingleton.clearData();
                        mySingleton.saveData(Constant.DEVICE_FCM_TOKEN, device_token);
                        login.putExtra(Constant.UNAUTHORISE_TOKEN, "0");
                        login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        login.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        login.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(login);
                        finish();
                    }
                })
                .show();
    }


    private void callSelelctedFragement(int fragmentSelected) {
        Log.e(TAG, "callSelelctedFragement: >>>>>"+fragmentSelected );
        if (fragmentSelected == -1){
        if(comefrom.equals("directnav") && mySingleton.getData(Constant.USER_GROUP_ID).equalsIgnoreCase(Constant.FOS_GROUP_ID))
        {
            try {
                Log.e(TAG, "indirectNavFragsection>>>>>:inside Rbm fragment " );
                fragmentClass = FosDashboardFrag.class;
                fragment = (Fragment) fragmentClass.newInstance();
                Bundle bundles = new Bundle();
                bundles.putString("profile_varified", profile_varified);
                mySingleton.saveData("navigatetodashboard","nav");
                fragment.setArguments(bundles);
            }
            catch (Exception e) {
                e.printStackTrace();
            }

                   }
           else if(comefrom.equals("directnavs") && mySingleton.getData(Constant.USER_GROUP_ID).equalsIgnoreCase(Constant.FOS_GROUP_ID))
            {
                try {
                    Log.e(TAG, "indirectNavFragsection>>sssss>>>: " );
                    fragmentClass = KycFragment.class;
                    fragment = new KycFragment();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }

            }
       else if (mySingleton.getData(Constant.USER_GROUP_ID).equalsIgnoreCase(Constant.FOS_GROUP_ID)) {

            fragmentClass = KycFragment.class;
            fragment = new KycFragment();
        }

       else if (mySingleton.getData(Constant.USER_GROUP_ID).equalsIgnoreCase(Constant.CP_GROUP_ID)) {

            fragmentClass = DashboardCPDetailFragment.class;
            try {
                fragment = (Fragment) fragmentClass.newInstance();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
            Bundle bundle = new Bundle();
            bundle.putString("Backtoleftpage", "backtoPage");
            fragment.setArguments(bundle);

        }
       else
           {
               Log.e(TAG, "callSelelctedFragement: " );
           }
        }

        if (fragmentSelected == 0) {
            if(comefrom.equals("directnav")&&mySingleton.getData(Constant.USER_GROUP_ID).equalsIgnoreCase(Constant.FOS_GROUP_ID))
            {
                try {
                    Log.e(TAG, "indirectNavFragsection>>>>>: " );
                    fragmentClass = FosDashboardFrag.class;
                    fragment = (Fragment) fragmentClass.newInstance();
                    Bundle bundles = new Bundle();
                    bundles.putString("profile_varified", profile_varified);
                    mySingleton.saveData("navigatetodashboard","nav");
                    fragment.setArguments(bundles);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (mySingleton.getData(Constant.USER_GROUP_ID).equalsIgnoreCase(Constant.FOS_GROUP_ID)) {
                try {

                    fragmentClass = FosDashboardFrag.class;
                    fragment = (Fragment) fragmentClass.newInstance();
                    Bundle bundle = new Bundle();
                    bundle.putString("profile_varified", profile_varified);
                    fragment.setArguments(bundle);
                    Log.e(TAG, "inside 111: " );
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                try {

                    fragmentClass = FragmentDashboard.class;
                    fragment = (Fragment) fragmentClass.newInstance();
                    Bundle bundle = new Bundle();
                    bundle.putString("profile_varified", profile_varified);
                    fragment.setArguments(bundle);
                    Log.e(TAG, "inside :222 " );
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }


        if (fragmentSelected == 1) {
            try {
                if (mySingleton.getData(Constant.USER_GROUP_ID).equalsIgnoreCase(Constant.CP_GROUP_ID) ||
                        mySingleton.getData(Constant.USER_GROUP_ID).equalsIgnoreCase(Constant.POS_GROUP_ID)) {
                    Log.e("fragmentSelected", "callSelelctedFragement: " );
                    Intent viewProfile = new Intent(ActivityDashboard.this, DashboardCPDetails.class);
                    viewProfile.putExtra("call_from", "1");
                    startActivity(viewProfile);
                }
                else if (mySingleton.getData(Constant.USER_GROUP_ID).equalsIgnoreCase(Constant.FOS_GROUP_ID)) {
                    Intent viewProfile = new Intent(ActivityDashboard.this, CallEditFOS.class);
                    viewProfile.putExtra("call_from", "1");
                    startActivity(viewProfile);
                }
                else {
                    Intent viewProfile = new Intent(ActivityDashboard.this, DashboardSuperCPDetails.class);
                    viewProfile.putExtra("call_from", "1");
                    startActivity(viewProfile);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (fragmentSelected == 4) {
            try {
                if (mySingleton.getData(Constant.USER_GROUP_ID).equalsIgnoreCase(Constant.CP_GROUP_ID) ||
                        mySingleton.getData(Constant.USER_GROUP_ID).equalsIgnoreCase(Constant.POS_GROUP_ID)) {
                    Log.e("fragmentSelected 444", "callSelelctedFragement: " );
                    Intent viewProfile = new Intent(ActivityDashboard.this, DashboardDocUploadEdit.class);
                    viewProfile.putExtra("call_from", "1");
                    startActivity(viewProfile);
                } else if (mySingleton.getData(Constant.USER_GROUP_ID).equalsIgnoreCase(Constant.FOS_GROUP_ID)) {
                    Intent viewProfile = new Intent(ActivityDashboard.this, DashboardFOSDocUploadEdit.class);
                    viewProfile.putExtra("call_from", "1");
                    startActivity(viewProfile);
                } else {
                    Log.e("else part", "callSelelctedFragement: " );
                    Intent viewProfile = new Intent(ActivityDashboard.this, DashboardDocUploadEdit.class);
                    viewProfile.putExtra("call_from", "1");
                    startActivity(viewProfile);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (fragmentSelected == 2) {
            try {
                fragmentClass = ChangePassword.class;
                fragment = (Fragment) fragmentClass.newInstance();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (fragmentSelected == 3) {
            try {
                Intent viewProfile = new Intent(ActivityDashboard.this, WebViewActivity.class);
                viewProfile.putExtra("call_from", "1");
                startActivity(viewProfile);
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        try {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commitAllowingStateLoss();
        }
        catch (IllegalStateException e){
            e.printStackTrace();
        }
    }

    public void callLogout() {


        kProgressHUD = KProgressHUD.create(ActivityDashboard.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .setWindowColor(getResources().getColor(R.color.progressbar_color))
                .show();

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        final Call<LogoutResponce> call = apiService.getUserLogout(mySingleton.getData(Constant.TOKEN_BASE_64));
        call.enqueue(new Callback<LogoutResponce>() {
            @Override
            public void onResponse(Call<LogoutResponce> call, Response<LogoutResponce> response) {
                kProgressHUD.dismiss();
                if (response.isSuccessful()) {
                    if (response.code() >= 200 && response.code() < 700) {
                        if (response.code() == 200) {
                            if (response.body().getStatus() == 1) {
                                if (response.body() != null) {
                                    Gson gson = new Gson();
                                    String json = gson.toJson(response.body(), LogoutResponce.class);
                                    showSimpleAlertDialog(response.body().getMessage());
                                }
                            } else if (response.body().getStatus() == 0) {
                                Log.e(TAG, "onResponse: status 0" );                            }
                        }

                    }
                } else {

                    try {
                        ErrorBody errorBody = new ErrorBody();
                        Gson gson = new Gson();
                        errorBody = gson.fromJson(response.errorBody().string(), ErrorBody.class);
                        Log.i("error2", errorBody.getMessage());
                        if (errorBody.getName().equalsIgnoreCase(getString(R.string.unAuthorisedUser))) {
                            Logout.Login(ActivityDashboard.this);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<LogoutResponce> call, Throwable t) {
                kProgressHUD.dismiss();
                if (t != null && (t instanceof IOException || t instanceof SocketTimeoutException
                        || t instanceof ConnectException || t instanceof NoRouteToHostException
                        || t instanceof SecurityException)) {

                }
            }
        });
    }

    private void getstatus() {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        final Call<FosAttendanceStatus> call = apiService.getstatus(mySingleton.getData(Constant.USER_ID));
        call.enqueue(new Callback<FosAttendanceStatus>() {
            @Override
            public void onResponse(Call<FosAttendanceStatus> call, Response<FosAttendanceStatus> response) {

                if (response.code() == 200) {
                    if (response.body().getData() != null) {
                        if (response.body().getData().getStatus() == 1) {
                            status = "Yes";
                        } else {
                            status = "No";
                        }
                    }
                    navigationView = findViewById(R.id.nav_view);
                    Menu menu1 = navigationView.getMenu();
                    menu1.findItem(R.id.nav_showstatus).setTitle("Attendence Status(" + status + ")");

                    value = 1;
                }
            }

            @Override
            public void onFailure(Call<FosAttendanceStatus> call, Throwable t) {
                Log.e("sljkljk", "lksjdfkl");
            }
        });
    }


    private void opendialog() {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_fosdashboard);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        dialog.findViewById(R.id.tv_yes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getattendence("1", dialog);
            }
        });
        dialog.findViewById(R.id.tv_no).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getattendence("0", dialog);
            }
        });
        dialog.show();
    }


    private void getattendence(String value, final Dialog dialog) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        final Call<CpListResponse> call = apiService.fosattendance(mySingleton.getData(Constant.USER_ID), value);
        call.enqueue(new Callback<CpListResponse>() {
            @Override
            public void onResponse(Call<CpListResponse> call, Response<CpListResponse> response) {

                if (response.isSuccessful()) {
                    if (response.code() == 200) {
                        dialog.dismiss();
                        getstatus();
                    }
                }
            }

            @Override
            public void onFailure(Call<CpListResponse> call, Throwable t) {
                Log.e("sljkljk", "lksjdfkl");
            }
        });
    }



    public void getNotificationUnreadCount() {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<NotificationListResponse>
                call = apiService.getNotificationList(mySingleton.getData(Constant.USER_ID));


        call.enqueue(new Callback<NotificationListResponse>() {
            @Override
            public void onResponse(Call<NotificationListResponse> call, Response<NotificationListResponse> response) {

                if (response.isSuccessful()) {
                    if (response.code() >= 200 && response.code() < 700) {
                        if (response.code() == 200) {
                            if (response.body().getStatus() == 1) {
                                if (response.body().getDesc() != null) {

                                    tvNotificationCount.setText(getUnreadCount(response.body().getDesc()) + "");

                                }
                            }
                        }

                    }
                } else {

                    try {
                        ErrorBody errorBody = new ErrorBody();
                        Gson gson = new Gson();
                        errorBody = gson.fromJson(response.errorBody().string(), ErrorBody.class);

                    } catch (Exception e) {

                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<NotificationListResponse> call, Throwable t) {

                if (t != null && (t instanceof IOException || t instanceof SocketTimeoutException
                        || t instanceof ConnectException || t instanceof NoRouteToHostException
                        || t instanceof SecurityException)) {

                }
            }
        });
    }

    private int getUnreadCount(List<Desc> notificationList) {
        int count = 0;
        for (Desc desc : notificationList) {
            if (desc.getReadStatus() == 0)
                count++;
        }
        return count;
    }

    private void checkAppVersionUpdateDialog() {
        Intent intent = new Intent(Intent.ACTION_SYNC, null, this, CheckAppVersion.class);
        /* Send optional extras to Download IntentService */
        intent.putExtra("currentVersion", getVersionInfo());
        startService(intent);
        callReceiver();
    }

    private String getVersionInfo() {
        String versionName = "";
        int versionCode = -1;
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            versionName = packageInfo.versionName;
            versionCode = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }


    private void callReceiver() {
        UpdateReceiver.bindListener(messageText -> showBuilder("Update", "Please update newer version on play store", "Update"));

    }

    private void showBuilder(String title, String message, String btnText) {
        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setIcon(R.mipmap.ic_launcher);
        alertDialogBuilder.setPositiveButton(btnText, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                callUpdate();
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            alertDialogBuilder.setOnDismissListener(dialog -> {

            });
        }
        alertDialogBuilder.show();
    }


    private void callUpdate() {
        final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));
        }
        finish();
    }


    @Override
    public void onInAppUpdateError(int code, Throwable error) {

    }

    @Override
    public void onInAppUpdateStatus(InAppUpdateStatus status) {

    }
}