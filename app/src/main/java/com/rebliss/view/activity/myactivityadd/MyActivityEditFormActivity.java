package com.rebliss.view.activity.myactivityadd;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.gson.Gson;
import com.rebliss.utils.GPSTracker;
import com.rebliss.R;
import com.rebliss.data.perf.MySingleton;
import com.rebliss.domain.constant.Constant;
import com.rebliss.domain.model.ActivitySelectModel;
import com.rebliss.domain.model.ErrorBody;
import com.rebliss.domain.model.activity_detail.ActivityDetailResponse;
import com.rebliss.domain.model.activity_detail.AllGroups;
import com.rebliss.domain.model.categoryresponse.AllCategory;
import com.rebliss.domain.model.categoryresponse.CategoryResponse;
import com.rebliss.domain.model.shopcategory.AllShop;
import com.rebliss.domain.model.shopcategory.ShopCategoryResponse;
import com.rebliss.presenter.helper.DisplaySnackBar;
import com.rebliss.presenter.retrofit.ApiClient;
import com.rebliss.presenter.retrofit.ApiInterface;
import com.rebliss.utils.MyProgressDialog;
import com.rebliss.utils.Utils;
import com.rebliss.view.adapter.FileListAdapter;
import com.rebliss.view.adapter.MyFormActivityListShowAdapter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.NoRouteToHostException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyActivityEditFormActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MyActivityEditFormActivty";
    private EditText tvShopname, storeid;
    private EditText tvMobileno;
    private String name;
    private Integer gstvalue = 0;
    private RecyclerView insiteimagelist, outsideimagelist, acivityimagelist;
    private TextView btnInsidePhoto;
    private ImageView imgCameraInside;
    private TextView btnOutsidePhoto;
    private ImageView imgCameraOutside;
    private TextView btnActivityPhoto;
    private ImageView imgCameraActivity;
    private TextView etPicode;

    private CheckBox otherActivityCheckBox;
    private EditText etGstNumber;
    private TextView btnUploadGstPic;
    private ImageView imgCameraGst;
    private ImageView imgGalleryGst;
    private RelativeLayout gstContainer;

    private CheckBox otherActivityCheckBoxPan, udyogaadhaarCheckBox, fssaiCheckBox, tradelicenseCheckBox;
    ;
    private EditText etPanNumber;
    private TextView btnUploadPanPic;
    private ImageView imgCameraPan;
    private ImageView imgGalleryPan;
    private RelativeLayout panContainer;

    private TextView btnYes;
    private TextView btnNo;
    private Button btnAddActivity;
    private RecyclerView rvActivity;
    private Button btnSave;
    private DisplaySnackBar displaySnackBar;
    private MySingleton mySingleton;
    private LinearLayout yesNoContainer;
    public ArrayList<Integer> outsideimage = new ArrayList<Integer>();
    public ArrayList<Integer> insidesideimage = new ArrayList<Integer>();
    public ArrayList<Integer> acivityimage = new ArrayList<Integer>();
    public ArrayList<com.rebliss.domain.model.file.File> previousoutsideimage = new ArrayList<com.rebliss.domain.model.file.File>();
    public ArrayList<com.rebliss.domain.model.file.File> previousinsidesideimage = new ArrayList<com.rebliss.domain.model.file.File>();
    public ArrayList<com.rebliss.domain.model.file.File> previousacivityimage = new ArrayList<com.rebliss.domain.model.file.File>();
    private ImageView imgInsideCheck, imgOutsideCheck, imgActivityCheck, icBack;
    private Uri mImageUri;

    String pattern = "^\\s*(?:\\+?(\\d{1,3}))?[-. (]*(\\d{3})[-. )]*(\\d{3})[-. ]*(\\d{4})(?: *x(\\d+))?\\s*$";
    Matcher m;
    private GPSTracker gpsTracker;

    public int activityId = 0;


    private AppCompatSpinner spinShopcategory;
    String selectShopCategory = "";
    int selectShopCategoryPosition = 0;


    private int CAMERA_PIC_REQUEST_OUSIDE = 101,
            CAMERA_PIC_REQUEST_ACTIVITY = 102,
            CAMERA_PIC_REQUEST_INSIDE = 103,
            CAMERA_PIC_REQUEST_GST_PIC = 104,
            GALLERY_PIC_REQUEST_GST_PIC = 105,
            CAMERA_PIC_REQUEST_PAN_PIC = 106,
            GALLERY_PIC_REQUEST_PAN_PIC = 107;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_form);
        activityId = getIntent().getIntExtra("id", 0);
        initView();
        if (!gpsTracker.canGetLocation()) {
            gpsTracker.showSettingsAlert();
        }
        isStoragePermissionGranted();
        if (ContextCompat.checkSelfPermission(MyActivityEditFormActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MyActivityEditFormActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            if (ActivityCompat.shouldShowRequestPermissionRationale(MyActivityEditFormActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(MyActivityEditFormActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            } else {

            }
        }

        listener();
        getDetail();

    }

    AllGroups ag;

    private void getDetail() {

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<ActivityDetailResponse>
                call = apiService.getActivityDetail(activityId);

        MyProgressDialog.showProgress(this);

        call.enqueue(new Callback<ActivityDetailResponse>() {
            @Override
            public void onResponse(Call<ActivityDetailResponse> call, Response<ActivityDetailResponse> response) {
                MyProgressDialog.dismissProgress();
                if (response.isSuccessful()) {
                    if (response.code() >= 200 && response.code() < 700) {
                        if (response.code() == 200) {
                            if (response.body().getStatus() == 1) {
                                if (response.body().getData() != null) {
                                    ag = response.body().getData().getAllGroups();
                                    tvShopname.setText(ag.getShopName());
                                    tvMobileno.setText(ag.getMobile());

                                    getShopCategory(ag.getShopCategory());

                                    storeid.setText(ag.getStoreid());

                                    for (int i = 0; i < 5; i++) {
                                        insidesideimage.add(1);
                                        acivityimage.add(1);
                                        outsideimage.add(1);
                                    }
                                    for (int i = 0; i < response.body().getData().getInsidePhotos().size(); i++) {
                                        com.rebliss.domain.model.file.File file = new com.rebliss.domain.model.file.File(null, 0, response.body().getData().getInsidePhotos().get(i).getImage(), 0, response.body().getData().getInsidePhotos().get(i).getKey());
                                        previousinsidesideimage.add(file);
                                        insidesideimage.set(response.body().getData().getInsidePhotos().get(i).getKey(), 0);
                                    }

                                    for (int i = 0; i < response.body().getData().getActivityPhotos().size(); i++) {

                                        com.rebliss.domain.model.file.File file = new com.rebliss.domain.model.file.File(null, 0, response.body().getData().getActivityPhotos().get(i).getImage(), 0, response.body().getData().getActivityPhotos().get(i).getKey());
                                        previousacivityimage.add(file);
                                        acivityimage.set(response.body().getData().getActivityPhotos().get(i).getKey(), 0);

                                    }

                                    for (int i = 0; i < response.body().getData().getOutsidePhotos().size(); i++) {
                                        com.rebliss.domain.model.file.File file = new com.rebliss.domain.model.file.File(null, 0, response.body().getData().getOutsidePhotos().get(i).getImage(), 0, response.body().getData().getOutsidePhotos().get(i).getKey());
                                        previousoutsideimage.add(file);
                                        outsideimage.set(response.body().getData().getOutsidePhotos().get(i).getKey(), 0);
                                    }


                                    LinearLayoutManager linearLayoutManager4 = new LinearLayoutManager(MyActivityEditFormActivity.this, LinearLayoutManager.HORIZONTAL, true);
                                    insiteimagelist.setLayoutManager(linearLayoutManager4);
                                    FileListAdapter adapterlist4 = new FileListAdapter(MyActivityEditFormActivity.this, previousinsidesideimage, 0);
                                    insiteimagelist.setAdapter(adapterlist4);

                                    LinearLayoutManager linearLayoutManager5 = new LinearLayoutManager(MyActivityEditFormActivity.this, LinearLayoutManager.HORIZONTAL, true);
                                    acivityimagelist.setLayoutManager(linearLayoutManager5);
                                    FileListAdapter adapterlist5 = new FileListAdapter(MyActivityEditFormActivity.this, previousacivityimage, 1);
                                    acivityimagelist.setAdapter(adapterlist5);


                                    LinearLayoutManager linearLayoutManager6 = new LinearLayoutManager(MyActivityEditFormActivity.this, LinearLayoutManager.HORIZONTAL, true);
                                    outsideimagelist.setLayoutManager(linearLayoutManager6);
                                    FileListAdapter adapterlist6 = new FileListAdapter(MyActivityEditFormActivity.this, previousoutsideimage, 2);
                                    outsideimagelist.setAdapter(adapterlist6);


                                    imgInsideCheck.setVisibility(View.VISIBLE);
                                    imgOutsideCheck.setVisibility(View.VISIBLE);
                                    imgActivityCheck.setVisibility(View.VISIBLE);


                                    etPicode.setText(ag.getPincode() + "");

                                    if (ag.getProofId() != null) {
                                        if (ag.getProofId() == 1) {
                                            otherActivityCheckBox.setChecked(true);
                                            gstContainer.setVisibility(View.VISIBLE);
                                            etGstNumber.setText(ag.getGstNumber());
                                            btnUploadGstPic.setText(ag.getGstPhoto());
                                        } else if (ag.getProofId() == 2) {
                                            udyogaadhaarCheckBox.setChecked(true);
                                            gstContainer.setVisibility(View.VISIBLE);
                                            etGstNumber.setText(ag.getGstNumber());
                                            btnUploadGstPic.setText(ag.getGstPhoto());
                                        } else if (ag.getProofId() == 3) {
                                            tradelicenseCheckBox.setChecked(true);
                                            gstContainer.setVisibility(View.VISIBLE);
                                            etGstNumber.setText(ag.getGstNumber());
                                            btnUploadGstPic.setText(ag.getGstPhoto());
                                        } else if (ag.getProofId() == 4) {
                                            fssaiCheckBox.setChecked(true);
                                            gstContainer.setVisibility(View.VISIBLE);
                                            etGstNumber.setText(ag.getGstNumber());
                                            btnUploadGstPic.setText(ag.getGstPhoto());
                                        } else {
                                            otherActivityCheckBox.setChecked(false);
                                            gstContainer.setVisibility(View.GONE);
                                        }
                                    }

                                    if (ag.getPanNumber() != null && !ag.getPanNumber().isEmpty() && ag.getPanNumber() != null) {
                                        otherActivityCheckBoxPan.setChecked(true);
                                        panContainer.setVisibility(View.VISIBLE);
                                        etPanNumber.setText(ag.getPanNumber());
                                        btnUploadPanPic.setText(ag.getPanPhoto());
                                    } else {
                                        otherActivityCheckBox.setChecked(false);
                                        panContainer.setVisibility(View.GONE);

                                    }


                                    Constant.activitiesList.clear();
                                    ActivitySelectModel selectModel = new ActivitySelectModel();
                                    selectModel.setCategory(ag.getCategoryId());
                                    selectModel.setSubCategory(ag.getSubCategoryId());
                                    selectModel.setSubCategory1(ag.getSubCategory1Id());


                                    Constant.activitiesList.add(selectModel);

                                    MyFormActivityListShowAdapter myFormActivityListShowAdapter = new MyFormActivityListShowAdapter(MyActivityEditFormActivity.this, Constant.activitiesList, 1);
                                    rvActivity.setAdapter(myFormActivityListShowAdapter);


                                    isApiCalled = true;


                                } else {
                                    displaySnackBar.DisplaySnackBar("Data not found", Constant.TYPE_ERROR);
                                }
                            } else if (response.body().getStatus() == 0) {
                                Log.e(TAG, "onResponse: " );
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
                            Log.e(TAG, "onResponse: invalid");
                        }
                    } catch (Exception e) {
                        displaySnackBar.DisplaySnackBar(Constant.ERROR_INVALID_JSON, Constant.TYPE_ERROR);

                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<ActivityDetailResponse> call, Throwable t) {
                MyProgressDialog.dismissProgress();
                if (t != null && (t instanceof IOException || t instanceof SocketTimeoutException
                        || t instanceof ConnectException || t instanceof NoRouteToHostException
                        || t instanceof SecurityException)) {
                    displaySnackBar.DisplaySnackBar(Constant.NETWIRK_ERROR, Constant.TYPE_ERROR);

                }
            }
        });

    }


    private void initView() {
        mySingleton = new MySingleton(this);
        gpsTracker = new GPSTracker(this);


        displaySnackBar = new DisplaySnackBar(this);
        tvShopname = findViewById(R.id.tv_shopname);
        storeid = findViewById(R.id.tv_storeid);
        tvMobileno = findViewById(R.id.tv_mobileno);
        spinShopcategory = findViewById(R.id.tv_shopcategory);
        btnInsidePhoto = findViewById(R.id.btnInsidePhoto);
        imgCameraInside = findViewById(R.id.imgCameraInside);
        btnOutsidePhoto = findViewById(R.id.btnOutsidePhoto);
        imgCameraOutside = findViewById(R.id.imgCameraOutside);
        btnActivityPhoto = findViewById(R.id.btnActivityPhoto);
        imgCameraActivity = findViewById(R.id.imgCameraActivity);
        etPicode = findViewById(R.id.etPicode);
        otherActivityCheckBox = findViewById(R.id.otherActivityCheckBox);
        etGstNumber = findViewById(R.id.etGstNumber);
        btnUploadGstPic = findViewById(R.id.tvGstFileName);
        imgCameraGst = findViewById(R.id.imgCameraGst);
        imgGalleryGst = findViewById(R.id.imgGalleryGst);
        gstContainer = findViewById(R.id.gstContainer);
        insiteimagelist = findViewById(R.id.tv_insiterecyler);
        outsideimagelist = findViewById(R.id.tv_outsiderecyler);
        acivityimagelist = findViewById(R.id.tv_acivityrecyler);
        udyogaadhaarCheckBox = findViewById(R.id.udyogaadhaarCheckBox);
        tradelicenseCheckBox = findViewById(R.id.tradelicenseCheckBox);
        fssaiCheckBox = findViewById(R.id.fssaiCheckBox);
        otherActivityCheckBoxPan = findViewById(R.id.otherActivityCheckBoxPan);
        etPanNumber = findViewById(R.id.etPanNumber);
        btnUploadPanPic = findViewById(R.id.tvPanFileName);
        imgCameraPan = findViewById(R.id.imgCameraPan);
        imgGalleryPan = findViewById(R.id.imgGalleryPan);
        panContainer = findViewById(R.id.panContainer);

        btnYes = findViewById(R.id.btnYes);
        btnNo = findViewById(R.id.btnNo);
        btnAddActivity = findViewById(R.id.btnAddActivity);
        rvActivity = findViewById(R.id.rvActivity);
        btnSave = findViewById(R.id.btnSave);

        yesNoContainer = findViewById(R.id.yesNoContainer);
        icBack = findViewById(R.id.icBack);

        imgInsideCheck = findViewById(R.id.imgInsideCheck);
        imgOutsideCheck = findViewById(R.id.imgOutsideCheck);
        imgActivityCheck = findViewById(R.id.imgAcivityCheck);

        yesNoContainer.setVisibility(View.GONE);
        btnAddActivity.setVisibility(View.GONE);
        btnSave.setText("Resubmit");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvActivity.setLayoutManager(linearLayoutManager);

    }


    private void listener() {

        imgCameraOutside.setOnClickListener(this);
        imgCameraActivity.setOnClickListener(this);
        imgCameraInside.setOnClickListener(this);
        imgCameraGst.setOnClickListener(this);
        imgGalleryGst.setOnClickListener(this);
        imgCameraPan.setOnClickListener(this);
        imgGalleryPan.setOnClickListener(this);
        btnYes.setOnClickListener(this);
        btnNo.setOnClickListener(this);
        btnAddActivity.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        icBack.setOnClickListener(this);


        spinShopcategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                selectShopCategoryPosition = position;
                if (position > 0)
                    selectShopCategory = categoryArray.get(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        otherActivityCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (b) {
                    gstContainer.setVisibility(View.VISIBLE);
                    btnUploadGstPic.setVisibility(View.VISIBLE);
                } else {
                    gstContainer.setVisibility(View.GONE);
                    btnUploadGstPic.setVisibility(View.GONE);
                }
            }
        });

        otherActivityCheckBoxPan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (b) {
                    panContainer.setVisibility(View.VISIBLE);
                    btnUploadPanPic.setVisibility(View.VISIBLE);
                } else {
                    panContainer.setVisibility(View.GONE);
                    btnUploadPanPic.setVisibility(View.GONE);
                }
            }
        });

        otherActivityCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (b) {
                    gstvalue = 1;
                    name = "GST";
                    etGstNumber.setHint("Enter GST");
                    udyogaadhaarCheckBox.setChecked(false);
                    fssaiCheckBox.setChecked(false);
                    tradelicenseCheckBox.setChecked(false);

                    gstContainer.setVisibility(View.VISIBLE);
                    btnUploadGstPic.setVisibility(View.VISIBLE);
                } else {
                    gstContainer.setVisibility(View.GONE);
                    btnUploadGstPic.setVisibility(View.GONE);
                }
            }
        });

        udyogaadhaarCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (b) {
                    gstvalue = 2;
                    name = "Udyog Aadhaar";
                    etGstNumber.setHint("Enter  Udyog Aadhaar");
                    otherActivityCheckBox.setChecked(false);
                    fssaiCheckBox.setChecked(false);
                    tradelicenseCheckBox.setChecked(false);
                    gstContainer.setVisibility(View.VISIBLE);
                    btnUploadGstPic.setVisibility(View.VISIBLE);
                } else {
                    gstContainer.setVisibility(View.GONE);
                    btnUploadGstPic.setVisibility(View.GONE);
                }
            }
        });
        fssaiCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                ;

                if (b) {
                    name = "FSSAI";
                    gstvalue = 4;
                    etGstNumber.setHint("Enter FSSAI");
                    udyogaadhaarCheckBox.setChecked(false);
                    otherActivityCheckBox.setChecked(false);
                    tradelicenseCheckBox.setChecked(false);
                    gstContainer.setVisibility(View.VISIBLE);
                    btnUploadGstPic.setVisibility(View.VISIBLE);
                } else {
                    gstContainer.setVisibility(View.GONE);
                    btnUploadGstPic.setVisibility(View.GONE);
                }
            }
        });
        tradelicenseCheckBox.setOnCheckedChangeListener((compoundButton, b) -> {
            ;
            if (b) {
                name = "Trade License";
                gstvalue = 3;
                etGstNumber.setHint("Enter Trade License");
                udyogaadhaarCheckBox.setChecked(false);

                fssaiCheckBox.setChecked(false);

                otherActivityCheckBox.setChecked(false);

                gstContainer.setVisibility(View.VISIBLE);
                btnUploadGstPic.setVisibility(View.VISIBLE);
            } else {
                gstContainer.setVisibility(View.GONE);
                btnUploadGstPic.setVisibility(View.GONE);
            }
        });


        tvMobileno.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (isApiCalled)
                    if (storeid.getText().toString().isEmpty()) {
                        storeid.setError("Please enter store id first");
                        Toast.makeText(MyActivityEditFormActivity.this, "Please enter store id first", Toast.LENGTH_LONG).show();


                    } else if (charSequence.length() == 10) {
                        checkValidationFromApi(charSequence.toString(), 1);
                    }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        etPanNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (isApiCalled)
                    if (otherActivityCheckBoxPan.isChecked()) {
                        if (storeid.getText().toString().isEmpty()) {
                            storeid.setError("Please enter store id first");
                            Toast.makeText(MyActivityEditFormActivity.this, "Please enter store id first", Toast.LENGTH_LONG).show();

                        } else if (charSequence.length() == 10) {

                            if (ag.getPanNumber().isEmpty() || !charSequence.toString().equals(ag.getPanNumber())) {
                                checkValidationFromApi(charSequence.toString(), 3);
                            }
                        }
                    }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        etGstNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (isApiCalled)
                    if (otherActivityCheckBox.isChecked()) {
                        if (storeid.getText().toString().isEmpty()) {
                            storeid.setError("Please enter store id first");
                            Toast.makeText(MyActivityEditFormActivity.this, "Please enter store id first", Toast.LENGTH_LONG).show();

                        } else if (charSequence.length() == 15) {
                            if (ag.getGstNumber().isEmpty() || !charSequence.toString().equals(ag.getGstNumber()))
                                checkValidationFromApi(charSequence.toString(), 2);
                        }
                    } else if (udyogaadhaarCheckBox.isChecked()) {
                        if (storeid.getText().toString().isEmpty()) {
                            storeid.setError("Please enter store id first");
                            Toast.makeText(MyActivityEditFormActivity.this, "Please enter store id first", Toast.LENGTH_LONG).show();

                        } else if (charSequence.length() == 12) {
                            if (ag.getGstNumber().isEmpty() || !charSequence.toString().equals(ag.getGstNumber()))
                                checkValidationFromApi(charSequence.toString(), 4);
                        }
                    }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }

    private boolean isApiCalled;


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.icBack:
                onBackPressed();
                break;
            case R.id.imgCameraOutside:
                Integer outsidecount = 0;
                for (int i = 0; i < previousoutsideimage.size(); i++) {
                    if (previousoutsideimage.get(i).getDelstatus() == 0) {
                        outsidecount++;
                    }
                }
                if (outsidecount > 4) {
                    Toast.makeText(this, "you can not select more then 5 image", Toast.LENGTH_LONG).show();
                    return;
                }


                ImagePicker.Companion.with(this).cameraOnly().start(CAMERA_PIC_REQUEST_OUSIDE);


                break;
            case R.id.imgCameraActivity:
                Integer acivitycount = 0;
                for (int i = 0; i < previousacivityimage.size(); i++) {
                    if (previousacivityimage.get(i).getDelstatus() == 0) {
                        acivitycount++;
                    }
                }
                if (acivitycount > 4) {
                    Toast.makeText(this, "you can not select more then 5 image", Toast.LENGTH_LONG).show();
                    return;
                }


                ImagePicker.Companion.with(this).cameraOnly().start(CAMERA_PIC_REQUEST_ACTIVITY);


                break;
            case R.id.imgCameraInside:
                Integer insidecount = 0;
                for (int i = 0; i < previousinsidesideimage.size(); i++) {
                    if (previousinsidesideimage.get(i).getDelstatus() == 0) {
                        insidecount++;
                    }
                }
                if (insidecount > 4) {
                    Toast.makeText(this, "you can not select more then 5 image", Toast.LENGTH_LONG).show();
                    return;
                }


                ImagePicker.Companion.with(this).cameraOnly().start(CAMERA_PIC_REQUEST_INSIDE);


                break;
            case R.id.imgCameraGst:
                Intent cameraIntent3 = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent3, CAMERA_PIC_REQUEST_GST_PIC);
                break;
            case R.id.imgGalleryGst:
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(gallery, GALLERY_PIC_REQUEST_GST_PIC);
                break;
            case R.id.imgCameraPan:
                Intent cameraIntent4 = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent4, CAMERA_PIC_REQUEST_PAN_PIC);
                break;
            case R.id.imgGalleryPan:
                Intent gallery1 = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(gallery1, GALLERY_PIC_REQUEST_PAN_PIC);
                break;
            case R.id.btnYes:
                btnAddActivity.setVisibility(View.VISIBLE);
                break;
            case R.id.btnNo:
                btnAddActivity.setVisibility(View.GONE);
                break;
            case R.id.btnAddActivity:
                showMyActivtiyDialog();
                break;
            case R.id.btnSave:
                Utils.getPincode(MyActivityEditFormActivity.this, gpsTracker.getLatitude(), gpsTracker.getLongitude());

                String shopName = tvShopname.getText().toString().trim();
                String mobile = tvMobileno.getText().toString().trim();
                String shopCategory = selectShopCategory;
                String shopPincode = etPicode.getText().toString().trim();
                String shopGst = etGstNumber.getText().toString().trim();
                String shopPan = etPanNumber.getText().toString().trim();


                if (!otherActivityCheckBoxPan.isChecked()) {
                    shopPan = "";
                }


                if (shopName.isEmpty()) {
                    Toast.makeText(this, "Please enter shop name", Toast.LENGTH_LONG).show();
                    return;
                }

                if (mobile.isEmpty()) {
                    Toast.makeText(this, "Please enter mobile", Toast.LENGTH_LONG).show();
                    return;
                }
                if (storeid.getText().toString().trim().isEmpty()) {
                    Toast.makeText(this, "Please enter Storeid", Toast.LENGTH_LONG).show();
                    return;
                }
                if (shopPincode.isEmpty()) {
                    Toast.makeText(this, "Unable to fetch address from your GPS location, please try again.", Toast.LENGTH_LONG).show();
                    return;
                }

                Pattern r = Pattern.compile(pattern);
                if (!tvMobileno.getText().toString().isEmpty()) {
                    m = r.matcher(tvMobileno.getText().toString().trim());
                } else {
                    Toast.makeText(MyActivityEditFormActivity.this, "Please enter mobile number ", Toast.LENGTH_LONG).show();
                    return;
                }
                if (!m.find()) {
                    Toast.makeText(MyActivityEditFormActivity.this, "Please enter valid mobile number ", Toast.LENGTH_LONG).show();
                    return;
                }

                if (selectShopCategoryPosition == 0) {
                    Toast.makeText(this, "Please select shop category", Toast.LENGTH_LONG).show();
                    return;
                }


                if (otherActivityCheckBox.isChecked()) {
                    if (shopGst.isEmpty()) {
                        Toast.makeText(this, "Please enter shop gst number", Toast.LENGTH_LONG).show();
                        return;
                    }

                    try {
                        if (!Utils.validGSTIN(shopGst)) {
                            Toast.makeText(this, "GST number is not valid", Toast.LENGTH_LONG).show();
                            return;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                if (otherActivityCheckBoxPan.isChecked()) {
                    if (shopPan.isEmpty()) {
                        Toast.makeText(this, "Please enter shop pan number", Toast.LENGTH_LONG).show();
                        return;
                    }

                    try {
                        if (!Utils.validPan(shopPan)) {
                            Toast.makeText(this, "PAN number is not valid", Toast.LENGTH_LONG).show();
                            return;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }


                ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);


                List<MultipartBody.Part> outside_photo = new ArrayList<>();


                for (int i = 0; i < previousoutsideimage.size(); i++) {


                    if (previousoutsideimage.get(i).getStatus() == 1 && previousoutsideimage.get(i).getDelstatus() == 0) {
                        File image1 = previousoutsideimage.get(i).getFile();

                        outside_photo.add(MultipartBody.Part.createFormData("outside_photo[]", image1.getName(), RequestBody.create(MediaType.parse("image/*"), image1)));
                    }

                }
                List<MultipartBody.Part> inside_photo = new ArrayList<>();


                for (int i = 0; i < previousinsidesideimage.size(); i++) {


                    if (previousinsidesideimage.get(i).getStatus() == 1 && previousinsidesideimage.get(i).getDelstatus() == 0) {
                        File image2 = previousinsidesideimage.get(i).getFile();

                        inside_photo.add(MultipartBody.Part.createFormData("inside_photo[]", image2.getName(), RequestBody.create(MediaType.parse("image/*"), image2)));
                    }

                }
                List<MultipartBody.Part> activity_photo = new ArrayList<>();


                for (int i = 0; i < previousacivityimage.size(); i++) {


                    if (previousacivityimage.get(i).getStatus() == 1 && previousacivityimage.get(i).getDelstatus() == 0) {
                        File image3 = previousacivityimage.get(i).getFile();

                        activity_photo.add(MultipartBody.Part.createFormData("activity_photo[]", image3.getName(), RequestBody.create(MediaType.parse("image/*"), image3)));
                    }

                }


                MultipartBody.Part gstPhotoPhotoBodyPart = null;
                if (gstPicFile != null)
                    gstPhotoPhotoBodyPart = MultipartBody.Part.createFormData("gst_photo", gstPicFile.getName(), RequestBody.create(MediaType.parse("image/*"), gstPicFile));

                MultipartBody.Part panPhotoPhotoBodyPart = null;
                if (panPicFile != null)
                    panPhotoPhotoBodyPart = MultipartBody.Part.createFormData("pan_photo", panPicFile.getName(), RequestBody.create(MediaType.parse("image/*"), panPicFile));


                RequestBody shopNameBody = RequestBody.create(MediaType.parse("text/plain"), shopName);
                RequestBody storeidBody = RequestBody.create(MediaType.parse("text/plain"), storeid.getText().toString().trim());
                RequestBody mobileBody = RequestBody.create(MediaType.parse("text/plain"), mobile);
                RequestBody shopCategoryBody = RequestBody.create(MediaType.parse("text/plain"), shopCategory);
                RequestBody shopPinCodeBody = RequestBody.create(MediaType.parse("text/plain"), shopPincode);
                RequestBody shopGstBody = RequestBody.create(MediaType.parse("text/plain"), shopGst);
                RequestBody shopPanBody = RequestBody.create(MediaType.parse("text/plain"), shopPan);
                RequestBody latBody = RequestBody.create(MediaType.parse("text/plain"), gpsTracker.getLatitude() + "");
                RequestBody longtBody = RequestBody.create(MediaType.parse("text/plain"), gpsTracker.getLongitude() + "");
                RequestBody fosIdBody = RequestBody.create(MediaType.parse("text/plain"), mySingleton.getData(Constant.USER_ID));
                RequestBody activityIdBody = RequestBody.create(MediaType.parse("text/plain"), activityId + "");
                RequestBody address = RequestBody.create(MediaType.parse("text/plain"), Utils.addressR);
                RequestBody city = RequestBody.create(MediaType.parse("text/plain"), Utils.cityR);
                RequestBody state = RequestBody.create(MediaType.parse("text/plain"), Utils.stateR);
                RequestBody proof = RequestBody.create(MediaType.parse("text/plain"), gstvalue.toString());


                HashMap<String, RequestBody> map = new HashMap<>();
                map.put("shop_name", shopNameBody);
                map.put("mobile", mobileBody);
                map.put("shop_id", storeidBody);
                map.put("shop_category", shopCategoryBody);
                map.put("pincode", shopPinCodeBody);
                map.put("lat", latBody);
                map.put("long", longtBody);
                map.put("gst_number", shopGstBody);
                map.put("pan_number", shopPanBody);
                map.put("fos_id", fosIdBody);
                map.put("activity_detail_id", activityIdBody);
                map.put("address", address);
                map.put("city", city);
                map.put("state", state);
                map.put("proof_id", proof);


                List<MultipartBody.Part> previous_activity_photo = new ArrayList<>();


                for (int i = 0; i < acivityimage.size(); i++) {


                    ;

                    previous_activity_photo.add(MultipartBody.Part.createFormData("previousactivityimage[]", acivityimage.get(i).toString()));


                }

                List<MultipartBody.Part> previous_inside_photo = new ArrayList<>();


                for (int i = 0; i < insidesideimage.size(); i++) {


                    ;

                    previous_inside_photo.add(MultipartBody.Part.createFormData("previousinsideimage[]", insidesideimage.get(i).toString()));


                }

                List<MultipartBody.Part> previous_out_photo = new ArrayList<>();


                for (int i = 0; i < outsideimage.size(); i++) {


                    ;

                    previous_out_photo.add(MultipartBody.Part.createFormData("previousoutsideimage[]", outsideimage.get(i).toString()));


                }


                Call<ErrorBody> call = null;

                if (gstPicFile == null) {
                    call = apiService.updateMyActivityWithoutGst(map,
                            outside_photo, activity_photo, inside_photo, panPhotoPhotoBodyPart, previous_out_photo, previous_inside_photo, previous_activity_photo);
                } else {
                    call = apiService.updateMyActivity(map,
                            outside_photo, activity_photo, inside_photo, gstPhotoPhotoBodyPart, panPhotoPhotoBodyPart, previous_out_photo, previous_inside_photo, previous_activity_photo);
                }


                MyProgressDialog.show(this);
                call.enqueue(new Callback<ErrorBody>() {
                    @Override
                    public void onResponse(Call<ErrorBody> call, Response<ErrorBody> response) {
                        MyProgressDialog.dismissProgress();
                        if (response.isSuccessful()) {
                            if (response.code() >= 200 && response.code() < 700) {
                                if (response.code() == 200) {

                                    if (response.body().getDesc() != null) {
                                        displaySnackBar.DisplaySnackBar(response.body().getDesc().toString(), Constant.TYPE_SUCCESS);
                                        Toast.makeText(MyActivityEditFormActivity.this, "Activity resubmitted successfully", Toast.LENGTH_LONG).show();
                                        onBackPressed();
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

                                }
                            } catch (Exception e) {
                                displaySnackBar.DisplaySnackBar(Constant.ERROR_INVALID_JSON, Constant.TYPE_ERROR);

                                e.printStackTrace();
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<ErrorBody> call, Throwable t) {
                        MyProgressDialog.dismissProgress();
                        if (t != null && (t instanceof IOException || t instanceof SocketTimeoutException
                                || t instanceof ConnectException || t instanceof NoRouteToHostException
                                || t instanceof SecurityException)) {
                            displaySnackBar.DisplaySnackBar(Constant.NETWIRK_ERROR, Constant.TYPE_ERROR);

                        }
                    }
                });


                break;

        }

    }

    private File createTemporaryFile(String part, String ext) throws Exception {

        File tempDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        tempDir = new File(tempDir.getAbsolutePath() + "/rebliss/");
        if (!tempDir.exists()) {
            tempDir.mkdir();
        }
        return File.createTempFile(part, ext, tempDir);
    }

    public File grabImageFile(boolean compress, int quality) {
        File returnFile = null;
        try {

            if (mImageUri != null) {
                returnFile = new File(mImageUri.getPath());
                if (returnFile.exists() && compress) {
                    BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                    Bitmap bitmap = BitmapFactory.decodeFile(returnFile.getAbsolutePath(), bmOptions);
                    File compressedFile = createTemporaryFile("capture_compressed", ".jpg");
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, quality, bos);
                    byte[] bitmapdata = bos.toByteArray();
                    FileOutputStream fos = new FileOutputStream(compressedFile);
                    fos.write(bitmapdata);
                    fos.flush();
                    fos.close();
                    returnFile.delete();
                    returnFile = compressedFile;
                }
            }

        } catch (Exception e) {
            Log.e("Image Capture Error", e.getMessage());
        }
        return returnFile;
    }

    @Override
    protected void onResume() {
        super.onResume();
        gpsTracker = new GPSTracker(this);
        if (Build.VERSION.SDK_INT >= 23) {

            if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("permission", "Permission is granted");


            } else {
                Toast.makeText(this, "Permission revoked: CAMERA:, STORAGE: data from camera ", Toast.LENGTH_LONG).show();
            }
        }


    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, @Nullable final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (data != null && (resultCode == Activity.RESULT_OK)) {

                    if (requestCode == CAMERA_PIC_REQUEST_OUSIDE) {


                        File f = ImagePicker.Companion.getFile(data);
                        com.rebliss.domain.model.file.File file = new com.rebliss.domain.model.file.File(f, 1, null, 0, 0);
                        previousoutsideimage.add(file);

                        LinearLayoutManager linearLayoutManager4 = new LinearLayoutManager(MyActivityEditFormActivity.this, LinearLayoutManager.HORIZONTAL, true);
                        outsideimagelist.setLayoutManager(linearLayoutManager4);
                        FileListAdapter adapterlist4 = new FileListAdapter(MyActivityEditFormActivity.this, previousoutsideimage, 2);
                        outsideimagelist.setAdapter(adapterlist4);

                        etPicode.setText(Utils.getPincode(MyActivityEditFormActivity.this, gpsTracker.getLatitude(), gpsTracker.getLongitude()));
                        imgOutsideCheck.setVisibility(View.VISIBLE);


                    }

                    if (requestCode == CAMERA_PIC_REQUEST_ACTIVITY) {


                        File f = ImagePicker.Companion.getFile(data);
                        com.rebliss.domain.model.file.File file1 = new com.rebliss.domain.model.file.File(f, 1, null, 0, 0);
                        previousacivityimage.add(file1);

                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MyActivityEditFormActivity.this, LinearLayoutManager.HORIZONTAL, true);
                        acivityimagelist.setLayoutManager(linearLayoutManager);
                        FileListAdapter adapterlist = new FileListAdapter(MyActivityEditFormActivity.this, previousacivityimage, 1);
                        acivityimagelist.setAdapter(adapterlist);


                        etPicode.setText(Utils.getPincode(MyActivityEditFormActivity.this, gpsTracker.getLatitude(), gpsTracker.getLongitude()));
                        imgActivityCheck.setVisibility(View.VISIBLE);


                    }
                    if (requestCode == CAMERA_PIC_REQUEST_INSIDE) {


                        File f = ImagePicker.Companion.getFile(data);

                        com.rebliss.domain.model.file.File file112 = new com.rebliss.domain.model.file.File(f, 1, null, 0, 0);
                        previousinsidesideimage.add(file112);

                        LinearLayoutManager linearLayoutManager112 = new LinearLayoutManager(MyActivityEditFormActivity.this, LinearLayoutManager.HORIZONTAL, true);
                        insiteimagelist.setLayoutManager(linearLayoutManager112);
                        FileListAdapter adapterlist112 = new FileListAdapter(MyActivityEditFormActivity.this, previousinsidesideimage, 0);
                        insiteimagelist.setAdapter(adapterlist112);


                        imgInsideCheck.setVisibility(View.VISIBLE);
                        etPicode.setText(Utils.getPincode(MyActivityEditFormActivity.this, gpsTracker.getLatitude(), gpsTracker.getLongitude()));

                    }
                }
            }
        }, 0);


        if (data != null) {

            if (requestCode == CAMERA_PIC_REQUEST_GST_PIC) {
                Bitmap image = (Bitmap) data.getExtras().get("data");
                bitmapToFile(image, CAMERA_PIC_REQUEST_GST_PIC);


            }
            if (requestCode == GALLERY_PIC_REQUEST_GST_PIC) {


                Uri imageUri = data.getData();
                try {
                    Bitmap image = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                    bitmapToFile(image, CAMERA_PIC_REQUEST_GST_PIC);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (requestCode == CAMERA_PIC_REQUEST_PAN_PIC) {
                Bitmap image = (Bitmap) data.getExtras().get("data");
                bitmapToFile(image, CAMERA_PIC_REQUEST_PAN_PIC);


            }
            if (requestCode == GALLERY_PIC_REQUEST_PAN_PIC) {
                Uri imageUri = data.getData();
                try {
                    Bitmap image = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                    bitmapToFile(image, CAMERA_PIC_REQUEST_PAN_PIC);
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        }

    }


    File outSideFile = null, activityFile = null, insideFile = null, gstPicFile = null, panPicFile = null;

    private void bitmapToFile(Bitmap yourBitmap, int reqCode) {
        String filename = Environment.getExternalStorageDirectory() + File.separator + Calendar.getInstance().getTimeInMillis() +
                "temporary_file.png";


        File f = new File(filename);
        try {
            f.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }


        Bitmap bitmap = yourBitmap;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
        byte[] bitmapdata = bos.toByteArray();


        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(f);

            fos.write(bitmapdata);

            String pincode = Utils.getPincode(MyActivityEditFormActivity.this, gpsTracker.getLatitude(), gpsTracker.getLongitude());

            switch (reqCode) {
                case 101:
                    outSideFile = f;


                    etPicode.setText(Utils.getPincode(this, gpsTracker.getLatitude(), gpsTracker.getLongitude()));
                    imgOutsideCheck.setVisibility(View.VISIBLE);


                    if (!pincode.isEmpty()) {
                        etPicode.setText(pincode);
                        etPicode.setFocusable(false);
                        etPicode.setFocusableInTouchMode(false);
                    }


                    com.rebliss.domain.model.file.File file = new com.rebliss.domain.model.file.File(f, 1, null, 0, 0);
                    previousoutsideimage.add(file);

                    LinearLayoutManager linearLayoutManager4 = new LinearLayoutManager(MyActivityEditFormActivity.this, LinearLayoutManager.HORIZONTAL, true);
                    outsideimagelist.setLayoutManager(linearLayoutManager4);
                    FileListAdapter adapterlist4 = new FileListAdapter(MyActivityEditFormActivity.this, previousoutsideimage, 2);
                    outsideimagelist.setAdapter(adapterlist4);


                    break;
                case 102:


                    imgActivityCheck.setVisibility(View.VISIBLE);
                    etPicode.setText(Utils.getPincode(MyActivityEditFormActivity.this, gpsTracker.getLatitude(), gpsTracker.getLongitude()));
                    if (!pincode.isEmpty()) {
                        etPicode.setText(pincode);
                        etPicode.setFocusable(false);
                        etPicode.setFocusableInTouchMode(false);
                    }


                    com.rebliss.domain.model.file.File file1 = new com.rebliss.domain.model.file.File(f, 1, null, 0, 0);
                    previousacivityimage.add(file1);

                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MyActivityEditFormActivity.this, LinearLayoutManager.HORIZONTAL, true);
                    acivityimagelist.setLayoutManager(linearLayoutManager);
                    FileListAdapter adapterlist = new FileListAdapter(MyActivityEditFormActivity.this, previousacivityimage, 1);
                    acivityimagelist.setAdapter(adapterlist);


                    break;
                case 103:


                    imgInsideCheck.setVisibility(View.VISIBLE);
                    etPicode.setText(Utils.getPincode(MyActivityEditFormActivity.this, gpsTracker.getLatitude(), gpsTracker.getLongitude()));
                    if (!pincode.isEmpty()) {
                        etPicode.setText(pincode);
                        etPicode.setFocusable(false);
                        etPicode.setFocusableInTouchMode(false);
                    }

                    Toast.makeText(this, "before add image data : " + new Gson().toJson(previousinsidesideimage) + " " + previousinsidesideimage.size(), Toast.LENGTH_LONG).show();

                    com.rebliss.domain.model.file.File file112 = new com.rebliss.domain.model.file.File(f, 1, null, 0, 0);
                    previousinsidesideimage.add(file112);

                    LinearLayoutManager linearLayoutManager112 = new LinearLayoutManager(MyActivityEditFormActivity.this, LinearLayoutManager.HORIZONTAL, true);
                    insiteimagelist.setLayoutManager(linearLayoutManager112);
                    FileListAdapter adapterlist112 = new FileListAdapter(MyActivityEditFormActivity.this, previousinsidesideimage, 0);
                    insiteimagelist.setAdapter(adapterlist112);


                    break;
                case 104:
                    gstPicFile = f;
                    btnUploadGstPic.setText(gstPicFile.getName());
                    break;
                case 106:
                    panPicFile = f;
                    btnUploadPanPic.setText(panPicFile.getName());
                    break;
            }

            fos.flush();
            fos.close();

        } catch (Exception e) {
            e.printStackTrace();
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
                ActivityCompat.requestPermissions(MyActivityEditFormActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                return false;
            }

        } else {
            Log.v("permission", "Permission is granted");
            return true;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        gpsTracker = new GPSTracker(MyActivityEditFormActivity.this);


    }


    private void showMyActivtiyDialog() {

        final Dialog dialog = new Dialog(Objects.requireNonNull(MyActivityEditFormActivity.this));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_select_my_activity);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();

        spinCat = dialog.findViewById(R.id.spinCat);
        spinSubCat = dialog.findViewById(R.id.spinSubCat);
        spinSubCat2 = dialog.findViewById(R.id.spinSubCat2);

        getCategory(spinCat, 1, 0);

        Button btnSubmit = dialog.findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (catSelected == null || subCatSelected == null || subCat1Selected == null) {
                    Toast.makeText(MyActivityEditFormActivity.this, "Please select all", Toast.LENGTH_LONG).show();
                    return;
                }

                if (Utils.checkActivityDuplicate(subCat1Selected)) {
                    Toast.makeText(MyActivityEditFormActivity.this, "You can't select duplicat activity", Toast.LENGTH_LONG).show();
                    return;
                }

                ActivitySelectModel selectModel = new ActivitySelectModel();
                selectModel.setCategory(catSelected);
                selectModel.setSubCategory(subCatSelected);
                selectModel.setSubCategory1(subCat1Selected);

                Constant.activitiesList.add(selectModel);

                Toast.makeText(MyActivityEditFormActivity.this, "Activity " + Constant.activitiesList.size() + " is added successfully.", Toast.LENGTH_LONG).show();

                MyFormActivityListShowAdapter myFormActivityListShowAdapter = new MyFormActivityListShowAdapter(MyActivityEditFormActivity.this, Constant.activitiesList, 1);
                rvActivity.setAdapter(myFormActivityListShowAdapter);
                dialog.dismiss();


            }
        });

        spinCat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {

                if (pos != 0) {
                    catSelected = catList.get(pos - 1).getCategoryId();
                    getCategory(spinSubCat, 2, catList.get(pos - 1).getCategoryId());
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinSubCat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {

                if (pos != 0) {
                    subCatSelected = subCatList.get(pos - 1).getCategoryId();
                    getCategory(spinSubCat2, 3, subCatList.get(pos - 1).getCategoryId());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinSubCat2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {

                if (pos != 0) {
                    subCat1Selected = subCat1List.get(pos - 1).getCategoryId();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }


    Spinner spinCat, spinSubCat, spinSubCat2;
    List<AllCategory> catList, subCatList, subCat1List;
    Integer catSelected, subCatSelected, subCat1Selected;

    public void getCategory(final Spinner spinCat, final int position, int categoryId) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<CategoryResponse> call = null;
        if (position == 1)
            call = apiService.getCategory();
        else if (position == 2)
            call = apiService.getSubCategory(categoryId);
        else if (position == 3)
            call = apiService.getSubCategory(categoryId);

        call.enqueue(new Callback<CategoryResponse>() {
            @Override
            public void onResponse(Call<CategoryResponse> call, Response<CategoryResponse> response) {

                if (response.isSuccessful()) {
                    if (response.code() >= 200 && response.code() < 700) {
                        if (response.code() == 200) {
                            if (response.body().getStatus() == 1) {
                                if (response.body().getData() != null) {
                                    Gson gson = new Gson();
                                    String json = gson.toJson(response.body(), CategoryResponse.class);
                                    Log.i("TAG", "json " + json);


                                    if (position == 1) {
                                        ArrayList<String> categoryArray = new ArrayList<>();
                                        categoryArray.add("Select Category");
                                        catList = response.body().getData().getAllCategory();
                                        for (AllCategory datum : response.body().getData().getAllCategory()) {
                                            categoryArray.add(datum.getCategoryName());
                                        }

                                        ArrayAdapter aa = new ArrayAdapter(MyActivityEditFormActivity.this, android.R.layout.simple_spinner_item, categoryArray);
                                        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                                        spinCat.setAdapter(aa);
                                    } else if (position == 2) {
                                        ArrayList<String> categoryArray = new ArrayList<>();
                                        categoryArray.add("Select Sub category");

                                        subCatList = response.body().getData().getAllGroups();
                                        for (AllCategory datum : response.body().getData().getAllGroups()) {
                                            categoryArray.add(datum.getCategoryName());
                                        }

                                        ArrayAdapter aa = new ArrayAdapter(MyActivityEditFormActivity.this, android.R.layout.simple_spinner_item, categoryArray);
                                        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                                        spinCat.setAdapter(aa);
                                    } else if (position == 3) {
                                        ArrayList<String> categoryArray = new ArrayList<>();
                                        categoryArray.add("Select Sub category1");
                                        subCat1List = response.body().getData().getAllGroups();
                                        for (AllCategory datum : response.body().getData().getAllGroups()) {
                                            categoryArray.add(datum.getCategoryName());
                                        }

                                        ArrayAdapter aa = new ArrayAdapter(MyActivityEditFormActivity.this, android.R.layout.simple_spinner_item, categoryArray);
                                        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                                        spinCat.setAdapter(aa);
                                    }


                                } else {
                                    displaySnackBar.DisplaySnackBar("Data not found", Constant.TYPE_ERROR);

                                }
                            } else if (response.body().getStatus() == 0) {

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

                        }
                    } catch (Exception e) {
                        displaySnackBar.DisplaySnackBar(Constant.ERROR_INVALID_JSON, Constant.TYPE_ERROR);

                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<CategoryResponse> call, Throwable t) {

                if (t != null && (t instanceof IOException || t instanceof SocketTimeoutException
                        || t instanceof ConnectException || t instanceof NoRouteToHostException
                        || t instanceof SecurityException)) {
                    displaySnackBar.DisplaySnackBar(Constant.NETWIRK_ERROR, Constant.TYPE_ERROR);

                }
            }
        });
    }

    private List<AllShop> shopCatList;
    ArrayList<String> categoryArray = new ArrayList<>();

    public void getShopCategory(final String category) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<ShopCategoryResponse>
                call = apiService.getShopCategories();

        call.enqueue(new Callback<ShopCategoryResponse>() {
            @Override
            public void onResponse(Call<ShopCategoryResponse> call, Response<ShopCategoryResponse> response) {

                if (response.isSuccessful()) {
                    if (response.code() >= 200 && response.code() < 700) {
                        if (response.code() == 200) {
                            if (response.body().getStatus() == 1) {
                                if (response.body().getData() != null) {


                                    categoryArray.add("Select Shop Category");
                                    shopCatList = response.body().getData().getAllShops();
                                    for (AllShop datum : shopCatList) {
                                        categoryArray.add(datum.getValue());
                                    }

                                    ArrayAdapter<String> adapter = new ArrayAdapter<String>
                                            (MyActivityEditFormActivity.this, android.R.layout.select_dialog_item, categoryArray);

                                    ArrayAdapter stateAdapter = new ArrayAdapter(getApplicationContext(), R.layout.spinner_item, categoryArray);
                                    spinShopcategory.setAdapter(stateAdapter);


                                    int position = categoryArray.indexOf(category);
                                    spinShopcategory.setSelection(position);


                                } else {
                                    displaySnackBar.DisplaySnackBar("Data not found", Constant.TYPE_ERROR);

                                }
                            } else if (response.body().getStatus() == 0) {

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

                        }
                    } catch (Exception e) {
                        displaySnackBar.DisplaySnackBar(Constant.ERROR_INVALID_JSON, Constant.TYPE_ERROR);

                        e.printStackTrace();
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

    public boolean islocationPermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("permission", "Permission is granted");
                return true;
            } else {

                Log.v("permission", "Permission is revoked");
                ActivityCompat.requestPermissions(MyActivityEditFormActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CAMERA}, 1);
                return false;
            }
        } else {
            Log.v("permission", "Permission is granted");
            return true;
        }
    }


    public void checkValidationFromApi(String mobile, final Integer type) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<CategoryResponse>
                call = apiService.checkDuplicasy(storeid.getText().toString().trim(), mobile, Constant.activitiesList.get(0).getCategory().toString(), Constant.activitiesList.get(0).getSubCategory1().toString(), type);

        call.enqueue(new Callback<CategoryResponse>() {
            @Override
            public void onResponse(Call<CategoryResponse> call, Response<CategoryResponse> response) {

                if (response.isSuccessful()) {
                    if (response.code() >= 200 && response.code() < 700) {
                        if (response.code() == 200) {
                            if (response.body().getStatus() == 0) {

                                if (type == 1) {

                                    Toast.makeText(MyActivityEditFormActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                                }
                                if (type == 2 || type == 4) {

                                    Toast.makeText(MyActivityEditFormActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                                }
                                if (type == 3) {

                                    Toast.makeText(MyActivityEditFormActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                                }


                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {

                                        if (type == 1) {
                                            tvMobileno.setText("");
                                        }
                                        if (type == 2 || type == 4) {
                                            etGstNumber.setText("");
                                        }
                                        if (type == 3) {
                                            etPanNumber.setText("");
                                        }

                                    }
                                }, 200);
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

                        }
                    } catch (Exception e) {
                        displaySnackBar.DisplaySnackBar(Constant.ERROR_INVALID_JSON, Constant.TYPE_ERROR);

                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<CategoryResponse> call, Throwable t) {

                if (t != null && (t instanceof IOException || t instanceof SocketTimeoutException
                        || t instanceof ConnectException || t instanceof NoRouteToHostException
                        || t instanceof SecurityException)) {
                    displaySnackBar.DisplaySnackBar(Constant.NETWIRK_ERROR, Constant.TYPE_ERROR);

                }
            }
        });
    }

}
