package com.rebliss.view.activity;

import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.gson.Gson;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.rebliss.utils.App;
import com.rebliss.R;
import com.rebliss.data.perf.MySingleton;
import com.rebliss.domain.constant.Constant;
import com.rebliss.domain.model.ErrorBody;
import com.rebliss.domain.model.profile.Data;
import com.rebliss.domain.model.profile.ProfileResponce;
import com.rebliss.presenter.helper.DisplaySnackBar;
import com.rebliss.presenter.helper.Logout;
import com.rebliss.presenter.helper.Network;
import com.rebliss.presenter.helper.TextUtil;
import com.rebliss.presenter.retrofit.ApiClient;
import com.rebliss.presenter.retrofit.ApiInterface;
import com.rebliss.view.adapter.ImageUploadAdapter;
import java.io.IOException;
import java.net.ConnectException;
import java.net.NoRouteToHostException;
import java.net.SocketTimeoutException;
import java.text.DateFormatSymbols;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class POSDetails extends AppCompatActivity {
    private Context context;
    private TextView userName, userMobile, userLandline, usrMobileTxt1, usrMobileTxt2, usrOffEmailTxt1, usrOffEmailTxt2,
            usrPrsEmailTxt1, usrPrsEmailTxt2, usrBusAddrTxt1, usrCommAddrTxt1, usrAadharTxt1, usrAadharTxt2, usrPanTxt1,
            usrPanTxt2, usrBankHolderTxt1, usrBankHolderTxt2, usrAccountNumTxt1, usrAccountNumTxt2, usrIfscTxt1, usrIfscTxt2,
            usrBankNamTxt1, usrBankNamTxt2, usrGndrTxt1, usrGndrTxt2, usrDobTxt1, usrDobTxt2, usrMrStatusTxt1, usrMrStatusTxt2,
            usrFrmNameTxt1, usrFrmNameTxt2, usrFrmTypeTxt1, usrFrmTypeTxt2, usrNtofBusnsTxt1, usrNtofBusnsTxt2, usrMnPwrStTxt1, usrMnPwrStTxt2,
            uploadTitle, usrFirmPanTxt1, usrFirmPanTxt2;

    String uploadTypeOption[] = {"Aadhaar Card", "Driving Licence", "Voter Card", "Passport", "Water Bill",
            "Landline or Postpaid mobile bill", "Electricity bill", "Bank Passbook/ Statement"};
    private ImageView aadharimageView1, aadharimageView2, panimageView1, panimageView2;
    private ImageView icBack;
    private ImageView imageUserProfile;
    private String userId,profileVerified;

    RecyclerView aadhar_recycler_view;
    RecyclerView pan_recycler_view;
    RecyclerView bsnAddr_photo_recycler_view;
    RecyclerView frmPan_recycler_view;
    RecyclerView cnclChq_recycler_view;
    RecyclerView pssprt_photo_recycler_view;
    RecyclerView upldsgn_photo_recycler_view;
    RecyclerView upldshop_photo_recycler_view;
    private MySingleton mySingleton;
    private Network network;
    private KProgressHUD kProgressHUD;
    private DisplaySnackBar displaySnackBar;
    public static Data profileData;
    private Intent intent;
    private int Day, Month, Year;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private  String  sDOB = "";
    private String Adhar_Url = "", Pan_Url = "", upload_path;
    private String Firm_Pan_Url = "", Gst_Url = "", Address_Url = "", Cheque_Url = "", Passport_Url = "", SignedForm_Url = "", Shop_Url = "", Idba_Url="";
    private String profileImage;
    LinearLayout lnPanLayout, lnBsnAddrLayout, lnFrmPanLayout, lnChquLayout, lnPasprtLayout,
            lnSigndPhotoLayout, lnShopLayout;

    LinearLayout lnIbdaLayout;
    TextView usrIbdaTitle,usrIbdanumber;
    RecyclerView ibda_recycler_view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posdetails);

        initView();
        intent = getIntent();
        userId = intent.getStringExtra("userId");

        if (network.isNetworkConnected(context)) {
            callUserProfile(userId);
        } else {
            displaySnackBar.DisplaySnackBar(Constant.NETWIRK_ERROR, Constant.TYPE_ERROR);
        }

        viewListener();


    }

    private void initView() {
        context = POSDetails.this;
        mySingleton = new MySingleton(this);
        displaySnackBar = new DisplaySnackBar(this);
        network = new Network();
        lnIbdaLayout = findViewById(R.id.lnIbdaLayout);
        usrIbdaTitle = findViewById(R.id.usrIbdaTitle);
        usrIbdanumber = findViewById(R.id.usrIbdanumber);
        ibda_recycler_view = findViewById(R.id.ibda_recycler_view);
        userName = findViewById(R.id.userName);
        userMobile = findViewById(R.id.userMobile);
        userLandline = findViewById(R.id.userLandline);
        usrMobileTxt1 = findViewById(R.id.usrMobileTxt1);
        usrMobileTxt2 = findViewById(R.id.usrMobileTxt2);
        usrOffEmailTxt1 = findViewById(R.id.usrOffEmailTxt1);
        usrOffEmailTxt2 = findViewById(R.id.usrOffEmailTxt2);
        usrPrsEmailTxt1 = findViewById(R.id.usrPrsEmailTxt1);
        usrPrsEmailTxt2 = findViewById(R.id.usrPrsEmailTxt2);
        usrBusAddrTxt1 = findViewById(R.id.usrBusAddrTxt1);
        usrCommAddrTxt1 = findViewById(R.id.usrCommAddrTxt1);
        usrAadharTxt1 = findViewById(R.id.usrAadharTxt1);
        usrAadharTxt2 = findViewById(R.id.usrAadharTxt2);
        usrPanTxt1 = findViewById(R.id.usrPanTxt1);
        usrPanTxt2 = findViewById(R.id.usrPanTxt2);
        usrBankHolderTxt1 = findViewById(R.id.usrBankHolderTxt1);
        usrBankHolderTxt2 = findViewById(R.id.usrBankHolderTxt2);
        usrAccountNumTxt1 = findViewById(R.id.usrAccountNumTxt1);
        usrAccountNumTxt2 = findViewById(R.id.usrAccountNumTxt2);
        usrIfscTxt1 = findViewById(R.id.usrIfscTxt1);
        usrIfscTxt2 = findViewById(R.id.usrIfscTxt2);
        usrBankNamTxt1 = findViewById(R.id.usrBankNamTxt1);
        usrBankNamTxt2 = findViewById(R.id.usrBankNamTxt2);
        usrGndrTxt1 = findViewById(R.id.usrGndrTxt1);
        usrGndrTxt2 = findViewById(R.id.usrGndrTxt2);
        usrDobTxt1 = findViewById(R.id.usrDobTxt1);
        usrDobTxt2 = findViewById(R.id.usrDobTxt2);
        usrMrStatusTxt1 = findViewById(R.id.usrMrStatusTxt1);
        usrMrStatusTxt2 = findViewById(R.id.usrMrStatusTxt2);
        usrFrmNameTxt1 = findViewById(R.id.usrFrmNameTxt1);
        usrFrmNameTxt2  = findViewById(R.id.usrFrmNameTxt2);
        usrFrmTypeTxt1 = findViewById(R.id.usrFrmTypeTxt1);
        usrFrmTypeTxt2 = findViewById(R.id.usrFrmTypeTxt2);
        usrNtofBusnsTxt1 = findViewById(R.id.usrNtofBusnsTxt1);
        usrNtofBusnsTxt2 = findViewById(R.id.usrNtofBusnsTxt2);
        usrMnPwrStTxt1 = findViewById(R.id.usrMnPwrStTxt1);
        usrMnPwrStTxt2 = findViewById(R.id.usrMnPwrStTxt2);
        uploadTitle = findViewById(R.id.uploadTitle);
        usrFirmPanTxt1 = findViewById(R.id.usrFirmPanTxt1);
        usrFirmPanTxt2 = findViewById(R.id.usrFirmPanTxt2);
        imageUserProfile =  findViewById(R.id.imgAddFos);
        aadharimageView1 =  findViewById(R.id.imgAddFos);
        aadharimageView2 =  findViewById(R.id.imgAddFos);
        panimageView1 =  findViewById(R.id.imgAddFos);
        panimageView2 =  findViewById(R.id.imgAddFos);
        imageUserProfile = findViewById(R.id.imageUserProfile);
        icBack =  findViewById(R.id.icBack);
        ibda_recycler_view = findViewById(R.id.ibda_recycler_view);
        LinearLayoutManager idbasupportersLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        ibda_recycler_view.setLayoutManager(idbasupportersLayoutManager);


        aadhar_recycler_view = findViewById(R.id.aadhar_recycler_view);
        LinearLayoutManager supportersLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        aadhar_recycler_view.setLayoutManager(supportersLayoutManager);

        pan_recycler_view = findViewById(R.id.pan_recycler_view);
        LinearLayoutManager pansupportersLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        pan_recycler_view.setLayoutManager(pansupportersLayoutManager);

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
        lnPanLayout = findViewById(R.id.lnPanLayout);
        lnBsnAddrLayout = findViewById(R.id.lnBsnAddrLayout);
        lnFrmPanLayout = findViewById(R.id.lnFrmPanLayout);
        lnChquLayout = findViewById(R.id.lnChquLayout);
        lnPasprtLayout = findViewById(R.id.lnPasprtLayout);
        lnSigndPhotoLayout = findViewById(R.id.lnSigndPhotoLayout);
        lnShopLayout = findViewById(R.id.lnShopLayout);
    }

    private void viewListener() {
        icBack.setOnClickListener(v -> finish());
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
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

    public void callUserProfile(String userId) {

        kProgressHUD = KProgressHUD.create(context)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .setWindowColor(getResources().getColor(R.color.progressbar_color))
                .show();

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        final Call<ProfileResponce> call = apiService.getProfileUser(mySingleton.getData(Constant.TOKEN_BASE_64), userId);
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
                                    Log.i("ProfData", "json " + json);
                                    profileData = response.body().getData();
                                    showDataOnView();
                                } else {
                                    displaySnackBar.DisplaySnackBar("Data not found", Constant.TYPE_ERROR);

                                }
                            } else if (response.body().getStatus() == 0) {
                                Log.e("TAG", "onResponse: status 0" );
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

    private void showDataOnView() {
        mySingleton.saveData(Constant.USER_PROFILE_VERIFYED, profileData.getProfile_verified());

        userName.setText((!TextUtil.isStringNullOrBlank(profileData.getFirst_name()) ? profileData.getFirst_name() : "")+" "+
                (!TextUtil.isStringNullOrBlank(profileData.getLast_name()) ? profileData.getLast_name() : ""));
        usrGndrTxt2.setText((!TextUtil.isStringNullOrBlank(profileData.getGender()) ? profileData.getGender() : ""));
        usrMnPwrStTxt2.setText((!TextUtil.isStringNullOrBlank(profileData.getManpower_strength()) ? profileData.getManpower_strength() : ""));
        usrMrStatusTxt2.setText((!TextUtil.isStringNullOrBlank(profileData.getMarital_status()) ? profileData.getMarital_status() : ""));
        userMobile.setText((!TextUtil.isStringNullOrBlank(profileData.getPhone_number()) ? profileData.getPhone_number() : ""));
        usrMobileTxt2.setText((!TextUtil.isStringNullOrBlank(profileData.getPhone_number()) ? profileData.getPhone_number() : ""));
        usrIbdanumber.setText((!TextUtil.isStringNullOrBlank(profileData.getIb_da_code()) ? profileData.getIb_da_code() : ""));

        Adhar_Url = (!TextUtil.isStringNullOrBlank(profileData.getCp_adhar_proof()) ? profileData.getCp_adhar_proof() : "");
        Pan_Url = (!TextUtil.isStringNullOrBlank(profileData.getCp_pan_proof()) ? profileData.getCp_pan_proof() : "");
        uploadTitle.setText("Proof of Address");
        if(profileData.getUpload_type_option() == 0) {
            usrAadharTxt1.setText("Aadhaar No.");
        } else if(profileData.getUpload_type_option() == 1) {
            usrAadharTxt1.setText("Driving Licence No.");
        } else if(profileData.getUpload_type_option() == 2) {
            usrAadharTxt1.setText("Voter Id No.");
        } else if(profileData.getUpload_type_option() == 3) {
            usrAadharTxt1.setText("passport No.");
        } else {
            usrAadharTxt1.setText(uploadTypeOption[profileData.getUpload_type_option()]);
            usrAadharTxt2.setVisibility(View.GONE);
        }

        Address_Url = (!TextUtil.isStringNullOrBlank(profileData.getAddress_proof()) ? profileData.getAddress_proof() : "");
        Firm_Pan_Url = (!TextUtil.isStringNullOrBlank(profileData.getFirm_pan_proof()) ? profileData.getFirm_pan_proof() : "");
        Cheque_Url = (!TextUtil.isStringNullOrBlank(profileData.getCheque_proof()) ? profileData.getCheque_proof() : "");
        Passport_Url = (!TextUtil.isStringNullOrBlank(profileData.getPassport_size_photo()) ? profileData.getPassport_size_photo() : "");
        SignedForm_Url = (!TextUtil.isStringNullOrBlank(profileData.getSigned_form()) ? profileData.getSigned_form() : "");
        Shop_Url = (!TextUtil.isStringNullOrBlank(profileData.getShop_photo()) ? profileData.getShop_photo() : "");
        Idba_Url = (!TextUtil.isStringNullOrBlank(profileData.getIb_da_proof()) ? profileData.getIb_da_proof() : "");


        upload_path = (!TextUtil.isStringNullOrBlank(profileData.getUpload_path()) ? profileData.getUpload_path() : "");
        profileImage = (!TextUtil.isStringNullOrBlank(profileData.getPhoto()) ? profileData.getPhoto() : "");
        if(!TextUtil.isStringNullOrBlank(profileImage)){
            CircleImageView cicleImage = findViewById(R.id.imageUserProfile);
            App.imageLoader.displayImage(upload_path+profileImage, cicleImage, App.defaultOptions);
        }

        if(!TextUtil.isStringNullOrBlank(Adhar_Url)){
            String[] Adhar_images = Adhar_Url.split(",");
            Log.i("aadhar Images", Adhar_images[0]);
            ImageUploadAdapter imageUploadAdapter;
            aadhar_recycler_view.setAdapter(new ImageUploadAdapter(Adhar_images, R.layout.image_items, context, upload_path));
            aadhar_recycler_view.setScrollContainer(false);

        }else{
            lnBsnAddrLayout.setVisibility(View.GONE);
        }
        if(!TextUtil.isStringNullOrBlank(Idba_Url)){
            String[] Adhar_images = Idba_Url.split(",");
            Log.i("AAAAAA", Adhar_images[0]);
            ibda_recycler_view.setAdapter(new ImageUploadAdapter(Adhar_images, R.layout.image_items, context, upload_path));
            ibda_recycler_view.setScrollContainer(false);

        }else{
            lnIbdaLayout.setVisibility(View.GONE);
        }

        if(!TextUtil.isStringNullOrBlank(Pan_Url)){
            String[] Pan_images = Pan_Url.split(",");
            Log.i("panImages", Pan_images[0]);
            pan_recycler_view.setAdapter(new ImageUploadAdapter(Pan_images, R.layout.image_items, context, upload_path));
            pan_recycler_view.setScrollContainer(false);

        }else{
            lnPanLayout.setVisibility(View.GONE);
        }

        if(!TextUtil.isStringNullOrBlank(Address_Url)){
            String[] Address_images = Address_Url.split(",");
            Log.i("addressimages", Address_images[0]);
            bsnAddr_photo_recycler_view.setAdapter(new ImageUploadAdapter(Address_images, R.layout.image_items, context, upload_path));
            bsnAddr_photo_recycler_view.setScrollContainer(false);

        }else{
            lnBsnAddrLayout.setVisibility(View.GONE);
        }

        if(!TextUtil.isStringNullOrBlank(Firm_Pan_Url)){
            String[] FirmPan_images = Firm_Pan_Url.split(",");
            Log.i("FirmImages", FirmPan_images[0]);
            frmPan_recycler_view.setAdapter(new ImageUploadAdapter(FirmPan_images, R.layout.image_items, context, upload_path));
            frmPan_recycler_view.setScrollContainer(false);

        }else{
            lnFrmPanLayout.setVisibility(View.GONE);
        }

        if(!TextUtil.isStringNullOrBlank(Cheque_Url)){
            String[] Cheque_images = Cheque_Url.split(",");
            Log.i("chequeImages", Cheque_images[0]);
            cnclChq_recycler_view.setAdapter(new ImageUploadAdapter(Cheque_images, R.layout.image_items, context, upload_path));
            cnclChq_recycler_view.setScrollContainer(false);

        }else{
            lnChquLayout.setVisibility(View.GONE);
        }

        if(!TextUtil.isStringNullOrBlank(Passport_Url)){
            String[] Passport_images = Passport_Url.split(",");
            Log.i("passportImages", Passport_images[0]);
            pssprt_photo_recycler_view.setAdapter(new ImageUploadAdapter(Passport_images, R.layout.image_items, context, upload_path));
            pssprt_photo_recycler_view.setScrollContainer(false);

        }else{
            lnPasprtLayout.setVisibility(View.GONE);
        }

        if(!TextUtil.isStringNullOrBlank(SignedForm_Url)){
            String[] Signed_images = SignedForm_Url.split(",");
            Log.i("signedImages", Signed_images[0]);
            upldsgn_photo_recycler_view.setAdapter(new ImageUploadAdapter(Signed_images, R.layout.image_items, context, upload_path));
            upldsgn_photo_recycler_view.setScrollContainer(false);

        }else{
            lnSigndPhotoLayout.setVisibility(View.GONE);
        }

        if(!TextUtil.isStringNullOrBlank(Shop_Url)){
            String[] Shop_images = Shop_Url.split(",");
            Log.i("shopImages", Shop_images[0]);
            upldshop_photo_recycler_view.setAdapter(new ImageUploadAdapter(Shop_images, R.layout.image_items, context, upload_path));
            upldshop_photo_recycler_view.setScrollContainer(false);

        }else{
            lnShopLayout.setVisibility(View.GONE);
        }

        if (!TextUtil.isStringNullOrBlank(profileData.getDob())) {
            Log.i("DateString", profileData.getDob());
            String[] dateformat = profileData.getDob().split("-");
            mYear = Integer.parseInt(dateformat[0]);
            mMonth = Integer.parseInt(dateformat[1]);
            mDay = Integer.parseInt(dateformat[2]);
            sDOB = mYear + "-" + checkDigit(mMonth) + "-" + checkDigit(mDay);
            usrDobTxt2.setText(checkDigit(mDay) + "-" + getMonthForInt(mMonth - 1) + "-" + mYear);
        }

        usrBankHolderTxt2.setText((!TextUtil.isStringNullOrBlank(profileData.getBank_holder_name()) ? profileData.getBank_holder_name() : ""));
        usrAccountNumTxt2.setText((!TextUtil.isStringNullOrBlank(profileData.getAccount_number()) ? profileData.getAccount_number() : ""));
        usrIfscTxt2.setText((!TextUtil.isStringNullOrBlank(profileData.getIfsc_code()) ? profileData.getIfsc_code() : ""));
        usrBankNamTxt2.setText((!TextUtil.isStringNullOrBlank(profileData.getBank_name()) ? profileData.getBank_name() : ""));
        usrFirmPanTxt2.setText((!TextUtil.isStringNullOrBlank(profileData.getCompany_pan_no()) ? profileData.getCompany_pan_no() : ""));
        usrAadharTxt2.setText((!TextUtil.isStringNullOrBlank(profileData.getAadhar_no()) ? profileData.getAadhar_no() : ""));
        usrPanTxt2.setText((!TextUtil.isStringNullOrBlank(profileData.getPan_no()) ? profileData.getPan_no() : ""));
        usrFrmTypeTxt2.setText((!TextUtil.isStringNullOrBlank(profileData.getBusiness_type()) ? profileData.getBusiness_type() : ""));
        usrNtofBusnsTxt2.setText((!TextUtil.isStringNullOrBlank(profileData.getNature_of_business()) ? profileData.getNature_of_business() : ""));
        userLandline.setText((!TextUtil.isStringNullOrBlank(profileData.getLandline_no()) ? profileData.getLandline_no() : ""));
        usrOffEmailTxt2.setText((!TextUtil.isStringNullOrBlank(profileData.getOfficial_email_id()) ? profileData.getOfficial_email_id() : ""));
        usrPrsEmailTxt2.setText((!TextUtil.isStringNullOrBlank(profileData.getPersonal_email_id()) ? profileData.getPersonal_email_id() : ""));
        usrFrmNameTxt2.setText((!TextUtil.isStringNullOrBlank(profileData.getCp_firm_name()) ? profileData.getCp_firm_name() : ""));
        if (profileData.getCommunication() != null) {
            usrCommAddrTxt1.setText((!TextUtil.isStringNullOrBlank(profileData.getCommunication().getAddress()) ? profileData.getCommunication().getAddress() : "")
                    + ", " + (!TextUtil.isStringNullOrBlank(profileData.getCommunication().getDistrict()) ? profileData.getCommunication().getDistrict() : "")
                    + ", "+(!TextUtil.isStringNullOrBlank(profileData.getCommunication().getPincode()) ? profileData.getCommunication().getPincode() : ""));
        }

        int uploadOption = profileData.getUpload_type_option();
        if(uploadOption == 0){
            uploadTitle.setText("Aadhaar Details");
            usrAadharTxt1.setText("Aadhaar No. :");
        } else if(uploadOption == 1){
            uploadTitle.setText("Driving Licence Details");
            usrAadharTxt1.setText("Driving Licence No. :");
        } else if(uploadOption == 2){
            uploadTitle.setText("Voter Card Details");
            usrAadharTxt1.setText("Voter Id No. :");
        }

        if (profileData.getBussiness() != null) {
            usrBusAddrTxt1.setText((!TextUtil.isStringNullOrBlank(profileData.getBussiness().getAddress()) ? profileData.getBussiness().getAddress() : "")
                    + ", " + (!TextUtil.isStringNullOrBlank(profileData.getBussiness().getDistrict()) ? profileData.getBussiness().getDistrict() : "")
                    + ", "+(!TextUtil.isStringNullOrBlank(profileData.getBussiness().getPincode()) ? profileData.getBussiness().getPincode() : ""));
        }


    }
    public String checkDigit(int number) {
        return number <= 9 ? "0" + number : String.valueOf(number);
    }

    String getMonthForInt(int m) {
        String month = "invalid";
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getMonths();
        if (m >= 0 && m <= 11) {
            month = months[m];
        }
        return month;
    }

}
