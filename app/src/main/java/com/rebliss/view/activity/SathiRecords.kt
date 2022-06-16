package com.rebliss.view.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Base64
import android.util.Base64OutputStream
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import cn.pedant.SweetAlert.SweetAlertDialog
import com.androidbuts.multispinnerfilter.KeyPairBoolData
import com.androidbuts.multispinnerfilter.SpinnerListener
import com.google.gson.Gson
import com.kaopiz.kprogresshud.KProgressHUD
import com.rebliss.utils.App
import com.rebliss.BuildConfig
import com.rebliss.R
import com.rebliss.domain.constant.Constant
import com.rebliss.domain.model.EducationResponse
import com.rebliss.domain.model.ErrorBody
import com.rebliss.domain.model.Occupation.OccupationResponse
import com.rebliss.domain.model.SearchSathiRecordResponse
import com.rebliss.domain.model.Service_Response
import com.rebliss.domain.model.agegroup.AgeGroupResponse
import com.rebliss.domain.model.agegroup.AllGroup
import com.rebliss.domain.model.city.CityResponce
import com.rebliss.domain.model.fileupload.FileUploadResponce
import com.rebliss.domain.model.fileupload.UploadRequest
import com.rebliss.domain.model.profile.Data
import com.rebliss.domain.model.searchstate.SearchStateResponce
import com.rebliss.domain.model.signup.SignupRequest
import com.rebliss.domain.model.signup.SignupResponce
import com.rebliss.domain.model.state.State
import com.rebliss.domain.model.state.StateResponce
import com.rebliss.presenter.helper.DisplaySnackBar
import com.rebliss.presenter.helper.Logout
import com.rebliss.presenter.helper.TextUtil
import com.rebliss.presenter.retrofit.ApiClient
import com.rebliss.presenter.retrofit.ApiInterface
import com.rebliss.view.adapter.UploadRemoveAdapter
import kotlinx.android.synthetic.main.activity_sathi_records.*
import kotlinx.android.synthetic.main.merchant_form.*
import kotlinx.android.synthetic.main.sathi_form.*
import kotlinx.android.synthetic.main.work_profile.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.*
import java.lang.NullPointerException
import java.net.ConnectException
import java.net.NoRouteToHostException
import java.net.SocketTimeoutException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class SathiRecords : BaseActivity() {
    private var educationName: String ? = ""
    private var mEducation: String =""
    private var mAgeRange: String = ""
    private var ageGroup_list_name: String? = ""
    private var mCity: String? = null
    private var mCityId: String? = null
    private var mState: String? = null
    private var mstateId: String? = null
    private val stateData: ArrayList<State> = java.util.ArrayList()
    private var displaySnackBar: DisplaySnackBar? = null
    private var firstName: String? = ""
    private var lastName: String? = ""
    private var phonenumber: String? = ""
    private var storeName: String? = ""
    private var mGender: String? = ""
    private var mGender_sathi: String? = ""
    private var sathi_type: String? = ""
    var allGroupArrayList: List<com.rebliss.domain.model.Occupation.AllGroup>? = null
    var occupationNameArrayList: java.util.ArrayList<String>? = null
    var occupation_name: String? = null
    var allGroupArrayAdapter: ArrayAdapter<String?>? = null
    var selected_cat1: String? = null
    var selected_cat_sathi: String? = null
    var mCurrentPhotoPath: String? = null
    private var mImageUri: Uri? = null
    var genderStringArray = arrayOf("Select Gender", "Male", "Female", "Transgender")
    var sathiTypeArray = arrayOf("Select Record (Merchant/Sathi)","Merchant","Sathi")
    private var profileData: Data? = null
    private lateinit var industryAl: java.util.ArrayList<String>
    private lateinit var industryValueAl: java.util.ArrayList<String>
    private var selected_current_services: String? = ""
    var obtainPathResult = ArrayList<String>()
    var positionForUpload = 0
    private var data: com.rebliss.domain.model.signup.Data? = null
    var shop_image_url: String = ""
    private lateinit var PanAdapter: UploadRemoveAdapter
    private var count:Int= 0
    var allGroupListAge: List<AllGroup>? = null
    var ageGroupNameArrayList:java.util.ArrayList<String?>? = null
    var educationNameArrayList:java.util.ArrayList<String?>? = null
    var allGroupListEducation: List<EducationResponse.Data.AllGroup?>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sathi_records)
        spinnerSathiType()
         getAgeGroup()
        getEducationData()
        pincodeListener()
        spinnerGender()
        datafromEditText()


        getServiceData()
        clicklistener()
        etSathiSearchNumber.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.toString().length == 10) {
                    callSearchSathiRecord(etSathiSearchNumber.text.toString())
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
    }

    private fun getEducationData() {
        val apiService = ApiClient.getClient().create(
            ApiInterface::class.java)
        val responseCall = apiService.educationData
        responseCall.enqueue(object : Callback<EducationResponse?> {
            override fun onResponse(
                call: Call<EducationResponse?>,
                response: Response<EducationResponse?>,
            ) {
                if (response.isSuccessful) {
                    if (response.code() >= 200 && response.code() < 700) {
                        if (response.body() != null) {
                            if (response.body()!!.status == 1) {
                                if (response.body()!!.data != null && response.body()!!.data!!.allGroups!!.size > 0) {
                                    allGroupListEducation = response.body()!!.data!!.allGroups
                                    educationNameArrayList = java.util.ArrayList()
                                    for (i in allGroupListEducation?.indices!!) {
                                        educationName = allGroupListEducation?.get(i)?.educationName
                                        educationNameArrayList?.add(educationName)
                                    }
                                    educationNameArrayList?.add(0, "Select Education")
                                    allGroupArrayAdapter =
                                        object : ArrayAdapter<String?>(this@SathiRecords,
                                            R.layout.spinner_item_gender, educationNameArrayList!!) {
                                            override fun isEnabled(position: Int): Boolean {
                                                return if (position == 0) {
                                                    false
                                                } else {
                                                    true
                                                }
                                            }



                                        }
                                    allGroupArrayAdapter?.setDropDownViewResource(R.layout.spinner_item_gender)
                                    spEducation_sathi.setAdapter(allGroupArrayAdapter)
                                }
                            }
                        }
                    }
                }
            }

            override fun onFailure(call: Call<EducationResponse?>, t: Throwable) {
                Log.e("Tag", "onFailure: " + t.message)
            }
        })
    }


    private fun getAgeGroup() {
        val apiService = ApiClient.getClient().create(
            ApiInterface::class.java)
        val responseCall = apiService.ageGroupDATA
        responseCall.enqueue(object : Callback<AgeGroupResponse?> {
            override fun onResponse(
                call: Call<AgeGroupResponse?>,
                response: Response<AgeGroupResponse?>,
            ) {
                if (response.isSuccessful) {
                    if (response.code() >= 200 && response.code() < 700) {
                        if (response.body() != null) {
                            if (response.body()!!.status == 1) {
                                if (response.body()!!.data != null && response.body()!!.data.allGroups.size > 0) {
                                    allGroupListAge = response.body()!!.data.allGroups
                                    ageGroupNameArrayList = java.util.ArrayList<String?>()
                                    for (i in allGroupListAge?.indices!!) {
                                        ageGroup_list_name =
                                            response.body()!!.data.allGroups[i].name
                                        ageGroupNameArrayList?.add(ageGroup_list_name)
                                    }
                                    ageGroupNameArrayList?.add(0, "Select Age Group")
                                    allGroupArrayAdapter =
                                        object : ArrayAdapter<String?>(this@SathiRecords,
                                            R.layout.spinner_item_gender, ageGroupNameArrayList!!) {
                                            override fun isEnabled(position: Int): Boolean {
                                                return if (position == 0) {
                                                    false
                                                } else {
                                                    true
                                                }
                                            }


                                        }
                                    allGroupArrayAdapter?.setDropDownViewResource(R.layout.spinner_item_gender)
                                    spAgeLimits_sathi.setAdapter(allGroupArrayAdapter)
                                }
                            } else if (response.body()!!.status == 401) {
                                Log.e("TAG", "onResponse: 401" )
                            }
                        }
                    }
                }
            }

            override fun onFailure(call: Call<AgeGroupResponse?>, t: Throwable) {
                if (t is IOException || t is SocketTimeoutException
                    || t is ConnectException || t is NoRouteToHostException
                    || t is SecurityException
                ) {
                }
            }
        })
    }

    private fun callSearchSathiRecord(searchText: String) {
        val call = apiService.getSearchSathiRecords(searchText)

        call.enqueue(object : Callback<SearchSathiRecordResponse> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<SearchSathiRecordResponse>, response: Response<SearchSathiRecordResponse>) {
                kProgressHUD.dismiss()
                if (response.isSuccessful && response.code() == 200) {
                    if (response.body()?.status == 1 && response.body()?.data != null) {

                        val builder = AlertDialog.Builder(this@SathiRecords,R.style.CustomAlertDialog)
                            .create()
                        val view = layoutInflater.inflate(R.layout.customview_layout,null)
                        val  button = view.findViewById<Button>(R.id.dialogDismiss_button)
                        val  description = view.findViewById<TextView>(R.id.txtDesc)
                        val  dataType = view.findViewById<TextView>(R.id.txtDataType)
                        description.text = "Number already registered"
                        dataType.text = response.body()!!.data.type
                        builder.setView(view)
                        builder.setCanceledOnTouchOutside(false)
                        builder.show()
                        button.setOnClickListener {
                            if(dataType.text.equals("rBM")&&response.body()!!.desc.equals("Data Found !")){
                            val intent = Intent(this@SathiRecords, MerchantActivity::class.java)
                            intent.putExtra("sathi_id", response.body()!!.data.id)
                                intent.putExtra("phone_number", response.body()!!.data.phone_number)
                                intent.putExtra("comingfrom", "sathiRecords")
                            startActivity(intent)
                                builder.dismiss()
                            finish()}
                            else
                            {
                                builder.dismiss()
                            }
                        }

                    }

                    if (response.body()?.status == 0 && response.body()?.data != null) {
                        showWarningSimpleAlertDialog(
                            Constant.TITLE,
                           "Number not registered, Please register first."
                        )
                    }
                }
            }

            override fun onFailure(call: Call<SearchSathiRecordResponse>, t: Throwable) {
                kProgressHUD.dismiss()
                t.printStackTrace()
            }
        })

    }
    private fun getServiceData() {
        kProgressHUD.show()
        val call = apiService.getServiceData("1")
        call.enqueue(object : Callback<Service_Response> {
            override fun onResponse(
                call: Call<Service_Response>,
                response: Response<Service_Response>,
            ) {
                kProgressHUD.dismiss()
                if (response.isSuccessful) {
                    if (response.code() >= 200 && response.code() < 700) {
                        if (response.code() == 200) {
                            if (response.body()!!.status == 1) {
                                if (response.body()!!.data != null) {
                                    getHashmapData(response.body()?.data?.industry!!)
                                }
                            }
                        }
                    }
                }
            }

            override fun onFailure(call: Call<Service_Response>, t: Throwable) {
                kProgressHUD.dismiss()
            }
        })
    }
    //multiselect
    fun getHashmapData(industry: List<Service_Response.Data.Industry>) {
        val hashMap: HashMap<String, Int?> = HashMap<String, Int?>() //define empty hashmap
        for (item in industry)
            hashMap.put(item.text, item.id)
        for (key in hashMap.keys) {
            getSpinnerValue(hashMap)
        }
    }
    private fun getSpinnerValue(industryHM: HashMap<String, Int?>) {
        val sortedMapAsc: HashMap<String, Int>? = sortByComparator(industryHM)
        industryAl = ArrayList()
        industryValueAl = ArrayList()
        industryAl = ArrayList()
        industryValueAl = ArrayList()
        industryAl.clear()
        industryValueAl.clear()
        for (i in -1 until (sortedMapAsc?.size!!)) {

            try {
                industryAl.add(sortedMapAsc.keys.toTypedArray()[i].trim { it < ' ' })
                industryValueAl.add(sortedMapAsc[industryAl.get(i)].toString())
            } catch (e: IndexOutOfBoundsException) {
                e.printStackTrace()
            }
        }
        val listArray0: MutableList<KeyPairBoolData> = java.util.ArrayList()
        try {
            for (i in industryAl.indices) {
                val h = KeyPairBoolData()
                h.id = (i + 1).toLong()
                h.name = industryAl.get(i)
                h.isSelected = false
                listArray0.add(h)
            }
        } catch (e: java.lang.NullPointerException) {
            e.printStackTrace()
        }

        spn_currentServicess.setItems(listArray0, -1, SpinnerListener { items ->
            for (i in items.indices) {
                if (items[i].isSelected) {
                    industryAl.add(sortedMapAsc.keys.toTypedArray()[i].trim { it <= ' ' })
                    industryValueAl.add(sortedMapAsc[industryAl.get(i)].toString())
                }
            }
        })
    }
    private fun datafromEditText() {
        firstName = etFirstName.text.toString()
        lastName = etLastName.text.toString()
        phonenumber = etPhoneNumber.text.toString()
        storeName = etStoreNames.text.toString()
    }

    fun pincodeListener() {
        etLocationPinCode.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                if (editable.length == 6) {
                    getStateCity(editable.toString())
                }
            }
        })

        etLocationPinCode_sathi.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                if (editable.length == 6) {
                    getStateCity(editable.toString())
                }
            }
        })
    }

    private fun getStateCity(zipCode: String) {
        val kProgressHUD = KProgressHUD.create(this)
            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
            .setCancellable(false)
            .setAnimationSpeed(2)
            .setDimAmount(0.5f)
            .setWindowColor(resources.getColor(R.color.progressbar_color))
            .show()
        val apiService = ApiClient.getClient().create(ApiInterface::class.java)
        val call = apiService.getStateCity(mySingleton.getData(Constant.TOKEN_BASE_64), zipCode)
        call.enqueue(object : Callback<SearchStateResponce> {
            override fun onResponse(call: Call<SearchStateResponce>, response: Response<SearchStateResponce>) {
                kProgressHUD.dismiss()
                if (response.isSuccessful) {
                    if (response.code() >= 200 && response.code() < 700) {
                        if (response.code() == 200) {
                            if (response.body()!!.data.details != null) {
                                mCity = response.body()!!.data.details.location
                                mstateId = response.body()!!.data.details.state_id
                                mCityId = response.body()!!.data.details.city_id
                                getState(mstateId)
                                getCityBusiness(mstateId)
                            } else {
                                showWarningSimpleAlertDialog("Data not found", "Pincode is not available in database")
                                etLocationPinCode.setText("")
                            }
                        }
                    } else {
                        displaySnackBar?.DisplaySnackBar(Constant.UNEXPECTED, Constant.TYPE_ERROR)
                    }
                }
            }

            override fun onFailure(call: Call<SearchStateResponce>, t: Throwable) {
                kProgressHUD.dismiss()
                if (t != null && (t is IOException || t is SocketTimeoutException
                            || t is ConnectException || t is NoRouteToHostException
                            || t is SecurityException)) {
                    displaySnackBar?.DisplaySnackBar(Constant.NETWIRK_ERROR, Constant.TYPE_ERROR)
                }
            }
        })
    }

    fun getCityBusiness(state_id: String?) {
        val apiService = ApiClient.getClient().create(ApiInterface::class.java)
        val call = apiService.getCity(mySingleton.getData(Constant.TOKEN_BASE_64), state_id)
        call.enqueue(object : Callback<CityResponce> {
            override fun onResponse(call: Call<CityResponce>, response: Response<CityResponce>) {
                if (response.isSuccessful) {
                    if (response.code() >= 200 && response.code() < 700) {
                        if (response.code() == 200) {
                            if (response.body()!!.status == 1) {
                                if (response.body()!!.data != null) {
                                    if (response.body()!!.data.citys.size > 0) {
                                        for (i in response.body()!!.data.citys.indices) {
                                            if (response.body()!!.data.citys[i].id == mCityId) {
                                                mCity = response.body()!!.data.citys[i].s_name
                                              //  etCity.setText(mCity)
                                            }
                                        }
                                    }
                                } else {
                                    displaySnackBar?.DisplaySnackBar("Data not found", Constant.TYPE_ERROR)
                                }
                            }
                        }
                    } else {
                        displaySnackBar?.DisplaySnackBar(Constant.UNEXPECTED, Constant.TYPE_ERROR)
                    }
                } else {
                    try {
                        var errorBody = ErrorBody()
                        val gson = Gson()
                        errorBody = gson.fromJson(response.errorBody()!!.string(), ErrorBody::class.java)
                    } catch (e: java.lang.Exception) {
                        displaySnackBar?.DisplaySnackBar(Constant.ERROR_INVALID_JSON, Constant.TYPE_ERROR)
                        e.printStackTrace()
                    }
                }
            }

            override fun onFailure(call: Call<CityResponce>, t: Throwable) {
                if (t != null && (t is IOException || t is SocketTimeoutException
                            || t is ConnectException || t is NoRouteToHostException
                            || t is SecurityException)) {
                    displaySnackBar?.DisplaySnackBar(Constant.NETWIRK_ERROR, Constant.TYPE_ERROR)
                }
            }
        })
    }
    fun getState(stateId: String?) {
        val apiService = ApiClient.getClient().create(ApiInterface::class.java)
        val call = apiService.getState(mySingleton.getData(Constant.TOKEN_BASE_64))
        call.enqueue(object : Callback<StateResponce> {
            override fun onResponse(call: Call<StateResponce>, response: Response<StateResponce>) {
                if (response.isSuccessful) {
                    if (response.code() >= 200 && response.code() < 700) {
                        if (response.code() == 200) {
                            if (response.body()!!.status == 1) {
                                if (response.body()!!.data != null) {
                                    val gson = Gson()
                                    val json = gson.toJson(response.body(), StateResponce::class.java)
                                    if (response.body()!!.data.state.size > 0) {
                                        stateData.addAll(response.body()!!.data.state)
                                        for (i in stateData.indices) {
                                            if (stateData.get(i).getId() == stateId) {
                                                mState = stateData.get(i).getS_name()
                                           //     etState.setText(mState)
                                            }
                                        }
                                    }
                                } else {
                                    displaySnackBar?.DisplaySnackBar("Data not found", Constant.TYPE_ERROR)
                                }
                            }
                        }
                    } else {
                        displaySnackBar?.DisplaySnackBar(Constant.UNEXPECTED, Constant.TYPE_ERROR)
                    }
                } else {
                    try {
                        var errorBody = ErrorBody()
                        val gson = Gson()
                        errorBody = gson.fromJson(response.errorBody()!!.string(), ErrorBody::class.java)
                    } catch (e: Exception) {
                        displaySnackBar?.DisplaySnackBar(Constant.ERROR_INVALID_JSON, Constant.TYPE_ERROR)
                        e.printStackTrace()
                    }
                }
            }

            override fun onFailure(call: Call<StateResponce>, t: Throwable) {
                if (t != null && (t is IOException || t is SocketTimeoutException
                            || t is ConnectException || t is NoRouteToHostException
                            || t is SecurityException)) {
                    displaySnackBar?.DisplaySnackBar(Constant.NETWIRK_ERROR, Constant.TYPE_ERROR)
                }
            }
        })
    }
    private fun showWarningSimpleAlertDialog(title: String, message: String) {
        SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE).setTitleText(title)
            .setContentText(message)
            .setConfirmClickListener { sweetAlertDialog -> sweetAlertDialog.dismissWithAnimation() }
            .show()
    }
    private fun spinnerSathiType() {
        val spinnerArrayAdapter: ArrayAdapter<String?> = object : ArrayAdapter<String?>(
            this, R.layout.spinner_item, sathiTypeArray) {
            override fun isEnabled(position: Int): Boolean {
                return if (position == 0) {
                    false
                } else {
                    true
                }
            }

         /*   override fun getDropDownView(
                position: Int, convertView: View?,
                parent: ViewGroup?,
            ): View {
                val view = super.getDropDownView(position, convertView, parent)
                val tv = view as TextView
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY)
                    tv.setTypeface(App.LATO_REGULAR)
                } else {
                    tv.setTextColor(Color.BLACK)
                    tv.setTypeface(App.LATO_REGULAR)
                }
                return view
            }*/
        }
        spinnerArrayAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown)
        spSathiType?.setAdapter(spinnerArrayAdapter)
        spSathiType?.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                sathi_type = parent.getItemAtPosition(position) as String
                var occupation_type : String = ""
                if (sathi_type?.equals("Merchant")!!) {
                    sathi_type = "Merchant"
                    occupation_type = "mo"
                    getOccupationApiData(occupation_type)
                    llBasicPersonal_sathi.visibility = View.GONE
                            llBasicPersonal_merchant.visibility = View.VISIBLE
                } else if (sathi_type?.equals("Sathi")!!) {
                    sathi_type = "Sathi"
                    occupation_type ="so"
                    getOccupationApiData(occupation_type)
                    llBasicPersonal_sathi.visibility = View.VISIBLE
                    llBasicPersonal_merchant.visibility = View.GONE

                } else {
                    sathi_type = "Select Merchant/Sathi"
                    llBasicPersonal_sathi.visibility = View.GONE
                    llBasicPersonal_merchant.visibility = View.GONE
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        })
    }
    private fun spinnerGender() {
        val spinnerArrayAdapter: ArrayAdapter<String?> = object : ArrayAdapter<String?>(
            this, R.layout.spinner_item_gender, genderStringArray) {
            override fun isEnabled(position: Int): Boolean {
                return if (position == 0) {
                    false
                } else {
                    true
                }
            }

        }
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item_gender)
        spGenders?.setAdapter(spinnerArrayAdapter)
        spGenders_sathi?.setAdapter(spinnerArrayAdapter)
        spGenders?.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                mGender = parent.getItemAtPosition(position) as String
                if (mGender?.contains("Male")!!) {
                    mGender = "M"
                } else if (mGender?.contains("Female")!!) {
                    mGender = "F"
                } else {
                    mGender = "O"
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        })

        spGenders?.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                mGender_sathi = parent.getItemAtPosition(position) as String
                if (mGender_sathi?.contains("Male")!!) {
                    mGender_sathi = "M"
                } else if (mGender_sathi?.contains("Female")!!) {
                    mGender_sathi = "F"
                } else {
                    mGender_sathi = "O"
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        })
    }
    private fun getOccupationApiData(occupation_type: String) {
        val apiService = ApiClient.getClient().create(ApiInterface::class.java)

        val responseCall = apiService.getOccupationDATA(occupation_type)
        responseCall.enqueue(object : Callback<OccupationResponse?> {
            override fun onResponse(call: Call<OccupationResponse?>, response: Response<OccupationResponse?>) {
                if (response.isSuccessful) {
                    Log.e("TAG", "onResponse: >>>>>>>>>>"+response.body() )
                    if (response.code() >= 200 && response.code() < 700) {
                        if (response.body() != null) {
                            if (response.body()!!.status == 1) {
                                if (response.body()!!.data != null && response.body()!!.data.allGroups.size > 0) {
                                    allGroupArrayList = response.body()!!.data.allGroups
                                    occupationNameArrayList = java.util.ArrayList<String>()
                                    for (i in allGroupArrayList?.indices!!) {
                                        occupation_name = response.body()!!.data.allGroups[i].name
                                        Log.e("TAG", "onResponse: >>>>"+occupation_name)
                                        occupationNameArrayList?.add(occupation_name!!)
                                    }
                                    if(occupation_type.equals("mo")) {
                                        occupationNameArrayList?.add(0, "Select Category")
                                    }
                                    else
                                    {
                                        occupationNameArrayList?.add(0, "Select Occupation")

                                    }
                                    allGroupArrayAdapter = object : ArrayAdapter<String?>(this@SathiRecords,
                                        R.layout.spinner_item_gender, occupationNameArrayList!! as List<String?>) {
                                        override fun isEnabled(position: Int): Boolean {
                                            return if (position == 0) {
                                                false
                                            } else {
                                                true
                                            }
                                        }

                                        override fun getDropDownView(
                                            position: Int, convertView: View?,
                                            parent: ViewGroup?,
                                        ): View {
                                            val view = super.getDropDownView(position, convertView, parent)
                                            val tv = view as TextView
                                            if (position == 0) {
                                                tv.setTextColor(Color.GRAY)
                                                tv.setTypeface(App.LATO_REGULAR)
                                            } else {
                                                tv.setTextColor(Color.BLACK)
                                                tv.setTypeface(App.LATO_REGULAR)
                                            }
                                            return view
                                        }
                                    }
                                    allGroupArrayAdapter?.setDropDownViewResource(R.layout.spinner_item_gender)

                                    spCategory?.adapter = allGroupArrayAdapter
                                    spCategory_sathi?.adapter = allGroupArrayAdapter
                                }
                            } else if (response.body()!!.status == 401) {
                                Log.e("TAG", "onResponse: 401 " )
                            }
                        }
                    }
                }
            }

            override fun onFailure(call: Call<OccupationResponse?>, t: Throwable) {
                if (t is IOException || t is SocketTimeoutException
                    || t is ConnectException || t is NoRouteToHostException
                    || t is SecurityException) {
                    Log.e("TAG", "onFailure: "+t.message )
                }
            }
        })




        spCategory?.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                selected_cat1 = parent.getItemAtPosition(position) as String

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        })
        spCategory_sathi?.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                selected_cat_sathi = parent.getItemAtPosition(position) as String

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        })
        spnOccupation?.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                occupation_name = parent.getItemAtPosition(position) as String

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        })
    }


    private fun clicklistener() {
        icBacks.setOnClickListener { onBackPressed() }

        imgSelfPhotos.setOnClickListener {
            // captureImage()
            if(count == 0) {
                val alertDialog2 = AlertDialog.Builder(
                    this@SathiRecords
                )
                alertDialog2.setTitle("Attention!")
                alertDialog2.setCancelable(false)
                alertDialog2.setMessage("Please Upload Store Photo")
                alertDialog2.setPositiveButton(
                    "OK"
                ) { dialog, which -> // Write your code here to execute after dialog
                    dispatchTakePictureIntentReal(109)
                    count++
                    dialog.cancel()
                }

                alertDialog2.show()


            }
            else
            {
                dispatchTakePictureIntentReal(109)
            }

        }

        btnSubmit.setOnClickListener {
if(isSelfValidated()) {
    val pincode = etLocationPinCode.text.toString()
    val firstName = etFirstName.text.toString()
    val lastName = etLastName.text.toString()
    val mobileNumber = etPhoneNumber.text.toString()
    val storename = etStoreNames.text.toString()
    Log.e("TAG", "clicklistener: "+shop_image_url )
    var shop_photos = shop_image_url


    val selectedcategory = selected_cat1
    var genderData: String = ""
    if (spGenders.selectedItem.equals("Male")) {
        genderData = "M"
    } else if (spGenders.selectedItem.equals("Female")) {
        genderData = "F"
    } else {
        genderData = "O"
    }
    var sathi_types: String = ""
    if (spSathiType.selectedItem.equals("Merchant")) {
        sathi_types = "1"
    } else if (spSathiType.selectedItem.equals("Sathi")) {
        sathi_types = "2"
    } else {
        sathi_types = "3"
    }
    val currentServices = selected_current_services

    Log.e(
        "TAG",
        "clicklistener: " + pincode + "\n" + firstName + "\n" + lastName + "\n" + mobileNumber + "\n"
                + selectedcategory + "\n" + genderData + "\n" + currentServices+ "\n" + sathi_types
    )

    callSignupService(
        pincode, firstName, lastName, mobileNumber,
        currentServices!!, genderData, selectedcategory, storename, shop_photos
    )
}
        }

        btnSubmit_sathi.setOnClickListener {
            if(isSelfValidated_sathi()) {
                val pincode = etLocationPinCode_sathi.text.toString()
                val firstName = etFirstName_sathi.text.toString()
                val lastName = etLastName_sathi.text.toString()
                val mobileNumber = etPhoneNumber_sathi.text.toString()
                val selectedcategory = selected_cat_sathi
                val ageGroup = mAgeRange
                val educations = mEducation
                var genderData: String = ""
                if (spGenders_sathi.selectedItem.equals("Male")) {
                    genderData = "M"
                } else if (spGenders_sathi.selectedItem.equals("Female")) {
                    genderData = "F"
                } else {
                    genderData = "O"
                }
                var sathi_types: String = ""
                if (spSathiType.selectedItem.equals("Merchant")) {
                    sathi_types = "1"
                } else if (spSathiType.selectedItem.equals("Sathi")) {
                    sathi_types = "2"
                } else {
                    sathi_types = "3"
                }
                val currentServices = selected_current_services

                Log.e(
                    "TAG",
                    "clicklistener: " + pincode + "\n" + firstName + "\n" + lastName + "\n" + mobileNumber + "\n"
                            + selectedcategory + "\n" + genderData + "\n" + sathi_types
                )

                callSignupServiceforSathi(
                    pincode, firstName, lastName, mobileNumber,
                    genderData, selectedcategory!!, ageGroup, educations
                )
            }
        }

        spn_currentServicess?.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {

                selected_current_services = parent.getItemAtPosition(position) as String
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        })

        spAgeLimits_sathi.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long,
            ) {
                mAgeRange = parent.getItemAtPosition(position) as String
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        })
        spEducation_sathi.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long,
            ) {
                mEducation = parent.getItemAtPosition(position) as String
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        })

    }

    private fun isSelfValidated_sathi(): Boolean {
        var status = true
        if (etLocationPinCode_sathi.text.toString().equals("") || etLocationPinCode_sathi.text.isEmpty()) {
            status = false
            showWarningSimpleAlertDialog(Constant.TITLE, "Please enter Pincode")
        }

        if (etLocationPinCode_sathi.text.toString().length<6) {
            status = false
            showWarningSimpleAlertDialog(Constant.TITLE, "Please enter valid Pincode")
        }
        if (etPhoneNumber_sathi.text.toString().length<6) {
            status = false
            showWarningSimpleAlertDialog(Constant.TITLE, "Please enter valid Phone Number")
        }

        if (etFirstName_sathi.text.toString().equals("") || etFirstName_sathi.text.isEmpty()) {
            status = false
            showWarningSimpleAlertDialog(Constant.TITLE, "Please enter First Name")

        }
        if (etLastName_sathi.text.toString().equals("") || etFirstName_sathi.text.isEmpty()) {
            status = false
            showWarningSimpleAlertDialog(Constant.TITLE, "Please enter Last Name")
        }
        if (etPhoneNumber_sathi.text.toString().equals("") || etPhoneNumber_sathi.text.isEmpty()) {
            status = false
            showWarningSimpleAlertDialog(Constant.TITLE, "Please enter Phone Number")
        }
        if (spGenders_sathi.selectedItem.toString().trim { it <= ' ' } == "Select Gender") {
            status = false
            showWarningSimpleAlertDialog(Constant.TITLE, "Please Select Gender")
        }

        if (spCategory_sathi.selectedItem.toString().trim { it <= ' ' } == "Select Category") {
            status = false
            showWarningSimpleAlertDialog(Constant.TITLE, "Please Select Category")
        }
        if (spAgeLimits_sathi.selectedItem.toString().trim { it <= ' ' } == "Select Age Group") {
            status = false
            showWarningSimpleAlertDialog(Constant.TITLE, "Please Select Age Group")
        }
        return status
    }

    private fun callSignupServiceforSathi(
        pincode: String,
        firstName: String,
        lastName: String,
        mobileNumber: String,
        genderData: String,
        selectedcategory: String?,
        ageGroup: String,
        education: String
    ) {
        var signupRequest = SignupRequest()
        signupRequest.first_name = firstName
        signupRequest.last_name = lastName
        signupRequest.phone_number = mobileNumber
        signupRequest.device_type = "android"
        signupRequest.device_id = mySingleton.getData(Constant.DEVICE_FCM_TOKEN)
        signupRequest.occupation = selectedcategory
        signupRequest.gender = genderData
        signupRequest.code = mySingleton.getData(Constant.USER_UNIQUE_REF_CODE)
        signupRequest.fos_type = "rBS"
        signupRequest.password = mobileNumber
        signupRequest.confirm_password = mobileNumber
        signupRequest.location_zipcode = pincode
        signupRequest.location_city = mCity
        signupRequest.location_state = mState
        signupRequest.fos_shop_name = storeName
        signupRequest.age_range = ageGroup
        signupRequest.education = education


        val gson = Gson()
        val json = gson.toJson(signupRequest, SignupRequest::class.java)
        Log.i("sathirecords", "json $json")
        val apiService = ApiClient.getClient().create(ApiInterface::class.java)
        val call = apiService.postUserSignupforRBS_BY_RBS(signupRequest)
        call.enqueue(object : Callback<SignupResponce> {
            override fun onResponse(
                call: Call<SignupResponce>,
                response: Response<SignupResponce>,
            ) {

                if (response.isSuccessful) {
                    if (response.code() >= 200 && response.code() < 700) {
                        if (response.code() == 200) {
                            if (response.body()!!.status == 1) {

                                if (response.body()!!.data != null) {
                                    val gson = Gson()
                                    val json = gson.toJson(
                                        response.body(),
                                        SignupResponce::class.java
                                    )
                                    Log.i("sathirecords", "json $json")
                                    data = response.body()!!.data

                                    Toast.makeText(this@SathiRecords, "registered successfully", Toast.LENGTH_SHORT).show()
                                    startActivity(Intent(this@SathiRecords, MyTaskActivity::class.java))



                                } else {
                                    displaySnackBar!!.DisplaySnackBar(
                                        "Data not found",
                                        Constant.TYPE_ERROR
                                    )
                                }
                            } else if (response.body()!!.status == 0) {
                                if (response.body()!!.data.error_message != null)
                                    showWarningSimpleAlertDialog(
                                        Constant.TITLE,
                                        response.body()!!
                                            .data.error_message[0]
                                    )
                            }

                        }
                    } else {
                        displaySnackBar!!.DisplaySnackBar(Constant.UNEXPECTED, Constant.TYPE_ERROR)
                    }
                } else {
                    try {
                        var errorBody = ErrorBody()
                        val gson = Gson()
                        errorBody =
                            gson.fromJson(response.errorBody()!!.string(), ErrorBody::class.java)

                        displaySnackBar!!.DisplaySnackBar(errorBody.message, Constant.TYPE_ERROR)
                        if (errorBody.name.equals(
                                this@SathiRecords.getString(R.string.unAuthorisedUser),
                                ignoreCase = true
                            )
                        ) {
                            Logout.Login(this@SathiRecords)
                        }
                    } catch (e: java.lang.Exception) {

                        e.printStackTrace()
                        Toast.makeText(this@SathiRecords, ""+e.printStackTrace(), Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<SignupResponce>, t: Throwable) {
                kProgressHUD.dismiss()
                if (t != null && (t is IOException || t is SocketTimeoutException
                            || t is ConnectException || t is NoRouteToHostException
                            || t is SecurityException)
                ) {
                    displaySnackBar!!.DisplaySnackBar(Constant.NETWIRK_ERROR, Constant.TYPE_ERROR)
                }
            }
        })
    }

    fun callSignupService(
        pincode: String,
        firstName: String,
        lastName: String,
        mobileNumber: String,
        currentServices: String?,
        genderData: String,
        selectedcategory: String?,
        storeName: String?,
        shop_photos: String?,

    ) {

        var signupRequest = SignupRequest()
        signupRequest.first_name = firstName
        signupRequest.last_name = lastName
        signupRequest.phone_number = mobileNumber
        signupRequest.device_type = "android"
        signupRequest.device_id = mySingleton.getData(Constant.DEVICE_FCM_TOKEN)
        signupRequest.occupation = selectedcategory
        signupRequest.gender = genderData
        signupRequest.code = mySingleton.getData(Constant.USER_UNIQUE_REF_CODE)
        signupRequest.fos_type = "rBM"
        signupRequest.password = mobileNumber
        signupRequest.confirm_password = mobileNumber
        signupRequest.location_zipcode = pincode
        signupRequest.location_city = mCity
        signupRequest.location_state = mState
        signupRequest.fos_shop_name = storeName
        signupRequest.shop_photo = shop_photos
        signupRequest.current_service = currentServices


        val gson = Gson()
        val json = gson.toJson(signupRequest, SignupRequest::class.java)
        Log.i("sathirecords", "json $json")
        val apiService = ApiClient.getClient().create(ApiInterface::class.java)
        val call = apiService.postUserSignupforRBS(signupRequest)
        call.enqueue(object : Callback<SignupResponce> {
            override fun onResponse(
                call: Call<SignupResponce>,
                response: Response<SignupResponce>,
            ) {

                if (response.isSuccessful) {
                    if (response.code() >= 200 && response.code() < 700) {
                        if (response.code() == 200) {
                            if (response.body()!!.status == 1) {

                                if (response.body()!!.data != null) {
                                    val gson = Gson()
                                    val json = gson.toJson(
                                          response.body(),
                                        SignupResponce::class.java
                                    )
                                    Log.i("sathirecords", "json $json")
                                    data = response.body()!!.data
                                    val shop_name = response.body()!!.data.user.shop_name
                                    if (shop_name != null) {
                                        mySingleton.saveData("shop_names", shop_name)
                                    }

                                    Toast.makeText(this@SathiRecords, "registered successfully", Toast.LENGTH_SHORT).show()
                                    startActivity(Intent(this@SathiRecords, MyTaskActivity::class.java))



                                } else {
                                    displaySnackBar!!.DisplaySnackBar(
                                        "Data not found",
                                        Constant.TYPE_ERROR
                                    )
                                }
                            } else if (response.body()!!.status == 0) {
                                if (response.body()!!.data.error_message != null)
                                    showWarningSimpleAlertDialog(
                                    Constant.TITLE,
                                    response.body()!!
                                        .data.error_message[0]
                                )
                            }
                        }
                    } else {
                        displaySnackBar!!.DisplaySnackBar(Constant.UNEXPECTED, Constant.TYPE_ERROR)
                    }
                } else {
                    try {
                        var errorBody = ErrorBody()
                        val gson = Gson()
                        errorBody =
                            gson.fromJson(response.errorBody()!!.string(), ErrorBody::class.java)

                        displaySnackBar!!.DisplaySnackBar(errorBody.message, Constant.TYPE_ERROR)
                        if (errorBody.name.equals(
                                this@SathiRecords.getString(R.string.unAuthorisedUser),
                                ignoreCase = true
                            )
                        ) {
                            Logout.Login(this@SathiRecords)
                        }
                    } catch (e: java.lang.Exception) {

                        e.printStackTrace()
                        Toast.makeText(this@SathiRecords, ""+e.printStackTrace(), Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<SignupResponce>, t: Throwable) {
                kProgressHUD.dismiss()
                if (t != null && (t is IOException || t is SocketTimeoutException
                            || t is ConnectException || t is NoRouteToHostException
                            || t is SecurityException)
                ) {
                    displaySnackBar!!.DisplaySnackBar(Constant.NETWIRK_ERROR, Constant.TYPE_ERROR)
                }
            }
        })
    }


    fun dispatchTakePictureIntentReal(type: Int) {
        CreateFile(type).execute();
    }
    inner class GetBase64Image(var file: List<String>?, var CallType: Int) : AsyncTask<String, String, String>() {
        var extension = ""
        override fun onPreExecute() {
            super.onPreExecute()
            if (positionForUpload == 0) {

            }
        }

        override fun doInBackground(vararg strings: String): String? {
            var base64: String? = ""
            val response = ""
            if (file != null && file!!.size > 0 && !TextUtil.isStringNullOrBlank(file!![positionForUpload])) {
                extension = file!![positionForUpload].substring(file!![positionForUpload].lastIndexOf("."))
                try {
                    base64 = encodeFileToBase64Binary(file!![positionForUpload])
                    Log.i("base", base64!!)
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            }
            return base64
        }

        override fun onPostExecute(s: String?) {
            super.onPostExecute(s)
            s?.let { callPostDocAPI(file!!, CallType, extension, it) }
        }
    }

    fun callPostDocAPI(file: List<String>, CallType: Int, extention: String, base64: String) {
        kProgressHUD.show()

        val uploadRequest = UploadRequest()
        if (!TextUtil.isStringNullOrBlank(base64)) {
            uploadRequest.id_proof = base64
            uploadRequest.id_proof_file_type = extention.replace(".", "")
        }

        val call = apiService.postUploadfile(uploadRequest)

        call.enqueue(object : Callback<FileUploadResponce> {
            override fun onResponse(call: Call<FileUploadResponce>, response: Response<FileUploadResponce>) {
                kProgressHUD.dismiss()
                if (response.isSuccessful && response.code() == 200) {
                    if (response.body()?.status == 1 && response.body()?.data != null) {

                        if (CallType == 109) {
                            if (TextUtil.isStringNullOrBlank(shop_image_url)) {
                                shop_image_url = shop_image_url + response.body()!!.getFile_name()
                                val alertDialog2 = AlertDialog.Builder(
                                    this@SathiRecords
                                )
                                alertDialog2.setTitle("Attention!")
                                alertDialog2.setCancelable(false)
                                alertDialog2.setMessage("Please Upload Merchant Photo")
                                alertDialog2.setPositiveButton(
                                    "OK"
                                ) { dialog, which -> // Write your code here to execute after dialog
                                    dispatchTakePictureIntentReal(109)
                                    dialog.cancel()
                                }

                                alertDialog2.show()


                            } else {
                                shop_image_url = shop_image_url + "," + response.body()!!.getFile_name()
                                val strings: Array<String> = shop_image_url.split(",").toTypedArray()

                            }

                            if (!TextUtil.isStringNullOrBlank(shop_image_url)) {
                                var Pan_images: Array<String> = shop_image_url.split(",".toRegex()).toTypedArray()
                                Log.e("TAG", "onResponse????: "+shop_image_url )
                                PanAdapter = UploadRemoveAdapter(Pan_images, R.layout.doc_upload_remove, this@SathiRecords, Constant.K_SHOP)
                                if (PanAdapter != null) {
                                    PanAdapter.updateData(Pan_images)
                                    rvSelfPhotos.adapter = PanAdapter
                                    rvSelfPhotos.isScrollContainer = false
                                }else {
                                    Log.e("TAG", "onResponse>>>>>: "+shop_image_url )
                                    PanAdapter = UploadRemoveAdapter(Pan_images, R.layout.doc_upload_remove, this@SathiRecords, Constant.K_SHOP)
                                    rvSelfPhotos.adapter = PanAdapter
                                    rvSelfPhotos.isScrollContainer = false
                                }
                            }


                        }



                    }
                }
            }

            override fun onFailure(call: Call<FileUploadResponce>, t: Throwable) {
                kProgressHUD.dismiss()
                t.printStackTrace()
            }
        })
    }
    private fun callCamera(file: File?, type: Int) {
        if (file != null) {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            // Ensure that there's a camera activity to handle the intent
            if (takePictureIntent.resolveActivity(packageManager) != null) {
                // Continue only if the File was successfully created*/
                if (file != null) {
                    val photoURI = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", file)

                    mImageUri = Uri.fromFile(file)
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, type)
                }
            }
        }
    }

    inner class CreateFile(var type: Int) : AsyncTask<String, File, File>() {
        override fun doInBackground(vararg strings: String): File? {
            var file: File? = null
            try {
                val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
                val imageFileName = "JPEG_" + timeStamp + "_"
                val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                file = File.createTempFile(
                    imageFileName,  // prefix /
                    ".jpg",  // suffix /
                    storageDir // directory /
                )
            } catch (e: IOException) {
                e.printStackTrace()
            }

            // Save a file: path for use with ACTION_VIEW intents
            return file
        }

        override fun onPostExecute(file: File?) {
            super.onPostExecute(file)
            if (file != null) {
                mCurrentPhotoPath = file.absolutePath
                callCamera(file, type)
            }
        }
    }
    private fun encodeTobase64(rotatedBitmap: Bitmap): String? {
        val baos = ByteArrayOutputStream()
        rotatedBitmap.compress(Bitmap.CompressFormat.PNG, 15, baos)
        val b = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 109 && resultCode == Activity.RESULT_OK) {

            val f: File? = grabImageFile(true, 80) //true for compression , 80% quality

            if (f != null) {

                obtainPathResult = java.util.ArrayList()
                obtainPathResult.add(f.absolutePath)
                GetBase64Image(obtainPathResult, 109).execute()
            }
        }
    }

    fun grabImageFile(compress: Boolean, quality: Int): File? {
        var returnFile: File? = null
        try {

            //InputStream is = getContentResolver().openInputStream(mImageUri);
            returnFile = File(mImageUri!!.path)
            if (returnFile.exists() && compress) {
                val bmOptions = BitmapFactory.Options()
                val bitmap = BitmapFactory.decodeFile(returnFile.absolutePath, bmOptions)
                val compressedFile: File? = createTemporaryFile("capture_compressed", ".jpg")
                val bos = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, quality, bos)
                val bitmapdata = bos.toByteArray()
                val fos = FileOutputStream(compressedFile)
                fos.write(bitmapdata)
                fos.flush()
                fos.close()
                returnFile.delete()
                returnFile = compressedFile
            }
            //
        } catch (e: java.lang.Exception) {
            Log.e("Image Capture Error","error")
        }
        catch (e:NullPointerException)
        {
            Log.e("TAG", "grabImageFile: Null pointer" )
        }
        return returnFile
    }

    private fun createTemporaryFile(part: String, ext: String): File? {
        var tempDir = Environment.getExternalStorageDirectory()
        tempDir = File(tempDir.absolutePath + "/rebliss/")
        if (!tempDir.exists()) {
            tempDir.mkdir()
        }
        return File.createTempFile(part, ext, tempDir)
    }


    private fun sortByComparator(symptomHM: HashMap<String, Int?>): HashMap<String, Int>? {
        val list: LinkedList<Map.Entry<String, Int?>> = LinkedList<Map.Entry<String, Int?>>(symptomHM.entries)

        // Sorting the list based on values
        Collections.sort(list) { o1: Map.Entry<String, Int?>, o2: Map.Entry<String, Int?> -> o1.value?.compareTo(o2.value!!)!! }

        // Maintaining insertion order with the help of LinkedList
        val sortedMap: HashMap<String, Int> = LinkedHashMap()
        for ((key, value) in list) {
            sortedMap[key] = value!!
        }
        return sortedMap
    }
    private fun isSelfValidated(): Boolean {
        var status = true
        if (etLocationPinCode.text.toString().equals("") || etLocationPinCode.text.isEmpty()) {
            status = false
            showWarningSimpleAlertDialog(Constant.TITLE, "Please enter Pincode")
        }

        if (etLocationPinCode.text.toString().length<6) {
            status = false
            showWarningSimpleAlertDialog(Constant.TITLE, "Please enter valid Pincode")
        }
        if (etPhoneNumber.text.toString().length<6) {
            status = false
            showWarningSimpleAlertDialog(Constant.TITLE, "Please enter valid Phone Number")
        }

        if (etFirstName.text.toString().equals("") || etFirstName.text.isEmpty()) {
            status = false
            showWarningSimpleAlertDialog(Constant.TITLE, "Please enter First Name")

        }
        if (etLastName.text.toString().equals("") || etFirstName.text.isEmpty()) {
            status = false
            showWarningSimpleAlertDialog(Constant.TITLE, "Please enter Last Name")
        }
        if (etPhoneNumber.text.toString().equals("") || etPhoneNumber.text.isEmpty()) {
            status = false
            showWarningSimpleAlertDialog(Constant.TITLE, "Please enter Phone Number")
        }
        if (spGenders.selectedItem.toString().trim { it <= ' ' } == "Select Gender") {
            status = false
            showWarningSimpleAlertDialog(Constant.TITLE, "Please Select Gender")
        }
        if (etStoreNames.text.toString().equals("") || etStoreNames.text.isEmpty()) {
            status = false
            showWarningSimpleAlertDialog(Constant.TITLE, "Please enter Store Name")
        }
        if (spCategory.selectedItem.toString().trim { it <= ' ' } == "Select Category") {
            status = false
            showWarningSimpleAlertDialog(Constant.TITLE, "Please Select Category")
        }
        if (spn_currentServicess.selectedItem.toString().equals("Select Current Services")) {
            status = false
            showWarningSimpleAlertDialog(Constant.TITLE, "Please Select Current Services")
        }
        if (shop_image_url.isBlank()) {
            status = false
            showWarningSimpleAlertDialog(Constant.TITLE, "Please Upload Store Photo")
        }

        val strings: Array<String> = shop_image_url.split(",").toTypedArray()
        if (strings.size < 2) {
            status = false
            showWarningSimpleAlertDialog(Constant.TITLE, "Please add atleast two photo")
        }
        return status
    }

    private fun encodeFileToBase64Binary(fileName: String): String? {
        var inputStream: InputStream? = null //You can get an inputStream using any IO API
        inputStream = FileInputStream(fileName)
        val buffer = ByteArray(8192)
        var bytesRead: Int
        val output = ByteArrayOutputStream()
        val output64 = Base64OutputStream(output, Base64.DEFAULT)
        try {
            while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                output64.write(buffer, 0, bytesRead)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        output64.close()
        return output.toString()
    }


}