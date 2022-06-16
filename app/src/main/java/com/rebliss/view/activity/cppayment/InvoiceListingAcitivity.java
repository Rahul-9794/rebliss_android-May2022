package com.rebliss.view.activity.cppayment;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.rebliss.R;
import com.rebliss.data.perf.MySingleton;
import com.rebliss.databinding.ActivityInvoiceListingAcitivityBinding;
import com.rebliss.domain.constant.Constant;
import com.rebliss.domain.model.ErrorBody;
import com.rebliss.domain.model.payment.InvoiceListResponse;
import com.rebliss.presenter.helper.DisplaySnackBar;
import com.rebliss.presenter.retrofit.ApiClient;
import com.rebliss.presenter.retrofit.ApiInterface;
import com.rebliss.view.adapter.InvoiceListShowAdapter;

import java.io.IOException;
import java.net.ConnectException;
import java.net.NoRouteToHostException;
import java.net.SocketTimeoutException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InvoiceListingAcitivity extends AppCompatActivity {

    private MySingleton mySingleton;
    private DisplaySnackBar displaySnackBar;

    ActivityInvoiceListingAcitivityBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_invoice_listing_acitivity);

        mySingleton = new MySingleton(this);
        displaySnackBar = new DisplaySnackBar(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        binding.rvInvoices.setLayoutManager(linearLayoutManager);

        getInvoiceListResponse();

        listener();

    }

    private void listener() {
        binding.btnBack.setOnClickListener(view -> onBackPressed());
    }


    public void getInvoiceListResponse() {
        ApiInterface apiService = ApiClient.paymentDetail().create(ApiInterface.class);

        Call<InvoiceListResponse>
                call = apiService.getInvoiceList(mySingleton.getData(Constant.USER_ID));


        call.enqueue(new Callback<InvoiceListResponse>() {
            @Override
            public void onResponse(Call<InvoiceListResponse> call, Response<InvoiceListResponse> response) {

                if (response.isSuccessful()) {
                    if (response.code() >= 200 && response.code() < 700) {
                        if (response.code() == 200) {
                            if (response.body().getStatus() == 1) {
                                if (response.body() != null) {
                                    Log.e("TAG", "onResponse: "+response.body().toString());
                                    InvoiceListShowAdapter listShowAdapter = new InvoiceListShowAdapter(InvoiceListingAcitivity.this,response.body().getData());
                                    binding.rvInvoices.setAdapter(listShowAdapter);
                                } else {
                                    displaySnackBar.DisplaySnackBar("Data not found", Constant.TYPE_ERROR);


                                }
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
                        displaySnackBar.DisplaySnackBar(errorBody.getMessage(), Constant.TYPE_ERROR);
                        if (errorBody.getMessage().contains("invalid")) {
                            Log.e("TAG", "onResponse: invalid invoiceactivity" );
                        }
                    } catch (Exception e) {
                        displaySnackBar.DisplaySnackBar(Constant.ERROR_INVALID_JSON, Constant.TYPE_ERROR);

                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<InvoiceListResponse> call, Throwable t) {

                if (t != null && (t instanceof IOException || t instanceof SocketTimeoutException
                        || t instanceof ConnectException || t instanceof NoRouteToHostException
                        || t instanceof SecurityException)) {
                    displaySnackBar.DisplaySnackBar(Constant.NETWIRK_ERROR, Constant.TYPE_ERROR);

                }
            }
        });
    }

}
