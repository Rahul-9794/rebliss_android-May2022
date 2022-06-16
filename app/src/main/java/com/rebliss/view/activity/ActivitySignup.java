 package com.rebliss.view.activity;

 import android.content.Context;
 import android.content.DialogInterface;
 import android.content.Intent;
 import android.graphics.Color;
 import android.os.Bundle;
 import android.text.Editable;
 import android.text.TextWatcher;
 import android.util.Log;
 import android.view.View;
 import android.view.ViewGroup;
 import android.widget.AdapterView;
 import android.widget.ArrayAdapter;
 import android.widget.Button;
 import android.widget.CheckBox;
 import android.widget.EditText;
 import android.widget.ImageView;
 import android.widget.Spinner;
 import android.widget.TextView;

 import androidx.appcompat.app.AlertDialog;
 import androidx.appcompat.app.AppCompatActivity;
 import androidx.appcompat.widget.Toolbar;
 import com.google.android.material.textfield.TextInputLayout;
 import com.google.gson.Gson;
 import com.kaopiz.kprogresshud.KProgressHUD;
 import com.rebliss.utils.App;
 import com.rebliss.R;
 import com.rebliss.data.perf.MySingleton;
 import com.rebliss.domain.constant.Constant;
 import com.rebliss.domain.model.EducationResponse;
 import com.rebliss.domain.model.ErrorBody;
 import com.rebliss.domain.model.Occupation.AllGroup;
 import com.rebliss.domain.model.Occupation.OccupationResponse;
 import com.rebliss.domain.model.agegroup.AgeGroupResponse;
 import com.rebliss.domain.model.city.City;
 import com.rebliss.domain.model.city.CityResponce;
 import com.rebliss.domain.model.searchstate.SearchStateResponce;
 import com.rebliss.domain.model.signup.Data;
 import com.rebliss.domain.model.signup.SignupRequest;
 import com.rebliss.domain.model.signup.SignupResponce;
 import com.rebliss.domain.model.state.State;
 import com.rebliss.domain.model.state.StateResponce;
 import com.rebliss.presenter.helper.Base64Encoded;
 import com.rebliss.presenter.helper.DisplaySnackBar;
 import com.rebliss.presenter.helper.Logout;
 import com.rebliss.presenter.helper.Network;
 import com.rebliss.presenter.helper.ShowHintOrText;
 import com.rebliss.presenter.helper.TextUtil;
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

public class ActivitySignup extends AppCompatActivity {

    private static final String TAG = ActivitySignup.class.getSimpleName();
    private Context context;
    private TextView textHeader, textLogin, textStore, textmaditory, textReblissInfo;
    private ImageView icBack;
    private Button btnSignup;
    private EditText etFirstName, etLastName, etNumber, etPassword, etCPassword, etrefferal, etLocationPinCode,etStoreName;
    private Data data;
    private MySingleton mySingleton;
    private Network network;
    private KProgressHUD kProgressHUD;
    private DisplaySnackBar displaySnackBar;
    private Intent intent;
    private String groupId, groupTitle;
    private String mFirstName, mLastName, mNumber, mPassword = "123654", mCPassword = "123654", mReffCode = "",
            mLocationPinCode, mCity, mCityId, mState, mstateId, mOccupation, mAgeRange, mGender, mEducation;
    private Base64Encoded base64Encoded;
    private CheckBox cbStore;
    TextInputLayout etstoreLayout;

    private Spinner spinnerGender, spinnerOccupation, spinnerAgeRange, spinnerEducation;
    String[] genderStringArray = new String[]{
            "Select Option",
            "Male",
            "Female",
            "Transgender"
    };

    String occupation_id;
    String occupation_name, ageGroup_list_name, educationName;

    List<AllGroup> allGroupArrayList;
    List<com.rebliss.domain.model.agegroup.AllGroup> allGroupListAge;
    List<EducationResponse.Data.AllGroup> allGroupListEducation;

    ArrayAdapter<String> allGroupArrayAdapter;
    ArrayList<String> occupationNameArrayList, ageGroupNameArrayList, educationNameArrayList;

