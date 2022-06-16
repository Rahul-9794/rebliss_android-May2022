package com.rebliss.view.activity.cudelsimilar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.rebliss.BuildConfig;
import com.rebliss.utils.GPSTracker;
import com.rebliss.R;
import com.rebliss.data.perf.MySingleton;
import com.rebliss.domain.constant.Constant;
import com.rebliss.domain.model.CudelResponse;
import com.rebliss.domain.model.ErrorBody;
import com.rebliss.domain.model.city.CityResponce;
import com.rebliss.domain.model.demandpartner.DemandPartnerNameResponse;
import com.rebliss.domain.model.searchstate.SearchStateResponce;
import com.rebliss.domain.model.shopcategory.AllShop;
import com.rebliss.domain.model.shopcategory.ShopCategoryResponse;
import com.rebliss.domain.model.state.State;
import com.rebliss.domain.model.state.StateResponce;
import com.rebliss.presenter.helper.DisplaySnackBar;
import com.rebliss.presenter.helper.Network;
import com.rebliss.presenter.retrofit.ApiClient;
import com.rebliss.presenter.retrofit.ApiInterface;
import com.rebliss.view.adapter.ShowListAdapter;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DailyDsr extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "DailyDsr";
    private EditText etPinCode, etState, etCity, etShopName, etMobileNumber, etRemarks1, etRemarks2, etAddress, etUniquecodeforshop;
    private Spinner shopCategory, status, demandPartnerName;
    private ImageView btnBack;

    private String mCity, mCityId, mState, mstateId;
    private List<AllShop> shopCatList;
    private List<DemandPartnerNameResponse.AllGroup> demandNameList;
    ArrayList<String> categoryArray = new ArrayList<>();

    private GPSTracker gpsTracker;
    private MySingleton mySingleton;
    private DisplaySnackBar displaySnackBar;
    private final List<State> stateData = new ArrayList<>();
    private ImageView imgCameraShop;
    public ArrayList<File> shopImage = new ArrayList<File>();
    private int CAMERA_PIC_REQUEST_SHOP = 101;
    Uri photoURI;
    String currentPhotoPath = "";
    String fileAbsolutePath = "";
    private RecyclerView rvShoplist;

    String[] statusArray = new String[]{
            "Status",
            "Successful",
            "Not Successful",
    };
    private KProgressHUD kProgressHUD;
    private DemandPartnerNameResponse.Data demandpartnerData;
    ArrayList<String> demandPartnerArray = new ArrayList<>();

    private Network network;
    String address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_dsr);
        initView();
        isStoragePermissionGranted();
        permission();
        if (network.isNetworkConnected(this)) {
            pincodeListener();
            spinnerAdapters();
            getShopCategory();
            getDemandPartner();
        } else {
            displaySnackBar.DisplaySnackBar(Constant.NETWIRK_ERROR, Constant.TYPE_ERROR);
        }

        if (!gpsTracker.canGetLocation()) {
            gpsTracker.showSettingsAlert();
        }

    }

    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Camera, Storage permission is granted", Toast.LENGTH_LONG).show();
                return true;
            } else {

                Log.v("permission", "Permission is revoked");
                Toast.makeText(this, "Permission is revoked,", Toast.LENGTH_LONG).show();
                ActivityCompat.requestPermissions(DailyDsr.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v("permission", "Permission is granted");
            return true;
        }
    }

    private void getDemandPartner() {
        kProgressHUD = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .setWindowColor(getResources().getColor(R.color.progressbar_color))
                .show();
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        final Call<DemandPartnerNameResponse> call = apiService.getDemandPartnerName();
        call.enqueue(new Callback<DemandPartnerNameResponse>() {
            @Override
            public void onResponse(@NotNull Call<DemandPartnerNameResponse> call, @NotNull Response<DemandPartnerNameResponse> response) {
                kProgressHUD.dismiss();
                if (response.isSuccessful()) {
                    if (response.code() >= 200 && response.code() < 700) {
                        if (response.code() == 200) {
                            assert response.body() != null;
                            if (response.body().getStatus() == 1) {
                                if (response.body().getData() != null) {
                                    Gson gson = new Gson();
                                    String json = gson.toJson(response.body(), DemandPartnerNameResponse.class);
                                    Log.i("TAG", "json " + json);
                                    demandpartnerData = response.body().getData();

                                    demandPartnerArray.clear();
                                    demandPartnerArray.add("Select Demand Partner");
                                    demandNameList = response.body().getData().getAllGroups();
                                    for (DemandPartnerNameResponse.AllGroup datum : demandNameList) {
                                        demandPartnerArray.add(datum.getCategoryName());
                                    }
                                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(DailyDsr.this, android.R.layout.select_dialog_item, demandPartnerArray);
                                    ArrayAdapter<String> stateAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, demandPartnerArray);
                                    demandPartnerName.setAdapter(stateAdapter);

                                } else {
                                    displaySnackBar.DisplaySnackBar("Data not found", Constant.TYPE_ERROR);
                                    etPinCode.setText("");


                                }
                            }
                        }

                    } else {
                        displaySnackBar.DisplaySnackBar(Constant.UNEXPECTED, Constant.TYPE_ERROR);

                    }
                } else {

                    try {
                        ErrorBody errorBody;
                        Gson gson = new Gson();
                        assert response.errorBody() != null;
                        errorBody = gson.fromJson(response.errorBody().string(), ErrorBody.class);

                        displaySnackBar.DisplaySnackBar(errorBody.getMessage(), Constant.TYPE_ERROR);

                    } catch (Exception e) {
                        displaySnackBar.DisplaySnackBar(Constant.ERROR_INVALID_JSON, Constant.TYPE_ERROR);

                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(@NotNull Call<DemandPartnerNameResponse> call, @NotNull Throwable t) {
                kProgressHUD.dismiss();
                Log.e("TAG", "onFailure: " + t.getMessage());
            }
        });
    }


    private void initView() {

        kProgressHUD = new KProgressHUD(this);
        network = new Network();
        gpsTracker = new GPSTracker(this);
        Button btnsave = findViewById(R.id.btnSave);
        displaySnackBar = new DisplaySnackBar(this);
        mySingleton = MySingleton.getInstance(this);


        etPinCode = findViewById(R.id.etPincode);
        etState = findViewById(R.id.etState);
        etCity = findViewById(R.id.etCity);
        etShopName = findViewById(R.id.tv_shopname);
        etMobileNumber = findViewById(R.id.tv_mobileno);
        etShopName = findViewById(R.id.tv_shopname);
        etUniquecodeforshop = findViewById(R.id.tv_shop_uniqueId);
        shopCategory = findViewById(R.id.tv_shopcategory);
        status = findViewById(R.id.tv_status);
        demandPartnerName = findViewById(R.id.spDemandPartnerName);
        btnBack = findViewById(R.id.icBack);
        etRemarks1 = findViewById(R.id.tv_remarks1);
        etRemarks2 = findViewById(R.id.tv_remarks2);
        etAddress = findViewById(R.id.etAddress);
        imgCameraShop = findViewById(R.id.imgCameraShop);
        rvShoplist = findViewById(R.id.rv_shopList);
        btnsave.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        imgCameraShop.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnSave) {
            if (isValidData()) {

                callPostDailyDSR();

            }
        }
        if (v.getId() == R.id.icBack) {
            onBackPressed();
        }
        if (v.getId() == R.id.imgCameraShop) {
            if (shopImage.size() > 4) {
                Toast.makeText(this, "You can not select more then 5 image", Toast.LENGTH_LONG).show();
                return;
            }
            try {

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File f = getImageFile();


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    photoURI = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID.concat(".provider"), f);
                } else {
                    photoURI = Uri.fromFile(f);
                }
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                setResult(RESULT_OK, null);
                startActivityForResult(intent, CAMERA_PIC_REQUEST_SHOP);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private File getImageFile() throws IOException {
        String imageFileName = "IMG_" + System.currentTimeMillis() + "_";
        String imagesFolder = "images";
        File storageDir = new File(
                Environment.getExternalStorageDirectory(), imagesFolder
        );
        System.out.println(storageDir.getAbsolutePath());
        if (!storageDir.exists()) {
            storageDir.mkdirs();
            Log.e("TAG", "file not exists: ");
        } else
            Log.e("TAG", "file exists: ");

        File file = File.createTempFile(imageFileName, ".jpg", storageDir);

        Log.e("TAG", "getImageFile: " + file.getAbsolutePath());

        currentPhotoPath = "file:" + file.getAbsolutePath();
        fileAbsolutePath = file.getAbsolutePath();
        Log.e("TAG", "currentphotopath: >>>>" + currentPhotoPath);
        mySingleton.saveData("currentpath", currentPhotoPath);
        mySingleton.saveData("fileabsolutepath", fileAbsolutePath);
        return file;
    }


    private void callPostDailyDSR() {
        KProgressHUD kProgressHUD = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .setWindowColor(getResources().getColor(R.color.progressbar_color))
                .show();

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(gpsTracker.getLatitude(), gpsTracker.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5


            address = addresses.get(0).getAddressLine(0);

        } catch (IOException e) {
            e.printStackTrace();
        }
        final Call<CudelResponse> call = apiService.postDailyDSR(
                mySingleton.getData(Constant.USER_ID),
                demandPartnerName.getSelectedItem().toString(),
                etShopName.getText().toString(),
                etUniquecodeforshop.getText().toString(),
                etMobileNumber.getText().toString(),
                shopCategory.getSelectedItem().toString(),
                etPinCode.getText().toString(),
                mState,
                mCity,
                etAddress.getText().toString(),
                status.getSelectedItem().toString(),
                etRemarks1.getText().toString(),
                etRemarks2.getText().toString(),
                gpsTracker.getLatitude(), gpsTracker.getLongitude(),
                address
        );

        call.enqueue(new Callback<CudelResponse>() {
            @Override
            public void onResponse(@NotNull Call<CudelResponse> call, @NotNull Response<CudelResponse> response) {
                kProgressHUD.dismiss();
                if (response.isSuccessful()) {
                    if (response.code() == 200) {
                        assert response.body() != null;
                        if (response.body().status == 1) {
                            Toast.makeText(DailyDsr.this, "Activity Submitted Successfully!", Toast.LENGTH_SHORT).show();

                            AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
                                    DailyDsr.this);
                            alertDialog2.setTitle("Confirm!");
                            alertDialog2.setCancelable(false);
                            alertDialog2.setMessage("Your activity is created with id: " + response.body().data.all_groups.id);
                            alertDialog2.setPositiveButton("OK",
                                    (dialog, which) -> {
                                        dialog.cancel();
                                        onBackPressed();
                                    });

                            alertDialog2.show();

                        }
                    }

                    if (response.code() == 500) {
                        displaySnackBar.DisplaySnackBar("Internal server error", 4);
                    }

                }
            }

            @Override
            public void onFailure(@NotNull Call<CudelResponse> call, @NotNull Throwable t) {
                kProgressHUD.dismiss();
                displaySnackBar.DisplaySnackBar(Constant.NETWIRK_ERROR, Constant.TYPE_ERROR);
            }
        });
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

    private boolean isValidData() {


        if (etShopName.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please enter Business name", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (shopCategory.getSelectedItem().toString().equals(categoryArray.get(0))) {
            Toast.makeText(this, "Please select shop category", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (demandPartnerName.getSelectedItem().toString().equals(demandPartnerArray.get(0))) {
            Toast.makeText(this, "Please select Demand Partner", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (etMobileNumber.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please enter Mobile number", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (etMobileNumber.getText().toString().length() < 10) {
            Toast.makeText(this, "Please enter valid Mobile number", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (status.getSelectedItem().toString().equals(statusArray[0])) {
            Toast.makeText(this, "Please select status category", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (etPinCode.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please enter Pincode", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (etAddress.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please enter Address", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (etPinCode.getText().toString().length() < 6) {
            Toast.makeText(this, "Please enter valid Pincode", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void spinnerAdapters() {


        ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, statusArray);
        status.setAdapter(statusAdapter);
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
            public void onResponse(@NotNull Call<SearchStateResponce> call, @NotNull Response<SearchStateResponce> response) {
                kProgressHUD.dismiss();
                if (response.isSuccessful()) {
                    if (response.code() >= 200 && response.code() < 700) {
                        if (response.code() == 200) {
                            assert response.body() != null;
                            if (response.body().getData().getDetails() != null) {
                                mCity = response.body().getData().getDetails().getLocation();
                                mstateId = response.body().getData().getDetails().getState_id();
                                mCityId = response.body().getData().getDetails().getCity_id();

                                getState(mstateId);
                                getCityBusiness(mstateId);
                            } else {
                                showWarningSimpleAlertDialog("Data not found", "Pincode is not available in database");
                                etPinCode.setText("");
                                Log.e(TAG, "onResponse: " + gpsTracker.getLatitude() + "\n" +
                                        gpsTracker.getLongitude());

                            }
                        }
                    } else {
                        displaySnackBar.DisplaySnackBar(Constant.UNEXPECTED, Constant.TYPE_ERROR);
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<SearchStateResponce> call, @NotNull Throwable t) {
                kProgressHUD.dismiss();
                displaySnackBar.DisplaySnackBar(Constant.UNEXPECTED, Constant.TYPE_ERROR);
            }
        });
    }

    public void getCityBusiness(String state_id) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        final Call<CityResponce> call = apiService.getCity(mySingleton.getData(Constant.TOKEN_BASE_64), state_id);
        call.enqueue(new Callback<CityResponce>() {
            @Override
            public void onResponse(@NotNull Call<CityResponce> call, @NotNull Response<CityResponce> response) {
                if (response.isSuccessful()) {
                    if (response.code() >= 200 && response.code() < 700) {
                        if (response.code() == 200) {
                            assert response.body() != null;
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
                        assert response.errorBody() != null;
                    } catch (Exception e) {
                        displaySnackBar.DisplaySnackBar(Constant.ERROR_INVALID_JSON, Constant.TYPE_ERROR);
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<CityResponce> call, @NotNull Throwable t) {
                displaySnackBar.DisplaySnackBar(Constant.UNEXPECTED, Constant.TYPE_ERROR);
            }
        });
    }

    public void getState(final String stateId) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        final Call<StateResponce> call = apiService.getState(mySingleton.getData(Constant.TOKEN_BASE_64));
        call.enqueue(new Callback<StateResponce>() {
            @Override
            public void onResponse(@NotNull Call<StateResponce> call, @NotNull Response<StateResponce> response) {
                if (response.isSuccessful()) {
                    if (response.code() >= 200 && response.code() < 700) {
                        if (response.code() == 200) {
                            assert response.body() != null;
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
                    displaySnackBar.DisplaySnackBar(Constant.ERROR_INVALID_JSON, Constant.TYPE_ERROR);
                }
            }

            @Override
            public void onFailure(@NotNull Call<StateResponce> call, @NotNull Throwable t) {
                displaySnackBar.DisplaySnackBar("failure", Constant.TYPE_ERROR);
            }
        });
    }

    public void getShopCategory() {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<ShopCategoryResponse> call = apiService.getShopCategories();
        call.enqueue(new Callback<ShopCategoryResponse>() {
            @Override
            public void onResponse(@NotNull Call<ShopCategoryResponse> call, @NotNull Response<ShopCategoryResponse> response) {

                if (response.isSuccessful()) {
                    if (response.code() >= 200 && response.code() < 700) {
                        if (response.code() == 200) {
                            assert response.body() != null;
                            if (response.body().getStatus() == 1) {
                                if (response.body().getData() != null) {

                                    categoryArray.clear();
                                    categoryArray.add("Select Shop Category");
                                    shopCatList = response.body().getData().getAllShops();
                                    for (AllShop datum : shopCatList) {
                                        categoryArray.add(datum.getValue());
                                    }
                                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(DailyDsr.this, android.R.layout.select_dialog_item, categoryArray);
                                    ArrayAdapter<String> stateAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, categoryArray);
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
            public void onFailure(@NotNull Call<ShopCategoryResponse> call, @NotNull Throwable t) {

                displaySnackBar.DisplaySnackBar(Constant.UNEXPECTED, Constant.TYPE_ERROR);
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

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, @Nullable final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_PIC_REQUEST_SHOP && resultCode == Activity.RESULT_OK) {
            try {

                currentPhotoPath = mySingleton.getData("currentpath");
                fileAbsolutePath = mySingleton.getData("fileabsolutepath");
                File f = new File(fileAbsolutePath);

                Log.e("TAG", "runnnn:>>>> " + f.toString());
               /* File compressedImage = new Compressor(this) .setMaxWidth(640) .setMaxHeight(480) .
                        setQuality(70) .setCompressFormat(Bitmap.CompressFormat.PNG)
                        .compressToFile(f);*/

                shopImage.add(f);


                Log.e("TAG", "size: " + shopImage.size() + f.getAbsolutePath());
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(DailyDsr.this, LinearLayoutManager.HORIZONTAL, true);
                rvShoplist.setLayoutManager(linearLayoutManager);
                ShowListAdapter adapterlist = new ShowListAdapter(DailyDsr.this, shopImage);
                rvShoplist.setAdapter(adapterlist);
                Log.e("TAG", "absolute " + f.getAbsolutePath());
                Log.e("TAG", "path " + f.getPath());


                if (f.getAbsolutePath() != null) {
                    Bitmap bitmap = BitmapFactory.decodeFile(fileAbsolutePath);
                } else {
                    Log.e("TAG", "file not exist: ");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            imgCameraShop.setVisibility(View.VISIBLE);
        }
    }


}