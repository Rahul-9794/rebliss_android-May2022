package com.rebliss.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.rebliss.utils.App;
import com.rebliss.R;
import com.rebliss.data.perf.MySingleton;
import com.rebliss.domain.constant.Constant;
import com.rebliss.domain.model.ErrorBody;
import com.rebliss.domain.model.signup.Data;
import com.rebliss.domain.model.signup.SignupRequest;
import com.rebliss.domain.model.signup.SignupResponce;
import com.rebliss.presenter.helper.Base64Encoded;
import com.rebliss.presenter.helper.DisplaySnackBar;
import com.rebliss.presenter.helper.Logout;
import com.rebliss.presenter.helper.Network;
import com.rebliss.presenter.helper.ShowHintOrText;
import com.rebliss.presenter.helper.TextUtil;
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

public class ActivitySignUpPartner extends AppCompatActivity {

    private static final String TAG = ActivitySignUpPartner.class.getSimpleName();
    private Context context;
    private TextView textHeader, textLogin, textReblissInfo;
    private ImageView icBack;
    private Button btnSignup;
    private EditText etFirstName, etLastName, etNumber, etPassword, etCPassword, etrefferal,etStore;
    private Data data;
    private MySingleton mySingleton;
    private Network network;
    private KProgressHUD kProgressHUD;
    private DisplaySnackBar displaySnackBar;
    private Intent intent;
    private String groupId, groupTitle;
    private String mFirstName, mLastName, mNumber, mPassword, mCPassword, mReffCode = "";
    private Base64Encoded base64Encoded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_partner);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        intent = getIntent();
        groupId = intent.getStringExtra(Constant.CHANNEL_GROUP_ID);
        groupTitle = intent.getStringExtra(Constant.CHANNEL_GROUP_NAME);
        Log.i(TAG, "groupId " + groupId);
        Log.i(TAG, "groupTitle " + groupTitle);
        initView();
        viewListener();

        Toast.makeText(context, "ActivitySignUpPartner", Toast.LENGTH_SHORT).show();

    }
    private void initView() {
        context = ActivitySignUpPartner.this;
        mySingleton = new MySingleton(this);
        displaySnackBar = new DisplaySnackBar(this);
        network = new Network();
        base64Encoded = new Base64Encoded(this);
        textHeader = findViewById(R.id.textHeader);
        textLogin = findViewById(R.id.textLogin);
        textReblissInfo = findViewById(R.id.textReblissInfo);
        etStore = findViewById(R.id.etStore);

        if (!TextUtil.isStringNullOrBlank(groupTitle)) {
            String title = "";
            if (groupId.equalsIgnoreCase("1")) {
                title = "Sign up CP form";
            } else if (groupId.equalsIgnoreCase("2")) {
                title = "Sign up Fos form";
            } else if (groupId.equalsIgnoreCase("3")) {
                title = "Sign up Retailer form";
            } else if (groupId.equalsIgnoreCase("4")) {
                title = "Sign up Super CP form";
            }
            textHeader.setText(title);
        }
        icBack = findViewById(R.id.icBack);
        icBack = findViewById(R.id.icBack);
        btnSignup = findViewById(R.id.btnSignup);
        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etNumber = findViewById(R.id.etNumber);
        etPassword = findViewById(R.id.etPassword);
        etCPassword = findViewById(R.id.etCPassword);
        etrefferal = findViewById(R.id.etrefferal);
        setFontOnView();
        setHint();
    }
    private void setHint() {

        etFirstName.setHint(ShowHintOrText.GetMandatory("First name"));
        etLastName.setHint(ShowHintOrText.GetMandatory("Last name"));
        etNumber.setHint(ShowHintOrText.GetMandatory("Mobile No."));
        etPassword.setHint(ShowHintOrText.GetMandatory("Password"));
        etCPassword.setHint(ShowHintOrText.GetMandatory("Confirm Password"));
        etrefferal.setHint("Referral code");
        etStore.setHint("Enter Your store name");


    }
    private void setFontOnView() {
        textHeader.setTypeface(App.LATO_REGULAR);
        textLogin.setTypeface(App.LATO_REGULAR);
        textReblissInfo.setTypeface(App.LATO_REGULAR);
        btnSignup.setTypeface(App.LATO_REGULAR);
        etFirstName.setTypeface(App.LATO_REGULAR);
        etLastName.setTypeface(App.LATO_REGULAR);
        etNumber.setTypeface(App.LATO_REGULAR);
        etPassword.setTypeface(App.LATO_REGULAR);
        etCPassword.setTypeface(App.LATO_REGULAR);
        etrefferal.setTypeface(App.LATO_REGULAR);

    }

    private void viewListener() {
        icBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (network.isNetworkConnected(context)) {
                    if (isFormValidatedWithSweetAlert()) {
                        callSignupService();
                    }
                } else {
                    displaySnackBar.DisplaySnackBar(Constant.NETWIRK_ERROR, Constant.TYPE_ERROR);
                }
            }
        });

        textLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login = new Intent(context, ActivityLogin.class);
                login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(login);
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
                        cleardata();
                        saveData(data);
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


    private void cleardata() {

        etFirstName.setText("");
        etLastName.setText("");
        etNumber.setText("");
        etPassword.setText("");
        etCPassword.setText("");
        etrefferal.setText("");
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

    private void saveData(Data signupResponce) {
        mySingleton.saveData(Constant.TOKEN, signupResponce.getToken());
        mySingleton.saveData(Constant.USER_PHONE, mNumber);
        mySingleton.saveData(Constant.USER_FIRST_NAME, mFirstName);
        mySingleton.saveData(Constant.USER_LAST_NAME, mLastName);
        mySingleton.saveData(Constant.USER_GROUP_ID, signupResponce.getUser().getGroup_id());
        mySingleton.saveData(Constant.USER_GROUP_DETAIL_ID, signupResponce.getUser().getGroup_detail_id()+"");
        mySingleton.saveData(Constant.USER_ID, signupResponce.getUser().getUser_id()+"");

        base64Encoded.EncodeToken();
        Intent callOtp = new Intent(context, OtpVerification.class);
        callOtp.putExtra("type", "0");
        callOtp.putExtra(Constant.CHANNEL_GROUP_ID, groupId);
        startActivity(callOtp);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private boolean isFormValidatedWithSweetAlert() {
        boolean status = true;
        mFirstName = etFirstName.getText().toString().trim();
        mLastName = etLastName.getText().toString().trim();
        mNumber = etNumber.getText().toString().trim();
        mPassword = etPassword.getText().toString().trim();
        mCPassword = etCPassword.getText().toString().trim();
        mReffCode = etrefferal.getText().toString().trim();

        Log.i("myRefferal", mReffCode);
        if (mFirstName.length() <= 0) {
            status = false;
            showWarningSimpleAlertDialog(Constant.TITLE, "Please Enter your first name");
        } else if (mLastName.length() <= 0) {
            status = false;
            showWarningSimpleAlertDialog(Constant.TITLE, "Please Enter your last name");
        } else if (mNumber.length() < 10) {
            status = false;
            showWarningSimpleAlertDialog(Constant.TITLE, getString(R.string.PHONE_VALIDATION));
        } else if (mPassword.length() < 6) {
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

    public void callSignupService() {

        kProgressHUD = KProgressHUD.create(context)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .setWindowColor(getResources().getColor(R.color.progressbar_color))
                .show();

        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setFirst_name(mFirstName);
        signupRequest.setLast_name(mLastName);
        signupRequest.setPhone_number(mNumber);
        signupRequest.setPassword(mPassword);
        signupRequest.setConfirm_password(mCPassword);
        signupRequest.setDevice_type("android");
        signupRequest.setDevice_id(mySingleton.getData(Constant.DEVICE_FCM_TOKEN));
        signupRequest.setGroup_id(groupId);
        signupRequest.setCode(mReffCode);


        Gson gson = new Gson();
        String json = gson.toJson(signupRequest, SignupRequest.class);
        Log.i(TAG, "json " + json);

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        final Call<SignupResponce> call = apiService.postUserSignup(signupRequest);
        call.enqueue(new Callback<SignupResponce>() {
            @Override
            public void onResponse(Call<SignupResponce> call, Response<SignupResponce> response) {
                kProgressHUD.dismiss();
                if (response.isSuccessful()) {
                    if (response.code() >= 200 && response.code() < 700) {
                        if (response.code() == 200) {
                            if (response.body().getStatus() == 1) {
                                if (response.body().getData() != null) {
                                    Gson gson = new Gson();
                                    String json = gson.toJson(response.body(), SignupResponce.class);
                                    Log.i(TAG, "json " + json);
                                    data = response.body().getData();
                                    showSimpleAlertDialog(response.body().getData().getMessage());

                                } else {
                                    displaySnackBar.DisplaySnackBar("Data not found", Constant.TYPE_ERROR);
                                }
                            } else if (response.body().getStatus() == 0) {
                                if (response.body().getData().getError_message() != null)
                                    showWarningSimpleAlertDialog(Constant.TITLE, response.body().getData().getError_message().get(0));
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
            public void onFailure(Call<SignupResponce> call, Throwable t) {
                kProgressHUD.dismiss();
                if (t != null && (t instanceof IOException || t instanceof SocketTimeoutException
                        || t instanceof ConnectException || t instanceof NoRouteToHostException
                        || t instanceof SecurityException)) {
                    displaySnackBar.DisplaySnackBar(Constant.NETWIRK_ERROR, Constant.TYPE_ERROR);

                }
            }
        });
    }
}
