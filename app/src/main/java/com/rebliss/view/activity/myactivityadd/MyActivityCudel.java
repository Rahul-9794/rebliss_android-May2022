package com.rebliss.view.activity.myactivityadd;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.gson.Gson;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.rebliss.utils.GPSTracker;
import com.rebliss.R;
import com.rebliss.data.perf.MySingleton;
import com.rebliss.domain.constant.Constant;
import com.rebliss.domain.model.CudelResponse;
import com.rebliss.domain.model.ErrorBody;
import com.rebliss.domain.model.city.CityResponce;
import com.rebliss.domain.model.payment.SuccessResponse;
import com.rebliss.domain.model.searchstate.SearchStateResponce;
import com.rebliss.domain.model.shopcategory.AllShop;
import com.rebliss.domain.model.shopcategory.ShopCategoryResponse;
import com.rebliss.domain.model.state.State;
import com.rebliss.domain.model.state.StateResponce;
import com.rebliss.presenter.helper.DisplaySnackBar;
import com.rebliss.presenter.helper.Network;
import com.rebliss.presenter.retrofit.ApiClient;
import com.rebliss.presenter.retrofit.ApiInterface;

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

public class MyActivityCudel extends AppCompatActivity {

    private EditText etPinCode, etState, etCity, etShopName, etMobileNumber;
    private Spinner shopType, shopCategory, status;
    private Button btnSave;
    private ImageView btnBack;

    private MySingleton mySingleton;
    private Network network;

    private DisplaySnackBar displaySnackBar;
    private List<State> stateData = new ArrayList<>();

