    package com.rebliss.view.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.rebliss.utils.App;
import com.rebliss.R;
import com.rebliss.data.perf.MySingleton;
import com.rebliss.domain.constant.Constant;
import com.rebliss.domain.model.ActivitySelectModel;
import com.rebliss.domain.model.CheckCPOrderModel;
import com.rebliss.domain.model.ErrorBody;
import com.rebliss.domain.model.bankit.BankItBody;
import com.rebliss.domain.model.bankit.BankItResponse;
import com.rebliss.domain.model.categoryresponse.AllCategory;
import com.rebliss.domain.model.categoryresponse.CategoryResponse;
import com.rebliss.domain.model.payment.PaymentDetailResponse;
import com.rebliss.domain.model.profile.Data;
import com.rebliss.domain.model.profile.ProfileResponce;
import com.rebliss.presenter.helper.DisplaySnackBar;
import com.rebliss.presenter.helper.Logout;
import com.rebliss.presenter.helper.Network;
import com.rebliss.presenter.helper.TextUtil;
import com.rebliss.presenter.retrofit.ApiClient;
import com.rebliss.presenter.retrofit.ApiInterface;
import com.rebliss.view.activity.ActivityCP;
import com.rebliss.view.activity.ActivityFOS;
import com.rebliss.view.activity.ActivityPOS;
import com.rebliss.view.activity.FosMyActivitiesDashboardActivity;
import com.rebliss.view.activity.KycActivity;
import com.rebliss.view.activity.RegisterFos;
import com.rebliss.view.activity.WebViewActivity;
import com.rebliss.view.activity.WebViewBankItActivity;
import com.rebliss.view.activity.cpdashboardforfos.CpAcivity;
import com.rebliss.view.activity.cppayment.VerifyPaymentDetailActivity;
import com.rebliss.view.activity.myactivityadd.MyActivityCudel;
import com.rebliss.view.activity.myactivityadd.MyActivityFormActivity;

