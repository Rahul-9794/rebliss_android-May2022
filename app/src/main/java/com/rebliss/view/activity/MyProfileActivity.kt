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
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.os.*
import android.provider.MediaStore
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.text.InputType
import android.text.TextUtils
import android.util.Base64
import android.util.Base64OutputStream
import android.util.Log
import android.util.Patterns
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.pedant.SweetAlert.SweetAlertDialog
import com.bumptech.glide.Glide
import com.google.android.youtube.player.internal.s
import com.rebliss.BuildConfig
import com.rebliss.R
import com.rebliss.domain.constant.Constant
import com.rebliss.domain.constant.Constant.USER_FOS_TYPE
import com.rebliss.domain.model.EducationResponse
import com.rebliss.domain.model.Occupation.AllGroup
import com.rebliss.domain.model.Occupation.OccupationResponse
import com.rebliss.domain.model.agegroup.AgeGroupResponse
import com.rebliss.domain.model.documentByUser.DocumentByUserModel
import com.rebliss.domain.model.editprofile.EditProfileRequest
import com.rebliss.domain.model.editprofile.EditProfileResponce
import com.rebliss.domain.model.fileupload.FileUploadResponce
import com.rebliss.domain.model.fileupload.UploadRequest
import com.rebliss.domain.model.logout.LogoutResponce
import com.rebliss.domain.model.profile.Data
import com.rebliss.domain.model.profile.ProfileResponce
import com.rebliss.presenter.helper.DisplaySnackBar
import com.rebliss.presenter.helper.RegexUtils
import com.rebliss.presenter.helper.ShowHintOrText
import com.rebliss.presenter.helper.TextUtil
import com.rebliss.presenter.retrofit.ApiClient
import com.rebliss.presenter.retrofit.ApiInterface
import com.rebliss.utils.*
import com.rebliss.view.adapter.DocumentAdapter
import com.rebliss.view.adapter.UploadRemoveAdapter
import kotlinx.android.synthetic.main.activity_my_profile.*
import kotlinx.android.synthetic.main.bank_details_layout.*
import kotlinx.android.synthetic.main.bank_details_layout.view.*
import kotlinx.android.synthetic.main.my_profile_layout.*
import kotlinx.android.synthetic.main.my_profile_layout.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.*
import java.text.SimpleDateFormat
import java.util.*


class MyProfileActivity : BaseActivity() {
    var uploadTypeOption =
        arrayOf("Select Document Type", "Aadhaar(Optional)", "Driving Licence", "Voter Id")
    private var selectedUploadType = ""
    private var isReloadNeed = true
    private var educationName: String = ""
    private var storeName: String = ""
    private var allGroupListEducation: ArrayList<EducationResponse.Data.AllGroup?>? = arrayListOf()
    private var educationNameArrayList: ArrayList<String> = arrayListOf()

    private var mGender: String = ""
    private var selectedImagePath: String? = null
    private var mImageUri: Uri? = null
    private lateinit var mCurrentPhotoPath: String
    private lateinit var ChequeAdapter: UploadRemoveAdapter
    private lateinit var AdharAdapter: UploadRemoveAdapter
    private lateinit var GSTAdapter: UploadRemoveAdapter
    private lateinit var PanAdapter: UploadRemoveAdapter
    var allGroupArrayList: List<AllGroup>? = null
    var allGroupListAge: List<com.rebliss.domain.model.agegroup.AllGroup>? = null
    var allGroupArrayAdapter: ArrayAdapter<String>? = null
    var occupationNameArrayList: MutableList<String> = ArrayList()
    var ageGroupNameArrayList: MutableList<String> = ArrayList()
    var occupation_name: String? = ""
    var ageGroup_list_name: String? = ""
    var Pan_Url: String = ""
    var Gst_Url: String = ""
    var Cheque_Url: String = ""
    var Adhar_Url: String = ""
    private var spUploadOptionPosition = 0

    var genderStringArray = arrayOf(
        "Select Gender",
        "Male",
        "Female",
        "Transgender"
    )

    private val PAN_IMAGE_CLICK_REQUEST = 201
    private val ADHAR_IMAGE_CLICK_REQUEST = 205
    private val ADHAR_IMAGE_ATTACH_REQUEST = 305
    private val PAN_IMAGE_ATTACH_REQUEST = 202
    private val GST_IMAGE_CLICK_REQUEST = 301
    private val GST_IMAGE_ATTACH_REQUEST = 302
    private val CHEQUE_IMAGE_ATTACH_REQUEST = 203
    private val CHEQUE_IMAGE_CLICK_REQUEST = 204

    private var firstNames: String = ""
    private var lastNames: String = ""
    private var occupation_type: String = ""

    private var displaySnackBar: DisplaySnackBar? = null
    private var mYear = 0
    private var mMonth: Int = 0
    private var mDay: Int = 0
    private var date = ""
    private var selectedDate = ""

    var spinnerArrayAdapter: ArrayAdapter<String>? = null

