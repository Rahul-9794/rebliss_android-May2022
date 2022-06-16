package com.rebliss.view.activity.cpdashboardforfos;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rebliss.R;
import com.rebliss.data.perf.MySingleton;
import com.rebliss.domain.constant.Constant;
import com.rebliss.presenter.retrofit.ApiClient;
import com.rebliss.presenter.retrofit.ApiInterface;
import com.rebliss.view.activity.cpdashboardforfos.dialogsmodel.AllGroup;
import com.rebliss.view.activity.cpdashboardforfos.dialogsmodel.CpListResponse;

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


public class CpAcivity extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView.LayoutManager layoutManager;
    public RecyclerView recyclerView, recyclerView1;
    TextView tvTotalActivtiy;
    MySingleton mySingleton;

    private Spinner spinFilter;
    String[] filterArr = {"All", "Pending", "Approved", "Declined", "Under Process"};

    int fosId = 0;
    public String selectedDate = "";
    private SearchView searchViewActivites;

    MyAcivityAdapter myAcivityAdapter;
    public TextView btnSelectFos, tvSelectDate;
    private TextView tvNodatafound;
    private ImageView btnBack;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // TODO for screenshot disability
        // getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE); //This will prevent Screenshots.
        setContentView(R.layout.activity_cp_acivity);
        mySingleton = new MySingleton(this);
        init();
        lisner();
    }


    private void init() {

        recyclerView = findViewById(R.id.tv_recyclerview);
        tvTotalActivtiy = findViewById(R.id.tvTotalActivity);
        tvSelectDate = findViewById(R.id.tvSelectDate);
        btnSelectFos = findViewById(R.id.tv_select);
        tvNodatafound = findViewById(R.id.tvNodatafound);
        btnBack = findViewById(R.id.btnBack);
        btnSelectFos.setOnClickListener(this);
        tvSelectDate.setOnClickListener(this);
        btnBack.setOnClickListener(this);

        spinFilter = findViewById(R.id.spinFilter);
        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, filterArr);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
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


        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


    }

    private void lisner() {


        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = df.format(c);
        selectedDate = formattedDate;
        tvSelectDate.setText("Selected Date: " + selectedDate);
        getacivityliastlist();

        searchViewActivites = findViewById(R.id.searchViewActivites);
        searchViewActivites.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String queryString) {
                      //myAcivityAdapter.getFilter().filter(queryString);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String queryString) {
              //  myAcivityAdapter.getFilter().filter(queryString);
                return false;
            }
        });
    }


    List<com.rebliss.view.activity.cpdashboardforfos.AllGroup> myActList;

    public void getacivityliastlist() {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        final Call<AcivityModel> call = apiService.getcpfiler(fosId, mySingleton.getData(Constant.USER_ID), selectedDate);
        call.enqueue(new Callback<AcivityModel>() {
            @Override
            public void onResponse(Call<AcivityModel> call, Response<AcivityModel> response) {

                if (response.isSuccessful()) {
                    if (response.code() == 200 && response.body() != null && response.body().getData() != null) {

                        if (response.body().getStatus() == 1) {
                            tvTotalActivtiy.setText("Total Activity: " + response.body().getData().getTotal());
                            myActList = response.body().getData().getAllGroups();
                            myAcivityAdapter = new MyAcivityAdapter(CpAcivity.this, response.body().getData().getAllGroups());
                            recyclerView.setAdapter(myAcivityAdapter);
                            recyclerView.setVisibility(View.VISIBLE);
                            tvNodatafound.setVisibility(View.GONE);
                        } else {
                            tvNodatafound.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                            tvTotalActivtiy.setText("Total Activity: 0");
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<AcivityModel> call, Throwable t) {


            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_select:
                opendialog();
                break;
            case R.id.tvSelectDate:
                DialogFragment newFragment = new DatePickerFragment(selectedDate);
                newFragment.show(getSupportFragmentManager(), "DatePicker");
                break;
            case R.id.btnBack:
                onBackPressed();
                break;
        }
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
        getdialoglist(dialog);

        etFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (fosList != null) {
                    List<AllGroup> temp = new ArrayList();
                    for (AllGroup d : fosList) {
                        //or use .equal(text) with you want equal match
                        //use .toLowerCase() for better matches
                        if (d.getFirstName().toLowerCase().contains(charSequence.toString()) || (d.getId() + "").contains(charSequence.toString())) {
                            temp.add(d);
                        }
                    }

                    SelectFosAdapter myAcivityAdapter = new SelectFosAdapter(dialog, CpAcivity.this, Objects.requireNonNull(temp));
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
        final Call<CpListResponse> call = apiService.getcplist(mySingleton.getData(Constant.USER_ID));
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

                        SelectFosAdapter myAcivityAdapter = new SelectFosAdapter(dialog, CpAcivity.this, Objects.requireNonNull(response.body()).getData().getAllGroups());
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
// default fragment
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

            datepic = new DatePickerDialog(requireActivity(), this, year, month, day);
            setMaximumDate();
            return datepic;
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            //            apad.dateString = "" + String.valueOf(day) + " " + String.valueOf(month) + " ";
            //formate Date  in Day Month Formate
            // Date d = new Date(year, month, day);
            Calendar calendar = GregorianCalendar.getInstance();
            calendar.set(Calendar.DAY_OF_MONTH, day);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.YEAR, year);


            Date d = calendar.getTime();

            @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MMM-yyyy");
            //this formate for get Day Like Sunday
//            @SuppressLint("SimpleDateFormat") SimpleDateFormat dateForDay = new SimpleDateFormat(
//                    "EEEE");

            String dateAndTime = dateFormatter.format(d);

            dateSelected = day + "-" + (month + 1) + "-" + year;
//            textView.setText("Selected Date: "+dateSelected);
            ((CpAcivity) requireActivity()).selectedDate = dateSelected;
            ((CpAcivity) getActivity()).tvSelectDate.setText("Selected Date: " + dateSelected);
            ((CpAcivity) getActivity()).getacivityliastlist();


            Calendar cal = Calendar.getInstance();
            cal.setTime(d);


        }

        public void setMaximumDate() {
            datepic.getDatePicker().setMaxDate(System.currentTimeMillis());
        }

    }

    private void filterPending(int operation) {
        List<com.rebliss.view.activity.cpdashboardforfos.AllGroup> tempList = new ArrayList<>();

        if (operation == 0) {
            tempList = myActList;
        } else if (operation == 1) {
            for (com.rebliss.view.activity.cpdashboardforfos.AllGroup a : myActList) {
                if (a.getStatus().equals("Pending") || a.getStatus().equals("Resubmitted")) {
                    tempList.add(a);
                }
            }
        }
        if (operation == 2) {
            for (com.rebliss.view.activity.cpdashboardforfos.AllGroup a : myActList) {
                if (a.getStatus().equals("Approved")) {
                    tempList.add(a);
                }
            }
        }
        if (operation == 3) {
            for (com.rebliss.view.activity.cpdashboardforfos.AllGroup a : myActList) {
                if (a.getStatus().equals("Declined")) {
                    tempList.add(a);
                }
            }
        }

        if (operation == 4) {
            for (com.rebliss.view.activity.cpdashboardforfos.AllGroup a : myActList) {
                if (a.getStatus().equals("Under Process")) {
                    tempList.add(a);
                }
            }
        }


        if (tempList.size() > 0) {
            tvTotalActivtiy.setText("Total Activity: " + tempList.size());
            myAcivityAdapter = new MyAcivityAdapter(CpAcivity.this, tempList);
            recyclerView.setAdapter(myAcivityAdapter);
            recyclerView.setVisibility(View.VISIBLE);
            tvNodatafound.setVisibility(View.GONE);
        } else {
            recyclerView.setVisibility(View.GONE);
            tvNodatafound.setVisibility(View.VISIBLE);
            tvTotalActivtiy.setText("Total Activity: 0");
        }
    }


}
