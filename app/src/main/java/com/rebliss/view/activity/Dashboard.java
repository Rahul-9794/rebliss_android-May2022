package com.rebliss.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.gson.Gson;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.rebliss.utils.App;
import com.rebliss.R;
import com.rebliss.data.perf.MySingleton;
import com.rebliss.domain.constant.Constant;
import com.rebliss.domain.model.ErrorBody;
import com.rebliss.domain.model.logout.LogoutResponce;
import com.rebliss.presenter.helper.Logout;
import com.rebliss.presenter.helper.Network;
import com.rebliss.presenter.retrofit.ApiClient;
import com.rebliss.presenter.retrofit.ApiInterface;
import java.io.IOException;
import java.net.ConnectException;
import java.net.NoRouteToHostException;
import java.net.SocketTimeoutException;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Dashboard extends AppCompatActivity {


    private ImageView imgAddFos;
    private TextView textHeader;
    private MySingleton mySingleton;
    Intent intent;
    ImageView imgLogout;
    String profile_varified;
    RelativeLayout relManagerFOSUser, relManagerPOSUser;
    TextView approvalPending, textManagePOSUser, textManageFOSUser,txtapplicationpendingapproval;
    LinearLayout relActiveUser;
    private Network network;
    private KProgressHUD kProgressHUD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard2);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        intent = getIntent();
        profile_varified = intent.getStringExtra("profile_varified");
        Log.i("profile_varified ", profile_varified);
        mySingleton = new MySingleton(Dashboard.this);
        initView();
        viewListener();
    }

    private void showSimpleAlertDialog(String message) {
        new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE).setTitleText(Constant.TITLE)
                .setContentText(message)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                        Intent login = new Intent(Dashboard.this, ActivityLogin.class);
                        String token = mySingleton.getData(Constant.DEVICE_FCM_TOKEN);
                        mySingleton.clearData();
                        mySingleton.saveData(Constant.DEVICE_FCM_TOKEN, token);
                        login.putExtra(Constant.UNAUTHORISE_TOKEN, "0");
                        login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(login);
                    }
                })
                .show();
    }

    private void showWarningSimpleAlertDialog(String title, String message) {
        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE).setTitleText(title)
                .setContentText(message)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                    }
                })
                .show();
    }


    private void initView() {
        network = new Network();
        imgLogout = findViewById(R.id.imgLogout);
        relManagerFOSUser = findViewById(R.id.relManagerFOSUser);
        txtapplicationpendingapproval = findViewById(R.id.txtapplicationpendingapproval);
        relManagerPOSUser = findViewById(R.id.relManagerPOSUser);
        imgAddFos = findViewById(R.id.imgAddFos);
        textManagePOSUser = findViewById(R.id.textManagePOSUser);
        textManageFOSUser = findViewById(R.id.textManageFOSUser);
        textHeader = findViewById(R.id.textHeader);
        relActiveUser = findViewById(R.id.relActiveUser);
        approvalPending = findViewById(R.id.approvalPending);

        if (mySingleton.getData(Constant.USER_GROUP_ID).equalsIgnoreCase(Constant.POS_GROUP_ID)) {
            imgAddFos.setVisibility(View.GONE);
        }
        if (profile_varified.equalsIgnoreCase("0")) {

            imgAddFos.setVisibility(View.GONE);
            approvalPending.setVisibility(View.VISIBLE);
            relActiveUser.setVisibility(View.GONE);
        } else if (profile_varified.equalsIgnoreCase("1")) {
            approvalPending.setVisibility(View.GONE);
            imgLogout.setVisibility(View.GONE);
            relActiveUser.setVisibility(View.VISIBLE);
            imgAddFos.setVisibility(View.VISIBLE);
            if (mySingleton.getData(Constant.USER_GROUP_ID).equalsIgnoreCase(Constant.CP_GROUP_ID)) {
                relManagerFOSUser.setVisibility(View.VISIBLE);
                relManagerPOSUser.setVisibility(View.VISIBLE);

            }
            if (mySingleton.getData(Constant.USER_GROUP_ID).equalsIgnoreCase(Constant.FOS_GROUP_ID)) {
                relManagerFOSUser.setVisibility(View.GONE);
                relManagerPOSUser.setVisibility(View.VISIBLE);
            }
            if (mySingleton.getData(Constant.USER_GROUP_ID).equalsIgnoreCase(Constant.POS_GROUP_ID)) {
                relManagerFOSUser.setVisibility(View.GONE);
                relManagerPOSUser.setVisibility(View.GONE);
                imgAddFos.setVisibility(View.GONE);
            }

        } else if (profile_varified.equalsIgnoreCase("2")) {
            imgLogout.setVisibility(View.GONE);
            imgAddFos.setVisibility(View.GONE);
            approvalPending.setVisibility(View.VISIBLE);
            relActiveUser.setVisibility(View.GONE);

        }

       seFontOnView();
    }

    private void viewListener() {
        imgAddFos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callRegisterFos();

            }
        });
        relManagerFOSUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callFOS = new Intent(Dashboard.this, ActivityFOS.class);
                startActivity(callFOS);
            }
        });
        relManagerPOSUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callFOS = new Intent(Dashboard.this, ActivityPOS.class);
                startActivity(callFOS);
            }
        });
        imgLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (network.isNetworkConnected(Dashboard.this)) {
                    callLogout();
                } else {
                    Log.e("TAG", "onClick: else network part dashboard" );
                }
            }
        });
    }

    private void seFontOnView() {
        textHeader.setTypeface(App.LATO_REGULAR);
        textManagePOSUser.setTypeface(App.LATO_REGULAR);
        textManageFOSUser.setTypeface(App.LATO_REGULAR);
    }

    private void callRegisterFos() {
        Intent registerFos = new Intent(Dashboard.this, RegisterFos.class);
        startActivity(registerFos);
    }
    public void callLogout() {


        kProgressHUD = KProgressHUD.create(Dashboard.this)
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
                                Log.e("TAG", "onResponse: status 0 DASHBOARD" );                            }
                        }

                    } else {
                        Log.e("TAG", "onResponse: else part dashboard" );

                    }
                } else {

                    try {
                        ErrorBody errorBody = new ErrorBody();
                        Gson gson = new Gson();
                        errorBody = gson.fromJson(response.errorBody().string(), ErrorBody.class);
                        if (errorBody.getName().equalsIgnoreCase(getString(R.string.unAuthorisedUser))) {
                            Logout.Login(Dashboard.this);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
//skfdnasjkfa
//akfjakfasfas
//dsalkfjasdfiaj
//afasfdasfas