    private List<State> stateData;
    private List<City> cityData;
    private TextView txtlabelGender;
    private String occupation_type = "";
    private String fos_type = "";
    private String fos_shop_name = "";
    private String storeName ="";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //so for saathi and mo for merchant
         occupation_type = "so";
        intent = getIntent();
        groupId = intent.getStringExtra(Constant.CHANNEL_GROUP_ID);
        groupTitle = intent.getStringExtra(Constant.CHANNEL_GROUP_NAME);
        Log.i(TAG, "groupId " + groupId);
        Log.i(TAG, "groupTitle " + groupTitle);
        initView();

        viewListener();

        if (network.isNetworkConnected(ActivitySignup.this)) {
            spinnerGender();
            getOccupationApiData();
            getAgeGroup();
            getEducationData();
        }
        else
            {
                displaySnackBar.DisplaySnackBar(Constant.NETWIRK_ERROR, Constant.TYPE_ERROR);
            }
    }


    private void initView() {
        context = ActivitySignup.this;
        mySingleton = new MySingleton(this);
        displaySnackBar = new DisplaySnackBar(this);
        network = new Network();
        base64Encoded = new Base64Encoded(this);
        textHeader = findViewById(R.id.textHeader);
        textLogin = findViewById(R.id.textLogin);
        textReblissInfo = findViewById(R.id.textReblissInfo);

        if (!TextUtil.isStringNullOrBlank(groupTitle)) {
            String title = "";
            if (groupId.equalsIgnoreCase("1")) {
                title = "Sign up partner form";
            } else if (groupId.equalsIgnoreCase("2")) {
                title = "Sign up form"; //changed
            } else if (groupId.equalsIgnoreCase("3")) {
                title = "Sign up Retailer form";
            } else if (groupId.equalsIgnoreCase("4")) {
                title = "Sign up Super CP form";
            }
            textHeader.setText(title);
        }




        textStore = findViewById(R.id.edit_textok);
        textmaditory = findViewById(R.id.textstore_maditory);
        icBack = findViewById(R.id.icBack);
        icBack = findViewById(R.id.icBack);
        btnSignup = findViewById(R.id.btnSignup);
        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etNumber = findViewById(R.id.etNumber);
        etPassword = findViewById(R.id.etPassword);
        etCPassword = findViewById(R.id.etCPassword);
        etrefferal = findViewById(R.id.etrefferal);
        txtlabelGender = findViewById(R.id.txtlabelGender);

        etLocationPinCode = findViewById(R.id.etLocationPinCode);
        spinnerGender = findViewById(R.id.spinnerGender);
        spinnerOccupation = findViewById(R.id.spinnerOccupation);
        spinnerAgeRange = findViewById(R.id.spinnerAgeRange);
        spinnerEducation = findViewById(R.id.spinnerEducation);
        cbStore = findViewById(R.id.cbStore);
        etStoreName = findViewById(R.id.etStore);
        etstoreLayout = findViewById(R.id.etstoreLayout);
        setFontOnView();
        setHint();

        stateData = new ArrayList<>();
    }

    private void setHint() {
/*
        etFirstName.setHint(ShowHintOrText.GetMandatory("\tFirst name"));
        etLastName.setHint(ShowHintOrText.GetMandatory("\tLast name"));
        etNumber.setHint(ShowHintOrText.GetMandatory("\tMobile No."));
        etPassword.setHint(ShowHintOrText.GetMandatory("\tPassword"));
        etCPassword.setHint(ShowHintOrText.GetMandatory("\tConfirm Password"));
        etrefferal.setHint("\tReferral code");
        etStoreName.setHint("\tEnter your store Name");
        etLocationPinCode.setHint(ShowHintOrText.GetMandatory("\tLocation:(Pincode)"));




 */
    }

    private void setFontOnView() {
    /*    textHeader.setTypeface(App.LATO_REGULAR);
        textLogin.setTypeface(App.LATO_REGULAR);
        textReblissInfo.setTypeface(App.LATO_REGULAR);
        btnSignup.setTypeface(App.LATO_REGULAR);
        etFirstName.setTypeface(App.LATO_REGULAR);
        etLastName.setTypeface(App.LATO_REGULAR);
        etNumber.setTypeface(App.LATO_REGULAR);
        etPassword.setTypeface(App.LATO_REGULAR);
        etCPassword.setTypeface(App.LATO_REGULAR);
        etrefferal.setTypeface(App.LATO_REGULAR);

        etLocationPinCode.setTypeface(App.LATO_REGULAR);





     */

    }

    private void viewListener() {
        icBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (network.isNetworkConnected(context)) {
                    if (isFormValidatedWithSweetAlert()) {
                        callSignupService();
                    }
                } else {
                    displaySnackBar.DisplaySnackBar(Constant.NETWIRK_ERROR, Constant.TYPE_ERROR);
                }
            }
        });

        textLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login = new Intent(context, ActivityLogin.class);
                login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(login);
            }
        });


        spinnerOccupation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mOccupation = (String) parent.getItemAtPosition(position);
            }

            public void onNothingSelected(AdapterView<?> parent) {}
        });

        spinnerAgeRange.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mAgeRange = (String) parent.getItemAtPosition(position);
            }

            public void onNothingSelected(AdapterView<?> parent)
            {}
        });

        spinnerEducation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mEducation = (String) parent.getItemAtPosition(position);
            }

            public void onNothingSelected(AdapterView<?> parent) {}
        });


        etLocationPinCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() == 6) {
                    if (network.isNetworkConnected(context)) {
                        getStateCity(editable.toString());
                    }
                    else
                        {
                            displaySnackBar.DisplaySnackBar(Constant.NETWIRK_ERROR, Constant.TYPE_ERROR);
                        }
                }
            }
        });

        cbStore.setOnCheckedChangeListener((buttonView, isChecked) -> {


            if (buttonView.isChecked()) {
                new AlertDialog.Builder(context)
                        .setMessage("You will become merchant after selecting store option")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                etstoreLayout.setVisibility(View.VISIBLE);
                                textStore.setVisibility(View.VISIBLE);
                                textmaditory.setVisibility(View.VISIBLE);
                                occupation_type = "mo";
                                fos_type = "rBM";
                                getOccupationApiData();
                            }
               }).setIcon(android.R.drawable.ic_dialog_alert).show();
            }
            else
            {


                etstoreLayout.setVisibility(View.GONE);
                textStore.setVisibility(View.GONE);
                textmaditory.setVisibility(View.GONE);



                occupation_type = "so";
                fos_type = "rBS";
                etStoreName.setText("");
                getOccupationApiData();             
            }
        });

    }


    private void getStateCity(final String zipCode) {
        kProgressHUD = KProgressHUD.create(context)
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

                            else {
                                showWarningSimpleAlertDialog("Data not found", "Pincode is not available in database");
                                etLocationPinCode.setText("");
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
                                            if (response.body().getData().getCitys().get(i).equals(mCityId)) {
                                                mCity = response.body().getData().getCitys().get(i).getS_name();
                                            }
                                        }
                                    }
                                } else {
                                    displaySnackBar.DisplaySnackBar("Data not found", Constant.TYPE_ERROR);
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
        Log.i(TAG, "Request Data " + mySingleton.getData(Constant.TOKEN_BASE_64));
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
                                            }
                                        }
                                    }
                                } else {
                                    displaySnackBar.DisplaySnackBar("Data not found", Constant.TYPE_ERROR);
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
            public void onFailure(Call<StateResponce> call, Throwable t) {
                if (t != null && (t instanceof IOException || t instanceof SocketTimeoutException
                        || t instanceof ConnectException || t instanceof NoRouteToHostException
                        || t instanceof SecurityException)) {
                    displaySnackBar.DisplaySnackBar(Constant.NETWIRK_ERROR, Constant.TYPE_ERROR);

                }
            }
        });
    }

    private void showSimpleAlertDialog(String message) {
        new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE).setTitleText(Constant.TITLE)
                .setContentText(message)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                        cleardata();
                        saveData(data);
                    }
                })
                .show();
    }

    private void showWarningSimpleAlertDialog(String title, String message) {
        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE).setTitleText(title)
                .setContentText(message)
                .setConfirmClickListener(sweetAlertDialog -> sweetAlertDialog.dismissWithAnimation())
                .show();
    }


    private void cleardata() {

        etFirstName.setText("");
        etLastName.setText("");
        etNumber.setText("");
        etPassword.setText("");
        etCPassword.setText("");
        etrefferal.setText("");

        etLocationPinCode.setText("");
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

    private void saveData(Data signupResponce) {
        mySingleton.saveData(Constant.TOKEN, signupResponce.getToken());
        mySingleton.saveData(Constant.USER_PHONE, mNumber);
        mySingleton.saveData(Constant.USER_FIRST_NAME, mFirstName);
        mySingleton.saveData(Constant.USER_LAST_NAME, mLastName);
        mySingleton.saveData(Constant.USER_GROUP_ID, signupResponce.getUser().getGroup_id());
        mySingleton.saveData(Constant.USER_GROUP_DETAIL_ID, signupResponce.getUser().getGroup_detail_id() + "");
        mySingleton.saveData(Constant.USER_ID, signupResponce.getUser().getUser_id() + "");

        mySingleton.saveData(Constant.USER_LOCATION, mLocationPinCode);
        mySingleton.saveData(Constant.USER_AGR_RANGE, mAgeRange);
        mySingleton.saveData(Constant.USER_OCCUPATION, mOccupation);
        mySingleton.saveData(Constant.USER_GENDER, mGender);
        mySingleton.saveData(Constant.USER_EDUCATION, mEducation);
        mySingleton.saveData(Constant.USER_IID, String.valueOf(signupResponce.getUser().getUser_id()));

        base64Encoded.EncodeToken();
        Intent callOtp = new Intent(context, OtpVerification.class);
        callOtp.putExtra("type", "0");
        callOtp.putExtra(Constant.CHANNEL_GROUP_ID, groupId);
        callOtp.putExtra(Constant.USER_IID, "user_id");
        startActivity(callOtp);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
    private boolean isFormValidatedWithSweetAlert() {
        boolean status = true;
        mFirstName = etFirstName.getText().toString().trim();
        mLastName = etLastName.getText().toString().trim();
        mNumber = etNumber.getText().toString().trim();
        mReffCode = etrefferal.getText().toString().trim();
        mLocationPinCode = etLocationPinCode.getText().toString().trim();
        storeName = etStoreName.getText().toString().trim();


        Log.i("myRefferal", mReffCode);

        if (mLocationPinCode.length() < 6) {
            status = false;
            showWarningSimpleAlertDialog(Constant.TITLE, "Please Enter min 6 digit Pincode");
        } else if (mFirstName.length() <= 0) {
            status = false;
            showWarningSimpleAlertDialog(Constant.TITLE, "Please Enter your first name");
        } else if (mLastName.length() <= 0) {
            status = false;
            showWarningSimpleAlertDialog(Constant.TITLE, "Please Enter your last name");
        } else if (mNumber.length() < 10) {
            status = false;
            showWarningSimpleAlertDialog(Constant.TITLE, getString(R.string.PHONE_VALIDATION));
        } else if (mPassword.length() < 6) {
            status = false;
            showWarningSimpleAlertDialog(Constant.TITLE, getString(R.string.PASSWORD_VALIDATION));
        } else if (mCPassword.length() < 6) {
            status = false;
            showWarningSimpleAlertDialog(Constant.TITLE, getString(R.string.PASSWORD_VALIDATION));
        }


        if (status) {
            if (!mPassword.equalsIgnoreCase(mCPassword)) {
                status = false;
                showWarningSimpleAlertDialog(Constant.TITLE, getString(R.string.MATCH_PASSWORD_VALIDATION));
            }
        }

        if (spinnerGender.getSelectedItem().toString().trim().equals("Select Option")) {
            status = false;
            showWarningSimpleAlertDialog(Constant.TITLE, "Please Select Gender");
        }

        if (spinnerAgeRange.getSelectedItem().toString().trim().equals("Select Option")) {
            status = false;
            showWarningSimpleAlertDialog(Constant.TITLE, "Please Select Age Limit");
        }


        if (spinnerEducation.getSelectedItem().toString().trim().equals("Select Option")) {
            status = false;
            showWarningSimpleAlertDialog(Constant.TITLE, "Please Select Education");
        }
        if(cbStore.isChecked()){

          if(storeName.length()<=0)
          {status = false;
              showWarningSimpleAlertDialog(Constant.TITLE, "Please Enter shop Name");
          }

        }

        if (spinnerOccupation.getSelectedItem().toString().trim().equals("Select Option")) {
            status = false;
            showWarningSimpleAlertDialog(Constant.TITLE, "Please Select Occupation");
        }
        return status;


    }

    public void callSignupService() {

        kProgressHUD = KProgressHUD.create(context)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .setWindowColor(getResources().getColor(R.color.progressbar_color))
                .show();

        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setFirst_name(mFirstName);
        signupRequest.setLast_name(mLastName);
        signupRequest.setPhone_number(mNumber);
        signupRequest.setPassword(mPassword);
        signupRequest.setConfirm_password(mCPassword);
        signupRequest.setDevice_type("android");
        signupRequest.setDevice_id(mySingleton.getData(Constant.DEVICE_FCM_TOKEN));
        signupRequest.setAge_range(mAgeRange);
        signupRequest.setOccupation(mOccupation);
        signupRequest.setCode(mReffCode);
        signupRequest.setGender(mGender);
        signupRequest.setLocation_zipcode(mLocationPinCode);
        signupRequest.setLocation_city(mCity);
        signupRequest.setLocation_state(mState);
        signupRequest.setEducation(mEducation);
        signupRequest.setFos_type(fos_type); //TODO rbs
        signupRequest.setFos_shop_name(storeName);

        Gson gson = new Gson();
        String json = gson.toJson(signupRequest, SignupRequest.class);
        Log.i(TAG, "json " + json);

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        final Call<SignupResponce> call = apiService.postUserSignup(signupRequest);
        call.enqueue(new Callback<SignupResponce>() {
            @Override
            public void onResponse(Call<SignupResponce> call, Response<SignupResponce> response) {
                kProgressHUD.dismiss();
                if (response.isSuccessful()) {
                    if (response.code() >= 200 && response.code() < 700) {
                        if (response.code() == 200) {
                            if (response.body().getStatus() == 1) {
                                if (response.body().getData() != null) {
                                    Gson gson = new Gson();
                                    String json = gson.toJson(response.body(), SignupResponce.class);
                                    Log.i(TAG, "json " + json);
                                    data = response.body().getData();
                                    String shop_name = response.body().getData().getUser().getShop_name();
                                    if(shop_name!=null) {
                                        mySingleton.saveData("shop_names", shop_name);
                                    }

                                    showSimpleAlertDialog(response.body().getData().getMessage());
                                } else {
                                    displaySnackBar.DisplaySnackBar("Data not found", Constant.TYPE_ERROR);
                                }
                            } else if (response.body().getStatus() == 0) {
                                if (response.body().getData().getError_message() != null)
                                    showWarningSimpleAlertDialog(Constant.TITLE, response.body().getData().getError_message().get(0));
                            }
                            callDisplayErrorCode(response.code(), "");
                        }

                    } else {
                        displaySnackBar.DisplaySnackBar(Constant.UNEXPECTED, Constant.TYPE_ERROR);
                        Log.e(TAG, "onResponse: else parttttttt" );

                    }
                } else {

                    try {
                        ErrorBody errorBody = new ErrorBody();
                        Gson gson = new Gson();
                        errorBody = gson.fromJson(response.errorBody().string(), ErrorBody.class);
                        callDisplayErrorCode(Integer.parseInt(errorBody.getStatus()), errorBody.getMessage());
                        displaySnackBar.DisplaySnackBar(errorBody.getMessage(), Constant.TYPE_ERROR);
                        Log.e(TAG, "onResponse: "+errorBody.getMessage() );
                        if (errorBody.getName().equalsIgnoreCase(context.getString(R.string.unAuthorisedUser))) {
                            Logout.Login(context);
                        }
                    } catch (Exception e) {
                        displaySnackBar.DisplaySnackBar(Constant.ERROR_INVALID_JSON, Constant.TYPE_ERROR);
                        Log.e(TAG, "onResponse: "+e.getMessage() );
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<SignupResponce> call, Throwable t) {
                Log.e(TAG, "onFailure message: "+t.getMessage() );
                kProgressHUD.dismiss();
                if (t != null && (t instanceof IOException || t instanceof SocketTimeoutException
                        || t instanceof ConnectException || t instanceof NoRouteToHostException
                        || t instanceof SecurityException)) {
                    displaySnackBar.DisplaySnackBar(Constant.NETWIRK_ERROR, Constant.TYPE_ERROR);

                }
            }
        });
    }
    private void spinnerGender() {
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this, R.layout.spinner_item_gender, genderStringArray) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                    tv.setTypeface(App.LATO_REGULAR);
                } else {
                    tv.setTextColor(Color.BLACK);
                    tv.setTypeface(App.LATO_REGULAR);

                }
                return view;
            }
        };
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item_gender);
        spinnerGender.setAdapter(spinnerArrayAdapter);
        spinnerGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mGender = (String) parent.getItemAtPosition(position);

                if (mGender.contains("Male")) {
                    mGender = "M";
                } else if (mGender.contains("Female")) {
                    mGender = "F";
                } else {
                    mGender = "O";
                }
                if (position > 0) {
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void getOccupationApiData() {

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<OccupationResponse> responseCall = apiService.getOccupationDATA(occupation_type);

        responseCall.enqueue(new Callback<OccupationResponse>() {
            @Override
            public void onResponse(Call<OccupationResponse> call, Response<OccupationResponse> response) {
                if (response.isSuccessful()) {
                    if (response.code() >= 200 && response.code() < 700) {

                        if (response.body() != null) {

                            if (response.body().getStatus() == 1) {

                              
                                if (response.body().getData() != null && response.body().getData().getAllGroups().size() > 0) {


                                    allGroupArrayList = response.body().getData().getAllGroups();
                                    occupationNameArrayList = new ArrayList<>();

                                    for (int i = 0; i < allGroupArrayList.size(); i++) {
                                        occupation_name = response.body().getData().getAllGroups().get(i).getName();
                                        occupationNameArrayList.add(occupation_name);
                                    }

                                    occupationNameArrayList.add(0, "Select Option");

                                    allGroupArrayAdapter = new ArrayAdapter<String>(ActivitySignup.this,
                                            R.layout.spinner_item_gender, occupationNameArrayList) {
                                        @Override
                                        public boolean isEnabled(int position) {
                                            if (position == 0) {
                                                return false;
                                            } else {
                                                return true;
                                            }
                                        }

                                        @Override
                                        public View getDropDownView(int position, View convertView,
                                                                    ViewGroup parent) {
                                            View view = super.getDropDownView(position, convertView, parent);
                                            TextView tv = (TextView) view;
                                            if (position == 0) {
                                                tv.setTextColor(Color.GRAY);
                                                tv.setTypeface(App.LATO_REGULAR);
                                            } else {
                                                tv.setTextColor(Color.BLACK);
                                                tv.setTypeface(App.LATO_REGULAR);
                                            }
                                            return view;
                                        }
                                    };
                                    allGroupArrayAdapter.setDropDownViewResource(R.layout.spinner_item_gender);
                                    spinnerOccupation.setAdapter(allGroupArrayAdapter);
                                }


                            } else if (response.body().getStatus() == 401) {

                            }

                        }
                    }
                }

            }

            @Override
            public void onFailure(Call<OccupationResponse> call, Throwable t) {

                if ((t instanceof IOException || t instanceof SocketTimeoutException
                        || t instanceof ConnectException || t instanceof NoRouteToHostException
                        || t instanceof SecurityException)) {

                }
            }
        });
    }

    private void getAgeGroup() {

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<AgeGroupResponse> responseCall = apiService.getAgeGroupDATA();

        responseCall.enqueue(new Callback<AgeGroupResponse>() {
            @Override
            public void onResponse(Call<AgeGroupResponse> call, Response<AgeGroupResponse> response) {
                if (response.isSuccessful()) {
                    if (response.code() >= 200 && response.code() < 700) {

                        if (response.body() != null) {

                            if (response.body().getStatus() == 1) {

                                if (response.body().getData() != null && response.body().getData().getAllGroups().size() > 0) {

                                    allGroupListAge = response.body().getData().getAllGroups();
                                    ageGroupNameArrayList = new ArrayList<>();

                                    for (int i = 0; i < allGroupListAge.size(); i++) {
                                        ageGroup_list_name = response.body().getData().getAllGroups().get(i).getName();

                                        ageGroupNameArrayList.add(ageGroup_list_name);

                                    }
                                    ageGroupNameArrayList.add(0, "Select Option");

                                    allGroupArrayAdapter = new ArrayAdapter<String>(ActivitySignup.this,
                                            R.layout.spinner_item_gender, ageGroupNameArrayList) {
                                        @Override
                                        public boolean isEnabled(int position) {
                                            if (position == 0) {
                                                return false;
                                            } else {
                                                return true;
                                            }
                                        }

                                        @Override
                                        public View getDropDownView(int position, View convertView,
                                                                    ViewGroup parent) {
                                            View view = super.getDropDownView(position, convertView, parent);
                                            TextView tv = (TextView) view;
                                            if (position == 0) {
                                                // Set the hint text color gray
                                                tv.setTextColor(Color.GRAY);
                                                tv.setTypeface(App.LATO_REGULAR);
                                            } else {
                                                tv.setTextColor(Color.BLACK);
                                                tv.setTypeface(App.LATO_REGULAR);
                                            }
                                            return view;
                                        }
                                    };
                                    allGroupArrayAdapter.setDropDownViewResource(R.layout.spinner_item_gender);
                                    spinnerAgeRange.setAdapter(allGroupArrayAdapter);
                                }


                            } else if (response.body().getStatus() == 401) {
//                                loge
                            }

                        }


                    }
                }

            }

            @Override
            public void onFailure(Call<AgeGroupResponse> call, Throwable t) {
                if ((t instanceof IOException || t instanceof SocketTimeoutException
                        || t instanceof ConnectException || t instanceof NoRouteToHostException
                        || t instanceof SecurityException)) {

                }
            }
        });
    }

    private void getEducationData() {

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<EducationResponse> responseCall = apiService.getEducationData();

        responseCall.enqueue(new Callback<EducationResponse>() {
            @Override
            public void onResponse(Call<EducationResponse> call, Response<EducationResponse> response) {

                if (response.isSuccessful()) {
                    if (response.code() >= 200 && response.code() < 700) {

                        if (response.body() != null) {

                            if (response.body().getStatus() == 1) {

                                if (response.body().getData() != null && response.body().getData().getAllGroups().size() > 0) {

                                    allGroupListEducation = response.body().getData().getAllGroups();
                                    educationNameArrayList = new ArrayList<>();

                                    for (int i = 0; i < allGroupListEducation.size(); i++) {
                                        educationName = allGroupListEducation.get(i).getEducationName();
                                        educationNameArrayList.add(educationName);
                                    }
                                    educationNameArrayList.add(0, "Select Option");

                                    allGroupArrayAdapter = new ArrayAdapter<String>(ActivitySignup.this,
                                            R.layout.spinner_item_gender, educationNameArrayList) {
                                        @Override
                                        public boolean isEnabled(int position) {
                                            if (position == 0) {
                                                return false;
                                            } else {
                                                return true;
                                            }
                                        }

                                        @Override
                                        public View getDropDownView(int position, View convertView,
                                                                    ViewGroup parent) {
                                            View view = super.getDropDownView(position, convertView, parent);
                                            TextView tv = (TextView) view;
                                            if (position == 0) {
                                                tv.setTextColor(Color.GRAY);
                                                tv.setTypeface(App.LATO_REGULAR);
                                            } else {
                                                tv.setTextColor(Color.BLACK);
                                                tv.setTypeface(App.LATO_REGULAR);
                                            }
                                            return view;
                                        }
                                    };
                                    allGroupArrayAdapter.setDropDownViewResource(R.layout.spinner_item_gender);
                                    spinnerEducation.setAdapter(allGroupArrayAdapter);
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<EducationResponse> call, Throwable t) {
                Log.e(TAG, "onFailure: "+t.getMessage() );
            }
        });
    }


}
