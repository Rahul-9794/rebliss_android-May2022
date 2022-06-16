package com.rebliss.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.gson.Gson;
import com.kaopiz.kprogresshud.KProgressHUD;

import com.rebliss.R;
import com.rebliss.data.perf.MySingleton;
import com.rebliss.domain.constant.Constant;
import com.rebliss.domain.model.ErrorBody;
import com.rebliss.domain.model.editprofile.EditProfileRequest;
import com.rebliss.domain.model.editprofile.EditProfileResponce;
import com.rebliss.domain.model.logout.LogoutResponce;
import com.rebliss.domain.model.profile.Data;
import com.rebliss.domain.model.profile.ProfileResponce;
import com.rebliss.presenter.helper.DisplaySnackBar;
import com.rebliss.presenter.helper.Logout;
import com.rebliss.presenter.helper.Network;
import com.rebliss.presenter.helper.ShowHintOrText;
import com.rebliss.presenter.helper.TextUtil;
import com.rebliss.presenter.retrofit.ApiClient;
import com.rebliss.presenter.retrofit.ApiInterface;
import com.rebliss.utils.App;
import com.rebliss.view.activity.selfassessmenttest.SelfAssessmentQuestionsActivity;
import java.io.IOException;
import java.net.ConnectException;
import java.net.NoRouteToHostException;
import java.net.SocketTimeoutException;
import java.text.DateFormatSymbols;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardBankDetails extends AppCompatActivity {
    private static final String TAG = DashboardBankDetails.class.getSimpleName();
    private Context context;
    private TextView textBankDetail, textAcName, textBankName, textAcNo, textIFSC, textHeader, textcAcNo;
    private ImageView imgLogout, imgCal;
    private Button btnSave;
    private EditText etBankDetail, etAcName, etBankName, etAcNo, etIFSC, etcAcNo;
    private MySingleton mySingleton;
    private Network network;
    private KProgressHUD kProgressHUD;
    private DisplaySnackBar displaySnackBar;

    private String sBankDetail = "", sAcName = "", sBankName = "",sAcNo = "", scAcNo = "", sIFSC = "";


    private int Day, Month, Year;
    private int mYear, mMonth, mDay, mHour, mMinute;
    public static EditProfileRequest editProfileRequest;
    private Intent intent;
    private TextView textStepOne, textStepTwo, textStepThree, textStepFour;
    private ImageView imgStepOne, imgStepTwo, imgStepThree, imgStepFour;
    private View viewStepOne, viewStepTwo, viewStepThree;
    private String call_from, callFromEnable = "";
    private LinearLayout stepThree, stepFour,llGST;
    ImageView icBack;

    TextView mywidget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_bank);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        intent = getIntent();
        call_from = intent.getStringExtra("call_from");
        callFromEnable = intent.getStringExtra("call_from_enable");

        editProfileRequest = (EditProfileRequest) intent.getSerializableExtra("saved_data");

        Gson gson = new Gson();
        String json = gson.toJson(editProfileRequest, EditProfileRequest.class);
        String jsonData = gson.toJson(DashboardCPDetails.profileData, Data.class);
        Log.i(TAG, "editProfileRequest Data " + json);
        Log.i(TAG, "DashboardCPDetails.profileData Data " + jsonData);

        initView();
        viewListener();

        disable();
    }

    private void disable() {
        if (!TextUtil.isStringNullOrBlank(callFromEnable)) {
            if (callFromEnable.equalsIgnoreCase("1")) {
                btnSave.setText("Finish");
            }
        }

    }

    private void initView() {
        context = DashboardBankDetails.this;
        mySingleton = new MySingleton(this);
        displaySnackBar = new DisplaySnackBar(this);
        network = new Network();
        icBack = findViewById(R.id.icBack);
        mywidget = findViewById(R.id.mywidget);
        textcAcNo = findViewById(R.id.textcAcNo);

        textHeader = findViewById(R.id.textHeader);
        textBankDetail = findViewById(R.id.textBankDetail);
        textAcName = findViewById(R.id.textAcName);
        textBankName = findViewById(R.id.textBankName);
        textAcNo = findViewById(R.id.textAcNo);
        textIFSC = findViewById(R.id.textIFSC);
        imgLogout = findViewById(R.id.imgLogout);
        imgCal = findViewById(R.id.imgCal);
        btnSave = findViewById(R.id.btnSave);
        etcAcNo = findViewById(R.id.etcAcNo);
        etBankDetail = findViewById(R.id.etBankDetail);
        etAcName = findViewById(R.id.etAcName);
        etBankName = findViewById(R.id.etBankName);
        etAcNo = findViewById(R.id.etAcNo);
        etIFSC = findViewById(R.id.etIFSC);

        stepThree = findViewById(R.id.stepThree);

        setFontOnView();
        showDataOnView();
        setHint();
    }

    private void setHint() {

        textAcName.setText(ShowHintOrText.GetOptional("Bank Account Holder Name"));
        textBankName.setText(ShowHintOrText.GetOptional("Bank name"));
        textAcNo.setText(ShowHintOrText.GetOptional("Account No."));
        textcAcNo.setText(ShowHintOrText.GetOptional("Confirm account No."));
        textIFSC.setText(ShowHintOrText.GetOptional("IFSC Code"));

    }

    private void setFontOnView() {


        /*
        mywidget.setTypeface(App.LATO_REGULAR);
        textHeader.setTypeface(App.LATO_REGULAR);
        // textBankDetail.setTypeface(App.LATO_REGULAR);
        // textAcName.setTypeface(App.LATO_REGULAR);
        // textBankName.setTypeface(App.LATO_REGULAR);
        // textAcNo.setTypeface(App.LATO_REGULAR);
        // textIFSC.setTypeface(App.LATO_REGULAR);

        etBankDetail.setTypeface(App.LATO_REGULAR);
        etAcName.setTypeface(App.LATO_REGULAR);
        etBankName.setTypeface(App.LATO_REGULAR);
        etAcNo.setTypeface(App.LATO_REGULAR);
        etIFSC.setTypeface(App.LATO_REGULAR);
        //  btnSave.setTypeface(App.LATO_REGULAR);
        //  textcAcNo.setTypeface(App.LATO_REGULAR);
        etcAcNo.setTypeface(App.LATO_REGULAR);



         */
    }

    private void showDataOnView() {
        if (call_from.equalsIgnoreCase("DashboardDocUpload")) {
            if (mySingleton.getData(Constant.USER_GROUP_ID).equalsIgnoreCase(Constant.SUPER_CP_GROUP_ID)) {
                if (DashboardSuperCPDetails.profileData != null) {
                    if (DashboardSuperCPDetails.profileData.getProfile_verified().equalsIgnoreCase("0")
                            || DashboardSuperCPDetails.profileData.getProfile_verified().equalsIgnoreCase("2")) {
                        if (!TextUtil.isStringNullOrBlank(DashboardSuperCPDetails.profileData.getDecline_message())) {
                            mywidget.setVisibility(View.VISIBLE);
                            mywidget.setText((!TextUtil.isStringNullOrBlank(DashboardSuperCPDetails.profileData.getDecline_message()) ? DashboardSuperCPDetails.profileData.getDecline_message() : ""));
                        } else {
                            mywidget.setVisibility(View.GONE);
                        }
                    } else {
                        mywidget.setVisibility(View.GONE);
                    }
                    if (DashboardSuperCPDetails.profileData.getPartner() != null)
                        //    etBankDetail.setText((!TextUtil.isStringNullOrBlank(DashboardCPDetails.profileData.get().getName()) ? DashboardCPDetails.profileData.getPartner().getName() : ""));
                        etAcName.setText((!TextUtil.isStringNullOrBlank(DashboardSuperCPDetails.profileData.getBank_holder_name()) ? DashboardSuperCPDetails.profileData.getBank_holder_name() : ""));
                    etBankName.setText((!TextUtil.isStringNullOrBlank(DashboardSuperCPDetails.profileData.getBank_name()) ? DashboardSuperCPDetails.profileData.getBank_name() : ""));
                    etAcNo.setText((!TextUtil.isStringNullOrBlank(DashboardSuperCPDetails.profileData.getAccount_number()) ? DashboardSuperCPDetails.profileData.getAccount_number() : ""));
                    etcAcNo.setText((!TextUtil.isStringNullOrBlank(DashboardSuperCPDetails.profileData.getAccount_number()) ? DashboardSuperCPDetails.profileData.getAccount_number() : ""));
                    etIFSC.setText((!TextUtil.isStringNullOrBlank(DashboardSuperCPDetails.profileData.getIfsc_code()) ? DashboardSuperCPDetails.profileData.getIfsc_code() : ""));
                }
            } else {
                if (DashboardCPDetails.profileData != null) {
                    if (DashboardCPDetails.profileData.getProfile_verified().equalsIgnoreCase("0")
                            || DashboardCPDetails.profileData.getProfile_verified().equalsIgnoreCase("2")) {
                        if (!TextUtil.isStringNullOrBlank(DashboardCPDetails.profileData.getDecline_message())) {
                            mywidget.setVisibility(View.VISIBLE);
                            mywidget.setText((!TextUtil.isStringNullOrBlank(DashboardCPDetails.profileData.getDecline_message()) ? DashboardCPDetails.profileData.getDecline_message() : ""));
                        } else {
                            mywidget.setVisibility(View.GONE);
                        }
                    } else {
                        mywidget.setVisibility(View.GONE);
                    }
                    if (DashboardCPDetails.profileData.getPartner() != null)
                        etAcName.setText((!TextUtil.isStringNullOrBlank(DashboardCPDetails.profileData.getBank_holder_name()) ? DashboardCPDetails.profileData.getBank_holder_name() : ""));
                    etBankName.setText((!TextUtil.isStringNullOrBlank(DashboardCPDetails.profileData.getBank_name()) ? DashboardCPDetails.profileData.getBank_name() : ""));
                    etAcNo.setText((!TextUtil.isStringNullOrBlank(DashboardCPDetails.profileData.getAccount_number()) ? DashboardCPDetails.profileData.getAccount_number() : ""));
                    etcAcNo.setText((!TextUtil.isStringNullOrBlank(DashboardCPDetails.profileData.getAccount_number()) ? DashboardCPDetails.profileData.getAccount_number() : ""));
                    etIFSC.setText((!TextUtil.isStringNullOrBlank(DashboardCPDetails.profileData.getIfsc_code()) ? DashboardCPDetails.profileData.getIfsc_code() : ""));
                }

            }

        } else if (call_from.equalsIgnoreCase("DashboardFOSDocUpload")) {
            if (CallEditFOS.profileData != null) {
                if (CallEditFOS.profileData.getProfile_verified().equalsIgnoreCase("0")
                        || CallEditFOS.profileData.getProfile_verified().equalsIgnoreCase("2")) {
                    if (!TextUtil.isStringNullOrBlank(CallEditFOS.profileData.getDecline_message())) {
                        mywidget.setVisibility(View.VISIBLE);
                        mywidget.setText((!TextUtil.isStringNullOrBlank(CallEditFOS.profileData.getDecline_message()) ? CallEditFOS.profileData.getDecline_message() : ""));
                    } else {
                        mywidget.setVisibility(View.GONE);
                    }
                } else {
                    mywidget.setVisibility(View.GONE);
                }

                if (CallEditFOS.profileData.getPartner() != null)
                    //    etBankDetail.setText((!TextUtil.isStringNullOrBlank(DashboardCPDetails.profileData.get().getName()) ? DashboardCPDetails.profileData.getPartner().getName() : ""));
                    etAcName.setText((!TextUtil.isStringNullOrBlank(CallEditFOS.profileData.getBank_holder_name()) ? CallEditFOS.profileData.getBank_holder_name() : ""));
                etBankName.setText((!TextUtil.isStringNullOrBlank(CallEditFOS.profileData.getBank_name()) ? CallEditFOS.profileData.getBank_name() : ""));
                etAcNo.setText((!TextUtil.isStringNullOrBlank(CallEditFOS.profileData.getAccount_number()) ? CallEditFOS.profileData.getAccount_number() : ""));
                etcAcNo.setText((!TextUtil.isStringNullOrBlank(CallEditFOS.profileData.getAccount_number()) ? CallEditFOS.profileData.getAccount_number() : ""));
                etIFSC.setText((!TextUtil.isStringNullOrBlank(CallEditFOS.profileData.getIfsc_code()) ? CallEditFOS.profileData.getIfsc_code() : ""));
            }
        }
    }

    private void viewListener() {
        icBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        imgLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (network.isNetworkConnected(context)) {
                    callLogout();
                } else {
                    displaySnackBar.DisplaySnackBar(Constant.NETWIRK_ERROR, Constant.TYPE_ERROR);
                }
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isFormValidated()) {
                    if (network.isNetworkConnected(context)) {
                        callUpdateProfile();
                    } else {
                        displaySnackBar.DisplaySnackBar(Constant.NETWIRK_ERROR, Constant.TYPE_ERROR);
                    }
                }

            }
        });

    }


    String getMonthForInt(int m) {
        String month = "invalid";
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getMonths();
        if (m >= 0 && m <= 11) {
            month = months[m];
        }
        return month;
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


    private void showSimpleAlertDialog(String message) {
        new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE).setTitleText(Constant.TITLE)
                .setContentText(message)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();

                        Intent login = new Intent(context, ActivityLogin.class);
                        String token = mySingleton.getData(Constant.DEVICE_FCM_TOKEN);
                        mySingleton.clearData();
                        mySingleton.saveData(Constant.DEVICE_FCM_TOKEN, token);
                        login.putExtra(Constant.UNAUTHORISE_TOKEN, "0");
                        login.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        login.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        login.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(login);
                        finish();
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


    private void callNextStep() {
        Intent stepSecond = new Intent(context, SelfAssessmentQuestionsActivity.class);
        if (call_from.equalsIgnoreCase("DashboardDocUpload")) {
            if (mySingleton.getData(Constant.USER_GROUP_ID).equalsIgnoreCase(Constant.SUPER_CP_GROUP_ID)) {
                stepSecond.putExtra("profile_varified", DashboardSuperCPDetails.profileData.getProfile_verified());
            } else {
                stepSecond.putExtra("profile_varified", DashboardCPDetails.profileData.getProfile_verified());
            }
        } else if (call_from.equalsIgnoreCase("DashboardFOSDocUpload")) {
            stepSecond.putExtra("profile_varified", mySingleton.getData(Constant.USER_PROFILE_VERIFYED));
        }

        stepSecond.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        stepSecond.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        stepSecond.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(stepSecond);
        finish();
    }


    public String checkDigit(int number) {
        return number <= 9 ? "0" + number : String.valueOf(number);
    }


    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    private boolean isFormValidated() {
        boolean status = true;

        sBankDetail = etBankDetail.getText().toString().trim();
        sAcName = etAcName.getText().toString().trim();

        sBankName = etBankName.getText().toString().trim();
        sAcNo = etAcNo.getText().toString().trim();
        scAcNo = etcAcNo.getText().toString().trim();
        sIFSC = etIFSC.getText().toString().trim();

        return status;
    }

    public void callProfileAPI() {

        kProgressHUD = KProgressHUD.create(context)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .setWindowColor(getResources().getColor(R.color.progressbar_color))
                .show();

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        final Call<ProfileResponce> call = apiService.getProfile(mySingleton.getData(Constant.TOKEN_BASE_64));
        call.enqueue(new Callback<ProfileResponce>() {
            @Override
            public void onResponse(Call<ProfileResponce> call, Response<ProfileResponce> response) {
                kProgressHUD.dismiss();
                if (response.isSuccessful()) {
                    if (response.code() >= 200 && response.code() < 700) {
                        if (response.code() == 200) {
                            if (response.body().getStatus() == 1) {
                                if (response.body().getData() != null) {
                                    Gson gson = new Gson();
                                    String json = gson.toJson(response.body(), ProfileResponce.class);
                                    Log.i(TAG, "json " + json);
                                    DashboardCPDetails.profileData = response.body().getData();
                                    showDataOnView();
                                } else {
                                    displaySnackBar.DisplaySnackBar("Data not found", Constant.TYPE_ERROR);

                                }
                            } else if (response.body().getStatus() == 0) {
                                Log.e(TAG, "onResponse: status 0 DashboardBankDetail" );                            }
                            callDisplayErrorCode(response.code(), "");
                        }

                    } else {
                        displaySnackBar.DisplaySnackBar(Constant.UNEXPECTED, Constant.TYPE_ERROR);

                    }
                } else {

                    try {
                        ErrorBody errorBody = new ErrorBody();
                        Gson gson = new Gson();
                        errorBody = gson.fromJson(response.errorBody().string(), ErrorBody.class);
                        callDisplayErrorCode(Integer.parseInt(errorBody.getStatus()), errorBody.getMessage());
                        displaySnackBar.DisplaySnackBar(errorBody.getMessage(), Constant.TYPE_ERROR);
                        if (errorBody.getName().equalsIgnoreCase(context.getString(R.string.unAuthorisedUser))) {
                            Logout.Login(context);
                        }
                    } catch (Exception e) {
                        displaySnackBar.DisplaySnackBar(Constant.ERROR_INVALID_JSON, Constant.TYPE_ERROR);

                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<ProfileResponce> call, Throwable t) {
                kProgressHUD.dismiss();
                if (t != null && (t instanceof IOException || t instanceof SocketTimeoutException
                        || t instanceof ConnectException || t instanceof NoRouteToHostException
                        || t instanceof SecurityException)) {
                    displaySnackBar.DisplaySnackBar(Constant.NETWIRK_ERROR, Constant.TYPE_ERROR);

                }
            }
        });
    }

    public void callLogout() {


        kProgressHUD = KProgressHUD.create(context)
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
                                    Log.i(TAG, "json " + json);
                                    showSimpleAlertDialog(response.body().getMessage());
                                } else {
                                    displaySnackBar.DisplaySnackBar("Data not found", Constant.TYPE_ERROR);

                                }
                            } else if (response.body().getStatus() == 0) {
                                Log.e(TAG, "onResponse: status 0 DashboardBankDetails" );                            }
                            callDisplayErrorCode(response.code(), "");
                        }

                    } else {
                        displaySnackBar.DisplaySnackBar(Constant.UNEXPECTED, Constant.TYPE_ERROR);

                    }
                } else {

                    try {
                        ErrorBody errorBody = new ErrorBody();
                        Gson gson = new Gson();
                        errorBody = gson.fromJson(response.errorBody().string(), ErrorBody.class);
                        callDisplayErrorCode(Integer.parseInt(errorBody.getStatus()), errorBody.getMessage());
                        displaySnackBar.DisplaySnackBar(errorBody.getMessage(), Constant.TYPE_ERROR);

                        // Toast.makeText(context, errorBody.getMessage(), Toast.LENGTH_SHORT).show();
                        if (errorBody.getName().equalsIgnoreCase(context.getString(R.string.unAuthorisedUser))) {
                            Logout.Login(context);
                        }
                    } catch (Exception e) {
                        displaySnackBar.DisplaySnackBar(Constant.ERROR_INVALID_JSON, Constant.TYPE_ERROR);

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
                    displaySnackBar.DisplaySnackBar(Constant.NETWIRK_ERROR, Constant.TYPE_ERROR);
                }
            }
        });
    }

    public void callUpdateProfile() {
        if (editProfileRequest == null) {
            editProfileRequest = new EditProfileRequest();
        }
        editProfileRequest.setBank_holder_name(sAcName);
        editProfileRequest.setBank_name(sBankName);
        editProfileRequest.setIfsc_code(sIFSC.toUpperCase());
        editProfileRequest.setAccount_number(sAcNo);
        editProfileRequest.setProfile_verified("2");


        Gson gson = new Gson();
        String json = gson.toJson(editProfileRequest, EditProfileRequest.class);
        Log.i(TAG, "Request Data " + json);

        kProgressHUD = KProgressHUD.create(context)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .setWindowColor(getResources().getColor(R.color.progressbar_color))
                .show();

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        final Call<EditProfileResponce> call = apiService.postEditProfile(mySingleton.getData(Constant.TOKEN_BASE_64), editProfileRequest);
        call.enqueue(new Callback<EditProfileResponce>() {
            @Override
            public void onResponse(Call<EditProfileResponce> call, Response<EditProfileResponce> response) {
                kProgressHUD.dismiss();
                if (response.isSuccessful()) {
                    if (response.code() >= 200 && response.code() < 700) {
                        if (response.code() == 200) {
                            Gson gson = new Gson();
                            String json = gson.toJson(response.body(), EditProfileResponce.class);
                            Log.i(TAG, "json " + json);
                            if (response.body().getStatus() == 1) {
                                if (response.body().getData() != null) {
                                    //changes done by bhavesh chand on 2 june 2021
                                    if (mySingleton.getData(Constant.USER_GROUP_ID).equalsIgnoreCase(Constant.FOS_GROUP_ID))
                                    {
                                        callNextStep();
                                    }
                                    else
                                    {
                                        startActivity(new Intent(DashboardBankDetails.this, ActivityDashboard.class).putExtra("profile_varified", "0"));
                                    }
                                } else {
                                    displaySnackBar.DisplaySnackBar("Data not found", Constant.TYPE_ERROR);

                                }
                            } else if (response.body().getStatus() == 0) {
                                if (response.body().getData().getMessage().getPersonal_email_id() != null) {
                                    showWarningSimpleAlertDialog(Constant.TITLE, response.body().getData().getMessage().getPersonal_email_id().get(0));
                                }
                                if (response.body().getData().getMessage().getOfficial_email_id() != null) {
                                    showWarningSimpleAlertDialog(Constant.TITLE, response.body().getData().getMessage().getOfficial_email_id().get(0));
                                }
                            }
                            callDisplayErrorCode(response.code(), "");
                        }

                    } else {
                        displaySnackBar.DisplaySnackBar(Constant.UNEXPECTED, Constant.TYPE_ERROR);

                    }
                } else {

                    try {
                        ErrorBody errorBody = new ErrorBody();
                        Gson gson = new Gson();
                        errorBody = gson.fromJson(response.errorBody().string(), ErrorBody.class);
                        callDisplayErrorCode(Integer.parseInt(errorBody.getStatus()), errorBody.getMessage());
                        displaySnackBar.DisplaySnackBar(errorBody.getMessage(), Constant.TYPE_ERROR);
                        if (errorBody.getName().equalsIgnoreCase(context.getString(R.string.unAuthorisedUser))) {
                            Logout.Login(context);
                        }
                    } catch (Exception e) {
                        displaySnackBar.DisplaySnackBar(Constant.ERROR_INVALID_JSON, Constant.TYPE_ERROR);

                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<EditProfileResponce> call, Throwable t) {
                kProgressHUD.dismiss();
                if (t != null && (t instanceof IOException || t instanceof SocketTimeoutException
                        || t instanceof ConnectException || t instanceof NoRouteToHostException
                        || t instanceof SecurityException)) {
                    displaySnackBar.DisplaySnackBar(Constant.NETWIRK_ERROR, Constant.TYPE_ERROR);

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
