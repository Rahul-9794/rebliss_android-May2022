package com.rebliss.view.activity.TermsAndConditions;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.rebliss.R;
import com.rebliss.data.perf.MySingleton;
import com.rebliss.domain.constant.Constant;
import com.rebliss.domain.model.TermsModel;
import com.rebliss.domain.model.payment.SuccessResponse;
import com.rebliss.domain.model.termsandconditions.TermsAndConditionsResponse;
import com.rebliss.presenter.helper.DisplaySnackBar;
import com.rebliss.presenter.helper.Network;
import com.rebliss.presenter.retrofit.ApiClient;
import com.rebliss.presenter.retrofit.ApiInterface;
import com.rebliss.view.activity.ActivityDashboard;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TermsAndConditionsScreenActivity extends AppCompatActivity {
    private KProgressHUD kProgressHUD;
    private TextView tvTermsAndCondition;
    private CheckBox checkBox;
    private Button submit;
    private DisplaySnackBar displaySnackBar;
    private Network network;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_and_condition_screen);
        tvTermsAndCondition = findViewById(R.id.tvTermsAndCondition);
        submit = findViewById(R.id.submit);
        displaySnackBar = new DisplaySnackBar(this);
        network = new Network();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (network.isNetworkConnected(TermsAndConditionsScreenActivity.this)) {
                    callTermsAPi();
                }
                else
                    {
                        displaySnackBar.DisplaySnackBar(Constant.NETWIRK_ERROR, Constant.TYPE_ERROR);
                    }
            }
        });

        checkBox = findViewById(R.id.checkbox_confirm);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    submit.setEnabled(true);
                    submit.setClickable(true);
                    submit.setBackground(getDrawable(R.drawable.rect_login_button));
                }
                else {
                    submit.setEnabled(false);
                    submit.setClickable(false);
                    submit.setBackground(getDrawable(R.drawable.rect_button_disabled));
                }
            }
        });
        if (network.isNetworkConnected(TermsAndConditionsScreenActivity.this)) {
            getTermsAndCondData();
        }
        else
        {
            displaySnackBar.DisplaySnackBar(Constant.NETWIRK_ERROR, Constant.TYPE_ERROR);
        }
    }

    private void callTermsAPi() {
        kProgressHUD = KProgressHUD.create(this).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<SuccessResponse> responseCall = apiService.postTermsCheck(new TermsModel(MySingleton.getInstance(this).getData(Constant.USER_ID), 1));
        responseCall.enqueue(new Callback<SuccessResponse>() {
            @Override
            public void onResponse(Call<SuccessResponse> call, Response<SuccessResponse> response) {
                kProgressHUD.dismiss();
                if (response.isSuccessful()) {
                    if (response.code() >= 200 && response.code() < 700) {
                        startActivity(new Intent(TermsAndConditionsScreenActivity.this, ActivityDashboard.class).putExtra("profile_varified", "0"));
                        finishAffinity();
                    }
                }
            }

            @Override
            public void onFailure(Call<SuccessResponse> call, Throwable t) {
                kProgressHUD.dismiss();
            }
        });
    }

    private void getTermsAndCondData() {
        kProgressHUD = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<TermsAndConditionsResponse> responseCall = apiService.getTermsAndConditionDATA();
        responseCall.enqueue(new Callback<TermsAndConditionsResponse>() {
            @Override
            public void onResponse(Call<TermsAndConditionsResponse> call, Response<TermsAndConditionsResponse> response) {
                kProgressHUD.dismiss();
                if (response.isSuccessful()) {
                    if (response.code() >= 200 && response.code() < 700) {
                        if (response.body() != null) {
                            tvTermsAndCondition.setText(response.body().getData().getAllGroups().getMessage());
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<TermsAndConditionsResponse> call, Throwable t)
            {
                kProgressHUD.dismiss();
            }
        });
    }
}