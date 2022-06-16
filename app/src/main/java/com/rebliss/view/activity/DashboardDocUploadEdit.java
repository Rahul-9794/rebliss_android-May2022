package com.rebliss.view.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.MediaStore;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Base64OutputStream;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.kaopiz.kprogresshud.KProgressHUD;
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
import com.rebliss.presenter.helper.FilePath;
import com.rebliss.presenter.helper.Logout;
import com.rebliss.presenter.helper.Network;
import com.rebliss.presenter.helper.RegexUtils;
import com.rebliss.presenter.helper.ShowHintOrText;
import com.rebliss.presenter.helper.TextUtil;
import com.rebliss.presenter.retrofit.ApiClient;
import com.rebliss.presenter.retrofit.ApiInterface;
import com.rebliss.utils.App;
import com.rebliss.utils.GPSTracker;
import com.rebliss.view.adapter.UploadRemoveAdapter;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.filter.Filter;
import com.zhihu.matisse.internal.entity.CaptureStrategy;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.NoRouteToHostException;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardDocUploadEdit extends AppCompatActivity {
    private static final String TAG = DashboardDocUploadEdit.class.getSimpleName();
    private Context context;
    // TextView References
    private TextView textCPAdhar, textPan, textFirmPan, textGST, textAddress, textCheque, textTurnOver, textOtherBusiness, textIbdaCode;
    private TextView textAdharFile, textCPPANFile, textFirmFile, textGSTFile, textaddressFile, textCHequeFile, textHeader;
    private TextView textPassport, textPassportFile, textSignedForm, textSignedFormFile, textshopPhoto, textshopPhotoFile;
    private TextView textUploadOption;

    private static final int REQUEST_CODE_CHOOSE = 23;
    // Image  References
    private ImageView imgLogout, imgCal;


    // Button References
    private Button btnSave, btnUpload, btnUploadPAN, btnUploadFirmPan, btnUploadGST, btnUploadAddress, btnUploadCheque, btnUploadIbdaCode;
    private Button btnUploadPassport, btnUploadSignedForm, btnUploadshopPhoto;
    LinearLayout click_to_upload, click_to_uploadPan, click_to_uploadFirmPan, click_to_uploadGST, click_to_uploadaddress, click_to_uploadcheque, click_to_uploadIbdaCode;
    LinearLayout liIBDACODE, click_to_uploadPassport, click_to_uploadSignedForm, click_to_uploadshopPhoto, linShopPhoto;

    // EditText References
    private EditText etAddhar, etPan, etFirmPan, etGST, etaddress, etcheque, etIbdaCode;
    private EditText etPassport, etSignedForm, etshopPhoto;

    // Layout References
    LinearLayout textUploadSection;
    LinearLayout spUploadLayout;

    // ImageView References

    // Others
    boolean bussinessTypeStatus = false;
    String shopLatitude; // latitude
    String shopLongitude;
    String bussinesstype;

    Spinner spUploadOption;
    private int sUploadOption;
    private int spUploadOptionPosition = 0;
    String uploadTypeOption[] = {"Aadhaar Card", "Driving Licence", "Voter Card", "Passport", "Water Bill",
            "Landline or Postpaid mobile bill", "Electricity bill", "Bank Passbook/ Statement"};
    private String selectedUploadType = "aadhaar";

    private MySingleton mySingleton;
    private Network network;
    private KProgressHUD kProgressHUD;
    private DisplaySnackBar displaySnackBar;
    private Data profileData;
    private String sAddhar = "", sPan = "", sFirmPan = "", sGST = "", saddress = "",
            scheque = "";


    private int Day, Month, Year;
    private int mYear, mMonth, mDay, mHour, mMinute;
    public static EditProfileRequest editProfileRequest;
    private Intent intent;
    private static final int REQUEST_CAMERA_JOB = 111;
    private static final int FILE_SELECT_CODE_JOB = 222;
    private static final int FILE_SELECT_ADHAR = 50;
    private static final int FILE_SELECT_CPPAN = 100;
    private static final int FILE_SELECT_FIRMPAN = 200;
    private static final int FILE_SELECT_GST = 300;
    private static final int FILE_SELECT_ADDRESS = 400;
    private static final int FILE_SELECT_CHEQUE = 500;
    private static final int FILE_SELECT_PASSPORT = 600;
    private static final int FILE_SELECT_SIGNED_FORM = 700;
    private static final int FILE_SELECT_SHOP = 800;
    private static final int FILE_SELECT_IBDA = 900;

    private String selectedFilePath = "";

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
    private List<String> ibdaImages;
    private String Adhar_Url = "", Pan_Url = "", Firm_Pan_Url = "", Gst_Url = "", Address_Url = "", Cheque_Url = "", Passport_Url = "", SignedForm_Url = "", Shop_Url = "", IbdaUrl = "";
    private TextView textStepOne, textStepTwo, textStepThree, textStepFour;
    private ImageView imgStepOne, imgStepTwo, imgStepThree, imgStepFour;
    private View viewStepOne, viewStepTwo, viewStepThree;
    String callFrom = "";
    ImageView icBack;
    TextView mywidget;
    int positionForUpload = 0;

    String Adhar_UrlNew="";
    String IbdaUrlNew="";
    String Pan_UrlNew="";
    String Firm_Pan_UrlNew="";
    String Gst_UrlNew="";
    String Address_UrlNew="";
    String Cheque_UrlNew="";
    String Passport_UrlNew="";
    String SignedForm_UrlNew="";
    String Shop_UrlNew="";

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
    private ImageView ImattachIbdaCode;
    private ImageView ImcameraIbdaCode;

    private ImageView Imcheck01;
    private ImageView Imcheck02;
    private ImageView Imcheck03;
    private ImageView Imcheck04;
    private ImageView Imcheck05;
    private ImageView Imcheck06;
    private ImageView Imcheck07;
    private ImageView Imcheck08;
    private ImageView Imcheck09;
    private ImageView ImcheckIbdaCode;

    RecyclerView aadhar_recycler_view;
    RecyclerView pan_recycler_view;
    RecyclerView gst_photo_recycler_view;
    RecyclerView bsnAddr_photo_recycler_view;
    RecyclerView frmPan_recycler_view;
    RecyclerView cnclChq_recycler_view;
    RecyclerView pssprt_photo_recycler_view;
    RecyclerView upldsgn_photo_recycler_view;
    RecyclerView upldshop_photo_recycler_view;
    RecyclerView IbdaCode_recycler_view;

    private String currentPhotoPath;
    private String selectedImagePath;
    private String uploadImageViaCamera;

    private static final int SELECT_PICTURE = 1;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int ACTIVITY_SELECT_IMAGE = 2;

    static final int AADHAR_IMAGE_CLICK_REQUEST = 101;
    static final int AADHAR_IMAGE_ATTACH_REQUEST = 102;
    static final int PAN_IMAGE_CLICK_REQUEST = 201;
    static final int PAN_IMAGE_ATTACH_REQUEST = 202;
    static final int FIRM_PAN_IMAGE_CLICK_REQUEST = 301;
    static final int FIRM_PAN_IMAGE_ATTACH_REQUEST = 302;
    static final int GST_IMAGE_CLICK_REQUEST = 401;
    static final int GST_IMAGE_ATTACH_REQUEST = 402;
    static final int ADDRESS_IMAGE_CLICK_REQUEST = 501;
    static final int ADDRESS_IMAGE_ATTACH_REQUEST = 502;
    static final int CHEQUE_IMAGE_CLICK_REQUEST = 601;
    static final int CHEQUE_IMAGE_ATTACH_REQUEST = 602;
    static final int PASSPORT_IMAGE_CLICK_REQUEST = 701;
    static final int PASSPORT_IMAGE_ATTACH_REQUEST = 702;
    static final int SIGNED_FORM_IMAGE_CLICK_REQUEST = 801;
    static final int SIGNED_FORM_IMAGE_ATTACH_REQUEST = 802;
    static final int OFFICE_PHOTO_IMAGE_CLICK_REQUEST = 901;
    static final int OFFICE_PHOTO_IMAGE_ATTACH_REQUEST = 902;

    static final int IBDA_IMAGE_CLICK_REQUEST = 1001;
    static final int IBDA_IMAGE_ATTACH_REQUEST = 1002;
    /**
     * @param savedInstanceState
     */
    private UploadRemoveAdapter uploadRemoveAdapter;
    private UploadRemoveAdapter upLoadAadharAdapter;
    private UploadRemoveAdapter uploadPanAdapter;
    private UploadRemoveAdapter uploadBsnAdhar;
    private UploadRemoveAdapter uploadGSTPhotoAdapter;
    private UploadRemoveAdapter uploadFirmPanAdapter;
    private UploadRemoveAdapter uploadCnclChequeAdapter;
    private UploadRemoveAdapter uploadPassportAdapter;
    private UploadRemoveAdapter uploadSignedAdapter;
    private UploadRemoveAdapter uploadShopAdapter;
    private UploadRemoveAdapter uploadBusinessAddressAdapter;
    private UploadRemoveAdapter uploadIBDAAdapter;
    private LinearLayout stepsLayout;

    private LinearLayout Layout_second_TextView,
            linearLayout_pan,
            linearLayout_ShopPhoto,
            linearLayout_GST,
            linearLayout_BAddress,
            linearLayout_firmPan,
            linearLayout_IDBA,
            linearLayout_Cheque,
            linearLayout_photoPass,
            linearLayout_SignedForm;

    private HorizontalScrollView scrollViewAadhar,
            scrollViewPan,
            scrollViewShopPhoto,
            scrollViewBAddress,
            scrollViewShopPan,
            scrollViewShopGST,
            scrollViewfirmPan,
            scrollViewIDBA,
            scrollViewPhotoPass,
            scrollViewSignedForm,
            scrollViewCheque;
    private TextView upladed_addhar_txt;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_third);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (islocationPermissionGranted()) {
            startService(new Intent(this, GPSTracker.class));
        }
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        // Initialize View
        try {
            intent = getIntent();
            callFrom = intent.getStringExtra("call_from");
            // bussinesstype = intent.getStringExtra("bussinessType");
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Initialize View
        initView();

        // Initialize view click listener
        viewListener();
        if (network.isNetworkConnected(context)) {
            callProfileAPI();

        } else {
            displaySnackBar.DisplaySnackBar(Constant.NETWIRK_ERROR, Constant.TYPE_ERROR);
        }





        if (mySingleton.getData(Constant.USER_GROUP_ID).equalsIgnoreCase(Constant.CP_GROUP_ID) || (mySingleton.getData(Constant.USER_GROUP_ID).equalsIgnoreCase(Constant.SUPER_CP_GROUP_ID))) {
            spUploadLayout.setVisibility(View.GONE);
        } else {
            spUploadLayout.setVisibility(View.VISIBLE);
        }

        // disable();

        etAddhar.setEnabled(false);
        etPan.setEnabled(false);
        etFirmPan.setEnabled(false);
        etGST.setEnabled(false);
        etaddress.setEnabled(false);
        etcheque.setEnabled(false);

    }

    /******************************************* Start Methods *************************************
     */
    private void disable() {
        if (!TextUtil.isStringNullOrBlank(callFrom)) {
            if (callFrom.equalsIgnoreCase("1")) {

                imgLogout.setVisibility(View.GONE);
                btnUploadPassport.setEnabled(false);
                btnUploadshopPhoto.setEnabled(false);
                btnUploadSignedForm.setEnabled(false);

                btnUploadshopPhoto.setEnabled(false);
                btnUpload.setEnabled(false);
                btnUploadPAN.setEnabled(false);
                btnUploadFirmPan.setEnabled(false);
                btnUploadGST.setEnabled(false);
                btnUploadAddress.setEnabled(false);
                btnUploadCheque.setEnabled(false);

                etAddhar.setEnabled(false);
                etPan.setEnabled(false);
                etFirmPan.setEnabled(false);
                etGST.setEnabled(false);
                etaddress.setEnabled(false);
                etcheque.setEnabled(false);

                Imcamera01.setEnabled(false);
                Imcamera02.setEnabled(false);
                Imcamera03.setEnabled(false);
                Imcamera04.setEnabled(false);
                Imcamera05.setEnabled(false);
                Imcamera06.setEnabled(false);
                Imcamera07.setEnabled(false);
                Imcamera08.setEnabled(false);
                Imcamera09.setEnabled(false);
                Imattach01.setEnabled(false);
                Imattach02.setEnabled(false);
                Imattach03.setEnabled(false);
                Imattach04.setEnabled(false);
                Imattach05.setEnabled(false);
                Imattach06.setEnabled(false);
                Imattach07.setEnabled(false);
                Imattach08.setEnabled(false);
                Imattach09.setEnabled(false);


                btnUploadPassport.setClickable(false);
                btnUploadshopPhoto.setClickable(false);
                btnUploadSignedForm.setClickable(false);

                btnUpload.setClickable(false);
                btnUploadPAN.setClickable(false);
                btnUploadFirmPan.setClickable(false);
                btnUploadGST.setClickable(false);
                btnUploadAddress.setClickable(false);
                btnUploadCheque.setClickable(false);

                Imcamera01.setClickable(false);
                Imcamera02.setClickable(false);
                Imcamera03.setClickable(false);
                Imcamera04.setClickable(false);
                Imcamera05.setClickable(false);
                Imcamera06.setClickable(false);
                Imcamera07.setClickable(false);
                Imcamera08.setClickable(false);
                Imcamera09.setClickable(false);
                Imattach01.setClickable(false);
                Imattach02.setClickable(false);
                Imattach03.setClickable(false);
                Imattach04.setClickable(false);
                Imattach05.setClickable(false);
                Imattach06.setClickable(false);
                Imattach07.setClickable(false);
                Imattach08.setClickable(false);
                Imattach09.setClickable(false);


                btnUploadshopPhoto.setClickable(false);
                etAddhar.setClickable(false);
                etPan.setClickable(false);
                etIbdaCode.setClickable(false);
                etFirmPan.setClickable(false);
                etGST.setClickable(false);
                etaddress.setClickable(false);
                etcheque.setClickable(false);

                aadhar_recycler_view.setClickable(false);
                pan_recycler_view.setClickable(false);
                gst_photo_recycler_view.setClickable(false);
                bsnAddr_photo_recycler_view.setClickable(false);
                frmPan_recycler_view.setClickable(false);
                cnclChq_recycler_view.setClickable(false);
                pssprt_photo_recycler_view.setClickable(false);
                upldsgn_photo_recycler_view.setClickable(false);
                upldshop_photo_recycler_view.setClickable(false);

                aadhar_recycler_view.setEnabled(false);
                pan_recycler_view.setEnabled(false);
                gst_photo_recycler_view.setEnabled(false);
                bsnAddr_photo_recycler_view.setEnabled(false);
                frmPan_recycler_view.setEnabled(false);
                cnclChq_recycler_view.setEnabled(false);
                pssprt_photo_recycler_view.setEnabled(false);
                upldsgn_photo_recycler_view.setEnabled(false);
                upldshop_photo_recycler_view.setEnabled(false);
                spUploadOption.setEnabled(false);


                if (mySingleton.getData(Constant.USER_GROUP_ID).equalsIgnoreCase(Constant.CP_GROUP_ID) || (mySingleton.getData(Constant.USER_GROUP_ID).equalsIgnoreCase(Constant.SUPER_CP_GROUP_ID))) {
                    spUploadLayout.setVisibility(View.GONE);
                } else {
                    spUploadLayout.setVisibility(View.VISIBLE);
                }


            }
        }
    }

    private void initView() {
        // editProfileRequest = new EditProfileRequest();
        context = DashboardDocUploadEdit.this;
        mySingleton = new MySingleton(this);
        displaySnackBar = new DisplaySnackBar(this);
        network = new Network();
        icBack = findViewById(R.id.icBack);
        textStepOne = findViewById(R.id.textStepOne);
        textStepTwo = findViewById(R.id.textStepTwo);
        textStepThree = findViewById(R.id.textStepThree);
        //  textStepFour = findViewById(R.id.textStepFour);
        textStepOne.setTypeface(App.LATO_REGULAR);
        textStepTwo.setTypeface(App.LATO_REGULAR);
        textStepThree.setTypeface(App.LATO_REGULAR);
        //textStepFour.setTypeface(App.LATO_REGULAR);

        // TextView References

        mywidget = findViewById(R.id.mywidget);
        textHeader = findViewById(R.id.textHeader);
        textCPAdhar = findViewById(R.id.textCPAdhar);
        textPan = findViewById(R.id.textPan);
        textFirmPan = findViewById(R.id.textFirmPan);
        textGST = findViewById(R.id.textGST);
        textAddress = findViewById(R.id.textAddress);

        textCheque = findViewById(R.id.textCheque);




        scrollViewAadhar = findViewById(R.id.scrollViewAadhar);
        Layout_second_TextView = findViewById(R.id.Layout_second_balls);



        linearLayout_pan = findViewById(R.id.linearLayout_pan);
        linearLayout_ShopPhoto = findViewById(R.id.linearLayout_ShopPhoto);
        linearLayout_GST = findViewById(R.id.linearLayout_GST);
        linearLayout_BAddress = findViewById(R.id.linearLayout_BAddress);
        linearLayout_firmPan = findViewById(R.id.linearLayout_firmPan);
        linearLayout_IDBA = findViewById(R.id.linearLayout_IDBA);
        linearLayout_Cheque = findViewById(R.id.linearLayout_Cheque);
        linearLayout_photoPass = findViewById(R.id.linearLayout_photoPass);
        linearLayout_SignedForm = findViewById(R.id.linearLayout_SignedForm);



        scrollViewPan = findViewById(R.id.scrollViewPan);
        scrollViewShopPhoto = findViewById(R.id.scrollViewShopPhoto);
        scrollViewBAddress = findViewById(R.id.scrollViewBAddress);
        scrollViewShopPan = findViewById(R.id.scrollViewPan);
        scrollViewShopGST = findViewById(R.id.scrollViewGST);
        scrollViewfirmPan = findViewById(R.id.scrollViewfirmPan);
        scrollViewIDBA = findViewById(R.id.scrollViewIDBA);
        scrollViewSignedForm = findViewById(R.id.scrollViewSignedForm);
        scrollViewCheque = findViewById(R.id.scrollViewCheque);
        scrollViewPhotoPass = findViewById(R.id.scrollViewPhotoPass);



        stepsLayout = findViewById(R.id.stepsLayout);
        stepsLayout.setVisibility(View.GONE);

        textAdharFile = findViewById(R.id.textAdharFile);
        textCPPANFile = findViewById(R.id.textCPPANFile);
        textFirmFile = findViewById(R.id.textFirmFile);
        textGSTFile = findViewById(R.id.textGSTFile);
        textaddressFile = findViewById(R.id.textaddressFile);
        textCHequeFile = findViewById(R.id.textCHequeFile);

        textUploadOption = findViewById(R.id.textUploadOption);
        textIbdaCode = findViewById(R.id.textIbdaCode);


        // Image  References
        imgLogout = findViewById(R.id.imgLogout);
//        imgCal = findViewById(R.id.imgCal);

        // Button References

        btnSave = findViewById(R.id.btnSave);
        btnSave.setText("Update");
        btnUpload = findViewById(R.id.btnUpload);
        btnUploadPAN = findViewById(R.id.btnUploadPAN);
        btnUploadFirmPan = findViewById(R.id.btnUploadFirmPan);
        btnUploadGST = findViewById(R.id.btnUploadGST);
        btnUploadAddress = findViewById(R.id.btnUploadAddress);
        btnUploadCheque = findViewById(R.id.btnUploadCheque);
        btnUploadIbdaCode = findViewById(R.id.btnUploadIbdaCode);


        // EditText References

        etAddhar = findViewById(R.id.etAddhar);
        etPan = findViewById(R.id.etPan);
        etFirmPan = findViewById(R.id.etFirmPan);
        etGST = findViewById(R.id.etGST);
        etaddress = findViewById(R.id.etaddress);
        etcheque = findViewById(R.id.etcheque);
        etIbdaCode = findViewById(R.id.etIbdaCode);

        // Layout References
        textUploadSection = findViewById(R.id.textUploadSection);
        spUploadLayout = findViewById(R.id.spUploadLayout);

        click_to_upload = findViewById(R.id.click_to_upload);
        click_to_uploadPan = findViewById(R.id.click_to_uploadPan);
        click_to_uploadFirmPan = findViewById(R.id.click_to_uploadFirmPan);
        click_to_uploadGST = findViewById(R.id.click_to_uploadGST);
        click_to_uploadaddress = findViewById(R.id.click_to_uploadaddress);
        click_to_uploadcheque = findViewById(R.id.click_to_uploadcheque);
        click_to_uploadIbdaCode = findViewById(R.id.click_to_uploadIbdaCode);
        liIBDACODE = findViewById(R.id.liIBDACODE);

        // ImageView References
        Imattach01 = (ImageView) findViewById(R.id.Imattach01);
        Imcamera01 = (ImageView) findViewById(R.id.Imcamera01);
        Imattach02 = (ImageView) findViewById(R.id.Imattach02);
        Imcamera02 = (ImageView) findViewById(R.id.Imcamera02);
        Imattach03 = (ImageView) findViewById(R.id.Imattach03);
        Imcamera03 = (ImageView) findViewById(R.id.Imcamera03);
        Imattach04 = (ImageView) findViewById(R.id.Imattach04);
        Imcamera04 = (ImageView) findViewById(R.id.Imcamera04);
        Imattach05 = (ImageView) findViewById(R.id.Imattach05);
        Imcamera05 = (ImageView) findViewById(R.id.Imcamera05);
        Imattach06 = (ImageView) findViewById(R.id.Imattach06);
        Imcamera06 = (ImageView) findViewById(R.id.Imcamera06);
        Imattach07 = (ImageView) findViewById(R.id.Imattach07);
        Imcamera07 = (ImageView) findViewById(R.id.Imcamera07);
        Imattach08 = (ImageView) findViewById(R.id.Imattach08);
        Imcamera08 = (ImageView) findViewById(R.id.Imcamera08);
        Imattach09 = (ImageView) findViewById(R.id.Imattach09);
        Imcamera09 = (ImageView) findViewById(R.id.Imcamera09);
        ImcameraIbdaCode = (ImageView) findViewById(R.id.ImcameraIbdaCode);
        ImattachIbdaCode = (ImageView) findViewById(R.id.ImattachIbdaCode);


        Imcheck01 = (ImageView) findViewById(R.id.Imcheck01);
        Imcheck02 = (ImageView) findViewById(R.id.Imcheck02);
        Imcheck03 = (ImageView) findViewById(R.id.Imcheck03);
        Imcheck04 = (ImageView) findViewById(R.id.Imcheck04);
        Imcheck05 = (ImageView) findViewById(R.id.Imcheck05);
        Imcheck06 = (ImageView) findViewById(R.id.Imcheck06);
        Imcheck07 = (ImageView) findViewById(R.id.Imcheck07);
        Imcheck08 = (ImageView) findViewById(R.id.Imcheck08);
        Imcheck09 = (ImageView) findViewById(R.id.Imcheck09);
        ImcheckIbdaCode = (ImageView) findViewById(R.id.ImcheckIbdaCode);

        IbdaCode_recycler_view = findViewById(R.id.IbdaCode_recycler_view);
        LinearLayoutManager supportersLayoutManager1 = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        IbdaCode_recycler_view.setLayoutManager(supportersLayoutManager1);


        aadhar_recycler_view = findViewById(R.id.aadhar_recycler_view);
        LinearLayoutManager supportersLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        aadhar_recycler_view.setLayoutManager(supportersLayoutManager);

        pan_recycler_view = findViewById(R.id.pan_recycler_view);
        LinearLayoutManager pansupportersLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        pan_recycler_view.setLayoutManager(pansupportersLayoutManager);

        gst_photo_recycler_view = findViewById(R.id.gst_photo_recycler_view);
        LinearLayoutManager gstsupportersLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        gst_photo_recycler_view.setLayoutManager(gstsupportersLayoutManager);

        bsnAddr_photo_recycler_view = findViewById(R.id.bsnAddr_photo_recycler_view);
        LinearLayoutManager bsnAddrsupportersLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        bsnAddr_photo_recycler_view.setLayoutManager(bsnAddrsupportersLayoutManager);

        frmPan_recycler_view = findViewById(R.id.frmPan_recycler_view);
        LinearLayoutManager frmpansupportersLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        frmPan_recycler_view.setLayoutManager(frmpansupportersLayoutManager);

        cnclChq_recycler_view = findViewById(R.id.cnclChq_recycler_view);
        LinearLayoutManager cnlcqesupportersLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        cnclChq_recycler_view.setLayoutManager(cnlcqesupportersLayoutManager);

        pssprt_photo_recycler_view = findViewById(R.id.pssprt_photo_recycler_view);
        LinearLayoutManager pssprtsupportersLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        pssprt_photo_recycler_view.setLayoutManager(pssprtsupportersLayoutManager);

        upldsgn_photo_recycler_view = findViewById(R.id.upldsgn_photo_recycler_view);
        LinearLayoutManager sgnsupportersLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        upldsgn_photo_recycler_view.setLayoutManager(sgnsupportersLayoutManager);

        upldshop_photo_recycler_view = findViewById(R.id.upldshop_photo_recycler_view);
        LinearLayoutManager shopsupportersLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        upldshop_photo_recycler_view.setLayoutManager(shopsupportersLayoutManager);


        // Others
        spUploadOption = findViewById(R.id.spUploadOption);
        spUploadOption.setEnabled(false);

        textPassport = findViewById(R.id.textPassport);
        textPassportFile = findViewById(R.id.textPassportFile);
        textSignedForm = findViewById(R.id.textSignedForm);
        textSignedFormFile = findViewById(R.id.textSignedFormFile);
        textshopPhoto = findViewById(R.id.textshopPhoto);
        textshopPhotoFile = findViewById(R.id.textshopPhotoFile);
        btnUploadPassport = findViewById(R.id.btnUploadPassport);
        btnUploadSignedForm = findViewById(R.id.btnUploadSignedForm);
        btnUploadshopPhoto = findViewById(R.id.btnUploadshopPhoto);
        click_to_uploadPassport = findViewById(R.id.click_to_uploadPassport);
        click_to_uploadSignedForm = findViewById(R.id.click_to_uploadSignedForm);
        click_to_uploadshopPhoto = findViewById(R.id.click_to_uploadshopPhoto);
        linShopPhoto = findViewById(R.id.linShopPhoto);


        // no longer use
//        btnUpload.setBackground(getResources().getDrawable(R.drawable.button_upload_file_pending));
//        btnUploadPAN.setBackground(getResources().getDrawable(R.drawable.button_upload_file_pending));
//        btnUploadFirmPan.setBackground(getResources().getDrawable(R.drawable.button_upload_file_pending));
//        btnUploadGST.setBackground(getResources().getDrawable(R.drawable.button_upload_file_pending));
//        btnUploadAddress.setBackground(getResources().getDrawable(R.drawable.button_upload_file_pending));
//        btnUploadCheque.setBackground(getResources().getDrawable(R.drawable.button_upload_file_pending));
//        btnUploadPassport.setBackground(getResources().getDrawable(R.drawable.button_upload_file_pending));
//        btnUploadSignedForm.setBackground(getResources().getDrawable(R.drawable.button_upload_file_pending));
//        btnUploadSihow to set textview in linearlayout programmatically horizontallygnedForm.setBackground(getResources().getDrawable(R.drawable.button_upload_file_pending));
//        btnUploadshopPhoto.setBackground(getResources().getDrawable(R.drawable.button_upload_file_pending));


        if (mySingleton.getData(Constant.USER_GROUP_ID).equalsIgnoreCase(Constant.CP_GROUP_ID) || mySingleton.getData(Constant.USER_GROUP_ID).equalsIgnoreCase(Constant.SUPER_CP_GROUP_ID)) {
            textshopPhoto.setText("Upload Office Photo");
        } else {
            textshopPhoto.setText("Upload Shop Photo");
        }

        if (mySingleton.getData(Constant.USER_GROUP_ID).equalsIgnoreCase("3")) {
            textPan.setText("PAN No");
            textFirmPan.setText("Firm PAN");
            textCheque.setText("Bank proof");
            textSignedForm.setText("Upload signed form");

        }
        /*if (mySingleton.getData(Constant.USER_GROUP_ID).equalsIgnoreCase(Constant.SUPER_CP_GROUP_ID)) {
            bussinesstype = editProfileRequest.getBusiness_type();
        } else {
            bussinesstype = editProfileRequest.getBusiness_type();
        }*/
        if (editProfileRequest != null) {
            bussinesstype = editProfileRequest.getBusiness_type();
            if (!TextUtil.isStringNullOrBlank(bussinesstype)) {
                if (!bussinesstype.equalsIgnoreCase("Proprietorship Firm")) {
                    bussinessTypeStatus = true;
                }
            }
        }

        showConditionalImageIcon();
        setFontOnView();
        showDataOnView();
        setHint();
    }


    private void setHint() {

        if (mySingleton.getData(Constant.USER_GROUP_ID).equalsIgnoreCase(Constant.CP_GROUP_ID) || (mySingleton.getData(Constant.USER_GROUP_ID).equalsIgnoreCase(Constant.SUPER_CP_GROUP_ID))) {
            //textCPAdhar.setText(ShowHintOrText.GetMandatory("Aadhar Number"));
            if (selectedUploadType.equalsIgnoreCase("aadhaar") || spUploadOptionPosition == 0) {
                textCPAdhar.setText(ShowHintOrText.GetMandatory("Aadhar Number"));
            } else if (selectedUploadType.equalsIgnoreCase("dl") || spUploadOptionPosition == 1) {
                textCPAdhar.setText(ShowHintOrText.GetMandatory("Driving Licence Number"));
            } else if (selectedUploadType.equalsIgnoreCase("voterId") || spUploadOptionPosition == 2) {
                textCPAdhar.setText(ShowHintOrText.GetMandatory("Voter Id Number"));
            }
            textPan.setText(ShowHintOrText.GetMandatory("PAN No"));
            textGST.setText(ShowHintOrText.GetOptional("GST Number"));
            textAddress.setText(ShowHintOrText.GetOptional("Business Address Proof"));
            if (bussinessTypeStatus) {
                textFirmPan.setText(ShowHintOrText.GetMandatory("Firm PAN"));
            } else {
                textFirmPan.setText(ShowHintOrText.GetOptional("Firm PAN"));
            }


            textCheque.setText(ShowHintOrText.GetMandatory("Bank proof"));
            textPassport.setText(ShowHintOrText.GetMandatory("Passport size photo"));
            textSignedForm.setText(ShowHintOrText.GetOptional("Upload signed Form "));
            textshopPhoto.setText(ShowHintOrText.GetOptional("Upload Office Photo"));
        } else if (mySingleton.getData(Constant.USER_GROUP_ID).equalsIgnoreCase(Constant.POS_GROUP_ID)) {
            if (selectedUploadType.equalsIgnoreCase("aadhaar") || spUploadOptionPosition == 0) {
                textCPAdhar.setText(ShowHintOrText.GetMandatory("Aadhar Number"));
                etAddhar.setHint("Enter 12 digits aadhar number");
            } else if (selectedUploadType.equalsIgnoreCase("dl") || spUploadOptionPosition == 1) {
                textCPAdhar.setText(ShowHintOrText.GetMandatory("Driving Licence Number"));
                etAddhar.setHint("Enter 15 digits driving licence number");
            } else if (selectedUploadType.equalsIgnoreCase("voterId") || spUploadOptionPosition == 2) {
                textCPAdhar.setText(ShowHintOrText.GetMandatory("Voter Card Number"));
                etAddhar.setHint("Enter 11 digits voter id number");
            } else if (selectedUploadType.equalsIgnoreCase("passport") || spUploadOptionPosition == 3) {
                textCPAdhar.setText(ShowHintOrText.GetMandatory("Passport Number"));
                etAddhar.setHint("Enter valid Passport number");
            } else if (selectedUploadType.equalsIgnoreCase("waterBill") || spUploadOptionPosition == 4) {
                textCPAdhar.setText(ShowHintOrText.GetMandatory("Water Bill"));
            } else if (selectedUploadType.equalsIgnoreCase("mobileBill") || spUploadOptionPosition == 5) {
                textCPAdhar.setText(ShowHintOrText.GetMandatory("Landline or Postpaid mobile bill"));
            } else if (selectedUploadType.equalsIgnoreCase("electricityBill") || spUploadOptionPosition == 6) {
                textCPAdhar.setText(ShowHintOrText.GetMandatory("Electricity bill"));
            } else if (selectedUploadType.equalsIgnoreCase("bankStatement") || spUploadOptionPosition == 7) {
                textCPAdhar.setText(ShowHintOrText.GetMandatory("Bank Passbook/ Statement\n" +
                        "\n"));
            }

            textIbdaCode.setText(ShowHintOrText.GetOptional("IB DA Code"));
            textPassport.setText(ShowHintOrText.GetMandatory("Passport size photo"));
            textPan.setText(ShowHintOrText.GetOptional("PAN No"));
            textAddress.setText(ShowHintOrText.GetOptional("Business Address Proof"));
            textCheque.setText(ShowHintOrText.GetMandatory("Bank proof"));
            textGST.setText(ShowHintOrText.GetOptional("GST Number"));
            textFirmPan.setText(ShowHintOrText.GetOptional("Firm PAN"));
            textSignedForm.setText(ShowHintOrText.GetOptional("Upload signed Form"));
            textshopPhoto.setText(ShowHintOrText.GetMandatory("Upload Shop Photo"));
        }
        textUploadOption.setText(ShowHintOrText.GetMandatory("Proof of Address"));

        //textDOB.setHint(ShowHintOrText.GetMandatory("DOB"));
    }

    private void showConditionalImageIcon() {
        Imcheck01.setVisibility(View.GONE);
        Imcheck02.setVisibility(View.GONE);
        Imcheck03.setVisibility(View.GONE);
        Imcheck04.setVisibility(View.GONE);
        Imcheck05.setVisibility(View.GONE);
        Imcheck06.setVisibility(View.GONE);
        Imcheck07.setVisibility(View.GONE);
        Imcheck08.setVisibility(View.GONE);
        Imcheck09.setVisibility(View.GONE);
        ImcheckIbdaCode.setVisibility(View.GONE);
    }

    private void setFontOnView() {
        // TextView References

        mywidget.setTypeface(App.LATO_REGULAR);
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
//

        btnSave.setTypeface(App.LATO_REGULAR);
        btnUpload.setTypeface(App.LATO_REGULAR);
        btnUploadPAN.setTypeface(App.LATO_REGULAR);
        btnUploadFirmPan.setTypeface(App.LATO_REGULAR);
        btnUploadGST.setTypeface(App.LATO_REGULAR);
        btnUploadAddress.setTypeface(App.LATO_REGULAR);
        btnUploadCheque.setTypeface(App.LATO_REGULAR);


        etAddhar.setTypeface(App.LATO_REGULAR);
        etPan.setTypeface(App.LATO_REGULAR);
        etIbdaCode.setTypeface(App.LATO_REGULAR);

        etFirmPan.setTypeface(App.LATO_REGULAR);
        etGST.setTypeface(App.LATO_REGULAR);
        etaddress.setTypeface(App.LATO_REGULAR);
        etcheque.setTypeface(App.LATO_REGULAR);
//
        textPassport.setTypeface(App.LATO_REGULAR);
        textPassportFile.setTypeface(App.LATO_REGULAR);
        textSignedForm.setTypeface(App.LATO_REGULAR);
        textSignedFormFile.setTypeface(App.LATO_REGULAR);
        textshopPhoto.setTypeface(App.LATO_REGULAR);
        textshopPhotoFile.setTypeface(App.LATO_REGULAR);
        btnUploadPassport.setTypeface(App.LATO_REGULAR);
        btnUploadSignedForm.setTypeface(App.LATO_REGULAR);
        btnUploadshopPhoto.setTypeface(App.LATO_REGULAR);

    }

    private void showDataOnView() {
        if (mySingleton.getData(Constant.USER_GROUP_ID).equalsIgnoreCase(Constant.CP_GROUP_ID) || mySingleton.getData(Constant.USER_GROUP_ID).equalsIgnoreCase(Constant.POS_GROUP_ID)) {
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
                if (profileData.getGroup_id().equalsIgnoreCase(Constant.POS_GROUP_ID)) {
                    liIBDACODE.setVisibility(View.VISIBLE);
                } else {
                    liIBDACODE.setVisibility(View.GONE);
                }
                etAddhar.setText((!TextUtil.isStringNullOrBlank(profileData.getAadhar_no()) ? profileData.getAadhar_no() : ""));
                etPan.setText((!TextUtil.isStringNullOrBlank(profileData.getPan_no()) ? profileData.getPan_no() : ""));
                etFirmPan.setText((!TextUtil.isStringNullOrBlank(profileData.getCompany_pan_no()) ? profileData.getCompany_pan_no() : ""));
                etGST.setText((!TextUtil.isStringNullOrBlank(profileData.getGst_no()) ? profileData.getGst_no() : ""));
                etaddress.setText((!TextUtil.isStringNullOrBlank(profileData.getAddress()) ? profileData.getAddress() : ""));
                etIbdaCode.setText((!TextUtil.isStringNullOrBlank(profileData.getIb_da_code()) ? profileData.getIb_da_code() : ""));


                /// textView Here
                Adhar_UrlNew = (!TextUtil.isStringNullOrBlank(profileData.getCp_adhar_proof()) ? profileData.getCp_adhar_proof() : "");
                String[] temp1 = Adhar_UrlNew.split(",");

                LinearLayout.LayoutParams params = new
                        LinearLayout.LayoutParams
                        (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);


                if (!TextUtil.isStringNullOrBlank(Adhar_UrlNew)) {

                    for (int a = 0; a < temp1.length; a++) {
                        params.setMargins(10, 0, 0, 0);
                        TextView first = new TextView(getApplicationContext());
                        first.setLayoutParams(params);
                        first.setTextColor(Color.parseColor("#FFFFFF"));
                        first.setTextSize(10);
                        first.setGravity(Gravity.CENTER);
                        first.setBackground(getResources().getDrawable(
                                R.drawable.oval_shape_theme_color));
                        // params.weight = 1; /// Need to give weight if you want equal size of textview inside Linearfirst.setGravity(Gravity.LEFT);
                        first.setPadding(15, 5, 15, 5);
                        first.setText(temp1[a]);
                        Layout_second_TextView.addView(first); //This is my linear layout which id is Layout_second_balls
                    }
                    scrollViewAadhar.setVisibility(View.VISIBLE);

                }

                /// end textView here

                /// textView Here
                IbdaUrlNew = (!TextUtil.isStringNullOrBlank(profileData.getIb_da_proof()) ? profileData.getIb_da_proof() : "");
                String[] temp2 = IbdaUrlNew.split(",");

                LinearLayout.LayoutParams params2 = new
                        LinearLayout.LayoutParams
                        (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);


                if (!TextUtil.isStringNullOrBlank(IbdaUrlNew)) {
                    for (int a = 0; a < temp2.length; a++) {
                        params2.setMargins(10, 0, 0, 0);
                        TextView first = new TextView(getApplicationContext());
                        first.setLayoutParams(params2);
                        first.setTextColor(Color.parseColor("#FFFFFF"));
                        first.setTextSize(10);
                        first.setGravity(Gravity.CENTER);
                        first.setBackground(getResources().getDrawable(
                                R.drawable.oval_shape_theme_color));
                        // params.weight = 1; /// Need to give weight if you want equal size of textview inside Linearfirst.setGravity(Gravity.LEFT);
                        first.setPadding(15, 5, 15, 5);
                        first.setText(temp2[a]);
                        linearLayout_IDBA.addView(first); //This is my linear layout which id is Layout_second_balls
                    }
                    scrollViewIDBA.setVisibility(View.VISIBLE);
                }



                Pan_UrlNew = (!TextUtil.isStringNullOrBlank(profileData.getCp_pan_proof()) ? profileData.getCp_pan_proof() : "");
                String[] temp3 = Pan_UrlNew.split(",");

                LinearLayout.LayoutParams params3 = new
                        LinearLayout.LayoutParams
                        (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);


                if (!TextUtil.isStringNullOrBlank(Pan_UrlNew)) {

                    for (int a = 0; a < temp3.length; a++) {
                        params3.setMargins(10, 0, 0, 0);
                        TextView first = new TextView(getApplicationContext());
                        first.setLayoutParams(params3);
                        first.setTextColor(Color.parseColor("#FFFFFF"));
                        first.setTextSize(10);
                        first.setGravity(Gravity.CENTER);
                        first.setBackground(getResources().getDrawable(
                                R.drawable.oval_shape_theme_color));
                        // params.weight = 1; /// Need to give weight if you want equal size of textview inside Linearfirst.setGravity(Gravity.LEFT);
                        first.setPadding(15, 5, 15, 5);
                        first.setText(temp3[a]);
                        linearLayout_pan.addView(first); //This is my linear layout which id is Layout_second_balls
                    }
                    scrollViewPan.setVisibility(View.VISIBLE);
                }
                /// end textView here


                Firm_Pan_UrlNew = (!TextUtil.isStringNullOrBlank(profileData.getFirm_pan_proof()) ? profileData.getFirm_pan_proof() : "");

                String[] temp4 = Firm_Pan_UrlNew.split(",");

                LinearLayout.LayoutParams params4 = new
                        LinearLayout.LayoutParams
                        (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);

                if (!TextUtil.isStringNullOrBlank(Firm_Pan_UrlNew)) {

                    for (int a = 0; a < temp4.length; a++) {
                        params4.setMargins(10, 0, 0, 0);
                        TextView first = new TextView(getApplicationContext());
                        first.setLayoutParams(params4);
                        first.setTextColor(Color.parseColor("#FFFFFF"));
                        first.setTextSize(10);
                        first.setGravity(Gravity.CENTER);
                        first.setBackground(getResources().getDrawable(
                                R.drawable.oval_shape_theme_color));
                        // params.weight = 1; /// Need to give weight if you want equal size of textview inside Linearfirst.setGravity(Gravity.LEFT);
                        first.setPadding(15, 5, 15, 5);
                        first.setText(temp4[a]);
                        linearLayout_firmPan.addView(first); //This is my linear layout which id is Layout_second_balls
                    }
                    scrollViewfirmPan.setVisibility(View.VISIBLE);
                }



                Gst_UrlNew = (!TextUtil.isStringNullOrBlank(profileData.getGst_proof()) ? profileData.getGst_proof() : "");


                String[] temp5 = Gst_UrlNew.split(",");

                LinearLayout.LayoutParams params5= new
                        LinearLayout.LayoutParams
                        (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);


                if (!TextUtil.isStringNullOrBlank(Gst_UrlNew)) {
                    for (int a = 0; a < temp5.length; a++) {
                        params5.setMargins(10, 0, 0, 0);
                        TextView first = new TextView(getApplicationContext());
                        first.setLayoutParams(params5);
                        first.setTextColor(Color.parseColor("#FFFFFF"));
                        first.setTextSize(10);
                        first.setGravity(Gravity.CENTER);
                        first.setBackground(getResources().getDrawable(
                                R.drawable.oval_shape_theme_color));
                        // params.weight = 1; /// Need to give weight if you want equal size of textview inside Linearfirst.setGravity(Gravity.LEFT);
                        first.setPadding(15, 5, 15, 5);
                        first.setText(temp5[a]);
                        linearLayout_GST.addView(first); //This is my linear layout which id is Layout_second_balls
                    }
                    scrollViewShopGST.setVisibility(View.VISIBLE);
                }




                Address_UrlNew = (!TextUtil.isStringNullOrBlank(profileData.getAddress_proof()) ? profileData.getAddress_proof() : "");

                String[] temp6 = Address_UrlNew.split(",");

                LinearLayout.LayoutParams params6= new
                        LinearLayout.LayoutParams
                        (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);

                if (!TextUtil.isStringNullOrBlank(Address_UrlNew)) {
                    for(int a = 0;a<temp6.length;a++)
                    {
                        params6.setMargins(10,0,0,0);
                        TextView first = new TextView(getApplicationContext());
                        first.setLayoutParams(params6);
                        first.setTextColor(Color.parseColor("#FFFFFF"));
                        first.setTextSize(10);
                        first.setGravity(Gravity.CENTER);
                        first.setBackground(getResources().getDrawable(
                                R.drawable.oval_shape_theme_color));
                        // params.weight = 1; /// Need to give weight if you want equal size of textview inside Linearfirst.setGravity(Gravity.LEFT);
                        first.setPadding(15, 5, 15, 5);
                        first.setText(temp6[a]);
                        linearLayout_BAddress.addView(first); //This is my linear layout which id is Layout_second_balls
                    }
                    scrollViewBAddress.setVisibility(View.VISIBLE);}


                Cheque_UrlNew = (!TextUtil.isStringNullOrBlank(profileData.getCheque_proof()) ? profileData.getCheque_proof() : "");

                String[] temp7 = Cheque_UrlNew.split(",");

                LinearLayout.LayoutParams params7= new
                        LinearLayout.LayoutParams
                        (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);

                if (!TextUtil.isStringNullOrBlank(Cheque_UrlNew)) {
                    for(int a = 0;a<temp7.length;a++)
                    {
                        params7.setMargins(10,0,0,0);
                        TextView first = new TextView(getApplicationContext());
                        first.setLayoutParams(params7);
                        first.setTextColor(Color.parseColor("#FFFFFF"));
                        first.setTextSize(10);
                        first.setGravity(Gravity.CENTER);
                        first.setBackground(getResources().getDrawable(
                                R.drawable.oval_shape_theme_color));
                        // params.weight = 1; /// Need to give weight if you want equal size of textview inside Linearfirst.setGravity(Gravity.LEFT);
                        first.setPadding(15, 5, 15, 5);
                        first.setText(temp7[a]);
                        linearLayout_Cheque.addView(first); //This is my linear layout which id is Layout_second_balls
                    }
                    scrollViewCheque.setVisibility(View.VISIBLE);}



                Passport_UrlNew = (!TextUtil.isStringNullOrBlank(profileData.getPassport_size_photo()) ? profileData.getPassport_size_photo() : "");

                String[] temp8 = Passport_UrlNew.split(",");

                LinearLayout.LayoutParams params8= new
                        LinearLayout.LayoutParams
                        (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);


                if (!TextUtil.isStringNullOrBlank(Passport_UrlNew)) {

                    for(int a = 0;a<temp8.length;a++)
                    {
                        params8.setMargins(10,0,0,0);
                        TextView first = new TextView(getApplicationContext());
                        first.setLayoutParams(params8);
                        first.setTextColor(Color.parseColor("#FFFFFF"));
                        first.setTextSize(10);
                        first.setGravity(Gravity.CENTER);
                        first.setBackground(getResources().getDrawable(
                                R.drawable.oval_shape_theme_color));
                        // params.weight = 1; /// Need to give weight if you want equal size of textview inside Linearfirst.setGravity(Gravity.LEFT);
                        first.setPadding(15, 5, 15, 5);
                        first.setText(temp8[a]);
                        linearLayout_photoPass.addView(first); //This is my linear layout which id is Layout_second_balls
                    }

                    scrollViewPhotoPass.setVisibility(View.VISIBLE);}



                SignedForm_UrlNew = (!TextUtil.isStringNullOrBlank(profileData.getSigned_form()) ? profileData.getSigned_form() : "");


                String[] temp9 = SignedForm_UrlNew.split(",");

                LinearLayout.LayoutParams params9= new
                        LinearLayout.LayoutParams
                        (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);


                if (!TextUtil.isStringNullOrBlank(SignedForm_UrlNew)) {
                    for(int a = 0;a<temp9.length;a++)
                    {
                        params9.setMargins(10,0,0,0);
                        TextView first = new TextView(getApplicationContext());
                        first.setLayoutParams(params9);
                        first.setTextColor(Color.parseColor("#FFFFFF"));
                        first.setTextSize(10);
                        first.setGravity(Gravity.CENTER);
                        first.setBackground(getResources().getDrawable(
                                R.drawable.oval_shape_theme_color));
                        // params.weight = 1; /// Need to give weight if you want equal size of textview inside Linearfirst.setGravity(Gravity.LEFT);
                        first.setPadding(15, 5, 15, 5);
                        first.setText(temp9[a]);
                        linearLayout_SignedForm.addView(first); //This is my linear layout which id is Layout_second_balls
                    }
                    scrollViewSignedForm.setVisibility(View.VISIBLE);}



                Shop_UrlNew = (!TextUtil.isStringNullOrBlank(profileData.getShop_photo()) ? profileData.getShop_photo() : "");

                String[] temp10 = Shop_UrlNew.split(",");

                LinearLayout.LayoutParams params10= new
                        LinearLayout.LayoutParams
                        (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);

                if (!TextUtil.isStringNullOrBlank(Shop_UrlNew)) {
                    for(int a = 0;a<temp10.length;a++)
                    {
                        params10.setMargins(10,0,0,0);
                        TextView first = new TextView(getApplicationContext());
                        first.setLayoutParams(params10);
                        first.setTextColor(Color.parseColor("#FFFFFF"));
                        first.setTextSize(10);
                        first.setGravity(Gravity.CENTER);
                        first.setBackground(getResources().getDrawable(
                                R.drawable.oval_shape_theme_color));
                        // params.weight = 1; /// Need to give weight if you want equal size of textview inside Linearfirst.setGravity(Gravity.LEFT);
                        first.setPadding(15, 5, 15, 5);
                        first.setText(temp10[a]);
                        linearLayout_ShopPhoto.addView(first); //This is my linear layout which id is Layout_second_balls
                    }

                    scrollViewShopPhoto.setVisibility(View.VISIBLE);}
            }
        } else if (mySingleton.getData(Constant.USER_GROUP_ID).equalsIgnoreCase(Constant.SUPER_CP_GROUP_ID)) {
            if (profileData != null) {
                liIBDACODE.setVisibility(View.GONE);
                if (profileData.getProfile_verified().equalsIgnoreCase("0")
                        ||profileData.getProfile_verified().equalsIgnoreCase("2")) {
                    if (!TextUtil.isStringNullOrBlank(profileData.getDecline_message())) {
                        mywidget.setText(profileData.getDecline_message());
                        mywidget.setVisibility(View.VISIBLE);
                    } else {
                        mywidget.setVisibility(View.GONE);
                    }
                } else {
                    mywidget.setVisibility(View.GONE);
                }

                etAddhar.setText((!TextUtil.isStringNullOrBlank(profileData.getAadhar_no()) ?profileData.getAadhar_no() : ""));
                etPan.setText((!TextUtil.isStringNullOrBlank(profileData.getPan_no()) ?profileData.getPan_no() : ""));
                etFirmPan.setText((!TextUtil.isStringNullOrBlank(profileData.getCompany_pan_no()) ?profileData.getCompany_pan_no() : ""));
                etGST.setText((!TextUtil.isStringNullOrBlank(profileData.getGst_no()) ?profileData.getGst_no() : ""));
                etaddress.setText((!TextUtil.isStringNullOrBlank(profileData.getAddress()) ?profileData.getAddress() : ""));

                // Adhar_Url = (!TextUtil.isStringNullOrBlank(profileData.getCp_adhar_proof()) ?profileData.getCp_adhar_proof() : "");

                /// textView Here
                Adhar_UrlNew = (!TextUtil.isStringNullOrBlank(profileData.getCp_adhar_proof()) ? profileData.getCp_adhar_proof() : "");
                String[] temp1 = Adhar_UrlNew.split(",");

                LinearLayout.LayoutParams params = new
                        LinearLayout.LayoutParams
                        (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);

                if (!TextUtil.isStringNullOrBlank(Adhar_UrlNew)) {
                    for (int a = 0; a < temp1.length; a++) {
                        params.setMargins(10, 0, 0, 0);
                        TextView first = new TextView(getApplicationContext());
                        first.setLayoutParams(params);
                        first.setTextColor(Color.parseColor("#FFFFFF"));
                        first.setTextSize(10);
                        first.setGravity(Gravity.CENTER);
                        first.setBackground(getResources().getDrawable(
                                R.drawable.oval_shape_theme_color));
                        // params.weight = 1; /// Need to give weight if you want equal size of textview inside Linearfirst.setGravity(Gravity.LEFT);
                        first.setPadding(15, 5, 15, 5);
                        first.setText(temp1[a]);
                        Layout_second_TextView.addView(first); //This is my linear layout which id is Layout_second_balls
                    }
                    scrollViewAadhar.setVisibility(View.VISIBLE);
                }

                /// end textView here



                Pan_UrlNew = (!TextUtil.isStringNullOrBlank(profileData.getCp_pan_proof()) ? profileData.getCp_pan_proof() : "");
                String[] temp3 = Pan_UrlNew.split(",");

                LinearLayout.LayoutParams params3 = new
                        LinearLayout.LayoutParams
                        (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);


                if (!TextUtil.isStringNullOrBlank(Pan_UrlNew)) {

                    for (int a = 0; a < temp3.length; a++) {
                        params3.setMargins(10, 0, 0, 0);
                        TextView first = new TextView(getApplicationContext());
                        first.setLayoutParams(params3);
                        first.setTextColor(Color.parseColor("#FFFFFF"));
                        first.setTextSize(10);
                        first.setGravity(Gravity.CENTER);
                        first.setBackground(getResources().getDrawable(
                                R.drawable.oval_shape_theme_color));
                        // params.weight = 1; /// Need to give weight if you want equal size of textview inside Linearfirst.setGravity(Gravity.LEFT);
                        first.setPadding(15, 5, 15, 5);
                        first.setText(temp3[a]);
                        linearLayout_pan.addView(first); //This is my linear layout which id is Layout_second_balls
                    }
                    scrollViewPan.setVisibility(View.VISIBLE);
                }
                /// end textView here


                Firm_Pan_UrlNew = (!TextUtil.isStringNullOrBlank(profileData.getFirm_pan_proof()) ? profileData.getFirm_pan_proof() : "");

                String[] temp4 = Firm_Pan_UrlNew.split(",");

                LinearLayout.LayoutParams params4 = new
                        LinearLayout.LayoutParams
                        (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);

                if (!TextUtil.isStringNullOrBlank(Firm_Pan_UrlNew)) {

                    for (int a = 0; a < temp4.length; a++) {

                        params4.setMargins(10, 0, 0, 0);
                        TextView first = new TextView(getApplicationContext());
                        first.setLayoutParams(params4);
                        first.setTextColor(Color.parseColor("#FFFFFF"));
                        first.setTextSize(10);
                        first.setGravity(Gravity.CENTER);
                        first.setBackground(getResources().getDrawable(
                                R.drawable.oval_shape_theme_color));
                        // params.weight = 1; /// Need to give weight if you want equal size of textview inside Linearfirst.setGravity(Gravity.LEFT);
                        first.setPadding(15, 5, 15, 5);
                        first.setText(temp4[a]);
                        linearLayout_firmPan.addView(first); //This is my linear layout which id is Layout_second_balls
                    }
                    scrollViewfirmPan.setVisibility(View.VISIBLE);
                }



                Gst_UrlNew = (!TextUtil.isStringNullOrBlank(profileData.getGst_proof()) ? profileData.getGst_proof() : "");


                String[] temp5 = Gst_UrlNew.split(",");

                LinearLayout.LayoutParams params5= new
                        LinearLayout.LayoutParams
                        (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);

                if (!TextUtil.isStringNullOrBlank(Gst_UrlNew)) {

                    for (int a = 0; a < temp5.length; a++) {

                        params5.setMargins(10, 0, 0, 0);
                        TextView first = new TextView(getApplicationContext());
                        first.setLayoutParams(params5);
                        first.setTextColor(Color.parseColor("#FFFFFF"));
                        first.setTextSize(10);
                        first.setGravity(Gravity.CENTER);
                        first.setBackground(getResources().getDrawable(
                                R.drawable.oval_shape_theme_color));
                        // params.weight = 1; /// Need to give weight if you want equal size of textview inside Linearfirst.setGravity(Gravity.LEFT);
                        first.setPadding(15, 5, 15, 5);
                        first.setText(temp5[a]);
                        linearLayout_GST.addView(first); //This is my linear layout which id is Layout_second_balls
                    }
                    scrollViewShopGST.setVisibility(View.VISIBLE);
                }




                Address_UrlNew = (!TextUtil.isStringNullOrBlank(profileData.getAddress_proof()) ? profileData.getAddress_proof() : "");

                String[] temp6 = Address_UrlNew.split(",");

                LinearLayout.LayoutParams params6= new
                        LinearLayout.LayoutParams
                        (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);

                if (!TextUtil.isStringNullOrBlank(Address_UrlNew)) {

                    for(int a = 0;a<temp6.length;a++)
                    {
                        params6.setMargins(10, 0, 0, 0);
                        TextView first = new TextView(getApplicationContext());
                        first.setLayoutParams(params6);
                        first.setTextColor(Color.parseColor("#FFFFFF"));
                        first.setTextSize(10);
                        first.setGravity(Gravity.CENTER);
                        first.setBackground(getResources().getDrawable(
                                R.drawable.oval_shape_theme_color));
                        // params.weight = 1; /// Need to give weight if you want equal size of textview inside Linearfirst.setGravity(Gravity.LEFT);
                        first.setPadding(15, 5, 15, 5);
                        first.setText(temp6[a]);
                        linearLayout_BAddress.addView(first); //This is my linear layout which id is Layout_second_balls
                    }
                    scrollViewBAddress.setVisibility(View.VISIBLE);}


                Cheque_UrlNew = (!TextUtil.isStringNullOrBlank(profileData.getCheque_proof()) ? profileData.getCheque_proof() : "");

                String[] temp7 = Cheque_UrlNew.split(",");

                LinearLayout.LayoutParams params7= new
                        LinearLayout.LayoutParams
                        (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);

                if (!TextUtil.isStringNullOrBlank(Cheque_UrlNew)) {

                    for(int a = 0;a<temp7.length;a++)
                    {
                        params7.setMargins(10, 0, 0, 0);
                        TextView first = new TextView(getApplicationContext());
                        first.setLayoutParams(params7);
                        first.setTextColor(Color.parseColor("#FFFFFF"));
                        first.setTextSize(10);
                        first.setGravity(Gravity.CENTER);
                        first.setBackground(getResources().getDrawable(
                                R.drawable.oval_shape_theme_color));
                        // params.weight = 1; /// Need to give weight if you want equal size of textview inside Linearfirst.setGravity(Gravity.LEFT);
                        first.setPadding(15, 5, 15, 5);
                        first.setText(temp7[a]);
                        linearLayout_Cheque.addView(first); //This is my linear layout which id is Layout_second_balls
                    }
                    scrollViewCheque.setVisibility(View.VISIBLE);}



                Passport_UrlNew = (!TextUtil.isStringNullOrBlank(profileData.getPassport_size_photo()) ? profileData.getPassport_size_photo() : "");

                String[] temp8 = Passport_UrlNew.split(",");

                LinearLayout.LayoutParams params8= new
                        LinearLayout.LayoutParams
                        (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);

                if (!TextUtil.isStringNullOrBlank(Passport_UrlNew)) {

                    for(int a = 0;a<temp8.length;a++)
                    {
                        params8.setMargins(10, 0, 0, 0);
                        TextView first = new TextView(getApplicationContext());
                        first.setLayoutParams(params8);
                        first.setTextColor(Color.parseColor("#FFFFFF"));
                        first.setTextSize(10);
                        first.setGravity(Gravity.CENTER);
                        first.setBackground(getResources().getDrawable(
                                R.drawable.oval_shape_theme_color));
                        // params.weight = 1; /// Need to give weight if you want equal size of textview inside Linearfirst.setGravity(Gravity.LEFT);
                        first.setPadding(15, 5, 15, 5);
                        first.setText(temp8[a]);
                        linearLayout_photoPass.addView(first); //This is my linear layout which id is Layout_second_balls
                    }

                    scrollViewPhotoPass.setVisibility(View.VISIBLE);}



                SignedForm_UrlNew = (!TextUtil.isStringNullOrBlank(profileData.getSigned_form()) ? profileData.getSigned_form() : "");


                String[] temp9 = SignedForm_UrlNew.split(",");

                LinearLayout.LayoutParams params9= new
                        LinearLayout.LayoutParams
                        (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);

                if (!TextUtil.isStringNullOrBlank(SignedForm_UrlNew)) {

                    for(int a = 0;a<temp9.length;a++)
                    {
                        params9.setMargins(10, 0, 0, 0);
                        TextView first = new TextView(getApplicationContext());
                        first.setLayoutParams(params9);
                        first.setTextColor(Color.parseColor("#FFFFFF"));
                        first.setTextSize(10);
                        first.setGravity(Gravity.CENTER);
                        first.setBackground(getResources().getDrawable(
                                R.drawable.oval_shape_theme_color));
                        // params.weight = 1; /// Need to give weight if you want equal size of textview inside Linearfirst.setGravity(Gravity.LEFT);
                        first.setPadding(15, 5, 15, 5);
                        first.setText(temp9[a]);
                        linearLayout_SignedForm.addView(first); //This is my linear layout which id is Layout_second_balls
                    }
                    scrollViewSignedForm.setVisibility(View.VISIBLE);}



                Shop_UrlNew = (!TextUtil.isStringNullOrBlank(profileData.getShop_photo()) ? profileData.getShop_photo() : "");

                String[] temp10 = Shop_UrlNew.split(",");

                LinearLayout.LayoutParams params10= new
                        LinearLayout.LayoutParams
                        (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);

                if (!TextUtil.isStringNullOrBlank(Shop_UrlNew)) {

                    for(int a = 0;a<temp10.length;a++)
                    {
                        params10.setMargins(10, 0, 0, 0);
                        TextView first = new TextView(getApplicationContext());
                        first.setLayoutParams(params10);
                        first.setTextColor(Color.parseColor("#FFFFFF"));
                        first.setTextSize(10);
                        first.setGravity(Gravity.CENTER);
                        first.setBackground(getResources().getDrawable(
                                R.drawable.oval_shape_theme_color));
                        // params.weight = 1; /// Need to give weight if you want equal size of textview inside Linearfirst.setGravity(Gravity.LEFT);
                        first.setPadding(15, 5, 15, 5);
                        first.setText(temp10[a]);
                        linearLayout_ShopPhoto.addView(first); //This is my linear layout which id is Layout_second_balls
                    }

                    scrollViewShopPhoto.setVisibility(View.VISIBLE);}
            }
        }

        if (!TextUtil.isStringNullOrBlank(Adhar_Url)) {
            String[] Adhar_images = Adhar_Url.split(",");
            upLoadAadharAdapter = new UploadRemoveAdapter(Adhar_images, R.layout.doc_upload_remove, context, Constant.K_AADHAAR);
            aadhar_recycler_view.setAdapter(upLoadAadharAdapter);
//            aadhar_recycler_view.setAdapter(new UploadRemoveAdapter(Adhar_images, R.layout.doc_upload_remove, context, Constant.K_AADHAAR));
//            aadhar_recycler_view.setScrollContainer(false);
        } else {
            String[] Adhar_images = new String[0];
            upLoadAadharAdapter = new UploadRemoveAdapter(Adhar_images, R.layout.doc_upload_remove, context, Constant.K_AADHAAR);
            aadhar_recycler_view.setAdapter(upLoadAadharAdapter);
        }
        if (!TextUtil.isStringNullOrBlank(Pan_Url)) {
            String[] Pan_images = Pan_Url.split(",");
            //List<String> images = Arrays.asList(Pan_images);
            uploadPanAdapter = new UploadRemoveAdapter(Pan_images, R.layout.doc_upload_remove, context, Constant.K_PAN);
            pan_recycler_view.setAdapter(uploadPanAdapter);
//            pan_recycler_view.setAdapter(new UploadRemoveAdapter(Pan_images, R.layout.doc_upload_remove, context, Constant.K_PAN));
//            pan_recycler_view.setScrollContainer(false);

        } else {
            String[] Pan_images = new String[0];
            //List<String> images = Arrays.asList(Pan_images);
            uploadPanAdapter = new UploadRemoveAdapter(Pan_images, R.layout.doc_upload_remove, context, Constant.K_PAN);
            pan_recycler_view.setAdapter(uploadPanAdapter);
        }
        if (!TextUtil.isStringNullOrBlank(IbdaUrl)) {
            String[] Ibda_images = IbdaUrl.split(",");

            uploadIBDAAdapter = new UploadRemoveAdapter(Ibda_images, R.layout.doc_upload_remove, context, Constant.K_IBDA);
            IbdaCode_recycler_view.setAdapter(uploadIBDAAdapter);
//            IbdaCode_recycler_view.setAdapter(new UploadRemoveAdapter(Ibda_images, R.layout.doc_upload_remove, context, Constant.K_IBDA));
//            IbdaCode_recycler_view.setScrollContainer(false);

        } else {
            String[] Ibda_images = new String[0];

            uploadIBDAAdapter = new UploadRemoveAdapter(Ibda_images, R.layout.doc_upload_remove, context, Constant.K_IBDA);
            IbdaCode_recycler_view.setAdapter(uploadIBDAAdapter);
        }
        if (!TextUtil.isStringNullOrBlank(Address_Url)) {
            String[] Address_images = Address_Url.split(",");
            // List<String> images = Arrays.asList(Address_images);
            uploadBsnAdhar = new UploadRemoveAdapter(Address_images, R.layout.doc_upload_remove, context, Constant.K_ADDRESS);
            bsnAddr_photo_recycler_view.setAdapter(uploadBsnAdhar);
//            bsnAddr_photo_recycler_view.setAdapter(new UploadRemoveAdapter(Address_images, R.layout.doc_upload_remove, context, Constant.K_ADDRESS));
//            bsnAddr_photo_recycler_view.setScrollContainer(false);

        } else {
            String[] Address_images = new String[0];
            // List<String> images = Arrays.asList(Address_images);
            uploadBsnAdhar = new UploadRemoveAdapter(Address_images, R.layout.doc_upload_remove, context, Constant.K_ADDRESS);
            bsnAddr_photo_recycler_view.setAdapter(uploadBsnAdhar);
        }

        if (!TextUtil.isStringNullOrBlank(Gst_Url)) {
            String[] gst_images = Gst_Url.split(",");

            uploadGSTPhotoAdapter = new UploadRemoveAdapter(gst_images, R.layout.doc_upload_remove, context, Constant.K_GST);
            gst_photo_recycler_view.setAdapter(uploadGSTPhotoAdapter);
//            gst_photo_recycler_view.setAdapter(new UploadRemoveAdapter(gst_images, R.layout.doc_upload_remove, context, Constant.K_GST));
//            gst_photo_recycler_view.setScrollContainer(false);

        } else {
            String[] gst_images = new String[0];

            uploadGSTPhotoAdapter = new UploadRemoveAdapter(gst_images, R.layout.doc_upload_remove, context, Constant.K_GST);
            gst_photo_recycler_view.setAdapter(uploadGSTPhotoAdapter);
        }

        if (!TextUtil.isStringNullOrBlank(Firm_Pan_Url)) {
            String[] FirmPan_images = Firm_Pan_Url.split(",");
            uploadFirmPanAdapter = new UploadRemoveAdapter(FirmPan_images, R.layout.doc_upload_remove, context, Constant.K_FIRM_PAN);
            frmPan_recycler_view.setAdapter(uploadFirmPanAdapter);
//            frmPan_recycler_view.setAdapter(new UploadRemoveAdapter(FirmPan_images, R.layout.doc_upload_remove, context, Constant.K_FIRM_PAN));
//            frmPan_recycler_view.setScrollContainer(false);

        } else {
            String[] FirmPan_images = new String[0];
            uploadFirmPanAdapter = new UploadRemoveAdapter(FirmPan_images, R.layout.doc_upload_remove, context, Constant.K_FIRM_PAN);
            frmPan_recycler_view.setAdapter(uploadFirmPanAdapter);
        }

        if (!TextUtil.isStringNullOrBlank(Cheque_Url)) {
            String[] Cheque_images = Cheque_Url.split(",");
            uploadCnclChequeAdapter = new UploadRemoveAdapter(Cheque_images, R.layout.doc_upload_remove, context, Constant.K_CHEQUE);
            cnclChq_recycler_view.setAdapter(uploadCnclChequeAdapter);
//            cnclChq_recycler_view.setAdapter(new UploadRemoveAdapter(Cheque_images, R.layout.doc_upload_remove, context, Constant.K_CHEQUE));
//            cnclChq_recycler_view.setScrollContainer(false);

        } else {
            String[] Cheque_images = new String[0];
            uploadCnclChequeAdapter = new UploadRemoveAdapter(Cheque_images, R.layout.doc_upload_remove, context, Constant.K_CHEQUE);
            cnclChq_recycler_view.setAdapter(uploadCnclChequeAdapter);
        }

        if (!TextUtil.isStringNullOrBlank(Passport_Url)) {
            String[] Passport_images = Passport_Url.split(",");
            uploadPassportAdapter = new UploadRemoveAdapter(Passport_images, R.layout.doc_upload_remove, context, Constant.K_PASSPORT);
            pssprt_photo_recycler_view.setAdapter(uploadPassportAdapter);
//            pssprt_photo_recycler_view.setAdapter(new UploadRemoveAdapter(Passport_images, R.layout.doc_upload_remove, context, Constant.K_PASSPORT));
//            pssprt_photo_recycler_view.setScrollContainer(false);

        } else {
            String[] Passport_images = new String[0];
            uploadPassportAdapter = new UploadRemoveAdapter(Passport_images, R.layout.doc_upload_remove, context, Constant.K_PASSPORT);
            pssprt_photo_recycler_view.setAdapter(uploadPassportAdapter);
        }

        if (!TextUtil.isStringNullOrBlank(SignedForm_Url)) {
            String[] Signed_images = SignedForm_Url.split(",");

            uploadSignedAdapter = new UploadRemoveAdapter(Signed_images, R.layout.doc_upload_remove, context, Constant.K_SIGNED);
            upldsgn_photo_recycler_view.setAdapter(uploadSignedAdapter);
//            upldsgn_photo_recycler_view.setAdapter(new UploadRemoveAdapter(Signed_images, R.layout.doc_upload_remove, context, Constant.K_SIGNED));
//            upldsgn_photo_recycler_view.setScrollContainer(false);

        } else {
            String[] Signed_images = new String[0];

            uploadSignedAdapter = new UploadRemoveAdapter(Signed_images, R.layout.doc_upload_remove, context, Constant.K_SIGNED);
            upldsgn_photo_recycler_view.setAdapter(uploadSignedAdapter);
        }

        if (!TextUtil.isStringNullOrBlank(Shop_Url)) {
            String[] Shop_images = Shop_Url.split(",");
            uploadShopAdapter = new UploadRemoveAdapter(Shop_images, R.layout.doc_upload_remove, context, Constant.K_SHOP);
            upldshop_photo_recycler_view.setAdapter(uploadShopAdapter);
//            upldshop_photo_recycler_view.setAdapter(new UploadRemoveAdapter(Shop_images, R.layout.doc_upload_remove, context, Constant.K_SHOP));
//            upldshop_photo_recycler_view.setScrollContainer(false);

        } else {
            String[] Shop_images = new String[0];
            uploadShopAdapter = new UploadRemoveAdapter(Shop_images, R.layout.doc_upload_remove, context, Constant.K_SHOP);
            upldshop_photo_recycler_view.setAdapter(uploadShopAdapter);
        }

        /*textAdharFile.setText(Adhar_Url);
        textCPPANFile.setText(Pan_Url);
        textFirmFile.setText(Firm_Pan_Url);
        textGSTFile.setText(Gst_Url);
        textaddressFile.setText(Address_Url);
        textCHequeFile.setText(Cheque_Url);
        textPassportFile.setText(Passport_Url);
        textSignedFormFile.setText(SignedForm_Url);
        textshopPhotoFile.setText(Shop_Url);*/
        if (!TextUtil.isStringNullOrBlank(Adhar_Url)) {
            // btnUpload.setText("Done");
            Imcheck01.setVisibility(View.VISIBLE);
            // btnUpload.setBackgroundColor(Color.GREEN);
            //btnUpload.setBackground(getResources().getDrawable(R.drawable.button_upload_file));

        }
        if (!TextUtil.isStringNullOrBlank(Pan_Url)) {
            // btnUploadPAN.setText("Done");
            Imcheck02.setVisibility(View.VISIBLE);
            // btnUploadPAN.setBackgroundColor(Color.GREEN);
            // btnUploadPAN.setBackground(getResources().getDrawable(R.drawable.button_upload_file));
        }
        if (!TextUtil.isStringNullOrBlank(IbdaUrl)) {
            // btnUploadIbdaCode.setText("Done");
            ImcheckIbdaCode.setVisibility(View.VISIBLE);
            // btnUploadPAN.setBackgroundColor(Color.GREEN);
            //  btnUploadIbdaCode.setBackground(getResources().getDrawable(R.drawable.button_upload_file));
        }
        if (!TextUtil.isStringNullOrBlank(Firm_Pan_Url)) {
            // btnUploadFirmPan.setText("Done");
            Imcheck05.setVisibility(View.VISIBLE);
            // btnUploadFirmPan.setBackgroundColor(Color.GREEN);
            //  btnUploadFirmPan.setBackground(getResources().getDrawable(R.drawable.button_upload_file));
        }
        if (!TextUtil.isStringNullOrBlank(Gst_Url)) {
            //  btnUploadGST.setText("Done");
            Imcheck03.setVisibility(View.VISIBLE);
            // btnUploadGST.setBackgroundColor(Color.GREEN);
            // btnUploadGST.setBackground(getResources().getDrawable(R.drawable.button_upload_file));
        }
        if (!TextUtil.isStringNullOrBlank(Address_Url)) {
            //  btnUploadAddress.setText("Done");
            Imcheck04.setVisibility(View.VISIBLE);
            // btnUploadAddress.setBackgroundColor(Color.GREEN);
            //  btnUploadAddress.setBackground(getResources().getDrawable(R.drawable.button_upload_file));
        }
        if (!TextUtil.isStringNullOrBlank(Cheque_Url)) {
            // btnUploadCheque.setText("Done");
            Imcheck06.setVisibility(View.VISIBLE);
            // btnUploadCheque.setBackgroundColor(Color.GREEN);
            // btnUploadCheque.setBackground(getResources().getDrawable(R.drawable.button_upload_file));
        }
        if (!TextUtil.isStringNullOrBlank(Passport_Url)) {
            // btnUploadPassport.setText("Done");
            Imcheck07.setVisibility(View.VISIBLE);
            //  btnUploadPassport.setBackgroundColor(Color.GREEN);
            // btnUploadPassport.setBackground(getResources().getDrawable(R.drawable.button_upload_file));
        }
        if (!TextUtil.isStringNullOrBlank(SignedForm_Url)) {
            // btnUploadSignedForm.setText("Done");
            Imcheck08.setVisibility(View.VISIBLE);
            //btnUploadSignedForm.setBackgroundColor(Color.GREEN);
            // btnUploadSignedForm.setBackground(getResources().getDrawable(R.drawable.button_upload_file));
        }
        if (!TextUtil.isStringNullOrBlank(Shop_Url)) {
            // btnUploadshopPhoto.setText("Done");
            Imcheck09.setVisibility(View.VISIBLE);
            // btnUploadshopPhoto.setBackgroundColor(Color.GREEN);
            //  btnUploadshopPhoto.setBackground(getResources().getDrawable(R.drawable.button_upload_file));
        }
        displayUploadOption();
