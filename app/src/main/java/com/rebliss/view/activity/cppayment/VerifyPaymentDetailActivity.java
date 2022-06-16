package com.rebliss.view.activity.cppayment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.gson.Gson;
import com.rebliss.MainActivity;
import com.rebliss.R;
import com.rebliss.data.perf.MySingleton;
import com.rebliss.databinding.ActivityVerifyPaymentDetailBinding;
import com.rebliss.domain.constant.Constant;
import com.rebliss.domain.model.ErrorBody;
import com.rebliss.domain.model.city.City;
import com.rebliss.domain.model.city.CityResponce;
import com.rebliss.domain.model.payment.AddressDetailResponse;
import com.rebliss.domain.model.payment.PincodeDetailResponse;
import com.rebliss.domain.model.payment.SuccessResponse;
import com.rebliss.domain.model.state.State;
import com.rebliss.domain.model.state.StateResponce;
import com.rebliss.presenter.helper.DisplaySnackBar;
import com.rebliss.presenter.helper.Logout;
import com.rebliss.presenter.helper.Network;
import com.rebliss.presenter.retrofit.ApiClient;
import com.rebliss.presenter.retrofit.ApiInterface;
import com.rebliss.utils.Utils;
import com.rebliss.view.adapter.CitySpinnerAdapter;
import com.rebliss.view.adapter.StateSpinnerAdapter;
import com.rebliss.view.fragment.FragmentDashboard;

