package com.rebliss.view.activity.myactivityadd;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.KeyguardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
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
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.rebliss.BuildConfig;
import com.rebliss.utils.GPSTracker;
import com.rebliss.R;
import com.rebliss.data.perf.MySingleton;
import com.rebliss.domain.constant.Constant;
import com.rebliss.domain.model.ActivitySelectModel;
import com.rebliss.domain.model.ErrorBody;
import com.rebliss.domain.model.categoryresponse.AllCategory;
import com.rebliss.domain.model.categoryresponse.CategoryResponse;
import com.rebliss.domain.model.payment.SuccessResponse;
import com.rebliss.domain.model.shopcategory.AllShop;
import com.rebliss.domain.model.shopcategory.ShopCategoryResponse;
import com.rebliss.presenter.helper.DisplaySnackBar;
import com.rebliss.presenter.retrofit.ApiClient;
import com.rebliss.presenter.retrofit.ApiInterface;
import com.rebliss.utils.MyProgressDialog;
import com.rebliss.utils.Utils;
import com.rebliss.view.activity.BaseActivity;
import com.rebliss.view.adapter.MyFormActivityListShowAdapter;
import com.rebliss.view.adapter.ShowListAdapter;

import org.jetbrains.annotations.NotNull;

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

import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyActivityFormActivity extends BaseActivity implements View.OnClickListener {
    private EditText tvShopname, storeid;
    private EditText tvMobileno;
    private TextView btnInsidePhoto;
    private ImageView imgCameraInside;
    private TextView btnOutsidePhoto;
    private ImageView imgCameraOutside;
    private Integer gstvalue = 0;
    private TextView btnActivityPhoto;
    private ImageView imgCameraActivity;
    private TextView etPicode;
    private CheckBox otherActivityCheckBox, udyogaadhaarCheckBox, fssaiCheckBox, tradelicenseCheckBox;
    private EditText etGstNumber;
    private String name;
    private TextView btnUploadGstPic, btnUploadudyogaadhaarPic;
    private ImageView imgCameraGst;
    private ImageView imgGalleryGst;
    private RelativeLayout gstContainer;
    private CheckBox otherActivityCheckBoxPan;
    private EditText etPanNumber;
    private TextView btnUploadPanPic;
    private ImageView imgCameraPan;
    private ImageView imgGalleryPan;
    private RelativeLayout panContainer;
    private TextView btnYes;
    private TextView btnNo;
    private Uri mImageUri;
    private Button btnAddActivity;
    private RecyclerView rvActivity;
    private RecyclerView insiteimagelist, outsideimagelist, acivityimagelist;
    private Button btnSave;
    private DisplaySnackBar displaySnackBar;
    private MySingleton mySingleton;
    private ImageView icBack;
    File outSideFile = null, activityFile = null, insideFile = null, gstPicFile = null, panPicFile = null;
    public ArrayList<File> outsideimage = new ArrayList<File>();
    public ArrayList<File> insidesideimage = new ArrayList<File>();
    public ArrayList<File> acivityimage = new ArrayList<File>();

    private ImageView imgInsideCheck, imgOutsideCheck, imgActivityCheck;

    private GPSTracker gpsTracker;

    String pattern = "^\\s*(?:\\+?(\\d{1,3}))?[-. (]*(\\d{3})[-. )]*(\\d{3})[-. ]*(\\d{4})(?: *x(\\d+))?\\s*$";
    Matcher m;

    private AppCompatSpinner spinShopcategory;
    String selectShopCategory = "";
    int selectShopCategoryPosition = 0;

    MyFormActivityListShowAdapter myFormActivityListShowAdapter;

    Toast toast;
    String path = "";
    String nbase64 = "";
    String currentPhotoPath = "";
    String fileAbsolutePath = "";
    int pos = 1;
    private KProgressHUD kProgressHUD;


    public File getActivityFile() {
        return activityFile;
    }

    private int CAMERA_PIC_REQUEST_OUSIDE = 101,
            CAMERA_PIC_REQUEST_ACTIVITY = 102,
            CAMERA_PIC_REQUEST_INSIDE = 103,
            CAMERA_PIC_REQUEST_GST_PIC = 104,
            GALLERY_PIC_REQUEST_GST_PIC = 105,
            CAMERA_PIC_REQUEST_PAN_PIC = 106,
            GALLERY_PIC_REQUEST_PAN_PIC = 107;

    Uri photoURI;
    File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_form);

        initView();
        if (!gpsTracker.canGetLocation()) {
            gpsTracker.showSettingsAlert();
        }
        isStoragePermissionGranted();
        permission();
        KeyguardManager keyguardManager = (KeyguardManager) getSystemService(Activity.KEYGUARD_SERVICE);
        KeyguardManager.KeyguardLock lock = keyguardManager.newKeyguardLock(KEYGUARD_SERVICE);
        lock.disableKeyguard();

        listener();
        acivityimage.clear();
        mImageUri = null;
        insidesideimage.clear();
        outsideimage.clear();

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);

    }

    public void permission() {
        if (ContextCompat.checkSelfPermission(MyActivityFormActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MyActivityFormActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            if (ActivityCompat.shouldShowRequestPermissionRationale(MyActivityFormActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(MyActivityFormActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            }
        }
    }

    private void initView() {
        kProgressHUD = new KProgressHUD(this);
        mySingleton = new MySingleton(this);
        gpsTracker = new GPSTracker(this);
        displaySnackBar = new DisplaySnackBar(this);
        tvShopname = findViewById(R.id.tv_shopname);
        tvMobileno = findViewById(R.id.tv_mobileno);
        spinShopcategory = findViewById(R.id.tv_shopcategory);
        btnInsidePhoto = findViewById(R.id.btnInsidePhoto);
        imgCameraInside = findViewById(R.id.imgCameraInside);
        btnOutsidePhoto = findViewById(R.id.btnOutsidePhoto);
        imgCameraOutside = findViewById(R.id.imgCameraOutside);
        btnActivityPhoto = findViewById(R.id.btnActivityPhoto);
        imgCameraActivity = findViewById(R.id.imgCameraActivity);
        etPicode = findViewById(R.id.etPicode);
        storeid = findViewById(R.id.tv_storeid);
        insiteimagelist = findViewById(R.id.tv_insiterecyler);
        outsideimagelist = findViewById(R.id.tv_outsiderecyler);
        acivityimagelist = findViewById(R.id.tv_acivityrecyler);
        otherActivityCheckBox = findViewById(R.id.otherActivityCheckBox);
        udyogaadhaarCheckBox = findViewById(R.id.udyogaadhaarCheckBox);
        tradelicenseCheckBox = findViewById(R.id.tradelicenseCheckBox);
        fssaiCheckBox = findViewById(R.id.fssaiCheckBox);
        otherActivityCheckBoxPan = findViewById(R.id.otherActivityCheckBoxPan);
        etGstNumber = findViewById(R.id.etGstNumber);
        btnUploadGstPic = findViewById(R.id.tvGstFileName);
        imgCameraGst = findViewById(R.id.imgCameraGst);
        imgGalleryGst = findViewById(R.id.imgGalleryGst);
        gstContainer = findViewById(R.id.gstContainer);

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
        imgInsideCheck = findViewById(R.id.imgInsideCheck);
        imgOutsideCheck = findViewById(R.id.imgOutsideCheck);
        imgActivityCheck = findViewById(R.id.imgAcivityCheck);
        icBack = findViewById(R.id.icBack);

        getShopCategory();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvActivity.setLayoutManager(linearLayoutManager);
        myFormActivityListShowAdapter = new MyFormActivityListShowAdapter(this, Constant.activitiesList, 0);
        rvActivity.setAdapter(myFormActivityListShowAdapter);

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
                Log.d("CAT", selectShopCategory.toString());

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        otherActivityCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (b) {
                    gstvalue = 1;
                    name = "GST";
                    etGstNumber.setText("");
                    etGstNumber.setHint("Enter GST");
                    udyogaadhaarCheckBox.setChecked(false);
                    fssaiCheckBox.setChecked(false);
                    tradelicenseCheckBox.setChecked(false);

                    gstContainer.setVisibility(View.VISIBLE);
                    btnUploadGstPic.setVisibility(View.VISIBLE);

                    gstPicFile = null;
                    btnUploadGstPic.setText("");
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
                    etGstNumber.setText("");
                    etGstNumber.setHint("Enter  Udyog Aadhaar");
                    otherActivityCheckBox.setChecked(false);
                    fssaiCheckBox.setChecked(false);
                    tradelicenseCheckBox.setChecked(false);
                    gstContainer.setVisibility(View.VISIBLE);
                    btnUploadGstPic.setVisibility(View.VISIBLE);
                    gstPicFile = null;
                    btnUploadGstPic.setText("");
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
                    etGstNumber.setText("");
                    etGstNumber.setHint("Enter FSSAI");
                    udyogaadhaarCheckBox.setChecked(false);
//                udyogaadhaarCheckBox.setSelected(false);
                    otherActivityCheckBox.setChecked(false);
//                otherActivityCheckBox.setSelected(false);
                    tradelicenseCheckBox.setChecked(false);
//                tradelicenseCheckBox.setSelected(false)
                    gstContainer.setVisibility(View.VISIBLE);
                    btnUploadGstPic.setVisibility(View.VISIBLE);

                    gstPicFile = null;
                    btnUploadGstPic.setText("");
                } else {
                    gstContainer.setVisibility(View.GONE);
                    btnUploadGstPic.setVisibility(View.GONE);
                }
            }
        });
        tradelicenseCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                ;
                if (b) {
                    name = "Trade License";
                    gstvalue = 3;
                    etGstNumber.setText("");
                    etGstNumber.setHint("Enter Trade License");
                    udyogaadhaarCheckBox.setChecked(false);
//                udyogaadhaarCheckBox.setSelected(false);
                    fssaiCheckBox.setChecked(false);
//                fssaiCheckBox.setSelected(false);
                    otherActivityCheckBox.setChecked(false);
//                otherActivityCheckBox.setSelected(false)
                    gstContainer.setVisibility(View.VISIBLE);
                    btnUploadGstPic.setVisibility(View.VISIBLE);

                    gstPicFile = null;
                    btnUploadGstPic.setText("");
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
                    AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
                            MyActivityFormActivity.this);
                    alertDialog2.setTitle("Confirm!");
                    alertDialog2.setCancelable(false);
                    alertDialog2.setMessage("Are you sure that the merchant has no other document?");
                    alertDialog2.setPositiveButton("YES",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Write your code here to execute after dialog

                                    panContainer.setVisibility(View.VISIBLE);
                                    btnUploadPanPic.setVisibility(View.VISIBLE);

                                    dialog.cancel();
                                }
                            });
                    alertDialog2.setNegativeButton("NO",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    otherActivityCheckBoxPan.setChecked(false);
                                    dialog.cancel();
                                }
                            });
                    alertDialog2.show();
                } else {
                    panContainer.setVisibility(View.GONE);
                    btnUploadPanPic.setVisibility(View.GONE);
                }


            }
        });

        tvMobileno.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (!charSequence.toString().isEmpty())
                    if (storeid.getText().toString().isEmpty()) {
                        storeid.setError("Please enter store id first");
                        ToastForSpecificTime("Please enter store id first");
                        tvMobileno.setText("");
                    } else if (charSequence.length() == 10) {
                        checkValidationFromApi(charSequence.toString(), 1, Constant.activitiesList.get(0).getCategory().toString(), Constant.activitiesList.get(0).getSubCategory1().toString());
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
                if (!charSequence.toString().isEmpty())
                    if (otherActivityCheckBoxPan.isChecked()) {
                        if (storeid.getText().toString().isEmpty()) {
                            storeid.setError("Please enter store id first");

                            ToastForSpecificTime("Please enter store id first");
                            //Toast.makeText(MyActivityFormActivity.this, "Please enter store id first", Toast.LENGTH_LONG).show();
                            etPanNumber.setText("");

                        } else if (charSequence.length() == 10) {
                            try {
                                checkValidationFromApi(charSequence.toString(), 3, Constant.activitiesList.get(0).getCategory().toString(), Constant.activitiesList.get(0).getSubCategory1().toString());

                            } catch (IndexOutOfBoundsException e) {
                                Log.e("TAG", "onTextChanged: " + e.getMessage());
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

                if (!charSequence.toString().isEmpty())
                    if (otherActivityCheckBox.isChecked()) {
                        if (storeid.getText().toString().isEmpty()) {
                            storeid.setError("Please enter store id first");
                            ToastForSpecificTime("Please enter store id first");
                            //  Toast.makeText(MyActivityFormActivity.this, "Please enter store id first", Toast.LENGTH_LONG).show();
                            etGstNumber.setText("");

                        } else if (charSequence.length() == 15) {
                            checkValidationFromApi(charSequence.toString(), 2, Constant.activitiesList.get(0).getCategory().toString(), Constant.activitiesList.get(0).getSubCategory1().toString());
                        }
                    } else if (udyogaadhaarCheckBox.isChecked()) {
                        if (storeid.getText().toString().isEmpty()) {
                            storeid.setError("Please enter store id first");
                            ToastForSpecificTime("Please enter store id first");
                            // Toast.makeText(MyActivityFormActivity.this, "Please enter store id first", Toast.LENGTH_LONG).show();
                            etGstNumber.setText("");
                        } else if (charSequence.length() == 12) {
                            checkValidationFromApi(charSequence.toString(), 4, Constant.activitiesList.get(0).getCategory().toString(), Constant.activitiesList.get(0).getSubCategory1().toString());
                        }
                    }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.icBack:
                onBackPressed();
                break;
            case R.id.imgCameraOutside:
                if (outsideimage.size() > 4) {
                    Log.e("TAG", "onClick: 1");
                    Toast.makeText(this, "you can not select more then 5 image", Toast.LENGTH_LONG).show();
                    return;
                }
                try {
                    Log.e("TAG", "onClick: 4");
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = getImageFile();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        photoURI = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID.concat(".provider"), f);
                    } else {
                        photoURI = Uri.fromFile(f);
                    }
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    setResult(RESULT_OK, null);
                    startActivityForResult(intent, CAMERA_PIC_REQUEST_OUSIDE);

                } catch (Exception e) {
                    e.printStackTrace();
                }


                break;
            case R.id.imgCameraActivity:

                if (acivityimage.size() > 4) {
                    Toast.makeText(this, "you can not select more then 5 image", Toast.LENGTH_LONG).show();
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
                    startActivityForResult(intent, CAMERA_PIC_REQUEST_ACTIVITY);

                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
            case R.id.imgCameraInside:
                if (insidesideimage.size() > 4) {
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
                    startActivityForResult(intent, CAMERA_PIC_REQUEST_INSIDE);

                } catch (Exception e) {
                    e.printStackTrace();
                }


                break;
            case R.id.imgCameraGst:
//                Intent cameraIntent3 = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//                startActivityForResult(cameraIntent3, CAMERA_PIC_REQUEST_GST_PIC);

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
                    startActivityForResult(intent, CAMERA_PIC_REQUEST_GST_PIC);

                } catch (Exception e) {
                    e.printStackTrace();
                }

                //  dispatchTakePictureIntentReal(CAMERA_PIC_REQUEST_GST_PIC);
                break;
            case R.id.imgGalleryGst:
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(gallery, GALLERY_PIC_REQUEST_GST_PIC);
                break;
            case R.id.imgCameraPan:
//                Intent cameraIntent4 = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//                startActivityForResult(cameraIntent4, CAMERA_PIC_REQUEST_PAN_PIC);
                //  dispatchTakePictureIntentReal(CAMERA_PIC_REQUEST_PAN_PIC);
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
                    startActivityForResult(intent, CAMERA_PIC_REQUEST_PAN_PIC);

                } catch (Exception e) {
                    e.printStackTrace();
                }

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


                String shopName = tvShopname.getText().toString().trim();
                String mobile = tvMobileno.getText().toString().trim();
                String shopCategory = selectShopCategory;
                String shopPincode = etPicode.getText().toString().trim();
                String shopGst = etGstNumber.getText().toString().trim();
                String shopPan = etPanNumber.getText().toString().trim();


                if (shopName.isEmpty()) {
                    Toast.makeText(this, "Please enter shop name", Toast.LENGTH_LONG).show();
                    return;
                }

                if (storeid.getText().toString().trim().isEmpty()) {
                    Toast.makeText(MyActivityFormActivity.this, "Please enter Store Id ", Toast.LENGTH_LONG).show();
                    return;
                }


                if (mobile.isEmpty()) {
                    Toast.makeText(this, "Please enter mobile number", Toast.LENGTH_LONG).show();
                    return;
                }
                if (mobile.toString().length() != 10) {
                    Toast.makeText(this, "Please enter vaild number", Toast.LENGTH_LONG).show();
                    return;
                }

                Pattern r = Pattern.compile(pattern);
                if (!tvMobileno.getText().toString().isEmpty()) {
                    m = r.matcher(tvMobileno.getText().toString().trim());
                } else {
                    Toast.makeText(MyActivityFormActivity.this, "Please enter mobile number ", Toast.LENGTH_LONG).show();
                    return;
                }
                if (!m.find()) {
                    Toast.makeText(MyActivityFormActivity.this, "Please enter valid mobile number ", Toast.LENGTH_LONG).show();
                    return;
                }


                if (selectShopCategoryPosition == 0) {
                    Toast.makeText(this, "Please select shop category", Toast.LENGTH_LONG).show();
                    return;
                }


                if (insidesideimage.size() == 0) {
                    Toast.makeText(this, "Please upload shop inside photo", Toast.LENGTH_LONG).show();
                    return;
                }
                if (outsideimage.size() == 0) {
                    Toast.makeText(this, "Please upload shop outside photo", Toast.LENGTH_LONG).show();
                    return;
                }
                if (acivityimage.size() == 0) {
                    Toast.makeText(this, "Please upload shop acvtivity photo", Toast.LENGTH_LONG).show();
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


                    if (gstPicFile == null) {
                        Toast.makeText(this, "Please capture gst picture", Toast.LENGTH_LONG).show();
                        return;
                    }

                }
                if (udyogaadhaarCheckBox.isChecked()) {
                    if (shopGst.isEmpty()) {
                        Toast.makeText(this, "Please enter shop  Udyog Aadhaar number", Toast.LENGTH_LONG).show();
                        return;
                    }


                    if (gstPicFile == null) {
                        Toast.makeText(this, "Please capture  Udyog Aadhaar picture", Toast.LENGTH_LONG).show();
                        return;
                    }

                }
                if (fssaiCheckBox.isChecked()) {
                    if (shopGst.isEmpty()) {
                        Toast.makeText(this, "Please enter shop  FSSAI number", Toast.LENGTH_LONG).show();
                        return;
                    }


                    if (gstPicFile == null) {
                        Toast.makeText(this, "Please capture  FSSAI picture", Toast.LENGTH_LONG).show();
                        return;
                    }

                }
                if (tradelicenseCheckBox.isChecked()) {
                    if (shopGst.isEmpty()) {
                        Toast.makeText(this, "Please enter shop  Trade License number", Toast.LENGTH_LONG).show();
                        return;
                    }


                    if (gstPicFile == null) {
                        Toast.makeText(this, "Please capture  Trade License picture", Toast.LENGTH_LONG).show();
                        return;
                    }

                }

//                if (gstPicFile == null) {
//                    Toast.makeText(this, "Please capture gst and other data", Toast.LENGTH_LONG).show();
//                    return;
//                }


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


                    if (panPicFile == null) {
                        Toast.makeText(this, "Please capture pan picture", Toast.LENGTH_LONG).show();
                        return;
                    }

                }
                if (shopPincode.isEmpty()) {
                    Toast.makeText(this, "Unable fetch address from your GPS location, please try again.", Toast.LENGTH_LONG).show();
                    return;
                }


                if (Constant.activitiesList.size() == 0) {
                    Toast.makeText(this, "Please add Activity", Toast.LENGTH_LONG).show();
                    return;
                }


                ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
                MultipartBody.Part gstPhotoPhotoBodyPart = null;
                if (gstPicFile != null) {
                    gstPhotoPhotoBodyPart = MultipartBody.Part.createFormData("gst_photo", gstPicFile.getName(), RequestBody.create(MediaType.parse("image/*"), gstPicFile));
                } else {
                    gstPhotoPhotoBodyPart = MultipartBody.Part.createFormData("gst_photo", "", RequestBody.create(MediaType.parse("text/plain"), ""));
                }

                MultipartBody.Part panPhotoPhotoBodyPart = null;
                if (panPicFile != null)
                    panPhotoPhotoBodyPart = MultipartBody.Part.createFormData("pan_photo", panPicFile.getName(), RequestBody.create(MediaType.parse("image/*"), panPicFile));


