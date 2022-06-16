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
import android.widget.RelativeLayout;
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
import com.rebliss.domain.model.profile.UserDetail;
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

public class FOSDetails extends AppCompatActivity {
    private Context context;
    private TextView userName, userMobile, userLandline, usrMobileTxt1, usrMobileTxt2, usrOffEmailTxt1, usrOffEmailTxt2,
            usrPrsEmailTxt1, usrPrsEmailTxt2, usrBusAddrTxt1, usrCommAddrTxt1, usrAadharTxt1, usrAadharTxt2, usrPanTxt1,
            usrPanTxt2, usrBankHolderTxt1, usrBankHolderTxt2, usrAccountNumTxt1, usrAccountNumTxt2, usrIfscTxt1, usrIfscTxt2,
            usrBankNamTxt1, usrBankNamTxt2, usrGndrTxt1, usrGndrTxt2, usrDobTxt1, usrDobTxt2, usrMrStatusTxt1, usrMrStatusTxt2,
            usrFrmNameTxt1, usrFrmNameTxt2, usrFrmTypeTxt1, usrFrmTypeTxt2, usrNtofBusnsTxt1, usrNtofBusnsTxt2;
    private TextView  usrEmgContactTxt1, usrEmgContactTxt2, usrEmgContactPrsnNameTxt1, usrEmgContactPrsnNameTxt2, uploadTitle;

