package com.rebliss.view.activity

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.*
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Base64
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import cn.pedant.SweetAlert.SweetAlertDialog
import com.androidbuts.multispinnerfilter.KeyPairBoolData
import com.androidbuts.multispinnerfilter.SpinnerListener
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.zxing.common.StringUtils
import com.kaopiz.kprogresshud.KProgressHUD
import com.rebliss.BuildConfig
import com.rebliss.R
import com.rebliss.domain.constant.Constant
import com.rebliss.domain.model.*
import com.rebliss.domain.model.Occupation.OccupationResponse
import com.rebliss.domain.model.agegroup.AgeGroupResponse
import com.rebliss.domain.model.agegroup.AllGroup
import com.rebliss.domain.model.categoryresponse.CategoryResponse
import com.rebliss.domain.model.city.CityResponce
import com.rebliss.domain.model.demandpartner.DemandPartnerNameResponse
import com.rebliss.domain.model.profile.Data
import com.rebliss.domain.model.profile.ProfileResponce
import com.rebliss.domain.model.response_for_activity.ActivityResponse
import com.rebliss.domain.model.searchstate.SearchStateResponce
import com.rebliss.domain.model.state.State
import com.rebliss.domain.model.state.StateResponce
import com.rebliss.domain.personal_data_response
import com.rebliss.presenter.helper.DisplaySnackBar
import com.rebliss.presenter.helper.RegexUtils
import com.rebliss.presenter.helper.TextUtil
import com.rebliss.presenter.retrofit.ApiClient
import com.rebliss.presenter.retrofit.ApiInterface
import com.rebliss.utils.App
import com.rebliss.utils.GPSTracker
import com.rebliss.utils.Utils
import com.rebliss.view.activity.Zoho.ZohoActivity
import com.rebliss.view.adapter.PhotoAdapter
import com.rebliss.view.adapter.RefrenceAdapter
import com.rebliss.view.adapter.ShowListAdapter
import id.zelory.compressor.Compressor
import kotlinx.android.synthetic.main.activity_merchant.*
import kotlinx.android.synthetic.main.address_merchant.*
import kotlinx.android.synthetic.main.assignments.*
import kotlinx.android.synthetic.main.kyc_merchant.*
import kotlinx.android.synthetic.main.merchant_tabs.*
import kotlinx.android.synthetic.main.personal_profile_merchant.*
import kotlinx.android.synthetic.main.refrences.*
import kotlinx.android.synthetic.main.services.*
import kotlinx.android.synthetic.main.work_kyc.*
import kotlinx.android.synthetic.main.work_profile.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.MultipartBody.Part.Companion.createFormData
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.lang.String.valueOf
import java.net.ConnectException
import java.net.NoRouteToHostException
import java.net.SocketTimeoutException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class MerchantActivity : BaseActivity() {

    private var reblissStatus: String? = ""
    private var name_of_merchant: String? = ""
    private var email_kyc: String? = ""
    private var work_kyc_doc_no: String? = ""
    private var self_kyc_doc_no: String? = ""
    private var self_kyc_dob: String? = ""
    private var phone_number: String? = ""
    private var selected_interested_services: String? = ""
    private var selected_current_services: String? = ""
    private var selected_activity_type: String? = ""
    private var mGender: String? = ""
    private var document_type: String? = ""
    private var document_type_work: String? = ""
    private var phonenumber: String? = ""
    private var storeName: String? = ""
    private var educationName: String? = ""
    var genderStringArray = arrayOf("Select Option", "Male", "Female", "Transgender")
    var document_type_stringArray =
        arrayOf("Select Option", "Aadhar", "Driving License", "Voter id")

    var merchant_name: String = ""
    var emailKYCrespose: String = ""
    var docnumberWorkKycResponse: String = ""
    var docnumberSelfKycResponse: String = ""
    var self_image_path: String = ""
    var self_kyc_dob_response: String = ""
    private var sathi_code: String? = ""
    private var comingfrom: String? = ""
    private var ageGroup_list_name: String? = ""
    private var category_list_name: String? = ""
    var allGroupListAge: List<AllGroup>? = null
    var ageGroupNameArrayList: ArrayList<String?>? = null
    var categoryRefArrayList: ArrayList<String?>? = null
    var educationNameArrayList: ArrayList<String?>? = null
    var allGroupListEducation: ArrayList<EducationResponse.Data.AllGroup?>? = null
    var allCategoryList: List<Category_Ref_Response.Data.All_category>? = null
    var allGroupArrayAdapter: ArrayAdapter<String?>? = null
    var categotyArrayAdapter: ArrayAdapter<String?>? = null

    private lateinit var symptomAl: java.util.ArrayList<String>
    private lateinit var assignmentAl: java.util.ArrayList<String>
    private lateinit var symptomValueAl: java.util.ArrayList<String>
    private lateinit var assignmentValueAl: java.util.ArrayList<String>
    private val mLocationPinCode: String? = null
    private var mCity: String? = null
    private var mCityId: String? = null
    private var mState: String? = null
    private var mstateId: String? = null
    private var displaySnackBar: DisplaySnackBar? = null
    private val stateData: ArrayList<State> = java.util.ArrayList()
    private var Latitude: Double = 0.0
    private var Longitude: Double = 0.0
    private var address_id: String? = ""
    private val AADHAR_IMAGE_CLICK_REQUEST = 101
    private val INSIDE_IMAGE_CLICK_REQUEST = 102
    private val OUTSIDE_IMAGE_CLICK_REQUEST = 103
    private val SELF_DOCUMENT_IMAGE_CLICK_REQUEST = 104
    private val WORK_DOCUMENT_IMAGE_CLICK_REQUEST = 105
    private val ASSIGNMENT_IMAGE_CLICK_REQUEST = 106
    var photoURI: Uri? = null


    var photoFile: File? = null
    var mCurrentPhotoPath: String? = null
    var f = File("")
    private var Adhar_Url = ""
    private var work_kyc_url = ""
    private var self_photo_url = ""
    private var inside_shop_url = ""
    private var outside_shop_url = ""
    private var AdharAdapter: PhotoAdapter? = null

    var allGroupArrayList: List<com.rebliss.domain.model.Occupation.AllGroup>? = null
    var listofActivityIds: List<com.rebliss.domain.model.response_for_activity.Id>? = null

    var occupationNameArrayList: java.util.ArrayList<String>? = null
    var occupation_name: String? = null
    var selected_cat1: String? = null

    var insideImage = java.util.ArrayList<File>()
    var selfPhoto = java.util.ArrayList<File>()
    var outsideImage = java.util.ArrayList<File>()
    var self_docImage = java.util.ArrayList<File>()
    var work_docImage = java.util.ArrayList<File>()
    var assignmentActitvityImage = java.util.ArrayList<File>()

    private var spUploadOptionPosition = ""
    private var spUploadOptionPositionwork = ""

    private var mYear = 0
    private var mMonth: Int = 0
    private var mDay: Int = 0
    private var date = ""
    private var selectedDate = ""

    private var demandpartnerData: DemandPartnerNameResponse.Data? = null
    var demandPartnerArray = java.util.ArrayList<String>()
    private var demandNameList: ArrayList<DemandPartnerNameResponse.AllGroup>? = null
    var hashMap: HashMap<String, Int?>? = null
    private var mImageUri: Uri? = null
    private var profileData: Data? = null
    var gstPicFile: File? = null
    var panPicFile: File? = null
    private var gstvalue = 0
    private var names: String? = null
    private val GST = 101
    private val CAMERA_PIC_REQUEST_GST_PIC = 504
    private val GALLERY_PIC_REQUEST_GST_PIC = 505
    private val GALLERY_PIC_REQUEST_PAN_PIC = 107
    private val CAMERA_PIC_REQUEST_PAN_PIC = 506
    var currentPhotoPath = ""
    var fileAbsolutePath = ""
    var order_id: String = ""
    var outsideimage = java.util.ArrayList<File>()
    private var mobileNumberComing: String? = ""


    override fun onBackPressed() {
        SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE).setTitleText("Discard Changes")
            .setContentText("Changes will not be saved. Do you want to proceed?")
            .setConfirmText("Discard")
            .setCancelText("Cancel")
            .setCancelClickListener { it.dismissWithAnimation() }
            .setConfirmClickListener { super.onBackPressed() }
            .show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_merchant)

        initView()
        initListeners()
        rvRefrenceList.adapter = RefrenceAdapter(this)
        getServiceDataforInterested()
        getRefrenceData()
        pincodeListener()
        getLocationData()
        spinnerDocumentType()
        getDemandPartner()


    }

    // Demand partner
    private fun getDemandPartner() {
        kProgressHUD.show()
        val apiService = ApiClient.getClient().create(ApiInterface::class.java)
        val call = apiService.getDemandPartnerNameforSathi(mobileNumberComing)
        call.enqueue(object : Callback<DemandPartnerNameResponse?> {
            override fun onResponse(
                call: Call<DemandPartnerNameResponse?>,
                response: Response<DemandPartnerNameResponse?>,
            ) {
                kProgressHUD.dismiss()
                if (response.isSuccessful) {
                    if (response.code() >= 200 && response.code() < 700) {
                        if (response.code() == 200) {
                            assert(response.body() != null)
                            if (response.body()!!.status == 1) {
                                if (response.body()!!.data != null) {
                                    val gson = Gson()
                                    val json = gson.toJson(response.body(),
                                        DemandPartnerNameResponse::class.java)
                                    Log.i("TAG", "json $json")
                                    demandpartnerData = response.body()!!.data
                                    demandPartnerArray.clear()
                                    demandPartnerArray.add("Select Demand Partner")
                                    demandNameList =
                                        response.body()!!.data.allGroups as ArrayList<DemandPartnerNameResponse.AllGroup>?
                                    for (datum in demandNameList!!) {
                                        demandPartnerArray.add(datum.categoryName)
                                        demandPartnerArray.add(datum.categoryId.toString())
                                    }
                                    getHashmapDataforAssignment(response.body()?.data?.allGroups!!)
                                } else {
                                    displaySnackBar!!.DisplaySnackBar("Data not found",
                                        Constant.TYPE_ERROR)
                                }
                            }

                            if (response.body()!!.status == 0) {
                                val builder = AlertDialog.Builder(this@MerchantActivity,
                                    R.style.CustomAlertDialog)
                                    .create()
                                val view = layoutInflater.inflate(R.layout.customview_layout, null)
                                val button = view.findViewById<Button>(R.id.dialogDismiss_button)
                                val description = view.findViewById<TextView>(R.id.txtDesc)
                                description.text =
                                    "All demand partners activities was already done on this store"
                                builder.setView(view)
                                builder.setCanceledOnTouchOutside(false)
                                builder.show()

                                button.setOnClickListener {
                                    if (response.body()!!.desc.equals("All Activity details are matched !")) {
                                        val intent = Intent(this@MerchantActivity,
                                            MyTaskActivity::class.java)
                                        startActivity(intent)
                                        builder.dismiss()
                                        finish()
                                    } else {
                                        builder.dismiss()
                                    }
                                }
                            }
                        }
                    } else {
                        displaySnackBar!!.DisplaySnackBar(Constant.UNEXPECTED, Constant.TYPE_ERROR)
                    }
                } else {
                    try {
                        val errorBody: ErrorBody
                        val gson = Gson()
                        assert(response.errorBody() != null)
                        errorBody =
                            gson.fromJson(response.errorBody()!!.string(), ErrorBody::class.java)
                        displaySnackBar!!.DisplaySnackBar(errorBody.message, Constant.TYPE_ERROR)
                    } catch (e: java.lang.Exception) {
                        displaySnackBar!!.DisplaySnackBar(Constant.ERROR_INVALID_JSON,
                            Constant.TYPE_ERROR)
                        e.printStackTrace()
                    }
                }
            }

            override fun onFailure(call: Call<DemandPartnerNameResponse?>, t: Throwable) {
                kProgressHUD.dismiss()
                Log.e("TAG", "onFailure: " + t.message)
            }
        })
    }

    fun getHashmapDataforAssignment(assignment_demandPartner: List<DemandPartnerNameResponse.AllGroup>) {
        hashMap = HashMap<String, Int?>()
        for (item in assignment_demandPartner)
            hashMap?.put(item.categoryName, item.categoryId)
        for (key in hashMap?.keys!!) {
            getSpinnervalueforassignment(hashMap!!)
        }
    }

    private fun getSpinnervalueforassignment(assignmentHM: HashMap<String, Int?>) {
        val sortedMapAsc: HashMap<String, Int>? = sortByComparator_assignment(assignmentHM)
        assignmentAl = ArrayList()
        assignmentValueAl = ArrayList()
        assignmentAl.clear()
        assignmentValueAl.clear()
        for (i in -1 until (sortedMapAsc?.size!!)) {

            try {
                assignmentAl.add(sortedMapAsc.keys.toTypedArray()[i].trim { it < ' ' })
                assignmentValueAl.add(sortedMapAsc[assignmentAl.get(i)].toString())
            } catch (e: IndexOutOfBoundsException) {
                e.printStackTrace()
            }
        }
        val listArray0: MutableList<KeyPairBoolData> = java.util.ArrayList()
        try {
            for (i in assignmentAl.indices) {
                val h = KeyPairBoolData()
                h.id = (i + 1).toLong()
                h.name = assignmentAl.get(i)
                h.isSelected = false
                listArray0.add(h)
            }
        } catch (e: java.lang.NullPointerException) {
            e.printStackTrace()
        }

        spActivityType.setItems(listArray0, -1, SpinnerListener { items ->

            var idss: String = ""
            val ddsss: List<KeyPairBoolData> = spActivityType.getSelectedItems()

            for (i in ddsss.indices) {
                val name = ddsss[i].name
                idss = idss.plus(valueOf(hashMap?.get(name))).plus(", ")
            }
            idss = idss.removeSuffix(", ")

            if (idss.contains("52")) {
                etAirtelShopID.visibility = View.VISIBLE
            } else {
                etAirtelShopID.visibility = View.GONE
            }

            if (idss.contains("59")) {
                etJioShopID.visibility = View.VISIBLE
                llcheckJioMart.visibility = View.VISIBLE
            } else {
                etJioShopID.visibility = View.GONE
                llcheckJioMart.visibility = View.GONE
            }
            if(!idss.contains("66")&&!idss.contains("52")&&!idss.contains("59")){
                etOtherStoreID.visibility = View.VISIBLE
            }
            if (idss.contains("66")) {
                etPineLabsShopID.visibility = View.VISIBLE
                btnPineLabs.visibility = View.VISIBLE
            } else {
                etPineLabsShopID.visibility = View.GONE
                btnPineLabs.visibility = View.GONE

            }

            if (idss.contains("75")) {
                etOtherStoreID.visibility = View.VISIBLE
            } else {
                etOtherStoreID.visibility = View.GONE
            }

        })

        chkYesOrderId.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                etOrderId.visibility = View.VISIBLE
                chkNoOrderId.isChecked = false
            }
        }
        chkNoOrderId.setOnCheckedChangeListener { buttonView, isChecked ->
            etOrderId.visibility = View.GONE
            chkYesOrderId.isChecked = false
        }

    }

    private fun sortByComparator_assignment(assignmentHM: HashMap<String, Int?>): HashMap<String, Int>? {
        val list: LinkedList<Map.Entry<String, Int?>> =
            LinkedList<Map.Entry<String, Int?>>(assignmentHM.entries)

        // Sorting the list based on values
        Collections.sort(list) { o1: Map.Entry<String, Int?>, o2: Map.Entry<String, Int?> ->
            o1.value?.compareTo(o2.value!!)!!
        }

        // Maintaining insertion order with the help of LinkedList
        val sortedMap: HashMap<String, Int> = LinkedHashMap()
        for ((key, value) in list) {
            sortedMap[key] = value!!
        }
        return sortedMap
    }

    fun getHashmapData(industry: List<Service_Response.Data.Industry>) {
        val hashMap: HashMap<String, Int?> = HashMap<String, Int?>() //define empty hashmap
        for (item in industry)
            hashMap.put(item.text, item.id)
        for (key in hashMap.keys) {
            getSpinnerValue(hashMap)
        }
    }

    private fun getSpinnerValue(symptomHM: HashMap<String, Int?>) {
        val sortedMapAsc: HashMap<String, Int>? = sortByComparator(symptomHM)
        symptomAl = ArrayList()
        symptomValueAl = ArrayList()
        symptomAl = ArrayList()
        symptomValueAl = ArrayList()
        symptomAl.clear()
        symptomValueAl.clear()
        for (i in -1 until (sortedMapAsc?.size!!)) {

            try {
                symptomAl.add(sortedMapAsc.keys.toTypedArray()[i].trim { it < ' ' })
                symptomValueAl.add(sortedMapAsc[symptomAl.get(i)].toString())
            } catch (e: IndexOutOfBoundsException) {
                e.printStackTrace()
            }
        }
        val listArray0: MutableList<KeyPairBoolData> = java.util.ArrayList()
        try {
            for (i in symptomAl.indices) {
                val h = KeyPairBoolData()
                h.id = (i + 1).toLong()
                h.name = symptomAl.get(i)
                h.isSelected = false
                listArray0.add(h)
            }
        } catch (e: java.lang.NullPointerException) {
            e.printStackTrace()
        }

        spn_currentServices.setItems(listArray0, -1, SpinnerListener { items ->
            for (i in items.indices) {
                if (items[i].isSelected) {
                    symptomAl.add(sortedMapAsc.keys.toTypedArray()[i].trim { it <= ' ' })
                    symptomValueAl.add(sortedMapAsc[symptomAl.get(i)].toString())
                }
            }
        })

        spn_interestedServices.setItems(listArray0, -1, SpinnerListener { items ->
            for (i in items.indices) {
                if (items[i].isSelected) {
                    symptomAl.add(sortedMapAsc.keys.toTypedArray()[i].trim { it <= ' ' })
                    symptomValueAl.add(sortedMapAsc[symptomAl.get(i)].toString())

                }
            }
        })
    }


    private fun sortByComparator(symptomHM: HashMap<String, Int?>): HashMap<String, Int>? {
        val list: LinkedList<Map.Entry<String, Int?>> =
            LinkedList<Map.Entry<String, Int?>>(symptomHM.entries)

        // Sorting the list based on values
        Collections.sort(list) { o1: Map.Entry<String, Int?>, o2: Map.Entry<String, Int?> ->
            o1.value?.compareTo(o2.value!!)!!
        }

        // Maintaining insertion order with the help of LinkedList
        val sortedMap: HashMap<String, Int> = LinkedHashMap()
        for ((key, value) in list) {
            sortedMap[key] = value!!
        }
        return sortedMap
    }

    private fun initView() {
        llpersonal_profiletab.isEnabled = false
        llServicestab.isEnabled = false
        llRefrences.isEnabled = false
        llAssignmentsTab.isEnabled = false
        displaySnackBar = DisplaySnackBar(this)
        iconAddressarrow.background = resources.getDrawable(R.drawable.up)
        iconadd.background = resources.getDrawable(R.drawable.up)
        iconaddWP.background = resources.getDrawable(R.drawable.up)
        iconaddServices.background = resources.getDrawable(R.drawable.up)
        iconaddReferences.background = resources.getDrawable(R.drawable.up)
        iconaddAssignment.background = resources.getDrawable(R.drawable.up)
        iconAddressarrow.background = resources.getDrawable(R.drawable.up)
        iconKYC.background = resources.getDrawable(R.drawable.up)
        val bundle = intent?.extras
        sathi_code = bundle?.getString("sathi_id").toString()

        if (mobileNumberComing == null) {
            mobileNumberComing = ""
        }
        mobileNumberComing = bundle?.getString("phone_number").toString()

        Log.e("TAG", "initView: >>>" + mobileNumberComing)
        comingfrom = bundle?.getString("comingfrom").toString()
        if (comingfrom.equals("editRbm")) {
            sathi_code = bundle?.getString("rbm_sathhii").toString()
        }

        if (comingfrom.equals("sathiRecords")) {
            llServicestab.visibility = View.GONE
            llservices.visibility = View.GONE
            llpersonal_profiletab.visibility = View.GONE
            llRefrences.visibility = View.GONE
            llAssignmentsTab.isEnabled = true
            ll_interestedServices.visibility = View.GONE

        }

        tvMerchantCode?.text = "rBM".plus(sathi_code)
        spinnerGender()
        getAgeGroup()
        getEducationData()
        getOccupationApiData()
        call_rbMDetail()
    }

    private fun initListeners() {
        btnPineLabs.setOnClickListener { startActivity(Intent(this, ZohoActivity::class.java)) }

        icBack.setOnClickListener { onBackPressed() }

        llpersonal_profiletab.setOnClickListener {
            call_rbMDetail()
            iconAddressarrow.background = resources.getDrawable(R.drawable.down)
            iconadd.background = resources.getDrawable(R.drawable.down)
            iconaddWP.background = resources.getDrawable(R.drawable.up)
            iconaddServices.background = resources.getDrawable(R.drawable.up)
            iconaddAssignment.background = resources.getDrawable(R.drawable.up)
            iconaddReferences.background = resources.getDrawable(R.drawable.down)
            llworkprofiletab.isEnabled = false
            llServicestab.isEnabled = false
            llRefrences.isEnabled = false
            llAssignmentsTab.isEnabled = false
            llKycTab.isEnabled = false
            llpersonal_profiletab.background = resources.getDrawable(R.color.merchantBackground)
            llworkprofiletab.background =
                resources.getDrawable(R.drawable.borderwith_light_green_noradii)
            llServicestab.background =
                resources.getDrawable(R.drawable.borderwith_light_green_noradii)
            llRefrences.background =
                resources.getDrawable(R.drawable.borderwith_light_green_noradii)
            llAssignmentsTab.background =
                resources.getDrawable(R.drawable.borderwith_light_green_noradii)
            llKycTab.background = resources.getDrawable(R.drawable.borderwith_light_green_noradii)
            llPersonalProfile?.visibility = View.VISIBLE
            btnBasicProfile?.visibility = View.VISIBLE
            llWorkProfile?.visibility = View.GONE
            imgProfileCheck?.visibility = View.GONE
            llBasicPersonal.visibility = View.VISIBLE
            btnNextrofile?.visibility = View.VISIBLE
            iconAddressarrow.background = resources.getDrawable(R.drawable.up)
        }

// work profile label clicklistener
        llworkprofiletab.setOnClickListener {
            iconaddWP.background = resources.getDrawable(R.drawable.down)
            iconaddReferences.background = resources.getDrawable(R.drawable.up)
            iconaddServices.background = resources.getDrawable(R.drawable.up)
            iconaddAssignment.background = resources.getDrawable(R.drawable.up)
            iconKYC.background = resources.getDrawable(R.drawable.up)
            llpersonal_profiletab.isEnabled = false
            llServicestab.isEnabled = false
            llRefrences.isEnabled = false
            llAssignmentsTab.isEnabled = false
            llKycTab.isEnabled = false
            llWorkProfile?.visibility = View.VISIBLE
            btnsaveWorkProfile.visibility = View.VISIBLE
            llPersonalProfile?.visibility = View.GONE
            llAssignments?.visibility = View.GONE
            llservices?.visibility = View.GONE
            llservices?.visibility = View.GONE
            llRefrences.visibility = View.GONE
            btnNextrofile?.visibility = View.GONE

            imgworkProfileCheck?.visibility = View.GONE
            btnsaveWorkProfile?.visibility = View.VISIBLE
            llpersonal_profiletab.background =
                resources.getDrawable(R.drawable.borderwith_light_green_noradii)
            llworkprofiletab.background = resources.getDrawable(R.color.merchantBackground)
            llServicestab.background =
                resources.getDrawable(R.drawable.borderwith_light_green_noradii)
            llRefrences.background =
                resources.getDrawable(R.drawable.borderwith_light_green_noradii)
            llAssignmentsTab.background =
                resources.getDrawable(R.drawable.borderwith_light_green_noradii)
            llKycTab.background = resources.getDrawable(R.drawable.borderwith_light_green_noradii)

        }

        //refrences label clicklistener
        llRefrences.setOnClickListener {
            iconaddWP.background = resources.getDrawable(R.drawable.up)
            iconKYC.background = resources.getDrawable(R.drawable.up)
            iconaddReferences.background = resources.getDrawable(R.drawable.down)

            llRefrencesList?.visibility = View.VISIBLE
            btnReference?.visibility = View.VISIBLE
            llpersonal_profiletab.background =
                resources.getDrawable(R.drawable.borderwith_light_green_noradii)
            llworkprofiletab.background =
                resources.getDrawable(R.drawable.borderwith_light_green_noradii)
            llServicestab.background =
                resources.getDrawable(R.drawable.borderwith_light_green_noradii)
            llRefrences.background = resources.getDrawable(R.color.merchantBackground)
            llAssignmentsTab.background =
                resources.getDrawable(R.drawable.borderwith_light_green_noradii)
            llKycTab.background = resources.getDrawable(R.drawable.borderwith_light_green_noradii)
            //Category Data for spinner
            getCategoryData()
        }

        // self kyc clickonlistner
        llselfKYC.setOnClickListener {
            btnSave_self_KYC.visibility = View.VISIBLE
            llkyc_self_layout.visibility = View.VISIBLE
            iconKYCarrow.background = resources.getDrawable(R.drawable.down)
            iconKYC.background = resources.getDrawable(R.drawable.down)
        }

        //work kyc clicklistener
        llworkKYC.setOnClickListener {
            btnSave_work_KYC.visibility = View.VISIBLE
            llKYC_work_layout.visibility = View.VISIBLE
            iconworkKYCarrow.background = resources.getDrawable(R.drawable.down)
            iconKYC.background = resources.getDrawable(R.drawable.down)
        }

        // save reference data
        btnNextRef1.setOnClickListener {

            val name = etNameRef1.text.toString().trim()
            val mobile_number = etPhoneRef1.text.toString().trim()
            val pincode = etPincode1.text.toString().trim()
            val selectedcategory = selected_cat1
            val refrenceRequest =
                RefrenceRequest(sathi_code!!, name, mobile_number, selectedcategory!!, pincode)
            if (isRefrenceValidated()) {
                checkMobileValidationforRefrences(etPhoneRef1.text.toString(), refrenceRequest)

            }
        }

        btnReference.setOnClickListener {

            if (!etNameRef1.text.isNullOrBlank() || !etPhoneRef1.text.isNullOrBlank() || !etPincode1.text.isNullOrBlank() || rvRefrenceList.adapter?.itemCount == null || rvRefrenceList.adapter?.itemCount == 0) {
                btnNextRef1.performClick()
                return@setOnClickListener
            }

            llRefrencesList.visibility = View.GONE

            iconaddReferences.background = resources.getDrawable(R.drawable.up)
            btnReference.visibility = View.GONE
            imgRefCheck.visibility = View.VISIBLE
            llRefrences.isEnabled = true
            btnAssignments.visibility = View.VISIBLE
            llAssignments.visibility = View.VISIBLE

//            llRefrences.isClickable=false

            llpersonal_profiletab.background =
                resources.getDrawable(R.drawable.borderwith_light_green_noradii)
            llworkprofiletab.background =
                resources.getDrawable(R.drawable.borderwith_light_green_noradii)
            llServicestab.background =
                resources.getDrawable(R.drawable.borderwith_light_green_noradii)
            llRefrences.background =
                resources.getDrawable(R.drawable.borderwith_light_green_noradii)
            llAssignmentsTab.background = resources.getDrawable(R.color.merchantBackground)
            llKycTab.background = resources.getDrawable(R.drawable.borderwith_light_green_noradii)

            //api integration for acitivity status check
            apiIntegrationforcheckActivityStatus()
        }

        //self image photo
        imgSelfPhoto.setOnClickListener {
            dispatchTakePictureIntentReal(109)
        }

        // work inside image
        img_wp_inside.setOnClickListener {
            captureImageforInsidePhoto()
        }
        //outside image
        img_wp_Outside.setOnClickListener {
            captureImageforOutsidePhoto()
        }

        //self document photo
        imgDocumentself.setOnClickListener {
            captureImageforSelfDocument()
        }

        //work document photo
        imgDocumentwork.setOnClickListener {
            captureImageforWorkDocument()
        }

        // assignment photo
        imgCameraAssignment.setOnClickListener {
            captureImageforAssignment()
        }


        llAddress.setOnClickListener {
            lladdress_merchant.visibility = View.VISIBLE
            iconAddressarrow.background = resources.getDrawable(R.drawable.down)
            llWorkProfile?.visibility = View.GONE
            btnSaveAddress?.visibility = View.VISIBLE
        }

        //services clicklistener
        llServicestab.setOnClickListener {
            llservices.visibility = View.VISIBLE
            btnsaveServices.visibility = View.VISIBLE
//            btnsaveWorkProfile.visibility = View.GONE
            iconaddServices.background = resources.getDrawable(R.drawable.down)
            /* llpersonal_profiletab.isEnabled = false
             llRefrences.isEnabled = false
             llworkprofiletab.isEnabled = false
             llAssignmentsTab.isEnabled = false
             llKycTab.isEnabled = false*/

//            llpersonal_profiletab.background = resources.getDrawable(R.drawable.borderwith_light_green_noradii)
            llworkprofiletab.background =
                resources.getDrawable(R.drawable.borderwith_light_green_noradii)
            llServicestab.background = resources.getDrawable(R.color.merchantBackground)
            llRefrences.background =
                resources.getDrawable(R.drawable.borderwith_light_green_noradii)
            llAssignmentsTab.background =
                resources.getDrawable(R.drawable.borderwith_light_green_noradii)
            llKycTab.background = resources.getDrawable(R.drawable.borderwith_light_green_noradii)
        }

        llAssignmentsTab.setOnClickListener {
//            iconAddressarrow.background = resources.getDrawable(R.drawable.up)
//            iconadd.background = resources.getDrawable(R.drawable.up)
//            iconaddWP.background = resources.getDrawable(R.drawable.up)
            iconaddAssignment.background = resources.getDrawable(R.drawable.down)
//            iconaddReferences.background = resources.getDrawable(R.drawable.up)
//            iconAddressarrow.background = resources.getDrawable(R.drawable.up)
            /* llpersonal_profiletab.isEnabled = false
             llServicestab.isEnabled = false
             llworkprofiletab.isEnabled = false
             llRefrences.isEnabled = false
             llKycTab.isEnabled = false*/
            llAssignments?.visibility = View.VISIBLE
//            btnBasicProfile?.visibility = View.GONE
//            llWorkProfile?.visibility = View.GONE
//            imgProfileCheck?.visibility = View.GONE
//            llBasicPersonal.visibility = View.GONE
//            btnNextrofile?.visibility = View.GONE
            btnAssignments.visibility = View.VISIBLE


            llpersonal_profiletab.background =
                resources.getDrawable(R.drawable.borderwith_light_green_noradii)
            llworkprofiletab.background =
                resources.getDrawable(R.drawable.borderwith_light_green_noradii)
            llServicestab.background =
                resources.getDrawable(R.drawable.borderwith_light_green_noradii)
            llRefrences.background =
                resources.getDrawable(R.drawable.borderwith_light_green_noradii)
            llAssignmentsTab.background = resources.getDrawable(R.color.merchantBackground)
            llKycTab.background = resources.getDrawable(R.drawable.borderwith_light_green_noradii)

            //api integration for acitivity status check
            apiIntegrationforcheckActivityStatus()

        }

        btnAssignments.setOnClickListener {


            if (!imgdone.isVisible) {
                Toast.makeText(applicationContext,
                    "Merchant mobile no. not verified, Please ask merchant to login in reBLISS app and verify OTP.",
                    Toast.LENGTH_SHORT).show()

            } else {
                val orderId: String = etOrderId.text.toString().trim { it <= ' ' }
                val shopGst = etGstNumber.text.toString().trim { it <= ' ' }
                val shopPan = etPanNumber.text.toString().trim { it <= ' ' }
                var shopsID: String = etAirtelShopID.text.toString().trim { it <= ' ' }.plus(",")
                    .plus(etJioShopID.text.toString().trim { it <= ' ' }).plus(",")
                    .plus(etPineLabsShopID.text.toString().trim { it <= ' ' }.plus(
                        etOtherStoreID.text.toString().trim { it <= ' ' }
                    ))
                shopsID = shopsID.removeSuffix(",")
                shopsID = shopsID.removePrefix(",")

                Log.e("TAG", "initListeners: "+orderId+"\n"+shopGst+"\n"+shopPan+"\n"+shopsID )

                if (isAssignmentValidated(shopsID, orderId, shopGst, shopPan)) {
                    if (etAirtelShopID.text.toString().length > 0) {
                        checkValidationFromApi(etAirtelShopID.text.toString(), 1, shopsID, "52")
                    }

                   else if (etPineLabsShopID.text.toString().length > 0) {
                        checkValidationFromApi(etPineLabsShopID.text.toString(), 1, shopsID, "66")
                    }
                   else if (etJioShopID.text.toString().length > 0) {
                        checkValidationFromApi(etJioShopID.text.toString(), 1, shopsID, "59")
                    }
                    else if(etOtherStoreID.text.toString().length>0)
                    {
                        checkValidationFromApi(etOtherStoreID.text.toString(), 1, shopsID, "75")

                    }
                    else{
                        Log.e("TAG", "initListeners: >>>SSSS" )
                    }
                }
            }
        }
        etDOB.setOnClickListener({ v -> openDatePickerDialog(v) })

        btnSaveAddress.setOnClickListener {
            val address1 = etShopUnit.text.toString().trim()
            val address2 = etArea.text.toString().trim()
            val pincode = etPin.text.toString().trim()
            val landmark = etLandmark.text.toString().trim()
            Log.e("TAG",
                "initListeners: " + address1 + "\n" + address2 + "\n" + pincode + "\n" + landmark + "\n" + Latitude + "\n" + Longitude + "\n" + address_id + "\n" + sathi_code + "\n" + mCity + "\n " + mState)
            val rbmAddressRequest = RbmAddressRequest(sathi_code!!,
                address_id!!,
                address1,
                address2,
                mCity!!,
                pincode,
                mState!!,
                landmark,
                Latitude,
                Longitude)
            saveDataforAddress(rbmAddressRequest)
        }
        // kyc tab click event
        llKycTab?.setOnClickListener {
            llSelfKYC_layout.visibility = View.VISIBLE
            llworkKYC_layout.visibility = View.VISIBLE

            iconKYC.background = resources.getDrawable(R.drawable.down)
            iconKYCarrow.background = resources.getDrawable(R.drawable.up)
            iconworkKYCarrow.background = resources.getDrawable(R.drawable.up)

            llpersonal_profiletab.isEnabled = false
            llServicestab.isEnabled = false
            llworkprofiletab.isEnabled = false
            llAssignmentsTab.isEnabled = false
            llRefrences.isEnabled = false

            llpersonal_profiletab.background =
                resources.getDrawable(R.drawable.borderwith_light_green_noradii)
            llworkprofiletab.background =
                resources.getDrawable(R.drawable.borderwith_light_green_noradii)
            llServicestab.background =
                resources.getDrawable(R.drawable.borderwith_light_green_noradii)
            llRefrences.background =
                resources.getDrawable(R.drawable.borderwith_light_green_noradii)
            llAssignmentsTab.background =
                resources.getDrawable(R.drawable.borderwith_light_green_noradii)
            llKycTab.background = resources.getDrawable(R.color.merchantBackground)

        }

        // save services
        btnsaveServices.setOnClickListener {
            val servicesRequest = servicesRequest(sathi_code!!,
                selected_interested_services!!,
                selected_current_services!!)
            if (isServiceValidation())
                saveDataforServices(servicesRequest)
        }

        btnBasicProfile.setOnClickListener {
            var genderData: String = ""
            if (spGender.selectedItem.equals("Male")) {
                genderData = "M"
            } else if (spGender.selectedItem.equals("Female")) {
                genderData = "F"
            } else {
                genderData = "O"
            }
            val editProfileRequest = personal_data_request(sathi_code!!,
                etName.text.toString(),
                "",
                etPhone.text.toString(),
                spAgeLimit.selectedItem as String,
                genderData,
                spEducation.selectedItem as String)

            if (isFormValidated()) {
                callUpdateDocProfile(editProfileRequest)
            }
        }

        btnNextrofile.setOnClickListener {
            llPersonalProfile.visibility = View.GONE
            btnNextrofile.visibility = View.GONE
            btnsaveWorkProfile.visibility = View.VISIBLE
            llWorkProfile.visibility = View.VISIBLE
            iconAddressarrow.background = resources.getDrawable(R.drawable.up)
            iconadd.background = resources.getDrawable(R.drawable.up)
            iconaddWP.background = resources.getDrawable(R.drawable.down)
            llpersonal_profiletab.background =
                resources.getDrawable(R.drawable.borderwith_light_green_noradii)
            llworkprofiletab.background = resources.getDrawable(R.color.merchantBackground)
            llServicestab.background =
                resources.getDrawable(R.drawable.borderwith_light_green_noradii)
            llRefrences.background =
                resources.getDrawable(R.drawable.borderwith_light_green_noradii)
            llAssignmentsTab.background =
                resources.getDrawable(R.drawable.borderwith_light_green_noradii)
            llKycTab.background = resources.getDrawable(R.drawable.borderwith_light_green_noradii)


        }

        btnsaveWorkProfile.setOnClickListener {
            val storeName: String = etStoreName.text.toString().trim { it <= ' ' }
            val occupation_data = spnOccupation.selectedItem as String
            Log.e("TAG", "initListeners: " + occupation_data)
            val apiService = ApiClient.getClient().create(ApiInterface::class.java)
// create a map of data to pass along
            val storeNameBody: RequestBody =
                RequestBody.create("text/plain".toMediaTypeOrNull(), storeName)
            val user_ids: RequestBody =
                RequestBody.create("text/plain".toMediaTypeOrNull(), sathi_code!!)
            val occupations: RequestBody =
                RequestBody.create("text/plain".toMediaTypeOrNull(), occupation_data)
            val map = HashMap<String, RequestBody>()
            map["store_name"] = storeNameBody
            map["user_id"] = user_ids
            map["occupation"] = occupations
            val inside_photo: MutableList<MultipartBody.Part> =
                java.util.ArrayList<MultipartBody.Part>()
            val outside_photo: MutableList<MultipartBody.Part> =
                java.util.ArrayList<MultipartBody.Part>()


            for (i in insideImage.indices) {

//                    Const.showLog("image path out " + filteredImageList.get(i));
                val image2: File = insideImage.get(i)
                Log.e("TAG", "insidesideimage.get(i): $image2")
                //                        Const.showLog("image path " + filteredImageList.get(i));
                inside_photo.add(MultipartBody.Part?.createFormData("inside_photo[]",
                    image2.name,
                    RequestBody.create("image/*".toMediaTypeOrNull(), image2)))
            }

            for (i in outsideImage.indices) {
                val image1: File = outsideImage.get(i)
                Log.e("TAG", "outsidePhoto.get(i): $image1")
                //                        Const.showLog("image path " + filteredImageList.get(i));
                outside_photo.add(MultipartBody.Part?.createFormData("outside_photo[]",
                    image1.name,
                    RequestBody.create("image/*".toMediaTypeOrNull(), image1)))
            }

            if (isWorkprofileValidated()) {
                apiIntegrationforWorkProfile(map, inside_photo, outside_photo)
            }


        }


// self kyc button
        btnSave_self_KYC.setOnClickListener {
            val email: String = etEmailselfKYC.text.toString().trim { it <= ' ' }
            val doc_number: String = etDocNumberSelfKyc.text.toString().trim { it <= ' ' }

            if (isSelfValidated())
                apiintegrationforSelfKYC(email, selectedDate, doc_number)


        }
// save kyc button
        btnSave_work_KYC.setOnClickListener {
            val doc_number: String = etDocNumberWorkKyc.text.toString().trim { it <= ' ' }

            if (isWorkKYCvalidated())
                apiIntegrationforWorkKyc(doc_number)

        }

        imgRefresh.setOnClickListener {
            apiIntegrationforcheckActivityStatus()
        }

        btnReview.setOnClickListener {
            val orderId: String = etOrderId.text.toString().trim { it <= ' ' }
            val shopGst = etGstNumber.text.toString().trim { it <= ' ' }
            val shopPan = etPanNumber.text.toString().trim { it <= ' ' }
            var shopsID: String = etOtherStoreID.text.toString().trim { it <= ' ' }.plus(",").plus(
                etAirtelShopID.text.toString().trim { it <= ' ' }).plus(",")
                .plus(etJioShopID.text.toString().trim { it <= ' ' }).plus(",")
                .plus(etPineLabsShopID.text.toString().trim { it <= ' ' }.plus(",").plus(" "
                ))

            var shopID_rem = shopsID.removeSuffix(",")
            shopID_rem = shopsID.removePrefix(",")

             val new_str = shopsID.replace(",".toRegex(), " ") //replace , with space

            val temp = new_str.trim { it <= ' ' } //remove trailing space

            val  temp1 = temp.replace("\\s".toRegex(), " ")
            shopsID = shopsID.removeSuffix(",")
            shopsID = shopsID.removePrefix(",")
            Log.e("TAG", "initListeners: "+temp )
            Log.e("TAG", "initListeners: "+temp1 )
            val  temp2  = temp1.trim().replace("[ ]{2,}".toRegex(), " ");
           Log.e("TAG", "inti:temp2 "+temp2 )
            val  temp3 = temp2.trim().replace("\\s".toRegex(), ",")
            Log.e("TAG", "inti:temp3 "+temp3 )
       //     Log.e("TAG", "initListeners: btnReview"+shopsID+">>>>>>>>"+"\n"+orderId )
            if (isAssignmentValidated(shopsID, orderId, shopGst, shopPan))



                apiIntegrationforAssignment(temp3, orderId, shopGst, shopPan)
        }

        otherActivityCheckBox.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                gstvalue = 1
                names = "GST"
                etGstNumber.setText("")
                etGstNumber.hint = "Enter GST"
                udyogaadhaarCheckBox.isChecked = false
                fssaiCheckBox.isChecked = false
                tradelicenseCheckBox.isChecked = false
                gstContainer.visibility = View.VISIBLE
                btnUploadGstPic.visibility = View.VISIBLE
                gstPicFile = null
                btnUploadGstPic.text = ""
            } else {
                gstContainer.visibility = View.GONE
                btnUploadGstPic.visibility = View.GONE
            }
        }

        udyogaadhaarCheckBox.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                gstvalue = 2
                names = "Udyog Aadhaar"
                etGstNumber.setText("")
                etGstNumber.hint = "Enter  Udyog Aadhaar"
                otherActivityCheckBox.isChecked = false
                fssaiCheckBox.isChecked = false
                tradelicenseCheckBox.isChecked = false
                gstContainer.visibility = View.VISIBLE
                btnUploadGstPic.visibility = View.VISIBLE
                gstPicFile = null
                btnUploadGstPic.text = ""
            } else {
                gstContainer.visibility = View.GONE
                btnUploadGstPic.visibility = View.GONE
            }
        }
        fssaiCheckBox.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                names = "FSSAI"
                gstvalue = 4
                etGstNumber.setText("")
                etGstNumber.hint = "Enter FSSAI"
                udyogaadhaarCheckBox.isChecked = false
                //                udyogaadhaarCheckBox.setSelected(false);
                otherActivityCheckBox.isChecked = false
                //                otherActivityCheckBox.setSelected(false);
                tradelicenseCheckBox.isChecked = false
                //                tradelicenseCheckBox.setSelected(false)
                gstContainer.visibility = View.VISIBLE
                btnUploadGstPic.visibility = View.VISIBLE
                gstPicFile = null
                btnUploadGstPic.text = ""
            } else {
                gstContainer.visibility = View.GONE
                btnUploadGstPic.visibility = View.GONE
            }
        }
        tradelicenseCheckBox.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                names = "Trade License"
                gstvalue = 3
                etGstNumber.setText("")
                etGstNumber.hint = "Enter Trade License"
                udyogaadhaarCheckBox.isChecked = false
                //                udyogaadhaarCheckBox.setSelected(false);
                fssaiCheckBox.isChecked = false
                //                fssaiCheckBox.setSelected(false);
                otherActivityCheckBox.isChecked = false
                //                otherActivityCheckBox.setSelected(false)
                gstContainer.visibility = View.VISIBLE
                btnUploadGstPic.visibility = View.VISIBLE
                gstPicFile = null
                btnUploadGstPic.text = ""
            } else {
                gstContainer.visibility = View.GONE
                btnUploadGstPic.visibility = View.GONE
            }
        }

        otherActivityCheckBoxPan.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                val alertDialog2 = AlertDialog.Builder(
                    this@MerchantActivity
                )
                alertDialog2.setTitle("Confirm!")
                alertDialog2.setCancelable(false)
                alertDialog2.setMessage("Are you sure that the merchant has no other document?")
                alertDialog2.setPositiveButton(
                    "YES"
                ) { dialog, which -> // Write your code here to execute after dialog
                    panContainer.visibility = View.VISIBLE
                    btnUploadPanPic.visibility = View.VISIBLE
                    dialog.cancel()
                }
                alertDialog2.setNegativeButton(
                    "NO"
                ) { dialog, which ->
                    otherActivityCheckBoxPan.isChecked = false
                    dialog.cancel()
                }
                alertDialog2.show()
            } else {
                panContainer.visibility = View.GONE
                btnUploadPanPic.visibility = View.GONE
            }
        }

        imgCameraGst.setOnClickListener {
            //                Intent cameraIntent3 = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//                startActivityForResult(cameraIntent3, CAMERA_PIC_REQUEST_GST_PIC);
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE),
                    0)
            } else {
                try {
                    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    val f: File = getImageFile()!!
                    photoURI = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        FileProvider.getUriForFile(this,
                            BuildConfig.APPLICATION_ID + ".provider",
                            f)
                    } else {
                        Uri.fromFile(f)
                    }
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    setResult(RESULT_OK, null)
                    startActivityForResult(intent, CAMERA_PIC_REQUEST_GST_PIC)
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            }
        }
        imgGalleryGst.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE),
                    0)
            } else {
                val gallery =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
                startActivityForResult(gallery, GALLERY_PIC_REQUEST_GST_PIC)
            }
        }
        imgGalleryPan.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE),
                    0)
            } else {
                val gallery1 =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
                startActivityForResult(gallery1, GALLERY_PIC_REQUEST_PAN_PIC)
            }
        }


        imgCameraPan.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE),
                    0)
            } else {
                try {
                    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    val f = getImageFile()
                    photoURI = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        FileProvider.getUriForFile(
                            this, BuildConfig.APPLICATION_ID + ".provider",
                            f!!
                        )
                    } else {
                        Uri.fromFile(f)
                    }
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    setResult(RESULT_OK, null)
                    startActivityForResult(intent, CAMERA_PIC_REQUEST_PAN_PIC)
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            }
        }

        llServicestab.performClick()
    }


    private fun apiIntegrationforUpdateStatus() {
        kProgressHUD.show()
        val updateStatusRbmlistRequest = update_status_rbmlist_request("1", sathi_code!!)
        val call = apiService.updateStatus(updateStatusRbmlistRequest)
        call.enqueue(object : Callback<update_status_rbmlist_response> {
            override fun onResponse(
                call: Call<update_status_rbmlist_response>,
                response: Response<update_status_rbmlist_response>,
            ) {
                Log.e("TAG", "onResponse: " + response.body())
                kProgressHUD.dismiss()
                if (response.isSuccessful) {
                    if (response.code() >= 200 && response.code() < 700) {
                        if (response.code() == 200) {
                            if (response.body()!!.status == 1) {
                                kProgressHUD.dismiss()
                                Toast.makeText(this@MerchantActivity,
                                    "Form submitted successfully",
                                    Toast.LENGTH_SHORT).show()
                                finish()
                            }
                        }
                    }
                }
            }

            override fun onFailure(call: Call<update_status_rbmlist_response>, t: Throwable) {
                kProgressHUD.dismiss()
            }
        })
    }

    @Throws(IOException::class)
    private fun getImageFile(): File? {
        val imageFileName = "IMG_" + System.currentTimeMillis() + "_"
        val imagesFolder = "images"
        val storageDir = File(
            getExternalFilesDir(Environment.DIRECTORY_PICTURES), imagesFolder
        )
        println(storageDir.absolutePath)
        if (!storageDir.exists()) {
            storageDir.mkdirs()
            Log.e("TAG", "file not exists: ")
        } else Log.e("TAG", "file exists: ")
        val file = File.createTempFile(imageFileName, ".jpg", storageDir)
        Log.e("TAG", "getImageFile: " + file.absolutePath)
        currentPhotoPath = "file:" + file.absolutePath
        fileAbsolutePath = file.absolutePath
        Log.e("TAG", "currentphotopath: >>>>$currentPhotoPath")
        mySingleton.saveData("currentpath", currentPhotoPath)
        mySingleton.saveData("fileabsolutepath", fileAbsolutePath)
        return file
    }

    private fun isAssignmentValidated(
        shopsID: String,
        orderId: String,
        shopGst: String,
        shopPan: String,
    ): Boolean {
        var status = true
        if (etAirtelShopID.isVisible && etAirtelShopID.text.isNullOrBlank()) {
            status = false
            showWarningSimpleAlertDialog(Constant.TITLE, "Please enter Airtel Store Id")
        } else if (etJioShopID.isVisible && etJioShopID.text.isNullOrBlank()) {
            status = false
            showWarningSimpleAlertDialog(Constant.TITLE, "Please enter Jio Store Id")
        } else if (etPineLabsShopID.isVisible && etPineLabsShopID.text.isNullOrBlank()) {
            status = false
            showWarningSimpleAlertDialog(Constant.TITLE, "Please enter Pine Labs Store Id")
        }

        else if (etOtherStoreID.isVisible && etOtherStoreID.text.isNullOrBlank()) {
            status = false
            showWarningSimpleAlertDialog(Constant.TITLE, "Please enter Store Id")
        }
        else if (spActivityType.selectedItem.toString()
                .trim { it <= ' ' } == "Select Activity Type"
        ) {
            status = false
            showWarningSimpleAlertDialog(Constant.TITLE, "Please Select Activity")
        } else if (otherActivityCheckBox.isChecked) {
            if (shopGst.isEmpty()) {
                showWarningSimpleAlertDialog(Constant.TITLE, "Please enter shop gst number")
                status = false
            }
            try {
                if (!Utils.validGSTIN(shopGst)) {
                    showWarningSimpleAlertDialog(Constant.TITLE, "GST number is not valid")
                    status = false
                }
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
            if (gstPicFile == null) {
                showWarningSimpleAlertDialog(Constant.TITLE, "Please capture gst picture")
                status = false
            }
        } else if (udyogaadhaarCheckBox.isChecked) {
            if (shopGst.isEmpty()) {
                showWarningSimpleAlertDialog(Constant.TITLE,
                    "Please enter shop  Udyog Aadhaar number")
                status = false
            }
            if (gstPicFile == null) {
                showWarningSimpleAlertDialog(Constant.TITLE,
                    "Please capture  Udyog Aadhaar picture")
                status = false
            }
        } else if (fssaiCheckBox.isChecked) {
            if (shopGst.isEmpty()) {
                showWarningSimpleAlertDialog(Constant.TITLE, "Please enter shop  FSSAI number")
                status = false
            }
            if (gstPicFile == null) {
                showWarningSimpleAlertDialog(Constant.TITLE, "Please capture  FSSAI picture")
                status = false
            }
        } else if (tradelicenseCheckBox.isChecked) {
            if (shopGst.isEmpty()) {
                showWarningSimpleAlertDialog(Constant.TITLE,
                    "Please enter shop  Trade License number")
                status = false
            }
            if (gstPicFile == null) {
                showWarningSimpleAlertDialog(Constant.TITLE,
                    "Please capture  Trade License picture")
                status = false
            }
        } else if (otherActivityCheckBoxPan.isChecked) {
            if (shopPan.isEmpty()) {
                showWarningSimpleAlertDialog(Constant.TITLE, "Please enter shop pan number")
                status = false
            }
            try {
                if (!Utils.validPan(shopPan)) {
                    showWarningSimpleAlertDialog(Constant.TITLE, "PAN number is not valid")
                    status = false
                }
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
            if (panPicFile == null) {
                showWarningSimpleAlertDialog(Constant.TITLE, "Please capture pan picture")
                status = false
            }
        } else if (assignmentActitvityImage.isEmpty()) {
            status = false
            showWarningSimpleAlertDialog(Constant.TITLE, "Please upload Activity photo")
        }


        else if (llcheckJioMart.isVisible) {
            if (!chkYesOrderId.isChecked && !chkNoOrderId.isChecked) {
                status = false
                showWarningSimpleAlertDialog(Constant.TITLE, "Please select JioMart Order Taken")
            } else if (chkYesOrderId.isChecked && etOrderId.text.isNullOrBlank()) {
                status = false
                showWarningSimpleAlertDialog(Constant.TITLE, "Please enter JioMart Order ID")
            }
        }

        return status
    }

    // api integration for assignment
    private fun apiIntegrationforAssignment(
        shopsID: String,
        orderId: String,
        shopGst: String,
        shopPan: String,
    ) {
        kProgressHUD.show()

        val apiService = ApiClient.getClient().create(ApiInterface::class.java)
// create a map of data to pass along
        val rbm_id: RequestBody = RequestBody.create("text/plain".toMediaTypeOrNull(), sathi_code!!)
        val user_ids: RequestBody = RequestBody.create("text/plain".toMediaTypeOrNull(),
            mySingleton.getData(Constant.USER_ID))
        val shopID: RequestBody = RequestBody.create("text/plain".toMediaTypeOrNull(), shopsID)
        Log.e("TAG", "apiIntegrationforAssignment: >>>" + shopsID)
        val orderIds: RequestBody = RequestBody.create("text/plain".toMediaTypeOrNull(), orderId)
        val shopGst: RequestBody = RequestBody.create("text/plain".toMediaTypeOrNull(), shopGst)
        val shopPAN: RequestBody = RequestBody.create("text/plain".toMediaTypeOrNull(), shopPan)
        val typeofdoc_choosed: RequestBody =
            RequestBody.create("text/plain".toMediaTypeOrNull(), gstvalue.toString())
        val longitude: RequestBody =
            RequestBody.create("text/plain".toMediaTypeOrNull(), Longitude.toString())
        val latitude: RequestBody =
            RequestBody.create("text/plain".toMediaTypeOrNull(), Latitude.toString())
        //NEED TO UPDATE THIS DYNAMICALLY
        var idss: String = ""
        val ddsss: List<KeyPairBoolData> = spActivityType.getSelectedItems()

        for (i in ddsss.indices) {
            val name = ddsss[i].name
            if (i == 0) {
                idss = valueOf(hashMap?.get(name))
                Log.i("selected_activitytype", "initListeners: " + idss)
                if (idss.equals("66")) {
                    btnPineLabs.visibility = View.VISIBLE
                }

            } else if (idss != null) {
                idss = idss.toString() + "," + valueOf(hashMap?.get(name))
                Log.i("selecteitsssytype", "initListeners: " + idss)

                if (idss.equals("66")) {
                    btnPineLabs.visibility = View.VISIBLE
                }
            }
        }

        val activity_type: RequestBody =
            RequestBody.create("text/plain".toMediaTypeOrNull(), idss)

        // TODO need to do dynamic for form_data later
        val formstatus: RequestBody = RequestBody.create("text/plain".toMediaTypeOrNull(), "1")
        //val formstatus: RequestBody = RequestBody.create("text/plain".toMediaTypeOrNull(), reblissStatus!!)
        val map = HashMap<String, RequestBody>()
        map["user_id"] = user_ids
        map["activity_type"] = activity_type
        map["store_id"] = shopID
        map["order_id"] = orderIds
        map["rbm_id"] = rbm_id
        map["form_status"] = formstatus
        map["proof_id"] = typeofdoc_choosed
        map["gst_number"] = shopGst
        map["pan_number"] = shopPAN
        map["latitude"] = latitude
        map["longitude"] = longitude


        val id_proof: MutableList<MultipartBody.Part> = java.util.ArrayList<MultipartBody.Part>()
        for (i in assignmentActitvityImage.indices) {
            val image2: File = assignmentActitvityImage.get(i)
            Log.e("TAG", "workkyc.get(i): $image2")
            id_proof.add(MultipartBody.Part?.createFormData("inside_photo[]",
                image2.name,
                RequestBody.create("image/*".toMediaTypeOrNull(), image2)))
        }
        var gstPhotoPhotoBodyPart: MultipartBody.Part? = null
        gstPhotoPhotoBodyPart = if (gstPicFile != null) {
            createFormData(
                "gst_photo", gstPicFile!!.name, RequestBody.create(
                    "image/*".toMediaTypeOrNull(),
                    gstPicFile!!
                )
            )
        } else {
            createFormData(
                "gst_photo",
                "",
                RequestBody.create("text/plain".toMediaTypeOrNull(), "")
            )
        }
        var panPhotoPhotoBodyPart: MultipartBody.Part? = null
        panPhotoPhotoBodyPart = if (panPicFile != null) {
            createFormData(
                "pan_photo", panPicFile!!.name, RequestBody.create(
                    "image/*".toMediaTypeOrNull(),
                    panPicFile!!
                )
            )
        } else {
            createFormData(
                "pan_photo",
                "",
                RequestBody.create("text/plain".toMediaTypeOrNull(), "")
            )
        }


        // API integration for Saving data of work KYC
        var call: Call<ActivityResponse?>? = null
        call = apiService.saveAssignmentData_new(
            map,
            id_proof, gstPhotoPhotoBodyPart, panPhotoPhotoBodyPart
        )

        call?.enqueue(object : Callback<ActivityResponse?> {
            override fun onResponse(
                call: Call<ActivityResponse?>,
                response: Response<ActivityResponse?>,
            ) {
                kProgressHUD.dismiss()

                if (response.isSuccessful) {
                    Log.e("TAG", "onResponse: >>>>>>>>>>>Aagaya")
                    if (response.code() >= 200 && response.code() < 700) {
                        Log.e("TAG", "onResponse: >>>>>>>>>>>Aagaya123")
                        if (response.code() == 200) {
                            Log.e("TAG", "onResponse: >>>>>>>>>>>Aagaya431")
                            if (response.body()?.status == 1) {
                                // response.body()?.id = listofActivityIds
                                Log.e("TAG",
                                    "onResponse:ram ram " + response.body()?.id!!.get(0)!!.category)
                                var catIds: String = ""
                                for (i in response.body()?.id!!.indices) {
                                    catIds += response.body()?.id!!.get(i)!!.category + ","

                                }
                                Log.e("TAG", "onResponse: " + catIds)
                                try {
                                    val alertDialog2 = AlertDialog.Builder(
                                        this@MerchantActivity
                                    )
                                    alertDialog2.setTitle("Attention!")
                                    alertDialog2.setCancelable(false)
                                    alertDialog2.setMessage("Activity has been created successfully with id " +
                                            catIds.dropLast(1))
                                    alertDialog2.setPositiveButton(
                                        "OK"
                                    ) { dialog, which -> // Write your code here to execute after dialog

                                        dialog.cancel()
                                        apiIntegrationforUpdateStatus()
                                        val intent = Intent(this@MerchantActivity,
                                            EditRbmDetailActivity::class.java)
                                        intent.putExtra("rbm_sathii", sathi_code)
                                        intent.putExtra("activity_id",
                                            catIds)
                                        startActivity(intent)
                                        finish()


                                    }

                                    alertDialog2.show()

                                } catch (e: java.lang.IndexOutOfBoundsException) {
                                    e.printStackTrace()
                                    Log.e("TAG", "onResponse: " + e.message)
                                }
                            }
                            if (response.body()?.status == 0) {
                                Toast.makeText(this@MerchantActivity,
                                    "" + response.body()?.desc,
                                    Toast.LENGTH_SHORT).show()

                            }

                        }
                    } else {
                        displaySnackBar!!.DisplaySnackBar(Constant.UNEXPECTED, Constant.TYPE_ERROR)
                        Log.e("TAG", "onResponse: esle part ")
                    }
                } else {
                    try {
                        var errorBody = ErrorBody()
                        val gson = Gson()
                        errorBody =
                            gson.fromJson(response.errorBody()!!.string(), ErrorBody::class.java)
                        displaySnackBar!!.DisplaySnackBar(errorBody.message, Constant.TYPE_ERROR)
                        Log.e("TAG", "errorr body: esle part "+errorBody.message)
                        if (errorBody.message.contains("invalid")) {
                        }
                    } catch (e: java.lang.Exception) {
                        displaySnackBar!!.DisplaySnackBar(Constant.ERROR_INVALID_JSON,
                            Constant.TYPE_ERROR)
                        Log.e("TAG", "onResponse: catc part ")
                        e.printStackTrace()
                    }
                }
            }

            override fun onFailure(call: Call<ActivityResponse?>, t: Throwable) {
                kProgressHUD.dismiss()
                Log.e("TAG", "onFailure: " + t.message)
                if (t != null && (t is IOException || t is SocketTimeoutException
                            || t is ConnectException || t is NoRouteToHostException
                            || t is SecurityException)
                ) {
                    displaySnackBar!!.DisplaySnackBar(Constant.NETWIRK_ERROR, Constant.TYPE_ERROR)
                }
            }
        })
    }

    private fun apiIntegrationforcheckActivityStatus() {
        val apiService = ApiClient.getClient().create(ApiInterface::class.java)
        val responseCall = apiService.getrBMStatus(sathi_code!!)

        responseCall.enqueue(object : Callback<AssignmentStatusCheckResponse?> {
            override fun onResponse(
                call: Call<AssignmentStatusCheckResponse?>,
                response: Response<AssignmentStatusCheckResponse?>,
            ) {
                if (response.isSuccessful) {
                    if (response.code() >= 200 && response.code() < 700) {
                        if (response.body() != null) {
                            if (response.body()!!.data == 1) {
                                reblissStatus = "1"
                                imgdone.visibility = View.VISIBLE
                            } else {
                                reblissStatus = "0"
                                imgnotdone.visibility = View.VISIBLE
                                imgdone.visibility = View.GONE
                                Toast.makeText(this@MerchantActivity,
                                    response.body()?.desc,
                                    Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }

            override fun onFailure(call: Call<AssignmentStatusCheckResponse?>, t: Throwable) {
                if (t is IOException || t is SocketTimeoutException
                    || t is ConnectException || t is NoRouteToHostException
                    || t is SecurityException
                ) {
                }
            }
        })
    }

    private fun isWorkKYCvalidated(): Boolean {
        var status = true
        if (etDocNumberWorkKyc.text.toString().equals("") || etDocNumberWorkKyc.text.isEmpty()) {
            status = false
            showWarningSimpleAlertDialog(Constant.TITLE, "Please enter Document Number")
            etDocNumberWorkKyc.isEnabled = true
        }


        if (spDocumentType_work.selectedItem.toString().trim { it <= ' ' } == "Select Option") {
            status = false
            showWarningSimpleAlertDialog(Constant.TITLE, "Please Select Category")
        }
        if (work_kyc_url == null) {
            work_kyc_url = ""
        }
        if (work_docImage.size < 2 && work_kyc_url.equals("")) {
            status = false
            showWarningSimpleAlertDialog(Constant.TITLE,
                "Please upload atleast two image of Work KYC ")
        }



        return status
    }

    private fun apiIntegrationforWorkKyc(doc_number: String) {
        val apiService = ApiClient.getClient().create(ApiInterface::class.java)
// create a map of data to pass along
        val user_ids: RequestBody =
            RequestBody.create("text/plain".toMediaTypeOrNull(), sathi_code!!)
        val doc_number_work: RequestBody =
            RequestBody.create("text/plain".toMediaTypeOrNull(), doc_number)
        //NEED TO UPDATE THIS DYNAMICALLY
        val doc_type: RequestBody =
            RequestBody.create("text/plain".toMediaTypeOrNull(), spUploadOptionPositionwork)
        val map = HashMap<String, RequestBody>()
        map["user_id"] = user_ids
        map["type"] = doc_type
        map["proof_number"] = doc_number_work
        val id_proof: MutableList<MultipartBody.Part> = java.util.ArrayList<MultipartBody.Part>()
        for (i in work_docImage.indices) {
            val image2: File = work_docImage.get(i)
            Log.e("TAG", "workkyc.get(i): $image2")
            id_proof.add(MultipartBody.Part?.createFormData("id_proof[]",
                image2.name,
                RequestBody.create("image/*".toMediaTypeOrNull(), image2)))
        }

        // API integration for Saving data of work KYC
        var call: Call<ErrorBody?>? = null
        call = apiService.saveworkKYC(map,
            id_proof)

        call?.enqueue(object : Callback<ErrorBody?> {
            override fun onResponse(call: Call<ErrorBody?>, response: Response<ErrorBody?>) {
                if (response.isSuccessful) {
                    if (response.code() >= 200 && response.code() < 700) {
                        if (response.code() == 200) {
                            llKYC_work_layout.visibility = View.GONE
                            btnSave_work_KYC.visibility = View.GONE
                            Toast.makeText(this@MerchantActivity,
                                "Work Kyc data uploaded successfully",
                                Toast.LENGTH_LONG).show()
                        }
                    } else {
                        displaySnackBar!!.DisplaySnackBar(Constant.UNEXPECTED, Constant.TYPE_ERROR)
                        Log.e("TAG", "onResponse: esle part ")
                    }
                } else {
                    try {
                        var errorBody = ErrorBody()
                        val gson = Gson()
                        errorBody =
                            gson.fromJson(response.errorBody()!!.string(), ErrorBody::class.java)
                        displaySnackBar!!.DisplaySnackBar(errorBody.message, Constant.TYPE_ERROR)
                        Log.e("TAG", "errorr body: esle part "+errorBody.message)
                        if (errorBody.message.contains("invalid")) {
                        }
                    } catch (e: java.lang.Exception) {
                        displaySnackBar!!.DisplaySnackBar(Constant.ERROR_INVALID_JSON,
                            Constant.TYPE_ERROR)
                        Log.e("TAG", "onResponse: catc part ")
                        e.printStackTrace()
                    }
                }
            }

            override fun onFailure(call: Call<ErrorBody?>, t: Throwable) {
                Log.e("TAG", "onFailure: " + t.message)
                if (t != null && (t is IOException || t is SocketTimeoutException
                            || t is ConnectException || t is NoRouteToHostException
                            || t is SecurityException)
                ) {
                    displaySnackBar!!.DisplaySnackBar(Constant.NETWIRK_ERROR, Constant.TYPE_ERROR)
                }
            }
        })

    }

    private fun isSelfValidated(): Boolean {
        var status = true
        if (etEmailselfKYC.text.toString().equals("") || etEmailselfKYC.text.isEmpty()) {
            status = false
            showWarningSimpleAlertDialog(Constant.TITLE, "Please enter Email")
        }

        if (!RegexUtils.isValidEmail(etEmailselfKYC.getText().toString())) {
            status = false
            showWarningSimpleAlertDialog(Constant.TITLE, "Please enter valid Email")
        }

        if (etDOB.text.toString().equals("") || etDOB.text.isEmpty()) {
            status = false
            showWarningSimpleAlertDialog(Constant.TITLE, "Please enter Date of Birth")
            etDOB.isEnabled = true
        }
        if (etDocNumberSelfKyc.text.toString().equals("") || etDocNumberSelfKyc.text.isEmpty()) {
            status = false
            showWarningSimpleAlertDialog(Constant.TITLE, "Please enter Document Number")
            etDocNumberSelfKyc.isEnabled = true
        }
        if (spDocumentType.selectedItem.toString().trim { it <= ' ' } == "Select Option") {
            status = false
            showWarningSimpleAlertDialog(Constant.TITLE, "Please Select Category")
        }
        if (Adhar_Url == null) {
            Adhar_Url = ""
        }
        if (self_docImage.size < 2 && Adhar_Url.equals("")) {
            status = false
            showWarningSimpleAlertDialog(Constant.TITLE,
                "Please upload atleast two image of self KYC ")
        }



        return status
    }

    private fun apiintegrationforSelfKYC(email: String, dob: String, doc_number: String) {
        val apiService = ApiClient.getClient().create(ApiInterface::class.java)
        // create a map of data to pass along
        val emailID: RequestBody = RequestBody.create("text/plain".toMediaTypeOrNull(), email)
        val user_ids: RequestBody =
            RequestBody.create("text/plain".toMediaTypeOrNull(), sathi_code!!)
        val dateofBirth: RequestBody = RequestBody.create("text/plain".toMediaTypeOrNull(), dob)
        val doc_number_self: RequestBody =
            RequestBody.create("text/plain".toMediaTypeOrNull(), doc_number)
        val doc_type: RequestBody =
            RequestBody.create("text/plain".toMediaTypeOrNull(), spUploadOptionPosition)
        val map = HashMap<String, RequestBody>()
        map["email"] = emailID
        map["user_id"] = user_ids
        map["dob"] = dateofBirth
        map["type"] = doc_type
        map["aadhar_no"] = doc_number_self
        Log.e("TAG", "apiintegrationforSelfKYC: " + map.toString())
        Log.e("TAG", "dob: " + dob)
        val id_proof: MutableList<MultipartBody.Part> = java.util.ArrayList<MultipartBody.Part>()
        for (i in self_docImage.indices) {

//                    Const.showLog("image path out " + filteredImageList.get(i));
            val image2: File = self_docImage.get(i)
            Log.e("TAG", "insidesideimage.get(i): $image2")
            //                        Const.showLog("image path " + filteredImageList.get(i));
            id_proof.add(MultipartBody.Part?.createFormData("id_proof[]",
                image2.name,
                RequestBody.create("image/*".toMediaTypeOrNull(), image2)))
        }


        // API integration for Saving data of self KYC
        var call: Call<ErrorBody?>? = null
        call = apiService.saveSelfKYC(map,
            id_proof)

        call?.enqueue(object : Callback<ErrorBody?> {
            override fun onResponse(call: Call<ErrorBody?>, response: Response<ErrorBody?>) {
                if (response.isSuccessful) {
                    if (response.code() >= 200 && response.code() < 700) {
                        if (response.code() == 200) {

                            llkyc_self_layout.visibility = View.GONE
                            btnSave_self_KYC.visibility = View.GONE
                            Toast.makeText(this@MerchantActivity,
                                "self Kyc data uploaded successfully",
                                Toast.LENGTH_LONG).show()
                        }
                    } else {
                        displaySnackBar!!.DisplaySnackBar(Constant.UNEXPECTED, Constant.TYPE_ERROR)
                        Log.e("TAG", "onResponse: esle part ")
                    }
                } else {
                    try {
                        var errorBody: ErrorBody
                        val gson = Gson()
                        errorBody =
                            gson.fromJson(response.errorBody()!!.string(), ErrorBody::class.java)
                        displaySnackBar!!.DisplaySnackBar(errorBody.message, Constant.TYPE_ERROR)
                        Log.e("TAG", "errorr body: esle part "+errorBody.message)
                        if (errorBody.message.contains("invalid")) {
                        }
                    } catch (e: java.lang.Exception) {
                        displaySnackBar!!.DisplaySnackBar(Constant.ERROR_INVALID_JSON,
                            Constant.TYPE_ERROR)
                        Log.e("TAG", "onResponse: catc part ")
                        e.printStackTrace()
                    }
                }
            }

            override fun onFailure(call: Call<ErrorBody?>, t: Throwable) {
                Log.e("TAG", "onFailure: " + t.message)
                if (t != null && (t is IOException || t is SocketTimeoutException
                            || t is ConnectException || t is NoRouteToHostException
                            || t is SecurityException)
                ) {
                    displaySnackBar!!.DisplaySnackBar(Constant.NETWIRK_ERROR, Constant.TYPE_ERROR)
                }
            }
        })
    }

    private fun isRefrenceValidated(): Boolean {
        var status = true

        if (etNameRef1.text.toString().equals("") || etNameRef1.text.isEmpty()) {
            status = false
            showWarningSimpleAlertDialog(Constant.TITLE, "Please enter  name of Refrence")
        } else if (etPhoneRef1.text.toString().equals("") || etPhoneRef1.text.isEmpty()) {
            status = false
            showWarningSimpleAlertDialog(Constant.TITLE, "Please enter  Phone number")
        } else if (etPhoneRef1.text.toString().length > 0 || !etPhoneRef1.text.isEmpty()) {
            if (etPhoneRef1.text.toString().length < 10) {
                status = false
                showWarningSimpleAlertDialog(Constant.TITLE, "Please enter  Valid Phone number")
            }
        } else if (etPincode1.text.toString().equals("") || etPincode1.text.isEmpty()) {
            status = false
            showWarningSimpleAlertDialog(Constant.TITLE, "Please enter  Pincode number")
        } else if (etPincode1.text.toString().length < 6) {
            status = false
            showWarningSimpleAlertDialog(Constant.TITLE, "Please enter valid Pincode number")
        } else if (spCategoryRefrence1.selectedItem.toString()
                .trim { it <= ' ' } == "Select Option"
        ) {
            status = false
            showWarningSimpleAlertDialog(Constant.TITLE, "Please Select Category")
        }


        return status
    }

    private fun checkMobileValidationforRefrences(
        mobileNumber: String,
        refrenceRequest: RefrenceRequest,
    ): Boolean {
        var status = true
        val apiService = ApiClient.getClient().create(ApiInterface::class.java)
        val responseCall = apiService.getCheckDuplicateRefrence(mobileNumber!!)

        responseCall.enqueue(object : Callback<AssignmentStatusCheckResponse?> {
            override fun onResponse(
                call: Call<AssignmentStatusCheckResponse?>,
                response: Response<AssignmentStatusCheckResponse?>,
            ) {
                if (response.isSuccessful) {
                    if (response.code() >= 200 && response.code() < 700) {
                        if (response.body() != null) {
                            if (response.body()!!.data == 0) {
                                Toast.makeText(this@MerchantActivity,
                                    "Mobile Already exist in database",
                                    Toast.LENGTH_SHORT).show()
                                etPhoneRef1.setText("")


                            } else {
                                saveDataforRef1API(refrenceRequest)
                            }
                        }
                    }
                }
            }

            override fun onFailure(call: Call<AssignmentStatusCheckResponse?>, t: Throwable) {
                if (t is IOException || t is SocketTimeoutException
                    || t is ConnectException || t is NoRouteToHostException
                    || t is SecurityException
                ) {
                }
            }
        })
        return status
    }

    private fun isServiceValidation(): Boolean {
        var status = true
        /*if (txtcurrentservices.text.toString().equals("")||txtcurrentservices.text.toString().equals("Select Current Services")) {
            status = false
            showWarningSimpleAlertDialog(Constant.TITLE, "Please Select Current Services")
        }*/

        if (txtInterestedservices.text.toString()
                .equals("") || txtInterestedservices.text.toString()
                .equals("Select Interested Services")
        ) {
            status = false
            showWarningSimpleAlertDialog(Constant.TITLE, "Please Select Interested Services")
        }
        return status
    }

    private fun apiIntegrationforWorkProfile(
        map: HashMap<String, RequestBody>,
        inside_photo: MutableList<MultipartBody.Part>,
        outside_photo: MutableList<MultipartBody.Part>,
    ) {
        var call: Call<ErrorBody?>? = null
        call = apiService.saveWorkProfile(map,
            inside_photo, outside_photo)

        call?.enqueue(object : Callback<ErrorBody?> {
            override fun onResponse(call: Call<ErrorBody?>, response: Response<ErrorBody?>) {
                if (response.isSuccessful) {
                    if (response.code() >= 200 && response.code() < 700) {
                        if (response.code() == 200) {
                            llWorkProfile.visibility = View.GONE
                            btnsaveWorkProfile.visibility = View.GONE
                            iconaddWP.background = resources.getDrawable(R.drawable.up)
                            iconaddServices.background = resources.getDrawable(R.drawable.down)
                            btnsaveServices.visibility = View.VISIBLE
                            llservices.visibility = View.VISIBLE
                            llpersonal_profiletab.background =
                                resources.getDrawable(R.drawable.borderwith_light_green_noradii)
                            llworkprofiletab.background =
                                resources.getDrawable(R.drawable.borderwith_light_green_noradii)
                            llServicestab.background =
                                resources.getDrawable(R.color.merchantBackground)
                            llRefrences.background =
                                resources.getDrawable(R.drawable.borderwith_light_green_noradii)
                            llAssignmentsTab.background =
                                resources.getDrawable(R.drawable.borderwith_light_green_noradii)
                            llKycTab.background =
                                resources.getDrawable(R.drawable.borderwith_light_green_noradii)
                            Toast.makeText(this@MerchantActivity,
                                "Work Profile Data submitted successfully",
                                Toast.LENGTH_LONG).show()
                        }
                    } else {
                        displaySnackBar!!.DisplaySnackBar(Constant.UNEXPECTED, Constant.TYPE_ERROR)
                        Log.e("TAG", "onResponse: esle part ")
                    }
                } else {
                    try {
                        var errorBody = ErrorBody()
                        val gson = Gson()
                        errorBody =
                            gson.fromJson(response.errorBody()!!.string(), ErrorBody::class.java)
                        displaySnackBar!!.DisplaySnackBar(errorBody.message, Constant.TYPE_ERROR)
                        Log.e("TAG", "errorr body: esle part "+errorBody.message)
                        if (errorBody.message.contains("invalid")) {
                        }
                    } catch (e: java.lang.Exception) {
                        displaySnackBar!!.DisplaySnackBar(Constant.ERROR_INVALID_JSON,
                            Constant.TYPE_ERROR)
                        Log.e("TAG", "onResponse: catc part ")
                        e.printStackTrace()
                    }
                }
            }

            override fun onFailure(call: Call<ErrorBody?>, t: Throwable) {
                Log.e("TAG", "onFailure: " + t.message)
                if (t != null && (t is IOException || t is SocketTimeoutException
                            || t is ConnectException || t is NoRouteToHostException
                            || t is SecurityException)
                ) {
                    displaySnackBar!!.DisplaySnackBar(Constant.NETWIRK_ERROR, Constant.TYPE_ERROR)
                }
            }
        })
    }

    private fun captureImageforAssignment() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE),
                0)
        } else {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (takePictureIntent.resolveActivity(packageManager) != null) {
                // Create the File where the photo should go
                try {
                    photoFile = createImageFile()
                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        val photoURI = FileProvider.getUriForFile(
                            this,
                            BuildConfig.APPLICATION_ID + ".provider",
                            photoFile!!
                        )
                        takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)

                        startActivityForResult(takePictureIntent, ASSIGNMENT_IMAGE_CLICK_REQUEST)

                    }
                } catch (ex: Exception) {
                    // Error occurred while creating the File
                    displayMessage(baseContext, ex.message.toString())
                }

            } else {
                displayMessage(baseContext, "Null")
            }
        }

    }

    private fun isFormValidated(): Boolean {
        var status = true
        if (etName.text.toString().equals("") || etName.text.isEmpty()) {
            status = false
            showWarningSimpleAlertDialog(Constant.TITLE, "Please enter  name of Merchant")
        }
        if (etPhone.text.toString().equals("") || etPhone.text.isEmpty()) {
            status = false
            showWarningSimpleAlertDialog(Constant.TITLE, "Please enter  Phone number")
        }
        if (spGender.selectedItem.toString().trim { it <= ' ' } == "Select Option") {
            status = false
            showWarningSimpleAlertDialog(Constant.TITLE, "Please Select Gender")
        }
        if (spEducation.selectedItem.toString().trim { it <= ' ' } == "Select Option") {
            status = false
            showWarningSimpleAlertDialog(Constant.TITLE, "Please Select Education")
        }
        if (spAgeLimit.selectedItem.toString().trim { it <= ' ' } == "Select Option") {
            status = false
            showWarningSimpleAlertDialog(Constant.TITLE, "Please Select Age Limit")
        }

        return status
    }

    private fun isWorkprofileValidated(): Boolean {
        var status = true
        if (inside_shop_url == null) {
            inside_shop_url = ""
        }
        if (outside_shop_url == null) {
            outside_shop_url = ""
        }
        if (insideImage.size < 1 && inside_shop_url.equals("")) {
            status = false
            showWarningSimpleAlertDialog(Constant.TITLE, "Please upload inside photo")
        }

        if (outsideImage.size < 1 && outside_shop_url.equals("")) {
            status = false
            showWarningSimpleAlertDialog(Constant.TITLE, "Please upload outside photo")
        }

        if (etStoreName.text.toString().equals("")) {
            status = false
            showWarningSimpleAlertDialog(Constant.TITLE, "Please enter  Store name")
        }
        if (spnOccupation.selectedItem.toString().trim { it <= ' ' } == "Select Option") {
            status = false
            showWarningSimpleAlertDialog(Constant.TITLE, "Please Select Occupation")
        }
        return status
    }

    private fun captureImageforSelfDocument() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE),
                0)
        } else {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (takePictureIntent.resolveActivity(packageManager) != null) {
                // Create the File where the photo should go
                try {
                    photoFile = createImageFile()
                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        val photoURI = FileProvider.getUriForFile(
                            this,
                            BuildConfig.APPLICATION_ID + ".provider",
                            photoFile!!
                        )
                        takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)

                        startActivityForResult(takePictureIntent, SELF_DOCUMENT_IMAGE_CLICK_REQUEST)

                    }
                } catch (ex: Exception) {
                    // Error occurred while creating the File
                    displayMessage(baseContext, ex.message.toString())
                }

            } else {
                displayMessage(baseContext, "Null")
            }
        }

    }

    private fun captureImageforWorkDocument() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE),
                0)
        } else {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (takePictureIntent.resolveActivity(packageManager) != null) {
                // Create the File where the photo should go
                try {
                    photoFile = createImageFile()
                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        val photoURI = FileProvider.getUriForFile(
                            this,
                            BuildConfig.APPLICATION_ID + ".provider",
                            photoFile!!
                        )
                        takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)

                        startActivityForResult(takePictureIntent, WORK_DOCUMENT_IMAGE_CLICK_REQUEST)

                    }
                } catch (ex: Exception) {
                    // Error occurred while creating the File
                    displayMessage(baseContext, ex.message.toString())
                }

            } else {
                displayMessage(baseContext, "Null")
            }
        }

    }

    private fun openDatePickerDialog(v: View) {
        val mcurrentDate = Calendar.getInstance()
        mDay = mcurrentDate[Calendar.DAY_OF_MONTH]
        mMonth = mcurrentDate[Calendar.MONTH]
        mYear = mcurrentDate[Calendar.YEAR]
        val datePickerDialog = DatePickerDialog(this, { view, year, month, dayOfMonth ->
            var month = month
            month = month + 1
            date = "$dayOfMonth-$month-$year"
            selectedDate = "$year-$month-$dayOfMonth"

            etDOB.setText(date)
        }, mYear, mMonth, mDay)
        datePickerDialog.datePicker.maxDate = System.currentTimeMillis() - 568025136000L
        datePickerDialog.show()
    }


    private fun saveDataforServices(servicesRequest: servicesRequest) {
        kProgressHUD.show()
        val call = apiService.saveServices(servicesRequest)
        call.enqueue(object : Callback<service_output_response> {
            override fun onResponse(
                call: Call<service_output_response>,
                response: Response<service_output_response>,
            ) {

                if (response.isSuccessful) {
                    if (response.code() >= 200 && response.code() < 700) {
                        kProgressHUD.dismiss()

                        if (response.code() == 200) {

                            if (response.body()!!.status == 1) {
                                llservices.visibility = View.GONE
                                btnsaveServices.visibility = View.GONE
                                imgserCheck?.visibility = View.VISIBLE

                                llRefrences.visibility = View.VISIBLE
                                llRefrencesList?.visibility = View.VISIBLE
                                btnReference?.visibility = View.VISIBLE

                                llServicestab.isEnabled = true
                                /*  llServicestab.isClickable = false
                                  llpersonal_profiletab.isClickable = false*/

                                iconaddServices.background = resources.getDrawable(R.drawable.up)
                                iconaddReferences.background =
                                    resources.getDrawable(R.drawable.down)
                                llpersonal_profiletab.background =
                                    resources.getDrawable(R.drawable.borderwith_light_green_noradii)
                                llworkprofiletab.background =
                                    resources.getDrawable(R.drawable.borderwith_light_green_noradii)
                                llServicestab.background =
                                    resources.getDrawable(R.drawable.borderwith_light_green_noradii)
                                llRefrences.background =
                                    resources.getDrawable(R.color.merchantBackground)
                                llAssignmentsTab.background =
                                    resources.getDrawable(R.drawable.borderwith_light_green_noradii)
                                llKycTab.background =
                                    resources.getDrawable(R.drawable.borderwith_light_green_noradii)


                                Toast.makeText(this@MerchantActivity,
                                    "Services data saved successfully",
                                    Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }

            override fun onFailure(call: Call<service_output_response>, t: Throwable) {
                kProgressHUD.dismiss()
            }
        })

    }

    private fun captureImage() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE),
                0)
        } else {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (takePictureIntent.resolveActivity(packageManager) != null) {
                // Create the File where the photo should go
                try {
                    photoFile = createImageFile()
                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        val photoURI = FileProvider.getUriForFile(this,
                            BuildConfig.APPLICATION_ID + ".provider",
                            photoFile!!)
                        takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                        startActivityForResult(takePictureIntent, AADHAR_IMAGE_CLICK_REQUEST)
                    }
                } catch (ex: Exception) {
                    displayMessage(baseContext, ex.message.toString())
                }

            } else {
                displayMessage(baseContext, "Null")
            }
        }
    }

    private fun captureImageforInsidePhoto() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE),
                0)
        } else {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (takePictureIntent.resolveActivity(packageManager) != null) {
                // Create the File where the photo should go
                try {
                    photoFile = createImageFile()
                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        val photoURI = FileProvider.getUriForFile(this,
                            BuildConfig.APPLICATION_ID + ".provider",
                            photoFile!!)
                        takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                        startActivityForResult(takePictureIntent, INSIDE_IMAGE_CLICK_REQUEST)
                    }
                } catch (ex: Exception) {
                    // Error occurred while creating the File
                    displayMessage(baseContext, ex.message.toString())
                }
            } else {
                displayMessage(baseContext, "Null")
            }
        }

    }

    private fun captureImageforOutsidePhoto() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE),
                0)
        } else {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (takePictureIntent.resolveActivity(packageManager) != null) {
                // Create the File where the photo should go
                try {
                    photoFile = createImageFile()
                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        val photoURI = FileProvider.getUriForFile(this,
                            BuildConfig.APPLICATION_ID + ".provider",
                            photoFile!!)
                        takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                        startActivityForResult(takePictureIntent, OUTSIDE_IMAGE_CLICK_REQUEST)
                    }
                } catch (ex: Exception) {
                    displayMessage(baseContext, ex.message.toString())
                }

            } else {
                displayMessage(baseContext, "Null")
            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(imageFileName, /* prefix */
            ".jpg", /* suffix */
            storageDir      /* directory */)

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.absolutePath
        return image
    }

    private fun saveDataforRef1API(refrenceRequest: RefrenceRequest?) {
        kProgressHUD.show()
        val call = apiService.saveRefData(refrenceRequest)
        call.enqueue(object : Callback<RefrenceResponse> {
            override fun onResponse(
                call: Call<RefrenceResponse>,
                response: Response<RefrenceResponse>,
            ) {
                kProgressHUD.dismiss()
                if (response.isSuccessful) {
                    if (response.code() >= 200 && response.code() < 700) {
                        if (response.code() == 200) {
                            if (response.body()!!.status == 1) {
                                Toast.makeText(applicationContext,
                                    "References saved successfully",
                                    Toast.LENGTH_SHORT).show()
                                getRefrenceData()
                            }
                        }
                    }
                }
            }

            override fun onFailure(call: Call<RefrenceResponse>, t: Throwable) {
                kProgressHUD.dismiss()
                Log.e(this.javaClass.name, "onFailure: ", t)
            }
        })
    }

    private fun getRefrenceData() {
        kProgressHUD.show()
        val call = apiService.getRefData(sathi_code)
        call.enqueue(object : Callback<getRefrenceDataResponse> {
            override fun onResponse(
                call: Call<getRefrenceDataResponse>,
                response: Response<getRefrenceDataResponse>,
            ) {
                kProgressHUD.dismiss()
                if (response.isSuccessful) {
                    if (response.code() >= 200 && response.code() < 700) {
                        if (response.code() == 200) {
                            if (response.body()!!.status == 1) {
                                (rvRefrenceList.adapter as? RefrenceAdapter)?.updateAdapter(response.body()?.data?.all_groups)
                                etNameRef1.setText("")
                                etPhoneRef1.setText("")
                                etPincode1.setText("")
                            }
                        }
                    }
                }
            }

            override fun onFailure(call: Call<getRefrenceDataResponse>, t: Throwable) {
                kProgressHUD.dismiss()
            }
        })

    }

    private fun getCategoryData() {
        kProgressHUD.show()
        val call = apiService.categoryData
        call.enqueue(object : Callback<Category_Ref_Response> {
            override fun onResponse(
                call: Call<Category_Ref_Response>,
                response: Response<Category_Ref_Response>,
            ) {
                kProgressHUD.dismiss()
                if (response.isSuccessful) {
                    if (response.code() >= 200 && response.code() < 700) {
                        if (response.code() == 200) {
                            if (response.body()!!.status == 1) {
                                response.body()?.data?.apply {
                                    allCategoryList = all_category
                                    categoryRefArrayList = ArrayList()
                                    for (i in all_category.indices) {
                                        category_list_name = all_category[i].category_name
                                        categoryRefArrayList?.add(category_list_name)
                                    }
                                    categoryRefArrayList?.add(0, "Select Option")
                                    categotyArrayAdapter =
                                        object : ArrayAdapter<String?>(this@MerchantActivity,
                                            R.layout.spinner_item_gender, categoryRefArrayList!!) {
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
                                                val view = super.getDropDownView(position,
                                                    convertView,
                                                    parent)
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
                                            }
                                        }

                                }
                            }
                        }
                    }
                }
            }

            override fun onFailure(call: Call<Category_Ref_Response>, t: Throwable) {
                kProgressHUD.dismiss()
            }
        })
    }

    private fun getServiceDataforInterested() {
        kProgressHUD.show()
        val call = apiService.getServiceData("3")
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

        spn_interestedServices?.setOnItemSelectedListener(object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long,
            ) {
                selected_interested_services = parent.getItemAtPosition(position) as String
                if (!selected_interested_services.toString().equals("Select Interested Services")) {
                    txtInterestedservices.setText(selected_interested_services)
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        })
        spn_currentServices?.setOnItemSelectedListener(object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long,
            ) {
                selected_current_services = parent.getItemAtPosition(position) as String
                if (!selected_current_services.toString().equals("Select Current Services")) {
                    txtcurrentservices.setText(selected_current_services)
                }


            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        })

    }

    //This is the api integration for showing rbm merchant detail and this helps to autofill data
    // which user has already filled at signup page
    private fun call_rbMDetail() {
        kProgressHUD.show()

        val call = apiService.getrBMDetail(sathi_code)

        call.enqueue(object : Callback<rBMDetailResponse> {
            override fun onResponse(
                call: Call<rBMDetailResponse>,
                response: Response<rBMDetailResponse>,
            ) {

                if (response.isSuccessful && response.code() == 200) {

                    if (response.body()?.status == 1 && response.body()?.data != null) {
                        kProgressHUD.dismiss();
                        response.body()?.data?.all_groups?.apply {
                            merchant_name = first_name.plus("\t").plus(last_name)
                            if (userDetail.communication_address_id == null) {
                                userDetail.communication_address_id = ""
                            }
                            address_id = userDetail.communication_address_id
                            if (phone_number == null) {
                                phone_number == ""
                            }
                            phonenumber = phone_number
                            if (fos_shop_name == null) {
                                fos_shop_name = ""
                            }
                            storeName = fos_shop_name
                            if (userDetail.personal_email_id == null) {

                                userDetail.personal_email_id = ""

                            }
                            emailKYCrespose = userDetail.personal_email_id

                            if (userDetail.pan_no == null) {
                                userDetail.pan_no = ""
                            }

                            docnumberWorkKycResponse = userDetail.pan_no
                            if (userDetail.aadhar_no == null) {
                                userDetail.aadhar_no = ""
                            }
                            docnumberSelfKycResponse = userDetail.aadhar_no
                            if (img_path !== null) {
                                self_image_path = img_path
                            }

                            if (userDetail.dob == null) {
                                userDetail.dob = ""
                            }
                            self_kyc_dob_response = parseDateToddMMyyyy(userDetail.dob)!!
                            Log.e("TAG", "onResponse: " + self_kyc_dob_response)
                            Log.e("TAG", "onResponse: " + userDetail.dob)


                            if (userDetail.gender == "M") {
                                spGender?.setSelection(1)
                                spGender.isEnabled = false
                            } else if (userDetail.gender == "F") {
                                spGender?.setSelection(2)
                                spGender.isEnabled = false

                            } else if (userDetail.gender == "O") {
                                spGender?.setSelection(3)
                                spGender.isEnabled = false

                            } else {
                                spGender?.setSelection(0)
                                spGender.isEnabled = false

                            }

                            if (userDetail.gender == "" || userDetail == null) {
                                spGender.isEnabled = true
                            }


                        }
                        setDataonUI(response)

                    }
                }
            }

            override fun onFailure(call: Call<rBMDetailResponse>, t: Throwable) {
                kProgressHUD.dismiss();
                t.printStackTrace()
            }
        })
    }

    fun parseDateToddMMyyyy(time: String?): String? {
        var time = time
        val inputPattern = "yyyy-dd-MM"
        val outputPattern = "dd-MM-yyyy"
        val inputFormat = SimpleDateFormat(inputPattern)
        val outputFormat = SimpleDateFormat(outputPattern)
        try {
            if (time == null) {
                time = ""
            }
            val dates = inputFormat.parse(time)
            date = outputFormat.format(dates)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return date
    }

    private fun getAgeGroup() {
        val apiService = ApiClient.getClient().create(ApiInterface::class.java)
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
                                    ageGroupNameArrayList = ArrayList()
                                    for (i in allGroupListAge?.indices!!) {
                                        ageGroup_list_name =
                                            response.body()!!.data.allGroups[i].name
                                        ageGroupNameArrayList?.add(ageGroup_list_name)
                                    }
                                    ageGroupNameArrayList?.add(0, "Select Option")
                                    allGroupArrayAdapter =
                                        object : ArrayAdapter<String?>(this@MerchantActivity,
                                            R.layout.spinner_item_gender, ageGroupNameArrayList!!) {
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
                                                val view = super.getDropDownView(position,
                                                    convertView,
                                                    parent)
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
                                            }
                                        }
                                    allGroupArrayAdapter?.setDropDownViewResource(R.layout.spinner_item_gender)
                                    spAgeLimit.setAdapter(allGroupArrayAdapter)
                                }
                            } else if (response.body()!!.status == 401) {
//
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

    private fun setDataonUI(response: Response<rBMDetailResponse>) {
        kProgressHUD.show()
        name_of_merchant = etName.setText(merchant_name).toString()
        if (!name_of_merchant.toString().equals("")) {
            etName.isEnabled = false
            etName.setTextColor(resources.getColor(R.color.gray))

        }
        txtmechantName?.text = merchant_name
        phone_number = etPhone.setText(phonenumber).toString()
        if (!phone_number.toString().equals("")) {
            etPhone.isEnabled = false
            etPhone.setTextColor(resources.getColor(R.color.gray))
        }
        email_kyc = etEmailselfKYC.setText(emailKYCrespose).toString()

        if (!email_kyc.toString().equals("")) {
            etEmailselfKYC.isEnabled = false
            etEmailselfKYC.setTextColor(resources.getColor(R.color.gray))

        }
        work_kyc_doc_no = etDocNumberWorkKyc.setText(docnumberWorkKycResponse).toString()
        if (!work_kyc_doc_no.toString().equals("")) {
            etDocNumberWorkKyc.isEnabled = false
            etDocNumberWorkKyc.setTextColor(resources.getColor(R.color.gray))

        }
        self_kyc_doc_no = etDocNumberSelfKyc.setText(docnumberSelfKycResponse).toString()
        if (!self_kyc_doc_no.toString().equals("")) {
            etDocNumberSelfKyc.isEnabled = false
            etDocNumberSelfKyc.setTextColor(resources.getColor(R.color.gray))
        }
        self_kyc_dob = etDOB.setText(self_kyc_dob_response).toString()
        if (!self_kyc_dob.toString().equals("")) {
            etDOB.isEnabled = false
            etDOB.setTextColor(resources.getColor(R.color.gray))
        }
        txtselfPhotoDisplay.setText(self_image_path).toString()

        etStoreName.setText(storeName)
        if (!storeName.toString().equals("")) {
            etStoreName.isEnabled = false
            etStoreName.setTextColor(resources.getColor(R.color.gray))
        }
        response.body()?.data?.all_groups?.userDetail?.apply {
            if (current_service != null) {
                if (!current_service.equals("")) {
                    txtcurrentservices.visibility = View.VISIBLE
                    txtcurrentservices.setText(current_service)
                }
            }
            if (interested_service != null) {
                if (!interested_service.equals("")) {
                    txtInterestedservices.visibility = View.VISIBLE
                    txtInterestedservices.setText(interested_service)
                }
            }
            Adhar_Url = cp_adhar_proof
            work_kyc_url = id_proof
            inside_shop_url = shop_inside_photo
            outside_shop_url = shop_photo

            if (!TextUtil.isStringNullOrBlank(Adhar_Url)) {
                val Adhar_images = Adhar_Url.split(",".toRegex())

                AdharAdapter = PhotoAdapter(Adhar_images.toTypedArray(),
                    R.layout.doc_upload_remove,
                    this@MerchantActivity,
                    Constant.K_AADHAAR)
                rvSelfDocPhoto.setAdapter(AdharAdapter)
            }
            if (!TextUtil.isStringNullOrBlank(work_kyc_url)) {
                val work_kyc_image = work_kyc_url.split(",".toRegex())

                AdharAdapter = PhotoAdapter(work_kyc_image.toTypedArray(),
                    R.layout.doc_upload_remove,
                    this@MerchantActivity,
                    Constant.K_AADHAAR)
                rvWorkkycDocPhoto.setAdapter(AdharAdapter)
            }
            if (!TextUtil.isStringNullOrBlank(inside_shop_url)) {
                val Adhar_images = inside_shop_url.split(",".toRegex())

                AdharAdapter = PhotoAdapter(Adhar_images.toTypedArray(),
                    R.layout.doc_upload_remove,
                    this@MerchantActivity,
                    Constant.K_AADHAAR)
                rvInsidePhoto.setAdapter(AdharAdapter)
            }
            if (!TextUtil.isStringNullOrBlank(outside_shop_url)) {
                val Adhar_images = outside_shop_url.split(",".toRegex())

                AdharAdapter = PhotoAdapter(Adhar_images.toTypedArray(),
                    R.layout.doc_upload_remove,
                    this@MerchantActivity,
                    Constant.K_AADHAAR)
                rvOutsidePhoto.setAdapter(AdharAdapter)

            }
        }
        if (!storeName.equals("")) {
            btnYesStore.visibility = View.VISIBLE
            btnNoStore.visibility = View.GONE
        } else {
            btnYesStore.visibility = View.GONE
            btnNoStore.visibility = View.VISIBLE
        }
        if (!response.body()?.data?.all_groups?.userDetail?.upload_type_option.toString()
                .isNullOrBlank()
        ) {
            kProgressHUD.dismiss()
            val upload_type_self_kyc =
                response.body()?.data?.all_groups?.userDetail?.upload_type_option.toString()

            if (upload_type_self_kyc?.equals("0")!!) {
                spDocumentType.setSelection(1)
            }
            if (upload_type_self_kyc?.equals("1")!!) {
                spDocumentType.setSelection(2)
            }
            if (upload_type_self_kyc?.equals("2")!!) {
                spDocumentType.setSelection(3)
            }
        }
        if (!response.body()?.data?.all_groups?.userDetail?.cp_firm_name.toString()
                .isNullOrBlank()
        ) {
            kProgressHUD.dismiss()
            val upload_type_work_kyc =
                response.body()?.data?.all_groups?.userDetail?.cp_firm_name.toString()

            if (upload_type_work_kyc.equals("0")) {
                spDocumentType_work.setSelection(1)
            }
            if (upload_type_work_kyc.equals("1")) {
                spDocumentType_work.setSelection(2)
            }
            if (upload_type_work_kyc.equals("2")) {
                spDocumentType_work.setSelection(3)
            }
        }
        response.body()?.data?.all_groups?.apply {
            try {


                if (!age_range.isNullOrBlank()) {
                    val ageRange =
                        (spAgeLimit.adapter as? ArrayAdapter<String>)?.getPosition(age_range)
                    spAgeLimit.setSelection(ageRange!!)
                    spAgeLimit.isEnabled = false
                }
                if (!userDetail.education.isNullOrBlank()) {
                    val education =
                        (spEducation.adapter as? ArrayAdapter<String>)?.getPosition(userDetail.education)
                    spEducation.setSelection(education!!)
                    spEducation.isEnabled = false
                }

                if (!response.body()?.data?.all_groups?.occupation.isNullOrBlank()) {
                    val occupation =
                        (spnOccupation.adapter as? ArrayAdapter<String>)?.getPosition(response.body()?.data?.all_groups?.occupation)
                    spnOccupation.setSelection(occupation!!)
                    spnOccupation.isEnabled = false
                }

                if (!userDetail.current_service.isNullOrBlank()) {
                    val currentService = spn_currentServices.selectedItem.toString()
                    //spn_currentServices.setSelection(currentService!!)
                }

                if (!userDetail.interested_service.isNullOrBlank()) {
                    kProgressHUD.dismiss()
                    val interestedService =
                        (spn_interestedServices.adapter as? ArrayAdapter<String>)?.getPosition(
                            userDetail.interested_service)
                    spn_interestedServices.setSelection(interestedService!!)
                }

            } catch (e: java.lang.NullPointerException) {
                e.printStackTrace()
            }
            if (userDetail.address != null) {
                userDetail.address.apply {
                    if (!state.equals("")) {
                        etState.isEnabled = false
                        etState.setTextColor(resources.getColor(R.color.gray))
                    }
                    etState.setText(state)
                    if (!zipcode.equals("")) {
                        etPin.isEnabled = false
                        etPin.setTextColor(resources.getColor(R.color.gray))
                    }
                    etPin.setText(zipcode)
                    if (!city.equals("")) {
                        etCity.isEnabled = false
                        etCity.setTextColor(resources.getColor(R.color.gray))
                    }
                    etCity.setText(city)
                    if (!address1.equals("")) {
                        etShopUnit.isEnabled = false
                        etShopUnit.setTextColor(resources.getColor(R.color.gray))
                    }
                    etShopUnit.setText(address1)
                    if (!address2.equals("")) {
                        etArea.isEnabled = false
                        etArea.setTextColor(resources.getColor(R.color.gray))
                    }
                    etArea.setText(address2)
                    if (!land_mark.equals("")) {
                        etLandmark.isEnabled = false
                        etLandmark.setTextColor(resources.getColor(R.color.gray))
                    }
                    etLandmark.setText(land_mark)
                }
            }


        }
    }


    // here gender is statically getting from making string arrray of Gender
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

            override fun getDropDownView(
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
            }
        }
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item_gender)
        spGender?.setAdapter(spinnerArrayAdapter)
        spGender?.setOnItemSelectedListener(object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long,
            ) {
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
    }

