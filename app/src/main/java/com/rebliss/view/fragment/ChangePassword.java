package com.rebliss.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.rebliss.utils.App;
import com.rebliss.R;
import com.rebliss.data.perf.MySingleton;
import com.rebliss.domain.constant.Constant;
import com.rebliss.domain.model.ErrorBody;
import com.rebliss.domain.model.changepassword.ChangePasswordRequest;
import com.rebliss.domain.model.changepassword.ChangePasswordResponce;
import com.rebliss.presenter.helper.Base64Encoded;
import com.rebliss.presenter.helper.DisplaySnackBar;
import com.rebliss.presenter.helper.Logout;
import com.rebliss.presenter.helper.Network;
import com.rebliss.presenter.helper.TextUtil;
import com.rebliss.presenter.retrofit.ApiClient;
import com.rebliss.presenter.retrofit.ApiInterface;
import com.rebliss.view.activity.ActivityLogin;

import java.io.IOException;
import java.net.ConnectException;
import java.net.NoRouteToHostException;
import java.net.SocketTimeoutException;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePassword extends Fragment {
    private static final String TAG = ActivityLogin.class.getSimpleName();
    private Context context;
    private View root;

    // EditText References
    private EditText etOldPassword, etNewPassword, etcNewPassword;
    private Button btnLogin;
    // Others
    private MySingleton mySingleton;
    private Network network;
    private KProgressHUD kProgressHUD;
    private DisplaySnackBar displaySnackBar;
    private String mPhoneNumber, mPassword, mNewPassword;
    private Base64Encoded base64Encoded;
    private AlertDialog dialog;
    private EditText forgotemail;
    private TextView send, cancel, title;
    private Intent intent;
    private String isUnAuthorise = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_change_password, container, false);

        // Initialize View

        initView();


        // Initialize view click listener
        viewListener();
        return root;
    }


    /******************************************* Start Methods *************************************
     */
    private void initView() {
        context = getContext();
        mySingleton = new MySingleton(context);
        displaySnackBar = new DisplaySnackBar(context);
        network = new Network();


        // TextView References


        // Image  References

        // Button References
        btnLogin = root.findViewById(R.id.btnLogin);


        // EditText References
        etOldPassword = root.findViewById(R.id.etOldPassword);
        etNewPassword = root.findViewById(R.id.etNewPassword);
        etcNewPassword = root.findViewById(R.id.etcNewPassword);

        // Layout References


        // ImageView References


        // Others

        if (!TextUtil.isStringNullOrBlank(isUnAuthorise)) {
            if (isUnAuthorise.equalsIgnoreCase("1")) {
                showSimpleAlertDialog(Constant.UNAUTHORISE_TOKEN_MESSAGE);
            }
        }
        setFontOnView();

    }

    /**
     * Setting Font on required Views
     */
    private void setFontOnView() {
        etOldPassword.setTypeface(App.LATO_REGULAR);
        etNewPassword.setTypeface(App.LATO_REGULAR);
        btnLogin.setTypeface(App.LATO_REGULAR);

    }


    private void viewListener() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
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

    private void showSimpleAlertDialog(String message) {
        new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE).setTitleText(Constant.TITLE)
                .setContentText(message)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                        if (dialog != null)
                            dialog.dismiss();
                        Logout.Login(context);
                    }
                })
                .show();
    }

    private void showWarningSimpleAlertDialog(String title, String message) {
        new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE).setTitleText(title)
                .setContentText(message)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();

                    }
                })
                .show();
    }

    /**
     * Print error code in case of any API call
     *
     * @param statusCode 200 / 400 / 401 / 402 etc
     */
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
    /******************************************* End Methods *************************************
     */
    /******************************************** Start Interface methods **************************
     */
    /******************************************** End Interface methods **************************
     */


    /********************************************* Start Validations *********************************
     */
    private boolean isFormValidatedWithSweetAlert() {
        boolean status = true;
        mPhoneNumber = etOldPassword.getText().toString().trim();
        mPassword = etNewPassword.getText().toString().trim();
        mNewPassword = etcNewPassword.getText().toString().trim();

        if (mPhoneNumber.length() < 6) {
            status = false;
            showWarningSimpleAlertDialog(Constant.TITLE, getString(R.string.PASSWORD_VALIDATION));
        } else if (mPassword.length() < 6) {
            status = false;
            showWarningSimpleAlertDialog(Constant.TITLE, getString(R.string.PASSWORD_VALIDATION));
        }
        if (status) {
            if (!mPassword.equalsIgnoreCase(mNewPassword)) {
                status = false;
                showWarningSimpleAlertDialog(Constant.TITLE, getString(R.string.MATCH_PASSWORD_VALIDATION));
            }
        }
        return status;
    }
    /********************************************* End Validations *********************************
     */
    /******************************************* Start Services ************************************
     */
    public void callLoginService() {

        kProgressHUD = KProgressHUD.create(context)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .setWindowColor(getResources().getColor(R.color.progressbar_color))
                .show();

        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest();
        changePasswordRequest.setOld_password(mPhoneNumber);
        changePasswordRequest.setPassword(mPassword);
        changePasswordRequest.setConfirm_password(mNewPassword);

        Gson gson = new Gson();
        String json = gson.toJson(changePasswordRequest, ChangePasswordRequest.class);
        Log.i(TAG, "json " + json);
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        final Call<ChangePasswordResponce> call = apiService.postUserChangePassword(mySingleton.getData(Constant.TOKEN_BASE_64), changePasswordRequest);
        call.enqueue(new Callback<ChangePasswordResponce>() {
            @Override
            public void onResponse(Call<ChangePasswordResponce> call, Response<ChangePasswordResponce> response) {
                kProgressHUD.dismiss();
                if (response.isSuccessful()) {
                    if (response.code() >= 200 && response.code() < 700) {
                        if (response.code() == 200) {
                            if (response.body().getStatus() == 1) {
                                Gson gson = new Gson();
                                String json = gson.toJson(response.body(), ChangePasswordResponce.class);
                                Log.i(TAG, "json " + json);
                                showSimpleAlertDialog(response.body().getMessage());
                            } else if (response.body().getStatus() == 0) {
                                showWarningSimpleAlertDialog(Constant.TITLE, response.body().getMessage());
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
//                        if (errorBody.getMessage().contains("invalid")) {
//                            Logout.Login(context);
//                        }
                    } catch (Exception e) {
                        displaySnackBar.DisplaySnackBar(Constant.ERROR_INVALID_JSON, Constant.TYPE_ERROR);

                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<ChangePasswordResponce> call, Throwable t) {
                kProgressHUD.dismiss();
                if (t != null && (t instanceof IOException || t instanceof SocketTimeoutException
                        || t instanceof ConnectException || t instanceof NoRouteToHostException
                        || t instanceof SecurityException)) {
                    displaySnackBar.DisplaySnackBar(Constant.NETWIRK_ERROR, Constant.TYPE_ERROR);

                }
            }
        });
    }
    /******************************************* End Services ************************************
     */

}
