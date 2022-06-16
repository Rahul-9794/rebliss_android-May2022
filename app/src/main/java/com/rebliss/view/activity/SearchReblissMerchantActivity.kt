package com.rebliss.view.activity

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import com.rebliss.R
import com.rebliss.domain.constant.Constant
import com.rebliss.domain.model.SeachMerchantData
import com.rebliss.view.adapter.SearchMerchantAdapter
import kotlinx.android.synthetic.main.activity_search_rebliss_merchant.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.NullPointerException
import java.util.*

class SearchReblissMerchantActivity : BaseActivity() {
    var selectedDate: String = ""
    var onDateSetListener:DatePickerDialog.OnDateSetListener ?=null
    private var dateselectedfromPicker: String =""
    var statusStringArray = arrayOf("ALL", "Complete", "InComplete", "Initiated")
    var sathiType = arrayOf("Choose Type","Merchant (rBM)","Sathi (rBS)")

    private var mStatus: String? = ""
    private var mSathiType: String? = ""
    private var typeofsathi: String? = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_rebliss_merchant)
        imgBack.setOnClickListener {onBackPressed()}
        callListAPI()
        initAdapter()
        spinnerStatusType()
        spinnerSathiType()
        tvSelectDate.setOnClickListener {
            val calendar = Calendar.getInstance()
        val year = calendar[Calendar.YEAR]
        val month = calendar[Calendar.MONTH]
        val day = calendar[Calendar.DAY_OF_MONTH]
            val datePickerDialog = DatePickerDialog(this,onDateSetListener,year,month,day)
            datePickerDialog.show()
        }
        onDateSetListener = DatePickerDialog.OnDateSetListener{
            datepicker,year,month,dayofMonth ->
            var monthofYear = (month+1).toString()
            var daydate = dayofMonth.toString()

            if(monthofYear.length<2){
                monthofYear = "0"+monthofYear
            }
            if(daydate.length<2){
                daydate = "0"+daydate
            }
            val date = "$daydate/$monthofYear/$year"
            selectedDate = "$year-$monthofYear-$daydate"

            tvSelectDate.setText(date)

            callListAPIs(etSearch.text.toString())

        }
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.toString().length > 2) {
                    callListAPIs(etSearch.text.toString())
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                callListAPIs(etSearch.text.toString())
            }
        })


        btnMyActivitys.setOnClickListener {
            val intent = Intent(this,
                FosMyActivitiesDashboardActivity::class.java)
            intent.putExtra("call_from", "sathirecord")
            startActivity(intent)

        }
    }


    private fun spinnerStatusType() {
        try {


            val spinnerArrayAdapter: ArrayAdapter<String?> = object : ArrayAdapter<String?>(
                this, R.layout.spinner_item_gender, statusStringArray) {
                override fun isEnabled(position: Int): Boolean {
                    return if (position == 400) {
                        false
                    } else {
                        true
                    }
                }
            }

        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item_gender)
        spStatus?.setAdapter(spinnerArrayAdapter)
        spStatus?.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                mStatus = parent.getItemAtPosition(position) as String
                if (mStatus?.equals("Complete")!!) {
                    mStatus = "1"
                } else if (mStatus?.equals("InComplete")!!) {
                    mStatus = "0"
                } else if (mStatus?.contains("Initiated")!!)  {
                    mStatus = "2"
                }
                else{
                    mStatus = ""
                }


                callListAPIs(etSearch.text.toString())
            }


            override fun onNothingSelected(parent: AdapterView<*>?) {}
        })

        }catch (e:NullPointerException)
        {
            Log.e("TAG", "spinnerStatusType: null pointer" )
        }
    }

    private fun spinnerSathiType() {
        val spinnerSathiAdapter: ArrayAdapter<String?> = object : ArrayAdapter<String?>(
            this, R.layout.spinner_item_gender, sathiType) {
            override fun isEnabled(position: Int): Boolean {
                return if (position == 0) {
                    false
                } else {
                    true
                }
            }


        }
        spinnerSathiAdapter.setDropDownViewResource(R.layout.spinner_item_gender)
        spSathiTypes?.setAdapter(spinnerSathiAdapter)
        spSathiTypes?.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {

                mSathiType = parent?.getItemAtPosition(position) as String
                if (mSathiType?.equals("Sathi (rBS)")!!) {
                    typeofsathi = "rBS"
                    callListAPI()
                } else if (mSathiType?.equals("Merchant (rBM)")!!) {
                    typeofsathi = "rBM"
                    callListAPI()
                }
                else{
                    mSathiType = ""
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        })
    }

    private fun callListAPIs(searchText: String) {
        val call = apiService.getMerchantListNew(mySingleton.getData(Constant.USER_ID),typeofsathi, selectedDate, searchText,mStatus)
        call.enqueue(object : Callback<SeachMerchantData> {
            override fun onResponse(call: Call<SeachMerchantData>, response: Response<SeachMerchantData>) {
                kProgressHUD.dismiss();
                if (response.isSuccessful && response.code() == 200) {
                    if (response.body()?.status == 1 && response.body()?.data != null) {
                        (rvMerchant.adapter as? SearchMerchantAdapter)?.updateAdapter(response.body()?.data?.all_groups)
                        txtListItem.text =  "Total :".plus(rvMerchant.adapter?.itemCount.toString())
                        txtNodata.visibility = View.GONE
                    }

                    if (response.body()?.status == 0 && response.body()?.data != null) {
                        (rvMerchant.adapter as? SearchMerchantAdapter)?.updateAdapter(response.body()?.data?.all_groups)
                        txtListItem.text =  "Total :".plus(rvMerchant.adapter?.itemCount.toString())
                        txtNodata.visibility = View.VISIBLE
                        txtNodata.setText("No Data found")
                    }
                }
            }

            override fun onFailure(call: Call<SeachMerchantData>, t: Throwable) {
                kProgressHUD.dismiss();
                t.printStackTrace()
            }
        })

    }


    private fun callListAPI() {
       kProgressHUD.show()
        val typeofsathiii = typeofsathi
       val call = apiService.getMerchantListNew(mySingleton.getData(Constant.USER_ID),typeofsathi, selectedDate,"", mStatus)
        call.enqueue(object : Callback<SeachMerchantData> {
            override fun onResponse(call: Call<SeachMerchantData>, response: Response<SeachMerchantData>) {
                kProgressHUD.dismiss();
                if (response.isSuccessful && response.code() == 200) {
                    if (response.body()?.status == 1 && response.body()?.data != null) {
                        (rvMerchant.adapter as? SearchMerchantAdapter)?.updateAdapter(response.body()?.data?.all_groups)
                         txtListItem.text =  rvMerchant.adapter?.itemCount.toString()
                        txtListItem.text =  "Total :".plus(rvMerchant.adapter?.itemCount.toString())
                        txtNodata.visibility = View.GONE
                    }

                    if (response.body()?.status == 0 && response.body()?.data != null) {
                        (rvMerchant.adapter as? SearchMerchantAdapter)?.updateAdapter(response.body()?.data?.all_groups)
                        txtListItem.text =  "Total :".plus(rvMerchant.adapter?.itemCount.toString())
                        txtNodata.visibility = View.VISIBLE
                        txtNodata.setText("No Data found")

                    }
                }
            }

            override fun onFailure(call: Call<SeachMerchantData>, t: Throwable) {
                Log.e("search", "onFailure: " + t.message)
                kProgressHUD.dismiss();
                t.printStackTrace()
            }
        })
    }

    private fun initAdapter() {
        rvMerchant.adapter = SearchMerchantAdapter(this)
    }
}