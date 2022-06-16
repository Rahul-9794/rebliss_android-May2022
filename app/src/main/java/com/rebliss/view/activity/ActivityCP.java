package com.rebliss.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.rebliss.domain.model.fospostest.Data;
import com.rebliss.domain.model.fospostest.GetFosPosTest;
import com.rebliss.presenter.helper.DisplaySnackBar;
import com.rebliss.presenter.helper.Logout;
import com.rebliss.presenter.helper.Network;
import com.rebliss.presenter.retrofit.ApiClient;
import com.rebliss.presenter.retrofit.ApiInterface;
import com.rebliss.view.adapter.FosPosAdapter;

import java.io.IOException;
import java.net.ConnectException;
import java.net.NoRouteToHostException;
import java.net.SocketTimeoutException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityCP extends AppCompatActivity {

    private ImageView ic_back, imgAddFos;
    private ListView listView;

    private static final String TAG = ActivityCP.class.getSimpleName();
    private Context context;
    private MySingleton mySingleton;
    private Network network;
    private KProgressHUD kProgressHUD;
    private DisplaySnackBar displaySnackBar;
    private List<Data> fosPosDataList;
    FosPosAdapter fosPosAdapter;
    TextView header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fos);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        initView();
        viewListener();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (network.isNetworkConnected(context)) {
            callProfileAPI();
        } else {
            displaySnackBar.DisplaySnackBar(Constant.NETWIRK_ERROR, Constant.TYPE_ERROR);
        }
    }

    private void initView() {
        context = ActivityCP.this;
        mySingleton = new MySingleton(this);
        displaySnackBar = new DisplaySnackBar(this);
        network = new Network();
        header = findViewById(R.id.textHeader);
        header.setText("CP ");
        header.setTypeface(App.LATO_REGULAR);
        ic_back = findViewById(R.id.ic_back);
        imgAddFos = findViewById(R.id.imgAddFos);

        if (mySingleton.getData(Constant.USER_GROUP_ID).equalsIgnoreCase("4")) {
            imgAddFos.setVisibility(View.GONE);
        }
        imgAddFos.setVisibility(View.GONE);
        listView = findViewById(R.id.listView);
    }

    public void reLoadData() {
        if (network.isNetworkConnected(context)) {
            callProfileAPI();
        } else {
            displaySnackBar.DisplaySnackBar(Constant.NETWIRK_ERROR, Constant.TYPE_ERROR);
        }
    }

    private void viewListener() {
        ic_back.setOnClickListener(v -> finish());

        imgAddFos.setOnClickListener(v -> callRegisterFos());
    }

    private void callRegisterFos() {
        Intent registerFos = new Intent(context, RegisterFos.class);
        startActivity(registerFos);
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


    private void showDataOnView() {
        fosPosAdapter = new FosPosAdapter(context, fosPosDataList, "CP");
        listView.setAdapter(fosPosAdapter);
        listView.setDivider(null);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            if (fosPosDataList.get(position) != null && fosPosDataList.get(position).getId() != null) {
                Intent callDetails = new Intent(context, CPDetails.class);
                callDetails.putExtra("userId", fosPosDataList.get(position).getId());
                startActivity(callDetails);
            }
        });
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
        final Call<GetFosPosTest> call = apiService.getFosPosTest(mySingleton.getData(Constant.TOKEN_BASE_64), "1");
        call.enqueue(new Callback<GetFosPosTest>() {
            @Override
            public void onResponse(Call<GetFosPosTest> call, Response<GetFosPosTest> response) {
                kProgressHUD.dismiss();
                if (response.isSuccessful()) {
                    if (response.code() >= 200 && response.code() < 700) {
                        if (response.code() == 200) {
                            if (response.body().getStatus() == 1) {
                                if (response.body().getData() != null) {
                                    Gson gson = new Gson();
                                    String json = gson.toJson(response.body(), GetFosPosTest.class);
                                    Log.i(TAG, "json " + json);
                                    if (response.body().getData().size() > 0) {
                                        fosPosDataList = response.body().getData();

                                        showDataOnView();
                                    }
                                } else {
                                    displaySnackBar.DisplaySnackBar("Data not found", Constant.TYPE_ERROR);

                                }
                            } else if (response.body().getStatus() == 0) {
                                displaySnackBar.DisplaySnackBar(response.body().getMessage(), Constant.TYPE_ERROR);
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
            public void onFailure(Call<GetFosPosTest> call, Throwable t) {
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

