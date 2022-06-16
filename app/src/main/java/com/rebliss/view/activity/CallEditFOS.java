package com.rebliss.view.activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.gson.Gson;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.rebliss.utils.App;
import com.rebliss.R;
import com.rebliss.data.perf.MySingleton;
import com.rebliss.domain.constant.Constant;
import com.rebliss.domain.model.ErrorBody;
import com.rebliss.domain.model.city.City;
import com.rebliss.domain.model.city.CityResponce;
import com.rebliss.domain.model.editprofile.Business;
import com.rebliss.domain.model.editprofile.Communication;
import com.rebliss.domain.model.editprofile.EditProfileRequest;
import com.rebliss.domain.model.editprofile.EditProfileResponce;
import com.rebliss.domain.model.logout.LogoutResponce;
import com.rebliss.domain.model.profile.Data;
import com.rebliss.domain.model.profile.ProfileResponce;
import com.rebliss.domain.model.searchstate.SearchStateResponce;
import com.rebliss.domain.model.state.State;
import com.rebliss.domain.model.state.StateResponce;
import com.rebliss.presenter.helper.CallAppVersion;
import com.rebliss.presenter.helper.DisplaySnackBar;
import com.rebliss.presenter.helper.Logout;
import com.rebliss.presenter.helper.Network;
import com.rebliss.presenter.helper.ShowHintOrText;
import com.rebliss.presenter.helper.TextUtil;
import com.rebliss.presenter.retrofit.ApiClient;
import com.rebliss.presenter.retrofit.ApiInterface;
import com.rebliss.view.adapter.CitySpinnerAdapter;
import com.rebliss.view.adapter.StateSpinnerAdapter;
import java.io.IOException;
import java.net.ConnectException;
import java.net.NoRouteToHostException;
import java.net.SocketTimeoutException;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CallEditFOS extends AppCompatActivity {
    private static final String TAG = CallEditFOS.class.getSimpleName();
    private Context context;
    private TextView textChName, textChLName, textMobile, textDOB, textpersonalEmail, textOfficialEmail,
            textFirmName, textCommunicationAddress, textBusinessAddress, textHeader, textmarital, textChFatherName;
    private ImageView imgLogout, imgCal;
    private Button btnSave;
    private EditText etChName, etChLName, etMobile, etDOB,
            etFirmName, etCommunicationAddress, etBusinessAddress, etBusinessLandmark;
    private EditText etCommunicationDistrict, etpersonalEmail, etCommunicationPIN, etCommunicationLandmark;
    private EditText etBusinessDistrict, etBusinessPIN, etChFatherName, etOfficialEmail;
    boolean isCommunicationPinNew = true;
    boolean isBussinessPinNew = true;
    ArrayAdapter<String> adapter, adapterMarride;
    private MySingleton mySingleton;
    private Network network;
    private KProgressHUD kProgressHUD;
    private DisplaySnackBar displaySnackBar;
    public static Data profileData;
    private final String sChName = "";
    private final String sChLName = "";
    private String sMobile = "";
    private String sDOB = "";
    private final String sLandline = "";
    private String spersonalEmail = "";
    private String sOfficialEmail = ", ";
    private String sFirmName = "";
    private String sCommunicationAddress = "";
    private String sBusinessAddress = "";
    private String sChFatherName = "";
    private String sCommunicationState = "", sCommunicationDistrict = "", sCommunicationCity = "", sCommunicationPIN = "", sBusinessState = "",
            sBusinessDistrict = "", sBusinessCity = "", sBusinessPIN = "", sCommunicationLandmark = "", sBusinessLandmark = "";
    private int Day, Month, Year;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private Spinner spinnerSexButton, signup_marital_spinner;
    private ArrayList<String> listSex;
    private ArrayList<String> listMaritalStatus;
    private String sex = "Male", maritalstatus = "Married";
    private TextView textStepOne, textStepTwo, textStepThree, textStepFour;
    private ImageView imgStepOne, imgStepTwo, imgStepThree, imgStepFour;
    private View viewStepOne, viewStepTwo, viewStepThree;

    private List<State> stateData;
    private List<City> cityData, citydataBusiness;

    Spinner spCommunicationState, spCommunicationCity, spBusinessState, spBusinessCity;
    private int spCommunicationStatePosition = 0, spCommunicationCityPosition = 0, spBusinessStatePosition = 0, spBusinessCityPosition = 0;
    StateSpinnerAdapter stateSpinnerAdapter, stateSpinnerAdapterBusiness;
    com.rebliss.view.adapter.CitySpinnerAdapter CitySpinnerAdapter;
    com.rebliss.view.adapter.CitySpinnerAdapter CitySpinnerAdapterBusiness;
    private Intent intent;
    private String callFrom = "0";

    private RadioButton radioBusiness;
    private boolean isRadioChecked = false;
    private LinearLayout liRadio, liBusiness;
    private Button findPin, findBusinessPin;
    ImageView icBack;
    LinearLayout lNameLi;
    TextView mywidget;
    String savedLocalLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_fos);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        CallAppVersion callAppVersion = new CallAppVersion();
        callAppVersion.CallAppVersion(CallEditFOS.this);
        initView();
        try {
            intent = getIntent();
            callFrom = intent.getStringExtra("call_from");
        } catch (Exception e) {

            callFrom = "0";
            e.printStackTrace();
        }

        savedLocalLocation = mySingleton.getData(Constant.USER_LOCATION);
        etCommunicationPIN.setText((!TextUtil.isStringNullOrBlank(savedLocalLocation) ? savedLocalLocation : ""));

        textViewByPin();

    }

    @Override
    protected void onResume() {
        super.onResume();
        disable();

        if (network.isNetworkConnected(context)) {
            getState();
        } else {
            displaySnackBar.DisplaySnackBar(Constant.NETWIRK_ERROR, Constant.TYPE_ERROR);
        }
        viewListener();
        textViewByPin();

        if (network.isNetworkConnected(context)) {
            callProfileAPI();
        } else {
            displaySnackBar.DisplaySnackBar(Constant.NETWIRK_ERROR, Constant.TYPE_ERROR);
        }


    }

    private void showDataOnViewOnFindCommuicationPIN() {


        mySingleton.saveData(Constant.USER_PROFILE_VERIFYED, profileData.getProfile_verified());
        if (profileData.getCommunication() != null) {
            etCommunicationAddress.setText((!TextUtil.isStringNullOrBlank(profileData.getCommunication().getAddress()) ? profileData.getCommunication().getAddress() : ""));
            etCommunicationDistrict.setText((!TextUtil.isStringNullOrBlank(profileData.getCommunication().getDistrict()) ? profileData.getCommunication().getDistrict() : ""));
            isCommunicationPinNew = false;
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
        }
    }

    private void showDataOnViewOnFindBusinessnPIN() {

        if (profileData.getBussiness() != null) {
            etBusinessAddress.setText((!TextUtil.isStringNullOrBlank(profileData.getBussiness().getAddress()) ? profileData.getBussiness().getAddress() : ""));
            etBusinessDistrict.setText((!TextUtil.isStringNullOrBlank(profileData.getBussiness().getDistrict()) ? profileData.getBussiness().getDistrict() : ""));
            etBusinessDistrict.setText((!TextUtil.isStringNullOrBlank(profileData.getBussiness().getDistrict()) ? profileData.getBussiness().getDistrict() : ""));
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
        }


    }
    private void disable() {
        if (!TextUtil.isStringNullOrBlank(callFrom)) {
            if (callFrom.equalsIgnoreCase("1")) {




                btnSave.setText("Next");
            }
        }
    }

    private void initView() {

        stateData = new ArrayList<>();
        State state = new State();
        state.setId("-1");
        state.setS_name("Select State");
        stateData.add(state);

        listSex = new ArrayList<>();
        listSex.add("Male");
        listSex.add("Female");
        listMaritalStatus = new ArrayList<>();
        listMaritalStatus.add("Married");
        listMaritalStatus.add("Unmarried");
        listMaritalStatus.add("divorced");
        listMaritalStatus.add("widowed");


        context = CallEditFOS.this;
        mySingleton = new MySingleton(this);
        displaySnackBar = new DisplaySnackBar(this);
        network = new Network();


        icBack = findViewById(R.id.icBack);
        mywidget = findViewById(R.id.mywidget);
        lNameLi = findViewById(R.id.lNameLi);
        textStepOne = findViewById(R.id.textStepOne);
        textStepTwo = findViewById(R.id.textStepTwo);
        textStepOne.setTypeface(App.LATO_REGULAR);
        textStepTwo.setTypeface(App.LATO_REGULAR);

        textHeader = findViewById(R.id.textHeader);
        textChFatherName = findViewById(R.id.textChFatherName);
        etChFatherName = findViewById(R.id.etChFatherName);
        textChName = findViewById(R.id.textChName);
        textMobile = findViewById(R.id.textMobile);
        textDOB = findViewById(R.id.textDOB);
        textpersonalEmail = findViewById(R.id.textpersonalEmail);
        textOfficialEmail = findViewById(R.id.textOfficialEmail);
        textFirmName = findViewById(R.id.textFirmName);
        textCommunicationAddress = findViewById(R.id.textCommunicationAddress);
        textBusinessAddress = findViewById(R.id.textBusinessAddress);
        textChLName = findViewById(R.id.textChLName);

        imgLogout = findViewById(R.id.imgLogout);
        imgCal = findViewById(R.id.imgCal);
        imgLogout.setVisibility(View.GONE);

        btnSave = findViewById(R.id.btnSave);

        etChName = findViewById(R.id.etChName);
        etMobile = findViewById(R.id.etMobile);
        etDOB = findViewById(R.id.etDOB);
        etOfficialEmail = findViewById(R.id.etOfficialEmail);
        etpersonalEmail = findViewById(R.id.etpersonalEmail);
        etCommunicationPIN = findViewById(R.id.etCommunicationPIN);
        etFirmName = findViewById(R.id.etFirmName);
        etCommunicationAddress = findViewById(R.id.etCommunicationAddress);
        etBusinessAddress = findViewById(R.id.etBusinessAddress);
        etChLName = findViewById(R.id.etChLName);
        spCommunicationState = findViewById(R.id.etCommunicationState);
        etCommunicationDistrict = findViewById(R.id.etCommunicationDistrict);
        spCommunicationCity = findViewById(R.id.etCommunicationCity);
        spBusinessState = findViewById(R.id.etBusinessState);
        etBusinessDistrict = findViewById(R.id.etBusinessDistrict);
        spBusinessCity = findViewById(R.id.etBusinessCity);
        etBusinessPIN = findViewById(R.id.etBusinessPIN);
        etCommunicationLandmark = findViewById(R.id.etCommunicationLandmark);
        etBusinessLandmark = findViewById(R.id.etBusinessLandmark);

        liBusiness = findViewById(R.id.liBusiness);
        radioBusiness = findViewById(R.id.radioBusiness);
        liRadio = findViewById(R.id.liRadio);
        radioBusiness.setChecked(isRadioChecked);

        findPin = findViewById(R.id.findPin);
        findBusinessPin = findViewById(R.id.findBusinessPin);

        spinnerSexButton = findViewById(R.id.signup_gender_spinner);
        signup_marital_spinner = findViewById(R.id.signup_marital_spinner);

        adapter = new ArrayAdapter<>(CallEditFOS.this, R.layout.custom_list2, R.id.or_item_tv2, listSex);
        spinnerSexButton.setAdapter(adapter);

        adapterMarride = new ArrayAdapter<>(CallEditFOS.this, R.layout.custom_list2, R.id.or_item_tv2, listMaritalStatus);
        signup_marital_spinner.setAdapter(adapterMarride);

        final Calendar c = Calendar.getInstance();

        setFontOnView();

        setHint();

    }

    private void setHint() {

        textChName.setText(ShowHintOrText.GetMandatory("Name"));
        textMobile.setText(ShowHintOrText.GetMandatory("Mobile No"));
        textpersonalEmail.setText(ShowHintOrText.GetMandatory("Email"));
        textOfficialEmail.setText(ShowHintOrText.GetOptional("Emergency Person's Contact No"));
        textFirmName.setText(ShowHintOrText.GetOptional("Emergency Contact Person Name"));
        textCommunicationAddress.setText(ShowHintOrText.GetMandatory("Resident Address"));
        textBusinessAddress.setText(ShowHintOrText.GetMandatory("Permanent Address"));
        etDOB.setHint(ShowHintOrText.GetMandatory("Date of birth"));
        etCommunicationPIN.setHint(ShowHintOrText.GetMandatory("PIN Code"));

        etCommunicationAddress.setHint(ShowHintOrText.GetMandatory("Locality, Area or Street"));
        etCommunicationDistrict.setHint(ShowHintOrText.GetMandatory("Flat no, Building name"));

        etCommunicationLandmark.setHint(ShowHintOrText.GetOptional("Landmark"));

        etBusinessPIN.setHint(ShowHintOrText.GetMandatory("PIN Code"));
        etBusinessAddress.setHint(ShowHintOrText.GetMandatory("Locality, Area or Street"));
        etBusinessDistrict.setHint(ShowHintOrText.GetMandatory("Flat no, Building name"));
        etBusinessLandmark.setHint(ShowHintOrText.GetOptional("Landmark"));


    }

    private void setFontOnView() {
        mywidget.setTypeface(App.LATO_REGULAR);
        findPin.setTypeface(App.LATO_REGULAR);
        findBusinessPin.setTypeface(App.LATO_REGULAR);
        textHeader.setTypeface(App.LATO_REGULAR);
        textChName.setTypeface(App.LATO_REGULAR);
        textMobile.setTypeface(App.LATO_REGULAR);
        textDOB.setTypeface(App.LATO_REGULAR);
        etChFatherName.setTypeface(App.LATO_REGULAR);
        textpersonalEmail.setTypeface(App.LATO_REGULAR);
        textOfficialEmail.setTypeface(App.LATO_REGULAR);
        textChFatherName.setTypeface(App.LATO_REGULAR);
        textFirmName.setTypeface(App.LATO_REGULAR);
        textCommunicationAddress.setTypeface(App.LATO_REGULAR);
        textBusinessAddress.setTypeface(App.LATO_REGULAR);
        textChLName.setTypeface(App.LATO_REGULAR);

        etpersonalEmail.setTypeface(App.LATO_REGULAR);
        etChName.setTypeface(App.LATO_REGULAR);
        etMobile.setTypeface(App.LATO_REGULAR);
        etChLName.setTypeface(App.LATO_REGULAR);
        etDOB.setTypeface(App.LATO_REGULAR);
        etOfficialEmail.setTypeface(App.LATO_REGULAR);

        etpersonalEmail.setTypeface(App.LATO_REGULAR);
        etFirmName.setTypeface(App.LATO_REGULAR);
        etCommunicationAddress.setTypeface(App.LATO_REGULAR);
        etBusinessAddress.setTypeface(App.LATO_REGULAR);
        etCommunicationDistrict.setTypeface(App.LATO_REGULAR);
        etCommunicationPIN.setTypeface(App.LATO_REGULAR);
        etBusinessDistrict.setTypeface(App.LATO_REGULAR);
        etBusinessPIN.setTypeface(App.LATO_REGULAR);

        btnSave.setTypeface(App.LATO_REGULAR);

        radioBusiness.setTypeface(App.LATO_REGULAR);
        etCommunicationLandmark.setTypeface(App.LATO_REGULAR);
        etBusinessLandmark.setTypeface(App.LATO_REGULAR);

    }

    private void showDataOnView() {


        mySingleton.saveData(Constant.USER_PROFILE_VERIFYED, profileData.getProfile_verified());
        if (profileData.getProfile_verified().equalsIgnoreCase("0") ||
                (profileData.getProfile_verified().equalsIgnoreCase("2"))) {
            if ((!TextUtil.isStringNullOrBlank(profileData.getDecline_message()))) {
                mywidget.setVisibility(View.VISIBLE);
                mywidget.setText((!TextUtil.isStringNullOrBlank(profileData.getDecline_message()) ? profileData.getDecline_message() : ""));
            } else {
                mywidget.setVisibility(View.GONE);
            }
        } else {
            mywidget.setVisibility(View.GONE);
        }

        etChName.setText((!TextUtil.isStringNullOrBlank(profileData.getFirst_name()) ? profileData.getFirst_name() : "") + " " +
                (!TextUtil.isStringNullOrBlank(profileData.getLast_name()) ? profileData.getLast_name() : ""));

        etChFatherName.setText((!TextUtil.isStringNullOrBlank(profileData.getPos_father_name()) ? profileData.getPos_father_name() : ""));
        etMobile.setText((!TextUtil.isStringNullOrBlank(profileData.getPhone_number()) ? profileData.getPhone_number() : ""));
        if (!TextUtil.isStringNullOrBlank(profileData.getDob())) {
            Log.i("DateString", profileData.getDob());
            String[] dateformat = profileData.getDob().split("-");
            mYear = Integer.parseInt(dateformat[0]);
            mMonth = Integer.parseInt(dateformat[1]);
            mDay = Integer.parseInt(dateformat[2]);
            sDOB = mYear + "-" + checkDigit(mMonth) + "-" + checkDigit(mDay);
            etDOB.setText(checkDigit(mDay) + "-" + getMonthForInt(mMonth - 1) + "-" + mYear);
        }

        etOfficialEmail.setText((!TextUtil.isStringNullOrBlank(profileData.getEm_person_contact_no()) ? profileData.getEm_person_contact_no() : ""));
        etpersonalEmail.setText((!TextUtil.isStringNullOrBlank(profileData.getPersonal_email_id()) ? profileData.getPersonal_email_id() : ""));
        etFirmName.setText((!TextUtil.isStringNullOrBlank(profileData.getEm_contact_person_name()) ? profileData.getEm_contact_person_name() : ""));
        if (profileData.getCommunication() != null) {
            etCommunicationAddress.setText((!TextUtil.isStringNullOrBlank(profileData.getCommunication().getAddress()) ? profileData.getCommunication().getAddress() : ""));
            etCommunicationDistrict.setText((!TextUtil.isStringNullOrBlank(profileData.getCommunication().getDistrict()) ? profileData.getCommunication().getDistrict() : ""));
            isCommunicationPinNew = false;
            etCommunicationPIN.setText((!TextUtil.isStringNullOrBlank(profileData.getCommunication().getPincode()) ? profileData.getCommunication().getPincode() : ""));

            isCommunicationPinNew = true;
            etCommunicationLandmark.setText((!TextUtil.isStringNullOrBlank(profileData.getCommunication().getLand_mark()) ? profileData.getCommunication().getLand_mark() : ""));

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
        }
        if (profileData.getBussiness() != null) {
            etBusinessAddress.setText((!TextUtil.isStringNullOrBlank(profileData.getBussiness().getAddress()) ? profileData.getBussiness().getAddress() : ""));
            etBusinessDistrict.setText((!TextUtil.isStringNullOrBlank(profileData.getBussiness().getDistrict()) ? profileData.getBussiness().getDistrict() : ""));
            etBusinessDistrict.setText((!TextUtil.isStringNullOrBlank(profileData.getBussiness().getDistrict()) ? profileData.getBussiness().getDistrict() : ""));
            isBussinessPinNew = false;
            etBusinessPIN.setText((!TextUtil.isStringNullOrBlank(profileData.getBussiness().getPincode()) ? profileData.getBussiness().getPincode() : ""));
            isBussinessPinNew = true;
            etBusinessLandmark.setText((!TextUtil.isStringNullOrBlank(profileData.getBussiness().getLand_mark()) ? profileData.getBussiness().getLand_mark() : ""));

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

        }

    }

    private void displaySateList() {
        stateSpinnerAdapter = new StateSpinnerAdapter(context, stateData);
        spCommunicationState.setAdapter(stateSpinnerAdapter);
        spCommunicationState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spCommunicationStatePosition = position;
                if (spCommunicationStatePosition != 0) {
                    if (network.isNetworkConnected(context)) {
                        getCity();
                    } else {
                        displaySnackBar.DisplaySnackBar(Constant.NETWIRK_ERROR, Constant.TYPE_ERROR);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spCommunicationState.setSelection(spCommunicationStatePosition);

    }

    private void displayCityList() {
        CitySpinnerAdapter = new CitySpinnerAdapter(context, cityData);
        spCommunicationCity.setAdapter(CitySpinnerAdapter);
        spCommunicationCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spCommunicationCityPosition = position;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        if (profileData != null) {
            if (profileData.getCommunication() != null) {
                if (!TextUtil.isStringNullOrBlank(profileData.getCommunication().getCity())) {
                    if (cityData != null) {
                        for (int i = 0; i < cityData.size(); i++) {
                            if (profileData.getCommunication().getCity().equalsIgnoreCase(cityData.get(i).getId())) {
                                spCommunicationCityPosition = i;
                            }
                        }
                    }
                }
            }

        }
        spCommunicationCity.setSelection(spCommunicationCityPosition);
    }

    private void displaySateListBusiness() {

        stateSpinnerAdapterBusiness = new StateSpinnerAdapter(context, stateData);
        spBusinessState.setAdapter(stateSpinnerAdapterBusiness);
        spBusinessState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spBusinessStatePosition = position;
                if (spBusinessStatePosition != 0) {
                    if (network.isNetworkConnected(context)) {
                        getCityBusiness();
                    } else {
                        displaySnackBar.DisplaySnackBar(Constant.NETWIRK_ERROR, Constant.TYPE_ERROR);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spBusinessState.setSelection(spBusinessStatePosition);

    }

    private void displayCityListBusiness() {
        CitySpinnerAdapterBusiness = new CitySpinnerAdapter(context, citydataBusiness);
        spBusinessCity.setAdapter(CitySpinnerAdapterBusiness);
        spBusinessCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spBusinessCityPosition = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if (profileData != null) {
            if (profileData.getBussiness() != null) {
                if (!TextUtil.isStringNullOrBlank(profileData.getBussiness().getCity())) {
                    if (citydataBusiness != null) {
                        for (int i = 0; i < citydataBusiness.size(); i++) {
                            if (profileData.getBussiness().getCity().equalsIgnoreCase(citydataBusiness.get(i).getId())) {
                                spBusinessCityPosition = i;
                            }
                        }

                    }
                }
            }
        }
        spBusinessCity.setSelection(spBusinessCityPosition);
    }

    private void callEnableDisableBusinness(boolean isRadioChecked) {

        if (isRadioChecked) {
            liBusiness.setVisibility(View.GONE);
        } else {
            liBusiness.setVisibility(View.VISIBLE);
        }
        etBusinessAddress.setEnabled(!isRadioChecked);
        spBusinessState.setEnabled(!isRadioChecked);
        etBusinessDistrict.setEnabled(!isRadioChecked);
        spBusinessCity.setEnabled(!isRadioChecked);
        etBusinessPIN.setEnabled(!isRadioChecked);
        etBusinessLandmark.setEnabled(!isRadioChecked);

        etBusinessAddress.setClickable(!isRadioChecked);
        spBusinessState.setClickable(!isRadioChecked);
        etBusinessDistrict.setClickable(!isRadioChecked);
        spBusinessCity.setClickable(!isRadioChecked);
        etBusinessPIN.setClickable(!isRadioChecked);
        etBusinessLandmark.setClickable(!isRadioChecked);

    }

    private void viewListener() {
        icBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        liRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isRadioChecked = !isRadioChecked;
                callEnableDisableBusinness(isRadioChecked);
                radioBusiness.setChecked(isRadioChecked);
            }
        });
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
        findBusinessPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (network.isNetworkConnected(context)) {
                    sBusinessPIN = etBusinessPIN.getText() + "";
                    if (sBusinessPIN.length() != 6) {
                        Toast.makeText(context, "Business Pin is not valid", Toast.LENGTH_SHORT).show();
                    } else {
                        if (profileData.getBussiness() == null) {
                            profileData.setBussiness(new Business());
                        }
                        profileData.getBussiness().setPincode(sBusinessPIN);
                        callBusinessStateSearchAPI();
                    }
                } else {
                    displaySnackBar.DisplaySnackBar(Constant.NETWIRK_ERROR, Constant.TYPE_ERROR);
                }
            }
        });
        findPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (network.isNetworkConnected(context)) {
                    sCommunicationPIN = etCommunicationPIN.getText() + "";
                    if (sCommunicationPIN.length() != 6) {
                        Toast.makeText(context, "Pin is not valid", Toast.LENGTH_SHORT).show();
                    } else {
                        if (profileData.getCommunication() == null) {
                            profileData.setCommunication(new Communication());
                        }
                        profileData.getCommunication().setPincode(sCommunicationPIN);
                        callStateSearchAPI();
                    }
                } else {
                    displaySnackBar.DisplaySnackBar(Constant.NETWIRK_ERROR, Constant.TYPE_ERROR);
                }
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isFormValidated()) {
                    if (network.isNetworkConnected(context)) {
                        callUpdateProfile();
                    } else {
                        displaySnackBar.DisplaySnackBar(Constant.NETWIRK_ERROR, Constant.TYPE_ERROR);
                    }
                }
            }
        });
        imgCal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();

                Date nextYear = c.getTime();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                c.set(mYear - 18, mMonth, mDay);
                DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                Log.i("Date", dayOfMonth + "-" + (monthOfYear) + "-" + year);

                                Day = dayOfMonth;
                                Month = monthOfYear;
                                Year = year;
                                sDOB = Year + "-" + checkDigit(Month + 1) + "-" + checkDigit(Day);
                                etDOB.setText(checkDigit(Day) + "-" + getMonthForInt(Month) + "-" + Year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMaxDate(c.getTimeInMillis());
                datePickerDialog.show();
            }
        });
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
                        String token = mySingleton.getData(Constant.DEVICE_FCM_TOKEN);
                        mySingleton.clearData();
                        mySingleton.saveData(Constant.DEVICE_FCM_TOKEN, token);
                        login.putExtra(Constant.UNAUTHORISE_TOKEN, "0");
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


    private void callNextStep(EditProfileRequest editProfileRequest1) {
        Intent stepSecond = new Intent(context, DashboardFOSDocUpload.class);
        if (!TextUtil.isStringNullOrBlank(callFrom)) {
            if (callFrom.equalsIgnoreCase("1")) {
                stepSecond.putExtra("call_from", "1");
            }
        }
        stepSecond.putExtra("saved_data", editProfileRequest);


        startActivity(stepSecond);
    }

    public String checkDigit(int number) {
        return number <= 9 ? "0" + number : String.valueOf(number);
    }


    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    private boolean isFormValidated() {
        boolean status = true;
        int pos = spinnerSexButton.getSelectedItemPosition();
        int maritalpos = signup_marital_spinner.getSelectedItemPosition();
        sex = spinnerSexButton.getSelectedItem().toString();
        maritalstatus = signup_marital_spinner.getSelectedItem().toString();
        sChFatherName = etChFatherName.getText().toString().trim();
        sMobile = etMobile.getText().toString().trim();
        sOfficialEmail = etOfficialEmail.getText().toString().trim();
        sDOB = etDOB.getText().toString().trim();
        spersonalEmail = etpersonalEmail.getText().toString().trim();
        sFirmName = etFirmName.getText().toString().trim();

        sCommunicationAddress = etCommunicationAddress.getText().toString().trim();
        sBusinessAddress = etBusinessAddress.getText().toString().trim();
        sCommunicationLandmark = etCommunicationLandmark.getText().toString().trim();
        sBusinessLandmark = etBusinessLandmark.getText().toString().trim();
        sCommunicationDistrict = etCommunicationDistrict.getText().toString().trim();
        sCommunicationPIN = etCommunicationPIN.getText().toString().trim();
        sBusinessDistrict = etBusinessDistrict.getText().toString().trim();
        sBusinessPIN = etBusinessPIN.getText().toString().trim();


        if (stateData != null) {
            sCommunicationState = stateData.get(spCommunicationStatePosition).getId();

        }
        sCommunicationDistrict = etCommunicationDistrict.getText().toString().trim();
        //
        sCommunicationPIN = etCommunicationPIN.getText().toString().trim();

        if (cityData != null) {

            sCommunicationCity = cityData.get(spCommunicationCityPosition).getId();
        }

        if (stateData != null) {
            sBusinessState = stateData.get(spBusinessStatePosition).getId();
        }

        if (citydataBusiness != null) {
            sBusinessCity = citydataBusiness.get(spBusinessCityPosition).getId();
        }
        sBusinessDistrict = etBusinessDistrict.getText().toString().trim();

        sBusinessPIN = etBusinessPIN.getText().toString().trim();
        if (sDOB.length() <= 0) {
            status = false;
            showWarningSimpleAlertDialog(Constant.TITLE, getString(R.string.validation_dob));
        } else if (sCommunicationAddress.length() <= 0) {
            status = false;
            showWarningSimpleAlertDialog(Constant.TITLE, "Please enter Locality, Area or Street  for Residence");
        } else if (spCommunicationStatePosition == 0) {
            status = false;
            showWarningSimpleAlertDialog(Constant.TITLE, "Please select state for Residence");
        } else if (spCommunicationCityPosition == 0) {
            status = false;
            showWarningSimpleAlertDialog(Constant.TITLE, "Please select City for Residence");
        } else if (sCommunicationDistrict.length() <= 0) {
            status = false;
            showWarningSimpleAlertDialog(Constant.TITLE, "Flat no, Building name for Residence ");
        } else if (sCommunicationPIN.length() <= 0) {
            status = false;
            showWarningSimpleAlertDialog(Constant.TITLE, "Please enter PIN code for Residence ");
        } else if (spersonalEmail.length() <= 0 || !isValidEmail(spersonalEmail)) {
            status = false;
            showWarningSimpleAlertDialog(Constant.TITLE, "Please enter valid Email id ");
        } else if (spersonalEmail.length() > 0) {
            if (!isValidEmail(spersonalEmail)) {
                status = false;
                showWarningSimpleAlertDialog(Constant.TITLE, getString(R.string.validation_valid_email));
            }
        }

        if (status) {
            if (!isRadioChecked) {
                if (sBusinessPIN.length() != 6) {
                    status = false;
                    showWarningSimpleAlertDialog(Constant.TITLE, "Please enter PIN code for Permanent ");
                } else if (spBusinessStatePosition == 0) {
                    status = false;
                    showWarningSimpleAlertDialog(Constant.TITLE, "Please select state for Permanent");
                } else if (spBusinessCityPosition == 0) {
                    status = false;
                    showWarningSimpleAlertDialog(Constant.TITLE, "Please select City for Permanent");
                } else if (sBusinessAddress.length() <= 0) {
                    status = false;
                    showWarningSimpleAlertDialog(Constant.TITLE, "Please enter Locality, Area or Street  for Permanent");
                } else if (sBusinessDistrict.length() <= 0) {
                    status = false;
                    showWarningSimpleAlertDialog(Constant.TITLE, "Flat no, Building name for Permanent ");
                }
            } else {
                sBusinessPIN = sCommunicationPIN;
                spBusinessStatePosition = spCommunicationStatePosition;
                spBusinessCityPosition = spCommunicationCityPosition;
                sBusinessState = stateData.get(spCommunicationStatePosition).getId();
                sBusinessCity = cityData.get(spCommunicationCityPosition).getId();
                sBusinessAddress = sCommunicationAddress;
                sBusinessDistrict = sCommunicationDistrict;
                sBusinessLandmark = sCommunicationLandmark;
            }
        }
        return status;
    }
    public void getCity() {
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
                                    Log.i(TAG, "json " + json);
                                    cityData = new ArrayList<>();
                                    if (response.body().getData().getCitys().size() > 0) {
                                        cityData = new ArrayList<>();
                                        City city = new City();
                                        city.setId("-1");
                                        city.setS_name("Select City");
                                        cityData.add(city);
                                        cityData.addAll(response.body().getData().getCitys());
                                        displayCityList();
                                    } else {

                                    }

                                } else {
                                    displaySnackBar.DisplaySnackBar("Data not found", Constant.TYPE_ERROR);

                                }
                            } else if (response.body().getStatus() == 0) {
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
                        Log.i("error5", errorBody.getMessage());
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
                                    setAddress();
                                    showDataOnView();
                                } else {
                                    displaySnackBar.DisplaySnackBar("Data not found", Constant.TYPE_ERROR);

                                }
                            } else if (response.body().getStatus() == 0) {
                                Log.e(TAG, "onResponse: status 0 call edit FOS" );
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

    public void getCityBusiness() {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        final Call<CityResponce> call = apiService.getCity(mySingleton.getData(Constant.TOKEN_BASE_64), stateData.get(spBusinessStatePosition).getId());
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
                                    Log.i(TAG, "json " + json);

                                    if (response.body().getData().getCitys().size() > 0) {
                                        citydataBusiness = new ArrayList<>();
                                        City city1 = new City();
                                        city1.setId("-1");
                                        city1.setS_name("Select City");
                                        citydataBusiness.add(city1);
                                        citydataBusiness.addAll(response.body().getData().getCitys());
                                        displayCityListBusiness();
                                    } else {

                                    }

                                } else {
                                    displaySnackBar.DisplaySnackBar("Data not found", Constant.TYPE_ERROR);

                                }
                            } else if (response.body().getStatus() == 0) {
                                Log.e(TAG, "onResponse: status 0 calleditFOS" );
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

    public void callStateSearchAPI() {


        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        final Call<SearchStateResponce> call = apiService.getStateCity(mySingleton.getData(Constant.TOKEN_BASE_64), sCommunicationPIN);
        call.enqueue(new Callback<SearchStateResponce>() {
            @Override
            public void onResponse(Call<SearchStateResponce> call, Response<SearchStateResponce> response) {
                if (response.isSuccessful()) {
                    if (response.code() >= 200 && response.code() < 700) {
                        if (response.code() == 200) {
                            if (response.body().getStatus() == 1) {
                                if (response.body() != null) {
                                    Gson gson = new Gson();
                                    String json = gson.toJson(response.body(), SearchStateResponce.class);
                                    if (response.body().getData() != null && response.body().getData().getDetails() != null) {
                                        Log.i(TAG, "json " + response.body().getData().getDetails());
                                        if (response.body().getData().getDetails().getState_id() != null) {
                                            if (profileData != null) {
                                                if (profileData.getCommunication() == null) {
                                                    profileData.setCommunication(new Communication());
                                                }
                                            }
                                            if (profileData != null && profileData.getCommunication() != null) {
                                                profileData.getCommunication().setState(response.body().getData().getDetails().getState_id());
                                            }
                                        }
                                        if (response.body().getData().getDetails().getCity_id() != null) {
                                            if (profileData != null) {
                                                if (profileData.getCommunication() == null) {
                                                    profileData.setCommunication(new Communication());
                                                }
                                            }
                                            if (profileData != null && profileData.getCommunication() != null) {
                                                profileData.getCommunication().setCity(response.body().getData().getDetails().getCity_id());
                                            }
                                        }
                                        showDataOnViewOnFindCommuicationPIN();
                                    }
                                    Log.i(TAG, "json " + json);
                                } else {
                                    displaySnackBar.DisplaySnackBar("Data not found", Constant.TYPE_ERROR);

                                }
                            } else if (response.body().getStatus() == 0) {
                                Log.e(TAG, "onResponse: Status 0" );
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
            public void onFailure(Call<SearchStateResponce> call, Throwable t) {
                if (t != null && (t instanceof IOException || t instanceof SocketTimeoutException
                        || t instanceof ConnectException || t instanceof NoRouteToHostException
                        || t instanceof SecurityException)) {
                    displaySnackBar.DisplaySnackBar(Constant.NETWIRK_ERROR, Constant.TYPE_ERROR);

                }
            }
        });
    }

    public void callBusinessStateSearchAPI() {


        kProgressHUD = KProgressHUD.create(context)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .setWindowColor(getResources().getColor(R.color.progressbar_color))
                .show();

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        final Call<SearchStateResponce> call = apiService.getStateCity(mySingleton.getData(Constant.TOKEN_BASE_64), sBusinessPIN);
        call.enqueue(new Callback<SearchStateResponce>() {
            @Override
            public void onResponse(Call<SearchStateResponce> call, Response<SearchStateResponce> response) {
                kProgressHUD.dismiss();
                if (response.isSuccessful()) {
                    if (response.code() >= 200 && response.code() < 700) {
                        if (response.code() == 200) {
                            if (response.body().getStatus() == 1) {
                                if (response.body() != null) {
                                    Gson gson = new Gson();
                                    String json = gson.toJson(response.body(), SearchStateResponce.class);
                                    if (response.body().getData() != null && response.body().getData().getDetails() != null) {
                                        Log.i(TAG, "json " + response.body().getData().getDetails());
                                        if (response.body().getData().getDetails().getState_id() != null) {
                                            if (profileData != null) {
                                                if (profileData.getBussiness() == null) {
                                                    profileData.setBussiness(new Business());
                                                }
                                            }
                                            if (profileData != null && profileData.getBussiness() != null) {
                                                profileData.getBussiness().setState(response.body().getData().getDetails().getState_id());
                                            }
                                        }
                                        if (response.body().getData().getDetails().getCity_id() != null) {
                                            if (profileData.getBussiness() == null) {
                                                profileData.setBussiness(new Business());
                                            }
                                            profileData.getBussiness().setCity(response.body().getData().getDetails().getCity_id());
                                        }
                                        showDataOnViewOnFindBusinessnPIN();
                                    }
                                    Log.i(TAG, "json " + json);
                                } else {
                                    displaySnackBar.DisplaySnackBar("Data not found", Constant.TYPE_ERROR);

                                }
                            } else if (response.body().getStatus() == 0) {
                                Log.e(TAG, "onResponse: status 0 calleditFOS" );                            }
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
                                Log.e(TAG, "onResponse: status 0 calleditFOS" );                            }
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

    public static EditProfileRequest editProfileRequest;

    public void callUpdateProfile() {
        getDataFromViews();

        Gson gson = new Gson();
        String json = gson.toJson(editProfileRequest, EditProfileRequest.class);
        Log.i(TAG, "Request Data " + json);
        Log.i(TAG, "Request Data " + mySingleton.getData(Constant.TOKEN_BASE_64));

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
                            // callNextStep(editProfileRequest);
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

    private void getDataFromViews() {
        editProfileRequest = new EditProfileRequest();
        editProfileRequest.setFirst_name(!TextUtil.isStringNullOrBlank(profileData.getFirst_name()) ? profileData.getFirst_name() : "");
        editProfileRequest.setLast_name(!TextUtil.isStringNullOrBlank(profileData.getLast_name()) ? profileData.getLast_name() : "");
        editProfileRequest.setPhone_number(!TextUtil.isStringNullOrBlank(profileData.getPhone_number()) ? profileData.getPhone_number() : "");
        editProfileRequest.setDob(sDOB);


        if (mySingleton.getData(Constant.USER_GROUP_ID).equalsIgnoreCase("2")) {
            editProfileRequest.setPersonal_email_id(spersonalEmail);
            editProfileRequest.setOfficial_email_id(spersonalEmail);
        } else {
            editProfileRequest.setPersonal_email_id(spersonalEmail);
            editProfileRequest.setOfficial_email_id(spersonalEmail);
        }


        editProfileRequest.setGender(sex.substring(0, 1));
        editProfileRequest.setEm_person_contact_no(sOfficialEmail);
        editProfileRequest.setEm_contact_person_name(sFirmName);

        editProfileRequest.setPos_father_name(sChFatherName);
        editProfileRequest.setMarital_status(maritalstatus.substring(0, 1));


        Communication communicationAddress = new Communication();
        communicationAddress.setAddress(sCommunicationAddress);
        communicationAddress.setState(sCommunicationState);
        communicationAddress.setDistrict(sCommunicationDistrict);
        communicationAddress.setCity(sCommunicationCity);
        communicationAddress.setPincode(sCommunicationPIN);
        communicationAddress.setLand_mark(sCommunicationLandmark);
        editProfileRequest.setCommunication(communicationAddress);

        Business communicationBusiness = new Business();
        communicationBusiness.setAddress(sBusinessAddress);
        communicationBusiness.setState(sBusinessState);
        communicationBusiness.setDistrict(sBusinessDistrict);
        communicationBusiness.setCity(sBusinessCity);
        communicationBusiness.setPincode(sBusinessPIN);
        communicationBusiness.setLand_mark(sBusinessLandmark);

        editProfileRequest.setBussiness(communicationBusiness);

    }

    public void getState() {
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
                                    Log.i(TAG, "json " + json);
                                    if (response.body().getData().getState().size() > 0) {
                                        stateData.addAll(response.body().getData().getState());
                                    } else {

                                    }

                                } else {
                                    displaySnackBar.DisplaySnackBar("Data not found", Constant.TYPE_ERROR);

                                }
                            } else if (response.body().getStatus() == 0) {

                                Log.e(TAG, "onResponse: callEditFOS status 0" );                            }
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

    private void textViewByPin() {
        etCommunicationPIN.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {


            }

            @Override
            public void afterTextChanged(Editable text) {

                if (text.length() == 6 && isCommunicationPinNew && etCommunicationPIN.length() == 6) {
                    if (network.isNetworkConnected(context)) {
                        sCommunicationPIN = etCommunicationPIN.getText() + "";
                        if (sCommunicationPIN.length() != 6) {
                            Toast.makeText(context, "Pin is not valid", Toast.LENGTH_SHORT).show();
                        } else {
                            if (profileData != null) {
                                if (profileData.getCommunication() == null) {
                                    profileData.setCommunication(new Communication());
                                }
                            }
                            if (profileData != null && profileData.getCommunication() != null) {
                                profileData.getCommunication().setPincode(sCommunicationPIN);
                            }
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(etCommunicationPIN.getWindowToken(), 0);
                            callStateSearchAPI();
                        }
                    } else {
                        displaySnackBar.DisplaySnackBar(Constant.NETWIRK_ERROR, Constant.TYPE_ERROR);
                    }
                }

            }
        });

        etBusinessPIN.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable text) {
                if (text.length() == 6 && isBussinessPinNew) {
                    if (network.isNetworkConnected(context)) {
                        sBusinessPIN = etBusinessPIN.getText() + "";
                        if (sBusinessPIN.length() != 6) {
                            Toast.makeText(context, "Business Pin is not valid", Toast.LENGTH_SHORT).show();
                        } else {
                            if (profileData != null) {
                                if (profileData.getBussiness() == null) {
                                    profileData.setBussiness(new Business());
                                }
                            }
                            if (profileData != null && profileData.getBussiness() != null) {
                                profileData.getBussiness().setPincode(sBusinessPIN);
                            }
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(etBusinessPIN.getWindowToken(), 0);
                            callBusinessStateSearchAPI();
                        }
                    } else {
                        displaySnackBar.DisplaySnackBar(Constant.NETWIRK_ERROR, Constant.TYPE_ERROR);
                    }
                }
            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


    private void getLocationAutoData() {

        if (network.isNetworkConnected(context)) {
            getState();
        } else {
            displaySnackBar.DisplaySnackBar(Constant.NETWIRK_ERROR, Constant.TYPE_ERROR);
        }

        viewListener();
        textViewByPin();

        if (network.isNetworkConnected(context)) {
            callProfileAPI();
        } else {
            displaySnackBar.DisplaySnackBar(Constant.NETWIRK_ERROR, Constant.TYPE_ERROR);
        }


    }

    private void setAddress() {

        if (isCommunicationPinNew && etCommunicationPIN.length() == 6) {

            if (network.isNetworkConnected(context)) {
                sCommunicationPIN = etCommunicationPIN.getText() + "";
                if (sCommunicationPIN.length() != 6) {
                    Toast.makeText(context, "Pin is not valid", Toast.LENGTH_SHORT).show();
                } else {
                    if (profileData != null) {
                        if (profileData.getCommunication() == null) {
                            profileData.setCommunication(new Communication());
                        }
                    }
                    if (profileData != null && profileData.getCommunication() != null) {
                        profileData.getCommunication().setPincode(sCommunicationPIN);
                    }
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(etCommunicationPIN.getWindowToken(), 0);
                    callStateSearchAPI();
                }
            } else {
                displaySnackBar.DisplaySnackBar(Constant.NETWIRK_ERROR, Constant.TYPE_ERROR);
            }
        }

    }
}
