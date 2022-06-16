package com.rebliss.view.activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.rebliss.R;
import com.rebliss.data.perf.MySingleton;
import com.rebliss.domain.constant.Constant;
import com.rebliss.domain.model.ErrorBody;
import com.rebliss.domain.model.myactivity.AllGroup;
import com.rebliss.domain.model.myactivity.DsrDatum;
import com.rebliss.domain.model.myactivity.MyActivityDashboardResponse;
import com.rebliss.domain.model.placedorderstatus.AcceptOrderStatusRequest;
import com.rebliss.domain.model.placedorderstatus.AcceptOrderStatusResponse;
import com.rebliss.onclickinterfaces.RecyclerViewClickInterface;
import com.rebliss.presenter.helper.DisplaySnackBar;
import com.rebliss.presenter.helper.Network;
import com.rebliss.presenter.retrofit.ApiClient;
import com.rebliss.presenter.retrofit.ApiInterface;
import com.rebliss.view.adapter.MyActivityListShowAdapter;
import com.rebliss.view.adapter.MyActivityListShowAdapterDSR;
import com.rebliss.view.adapter.MyDeclineActivityListShowAdapter;

import java.io.IOException;
import java.net.ConnectException;
import java.net.NoRouteToHostException;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FosMyActivitiesDashboardActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView btnMyActivity, btnDeclineActivity, btnSelectDate, tvTotalActivity, tvNodata;
    private RecyclerView rvActivtivities;
    private DisplaySnackBar displaySnackBar;
    private ImageView btnBack;
    private MySingleton mySingleton;
    private Spinner spinFilter;
    private RelativeLayout rlFilter;
    private String[] filterArr = {"All", "Pending", "Approved", "Under process"};
    private Dialog dialog;
    private SearchView searchViewActivites;
    private MyActivityListShowAdapter adapter;
    private MyActivityListShowAdapterDSR adapterforDsr;
    private Network network;

    String subcategoryId = "";
    String comingfrom = "";
    String comefrom = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_fos_my_activities_dashboard);
        Bundle bundle = getIntent().getExtras();
        subcategoryId = bundle.getString(Constant.CATEGORY_ID);
        comingfrom = bundle.getString(Constant.COMING_FROM);
        comefrom = bundle.getString("call_from");
        network = new Network();
        displaySnackBar = new DisplaySnackBar(this);
        mySingleton = new MySingleton(this);

        btnMyActivity = findViewById(R.id.btnMyActivity);
        btnDeclineActivity = findViewById(R.id.btnDeclineActivity);
        btnSelectDate = findViewById(R.id.btnSelectDate);
        tvTotalActivity = findViewById(R.id.tvTotalActivity);

        tvNodata = findViewById(R.id.tvNodata);
        btnBack = findViewById(R.id.btnBack);
        rvActivtivities = findViewById(R.id.rvActivtivities);

        btnMyActivity.setOnClickListener(this);
        btnDeclineActivity.setOnClickListener(this);
        btnSelectDate.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        searchViewActivites = findViewById(R.id.searchViewActivites);
        spinFilter = findViewById(R.id.spinFilter);
        rlFilter = findViewById(R.id.rlSpinFilter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvActivtivities.setLayoutManager(linearLayoutManager);
        if (comingfrom != null && comingfrom.equals("DailyDsr")) {
            getMyActivityTestforDsr();


            searchViewActivites.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String queryString) {
                    try {
                        adapterforDsr.getFilter().filter(queryString);

                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String queryString) {
                    try {
                        adapterforDsr.getFilter().filter(queryString);
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    return false;
                }
            });


            ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, filterArr);
            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinFilter.setAdapter(aa);
            spinFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                    if (myActList != null)
                        filterPendingfordsr(pos);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });


        } else {
            getMyActivityTest();
            searchViewActivites.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String queryString) {
                    try {
                        adapter.getFilter().filter(queryString);
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String queryString) {
                    try {
                        adapter.getFilter().filter(queryString);
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    return false;
                }
            });


            ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, filterArr);
            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinFilter.setAdapter(aa);
            spinFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                    if (myActList != null)
                        filterPending(pos);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });
        }


    }

    private void getMyActivityTest() {
        if (comefrom == null) {
            comefrom = "";
        }
        if (comefrom != null || comefrom.equals("sathirecord")) {
            subcategoryId = "72";
        }
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<MyActivityDashboardResponse> call = apiService.getMyactivityListTest(mySingleton.getData(Constant.USER_ID), selectedDate, subcategoryId);
        call.enqueue(new Callback<MyActivityDashboardResponse>() {
            @Override
            public void onResponse(Call<MyActivityDashboardResponse> call, final Response<MyActivityDashboardResponse> response) {

                if (response.isSuccessful()) {
                    if (response.code() >= 200 && response.code() < 700) {
                        if (response.code() == 200) {
                            if (response.body().getStatus() == 1) {
                                if (response.body().getData() != null) {
                                    rvActivtivities.setVisibility(View.VISIBLE);
                                    tvNodata.setVisibility(View.GONE);

                                    myActList = response.body().getData().getAllGroups();
                                    myActList.addAll(response.body().getData().getCuDelData());

                                    tvTotalActivity.setText("Total Activity: " + (response.body().getData().getTotal() + response.body().getData().getCuDelDataCount()));

                                    adapter = new MyActivityListShowAdapter(FosMyActivitiesDashboardActivity.this, myActList, new RecyclerViewClickInterface() {
                                        @Override
                                        public void onItemClick(int position) {
                                            openActivityPendingDialog(myActList.get(position).getActivityDetailId());
                                            Toast.makeText(FosMyActivitiesDashboardActivity.this, String.valueOf(myActList.get(position).getActivityDetailId()), Toast.LENGTH_SHORT).show();
                                        }

                                        @Override
                                        public void filterItemCount(int activityCount) {
                                            tvTotalActivity.setText("Total Activity: " + activityCount);
                                        }


                                    });

                                    rvActivtivities.setAdapter(adapter);
                                    adapter.notifyDataSetChanged();

                                } else {
                                    displaySnackBar.DisplaySnackBar("Data not found", Constant.TYPE_ERROR);
                                    tvTotalActivity.setText("Total Activity: " + 0);
                                }
                            } else if (response.body().getStatus() == 0) {
                                rvActivtivities.setVisibility(View.GONE);
                                tvNodata.setVisibility(View.VISIBLE);
                                tvTotalActivity.setText("Total Activity: 0");
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
            public void onFailure(Call<MyActivityDashboardResponse> call, Throwable t) {

                if (t != null && (t instanceof IOException || t instanceof SocketTimeoutException
                        || t instanceof ConnectException || t instanceof NoRouteToHostException
                        || t instanceof SecurityException)) {
                    displaySnackBar.DisplaySnackBar(Constant.NETWIRK_ERROR, Constant.TYPE_ERROR);

                }
            }
        });
    }

    private void getMyActivityTestforDsr() {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<MyActivityDashboardResponse> call = apiService.getMyactivityListTest(mySingleton.getData(Constant.USER_ID), selectedDate, subcategoryId);
        call.enqueue(new Callback<MyActivityDashboardResponse>() {
            @Override
            public void onResponse(Call<MyActivityDashboardResponse> call, final Response<MyActivityDashboardResponse> response) {

                if (response.isSuccessful()) {
                    if (response.code() >= 200 && response.code() < 700) {
                        if (response.code() == 200) {
                            if (response.body().getStatus() == 1) {
                                if (response.body().getData() != null) {
                                    rvActivtivities.setVisibility(View.VISIBLE);
                                    tvNodata.setVisibility(View.GONE);

                                    myActListforDsr = response.body().getData().getDsrData();
                                    // myActList.addAll(response.body().getData().getCuDelData());

                                    tvTotalActivity.setText("Total Activity: " + (response.body().getData().getTotal() + response.body().getData().getDsrDataCount()));

                                    adapterforDsr = new MyActivityListShowAdapterDSR(FosMyActivitiesDashboardActivity.this, myActListforDsr, new RecyclerViewClickInterface() {
                                        @Override
                                        public void onItemClick(int position) {
                                            openActivityPendingDialog(myActListforDsr.get(position).getActivityDetailId());
                                            Toast.makeText(FosMyActivitiesDashboardActivity.this, String.valueOf(myActListforDsr.get(position).getActivityDetailId()), Toast.LENGTH_SHORT).show();
                                        }

                                        @Override
                                        public void filterItemCount(int activityCount) {
                                            tvTotalActivity.setText("Total Activity: " + activityCount);
                                        }


                                    });

                                    rvActivtivities.setAdapter(adapterforDsr);
                                    adapterforDsr.notifyDataSetChanged();

                                } else {
                                    displaySnackBar.DisplaySnackBar("Data not found", Constant.TYPE_ERROR);
                                    tvTotalActivity.setText("Total Activity: " + 0);
                                }
                            } else if (response.body().getStatus() == 0) {
                                rvActivtivities.setVisibility(View.GONE);
                                tvNodata.setVisibility(View.VISIBLE);
                                tvTotalActivity.setText("Total Activity: 0");
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
            public void onFailure(Call<MyActivityDashboardResponse> call, Throwable t) {

                if (t != null && (t instanceof IOException || t instanceof SocketTimeoutException
                        || t instanceof ConnectException || t instanceof NoRouteToHostException
                        || t instanceof SecurityException)) {
                    displaySnackBar.DisplaySnackBar(Constant.NETWIRK_ERROR, Constant.TYPE_ERROR);

                }
            }
        });
    }

    public String selectedDate = "";
    private List<AllGroup> myActList;
    private List<DsrDatum> myActListforDsr;

    public void getMyActivity() {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<MyActivityDashboardResponse> call = apiService.getMyactivityList(mySingleton.getData(Constant.USER_ID), selectedDate);
        call.enqueue(new Callback<MyActivityDashboardResponse>() {
            @Override
            public void onResponse(Call<MyActivityDashboardResponse> call, final Response<MyActivityDashboardResponse> response) {

                if (response.isSuccessful()) {
                    if (response.code() >= 200 && response.code() < 700) {
                        if (response.code() == 200) {
                            if (response.body().getStatus() == 1) {
                                if (response.body().getData() != null) {
                                    rvActivtivities.setVisibility(View.VISIBLE);
                                    tvNodata.setVisibility(View.GONE);

                                    myActList = response.body().getData().getAllGroups();
                                    myActList.addAll(response.body().getData().getCuDelData());

                                    tvTotalActivity.setText("Total Activity: " + (response.body().getData().getTotal() + response.body().getData().getCuDelDataCount()));

                                    adapter = new MyActivityListShowAdapter(FosMyActivitiesDashboardActivity.this, myActList, new RecyclerViewClickInterface() {
                                        @Override
                                        public void onItemClick(int position) {
                                            openActivityPendingDialog(myActList.get(position).getActivityDetailId());
                                            Toast.makeText(FosMyActivitiesDashboardActivity.this, String.valueOf(myActList.get(position).getActivityDetailId()), Toast.LENGTH_SHORT).show();
                                        }

                                        @Override
                                        public void filterItemCount(int activityCount) {
                                            tvTotalActivity.setText("Total Activity: " + activityCount);
                                        }


                                    });

                                    rvActivtivities.setAdapter(adapter);
                                    adapter.notifyDataSetChanged();

                                } else {
                                    displaySnackBar.DisplaySnackBar("Data not found", Constant.TYPE_ERROR);
                                    tvTotalActivity.setText("Total Activity: " + 0);
                                }
                            } else if (response.body().getStatus() == 0) {
                                rvActivtivities.setVisibility(View.GONE);
                                tvNodata.setVisibility(View.VISIBLE);
                                tvTotalActivity.setText("Total Activity: 0");
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
            public void onFailure(Call<MyActivityDashboardResponse> call, Throwable t) {

                if (t != null && (t instanceof IOException || t instanceof SocketTimeoutException
                        || t instanceof ConnectException || t instanceof NoRouteToHostException
                        || t instanceof SecurityException)) {
                    displaySnackBar.DisplaySnackBar(Constant.NETWIRK_ERROR, Constant.TYPE_ERROR);

                }
            }
        });
    }


    public void getMyDeclinedActivity() {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<MyActivityDashboardResponse>
                call = apiService.getMyDeclineActivityList(mySingleton.getData(Constant.USER_ID));


        call.enqueue(new Callback<MyActivityDashboardResponse>() {
            @Override
            public void onResponse(Call<MyActivityDashboardResponse> call, Response<MyActivityDashboardResponse> response) {

                if (response.isSuccessful()) {
                    if (response.code() >= 200 && response.code() < 700) {
                        if (response.code() == 200) {
                            if (response.body().getStatus() == 1) {
                                if (response.body().getData() != null) {
                                    rvActivtivities.setVisibility(View.VISIBLE);
                                    tvNodata.setVisibility(View.GONE);

                                    tvTotalActivity.setText("Total Declined Activity: " + response.body().getData().getTotal());
                                    MyDeclineActivityListShowAdapter adapter = new MyDeclineActivityListShowAdapter(FosMyActivitiesDashboardActivity.this, response.body().getData().getAllGroups());
                                    rvActivtivities.setAdapter(adapter);

                                } else {
                                    displaySnackBar.DisplaySnackBar("Data not found", Constant.TYPE_ERROR);

                                }
                            } else if (response.body().getStatus() == 0) {
                                rvActivtivities.setVisibility(View.GONE);
                                tvNodata.setVisibility(View.VISIBLE);
                                tvTotalActivity.setText("Total Activity: 0");
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
            public void onFailure(Call<MyActivityDashboardResponse> call, Throwable t) {

                if (t != null && (t instanceof IOException || t instanceof SocketTimeoutException
                        || t instanceof ConnectException || t instanceof NoRouteToHostException
                        || t instanceof SecurityException)) {
                    displaySnackBar.DisplaySnackBar(Constant.NETWIRK_ERROR, Constant.TYPE_ERROR);

                }
            }
        });
    }


    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        private DatePickerDialog datepic;

        TextView textView;
        String dateSelected;

        public DatePickerFragment() {
        }

        @SuppressLint("ValidFragment")
        public DatePickerFragment(TextView textView, String dateSelected) {

            this.textView = textView;
            this.dateSelected = dateSelected;

        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();


            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            if (dateSelected != null && !dateSelected.isEmpty()) {
                String[] days = dateSelected.split("-");

                day = Integer.parseInt(days[0]);
                month = Integer.parseInt(days[1]) - 1;
                year = Integer.parseInt(days[2]);


            }


            datepic = new DatePickerDialog(requireActivity(), this, year, month, day);
            setMaximumDate();
            return datepic;
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            Calendar calendar = GregorianCalendar.getInstance();
            calendar.set(Calendar.DAY_OF_MONTH, day);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.YEAR, year);


            Date d = calendar.getTime();

            @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MMM-yyyy");

            String dateAndTime = dateFormatter.format(d);

            dateSelected = day + "-" + (month + 1) + "-" + year;
            ((FosMyActivitiesDashboardActivity) requireActivity()).selectedDate = dateSelected;
            ((FosMyActivitiesDashboardActivity) getActivity()).btnSelectDate.setText("Selected Date: " + dateSelected);
            ((FosMyActivitiesDashboardActivity) getActivity()).getMyActivityTest();


            Calendar cal = Calendar.getInstance();
            cal.setTime(d);


        }


        public void setMinimumDate() {
            //minDate.add(Calendar.DATE, 1);
            datepic.getDatePicker().setMinDate(System.currentTimeMillis());
        }

        public void setMaximumDate() {
            datepic.getDatePicker().setMaxDate(System.currentTimeMillis());
        }

    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btnMyActivity:
                // getMyActivity();
                if (comingfrom != null && comingfrom.equals("DailyDsr")) {
                    getMyActivityTestforDsr();
                } else {
                    getMyActivityTest();
                }
                btnMyActivity.setBackground(getResources().getDrawable(R.drawable.borderwith_dark_green_noradii));
                btnDeclineActivity.setBackground(getResources().getDrawable(R.drawable.borderwithaasmani));
                btnDeclineActivity.setTextColor(getResources().getColor(R.color.colorBlack));
                btnMyActivity.setTextColor(getResources().getColor(R.color.black));
                btnSelectDate.setVisibility(View.VISIBLE);
                spinFilter.setVisibility(View.VISIBLE);
                break;
            case R.id.btnDeclineActivity:
                getMyDeclinedActivity();
                btnMyActivity.setBackground(getResources().getDrawable(R.drawable.borderwithaasmani));
                btnDeclineActivity.setBackground(getResources().getDrawable(R.drawable.borderwith_dark_green_noradii));
                btnMyActivity.setTextColor(getResources().getColor(R.color.colorBlack));
                btnDeclineActivity.setTextColor(getResources().getColor(R.color.black));
                btnSelectDate.setVisibility(View.GONE);
                spinFilter.setVisibility(View.GONE);
                rlFilter.setVisibility(View.GONE);
                break;
            case R.id.btnSelectDate:
                if (comingfrom != null && comingfrom.equals("DailyDsr")) {
                } else {
                    DialogFragment newFragment = new DatePickerFragment(btnSelectDate, selectedDate);
                    newFragment.show(getSupportFragmentManager(), "DatePicker");
                }
                break;
            case R.id.btnBack:
                onBackPressed();
                break;
        }

    }


    private void filterPending(int operation) {
        List<AllGroup> tempList = new ArrayList<>();

        if (operation == 0) {
            tempList = myActList;
        } else if (operation == 1) {
            for (AllGroup a : myActList) {
                if (a.getStatus().equals("Pending")) {
                    tempList.add(a);
                }
            }
        }
        if (operation == 2) {
            for (AllGroup a : myActList) {
                if (a.getStatus().equals("Approved")) {
                    tempList.add(a);
                }
            }
        }

        if (operation == 3) {
            for (AllGroup a : myActList) {
                if (a.getStatus().equals("Under Process")) {
                    tempList.add(a);
                }
            }
        }

        if (tempList.size() > 0) {
            tvTotalActivity.setText("Total Activity: " + tempList.size());
            adapter = new MyActivityListShowAdapter(FosMyActivitiesDashboardActivity.this, tempList, new RecyclerViewClickInterface() {
                @Override
                public void onItemClick(int position) {
                    openActivityPendingDialog(myActList.get(position).getActivityDetailId());
                    Toast.makeText(FosMyActivitiesDashboardActivity.this, String.valueOf(myActList.get(position).getActivityDetailId()), Toast.LENGTH_SHORT).show();

                }

                @Override
                public void filterItemCount(int activityCount) {
                    tvTotalActivity.setText("Total Activity: " + activityCount);
                }
            });
            rvActivtivities.setAdapter(adapter);
            rvActivtivities.setVisibility(View.VISIBLE);
            tvNodata.setVisibility(View.GONE);
        } else {
            rvActivtivities.setVisibility(View.GONE);
            tvNodata.setVisibility(View.VISIBLE);
            tvTotalActivity.setText("Total Activity: 0");
        }
    }

    private void filterPendingfordsr(int operation) {
        List<DsrDatum> tempList = new ArrayList<>();

        if (operation == 0) {
            tempList = myActListforDsr;
        } else if (operation == 1) {
            for (DsrDatum a : myActListforDsr) {
                if (a.getStatus().equals("Pending")) {
                    tempList.add(a);
                }
            }
        }
        if (operation == 2) {
            for (DsrDatum a : myActListforDsr) {
                if (a.getStatus().equals("Approved")) {
                    tempList.add(a);
                }
            }
        }

        if (operation == 3) {
            for (DsrDatum a : myActListforDsr) {
                if (a.getStatus().equals("Under Process")) {
                    tempList.add(a);
                }
            }
        }

        if (tempList.size() > 0) {
            tvTotalActivity.setText("Total Activity: " + tempList.size());
            adapterforDsr = new MyActivityListShowAdapterDSR(FosMyActivitiesDashboardActivity.this, tempList, new RecyclerViewClickInterface() {
                @Override
                public void onItemClick(int position) {
                    openActivityPendingDialog(myActListforDsr.get(position).getActivityDetailId());
                    Toast.makeText(FosMyActivitiesDashboardActivity.this, String.valueOf(myActListforDsr.get(position).getActivityDetailId()), Toast.LENGTH_SHORT).show();

                }

                @Override
                public void filterItemCount(int activityCount) {
                    tvTotalActivity.setText("Total Activity: " + activityCount);
                }
            });
            rvActivtivities.setAdapter(adapter);
            rvActivtivities.setVisibility(View.VISIBLE);
            tvNodata.setVisibility(View.GONE);
        } else {
            rvActivtivities.setVisibility(View.GONE);
            tvNodata.setVisibility(View.VISIBLE);
            tvTotalActivity.setText("Total Activity: 0");
        }
    }

    private void openActivityPendingDialog(final int activityID) {
        dialog = new Dialog(FosMyActivitiesDashboardActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_activity_pending);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        dialog.findViewById(R.id.tv_yes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(FosMyActivitiesDashboardActivity.this, "Order Accepted", Toast.LENGTH_SHORT).show();
                if (network.isNetworkConnected(FosMyActivitiesDashboardActivity.this)) {
                    dialog.dismiss();

                    dialogtoacceptOrderId(activityID);


                } else {
                    displaySnackBar.DisplaySnackBar(Constant.NETWIRK_ERROR, Constant.TYPE_ERROR);
                }
            }
        });
        dialog.findViewById(R.id.tv_no).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postAccetedDataApi(activityID, 0, 0);
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    private void dialogtoacceptOrderId(int activityID) {
        dialog = new Dialog(FosMyActivitiesDashboardActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_order_id);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        dialog.findViewById(R.id.tv_Ok).setOnClickListener(v -> {
            if (network.isNetworkConnected(FosMyActivitiesDashboardActivity.this)) {
                EditText etOrderId = dialog.findViewById(R.id.etOrderIdss);
                int orderid = 0;
                try {
                    orderid = Integer.parseInt(etOrderId.getText().toString());
                    postAccetedDataApi(activityID, 1, orderid);
                }catch (NumberFormatException e)
                {
                    Toast.makeText(FosMyActivitiesDashboardActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            } else {
                displaySnackBar.DisplaySnackBar(Constant.NETWIRK_ERROR, Constant.TYPE_ERROR);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void postAccetedDataApi(int activityID, int activityStatus, int order_id) {

        AcceptOrderStatusRequest acceptOrderStatusRequest = new AcceptOrderStatusRequest();
        acceptOrderStatusRequest.setActivity_id(activityID);
        acceptOrderStatusRequest.setActivity_status(activityStatus);
        acceptOrderStatusRequest.setOrder_id(order_id);

        final ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        final Call<AcceptOrderStatusResponse> call = apiService.postAcceptOrderStatusActivityCloneApi(acceptOrderStatusRequest);
        call.enqueue(new Callback<AcceptOrderStatusResponse>() {
            @Override
            public void onResponse(Call<AcceptOrderStatusResponse> call, Response<AcceptOrderStatusResponse> response) {

                Log.e("TAG", "onResponse: " + response.body());
                if (response.isSuccessful()) {
                    Log.e("TAG", "onClickddddd: " + order_id);
                    if (response.code() >= 200 && response.code() < 700) {
                        Log.e("TAG", "onClickddddd: " + order_id);
                        if (response.body() != null) {

                            if (response.body().getStatus() == 1) {
                                Log.e("TAG", "ddddonClickddddd: " + order_id);
                                // getMyActivity();
                                getMyActivityTest();
                                dialog.dismiss();

                            } else {
                                Log.e("TAG", "sdfdsfsfdsfsfs: " + order_id);
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<AcceptOrderStatusResponse> call, Throwable t) {
                dialog.dismiss();
                Log.e("TAG", "onFailure: " + t.getMessage());

            }
        });
    }
}