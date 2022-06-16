package com.rebliss.view.activity;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.google.gson.Gson;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.rebliss.utils.App;
import com.rebliss.R;
import com.rebliss.data.perf.MySingleton;
import com.rebliss.domain.constant.Constant;
import com.rebliss.domain.model.ErrorBody;
import com.rebliss.domain.model.otp.OtpRequest;
import com.rebliss.domain.model.otp.OtpResponce;
import com.rebliss.domain.model.otp.ResendOTPResponce;
import com.rebliss.presenter.helper.Network;
import com.rebliss.presenter.retrofit.ApiClient;
import com.rebliss.presenter.retrofit.ApiInterface;
import java.io.IOException;
import java.net.ConnectException;
import java.net.NoRouteToHostException;
import java.net.SocketTimeoutException;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OtpVerification extends AppCompatActivity {

    private static final String TAG = OtpVerification.class.getSimpleName();
    private TextView text_phone, header,otpverify,txtotpVerify;
    private EditText input_edit;
    private Button resend, verify;
    private ImageView back;
    private int count = 0;
    private MySingleton mySingleton;
    private Network network;
    private String mInput_edit;
    private BroadcastReceiver broadcastReceiver;
    private KProgressHUD kProgressHUD;
    private Intent intent;
    private Integer groupId, groupTitle;
    private String comefrom ="";
    String type = "", Phone = "", userId = "";

    public OtpVerification() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verification);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initView();
        intent = getIntent();
        try {
            groupId = intent.getIntExtra(Constant.CHANNEL_GROUP_ID,0);
            Log.e(TAG, "onCreate: "+groupId.toString() );
        }
        catch (NumberFormatException e){
            e.printStackTrace();
        }
        if (network.isNetworkConnected(OtpVerification.this)) {
            count++;
            if(count==1) {
                callResendOtp();
            }
        }

        type = intent.getStringExtra("type");
        Bundle bundle = getIntent().getExtras();
        assert bundle !=null;
        comefrom = bundle.getString("khaase");

        Phone = intent.getStringExtra("Phone");
        if(type==null)
        {
            type = "";
        }
        if (type != null) {
            if (type.equalsIgnoreCase("1")) {
                userId = intent.getStringExtra("userId");
            }
        }
        viewListener();
        setData();
        resendotpaferOneMinute();
    }

    private void resendotpaferOneMinute()
    {
        new  CountDownTimer(60000, 1000)
        {
            @Override
            public void onTick(long l) {
                resend.setVisibility( View.GONE);
                otpverify.setVisibility( View.VISIBLE);
                NumberFormat f = new DecimalFormat("00");
                long sec = (l / 1000) % 60;

                otpverify.setText("Resend OTP after : "+ f.format(sec));
                otpverify.setTextColor(ContextCompat.getColor(OtpVerification.this, R.color.colorMegentaText));
            }
            @Override
            public void onFinish() {
                resend.setVisibility( View.VISIBLE);
                otpverify.setVisibility( View.GONE);

            }

        }.start();
    }

    private void initView() {
        mySingleton = new MySingleton(OtpVerification.this);
        network = new Network();
        text_phone = findViewById(R.id.text_phone);
        otpverify = findViewById(R.id.txtotpVerify);
        header = findViewById(R.id.header);
        resend = findViewById(R.id.resend);
        verify = findViewById(R.id.verify);
        input_edit = findViewById(R.id.input_edit);
        back = findViewById(R.id.icBack);
        setFont();

    }

    private void setData() {
        if (type.equalsIgnoreCase("1")) {
            text_phone.setText("Please type verification code \nsent to : " + Phone);
        } else {
            text_phone.setText("Please type verification code \nsent to : " + mySingleton.getData(Constant.USER_PHONE));
        }
    }

    private void setFont() {
        /*
        text_phone.setTypeface(App.LATO_REGULAR);
        resend.setTypeface(App.LATO_REGULAR);
        verify.setTypeface(App.LATO_REGULAR);
        input_edit.setTypeface(App.LATO_REGULAR);
    //    header.setTypeface(App.LATO_REGULAR);


         */
    }

    private void callDisplayErrorCode(int statusCode, String message) {
        String Mmessage = message;
        if (statusCode == 400) {
            if (message.length() <= 0) {
                Mmessage = Mmessage + Constant.ERROR_400;
            }
        }
        if (statusCode == 401) {
            if (message.length() <= 0) {
                Mmessage = Mmessage + Constant.ERROR_401;
            }
        }
        if (statusCode == 403) {
            if (message.length() <= 0) {
                Mmessage = Mmessage + Constant.ERROR_403;
            }
        }
        if (statusCode == 404) {
            if (message.length() <= 0) {
                Mmessage = Mmessage + Constant.ERROR_404;
            }
        }
        if (statusCode == 405) {
            if (message.length() <= 0) {
                Mmessage = Mmessage + Constant.ERROR_405;
            }
        }
        if (statusCode == 500) {
            if (message.length() <= 0) {
                Mmessage = Mmessage + Constant.ERROR_500;
            }
        }
    }

    private void viewListener() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            }
        });
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isOTPValidated()) {
                    if (network.isNetworkConnected(OtpVerification.this)) {
                        callVerifyOtp();
                    } else {
                        Toast.makeText(OtpVerification.this, Constant.NETWIRK_ERROR, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        resend.setOnClickListener(view -> {
                    if (network.isNetworkConnected(OtpVerification.this)) {
                        callResendOtp();
                    } else {
                        Toast.makeText(OtpVerification.this, Constant.NETWIRK_ERROR, Toast.LENGTH_SHORT).show();
                    }
            resendotpaferOneMinute();

        });

    }


    private void callIntent() {

        if (type.equalsIgnoreCase("0")) {
            mySingleton.saveData(Constant.USER_VERIFYED, "1");

            if (mySingleton.getData(Constant.USER_GROUP_ID).equalsIgnoreCase("1")) {
                Intent login = new Intent(OtpVerification.this, DashboardCPDetails.class);
                login.putExtra("call_from", "0");
                login.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                login.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                login.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(login);
            }
            //+==================== Navigation from OTP verification to Activity Dashboard
            if(comefrom.equalsIgnoreCase("directedtoDashboard"))
            {
                Intent login = new Intent(OtpVerification.this, ActivityDashboard.class);
                login.putExtra("directnavigation","directnav");
                login.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                login.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                login.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(login);
            }
            if(comefrom.equalsIgnoreCase("directedtoDashboards"))
            {
                Intent login = new Intent(OtpVerification.this, ActivityDashboard.class);
                login.putExtra("directnavigation","directnavs");
                login.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                login.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                login.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(login);
            }
            else if (mySingleton.getData(Constant.USER_GROUP_ID).equalsIgnoreCase("2")) {
                Intent login = new Intent(OtpVerification.this, CallEditFOS.class);
                login.putExtra("call_from", "0");
                login.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                login.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                login.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(login);
            } else if (mySingleton.getData(Constant.USER_GROUP_ID).equalsIgnoreCase("3")) {
                Intent login = new Intent(OtpVerification.this, DashboardCPDetails.class);
                login.putExtra("call_from", "0");
                login.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                login.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                login.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(login);
            } else if (mySingleton.getData(Constant.USER_GROUP_ID).equalsIgnoreCase("4")) {
                Intent login = new Intent(OtpVerification.this, DashboardSuperCPDetails.class);
                login.putExtra("call_from", "0");
                login.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                login.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                login.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(login);
            }
        }

        finish();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(comefrom!=null){
            Intent intent = new Intent(OtpVerification.this,ActivityLogin.class);
            startActivity(intent);
        }
        else {
            finish();
            overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        }
    }

    private boolean isOTPValidated() {
        boolean status = true;
        mInput_edit = input_edit.getText().toString();
        if (mInput_edit.length() <= 0) {
            status = false;
        }
        return status;
    }

    private void callVerifyOtp() {
        kProgressHUD = KProgressHUD.create(OtpVerification.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .setWindowColor(getResources().getColor(R.color.progressbar_color))
                .show();
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        OtpRequest otpRequest = new OtpRequest();
        otpRequest.setVerify_code(mInput_edit);
        String url = "";
        if (type.equalsIgnoreCase("1")) {
            otpRequest.setUser_id(userId);
        } else {
            url = Constant.kBaseURL + Constant.K_GET_RESEND;
        }
        Gson gson = new Gson();
        String json = gson.toJson(otpRequest, OtpRequest.class);
        Log.i(TAG, "json " + json);
        Log.i(TAG, "token " + mySingleton.getData(Constant.TOKEN_BASE_64));
        Log.i(TAG, "token " + mySingleton.getData(Constant.TOKEN));
        final Call<OtpResponce> call = apiService.verifyOTP(mySingleton.getData(Constant.TOKEN_BASE_64), otpRequest);

        call.enqueue(new Callback<OtpResponce>() {
            @Override
            public void onResponse(Call<OtpResponce> call, Response<OtpResponce> response) {
                kProgressHUD.dismiss();
                if (response.isSuccessful()) {
                    if (response.code() >= 200 && response.code() < 700) {
                        if (response.code() == 200) {
                            if (response.body().getStatus() == 1) {
                                input_edit.setText("");

                                if (groupId != null && groupId.equals("1")) {
                                    callIntent();
                                }
                                if(comefrom==null)
                                {
                                    comefrom ="";
                                }

                                    if (comefrom!=null && comefrom.equalsIgnoreCase("directedtoDashboard")) {
                                        Intent login = new Intent(OtpVerification.this, ActivityDashboard.class);
                                        login.putExtra("directnavigation", "directnav");
                                        login.putExtra("profile_varified", "0");
                                        mySingleton.saveData("from_rBM","fromRBM");
                                        login.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        login.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        login.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        Log.e(TAG, "dashboard for rbm: " );

                                        startActivity(login);
                                    }

                              else if (comefrom!=null && comefrom.equalsIgnoreCase("directedtoDashboards")) {
                                    Intent login = new Intent(OtpVerification.this, ActivityDashboard.class);
                                    login.putExtra("directnavigation", "directnavs");
                                    login.putExtra("profile_varified", "0");
                                    login.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    login.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    mySingleton.saveData("from_rBS","fromRBS");
                                    login.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    Log.e(TAG, "dashboard for rbs: " );
                                    startActivity(login);
                                }
                                   else {
                                        Intent login = new Intent(OtpVerification.this, PasswordSetupActivity.class);
                                        login.putExtra("type", "0");
                                        login.putExtra(Constant.CHANNEL_GROUP_ID, groupId);
                                        login.putExtra("call_from", "0");
                                        login.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        login.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        login.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    Log.e(TAG, "Password: " );
                                        startActivity(login);
                                    }
                            } else if (response.body().getStatus() == 0) {
                                input_edit.setText("");
                                Toast.makeText(OtpVerification.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                        callDisplayErrorCode(response.code(), "");
                    } else {
                        Toast.makeText(OtpVerification.this, Constant.UNEXPECTED, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    try {
                        ErrorBody errorBody = new ErrorBody();
                        Gson gson = new Gson();
                        errorBody = gson.fromJson(response.errorBody().string(), ErrorBody.class);
                        callDisplayErrorCode(Integer.parseInt(errorBody.getStatus()), errorBody.getMessage());
                        Toast.makeText(OtpVerification.this, errorBody.getMessage(), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(OtpVerification.this, Constant.ERROR_INVALID_JSON, Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<OtpResponce> call, Throwable t) {
                kProgressHUD.dismiss();
                if (t != null && (t instanceof IOException || t instanceof SocketTimeoutException
                        || t instanceof ConnectException || t instanceof NoRouteToHostException
                        || t instanceof SecurityException)) {
                    Toast.makeText(OtpVerification.this, Constant.NETWIRK_ERROR, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void callResendOtp() {

        kProgressHUD = KProgressHUD.create(OtpVerification.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .setWindowColor(getResources().getColor(R.color.progressbar_color))
                .show();
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        String url = "";
        if (type.equalsIgnoreCase("1")) {
            url = Constant.kBaseURL + Constant.K_GET_RESEND + "?" + userId;
        } else {
            url = Constant.kBaseURL + Constant.K_GET_RESEND;
        }
        final Call<ResendOTPResponce> call = apiService.getUserResendOTP(mySingleton.getData(Constant.TOKEN_BASE_64), url);

        call.enqueue(new Callback<ResendOTPResponce>() {
            @Override
            public void onResponse(Call<ResendOTPResponce> call, Response<ResendOTPResponce> response) {
                kProgressHUD.dismiss();
                if (response.isSuccessful()) {
                    if (response.code() >= 200 && response.code() < 700) {
                        if (response.code() == 200) {
                            if (response.body().getStatus() == 1) {
                                Toast.makeText(OtpVerification.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                Log.e(TAG, "onResponse: >>>>>"+response.body().getMessage() );

                            } else if (response.body().getStatus() == 0) {

                                Toast.makeText(OtpVerification.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                        callDisplayErrorCode(response.code(), "");
                    } else {
                        Toast.makeText(OtpVerification.this, Constant.UNEXPECTED, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    try {
                        ErrorBody errorBody = new ErrorBody();
                        Gson gson = new Gson();
                        errorBody = gson.fromJson(response.errorBody().string(), ErrorBody.class);
                        callDisplayErrorCode(Integer.parseInt(errorBody.getStatus()), errorBody.getMessage());
                        Toast.makeText(OtpVerification.this, errorBody.getMessage(), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(OtpVerification.this, Constant.ERROR_INVALID_JSON, Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResendOTPResponce> call, Throwable t) {
                kProgressHUD.dismiss();
                if (t != null && (t instanceof IOException || t instanceof SocketTimeoutException
                        || t instanceof ConnectException || t instanceof NoRouteToHostException
                        || t instanceof SecurityException)) {
                    Toast.makeText(OtpVerification.this, Constant.NETWIRK_ERROR, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}