    private lateinit var linearLayoutManager: LinearLayoutManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_profile)

        initViews()

        visibilityHideShow()

        apiIntegrationforDocument()
        if (isNetworkConnected()) {
            spinnerGender()
            spinnerTypeofDoc()
            getAgeGroup()
            getEducationData()
            getOccupationApiData()

            Handler().postDelayed({
                callProfileAPI()
            }, 200)
        } else {
            displaySnackBar!!.DisplaySnackBar(Constant.NETWIRK_ERROR, Constant.TYPE_ERROR)
        }

        initClickListeners()
        findViewById<View>(R.id.ivLogout).setOnClickListener {
            callLogout()
        }

    }

    private fun apiIntegrationforDocument()
    {
            kProgressHUD.show()
            var monthDate = date
            if (monthDate.isBlank()) {
                monthDate = getCurrentMonthDate()
            }
            val call = apiService.getDocbyUser(mySingleton.getData(Constant.USER_ID))
            call.enqueue(object : Callback<DocumentByUserModel> {
                override fun onResponse(
                    call: Call<DocumentByUserModel>,
                    response: Response<DocumentByUserModel>,
                ) {
                    kProgressHUD.dismiss()
                    if (response.isSuccessful && response.code() == 200) {
                        if (response.body()?.status == 1 && response.body()?.data != null) {
                            val categoryList = response.body()?.data
                            val rvDatas = findViewById(R.id.rvDocument) as RecyclerView
                            linearLayoutManager = LinearLayoutManager(this@MyProfileActivity)
                            rvDatas.adapter =
                                DocumentAdapter(categoryList as ArrayList<com.rebliss.domain.model.documentByUser.Data>, this@MyProfileActivity)
                            rvDatas.layoutManager = linearLayoutManager
                            (rvDatas.adapter as? DocumentAdapter)?.updateAdapter(categoryList)
                        } else {

                            Log.e("TAG", "onResponse: elseeeeeeeee" )
                        }
                    }
                }

                override fun onFailure(call: Call<DocumentByUserModel>, t: Throwable) {
                    kProgressHUD.dismiss()
                    t.printStackTrace()
                }
            })
    }
    private fun visibilityHideShow() {
        if (mySingleton.getData(Constant.USER_FOS_TYPE).contains("rBM")) {
            llDOB.visibility = View.VISIBLE
            llGST.visibility = View.VISIBLE
            llAdhar.visibility = View.VISIBLE
        }

    }

    override fun onResume() {
        super.onResume()

        if (isReloadNeed) {

            //showDataOnView();
            callProfileAPI()
            setHint()
            isReloadNeed = false
        }
    }

    private fun setHint() {
        if (selectedUploadType.equals("aadhaar",
                ignoreCase = true) || spUploadOptionPosition == 1
        ) {
            textCPAdhar.setText(ShowHintOrText.GetMandatory("Aadhar Number"))
        } else if (selectedUploadType.equals("dl",
                ignoreCase = true) || spUploadOptionPosition == 2
        ) {
            textCPAdhar.setText(ShowHintOrText.GetMandatory("Driving Licence Number"))
        } else if (selectedUploadType.equals("voterId",
                ignoreCase = true) || spUploadOptionPosition == 3
        ) {
            textCPAdhar.setText(ShowHintOrText.GetMandatory("Voter Id Number"))
        }
    }

    private fun callLogout() {

        kProgressHUD.show()

        val call = apiService.getUserLogout(mySingleton.getData(Constant.TOKEN_BASE_64))
        call.enqueue(object : Callback<LogoutResponce> {
            override fun onResponse(
                call: Call<LogoutResponce>,
                response: Response<LogoutResponce>,
            ) {
                this@MyProfileActivity.kProgressHUD?.dismiss()
                if (response.isSuccessful && response.code() == 200) {
                    if (response.body()?.status == 1 && response.body()?.message != null) {
                        showSimpleAlertDialog(response.body()!!.message)
                    }
                }
            }

            override fun onFailure(call: Call<LogoutResponce>, t: Throwable) {
                kProgressHUD?.dismiss()
                t.printStackTrace()
            }
        })


    }

    private fun showSimpleAlertDialog(message: String?) {

        SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE).setTitleText(Constant.TITLE)
            .setContentText(message)
            .setConfirmClickListener { sweetAlertDialog ->
                sweetAlertDialog.dismissWithAnimation()
                val login = Intent(this@MyProfileActivity, ActivityLogin::class.java)
                val device_token = mySingleton.getData(Constant.DEVICE_FCM_TOKEN)
                mySingleton.clearData()
                mySingleton.saveData(Constant.DEVICE_FCM_TOKEN, device_token)
                login.putExtra(Constant.UNAUTHORISE_TOKEN, "0")
                login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                login.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                login.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(login)
                finishAffinity()
            }
            .show()
    }

    private fun initClickListeners() {
        btnMyProfile.setOnClickListener {
            layoutMyProfile.visibility = View.VISIBLE
            layoutBankDetails.visibility = View.GONE
            layoutDocument.visibility = View.GONE
            btnMyProfile.selectButton()
            btnDocument.deselectButton()
            btnBankDetails.deselectButton()
        }

        btnBankDetails.setOnClickListener {
            layoutBankDetails.visibility = View.VISIBLE
            layoutMyProfile.visibility = View.GONE
            layoutDocument.visibility = View.GONE
            btnMyProfile.deselectButton()
            btnDocument.deselectButton()
            btnBankDetails.selectButton()
        }

        btnDocument.setOnClickListener {
            layoutBankDetails.visibility = View.GONE
            layoutMyProfile.visibility = View.GONE
            layoutDocument.visibility = View.VISIBLE
            btnMyProfile.deselectButton()
            btnDocument.selectButton()
            btnBankDetails.deselectButton()
        }

        btnEditProfile.setOnClickListener {
            if (forValidation()) {
                if (isNetworkConnected()) {
                    callUpdateProfile()
                } else {
                    displaySnackBar?.DisplaySnackBar(Constant.NETWIRK_ERROR, Constant.TYPE_ERROR)
                }
            }
        }

        btnEditDocs.setOnClickListener {
            if (isValidDocs())
                if (isNetworkConnected()) {
                    callUpdateDocProfile()
                } else {
                    displaySnackBar?.DisplaySnackBar(Constant.NETWIRK_ERROR, Constant.TYPE_ERROR)
                }
        }

        Imcamera01.setOnClickListener {
            if (isStoragePermissionGranted()) dispatchTakePictureIntentReal(
                ADHAR_IMAGE_CLICK_REQUEST)
        }

        Imattach01.setOnClickListener {
            if (isStoragePermissionGranted()) choosePictureFromGallery(ADHAR_IMAGE_ATTACH_REQUEST)
        }

        Imcamera02.setOnClickListener {
            if (isStoragePermissionGranted())
                dispatchTakePictureIntentReal(PAN_IMAGE_CLICK_REQUEST)
        }
        ImcameraGst.setOnClickListener {
            if (isStoragePermissionGranted())
                dispatchTakePictureIntentReal(GST_IMAGE_CLICK_REQUEST)
        }
        ImattachGst.setOnClickListener {
            if (isStoragePermissionGranted()) choosePictureFromGallery(GST_IMAGE_ATTACH_REQUEST)
        }
        Imattach02.setOnClickListener {
            if (isStoragePermissionGranted()) choosePictureFromGallery(PAN_IMAGE_ATTACH_REQUEST)
        }

        Imcamera06.setOnClickListener {
            if (isStoragePermissionGranted())
                dispatchTakePictureIntentReal(CHEQUE_IMAGE_CLICK_REQUEST)
        }

        Imattach06.setOnClickListener {
            if (isStoragePermissionGranted()) choosePictureFromGallery(CHEQUE_IMAGE_ATTACH_REQUEST)
        }

        ivBack.setOnClickListener { onBackPressed() }

        etdateofbirth.setOnClickListener(View.OnClickListener { v -> openDatePickerDialog(v) })

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

            etdateofbirth.setText(date)
        }, mYear, mMonth, mDay)
        datePickerDialog.datePicker.maxDate = System.currentTimeMillis() - 568025136000L
        datePickerDialog.show()
    }


    private fun isValidDocs(): Boolean {
        var status = true
        if (etPan.text.isNotBlank() && Pan_Url.isBlank()) {
            status = false
            Toast.makeText(this, "Please upload Pan Image", Toast.LENGTH_SHORT).show()
        }
        if (!Cheque_Url.isBlank()) {
            if (etBankName.text.isBlank() || etAcNo.text.isBlank() || etIFSC.text.isBlank() || etNameAsBank.text.isBlank()) {
                status = false
                showWarningSimpleAlertDialog(Constant.TITLE, "Please enter Bank Details ")
            }

        }
        if (etPan.text.isBlank() && Pan_Url.isNotBlank()) {
            status = false
            showWarningSimpleAlertDialog(Constant.TITLE, "Please enter Pan number")
        }
        if (etGSTnum.text.isBlank() && Gst_Url.isNotBlank()) {
            status = false
            showWarningSimpleAlertDialog(Constant.TITLE, "Please enter GST number")
        }
        if (etPan.text.isNotBlank() && etPan.text.length != 10 && !RegexUtils.isValidIPan(etPan.text.toString())) {
            status = false
            showWarningSimpleAlertDialog(Constant.TITLE, "Please enter valid Pan number")

        }

        if (etBankName.text.isNotBlank() && Cheque_Url.isBlank()) {
            status = false
            showWarningSimpleAlertDialog(Constant.TITLE, "Please upload Bank Proof")

        }
        return status
    }

    private fun callUpdateProfile() {
        kProgressHUD.show()
        if (selectedUploadType.equals("aadhaar")) {
            spUploadOptionPosition = 0
        } else if (selectedUploadType.equals("dl")) {
            spUploadOptionPosition = 1
        } else {
            spUploadOptionPosition = 2
        }
        val call: Call<ProfileResponce> = apiService.updateUserProfile(
            mySingleton.getData(Constant.USER_ID),
            etFirstName.text.toString(),
            etLastName.text.toString(),
            etShopName.text.toString(),
            spinnerOccupation.selectedItem as String,
            spinnerAgeRange.selectedItem as String,
            mGender,
            spinnerEducation.selectedItem as String,
            etEmailId.text.toString(),
            getServerFormatDate(dob),
            Adhar_Url,
            etAadhar.text.toString(),
            spUploadOptionPosition
        )

        call.enqueue(object : Callback<ProfileResponce> {
            override fun onResponse(
                call: Call<ProfileResponce>,
                response: Response<ProfileResponce>,
            ) {
                kProgressHUD.dismiss()
                if (response.isSuccessful && response.code() == 200) {
                    Toast.makeText(this@MyProfileActivity,
                        "Profile Updated Successfully",
                        Toast.LENGTH_SHORT).show()
                    finish()
                }
            }

            override fun onFailure(call: Call<ProfileResponce>, t: Throwable) {
                kProgressHUD.dismiss()
            }
        })
    }

    var dob: String? = null
    private fun forValidation(): Boolean {
        var status = true
        firstNames = etFirstName.text.toString().trim()
        lastNames = etLastName.text.toString().trim()

        if (firstNames.length <= 0) {
            status = false
            showWarningSimpleAlertDialog(Constant.TITLE, getString(R.string.FIRST_NAME))
        }
        if (lastNames.length <= 0) {
            status = false
            showWarningSimpleAlertDialog(Constant.TITLE, getString(R.string.LAST_NAME))
        }

        val email = etEmailId.text.toString()
        if (!isValidEmail(email)) {
            status = false
            showWarningSimpleAlertDialog(Constant.TITLE, getString(R.string.validation_valid_email))
        }

        if (llDOB.isVisible) {
            if (etdateofbirth.text.isNullOrBlank()) {
                status = false
                showWarningSimpleAlertDialog(Constant.TITLE, getString(R.string.validation_dob))
            } else {
                dob = etdateofbirth.text.toString()
            }
        }

        if (llAdhar.isVisible) {
            if (etAadhar.text.isNullOrBlank() || etAadhar.text.length < 12) {
                status = false
                showWarningSimpleAlertDialog(Constant.TITLE, getString(R.string.validation_adhar))
            }
        }

        if (Adhar_Url.isNullOrBlank()) {
            status = false
            showWarningSimpleAlertDialog(Constant.TITLE,
                getString(R.string.validation_adhar_images))
        }
        if (selectedUploadType.equals("aadhaar", ignoreCase = true)) {
            val strings = Adhar_Url.split(",").toTypedArray()
            if (strings.size < 2 && (selectedUploadType.equals("aadhaar",
                    ignoreCase = true) || spUploadOptionPosition == 0)
            ) {
                status = false
                showWarningSimpleAlertDialog(Constant.TITLE, "Please add both sided Aadhaar proof")
            }
        }

        return status
    }

    fun isValidEmail(target: CharSequence?): Boolean {
        if (target.isNullOrBlank()) return false
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches()
    }


    private fun showWarningSimpleAlertDialog(title: String, message: String) {
        SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE).setTitleText(title)
            .setContentText(message)
            .setConfirmClickListener { sweetAlertDialog -> sweetAlertDialog.dismissWithAnimation() }
            .show()
    }


    private fun dispatchTakePictureIntentReal(panImageClickRequest: Int) {
        CreateFile(panImageClickRequest).execute()
    }

    private fun initViews() {
        displaySnackBar = DisplaySnackBar(this)
        btnMyProfile.selectButton()
        layoutMyProfile.visibility = View.VISIBLE

        btnBankDetails.deselectButton()
        btnDocument.deselectButton()
        layoutBankDetails.visibility = View.GONE

        imgChkPan.visibility = View.GONE
        imgChkPan.visibility = View.GONE


    }


    fun callProfileAPI() {
        kProgressHUD.show()

        val call = apiService.getProfile(mySingleton.getData(Constant.TOKEN_BASE_64))
        call.enqueue(object : Callback<ProfileResponce> {
            override fun onResponse(
                call: Call<ProfileResponce>,
                response: Response<ProfileResponce>,
            ) {
                kProgressHUD.dismiss()
                if (response.isSuccessful && response.code() == 200) {
                    if (response.body()?.status == 1 && response.body()?.data != null) {
                        response.body()?.data?.let {

                            Handler().postDelayed({
                                try {
                                    setDataOnViews(it)
                                } catch (e: NullPointerException) {
                                    e.printStackTrace()
                                }
                            }, 500)
                        }

                        if (response?.body()?.data!!.fos_type.equals("rBM")) {
                            occupation_type = "mo"

                            val sharedPreferences =
                                getSharedPreferences("MySharedPref", MODE_PRIVATE)
                            val myEdit = sharedPreferences.edit()
                            myEdit.putString("occupation", occupation_type);
                            myEdit.commit();

                        } else {
                            occupation_type = "so"
                            val sharedPreferences =
                                getSharedPreferences("MySharedPref", MODE_PRIVATE)
                            val myEdit = sharedPreferences.edit()
                            myEdit.putString("occupation", occupation_type);
                            myEdit.commit();

                        }
                    }
                }
            }

            override fun onFailure(call: Call<ProfileResponce>, t: Throwable) {
                kProgressHUD.dismiss()
                t.printStackTrace()
            }
        })
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
            override fun onResponse(
                call: Call<FileUploadResponce>,
                response: Response<FileUploadResponce>,
            ) {
                kProgressHUD.dismiss()
                if (response.isSuccessful && response.code() == 200) {
                    if (response.body()?.status == 1 && response.body()?.data != null) {

                        if (CallType == PAN_IMAGE_ATTACH_REQUEST || CallType == PAN_IMAGE_CLICK_REQUEST) {
                            if (TextUtil.isStringNullOrBlank(etPan.text.toString())) {
                                Pan_Url = Pan_Url + response.body()!!.file_name
                            } else {
                                Pan_Url = Pan_Url + "," + response.body()!!.file_name
                            }

                            if (!TextUtil.isStringNullOrBlank(Pan_Url)) {
                                val Pan_images: Array<String> =
                                    Pan_Url.split(",".toRegex()).toTypedArray()
                                // List<String> images = Arrays.asList(Pan_images);
                                if (PanAdapter != null) {
                                    PanAdapter.updateData(Pan_images)
                                } else {
                                    PanAdapter = UploadRemoveAdapter(Pan_images,
                                        R.layout.doc_upload_remove,
                                        this@MyProfileActivity,
                                        Constant.K_GST)
                                    pan_recycler_view.adapter = PanAdapter
                                    pan_recycler_view.isScrollContainer = false
                                }
                            }
                            imgChkPan.visibility = View.VISIBLE

                        } else if (CallType == GST_IMAGE_ATTACH_REQUEST || CallType == GST_IMAGE_CLICK_REQUEST) {
                            if (TextUtil.isStringNullOrBlank(etPan.text.toString())) {
                                Gst_Url = Gst_Url + response.body()!!.file_name
                            } else {
                                Gst_Url = Gst_Url + "," + response.body()!!.file_name
                            }

                            if (!TextUtil.isStringNullOrBlank(Gst_Url)) {
                                val gstImages: Array<String> =
                                    Gst_Url.split(",".toRegex()).toTypedArray()
                                // List<String> images = Arrays.asList(Pan_images);
                                if (GSTAdapter != null) {
                                    GSTAdapter.updateData(gstImages)
                                } else {
                                    GSTAdapter = UploadRemoveAdapter(gstImages,
                                        R.layout.doc_upload_remove,
                                        this@MyProfileActivity,
                                        Constant.K_GST)
                                    gst_recycler_view.adapter = GSTAdapter
                                    gst_recycler_view.isScrollContainer = false
                                }
                            }
                            imgChkGST.visibility = View.VISIBLE
                        } else if (CallType == CHEQUE_IMAGE_ATTACH_REQUEST || CallType == CHEQUE_IMAGE_CLICK_REQUEST) {
                            if (TextUtil.isStringNullOrBlank(Cheque_Url)) {
                                Cheque_Url = Cheque_Url + response.body()!!.file_name
                            } else {
                                Cheque_Url = Cheque_Url + "," + response.body()!!.file_name
                            }
                            if (!TextUtil.isStringNullOrBlank(Cheque_Url)) {
                                val Cheque_images: Array<String> =
                                    Cheque_Url.split(",".toRegex()).toTypedArray()
                                if (ChequeAdapter != null) {
                                    ChequeAdapter.updateData(Cheque_images)

                                } else {
                                    ChequeAdapter = UploadRemoveAdapter(Cheque_images,
                                        R.layout.doc_upload_remove,
                                        this@MyProfileActivity,
                                        Constant.K_CHEQUE)
                                    bankProofRecycler.adapter = ChequeAdapter
                                    bankProofRecycler.isScrollContainer = false
                                }
                            }
                            imgChkBank.visibility = View.VISIBLE
                        } else if (CallType == ADHAR_IMAGE_ATTACH_REQUEST || CallType == ADHAR_IMAGE_CLICK_REQUEST) {
                            if (TextUtil.isStringNullOrBlank(Adhar_Url)) {
                                Adhar_Url = Adhar_Url + response.body()!!.file_name
                            } else {
                                Adhar_Url = Adhar_Url + "," + response.body()!!.file_name
                            }
                            if (!TextUtil.isStringNullOrBlank(Adhar_Url)) {
                                val Adhar_images: Array<String> =
                                    Adhar_Url.split(",".toRegex()).toTypedArray()
                                if (AdharAdapter != null) {
                                    AdharAdapter.updateData(Adhar_images)
                                } else {
                                    AdharAdapter = UploadRemoveAdapter(Adhar_images,
                                        R.layout.doc_upload_remove,
                                        this@MyProfileActivity,
                                        Constant.K_AADHAAR)
                                    aadhar_recycler_view.adapter = AdharAdapter
                                    aadhar_recycler_view.isScrollContainer = false
                                }
                            }
                            Imcheck01.visibility = View.VISIBLE
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

    private fun setDataOnViews(data: Data) {


        Glide.with(this).load(data.img_path).placeholder(R.drawable.placehold)
            .into(layoutMyProfile.ivUserProfile)

        layoutMyProfile.etFirstName.setText(data.first_name)
        layoutMyProfile.etLastName.setText(data.last_name)

        if (data.upload_type_option == null) {
            data.upload_type_option = 0
        }

        if (data.upload_type_option == 0) {
            spUploadOption.setSelection(1)
        }
        if (data.upload_type_option == 1) {
            spUploadOption.setSelection(2)
        }
        if (data.upload_type_option == 2) {
            spUploadOption.setSelection(3)
        }

        if (data.fos_shop_name == null) {
            data.fos_shop_name = ""
        }
        if (data.fos_shop_name != null && data.fos_shop_name.equals("")) {
            layoutMyProfile.llShopName.visibility = View.GONE
        } else {
            layoutMyProfile.llShopName.visibility = View.VISIBLE
        }
        layoutMyProfile.etShopName.setText(data.fos_shop_name)
        layoutMyProfile.etMobileNumber.setText(data.phone_number)
        layoutMyProfile.etRefferalCode.setText(data.code)
        layoutMyProfile.etEmailId.setText(data.personal_email_id)

        if (llDOB.isVisible) {
            if (data.dob != null) {
                etdateofbirth.setText(getServerDisplayFormatDate(data.dob))
            }
        }

        if (!data.gender.isNullOrBlank()) {
            var tempGender = ""
            if (data.gender.contains("M")) {
                tempGender = "Male"
            } else if (data.gender.contains("F")) {
                tempGender = "Female"
            } else {
                tempGender = "Transgender"
            }
            val genderPosition =
                (spinnerGender.adapter as? ArrayAdapter<String>)?.getPosition(tempGender)
            spinnerGender.setSelection(genderPosition!!)
        }

        if (!data.occupation.isNullOrBlank()) {
            val occupationPosition =
                (spinnerOccupation.adapter as? ArrayAdapter<String>)?.getPosition(data.occupation)
            spinnerOccupation.setSelection(occupationPosition!!)
        }

        if (!data.ageRange.isNullOrBlank()) {
            val position =
                (spinnerAgeRange.adapter as? ArrayAdapter<String>)?.getPosition(data.ageRange)
            spinnerAgeRange.setSelection(position!!)
        }
        if (!data.education.isNullOrBlank()) {
            val position =
                (spinnerEducation.adapter as? ArrayAdapter<String>)?.getPosition(data.education)
            spinnerEducation.setSelection(position!!)
        }

        layoutBankDetails.etBankName.setText(data.bank_name)
        layoutBankDetails.etAcNo.setText(data.account_number)
        layoutBankDetails.etIFSC.setText(data.ifsc_code)
        layoutBankDetails.etNameAsBank.setText(data.bank_holder_name)
        layoutBankDetails.etPan.setText(data.pan_no)
        layoutBankDetails.etGSTnum.setText(data.gst_no)

        if (!data.cp_pan_proof.isNullOrBlank()) {
            val Pan_images: Array<String> = data.cp_pan_proof.split(",".toRegex()).toTypedArray()
            //List<String> images = Arrays.asList(Pan_images);
            Pan_Url = Pan_images.get(0)
            PanAdapter =
                UploadRemoveAdapter(Pan_images, R.layout.doc_upload_remove, this, Constant.K_PAN)
            pan_recycler_view.adapter = PanAdapter
            pan_recycler_view.isScrollContainer = false
        } else {
            val Pan_images = arrayOfNulls<String>(0)
            //List<String> images = Arrays.asList(Pan_images);
            PanAdapter =
                UploadRemoveAdapter(Pan_images, R.layout.doc_upload_remove, this, Constant.K_PAN)
            pan_recycler_view.adapter = PanAdapter
            pan_recycler_view.isScrollContainer = false
        }
        if (!data.gst_proof.isNullOrBlank()) {
            val gst_images: Array<String> = data.gst_proof.split(",".toRegex()).toTypedArray()
            //List<String> images = Arrays.asList(Pan_images);
            Gst_Url = gst_images.get(0)
            GSTAdapter =
                UploadRemoveAdapter(gst_images, R.layout.doc_upload_remove, this, Constant.K_GST)
            gst_recycler_view.adapter = GSTAdapter
            gst_recycler_view.isScrollContainer = false
        } else {
            val gst_images = arrayOfNulls<String>(0)
            //List<String> images = Arrays.asList(Pan_images);
            GSTAdapter =
                UploadRemoveAdapter(gst_images, R.layout.doc_upload_remove, this, Constant.K_GST)
            gst_recycler_view.adapter = GSTAdapter
            gst_recycler_view.isScrollContainer = false
        }
        if (!data.cheque_proof.isNullOrBlank()) {
            val Cheque_images: Array<String> = data.cheque_proof.split(",".toRegex()).toTypedArray()
            Cheque_Url = Cheque_images.get(0)
            ChequeAdapter = UploadRemoveAdapter(Cheque_images,
                R.layout.doc_upload_remove,
                this,
                Constant.K_CHEQUE)
            bankProofRecycler.adapter = ChequeAdapter
            bankProofRecycler.isScrollContainer = false
        } else {
            val Cheque_images = arrayOfNulls<String>(0)
            ChequeAdapter = UploadRemoveAdapter(Cheque_images,
                R.layout.doc_upload_remove,
                this,
                Constant.K_CHEQUE)
            bankProofRecycler.adapter = ChequeAdapter
            bankProofRecycler.isScrollContainer = false
        }
        if (!data.cp_adhar_proof.isNullOrBlank()) {
            etAadhar.setText(data.aadhar_no)
            val adhar_images: Array<String> =
                data.cp_adhar_proof.split(",".toRegex()).toTypedArray()
            adhar_images.forEach {
                Adhar_Url = Adhar_Url.plus(it).plus(",")
            }
            Adhar_Url = Adhar_Url.removeSuffix(",")
            AdharAdapter = UploadRemoveAdapter(adhar_images,
                R.layout.doc_upload_remove,
                this,
                Constant.K_AADHAAR)
            aadhar_recycler_view.adapter = AdharAdapter
            aadhar_recycler_view.isScrollContainer = false
        } else {
            val adhar_images = arrayOfNulls<String>(0)
            AdharAdapter = UploadRemoveAdapter(adhar_images,
                R.layout.doc_upload_remove,
                this,
                Constant.K_AADHAAR)
            aadhar_recycler_view.adapter = AdharAdapter
            aadhar_recycler_view.isScrollContainer = false
        }
    }


    fun isStoragePermissionGranted(): Boolean {
        return if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED
                && checkSelfPermission(Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED
            ) {
                Log.v("permission", "Permission is granted")
                true
            } else {

                Log.v("permission", "Permission is revoked")
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA),
                    1)
                false
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v("permission", "Permission is granted")
            true
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.v("Permission: ", "was " + grantResults[0])
            //resume tasks needing this permission
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


    private fun callCamera(file: File?, type: Int) {
        if (file != null) {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            // Ensure that there's a camera activity to handle the intent
            if (takePictureIntent.resolveActivity(packageManager) != null) {
                // Continue only if the File was successfully created*/
                if (file != null) {
                    val photoURI = FileProvider.getUriForFile(this,
                        BuildConfig.APPLICATION_ID + ".provider",
                        file)

                    mImageUri = Uri.fromFile(file)
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, type)
                }
            }
        }
    }


    private fun choosePictureFromGallery(fromAttach: Int) {
        if (isStoragePermissionGranted()) {
            val i = Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(i, fromAttach)
        }
    }

    fun getImageUri(inContext: Context, inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path =
            MediaStore.Images.Media.insertImage(inContext.contentResolver, inImage, "Title", null)
        return Uri.parse(path)
    }

    fun getRealPathFromURI(uri: Uri?): String? {
        val cursor = contentResolver.query(uri!!, null, null, null, null)
        cursor!!.moveToFirst()
        val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
        return cursor.getString(idx)
    }

    private fun galleryAddPic() {
        val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
        val f: File = File(mCurrentPhotoPath)
        val contentUri = Uri.fromFile(f)
        mediaScanIntent.data = contentUri
        this.sendBroadcast(mediaScanIntent)
    }

    fun getPath(uri: Uri?): String? {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = managedQuery(uri, projection, null, null, null)
        val column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        return cursor.getString(column_index)
    }

    @Throws(IOException::class)
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

    var obtainPathResult = ArrayList<String>()
    var positionForUpload = 0

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != Activity.RESULT_OK)
            return

        Handler().postDelayed({
            runOnUiThread {
                when (requestCode) {
                    PAN_IMAGE_ATTACH_REQUEST -> {
                        try {
                            val selectedImageUri = data!!.data
                            selectedImagePath = getPath(selectedImageUri)
                            Log.i("GalleryPath", selectedImagePath ?: " ")
                            obtainPathResult = ArrayList()
                            obtainPathResult.add(selectedImagePath!!)
                            positionForUpload = 0
                            GetBase64Image(obtainPathResult, PAN_IMAGE_ATTACH_REQUEST).execute()
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }

                    ADHAR_IMAGE_ATTACH_REQUEST -> {
                        try {
                            val selectedImageUri = data!!.data
                            selectedImagePath = getPath(selectedImageUri)
                            Log.i("GalleryPath", selectedImagePath ?: " ")
                            obtainPathResult = ArrayList()
                            obtainPathResult.add(selectedImagePath!!)
                            positionForUpload = 0
                            GetBase64Image(obtainPathResult, ADHAR_IMAGE_ATTACH_REQUEST).execute()
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }

                    GST_IMAGE_ATTACH_REQUEST -> {
                        try {
                            val selectedImageUri = data!!.data
                            selectedImagePath = getPath(selectedImageUri)
                            Log.i("GalleryPath", selectedImagePath ?: " ")
                            obtainPathResult = ArrayList()
                            obtainPathResult.add(selectedImagePath!!)
                            positionForUpload = 0
                            GetBase64Image(obtainPathResult, GST_IMAGE_ATTACH_REQUEST).execute()
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                    PAN_IMAGE_CLICK_REQUEST -> {
                        val f: File? = grabImageFile(true, 80) //true for compression , 80% quality

                        if (f != null) {
                            obtainPathResult = java.util.ArrayList()
                            obtainPathResult.add(f.absolutePath)
                            GetBase64Image(obtainPathResult, PAN_IMAGE_CLICK_REQUEST).execute()
                        }
                    }
                    ADHAR_IMAGE_CLICK_REQUEST -> {
                        val f: File? = grabImageFile(true, 80) //true for compression , 80% quality

                        if (f != null) {
                            //  Toast.makeText(getApplicationContext(),"File to upload is " + f.getAbsolutePath(),Toast.LENGTH_LONG).show();
                            //call image uplaod code here
                            //doFileUpload(f, Constants.IMAGE,f.getName());
                            obtainPathResult = java.util.ArrayList()
                            obtainPathResult.add(f.absolutePath)
                            GetBase64Image(obtainPathResult, ADHAR_IMAGE_CLICK_REQUEST).execute()
                        }
                    }

                    GST_IMAGE_CLICK_REQUEST -> {
                        val f: File? = grabImageFile(true, 80) //true for compression , 80% quality

                        if (f != null) {
                            //  Toast.makeText(getApplicationContext(),"File to upload is " + f.getAbsolutePath(),Toast.LENGTH_LONG).show();
                            //call image uplaod code here
                            //doFileUpload(f, Constants.IMAGE,f.getName());
                            obtainPathResult = java.util.ArrayList()
                            obtainPathResult.add(f.absolutePath)
                            GetBase64Image(obtainPathResult, GST_IMAGE_CLICK_REQUEST).execute()
                        }
                    }
                    CHEQUE_IMAGE_ATTACH_REQUEST -> {
                        try {
                            val selectedImageUri = data!!.data
                            selectedImagePath = getPath(selectedImageUri)
                            Log.i("GalleryPath", selectedImagePath ?: " ")
                            obtainPathResult = ArrayList()
                            obtainPathResult.add(selectedImagePath!!)
                            positionForUpload = 0
                            GetBase64Image(obtainPathResult, CHEQUE_IMAGE_ATTACH_REQUEST).execute()
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                    CHEQUE_IMAGE_CLICK_REQUEST -> {
                        val f: File? = grabImageFile(true, 80) //true for compression , 80% quality

                        if (f != null) {
                            //  Toast.makeText(getApplicationContext(),"File to upload is " + f.getAbsolutePath(),Toast.LENGTH_LONG).show();
                            //call image uplaod code here
                            //doFileUpload(f, Constants.IMAGE,f.getName());
                            obtainPathResult = java.util.ArrayList()
                            obtainPathResult.add(f.absolutePath)
                            GetBase64Image(obtainPathResult, CHEQUE_IMAGE_CLICK_REQUEST).execute()
                        }
                    }
                }

            }
        }, 500)
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
            Log.e("Image Capture Error", e.message!!)
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

    inner class GetBase64Image(var file: List<String>?, var CallType: Int) :
        AsyncTask<String, String, String>() {
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
                extension =
                    file!![positionForUpload].substring(file!![positionForUpload].lastIndexOf("."))
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


    private fun spinnerGender() {
        val spinnerArrayAdapter: ArrayAdapter<String?> =
            object : ArrayAdapter<String?>(this, R.layout.spinner_item_gender, genderStringArray) {
                override fun isEnabled(position: Int): Boolean {
                    return position != 0
                }

                override fun getDropDownView(
                    position: Int,
                    convertView: View?,
                    parent: ViewGroup,
                ): View {
                    val view = super.getDropDownView(position, convertView, parent)
                    val tv = view as TextView
                    if (position == 0) {
                        tv.setTextColor(Color.GRAY)
                        tv.typeface = App.LATO_REGULAR
                    } else {
                        tv.setTextColor(Color.BLACK)
                        tv.typeface = App.LATO_REGULAR
                    }
                    return view
                }
            }

        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item_gender)
        spinnerGender?.adapter = spinnerArrayAdapter
        spinnerGender?.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long,
            ) {
                mGender = parent?.getItemAtPosition(position) as String
                if (mGender.contains("Male")) {
                    mGender = "M"
                } else if (mGender.contains("Female")) {
                    mGender = "F"
                } else {
                    mGender = "O"
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun spinnerTypeofDoc() {
        val spinnerArrayAdapter: ArrayAdapter<String?> =
            object : ArrayAdapter<String?>(this, R.layout.spinner_item_gender, uploadTypeOption) {
                /* override fun isEnabled(position: Int): Boolean {
                     return position != 0
                 }*/

                override fun getDropDownView(
                    position: Int,
                    convertView: View?,
                    parent: ViewGroup,
                ): View {
                    val view = super.getDropDownView(position, convertView, parent)
                    val tv = view as TextView
                    if (position == 0) {
                        tv.setTextColor(Color.GRAY)
                        tv.typeface = App.LATO_REGULAR
                    } else {
                        tv.setTextColor(Color.BLACK)
                        tv.typeface = App.LATO_REGULAR
                    }
                    return view
                }
            }

        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item)
        spUploadOption?.adapter = spinnerArrayAdapter




        spUploadOption.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long,
            ) {
                spUploadOptionPosition = position
                if (spUploadOptionPosition == 1) {
                    selectedUploadType = "aadhaar"
                    etAadhar.setHint("Enter 12 digits aadhar number")
                    etAadhar.setInputType(InputType.TYPE_CLASS_NUMBER)
                    val maxLength = 12
                    val FilterArray = arrayOfNulls<InputFilter>(1)
                    FilterArray[0] = LengthFilter(maxLength)
                    etAadhar.setFilters(FilterArray)
                    textCPAdhar.text = ShowHintOrText.GetMandatory("Aadhar Number")
                } else if (spUploadOptionPosition == 2) {
                    selectedUploadType = "dl"
                    etAadhar.setInputType(InputType.TYPE_CLASS_TEXT)
                    etAadhar.setHint("Enter 15 digits driving licence number")
                    val maxLength = 15
                    val FilterArray = arrayOfNulls<InputFilter>(1)
                    FilterArray[0] = LengthFilter(maxLength)
                    etAadhar.setFilters(FilterArray)
                    textCPAdhar.text = ShowHintOrText.GetMandatory("Driving Licence Number")
                } else if (spUploadOptionPosition == 3) {
                    selectedUploadType = "voterId"
                    etAadhar.setInputType(InputType.TYPE_CLASS_TEXT)
                    etAadhar.setHint("Enter 11 digits voter id number")
                    val maxLength = 11
                    val FilterArray = arrayOfNulls<InputFilter>(1)
                    FilterArray[0] = LengthFilter(maxLength)
                    etAadhar.setFilters(FilterArray)
                    textCPAdhar.text = ShowHintOrText.GetMandatory("Voter Id Number")
                } else {
                    Log.e("TAG", "onItemSelected: ")
                }
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {}
        }

    }

    private fun getOccupationApiData() {
        val apiService = ApiClient.getClient().create(ApiInterface::class.java)
        Log.e("TAG", "occupationtype" + occupation_type)

        val occupation_type = if (mySingleton.getData(USER_FOS_TYPE).contains("rBM")) {
            "mo"
        } else {
            "so"
        }

        val responseCall = apiService.getOccupationDATA(occupation_type)
        responseCall.enqueue(object : Callback<OccupationResponse?> {
            override fun onResponse(
                call: Call<OccupationResponse?>,
                response: Response<OccupationResponse?>,
            ) {
                // kProgressHUD.dismiss();
                if (response.isSuccessful) {
                    if (response.code() >= 200 && response.code() < 700) {
                        if (response.body() != null) {
                            if (response.body()!!.status == 1) {
                                if (response.body()!!.data != null && response.body()!!.data.allGroups.size > 0) {
                                    allGroupArrayList = response.body()!!.data.allGroups
                                    occupationNameArrayList = java.util.ArrayList<String>()
                                    for (i in allGroupArrayList!!.indices) {
                                        //occupation_id = String.valueOf(response.body().getData().getAllGroups().get(i).getId());
                                        occupation_name = response.body()!!.data.allGroups[i].name
                                        occupationNameArrayList.add(occupation_name!!)
                                    }
                                    if (mySingleton.getData(USER_FOS_TYPE).contains("rBS")) {
                                        occupationNameArrayList.add(0, "Select Category")
                                        txtOccupation.text = "Category"
                                    }
                                    if (!mySingleton.getData(USER_FOS_TYPE).contains("rBM")) {
                                        occupationNameArrayList.add(0, "Select Occupation")
                                        txtOccupation.text = "Occupation"
                                    }
                                    allGroupArrayAdapter = object :
                                        ArrayAdapter<String>(this@MyProfileActivity,
                                            R.layout.spinner_item_gender,
                                            occupationNameArrayList) {

                                        override fun isEnabled(position: Int): Boolean {
                                            return position != 0
                                        }

                                        override fun getDropDownView(
                                            position: Int, convertView: View?,
                                            parent: ViewGroup,
                                        ): View {
                                            val view =
                                                super.getDropDownView(position, convertView, parent)
                                            val tv = view as TextView
                                            if (position == 0) {
                                                tv.setTextColor(Color.GRAY)
                                                tv.typeface = App.LATO_REGULAR
                                            } else {
                                                tv.setTextColor(Color.BLACK)
                                                tv.typeface = App.LATO_REGULAR
                                            }
                                            return view
                                        }
                                    }

                                    allGroupArrayAdapter?.setDropDownViewResource(R.layout.spinner_item_gender)
                                    spinnerOccupation.adapter = allGroupArrayAdapter
                                }
                            }
                        }
                    }
                }
            }

            override fun onFailure(call: Call<OccupationResponse?>, t: Throwable) {
                kProgressHUD.dismiss()
            }
        })
    }

    private fun getAgeGroup() {
        val apiService = ApiClient.getClient().create(ApiInterface::class.java)
        val responseCall = apiService.ageGroupDATA
        responseCall.enqueue(object : Callback<AgeGroupResponse?> {
            override fun onResponse(
                call: Call<AgeGroupResponse?>,
                response: Response<AgeGroupResponse?>,
            ) {
                // kProgressHUD.dismiss();
                if (response.isSuccessful) {
                    if (response.code() in 200..699) {
                        if (response.body() != null) {
                            if (response.body()!!.status == 1) {
                                if (response.body()!!.data != null && response.body()!!.data.allGroups.size > 0) {
                                    allGroupListAge = response.body()!!.data.allGroups
                                    ageGroupNameArrayList = java.util.ArrayList<String>()
                                    for (i in allGroupListAge!!.indices) {
                                        //occupation_id = String.valueOf(response.body().getData().getAllGroups().get(i).getId());
                                        ageGroup_list_name =
                                            response.body()!!.data.allGroups[i].name
                                        ageGroupNameArrayList.add(ageGroup_list_name!!)
                                    }
                                    ageGroupNameArrayList.add(0, "Select Age Limit")
                                    allGroupArrayAdapter = object :
                                        ArrayAdapter<String>(this@MyProfileActivity,
                                            R.layout.spinner_item_gender,
                                            ageGroupNameArrayList) {
                                        override fun isEnabled(position: Int): Boolean {
                                            return position != 0
                                        }

                                        override fun getDropDownView(
                                            position: Int,
                                            convertView: View?,
                                            parent: ViewGroup,
                                        ): View {
                                            val view =
                                                super.getDropDownView(position, convertView, parent)
                                            val tv = view as TextView
                                            if (position == 0) {
                                                // Set the hint text color gray
                                                tv.setTextColor(Color.GRAY)
                                                tv.typeface = App.LATO_REGULAR
                                            } else {
                                                tv.setTextColor(Color.BLACK)
                                                tv.typeface = App.LATO_REGULAR
                                            }
                                            return view
                                        }
                                    }
                                    allGroupArrayAdapter?.setDropDownViewResource(R.layout.spinner_item_gender)
                                    spinnerAgeRange.adapter = allGroupArrayAdapter
                                }
                            }
                        }
                    }
                }
            }

            override fun onFailure(call: Call<AgeGroupResponse?>, t: Throwable) {
                kProgressHUD.dismiss()
            }
        })
    }

    private fun getEducationData() {
        val apiService = ApiClient.getClient().create(ApiInterface::class.java)
        val responseCall = apiService.educationData
        responseCall.enqueue(object : Callback<EducationResponse?> {
            override fun onResponse(
                call: Call<EducationResponse?>,
                response: Response<EducationResponse?>,
            ) {
                // kProgressHUD.dismiss();
                if (response.isSuccessful) {
                    if (response.code() >= 200 && response.body() != null) {
                        if (response.body()!!.status == 1) {
                            if (response.body()!!.data != null && response.body()!!.data!!.allGroups!!.size > 0) {
                                allGroupListEducation = response.body()!!.data!!.allGroups
                                educationNameArrayList = ArrayList<String>()
                                for (i in allGroupListEducation!!.indices) {
                                    educationName = allGroupListEducation?.get(i)?.educationName
                                        ?: ""
                                    educationNameArrayList.add(educationName)
                                }
                                educationNameArrayList.add(0, "Select Education")
                                allGroupArrayAdapter =
                                    object : ArrayAdapter<String>(this@MyProfileActivity,
                                        R.layout.spinner_item_gender, educationNameArrayList) {

                                        override fun isEnabled(position: Int): Boolean {
                                            return position != 0
                                        }

                                        override fun getDropDownView(
                                            position: Int,
                                            convertView: View?,
                                            parent: ViewGroup,
                                        ): View {
                                            val view =
                                                super.getDropDownView(position, convertView, parent)
                                            val tv = view as TextView
                                            if (position == 0) {
                                                // Set the hint text color gray
                                                tv.setTextColor(Color.GRAY)
                                                tv.typeface = App.LATO_REGULAR
                                            } else {
                                                tv.setTextColor(Color.BLACK)
                                                tv.typeface = App.LATO_REGULAR
                                            }
                                            return view
                                        }
                                    }

                                //  allGroupArrayAdapter = new ArrayAdapter<String>(ActivitySignup.this,  android.R.layout.simple_spinner_dropdown_item, ageGroupNameArrayList);
                                //  allGroupArrayAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
                                allGroupArrayAdapter?.setDropDownViewResource(R.layout.spinner_item_gender)
                                spinnerEducation.adapter = allGroupArrayAdapter
                            }
                        }
                    }
                }
            }

            override fun onFailure(call: Call<EducationResponse?>, t: Throwable) {
                kProgressHUD.dismiss()
            }
        })
    }

    val editProfileRequest = EditProfileRequest()
    fun callUpdateDocProfile() {
        try {
            getDataFromViews()
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }
        kProgressHUD.show()
        val call = apiService.postEditProfile(mySingleton.getData(Constant.TOKEN_BASE_64),
            editProfileRequest)
        call.enqueue(object : Callback<EditProfileResponce> {
            override fun onResponse(
                call: Call<EditProfileResponce>,
                response: Response<EditProfileResponce>,
            ) {
                kProgressHUD.dismiss()
                if (response.isSuccessful) {
                    if (response.code() >= 200 && response.code() < 700) {
                        if (response.code() == 200) {
                            if (response.body()!!.status == 1) {
                                if (response.body()!!.data != null) {
                                    Toast.makeText(this@MyProfileActivity,
                                        "Profile Updated Successfully",
                                        Toast.LENGTH_SHORT).show()
                                    finish()
                                }
                            }
                            if (response.body()!!.status == 0) {
                                if (response.body()!!.data != null) {
                                    Toast.makeText(this@MyProfileActivity,
                                        "something went wrong",
                                        Toast.LENGTH_SHORT).show()

                                }
                            }
                        }
                    }
                }
            }

            override fun onFailure(call: Call<EditProfileResponce>, t: Throwable) {
                kProgressHUD.dismiss()
            }
        })
    }

    private fun getDataFromViews() {
        editProfileRequest.pan_no = etPan.text.toString()
        editProfileRequest.cp_pan_proof = Pan_Url
        editProfileRequest.cheque_proof = Cheque_Url
        Log.e("TAG", "getDataFromViews: " + Gst_Url)
        editProfileRequest.gst_proof = Gst_Url
        editProfileRequest.bank_name = etBankName.text.toString()
        editProfileRequest.account_number = etAcNo.text.toString()
        editProfileRequest.ifsc_code = etIFSC.text.toString()
        editProfileRequest.gst_no = etGSTnum.text.toString()
        editProfileRequest.bank_holder_name = etNameAsBank.text.toString()
    }

    fun isNetworkConnected(): Boolean {
        val connectivityManager =
            applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        var networkInfo: NetworkInfo? = null
        networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }
}