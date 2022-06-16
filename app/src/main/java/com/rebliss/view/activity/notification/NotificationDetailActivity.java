package com.rebliss.view.activity.notification;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.gson.Gson;
import com.rebliss.R;
import com.rebliss.data.perf.MySingleton;
import com.rebliss.domain.constant.Constant;
import com.rebliss.domain.model.CommonResponse;
import com.rebliss.domain.model.ErrorBody;
import com.rebliss.domain.model.notificationlist.Desc;
import com.rebliss.presenter.retrofit.ApiClient;
import com.rebliss.presenter.retrofit.ApiInterface;

import java.io.IOException;
import java.net.ConnectException;
import java.net.NoRouteToHostException;
import java.net.SocketTimeoutException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationDetailActivity extends AppCompatActivity {


    TextView tvReceivedOn, tvSubject, tvDescription,tvLink;
    ImageView btnBack;
    PhotoView img;
    Desc desc;
    MySingleton mySingleton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_detail);

        mySingleton = new MySingleton(this);

        if (mySingleton.getData(Constant.USER_ID) == null || mySingleton.getData(Constant.USER_ID).isEmpty()) {
            Toast.makeText(this, "You have logged out", Toast.LENGTH_LONG).show();
            finish();
        }


        tvReceivedOn = findViewById(R.id.tvReceivedOn);
        tvSubject = findViewById(R.id.tvSubject);
        tvDescription = findViewById(R.id.tvDescription);
        btnBack = findViewById(R.id.btnBack);
        img = findViewById(R.id.img);
        tvLink = findViewById(R.id.tvLink);
        onNewIntent(getIntent());

        tvLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (desc.getLink()!=null) {
                    String url = desc.getLink();
                    if (!url.startsWith("http://") && !url.startsWith("https://"))
                        url = "http://" + url;

                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(browserIntent);

                }
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                desc = (Desc)getIntent().getSerializableExtra("desc");

                updateNotificationReadStatus(desc.getId());
                if (desc.getCreated() != null)
                    tvReceivedOn.setText(desc.getCreated());
                if (desc.getPushMessage() != null)
                    tvSubject.setText(desc.getPushMessage());

                if (desc.getExtraData() != null)
                    tvDescription.setText(desc.getExtraData());

                if (desc.getImgUrl() != null) {
                    img.setVisibility(View.VISIBLE);
                    Glide.with(this).load(Constant.kBaseURL_Download_Image + desc.getImgUrl()).into(img);
                }
                if (desc.getLink() != null && !desc.getLink().isEmpty())
                    tvLink.setVisibility(View.VISIBLE);
                tvLink.setText(Html.fromHtml("<u>" + desc.getLink() + "</u>"));
            }



    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent notificationIntent = new Intent(this, NotificationListActivity.class);
        startActivity(notificationIntent);

    }

    public void updateNotificationReadStatus(int id) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<CommonResponse>
                call = apiService.updateNotificationReadStatus(id);


        call.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {

                if (response.isSuccessful()) {
                    if (response.code() >= 200 && response.code() < 700) {
                        if (response.code() == 200) {
                            if (response.body().getStatus() == 1) {

                            } else if (response.body().getStatus() == 0) {

                            }

                        }

                    } else {

                    }
                } else {

                    try {
                        ErrorBody errorBody = new ErrorBody();
                        Gson gson = new Gson();
                        errorBody = gson.fromJson(response.errorBody().string(), ErrorBody.class);

                        if (errorBody.getMessage().contains("invalid")) {
                            Log.e("TAG", "onResponse:notification invalid " );
                        }
                    } catch (Exception e) {

                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {

                if (t != null && (t instanceof IOException || t instanceof SocketTimeoutException
                        || t instanceof ConnectException || t instanceof NoRouteToHostException
                        || t instanceof SecurityException)) {

                }
            }
        });
    }

}
