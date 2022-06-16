package com.rebliss.view.activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
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
import com.rebliss.domain.model.industrytype.IndustryType;
import com.rebliss.domain.model.industrytype.IndustryTypeResponse;
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
import com.rebliss.view.adapter.IndustryAdapter;
import com.rebliss.view.adapter.StateSpinnerAdapter;
import java.io.IOException;
import java.net.ConnectException;
import java.net.NoRouteToHostException;
import java.net.SocketTimeoutException;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardCPDetails extends AppCompatActivity {
    private static final String TAG = DashboardCPDetails.class.getSimpleName();
    private Context context;

    private TextView textChName, textChLName, textMobile, textDOB, textLandline, textpersonalEmail, textOfficialEmail,
            textFirmName, textCommunicationAddress, textBusinessAddress, textHeader;

    private ImageView imgLogout, imgCal;

    private Button btnSave, findPin, findBusinessPin;

    private EditText etChName, etChLName, etMobile, etDOB, etLandline, etpersonalEmail, etOfficialEmail,
            etFirmName, etCommunicationAddress, etBusinessAddress, etCommunicationLandmark, etBusinessLandmark;
    private EditText etCommunicationDistrict, etCommunicationPIN;
    private EditText etBusinessDistrict, etBusinessPIN;

    LinearLayout lnAddNrtBsns;

    boolean isComPinNew = true;
    boolean isBusPinNew = true;

    String slctBussinessNature = "";
    private String[] industryArray;
    private MySingleton mySingleton;
    private Network network;
    private KProgressHUD kProgressHUD;
    private DisplaySnackBar displaySnackBar;
    public static Data profileData;
    private List<State> stateData;
    private List<IndustryType> industryTypeData;
    private HashMap<String, String> industrySpinner;
    private List<City> cityData, citydataBusiness;

    private String sChName = "", sChLName = "", sMobile = "", sDOB = "", sLandline = "",
            spersonalEmail = "", sOfficialEmail = "",
            sFirmName = "", sCommunicationAddress = "", sBusinessAddress = "", sAddBusinessNature = "";

    private String sCommunicationState = "", sCommunicationDistrict = "", sCommunicationCity = "", sCommunicationPIN = "", sBusinessState = "",
            sBusinessDistrict = "", sBusinessCity = "", sBusinessPIN = "", sCommunicationLandmark = "", sBusinessLandmark = "", sBussinessNature = "";


    private int Day, Month, Year;
    private String industryGroup_id;
    private int mYear, mMonth, mDay;
    Spinner spCommunicationState, spCommunicationCity, spBusinessState, spBusinessCity, spFirmType, spBusinessNature;
    private int spCommunicationStatePosition = 0, spCommunicationCityPosition = 0, spBusinessStatePosition = 0,
            spBusinessCityPosition = 0, spFirmTypePosition = 0, spBussinessNaturePosition = 0;
    StateSpinnerAdapter stateSpinnerAdapter, stateSpinnerAdapterBusiness;

    com.rebliss.view.adapter.CitySpinnerAdapter CitySpinnerAdapter;
    com.rebliss.view.adapter.CitySpinnerAdapter CitySpinnerAdapterBusiness;
    com.rebliss.view.adapter.IndustryAdapter IndustryAdapterType;

    private TextView textStepOne, textStepTwo, textStepThree, textStepFour;
    private Intent intent;
    private String callFrom = "";

    private AutoCompleteTextView actvBusinessNature;
    private RadioButton radioBusiness;
    private boolean isRadioChecked = false;
    private LinearLayout liRadio, liBusiness;
    // Array of choices
    String firmTypeString[] = {"Proprietorship Firm", "Partnership Firm", "Pvt Ltd", "Public Ltd."};
    private EditText etBusinessNature, etManpower, addBusinessNature;
    private String sBusinessNature, sManpower, sBusinessType, sfirmTypeSpinner;
    private TextView textBusinessNature, textfirmType, textManpower, addtextBusinessNature;
    LinearLayout lNameLi;
    TextView mywidget;
    ImageView icBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initView();
        CallAppVersion callAppVersion = new CallAppVersion();
        callAppVersion.CallAppVersion(DashboardCPDetails.this);

        viewListener();
        textWatch();

        try {
            intent = getIntent();
            callFrom = intent.getStringExtra("call_from");
        } catch (Exception e) {
            callFrom = "";
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        disable();
        if (network.isNetworkConnected(context)) {
            getIndustryType();
        } else {
            displaySnackBar.DisplaySnackBar(Constant.NETWIRK_ERROR, Constant.TYPE_ERROR);
        }
        if (network.isNetworkConnected(context)) {
            getState();
        } else {
            displaySnackBar.DisplaySnackBar(Constant.NETWIRK_ERROR, Constant.TYPE_ERROR);
        }
        if (network.isNetworkConnected(context)) {
            callProfileAPI();
        } else {
            displaySnackBar.DisplaySnackBar(Constant.NETWIRK_ERROR, Constant.TYPE_ERROR);
        }
    }

    private void disable() {
        if (!TextUtil.isStringNullOrBlank(callFrom)) {
            if (callFrom.equalsIgnoreCase("1")) {
                lNameLi.setVisibility(View.GONE);
                imgLogout.setVisibility(View.GONE);
                etBusinessNature.setEnabled(false);
                liRadio.setEnabled(false);
                findPin.setEnabled(false);
                etManpower.setEnabled(false);
                etCommunicationLandmark.setEnabled(false);
                etBusinessLandmark.setEnabled(false);
                findBusinessPin.setEnabled(false);
                spFirmType.setEnabled(false);
                spBusinessNature.setEnabled(false);
                etChName.setEnabled(false);
                etChName.setEnabled(false);
                etMobile.setEnabled(false);
                etDOB.setEnabled(false);
                etLandline.setEnabled(false);
                etpersonalEmail.setEnabled(false);
                etOfficialEmail.setEnabled(false);
                etFirmName.setEnabled(false);
                etCommunicationAddress.setEnabled(false);
                etBusinessAddress.setEnabled(false);
                spCommunicationState.setEnabled(false);
                etCommunicationDistrict.setEnabled(false);
                spCommunicationCity.setEnabled(false);
                etCommunicationPIN.setEnabled(false);
                spBusinessState.setEnabled(false);
                etBusinessDistrict.setEnabled(false);
                spBusinessCity.setEnabled(false);
                etBusinessPIN.setEnabled(false);
                imgCal.setEnabled(false);
                etManpower.setClickable(false);
                liRadio.setClickable(false);
                etBusinessNature.setClickable(false);
                etCommunicationLandmark.setClickable(false);
                etBusinessLandmark.setClickable(false);
                findPin.setClickable(false);
                findBusinessPin.setClickable(false);
                spFirmType.setClickable(false);
                addBusinessNature.setClickable(false);
                addBusinessNature.setEnabled(false);
                spBusinessNature.setClickable(false);
                etChName.setClickable(false);
                etMobile.setClickable(false);
                etDOB.setClickable(false);
                etLandline.setClickable(false);
                etpersonalEmail.setClickable(false);
                etOfficialEmail.setClickable(false);
                etFirmName.setClickable(false);
                etCommunicationAddress.setClickable(false);
                etBusinessAddress.setClickable(false);
                spCommunicationState.setClickable(false);
                etCommunicationDistrict.setClickable(false);
                spCommunicationCity.setClickable(false);
                etCommunicationPIN.setClickable(false);
                spBusinessState.setClickable(false);
                etBusinessDistrict.setClickable(false);
                spBusinessCity.setClickable(false);
                etBusinessPIN.setClickable(false);
                imgCal.setClickable(false);


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
        icBack = findViewById(R.id.icBack);

        context = DashboardCPDetails.this;
        mySingleton = new MySingleton(this);
        displaySnackBar = new DisplaySnackBar(this);
        network = new Network();


        mywidget = findViewById(R.id.mywidget);
        lNameLi = findViewById(R.id.lNameLi);
        textStepOne = findViewById(R.id.textStepOne);
        textStepTwo = findViewById(R.id.textStepTwo);
        textStepThree = findViewById(R.id.textStepThree);
        textStepFour = findViewById(R.id.textStepFour);
        textStepOne.setTypeface(App.LATO_REGULAR);
        textStepTwo.setTypeface(App.LATO_REGULAR);
        textStepThree.setTypeface(App.LATO_REGULAR);
        textHeader = findViewById(R.id.textHeader);
        textChName = findViewById(R.id.textChName);
        textMobile = findViewById(R.id.textMobile);
        textDOB = findViewById(R.id.textDOB);
        textLandline = findViewById(R.id.textLandline);
        textpersonalEmail = findViewById(R.id.textpersonalEmail);
        textOfficialEmail = findViewById(R.id.textOfficialEmail);
        textFirmName = findViewById(R.id.textFirmName);
        textCommunicationAddress = findViewById(R.id.textCommunicationAddress);
        textBusinessAddress = findViewById(R.id.textBusinessAddress);
        textChLName = findViewById(R.id.textChLName);
        textfirmType = findViewById(R.id.textfirmType);
        textBusinessNature = findViewById(R.id.textBusinessNature);
        textfirmType = findViewById(R.id.textfirmType);
        textManpower = findViewById(R.id.textManpower);
        addtextBusinessNature = findViewById(R.id.addtextBusinessNature);

        imgLogout = findViewById(R.id.imgLogout);
        imgLogout.setVisibility(View.VISIBLE);
        imgCal = findViewById(R.id.imgCal);

        // Button References
        btnSave = findViewById(R.id.btnSave);
        findPin = findViewById(R.id.findPin);
        findBusinessPin = findViewById(R.id.findBusinessPin);
        spCommunicationState = findViewById(R.id.etCommunicationState);
        spCommunicationCity = findViewById(R.id.etCommunicationCity);

        actvBusinessNature = findViewById(R.id.actvBusinessNature);

        // EditText References

        etChName = findViewById(R.id.etChName);
        etMobile = findViewById(R.id.etMobile);
        etDOB = findViewById(R.id.etDOB);
        etLandline = findViewById(R.id.etLandline);
        etpersonalEmail = findViewById(R.id.etpersonalEmail);
        etOfficialEmail = findViewById(R.id.etOfficialEmail);
        etFirmName = findViewById(R.id.etFirmName);
        etCommunicationAddress = findViewById(R.id.etCommunicationAddress);
        etBusinessNature = findViewById(R.id.etBusinessNature);
        etManpower = findViewById(R.id.etManpower);
        addBusinessNature = findViewById(R.id.addBusinessNature);


        etBusinessAddress = findViewById(R.id.etBusinessAddress);
        etChLName = findViewById(R.id.etChLName);
        spCommunicationState = findViewById(R.id.etCommunicationState);
        etCommunicationDistrict = findViewById(R.id.etCommunicationDistrict);
        spCommunicationCity = findViewById(R.id.etCommunicationCity);
        etCommunicationPIN = findViewById(R.id.etCommunicationPIN);
        spBusinessState = findViewById(R.id.etBusinessState);
        spFirmType = findViewById(R.id.spFirmType);
        spBusinessNature = findViewById(R.id.spBusinessNature);
        etBusinessDistrict = findViewById(R.id.etBusinessDistrict);
        spBusinessCity = findViewById(R.id.etBusinessCity);
        etBusinessPIN = findViewById(R.id.etBusinessPIN);
        etCommunicationLandmark = findViewById(R.id.etCommunicationLandmark);
        etBusinessLandmark = findViewById(R.id.etBusinessLandmark);

        // Layout References
        lnAddNrtBsns = findViewById(R.id.lnAddNrtBsns);
        liBusiness = findViewById(R.id.liBusiness);
        radioBusiness = findViewById(R.id.radioBusiness);
        liRadio = findViewById(R.id.liRadio);
        radioBusiness.setChecked(isRadioChecked);
        final Calendar c = Calendar.getInstance();
        setFontOnView();
        ((TextInputLayout) findViewById(R.id.etFirstNameLayout)).setTypeface(App.LATO_REGULAR);
        setHint();

    }

    private void setHint() {


        textChName.setText(ShowHintOrText.GetMandatory("Name"));
        textMobile.setText(ShowHintOrText.GetMandatory("Mobile No"));
        if (!mySingleton.getData(Constant.USER_GROUP_ID).equalsIgnoreCase(Constant.POS_GROUP_ID)) {
            textOfficialEmail.setText(ShowHintOrText.GetMandatory("Mail id"));
        } else {
            textOfficialEmail.setText(ShowHintOrText.GetOptional("Mail id"));

        }
        textpersonalEmail.setText(ShowHintOrText.GetOptional("Personal Mail id"));
        textFirmName.setText(ShowHintOrText.GetMandatory("Your Firm Name"));
        if (!mySingleton.getData(Constant.USER_GROUP_ID).equalsIgnoreCase(Constant.POS_GROUP_ID)) {
            textBusinessNature.setText(ShowHintOrText.GetMandatory("Nature of Business"));
            textfirmType.setText(ShowHintOrText.GetMandatory("Firm Type"));
        } else {
            textBusinessNature.setText(ShowHintOrText.GetOptional("Nature of Business"));
            textfirmType.setText(ShowHintOrText.GetOptional("Firm Type"));
        }

        textManpower.setText(ShowHintOrText.GetOptional("Current Manpower Strength"));
        textCommunicationAddress.setText(ShowHintOrText.GetMandatory("Billing Address"));
        textBusinessAddress.setText(ShowHintOrText.GetMandatory("Business Address"));

        if (slctBussinessNature.equalsIgnoreCase("Others")) {
            addtextBusinessNature.setText(ShowHintOrText.GetMandatory("Add Nature Of Business"));
        }


        etDOB.setHint(ShowHintOrText.GetMandatory("Date of birth"));
        etLandline.setHint(ShowHintOrText.GetOptional(" "));

        etCommunicationPIN.setHint(ShowHintOrText.GetMandatory("PIN Code"));
        etCommunicationAddress.setHint(ShowHintOrText.GetMandatory("Locality, Area or Street"));
        etCommunicationDistrict.setHint(ShowHintOrText.GetMandatory("Flat no, Building name"));

        etCommunicationLandmark.setHint(ShowHintOrText.GetOptional("Landmark"));


        etBusinessPIN.setHint(ShowHintOrText.GetMandatory("PIN Code"));
        etBusinessAddress.setHint(ShowHintOrText.GetMandatory("Locality, Area or Street"));
        etBusinessDistrict.setHint(ShowHintOrText.GetMandatory("Flat no, Building name"));
        etBusinessLandmark.setHint(ShowHintOrText.GetOptional("Landmark"));
    }

    private void displayType() {
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, firmTypeString) {

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
        ;
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        spFirmType.setAdapter(spinnerArrayAdapter);

        if (profileData != null && !TextUtil.isStringNullOrBlank(profileData.getBusiness_type())) {
            for (int i = 0; i < firmTypeString.length; i++) {
                if (firmTypeString[i].equalsIgnoreCase(profileData.getBusiness_type())) {
                    spFirmTypePosition = i;
                }
                spFirmType.setSelection(spFirmTypePosition);
            }
        }
    }

    private void displayIndustryList() {
        IndustryAdapterType = new IndustryAdapter(context, industryTypeData); // The drop down view
        spBusinessNature.setAdapter(IndustryAdapterType);
        spBusinessNature.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spBussinessNaturePosition = position;
                slctBussinessNature = industryTypeData.get(position).getText();
                if (slctBussinessNature.equalsIgnoreCase("Others")) {
                    lnAddNrtBsns.setVisibility(View.VISIBLE);
                    addtextBusinessNature.setText(ShowHintOrText.GetMandatory("Add Nature Of Business"));
                } else {
                    lnAddNrtBsns.setVisibility(View.GONE);
                }
                Log.i("select", industryTypeData.get(position).getText());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if (profileData != null && !TextUtil.isStringNullOrBlank(profileData.getNature_of_business())) {
            if (industryTypeData != null) {
                for (int i = 0; i < industryTypeData.size(); i++) {
                    if (profileData.getNature_of_business().equalsIgnoreCase(industryTypeData.get(i).getText())) {
                        spBussinessNaturePosition = i;
                        sBussinessNature = industryTypeData.get(i).getText();
                        if (profileData.getNature_of_business().equalsIgnoreCase("Others")) {
                            lnAddNrtBsns.setVisibility(View.VISIBLE);
                            addtextBusinessNature.setText(ShowHintOrText.GetMandatory("Add Nature Of Business"));
                        }
                    }

                }
            }
            Log.i("spnr", sBussinessNature);
        }

        spBusinessNature.setSelection(spBussinessNaturePosition);
    }

    private void setAutoCompleteList() {
        actvBusinessNature = (AutoCompleteTextView) findViewById(R.id.actvBusinessNature);
        industryArray = new String[industryTypeData.size()];

        for (int i = 0; i < industryArray.length; i++) {
            industryArray[i] = industryTypeData.get(i).getText().toString();

        }

        ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<>(DashboardCPDetails.this, android.R.layout.simple_list_item_1, industryArray);

        actvBusinessNature.setAdapter(adapter);
        actvBusinessNature.setThreshold(1);

        actvBusinessNature.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //spBussinessNaturePosition = position;
                String selectedItem = actvBusinessNature.getAdapter().getItem(position).toString();

                for (int i = 0; i < industryArray.length; i++) {
                    if (industryArray[i].equalsIgnoreCase(selectedItem)) {
                        sBussinessNature = industryTypeData.get(i).getId();
                        break;
                    }
                }
                Log.i("pppp", sBussinessNature + "");

            }

        });
        // Log.i("INDST", DashboardCPDetails.profileData.getNature_of_business());
        if (profileData != null && !TextUtil.isStringNullOrBlank(profileData.getNature_of_business())) {
            Log.i("INDST", profileData.getNature_of_business());
            if (industryArray != null) {
                for (int i = 0; i < industryArray.length; i++) {
                    if (profileData.getNature_of_business().equalsIgnoreCase(industryArray[i])) {
                        spBussinessNaturePosition = i;
                        Log.i("III", i + "");
                    }
                }
            }
        }
        //actvBusinessNature.setSelection(spBussinessNaturePosition);
        actvBusinessNature.setText(industryTypeData.get(spBussinessNaturePosition).getText());

    }

    private void setFontOnView() {
        // TextView References
        findPin.setTypeface(App.LATO_REGULAR);
        findBusinessPin.setTypeface(App.LATO_REGULAR);
        mywidget.setTypeface(App.LATO_REGULAR);
        textHeader.setTypeface(App.LATO_REGULAR);
        textChName.setTypeface(App.LATO_REGULAR);
        textMobile.setTypeface(App.LATO_REGULAR);
        textDOB.setTypeface(App.LATO_REGULAR);
        textLandline.setTypeface(App.LATO_REGULAR);
        textpersonalEmail.setTypeface(App.LATO_REGULAR);
        textOfficialEmail.setTypeface(App.LATO_REGULAR);
        textFirmName.setTypeface(App.LATO_REGULAR);
        textCommunicationAddress.setTypeface(App.LATO_REGULAR);
        textBusinessAddress.setTypeface(App.LATO_REGULAR);
        textChLName.setTypeface(App.LATO_REGULAR);
        textBusinessNature.setTypeface(App.LATO_REGULAR);
        textfirmType.setTypeface(App.LATO_REGULAR);
        textManpower.setTypeface(App.LATO_REGULAR);


        etChName.setTypeface(App.LATO_REGULAR);
        etMobile.setTypeface(App.LATO_REGULAR);
        etChLName.setTypeface(App.LATO_REGULAR);
        etDOB.setTypeface(App.LATO_REGULAR);
        etLandline.setTypeface(App.LATO_REGULAR);
        etpersonalEmail.setTypeface(App.LATO_REGULAR);
        etOfficialEmail.setTypeface(App.LATO_REGULAR);
        etFirmName.setTypeface(App.LATO_REGULAR);
        etCommunicationAddress.setTypeface(App.LATO_REGULAR);
        etBusinessAddress.setTypeface(App.LATO_REGULAR);
        etManpower.setTypeface(App.LATO_REGULAR);
        etBusinessNature.setTypeface(App.LATO_REGULAR);

        // etCommunicationState.setTypeface(App.LATO_REGULAR);
        etCommunicationDistrict.setTypeface(App.LATO_REGULAR);
        //etCommunicationCity.setTypeface(App.LATO_REGULAR);
        etCommunicationPIN.setTypeface(App.LATO_REGULAR);

        //etBusinessState.setTypeface(App.LATO_REGULAR);
        etBusinessDistrict.setTypeface(App.LATO_REGULAR);
        //etBusinessCity.setTypeface(App.LATO_REGULAR);
        etBusinessPIN.setTypeface(App.LATO_REGULAR);

        btnSave.setTypeface(App.LATO_REGULAR);
        radioBusiness.setTypeface(App.LATO_REGULAR);
        etCommunicationLandmark.setTypeface(App.LATO_REGULAR);
        etBusinessLandmark.setTypeface(App.LATO_REGULAR);

    }

    private void showDataOnView() {

        mySingleton.saveData(Constant.USER_PROFILE_VERIFYED, profileData.getProfile_verified());
        if (profileData.getProfile_verified().equalsIgnoreCase("0")
                || profileData.getProfile_verified().equalsIgnoreCase("2")) {
            if (!TextUtil.isStringNullOrBlank(profileData.getDecline_message())) {
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
        etMobile.setText((!TextUtil.isStringNullOrBlank(profileData.getPhone_number()) ? profileData.getPhone_number() : ""));
        if (!TextUtil.isStringNullOrBlank(profileData.getDob())) {
            Log.i("DateString", profileData.getDob());
            String[] dateformat = profileData.getDob().split("-");
            mYear = Integer.parseInt(dateformat[0]);
            mMonth = Integer.parseInt(dateformat[1]);
            mDay = Integer.parseInt(dateformat[2]);
            sDOB = mYear + "-" + checkDigit(mMonth) + "-" + checkDigit(mDay);
            etDOB.setText(checkDigit(mDay) + "-" + getMonthForInt(mMonth - 1) + "-" + mYear);
            etDOB.setTypeface(App.LATO_REGULAR);

        }

        if (profileData.getPartner() != null) {
            // etChName.setText((!TextUtil.isStringNullOrBlank(profileData.getPartner().getName()) ? profileData.getPartner().getName() : ""));
        }

        displayType();

        etManpower.setText((!TextUtil.isStringNullOrBlank(profileData.getManpower_strength()) ? profileData.getManpower_strength() : ""));
        etBusinessNature.setText((!TextUtil.isStringNullOrBlank(profileData.getNature_of_business()) ? profileData.getNature_of_business() : ""));
        addBusinessNature.setText((!TextUtil.isStringNullOrBlank(profileData.getIndustry_type_other()) ? profileData.getIndustry_type_other() : ""));
        etLandline.setText((!TextUtil.isStringNullOrBlank(profileData.getLandline_no()) ? profileData.getLandline_no() : ""));
        etpersonalEmail.setText((!TextUtil.isStringNullOrBlank(profileData.getPersonal_email_id()) ? profileData.getPersonal_email_id() : ""));
        etOfficialEmail.setText((!TextUtil.isStringNullOrBlank(profileData.getOfficial_email_id()) ? profileData.getOfficial_email_id() : ""));
        etFirmName.setText((!TextUtil.isStringNullOrBlank(profileData.getCp_firm_name()) ? profileData.getCp_firm_name() : ""));
        if (profileData.getCommunication() != null) {
            etCommunicationAddress.setText((!TextUtil.isStringNullOrBlank(profileData.getCommunication().getAddress()) ? profileData.getCommunication().getAddress() : ""));
            etCommunicationDistrict.setText((!TextUtil.isStringNullOrBlank(profileData.getCommunication().getDistrict()) ? profileData.getCommunication().getDistrict() : ""));
            isComPinNew = false;
            etCommunicationPIN.setText((!TextUtil.isStringNullOrBlank(profileData.getCommunication().getPincode()) ? profileData.getCommunication().getPincode() : ""));
            isComPinNew = true;
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
             isBusPinNew = false;
            etBusinessPIN.setText((!TextUtil.isStringNullOrBlank(profileData.getBussiness().getPincode()) ? profileData.getBussiness().getPincode() : ""));
            isBusPinNew = true;
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
        if (profileData.getNature_of_business() != null) {
            if (!TextUtil.isStringNullOrBlank(profileData.getNature_of_business())) {
                Log.i("INDST", profileData.getNature_of_business());
               /* if (industryArray != null) {
                    for (int i = 0; i < industryArray.length; i++) {
                        if (profileData.getNature_of_business().equalsIgnoreCase(industryArray[i])) {
                            spBussinessNaturePosition = i;
                            Log.i("III", i + "");
                        }
                    }
                } */
                if (industryTypeData != null) {
                    for (int i = 0; i < industryTypeData.size(); i++) {
                        if (profileData.getNature_of_business().equalsIgnoreCase(industryTypeData.get(i).getText())) {
                            spBussinessNaturePosition = i;
                        }

                    }
                }
            }
            displayIndustryList();
        }
      /* } else if (profileData.getProfile_verified().equalsIgnoreCase("1")) {
            Intent callDashboar = new Intent(context, Dashboard.class);
            callDashboar.putExtra("profile_varified", profileData.getProfile_verified());
            startActivity(callDashboar);
            finish();
        } else if (profileData.getProfile_verified().equalsIgnoreCase("2")) {
            Intent callDashboar = new Intent(context, Dashboard.class);
            callDashboar.putExtra("profile_varified", profileData.getProfile_verified());
            startActivity(callDashboar);
         finish();
        }*/
        //textWatch();
    }

    private void showDataOnViewOnFindCommuicationPIN() {

        mySingleton.saveData(Constant.USER_PROFILE_VERIFYED, profileData.getProfile_verified());
        // if (profileData.getProfile_verified().equalsIgnoreCase("0")) {
//        if(!TextUtil.isStringNullOrBlank(profileData.getFirst_name())){
//            etChName.setText(profileData.getFirst_name());
//        }

        if (profileData.getCommunication() != null) {
            etCommunicationAddress.setText((!TextUtil.isStringNullOrBlank(profileData.getCommunication().getAddress()) ? profileData.getCommunication().getAddress() : ""));
            // etCommunicationState.setText((!TextUtil.isStringNullOrBlank(profileData.getCommunication().getState()) ? profileData.getCommunication().getState() : ""));
            etCommunicationDistrict.setText((!TextUtil.isStringNullOrBlank(profileData.getCommunication().getDistrict()) ? profileData.getCommunication().getDistrict() : ""));
            //etCommunicationCity.setText((!TextUtil.isStringNullOrBlank(profileData.getCommunication().getCity()) ? profileData.getCommunication().getCity() : ""));
            isComPinNew = false;
            etCommunicationPIN.setText((!TextUtil.isStringNullOrBlank(profileData.getCommunication().getPincode()) ? profileData.getCommunication().getPincode() : ""));
            isComPinNew = true;
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
            // etBusinessState.setText((!TextUtil.isStringNullOrBlank(profileData.getBussiness().getState()) ? profileData.getBussiness().getState() : ""));
            etBusinessDistrict.setText((!TextUtil.isStringNullOrBlank(profileData.getBussiness().getDistrict()) ? profileData.getBussiness().getDistrict() : ""));
            etBusinessDistrict.setText((!TextUtil.isStringNullOrBlank(profileData.getBussiness().getDistrict()) ? profileData.getBussiness().getDistrict() : ""));
            // etBusinessCity.setText((!TextUtil.isStringNullOrBlank(profileData.getBussiness().getCity()) ? profileData.getBussiness().getCity() : ""));
            isBusPinNew = false;
            etBusinessPIN.setText((!TextUtil.isStringNullOrBlank(profileData.getBussiness().getPincode()) ? profileData.getBussiness().getPincode() : ""));
            isBusPinNew = true;
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

    private void viewListener() {
        icBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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

                if (!TextUtil.isStringNullOrBlank(callFrom)) {
                    if (callFrom.equalsIgnoreCase("1")) {
                        EditProfileRequest editProfileRequest = new EditProfileRequest();
                        callNextStep(editProfileRequest);
                        Log.e(TAG, "onClick: 1 step" );
                    } else {
                        if (isFormValidated()) {
                            Log.e(TAG, "onClick: sdsfsfsfsdfsafsafasfdadsfaf");
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
                            Log.e(TAG, "onClick: 1 sfddsaffda step" );
                            callUpdateProfile();
                        } else {
                            displaySnackBar.DisplaySnackBar(Constant.NETWIRK_ERROR, Constant.TYPE_ERROR);
                        }
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
//       radioBusiness.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if(isChecked){
//                   isRadioChecked =false;
//                }else{
//                    isRadioChecked =true;
//                }
//                radioBusiness.setChecked(isRadioChecked);
//            }
//        });
        liRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRadioChecked) {
                    isRadioChecked = false;

                } else {
                    isRadioChecked = true;
                }
                callEnableDisableBusinness(isRadioChecked);
                radioBusiness.setChecked(isRadioChecked);
            }
        });

    }


    private void textWatch() {
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

                if (text.length() == 6 && isComPinNew) {
                    // text.append('-');

                    if (network.isNetworkConnected(context)) {
                        sCommunicationPIN = etCommunicationPIN.getText() + "";
                        if (sCommunicationPIN.length() != 6) {
                            Toast.makeText(context, "Pin is not valid", Toast.LENGTH_SHORT).show();
                        } else {
                            if (profileData != null) {
                                if (DashboardCPDetails.profileData.getCommunication() == null) {
                                    DashboardCPDetails.profileData.setCommunication(new Communication());
                                }
                            }
                            if (profileData != null && profileData.getCommunication() != null) {
                                DashboardCPDetails.profileData.getCommunication().setPincode(sCommunicationPIN);
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

                if (text.length() == 6 && isBusPinNew) {
                    if (network.isNetworkConnected(context)) {
                        sBusinessPIN = etBusinessPIN.getText() + "";
                        if (sBusinessPIN.length() != 6) {
                            Toast.makeText(context, "Business Pin is not valid", Toast.LENGTH_SHORT).show();
                        } else {

                            if (profileData != null && profileData.getBussiness() == null) {
                                profileData.setBussiness(new Business());
                            }
                            if (profileData != null && profileData.getBussiness() != null) {
                                profileData.getBussiness().setPincode(sBusinessPIN);
                                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(etBusinessPIN.getWindowToken(), 0);
                                callBusinessStateSearchAPI();
                            }
                        }
                    } else {
                        displaySnackBar.DisplaySnackBar(Constant.NETWIRK_ERROR, Constant.TYPE_ERROR);
                    }
                }
            }
        });
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

    String getMonthForInt(int m) {
        String month = "invalid";
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getMonths();
        if (m >= 0 && m <= 11) {
            month = months[m];
        }
        return month;
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


    private void callNextStep(EditProfileRequest editProfileRequest) {
        if (!TextUtil.isStringNullOrBlank(callFrom)) {
            if (callFrom.equalsIgnoreCase("1")) {
            } else {

            }
        } else {

        }

//        Intent stepSecond = new Intent(context, DashboardBusinessDetails.class);
        Intent stepSecond = new Intent(context, DashboardDocUpload.class);
        if (!TextUtil.isStringNullOrBlank(callFrom)) {
            if (callFrom.equalsIgnoreCase("1")) {
                stepSecond.putExtra("call_from", "1");
                Log.e(TAG, "callNextStep: step 3" );
            }
        }

//        Intent stepSecond = new Intent(context, DashboardDocUpload.class);
        DashboardDocUpload.editProfileRequest = editProfileRequest;
//        DashboardDocUpload.editProfileRequest = editProfileRequest;
        startActivity(stepSecond);
    }
    /******************************************* End Methods *************************************
     */
    /******************************************** Start Interface methods **************************
     */
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

//        sChName = etChName.getText().toString().trim();
//        sChLName = etChLName.getText().toString().trim();
//        sMobile = etMobile.getText().toString().trim();
//        sDOB = etDOB.getText().toString().trim();
        sLandline = etLandline.getText().toString().trim();
        spersonalEmail = etpersonalEmail.getText().toString().trim();
        sOfficialEmail = etOfficialEmail.getText().toString().trim();
        sFirmName = etFirmName.getText().toString().trim();

        sCommunicationAddress = etCommunicationAddress.getText().toString().trim();
        sBusinessAddress = etBusinessAddress.getText().toString().trim();
        sCommunicationLandmark = etCommunicationLandmark.getText().toString().trim();
        sBusinessLandmark = etBusinessLandmark.getText().toString().trim();

        sAddBusinessNature = addBusinessNature.getText().toString().trim();


        // sBusinessNature = etBusinessNature.getText().toString().trim();
        //sBusinessNature = actvBusinessNature.getText().toString().trim();
        sBusinessNature = industryTypeData.get(spBussinessNaturePosition).getText();
        Log.i("send", sBusinessNature);
        sfirmTypeSpinner = spFirmType.getSelectedItem().toString();
        sManpower = etManpower.getText().toString().trim();

        if (stateData != null) {
            sCommunicationState = stateData.get(spCommunicationStatePosition).getId();
        }
        if (cityData != null) {

            sCommunicationCity = cityData.get(spCommunicationCityPosition).getId();
        }
        if (industryTypeData != null) {
            //sCommunicationState = industryTypeData.get(spBussinessNaturePosition).getId();
        }
        sCommunicationDistrict = etCommunicationDistrict.getText().toString().trim();

        sCommunicationPIN = etCommunicationPIN.getText().toString().trim();


        if (stateData != null) {
            sBusinessState = stateData.get(spBusinessStatePosition).getId();
        }

        if (citydataBusiness != null) {
            sBusinessCity = citydataBusiness.get(spBusinessCityPosition).getId();
        }
        sBusinessDistrict = etBusinessDistrict.getText().toString().trim();

        sBusinessPIN = etBusinessPIN.getText().toString().trim();

        if ((sOfficialEmail.length() <= 0 || !isValidEmail(sOfficialEmail))) {
            status = false;
            showWarningSimpleAlertDialog(Constant.TITLE, "Please enter valid Official email");

        }
        if (sFirmName.length() <= 0) {
            status = false;
            showWarningSimpleAlertDialog(Constant.TITLE, getString(R.string.validation_firm_name));
        } else if (sDOB.length() <= 0) {
            status = false;
            showWarningSimpleAlertDialog(Constant.TITLE, getString(R.string.validation_dob));

        } /*else if ((spersonalEmail.length() <= 0 || !isValidEmail(spersonalEmail)) && (mySingleton.getData(Constant.USER_GROUP_ID).equalsIgnoreCase(Constant.POS_GROUP_ID))) {
            status = false;
            showWarningSimpleAlertDialog(Constant.TITLE, "Please enter valid Personal email");

        } */ else if ((spersonalEmail.length() > 0 && !isValidEmail(spersonalEmail))) {
            status = false;
            showWarningSimpleAlertDialog(Constant.TITLE, "Please enter valid Personal email");

        } else if ((sOfficialEmail.length() <= 0 || !isValidEmail(sOfficialEmail)) && (mySingleton.getData(Constant.USER_GROUP_ID).equalsIgnoreCase(Constant.CP_GROUP_ID))) {
            status = false;
            showWarningSimpleAlertDialog(Constant.TITLE, "Please enter valid Official email");

        } else if ((sOfficialEmail.length() > 0 && !isValidEmail(sOfficialEmail)) && (mySingleton.getData(Constant.USER_GROUP_ID).equalsIgnoreCase(Constant.POS_GROUP_ID))) {
            status = false;
            showWarningSimpleAlertDialog(Constant.TITLE, "Please enter valid Official email");

        }

        /*else if(!checkInListIndustry(sBusinessNature)) {
            status = false;
            showWarningSimpleAlertDialog(Constant.TITLE, "Please select Nature of Bussiness from the suggestions only!");

        } */

        else if (sCommunicationPIN.length() != 6) {
            status = false;
            showWarningSimpleAlertDialog(Constant.TITLE, "Please enter PIN code for Communication ");
        } else if (spCommunicationStatePosition == 0) {
            status = false;
            showWarningSimpleAlertDialog(Constant.TITLE, "Please select state for communication");
        } else if (spCommunicationCityPosition == 0) {
            status = false;
            showWarningSimpleAlertDialog(Constant.TITLE, "Please select City for communication");
        } else if (sCommunicationAddress.length() <= 0) {
            status = false;
            showWarningSimpleAlertDialog(Constant.TITLE, getString(R.string.validation_communication_address));
        } else if (sCommunicationDistrict.length() <= 0) {
            status = false;
            showWarningSimpleAlertDialog(Constant.TITLE, "Flat no, Building name for Communication ");
        } else if (sBusinessNature.length() <= 0) {
            status = false;
            showWarningSimpleAlertDialog(Constant.TITLE, "Please enter Business nature ");
        } else if (sAddBusinessNature.length() <= 0 && slctBussinessNature.equalsIgnoreCase("Others")) {
            status = false;
            showWarningSimpleAlertDialog(Constant.TITLE, "Please enter Nature of Business");
        } else if (spersonalEmail.length() > 0 && !isValidEmail(spersonalEmail)) {
            status = false;
            showWarningSimpleAlertDialog(Constant.TITLE, "Please enter valid Personal Mail Id");
        } /* else if (sOfficialEmail.length() <= 0 || !isValidEmail(sOfficialEmail)) {
            status = false;
            showWarningSimpleAlertDialog(Constant.TITLE, "Please enter valid Official Mail Id");

        }*/
        if (status) {
            if (!isRadioChecked) {
                if (sBusinessPIN.length() != 6) {
                    status = false;
                    showWarningSimpleAlertDialog(Constant.TITLE, "Please enter PIN code for Business ");
                } else if (spBusinessStatePosition == 0) {
                    status = false;
                    showWarningSimpleAlertDialog(Constant.TITLE, "Please select state for Business");
                } else if (spBusinessCityPosition == 0) {
                    status = false;
                    showWarningSimpleAlertDialog(Constant.TITLE, "Please select City for Business");
                } else if (sBusinessAddress.length() <= 0) {
                    status = false;
                    showWarningSimpleAlertDialog(Constant.TITLE, "Please enter Locality, Area or Street  for Business");
                } else if (sBusinessDistrict.length() <= 0) {
                    status = false;
                    showWarningSimpleAlertDialog(Constant.TITLE, "Flat no, Building name for Business ");
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

        // getCity();

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
        /*spCommunicationState.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });*/
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

        /*spCommunicationState.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });*/
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
        /*spCommunicationState.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });*/
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
                        Log.i("error3", errorBody.getMessage());
                        // Toast.makeText(context, errorBody.getMessage(), Toast.LENGTH_SHORT).show();
                        if (errorBody.getMessage().contains("invalid")) {
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

    public void getState() {
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
                                    //stateData = response.body().getData().getState();
                                    if (response.body().getData().getState().size() > 0) {
                                        stateData.addAll(response.body().getData().getState());
//                                        displaySateList();
//                                        displaySateListBusiness();
                                    } else {

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
                        if (errorBody.getMessage().contains("invalid")) {
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
                        if (errorBody.getMessage().contains("invalid")) {
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

    public void getIndustryType() {
        industryGroup_id = mySingleton.getData(Constant.USER_GROUP_ID);

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        final Call<IndustryTypeResponse> call = apiService.getIndustyType(mySingleton.getData(Constant.TOKEN_BASE_64), "1");
        call.enqueue(new Callback<IndustryTypeResponse>() {
            @Override
            public void onResponse(Call<IndustryTypeResponse> call, Response<IndustryTypeResponse> response) {

                if (response.isSuccessful()) {
                    if (response.code() >= 200 && response.code() < 700) {
                        if (response.code() == 200) {
                            if (response.body().getStatus() == 1) {
                                if (response.body().getData() != null) {
                                    Gson gson = new Gson();
                                    String json = gson.toJson(response.body(), IndustryTypeResponse.class);
                                    Log.i(TAG, "json " + json);
                                    if (response.body().getData().getIndustryType().size() > 0) {
                                        industryTypeData = new ArrayList<>();
                                        //Log.i("TTT", industryTypeData.toString());                            //IndustryType indsType = new IndustryType();
                                        // indsType.setId("-1");
                                        //indsType.setText("Select Nature of Bussiness");
                                        // industryTypeData.add(indsType);
                                        industryTypeData.addAll(response.body().getData().getIndustryType());
                                        displayIndustryList();
                                        // setAutoCompleteList();

//                                        displaySateList();
//                                        displaySateListBusiness();
                                    } else {

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
                        if (errorBody.getMessage().contains("invalid")) {
                            Logout.Login(context);
                        }
                    } catch (Exception e) {
                        displaySnackBar.DisplaySnackBar(Constant.ERROR_INVALID_JSON, Constant.TYPE_ERROR);

                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<IndustryTypeResponse> call, Throwable t) {

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
                        if (errorBody.getMessage().contains("invalid")) {
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


        kProgressHUD = KProgressHUD.create(context)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .setWindowColor(getResources().getColor(R.color.progressbar_color))
                .show();

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        final Call<SearchStateResponce> call = apiService.getStateCity(mySingleton.getData(Constant.TOKEN_BASE_64), sCommunicationPIN);
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
                                    //showSimpleAlertDialog("");
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
                        if (errorBody.getMessage().contains("invalid")) {
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
                                            if (profileData.getBussiness() == null) {
                                                profileData.setBussiness(new Business());
                                            }
                                            profileData.getBussiness().setState(response.body().getData().getDetails().getState_id());
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
                                    //showSimpleAlertDialog("");
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
                        if (errorBody.getMessage().contains("invalid")) {
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
                        if (errorBody.getMessage().contains("invalid")) {
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
        Log.e(TAG, "onClick: 2 step" );
        editProfileRequest = new EditProfileRequest();
        editProfileRequest.setFirst_name(!TextUtil.isStringNullOrBlank(profileData.getFirst_name()) ? profileData.getFirst_name() : "");
        editProfileRequest.setLast_name(!TextUtil.isStringNullOrBlank(profileData.getLast_name()) ? profileData.getLast_name() : "");
        editProfileRequest.setPhone_number(!TextUtil.isStringNullOrBlank(profileData.getPhone_number()) ? profileData.getPhone_number() : "");
        editProfileRequest.setDob(sDOB);
        editProfileRequest.setLandline_no(sLandline);
        editProfileRequest.setPersonal_email_id(spersonalEmail);
        editProfileRequest.setOfficial_email_id(sOfficialEmail);
        editProfileRequest.setCp_firm_name(sFirmName);

        editProfileRequest.setIndustry_type_other(sAddBusinessNature);

        Communication communicationAddress = new Communication();
        communicationAddress.setAddress(sCommunicationAddress);
        communicationAddress.setState(sCommunicationState);
        mySingleton.saveData("commucicationState", sCommunicationState);
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
        communicationBusiness.setPincode(sBusinessPIN);
        communicationBusiness.setLand_mark(sBusinessLandmark);
        editProfileRequest.setBussiness(communicationBusiness);

        editProfileRequest.setBusiness_type(sBusinessType);
        editProfileRequest.setBusiness_type(sfirmTypeSpinner);
        editProfileRequest.setManpower_strength(sManpower);
        editProfileRequest.setNature_of_business(sBusinessNature);


        Gson gson = new Gson();
        String json = gson.toJson(editProfileRequest, EditProfileRequest.class);
        Log.i(TAG, "Request Data " + json);
        Log.i("rssss", "Request Data " + json);

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

                                    //displaySnackBar.DisplaySnackBar(response.body().getData().getMessage(), Constant.TYPE_ERROR);
                                    callNextStep(editProfileRequest);
                                } else {
                                    displaySnackBar.DisplaySnackBar("Data not found", Constant.TYPE_ERROR);

                                }
                            } else if (response.body().getStatus() == 2) {

                                Log.i(">>>>>>", "onResponse: " + json);
                                if (response.body().getData().getMessage().getPersonal_email_id() != null) {
                                    showWarningSimpleAlertDialog(Constant.TITLE, response.body().getData().getMessage().getPersonal_email_id().get(0));
                                }
                                if (response.body().getData().getMessage().getOfficial_email_id() != null) {
                                    showWarningSimpleAlertDialog(Constant.TITLE, response.body().getData().getMessage().getOfficial_email_id().get(0));
                                }
                            }
                            // callNextStep(editProfileRequest);
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
                        if (errorBody.getMessage().contains("invalid")) {
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

    private boolean checkInListIndustry(String indstry) {

        for (int i = 0; i < industryArray.length; i++) {
            if (indstry.equalsIgnoreCase(industryArray[i].toString())) {
                return true;
            }
        }
        return false;
    }

    /******************************************* End Services ************************************
     */

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
