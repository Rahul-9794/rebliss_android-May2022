package com.rebliss.view.activity;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.os.Build.VERSION.SDK_INT;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Base64OutputStream;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.gson.Gson;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.rebliss.utils.App;
import com.rebliss.BuildConfig;
import com.rebliss.R;
import com.rebliss.data.perf.MySingleton;
import com.rebliss.domain.constant.Constant;
import com.rebliss.domain.model.ErrorBody;
import com.rebliss.domain.model.editprofile.EditProfileRequest;
import com.rebliss.domain.model.editprofile.EditProfileResponce;
import com.rebliss.domain.model.fileupload.FileUploadResponce;
import com.rebliss.domain.model.fileupload.UploadRequest;
import com.rebliss.domain.model.logout.LogoutResponce;
import com.rebliss.domain.model.profile.Data;
import com.rebliss.domain.model.profile.ProfileResponce;
import com.rebliss.presenter.helper.DisplaySnackBar;
import com.rebliss.presenter.helper.Logout;
import com.rebliss.presenter.helper.Network;
import com.rebliss.presenter.helper.RegexUtils;
import com.rebliss.presenter.helper.ShowHintOrText;
import com.rebliss.presenter.helper.TextUtil;
import com.rebliss.presenter.retrofit.ApiClient;
import com.rebliss.presenter.retrofit.ApiInterface;
import com.rebliss.utils.Util;
import com.rebliss.view.activity.selfassessmenttest.SelfAssessmentQuestionsActivity;
import com.rebliss.view.adapter.UploadRemoveAdapter;
import com.zhihu.matisse.Matisse;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.NoRouteToHostException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardFOSDocUpload2 extends AppCompatActivity {
    private static final String TAG = DashboardFOSDocUpload2.class.getSimpleName();
    private Context context;
    private TextView textCPAdhar, textPan, textFirmPan, textGST, textAddress, textCheque, textTurnOver, textOtherBusiness;
    private TextView textAdharFile, textCPPANFile, textFirmFile, textGSTFile, textaddressFile, textCHequeFile, textHeader;
    private TextView textUploadOption;
    private EditText etDateofBirth;
    private  String date ="";
    private ImageView imgLogout, imgCal;
    private Button btnSave, btnUpload, btnUploadPAN, btnUploadFirmPan, btnUploadGST, btnUploadAddress, btnUploadCheque, btnUploadPassport;
    LinearLayout click_to_upload, click_to_uploadPan, click_to_uploadFirmPan, click_to_uploadGST, click_to_uploadaddress, click_to_uploadcheque;
    private EditText etAddhar, etPan, etFirmPan, etGST, etaddress, etcheque, etEmail;
    LinearLayout textUploadSection;
    TextView emailLabel;
    TextView dateofBirthlabel;
    Spinner spUploadOption;
    private int sUploadOption;
    private int spUploadOptionPosition = 0;
    String[] uploadTypeOption = {"Select Document Type", "Aadhaar(Optional)", "Driving Licence", "Voter Id"};
    private String selectedUploadType = "";

    private MySingleton mySingleton;
    private Network network;
    private KProgressHUD kProgressHUD;
    private DisplaySnackBar displaySnackBar;
    private Data profileData;
    private String sAddhar = "", sPan = "", sFirmPan = "", sGST = "", saddress = "", scheque = "", sEmail = "";


    private int Day, Month, Year;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private EditProfileRequest editProfileRequest;
    private Intent intent;
    private final int REQUEST_CAMERA_JOB = 111;
    private final int FILE_SELECT_CODE_JOB = 222;
    private final int FILE_SELECT_ADHAR = 50;
    private final int FILE_SELECT_CPPAN = 100;
    private final int FILE_SELECT_FIRMPAN = 200;
    private final int FILE_SELECT_GST = 300;
    private final int FILE_SELECT_ADDRESS = 400;
    private final int FILE_SELECT_CHEQUE = 500;
    private final int FILE_SELECT_PASSPORT = 600;
    private final String selectedFilePath = "";
    private List<Uri> obtainUri;
    private List<String> obtainPathResult;
    private List<String> aadhaarImages;
    private List<String> panImages;
    private List<String> firmPanImages;
    private List<String> addressProofImages;
    private List<String> gstImages;
    private List<String> cancelChequeImages;
    private List<String> passportImages;
    private List<String> signedPhotoImages;
    private List<String> shopImages;
    ArrayAdapter<String> spinnerArrayAdapter;
    private String Adhar_Url = "", Pan_Url = "", Firm_Pan_Url = "", Gst_Url = "", Address_Url = "", Cheque_Url = "", Passport_Url = "";
    private TextView textStepOne, textStepTwo, textStepThree, textStepFour, textPassport, textPassportFile;
    private ImageView imgStepOne, imgStepTwo, imgStepThree, imgStepFour;
    private View viewStepOne, viewStepTwo, viewStepThree;
    private String callFrom = "";
    ImageView icBack;
    TextView mywidget;
    int positionForUpload = 0;
    RecyclerView aadhar_recycler_view;
    RecyclerView pan_recycler_view;
    RecyclerView cnclChq_recycler_view;
    RecyclerView pssprt_photo_recycler_view;
    private ImageView Imcamera01;
    private ImageView Imattach01;
    private ImageView Imcamera02;
    private ImageView Imattach02;
    private ImageView Imcamera03;
    private ImageView Imattach03;
    private ImageView Imcamera04;
    private ImageView Imattach04;
    private ImageView Imcamera05;
    private ImageView Imattach05;
    private ImageView Imcamera06;
    private ImageView Imattach06;
    private ImageView Imcamera07;
    private ImageView Imattach07;
    private ImageView Imcamera08;
    private ImageView Imattach08;
    private ImageView Imcamera09;
    private ImageView Imattach09;
    private ImageView Imcheck01;
    private ImageView Imcheck02;
    private ImageView Imcheck06;
    private ImageView Imcheck07;
    private String currentPhotoPath;
    private String selectedImagePath;
    private String uploadImageViaCamera;
    private final int AADHAR_IMAGE_CLICK_REQUEST = 101;
    private final int AADHAR_IMAGE_ATTACH_REQUEST = 102;
    private final int PAN_IMAGE_CLICK_REQUEST = 201;
    private final int PAN_IMAGE_ATTACH_REQUEST = 202;
    private final int FIRM_PAN_IMAGE_CLICK_REQUEST = 301;
    private final int FIRM_PAN_IMAGE_ATTACH_REQUEST = 302;
    private final int GST_IMAGE_CLICK_REQUEST = 401;
    private final int GST_IMAGE_ATTACH_REQUEST = 402;
    private final int ADDRESS_IMAGE_CLICK_REQUEST = 501;
    private final int ADDRESS_IMAGE_ATTACH_REQUEST = 502;
    private final int CHEQUE_IMAGE_CLICK_REQUEST = 601;
    private final int CHEQUE_IMAGE_ATTACH_REQUEST = 602;
    private final int PASSPORT_IMAGE_CLICK_REQUEST = 701;
    private final int PASSPORT_IMAGE_ATTACH_REQUEST = 702;
    private final int SIGNED_FORM_IMAGE_CLICK_REQUEST = 801;
    private final int SIGNED_FORM_IMAGE_ATTACH_REQUEST = 802;
    private final int OFFICE_PHOTO_IMAGE_CLICK_REQUEST = 901;
    private final int OFFICE_PHOTO_IMAGE_ATTACH_REQUEST = 902;
    private UploadRemoveAdapter AdharAdapter, PanAdapter, ChequeAdapter, PassportAdapter;
    private Bundle outState;
    private boolean isReloadNeed = true;
    private Uri mImageUri;
    String currentPhotoPaths ="";
    String fileAbsolutePath ="";
    Uri photoURI;
    String nbase64 ="";
    public static File dir = new File(new File(Environment.getExternalStorageDirectory(), "rebliss"), "rebliss");
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("call_from", callFrom);
        outState.putSerializable("save_data", editProfileRequest);
        outState.putSerializable("s", editProfileRequest);
        this.outState = outState;
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        callFrom = savedInstanceState.getString("call_from");
        editProfileRequest = (EditProfileRequest) savedInstanceState.getSerializable("save_data");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isReloadNeed) {
            showConditionalImageIcon();
            setFontOnView();
            callProfileAPI();
            setHint();
            isReloadNeed = false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fos_doc2);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        initView();
        viewListener();


        try {
            intent = getIntent();
            callFrom = intent.getStringExtra("call_from");
            editProfileRequest = (EditProfileRequest) intent.getSerializableExtra("saved_data");
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (savedInstanceState != null) {
            callFrom = savedInstanceState.getString("call_from");
        }
        disable();
    }
    private void disable() {
        if (!TextUtil.isStringNullOrBlank(callFrom)) {
            if (callFrom.equalsIgnoreCase("1")) {
                imgLogout.setVisibility(View.GONE);
                btnUploadPassport.setEnabled(false);
                etAddhar.setEnabled(false);
                etPan.setEnabled(false);
                etFirmPan.setEnabled(false);
                etcheque.setEnabled(false);
                etaddress.setEnabled(false);
                etGST.setEnabled(false);
                btnUpload.setEnabled(false);
                btnUploadPAN.setEnabled(false);
                btnUploadFirmPan.setEnabled(false);
                btnUploadGST.setEnabled(false);
                btnUploadAddress.setEnabled(false);
                btnUploadCheque.setEnabled(false);
                Imcamera01.setEnabled(false);
                Imcamera02.setEnabled(false);
                Imcamera06.setEnabled(false);
                Imcamera07.setEnabled(false);
                Imattach01.setEnabled(false);
                Imattach02.setEnabled(false);
                Imattach06.setEnabled(false);
                Imattach07.setEnabled(false);
                etAddhar.setClickable(false);
                etPan.setClickable(false);
                etFirmPan.setClickable(false);
                etcheque.setClickable(false);
                etaddress.setClickable(false);
                etGST.setClickable(false);
                btnUploadPassport.setClickable(false);
                btnUpload.setClickable(false);
                btnUploadPAN.setClickable(false);
                btnUploadFirmPan.setClickable(false);
                btnUploadGST.setClickable(false);
                btnUploadAddress.setClickable(false);
                btnUploadCheque.setClickable(false);
                Imcamera01.setClickable(false);
                Imcamera02.setClickable(false);
                Imcamera06.setClickable(false);
                Imcamera07.setClickable(false);
                Imattach01.setClickable(false);
                Imattach02.setClickable(false);
                Imattach06.setClickable(false);
                Imattach07.setClickable(false);
                aadhar_recycler_view.setClickable(false);
                pan_recycler_view.setClickable(false);
                cnclChq_recycler_view.setClickable(false);
                pssprt_photo_recycler_view.setClickable(false);
                btnSave.setText("Save & Continue");
            }
        }
    }

    private void initView() {
        context = DashboardFOSDocUpload2.this;
        mySingleton = new MySingleton(this);
        displaySnackBar = new DisplaySnackBar(this);
        network = new Network();
        emailLabel = findViewById(R.id.txtemaillabel);
        dateofBirthlabel = findViewById(R.id.txtdateofBirthlabel);
        icBack = findViewById(R.id.icBack);
         etDateofBirth = findViewById(R.id.etdateofbirth);
        textPassportFile = findViewById(R.id.textPassportFile);
        textStepOne = findViewById(R.id.textStepOne);
        textStepTwo = findViewById(R.id.textStepTwo);
        textStepOne.setTypeface(App.LATO_REGULAR);
        textStepTwo.setTypeface(App.LATO_REGULAR);
        mywidget = findViewById(R.id.mywidget);
        btnUploadPassport = findViewById(R.id.btnUploadPassport);
        textPassport = findViewById(R.id.textPassport);
        textHeader = findViewById(R.id.textHeader);
        textCPAdhar = findViewById(R.id.textCPAdhar);
        textPan = findViewById(R.id.textPan);
        textFirmPan = findViewById(R.id.textFirmPan);
        textGST = findViewById(R.id.textGST);
        textAddress = findViewById(R.id.textAddress);
        textCheque = findViewById(R.id.textCheque);
        textAdharFile = findViewById(R.id.textAdharFile);
        textCPPANFile = findViewById(R.id.textCPPANFile);
        textFirmFile = findViewById(R.id.textFirmFile);
        textGSTFile = findViewById(R.id.textGSTFile);
        textaddressFile = findViewById(R.id.textaddressFile);
        textCHequeFile = findViewById(R.id.textCHequeFile);
        textUploadOption = findViewById(R.id.textUploadOption);
        imgLogout = findViewById(R.id.imgLogout);
        Imattach01 = findViewById(R.id.Imattach01);
        Imcamera01 = findViewById(R.id.Imcamera01);
        Imattach02 = findViewById(R.id.Imattach02);
        Imcamera02 = findViewById(R.id.Imcamera02);
        Imattach06 = findViewById(R.id.Imattach06);
        Imcamera06 = findViewById(R.id.Imcamera06);
        Imattach07 = findViewById(R.id.Imattach07);
        Imcamera07 = findViewById(R.id.Imcamera07);
        Imcheck01 = findViewById(R.id.Imcheck01);
        Imcheck02 = findViewById(R.id.Imcheck02);
        Imcheck06 = findViewById(R.id.Imcheck06);
        Imcheck07 = findViewById(R.id.Imcheck07);
        btnSave = findViewById(R.id.btnSave);
        btnUpload = findViewById(R.id.btnUpload);
        btnUploadPAN = findViewById(R.id.btnUploadPAN);
        btnUploadFirmPan = findViewById(R.id.btnUploadFirmPan);
        btnUploadGST = findViewById(R.id.btnUploadGST);
        btnUploadAddress = findViewById(R.id.btnUploadAddress);
        btnUploadCheque = findViewById(R.id.btnUploadCheque);
        etAddhar = findViewById(R.id.etAddhar);
        etPan = findViewById(R.id.etPan);
        etFirmPan = findViewById(R.id.etFirmPan);
        etGST = findViewById(R.id.etGST);
        etaddress = findViewById(R.id.etaddress);
        etcheque = findViewById(R.id.etcheque);
        etEmail = findViewById(R.id.etEmail);
        textUploadSection = findViewById(R.id.textUploadSection);
        click_to_upload = findViewById(R.id.click_to_upload);
        click_to_uploadPan = findViewById(R.id.click_to_uploadPan);
        click_to_uploadFirmPan = findViewById(R.id.click_to_uploadFirmPan);
        click_to_uploadGST = findViewById(R.id.click_to_uploadGST);
        click_to_uploadaddress = findViewById(R.id.click_to_uploadaddress);
        click_to_uploadcheque = findViewById(R.id.click_to_uploadcheque);
        spUploadOption = findViewById(R.id.spUploadOption);
        aadhar_recycler_view = findViewById(R.id.aadhar_recycler_view);
        aadhar_recycler_view.setHasFixedSize(true);
        LinearLayoutManager supportersLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        aadhar_recycler_view.setLayoutManager(supportersLayoutManager);
        pan_recycler_view = findViewById(R.id.pan_recycler_view);
        pan_recycler_view.setHasFixedSize(true);
        LinearLayoutManager pansupportersLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        pan_recycler_view.setLayoutManager(pansupportersLayoutManager);
        cnclChq_recycler_view = findViewById(R.id.cnclChq_recycler_view);
        cnclChq_recycler_view.setHasFixedSize(true);
        LinearLayoutManager chqsupportersLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        cnclChq_recycler_view.setLayoutManager(chqsupportersLayoutManager);
        pssprt_photo_recycler_view = findViewById(R.id.pssprt_photo_recycler_view);
        pssprt_photo_recycler_view.setHasFixedSize(true);
        LinearLayoutManager pssprtsupportersLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        pssprt_photo_recycler_view.setLayoutManager(pssprtsupportersLayoutManager);
    }


    private void setHint() {
        if (selectedUploadType.equalsIgnoreCase("aadhaar") || spUploadOptionPosition == 1) {
            textCPAdhar.setText(ShowHintOrText.GetMandatory("Aadhar Number"));
        } else if (selectedUploadType.equalsIgnoreCase("dl") || spUploadOptionPosition == 2) {
            textCPAdhar.setText(ShowHintOrText.GetMandatory("Driving Licence Number"));
        } else if (selectedUploadType.equalsIgnoreCase("voterId") || spUploadOptionPosition == 3) {
            textCPAdhar.setText(ShowHintOrText.GetMandatory("Voter Id Number"));
        }
        textPan.setText(ShowHintOrText.GetOptional("PAN No"));
        textCheque.setText(ShowHintOrText.GetMandatory("Bank proof"));
        textPassport.setText(ShowHintOrText.GetMandatory("Passport size photo"));
        textUploadOption.setText(ShowHintOrText.GetMandatory("Choose to Upload"));
        emailLabel.setText(ShowHintOrText.GetMandatory("Email"));
        dateofBirthlabel.setText(ShowHintOrText.GetMandatory("Date of Birth"));
    }

    private void showConditionalImageIcon() {
        Imcheck01.setVisibility(View.GONE);
        Imcheck02.setVisibility(View.GONE);
        Imcheck06.setVisibility(View.GONE);
        Imcheck07.setVisibility(View.GONE);
    }

    private void setFontOnView() {
        mywidget.setTypeface(App.LATO_REGULAR);
        textPassportFile.setTypeface(App.LATO_REGULAR);
        btnUploadPassport.setTypeface(App.LATO_REGULAR);
        textPassport.setTypeface(App.LATO_REGULAR);
        textHeader.setTypeface(App.LATO_REGULAR);
        textCPAdhar.setTypeface(App.LATO_REGULAR);
        textPan.setTypeface(App.LATO_REGULAR);
        textFirmPan.setTypeface(App.LATO_REGULAR);
        textGST.setTypeface(App.LATO_REGULAR);
        textAddress.setTypeface(App.LATO_REGULAR);
        textCheque.setTypeface(App.LATO_REGULAR);
        textAdharFile.setTypeface(App.LATO_REGULAR);
        textCPPANFile.setTypeface(App.LATO_REGULAR);
        textGSTFile.setTypeface(App.LATO_REGULAR);
        textFirmPan.setTypeface(App.LATO_REGULAR);
        textaddressFile.setTypeface(App.LATO_REGULAR);
        textCHequeFile.setTypeface(App.LATO_REGULAR);
        textUploadOption.setTypeface(App.LATO_REGULAR);
        btnSave.setTypeface(App.LATO_REGULAR);
        btnUpload.setTypeface(App.LATO_REGULAR);
        btnUploadPAN.setTypeface(App.LATO_REGULAR);
        btnUploadFirmPan.setTypeface(App.LATO_REGULAR);
        btnUploadGST.setTypeface(App.LATO_REGULAR);
        btnUploadAddress.setTypeface(App.LATO_REGULAR);
        btnUploadCheque.setTypeface(App.LATO_REGULAR);
        etAddhar.setTypeface(App.LATO_REGULAR);
        etPan.setTypeface(App.LATO_REGULAR);
        etFirmPan.setTypeface(App.LATO_REGULAR);
        etGST.setTypeface(App.LATO_REGULAR);
        etaddress.setTypeface(App.LATO_REGULAR);
        etcheque.setTypeface(App.LATO_REGULAR);
    }


    private void showDataOnView(Data profileData) {

        if (profileData != null) {
            if (profileData.getProfile_verified().equalsIgnoreCase("0")
                    || profileData.getProfile_verified().equalsIgnoreCase("2")) {
                if (!TextUtil.isStringNullOrBlank(profileData.getDecline_message())) {
                    mywidget.setText(profileData.getDecline_message());
                    mywidget.setVisibility(View.VISIBLE);
                } else {
                    mywidget.setVisibility(View.GONE);
                }
            } else {
                mywidget.setVisibility(View.GONE);
            }
            etEmail.setText((!TextUtil.isStringNullOrBlank(profileData.getPersonal_email_id()) ?profileData.getPersonal_email_id() : ""));
            String datess ="";
            datess =   parseDateToddMMyyyy(profileData.getDob());
            if(datess.length()>0) {
                etDateofBirth.setText((!TextUtil.isStringNullOrBlank(datess) ? datess : ""));
            }
            etAddhar.setText((!TextUtil.isStringNullOrBlank(profileData.getAadhar_no()) ?profileData.getAadhar_no() : ""));
            etPan.setText((!TextUtil.isStringNullOrBlank(profileData.getPan_no()) ? profileData.getPan_no() : ""));
            etFirmPan.setText((!TextUtil.isStringNullOrBlank(profileData.getCompany_pan_no()) ? profileData.getCompany_pan_no() : ""));
            etGST.setText((!TextUtil.isStringNullOrBlank(profileData.getGst_no()) ? profileData.getGst_no() : ""));
            etaddress.setText((!TextUtil.isStringNullOrBlank(profileData.getAddress()) ? profileData.getAddress() : ""));
            Adhar_Url = (!TextUtil.isStringNullOrBlank(profileData.getCp_adhar_proof()) ? profileData.getCp_adhar_proof() : "");
            Pan_Url = (!TextUtil.isStringNullOrBlank(profileData.getCp_pan_proof()) ? profileData.getCp_pan_proof() : "");
            Firm_Pan_Url = (!TextUtil.isStringNullOrBlank(profileData.getFirm_pan_proof()) ? profileData.getFirm_pan_proof() : "");
            int spinvalue = profileData.getUpload_type_option();
            spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, uploadTypeOption);
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
            spUploadOption.setAdapter(spinnerArrayAdapter);
            if(spinvalue==1)
            {

                spUploadOptionPosition= spinnerArrayAdapter.getPosition("Aadhaar");
                Log.e(TAG, "showDataOnView: "+spUploadOptionPosition );
                          spUploadOption.setSelection(spUploadOptionPosition);
            }

            if(spinvalue==2)
            {

                spUploadOptionPosition = spinnerArrayAdapter.getPosition("DL");
                Log.e(TAG, "showDataOnView: "+spUploadOptionPosition );
                spUploadOption.setSelection(spUploadOptionPosition);
            }


            if(spinvalue==3)
            {

               spUploadOptionPosition = spinnerArrayAdapter.getPosition("Voter Id");
                Log.e(TAG, "showDataOnView: "+spUploadOptionPosition );
                spUploadOption.setSelection(spUploadOptionPosition);
            }


            Gst_Url = (!TextUtil.isStringNullOrBlank(profileData.getGst_proof()) ? profileData.getGst_proof() : "");
            Address_Url = (!TextUtil.isStringNullOrBlank(profileData.getAddress_proof()) ? profileData.getAddress_proof() : "");
            Cheque_Url = (!TextUtil.isStringNullOrBlank(profileData.getCheque_proof()) ? profileData.getCheque_proof() : "");
            Passport_Url = (!TextUtil.isStringNullOrBlank(profileData.getPassport_size_photo()) ? profileData.getPassport_size_photo() : "");

            Log.i("PASSPHOTOI", Passport_Url);


        }
        if (!TextUtil.isStringNullOrBlank(Adhar_Url)) {
            String[] Adhar_images = Adhar_Url.split(",");
            AdharAdapter = new UploadRemoveAdapter(Adhar_images, R.layout.doc_upload_remove, context, Constant.K_AADHAAR);
            aadhar_recycler_view.setAdapter(AdharAdapter);
            aadhar_recycler_view.setScrollContainer(false);

        } else {
            String[] Adhar_images = new String[0];
            AdharAdapter = new UploadRemoveAdapter(Adhar_images, R.layout.doc_upload_remove, context, Constant.K_AADHAAR);
            aadhar_recycler_view.setAdapter(AdharAdapter);
            aadhar_recycler_view.setScrollContainer(false);
        }
        if (!TextUtil.isStringNullOrBlank(Pan_Url)) {
            String[] Pan_images = Pan_Url.split(",");
            PanAdapter = new UploadRemoveAdapter(Pan_images, R.layout.doc_upload_remove, context, Constant.K_PAN);
            pan_recycler_view.setAdapter(PanAdapter);
            pan_recycler_view.setScrollContainer(false);

        } else {
            String[] Pan_images = new String[0];
            PanAdapter = new UploadRemoveAdapter(Pan_images, R.layout.doc_upload_remove, context, Constant.K_PAN);
            pan_recycler_view.setAdapter(PanAdapter);
            pan_recycler_view.setScrollContainer(false);
        }
        if (!TextUtil.isStringNullOrBlank(Cheque_Url)) {
            String[] Cheque_images = Cheque_Url.split(",");
            ChequeAdapter = new UploadRemoveAdapter(Cheque_images, R.layout.doc_upload_remove, context, Constant.K_CHEQUE);
            cnclChq_recycler_view.setAdapter(ChequeAdapter);
            cnclChq_recycler_view.setScrollContainer(false);
        } else {
            String[] Cheque_images = new String[0];
            ChequeAdapter = new UploadRemoveAdapter(Cheque_images, R.layout.doc_upload_remove, context, Constant.K_CHEQUE);
            cnclChq_recycler_view.setAdapter(ChequeAdapter);
            cnclChq_recycler_view.setScrollContainer(false);
        }
        if (!TextUtil.isStringNullOrBlank(Passport_Url)) {
            String[] Passport_images = Passport_Url.split(",");
            PassportAdapter = new UploadRemoveAdapter(Passport_images, R.layout.doc_upload_remove, context, Constant.K_PASSPORT);
            pssprt_photo_recycler_view.setAdapter(PassportAdapter);
            pssprt_photo_recycler_view.setScrollContainer(false);

        } else {
            String[] Passport_images = new String[0];
            PassportAdapter = new UploadRemoveAdapter(Passport_images, R.layout.doc_upload_remove, context, Constant.K_PASSPORT);
            pssprt_photo_recycler_view.setAdapter(PassportAdapter);
            pssprt_photo_recycler_view.setScrollContainer(false);
        }
        textCPPANFile.setText(Pan_Url);
        textFirmFile.setText(Firm_Pan_Url);
        textGSTFile.setText(Gst_Url);
        textaddressFile.setText(Address_Url);
        textCHequeFile.setText(Cheque_Url);
        textPassportFile.setText(Passport_Url);

        if (!TextUtil.isStringNullOrBlank(Adhar_Url)) {
            Imcheck01.setVisibility(View.VISIBLE);
        }
        if (!TextUtil.isStringNullOrBlank(Pan_Url)) {
            Imcheck02.setVisibility(View.VISIBLE);
        }

        if (!TextUtil.isStringNullOrBlank(Address_Url)) {
            btnUploadAddress.setText("Done");
        }
        if (!TextUtil.isStringNullOrBlank(Cheque_Url)) {
            Imcheck06.setVisibility(View.VISIBLE);
        }
        if (!TextUtil.isStringNullOrBlank(Passport_Url)) {
            Imcheck07.setVisibility(View.VISIBLE);
        }
        displayUploadOption();
    }

    private void displayUploadOption() {
        spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, uploadTypeOption) {

            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setTypeface(App.LATO_REGULAR);//Typeface for normal view

                return v;
            }

            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                ((TextView) v).setTypeface(App.LATO_REGULAR);//Typeface for dropdown view
                return v;
            }
        };

        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        spUploadOption.setAdapter(spinnerArrayAdapter);

        spUploadOption.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spUploadOptionPosition = position;
                if (spUploadOptionPosition == 1) {
                    selectedUploadType = "aadhaar";
                    etAddhar.setHint("Enter 12 digits aadhar number");
                    etAddhar.setInputType(InputType.TYPE_CLASS_NUMBER);
                    int maxLength = 12;
                    InputFilter[] FilterArray = new InputFilter[1];
                    FilterArray[0] = new InputFilter.LengthFilter(maxLength);
                    etAddhar.setFilters(FilterArray);
                    textCPAdhar.setText(ShowHintOrText.GetMandatory("Aadhar Number"));
                }
                else if (spUploadOptionPosition == 2) {
                    selectedUploadType = "dl";
                    etAddhar.setInputType(InputType.TYPE_CLASS_TEXT);
                    etAddhar.setHint("Enter 15 digits driving licence number");
                    int maxLength = 15;
                    InputFilter[] FilterArray = new InputFilter[1];
                    FilterArray[0] = new InputFilter.LengthFilter(maxLength);
                    etAddhar.setFilters(FilterArray);
                    textCPAdhar.setText(ShowHintOrText.GetMandatory("Driving Licence Number"));
                }

                else if (spUploadOptionPosition == 3) {
                    selectedUploadType = "voterId";
                    etAddhar.setInputType(InputType.TYPE_CLASS_TEXT);
                    etAddhar.setHint("Enter 11 digits voter id number");
                    int maxLength = 11;
                    InputFilter[] FilterArray = new InputFilter[1];
                    FilterArray[0] = new InputFilter.LengthFilter(maxLength);
                    etAddhar.setFilters(FilterArray);
                    textCPAdhar.setText(ShowHintOrText.GetMandatory("Voter Id Number"));
                }
                else
                    {
                        Log.e(TAG, "onItemSelected: " );
                    }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        if (CallEditFOS.profileData != null) {
            for (int i = 0; i < uploadTypeOption.length; i++) {
                if (i == CallEditFOS.profileData.getUpload_type_option()) {
                    this.spUploadOptionPosition = i;
                    Log.i("rtn", this.spUploadOptionPosition + "");
                }
            }
        }
        spUploadOption.setSelection(this.spUploadOptionPosition);
        if (this.spUploadOptionPosition == 1) {
            selectedUploadType = "aadhaar";
            etAddhar.setHint("Enter 12 digits aadhar number");
            textCPAdhar.setText(ShowHintOrText.GetMandatory("Aadhar Number"));
        } else if (this.spUploadOptionPosition == 2) {
            selectedUploadType = "dl";
            etAddhar.setHint("Enter 15 digits driving licence number");
            textCPAdhar.setText(ShowHintOrText.GetMandatory("Driving Licence Number"));
        } else if (this.spUploadOptionPosition == 3) {
            selectedUploadType = "voterId";
            etAddhar.setHint("Enter 11 digits voter id number");
            textCPAdhar.setText(ShowHintOrText.GetMandatory("Voter Id Number"));
        }

    }

    private void viewListener() {
        imgLogout.setOnClickListener(v -> {
            if (network.isNetworkConnected(context)) {
                callLogout();
            } else {
                displaySnackBar.DisplaySnackBar(Constant.NETWIRK_ERROR, Constant.TYPE_ERROR);
            }
        });
        icBack.setOnClickListener(v -> finish());

        etDateofBirth.setOnClickListener(v -> openDatePickerDialog(v));
        btnSave.setOnClickListener(v -> {
            if (!TextUtil.isStringNullOrBlank(callFrom)) {
                if (callFrom.equalsIgnoreCase("1")) {
                    EditProfileRequest editProfileRequest = new EditProfileRequest();
                    callNextStep(editProfileRequest);
                } else {
                    if (isFormValidated()) {
                        if (network.isNetworkConnected(context)) {
                            callUpdateProfile();
                        } else {
                            displaySnackBar.DisplaySnackBar(Constant.NETWIRK_ERROR, Constant.TYPE_ERROR);
                        }
                    }
                }
            } else {
                if (isFormValidated()) {
                    if (network.isNetworkConnected(context)) {
                        callUpdateProfile();
                    } else {
                        displaySnackBar.DisplaySnackBar(Constant.NETWIRK_ERROR, Constant.TYPE_ERROR);
                    }
                }
            }
        });


        Imcamera01.setOnClickListener(view -> {

            if (isStoragePermissionGranted()) {

                try {

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                    File f = getImageFile();
                    if (SDK_INT >= Build.VERSION_CODES.N) {
                        photoURI = FileProvider.getUriForFile(DashboardFOSDocUpload2.this, BuildConfig.APPLICATION_ID.concat(".provider"), f);
                    }
                    else {
                        photoURI = Uri.fromFile(f);
                    }
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    setResult(RESULT_OK, null);
                    startActivityForResult(intent,AADHAR_IMAGE_CLICK_REQUEST );
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        Imattach01.setOnClickListener(view -> {
            if (isStoragePermissionGranted())
                choosePictureFromGallery(AADHAR_IMAGE_ATTACH_REQUEST);
        });
        Imcamera02.setOnClickListener(view -> {
            if (isStoragePermissionGranted())
                dispatchTakePictureIntentReal(PAN_IMAGE_CLICK_REQUEST);
        });
        Imattach02.setOnClickListener(view -> {
            if (isStoragePermissionGranted())
                choosePictureFromGallery(PAN_IMAGE_ATTACH_REQUEST);
        });

        Imcamera06.setOnClickListener(view -> {
            if (isStoragePermissionGranted())
                dispatchTakePictureIntentReal(CHEQUE_IMAGE_CLICK_REQUEST);
        });
        Imattach06.setOnClickListener(view -> {
            if (isStoragePermissionGranted())
                choosePictureFromGallery(CHEQUE_IMAGE_ATTACH_REQUEST);
        });
        Imcamera07.setOnClickListener(view -> {
            if (isStoragePermissionGranted())
                dispatchTakePictureIntentReal(PASSPORT_IMAGE_CLICK_REQUEST);
        });
        Imattach07.setOnClickListener(view -> {
            if (isStoragePermissionGranted())
                choosePictureFromGallery(PASSPORT_IMAGE_ATTACH_REQUEST);
        });
    }


    private void openDatePickerDialog(View v)
    {
        Calendar mcurrentDate = Calendar.getInstance();
        mDay   = mcurrentDate.get(Calendar.DAY_OF_MONTH);
        mMonth = mcurrentDate.get(Calendar.MONTH);
        mYear  = mcurrentDate.get(Calendar.YEAR);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                date = dayOfMonth + "/" + month + "/" + year;
                etDateofBirth.setText(date);

            }
        }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() - 568025136000L);
        datePickerDialog.show();


    }
    File photoFile = null;
    private File createImageFile4() {
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "Rebliss");
        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile = new File(mediaStorageDir.getPath() + File.separator
                + "IMG_" + timeStamp + ".jpg");

        return mediaFile;

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


    private void showSimpleAlertDialog(String message) {
        new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE).setTitleText(Constant.TITLE)
                .setContentText(message)
                .setConfirmClickListener(sweetAlertDialog -> {
                    sweetAlertDialog.dismissWithAnimation();
                     Intent login = new Intent(context, ActivityLogin.class);
                    login.putExtra(Constant.UNAUTHORISE_TOKEN, "0");
                    String token = mySingleton.getData(Constant.DEVICE_FCM_TOKEN);
                    mySingleton.clearData();
                    mySingleton.saveData(Constant.DEVICE_FCM_TOKEN, token);
                    login.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    login.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    login.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(login);
                    finish();
                })
                .show();
    }

    private void showWarningSimpleAlertDialog(String title, String message) {
        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE).setTitleText(title)
                .setContentText(message)
                .setConfirmClickListener(sweetAlertDialog -> sweetAlertDialog.dismissWithAnimation())
                .show();
    }

    private void callNextStep(EditProfileRequest editProfileRequest) {
        Intent stepSecond = new Intent(context, SelfAssessmentQuestionsActivity.class);
        startActivity(stepSecond);

    }
    public boolean isStoragePermissionGranted() {
        if (SDK_INT >= 23) {
            if (checkSelfPermission(WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("permission", "Permission is granted");
                return true;
            } else {

                Log.v("permission", "Permission is revoked");
                ActivityCompat.requestPermissions(DashboardFOSDocUpload2.this, new String[]{WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 1);
                return false;
            }
        }

       else if (SDK_INT >= Build.VERSION_CODES.R) {
            if (Environment.isExternalStorageManager()) {
                Log.e(TAG, "isStoragePermissionGranted: 1234" );
            } else { //request for the permission
                Log.e(TAG, "isStoragePermissionGranted: else 1234" );
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }
        } else if(SDK_INT < Build.VERSION_CODES.R) {
            Log.e(TAG, "isStoragePermissionGranted: below 1234" );
            ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE}, 1);

        }

        else {
            Log.e(TAG, "isStoragePermissionGranted: sdfsdfsfs" );
            Log.v("permission", "Permission is granted");
            return true;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.v("Permission: ", "was " + grantResults[0]);
        }
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        isReloadNeed = false;
        new Handler().postDelayed(() -> runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (resultCode == RESULT_OK && requestCode == FILE_SELECT_ADHAR && data != null) {
                        try {
                            obtainPathResult = new ArrayList<>();
                            obtainPathResult.add(mCurrentPhotoPath);
                            positionForUpload = 0;
                            new GetBase64Image(obtainPathResult, FILE_SELECT_ADHAR).execute();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    else if (resultCode == RESULT_OK && requestCode == FILE_SELECT_CPPAN && data != null) {
                        try {
                            obtainUri = Matisse.obtainResult(data);
                            obtainPathResult = Matisse.obtainPathResult(data);
                            positionForUpload = 0;
                            new GetBase64Image(obtainPathResult, FILE_SELECT_CPPAN).execute();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else if (resultCode == RESULT_OK && requestCode == FILE_SELECT_CHEQUE && data != null) {

                        try {
                            obtainUri = Matisse.obtainResult(data);
                            obtainPathResult = Matisse.obtainPathResult(data);
                            positionForUpload = 0;
                            new GetBase64Image(obtainPathResult, FILE_SELECT_CHEQUE).execute();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else if (resultCode == RESULT_OK && requestCode == FILE_SELECT_PASSPORT && data != null) {
                        try {
                            obtainUri = Matisse.obtainResult(data);
                            obtainPathResult = Matisse.obtainPathResult(data);
                            positionForUpload = 0;
                            new GetBase64Image(obtainPathResult, FILE_SELECT_PASSPORT).execute();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    else if (resultCode == RESULT_OK && requestCode == AADHAR_IMAGE_CLICK_REQUEST) {
                        try{

          currentPhotoPath =   mySingleton.getData("currentpath");
        fileAbsolutePath =   mySingleton.getData("fileabsolutepath");
        File f = new File(fileAbsolutePath);

        Log.e("TAG", "runnnn:>>>> "+f.toString() );

        Log.e("TAG", "absolute " + f.getAbsolutePath());
        Log.e("TAG", "path " + f.getPath());
        InputStream inputStream = (InputStream) new URL("file://" + f.getAbsolutePath()).getContent();
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inSampleSize = 2;
        if (SDK_INT >= 24) {
            photoURI = FileProvider.getUriForFile(DashboardFOSDocUpload2.this,
                    BuildConfig.APPLICATION_ID + ".provider", f);
        } else {
            photoURI = Uri.fromFile(f);
        }
        int rotateImage = Util.getCameraPhotoOrientation(DashboardFOSDocUpload2.this, photoURI, f.getAbsolutePath());
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null, bitmapOptions);
        Matrix matrix = new Matrix();
        matrix.setRotate(rotateImage, (float) bitmap.getWidth() / 2, (float) bitmap.getHeight() / 2);
        Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmapOptions.outWidth, bitmapOptions.outHeight, matrix, true);
        OutputStream outFile = new FileOutputStream(f);
        rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 60, outFile);
        outFile.flush();
        outFile.close();
        nbase64 = encodeTobase64(rotatedBitmap);
        Log.e("TAG", "onActivityResult: "+nbase64 );

         callPostDocAPI(f, 101, ".jpg", nbase64);

    } catch (Exception e) {
        e.printStackTrace();
    }
                    }
                    else if (resultCode == RESULT_OK && requestCode == AADHAR_IMAGE_ATTACH_REQUEST && data != null) {
                        try {
                            Uri selectedImageUri = data.getData();
                            selectedImagePath = getPath(selectedImageUri);
                            Log.i("GalleryPath", selectedImagePath);
                            obtainPathResult = new ArrayList<>();
                            obtainPathResult.add(selectedImagePath);
                            positionForUpload = 0;
                            new GetBase64Image(obtainPathResult, AADHAR_IMAGE_ATTACH_REQUEST).execute();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else if (resultCode == RESULT_OK && requestCode == PAN_IMAGE_ATTACH_REQUEST && data != null) {
                        try {
                            Uri selectedImageUri = data.getData();
                            selectedImagePath = getPath(selectedImageUri);
                            Log.i("GalleryPath", selectedImagePath);
                            obtainPathResult = new ArrayList<>();
                            obtainPathResult.add(selectedImagePath);
                            positionForUpload = 0;
                            new GetBase64Image(obtainPathResult, PAN_IMAGE_ATTACH_REQUEST).execute();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else if (resultCode == RESULT_OK && requestCode == PAN_IMAGE_CLICK_REQUEST) {

                        File f = grabImageFile(true, 80);
                        if (f != null) {

                            obtainPathResult = new ArrayList<>();
                            obtainPathResult.add(f.getAbsolutePath());
                            new GetBase64Image(obtainPathResult, PAN_IMAGE_CLICK_REQUEST).execute();
                        } else {
                            Toast.makeText(getApplicationContext(), "Image Capture Error", Toast.LENGTH_LONG).show();
                        }
                    } else if (resultCode == RESULT_OK && requestCode == CHEQUE_IMAGE_ATTACH_REQUEST && data != null) {
                        try {
                            Uri selectedImageUri = data.getData();
                            selectedImagePath = getPath(selectedImageUri);
                            Log.i("GalleryPath", selectedImagePath);
                            obtainPathResult = new ArrayList<>();
                            obtainPathResult.add(selectedImagePath);
                            positionForUpload = 0;
                            new GetBase64Image(obtainPathResult, CHEQUE_IMAGE_ATTACH_REQUEST).execute();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else if (resultCode == RESULT_OK && requestCode == CHEQUE_IMAGE_CLICK_REQUEST) {

                        File f = grabImageFile(true, 80);
                        if (f != null) {
                            obtainPathResult = new ArrayList<>();
                            obtainPathResult.add(f.getAbsolutePath());
                            new GetBase64Image(obtainPathResult, CHEQUE_IMAGE_CLICK_REQUEST).execute();
                        }
                    } else if (resultCode == RESULT_OK && requestCode == PASSPORT_IMAGE_ATTACH_REQUEST && data != null) {
                        try {
                            Uri selectedImageUri = data.getData();
                            selectedImagePath = getPath(selectedImageUri);
                            Log.i("GalleryPath", selectedImagePath);
                            obtainPathResult = new ArrayList<>();
                            obtainPathResult.add(selectedImagePath);
                            positionForUpload = 0;
                            new GetBase64Image(obtainPathResult, PASSPORT_IMAGE_ATTACH_REQUEST).execute();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else if (resultCode == RESULT_OK && requestCode == PASSPORT_IMAGE_CLICK_REQUEST) {

                        File f = grabImageFile(true, 80);
                        if (f != null) {

                            obtainPathResult = new ArrayList<>();
                            obtainPathResult.add(f.getAbsolutePath());
                            new GetBase64Image(obtainPathResult, PASSPORT_IMAGE_CLICK_REQUEST).execute();
                        } else {
                            Toast.makeText(getApplicationContext(), "Image Capture Error", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Please reselect media", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }), 500);

    }

    public String checkDigit(int number) {
        return number <= 9 ? "0" + number : String.valueOf(number);
    }

    public final boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    private boolean isFormValidated() {
        boolean status = true;

        sAddhar = etAddhar.getText().toString().trim();
        sPan = etPan.getText().toString().trim();
        sFirmPan = etFirmPan.getText().toString().trim();
        sGST = etGST.getText().toString().trim();
        saddress = etaddress.getText().toString().trim();
        scheque = etcheque.getText().toString().trim();
        sEmail = etEmail.getText().toString().trim();
        sUploadOption = spUploadOptionPosition;
        Log.i("UPLD", sUploadOption + "");
        Log.i("Passport_Url", Passport_Url + "");

        if (date.length() <= 0) {
            status = false;
            showWarningSimpleAlertDialog(Constant.TITLE, "Please enter Date of Birth");
        }

        if (selectedUploadType.equalsIgnoreCase("aadhaar") || sUploadOption == 1) {
            if (sAddhar.length() < 12) {
                status = false;
                showWarningSimpleAlertDialog(Constant.TITLE, "Please enter  valid Aadhaar No");
            }
        }

        else if (selectedUploadType.equalsIgnoreCase("dl") || sUploadOption == 2) {
            if (sAddhar.length() < 15) {
                status = false;
                showWarningSimpleAlertDialog(Constant.TITLE, "Please enter  valid Driving Licence No");
            }
        } else if (selectedUploadType.equalsIgnoreCase("voterId") || sUploadOption == 3) {
            if (sAddhar.length() < 11) {
                status = false;
                showWarningSimpleAlertDialog(Constant.TITLE, "Please enter  valid Voter Id No");
            }
        } else if (selectedUploadType.equalsIgnoreCase("")) {
            status = false;
            showWarningSimpleAlertDialog(Constant.TITLE, "Please select document to upload");
        } else if (sEmail.isEmpty()) {
            status = false;
            showWarningSimpleAlertDialog(Constant.TITLE, "Please enter Email");
        } else if (!isValidEmail(sEmail)) {
            status = false;
            showWarningSimpleAlertDialog(Constant.TITLE, "Please enter valid Email");
        }

        if(!RegexUtils.isValidEmail(etEmail.getText().toString()))
        {
            status = false;
            showWarningSimpleAlertDialog(Constant.TITLE, "Please enter valid Email");
        }

        if (Adhar_Url == null || Adhar_Url.length() <= 0) {
            status = false;
            if (selectedUploadType.equalsIgnoreCase("aadhaar")) {
                showWarningSimpleAlertDialog(Constant.TITLE, "Please Upload  Aadhaar proof");
            } else if (selectedUploadType.equalsIgnoreCase("dl")) {
                showWarningSimpleAlertDialog(Constant.TITLE, "Please Upload Driving Licence proof");
            } else if (selectedUploadType.equalsIgnoreCase("voterId")) {
                showWarningSimpleAlertDialog(Constant.TITLE, "Please Upload Voter Id proof");
            } else {
                showWarningSimpleAlertDialog(Constant.TITLE, "Please select document to upload");
            }

        }

        if (selectedUploadType.equalsIgnoreCase("aadhaar")) {
            String[] strings = Adhar_Url.split(",");
            if (strings.length < 2) {
                status = false;
                showWarningSimpleAlertDialog(Constant.TITLE, "Please add both side of Aadhaar proof");
            }
        } else if (selectedUploadType.equalsIgnoreCase("dl")) {
            String[] strings = Adhar_Url.split(",");
            if (strings.length < 2) {
                status = false;
                showWarningSimpleAlertDialog(Constant.TITLE, "Please add both side of Driving Licence proof");
            }
        } else if (selectedUploadType.equalsIgnoreCase("voterId")) {
            String[] strings = Adhar_Url.split(",");
            if (strings.length < 2) {
                status = false;
                showWarningSimpleAlertDialog(Constant.TITLE, "Please add both side of Voter Id proof");
            }
        } else {
            showWarningSimpleAlertDialog(Constant.TITLE, "Please select document to upload");
        }

        return status;
    }

    public void callProfileAPI() {

        kProgressHUD = KProgressHUD.create(context)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .setWindowColor(getResources().getColor(R.color.progressbar_color))
                .show();

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        final Call<ProfileResponce> call = apiService.getProfile(mySingleton.getData(Constant.TOKEN_BASE_64));
        call.enqueue(new Callback<ProfileResponce>() {
            @Override
            public void onResponse(Call<ProfileResponce> call, Response<ProfileResponce> response) {
                kProgressHUD.dismiss();
                if (response.isSuccessful()) {
                    if (response.code() >= 200 && response.code() < 700) {
                        if (response.code() == 200) {
                            if (response.body().getStatus() == 1) {
                                if (response.body().getData() != null) {
                                    Gson gson = new Gson();
                                    String json = gson.toJson(response.body(), ProfileResponce.class);
                                    Log.i(TAG, "json " + json);
                                    profileData = response.body().getData();
                                    showDataOnView(profileData);
                                } else {
                                    displaySnackBar.DisplaySnackBar("Data not found", Constant.TYPE_ERROR);

                                }
                            } else if (response.body().getStatus() == 0) {
                                Log.e(TAG, "onResponse: 0000" );                            }
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
                        if (errorBody.getName().equalsIgnoreCase(context.getString(R.string.unAuthorisedUser))) {
                            Logout.Login(context);
                        }
                    } catch (Exception e) {
                        displaySnackBar.DisplaySnackBar(Constant.ERROR_INVALID_JSON, Constant.TYPE_ERROR);

                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<ProfileResponce> call, Throwable t) {
                kProgressHUD.dismiss();
                if (t != null && (t instanceof IOException || t instanceof SocketTimeoutException
                        || t instanceof ConnectException || t instanceof NoRouteToHostException
                        || t instanceof SecurityException)) {
                    displaySnackBar.DisplaySnackBar(Constant.NETWIRK_ERROR, Constant.TYPE_ERROR);
                }
            }
        });
    }

    public void callLogout() {


        kProgressHUD = KProgressHUD.create(context)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .setWindowColor(getResources().getColor(R.color.progressbar_color))
                .show();

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        final Call<LogoutResponce> call = apiService.getUserLogout(mySingleton.getData(Constant.TOKEN_BASE_64));
        call.enqueue(new Callback<LogoutResponce>() {
            @Override
            public void onResponse(Call<LogoutResponce> call, Response<LogoutResponce> response) {
                kProgressHUD.dismiss();
                if (response.isSuccessful()) {
                    if (response.code() >= 200 && response.code() < 700) {
                        if (response.code() == 200) {
                            if (response.body().getStatus() == 1) {
                                if (response.body() != null) {
                                    Gson gson = new Gson();
                                    String json = gson.toJson(response.body(), LogoutResponce.class);
                                    Log.i(TAG, "json " + json);
                                    showSimpleAlertDialog(response.body().getMessage());
                                } else {
                                    displaySnackBar.DisplaySnackBar("Data not found", Constant.TYPE_ERROR);

                                }
                            } else if (response.body().getStatus() == 0) {
                                Log.e(TAG, "onResponse: 0000" );                            }
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
                        if (errorBody.getName().equalsIgnoreCase(context.getString(R.string.unAuthorisedUser))) {
                            Logout.Login(context);
                        }
                    } catch (Exception e) {
                        displaySnackBar.DisplaySnackBar(Constant.ERROR_INVALID_JSON, Constant.TYPE_ERROR);
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<LogoutResponce> call, Throwable t) {
                kProgressHUD.dismiss();
                if (t != null && (t instanceof IOException || t instanceof SocketTimeoutException
                        || t instanceof ConnectException || t instanceof NoRouteToHostException
                        || t instanceof SecurityException)) {
                    displaySnackBar.DisplaySnackBar(Constant.NETWIRK_ERROR, Constant.TYPE_ERROR);

                }
            }
        });
    }

    public void callUpdateProfile() {

        if (editProfileRequest == null) {
            editProfileRequest = new EditProfileRequest();
        }
        if (editProfileRequest != null) {
            editProfileRequest.setAadhar_no(sAddhar);
            editProfileRequest.setCp_adhar_proof(Adhar_Url);
            Log.e(TAG, "uploadoption: "+sUploadOption );
            editProfileRequest.setUpload_type_option(sUploadOption);
            editProfileRequest.setPan_no(sPan);
            editProfileRequest.setCp_pan_proof(Pan_Url);
            editProfileRequest.setAddress(saddress);
            editProfileRequest.setCheque_proof(Cheque_Url);
            editProfileRequest.setPassport_size_photo(Passport_Url);
            editProfileRequest.setPersonal_email_id(sEmail);
            editProfileRequest.setDob(date);
            Gson gson = new Gson();
            String json = gson.toJson(editProfileRequest, EditProfileRequest.class);
            Log.i(TAG, "Request Data " + json);
            kProgressHUD = KProgressHUD.create(context)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setCancellable(false)
                    .setAnimationSpeed(2)
                    .setDimAmount(0.5f)
                    .setWindowColor(getResources().getColor(R.color.progressbar_color))
                    .show();

            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            final Call<EditProfileResponce> call = apiService.postEditProfile(mySingleton.getData(Constant.TOKEN_BASE_64), editProfileRequest);
            call.enqueue(new Callback<EditProfileResponce>() {
                @Override
                public void onResponse(Call<EditProfileResponce> call, Response<EditProfileResponce> response) {
                    kProgressHUD.dismiss();
                    if (response.isSuccessful()) {
                        if (response.code() >= 200 && response.code() < 700) {
                            if (response.code() == 200) {
                                Gson gson = new Gson();
                                String json = gson.toJson(response.body(), EditProfileResponce.class);
                                Log.i(TAG, "json " + json);
                                if (response.body().getStatus() == 1) {
                                    if (response.body().getData() != null) {
                                        callNextStep(editProfileRequest);
                                    } else {
                                        displaySnackBar.DisplaySnackBar("Data not found", Constant.TYPE_ERROR);
                                    }
                                } else if (response.body().getStatus() == 0) {
                                    if (response.body().getData().getMessage().getPersonal_email_id() != null) {
                                        showWarningSimpleAlertDialog(Constant.TITLE, response.body().getData().getMessage().getPersonal_email_id().get(0));
                                    }

                                    if (response.body().getData().getMessage().getOfficial_email_id() != null) {
                                        showWarningSimpleAlertDialog(Constant.TITLE, response.body().getData().getMessage().getOfficial_email_id().get(0));
                                    }
                                    if (response.body().getData().getMessage().getAadhar_no() != null) {
                                        showWarningSimpleAlertDialog(Constant.TITLE, response.body().getData().getMessage().getAadhar_no().get(0));
                                    }
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
                            if (errorBody.getName().equalsIgnoreCase(context.getString(R.string.unAuthorisedUser))) {
                                Logout.Login(context);
                            }
                        } catch (Exception e) {
                            displaySnackBar.DisplaySnackBar(Constant.ERROR_INVALID_JSON, Constant.TYPE_ERROR);

                            e.printStackTrace();
                        }

                    }
                }

                @Override
                public void onFailure(Call<EditProfileResponce> call, Throwable t) {
                    kProgressHUD.dismiss();
                    if (t != null && (t instanceof IOException || t instanceof SocketTimeoutException
                            || t instanceof ConnectException || t instanceof NoRouteToHostException
                            || t instanceof SecurityException)) {
                        displaySnackBar.DisplaySnackBar(Constant.NETWIRK_ERROR, Constant.TYPE_ERROR);

                    }
                }
            });
        }
    }

    private void showWarningAddMoreAdhar(String title, String message, final int selectionType) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        if (selectionType == FILE_SELECT_ADHAR || selectionType == AADHAR_IMAGE_ATTACH_REQUEST) {
                            choosePictureFromGallery(selectionType);
                        } else if (selectionType == AADHAR_IMAGE_CLICK_REQUEST) {
                            if (isStoragePermissionGranted())
                                dispatchTakePictureIntentReal(selectionType);
                        }
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void deleteUpload(String type, String position) {
        if (type.equalsIgnoreCase(Constant.K_AADHAAR)) {
            String[] temp = Adhar_Url.split(",");
            String New = "";
            for (int i = 0; i < temp.length; i++) {
                if (temp[i].equalsIgnoreCase(position)) {
                    continue;
                } else {
                    if (!TextUtil.isStringNullOrBlank(New)) {
                        New = New + "," + temp[i];
                    } else {
                        New = New + temp[i];
                    }
                }
            }
            Adhar_Url = New;
        } else if (type.equalsIgnoreCase(Constant.K_PAN)) {
            Log.i("newww1", position);

            String[] temp = Pan_Url.split(",");
            String New = "";
            for (int i = 0; i < temp.length; i++) {
                if (temp[i].equalsIgnoreCase(position)) {
                    continue;
                } else {
                    if (!TextUtil.isStringNullOrBlank(New)) {
                        New = New + "," + temp[i];
                    } else {
                        New = New + temp[i];
                    }
                }
            }
            Pan_Url = New;

        } else if (type.equalsIgnoreCase(Constant.K_FIRM_PAN)) {
            String[] temp = Firm_Pan_Url.split(",");
            String New = "";
            for (int i = 0; i < temp.length; i++) {
                if (temp[i].equalsIgnoreCase(position)) {
                    continue;
                } else {
                    if (!TextUtil.isStringNullOrBlank(New)) {
                        New = New + "," + temp[i];
                    } else {
                        New = New + temp[i];
                    }
                }
            }
            Firm_Pan_Url = New;
        } else if (type.equalsIgnoreCase(Constant.K_ADDRESS)) {
            String[] temp = Address_Url.split(",");
            String New = "";
            for (int i = 0; i < temp.length; i++) {
                if (temp[i].equalsIgnoreCase(position)) {
                    continue;
                } else {
                    if (!TextUtil.isStringNullOrBlank(New)) {
                        New = New + "," + temp[i];
                    } else {
                        New = New + temp[i];
                    }
                }
            }
            Address_Url = New;
        } else if (type.equalsIgnoreCase(Constant.K_GST)) {
            String[] temp = Gst_Url.split(",");
            String New = "";
            for (int i = 0; i < temp.length; i++) {
                if (temp[i].equalsIgnoreCase(position)) {
                    continue;
                } else {
                    if (!TextUtil.isStringNullOrBlank(New)) {
                        New = New + "," + temp[i];
                    } else {
                        New = New + temp[i];
                    }
                }
            }
            Gst_Url = New;
        } else if (type.equalsIgnoreCase(Constant.K_CHEQUE)) {
            String[] temp = Cheque_Url.split(",");
            String New = "";
            for (int i = 0; i < temp.length; i++) {
                if (temp[i].equalsIgnoreCase(position)) {
                    continue;
                } else {
                    if (!TextUtil.isStringNullOrBlank(New)) {
                        New = New + "," + temp[i];
                    } else {
                        New = New + temp[i];
                    }
                }
            }
            Cheque_Url = New;
        } else if (type.equalsIgnoreCase(Constant.K_PASSPORT)) {
            String[] temp = Passport_Url.split(",");
            String New = "";
            for (int i = 0; i < temp.length; i++) {
                if (temp[i].equalsIgnoreCase(position)) {
                    continue;
                } else {
                    if (!TextUtil.isStringNullOrBlank(New)) {
                        New = New + "," + temp[i];
                    } else {
                        New = New + temp[i];
                    }
                }
            }
            Passport_Url = New;
        }

    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    private class GetBase64Image extends AsyncTask<String, String, String> {
        List<String> file;
        int CallType;
        String extension = "";

        public GetBase64Image(final List<String> file, final int CallType) {
            this.file = file;
            this.CallType = CallType;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (positionForUpload == 0) {
                kProgressHUD = KProgressHUD.create(context)
                        .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                        .setCancellable(false)
                        .setAnimationSpeed(2)
                        .setDimAmount(0.5f)
                        .setWindowColor(getResources().getColor(R.color.progressbar_color))
                        .show();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            String base64 = "";
            String response = "";
            if (file != null && file.size() > 0 && (!TextUtil.isStringNullOrBlank(file.get(positionForUpload)))) {
                extension = file.get(positionForUpload).substring(file.get(positionForUpload).lastIndexOf("."));
                try {
                    base64 = encodeFileToBase64Binary(file.get(positionForUpload));
                    Log.i("base", base64);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return base64;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
     kProgressHUD.dismiss();
            if (s != null) {
                callPostDocAPIs(file, CallType, extension, s);
            }
        }
    }

    public void callPostDocAPI(final File file, final int CallType, String extention, String base64) {
        kProgressHUD = KProgressHUD.create(context)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .setWindowColor(getResources().getColor(R.color.progressbar_color))
                .show();
        UploadRequest uploadRequest = new UploadRequest();
        if (!TextUtil.isStringNullOrBlank(base64)) {
            uploadRequest.setId_proof(base64);
            uploadRequest.setId_proof_file_type(extention.replace(".", ""));
        }
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        final Call<FileUploadResponce> call = apiService.postUploadfile(uploadRequest);
        call.enqueue(new Callback<FileUploadResponce>() {
            @Override
            public void onResponse(Call<FileUploadResponce> call, Response<FileUploadResponce> response) {
                kProgressHUD.dismiss();
                if (response.isSuccessful()) {
                    if (response.code() >= 200 && response.code() < 700) {
                        if (response.code() == 200) {
                            if (response.body().getStatus() == 1) {
                                if (response.body().getData() != null) {
                                    Gson gson = new Gson();
                                    String json = gson.toJson(response.body(), FileUploadResponce.class);
                                    Log.i(TAG, "json " + json);
                                    if (CallType == FILE_SELECT_ADHAR || CallType == AADHAR_IMAGE_ATTACH_REQUEST || CallType == AADHAR_IMAGE_CLICK_REQUEST) {
                                        if (TextUtil.isStringNullOrBlank(Adhar_Url)) {
                                            Adhar_Url = Adhar_Url + response.body().getFile_name();
                                        } else {
                                            Adhar_Url = Adhar_Url + "," + response.body().getFile_name();
                                        }
                                        if (!TextUtil.isStringNullOrBlank(Adhar_Url)) {
                                            String[] Adhar_images = Adhar_Url.split(",");
                                            if (AdharAdapter != null) {
                                                AdharAdapter.updateData(Adhar_images);
                                            } else {
                                                AdharAdapter = new UploadRemoveAdapter(Adhar_images, R.layout.doc_upload_remove, context, Constant.K_AADHAAR);
                                                aadhar_recycler_view.setAdapter(AdharAdapter);
                                                aadhar_recycler_view.setScrollContainer(false);
                                            }
                                        }
                                        Imcheck01.setVisibility(View.VISIBLE);
                                        runOnUiThread(() -> {
                                            if (selectedUploadType.equalsIgnoreCase("aadhaar")) {
                                                String[] splitAdhar = Adhar_Url.split(",");
                                                if (splitAdhar.length <= 1) {
                                                    showWarningAddMoreAdhar("Message!", "Add one more photo for Aadhaar", CallType);
                                                }
                                            } else if (selectedUploadType.equalsIgnoreCase("dl")) {
                                                String[] splitAdhar = Adhar_Url.split(",");
                                                if (splitAdhar.length <= 1) {
                                                    showWarningAddMoreAdhar("Message!", "Add one more photo for Driving Licence", CallType);
                                                }
                                            } else if (selectedUploadType.equalsIgnoreCase("voterId")) {
                                                String[] splitAdhar = Adhar_Url.split(",");
                                                if (splitAdhar.length <= 1) {
                                                    showWarningAddMoreAdhar("Message!", "Add one more photo for Voter Id", CallType);
                                                }
                                            }
                                        });


                                    } else if (CallType == FILE_SELECT_CPPAN || CallType == PAN_IMAGE_ATTACH_REQUEST || CallType == PAN_IMAGE_CLICK_REQUEST) {
                                        if (TextUtil.isStringNullOrBlank(Pan_Url)) {
                                            Pan_Url = Pan_Url + response.body().getFile_name();
                                        } else {
                                            Pan_Url = Pan_Url + "," + response.body().getFile_name();
                                        }

                                        if (!TextUtil.isStringNullOrBlank(Pan_Url)) {
                                            String[] Pan_images = Pan_Url.split(",");
                                            if (PanAdapter != null) {
                                                PanAdapter.updateData(Pan_images);
                                            } else {
                                                PanAdapter = new UploadRemoveAdapter(Pan_images, R.layout.doc_upload_remove, context, Constant.K_PAN);
                                                pan_recycler_view.setAdapter(PanAdapter);
                                                pan_recycler_view.setScrollContainer(false);
                                            }
                                        }
                                        Imcheck02.setVisibility(View.VISIBLE);
                                    } else if (CallType == FILE_SELECT_CHEQUE || CallType == CHEQUE_IMAGE_ATTACH_REQUEST || CallType == CHEQUE_IMAGE_CLICK_REQUEST) {
                                        if (TextUtil.isStringNullOrBlank(Cheque_Url)) {
                                            Cheque_Url = Cheque_Url + response.body().getFile_name();
                                        } else {
                                            Cheque_Url = Cheque_Url + "," + response.body().getFile_name();
                                        }

                                        if (!TextUtil.isStringNullOrBlank(Cheque_Url)) {
                                            String[] Cheque_images = Cheque_Url.split(",");

                                            if (ChequeAdapter != null) {
                                                ChequeAdapter.updateData(Cheque_images);
                                            } else {
                                                ChequeAdapter = new UploadRemoveAdapter(Cheque_images, R.layout.doc_upload_remove, context, Constant.K_CHEQUE);
                                                cnclChq_recycler_view.setAdapter(ChequeAdapter);
                                                cnclChq_recycler_view.setScrollContainer(false);
                                            }
                                        }
                                        Imcheck06.setVisibility(View.VISIBLE);
                                    } else if (CallType == FILE_SELECT_PASSPORT || CallType == PASSPORT_IMAGE_ATTACH_REQUEST || CallType == PASSPORT_IMAGE_CLICK_REQUEST) {
                                        if (TextUtil.isStringNullOrBlank(Passport_Url)) {
                                            Passport_Url = Passport_Url + response.body().getFile_name();
                                        } else {
                                            Passport_Url = Passport_Url + "," + response.body().getFile_name();
                                        }
                                        if (!TextUtil.isStringNullOrBlank(Passport_Url)) {
                                            String[] Passport_images = Passport_Url.split(",");

                                            if (PassportAdapter != null) {
                                                PassportAdapter.updateData(Passport_images);
                                            } else {
                                                PassportAdapter = new UploadRemoveAdapter(Passport_images, R.layout.doc_upload_remove, context, Constant.K_PASSPORT);
                                                pssprt_photo_recycler_view.setAdapter(PassportAdapter);
                                                pssprt_photo_recycler_view.setScrollContainer(false);
                                            }
                                        }
                                        Imcheck07.setVisibility(View.VISIBLE);
                                    }
                                } else {
                                    displaySnackBar.DisplaySnackBar("Data not found", Constant.TYPE_ERROR);
                                }
                            } else if (response.body().getStatus() == 0) {
                                Log.e(TAG, "onResponse: 0000" );                            }
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
                        if (errorBody.getName().equalsIgnoreCase(context.getString(R.string.unAuthorisedUser))) {
                            Logout.Login(context);
                        }
                    } catch (Exception e) {
                        displaySnackBar.DisplaySnackBar(Constant.ERROR_INVALID_JSON, Constant.TYPE_ERROR);
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<FileUploadResponce> call, Throwable t) {
                kProgressHUD.dismiss();
                if (t != null && (t instanceof IOException || t instanceof SocketTimeoutException
                        || t instanceof ConnectException || t instanceof NoRouteToHostException
                        || t instanceof SecurityException)) {
                    displaySnackBar.DisplaySnackBar(Constant.NETWIRK_ERROR, Constant.TYPE_ERROR);

                }
            }
        });
    }
    public void callPostDocAPIs(final List<String> file, final int CallType, String extention, String base64)      {
        kProgressHUD = KProgressHUD.create(context)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .setWindowColor(getResources().getColor(R.color.progressbar_color))
                .show();
        UploadRequest uploadRequest = new UploadRequest();
        if (!TextUtil.isStringNullOrBlank(base64)) {
            uploadRequest.setId_proof(base64);
            uploadRequest.setId_proof_file_type(extention.replace(".", ""));
        }
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        final Call<FileUploadResponce> call = apiService.postUploadfile(uploadRequest);
        call.enqueue(new Callback<FileUploadResponce>() {
            @Override
            public void onResponse(Call<FileUploadResponce> call, Response<FileUploadResponce> response) {
                kProgressHUD.dismiss();
                if (response.isSuccessful()) {
                    if (response.code() >= 200 && response.code() < 700) {
                        if (response.code() == 200) {
                            if (response.body().getStatus() == 1) {
                                if (response.body().getData() != null) {
                                    Gson gson = new Gson();
                                    String json = gson.toJson(response.body(), FileUploadResponce.class);
                                    Log.i(TAG, "json " + json);
                                    if (CallType == FILE_SELECT_ADHAR || CallType == AADHAR_IMAGE_ATTACH_REQUEST || CallType == AADHAR_IMAGE_CLICK_REQUEST) {
                                        if (TextUtil.isStringNullOrBlank(Adhar_Url)) {
                                            Adhar_Url = Adhar_Url + response.body().getFile_name();
                                        } else {
                                            Adhar_Url = Adhar_Url + "," + response.body().getFile_name();
                                        }
                                        if (!TextUtil.isStringNullOrBlank(Adhar_Url)) {
                                            String[] Adhar_images = Adhar_Url.split(",");
                                            if (AdharAdapter != null) {
                                                AdharAdapter.updateData(Adhar_images);
                                            } else {
                                                AdharAdapter = new UploadRemoveAdapter(Adhar_images, R.layout.doc_upload_remove, context, Constant.K_AADHAAR);
                                                aadhar_recycler_view.setAdapter(AdharAdapter);
                                                aadhar_recycler_view.setScrollContainer(false);
                                            }
                                        }
                                        Imcheck01.setVisibility(View.VISIBLE);
                                        runOnUiThread(() -> {
                                            if (selectedUploadType.equalsIgnoreCase("aadhaar")) {
                                                String[] splitAdhar = Adhar_Url.split(",");
                                                if (splitAdhar.length <= 1) {
                                                    showWarningAddMoreAdhar("Message!", "Add one more photo for Aadhaar", CallType);
                                                }
                                            } else if (selectedUploadType.equalsIgnoreCase("dl")) {
                                                String[] splitAdhar = Adhar_Url.split(",");
                                                if (splitAdhar.length <= 1) {
                                                    showWarningAddMoreAdhar("Message!", "Add one more photo for Driving Licence", CallType);
                                                }
                                            } else if (selectedUploadType.equalsIgnoreCase("voterId")) {
                                                String[] splitAdhar = Adhar_Url.split(",");
                                                if (splitAdhar.length <= 1) {
                                                    showWarningAddMoreAdhar("Message!", "Add one more photo for Voter Id", CallType);
                                                }
                                            }
                                        });


                                    } else if (CallType == FILE_SELECT_CPPAN || CallType == PAN_IMAGE_ATTACH_REQUEST || CallType == PAN_IMAGE_CLICK_REQUEST) {
                                        if (TextUtil.isStringNullOrBlank(Pan_Url)) {
                                            Pan_Url = Pan_Url + response.body().getFile_name();
                                        } else {
                                            Pan_Url = Pan_Url + "," + response.body().getFile_name();
                                        }

                                        if (!TextUtil.isStringNullOrBlank(Pan_Url)) {
                                            String[] Pan_images = Pan_Url.split(",");
                                            if (PanAdapter != null) {
                                                PanAdapter.updateData(Pan_images);
                                            } else {
                                                PanAdapter = new UploadRemoveAdapter(Pan_images, R.layout.doc_upload_remove, context, Constant.K_PAN);
                                                pan_recycler_view.setAdapter(PanAdapter);
                                                pan_recycler_view.setScrollContainer(false);
                                            }
                                        }
                                        Imcheck02.setVisibility(View.VISIBLE);
                                    } else if (CallType == FILE_SELECT_CHEQUE || CallType == CHEQUE_IMAGE_ATTACH_REQUEST || CallType == CHEQUE_IMAGE_CLICK_REQUEST) {
                                        if (TextUtil.isStringNullOrBlank(Cheque_Url)) {
                                            Cheque_Url = Cheque_Url + response.body().getFile_name();
                                        } else {
                                            Cheque_Url = Cheque_Url + "," + response.body().getFile_name();
                                        }

                                        if (!TextUtil.isStringNullOrBlank(Cheque_Url)) {
                                            String[] Cheque_images = Cheque_Url.split(",");

                                            if (ChequeAdapter != null) {
                                                ChequeAdapter.updateData(Cheque_images);
                                            } else {
                                                ChequeAdapter = new UploadRemoveAdapter(Cheque_images, R.layout.doc_upload_remove, context, Constant.K_CHEQUE);
                                                cnclChq_recycler_view.setAdapter(ChequeAdapter);
                                                cnclChq_recycler_view.setScrollContainer(false);
                                            }
                                        }
                                        Imcheck06.setVisibility(View.VISIBLE);
                                    } else if (CallType == FILE_SELECT_PASSPORT || CallType == PASSPORT_IMAGE_ATTACH_REQUEST || CallType == PASSPORT_IMAGE_CLICK_REQUEST) {
                                        if (TextUtil.isStringNullOrBlank(Passport_Url)) {
                                            Passport_Url = Passport_Url + response.body().getFile_name();
                                        } else {
                                            Passport_Url = Passport_Url + "," + response.body().getFile_name();
                                        }
                                        if (!TextUtil.isStringNullOrBlank(Passport_Url)) {
                                            String[] Passport_images = Passport_Url.split(",");

                                            if (PassportAdapter != null) {
                                                PassportAdapter.updateData(Passport_images);
                                            } else {
                                                PassportAdapter = new UploadRemoveAdapter(Passport_images, R.layout.doc_upload_remove, context, Constant.K_PASSPORT);
                                                pssprt_photo_recycler_view.setAdapter(PassportAdapter);
                                                pssprt_photo_recycler_view.setScrollContainer(false);
                                            }
                                        }
                                        Imcheck07.setVisibility(View.VISIBLE);
                                    }
                                } else {
                                    displaySnackBar.DisplaySnackBar("Data not found", Constant.TYPE_ERROR);
                                }
                            } else if (response.body().getStatus() == 0) {
                                Log.e(TAG, "onResponse: status 0" );                            }
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
                        if (errorBody.getName().equalsIgnoreCase(context.getString(R.string.unAuthorisedUser))) {
                            Logout.Login(context);
                        }
                    } catch (Exception e) {
                        displaySnackBar.DisplaySnackBar(Constant.ERROR_INVALID_JSON, Constant.TYPE_ERROR);
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<FileUploadResponce> call, Throwable t) {
                kProgressHUD.dismiss();
                if (t != null && (t instanceof IOException || t instanceof SocketTimeoutException
                        || t instanceof ConnectException || t instanceof NoRouteToHostException
                        || t instanceof SecurityException)) {
                    displaySnackBar.DisplaySnackBar(Constant.NETWIRK_ERROR, Constant.TYPE_ERROR);

                }
            }
        });
    }

    private String encodeFileToBase64Binary(String fileName)
            throws IOException {

        InputStream inputStream = null;
        inputStream = new FileInputStream(fileName);
        byte[] buffer = new byte[8192];
        int bytesRead;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        Base64OutputStream output64 = new Base64OutputStream(output, Base64.DEFAULT);
        try {
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                output64.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        output64.close();

        String attachedFile = output.toString();


        return attachedFile;
    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    private void choosePictureFromGallery(int fromAttach) {
        if (isStoragePermissionGranted()) {
            Intent i = new Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            startActivityForResult(i, fromAttach);
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    String mCurrentPhotoPath;

    public class CreateFile extends AsyncTask<String, File, File> {
        int type;

        public CreateFile(int type) {
            this.type = type;
        }

        @Override
        protected File doInBackground(String... strings) {
            File file = null;
            try {
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String imageFileName = "JPEG_" + timeStamp + "_";
                File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                file = File.createTempFile(
                        imageFileName,  // prefix /
                        ".jpg",        // suffix /
                        storageDir      // directory /
                );
            } catch (IOException e) {
                e.printStackTrace();
            }
            return file;
        }

        @Override
        protected void onPostExecute(File file) {
            super.onPostExecute(file);
            if (file != null) {
                mCurrentPhotoPath = file.getAbsolutePath();
                callCamera(file, type);
            }
        }
    }


    private void callCamera(File file, int type) {
        if (file != null) {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

                if (file != null) {
                    Uri photoURI  = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID.concat(".provider"), file);

                    mImageUri = Uri.fromFile(file);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(takePictureIntent, type);
                }
            }
        }

    }

    private void dispatchTakePictureIntentReal(int Type) {
        new CreateFile(Type).execute();
    }

    public File grabImageFile(boolean compress, int quality) {
        File returnFile = null;
        try {
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
        } catch (Exception e) {
            Log.e("Image Capture Error", e.getMessage());
        }
        return returnFile;
    }

    private File createTemporaryFile(String part, String ext) throws Exception {
        File tempDir = Environment.getExternalStorageDirectory();
        tempDir = new File(tempDir.getAbsolutePath() + "/rebliss/");
        if (!tempDir.exists()) {
            tempDir.mkdir();
        }
        return File.createTempFile(part, ext, tempDir);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public String parseDateToddMMyyyy(String time) {
        String inputPattern = "yyyy-dd-MM";
        String outputPattern = "dd/MM/yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        try {
            if(time==null){
                time ="";
            }
           Date dates = inputFormat.parse(time);
            date = outputFormat.format(dates);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    private File getImageFile() throws IOException {
        String imageFileName = "JPEG_" + System.currentTimeMillis() + "_";
        String imagesFolder="images";
        File storageDir = new File(
                getExternalFilesDir(Environment.DIRECTORY_PICTURES), imagesFolder
        );
        System.out.println(storageDir.getAbsolutePath());
        if (!storageDir.exists()){
            storageDir.mkdirs();
            Log.e("TAG", "file not exists: " ); }
        else
            Log.e("TAG", "file exists: " );
        File file = File.createTempFile(imageFileName, ".jpg", storageDir);
        Log.e("TAG", "getImageFile: "+file.getAbsolutePath() );
        currentPhotoPaths = "file:" + file.getAbsolutePath();
        fileAbsolutePath =  file.getAbsolutePath();
        Log.e("TAG", "currentphotopath: >>>>"+currentPhotoPaths );
        mySingleton.saveData("currentpath",currentPhotoPaths);
        mySingleton.saveData("fileabsolutepath",fileAbsolutePath);
        return file;
    }

    private String encodeTobase64(Bitmap rotatedBitmap)
    {
        Bitmap base644 = rotatedBitmap;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        base644.compress(Bitmap.CompressFormat.PNG,15,baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b,Base64.DEFAULT);
        return imageEncoded;

    }

}