import java.io.IOException;
import java.net.ConnectException;
import java.net.NoRouteToHostException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentDashboard extends Fragment {

    private static final String TAG = "Dashboard";
    private View root;
    String totalamountforCPDashboard ="";

    private MySingleton mySingleton;
    Intent intent;
    String profile_varified;
    RelativeLayout relManagerFOSUser, relManagerPOSUser, relManagerCPUser, relManagerShop, relManagerBankIt;
    TextView approvalPending, textManagePOSUser, textManageFOSUser, textManageCPUser, Hello, name, approvalmessage, textManageShop, tvPrice;
    LinearLayout relActiveUser, relContinueToPayment;
    private Button btnContinueToPayment;
    FloatingActionButton fab;
    private boolean isFabClicked = false;
    private LinearLayout fabLayout;
    private TextView textAddFos, textAddPos, textRefCode, textStatis, textAddCP, btnDashBoard;
    CardView cardFos, cardPos, cardCP;
    private Network network;
    Data profileData;
    ImageView imgShare;
    LinearLayout liShareLayout;
    DisplaySnackBar displaySnackBar;
    private TextView txtpaymentAreadydone;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.content_dashboard2, container, false);
        Bundle bundle = getActivity().getIntent().getExtras();
        profile_varified = bundle.getString("profile_varified");
        initView();

        viewListener();

        if (network.isNetworkConnected(getContext())) {
            callCheckforOrderofCP();
            callProfileAPI();
           callPaymentDetailTest();
        }
        else
            {
                displaySnackBar.DisplaySnackBar(Constant.UNEXPECTED, Constant.TYPE_ERROR);
            }



        return root;
    }

    private void callCheckforOrderofCP()
    {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        final Call<CheckCPOrderModel> call = apiService.checkOrderforCP(mySingleton.getData(Constant.USER_ID));
        call.enqueue(new Callback<CheckCPOrderModel>() {
            @Override
            public void onResponse(Call<CheckCPOrderModel> call, Response<CheckCPOrderModel> response) {

                if (response.isSuccessful()) {
                    if (response.code() >= 200 && response.code() < 700) {
                        if (response.code() == 200) {
                            if( response.body().getStatus()==1)
                        {
                            totalamountforCPDashboard = response.body().getData().getTotal();
                            tvPrice.setText("\u20B9 " + totalamountforCPDashboard);
                            btnContinueToPayment.setVisibility(View.GONE);
                            txtpaymentAreadydone.setText("Payment already done for " + mySingleton.getData("contactnum"));

                        }
                        }

                    }
                } else {

                    try {
                        ErrorBody errorBody;
                        Gson gson = new Gson();
                        errorBody = gson.fromJson(response.errorBody().string(), ErrorBody.class);
                        Toast.makeText(getContext(), errorBody.getMessage(), Toast.LENGTH_SHORT).show();
                        if (errorBody.getName().equalsIgnoreCase(getActivity().getString(R.string.unAuthorisedUser))) {
                            Logout.Login(getContext());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<CheckCPOrderModel> call, Throwable t) {
                // kProgressHUD.dismiss();
                if (t != null && (t instanceof IOException || t instanceof SocketTimeoutException
                        || t instanceof ConnectException || t instanceof NoRouteToHostException
                        || t instanceof SecurityException)) {
                }
            }
        });

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }


    private void initView() {
        mySingleton = new MySingleton(getContext());
        network = new Network();
        displaySnackBar = new DisplaySnackBar(getActivity());

        cardFos = root.findViewById(R.id.cardFos);
        txtpaymentAreadydone = root.findViewById(R.id.txtpaymentAreadydone);
        cardPos = root.findViewById(R.id.cardPos);
        cardCP = root.findViewById(R.id.cardCP);
        textAddFos = root.findViewById(R.id.textAddFos);
        textAddPos = root.findViewById(R.id.textAddPos);
        textAddCP = root.findViewById(R.id.textAddCP);
        btnDashBoard = root.findViewById(R.id.btnDashBoard);
        fab = root.findViewById(R.id.fab);
        fabLayout = root.findViewById(R.id.fabLayout);

        relManagerFOSUser = root.findViewById(R.id.relManagerFOSUser);
        relManagerPOSUser = root.findViewById(R.id.relManagerPOSUser);
        relManagerCPUser = root.findViewById(R.id.relManagerCPUser);
        relManagerShop = root.findViewById(R.id.relManagerShop);
        relManagerBankIt = root.findViewById(R.id.relManagerBankIt);

        btnContinueToPayment = root.findViewById(R.id.btnContinueToPayment);


        textStatis = root.findViewById(R.id.textStatis);
        liShareLayout = root.findViewById(R.id.liShareLayout);
        imgShare = root.findViewById(R.id.imgShare);
        textRefCode = root.findViewById(R.id.textRefCode);
        Hello = root.findViewById(R.id.Hello);
        name = root.findViewById(R.id.name);
        approvalmessage = root.findViewById(R.id.approvalmessage);
        name.setText(mySingleton.getData(Constant.USER_FIRST_NAME) + " " + mySingleton.getData(Constant.USER_LAST_NAME));


        // imgAddFos = root.findViewById(R.id.imgAddFos);
        textManagePOSUser = root.findViewById(R.id.textManagePOSUser);
        textManageFOSUser = root.findViewById(R.id.textManageFOSUser);
        textManageCPUser = root.findViewById(R.id.textManageCPUser);
        textManageShop = root.findViewById(R.id.textManageShop);
        tvPrice = root.findViewById(R.id.tvPrice);
        //  textHeader = root.findViewById(R.id.textHeader);
        relActiveUser = root.findViewById(R.id.relActiveUser);
        relContinueToPayment = root.findViewById(R.id.relContinueToPayment);
        approvalPending = root.findViewById(R.id.approvalPending);
        approvalPending.setTypeface(App.LATO_REGULAR);
        if (mySingleton.getData(Constant.USER_GROUP_ID).equalsIgnoreCase(Constant.POS_GROUP_ID)) {
            //  imgAddFos.setVisibility(View.GONE);
        }

        //  relActiveUser.setVisibility(View.VISIBLE);
        setConditionalData();
        seFontOnView();
    }


    @SuppressLint("RestrictedApi")
    private void setConditionalData() {
        if (profileData != null && profileData.getGroup_id().equals(Constant.POS_GROUP_ID)) {
            liShareLayout.setVisibility(View.VISIBLE);
            imgShare.setVisibility(View.GONE);
            fab.setVisibility(View.GONE);
        }


        if (profile_varified.equalsIgnoreCase("0")) {
            //   imgAddFos.setVisibility(View.GONE);
            approvalPending.setVisibility(View.VISIBLE);
            approvalmessage.setVisibility(View.VISIBLE);

            // textRefCode.setVisibility(View.VISIBLE);
            Hello.setVisibility(View.VISIBLE);
            name.setVisibility(View.VISIBLE);

            relActiveUser.setVisibility(View.GONE);
            fab.setVisibility(View.GONE);
        } else if (profile_varified.equalsIgnoreCase("1")) {
            approvalPending.setVisibility(View.GONE);

//            Hello.setVisibility(View.GONE);
//            name.setVisibility(View.GONE);
            approvalmessage.setVisibility(View.GONE);
            relActiveUser.setVisibility(View.VISIBLE);
            fab.setVisibility(View.GONE);
            // imgAddFos.setVisibility(View.VISIBLE);
            if (mySingleton.getData(Constant.USER_GROUP_ID).equalsIgnoreCase(Constant.SUPER_CP_GROUP_ID)) {
                relManagerFOSUser.setVisibility(View.VISIBLE);
                relManagerShop.setVisibility(View.GONE);
//                relManagerPOSUser.setVisibility(View.VISIBLE);
                relManagerCPUser.setVisibility(View.VISIBLE);
                textManageShop.setVisibility(View.GONE);
                fab.setVisibility(View.VISIBLE);
                cardFos.setVisibility(View.VISIBLE);
                cardPos.setVisibility(View.GONE);
                cardCP.setVisibility(View.VISIBLE);
                relContinueToPayment.setVisibility(View.GONE);

            }
//visibilty
            if (mySingleton.getData(Constant.USER_GROUP_ID).equalsIgnoreCase(Constant.CP_GROUP_ID)) {
                relManagerFOSUser.setVisibility(View.VISIBLE);
                relContinueToPayment.setVisibility(View.GONE);
                relManagerCPUser.setVisibility(View.GONE);
                relManagerShop.setVisibility(View.GONE);
                fab.setVisibility(View.VISIBLE);
                cardFos.setVisibility(View.VISIBLE);
                cardPos.setVisibility(View.GONE);
                cardCP.setVisibility(View.GONE);

                btnDashBoard.setVisibility(View.VISIBLE);

            }

            if (mySingleton.getData(Constant.USER_GROUP_ID).equalsIgnoreCase(Constant.EMPLOYEE_GROUP_ID)) {
                relManagerFOSUser.setVisibility(View.GONE);
                relManagerPOSUser.setVisibility(View.GONE);
                relManagerCPUser.setVisibility(View.GONE);
                fab.setVisibility(View.GONE);
                cardFos.setVisibility(View.GONE);
                cardPos.setVisibility(View.GONE);
                cardCP.setVisibility(View.GONE);

                btnDashBoard.setVisibility(View.VISIBLE);
            }


            if (mySingleton.getData(Constant.USER_GROUP_ID).equalsIgnoreCase(Constant.FOS_GROUP_ID)) {
                relManagerFOSUser.setVisibility(View.GONE);
                relManagerPOSUser.setVisibility(View.GONE);
                relManagerCPUser.setVisibility(View.GONE);
                relManagerShop.setVisibility(View.GONE);
                relContinueToPayment.setVisibility(View.GONE);
                fab.setVisibility(View.VISIBLE);
                cardFos.setVisibility(View.GONE);
                cardPos.setVisibility(View.VISIBLE);
                cardCP.setVisibility(View.GONE);
                btnDashBoard.setVisibility(View.VISIBLE);
            }
            if (mySingleton.getData(Constant.USER_GROUP_ID).equalsIgnoreCase(Constant.POS_GROUP_ID)) {
                relManagerFOSUser.setVisibility(View.GONE);
                relManagerPOSUser.setVisibility(View.GONE);
                relManagerCPUser.setVisibility(View.GONE);
                fab.setVisibility(View.GONE);
                cardFos.setVisibility(View.GONE);
                cardPos.setVisibility(View.GONE);
                cardCP.setVisibility(View.GONE);

            }
        } else if (profile_varified.equalsIgnoreCase("2")) {
            //  imgAddFos.setVisibility(View.GONE);
            approvalPending.setVisibility(View.VISIBLE);
            approvalmessage.setVisibility(View.VISIBLE);
            relActiveUser.setVisibility(View.GONE);
            if (mySingleton.getData(Constant.USER_GROUP_ID).equalsIgnoreCase(Constant.FOS_GROUP_ID)) {
                relContinueToPayment.setVisibility(View.GONE);
            }
            else
                {
                    relContinueToPayment.setVisibility(View.VISIBLE);
                }


        }
        if (!isFabClicked) {
            fabLayout.setVisibility(View.GONE);
        } else {
            fabLayout.setVisibility(View.VISIBLE);
        }

        if (mySingleton.getData(Constant.USER_GROUP_ID).equalsIgnoreCase(Constant.POS_GROUP_ID)) {
            fab.setVisibility(View.GONE);
        }
        if (profile_varified.equalsIgnoreCase("0")) {
            fab.setVisibility(View.GONE);
        } else if (profile_varified.equalsIgnoreCase("1") && !(mySingleton.getData(Constant.USER_GROUP_ID).equalsIgnoreCase(Constant.POS_GROUP_ID))) {
            fab.setVisibility(View.GONE);
        } else if (profile_varified.equalsIgnoreCase("2")) {
            fab.setVisibility(View.GONE);
        }
    }

    private void viewListener() {
        imgShare.setOnClickListener(
                v -> {
                    if (profileData.getUnique_ref_code() != null) {
                        String appUrl = "http://play.google.com/store/apps/details?id=" + getActivity().getPackageName();
                        String shareBody = "\nAnd Use " + profileData.getUnique_ref_code() + "  to get register with ReBLISS";
                        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                        sharingIntent.setType("text/plain");
                        sharingIntent.putExtra(Intent.EXTRA_TEXT, appUrl + shareBody);
                        startActivity(Intent.createChooser(sharingIntent, "Share Using"));
                    }
                });
        relManagerFOSUser.setOnClickListener(v -> {
            Intent callFOS = new Intent(getContext(), ActivityFOS.class);
            startActivity(callFOS);
        });
        relManagerPOSUser.setOnClickListener(v -> {
            Intent callFOS = new Intent(getContext(), ActivityPOS.class);
            startActivity(callFOS);
        });
        relManagerCPUser.setOnClickListener(v -> {
            Intent callCP = new Intent(getContext(), ActivityCP.class);
            startActivity(callCP);
        });
        relManagerShop.setOnClickListener(v -> {
            Intent callWeb = new Intent(getContext(), WebViewActivity.class);
            startActivity(callWeb);
        });
        relManagerBankIt.setOnClickListener(v -> {
            String action = "", agentId = "", checksum = "", emailId = "", mdId = "";

            action = mySingleton.getData(Constant.BANK_ACTION);
            agentId = mySingleton.getData(Constant.BANK_AGENT_ID);
            checksum = mySingleton.getData(Constant.BANK_CHECKSUM);
            emailId = mySingleton.getData(Constant.BANK_EMAIL);
            mdId = mySingleton.getData(Constant.BANK_MD_ID);

            if (!action.isEmpty() && !action.equals("") &&
                    !agentId.isEmpty() && !agentId.equals("") &&
                    !checksum.isEmpty() && !checksum.equals("") &&
                    !emailId.isEmpty() && !emailId.equals("") &&
                    !mdId.isEmpty() && !mdId.equals("")
            ) {
                Intent callWeb = new Intent(getContext(), WebViewBankItActivity.class);
                callWeb.putExtra("redirection_url_key", action);
                callWeb.putExtra("agentId_key", agentId);
                callWeb.putExtra("checksum_key", checksum);
                callWeb.putExtra("emailId_key", emailId);
                callWeb.putExtra("mdId_key", mdId);
                startActivity(callWeb);
            }


        });

        fab.setOnClickListener(v -> {

            if (!isFabClicked) {
                fabLayout.setVisibility(View.VISIBLE);
                fab.setBackgroundResource(R.drawable.ic_download);
                isFabClicked = true;
            } else {
                isFabClicked = false;
                fabLayout.setVisibility(View.GONE);
                fab.setBackgroundResource(R.mipmap.ic_add_user);
            }
        });

        textAddFos.setOnClickListener(v -> callRegisterFos(2));
        textAddPos.setOnClickListener(v -> {
            callProfileAPI();
            showMyActivtiyDialog();
        });
        textAddCP.setOnClickListener(v -> callRegisterFos(1));

        btnDashBoard.setOnClickListener(view -> {

            if (mySingleton.getData(Constant.USER_GROUP_ID).equalsIgnoreCase(Constant.FOS_GROUP_ID)) {
                startActivity(new Intent(getActivity(), FosMyActivitiesDashboardActivity.class));
            } else if (mySingleton.getData(Constant.USER_GROUP_ID).equalsIgnoreCase(Constant.CP_GROUP_ID)) {
                startActivity(new Intent(getActivity(), CpAcivity.class));
            }
        });

        btnContinueToPayment.setOnClickListener(view -> requireActivity().startActivity(new Intent(getActivity(), VerifyPaymentDetailActivity.class)));

    }

    private void seFontOnView() {

        Hello.setTypeface(App.LATO_REGULAR, Typeface.BOLD);
        name.setTypeface(App.LATO_REGULAR);
        approvalmessage.setTypeface(App.LATO_REGULAR);
        textManagePOSUser.setTypeface(App.LATO_REGULAR);
        textManageFOSUser.setTypeface(App.LATO_REGULAR);
        textManageCPUser.setTypeface(App.LATO_REGULAR);
        textAddFos.setTypeface(App.LATO_REGULAR);
        textAddPos.setTypeface(App.LATO_REGULAR);
        textAddCP.setTypeface(App.LATO_REGULAR);
        textManageShop.setTypeface(App.LATO_REGULAR);
        textStatis.setTypeface(App.LATO_REGULAR);
        textRefCode.setTypeface(App.LATO_BOLD, Typeface.BOLD);
    }

    private void callRegisterFos(int userType) {
        Intent registerFos = new Intent(getContext(), RegisterFos.class);
        registerFos.putExtra("userType", userType + "");
        registerFos.putExtra("userRefferal", mySingleton.getData(Constant.USER_UNIQUE_REF_CODE) + "");
        startActivity(registerFos);
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


                                    if (position == 1) {
                                        ArrayList<String> categoryArray = new ArrayList<>();
                                        categoryArray.add("Select Category");
                                        catList = response.body().getData().getAllCategory();
                                        for (AllCategory datum : response.body().getData().getAllCategory()) {
                                            categoryArray.add(datum.getCategoryName());
                                        }
                                        ArrayAdapter aa = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, categoryArray);
                                        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                                        spinCat.setAdapter(aa);
                                    } else if (position == 2) {
                                        ArrayList<String> categoryArray = new ArrayList<>();
                                        categoryArray.add("Select Sub category");

                                        subCatList = response.body().getData().getAllGroups();
                                        for (AllCategory datum : response.body().getData().getAllGroups()) {
                                            categoryArray.add(datum.getCategoryName());
                                        }
                                        //Creating the ArrayAdapter instance having the country list
                                        ArrayAdapter aa = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, categoryArray);
                                        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        //Setting the ArrayAdapter data on the Spinner
                                        spinCat.setAdapter(aa);
                                    } else if (position == 3) {
                                        ArrayList<String> categoryArray = new ArrayList<>();
                                        categoryArray.add("Select Sub category1");
                                        subCat1List = response.body().getData().getAllGroups();
                                        for (AllCategory datum : response.body().getData().getAllGroups()) {
                                            categoryArray.add(datum.getCategoryName());
                                        }
                                        //Creating the ArrayAdapter instance having the country list
                                        ArrayAdapter aa = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, categoryArray);
                                        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        //Setting the ArrayAdapter data on the Spinner
                                        spinCat.setAdapter(aa);
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
                        ErrorBody errorBody = new ErrorBody();
                        Gson gson = new Gson();
                        errorBody = gson.fromJson(response.errorBody().string(), ErrorBody.class);
                        displaySnackBar.DisplaySnackBar(errorBody.getMessage(), Constant.TYPE_ERROR);

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

    private void showMyActivtiyDialog() {

        final Dialog dialog = new Dialog(requireActivity());
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

        getCategory(spinCat, 1, 0);

        Button btnSubmit = dialog.findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (catSelected == null) {
                    Toast.makeText(getActivity(), "Please select category", Toast.LENGTH_LONG).show();
                    return;
                }

                if (subCatSelected == null) {
                    Toast.makeText(getActivity(), "Please select sub category", Toast.LENGTH_LONG).show();
                    return;
                }

                if (subCat1Selected == null) {
                    Toast.makeText(getActivity(), "Please select sub category1", Toast.LENGTH_LONG).show();
                    return;
                }


                Constant.activitiesList.clear();
                ActivitySelectModel selectModel = new ActivitySelectModel();
                selectModel.setCategory(catSelected);
                selectModel.setSubCategory(subCatSelected);
                selectModel.setSubCategory1(subCat1Selected);

                Constant.activitiesList.add(selectModel);

                Toast.makeText(getActivity(), "Activity " + Constant.activitiesList.size() + " is added successfully.", Toast.LENGTH_LONG).show();
                if (Constant.activitiesList.size() > 0) {
                    if (selectModel.getSubCategory1() == 65) {
                        Intent intent = new Intent(getActivity(), MyActivityCudel.class);
                        requireActivity().startActivity(intent);
                    } else {
                        Intent intent = new Intent(getActivity(), MyActivityFormActivity.class);
                        requireActivity().startActivity(intent);
                    }
                    dialog.dismiss();
                }
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


    public void callProfileAPI() {

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        final Call<ProfileResponce> call = apiService.getProfile(mySingleton.getData(Constant.TOKEN_BASE_64));
        call.enqueue(new Callback<ProfileResponce>() {
            @Override
            public void onResponse(Call<ProfileResponce> call, Response<ProfileResponce> response) {

                if (response.isSuccessful()) {
                    if (response.code() >= 200 && response.code() < 700) {
                        if (response.code() == 200) {

                            if (response.body().getStatus() == 1) {
                                if (response.body().getData() != null) {
                                    Gson gson = new Gson();
                                    String json = gson.toJson(response.body(), ProfileResponce.class);

                                    profileData = response.body().getData();
                                    mySingleton.saveData(Constant.USER_GROUP_ID, response.body().getData().getGroup_id());
                                    mySingleton.saveData(Constant.USER_GROUP_DETAIL_ID, response.body().getData().getGroup_detail_id());
                                    mySingleton.saveData(Constant.USER_UNIQUE_REF_CODE, response.body().getData().getUnique_ref_code());
                                    profile_varified = profileData.getProfile_verified();
                                    setConditionalData();


                                    if (profileData.getProfile_verified().equalsIgnoreCase("1")) {
                                        if (!TextUtil.isStringNullOrBlank(mySingleton.getData(Constant.USER_UNIQUE_REF_CODE))) {
                                            liShareLayout.setVisibility(View.VISIBLE);
                                            textRefCode.setText((!TextUtil.isStringNullOrBlank(mySingleton.getData(Constant.USER_UNIQUE_REF_CODE)) ? mySingleton.getData(Constant.USER_UNIQUE_REF_CODE) : ""));
                                        } else {
                                            liShareLayout.setVisibility(View.GONE);
                                        }
                                    }
                                    if (profileData.getProfile_verified().equalsIgnoreCase("0")) {
                                        startActivity(new Intent(getActivity(), KycActivity.class));
                                        liShareLayout.setVisibility(View.GONE);
                                    }
                                    if (profileData.getProfile_verified().equalsIgnoreCase("2")) {

                                        liShareLayout.setVisibility(View.GONE);

                                    }
                                }
                            }
                        }

                    }
                } else {

                    try {
                        ErrorBody errorBody = new ErrorBody();
                        Gson gson = new Gson();
                        errorBody = gson.fromJson(response.errorBody().string(), ErrorBody.class);
                        //callDisplayErrorCode(Integer.parseInt(errorBody.getStatus()), errorBody.getMessage());
                        // displaySnackBar.DisplaySnackBar(errorBody.getMessage(), Constant.TYPE_ERROR);
                        Toast.makeText(getContext(), errorBody.getMessage(), Toast.LENGTH_SHORT).show();
                        if (errorBody.getName().equalsIgnoreCase(getActivity().getString(R.string.unAuthorisedUser))) {
                            Logout.Login(getContext());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<ProfileResponce> call, Throwable t) {
                // kProgressHUD.dismiss();
                if (t != null && (t instanceof IOException || t instanceof SocketTimeoutException
                        || t instanceof ConnectException || t instanceof NoRouteToHostException
                        || t instanceof SecurityException)) {
                }
            }
        });
    }

    public void callPaymentDetailTest() {
        Log.i(TAG, "comeeeeee:");
        ApiInterface apiService = ApiClient.paymentDetail().create(ApiInterface.class);
        final Call<PaymentDetailResponse> call = apiService.getPaymentDetailTest(mySingleton.getData("commucicationState"),mySingleton.getData(Constant.USER_ID));
        call.enqueue(new Callback<PaymentDetailResponse>() {
            @Override
            public void onResponse(Call<PaymentDetailResponse> call, Response<PaymentDetailResponse> response) {

                if (response.isSuccessful()) {
             //       Log.i(TAG, "onResponse: >>>"+response.body().getData().getTotal());
                    if (response.code() >= 200 && response.code() < 700) {
                        if (response.code() == 200) {
                            if (response.body().getStatus() == 1) {
                                if (response.body().getData() != null) {
                                    if(totalamountforCPDashboard.equals("")) {
                                        tvPrice.setText("\u20B9 " + response.body().getData().getTotal());
                                    }
                                }
                            }
                        }

                    } else {
                        Log.e(TAG, "onResponse: >>>>SF" );
                    }
                } else {

                    try {

                        ErrorBody errorBody = new ErrorBody();
                        Gson gson = new Gson();
                        Log.e(TAG, "onResponse: "+response.errorBody().string() );
                        errorBody = gson.fromJson(response.errorBody().string(), ErrorBody.class);
                        if (errorBody.getName().equalsIgnoreCase(getActivity().getString(R.string.unAuthorisedUser))) {
                            Logout.Login(getContext());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<PaymentDetailResponse> call, Throwable t) {
                // kProgressHUD.dismiss();
                if (t != null && (t instanceof IOException || t instanceof SocketTimeoutException
                        || t instanceof ConnectException || t instanceof NoRouteToHostException
                        || t instanceof SecurityException)) {
                }
            }
        });
    }
    public void callBankItAPI() {

        BankItBody bankItBody = new BankItBody();
        bankItBody.setUserId(mySingleton.getData(Constant.USER_ID));
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        final Call<BankItResponse> call = apiService.postBankItButton(mySingleton.getData(Constant.TOKEN_BASE_64), bankItBody);
        call.enqueue(new Callback<BankItResponse>() {
            @Override
            public void onResponse(Call<BankItResponse> call, Response<BankItResponse> response) {

                if (response.isSuccessful()) {
                    if (response.code() >= 200 && response.code() < 700) {
                        if (response.code() == 200) {
                            if (response.body() != null) {

                                BankItResponse bankItResponse = response.body();
                                Gson gson = new Gson();
                                String json = gson.toJson(bankItResponse, BankItResponse.class);

                                if (!bankItResponse.getAction().equals("") && !bankItResponse.getAction().isEmpty() &&
                                        !bankItResponse.getAgentId().equals("") && !bankItResponse.getAgentId().isEmpty() &&
                                        !bankItResponse.getChecksum().equals("") && !bankItResponse.getChecksum().isEmpty() &&
                                        !bankItResponse.getEmailId().equals("") && !bankItResponse.getEmailId().isEmpty() &&
                                        !bankItResponse.getMdId().equals("") && !bankItResponse.getMdId().isEmpty()
                                ) {
                                    relManagerBankIt.setVisibility(View.VISIBLE);
                                    callSaveBankItData(bankItResponse);
                                } else {
                                    relManagerBankIt.setVisibility(View.GONE);
                                }
                            }
                        }

                    }
                } else {

                    try {
                        ErrorBody errorBody = new ErrorBody();
                        Gson gson = new Gson();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<BankItResponse> call, Throwable t) {
                if (t != null && (t instanceof IOException || t instanceof SocketTimeoutException
                        || t instanceof ConnectException || t instanceof NoRouteToHostException
                        || t instanceof SecurityException)) {
                }
            }
        });
    }

    private void callSaveBankItData(BankItResponse bankItResponse) {

        mySingleton.saveData(Constant.BANK_ACTION, bankItResponse.getAction());
        mySingleton.saveData(Constant.BANK_AGENT_ID, bankItResponse.getAgentId());
        mySingleton.saveData(Constant.BANK_EMAIL, bankItResponse.getEmailId());
        mySingleton.saveData(Constant.BANK_CHECKSUM, bankItResponse.getChecksum());
        mySingleton.saveData(Constant.BANK_MD_ID, bankItResponse.getMdId());

    }

    @Override
    public void onResume() {
        super.onResume();
        callCheckforOrderofCP();
    }
}
