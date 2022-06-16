package com.rebliss.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
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
import com.rebliss.domain.model.PartnerSelectionModel;
import com.rebliss.domain.model.group.Data;
import com.rebliss.domain.model.group.GroupResponce;
import com.rebliss.domain.model.payment.SuccessResponse;
import com.rebliss.presenter.helper.DisplaySnackBar;
import com.rebliss.presenter.helper.Logout;
import com.rebliss.presenter.helper.Network;
import com.rebliss.presenter.retrofit.ApiClient;
import com.rebliss.presenter.retrofit.ApiInterface;
import com.rebliss.view.adapter.GroupAdapter;
import java.io.IOException;
import java.net.ConnectException;
import java.net.NoRouteToHostException;
import java.net.SocketTimeoutException;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityPartnerSelecion extends AppCompatActivity {


    private static final String TAG = ActivityPartnerSelecion.class.getSimpleName();
    private Context context;
    private TextView textHeader, textReblissInfo, welcomeText;
    private ImageView icBack;
    private ListView listView;
    private MySingleton mySingleton;
    private Network network;
    private KProgressHUD kProgressHUD;
    private DisplaySnackBar displaySnackBar;
    private Data groupData;
    private GroupAdapter groupAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partner_selecion);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setVisibility(View.VISIBLE);
        initView();
        viewListener();

        if (network.isNetworkConnected(context)) {
            callGetGroupAPI();
        } else {
            displaySnackBar.DisplaySnackBar(Constant.NETWIRK_ERROR, Constant.TYPE_ERROR);
        }
    }
    private void initView() {
        context = ActivityPartnerSelecion.this;
        mySingleton = new MySingleton(this);
        displaySnackBar = new DisplaySnackBar(this);
        network = new Network();

      //  textHeader = findViewById(R.id.textHeader);
        textReblissInfo = findViewById(R.id.textReblissInfo);
        welcomeText = findViewById(R.id.welcomeText);
        icBack = findViewById(R.id.icBack);
        listView = findViewById(R.id.listView);

        setFontOnView();
    }


    private void setFontOnView() {
        /*
        textHeader.setTypeface(App.LATO_REGULAR);
        textReblissInfo.setTypeface(App.LATO_REGULAR);
        welcomeText.setTypeface(App.LATO_REGULAR);


         */
    }

    private void viewListener() {
        icBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String channel_id = String.valueOf(groupData.getAll_groups().get(position).getId());
                String channel_name = String.valueOf(groupData.getAll_groups().get(position).getName());
                if (network.isNetworkConnected(ActivityPartnerSelecion.this)) {
                    updateGroupId(channel_id, channel_name);
                    Log.e(TAG, "onItemClick: "+channel_id+"\n"+channel_name );
                }
                else
                    {
                        displaySnackBar.DisplaySnackBar(Constant.NETWIRK_ERROR, Constant.TYPE_ERROR);
                    }
            }
        });
    }

    private void updateGroupId(String channel_id, String channel_name) {

        kProgressHUD = KProgressHUD.create(context)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .setWindowColor(getResources().getColor(R.color.progressbar_color))
                .show();

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        final Call<SuccessResponse> call = apiService.updateGroupId(new PartnerSelectionModel(mySingleton.getData(Constant.USER_ID), channel_id));
        call.enqueue(new Callback<SuccessResponse>() {
            @Override
            public void onResponse(Call<SuccessResponse> call, Response<SuccessResponse> response) {
                kProgressHUD.dismiss();
                if (response.isSuccessful()) {
                    if (response.code() >= 200 && response.code() < 700) {
                        if (response.code() == 200) {
                            if (response.body().getStatus() == 1) {
                                if (channel_id.contains("2")) {
                                    Intent intent = new Intent(context, KycActivity.class);
                                    intent.putExtra(Constant.CHANNEL_GROUP_ID, channel_id);
                                    intent.putExtra(Constant.CHANNEL_GROUP_NAME, channel_name);
                                    intent.putExtra(Constant.FRAGMENT_TYPE, Constant.FRAGMENT_KYC);
                                    startActivity(intent);
                                } else if (channel_id.contains("1")) {
                                    Intent intent = new Intent(context, ActivityCp_PartnerType.class);
                                    intent.putExtra(Constant.CHANNEL_GROUP_ID, channel_id);
                                    intent.putExtra(Constant.CHANNEL_GROUP_NAME, channel_name);
                                    startActivity(intent);
                                }
                            }
                            callDisplayErrorCode(response.code(), "");
                        }
                    } else {
                        displaySnackBar.DisplaySnackBar(Constant.UNEXPECTED, Constant.TYPE_ERROR);
                    }
                }
            }

            @Override
            public void onFailure(Call<SuccessResponse> call, Throwable t) {
                kProgressHUD.dismiss();
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


    private void showDataOnView() {
        if (groupData.getAll_groups() != null && groupData.getAll_groups().size() > 0) {
            groupAdapter = new GroupAdapter(context, groupData.getAll_groups());
            listView.setAdapter(groupAdapter);
            listView.setDivider(null);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void callGetGroupAPI() {

        kProgressHUD = KProgressHUD.create(context)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .setWindowColor(getResources().getColor(R.color.progressbar_color))
                .show();
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        final Call<GroupResponce> call = apiService.getGrouplist();
        call.enqueue(new Callback<GroupResponce>() {
            @Override
            public void onResponse(Call<GroupResponce> call, Response<GroupResponce> response) {
                kProgressHUD.dismiss();
                if (response.isSuccessful()) {
                    if (response.code() >= 200 && response.code() < 700) {
                        if (response.code() == 200) {
                            if (response.body().getStatus() == 1) {
                                if (response.body().getData() != null) {
                                    Gson gson = new Gson();
                                    String json = gson.toJson(response.body(), GroupResponce.class);
                                    Log.i(TAG, "json " + json);
                                    groupData = response.body().getData();
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
            public void onFailure(Call<GroupResponce> call, Throwable t) {
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