//        etFirmName.setText((!TextUtil.isStringNullOrBlank(profileData.getUserDetail().getCp_firm_name()) ? profileData.getUserDetail().getCp_firm_name() : ""));
//        etCommunicationAddress.setText((!TextUtil.isStringNullOrBlank(profileData.getUserDetail().getCommunication_address_id()) ? profileData.getUserDetail().getCommunication_address_id() : ""));
//        etBusinessAddress.setText((!TextUtil.isStringNullOrBlank(profileData.getUserDetail().getBussiness_address_id()) ? profileData.getUserDetail().getBussiness_address_id() : ""));
    }


    private void displayUploadOption() {
        ArrayAdapter<String> spinnerOptionArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, uploadTypeOption) {

            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setTypeface(App.LATO_REGULAR);//Typeface for normal view
                return v;
            }

            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                ((TextView) v).setTypeface(App.LATO_REGULAR);//Typeface for dropdown view
                // ((TextView) v).setBackgroundColor(Color.parseColor("#BBfef3da"));
                return v;
            }
        };
        ;
        spinnerOptionArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        spUploadOption.setAdapter(spinnerOptionArrayAdapter);

        spUploadOption.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spUploadOptionPosition = position;
                Log.i("UPLDD", spUploadOptionPosition + "");
                if (spUploadOptionPosition == 0) {
                    selectedUploadType = "aadhaar";
                    textCPAdhar.setText(ShowHintOrText.GetMandatory("Aadhar Number"));
                    etAddhar.setHint("Enter 12 digits aadhar number");
                    etAddhar.setVisibility(View.VISIBLE);
                } else if (spUploadOptionPosition == 1) {
                    selectedUploadType = "dl";
                    textCPAdhar.setText(ShowHintOrText.GetMandatory("Driving Licence Number"));
                    etAddhar.setHint("Enter 15 digits driving licence number");
                    etAddhar.setVisibility(View.VISIBLE);
                } else if (spUploadOptionPosition == 2) {
                    selectedUploadType = "voterId";
                    textCPAdhar.setText(ShowHintOrText.GetMandatory("Voter Id Number"));
                    etAddhar.setHint("Enter 11 digits voter id number");
                    etAddhar.setVisibility(View.VISIBLE);
                } else if (spUploadOptionPosition == 3) {
                    selectedUploadType = "passport";
                    textCPAdhar.setText(ShowHintOrText.GetMandatory("Passport Number"));
                    etAddhar.setHint("Enter valid Passport Number");
                    etAddhar.setVisibility(View.VISIBLE);
                } else if (spUploadOptionPosition == 4) {
                    selectedUploadType = "waterBill";
                    textCPAdhar.setText(ShowHintOrText.GetMandatory("Water Bill"));
                    etAddhar.setVisibility(View.GONE);
                } else if (spUploadOptionPosition == 5) {
                    selectedUploadType = "mobileBill";
                    textCPAdhar.setText(ShowHintOrText.GetMandatory("Landline or Postpaid mobile bill"));
                    etAddhar.setVisibility(View.GONE);
                } else if (spUploadOptionPosition == 6) {
                    selectedUploadType = "electricityBill";
                    textCPAdhar.setText(ShowHintOrText.GetMandatory("Electricity bill"));
                    etAddhar.setVisibility(View.GONE);
                } else if (spUploadOptionPosition == 7) {
                    selectedUploadType = "bankStatement";
                    textCPAdhar.setText(ShowHintOrText.GetMandatory("Bank Passbook/ Statement"));
                    etAddhar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });
        if (mySingleton.getData(Constant.USER_GROUP_ID).equalsIgnoreCase(Constant.SUPER_CP_GROUP_ID)) {
            if (profileData != null) {
                Log.i("rtn1", spUploadOptionPosition + "");
                for (int i = 0; i < uploadTypeOption.length; i++) {
                    if (i ==profileData.getUpload_type_option()) {
                        spUploadOptionPosition = i;
                        Log.i("rtn", spUploadOptionPosition + "");
                    }
                }
                //spUploadOptionPosition = profileData.getUpload_type_option();
            }
        } else {
            if (profileData != null) {
                Log.i("rtn1", spUploadOptionPosition + "");
                for (int i = 0; i < uploadTypeOption.length; i++) {
                    if (i == profileData.getUpload_type_option()) {
                        spUploadOptionPosition = i;
                        Log.i("rtn", spUploadOptionPosition + "");
                    }
                }
                //spUploadOptionPosition = profileData.getUpload_type_option();
            }
        }

        //Log.i("rtn", spUploadOptionPosition+"");
        spUploadOption.setSelection(spUploadOptionPosition);
        if (spUploadOptionPosition == 0) {
            selectedUploadType = "aadhaar";
            textCPAdhar.setText(ShowHintOrText.GetMandatory("Aadhar Number"));
            etAddhar.setHint("Enter 12 digits aadhar number");
            etAddhar.setVisibility(View.VISIBLE);
        } else if (spUploadOptionPosition == 1) {
            selectedUploadType = "dl";
            textCPAdhar.setText(ShowHintOrText.GetMandatory("Driving Licence Number"));
            etAddhar.setHint("Enter 15 digits driving licence number");
            etAddhar.setVisibility(View.VISIBLE);
        } else if (spUploadOptionPosition == 2) {
            selectedUploadType = "voterId";
            textCPAdhar.setText(ShowHintOrText.GetMandatory("Voter Id Number"));
            etAddhar.setHint("Enter 11 digits voter id number");
            etAddhar.setVisibility(View.VISIBLE);
        } else if (spUploadOptionPosition == 3) {
            selectedUploadType = "passport";
            textCPAdhar.setText(ShowHintOrText.GetMandatory("Passport Number"));
            etAddhar.setHint("Enter valid Passport Number");
            etAddhar.setVisibility(View.VISIBLE);
        } else if (spUploadOptionPosition == 4) {
            selectedUploadType = "waterBill";
            textCPAdhar.setText(ShowHintOrText.GetMandatory("Water Bill"));
            etAddhar.setVisibility(View.GONE);
        } else if (spUploadOptionPosition == 5) {
            selectedUploadType = "mobileBill";
            textCPAdhar.setText(ShowHintOrText.GetMandatory("Landline or Postpaid mobile bill"));
            etAddhar.setVisibility(View.GONE);
        } else if (spUploadOptionPosition == 6) {
            selectedUploadType = "electricityBill";
            textCPAdhar.setText(ShowHintOrText.GetMandatory("Electricity bill"));
            etAddhar.setVisibility(View.GONE);
        } else if (spUploadOptionPosition == 7) {
            selectedUploadType = "bankStatement";
            textCPAdhar.setText(ShowHintOrText.GetMandatory("Bank Passbook/ Statement"));
            etAddhar.setVisibility(View.GONE);
        }

    }

    private void viewListener() {
        imgLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (network.isNetworkConnected(context)) {
                    callLogout();
                } else {
                    displaySnackBar.DisplaySnackBar(Constant.NETWIRK_ERROR, Constant.TYPE_ERROR);
                }
            }
        });
        icBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (network.isNetworkConnected(context)) {
                    callUpdateProfile();
                } else {
                    displaySnackBar.DisplaySnackBar(Constant.NETWIRK_ERROR, Constant.TYPE_ERROR);
                }