    private ImageView  aadharimageView1, aadharimageView2, panimageView1, panimageView2;
   private ImageView icBack;
    private CircleImageView imageUserProfile;
    LinearLayout lnPanLayout, lnBsnAddrLayout, lnFrmPanLayout, lnChquLayout, lnPasprtLayout,
            lnSigndPhotoLayout, lnShopLayout;
    RelativeLayout relCheqlayout;
    private String userId,profileVerified;
    RecyclerView aadhar_recycler_view;
    RecyclerView pan_recycler_view;
    RecyclerView pssprt_photo_recycler_view;
    RecyclerView cnclChq_recycler_view;
    private MySingleton mySingleton;
    private Network network;
    private KProgressHUD kProgressHUD;
    private DisplaySnackBar displaySnackBar;
    public static Data profileData;
    private Intent intent;
    private int Day, Month, Year;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private  String  sDOB = "";
    private String Adhar_Url = "", Pan_Url = "", upload_path, Passport_Url = "", Cheque_Url = "";
    private String[] Adhar_images ;
    private String profileImage;
    private int currentIndex;
    private int startIndex;
    private int endIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fosdetails);

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
        context = FOSDetails.this;
        mySingleton = new MySingleton(this);
        displaySnackBar = new DisplaySnackBar(this);
        network = new Network();
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
        usrEmgContactTxt1 = findViewById(R.id.usrEmgContactTxt1);
        usrEmgContactTxt2 = findViewById(R.id.usrEmgContactTxt2);
        usrEmgContactPrsnNameTxt1 = findViewById(R.id.usrEmgContactPrsnNameTxt1);
        usrEmgContactPrsnNameTxt2 = findViewById(R.id.usrEmgContactPrsnNameTxt2);
        uploadTitle = findViewById(R.id.uploadTitle);
        aadharimageView1 =  findViewById(R.id.imgAddFos);
        aadharimageView2 =  findViewById(R.id.imgAddFos);
        panimageView1 =  findViewById(R.id.imgAddFos);
        panimageView2 =  findViewById(R.id.imgAddFos);
        icBack =  findViewById(R.id.icBack);
        imageUserProfile = findViewById(R.id.imageUserProfile);
        lnChquLayout = findViewById(R.id.lnChquLayout);
        lnPasprtLayout = findViewById(R.id.lnPasprtLayout);
        aadhar_recycler_view = findViewById(R.id.aadhar_recycler_view);
        LinearLayoutManager supportersLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        aadhar_recycler_view.setLayoutManager(supportersLayoutManager);
        pan_recycler_view = findViewById(R.id.pan_recycler_view);
        LinearLayoutManager pansupportersLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        pan_recycler_view.setLayoutManager(pansupportersLayoutManager);
        pssprt_photo_recycler_view = findViewById(R.id.pssprt_photo_recycler_view);
        LinearLayoutManager pssprtsupportersLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        pssprt_photo_recycler_view.setLayoutManager(pssprtsupportersLayoutManager);
        cnclChq_recycler_view = findViewById(R.id.cnclChq_recycler_view);
        LinearLayoutManager cnlcqesupportersLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        cnclChq_recycler_view.setLayoutManager(cnlcqesupportersLayoutManager);

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

        UserDetail userdetail = new UserDetail();
        userdetail.setUser_id(userId);
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

                                Log.e("TAG", "onResponse: status 0" );                            }
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

   private void showDataOnView() {
        mySingleton.saveData(Constant.USER_PROFILE_VERIFYED, profileData.getProfile_verified());
        //  if (profileData.getProfile_verified().equalsIgnoreCase("0")) {
//

       userName.setText((!TextUtil.isStringNullOrBlank(profileData.getFirst_name()) ? profileData.getFirst_name() : "")+" "+
                (!TextUtil.isStringNullOrBlank(profileData.getLast_name()) ? profileData.getLast_name() : ""));
        // textChName.setText("Name");
//        }else {
//            etChName.setText((!TextUtil.isStringNullOrBlank(profileData.getFirst_name()) ? profileData.getFirst_name() : ""));
//            etChLName.setText((!TextUtil.isStringNullOrBlank(profileData.getLast_name()) ? profileData.getLast_name() : ""));
//        }

        // etChName.setText((!TextUtil.isStringNullOrBlank(profileData.getFirst_name()) ? profileData.getFirst_name() : ""));
        // etChName.setText((!TextUtil.isStringNullOrBlank(profileData.getFirst_name()) ? profileData.getFirst_name() : ""));
       // etChFatherName.setText((!TextUtil.isStringNullOrBlank(profileData.getPos_father_name()) ? profileData.getPos_father_name() : ""));
       usrGndrTxt2.setText((!TextUtil.isStringNullOrBlank(profileData.getGender()) ? profileData.getGender() : ""));
       usrMrStatusTxt2.setText((!TextUtil.isStringNullOrBlank(profileData.getMarital_status()) ? profileData.getMarital_status() : ""));
       userMobile.setText((!TextUtil.isStringNullOrBlank(profileData.getPhone_number()) ? profileData.getPhone_number() : ""));
       usrMobileTxt2.setText((!TextUtil.isStringNullOrBlank(profileData.getPhone_number()) ? profileData.getPhone_number() : ""));
       usrFrmTypeTxt2.setText((!TextUtil.isStringNullOrBlank(profileData.getBusiness_type()) ? profileData.getBusiness_type() : ""));
       usrNtofBusnsTxt2.setText((!TextUtil.isStringNullOrBlank(profileData.getNature_of_business()) ? profileData.getNature_of_business() : ""));
       usrEmgContactPrsnNameTxt2.setText((!TextUtil.isStringNullOrBlank(profileData.getEm_contact_person_name()) ? profileData.getEm_contact_person_name() : ""));
       usrEmgContactTxt2.setText((!TextUtil.isStringNullOrBlank(profileData.getEm_person_contact_no()) ? profileData.getEm_person_contact_no() : ""));

       Adhar_Url = (!TextUtil.isStringNullOrBlank(profileData.getCp_adhar_proof()) ? profileData.getCp_adhar_proof() : "");
       Pan_Url = (!TextUtil.isStringNullOrBlank(profileData.getCp_pan_proof()) ? profileData.getCp_pan_proof() : "");
       Cheque_Url = (!TextUtil.isStringNullOrBlank(profileData.getCheque_proof()) ? profileData.getCheque_proof() : "");
       upload_path = (!TextUtil.isStringNullOrBlank(profileData.getUpload_path()) ? profileData.getUpload_path() : "");
       profileImage = (!TextUtil.isStringNullOrBlank(profileData.getPhoto()) ? profileData.getPhoto() : "");
       Passport_Url = (!TextUtil.isStringNullOrBlank(profileData.getPassport_size_photo()) ? profileData.getPassport_size_photo() : "");
       if(!TextUtil.isStringNullOrBlank(profileImage)){
           CircleImageView cicleImage = (CircleImageView)findViewById(R.id.imageUserProfile);
           //cicleImage.setImageURI(Uri.fromFile(new File(upload_path+profileImage)));
           App.imageLoader.displayImage(upload_path+profileImage, cicleImage, App.defaultOptions);
       }

      if(!TextUtil.isStringNullOrBlank(Adhar_Url)){
           String[] Adhar_images = Adhar_Url.split(",");
          Log.i("AAAAAA", Adhar_images[0]);
          //ImageView imageview = (ImageView)findViewById(R.id.aadharimageView1);
          //imageview.setImageDrawable(upload_path+Adhar_Url);
          //imageview.setImageURI(Uri.fromFile(new File(upload_path+Adhar_Url)));
          ImageUploadAdapter imageUploadAdapter;
          aadhar_recycler_view.setAdapter(new ImageUploadAdapter(Adhar_images, R.layout.image_items, context, upload_path));
          aadhar_recycler_view.setScrollContainer(false);

       }

       if(!TextUtil.isStringNullOrBlank(Pan_Url)){
           String[] Pan_images = Pan_Url.split(",");
           Log.i("AAAAAAP", Pan_images[0]);
           //ImageView imageview = (ImageView)findViewById(R.id.aadharimageView1);
           //imageview.setImageDrawable(upload_path+Adhar_Url);
           //imageview.setImageURI(Uri.fromFile(new File(upload_path+Adhar_Url)));
           ImageUploadAdapter panimageUploadAdapter;
           pan_recycler_view.setAdapter(new ImageUploadAdapter(Pan_images, R.layout.image_items, context, upload_path));
           pan_recycler_view.setScrollContainer(false);

       } /*else {
           lnPanLayout.setVisibility(View.GONE);
       }*/

       if(!TextUtil.isStringNullOrBlank(Passport_Url)){
           String[] Passport_images = Passport_Url.split(",");
           Log.i("AAAAAA", Passport_images[0]);
           //ImageView imageview = (ImageView)findViewById(R.id.aadharimageView1);
           //imageview.setImageDrawable(upload_path+Adhar_Url);
           //imageview.setImageURI(Uri.fromFile(new File(upload_path+Adhar_Url)));
           ImageUploadAdapter imageUploadAdapter;
           pssprt_photo_recycler_view.setAdapter(new ImageUploadAdapter(Passport_images, R.layout.image_items, context, upload_path));
           pssprt_photo_recycler_view.setScrollContainer(false);

       }  else {
           lnPasprtLayout.setVisibility(View.GONE);
       }


       if(!TextUtil.isStringNullOrBlank(Cheque_Url)){
           String[] Cheque_images = Cheque_Url.split(",");
           Log.i("AAAAAAP", Cheque_images[0]);
           //ImageView imageview = (ImageView)findViewById(R.id.aadharimageView1);
           //imageview.setImageDrawable(upload_path+Adhar_Url);
           //imageview.setImageURI(Uri.fromFile(new File(upload_path+Adhar_Url)));
           ImageUploadAdapter panimageUploadAdapter;
           cnclChq_recycler_view.setAdapter(new ImageUploadAdapter(Cheque_images, R.layout.image_items, context, upload_path));
           cnclChq_recycler_view.setScrollContainer(false);

       } else {
           lnChquLayout.setVisibility(View.GONE);
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

       usrBankHolderTxt2.setText((!TextUtil.isStringNullOrBlank(profileData.getBank_holder_name()) ? profileData.getBank_holder_name() : ""));
       usrAccountNumTxt2.setText((!TextUtil.isStringNullOrBlank(profileData.getAccount_number()) ? profileData.getAccount_number() : ""));
       usrIfscTxt2.setText((!TextUtil.isStringNullOrBlank(profileData.getIfsc_code()) ? profileData.getIfsc_code() : ""));
       usrBankNamTxt2.setText((!TextUtil.isStringNullOrBlank(profileData.getBank_name()) ? profileData.getBank_name() : ""));

       usrAadharTxt2.setText((!TextUtil.isStringNullOrBlank(profileData.getAadhar_no()) ? profileData.getAadhar_no() : ""));
       usrPanTxt2.setText((!TextUtil.isStringNullOrBlank(profileData.getPan_no()) ? profileData.getPan_no() : ""));

        userLandline.setText((!TextUtil.isStringNullOrBlank(profileData.getLandline_no()) ? profileData.getLandline_no() : ""));
        usrOffEmailTxt2.setText((!TextUtil.isStringNullOrBlank(profileData.getEm_person_contact_no()) ? profileData.getEm_person_contact_no() : ""));
        usrPrsEmailTxt2.setText((!TextUtil.isStringNullOrBlank(profileData.getOfficial_email_id()) ? profileData.getOfficial_email_id() : ""));
       usrFrmNameTxt2.setText((!TextUtil.isStringNullOrBlank(profileData.getCp_firm_name()) ? profileData.getCp_firm_name() : ""));
        if (profileData.getCommunication() != null) {
            usrCommAddrTxt1.setText((!TextUtil.isStringNullOrBlank(profileData.getCommunication().getAddress()) ? profileData.getCommunication().getAddress() : "")
                    + ", " + (!TextUtil.isStringNullOrBlank(profileData.getCommunication().getDistrict()) ? profileData.getCommunication().getDistrict() : "")
                    + ", "+(!TextUtil.isStringNullOrBlank(profileData.getCommunication().getPincode()) ? profileData.getCommunication().getPincode() : ""));
            ;
            //etCommunicationState.setText((!TextUtil.isStringNullOrBlank(profileData.getCommunication().getState()) ? profileData.getCommunication().getState() : ""));
            // etCommunicationDistrict.setText((!TextUtil.isStringNullOrBlank(profileData.getCommunication().getDistrict()) ? profileData.getCommunication().getDistrict() : ""));
            //  etCommunicationCity.setText((!TextUtil.isStringNullOrBlank(profileData.getCommunication().getCity()) ? profileData.getCommunication().getCity() : ""));
           /* isCommunicationPinNew = false;
            etCommunicationPIN.setText((!TextUtil.isStringNullOrBlank(profileData.getCommunication().getPincode()) ? profileData.getCommunication().getPincode() : ""));
            isCommunicationPinNew = true;

            if (!TextUtil.isStringNullOrBlank(profileData.getCommunication().getState())) {
                if (stateData != null) {

                    for (int i = 0; i < stateData.size(); i++) {
                        if (profileData.getCommunication().getState().equalsIgnoreCase(stateData.get(i).getId())) {
                            spCommunicationStatePosition = i;
                        }
                    }
                }
            }
            displaySateList();

        } else {
            displaySateList();
        } */
        }
        if (profileData.getBussiness() != null) {
            usrBusAddrTxt1.setText((!TextUtil.isStringNullOrBlank(profileData.getBussiness().getAddress()) ? profileData.getBussiness().getAddress() : "")
                    + ", " + (!TextUtil.isStringNullOrBlank(profileData.getBussiness().getDistrict()) ? profileData.getBussiness().getDistrict() : "")
                    + ", "+(!TextUtil.isStringNullOrBlank(profileData.getBussiness().getPincode()) ? profileData.getBussiness().getPincode() : ""));

            //etBusinessState.setText((!TextUtil.isStringNullOrBlank(profileData.getBussiness().getState()) ? profileData.getBussiness().getState() : ""));
            /*etBusinessDistrict.setText((!TextUtil.isStringNullOrBlank(profileData.getBussiness().getDistrict()) ? profileData.getBussiness().getDistrict() : ""));
            etBusinessDistrict.setText((!TextUtil.isStringNullOrBlank(profileData.getBussiness().getDistrict()) ? profileData.getBussiness().getDistrict() : ""));
            //  etBusinessCity.setText((!TextUtil.isStringNullOrBlank(profileData.getBussiness().getCity()) ? profileData.getBussiness().getCity() : ""));
            isBussinessPinNew = false;
            etBusinessPIN.setText((!TextUtil.isStringNullOrBlank(profileData.getBussiness().getPincode()) ? profileData.getBussiness().getPincode() : ""));
            isBussinessPinNew = true;
            if (!TextUtil.isStringNullOrBlank(profileData.getBussiness().getState())) {
                if (stateData != null) {

                    for (int i = 0; i < stateData.size(); i++) {
                        if (profileData.getBussiness().getState().equalsIgnoreCase(stateData.get(i).getId())) {
                            spBusinessStatePosition = i;
                        }
                    }
                }
            }
            if (!TextUtil.isStringNullOrBlank(profileData.getBussiness().getCity())) {
                if (citydataBusiness != null) {
                    for (int i = 0; i < citydataBusiness.size(); i++) {
                        if (profileData.getBussiness().getCity().equalsIgnoreCase(citydataBusiness.get(i).getId())) {
                            spBusinessCityPosition = i;
                        }
                    }
                }
            }
            displaySateListBusiness();
        } else {
            displaySateListBusiness();

        } */
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

   /* public void onClick(View v) {
        Animation inFromRight = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, +1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        inFromRight.setDuration(500);
        ImageView imageview = (ImageView)findViewById(R.id.aadharimageView1);
        imageview.startAnimation(inFromRight);

        if ((Adhar_images.length)> currentIndex){

            imageview.setImageResource(Adhar_images[currentIndex]);

            currentIndex++;

        }


    }*/

}
