package com.rebliss.view.activity.notification;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.rebliss.R;
import com.rebliss.data.perf.MySingleton;
import com.rebliss.domain.constant.Constant;
import com.rebliss.domain.model.ErrorBody;
import com.rebliss.domain.model.notificationlist.Desc;
import com.rebliss.domain.model.notificationlist.NotificationListResponse;
import com.rebliss.presenter.helper.DisplaySnackBar;
import com.rebliss.presenter.retrofit.ApiClient;
import com.rebliss.presenter.retrofit.ApiInterface;
import com.rebliss.view.adapter.NotificationListShowAdapter;

import java.io.IOException;
import java.net.ConnectException;
import java.net.NoRouteToHostException;
import java.net.SocketTimeoutException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationListActivity extends AppCompatActivity {

    MySingleton mySingleton;
    DisplaySnackBar displaySnackBar;
    RecyclerView rvNotification;
    TextView tvTotalCount, tvUnreadCount, tvNodataFound;
    private ImageView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_list);

        rvNotification = findViewById(R.id.rvNotification);
        tvTotalCount = findViewById(R.id.tvTotalCount);
        tvUnreadCount = findViewById(R.id.tvUnreadCount);
        tvNodataFound = findViewById(R.id.tvNodata);
        btnBack = findViewById(R.id.btnBack);

        mySingleton = new MySingleton(this);
        displaySnackBar = new DisplaySnackBar(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvNotification.setLayoutManager(linearLayoutManager);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        getMyActivity();
    }

    public void getMyActivity() {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<NotificationListResponse>
                call = apiService.getNotificationList(mySingleton.getData(Constant.USER_ID));


        call.enqueue(new Callback<NotificationListResponse>() {
            @Override
            public void onResponse(Call<NotificationListResponse> call, Response<NotificationListResponse> response) {

                if (response.isSuccessful()) {
                    if (response.code() >= 200 && response.code() < 700) {
                        if (response.code() == 200) {
                            if (response.body().getStatus() == 1) {
                                if (response.body().getDesc() != null) {
                                    rvNotification.setVisibility(View.VISIBLE);
                                    tvNodataFound.setVisibility(View.GONE);

                                    tvTotalCount.setText(response.body().getDesc().size() + "");
                                    tvUnreadCount.setText(getUnreadCount(response.body().getDesc())+"");
                                    NotificationListShowAdapter adapter = new NotificationListShowAdapter(NotificationListActivity.this, response.body().getDesc());
                                    rvNotification.setAdapter(adapter);

                                } else {
                                    displaySnackBar.DisplaySnackBar("Data not found", Constant.TYPE_ERROR);


                                }
                            } else if (response.body().getStatus() == 0) {
                                //showWarningSimpleAlertDialog(response.body().getData().getMessage(), response.body().getData().getValidation_error().getPassword().get(0));
                                rvNotification.setVisibility(View.GONE);
                                tvNodataFound.setVisibility(View.VISIBLE);
                                tvTotalCount.setText("0");
                            }
//                            callDisplayErrorCode(response.code(), "");
                        }

                    } else {
                        displaySnackBar.DisplaySnackBar(Constant.UNEXPECTED, Constant.TYPE_ERROR);

                    }
                } else {

                    try {
                        ErrorBody errorBody = new ErrorBody();
                        Gson gson = new Gson();
                        errorBody = gson.fromJson(response.errorBody().string(), ErrorBody.class);
//                        callDisplayErrorCode(Integer.parseInt(errorBody.getStatus()), errorBody.getMessage());
                        displaySnackBar.DisplaySnackBar(errorBody.getMessage(), Constant.TYPE_ERROR);

                        // Toast.makeText(context, errorBody.getMessage(), Toast.LENGTH_SHORT).show();
                        if (errorBody.getMessage().contains("invalid")) {
//                            Logout.Login(context);
                        }
                    } catch (Exception e) {
                        displaySnackBar.DisplaySnackBar(Constant.ERROR_INVALID_JSON, Constant.TYPE_ERROR);

                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<NotificationListResponse> call, Throwable t) {

                if (t != null && (t instanceof IOException || t instanceof SocketTimeoutException
                        || t instanceof ConnectException || t instanceof NoRouteToHostException
                        || t instanceof SecurityException)) {
                    displaySnackBar.DisplaySnackBar(Constant.NETWIRK_ERROR, Constant.TYPE_ERROR);

                }
            }
        });
    }

    private int getUnreadCount(List<Desc> notificationList) {
        int count = 0;
        for (Desc desc : notificationList) {
            if (desc.getReadStatus() == 0)
                count++;
        }
        return count;
    }
}