//                if (!TextUtil.isStringNullOrBlank(callFrom)) {
//                    if (callFrom.equalsIgnoreCase("1")) {
//                        EditProfileRequest editProfileRequest = new EditProfileRequest();
//                        callNextStep(editProfileRequest);
//                    } else {
//                        if (isFormValidated()) {
//                            if (network.isNetworkConnected(context)) {
//                                callUpdateProfile();
//                            } else {
//                                displaySnackBar.DisplaySnackBar(Constant.NETWIRK_ERROR, Constant.TYPE_ERROR);
//                            }
//                        }
//                    }
//                } else {
//                    if (isFormValidated()) {
//                        if (network.isNetworkConnected(context)) {
//                            callUpdateProfile();
//                        } else {
//                            displaySnackBar.DisplaySnackBar(Constant.NETWIRK_ERROR, Constant.TYPE_ERROR);
//                        }
//                    }
//                }
            }
        });

        // No Longer use

       /* btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               *//* if (isStoragePermissionGranted()) {
                    selectResume(FILE_SELECT_ADHAR);
                }*//*
                callPickMedia(FILE_SELECT_ADHAR);
            }
        });
        btnUploadPAN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                *//*if (isStoragePermissionGranted()) {
                    selectResume(FILE_SELECT_CPPAN);
                }*//*
                callPickMedia(FILE_SELECT_CPPAN);
            }
        });
        btnUploadFirmPan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              *//*  if (isStoragePermissionGranted()) {
                    selectResume(FILE_SELECT_FIRMPAN);
                }*//*
                callPickMedia(FILE_SELECT_FIRMPAN);
            }
        });
        btnUploadGST.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               *//* if (isStoragePermissionGranted()) {
                    selectResume(FILE_SELECT_GST);
                }*//*
                callPickMedia(FILE_SELECT_GST);
            }
        });
        btnUploadAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               *//* if (isStoragePermissionGranted()) {
                    selectResume(FILE_SELECT_ADDRESS);
                }*//*
                callPickMedia(FILE_SELECT_ADDRESS);
            }
        });
        btnUploadCheque.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               *//* if (isStoragePermissionGranted()) {
                    selectResume(FILE_SELECT_CHEQUE);
                }*//*
                callPickMedia(FILE_SELECT_CHEQUE);
            }
        });
        btnUploadPassport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               *//* if (isStoragePermissionGranted()) {
                    selectResume(FILE_SELECT_CHEQUE);
                }*//*
                callPickMedia(FILE_SELECT_PASSPORT);
            }
        });
        btnUploadSignedForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               *//* if (isStoragePermissionGranted()) {
                    selectResume(FILE_SELECT_CHEQUE);
                }*//*
                callPickMedia(FILE_SELECT_SIGNED_FORM);
            }
        });
        btnUploadshopPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               *//* if (isStoragePermissionGranted()) {
                    selectResume(FILE_SELECT_CHEQUE);
                }*//*
                callPickMedia(FILE_SELECT_SHOP);
            }
        });
        btnUploadIbdaCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               *//* if (isStoragePermissionGranted()) {
                    selectResume(FILE_SELECT_CHEQUE);
                }*//*
                callPickMedia(FILE_SELECT_IBDA);
            }
        });*/


        Imcamera01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //dispatchTakePictureIntent(AADHAR_IMAGE_CLICK_REQUEST);
                if (isStoragePermissionGranted())
                    dispatchTakePictureIntentReal(AADHAR_IMAGE_CLICK_REQUEST);
            }
        });
        Imattach01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isStoragePermissionGranted())
                    choosePictureFromGallery(AADHAR_IMAGE_ATTACH_REQUEST);
            }
        });

        Imcamera02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //dispatchTakePictureIntent(PAN_IMAGE_CLICK_REQUEST);
                if (isStoragePermissionGranted())
                    dispatchTakePictureIntentReal(PAN_IMAGE_CLICK_REQUEST);
            }
        });
        Imattach02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isStoragePermissionGranted())
                    choosePictureFromGallery(PAN_IMAGE_ATTACH_REQUEST);
            }
        });


        Imcamera03.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //dispatchTakePictureIntent(GST_IMAGE_CLICK_REQUEST);
                if (isStoragePermissionGranted())
                    dispatchTakePictureIntentReal(GST_IMAGE_CLICK_REQUEST);
            }
        });
        Imattach03.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isStoragePermissionGranted())
                    choosePictureFromGallery(GST_IMAGE_ATTACH_REQUEST);
            }
        });


        Imcamera04.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //dispatchTakePictureIntent(ADDRESS_IMAGE_CLICK_REQUEST);
                if (isStoragePermissionGranted())
                    dispatchTakePictureIntentReal(ADDRESS_IMAGE_CLICK_REQUEST);
            }
        });
        Imattach04.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isStoragePermissionGranted())
                    choosePictureFromGallery(ADDRESS_IMAGE_ATTACH_REQUEST);
            }
        });


        Imcamera05.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // dispatchTakePictureIntent(FIRM_PAN_IMAGE_CLICK_REQUEST);
                if (isStoragePermissionGranted())
                    dispatchTakePictureIntentReal(FIRM_PAN_IMAGE_CLICK_REQUEST);
            }
        });
        Imattach05.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isStoragePermissionGranted())
                    choosePictureFromGallery(FIRM_PAN_IMAGE_ATTACH_REQUEST);
            }
        });


        Imcamera06.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //dispatchTakePictureIntent(CHEQUE_IMAGE_CLICK_REQUEST);
                if (isStoragePermissionGranted())
                    dispatchTakePictureIntentReal(CHEQUE_IMAGE_CLICK_REQUEST);
            }
        });
        Imattach06.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isStoragePermissionGranted())
                    choosePictureFromGallery(CHEQUE_IMAGE_ATTACH_REQUEST);
            }
        });

        Imcamera07.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // dispatchTakePictureIntent(PASSPORT_IMAGE_CLICK_REQUEST);
                if (isStoragePermissionGranted())
                    dispatchTakePictureIntentReal(PASSPORT_IMAGE_CLICK_REQUEST);
            }
        });
        Imattach07.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isStoragePermissionGranted())
                    choosePictureFromGallery(PASSPORT_IMAGE_ATTACH_REQUEST);
            }
        });


        Imcamera08.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //dispatchTakePictureIntent(SIGNED_FORM_IMAGE_CLICK_REQUEST);
                if (isStoragePermissionGranted())
                    dispatchTakePictureIntentReal(SIGNED_FORM_IMAGE_CLICK_REQUEST);
            }
        });
        Imattach08.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isStoragePermissionGranted())
                    choosePictureFromGallery(SIGNED_FORM_IMAGE_ATTACH_REQUEST);
            }
        });


        Imcamera09.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLocationData();
                //dispatchTakePictureIntent(OFFICE_PHOTO_IMAGE_CLICK_REQUEST);
                if (isStoragePermissionGranted())
                    dispatchTakePictureIntentReal(OFFICE_PHOTO_IMAGE_CLICK_REQUEST);
            }
        });
        Imattach09.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLocationData();
                choosePictureFromGallery(OFFICE_PHOTO_IMAGE_ATTACH_REQUEST);
            }
        });
        ImcameraIbdaCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLocationData();
                //dispatchTakePictureIntent(OFFICE_PHOTO_IMAGE_CLICK_REQUEST);
                if (isStoragePermissionGranted())
                    dispatchTakePictureIntentReal(IBDA_IMAGE_CLICK_REQUEST);
            }
        });
        ImattachIbdaCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLocationData();
                choosePictureFromGallery(IBDA_IMAGE_ATTACH_REQUEST);
            }
        });
    }

    private void callPickMedia(int callfor) {
        if (isStoragePermissionGranted()) {
            Matisse.from(DashboardDocUploadEdit.this)
                    .choose(MimeType.ofImage(), false)
                    .countable(true)
                    .capture(true)
                    .captureStrategy(
                            new CaptureStrategy(true, getPackageName() + ".fileprovider"))
                    .maxSelectable(2)
                    .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
                    .gridExpectedSize(
                            getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                    .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                    .thumbnailScale(0.85f)
                    .imageEngine(new GlideEngine())
                    .forResult(callfor);
        }
    }

    /**
     * Print error code in case of any API call
     *
     * @param statusCode 200 / 400 / 401 / 402 etc
     */
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
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
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
                    }
                })
                .show();
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
                            dispatchTakePictureIntentReal(selectionType);
                        }
                    }
                });


        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    private void callNextStep(EditProfileRequest editProfileRequest) {
        Intent stepSecond = new Intent(context, DashboardBankDetails.class);
        if (!TextUtil.isStringNullOrBlank(callFrom)) {
            if (callFrom.equalsIgnoreCase("1")) {
                stepSecond.putExtra("call_from_enable", "1");
            }
        }
        DashboardBankDetails.editProfileRequest = editProfileRequest;
        stepSecond.putExtra("call_from", "DashboardDocUpload");
        startActivity(stepSecond);
    }
    /******************************************* End Methods *************************************
     */
    /******************************************** Start Interface methods **************************
     */

    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("permission", "Permission is granted");
                return true;
            } else {

                Log.v("permission", "Permission is revoked");
                ActivityCompat.requestPermissions(DashboardDocUploadEdit.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v("permission", "Permission is granted");
            return true;
        }
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
                ActivityCompat.requestPermissions(DashboardDocUploadEdit.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CAMERA}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v("permission", "Permission is granted");
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.v("Permission: ", "was " + grantResults[0]);
            //resume tasks needing this permission
        }
    }

    public void freeMemory() {
//        System.runFinalization();
//        Runtime.getRuntime().gc();
//        System.gc();
    }

    @Override
    protected void onDestroy() {
        freeMemory();
        super.onDestroy();

    }
    private Uri mImageUri;
    public File grabImageFile(boolean compress, int quality) {
        File returnFile = null;
        try {
            //InputStream is = getContentResolver().openInputStream(mImageUri);
            if(mImageUri!=null) {
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
//
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
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("OnActivityResult", "Job Criteria");

        freeMemory();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (resultCode == RESULT_OK && requestCode == FILE_SELECT_ADHAR ) {

                            obtainUri = new ArrayList<>();
                            obtainPathResult = new ArrayList<>();

                            obtainUri = Matisse.obtainResult(data);
                            obtainPathResult = Matisse.obtainPathResult(data);
                            positionForUpload = 0;

//                            callPostDocAPI(obtainPathResult, FILE_SELECT_ADHAR);
                            new GetBase64Image(obtainPathResult, FILE_SELECT_ADHAR).execute();

                        }
                        if (resultCode == RESULT_OK && requestCode == FILE_SELECT_CPPAN ) {
                            //  mAdapter.setData(Matisse.obtainResult(data), Matisse.obtainPathResult(data));
                            // Toast.makeText(DashboardDocUpload.this, "Media Selected", Toast.LENGTH_SHORT).show();
                            obtainUri = Matisse.obtainResult(data);

                            obtainPathResult = Matisse.obtainPathResult(data);

                            positionForUpload = 0;
//                            callPostDocAPI(obtainPathResult, FILE_SELECT_CPPAN);
                            new GetBase64Image(obtainPathResult, FILE_SELECT_CPPAN).execute();

                        }
                        if (resultCode == RESULT_OK && requestCode == FILE_SELECT_FIRMPAN ) {

                            obtainUri = Matisse.obtainResult(data);
                            obtainPathResult = Matisse.obtainPathResult(data);
                            positionForUpload = 0;

//                            callPostDocAPI(obtainPathResult, FILE_SELECT_FIRMPAN);
                            new GetBase64Image(obtainPathResult, FILE_SELECT_FIRMPAN).execute();
                        }
                        if (resultCode == RESULT_OK && requestCode == FILE_SELECT_GST) {

                            obtainUri = Matisse.obtainResult(data);
                            obtainPathResult = Matisse.obtainPathResult(data);
                            positionForUpload = 0;

//                            callPostDocAPI(obtainPathResult, FILE_SELECT_GST);
                            new GetBase64Image(obtainPathResult, FILE_SELECT_GST).execute();
                        }
                        if (resultCode == RESULT_OK && requestCode == FILE_SELECT_ADDRESS ) {

                            obtainUri = Matisse.obtainResult(data);
                            obtainPathResult = Matisse.obtainPathResult(data);
                            positionForUpload = 0;

//                            callPostDocAPI(obtainPathResult, FILE_SELECT_ADDRESS);
                            new GetBase64Image(obtainPathResult, FILE_SELECT_ADDRESS).execute();
                        }
                        if (resultCode == RESULT_OK && requestCode == FILE_SELECT_ADDRESS ) {

                            obtainUri = Matisse.obtainResult(data);
                            obtainPathResult = Matisse.obtainPathResult(data);
                            positionForUpload = 0;

//                            callPostDocAPI(obtainPathResult, FILE_SELECT_CHEQUE);
                            new GetBase64Image(obtainPathResult, FILE_SELECT_ADDRESS).execute();
                        }
                        if (resultCode == RESULT_OK && requestCode == FILE_SELECT_ADDRESS ) {

                            obtainUri = Matisse.obtainResult(data);
                            obtainPathResult = Matisse.obtainPathResult(data);
                            positionForUpload = 0;
                            // Passport_Url = "";
//                            callPostDocAPI(obtainPathResult, FILE_SELECT_PASSPORT);
                            new GetBase64Image(obtainPathResult, FILE_SELECT_ADDRESS).execute();
                        }
                        if (resultCode == RESULT_OK && requestCode == FILE_SELECT_ADDRESS ) {
                            //  mAdapter.setData(Matisse.obtainResult(data), Matisse.obtainPathResult(data));
                            //Toast.makeText(DashboardDocUpload.this, "Media Selected", Toast.LENGTH_SHORT).show();
                            obtainUri = Matisse.obtainResult(data);
                            obtainPathResult = Matisse.obtainPathResult(data);
                            positionForUpload = 0;
                            // SignedForm_Url = "";
//                            callPostDocAPI(obtainPathResult, FILE_SELECT_SIGNED_FORM);
                            new GetBase64Image(obtainPathResult, FILE_SELECT_ADDRESS).execute();
                        }
                        if (resultCode == RESULT_OK && requestCode == FILE_SELECT_SHOP ) {

                            obtainUri = Matisse.obtainResult(data);
                            obtainPathResult = Matisse.obtainPathResult(data);

                            positionForUpload = 0;

//                            callPostDocAPI(obtainPathResult, FILE_SELECT_SHOP);
                            new GetBase64Image(obtainPathResult, FILE_SELECT_SHOP).execute();
                        }


                        if (resultCode == RESULT_OK && requestCode == FILE_SELECT_IBDA ) {

                            obtainUri = Matisse.obtainResult(data);
                            obtainPathResult = Matisse.obtainPathResult(data);

                            positionForUpload = 0;

//                            callPostDocAPI(obtainPathResult, FILE_SELECT_SHOP);
                            new GetBase64Image(obtainPathResult, FILE_SELECT_IBDA).execute();
                        }
                        if (resultCode == RESULT_OK && requestCode == AADHAR_IMAGE_ATTACH_REQUEST && data!=null) {
                            Uri selectedImageUri = data.getData();
                            //selectedImagePath = getPath(selectedImageUri);
                            //  Log.i("GalleryPath", selectedImagePath);
                            selectedImagePath = FilePath.getPath(getBaseContext(), selectedImageUri);
                            String docType = "";
                            if (selectedFilePath.contains(".doc")) {
                                docType = "doc";
                            } else if (selectedFilePath.contains(".pdf")) {
                                docType = "pdf";
                            }
                            obtainPathResult = new ArrayList<>();
                            obtainPathResult.add(selectedImagePath);
                            //obtainPathResult = Arrays.asList(selectedImagePath);
                            positionForUpload = 0;
//                            callPostDocAPI(obtainPathResult, AADHAR_IMAGE_ATTACH_REQUEST);
                            new GetBase64Image(obtainPathResult, AADHAR_IMAGE_ATTACH_REQUEST).execute();
                            //myImage.setImageURI(selectedImageUri);
                        }
                        if (resultCode == RESULT_OK && requestCode == AADHAR_IMAGE_CLICK_REQUEST  ) {
                            File f = grabImageFile(true, 80); //true for compression , 80% quality
                            if (f != null) {
                                //  Toast.makeText(getApplicationContext(), "File to upload is " + f.getAbsolutePath(), Toast.LENGTH_LONG).show();
                                //call image uplaod code here
                                //doFileUpload(f, Constants.IMAGE,f.getName());
                                obtainPathResult = new ArrayList<>();
                                obtainPathResult.add(f.getAbsolutePath());
                                new GetBase64Image(obtainPathResult, AADHAR_IMAGE_CLICK_REQUEST).execute();
                            } else {
                                Toast.makeText(getApplicationContext(), "Image Capture Error", Toast.LENGTH_LONG).show();
                            }

                        }
                        if (resultCode == RESULT_OK && requestCode == PAN_IMAGE_ATTACH_REQUEST && data!=null) {
                            Uri selectedImageUri = data.getData();
                            selectedImagePath = getPath(selectedImageUri);
                            Log.i("GalleryPath", selectedImagePath);
                            obtainPathResult = new ArrayList<>();
                            obtainPathResult.add(selectedImagePath);
                            positionForUpload = 0;
//                            callPostDocAPI(obtainPathResult, PAN_IMAGE_ATTACH_REQUEST);
                            new GetBase64Image(obtainPathResult, PAN_IMAGE_ATTACH_REQUEST).execute();
                            //myImage.setImageURI(selectedImageUri);
                        }
                        if (requestCode == PAN_IMAGE_CLICK_REQUEST && resultCode == RESULT_OK ) {
//                            try {
//                                obtainPathResult = new ArrayList<>();
//                                obtainPathResult.add(mCurrentPhotoPath);
//                                // obtainPathResult = Arrays.asList(mCurrentPhotoPath);
//                                positionForUpload = 0;
////                                 callPostDocAPI(obtainPathResult, PAN_IMAGE_CLICK_REQUEST);
//                                new GetBase64Image(obtainPathResult, PAN_IMAGE_CLICK_REQUEST).execute();
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
                            File f = grabImageFile(true, 80); //true for compression , 80% quality
                            if (f != null) {
                                //  Toast.makeText(getApplicationContext(), "File to upload is " + f.getAbsolutePath(), Toast.LENGTH_LONG).show();
                                //call image uplaod code here
                                //doFileUpload(f, Constants.IMAGE,f.getName());
                                obtainPathResult = new ArrayList<>();
                                obtainPathResult.add(f.getAbsolutePath());
                                new GetBase64Image(obtainPathResult, PAN_IMAGE_CLICK_REQUEST).execute();
                            } else {
                                Toast.makeText(getApplicationContext(), "Image Capture Error", Toast.LENGTH_LONG).show();
                            }

                            //myImage.setImageBitmap(image);
                        }
                        if (resultCode == RESULT_OK && requestCode == IBDA_IMAGE_ATTACH_REQUEST && data!=null) {
                            Uri selectedImageUri = data.getData();
                            selectedImagePath = getPath(selectedImageUri);
                            Log.i("GalleryPath", selectedImagePath);
                            obtainPathResult = new ArrayList<>();
                            obtainPathResult.add(selectedImagePath);
                            positionForUpload = 0;
//                            callPostDocAPI(obtainPathResult, IBDA_IMAGE_ATTACH_REQUEST);
                            new GetBase64Image(obtainPathResult, IBDA_IMAGE_ATTACH_REQUEST).execute();
                            //myImage.setImageURI(selectedImageUri);
                        }
                        if (resultCode == RESULT_OK && requestCode == IBDA_IMAGE_CLICK_REQUEST ) {
//                            try {
//
//                                obtainPathResult = new ArrayList<>();
//                                obtainPathResult.add(mCurrentPhotoPath);
//                                positionForUpload = 0;
////                              callPostDocAPI(obtainPathResult, IBDA_IMAGE_ATTACH_REQUEST);
//                                new GetBase64Image(obtainPathResult, IBDA_IMAGE_CLICK_REQUEST).execute();
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }

                            File f = grabImageFile(true, 80); //true for compression , 80% quality
                            if (f != null) {
                                //  Toast.makeText(getApplicationContext(), "File to upload is " + f.getAbsolutePath(), Toast.LENGTH_LONG).show();
                                //call image uplaod code here
                                //doFileUpload(f, Constants.IMAGE,f.getName());
                                obtainPathResult = new ArrayList<>();
                                obtainPathResult.add(f.getAbsolutePath());
                                new GetBase64Image(obtainPathResult, IBDA_IMAGE_CLICK_REQUEST).execute();
                            } else {
                                Toast.makeText(getApplicationContext(), "Image Capture Error", Toast.LENGTH_LONG).show();
                            }
                        }
                        if (resultCode == RESULT_OK && requestCode == GST_IMAGE_ATTACH_REQUEST && data!=null ) {
                            Uri selectedImageUri = data.getData();
                            selectedImagePath = getPath(selectedImageUri);
                            Log.i("GalleryPath", selectedImagePath);
                            obtainPathResult = new ArrayList<>();
                            obtainPathResult.add(selectedImagePath);
                            positionForUpload = 0;
//                            callPostDocAPI(obtainPathResult, GST_IMAGE_ATTACH_REQUEST);
                            new GetBase64Image(obtainPathResult, GST_IMAGE_ATTACH_REQUEST).execute();
                        }
                        if (resultCode == RESULT_OK && requestCode == GST_IMAGE_CLICK_REQUEST  ) {
//                            try {
//                                obtainPathResult = new ArrayList<>();
//                                obtainPathResult.add(mCurrentPhotoPath);
//                                positionForUpload = 0;
//                                //callPostDocAPI(obtainPathResult, GST_IMAGE_CLICK_REQUEST);
//                                new GetBase64Image(obtainPathResult, GST_IMAGE_CLICK_REQUEST).execute();
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }

                            File f = grabImageFile(true, 80); //true for compression , 80% quality
                            if (f != null) {
                                // Toast.makeText(getApplicationContext(), "File to upload is " + f.getAbsolutePath(), Toast.LENGTH_LONG).show();
                                //call image uplaod code here
                                //doFileUpload(f, Constants.IMAGE,f.getName());
                                obtainPathResult = new ArrayList<>();
                                obtainPathResult.add(f.getAbsolutePath());
                                new GetBase64Image(obtainPathResult, GST_IMAGE_CLICK_REQUEST).execute();
                            } else {
                                Toast.makeText(getApplicationContext(), "Image Capture Error", Toast.LENGTH_LONG).show();
                            }
                        }
                        if (resultCode == RESULT_OK && requestCode == ADDRESS_IMAGE_ATTACH_REQUEST && data!=null) {
                            Uri selectedImageUri = data.getData();
                            selectedImagePath = getPath(selectedImageUri);
                            Log.i("GalleryPath", selectedImagePath);
                            obtainPathResult = new ArrayList<>();
                            obtainPathResult.add(selectedImagePath);
                            positionForUpload = 0;
//                            callPostDocAPI(obtainPathResult, ADDRESS_IMAGE_ATTACH_REQUEST);
                            new GetBase64Image(obtainPathResult, ADDRESS_IMAGE_ATTACH_REQUEST).execute();
                        }
                        if (resultCode == RESULT_OK && requestCode == ADDRESS_IMAGE_CLICK_REQUEST ) {
                          /*  try {
                                obtainPathResult = new ArrayList<>();
                                obtainPathResult.add(mCurrentPhotoPath);
                                // obtainPathResult = Arrays.asList(mCurrentPhotoPath);
                                positionForUpload = 0;
//                                callPostDocAPI(obtainPathResult, ADDRESS_IMAGE_CLICK_REQUEST);
                                new GetBase64Image(obtainPathResult, ADDRESS_IMAGE_CLICK_REQUEST).execute();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }*/
                            File f = grabImageFile(true, 80); //true for compression , 80% quality
                            if (f != null) {
                                //  Toast.makeText(getApplicationContext(), "File to upload is " + f.getAbsolutePath(), Toast.LENGTH_LONG).show();
                                //call image uplaod code here
                                //doFileUpload(f, Constants.IMAGE,f.getName());
                                obtainPathResult = new ArrayList<>();
                                obtainPathResult.add(f.getAbsolutePath());
                                new GetBase64Image(obtainPathResult, ADDRESS_IMAGE_CLICK_REQUEST).execute();
                            } else {
                                Toast.makeText(getApplicationContext(), "Image Capture Error", Toast.LENGTH_LONG).show();
                            }
                        }
                        if (resultCode == RESULT_OK && requestCode == FIRM_PAN_IMAGE_ATTACH_REQUEST && data!=null) {
                            Uri selectedImageUri = data.getData();
                            selectedImagePath = getPath(selectedImageUri);
                            Log.i("GalleryPath", selectedImagePath);
                            obtainPathResult = new ArrayList<>();
                            obtainPathResult.add(selectedImagePath);
                            positionForUpload = 0;
//                            callPostDocAPI(obtainPathResult, FIRM_PAN_IMAGE_ATTACH_REQUEST);
                            new GetBase64Image(obtainPathResult, FIRM_PAN_IMAGE_ATTACH_REQUEST).execute();
                            //myImage.setImageURI(selectedImageUri);
                        }
                        if (resultCode == RESULT_OK && requestCode == FIRM_PAN_IMAGE_CLICK_REQUEST  ) {
//                            try {
//                                obtainPathResult = new ArrayList<>();
//                                obtainPathResult.add(mCurrentPhotoPath);
//                                positionForUpload = 0;
////                                                callPostDocAPI(obtainPathResult, FIRM_PAN_IMAGE_CLICK_REQUEST);
//                                new GetBase64Image(obtainPathResult, FIRM_PAN_IMAGE_CLICK_REQUEST).execute();
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
                            File f = grabImageFile(true, 80); //true for compression , 80% quality
                            if (f != null) {
                                //  Toast.makeText(getApplicationContext(), "File to upload is " + f.getAbsolutePath(), Toast.LENGTH_LONG).show();
                                //call image uplaod code here
                                //doFileUpload(f, Constants.IMAGE,f.getName());
                                obtainPathResult = new ArrayList<>();
                                obtainPathResult.add(f.getAbsolutePath());
                                new GetBase64Image(obtainPathResult, FIRM_PAN_IMAGE_CLICK_REQUEST).execute();
                            } else {
                                Toast.makeText(getApplicationContext(), "Image Capture Error", Toast.LENGTH_LONG).show();
                            }
                        }
                        if (resultCode == RESULT_OK && requestCode == CHEQUE_IMAGE_ATTACH_REQUEST && data!=null ) {
                            Uri selectedImageUri = data.getData();
                            selectedImagePath = getPath(selectedImageUri);
                            Log.i("GalleryPath", selectedImagePath);
                            obtainPathResult = new ArrayList<>();
                            obtainPathResult.add(selectedImagePath);
                            positionForUpload = 0;
//                            callPostDocAPI(obtainPathResult, CHEQUE_IMAGE_ATTACH_REQUEST);
                            new GetBase64Image(obtainPathResult, CHEQUE_IMAGE_ATTACH_REQUEST).execute();
                            //myImage.setImageURI(selectedImageUri);
                        }
                        if (resultCode == RESULT_OK && requestCode == CHEQUE_IMAGE_CLICK_REQUEST  ) {
//                            try {
//                                obtainPathResult = new ArrayList<>();
//                                obtainPathResult.add(mCurrentPhotoPath);
//                                positionForUpload = 0;
////                              callPostDocAPI(obtainPathResult, CHEQUE_IMAGE_CLICK_REQUEST);
//                                new GetBase64Image(obtainPathResult, CHEQUE_IMAGE_CLICK_REQUEST).execute();
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
                            File f = grabImageFile(true, 80); //true for compression , 80% quality
                            if (f != null) {
                                //   Toast.makeText(getApplicationContext(), "File to upload is " + f.getAbsolutePath(), Toast.LENGTH_LONG).show();
                                //call image uplaod code here
                                //doFileUpload(f, Constants.IMAGE,f.getName());
                                obtainPathResult = new ArrayList<>();
                                obtainPathResult.add(f.getAbsolutePath());
                                new GetBase64Image(obtainPathResult, CHEQUE_IMAGE_CLICK_REQUEST).execute();
                            } else {
                                Toast.makeText(getApplicationContext(), "Image Capture Error", Toast.LENGTH_LONG).show();
                            }
                        }
                        if (resultCode == RESULT_OK && requestCode == PASSPORT_IMAGE_ATTACH_REQUEST && data!=null) {
                            Uri selectedImageUri = data.getData();
                            selectedImagePath = getPath(selectedImageUri);
                            Log.i("GalleryPath", selectedImagePath);
                            obtainPathResult = new ArrayList<>();
                            obtainPathResult.add(selectedImagePath);
                            positionForUpload = 0;
//                            callPostDocAPI(obtainPathResult, PASSPORT_IMAGE_ATTACH_REQUEST);
                            new GetBase64Image(obtainPathResult, PASSPORT_IMAGE_ATTACH_REQUEST).execute();
                        }
                        if (resultCode == RESULT_OK && requestCode == PASSPORT_IMAGE_CLICK_REQUEST ) {
                           /* try {
                                obtainPathResult = new ArrayList<>();
                                obtainPathResult.add(mCurrentPhotoPath);
                                positionForUpload = 0;
//                              callPostDocAPI(obtainPathResult, PASSPORT_IMAGE_CLICK_REQUEST);
                                new GetBase64Image(obtainPathResult, PASSPORT_IMAGE_CLICK_REQUEST).execute();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }*/

                            File f = grabImageFile(true, 80); //true for compression , 80% quality
                            if (f != null) {
                                // Toast.makeText(getApplicationContext(), "File to upload is " + f.getAbsolutePath(), Toast.LENGTH_LONG).show();
                                //call image uplaod code here
                                //doFileUpload(f, Constants.IMAGE,f.getName());
                                obtainPathResult = new ArrayList<>();
                                obtainPathResult.add(f.getAbsolutePath());
                                new GetBase64Image(obtainPathResult, PASSPORT_IMAGE_CLICK_REQUEST).execute();
                            } else {
                                Toast.makeText(getApplicationContext(), "Image Capture Error", Toast.LENGTH_LONG).show();
                            }

                        }
                        if (resultCode == RESULT_OK && requestCode == SIGNED_FORM_IMAGE_ATTACH_REQUEST && data!=null) {
                            Uri selectedImageUri = data.getData();
                            selectedImagePath = getPath(selectedImageUri);
                            Log.i("GalleryPath", selectedImagePath);
                            obtainPathResult = new ArrayList<>();
                            obtainPathResult.add(selectedImagePath);
                            positionForUpload = 0;
//                            callPostDocAPI(obtainPathResult, SIGNED_FORM_IMAGE_ATTACH_REQUEST);;
                            new GetBase64Image(obtainPathResult, SIGNED_FORM_IMAGE_ATTACH_REQUEST).execute();
                        }
                        if (resultCode == RESULT_OK && requestCode == SIGNED_FORM_IMAGE_CLICK_REQUEST  ) {
                           /* try {
                                obtainPathResult = new ArrayList<>();
                                obtainPathResult.add(mCurrentPhotoPath);
                                positionForUpload = 0;
//                              callPostDocAPI(obtainPathResult, SIGNED_FORM_IMAGE_CLICK_REQUEST);
                                new GetBase64Image(obtainPathResult, SIGNED_FORM_IMAGE_CLICK_REQUEST).execute();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }*/
                            File f = grabImageFile(true, 80); //true for compression , 80% quality
                            if (f != null) {
                                //   Toast.makeText(getApplicationContext(), "File to upload is " + f.getAbsolutePath(), Toast.LENGTH_LONG).show();
                                //call image uplaod code here
                                //doFileUpload(f, Constants.IMAGE,f.getName());
                                obtainPathResult = new ArrayList<>();
                                obtainPathResult.add(f.getAbsolutePath());
                                new GetBase64Image(obtainPathResult, SIGNED_FORM_IMAGE_CLICK_REQUEST).execute();
                            } else {
                                Toast.makeText(getApplicationContext(), "Image Capture Error", Toast.LENGTH_LONG).show();
                            }
                            //myImage.setImageBitmap(image);
                        }
                        if (resultCode == RESULT_OK && requestCode == OFFICE_PHOTO_IMAGE_ATTACH_REQUEST && data!=null) {
                            Uri selectedImageUri = data.getData();
                            selectedImagePath = getPath(selectedImageUri);
                            Log.i("GalleryPath", selectedImagePath);
                            obtainPathResult = new ArrayList<>();
                            obtainPathResult.add(selectedImagePath);
                            positionForUpload = 0;
//                            callPostDocAPI(obtainPathResult, OFFICE_PHOTO_IMAGE_ATTACH_REQUEST);
                            new GetBase64Image(obtainPathResult, OFFICE_PHOTO_IMAGE_ATTACH_REQUEST).execute();
                            //myImage.setImageURI(selectedImageUri);
                        }
                        if (resultCode == RESULT_OK && requestCode == OFFICE_PHOTO_IMAGE_CLICK_REQUEST ) {
                           /* try {
                                obtainPathResult = new ArrayList<>();
                                obtainPathResult.add(mCurrentPhotoPath);
                                positionForUpload = 0;
//                             callPostDocAPI(obtainPathResult, OFFICE_PHOTO_IMAGE_CLICK_REQUEST);
                                new GetBase64Image(obtainPathResult, OFFICE_PHOTO_IMAGE_CLICK_REQUEST).execute();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }*/
                            File f = grabImageFile(true, 80); //true for compression , 80% quality
                            if (f != null) {
                                //Toast.makeText(getApplicationContext(), "File to upload is " + f.getAbsolutePath(), Toast.LENGTH_LONG).show();
                                //call image uplaod code here
                                //doFileUpload(f, Constants.IMAGE,f.getName());
                                obtainPathResult = new ArrayList<>();
                                obtainPathResult.add(f.getAbsolutePath());
                                new GetBase64Image(obtainPathResult, OFFICE_PHOTO_IMAGE_CLICK_REQUEST).execute();
                            } else {
                                Toast.makeText(getApplicationContext(), "Image Capture Error", Toast.LENGTH_LONG).show();
                            }

                        }
                    }
                });
            }

        }, 500);
    }
    /******************************************** End Interface methods **************************
     */


    /********************************************* Start Validations *********************************
     */

    public String checkDigit(int number) {
        return number <= 9 ? "0" + number : String.valueOf(number);
    }


    public final static boolean isValidEmail(CharSequence target) {
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

        sUploadOption = spUploadOptionPosition;


        if (selectedUploadType.equalsIgnoreCase("aadhaar") || sUploadOption == 0) {
            if (sAddhar.length() <= 0) {
                status = false;
                showWarningSimpleAlertDialog(Constant.TITLE, "Please enter  valid Aadhaar No");
            }
        } else if (selectedUploadType.equalsIgnoreCase("dl") || sUploadOption == 1) {
            if (sAddhar.length() <= 0) {
                status = false;
                showWarningSimpleAlertDialog(Constant.TITLE, "Please enter valid Driving Licence No");
            }
        } else if (selectedUploadType.equalsIgnoreCase("voterId") || sUploadOption == 2) {
            if (sAddhar.length() <= 0) {
                status = false;
                showWarningSimpleAlertDialog(Constant.TITLE, "Please enter  valid Voter Id No");
            }
        } else if (selectedUploadType.equalsIgnoreCase("passport") || sUploadOption == 3) {
            if (sAddhar.length() <= 0) {
                status = false;
                showWarningSimpleAlertDialog(Constant.TITLE, "Please enter valid Passport No");
            }
        }
        if (Adhar_Url == null || Adhar_Url.length() <= 0) {
            status = false;
            if (selectedUploadType.equalsIgnoreCase("aadhaar")) {
                showWarningSimpleAlertDialog(Constant.TITLE, "Please Upload  Aadhaar proof");
            } else if (selectedUploadType.equalsIgnoreCase("dl")) {
                showWarningSimpleAlertDialog(Constant.TITLE, "Please Upload Driving Licence proof");
            } else if (selectedUploadType.equalsIgnoreCase("voterId")) {
                showWarningSimpleAlertDialog(Constant.TITLE, "Please Upload Voter Id proof");
            } else if (selectedUploadType.equalsIgnoreCase("passport")) {
                showWarningSimpleAlertDialog(Constant.TITLE, "Please Upload Passport as a proof of address");
            } else if (selectedUploadType.equalsIgnoreCase("waterBill")) {
                showWarningSimpleAlertDialog(Constant.TITLE, "Please Upload Water Bill as a proof of address");
            } else if (selectedUploadType.equalsIgnoreCase("mobileBill")) {
                showWarningSimpleAlertDialog(Constant.TITLE, "Please Upload Landline or Postpaid mobile bill as a proof of address");
            } else if (selectedUploadType.equalsIgnoreCase("electricityBill")) {
                showWarningSimpleAlertDialog(Constant.TITLE, "Please Upload Electricity bill as a proof of address");
            } else if (selectedUploadType.equalsIgnoreCase("bankStatement")) {
                showWarningSimpleAlertDialog(Constant.TITLE, "Please Upload Bank Passbook/ Statement as a proof of address");
            }
        } else if (selectedUploadType.equalsIgnoreCase("aadhaar")) {
            String[] strings = Adhar_Url.split(",");
            if (strings.length < 2 && (selectedUploadType.equalsIgnoreCase("aadhaar") || spUploadOptionPosition == 0)) {
                status = false;
                showWarningSimpleAlertDialog(Constant.TITLE, "Please add both sided Aadhaar proof");
            }
        }
        if (!(mySingleton.getData(Constant.USER_GROUP_ID).equalsIgnoreCase(Constant.POS_GROUP_ID))) {
            if (sPan.length() != 10 || !RegexUtils.isValidIPan(sPan)) {
                status = false;
                showWarningSimpleAlertDialog(Constant.TITLE, "Please enter valid PAN No");
            } else if (sPan.length() != 10 || !RegexUtils.isValidIPan(sPan)) {
                status = false;
                showWarningSimpleAlertDialog(Constant.TITLE, "Please enter valid PAN No");
            } else if (Pan_Url.length() <= 0) {
                status = false;
                showWarningSimpleAlertDialog(Constant.TITLE, "Please Upload PAN proof");
            } else if (Firm_Pan_Url == null || Firm_Pan_Url.length() <= 0 && bussinessTypeStatus) {
                status = false;
                showWarningSimpleAlertDialog(Constant.TITLE, "Please Upload Firm PAN proof");
            } else if (sFirmPan.length() != 10 && bussinessTypeStatus) {
                status = false;
                showWarningSimpleAlertDialog(Constant.TITLE, "Please enter valid Firm PAN No");
            } else if (Cheque_Url.length() <= 0) {
                status = false;
                showWarningSimpleAlertDialog(Constant.TITLE, "Please Upload Cheque proof");
            } else if (Passport_Url.length() <= 0) {
                status = false;
                showWarningSimpleAlertDialog(Constant.TITLE, "Please Upload passport size photo");
            }
            /*else if (SignedForm_Url.length() <= 0 && (mySingleton.getData(Constant.USER_GROUP_ID).equalsIgnoreCase(Constant.POS_GROUP_ID))) {
                     status = false;
                     showWarningSimpleAlertDialog(Constant.TITLE, "Please Upload signed form");
                 }  */
        } else if (mySingleton.getData(Constant.USER_GROUP_ID).equalsIgnoreCase(Constant.POS_GROUP_ID)) {
            if (sPan.length() > 0) {
                if (sPan.length() != 10 || !RegexUtils.isValidIPan(sPan)) {
                    status = false;
                    showWarningSimpleAlertDialog(Constant.TITLE, "Please enter valid PAN No");
                }
            } else if (Shop_Url.length() <= 0) {
                String message = "";
                status = false;
                message = "Please Upload Shop Photo";
                showWarningSimpleAlertDialog(Constant.TITLE, message);
            }

        }

        return status;
    }
    /********************************************* End Validations *********************************
     */
    /******************************************* Start Services ************************************
     */
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
                                    showDataOnView();
                                } else {
                                    displaySnackBar.DisplaySnackBar("Data not found", Constant.TYPE_ERROR);

                                }
                            } else if (response.body().getStatus() == 0) {
                                //showWarningSimpleAlertDialog(response.body().getData().getMessage(), response.body().getData().getValidation_error().getPassword().get(0));
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

                        // Toast.makeText(context, errorBody.getMessage(), Toast.LENGTH_SHORT).show();
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
                                //showWarningSimpleAlertDialog(response.body().getData().getMessage(), response.body().getData().getValidation_error().getPassword().get(0));
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

                        // Toast.makeText(context, errorBody.getMessage(), Toast.LENGTH_SHORT).show();
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


        sAddhar = etAddhar.getText().toString().trim();
        sPan = etPan.getText().toString().trim();
        sFirmPan = etFirmPan.getText().toString().trim();
        sGST = etGST.getText().toString().trim();
        saddress = etaddress.getText().toString().trim();
        scheque = etcheque.getText().toString().trim();

        sUploadOption = spUploadOptionPosition;

        if (editProfileRequest == null) {
            editProfileRequest = new EditProfileRequest();
        }
        if (editProfileRequest != null) {

            editProfileRequest.setAadhar_no(sAddhar);



            //  editProfileRequest.setCp_adhar_proof(Adhar_UrlNew+","+Adhar_Url);

            if (!TextUtil.isStringNullOrBlank(Adhar_Url) && !TextUtil.isStringNullOrBlank(Adhar_UrlNew))
            {
                editProfileRequest.setCp_adhar_proof(Adhar_UrlNew+","+Adhar_Url);

            }
            else  if (TextUtil.isStringNullOrBlank(Adhar_Url))
            {
                if (!TextUtil.isStringNullOrBlank(Adhar_UrlNew))
                {
                    editProfileRequest.setCp_adhar_proof(Adhar_UrlNew);
                }
                else
                {
                    editProfileRequest.setCp_adhar_proof("");
                }
            }
            else
            {
                editProfileRequest.setCp_adhar_proof(Adhar_Url);
            }


            editProfileRequest.setUpload_type_option(sUploadOption);

            editProfileRequest.setPan_no(sPan.toUpperCase());
            editProfileRequest.setCompany_pan_no(sFirmPan);


            //      editProfileRequest.setCp_pan_proof(Pan_UrlNew+","+Pan_Url);

            if (!TextUtil.isStringNullOrBlank(Pan_Url) && !TextUtil.isStringNullOrBlank(Pan_UrlNew))
            {
                editProfileRequest.setCp_pan_proof(Pan_UrlNew+","+Pan_Url);

            }
            else  if (TextUtil.isStringNullOrBlank(Pan_Url))
            {
                if (!TextUtil.isStringNullOrBlank(Pan_UrlNew))
                {
                    editProfileRequest.setCp_pan_proof(Pan_UrlNew);
                }
                else
                {
                    editProfileRequest.setCp_pan_proof("");
                }
            }
            else
            {
                editProfileRequest.setCp_pan_proof(Pan_Url);
            }



            //       editProfileRequest.setFirm_pan_proof(Firm_Pan_UrlNew+","+Firm_Pan_Url);


            if (!TextUtil.isStringNullOrBlank(Firm_Pan_Url)&&!TextUtil.isStringNullOrBlank(Firm_Pan_UrlNew))
            {
                editProfileRequest.setFirm_pan_proof(Firm_Pan_UrlNew+","+Firm_Pan_Url);
            }
            else if(TextUtil.isStringNullOrBlank(Firm_Pan_Url))
            {
                if (!TextUtil.isStringNullOrBlank(Firm_Pan_UrlNew))
                {
                    editProfileRequest.setFirm_pan_proof(Firm_Pan_UrlNew);
                }
                else
                {
                    editProfileRequest.setFirm_pan_proof(Firm_Pan_Url);
                }
            }
            else
            {
                editProfileRequest.setFirm_pan_proof("");
            }

            editProfileRequest.setGst_no(sGST);


            //          editProfileRequest.setGst_proof(Gst_UrlNew+","+Gst_Url);


            if (!TextUtil.isStringNullOrBlank(Gst_Url)&&!TextUtil.isStringNullOrBlank(Gst_UrlNew))
            {
                editProfileRequest.setGst_proof(Gst_UrlNew+","+Gst_Url);
            }
            else if(TextUtil.isStringNullOrBlank(Gst_Url))
            {
                if (!TextUtil.isStringNullOrBlank(Gst_UrlNew))
                {
                    editProfileRequest.setGst_proof(Gst_UrlNew);
                }
                else
                {
                    editProfileRequest.setGst_proof("");
                }
            }
            else
            {
                editProfileRequest.setGst_proof(Gst_Url);
            }


            //     editProfileRequest.setAddress_proof(Address_UrlNew+","+Address_Url);


            if (!TextUtil.isStringNullOrBlank(Address_Url)&&!TextUtil.isStringNullOrBlank(Address_UrlNew))
            {
                editProfileRequest.setAddress_proof(Address_UrlNew+","+Address_Url);
            }
            else if(TextUtil.isStringNullOrBlank(Address_Url))
            {
                if (!TextUtil.isStringNullOrBlank(Address_UrlNew))
                {
                    editProfileRequest.setAddress_proof(Address_UrlNew);
                }
                else
                {
                    editProfileRequest.setAddress_proof("");
                }
            }else
            {
                editProfileRequest.setAddress_proof(Address_Url);
            }


            editProfileRequest.setAddress(saddress);



            //   editProfileRequest.setCheque_proof(Cheque_UrlNew+","+Cheque_Url);



            if (!TextUtil.isStringNullOrBlank(Cheque_Url)&&!TextUtil.isStringNullOrBlank(Cheque_UrlNew))
            {
                editProfileRequest.setCheque_proof(Cheque_UrlNew+","+Cheque_Url);
            }
            else if(TextUtil.isStringNullOrBlank(Cheque_Url))
            {
                if (!TextUtil.isStringNullOrBlank(Cheque_UrlNew))
                {
                    editProfileRequest.setCheque_proof(Cheque_UrlNew);
                }
                else
                {
                    editProfileRequest.setCheque_proof("");
                }
            }
            else
            {
                editProfileRequest.setCheque_proof(Cheque_Url);
            }




            //    editProfileRequest.setPassport_size_photo(Passport_UrlNew+","+Passport_Url);


            if (!TextUtil.isStringNullOrBlank(Passport_Url)&&!TextUtil.isStringNullOrBlank(Passport_UrlNew))
            {
                editProfileRequest.setPassport_size_photo(Passport_UrlNew+","+Passport_Url);
            }
            else if(TextUtil.isStringNullOrBlank(Passport_Url))
            {
                if (!TextUtil.isStringNullOrBlank(Passport_UrlNew))
                {
                    editProfileRequest.setPassport_size_photo(Passport_UrlNew);
                }
                else
                {
                    editProfileRequest.setPassport_size_photo("");
                }
            }
            else
            {
                editProfileRequest.setPassport_size_photo(Passport_Url);
            }



            //         editProfileRequest.setSigned_form(SignedForm_UrlNew+","+SignedForm_Url);


            if (!TextUtil.isStringNullOrBlank(SignedForm_Url)&&!TextUtil.isStringNullOrBlank(SignedForm_UrlNew))
            {
                editProfileRequest.setSigned_form(SignedForm_UrlNew+","+SignedForm_Url);
            }
            else if(TextUtil.isStringNullOrBlank(SignedForm_Url))
            {
                if (!TextUtil.isStringNullOrBlank(SignedForm_UrlNew))
                {
                    editProfileRequest.setSigned_form(SignedForm_UrlNew);
                }
                else
                {
                    editProfileRequest.setSigned_form("");
                }
            }
            else
            {
                editProfileRequest.setSigned_form(SignedForm_Url);
            }



            //          editProfileRequest.setShop_photo(Shop_UrlNew+","+Shop_Url);


            if (!TextUtil.isStringNullOrBlank(Shop_Url)&&!TextUtil.isStringNullOrBlank(Shop_UrlNew))
            {
                editProfileRequest.setShop_photo(Shop_UrlNew+","+Shop_Url);
            }
            else if(TextUtil.isStringNullOrBlank(Shop_Url))
            {
                if (!TextUtil.isStringNullOrBlank(Shop_UrlNew))
                {
                    editProfileRequest.setShop_photo(Shop_UrlNew);
                }
                else
                {
                    editProfileRequest.setShop_photo("");
                }
            }
            else
            {
                editProfileRequest.setShop_photo(Shop_Url);
            }



            editProfileRequest.setShop_latitude(shopLatitude);
            editProfileRequest.setIb_da_code(etIbdaCode.getText().toString().trim());



//            editProfileRequest.setIb_da_proof(IbdaUrlNew+","+IbdaUrl);

            if (!TextUtil.isStringNullOrBlank(IbdaUrl)&&!TextUtil.isStringNullOrBlank(IbdaUrlNew))
            {
                editProfileRequest.setIb_da_proof(IbdaUrlNew+","+IbdaUrl);
            }
            else if(TextUtil.isStringNullOrBlank(IbdaUrl))
            {
                if (!TextUtil.isStringNullOrBlank(IbdaUrlNew))
                {
                    editProfileRequest.setIb_da_proof(IbdaUrlNew);
                }
                else
                {
                    editProfileRequest.setIb_da_proof("");
                }
            }
            else
            {
                editProfileRequest.setIb_da_proof(IbdaUrl);
            }





            editProfileRequest.setShop_longitude(shopLongitude);
        } else {

            displaySnackBar.DisplaySnackBar("Data not found", Constant.TYPE_ERROR);
        }
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
                                    //  displaySnackBar.DisplaySnackBar(response.body().getData().getMessage(), Constant.TYPE_ERROR);
                                    // callNextStep(editProfileRequest);
                                    displaySnackBar.DisplaySnackBar(response.body().getData().getMessage().toString(), Constant.TYPE_SUCCESS);
                                    finish();
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
                        Log.i("ERRR", errorBody.getMessage() + "");
                        callDisplayErrorCode(Integer.parseInt(errorBody.getStatus()), errorBody.getMessage());
                        displaySnackBar.DisplaySnackBar(errorBody.getMessage(), Constant.TYPE_ERROR);

                        // Toast.makeText(context, errorBody.getMessage(), Toast.LENGTH_SHORT).show();
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

    public void deleteUpload(String type, String position) {
        if (type.equalsIgnoreCase(Constant.K_AADHAAR)) {
            //aadhaarImages.remove(position);
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
            //callPostDocAPI(aadhaarImages, AADHAR_IMAGE_ATTACH_REQUEST);
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
            Log.i("Pann", New);
            Log.i("Pannn", Pan_Url);

            // callPostDocAPI(panImages, PAN_IMAGE_ATTACH_REQUEST);
        } else if (type.equalsIgnoreCase(Constant.K_IBDA)) {
            Log.i("newww1", position);

            String[] temp = IbdaUrl.split(",");
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
            IbdaUrl = New;
            Log.i("Pann", New);
            Log.i("Pannn", IbdaUrl);

            // callPostDocAPI(panImages, PAN_IMAGE_ATTACH_REQUEST);
        } else if (type.equalsIgnoreCase(Constant.K_FIRM_PAN)) {
            //firmPanImages.remove(position);
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
            //callPostDocAPI(firmPanImages, FIRM_PAN_IMAGE_ATTACH_REQUEST);
        } else if (type.equalsIgnoreCase(Constant.K_ADDRESS)) {
            // addressProofImages.remove(position);
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
            // callPostDocAPI(addressProofImages, ADDRESS_IMAGE_ATTACH_REQUEST);
        } else if (type.equalsIgnoreCase(Constant.K_GST)) {
            //gstImages.remove(position);
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
            // callPostDocAPI(gstImages, GST_IMAGE_ATTACH_REQUEST);
        } else if (type.equalsIgnoreCase(Constant.K_CHEQUE)) {
            //cancelChequeImages.remove(position);
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
            //callPostDocAPI(cancelChequeImages, CHEQUE_IMAGE_ATTACH_REQUEST);
        } else if (type.equalsIgnoreCase(Constant.K_PASSPORT)) {
            // passportImages.remove(position);
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
            //callPostDocAPI(passportImages, PASSPORT_IMAGE_ATTACH_REQUEST);
        } else if (type.equalsIgnoreCase(Constant.K_SIGNED)) {
            //signedPhotoImages.remove(position);
            String[] temp = SignedForm_Url.split(",");
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
            SignedForm_Url = New;
            //callPostDocAPI(signedPhotoImages, SIGNED_FORM_IMAGE_ATTACH_REQUEST);
        } else if (type.equalsIgnoreCase(Constant.K_SHOP)) {
            //shopImages.remove(position);
            String[] temp = Shop_Url.split(",");
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
            Shop_Url = New;
            // callPostDocAPI(shopImages, OFFICE_PHOTO_IMAGE_ATTACH_REQUEST);
        }

    }


    public void callPostDocAPI(final List<String> file, final int CallType, String extention, String base64) {
        UploadRequest uploadRequest = new UploadRequest();


        uploadRequest = new UploadRequest();
        if (!TextUtil.isStringNullOrBlank(base64)) {
            uploadRequest.setId_proof(base64);
            uploadRequest.setId_proof_file_type(extention.replace(".", ""));
        }

        Gson gson = new Gson();
        String json = gson.toJson(uploadRequest, UploadRequest.class);
        Log.i(TAG, "json " + json);


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
//                                        Gson gson = new Gson();
//                                        String json = gson.toJson(response.body(), FileUploadResponce.class);
//                                        Log.i(TAG, "json " + json);

                                    if (CallType == FILE_SELECT_ADHAR || CallType == AADHAR_IMAGE_ATTACH_REQUEST || CallType == AADHAR_IMAGE_CLICK_REQUEST) {
                                        if (TextUtil.isStringNullOrBlank(Adhar_Url)) {
                                            Adhar_Url = Adhar_Url + response.body().getFile_name();
                                        } else {
                                            Adhar_Url = Adhar_Url + "," + response.body().getFile_name();
                                        }
                                        if (!TextUtil.isStringNullOrBlank(Adhar_Url)) {
                                            String[] Adhar_images = Adhar_Url.split(",");
                                            // List<String> images = Arrays.asList(Adhar_images);
//                                                    UploadRemoveAdapter uploadRemoveAdapter = ;
                                            if (upLoadAadharAdapter != null) {
                                                upLoadAadharAdapter.updateData(Adhar_images);
                                            } else {
                                                upLoadAadharAdapter = new UploadRemoveAdapter(Adhar_images, R.layout.doc_upload_remove, context, Constant.K_AADHAAR);
                                                aadhar_recycler_view.setAdapter(upLoadAadharAdapter);
                                                aadhar_recycler_view.setScrollContainer(false);
                                            }
                                        }

                                        // textAdharFile.setText(Adhar_Url);
                                        Imcheck01.setVisibility(View.VISIBLE);
                                        //  btnUpload.setBackground(getResources().getDrawable(R.drawable.button_upload_file));
                                        //btnUpload.setText(" Done");
                                        String[] splitAdhar = Adhar_Url.split(",");
                                        if (splitAdhar.length <= 1 && (spUploadOptionPosition == 0 || selectedUploadType.equalsIgnoreCase("aadhaar"))) {
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    showWarningAddMoreAdhar("Message!", "Add one more photo for Aadhaar", CallType);
                                                }
                                            });
                                        }
                                    } else if (CallType == FILE_SELECT_CPPAN || CallType == PAN_IMAGE_ATTACH_REQUEST || CallType == PAN_IMAGE_CLICK_REQUEST) {
                                        //   Pan_Url = response.body().getFile_name();
                                        if (TextUtil.isStringNullOrBlank(Pan_Url)) {
                                            Pan_Url = Pan_Url + response.body().getFile_name();
                                        } else {
                                            Pan_Url = Pan_Url + "," + response.body().getFile_name();
                                        }

                                        if (!TextUtil.isStringNullOrBlank(Pan_Url)) {
                                            String[] Pan_images = Pan_Url.split(",");
                                            // List<String> images = Arrays.asList(Pan_images);
                                            if (uploadPanAdapter != null) {
                                                uploadPanAdapter.updateData(Pan_images);
                                            } else {
                                                uploadPanAdapter = new UploadRemoveAdapter(Pan_images, R.layout.doc_upload_remove, context, Constant.K_PAN);
                                                pan_recycler_view.setAdapter(uploadPanAdapter);
                                                pan_recycler_view.setScrollContainer(false);
                                            }
                                        }

                                        // textCPPANFile.setText(Pan_Url);
                                        Imcheck02.setVisibility(View.VISIBLE);
                                        // btnUploadPAN.setText("Done");
                                        //  btnUploadPAN.setBackground(getResources().getDrawable(R.drawable.button_upload_file));
                                        //   btnUploadPAN.setBackgroundColor(Color.GREEN);
                                    } else if (CallType == FILE_SELECT_FIRMPAN || CallType == FIRM_PAN_IMAGE_ATTACH_REQUEST || CallType == FIRM_PAN_IMAGE_CLICK_REQUEST) {
                                        //    Firm_Pan_Url = response.body().getFile_name();
                                        if (TextUtil.isStringNullOrBlank(Firm_Pan_Url)) {
                                            Firm_Pan_Url = Firm_Pan_Url + response.body().getFile_name();
                                        } else {
                                            Firm_Pan_Url = Firm_Pan_Url + "," + response.body().getFile_name();
                                        }

                                        if (!TextUtil.isStringNullOrBlank(Firm_Pan_Url)) {
                                            String[] FirmPan_images = Firm_Pan_Url.split(",");
                                            //List<String> images = Arrays.asList(FirmPan_images);
                                            if (uploadFirmPanAdapter != null) {
                                                uploadFirmPanAdapter.updateData(FirmPan_images);
                                            } else {
                                                uploadFirmPanAdapter = new UploadRemoveAdapter(FirmPan_images, R.layout.doc_upload_remove, context, Constant.K_FIRM_PAN);
                                                frmPan_recycler_view.setAdapter(uploadFirmPanAdapter);
                                                frmPan_recycler_view.setScrollContainer(false);
                                            }
                                        }

                                        //textFirmFile.setText(Firm_Pan_Url);
                                        Imcheck05.setVisibility(View.VISIBLE);
                                        // btnUploadFirmPan.setText("Done");
                                        //  btnUploadFirmPan.setBackground(getResources().getDrawable(R.drawable.button_upload_file));
                                        //btnUploadFirmPan.setBackgroundColor(Color.GREEN);
                                    } else if (CallType == FILE_SELECT_GST || CallType == GST_IMAGE_ATTACH_REQUEST || CallType == GST_IMAGE_CLICK_REQUEST) {
                                        //  Gst_Url = response.body().getFile_name();
                                        if (TextUtil.isStringNullOrBlank(Gst_Url)) {
                                            Gst_Url = Gst_Url + response.body().getFile_name();
                                        } else {
                                            Gst_Url = Gst_Url + "," + response.body().getFile_name();
                                        }

                                        if (!TextUtil.isStringNullOrBlank(Gst_Url)) {
                                            String[] gst_images = Gst_Url.split(",");
                                            if (uploadGSTPhotoAdapter != null) {
                                                uploadGSTPhotoAdapter.updateData(gst_images);
                                            } else {
                                                uploadGSTPhotoAdapter = new UploadRemoveAdapter(gst_images, R.layout.doc_upload_remove, context, Constant.K_GST);
                                                gst_photo_recycler_view.setAdapter(uploadGSTPhotoAdapter);
                                                gst_photo_recycler_view.setScrollContainer(false);
                                            }
                                        }

                                        //textGSTFile.setText(Gst_Url);
                                        Imcheck03.setVisibility(View.VISIBLE);
                                        //   btnUploadGST.setText("Done");
                                        //  btnUploadGST.setBackground(getResources().getDrawable(R.drawable.button_upload_file));
                                        // btnUploadGST.setBackgroundColor(Color.GREEN);
                                    } else if (CallType == FILE_SELECT_ADDRESS || CallType == ADDRESS_IMAGE_ATTACH_REQUEST || CallType == ADDRESS_IMAGE_CLICK_REQUEST) {
                                        //  Address_Url = response.body().getFile_name();
                                        if (TextUtil.isStringNullOrBlank(Address_Url)) {
                                            Address_Url = Address_Url + response.body().getFile_name();
                                        } else {
                                            Address_Url = Address_Url + "," + response.body().getFile_name();
                                        }

                                        if (!TextUtil.isStringNullOrBlank(Address_Url)) {
                                            String[] Address_images = Address_Url.split(",");
                                            if (uploadBsnAdhar != null) {
                                                uploadBsnAdhar.updateData(Address_images);
                                            } else {
                                                uploadBsnAdhar = new UploadRemoveAdapter(Address_images, R.layout.doc_upload_remove, context, Constant.K_ADDRESS);
                                                bsnAddr_photo_recycler_view.setAdapter(uploadBsnAdhar);
                                                bsnAddr_photo_recycler_view.setScrollContainer(false);
                                            }
                                        }
                                        Imcheck04.setVisibility(View.VISIBLE);
                                        //btnUploadAddress.setText("Done");
                                        //btnUploadAddress.setBackground(getResources().getDrawable(R.drawable.button_upload_file));
                                        // btnUploadAddress.setBackgroundColor(Color.GREEN);
                                    } else if (CallType == FILE_SELECT_IBDA || CallType == IBDA_IMAGE_ATTACH_REQUEST || CallType == IBDA_IMAGE_CLICK_REQUEST) {
                                        //  Address_Url = response.body().getFile_name();
                                        if (TextUtil.isStringNullOrBlank(IbdaUrl)) {
                                            IbdaUrl = IbdaUrl + response.body().getFile_name();
                                        } else {
                                            IbdaUrl = IbdaUrl + "," + response.body().getFile_name();
                                        }

                                        if (!TextUtil.isStringNullOrBlank(IbdaUrl)) {
                                            String[] Address_images = IbdaUrl.split(",");
                                            if (uploadIBDAAdapter != null) {
                                                uploadIBDAAdapter.updateData(Address_images);

                                            } else {
                                                uploadIBDAAdapter = new UploadRemoveAdapter(Address_images, R.layout.doc_upload_remove, context, Constant.K_IBDA);
                                                IbdaCode_recycler_view.setAdapter(uploadIBDAAdapter);
                                                IbdaCode_recycler_view.setScrollContainer(false);
                                            }
                                        }

                                        ImcheckIbdaCode.setVisibility(View.VISIBLE);
                                        // btnUploadIbdaCode.setText("Done");
                                        //  btnUploadIbdaCode.setBackground(getResources().getDrawable(R.drawable.button_upload_file));
                                        // btnUploadAddress.setBackgroundColor(Color.GREEN);
                                    } else if (CallType == FILE_SELECT_CHEQUE || CallType == CHEQUE_IMAGE_ATTACH_REQUEST || CallType == CHEQUE_IMAGE_CLICK_REQUEST) {
                                        // Cheque_Url = response.body().getFile_name();
                                        if (TextUtil.isStringNullOrBlank(Cheque_Url)) {
                                            Cheque_Url = Cheque_Url + response.body().getFile_name();
                                        } else {
                                            Cheque_Url = Cheque_Url + "," + response.body().getFile_name();
                                        }

                                        if (!TextUtil.isStringNullOrBlank(Cheque_Url)) {
                                            String[] Cheque_images = Cheque_Url.split(",");
                                            if (uploadCnclChequeAdapter != null) {
                                                uploadCnclChequeAdapter.updateData(Cheque_images);

                                            } else {
                                                uploadCnclChequeAdapter = new UploadRemoveAdapter(Cheque_images, R.layout.doc_upload_remove, context, Constant.K_CHEQUE);
                                                cnclChq_recycler_view.setAdapter(uploadCnclChequeAdapter);
                                                cnclChq_recycler_view.setScrollContainer(false);
                                            }
//
                                        }

                                        //textCHequeFile.setText(Cheque_Url);
                                        Imcheck06.setVisibility(View.VISIBLE);
                                        // btnUploadCheque.setText("Done");
                                        // btnUploadCheque.setBackground(getResources().getDrawable(R.drawable.button_upload_file));
                                        // btnUploadCheque.setBackgroundColor(Color.GREEN);
                                    } else if (CallType == FILE_SELECT_PASSPORT || CallType == PASSPORT_IMAGE_ATTACH_REQUEST || CallType == PASSPORT_IMAGE_CLICK_REQUEST) {
                                        // Passport_Url = response.body().getFile_name();
                                        if (TextUtil.isStringNullOrBlank(Passport_Url)) {
                                            Passport_Url = Passport_Url + response.body().getFile_name();
                                        } else {
                                            Passport_Url = Passport_Url + "," + response.body().getFile_name();
                                        }

                                        if (!TextUtil.isStringNullOrBlank(Passport_Url)) {
                                            String[] Passport_images = Passport_Url.split(",");
                                            //List<String> images = Arrays.asList(Passport_images);
//                                            UploadRemoveAdapter panimageUploadAdapter;
                                            if (uploadPassportAdapter != null) {
                                                uploadPassportAdapter.updateData(Passport_images);

                                            } else {
                                                uploadPassportAdapter = new UploadRemoveAdapter(Passport_images, R.layout.doc_upload_remove, context, Constant.K_PASSPORT);
                                                pssprt_photo_recycler_view.setAdapter(uploadPassportAdapter);
                                                pssprt_photo_recycler_view.setScrollContainer(false);
                                            }
//                                                uploadPassportAdapter.notifyDataSetChanged();
                                        }

                                        //textPassportFile.setText(Passport_Url);
                                        Imcheck07.setVisibility(View.VISIBLE);
                                        // btnUploadPassport.setText("Done");
                                        // btnUploadPassport.setBackground(getResources().getDrawable(R.drawable.button_upload_file));
                                        //   btnUploadPassport.setBackgroundColor(Color.GREEN);
                                    } else if (CallType == FILE_SELECT_SIGNED_FORM || CallType == SIGNED_FORM_IMAGE_ATTACH_REQUEST || CallType == SIGNED_FORM_IMAGE_CLICK_REQUEST) {
                                        //   SignedForm_Url = response.body().getFile_name();
                                        if (TextUtil.isStringNullOrBlank(SignedForm_Url)) {
                                            SignedForm_Url = SignedForm_Url + response.body().getFile_name();
                                        } else {
                                            SignedForm_Url = SignedForm_Url + "," + response.body().getFile_name();
                                        }

                                        if (!TextUtil.isStringNullOrBlank(SignedForm_Url)) {
                                            String[] Signed_images = SignedForm_Url.split(",");
                                            //List<String> images = Arrays.asList(Signed_images);
//                                               UploadRemoveAdapter panimageUploadAdapter;
                                            if (uploadSignedAdapter != null) {
                                                uploadSignedAdapter.updateData(Signed_images);

                                            } else {
                                                uploadSignedAdapter = new UploadRemoveAdapter(Signed_images, R.layout.doc_upload_remove, context, Constant.K_SIGNED);
                                                upldsgn_photo_recycler_view.setAdapter(uploadSignedAdapter);
                                                upldsgn_photo_recycler_view.setScrollContainer(false);
                                            }

                                            //uploadSignedAdapter.notifyDataSetChanged();
                                        }

                                        //textSignedFormFile.setText(SignedForm_Url);
                                        Imcheck08.setVisibility(View.VISIBLE);
                                        //  btnUploadSignedForm.setText("Done");
                                        // btnUploadSignedForm.setBackground(getResources().getDrawable(R.drawable.button_upload_file));
                                        // btnUploadSignedForm.setBackgroundColor(Color.GREEN);
                                    } else if (CallType == FILE_SELECT_SHOP || CallType == OFFICE_PHOTO_IMAGE_ATTACH_REQUEST || CallType == OFFICE_PHOTO_IMAGE_CLICK_REQUEST) {
                                        //  Shop_Url = response.body().getFile_name();
                                        if (TextUtil.isStringNullOrBlank(Shop_Url)) {
                                            Shop_Url = Shop_Url + response.body().getFile_name();
                                        } else {
                                            Shop_Url = Shop_Url + "," + response.body().getFile_name();
                                        }

                                        if (!TextUtil.isStringNullOrBlank(Shop_Url)) {
                                            String[] Shop_images = Shop_Url.split(",");
                                            // List<String> images = Arrays.asList(Shop_images);
//                                            UploadRemoveAdapter panimageUploadAdapter;
                                            if (uploadShopAdapter != null) {
                                                uploadShopAdapter.updateData(Shop_images);

                                            } else {
                                                uploadShopAdapter = new UploadRemoveAdapter(Shop_images, R.layout.doc_upload_remove, context, Constant.K_SHOP);
                                                upldshop_photo_recycler_view.setAdapter(uploadShopAdapter);
                                                upldshop_photo_recycler_view.setScrollContainer(false);
//                                                uploadShopAdapter.notifyDataSetChanged();
                                            }
                                        }

                                        //textshopPhotoFile.setText(Shop_Url);
                                        Imcheck09.setVisibility(View.VISIBLE);
                                        // btnUploadshopPhoto.setText("Done");
                                        // btnUploadshopPhoto.setBackground(getResources().getDrawable(R.drawable.button_upload_file));
                                        // btnUploadshopPhoto.setBackgroundColor(Color.GREEN);
                                    }
                                    if (positionForUpload < file.size() - 1) {
                                        positionForUpload++;
                                        //  callPostDocAPI(file, CallType);
                                        new GetBase64Image(file, CallType).execute();
                                    }
                                } else {
                                    displaySnackBar.DisplaySnackBar("Data not found", Constant.TYPE_ERROR);

                                }
                            } else if (response.body().getStatus() == 0) {
                                //showWarningSimpleAlertDialog(response.body().getData().getMessage(), response.body().getData().getValidation_error().getPassword().get(0));
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

                        // Toast.makeText(context, errorBody.getMessage(), Toast.LENGTH_SHORT).show();
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
                if (kProgressHUD !=null && kProgressHUD.isShowing()) {
                    kProgressHUD.dismiss();
                }
                if (t != null && (t instanceof IOException || t instanceof SocketTimeoutException
                        || t instanceof ConnectException || t instanceof NoRouteToHostException
                        || t instanceof SecurityException)) {
                    displaySnackBar.DisplaySnackBar(Constant.NETWIRK_ERROR, Constant.TYPE_ERROR);

                }
            }
        });

    }

    private class GetBase64Image extends AsyncTask<String, String, String> {
        List<String> file;
        int CallType;
        String extension = "";

        public  GetBase64Image(final List<String> file, final int CallType) {
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

            if (s != null) {
                callPostDocAPI(file, CallType, extension, s);
            }
        }
    }

    private String encodeFileToBase64Binary(String fileName)
            throws IOException {

        InputStream inputStream = null;//You can get an inputStream using any IO API
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

    private void dispatchTakePictureIntent(int fromCamera) {
        if (isStoragePermissionGranted()) {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(takePictureIntent, fromCamera);
        /* if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

       } */
        }
    }

    private void choosePictureFromGallery(int fromAttach) {
        // if (isStoragePermissionGranted()) {
//            Intent i;
//            if (fromAttach == AADHAR_IMAGE_ATTACH_REQUEST) {
//                i = new Intent(Intent.ACTION_GET_CONTENT,
//                        MediaStore.Images.Media.INTERNAL_CONTENT_URI);
//            } else {
//                i = new Intent(Intent.ACTION_PICK,
//                        MediaStore.Images.Media.INTERNAL_CONTENT_URI);
//            }
//            startActivityForResult(i, fromAttach);

        if (isStoragePermissionGranted()) {
            Intent i = new Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            startActivityForResult(i, fromAttach);
        }
        // }
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

    private void getLocationData() {
        if (islocationPermissionGranted()) {
            GPSTracker gps = new GPSTracker(context);
            if (gps.canGetLocation()) {
                shopLatitude = String.valueOf(gps.getLatitude());
                shopLongitude = String.valueOf(gps.getLatitude());
                Log.i("lattt", shopLatitude);
                Log.i("lannn", shopLongitude);
            }

        }
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    String mCurrentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = null;
        storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
//        if(Build.VERSION.SDK_INT > 19) {
//
//        }else{
//            String imageFolderPath = Environment.getExternalStorageDirectory().toString()
//                    + imageFileName;
//            storageDir = new File(imageFolderPath);
//        }
        File image = File.createTempFile(
                imageFileName,  // prefix /
                ".jpg",         // suffix /
                storageDir      // directory /
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }
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

            // Save a file: path for use with ACTION_VIEW intents

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
            // Ensure that there's a camera activity to handle the intent
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

                if (file != null) {
                    Uri photoURI = FileProvider.getUriForFile(this,
                            BuildConfig.APPLICATION_ID.concat(".provider"),
                            file);

                    mImageUri = Uri.fromFile(file);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
                    startActivityForResult(takePictureIntent, type);
                }
            }
        }
    }
    private void dispatchTakePictureIntentReal(int Type) {
        //  new CreateFile(Type).execute();
       /* Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.rebliss.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, Type);
            }
        }*/
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        File photo;
        try {
            // place where to store camera taken picture
            photo = createTemporaryFile("capture", ".jpg");
            photo.delete();
        } catch (Exception e) {
            Log.v("Djsce Image capture", "Can't create file to take picture!");
            Toast.makeText(getApplicationContext(), "Please cssheck SD card! Image shot is impossible!", Toast.LENGTH_LONG).show();
            return;
        }
        mImageUri = Uri.fromFile(photo);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
        startActivityForResult(intent, Type);
    }


    /******************************************* End Services ************************************
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}