// create a map of data to pass along
                RequestBody shopNameBody = RequestBody.create(MediaType.parse("text/plain"), shopName);
                RequestBody storeidBody = RequestBody.create(MediaType.parse("text/plain"), storeid.getText().toString());
                RequestBody mobileBody = RequestBody.create(MediaType.parse("text/plain"), mobile);
                RequestBody shopCategoryBody = RequestBody.create(MediaType.parse("text/plain"), shopCategory);
                RequestBody shopPinCodeBody = RequestBody.create(MediaType.parse("text/plain"), shopPincode);
                RequestBody shopGstBody = RequestBody.create(MediaType.parse("text/plain"), shopGst);
                RequestBody shopPanBody = RequestBody.create(MediaType.parse("text/plain"), shopPan);
                RequestBody latBody = RequestBody.create(MediaType.parse("text/plain"), gpsTracker.getLatitude() + "");
                RequestBody longtBody = RequestBody.create(MediaType.parse("text/plain"), gpsTracker.getLongitude() + "");
                RequestBody fosIdBody = RequestBody.create(MediaType.parse("text/plain"), mySingleton.getData(Constant.USER_ID));
                //  RequestBody address = RequestBody.create(MediaType.parse("text/plain"), Utils.addressR);
                RequestBody address = RequestBody.create(MediaType.parse("text/plain"), "");
                // RequestBody city = RequestBody.create(MediaType.parse("text/plain"), Utils.cityR);
                RequestBody city = RequestBody.create(MediaType.parse("text/plain"), "");
                // RequestBody state = RequestBody.create(MediaType.parse("text/plain"), Utils.stateR);
                RequestBody state = RequestBody.create(MediaType.parse("text/plain"), "");
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
                map.put("address", address);
                map.put("city", city);
                map.put("state", state);
                map.put("proof_id", proof);


                HashMap<String, Integer> contributorsMap = new HashMap<String, Integer>();


                for (int i = 0; i < Constant.activitiesList.size(); i++) {
                    contributorsMap.put("category_id[" + i + "]", Constant.activitiesList.get(i).getCategory());
                    contributorsMap.put("sub_category_id[" + i + "]", Constant.activitiesList.get(i).getSubCategory());
                    contributorsMap.put("sub_category1_id[" + i + "]", Constant.activitiesList.get(i).getSubCategory1());
                }


                Call<ErrorBody> call = null;
                List<MultipartBody.Part> outside_photo = new ArrayList<>();


                for (int i = 0; i < outsideimage.size(); i++) {

//                    Const.showLog("image path out " + filteredImageList.get(i));

                    File image1 = outsideimage.get(i);
//                        Const.showLog("image path " + filteredImageList.get(i));
                    outside_photo.add(MultipartBody.Part.createFormData("outside_photo[]", image1.getName(), RequestBody.create(MediaType.parse("image/*"), image1)));

                }
                List<MultipartBody.Part> inside_photo = new ArrayList<>();


                for (int i = 0; i < insidesideimage.size(); i++) {

//                    Const.showLog("image path out " + filteredImageList.get(i));


                    File image2 = insidesideimage.get(i);
                    Log.e("TAG", "insidesideimage.get(i): " + image2);
//                        Const.showLog("image path " + filteredImageList.get(i));
                    inside_photo.add(MultipartBody.Part.createFormData("inside_photo[]", image2.getName(), RequestBody.create(MediaType.parse("image/*"), image2)));

                }
                List<MultipartBody.Part> activity_photo = new ArrayList<>();


                for (int i = 0; i < acivityimage.size(); i++) {

//                    Const.showLog("image path out " + filteredImageList.get(i));

                    File image3 = acivityimage.get(i);
//                        Const.showLog("image path " + filteredImageList.get(i));
                    activity_photo.add(MultipartBody.Part.createFormData("activity_photo[]", image3.getName(), RequestBody.create(MediaType.parse("image/*"), image3)));

                }

                if (gstPicFile == null) {
                    call = apiService.saveMyActivityWithoutGst(map, contributorsMap,
                            outside_photo, activity_photo, inside_photo, panPhotoPhotoBodyPart);
                } else {
                    //send multipart data
                    call = apiService.saveMyActivity(map, contributorsMap,
                            outside_photo, activity_photo, inside_photo, gstPhotoPhotoBodyPart, panPhotoPhotoBodyPart);
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
                                        Toast.makeText(MyActivityFormActivity.this, "Activity submitted successfully", Toast.LENGTH_LONG).show();
                                        callDailyDeductionApi();
                                        // Write your code here to execute after dialog
                                        hitEarningTaskApi(Constant.activitiesList.get(0).getEarningTaskID(),
                                                response.body().getId().toString(),
                                                Constant.activitiesList.get(0).getAmount());

                                        AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
                                                MyActivityFormActivity.this);
                                        alertDialog2.setTitle("Confirm!");
                                        alertDialog2.setCancelable(false);
                                        alertDialog2.setMessage("Your activity is created with id: " + response.body().getId());
                                        alertDialog2.setPositiveButton("OK",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();
                                                        onBackPressed();
                                                    }
                                                });
                                        alertDialog2.show();
                                    }
                                }
                            } else {
                                displaySnackBar.DisplaySnackBar(Constant.UNEXPECTED, Constant.TYPE_ERROR);
                                Log.e("TAG", "onResponse: esle part ");
                            }
                        } else {
                            try {
                                ErrorBody errorBody = new ErrorBody();
                                Gson gson = new Gson();
                                errorBody = gson.fromJson(response.errorBody().string(), ErrorBody.class);
                                displaySnackBar.DisplaySnackBar(errorBody.getMessage(), Constant.TYPE_ERROR);
                                Log.e("TAG", "errorr body: esle part ");
                                if (errorBody.getMessage().contains("invalid")) {
                                }
                            } catch (Exception e) {
                                displaySnackBar.DisplaySnackBar(Constant.ERROR_INVALID_JSON, Constant.TYPE_ERROR);
                                Log.e("TAG", "onResponse: catc part ");
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ErrorBody> call, Throwable t) {
                        Log.e("TAG", "onFailure: " + t.getMessage());
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

    private void callDailyDeductionApi() {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        final Call<SuccessResponse> call = apiService.getDailyDeduction(mySingleton.getData("id"));
        call.enqueue(new Callback<SuccessResponse>() {
            @Override
            public void onResponse(Call<SuccessResponse> call, Response<SuccessResponse> response) {

                if (response.isSuccessful()) {
                    if (response.code() >= 200 && response.code() < 700) {
                        if (response.code() == 200) {
//                            if (response.body().getData().getDetails() != null) {
//                            }
                        }
                    } else {
                        displaySnackBar.DisplaySnackBar(Constant.UNEXPECTED, Constant.TYPE_ERROR);
                    }
                }
            }

            @Override
            public void onFailure(Call<SuccessResponse> call, Throwable t) {
                if (t != null && (t instanceof IOException || t instanceof SocketTimeoutException
                        || t instanceof ConnectException || t instanceof NoRouteToHostException
                        || t instanceof SecurityException)) {
                    displaySnackBar.DisplaySnackBar(Constant.NETWIRK_ERROR, Constant.TYPE_ERROR);

                }
            }
        });
    }

    private void hitEarningTaskApi(String earningTaskID, String activityId, String amount) {
       /* KProgressHUD kProgressHUD = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .setWindowColor(getResources().getColor(R.color.progressbar_color))
                .show();
*/
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        final Call<SuccessResponse> call = apiService.getEarningOnTask(mySingleton.getData(Constant.USER_ID), earningTaskID, activityId, amount);
        call.enqueue(new Callback<SuccessResponse>() {
            @Override
            public void onResponse(Call<SuccessResponse> call, Response<SuccessResponse> response) {
                kProgressHUD.dismiss();
                if (response.isSuccessful()) {
                    if (response.code() >= 200 && response.code() < 700) {
                        if (response.code() == 200) {
//                            if (response.body().getData().getDetails() != null) {
//                            }
                        }
                    } else {
                        displaySnackBar.DisplaySnackBar(Constant.UNEXPECTED, Constant.TYPE_ERROR);
                        kProgressHUD.dismiss();
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
//                Toast.makeText(this, "Permission revoked: CAMERA:, STORAGE: data from camera ", Toast.LENGTH_LONG).show();
            }
        }


    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, @Nullable final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == CAMERA_PIC_REQUEST_INSIDE && resultCode == Activity.RESULT_OK) {
            try {
                currentPhotoPath = mySingleton.getData("currentpath");
                fileAbsolutePath = mySingleton.getData("fileabsolutepath");
                File f = new File(fileAbsolutePath);
                Log.e("TAG", "runnnn:>>>> " + f.toString());
                File compressedImage = new Compressor(this).setMaxWidth(640).setMaxHeight(480).
                        setQuality(70).setCompressFormat(Bitmap.CompressFormat.PNG)
                        .compressToFile(f);
                insidesideimage.add(compressedImage);
                Log.e("TAG", "size: " + insidesideimage.size() + f.getAbsolutePath());
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MyActivityFormActivity.this, LinearLayoutManager.HORIZONTAL, true);
                insiteimagelist.setLayoutManager(linearLayoutManager);
                ShowListAdapter adapterlist = new ShowListAdapter(MyActivityFormActivity.this, insidesideimage);
                insiteimagelist.setAdapter(adapterlist);
                Log.e("TAG", "absolute " + f.getAbsolutePath());
                Log.e("TAG", "path " + f.getPath());
                if (f.getAbsolutePath() != null) {
                    Bitmap bitmap = BitmapFactory.decodeFile(fileAbsolutePath);

                    // imgCameraInside.setImageBitmap(bitmap);
                    //  Log.e("TAG", "bitmap " + bitmap.toString());
                } else {
                    Log.e("TAG", "file not exist: ");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            imgInsideCheck.setVisibility(View.VISIBLE);
            etPicode.setText(Utils.getPincode(MyActivityFormActivity.this, gpsTracker.getLatitude(), gpsTracker.getLongitude()));
        } else if (requestCode == CAMERA_PIC_REQUEST_OUSIDE && resultCode == Activity.RESULT_OK) {


            try {

                currentPhotoPath = mySingleton.getData("currentpath");
                fileAbsolutePath = mySingleton.getData("fileabsolutepath");
                File f = new File(fileAbsolutePath);

                Log.e("TAG", "runnnn:>>>> " + f.toString());

                File compressedImage = new Compressor(this).setMaxWidth(640).setMaxHeight(480).
                        setQuality(70).setCompressFormat(Bitmap.CompressFormat.PNG)
                        .compressToFile(f);

                outsideimage.add(compressedImage);
                Log.e("TAG", "size: " + outsideimage.size() + f.getAbsolutePath());
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MyActivityFormActivity.this, LinearLayoutManager.HORIZONTAL, true);
                outsideimagelist.setLayoutManager(linearLayoutManager);
                ShowListAdapter adapterlist = new ShowListAdapter(MyActivityFormActivity.this, outsideimage);
                outsideimagelist.setAdapter(adapterlist);
                Log.e("TAG", "absolute " + compressedImage.getAbsolutePath());
                Log.e("TAG", "path " + compressedImage.getPath());


                if (f.getAbsolutePath() != null) {
                    Bitmap bitmap = BitmapFactory.decodeFile(fileAbsolutePath);

                    //   imgCameraOutside.setImageBitmap(bitmap);
                    //    Log.e("TAG", "bitmap " + bitmap.toString());
                } else {
                    Log.e("TAG", "file not exist: ");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            etPicode.setText(Utils.getPincode(MyActivityFormActivity.this, gpsTracker.getLatitude(), gpsTracker.getLongitude()));
            imgOutsideCheck.setVisibility(View.VISIBLE);
        }

        if (requestCode == CAMERA_PIC_REQUEST_ACTIVITY) {

            try {

                currentPhotoPath = mySingleton.getData("currentpath");
                fileAbsolutePath = mySingleton.getData("fileabsolutepath");
                File f = new File(fileAbsolutePath);

                Log.e("TAG", "runnnn:>>>> " + f.toString());
                File compressedImage = new Compressor(this).setMaxWidth(640).setMaxHeight(480).
                        setQuality(70).setCompressFormat(Bitmap.CompressFormat.PNG)
                        .compressToFile(f);
                acivityimage.add(compressedImage);
                Log.e("TAG", "size: " + outsideimage.size() + f.getAbsolutePath());
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MyActivityFormActivity.this, LinearLayoutManager.HORIZONTAL, true);
                acivityimagelist.setLayoutManager(linearLayoutManager);
                ShowListAdapter adapterlist = new ShowListAdapter(MyActivityFormActivity.this, acivityimage);
                acivityimagelist.setAdapter(adapterlist);
                Log.e("TAG", "absolute " + compressedImage.getAbsolutePath());
                Log.e("TAG", "path " + f.getPath());


                if (f.getAbsolutePath() != null) {
                    Bitmap bitmap = BitmapFactory.decodeFile(fileAbsolutePath);

                    //    imgCameraActivity.setImageBitmap(bitmap);
                    //    Log.e("TAG", "bitmap " + bitmap.toString());
                } else {
                    Log.e("TAG", "file not exist: ");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            imgActivityCheck.setVisibility(View.VISIBLE);
            etPicode.setText(Utils.getPincode(MyActivityFormActivity.this, gpsTracker.getLatitude(), gpsTracker.getLongitude()));

        }


        if ((resultCode == Activity.RESULT_OK)) {
            if (requestCode == CAMERA_PIC_REQUEST_GST_PIC) {

                currentPhotoPath = mySingleton.getData("currentpath");
                fileAbsolutePath = mySingleton.getData("fileabsolutepath");
                File f = new File(fileAbsolutePath);


                //File f = grabImageFile(true, 80);
                gstPicFile = f;
                btnUploadGstPic.setText(gstPicFile.getName());
                btnUploadGstPic.setVisibility(View.VISIBLE);
            }
        }
        if (data != null) {
            if (requestCode == GALLERY_PIC_REQUEST_GST_PIC) {
                Uri imageUri = data.getData();
                try {
                    Bitmap image = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                    bitmapToFile(image, CAMERA_PIC_REQUEST_GST_PIC);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if ((resultCode == Activity.RESULT_OK)) {
            if (requestCode == CAMERA_PIC_REQUEST_PAN_PIC) {
                // panPicFile = grabImageFile(true, 80);
                currentPhotoPath = mySingleton.getData("currentpath");
                fileAbsolutePath = mySingleton.getData("fileabsolutepath");
                File f = new File(fileAbsolutePath);
                panPicFile = f;
                btnUploadPanPic.setVisibility(View.VISIBLE);
                btnUploadPanPic.setText(panPicFile.getName());
            }
        }
        if (data != null) {
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

//    File outSideFile = null, activityFile = null, insideFile = null, gstPicFile = null, panPicFile = null;

    private void bitmapToFile(Bitmap yourBitmap, int reqCode) {
        String filename = Environment.getExternalStorageDirectory() + File.separator + Calendar.getInstance().getTimeInMillis() +
                "temporary_file.png";
//        String filename = Calendar.getInstance().getTimeInMillis() +
//                "temporary_file.png";

        File f = new File(filename);
        try {
            f.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
//Convert bitmap to byte array
        Bitmap bitmap = yourBitmap;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80 /*ignored for PNG*/, bos);
        byte[] bitmapdata = bos.toByteArray();

//write the bytes in file
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(f);

            fos.write(bitmapdata);
            String pincode = Utils.getPincode(MyActivityFormActivity.this, gpsTracker.getLatitude(), gpsTracker.getLongitude());

            switch (reqCode) {
                case 101:
                    outsideimage.add(f);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true);
                    outsideimagelist.setLayoutManager(linearLayoutManager);
                    ShowListAdapter adapterlist = new ShowListAdapter(this, outsideimage);
                    outsideimagelist.setAdapter(adapterlist);

//                    btnOutsidePhoto.setText("Outside Photo: " + outSideFile.getName());
                    etPicode.setText(Utils.getPincode(MyActivityFormActivity.this, gpsTracker.getLatitude(), gpsTracker.getLongitude()));
                    imgOutsideCheck.setVisibility(View.VISIBLE);

                    break;
                case 102:
                    acivityimage.add(f);
                    LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true);
                    acivityimagelist.setLayoutManager(linearLayoutManager1);
                    ShowListAdapter adapterlist1 = new ShowListAdapter(this, acivityimage);
                    acivityimagelist.setAdapter(adapterlist1);
//                    activityFile = f;
//                    btnActivityPhoto.setText("Activity Photo: " + activityFile.getName());
                    etPicode.setText(Utils.getPincode(MyActivityFormActivity.this, gpsTracker.getLatitude(), gpsTracker.getLongitude()));
                    imgActivityCheck.setVisibility(View.VISIBLE);
                    etPicode.setText(Utils.getPincode(MyActivityFormActivity.this, gpsTracker.getLatitude(), gpsTracker.getLongitude()));


                    break;
                case 103:


                    insidesideimage.add(f);
                    LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true);
                    insiteimagelist.setLayoutManager(linearLayoutManager2);
                    ShowListAdapter adapterlist2 = new ShowListAdapter(this, insidesideimage);
                    insiteimagelist.setAdapter(adapterlist2);

//                    insideFile = f;
//                    btnInsidePhoto.setText("Inside Photo: " + insideFile.getName());
                    imgInsideCheck.setVisibility(View.VISIBLE);
                    etPicode.setText(Utils.getPincode(MyActivityFormActivity.this, gpsTracker.getLatitude(), gpsTracker.getLongitude()));

                    break;
                case 104:
                    gstPicFile = f;
                    btnUploadGstPic.setText(gstPicFile.getName());
                    btnUploadGstPic.setVisibility(View.VISIBLE);
                    break;
                case 106:
                    panPicFile = f;
                    btnUploadPanPic.setText(panPicFile.getName());
                    btnUploadPanPic.setVisibility(View.VISIBLE);
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
                ActivityCompat.requestPermissions(MyActivityFormActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v("permission", "Permission is granted");
            return true;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        gpsTracker = new GPSTracker(MyActivityFormActivity.this);


    }


    private File createTemporaryFile(String part, String ext) throws Exception {
        File tempDir = Environment.getExternalStorageDirectory();
        tempDir = new File(tempDir.getAbsolutePath() + "/rebliss/");
        if (!tempDir.exists()) {
            tempDir.mkdir();
        }
        return File.createTempFile(part, ext, tempDir);
    }

    public File grabImageFile(boolean compress, int quality) {
        File returnFile = null;
        try {
            //InputStream is = getContentResolver().openInputStream(mImageUri);
            if (mImageUri != null) {
                returnFile = new File(mImageUri.getPath());
                if (returnFile.exists() && compress) {
                    BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                    Bitmap bitmap = BitmapFactory.decodeFile(returnFile.getAbsolutePath(), bmOptions);
                    File compressedFile = createTemporaryFile("Image", ".jpg");
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


    private void showMyActivtiyDialog() {

        final Dialog dialog = new Dialog(Objects.requireNonNull(MyActivityFormActivity.this));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_select_my_activity);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();

        spinCat = dialog.findViewById(R.id.spinCat);
        spinSubCat = dialog.findViewById(R.id.spinSubCat);
        spinSubCat2 = dialog.findViewById(R.id.spinSubCat2);

        catSelected = null;
        subCatSelected = null;
        subCat1Selected = null;


        if (catStringList == null) {
            getCategory(spinCat, 1, 0);
        } else {

            ArrayAdapter aa = new ArrayAdapter(MyActivityFormActivity.this, android.R.layout.simple_spinner_item, catStringList);
            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            //Setting the ArrayAdapter data on the Spinner
            spinCat.setAdapter(aa);
        }

        Button btnSubmit = dialog.findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (catSelected == null) {
                    Toast.makeText(MyActivityFormActivity.this, "Please select category", Toast.LENGTH_LONG).show();
                    return;
                }

                if (subCatSelected == null) {
                    Toast.makeText(MyActivityFormActivity.this, "Please select sub category", Toast.LENGTH_LONG).show();
                    return;
                }

                if (subCat1Selected == null) {
                    Toast.makeText(MyActivityFormActivity.this, "Please select sub category1", Toast.LENGTH_LONG).show();
                    return;
                }

                if (Utils.checkActivityDuplicate(subCat1Selected)) {
                    Toast.makeText(MyActivityFormActivity.this, "You can't select duplicate activity", Toast.LENGTH_LONG).show();
                    return;
                }


                ActivitySelectModel selectModel = new ActivitySelectModel();
                selectModel.setCategory(catSelected);
                selectModel.setSubCategory(subCatSelected);
                selectModel.setSubCategory1(subCat1Selected);

                Constant.activitiesList.add(selectModel);

                Toast.makeText(MyActivityFormActivity.this, "Activity " + Constant.activitiesList.size() + " is added successfully.", Toast.LENGTH_LONG).show();

                myFormActivityListShowAdapter = new MyFormActivityListShowAdapter(MyActivityFormActivity.this, Constant.activitiesList, 0);
                rvActivity.setAdapter(myFormActivityListShowAdapter);

                checkValidationFromApi(tvMobileno.getText().toString(), 5, Constant.activitiesList.get(0).getCategory().toString(), Constant.activitiesList.get(0).getSubCategory1().toString());

                dialog.dismiss();


            }
        });

        spinCat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {

                if (pos != 0 && subCatStringList == null) {
                    catSelected = catList.get(pos - 1).getCategoryId();
                    getCategory(spinSubCat, 2, catList.get(pos - 1).getCategoryId());
                } else if (pos != 0) {
                    catSelected = catList.get(pos - 1).getCategoryId();
                    getCategory(spinSubCat, 2, catList.get(pos - 1).getCategoryId());

//                    ArrayAdapter aa = new ArrayAdapter(MyActivityFormActivity.this, android.R.layout.simple_spinner_item, subCatStringList);
//                    aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                    spinSubCat.setAdapter(aa);


                    List<String> oneItemList = new ArrayList<>();
                    oneItemList.add("Select sub category1");
                    ArrayAdapter aa = new ArrayAdapter(MyActivityFormActivity.this, android.R.layout.simple_spinner_item, oneItemList);
                    aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinSubCat2.setAdapter(aa);

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        spinSubCat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {

                if (pos != 0 && subCat1StringList == null) {
                    subCatSelected = subCatList.get(pos - 1).getCategoryId();
                    getCategory(spinSubCat2, 3, subCatList.get(pos - 1).getCategoryId());
                } else if (pos != 0) {
                    subCatSelected = subCatList.get(pos - 1).getCategoryId();
                    getCategory(spinSubCat2, 3, subCatList.get(pos - 1).getCategoryId());

//                    ArrayAdapter aa = new ArrayAdapter(MyActivityFormActivity.this, android.R.layout.simple_spinner_item, subCat1StringList);
//                    aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                    spinSubCat2.setAdapter(aa);
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

    List<String> catStringList = null, subCatStringList, subCat1StringList;

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
                                    //stateData = response.body().getData().getState();
                                    if (position == 1) {
                                        catStringList = new ArrayList<>();
                                        catStringList.add("Select Category");
                                        catList = response.body().getData().getAllCategory();
                                        for (AllCategory datum : response.body().getData().getAllCategory()) {
                                            catStringList.add(datum.getCategoryName());
                                        }
                                        //Creating the ArrayAdapter instance having the country list
                                        ArrayAdapter aa = new ArrayAdapter(MyActivityFormActivity.this, android.R.layout.simple_spinner_item, catStringList);
                                        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        //Setting the ArrayAdapter data on the Spinner
                                        spinCat.setAdapter(aa);
                                    } else if (position == 2) {
                                        subCatStringList = new ArrayList<>();
                                        subCatStringList.add("Select Sub category");

                                        subCatList = response.body().getData().getAllGroups();
                                        for (AllCategory datum : response.body().getData().getAllGroups()) {
                                            subCatStringList.add(datum.getCategoryName());
                                        }
                                        //Creating the ArrayAdapter instance having the country list
                                        ArrayAdapter aa = new ArrayAdapter(MyActivityFormActivity.this, android.R.layout.simple_spinner_item, subCatStringList);
                                        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        //Setting the ArrayAdapter data on the Spinner
                                        spinCat.setAdapter(aa);
                                    } else if (position == 3) {
                                        subCat1StringList = new ArrayList<>();
                                        subCat1StringList.add("Select Sub category1");
                                        subCat1List = response.body().getData().getAllGroups();
                                        for (AllCategory datum : response.body().getData().getAllGroups()) {
                                            subCat1StringList.add(datum.getCategoryName());
                                        }
                                        //Creating the ArrayAdapter instance having the country list
                                        ArrayAdapter aa = new ArrayAdapter(MyActivityFormActivity.this, android.R.layout.simple_spinner_item, subCat1StringList);
                                        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        //Setting the ArrayAdapter data on the Spinner
                                        spinCat.setAdapter(aa);
                                    }
                                } else {
                                    displaySnackBar.DisplaySnackBar("Data not found", Constant.TYPE_ERROR);
                                }
                            } else if (response.body().getStatus() == 0) {
                                //showWarningSimpleAlertDialog(response.body().getData().getMessage(), response.body().getData().getValidation_error().getPassword().get(0));
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

    public void getShopCategory() {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<ShopCategoryResponse>
                call = apiService.getShopCategories();

        call.enqueue(new Callback<ShopCategoryResponse>() {
            @Override
            public void onResponse(@NotNull Call<ShopCategoryResponse> call, Response<ShopCategoryResponse> response) {
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

                                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(MyActivityFormActivity.this, android.R.layout.select_dialog_item, categoryArray);
                                    ArrayAdapter stateAdapter = new ArrayAdapter(getApplicationContext(), R.layout.spinner_item, categoryArray);
                                    spinShopcategory.setAdapter(stateAdapter);
//                                    tvShopcategory.setThreshold(1);//will start working from first character
//                                    tvShopcategory.setAdapter(adapter);
//                                    tvShopcategory.setTextColor(Color.BLACK);

                                } else {
                                    displaySnackBar.DisplaySnackBar("Data not found", Constant.TYPE_ERROR);
                                }
                            } else if (response.body().getStatus() == 0) {
                                //showWarningSimpleAlertDialog(response.body().getData().getMessage(), response.body().getData().getValidation_error().getPassword().get(0));
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
            public void onFailure(Call<ShopCategoryResponse> call, Throwable t) {
                if (t != null && (t instanceof IOException || t instanceof SocketTimeoutException
                        || t instanceof ConnectException || t instanceof NoRouteToHostException
                        || t instanceof SecurityException)) {
                    displaySnackBar.DisplaySnackBar(Constant.NETWIRK_ERROR, Constant.TYPE_ERROR);
                }
            }
        });
    }


    public void checkValidationFromApi(String mobile, final Integer type, String categoryId, String subCategoryId1) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<CategoryResponse>
                call = apiService.checkDuplicasy(storeid.getText().toString().trim(), mobile, categoryId, subCategoryId1, type);
        call.enqueue(new Callback<CategoryResponse>() {
            @Override
            public void onResponse(Call<CategoryResponse> call, Response<CategoryResponse> response) {
                if (response.isSuccessful()) {
                    if (response.code() >= 200 && response.code() < 700) {
                        if (response.code() == 200) {
                            if (response.body().getStatus() == 0) {
                                if (type == 1 || type == 5) {
//                                    tvMobileno.setError(response.body().getMessage());
                                    ToastForSpecificTime(response.body().getMessage());
                                    // Toast.makeText(MyActivityFormActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                                }
                                if (type == 2 || type == 4) {
//                                    etGstNumber.setError(response.body().getMessage());
                                    ToastForSpecificTime(response.body().getMessage());
                                    Toast.makeText(MyActivityFormActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                                }
                                if (type == 3) {
//                                    etPanNumber.setError(response.body().getMessage());
                                    ToastForSpecificTime(response.body().getMessage());
                                    // Toast.makeText(MyActivityFormActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
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
                                        if (type == 5) {
                                            Constant.activitiesList.remove(Constant.activitiesList.size() - 1);
                                            if (myFormActivityListShowAdapter != null)
                                                myFormActivityListShowAdapter.notifyDataSetChanged();
                                        }
                                    }
                                }, 200);
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
            public void onFailure(Call<CategoryResponse> call, Throwable t) {
                if (t != null && (t instanceof IOException || t instanceof SocketTimeoutException
                        || t instanceof ConnectException || t instanceof NoRouteToHostException
                        || t instanceof SecurityException)) {
                    displaySnackBar.DisplaySnackBar(Constant.NETWIRK_ERROR, Constant.TYPE_ERROR);
                }
            }
        });
    }

    private void ToastForSpecificTime(String response) {
        int toastDurationInMilliSeconds = 20000;
        toast = Toast.makeText(this, response, Toast.LENGTH_LONG);
        // Set the countdown to display the toast
        CountDownTimer toastCountDown;
        toastCountDown = new CountDownTimer(toastDurationInMilliSeconds, 1000 /*Tick duration*/) {
            public void onTick(long millisUntilFinished) {
                toast.show();
            }

            public void onFinish() {
                toast.cancel();
            }
        };
        // Show the toast and starts the countdown
        toast.show();
        toastCountDown.start();
    }

    private String encodeTobase64(Bitmap rotatedBitmap) {
        Bitmap base644 = rotatedBitmap;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        base644.compress(Bitmap.CompressFormat.PNG, 15, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
        return imageEncoded;
    }

    private File getImageFile() throws IOException {
        String imageFileName = "IMG_" + System.currentTimeMillis() + "_";
        String imagesFolder = "images";
        File storageDir = new File(
                getExternalFilesDir(Environment.DIRECTORY_PICTURES), imagesFolder
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
}