// this is api integration for age group dropdown

    private fun spinnerDocumentType() {
        val spinnerArrayAdapter: ArrayAdapter<String?> = object : ArrayAdapter<String?>(
            this, R.layout.spinner_item_gender, document_type_stringArray) {
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
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY)
                    tv.setTypeface(App.LATO_REGULAR)
                } else {
                    tv.setTextColor(Color.BLACK)
                    tv.setTypeface(App.LATO_REGULAR)
                }
                return view
            }
        }
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item_gender)
        spDocumentType?.setAdapter(spinnerArrayAdapter)



        spDocumentType_work?.setAdapter(spinnerArrayAdapter)
        spDocumentType?.setOnItemSelectedListener(object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long,
            ) {
                document_type = parent.getItemAtPosition(position) as String
                if (document_type?.contains("Aadhar")!!) {
                    spUploadOptionPosition = "0"
                } else if (document_type?.contains("Voter id")!!) {
                    spUploadOptionPosition = "2"
                } else {
                    spUploadOptionPosition = "1"
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        })

        spDocumentType_work?.setOnItemSelectedListener(object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long,
            ) {
                document_type_work = parent.getItemAtPosition(position) as String
                if (document_type_work?.contains("Aadhar")!!) {
                    spUploadOptionPositionwork = "0"
                } else if (document_type_work?.contains("Voter Id")!!) {
                    spUploadOptionPositionwork = "2"
                } else {
                    spUploadOptionPositionwork = "1"
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        })
    }

    //Api integration for getting data of education dropdown
    private fun getEducationData() {
        val apiService = ApiClient.getClient().create(ApiInterface::class.java)
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
                                    educationNameArrayList = ArrayList<String?>()
                                    for (i in allGroupListEducation?.indices!!) {
                                        educationName = allGroupListEducation?.get(i)?.educationName
                                        educationNameArrayList?.add(educationName)
                                    }
                                    educationNameArrayList?.add(0, "Select Option")
                                    allGroupArrayAdapter =
                                        object : ArrayAdapter<String?>(this@MerchantActivity,
                                            R.layout.spinner_item_gender,
                                            educationNameArrayList!!) {
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
                                                val view = super.getDropDownView(position,
                                                    convertView,
                                                    parent)
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
                                    spEducation.setAdapter(allGroupArrayAdapter)
                                }
                            }
                        }
                    }
                }
            }

            override fun onFailure(call: Call<EducationResponse?>, t: Throwable) {
                Log.e("MerchantActivity", "onFailure: " + t.message)
            }
        })
    }

    private fun getOccupationApiData() {
        val apiService = ApiClient.getClient().create(ApiInterface::class.java)
        val responseCall = apiService.getOccupationDATA("mo")
        responseCall.enqueue(object : Callback<OccupationResponse?> {
            override fun onResponse(
                call: Call<OccupationResponse?>,
                response: Response<OccupationResponse?>,
            ) {
                if (response.isSuccessful) {
                    if (response.code() >= 200 && response.code() < 700) {
                        if (response.body() != null) {
                            if (response.body()!!.status == 1) {
                                if (response.body()!!.data != null && response.body()!!.data.allGroups.size > 0) {
                                    allGroupArrayList = response.body()!!.data.allGroups
                                    occupationNameArrayList = java.util.ArrayList<String>()
                                    for (i in allGroupArrayList?.indices!!) {
                                        occupation_name = response.body()!!.data.allGroups[i].name
                                        occupationNameArrayList?.add(occupation_name!!)
                                    }
                                    occupationNameArrayList?.add(0, "Select Option")
                                    allGroupArrayAdapter =
                                        object : ArrayAdapter<String?>(this@MerchantActivity,
                                            R.layout.spinner_item_gender,
                                            occupationNameArrayList!! as List<String?>) {
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
                                                val view = super.getDropDownView(position,
                                                    convertView,
                                                    parent)
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
                                    spnOccupation.adapter = allGroupArrayAdapter

                                    spCategoryRefrence1.adapter = allGroupArrayAdapter
                                }
                            } else if (response.body()!!.status == 401) {
                            }
                        }
                    }
                }
            }

            override fun onFailure(call: Call<OccupationResponse?>, t: Throwable) {
                if (t is IOException || t is SocketTimeoutException
                    || t is ConnectException || t is NoRouteToHostException
                    || t is SecurityException
                ) {
                }
            }
        })




        spCategoryRefrence1?.setOnItemSelectedListener(object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long,
            ) {
                selected_cat1 = parent.getItemAtPosition(position) as String

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        })

        spnOccupation?.setOnItemSelectedListener(object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long,
            ) {
                occupation_name = parent.getItemAtPosition(position) as String

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        })
    }

    // For saving data to server on next button click
    fun callUpdateDocProfile(editProfileRequest: personal_data_request) {
        try {
            //  getDataFromViews()

        } catch (e: NullPointerException) {
            e.printStackTrace()
        }
        kProgressHUD.show()
        Log.e("TOKEN", "callUpdateDocProfile: " + mySingleton.getData(Constant.TOKEN_BASE_64))
        val call = apiService.savePersonalData(editProfileRequest)
        call.enqueue(object : Callback<personal_data_response> {
            override fun onResponse(
                call: Call<personal_data_response>,
                response: Response<personal_data_response>,
            ) {
                kProgressHUD.dismiss()
                if (response.isSuccessful) {
                    if (response.code() >= 200 && response.code() < 700) {
                        if (response.code() == 200) {
                            llBasicPersonal.visibility = View.GONE
                            btnBasicProfile.visibility = View.GONE

                            Toast.makeText(this@MerchantActivity,
                                "Profile Updated Successfully",
                                Toast.LENGTH_SHORT).show()

                        }
                    }
                }
            }

            override fun onFailure(call: Call<personal_data_response>, t: Throwable) {
                Log.e("aaya re", "dfsfdfsdff: " + t.message)
                kProgressHUD.dismiss()
            }
        })
    }

    fun pincodeListener() {
        etPin.addTextChangedListener(object : TextWatcher {
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
            override fun onResponse(
                call: Call<SearchStateResponce>,
                response: Response<SearchStateResponce>,
            ) {
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
                                showWarningSimpleAlertDialog("Data not found",
                                    "Pincode is not available in database")
                                etPin.setText("")
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
                            || t is SecurityException)
                ) {
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
                                                etCity.setText(mCity)
                                            }
                                        }
                                    }
                                } else {
                                    displaySnackBar?.DisplaySnackBar("Data not found",
                                        Constant.TYPE_ERROR)
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
                        errorBody =
                            gson.fromJson(response.errorBody()!!.string(), ErrorBody::class.java)
                    } catch (e: java.lang.Exception) {
                        displaySnackBar?.DisplaySnackBar(Constant.ERROR_INVALID_JSON,
                            Constant.TYPE_ERROR)
                        e.printStackTrace()
                    }
                }
            }

            override fun onFailure(call: Call<CityResponce>, t: Throwable) {
                if (t != null && (t is IOException || t is SocketTimeoutException
                            || t is ConnectException || t is NoRouteToHostException
                            || t is SecurityException)
                ) {
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
                                    val json =
                                        gson.toJson(response.body(), StateResponce::class.java)
                                    if (response.body()!!.data.state.size > 0) {
                                        stateData.addAll(response.body()!!.data.state)
                                        for (i in stateData.indices) {
                                            if (stateData.get(i).getId() == stateId) {
                                                mState = stateData.get(i).getS_name()
                                                etState.setText(mState)
                                            }
                                        }
                                    }
                                } else {
                                    displaySnackBar?.DisplaySnackBar("Data not found",
                                        Constant.TYPE_ERROR)
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
                        errorBody =
                            gson.fromJson(response.errorBody()!!.string(), ErrorBody::class.java)
                    } catch (e: Exception) {
                        displaySnackBar?.DisplaySnackBar(Constant.ERROR_INVALID_JSON,
                            Constant.TYPE_ERROR)
                        e.printStackTrace()
                    }
                }
            }

            override fun onFailure(call: Call<StateResponce>, t: Throwable) {
                if (t != null && (t is IOException || t is SocketTimeoutException
                            || t is ConnectException || t is NoRouteToHostException
                            || t is SecurityException)
                ) {
                    displaySnackBar?.DisplaySnackBar(Constant.NETWIRK_ERROR, Constant.TYPE_ERROR)
                }
            }
        })
    }


    private fun saveDataforAddress(rbmAddressRequest: RbmAddressRequest?) {
        kProgressHUD.show()
        val call = apiService.rbmAddress(rbmAddressRequest)
        call.enqueue(object : Callback<rbmAddressResponse> {
            override fun onResponse(
                call: Call<rbmAddressResponse>,
                response: Response<rbmAddressResponse>,
            ) {
                kProgressHUD.dismiss()
                if (response.isSuccessful) {

                    if (response.code() >= 200 && response.code() < 700) {
                        if (response.code() == 200) {
                            if (response.body()!!.status == 1) {
                                btnSaveAddress.visibility = View.GONE
                                lladdress_merchant.visibility = View.GONE
                                iconAddressarrow.background = resources.getDrawable(R.drawable.up)
                                Toast.makeText(this@MerchantActivity,
                                    "address saved successfully",
                                    Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }

            override fun onFailure(call: Call<rbmAddressResponse>, t: Throwable) {
                kProgressHUD.dismiss()
            }
        })
    }


    private fun showWarningSimpleAlertDialog(title: String, message: String) {
        SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE).setTitleText(title)
            .setContentText(message)
            .setConfirmClickListener { sweetAlertDialog -> sweetAlertDialog.dismissWithAnimation() }
            .show()
    }

    private fun getLocationData() {
        if (islocationPermissionGranted()) {
            val gps = GPSTracker(this)
            if (gps.canGetLocation()) {
                try {
                    Latitude = gps.latitude
                    Longitude = gps.longitude
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
        }
    }


    fun islocationPermissionGranted(): Boolean {
        return if (Build.VERSION.SDK_INT >= 23) {
            if ((checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) && (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) && (checkSelfPermission(Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_GRANTED)
            ) {
                Log.v("permission", "Permission is granted")
                true
            } else {
                Log.v("permission", "Permission is revoked")
                ActivityCompat.requestPermissions(this@MerchantActivity,
                    arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.CAMERA),
                    1)
                false
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v("permission", "Permission is granted")
            true
        }
    }

    private fun displayMessage(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 109 && resultCode == Activity.RESULT_OK) {

            Handler().postDelayed(
                {
                    if (resultCode == Activity.RESULT_OK && requestCode == 109) {
                        callImagePost(null, mCurrentPhotoPath!!)
                    }
                }, 500
            )

            // callPostDocAPI(f, 101, ".jpg", base64)

        } else if (requestCode == INSIDE_IMAGE_CLICK_REQUEST && resultCode == Activity.RESULT_OK) {
            val compressedImage = Compressor(this).setMaxWidth(640).setMaxHeight(480).setQuality(70)
                .setCompressFormat(Bitmap.CompressFormat.PNG)
                .compressToFile(photoFile)

            insideImage.add(compressedImage)
            val myBitmap = BitmapFactory.decodeFile(compressedImage!!.absolutePath)
            //   img_wp_inside.setImageBitmap(myBitmap)
            val base64: String = encodeTobase64(myBitmap)!!
            Log.e("TAG", "onActivityResultsss: " + base64)

            val adapterlist = ShowListAdapter(this@MerchantActivity, insideImage)
            rvInsidePhoto.setAdapter(adapterlist)


        } else if (requestCode == OUTSIDE_IMAGE_CLICK_REQUEST && resultCode == Activity.RESULT_OK) {
            val compressedImage = Compressor(this).setMaxWidth(640).setMaxHeight(480).setQuality(70)
                .setCompressFormat(Bitmap.CompressFormat.PNG)
                .compressToFile(photoFile)

            outsideImage.add(compressedImage)
            val myBitmap = BitmapFactory.decodeFile(photoFile!!.absolutePath)
            //  img_wp_Outside.setImageBitmap(myBitmap)
            val base64: String = encodeTobase64(myBitmap)!!
            Log.e("TAG", "onActivityResult: " + base64)

            val adapterlist = ShowListAdapter(this@MerchantActivity, outsideImage)
            rvOutsidePhoto.setAdapter(adapterlist)

        } else if (requestCode == SELF_DOCUMENT_IMAGE_CLICK_REQUEST && resultCode == Activity.RESULT_OK) {
            val compressedImage = Compressor(this).setMaxWidth(640).setMaxHeight(480).setQuality(70)
                .setCompressFormat(Bitmap.CompressFormat.PNG)
                .compressToFile(photoFile)

            self_docImage.add(compressedImage)
            val myBitmap = BitmapFactory.decodeFile(photoFile!!.absolutePath)
            //  imgDocumentself.setImageBitmap(myBitmap)
            val base64: String = encodeTobase64(myBitmap)!!
            Log.e("TAG", "onActivityResult: " + base64)

            val adapterlist = ShowListAdapter(this@MerchantActivity, self_docImage)
            rvSelfDocPhoto.setAdapter(adapterlist)
            apiIntegrationforSelfPhoto()
        } else if (requestCode == WORK_DOCUMENT_IMAGE_CLICK_REQUEST && resultCode == Activity.RESULT_OK) {
            val compressedImage = Compressor(this).setMaxWidth(640).setMaxHeight(480).setQuality(60)
                .setCompressFormat(Bitmap.CompressFormat.PNG)
                .compressToFile(photoFile)

            work_docImage.add(compressedImage)
            val myBitmap = BitmapFactory.decodeFile(photoFile!!.absolutePath)
            //   imgDocumentwork.setImageBitmap(myBitmap)
            val base64: String = encodeTobase64(myBitmap)!!
            Log.e("TAG", "onActivityResult: " + base64)

            val adapterlist = ShowListAdapter(this@MerchantActivity, work_docImage)
            rvWorkkycDocPhoto.setAdapter(adapterlist)

        } else if (requestCode == ASSIGNMENT_IMAGE_CLICK_REQUEST && resultCode == Activity.RESULT_OK) {
            val compressedImage = Compressor(this).setMaxWidth(640).setMaxHeight(480).setQuality(60)
                .setCompressFormat(Bitmap.CompressFormat.PNG)
                .compressToFile(photoFile)

            assignmentActitvityImage.add(compressedImage)
            val myBitmap = BitmapFactory.decodeFile(photoFile!!.absolutePath)
            val base64: String = encodeTobase64(myBitmap)!!
            val adapterlist = ShowListAdapter(this@MerchantActivity, assignmentActitvityImage)
            rvAssignmentActivity.setAdapter(adapterlist)


        }

        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_PIC_REQUEST_GST_PIC) {
                currentPhotoPath = mySingleton.getData("currentpath")
                fileAbsolutePath = mySingleton.getData("fileabsolutepath")
                val f = File(fileAbsolutePath)


                //File f = grabImageFile(true, 80);
                gstPicFile = f
                btnUploadGstPic.text = gstPicFile!!.name
                btnUploadGstPic.visibility = View.VISIBLE
            }
        }

        if (data != null) {
            if (requestCode == GALLERY_PIC_REQUEST_GST_PIC) {
                val imageUri = data.data
                try {
                    val image = MediaStore.Images.Media.getBitmap(contentResolver, imageUri)
                    bitmapToFile(image, CAMERA_PIC_REQUEST_GST_PIC)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }

        if (data != null) {
            if (requestCode == GALLERY_PIC_REQUEST_PAN_PIC) {
                val imageUri = data.data
                try {
                    val image = MediaStore.Images.Media.getBitmap(contentResolver, imageUri)
                    bitmapToFile(image, CAMERA_PIC_REQUEST_PAN_PIC)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }

        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_PIC_REQUEST_PAN_PIC) {
                // panPicFile = grabImageFile(true, 80);
                currentPhotoPath = mySingleton.getData("currentpath")
                fileAbsolutePath = mySingleton.getData("fileabsolutepath")
                val f = File(fileAbsolutePath)
                panPicFile = f
                btnUploadPanPic.visibility = View.VISIBLE
                btnUploadPanPic.text = panPicFile!!.name
            }
        } else {
            Toast.makeText(this@MerchantActivity, "something went wrong", Toast.LENGTH_SHORT).show()
        }

    }

    //    File outSideFile = null, activityFile = null, insideFile = null, gstPicFile = null, panPicFile = null;
    private fun bitmapToFile(yourBitmap: Bitmap, reqCode: Int) {
        val filename = Environment.getExternalStorageDirectory()
            .toString() + File.separator + Calendar.getInstance().timeInMillis +
                "temporary_file.png"
        //        String filename = Calendar.getInstance().getTimeInMillis() +
//                "temporary_file.png";
        val f = File(filename)
        try {
            f.createNewFile()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        //Convert bitmap to byte array
        val bos = ByteArrayOutputStream()
        yourBitmap.compress(Bitmap.CompressFormat.PNG, 80 /*ignored for PNG*/, bos)
        val bitmapdata = bos.toByteArray()

//write the bytes in file
        var fos: FileOutputStream? = null
        try {
            fos = FileOutputStream(f)
            fos.write(bitmapdata)

            when (reqCode) {


                504 -> {
                    gstPicFile = f
                    btnUploadGstPic.text = gstPicFile!!.name
                    btnUploadGstPic.visibility = View.VISIBLE
                }
                506 -> {
                    panPicFile = f
                    btnUploadPanPic.text = panPicFile!!.name
                    btnUploadPanPic.visibility = View.VISIBLE
                }


            }
            fos.flush()
            fos.close()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    private fun callImagePost(imagePath: Uri?, imagePathReal: String) {
        kProgressHUD.show()

        var imageFile: File
        if (imagePath != null)
            imageFile = File(Utils.getRealPathFromUri(this, imagePath))
        else
            imageFile = File(imagePathReal)


        val call = apiService.postUserProfileImage(
            MultipartBody.Part.createFormData("profile_image",
                imageFile.name,
                RequestBody.create("image/*".toMediaTypeOrNull(), imageFile)),
            RequestBody.create("text/plain".toMediaTypeOrNull(), sathi_code!!))

        call.enqueue(object : Callback<ProfileResponce> {
            override fun onResponse(
                call: Call<ProfileResponce>,
                response: Response<ProfileResponce>,
            ) {
                kProgressHUD.dismiss();
                if (response.isSuccessful && response.code() == 200) {
                    if (response.body()?.status == 1 && response.body()?.data != null) {
                        imgcheckConfirmation.setVisibility(View.VISIBLE)
                        profileData = response.body()?.data
                        imgselfImageDisplay.visibility = View.VISIBLE
                        profileData?.img_path?.let {
                            txtselfPhotoDisplay.visibility = View.GONE
                            Glide.with(this@MerchantActivity).load(it).into(imgselfImageDisplay)
                        }
                        Toast.makeText(this@MerchantActivity,
                            "image upload successfully",
                            Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<ProfileResponce>, t: Throwable) {
                kProgressHUD.dismiss();
                t.printStackTrace()
            }
        })
    }

    private fun apiIntegrationforSelfPhoto() {

        dispatchTakePictureIntentReal(109)
    }

    fun dispatchTakePictureIntentReal(type: Int) {
        CreateFile(type).execute();
    }

    inner class CreateFile(var type: Int) : AsyncTask<String, File, File>() {
        protected override fun doInBackground(vararg strings: String): File? {
            var file: File? = null
            try {
                val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
                val imageFileName = "JPEG_" + timeStamp + "_"
                val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
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

    private fun callCamera(file: File?, type: Int) {
        if (file != null) {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (takePictureIntent.resolveActivity(this.packageManager) != null) {
                if (file != null) {
                    val photoURI = FileProvider.getUriForFile(this@MerchantActivity,
                        BuildConfig.APPLICATION_ID + ".provider",
                        file)
                    mImageUri = Uri.fromFile(file)
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, type)
                }
            }
        }
    }

    private fun encodeTobase64(rotatedBitmap: Bitmap): String? {
        val baos = ByteArrayOutputStream()
        rotatedBitmap.compress(Bitmap.CompressFormat.PNG, 15, baos)
        val b = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
    }

    fun checkValidationFromApi(storeId: String?, type: Int, shopsID: String, categoryid: String):Boolean {
        var validationstatus = true

        val apiService = ApiClient.getClient().create(ApiInterface::class.java)
        Log.e("TAG", "checkValidationFromApi: StoreID>>>>>>>>>"+storeId )
        Log.e("TAG", "checkValidationFromApi: StoreID>>>>>>>>>"+shopsID )
        Log.e("TAG", "checkValidationFromApi: StoreID>>>>>>>>>"+categoryid )
        Log.e("TAG", "checkValidationFromApi: StoreID>>>>>>>>>"+type )
        val call = apiService.checkDuplicasy(storeId,
            "",
            shopsID,
            categoryid,
            type)
        call.enqueue(object : Callback<CategoryResponse> {
            override fun onResponse(
                call: Call<CategoryResponse>,
                response: Response<CategoryResponse>,
            ) {
                if (response.isSuccessful) {
                    if (response.code() >= 200 && response.code() < 700) {
                        if (response.code() == 200) {
                            if (response.body()!!.status == 0) {
                                if (type == 1) {
//
                                    showWarningSimpleAlertDialog(Constant.TITLE,
                                        response.body()!!.message)
                                    if (categoryid == "52") {
                                        etAirtelShopID.setText("")
                                    }
                                    if (categoryid == "66") {
                                        etPineLabsShopID.setText("")
                                    }
                                    if (categoryid == "59") {
                                        etJioShopID.setText("")
                                    }
                                    if(categoryid == "75"){
                                        etOtherStoreID.setText("")
                                    }
                                    else
                                    {
                                         etAirtelShopID.setText("")
                                        etPineLabsShopID.setText("")
                                        etJioShopID.setText("")
                                    }
                                }
                                validationstatus = false
                            }
                           else if (response.body()!!.status == 1) {
                                imgAssignmentCheck.visibility = View.VISIBLE
                                llAssignmentsTab.isEnabled = true
                                btnAssignments.visibility = View.GONE
                                llAssignments.visibility = View.GONE
                                iconaddAssignment.background =
                                    resources.getDrawable(R.drawable.up)
                                llKycTab.background = resources.getDrawable(R.color.primary)
                                llpersonal_profiletab.background =
                                    resources.getDrawable(R.drawable.borderwith_light_green_noradii)
                                llworkprofiletab.background =
                                    resources.getDrawable(R.drawable.borderwith_light_green_noradii)
                                llServicestab.background =
                                    resources.getDrawable(R.drawable.borderwith_light_green_noradii)
                                llRefrences.background =
                                    resources.getDrawable(R.drawable.borderwith_light_green_noradii)
                                llAssignmentsTab.background =
                                    resources.getDrawable(R.drawable.borderwith_light_green_noradii)
                                llKycTab.background =
                                    resources.getDrawable(R.color.merchantBackground)
                                btnReview.visibility = View.VISIBLE

                                if (!imgdone.isVisible) {
                                    Toast.makeText(applicationContext,
                                        "Merchant mobile no. not verified, Please ask merchant to login in reBLISS app and verify OTP.",
                                        Toast.LENGTH_SHORT).show()
                                }
                                validationstatus = true
                            }
                            else{
                                Toast.makeText(this@MerchantActivity, "something went wrong", Toast.LENGTH_SHORT).show()
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
                        //                        callDisplayErrorCode(Integer.parseInt(errorBody.getStatus()), errorBody.getMessage());
                        displaySnackBar!!.DisplaySnackBar(errorBody.message, Constant.TYPE_ERROR)
                        if (errorBody.message.contains("invalid")) {
//                            Logout.Login(context);
                        }
                    } catch (e: java.lang.Exception) {
                        displaySnackBar!!.DisplaySnackBar(Constant.ERROR_INVALID_JSON,
                            Constant.TYPE_ERROR)
                        e.printStackTrace()
                    }
                }
            }

            override fun onFailure(call: Call<CategoryResponse>, t: Throwable) {
                if (t != null && (t is IOException || t is SocketTimeoutException
                            || t is ConnectException || t is NoRouteToHostException
                            || t is SecurityException)
                ) {
                    displaySnackBar!!.DisplaySnackBar(Constant.NETWIRK_ERROR, Constant.TYPE_ERROR)
                }
            }
        })
        return validationstatus
    }
}