import java.io.IOException;
import java.net.ConnectException;
import java.net.NoRouteToHostException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerifyPaymentDetailActivity extends AppCompatActivity {
    private static final String TAG = "VerifyPaymentDetailActi";
    MySingleton mySingleton;
    ActivityVerifyPaymentDetailBinding binding;

    private DisplaySnackBar displaySnackBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_verify_payment_detail);
        mySingleton = new MySingleton(this);
        displaySnackBar = new DisplaySnackBar(this);
        binding.tvTermAndCondition.setText(Html.fromHtml("<font color=#000000><u>I accept </font><font color=#2196F3>terms and conditions</u></font>"));
        callAddressDetail();

        listener();

    }

    private void listener() {
        binding.btnAcceptAndContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!binding.checkBox1.isChecked()) {
                    Toast.makeText(VerifyPaymentDetailActivity.this, "Please accept all check box", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!binding.checkBox2.isChecked()) {
                    Toast.makeText(VerifyPaymentDetailActivity.this, "Please accept all check box", Toast.LENGTH_SHORT).show();
                    return;
                }

                finish();
                startActivity(new Intent(VerifyPaymentDetailActivity.this, GSTDetailActivity.class));
            }
        });

        binding.btnEdit.setOnClickListener(view -> showEditDialog());

        binding.btnBack.setOnClickListener(view -> onBackPressed());

        binding.tvTermAndCondition.setOnClickListener(view -> startActivity(new Intent(VerifyPaymentDetailActivity.this, TermsAndConditionActivity.class)));
    }

    private AddressDetailResponse.Data editDetail;

    public void callAddressDetail() {

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        final Call<AddressDetailResponse> call = apiService.getAddressDetail(mySingleton.getData(Constant.USER_ID));
        call.enqueue(new Callback<AddressDetailResponse>() {
            @Override
            public void onResponse(Call<AddressDetailResponse> call, Response<AddressDetailResponse> response) {

                if (response.isSuccessful()) {
                    if (response.code() >= 200 && response.code() < 700) {
                        if (response.code() == 200) {
                            if (response.body().getStatus() == 1) {
                                if (response.body().getData() != null) {
                                    AddressDetailResponse.Data data = response.body().getData();
                                    editDetail = response.body().getData();
                                    binding.tvBillTo.setText("" + data.getBillTo());
                                    binding.tvBillAddress.setText("" + data.getAddress());

                                    binding.tvGSTNo.setText("" + data.getGstNumber());

                                    binding.tvPhone.setText("Phone No.: " + data.getPhone());
                                    mySingleton.saveData("contactnum",data.getPhone());
                                    binding.tvEmail.setText("Email: " + data.getEmail());
                                    mySingleton.saveData("emailidd",data.getEmail().toString());

                                    getState(0);


                                }
                            } else if (response.body().getStatus() == 0) {
                                Log.e("TAG", "status 0" );
                            }
                        }
                    }
                } else {

                    try {
                        ErrorBody errorBody = new ErrorBody();
                        Gson gson = new Gson();
                        errorBody = gson.fromJson(response.errorBody().string(), ErrorBody.class);
                        Toast.makeText(VerifyPaymentDetailActivity.this, errorBody.getMessage(), Toast.LENGTH_SHORT).show();
                        if (errorBody.getName().equalsIgnoreCase(VerifyPaymentDetailActivity.this.getString(R.string.unAuthorisedUser))) {
                            Logout.Login(VerifyPaymentDetailActivity.this);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<AddressDetailResponse> call, Throwable t) {
                if (t != null && (t instanceof IOException || t instanceof SocketTimeoutException
                        || t instanceof ConnectException || t instanceof NoRouteToHostException
                        || t instanceof SecurityException)) {
                }
            }
        });
    }

    public void updateAddressDetail() {

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        final Call<SuccessResponse> call = apiService.updateAddressDetail(mySingleton.getData("id"),
                etAddress1.getText().toString(),
                etAddress2.getText().toString(),
                etCity.getText().toString(),
                etState.getText().toString(),
                etPin.getText().toString(),
                etGst.getText().toString());
        call.enqueue(new Callback<SuccessResponse>() {
            @Override
            public void onResponse(Call<SuccessResponse> call, Response<SuccessResponse> response) {

                if (response.isSuccessful()) {
                    if (response.code() >= 200 && response.code() < 700) {
                        if (response.code() == 200) {
                            if (response.body().getStatus() == 1) {
                                mySingleton.saveData("commucicationState", etState.getText().toString());
                                startActivity(new Intent(VerifyPaymentDetailActivity.this, VerifyPaymentDetailActivity.class));
                                finish();
                            } else if (response.body().getStatus() == 0) {
                                Toast.makeText(VerifyPaymentDetailActivity.this, "City Not Found", Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                } else {

                    try {
                        ErrorBody errorBody = new ErrorBody();
                        Gson gson = new Gson();
                        errorBody = gson.fromJson(response.errorBody().string(), ErrorBody.class);
                        Toast.makeText(VerifyPaymentDetailActivity.this, errorBody.getMessage(), Toast.LENGTH_SHORT).show();
                        if (errorBody.getName().equalsIgnoreCase(VerifyPaymentDetailActivity.this.getString(R.string.unAuthorisedUser))) {
                            Logout.Login(VerifyPaymentDetailActivity.this);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<SuccessResponse> call, Throwable t) {
                if (t != null && (t instanceof IOException || t instanceof ConnectException || t instanceof NoRouteToHostException || t instanceof SecurityException)) {
                }
            }
        });
    }


    private void showEditDialog() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_edit_payment_address);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        dialog.show();
        initDataDialog(dialog);


        Button btnUpdate = dialog.findViewById(R.id.btnUpdate);
        btnUpdate.setOnClickListener(view -> {
         if(validation()) {
             updateAddressDetail();
         }
        });
    }
    private boolean validation() {
        boolean status = true;

        if (etPin.getText().toString().trim().length() < 6) {
            status = false;
            showWarningSimpleAlertDialog(Constant.TITLE, "Please enter valid pin");
        }
        if (etState.getText().toString().trim().isEmpty()) {
            status = false;
            showWarningSimpleAlertDialog(Constant.TITLE, "Please select state");
        }

        if (etCity.getText().toString().trim().isEmpty()) {
            status = false;
            showWarningSimpleAlertDialog(Constant.TITLE, "Please select city");
        }


        if (etAddress1.getText().toString().trim().isEmpty()) {
            status = false;
            showWarningSimpleAlertDialog(Constant.TITLE, "Please enter address line 1");
        }

        if (etAddress2.getText().toString().trim().isEmpty()) {
            status = false;
            showWarningSimpleAlertDialog(Constant.TITLE, "Please enter address line 2");
        }
        try { if (!etGst.getText().toString().isEmpty()) {
            if (!Utils.validGSTIN(etGst.getText().toString())) {
                status = false;
                showWarningSimpleAlertDialog(Constant.TITLE, "GST Number is not valid");
            }
        }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return status;
    }

    EditText etState, etCity;
    private EditText etPin, etAddress1, etAddress2, etGst;

    private void initDataDialog(Dialog dialog) {

        etState = dialog.findViewById(R.id.etState);
        etCity = dialog.findViewById(R.id.etCity);
        etPin = dialog.findViewById(R.id.etPin);
        etAddress1 = dialog.findViewById(R.id.etAddress1);
        etAddress2 = dialog.findViewById(R.id.etAddress2);
        etGst = dialog.findViewById(R.id.etGst);

        try {
            etPin.setText(editDetail.getZipcode());
            etAddress1.setText(editDetail.getAddressLine1());
            etAddress2.setText(editDetail.getAddressLine2());
            etGst.setText(editDetail.getGstNumber());

            etState.setText(editDetail.getState());
            etCity.setText(editDetail.getCity());
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        etPin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 6) {

                    getPincodeDetail("https://maps.googleapis.com/maps/api/geocode/json?address=" + charSequence.toString() + "&key=AIzaSyB9colSXRp0FVFonvcXbzSSgJXWENVSUg0");

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        network = new Network();
        stateData = new ArrayList<>();
        State state = new State();
        state.setId("-1");
        state.setS_name("Select State");
        stateData.add(state);
    }

    private String getStateNameFromId(String id) {
        String pos = "";
        for (int i = 0; i < stateData.size(); i++) {
            if (stateData.get(i).getId().equals(id)) {
                pos = stateData.get(i).getS_name();
                break;
            }
        }
        return pos;
    }
    private int spCommunicationStatePosition;
    private Network network;
    private List<State> stateData;
    private List<City> cityData;

    public void getState(final int dialog) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        final Call<StateResponce> call = apiService.getState(mySingleton.getData(Constant.TOKEN_BASE_64));
        call.enqueue(new Callback<StateResponce>() {
            @Override
            public void onResponse(Call<StateResponce> call, Response<StateResponce> response) {

                if (response.isSuccessful()) {
                    if (response.code() >= 200 && response.code() < 700) {
                        if (response.code() == 200) {
                            if (response.body().getStatus() == 1) {
                                if (response.body().getData() != null) {
                                    Gson gson = new Gson();
                                    String json = gson.toJson(response.body(), StateResponce.class);
                                    Log.i("TAG", "json " + json);
                                    if (response.body().getData().getState().size() > 0) {


                                        if (dialog == 1) {
                                            stateData.addAll(response.body().getData().getState());
                                        } else {
                                            stateData = new ArrayList<>();
                                            stateData.addAll(response.body().getData().getState());
                                            binding.tvPlaceOfSupply.setText(getStateNameFromId(editDetail.getState()) + " (" + editDetail.getState() + ")");
                                        }
                                    }

                                } else {
                                    displaySnackBar.DisplaySnackBar("Data not found", Constant.TYPE_ERROR);


                                }
                            } else if (response.body().getStatus() == 0) {
                                Log.e(TAG, "onResponse:status 0 " );
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
                        if (errorBody.getName().equalsIgnoreCase(getString(R.string.unAuthorisedUser))) {
                            Logout.Login(VerifyPaymentDetailActivity.this);
                        }
                    } catch (Exception e) {
                        displaySnackBar.DisplaySnackBar(Constant.ERROR_INVALID_JSON, Constant.TYPE_ERROR);

                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<StateResponce> call, Throwable t) {

                if (t != null && (t instanceof IOException || t instanceof SocketTimeoutException
                        || t instanceof ConnectException || t instanceof NoRouteToHostException
                        || t instanceof SecurityException)) {
                    displaySnackBar.DisplaySnackBar(Constant.NETWIRK_ERROR, Constant.TYPE_ERROR);

                }
            }
        });
    }

    public void getCity(final boolean isFirst) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        final Call<CityResponce> call = apiService.getCity(mySingleton.getData(Constant.TOKEN_BASE_64), stateData.get(spCommunicationStatePosition).getId());
        call.enqueue(new Callback<CityResponce>() {
            @Override
            public void onResponse(Call<CityResponce> call, Response<CityResponce> response) {

                if (response.isSuccessful()) {
                    if (response.code() >= 200 && response.code() < 700) {
                        if (response.code() == 200) {
                            if (response.body().getStatus() == 1) {
                                if (response.body().getData() != null) {
                                    Gson gson = new Gson();
                                    String json = gson.toJson(response.body(), CityResponce.class);
                                    Log.i("TAG", "json " + json);
                                    cityData = new ArrayList<>();
                                    if (response.body().getData().getCitys().size() > 0) {
                                        cityData = new ArrayList<>();
                                        City city = new City();
                                        city.setId("-1");
                                        city.setS_name("Select City");
                                        cityData.add(city);
                                        cityData.addAll(response.body().getData().getCitys());

                                    } else {

                                    }

                                } else {
                                    displaySnackBar.DisplaySnackBar("Data not found", Constant.TYPE_ERROR);

                                }
                            } else if (response.body().getStatus() == 0) {
                                Log.e(TAG, "onResponse: status 00" );                            }
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
                        Log.i("error5", errorBody.getMessage());
                        if (errorBody.getName().equalsIgnoreCase(getString(R.string.unAuthorisedUser))) {
                            Logout.Login(VerifyPaymentDetailActivity.this);
                        }
                    } catch (Exception e) {
                        displaySnackBar.DisplaySnackBar(Constant.ERROR_INVALID_JSON, Constant.TYPE_ERROR);

                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<CityResponce> call, Throwable t) {

                if (t != null && (t instanceof IOException || t instanceof SocketTimeoutException
                        || t instanceof ConnectException || t instanceof NoRouteToHostException
                        || t instanceof SecurityException)) {
                    displaySnackBar.DisplaySnackBar(Constant.NETWIRK_ERROR, Constant.TYPE_ERROR);

                }
            }
        });
    }


    public void getPincodeDetail(String url) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        final Call<PincodeDetailResponse> call = apiService.getPincodeDetail(url);
        call.enqueue(new Callback<PincodeDetailResponse>() {
            @Override
            public void onResponse(Call<PincodeDetailResponse> call, Response<PincodeDetailResponse> response) {

                if (response.isSuccessful()) {
                    if (response.code() >= 200 && response.code() < 700) {
                        if (response.code() == 200) {
                            if (response.body().getStatus().equals("OK")) {

                                PincodeDetailResponse.Location location = response.body().getResults().get(0).getGeometry().getLocation();
                                Utils.getPincode(VerifyPaymentDetailActivity.this, location.getLat(), location.getLng());

                                etState.setText(Utils.stateR);
                                etCity.setText(Utils.districtR);

                            } else {
                                showWarningSimpleAlertDialog(Constant.TITLE, "Data not found for this pincode");
                                etState.setText("");
                                etCity.setText("");
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
                        callDisplayErrorCode(Integer.parseInt(errorBody.getStatus()), errorBody.getMessage());
                        displaySnackBar.DisplaySnackBar(errorBody.getMessage(), Constant.TYPE_ERROR);
                        Log.i("error5", errorBody.getMessage());
                        if (errorBody.getName().equalsIgnoreCase(getString(R.string.unAuthorisedUser))) {
                            Logout.Login(VerifyPaymentDetailActivity.this);
                        }
                    } catch (Exception e) {
                        displaySnackBar.DisplaySnackBar(Constant.ERROR_INVALID_JSON, Constant.TYPE_ERROR);

                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<PincodeDetailResponse> call, Throwable t) {

                if (t != null && (t instanceof IOException || t instanceof SocketTimeoutException
                        || t instanceof ConnectException || t instanceof NoRouteToHostException
                        || t instanceof SecurityException)) {
                    displaySnackBar.DisplaySnackBar(Constant.NETWIRK_ERROR, Constant.TYPE_ERROR);

                }
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
    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() == 0) {
            this.finish();
        } else {
            if (getFragmentManager().getBackStackEntryCount() == 1) {
                this.finish();
            }

        }
    }
    private void showWarningSimpleAlertDialog(String title, String message) {
        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE).setTitleText(title)
                .setContentText(message)
                .setConfirmClickListener(sweetAlertDialog -> sweetAlertDialog.dismissWithAnimation())
                .show();
    }
}



