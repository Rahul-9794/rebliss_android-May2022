package com.rebliss.view.activity.cppayment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.gson.Gson;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import com.rebliss.R;
import com.rebliss.data.perf.MySingleton;
import com.rebliss.databinding.ActivityGSTDetailBinding;
import com.rebliss.domain.constant.Constant;
import com.rebliss.domain.model.ErrorBody;
import com.rebliss.domain.model.payment.OrderIdRazorPayResponse;
import com.rebliss.domain.model.payment.PaymentDetailResponse;
import com.rebliss.domain.model.payment.SuccessResponse;
import com.rebliss.presenter.helper.Logout;
import com.rebliss.presenter.helper.TextUtil;
import com.rebliss.presenter.retrofit.ApiClient;
import com.rebliss.presenter.retrofit.ApiInterface;
import com.rebliss.utils.Utils;

import org.json.JSONObject;

import java.io.IOException;
import java.net.ConnectException;
import java.net.NoRouteToHostException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GSTDetailActivity extends AppCompatActivity implements PaymentResultListener {

    ActivityGSTDetailBinding binding;
    Checkout checkout;
    private int totalAmount = 0;
    private double tax = 0.0;

    private MySingleton mySingleton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_g_s_t_detail);
        mySingleton = new MySingleton(this);
        checkout = new Checkout();

        Checkout.preload(getApplicationContext());
        callPaymentDetailTest();
        listener();

    }

    public void callPaymentDetailTest() {
        ApiInterface apiService = ApiClient.paymentDetail().create(ApiInterface.class);
        final Call<PaymentDetailResponse> call = apiService.getPaymentDetailTest(mySingleton.getData("commucicationState"), mySingleton.getData(Constant.USER_ID));
        call.enqueue(new Callback<PaymentDetailResponse>() {
            @Override
            public void onResponse(Call<PaymentDetailResponse> call, Response<PaymentDetailResponse> response) {

                if (response.isSuccessful()) {
                    if (response.code() >= 200 && response.code() < 700) {
                        if (response.code() == 200) {
                            if (response.body().getStatus() == 1) {
                                if (response.body().getData() != null) {

                                    PaymentDetailResponse.Data data = response.body().getData();
                                    binding.tvOnboardingFee.setText("" + data.getOnboardingFee());
                                    binding.tvSGST.setText("" + data.getSGST());
                                    binding.tvCGST.setText("" + data.getCGST());
                                    binding.tvIGST.setText("" + data.getIGST());
                                    tax = data.getSGST() + data.getCGST() + data.getIGST();
                                    binding.tvTotalAmount.setText("" + data.getTotal());
                                    totalAmount = data.getTotal();
                                    binding.tvInWords.setText(getString(R.string.rs) + Utils.convert(totalAmount) + " Only");

                                }
                            } else if (response.body().getStatus() == 0) {
                                Log.e("TAG", "onResponse:>>>>>>>>>Status 0 ");
                            }
                        }

                    } else {
                        Log.e("TAG", "onResponse:>>>>>>>>>elseeeeeeee0 ");
                    }
                } else {

                    try {
                        ErrorBody errorBody = new ErrorBody();
                        Gson gson = new Gson();
                        errorBody = gson.fromJson(response.errorBody().string(), ErrorBody.class);
                        Toast.makeText(GSTDetailActivity.this, errorBody.getMessage(), Toast.LENGTH_SHORT).show();
                        if (errorBody.getName().equalsIgnoreCase(GSTDetailActivity.this.getString(R.string.unAuthorisedUser))) {
                            Logout.Login(GSTDetailActivity.this);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<PaymentDetailResponse> call, Throwable t) {
                Log.e("TAG", "onFailure: " + t.getMessage());
                if (t != null && (t instanceof IOException || t instanceof SocketTimeoutException
                        || t instanceof ConnectException || t instanceof NoRouteToHostException
                        || t instanceof SecurityException)) {
                }
            }
        });
    }

    private void listener() {
        binding.btnAcceptAndContinueToPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getOrderId();
            }
        });

        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }


    public void callPaymentDetail() {
        ApiInterface apiService = ApiClient.paymentDetail().create(ApiInterface.class);
        final Call<PaymentDetailResponse> call = apiService.getPaymentDetail(mySingleton.getData("commucicationState"));
        call.enqueue(new Callback<PaymentDetailResponse>() {
            @Override
            public void onResponse(Call<PaymentDetailResponse> call, Response<PaymentDetailResponse> response) {

                if (response.isSuccessful()) {
                    if (response.code() >= 200 && response.code() < 700) {
                        if (response.code() == 200) {
                            if (response.body().getStatus() == 1) {
                                if (response.body().getData() != null) {

                                    PaymentDetailResponse.Data data = response.body().getData();
                                    binding.tvOnboardingFee.setText("" + data.getOnboardingFee());
                                    binding.tvSGST.setText("" + data.getSGST());
                                    binding.tvCGST.setText("" + data.getCGST());
                                    binding.tvIGST.setText("" + data.getIGST());
                                    tax = data.getSGST() + data.getCGST() + data.getIGST();
                                    binding.tvTotalAmount.setText("" + data.getTotal());
                                    totalAmount = data.getTotal();
                                    binding.tvInWords.setText(getString(R.string.rs) + Utils.convert(totalAmount) + " Only");

                                }
                            } else if (response.body().getStatus() == 0) {
                                Log.e("TAG", "onResponse:>>>>>>>>>Status 0 ");
                            }
                        }

                    } else {
                        Log.e("TAG", "onResponse:>>>>>>>>>elseeeeeeee0 ");
                    }
                } else {

                    try {
                        ErrorBody errorBody = new ErrorBody();
                        Gson gson = new Gson();
                        errorBody = gson.fromJson(response.errorBody().string(), ErrorBody.class);
                        Toast.makeText(GSTDetailActivity.this, errorBody.getMessage(), Toast.LENGTH_SHORT).show();
                        if (errorBody.getName().equalsIgnoreCase(GSTDetailActivity.this.getString(R.string.unAuthorisedUser))) {
                            Logout.Login(GSTDetailActivity.this);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<PaymentDetailResponse> call, Throwable t) {
                Log.e("TAG", "onFailure: " + t.getMessage());
                if (t != null && (t instanceof IOException || t instanceof SocketTimeoutException
                        || t instanceof ConnectException || t instanceof NoRouteToHostException
                        || t instanceof SecurityException)) {
                }
            }
        });
    }

    public void getOrderId() {

        if (totalAmount == 0) {
            Toast.makeText(this, "Total amount should not be zero", Toast.LENGTH_LONG);
            return;
        }

        ApiInterface apiService = ApiClient.getRazorPayClient().create(ApiInterface.class);
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("amount", totalAmount + "00");
        hashMap.put("currency", "INR");
        String credentials = "rzp_live_ZgXqFBwYlIYXMN" + ":" + "EBbihWm0SDN0z3LC0OeAPH11";
         //String credentials = "rzp_test_IjjnEfvOJAehCu" + ":" + "CMzX4I36PgNl9cLRtjlLlPyp";
        final String basic =
                "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);


        final Call<OrderIdRazorPayResponse> call = apiService.getRazorPayOrderId(basic, hashMap);
        call.enqueue(new Callback<OrderIdRazorPayResponse>() {
            @Override
            public void onResponse(Call<OrderIdRazorPayResponse> call, Response<OrderIdRazorPayResponse> response) {

                if (response.isSuccessful()) {
                    if (response.code() >= 200 && response.code() < 700) {
                        if (response.code() == 200) {

                            if (response.body() != null) {

                                startPayment(response.body().getId());

                            }

                        }

                    }
                } else {

                    try {
                        ErrorBody errorBody = new ErrorBody();
                        Gson gson = new Gson();
                        errorBody = gson.fromJson(response.errorBody().string(), ErrorBody.class);
                        Toast.makeText(GSTDetailActivity.this, errorBody.getMessage(), Toast.LENGTH_SHORT).show();
                        if (errorBody.getName().equalsIgnoreCase(GSTDetailActivity.this.getString(R.string.unAuthorisedUser))) {
                            Logout.Login(GSTDetailActivity.this);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<OrderIdRazorPayResponse> call, Throwable t) {
                if (t != null && (t instanceof IOException || t instanceof SocketTimeoutException
                        || t instanceof ConnectException || t instanceof NoRouteToHostException
                        || t instanceof SecurityException)) {
                }
            }
        });
    }


    public void submitOrder() {

        ApiInterface apiService = ApiClient.paymentDetail().create(ApiInterface.class);

        Log.e("TAG", "submitOrder: "+ mySingleton.getData(Constant.USER_ID)+"\n"+orderIdGlobal
                +"\n"+totalAmount+"\n"+tax);
        final Call<SuccessResponse> call = apiService.submitOrder(mySingleton.getData(Constant.USER_ID),
                orderIdGlobal, totalAmount + "", tax);
        call.enqueue(new Callback<SuccessResponse>() {
            @Override
            public void onResponse(Call<SuccessResponse> call, Response<SuccessResponse> response) {

                if (response.isSuccessful()) {
                    if (response.code() >= 200 && response.code() < 700) {
                        if (response.code() == 200) {
                            if (response.body().getStatus() == 1) {
                                finish();
                                Intent intent = new Intent(GSTDetailActivity.this, PaymentSuccessActivity.class);
                                intent.putExtra("trans_id", orderIdGlobal);
                                startActivity(intent);

                            } else if (response.body().getStatus() == 0) {
                                Toast.makeText(GSTDetailActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                } else {

                    try {
                        ErrorBody errorBody = new ErrorBody();
                        Gson gson = new Gson();
                        errorBody = gson.fromJson(response.errorBody().string(), ErrorBody.class);
                        Log.e("GST DETAIL", "onResponse: >>>>>>"+errorBody.getMessage() );
                        Toast.makeText(GSTDetailActivity.this, errorBody.getMessage(), Toast.LENGTH_SHORT).show();
                        if (errorBody.getName().equalsIgnoreCase(GSTDetailActivity.this.getString(R.string.unAuthorisedUser))) {
                            Logout.Login(GSTDetailActivity.this);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<SuccessResponse> call, Throwable t) {
                if (t != null && (t instanceof IOException || t instanceof SocketTimeoutException
                        || t instanceof ConnectException || t instanceof NoRouteToHostException
                        || t instanceof SecurityException)) {
                }
            }
        });
    }


    String orderIdGlobal;

    public void startPayment(String orderId) {
        checkout.setKeyID("rzp_live_ZgXqFBwYlIYXMN");
        //checkout.setKeyID("rzp_test_IjjnEfvOJAehCu");
        orderIdGlobal = orderId;
        checkout.setImage(R.drawable.app_icon);
        final Activity activity = this;
        try {
            JSONObject options = new JSONObject();

            String idss = UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();

            String idssss = idss.substring(0, 12);
            options.put("name", "reBLISS");
            options.put("description", "reference no. is " + idssss);
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            options.put("order_id", orderId);
            options.put("theme.color", "#3399cc");
            options.put("currency", "INR");
            options.put("amount", totalAmount + "00");
            options.put("prefill.email", mySingleton.getData("emailidd"));
            options.put("prefill.contact", mySingleton.getData("contactnum"));
            checkout.open(activity, options);
        } catch (Exception e) {
            Log.e("TAG", "Error in starting Razorpay Checkout", e);
        }
    }


    @Override
    public void onPaymentSuccess(String s) {
        Toast.makeText(this, "Payment done successfully.", Toast.LENGTH_LONG).show();

        submitOrder();
    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }


    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, VerifyPaymentDetailActivity.class));
        finish();

    }
}
