package com.rebliss.view.activity.employee;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rebliss.R;
import com.rebliss.data.perf.MySingleton;
import com.rebliss.domain.constant.Constant;
import com.rebliss.domain.model.employee.EmployeeFilter;
import com.rebliss.presenter.helper.DisplaySnackBar;
import com.rebliss.presenter.helper.Network;
import com.rebliss.presenter.retrofit.ApiClient;
import com.rebliss.presenter.retrofit.ApiInterface;
import com.rebliss.view.activity.ActivityLogin;
import com.rebliss.view.activity.cpdashboardforfos.SelectFosForEmployeeAdapter;
import com.rebliss.view.activity.cpdashboardforfos.dialogsmodel.AllGroup;
import com.rebliss.view.activity.cpdashboardforfos.dialogsmodel.CpListResponse;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmployeeAcivity extends AppCompatActivity implements View.OnClickListener {

    private TextView todaytotal, totayacivity, yesterdaytotal, yesterdayacivity, totalacivity, declinedacivity, btnSelectDate, tvPendingActivity, tvApprovedActivity;
    public TextView selectfos;
    private ImageView btnLogout;
    public Integer fosid = 0;
    private String date;
    private String selectedDate = "";
    private MySingleton mySingleton;
    private RecyclerView.LayoutManager layoutManager;
    public RecyclerView recyclerView, recyclerView1;
    private Dialog dialog;
    private Network network;
    private DisplaySnackBar displaySnackBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_acivity);
        network = new Network();
        displaySnackBar = new DisplaySnackBar(this);

        mySingleton = new MySingleton(this);
        lisner();
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = df.format(c);
        date = formattedDate;
        if (network.isNetworkConnected(EmployeeAcivity.this)) {
            getdata(0);
        } else {
            displaySnackBar.DisplaySnackBar(Constant.NETWIRK_ERROR, Constant.TYPE_ERROR);
        }

        Date mydate = new Date(System.currentTimeMillis() - (1000 * 60 * 60 * 24));
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String yestr = dateFormat.format(mydate);
        date = yestr;
        if (network.isNetworkConnected(EmployeeAcivity.this)) {
            getdata(1);


            getdataWithselectedDate();
        } else {
            displaySnackBar.DisplaySnackBar(Constant.NETWIRK_ERROR, Constant.TYPE_ERROR);
        }
        click();
    }

    private void click() {
        selectfos.setOnClickListener(this);
        btnSelectDate.setOnClickListener(this);
        btnLogout.setOnClickListener(this);
    }

    private void lisner() {
        todaytotal = findViewById(R.id.tv_todaytotal);
        totayacivity = findViewById(R.id.tv_todayacivity);
        yesterdaytotal = findViewById(R.id.tv_yesterdaytotal);
        yesterdayacivity = findViewById(R.id.tv_yesterdayacivity);
        totalacivity = findViewById(R.id.tv_totalacivity);
        tvPendingActivity = findViewById(R.id.tvPendingActivity);

        tvApprovedActivity = findViewById(R.id.tvApprovedActivity);
        declinedacivity = findViewById(R.id.tv_declinedacivity);
        selectfos = findViewById(R.id.tv_selectfos);
        btnSelectDate = findViewById(R.id.btnSelectDate);
        btnLogout = findViewById(R.id.btnLogout);


    }

    public void getdata(final int status) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        final Call<EmployeeFilter> call = apiService.getemployeefilter(fosid, mySingleton.getData(Constant.USER_ID), date);
        call.enqueue(new Callback<EmployeeFilter>() {
            @Override
            public void onResponse(Call<EmployeeFilter> call, Response<EmployeeFilter> response) {

                if (response.isSuccessful()) {
                    if (response.code() == 200 && response.body() != null && response.body().getData() != null) {
                        if (status == 0) {
                            todaytotal.setText("Total: " + response.body().getData().getTotalApproved());
                            totayacivity.setText("Active: " + response.body().getData().getTotalDeclined());
                            totalacivity.setText("Total Activity: " + (response.body().getData().getTotalApproved() + response.body().getData().getTotalPending() + response.body().getData().getTotalDeclined() + response.body().getData().getTotalResubmitted()));
                            declinedacivity.setText("Declined: " + response.body().getData().getTotalDeclined());
                            tvPendingActivity.setText("Pending: " + response.body().getData().getTotalPending());
                            tvApprovedActivity.setText("Approved: " + response.body().getData().getTotalApproved());


                        } else if (status == 1) {
                            yesterdaytotal.setText("Total: " + response.body().getData().getTotalApproved());
                            yesterdayacivity.setText("Active: " + response.body().getData().getTotalDeclined());
                        } else if (status == 2) {
                            totalacivity.setText("Total Activity: " + response.body().getData().getTotalApproved());
                            declinedacivity.setText("Declined: " + response.body().getData().getTotalDeclined());
                            tvPendingActivity.setText("Pending: " + response.body().getData().getTotalPending());
                            tvApprovedActivity.setText("Approved: " + response.body().getData().getTotalApproved());
                        }

                    }
                }
            }

            @Override
            public void onFailure(Call<EmployeeFilter> call, Throwable t) {


            }
        });
    }


    public void getdataWithselectedDate() {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        final Call<EmployeeFilter> call = apiService.getemployeefilter(fosid, mySingleton.getData(Constant.USER_ID), selectedDate);
        call.enqueue(new Callback<EmployeeFilter>() {
            @Override
            public void onResponse(Call<EmployeeFilter> call, Response<EmployeeFilter> response) {

                if (response.isSuccessful()) {
                    if (response.code() == 200 && response.body() != null && response.body().getData() != null) {

                        totalacivity.setText("Total Activity: " + response.body().getData().getTotalApproved());
                        declinedacivity.setText("Declined: " + response.body().getData().getTotalDeclined());
                        tvPendingActivity.setText("Pending: " + response.body().getData().getTotalPending());
                        tvApprovedActivity.setText("Approved: " + response.body().getData().getTotalApproved());


                    }
                }
            }

            @Override
            public void onFailure(Call<EmployeeFilter> call, Throwable t) {


            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_selectfos:
                opendialog();
                break;
            case R.id.btnSelectDate:
                DialogFragment newFragment = new DatePickerFragment(selectedDate);
                newFragment.show(getSupportFragmentManager(), "DatePicker");
                break;
            case R.id.btnLogout:
                showSimpleAlertDialog();
                break;
        }
    }

    private void showSimpleAlertDialog() {
        new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE).setTitleText(Constant.TITLE)
                .setContentText("Are you sure! you want to logout")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                        Intent login = new Intent(EmployeeAcivity.this, ActivityLogin.class);
                        String device_token = mySingleton.getData(Constant.DEVICE_FCM_TOKEN);
                        mySingleton.clearData();
                        mySingleton.saveData(Constant.DEVICE_FCM_TOKEN, device_token);
                        login.putExtra(Constant.UNAUTHORISE_TOKEN, "0");
                        login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        login.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        login.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(login);
                        finish();
                    }
                })
                .show();
    }

    private void opendialog() {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_cp);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        dialog.show();

        layoutManager = new LinearLayoutManager(this);
        recyclerView1 = dialog.findViewById(R.id.tv_dialogrecycer);
        EditText etFilter = dialog.findViewById(R.id.etFilter);
        etFilter.setVisibility(View.VISIBLE);
        recyclerView1.setLayoutManager(layoutManager);
        if (network.isNetworkConnected(EmployeeAcivity.this)) {
            getdialoglist(dialog);
        }

        etFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (fosList != null) {
                    List<AllGroup> temp = new ArrayList();
                    for (AllGroup d : fosList) {


                        if (d.getFirstName().toLowerCase().contains(charSequence.toString()) || (d.getId() + "").contains(charSequence.toString())) {
                            temp.add(d);
                        }
                    }

                    SelectFosForEmployeeAdapter myAcivityAdapter = new SelectFosForEmployeeAdapter(dialog, EmployeeAcivity.this, Objects.requireNonNull(temp));
                    recyclerView1.setAdapter(myAcivityAdapter);


                }


            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    List<AllGroup> fosList;

    private void getdialoglist(final Dialog dialog) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        final Call<CpListResponse> call = apiService.fosbyempolyee(mySingleton.getData(Constant.USER_ID));
        call.enqueue(new Callback<CpListResponse>() {
            @Override
            public void onResponse(Call<CpListResponse> call, Response<CpListResponse> response) {

                if (response.isSuccessful()) {
                    if (response.code() == 200) {
                        fosList = response.body().getData().getAllGroups();
                        AllGroup obj = new AllGroup();
                        obj.setFirstName("All");
                        obj.setId(0);
                        fosList.add(0, obj);

                        SelectFosForEmployeeAdapter myAcivityAdapter = new SelectFosForEmployeeAdapter(dialog, EmployeeAcivity.this, Objects.requireNonNull(response.body()).getData().getAllGroups());
                        recyclerView1.setAdapter(myAcivityAdapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<CpListResponse> call, Throwable t) {
                Log.e("sljkljk", "lksjdfkl");
            }
        });
    }


    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        private DatePickerDialog datepic;


        String dateSelected;

        public DatePickerFragment() {

        }

        @SuppressLint("ValidFragment")
        public DatePickerFragment(String dateSelected) {


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
            datepic = new DatePickerDialog(Objects.requireNonNull(getActivity()), this, year, month, day);
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

            ((EmployeeAcivity) requireActivity()).selectedDate = dateSelected;
            ((EmployeeAcivity) getActivity()).btnSelectDate.setText("Select Date: " + dateSelected);
            ((EmployeeAcivity) getActivity()).getdataWithselectedDate();


            Calendar cal = Calendar.getInstance();
            cal.setTime(d);


        }

        public void setMaximumDate() {
            datepic.getDatePicker().setMaxDate(System.currentTimeMillis());
        }

    }


}
