package com.rebliss.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.gson.Gson;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.rebliss.utils.App;
import com.rebliss.R;
import com.rebliss.data.perf.MySingleton;
import com.rebliss.domain.constant.Constant;
import com.rebliss.domain.model.ErrorBody;
import com.rebliss.domain.model.editprofile.EditProfileRequest;
import com.rebliss.domain.model.editprofile.EditProfileResponce;
import com.rebliss.domain.model.editprofile.Partner;
import com.rebliss.domain.model.logout.LogoutResponce;
import com.rebliss.domain.model.profile.Data;
import com.rebliss.domain.model.profile.ProfileResponce;
import com.rebliss.presenter.helper.DisplaySnackBar;
import com.rebliss.presenter.helper.Logout;
import com.rebliss.presenter.helper.Network;
import com.rebliss.presenter.helper.TextUtil;
import com.rebliss.presenter.retrofit.ApiClient;
import com.rebliss.presenter.retrofit.ApiInterface;
import java.io.IOException;
import java.net.ConnectException;
import java.net.NoRouteToHostException;
import java.net.SocketTimeoutException;
import java.text.DateFormatSymbols;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardBusinessDetails extends AppCompatActivity {
    private static final String TAG = DashboardBusinessDetails.class.getSimpleName();
    private Context context;
    private TextView textChName, textChLName, textBusinessType, textLocation, textManpower, textTurnOver, textOtherBusiness, textHeader;
    private ImageView imgLogout, imgCal;
    private Button btnSave;
    private EditText etChName, etBusinessType, etLocation, etManpower, etTurnOver, etOtherBusiness;
    private MySingleton mySingleton;
    private Network network;
    private KProgressHUD kProgressHUD;
    private DisplaySnackBar displaySnackBar;

    private String sChName = "", sBusinessType = "", sLocation = "", sManpowerOB = "", sTurnOver = "",
            sOtherBusiness = "";
    public static EditProfileRequest editProfileRequest;
    private Intent intent;
    String callFrom = "";
    private TextView textStepOne, textStepTwo, textStepThree, textStepFour;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_seond);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        Gson gson = new Gson();
        String json = gson.toJson(editProfileRequest, EditProfileRequest.class);
        String jsonData = gson.toJson(DashboardCPDetails.profileData, Data.class);
        Log.i(TAG, "editProfileRequest Data " + json);
        Log.i(TAG, "DashboardCPDetails.profileData Data " + jsonData);
        initView();
        viewListener();

        try {
            intent = getIntent();
            callFrom = intent.getStringExtra("call_from");
        } catch (Exception e) {
            e.printStackTrace();
        }

        disable();


    }

    private void disable() {
        if (!TextUtil.isStringNullOrBlank(callFrom)) {
            if (callFrom.equalsIgnoreCase("1")) {

                etChName.setEnabled(false);
                etBusinessType.setEnabled(false);
                etLocation.setEnabled(false);
                etManpower.setEnabled(false);
                etTurnOver.setEnabled(false);
                etOtherBusiness.setEnabled(false);


                etChName.setClickable(false);
                etBusinessType.setClickable(false);
                etLocation.setClickable(false);
                etManpower.setClickable(false);
                etTurnOver.setClickable(false);
                etOtherBusiness.setClickable(false);


                btnSave.setText("Next");
            }
        }
    }

    private void initView() {
        context = DashboardBusinessDetails.this;
        mySingleton = new MySingleton(this);
        displaySnackBar = new DisplaySnackBar(this);
        network = new Network();


        textStepOne = findViewById(R.id.textStepOne);
        textStepTwo = findViewById(R.id.textStepTwo);
        textStepThree = findViewById(R.id.textStepThree);
        textStepFour = findViewById(R.id.textStepFour);
        textStepOne.setTypeface(App.LATO_REGULAR);
        textStepTwo.setTypeface(App.LATO_REGULAR);
        textStepThree.setTypeface(App.LATO_REGULAR);
        textStepFour.setTypeface(App.LATO_REGULAR);
        textHeader = findViewById(R.id.textHeader);
        textChName = findViewById(R.id.textChName);
        textBusinessType = findViewById(R.id.textBusinessType);
        textLocation = findViewById(R.id.textLocation);
        textManpower = findViewById(R.id.textManpower);
        textTurnOver = findViewById(R.id.textTurnOver);
        textOtherBusiness = findViewById(R.id.textOtherBusiness);

        imgLogout = findViewById(R.id.imgLogout);
        imgCal = findViewById(R.id.imgCal);
        imgLogout.setVisibility(View.GONE);

        btnSave = findViewById(R.id.btnSave);

        etChName = findViewById(R.id.etChName);
        etBusinessType = findViewById(R.id.etBusinessType);
        etLocation = findViewById(R.id.etLocation);
        etManpower = findViewById(R.id.etManpower);
        etTurnOver = findViewById(R.id.etTurnOver);
        etOtherBusiness = findViewById(R.id.etOtherBusiness);
        setFontOnView();
        showDataOnView();
    }

    private void setFontOnView() {
        textHeader.setTypeface(App.LATO_REGULAR);
        textChName.setTypeface(App.LATO_REGULAR);
        textChName.setTypeface(App.LATO_REGULAR);
        textBusinessType.setTypeface(App.LATO_REGULAR);
        textLocation.setTypeface(App.LATO_REGULAR);
        textManpower.setTypeface(App.LATO_REGULAR);
        textTurnOver.setTypeface(App.LATO_REGULAR);
        textOtherBusiness.setTypeface(App.LATO_REGULAR);

        etChName.setTypeface(App.LATO_REGULAR);
        etBusinessType.setTypeface(App.LATO_REGULAR);

        etLocation.setTypeface(App.LATO_REGULAR);
        etManpower.setTypeface(App.LATO_REGULAR);
        etTurnOver.setTypeface(App.LATO_REGULAR);
        etOtherBusiness.setTypeface(App.LATO_REGULAR);

        btnSave.setTypeface(App.LATO_REGULAR);

    }

    private void showDataOnView() {
        if (DashboardCPDetails.profileData != null) {
            if (DashboardCPDetails.profileData.getPartner() != null) {
                etChName.setText((!TextUtil.isStringNullOrBlank(DashboardCPDetails.profileData.getPartner().getName()) ? DashboardCPDetails.profileData.getPartner().getName() : ""));
            }
            etBusinessType.setText((!TextUtil.isStringNullOrBlank(DashboardCPDetails.profileData.getBusiness_type()) ? DashboardCPDetails.profileData.getBusiness_type() : ""));
            etLocation.setText((!TextUtil.isStringNullOrBlank(DashboardCPDetails.profileData.getBusiness_locations()) ? DashboardCPDetails.profileData.getBusiness_locations() : ""));
            etManpower.setText((!TextUtil.isStringNullOrBlank(DashboardCPDetails.profileData.getManpower_strength()) ? DashboardCPDetails.profileData.getManpower_strength() : ""));
            etTurnOver.setText((!TextUtil.isStringNullOrBlank(DashboardCPDetails.profileData.getTurnover_business()) ? DashboardCPDetails.profileData.getTurnover_business() : ""));
        }
    }

    private void viewListener() {
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
                if (!TextUtil.isStringNullOrBlank(callFrom)) {
                    if (callFrom.equalsIgnoreCase("1")) {
                        EditProfileRequest editProfileRequest = new EditProfileRequest();
                        callNextStep(editProfileRequest);
                    } else {
                        if (isFormValidated()) {
                            if (network.isNetworkConnected(context)) {
                                callUpdateProfile();
                            } else {
                                displaySnackBar.DisplaySnackBar(Constant.NETWIRK_ERROR, Constant.TYPE_ERROR);
                            }
                        }
                    }
                } else if (isFormValidated()) {
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


    private void callNextStep(EditProfileRequest editProfileRequest) {
        Intent stepSecond = new Intent(context, DashboardDocUpload.class);
        if (!TextUtil.isStringNullOrBlank(callFrom)) {
            if (callFrom.equalsIgnoreCase("1")) {
                stepSecond.putExtra("call_from", "1");
            }
        }

        DashboardDocUpload.editProfileRequest = editProfileRequest;
        startActivity(stepSecond);
    }


    public String checkDigit(int number) {
        return number <= 9 ? "0" + number : String.valueOf(number);
    }


    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    private boolean isFormValidated() {
        boolean status = true;

        sChName = etChName.getText().toString().trim();
        sBusinessType = etBusinessType.getText().toString().trim();

        sLocation = etLocation.getText().toString().trim();
        sManpowerOB = etManpower.getText().toString().trim();
        sTurnOver = etTurnOver.getText().toString().trim();
        sOtherBusiness = etOtherBusiness.getText().toString().trim();

        if (sChName.length() <= 0) {
            status = false;
            showWarningSimpleAlertDialog(Constant.TITLE, "Please enter partner name");
        } else if (sBusinessType.length() <= 0) {
            status = false;
            showWarningSimpleAlertDialog(Constant.TITLE, "Please enter partner business type");
        } else if (sLocation.length() <= 0) {
            status = false;
            showWarningSimpleAlertDialog(Constant.TITLE, "Please enter current business location");
        } else if (sManpowerOB.length() <= 0) {
            status = false;
            showWarningSimpleAlertDialog(Constant.TITLE, "Enter Current Manpower Strength (Business wise and Total)");
        } else if (sTurnOver.length() <= 0) {
            status = false;
            showWarningSimpleAlertDialog(Constant.TITLE, "Enter Turnover Business wise and total turn over");
        }


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
                                //showWarningSimpleAlertDialog(response.body().getData().getMessage(), response.body().getData().getValidation_error().getPassword().get(0));
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
                                Log.e(TAG, "onResponse: status 0 for DashboardBusinessDetail" );                            }
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
        Partner partner = new Partner();
        try {
            partner.setName(sChName);
            editProfileRequest.setPartner(partner);
            editProfileRequest.setBusiness_type(sBusinessType);
            editProfileRequest.setBusiness_locations(sLocation);
            editProfileRequest.setManpower_strength(sManpowerOB);
            editProfileRequest.setTurnover_business(sTurnOver);
        } catch (Exception e) {
            e.printStackTrace();
        }


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

                                    displaySnackBar.DisplaySnackBar(response.body().getData().getMessage().getSuccess().get(0), Constant.TYPE_ERROR);
                                    callNextStep(editProfileRequest);
                                } else {
                                    displaySnackBar.DisplaySnackBar("Data not found", Constant.TYPE_ERROR);

                                }
                            } else if (response.body().getStatus() == 0) {
                                showWarningSimpleAlertDialog(Constant.TITLE, response.body().getData().getMessage().getSuccess().get(0));
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
