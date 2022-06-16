package com.rebliss.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.gson.Gson;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.rebliss.utils.App;
import com.rebliss.R;
import com.rebliss.data.perf.MySingleton;
import com.rebliss.domain.constant.Constant;
import com.rebliss.domain.model.ErrorBody;
import com.rebliss.domain.model.passwordsetup.PasswordSetUpRequest;
import com.rebliss.domain.model.passwordsetup.PasswordSetupResponse;
import com.rebliss.presenter.helper.Base64Encoded;
import com.rebliss.presenter.helper.DisplaySnackBar;
import com.rebliss.presenter.helper.Network;
import com.rebliss.presenter.helper.ShowHintOrText;
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

public class PasswordSetupActivity extends AppCompatActivity {
    private EditText etPassword, etCPassword;
    Context context;
    private MySingleton mySingleton;
    private Network network;
    private DisplaySnackBar displaySnackBar;
    private Button btn_Password_setup;
    private String mPassword, mCPassword;
    private KProgressHUD kProgressHUD;
    private static final String TAG = ActivityLogin.class.getSimpleName();
    AlertDialog dialog;
    private Intent intent;
    private String groupId, groupTitle;
    String type = "2", Phone = "", userId;
    Base64Encoded base64Encoded;
    String AuthToken;

    private TextView textHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_setup);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        intent = getIntent();
        groupId = intent.getStringExtra(Constant.CHANNEL_GROUP_ID);
        type = intent.getStringExtra("type");
        if (type.equalsIgnoreCase("1")) {
            userId = intent.getStringExtra("userId");
        }
        initView();
        viewListener();

    }

    private void initView() {
        context = PasswordSetupActivity.this;
        displaySnackBar = new DisplaySnackBar(this);
        network = new Network();
        base64Encoded = new Base64Encoded(this);
        mySingleton = new MySingleton(this);// get shared data
        etPassword = findViewById(R.id.etPassword);
        etCPassword = findViewById(R.id.etCPassword);
        btn_Password_setup = findViewById(R.id.btn_Password_setup);
        textHeader = findViewById(R.id.textHeader);
        textHeader.setText("Set Password");

        setFontOnView();
        setHint();
    }

    private void setHint() {
        etPassword.setHint(ShowHintOrText.GetMandatory("Password"));
        etCPassword.setHint(ShowHintOrText.GetMandatory("Confirm Password"));
    }

    private void setFontOnView() {
        etPassword.setTypeface(App.LATO_REGULAR);
        etCPassword.setTypeface(App.LATO_REGULAR);


    }

    private void viewListener() {

        btn_Password_setup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (network.isNetworkConnected(context)) {
                    if (isFormValidatedWithSweetAlert()) {
                        callLoginService();
                    }
                } else {
                    displaySnackBar.DisplaySnackBar(Constant.NETWIRK_ERROR, Constant.TYPE_ERROR);
                }
            }
        });


    }

    private boolean isFormValidatedWithSweetAlert() {
        boolean status = true;

        mPassword = etPassword.getText().toString().trim();
        mCPassword = etCPassword.getText().toString().trim();

        if (mPassword.length() < 6) {
            status = false;
            showWarningSimpleAlertDialog(Constant.TITLE, getString(R.string.PASSWORD_VALIDATION));
        } else if (mCPassword.length() < 6) {
            status = false;
            showWarningSimpleAlertDialog(Constant.TITLE, getString(R.string.PASSWORD_VALIDATION));
        }

        if (status) {
            if (!mPassword.equalsIgnoreCase(mCPassword)) {
                status = false;
                showWarningSimpleAlertDialog(Constant.TITLE, getString(R.string.MATCH_PASSWORD_VALIDATION));
            }
        }
        return status;
    }

    private void showWarningSimpleAlertDialog(String title, String message) {
        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE).setTitleText(title)
                .setContentText(message)
                .setConfirmClickListener(sweetAlertDialog -> sweetAlertDialog.dismissWithAnimation())
                .show();
    }


    public void callLoginService() {

        kProgressHUD = KProgressHUD.create(context)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .setWindowColor(getResources().getColor(R.color.progressbar_color))
                .show();

        PasswordSetUpRequest passwordSetUpRequest = new PasswordSetUpRequest();
        passwordSetUpRequest.setUser_id(mySingleton.getData("id"));
        passwordSetUpRequest.setPassword(mCPassword);
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        final Call<PasswordSetupResponse> call = apiService.getPASSWORDSetup(passwordSetUpRequest);
        call.enqueue(new Callback<PasswordSetupResponse>() {
            @Override
            public void onResponse(Call<PasswordSetupResponse> call, Response<PasswordSetupResponse> response) {
                kProgressHUD.dismiss();
                if (response.isSuccessful()) {
                    if (response.code() >= 200 && response.code() < 700) {
                        if (response.code() == 200) {
                            String message = response.body().getMessage();
                            changePasswordDialog(message);

                            Log.d("RESP", response.body().toString());

                            if (response.body().getStatus() == 1) {
                            }
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
                    } catch (Exception e) {
                        displaySnackBar.DisplaySnackBar(Constant.ERROR_INVALID_JSON, Constant.TYPE_ERROR);

                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<PasswordSetupResponse> call, Throwable t) {
                kProgressHUD.dismiss();
                if (t != null && (t instanceof IOException || t instanceof SocketTimeoutException
                        || t instanceof ConnectException || t instanceof NoRouteToHostException
                        || t instanceof SecurityException)) {
                    displaySnackBar.DisplaySnackBar(Constant.NETWIRK_ERROR, Constant.TYPE_ERROR);

                }
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

    private void callIntent() {

        if (type.equalsIgnoreCase("0")) {
            mySingleton.saveData(Constant.USER_VERIFYED, "1");

            if (mySingleton.getData(Constant.USER_GROUP_ID).equalsIgnoreCase("1")) {
                Intent login = new Intent(PasswordSetupActivity.this, DashboardCPDetails.class);
                startActivity(login);
            } else if (mySingleton.getData(Constant.USER_GROUP_ID).equalsIgnoreCase("2")) {
                Intent login = new Intent(PasswordSetupActivity.this, ActivityDashboard.class);
                login.putExtra("profile_varified", "1");
                login.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                login.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                login.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(login);
            } else if (mySingleton.getData(Constant.USER_GROUP_ID).equalsIgnoreCase("3")) {
                Intent login = new Intent(PasswordSetupActivity.this, DashboardCPDetails.class);
                startActivity(login);
            } else if (mySingleton.getData(Constant.USER_GROUP_ID).equalsIgnoreCase("4")) {
                Intent login = new Intent(PasswordSetupActivity.this, DashboardSuperCPDetails.class);
                startActivity(login);
            } else {
                String shopName = mySingleton.getData("shop_names");
                Log.e(TAG, "callIntent: "+shopName);
                if (shopName.length() > 0) {
                    startActivity(new Intent(PasswordSetupActivity.this, KycActivity.class));

                } else {
                    startActivity(new Intent(PasswordSetupActivity.this, ActivityPartnerSelecion.class));
               }
            }
        }
        finish();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }


    private void changePasswordDialog(String message) {
        new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE).setTitleText(message)
                .setContentText(message)
                .setConfirmClickListener(sweetAlertDialog -> {
                    sweetAlertDialog.dismissWithAnimation();
                    callIntent();
                })
                .show();
    }

}