package com.rebliss.view.activity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.rebliss.utils.App;
import com.rebliss.R;
import com.rebliss.data.perf.MySingleton;
import com.rebliss.domain.constant.Constant;
import com.rebliss.domain.model.ErrorBody;
import com.rebliss.domain.model.approvedecline.ApproveRequest;
import com.rebliss.domain.model.approvedecline.ApproveResponce;
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

public class CallFosPosDetails extends AppCompatActivity {

    private WebView webView;
    private Intent intent;
    private String userId,profileVerified;
    ImageView icBack;
    TextView header;
    String HOME_URL = "http://headsocial.com/rebliss/usermgmt/user/web-view?userId=";
    RelativeLayout buttonBar;
    TextView text_accept,text_decline;
    private Context context;
    private KProgressHUD kProgressHUD;
    private MySingleton mySingleton;
    private DisplaySnackBar displaySnackBar;
    String type;
    private AlertDialog dialog;
    private EditText forgotemail;
    private TextView send, cancel, title;
    private Network network;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_fos_pos_details);
        displaySnackBar = new DisplaySnackBar(this);
        intent = getIntent();
        userId = intent.getStringExtra("userId");
        this.context = CallFosPosDetails.this;
        network = new Network();
        mySingleton = new MySingleton(context);
        profileVerified = intent.getStringExtra("profileVerified");
        initView();
        viewListener();
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @SuppressWarnings("deprecation")
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            }
            @TargetApi(android.os.Build.VERSION_CODES.M)
            @Override
            public void onReceivedError(WebView view, WebResourceRequest req, WebResourceError rerr) {
                onReceivedError(view, rerr.getErrorCode(), rerr.getDescription().toString(), req.getUrl().toString());
            }
        });
        Log.i("WebURL", HOME_URL+userId);
        webView .loadUrl(HOME_URL+userId);


    }

    private void initView(){
        webView = findViewById(R.id.webView);
        icBack = findViewById(R.id.icBack);
        header = findViewById(R.id.header);
        header.setTypeface(App.LATO_REGULAR);
        buttonBar = findViewById(R.id.buttonBar);
        text_accept = findViewById(R.id.text_accept);
        text_decline = findViewById(R.id.text_decline);
    }
    private void viewListener(){


        if(mySingleton.getData(Constant.USER_GROUP_ID).equalsIgnoreCase(Constant.CP_GROUP_ID) && profileVerified.equalsIgnoreCase("2")){
            buttonBar.setVisibility(View.VISIBLE);
        }else{
            buttonBar.setVisibility(View.GONE);
        }
        text_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (network.isNetworkConnected(CallFosPosDetails.this)) {
                    if (!TextUtil.isStringNullOrBlank(userId)) {
                        callAcceptAccount(userId);
                    }
                    else
                        {
                            displaySnackBar.DisplaySnackBar(Constant.NETWIRK_ERROR, Constant.TYPE_ERROR);
                        }
                }
            }
        });

        text_decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (network.isNetworkConnected(CallFosPosDetails.this)) {
                    if (!TextUtil.isStringNullOrBlank(userId)) {
                        disPlayDialog(userId);
                    }
                }
                else
                    {
                        displaySnackBar.DisplaySnackBar(Constant.NETWIRK_ERROR, Constant.TYPE_ERROR);
                    }
            }
        });
icBack.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        finish();
    }
});


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
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
        new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE).setTitleText(Constant.TITLE)
                .setContentText(message)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                        finish();
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
    private void disPlayDialog(final String userID) {
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle("Remarks for Decline");
        alert.setView(R.layout.reasonforreject);
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
        forgotemail.setHint(ShowHintOrText.GetMandatory("Please enter Remark for decline"));
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtil.isStringNullOrBlank(forgotemail.getText().toString())) {
                    if (network.isNetworkConnected(context)) {
                        dialog.dismiss();
                        callDeclineAccount(userID, forgotemail.getText().toString());

                    } else {
                        Toast.makeText(context, "There are some connection issue", Toast.LENGTH_SHORT).show();

                    }
                }else{
                    Toast.makeText(context, "Please Enter Reject Remark", Toast.LENGTH_SHORT).show();
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
    public void callAcceptAccount(String userId) {

        kProgressHUD = KProgressHUD.create(context)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .setWindowColor(context.getResources().getColor(R.color.progressbar_color))
                .show();


        ApproveRequest approveRequest = new ApproveRequest();
        approveRequest.setUser_id(userId);
        Gson gson = new Gson();
        String json = gson.toJson(approveRequest, ApproveRequest.class);

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        final Call<ApproveResponce> call = apiService.postAccountApprove(mySingleton.getData(Constant.TOKEN_BASE_64), userId);
        call.enqueue(new Callback<ApproveResponce>() {
            @Override
            public void onResponse(Call<ApproveResponce> call, Response<ApproveResponce> response) {
                kProgressHUD.dismiss();
                if (response.isSuccessful()) {
                    if (response.code() >= 200 && response.code() < 700) {
                        if (response.code() == 200) {
                            if (response.body().getStatus() == 1) {
                                if (response.body().getData() != null) {
                                    Gson gson = new Gson();
                                    String json = gson.toJson(response.body(), ApproveResponce.class);
                                    Log.i("json", "json " + json);
                                    showSimpleAlertDialog(response.body().getData().getMessage());
                                } else {
                                    showWarningSimpleAlertDialog(response.body().getData().getMessage(), "Data not found");

                                }
                            } else if (response.body().getStatus() == 0) {
                                showWarningSimpleAlertDialog(response.body().getData().getMessage(), response.body().getData().getMessage());
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
            public void onFailure(Call<ApproveResponce> call, Throwable t) {
                kProgressHUD.dismiss();
                if (t != null && (t instanceof IOException || t instanceof SocketTimeoutException
                        || t instanceof ConnectException || t instanceof NoRouteToHostException
                        || t instanceof SecurityException)) {
                    displaySnackBar.DisplaySnackBar(Constant.NETWIRK_ERROR, Constant.TYPE_ERROR);

                }
            }
        });
    }
    public void callDeclineAccount(String userId, String Message) {

        kProgressHUD = KProgressHUD.create(context)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .setWindowColor(context.getResources().getColor(R.color.progressbar_color))
                .show();


        ApproveRequest approveRequest = new ApproveRequest();
        approveRequest.setUser_id(userId);
        approveRequest.setDecline_message(Message);
        Gson gson = new Gson();
        String json = gson.toJson(approveRequest, ApproveRequest.class);

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        final Call<ApproveResponce> call = apiService.postAccountDecline(mySingleton.getData(Constant.TOKEN_BASE_64), approveRequest);
        call.enqueue(new Callback<ApproveResponce>() {
            @Override
            public void onResponse(Call<ApproveResponce> call, Response<ApproveResponce> response) {
                kProgressHUD.dismiss();
                if (response.isSuccessful()) {
                    if (response.code() >= 200 && response.code() < 700) {
                        if (response.code() == 200) {
                            if (response.body().getStatus() == 1) {
                                if (response.body().getData() != null) {
                                    Gson gson = new Gson();
                                    String json = gson.toJson(response.body(), ApproveResponce.class);
                                    Log.i("json", "json " + json);
                                    showSimpleAlertDialog(response.body().getData().getMessage());
                                } else {
                                    showWarningSimpleAlertDialog(response.body().getData().getMessage(), "Data not found");

                                }
                            } else if (response.body().getStatus() == 0) {
                                showWarningSimpleAlertDialog(response.body().getData().getMessage(), response.body().getData().getMessage());
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
            public void onFailure(Call<ApproveResponce> call, Throwable t) {
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