    private String mLocationPinCode, mCity, mCityId, mState, mstateId;
    private List<AllShop> shopCatList;
    ArrayList<String> categoryArray = new ArrayList<>();
    String[] shopTypeArray = new String[]{
            "Type",
            "Client",
            "Partner",
    };
    String[] statusArray = new String[]{
            "Status",
            "Successful",
            "Not Successful",
    };
    private GPSTracker gpsTracker;
    private String currentVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_cudel);

        gpsTracker = new GPSTracker(this);
        mySingleton = MySingleton.getInstance(this);

        setupViews();
        getShopCategory();

    }

    public void permission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        gpsTracker = new GPSTracker(this);
    }

    private void setupViews() {
        findViews();
        pincodeListener();
        spinnerAdapters();

        if (!gpsTracker.canGetLocation()) {
            gpsTracker.showSettingsAlert();
        }

        try {
            currentVersion = this.getPackageManager().getPackageInfo(this.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        btnSave.setOnClickListener(view -> {

            if (isValidData()) {
                callPostCudel();
            }
        });

        btnBack.setOnClickListener(view -> onBackPressed());
    }

    private boolean isValidData() {

        if (shopType.getSelectedItem().toString().equals(shopTypeArray[0])) {
            Toast.makeText(this, "Please select shop type", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (etShopName.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please enter Business name", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (shopCategory.getSelectedItem().toString().equals(categoryArray.get(0))) {
            Toast.makeText(this, "Please select shop category", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (etMobileNumber.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please enter Mobile number", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (status.getSelectedItem().toString().equals(statusArray[0])) {
            Toast.makeText(this, "Please select status", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (etPinCode.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please enter Pincode", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    private void callPostCudel() {
      KProgressHUD  kProgressHUD = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .setWindowColor(getResources().getColor(R.color.progressbar_color))
                .show();

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        final Call<CudelResponse> call = apiService.postCudel(
                mySingleton.getData(Constant.USER_ID),
                shopType.getSelectedItem().toString(),
                etShopName.getText().toString(),
                shopCategory.getSelectedItem().toString(),
                status.getSelectedItem().toString(),
                "50",
                "64",
                "65",
                mCity,
                mCity,
                mState,
                etPinCode.getText().toString(),
                gpsTracker.getLatitude(),
                gpsTracker.getLongitude(),
                currentVersion,
                etMobileNumber.getText().toString());
        call.enqueue(new Callback<CudelResponse>() {
            @Override
            public void onResponse(Call<CudelResponse> call, Response<CudelResponse> response) {
                kProgressHUD.dismiss();
                if (response.isSuccessful()) {
                    if (response.code() == 200) {
                        if (response.body().status == 1) {
                            Toast.makeText(MyActivityCudel.this, "Activity Submitted Successfully!", Toast.LENGTH_SHORT).show();

                            AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
                                    MyActivityCudel.this);
                            alertDialog2.setTitle("Confirm!");
                            alertDialog2.setCancelable(false);
                            alertDialog2.setMessage("Your activity is created with id: " + response.body().data.all_groups.id);
                            alertDialog2.setPositiveButton("OK",
                                    (dialog, which) -> {
                                        hitEarningTaskApi(Constant.activitiesList.get(0).getEarningTaskID(),
                                                "65",
                                                Constant.activitiesList.get(0).getAmount());

                                        dialog.cancel();
                                        onBackPressed();
                                    });

                            alertDialog2.show();
                        }
                        else {
                            AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
                                    MyActivityCudel.this);
                            alertDialog2.setTitle("Confirm!");
                            alertDialog2.setCancelable(false);
                            alertDialog2.setMessage(response.body().desc);
                            alertDialog2.setPositiveButton("OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                            onBackPressed();
                                        }
                                    });
                            alertDialog2.show();
                        }

                    } else {
                        displaySnackBar.DisplaySnackBar(Constant.UNEXPECTED, Constant.TYPE_ERROR);
                    }
                }
            }

            @Override
            public void onFailure(Call<CudelResponse> call, Throwable t) {
                kProgressHUD.dismiss();
                if (t != null && (t instanceof IOException || t instanceof SocketTimeoutException
                        || t instanceof ConnectException || t instanceof NoRouteToHostException
                        || t instanceof SecurityException)) {
                    displaySnackBar.DisplaySnackBar(Constant.NETWIRK_ERROR, Constant.TYPE_ERROR);

                }
            }
        });
    }

    private void hitEarningTaskApi(String earningTaskID, String activityId, String amount) {
        KProgressHUD kProgressHUD = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .setWindowColor(getResources().getColor(R.color.progressbar_color))
                .show();

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        final Call<SuccessResponse> call = apiService.getEarningOnTask(mySingleton.getData(Constant.USER_ID), earningTaskID, activityId, amount);
        call.enqueue(new Callback<SuccessResponse>() {
            @Override
            public void onResponse(Call<SuccessResponse> call, Response<SuccessResponse> response) {
                kProgressHUD.dismiss();
                if (response.isSuccessful()) {
                    if (response.code() >= 200 && response.code() < 700) {
                        if (response.code() == 200) {
                            Log.e("TAG", "onResponse: success myacitivitycudel" );
                        }
                    } else {
                        displaySnackBar.DisplaySnackBar(Constant.UNEXPECTED, Constant.TYPE_ERROR);
                    }
                }
            }

            @Override
            public void onFailure(Call<SuccessResponse> call, Throwable t) {
                kProgressHUD.dismiss();
                if (t != null && (t instanceof IOException || t instanceof SocketTimeoutException
                        || t instanceof ConnectException || t instanceof NoRouteToHostException
                        || t instanceof SecurityException)) {
                    displaySnackBar.DisplaySnackBar(Constant.NETWIRK_ERROR, Constant.TYPE_ERROR);

                }
            }
        });

    }


    private void spinnerAdapters() {
        ArrayAdapter shopTypeAdapter = new ArrayAdapter(getApplicationContext(), R.layout.spinner_item, shopTypeArray);
        shopType.setAdapter(shopTypeAdapter);

        ArrayAdapter statusAdapter = new ArrayAdapter(getApplicationContext(), R.layout.spinner_item, statusArray);
        status.setAdapter(statusAdapter);
    }

    private void pincodeListener() {
        etPinCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() == 6) {
                    getStateCity(editable.toString());
                }
            }
        });
    }

    private void findViews() {
        displaySnackBar = new DisplaySnackBar(this);
        etPinCode = findViewById(R.id.etPincode);
        etState = findViewById(R.id.etState);
        etCity = findViewById(R.id.etCity);
        etShopName = findViewById(R.id.tv_shopname);
        etMobileNumber = findViewById(R.id.tv_mobileno);
        etShopName = findViewById(R.id.tv_shopname);

        shopType = findViewById(R.id.tv_shopType);
        shopCategory = findViewById(R.id.tv_shopcategory);
        status = findViewById(R.id.tv_status);

        btnSave = findViewById(R.id.btnSave);
        btnBack = findViewById(R.id.icBack);
    }

    private void getStateCity(final String zipCode) {
     KProgressHUD kProgressHUD = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .setWindowColor(getResources().getColor(R.color.progressbar_color))
                .show();

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        final Call<SearchStateResponce> call = apiService.getStateCity(mySingleton.getData(Constant.TOKEN_BASE_64), zipCode);
        call.enqueue(new Callback<SearchStateResponce>() {
            @Override
            public void onResponse(Call<SearchStateResponce> call, Response<SearchStateResponce> response) {
                kProgressHUD.dismiss();
                if (response.isSuccessful()) {
                    if (response.code() >= 200 && response.code() < 700) {
                        if (response.code() == 200) {
                            if (response.body().getData().getDetails() != null) {
                                mCity = response.body().getData().getDetails().getLocation();
                                mstateId = response.body().getData().getDetails().getState_id();
                                mCityId = response.body().getData().getDetails().getCity_id();

                                getState(mstateId);
                                getCityBusiness(mstateId);
                            }

                            else
                            {
                                showWarningSimpleAlertDialog("Data not found","Pincode is not available in database");
                                etPinCode.setText("");


                            }
                        }
                    } else {
                        displaySnackBar.DisplaySnackBar(Constant.UNEXPECTED, Constant.TYPE_ERROR);
                    }
                }
            }

            @Override
            public void onFailure(Call<SearchStateResponce> call, Throwable t) {
                kProgressHUD.dismiss();
                if (t != null && (t instanceof IOException || t instanceof SocketTimeoutException
                        || t instanceof ConnectException || t instanceof NoRouteToHostException
                        || t instanceof SecurityException)) {
                    displaySnackBar.DisplaySnackBar(Constant.NETWIRK_ERROR, Constant.TYPE_ERROR);
                }
            }
        });
    }

    public void getCityBusiness(String state_id) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        final Call<CityResponce> call = apiService.getCity(mySingleton.getData(Constant.TOKEN_BASE_64), state_id);
        call.enqueue(new Callback<CityResponce>() {
            @Override
            public void onResponse(Call<CityResponce> call, Response<CityResponce> response) {
                if (response.isSuccessful()) {
                    if (response.code() >= 200 && response.code() < 700) {
                        if (response.code() == 200) {
                            if (response.body().getStatus() == 1) {
                                if (response.body().getData() != null) {
                                    if (response.body().getData().getCitys().size() > 0) {
                                        for (int i = 0; i < response.body().getData().getCitys().size(); i++) {
                                            if (response.body().getData().getCitys().get(i).getId().equals(mCityId)) {
                                                mCity = response.body().getData().getCitys().get(i).getS_name();
                                                etCity.setText(mCity);
                                            }
                                        }
                                    }
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

    public void getState(final String stateId) {
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
                                    if (response.body().getData().getState().size() > 0) {
                                        stateData.addAll(response.body().getData().getState());
                                        for (int i = 0; i < stateData.size(); i++) {
                                            if (stateData.get(i).getId().equals(stateId)) {
                                                mState = stateData.get(i).getS_name();
                                                etState.setText(mState);
                                            }
                                        }
                                    }
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

    public void getShopCategory() {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<ShopCategoryResponse> call = apiService.getShopCategories();
        call.enqueue(new Callback<ShopCategoryResponse>() {
            @Override
            public void onResponse(Call<ShopCategoryResponse> call, Response<ShopCategoryResponse> response) {

                if (response.isSuccessful()) {
                    if (response.code() >= 200 && response.code() < 700) {
                        if (response.code() == 200) {
                            if (response.body().getStatus() == 1) {
                                if (response.body().getData() != null) {

                                    categoryArray.clear();
                                    categoryArray.add("Select Shop Category");
                                    shopCatList = response.body().getData().getAllShops();
                                    for (AllShop datum : shopCatList) {
                                        categoryArray.add(datum.getValue());
                                    }
                                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(MyActivityCudel.this, android.R.layout.select_dialog_item, categoryArray);
                                    ArrayAdapter stateAdapter = new ArrayAdapter(getApplicationContext(), R.layout.spinner_item, categoryArray);
                                    shopCategory.setAdapter(stateAdapter);
                                } else {
                                    displaySnackBar.DisplaySnackBar("Data not found", Constant.TYPE_ERROR);
                                }
                            }
                        }
                    } else {
                        displaySnackBar.DisplaySnackBar(Constant.UNEXPECTED, Constant.TYPE_ERROR);
                    }
                }
            }

            @Override
            public void onFailure(Call<ShopCategoryResponse> call, Throwable t) {

                if (t != null && (t instanceof IOException || t instanceof SocketTimeoutException
                        || t instanceof ConnectException || t instanceof NoRouteToHostException
                        || t instanceof SecurityException)) {
                    displaySnackBar.DisplaySnackBar(Constant.NETWIRK_ERROR, Constant.TYPE_ERROR);
                }
            }
        });
    }

    private void showWarningSimpleAlertDialog(String title, String message) {
        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE).setTitleText(title)
                .setContentText(message)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();

                    }
                })
                .show();
    }
}