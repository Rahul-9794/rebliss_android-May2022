package com.rebliss.view.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.Gson;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.rebliss.utils.App;
import com.rebliss.R;
import com.rebliss.data.perf.MySingleton;
import com.rebliss.domain.constant.Constant;
import com.rebliss.domain.model.CarouselResponse;
import com.rebliss.domain.model.ErrorBody;
import com.rebliss.domain.model.forgotpassword.ForgotPasswordRequest;
import com.rebliss.domain.model.forgotpassword.ForgotResponce;
import com.rebliss.domain.model.login.Log_inRequest;
import com.rebliss.domain.model.login.LoginResponce;
import com.rebliss.presenter.helper.Base64Encoded;
import com.rebliss.presenter.helper.DisplaySnackBar;
import com.rebliss.presenter.helper.Network;
import com.rebliss.presenter.helper.ShowHintOrText;
import com.rebliss.presenter.helper.TextUtil;
import com.rebliss.presenter.retrofit.ApiClient;
import com.rebliss.presenter.retrofit.ApiInterface;
import com.rebliss.view.activity.employee.EmployeeAcivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.ConnectException;
import java.net.NoRouteToHostException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeoutException;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityLogin extends AppCompatActivity {

    private static final String TAG = ActivityLogin.class.getSimpleName();
    private Context context;
    TextView textForgot, textReblissInfo,txtSignUp;
    TextView forgottext, textPortal;
    String currentVersion = "";
    private Button btnLogin;
    private EditText etNumber, etPassword;
    private MySingleton mySingleton;
    private KProgressHUD kProgressHUD;
    private Network network;
    private DisplaySnackBar displaySnackBar;
    private String mPhoneNumber, mPassword;
    private Base64Encoded base64Encoded;
    private AlertDialog dialog;
    private EditText forgotemail;
    private TextView send, cancel, title;
    private Intent intent;
    private String isUnAuthorise = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        intent = getIntent();
        isUnAuthorise = intent.getStringExtra(Constant.UNAUTHORISE_TOKEN);
       // setTaskBarColored();

        initView();
        viewListener();
    }

    private void initView() {
        context = ActivityLogin.this;
        mySingleton = new MySingleton(this);
        displaySnackBar = new DisplaySnackBar(this);
        network = new Network();
        base64Encoded = new Base64Encoded(context);
        textForgot = findViewById(R.id.textForgot);
        txtSignUp = findViewById(R.id.txtSignUp);
        textReblissInfo = findViewById(R.id.textReblissInfo);
        textPortal = findViewById(R.id.textPortal);
        btnLogin = findViewById(R.id.btnLogin);
        etNumber = findViewById(R.id.etNumber);
        etPassword = findViewById(R.id.etPassword);

        if (mySingleton.getData(Constant.DEVICE_FCM_TOKEN) == null || mySingleton.getData(Constant.DEVICE_FCM_TOKEN).isEmpty())
            FirebaseInstanceId.getInstance().getInstanceId()
                    .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                        @Override
                        public void onComplete(@NonNull Task<InstanceIdResult> task) {
                            if (!task.isSuccessful()) {
                                Log.w(TAG, "getInstanceId failed", task.getException());
                                return;
                            }
                            String token = task.getResult().getToken();
                            mySingleton.saveData(Constant.DEVICE_FCM_TOKEN, token);
                            Log.i("tokensssssss", token);

                        }
                    });
        if (!TextUtil.isStringNullOrBlank(isUnAuthorise)) {
            if (isUnAuthorise.equalsIgnoreCase("1")) {
                showSimpleAlertDialog(Constant.UNAUTHORISE_TOKEN_MESSAGE);
            }
        }
        setFontOnView();
        setHint();
        if (network.isNetworkConnected(ActivityLogin.this)) {
            callAppBannerService();
        } else {
            displaySnackBar.DisplaySnackBar(Constant.NETWIRK_ERROR, Constant.TYPE_ERROR);
        }
    }

    private void setHint() {
        etNumber.setHint(ShowHintOrText.GetMandatory("Mobile No."));
        etPassword.setHint(ShowHintOrText.GetMandatory("Password"));
    }

    private void setFontOnView() {
        textForgot.setTypeface(App.LATO_REGULAR);
        textReblissInfo.setTypeface(App.LATO_REGULAR);
        btnLogin.setTypeface(App.LATO_REGULAR);
        etNumber.setTypeface(App.LATO_REGULAR);
        etPassword.setTypeface(App.LATO_REGULAR);
        textPortal.setTypeface(App.LATO_REGULAR);
    }

    private void viewListener() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFormValidatedWithSweetAlert()) {
                    if (network.isNetworkConnected(context)) {
                        if (!TextUtil.isStringNullOrBlank(mySingleton.getData(Constant.DEVICE_FCM_TOKEN))) {
                            callLoginService();
                        } else {
                            showSimpleAlertDialog(Constant.APP_NOT_REGISTER);
                        }
                    } else {
                        displaySnackBar.DisplaySnackBar(Constant.NETWIRK_ERROR, Constant.TYPE_ERROR);
                    }
                }
            }
        });

        textForgot.setOnClickListener(v -> forgotPasswrod());

        txtSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivityLogin.this,ActivitySignup.class));
            }
        });
    }

    private void showSimpleAlertDialog(String message) {
        new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE).setTitleText(Constant.TITLE)
                .setContentText(message)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                        if (dialog != null)
                            dialog.dismiss();
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


    private void forgotPasswrod() {
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle("Request Password Reset");
        alert.setView(R.layout.forgot_password);
        alert.setCancelable(false);
        dialog = alert.create();

        dialog.show();
        forgotemail = dialog.findViewById(R.id.email);
        title = dialog.findViewById(R.id.title);
        cancel = dialog.findViewById(R.id.cancel);
        send = dialog.findViewById(R.id.send);
        forgotemail.setTypeface(App.LATO_REGULAR);
        cancel.setTypeface(App.LATO_REGULAR);
        send.setTypeface(App.LATO_REGULAR);
        title.setTypeface(App.LATO_REGULAR);
        forgotemail.setHint(ShowHintOrText.GetMandatory("Phone no"));
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (forEmailValidated()) {
                    if (network.isNetworkConnected(context)) {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                callForgotPasswordService(forgotemail.getText().toString().trim());
                            }
                        });
                    } else {
                        Toast.makeText(context, "There are some connection issue", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
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
        Log.i("", "response " + statusCode + " " + Mmessage);
    }


    private void saveData(LoginResponce mobile_verified) {
        mySingleton.saveData(Constant.TOKEN, (!TextUtil.isStringNullOrBlank(mobile_verified.getData().getToken()) ? mobile_verified.getData().getToken() : ""));
        Log.e(TAG, "saveData: " + mobile_verified.getData().getToken());
        mySingleton.saveData(Constant.USER_ID, (!TextUtil.isStringNullOrBlank(mobile_verified.getData().getId() + "") ? mobile_verified.getData().getId() + "" : ""));
        mySingleton.saveData(Constant.USER_FIRST_NAME, (!TextUtil.isStringNullOrBlank(mobile_verified.getData().getFirst_name()) ? mobile_verified.getData().getFirst_name() : ""));
        mySingleton.saveData(Constant.USER_LAST_NAME, (!TextUtil.isStringNullOrBlank(mobile_verified.getData().getLast_name()) ? mobile_verified.getData().getLast_name() : ""));
        mySingleton.saveData(Constant.USER_EMAIl, (!TextUtil.isStringNullOrBlank(mobile_verified.getData().getEmail()) ? mobile_verified.getData().getEmail() : ""));
        mySingleton.saveData(Constant.USER_PHONE, (!TextUtil.isStringNullOrBlank(mobile_verified.getData().getPhone_number()) ? mobile_verified.getData().getPhone_number() : ""));
        mySingleton.saveData(Constant.USER_VERIFYED, (!TextUtil.isStringNullOrBlank(mobile_verified.getData().getMobile_verified() + "") ? mobile_verified.getData().getMobile_verified() + "" : ""));
        mySingleton.saveData(Constant.USER_PROFILE_VERIFYED, (!TextUtil.isStringNullOrBlank(mobile_verified.getData().getProfile_verified() + "") ? mobile_verified.getData().getProfile_verified() + "" : ""));
        mySingleton.saveData(Constant.USER_FOS_TYPES, (!TextUtil.isStringNullOrBlank(mobile_verified.getData().getFos_type() + "") ? mobile_verified.getData().getFos_type() + "" : ""));
        mySingleton.saveData(Constant.USER_GROUP_ID, (!TextUtil.isStringNullOrBlank(mobile_verified.getData().getGroup_id() + "") ? mobile_verified.getData().getGroup_id() + "" : ""));
        mySingleton.saveData(Constant.USER_GROUP_DETAIL_ID, (!TextUtil.isStringNullOrBlank(mobile_verified.getData().getGroup_detail_id() + "") ? mobile_verified.getData().getGroup_detail_id() + "" : ""));
        base64Encoded.EncodeToken();
        Log.e(TAG, "saveData: " + mySingleton.getData(Constant.USER_GROUP_ID));

        if (mobile_verified.getData().getFos_type() == null) {
            mobile_verified.getData().setFos_type("");
        }
        if (mobile_verified.getData().getCode() == null) {
            mobile_verified.getData().setCode("");
        }
        //Log.i("TAG", "UserId: "+mySingleton.getData("id"));

        //==============Code for direct navigation to OTP Screen for sathi revision=====================
        //Here we are checking 3 parameter i.e mobile verified, fos_type and code. if code is availble and rBM is the value and mobile verified is 0 than navigation from login to dashboard
        if (mobile_verified.getData().getFos_type() == null) {
            mobile_verified.getData().getFos_type().equals("");
        }

        if (mobile_verified != null) {

            if (mobile_verified.getData().getMobile_verified() == 0 && mobile_verified.getData().getFos_type().equals("rBM") && mobile_verified.getData().getCode().length() > 0) {
                Intent intent = new Intent(context, OtpVerification.class);
                intent.putExtra("khaase", "directedtoDashboard");
                intent.putExtra(Constant.CHANNEL_GROUP_ID, mobile_verified.getData().getGroup_id());
                intent.putExtra("type", "0");
                Log.e(TAG, "gointoOtpverfrommobile: >>>>>>>!111111");
                startActivity(intent);
                finish();

            } else if (mobile_verified.getData().getFos_type().equals("rBM") && mobile_verified.getData().getCode().length() > 0) {
                Intent login = new Intent(context, ActivityDashboard.class);
                login.putExtra("directnavigation", "directnav");
                login.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                login.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                login.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                Log.e(TAG, "saveData: >>>>>@2222222");
                startActivity(login);
                finish();
            } else if (mobile_verified.getData().getMobile_verified() == 0 && mobile_verified.getData().getFos_type().equals("rBS") && mobile_verified.getData().getCode().length() > 0) {
                Intent intent = new Intent(context, OtpVerification.class);
                intent.putExtra("khaase", "directedtoDashboards");
                intent.putExtra(Constant.CHANNEL_GROUP_ID, mobile_verified.getData().getGroup_id());
                intent.putExtra("type", "0");
                Log.e(TAG, "rBS1: >>>>33333332");
                startActivity(intent);
                finish();

            } else if (mobile_verified.getData().getFos_type().equals("rBS") && mobile_verified.getData().getCode().length() > 0) {
                Intent login = new Intent(context, ActivityDashboard.class);
                login.putExtra("directnavigation", "directnavs");
                login.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                login.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                login.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(login);
                Log.e(TAG, "saveData: >>>>>>>>>>>>44444444");
                finish();
            } else if (mobile_verified.getData().getGroup_id() == -1) {
                Intent callOtp = new Intent(context, ActivityPartnerSelecion.class);
                callOtp.putExtra("profile_varified", mobile_verified.getData().getProfile_verified());
                startActivity(callOtp);
                finish();
            } else if (mySingleton.getData(Constant.USER_GROUP_ID).equalsIgnoreCase(Constant.EMPLOYEE_GROUP_ID)) {
                if (mySingleton.getData(Constant.USER_PROFILE_VERIFYED).equalsIgnoreCase("1")) {

                    Intent callOtp = new Intent(context, EmployeeAcivity.class);
                    callOtp.putExtra("profile_varified", mobile_verified.getData().getProfile_verified());
                    startActivity(callOtp);
                    finish();
                }

            } else if (mySingleton.getData(Constant.USER_VERIFYED).equalsIgnoreCase("1")) {

                if (mySingleton.getData(Constant.USER_GROUP_ID).equalsIgnoreCase(Constant.CP_GROUP_ID) ||
                        mySingleton.getData(Constant.USER_GROUP_ID).equalsIgnoreCase(Constant.POS_GROUP_ID)) {
                    if (mySingleton.getData(Constant.USER_PROFILE_VERIFYED).equalsIgnoreCase("0")) {
                        Intent callOtp = new Intent(context, DashboardDocUpload.class);
                        callOtp.putExtra("call_from", "0");
                        startActivity(callOtp);
                        finish();
                    } else {
                        Intent callOtp = new Intent(context, ActivityDashboard.class);
                        callOtp.putExtra("profile_varified", mobile_verified.getData().getProfile_verified());
                        startActivity(callOtp);
                        finish();
                    }
                } else if (mySingleton.getData(Constant.USER_GROUP_ID).equalsIgnoreCase(Constant.FOS_GROUP_ID)) {
                    if (mySingleton.getData(Constant.USER_PROFILE_VERIFYED).equalsIgnoreCase("0")) {
                        Intent callOtp = new Intent(context, ActivityDashboard.class);
                        callOtp.putExtra("profile_varified", mobile_verified.getData().getProfile_verified());
                        callOtp.putExtra("call_from", "0");

                        startActivity(callOtp);
                        finish();
                    } else {
                        Intent callOtp = new Intent(context, ActivityDashboard.class);
                        callOtp.putExtra("profile_varified", mobile_verified.getData().getProfile_verified());
                        startActivity(callOtp);
                        finishAffinity();
                    }

                } else if (mySingleton.getData(Constant.USER_GROUP_ID).equalsIgnoreCase(Constant.SUPER_CP_GROUP_ID)) {
                    if (mySingleton.getData(Constant.USER_PROFILE_VERIFYED).equalsIgnoreCase("0")) {
                        Intent callOtp = new Intent(context, DashboardSuperCPDetails.class);
                        callOtp.putExtra("call_from", "0");
                        startActivity(callOtp);
                        finish();
                    } else {
                        Intent callOtp = new Intent(context, ActivityDashboard.class);
                        callOtp.putExtra("profile_varified", mobile_verified.getData().getProfile_verified());
                        startActivity(callOtp);

                        finish();
                    }

                }
            } else {
                Intent callOtp = new Intent(context, OtpVerification.class);
                callOtp.putExtra(Constant.CHANNEL_GROUP_ID, mobile_verified.getData().getGroup_id());
                callOtp.putExtra("type", "0");
                startActivity(callOtp);
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }


    private boolean isFormValidatedWithSweetAlert() {
        boolean status = true;
        mPhoneNumber = etNumber.getText().toString().trim();
        mPassword = etPassword.getText().toString().trim();

        if (mPhoneNumber.length() != 10) {
            status = false;
            showWarningSimpleAlertDialog(Constant.TITLE, getString(R.string.PHONE_VALIDATION));
        } else if (mPassword.length() < 6) {
            status = false;
            showWarningSimpleAlertDialog(Constant.TITLE, getString(R.string.PASSWORD_VALIDATION));
        }
        return status;
    }

    private boolean forEmailValidated() {
        boolean status = true;
        forgotemail = dialog.findViewById(R.id.email);
        String emailData = forgotemail.getText().toString().trim();
        if (emailData.length() < 10) {
            status = false;
            showWarningSimpleAlertDialog(Constant.TITLE, getString(R.string.PHONE_VALIDATION));
        }
        return status;
    }

    public void callLoginService() {

        try {
            kProgressHUD = KProgressHUD.create(ActivityLogin.this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setCancellable(false)
                    .setAnimationSpeed(2)
                    .setDimAmount(0.5f)
                    .show();
        } catch (Exception e) {
            e.printStackTrace();
        }


        Log_inRequest lRequest = new Log_inRequest();
        lRequest.setPhone_number(mPhoneNumber);
        lRequest.setPassword(mPassword);
        lRequest.setDevice_id(mySingleton.getData(Constant.DEVICE_FCM_TOKEN));
        lRequest.setDevice_type("android");
        lRequest.setIsNew(1);
        Gson gson = new Gson();
        String json = gson.toJson(lRequest, Log_inRequest.class);
        Log.i(TAG, "json " + json);
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        final Call<LoginResponce> call = apiService.postUserLogin(lRequest);
        call.enqueue(new Callback<LoginResponce>() {
            @Override
            public void onResponse(Call<LoginResponce> call, Response<LoginResponce> response) {


                if (kProgressHUD.isShowing())
                    kProgressHUD.dismiss();
                if (response.isSuccessful()) {
                    if (response.code() >= 200 && response.code() < 700) {
                        if (response.code() == 200) {
                            if (response.body().getStatus() == 1) {
                                if (response.body().getData() != null) {
                                    Gson gson = new Gson();
                                    String json = gson.toJson(response.body(), LoginResponce.class);
                                    Log.i(TAG, "json " + json);
                                    saveData(response.body());
                                } else {
                                    displaySnackBar.DisplaySnackBar("Data not found", Constant.TYPE_ERROR);
                                }
                            } else if (response.body().getStatus() == 0) {
                                try {
                                    showWarningSimpleAlertDialog(response.body().getData().getMessage(), response.body().getData().getValidation_error().getPassword().get(0));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            callDisplayErrorCode(response.code(), "");
                        }
                    }
                    if (response.code() ==500) {
                        displaySnackBar.DisplaySnackBar(Constant.UNEXPECTED, Constant.TYPE_ERROR);

                    }
                    else {
                        displaySnackBar.DisplaySnackBar(Constant.UNEXPECTED, Constant.TYPE_ERROR);

                    }
                } else {

                    try {
                        ErrorBody errorBody = new ErrorBody();
                        Gson gson = new Gson();
                        errorBody = gson.fromJson(response.errorBody().string(), ErrorBody.class);
                        callDisplayErrorCode(Integer.parseInt(errorBody.getStatus()), errorBody.getMessage());
                        displaySnackBar.DisplaySnackBar(errorBody.getMessage(), Constant.TYPE_ERROR);
                        Log.i("error1", errorBody.getMessage());
                    } catch (Exception e) {
                        displaySnackBar.DisplaySnackBar(Constant.ERROR_INVALID_JSON, Constant.TYPE_ERROR);

                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<LoginResponce> call, Throwable t) {
                if (kProgressHUD.isShowing())
                    kProgressHUD.dismiss();
                if (t != null && (t instanceof IOException || t instanceof SocketTimeoutException
                        || t instanceof ConnectException || t instanceof NoRouteToHostException
                        || t instanceof SecurityException)) {
                    displaySnackBar.DisplaySnackBar(Constant.NETWIRK_ERROR, Constant.TYPE_ERROR);

                }
            }
        });
    }

    private void callForgotPasswordService(final String email) {
        kProgressHUD = KProgressHUD.create(context)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(false)
                .setWindowColor(getResources().getColor(R.color.colorPrimaryDark))
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        ForgotPasswordRequest gotPasswordRequest = new ForgotPasswordRequest();
        gotPasswordRequest.setPhone_number(email);

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        final Call<ForgotResponce> call = apiService.postForgotPasswrod(mySingleton.getData(Constant.TOKEN_BASE_64), gotPasswordRequest);
        call.enqueue(new Callback<ForgotResponce>() {
            @Override
            public void onResponse(Call<ForgotResponce> call, Response<ForgotResponce> response) {
                kProgressHUD.dismiss();
                if (response.isSuccessful()) {
                    if (response.code() >= 200 && response.code() < 550) {
                        Gson gson = new Gson();
                        String json = gson.toJson(response.body(), ForgotResponce.class);

                        Log.i("codeJson", json + "");
                        Log.i("code", response.code() + "");
                        if (response.body().getStatus() == 1) {
                            dialog.dismiss();
                            showSimpleAlertDialog(response.body().getData().getMessage());
                        } else {
                            showWarningSimpleAlertDialog(Constant.TITLE, response.body().getData().getMessage());
                        }
                    }
                } else {

                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Toast.makeText(context, jObjError.get("error").toString(), Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {

                        e.printStackTrace();
                        Toast.makeText(context, " The data couldn’t be read because it isn’t in the correct format.", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<ForgotResponce> call, Throwable t) {
                if (t != null && (t instanceof IOException || t instanceof SocketTimeoutException || t instanceof ConnectException)) {
                    if (t instanceof SocketTimeoutException || t instanceof TimeoutException) {
                        Toast.makeText(context, "Connection Time out. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                }
                kProgressHUD.dismiss();
            }
        });
    }

    private void callAppBannerService() {
        kProgressHUD = KProgressHUD.create(context)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(false)
                .setWindowColor(getResources().getColor(R.color.colorPrimaryDark))
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);


        final Call<CarouselResponse> call = apiService.getCarousel("0", "home");
        call.enqueue(new Callback<CarouselResponse>() {
            @Override
            public void onResponse(Call<CarouselResponse> call, Response<CarouselResponse> response) {
                kProgressHUD.dismiss();
                if (response.code() == 200) {
                    if (response.body().getStatus() == 1) {
                        showAppBannerDialog(response.body().getData().getAllGroups().get(0).getImage());
                    }
                } else {

                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Toast.makeText(context, jObjError.get("error").toString(), Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {

                        e.printStackTrace();
                        Toast.makeText(context, " The data couldn’t be read because it isn’t in the correct format.", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<CarouselResponse> call, Throwable t) {
                if (t != null && (t instanceof IOException || t instanceof SocketTimeoutException || t instanceof ConnectException)) {
                    if (t instanceof SocketTimeoutException || t instanceof TimeoutException) {
                        Toast.makeText(context, "Connection Time out. Please try again.", Toast.LENGTH_SHORT).show();
                        //avsInterface.onError(call, new AvsException("Oops something went wrong, please try again later..."));
                    }
                }
                kProgressHUD.dismiss();
            }
        });
    }

    private void showAppBannerDialog(String image) {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_app_banner);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );

        Glide.with(this)
                .load(image)
                .into((ImageView) dialog.findViewById(R.id.ivAppBanner));

        dialog.findViewById(R.id.ivCross).setOnClickListener(view -> dialog.dismiss());
        dialog.show();
    }

    public void setTaskBarColored() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.WHITE);
        }
    }


}
