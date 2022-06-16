package com.rebliss.view.activity

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import cn.pedant.SweetAlert.SweetAlertDialog
import com.google.gson.Gson
import com.rebliss.utils.GPSTracker
import com.rebliss.R
import com.rebliss.domain.constant.Constant
import com.rebliss.domain.model.ActivitySelectModel
import com.rebliss.domain.model.ActivityTaskResponse
import com.rebliss.domain.model.ErrorBody
import com.rebliss.domain.model.InsuranceAmount.InsuranceAmountResponse
import com.rebliss.domain.model.payment.SuccessResponse
import com.rebliss.presenter.helper.DisplaySnackBar
import com.rebliss.presenter.retrofit.ApiClient
import com.rebliss.presenter.retrofit.ApiInterface
import com.rebliss.view.activity.Zoho.ZohoActivity
import com.rebliss.view.activity.cudelsimilar.DailyDsr
import com.rebliss.view.activity.myactivityadd.MyActivityCudel
import com.rebliss.view.activity.myactivityadd.MyActivityFormActivity
import com.rebliss.view.adapter.MyTaskAdapter
import kotlinx.android.synthetic.main.activity_my_task.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.net.ConnectException
import java.net.NoRouteToHostException
import java.net.SocketTimeoutException
import java.util.*


class   MyTaskActivity : BaseActivity() {
    private var comingfrom: String = ""
    private var Latitude: Double = 0.0
    private var Longitude: Double = 0.0
    private var city: String = ""
    private var district: String = ""
    private var state: String = ""
    private var zipcode: String = ""
    private var insuranceAmount: String = ""
    private var displaySnackBar: DisplaySnackBar? = null
    var context: Context? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_task)
        displaySnackBar = DisplaySnackBar(this)
        context = this


        if (islocationPermissionGranted()) {
            startService(Intent(this@MyTaskActivity, GPSTracker::class.java))
        }

        if (network.isNetworkConnected(context)) {

            callRupeeAPI()
            getLocationData()
            if (checkGPSstatus()) {
                location()

                callActivityTaskTest()

                checkUserLocation()
            }

        } else {
            displaySnackBar!!.DisplaySnackBar(Constant.NETWIRK_ERROR, Constant.TYPE_ERROR)
        }
        initAdapter()
        initClickListeners()

    }


    private fun checkGPSstatus(): Boolean {
        var locationManager: LocationManager? = null
        var gps_enabled = false
        var network_enabled = false
        if (locationManager == null) {
            locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        }
        try {
            gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        } catch (ex: Exception) {
        }
        try {
            network_enabled = locationManager!!.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        } catch (ex: Exception) {
        }
        if (!gps_enabled && !network_enabled) {
            val dialog: AlertDialog.Builder = AlertDialog.Builder(this@MyTaskActivity)
            dialog.setMessage("GPS not enabled")
            dialog.setPositiveButton("Ok",
                DialogInterface.OnClickListener { dialog, which -> //this will navigate user to the device location settings screen
                    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    startActivity(intent)
                    callActivityTaskTest()

                })
            val alert: AlertDialog = dialog.create()
            alert.show()
        }
        return true
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
                ActivityCompat.requestPermissions(this@MyTaskActivity,
                    arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.CAMERA),
                    1)
                false
            }
        } else {
            //permission is automatically granted on sdk<23 upon installation
            Log.v("permission", "Permission is granted")
            true
        }
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


    private fun location() {
        val geocoder: Geocoder
        val addresses: List<Address>
        geocoder = Geocoder(this, Locale.getDefault())


        try {
            addresses = geocoder.getFromLocation(Latitude,
                Longitude,
                1) // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            if (addresses != null || addresses.size > 0) {
                zipcode = addresses[0].postalCode
                Log.e("TAG", "location: " + addresses[0].subAdminArea)
                val address: String =
                    addresses[0].getAddressLine(0) // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()

                city = addresses[0].getLocality()
                district = addresses[0].subAdminArea

                state = addresses[0].getAdminArea()


            }
        } catch (e: IndexOutOfBoundsException) {
            e.printStackTrace()
        } catch (e: NullPointerException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    private fun initClickListeners() {
        ivBack.setOnClickListener { startActivity(Intent(this, ActivityDashboard::class.java)) }
    }

    private fun initAdapter() {
        rvMyTask.adapter = MyTaskAdapter {
            when (it.clickType) {
                MyTaskAdapter.TRAINING -> {
                    startActivity(Intent(this,
                        TrainingActivity::class.java).putExtra(Constant.CATEGORY_ID,
                        it.subcategoryId1))
                }

                MyTaskAdapter.START_NEW -> {


                    val lastDate = mySingleton.getInt("CURRENT_DATE")
                    val todayDate = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)

                    if (lastDate != todayDate) {


                        callTodaysDeducation()
                        Constant.activitiesList.clear()
                        val selectModel = ActivitySelectModel()
                        selectModel.category = it.categoryId1.toInt()
                        selectModel.subCategory = it.subcategoryId.toInt()
                        selectModel.subCategory1 = it.subcategoryId1.toInt()
                        selectModel.earningTaskID = it.earningTaskId
                        selectModel.amount = it.amount
                        Constant.activitiesList.add(selectModel)


                        mySingleton.saveInt("CURRENT_DATE",
                            Calendar.getInstance().get(Calendar.DAY_OF_MONTH))

                    } else {

                        Log.e("elsePart", "onResponse: " + insuranceAmount)
                        Constant.activitiesList.clear()
                        val selectModel = ActivitySelectModel()
                        selectModel.category = it.categoryId1.toInt()
                        selectModel.subCategory = it.subcategoryId.toInt()
                        selectModel.subCategory1 = it.subcategoryId1.toInt()
                        selectModel.earningTaskID = it.earningTaskId
                        selectModel.amount = it.amount
                        Constant.activitiesList.add(selectModel)

                        mySingleton.saveInt("CURRENT_DATE",
                            Calendar.getInstance().get(Calendar.DAY_OF_MONTH))
                        callTodaysDeducation()
                    }

                }

                MyTaskAdapter.START_CUDEL -> {

                    val lastDate = mySingleton.getInt("CURRENT_DATE")
                    val todayDate = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)


                    if (lastDate != todayDate) {
                        Log.e("Today", "onnnnnnnnnnnnn" + insuranceAmount)

                        callTodaysDeducationforCudel()
                        Constant.activitiesList.clear()
                        val selectModel = ActivitySelectModel()
                        selectModel.category = it.categoryId1.toInt()
                        selectModel.subCategory = it.subcategoryId.toInt()
                        selectModel.subCategory1 = it.subcategoryId1.toInt()
                        selectModel.earningTaskID = it.earningTaskId
                        selectModel.amount = it.amount
                        Constant.activitiesList.add(selectModel)

                        mySingleton.saveInt("CURRENT_DATE",
                            Calendar.getInstance().get(Calendar.DAY_OF_MONTH))
                    } else {
                        Constant.activitiesList.clear()
                        val selectModel = ActivitySelectModel()
                        selectModel.category = it.categoryId1.toInt()
                        selectModel.subCategory = it.subcategoryId.toInt()
                        selectModel.subCategory1 = it.subcategoryId1.toInt()
                        selectModel.earningTaskID = it.earningTaskId
                        selectModel.amount = it.amount
                        Constant.activitiesList.add(selectModel)

                        mySingleton.saveInt("CURRENT_DATE",
                            Calendar.getInstance().get(Calendar.DAY_OF_MONTH))
                        callTodaysDeducationforCudel()

                    }
                }
                MyTaskAdapter.START_DAILY_DSR -> {

                    val lastDate = mySingleton.getInt("CURRENT_DATE")
                    val todayDate = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)


                    if (lastDate != todayDate) {
                        Log.e("Today", "onnnnnnnnnnnnn" + insuranceAmount)
                        callTodaysDeducationforDailyDSR()
                        Constant.activitiesList.clear()
                        val selectModel = ActivitySelectModel()
                        selectModel.category = it.categoryId1.toInt()
                        selectModel.subCategory = it.subcategoryId.toInt()
                        selectModel.subCategory1 = it.subcategoryId1.toInt()
                        selectModel.earningTaskID = it.earningTaskId
                        selectModel.amount = it.amount
                        Constant.activitiesList.add(selectModel)

                        mySingleton.saveInt("CURRENT_DATE",
                            Calendar.getInstance().get(Calendar.DAY_OF_MONTH))
                    } else {
                        Constant.activitiesList.clear()
                        val selectModel = ActivitySelectModel()
                        selectModel.category = it.categoryId1.toInt()
                        selectModel.subCategory = it.subcategoryId.toInt()
                        selectModel.subCategory1 = it.subcategoryId1.toInt()
                        selectModel.earningTaskID = it.earningTaskId
                        selectModel.amount = it.amount
                        Constant.activitiesList.add(selectModel)

                        mySingleton.saveInt("CURRENT_DATE",
                            Calendar.getInstance().get(Calendar.DAY_OF_MONTH))
                        callTodaysDeducationforDailyDSR()
                        //  startActivity(Intent(this, MyActivityCudel::class.java))
                    }
                }

                MyTaskAdapter.PINE_LABS -> {
                    val lastDate = mySingleton.getInt("CURRENT_DATE")
                    val todayDate = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)


                    if (lastDate != todayDate) {
                        Log.e("Today", "onnnnnnnnnnnnn" + insuranceAmount)
                        callTodaysDeducationforPineLabs()
                        Constant.activitiesList.clear()
                        val selectModel = ActivitySelectModel()
                        selectModel.category = it.categoryId1.toInt()
                        selectModel.subCategory = it.subcategoryId.toInt()
                        selectModel.subCategory1 = it.subcategoryId1.toInt()
                        selectModel.earningTaskID = it.earningTaskId
                        selectModel.amount = it.amount
                        Constant.activitiesList.add(selectModel)

                        mySingleton.saveInt("CURRENT_DATE",
                            Calendar.getInstance().get(Calendar.DAY_OF_MONTH))
                    } else {
                        Constant.activitiesList.clear()
                        val selectModel = ActivitySelectModel()
                        selectModel.category = it.categoryId1.toInt()
                        selectModel.subCategory = it.subcategoryId.toInt()
                        selectModel.subCategory1 = it.subcategoryId1.toInt()
                        selectModel.earningTaskID = it.earningTaskId
                        selectModel.amount = it.amount
                        Constant.activitiesList.add(selectModel)

                        mySingleton.saveInt("CURRENT_DATE",
                            Calendar.getInstance().get(Calendar.DAY_OF_MONTH))
                        callTodaysDeducationforPineLabs()
                    }
                }
                MyTaskAdapter.START_NEW_FORM -> {
                    val intent = Intent(this, SathiRecords::class.java)
                    intent.putExtra(Constant.CATEGORY_ID, it.subcategoryId1)
                    intent.putExtra(Constant.COMING_FROM, comingfrom)
                    startActivity(intent)

                }

                MyTaskAdapter.VIEW_STATUS -> {
                    if (it.subcategoryId1.equals("66")) {
                        Toast.makeText(this,
                            "functionality is disabled for this category",
                            Toast.LENGTH_SHORT).show()
                    } else if (it.subcategoryId1.equals("72")) {
                        val lastDate = mySingleton.getInt("CURRENT_DATE")
                        val todayDate = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)


                        if (lastDate != todayDate) {
                            Log.e("Today", "onnnnnnnnnnnnn" + insuranceAmount)
                            callTodaysDeducationforNewForm()
                            Constant.activitiesList.clear()
                            val selectModel = ActivitySelectModel()
                            selectModel.category = it.categoryId1.toInt()
                            selectModel.subCategory = it.subcategoryId.toInt()
                            selectModel.subCategory1 = it.subcategoryId1.toInt()
                            selectModel.earningTaskID = it.earningTaskId
                            selectModel.amount = it.amount
                            Constant.activitiesList.add(selectModel)

                            mySingleton.saveInt("CURRENT_DATE",
                                Calendar.getInstance().get(Calendar.DAY_OF_MONTH))
                        } else {
                            Constant.activitiesList.clear()
                            val selectModel = ActivitySelectModel()
                            try {
                                selectModel.category = it.categoryId1.toInt()
                                selectModel.subCategory = it.subcategoryId.toInt()
                                selectModel.subCategory1 = it.subcategoryId1.toInt()
                                selectModel.earningTaskID = it.earningTaskId
                                selectModel.amount = it.amount
                                Constant.activitiesList.add(selectModel)

                                mySingleton.saveInt(
                                    "CURRENT_DATE",
                                    Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
                                )
                                callTodaysDeducationforNewForm()
                            } catch (e: NumberFormatException) {
                                e.printStackTrace()
                            }
                        }
                    } else {

                        if (it.subcategoryId1.equals("69")) {
                            comingfrom = "DailyDsr"
                            val intent = Intent(this, FosMyActivitiesDashboardActivity::class.java)
                            intent.putExtra(Constant.CATEGORY_ID, it.subcategoryId1)
                            intent.putExtra(Constant.COMING_FROM, comingfrom)
                            startActivity(intent)
                        } else if (it.subcategoryId1.equals("65")) {
                            comingfrom = "Cudel"
                            val intent = Intent(this, FosMyActivitiesDashboardActivity::class.java)
                            intent.putExtra(Constant.CATEGORY_ID, it.subcategoryId1)
                            intent.putExtra(Constant.COMING_FROM, comingfrom)
                            startActivity(intent)
                        } else {
                            startActivity(Intent(this,
                                FosMyActivitiesDashboardActivity::class.java).putExtra(Constant.CATEGORY_ID,
                                it.subcategoryId1))
                        }
                    }
                }


            }
        }
    }

    private fun callTodaysDeducationforDailyDSR() {
        kProgressHUD.show()
        val call = apiService.getTodaysDeduction(mySingleton.getData("id"))

        call.enqueue(object : Callback<SuccessResponse> {
            override fun onResponse(
                call: Call<SuccessResponse>,
                response: Response<SuccessResponse>,
            ) {
                Log.e("Today", "onResponse?????????>>>>>>>>: " + insuranceAmount)
                Log.e("status", "onResponse?????????>>>>>>>>: " + response.body()?.status)
                kProgressHUD.dismiss();
                if (response.body()?.status == 1 && !insuranceAmount.equals("0")) {
                    SweetAlertDialog(this@MyTaskActivity, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText(Constant.TITLE)
                        .setContentText("Congratulations, You will be insured today, An amount of \u20B9 " + insuranceAmount + " will deduct for today’s insurance cost from your today’s earning. Applicable only if you will complete any task.")
                        .setConfirmText("I agree")
                        .setConfirmClickListener { sweetAlertDialog ->
                            // callDailyDeductionApi()
                            startActivity(Intent(this@MyTaskActivity, DailyDsr::class.java))
                            sweetAlertDialog.dismissWithAnimation()
                        }.show()


                }
                if (response.body()?.status == 0 && !insuranceAmount.equals("0")) {
                    startActivity(Intent(this@MyTaskActivity, DailyDsr::class.java))
                } else {
                    Log.e("else", "onResponse: " + ">>>>>>>else part")
                    startActivity(Intent(this@MyTaskActivity, DailyDsr::class.java))
                }

            }

            override fun onFailure(call: Call<SuccessResponse>, t: Throwable) {
                kProgressHUD.dismiss();
                t.printStackTrace()
            }
        })

    }

    private fun callTodaysDeducationforPineLabs() {
        kProgressHUD.show()
        val call = apiService.getTodaysDeduction(mySingleton.getData("id"))

        call.enqueue(object : Callback<SuccessResponse> {
            override fun onResponse(
                call: Call<SuccessResponse>,
                response: Response<SuccessResponse>,
            ) {
                Log.e("Today", "onResponse?????????>>>>>>>>: " + insuranceAmount)
                Log.e("status", "onResponse?????????>>>>>>>>: " + response.body()?.status)
                kProgressHUD.dismiss();
                if (response.body()?.status == 1 && !insuranceAmount.equals("0")) {
                    SweetAlertDialog(this@MyTaskActivity, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText(Constant.TITLE)
                        .setContentText("Congratulations, You will be insured today, An amount of \u20B9 " + insuranceAmount + " will deduct for today’s insurance cost from your today’s earning. Applicable only if you will complete any task.")
                        .setConfirmText("I agree")
                        .setConfirmClickListener { sweetAlertDialog ->
                            // callDailyDeductionApi()
                            startActivity(Intent(this@MyTaskActivity, ZohoActivity::class.java))
                            sweetAlertDialog.dismissWithAnimation()
                        }.show()
                }
                if (response.body()?.status == 0 && !insuranceAmount.equals("0")) {
                    startActivity(Intent(this@MyTaskActivity, ZohoActivity::class.java))
                } else {
                    Log.e("else", "onResponse: " + ">>>>>>>else part")
                    startActivity(Intent(this@MyTaskActivity, ZohoActivity::class.java))
                }
            }

            override fun onFailure(call: Call<SuccessResponse>, t: Throwable) {
                kProgressHUD.dismiss();
                t.printStackTrace()
            }
        })

    }

    private fun callTodaysDeducationforNewForm() {
        kProgressHUD.show()
        val call = apiService.getTodaysDeduction(mySingleton.getData("id"))

        call.enqueue(object : Callback<SuccessResponse> {
            override fun onResponse(
                call: Call<SuccessResponse>,
                response: Response<SuccessResponse>,
            ) {
                Log.e("Today", "onResponse?????????>>>>>>>>: " + insuranceAmount)
                Log.e("status", "onResponse?????????>>>>>>>>: " + response.body()?.status)
                kProgressHUD.dismiss();
                if (response.body()?.status == 1 && !insuranceAmount.equals("0")) {
                    SweetAlertDialog(this@MyTaskActivity, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText(Constant.TITLE)
                        .setContentText("Congratulations, You will be insured today, An amount of \u20B9 " + insuranceAmount + " will deduct for today’s insurance cost from your today’s earning. Applicable only if you will complete any task.")
                        .setConfirmText("I agree")
                        .setConfirmClickListener { sweetAlertDialog ->
                            // callDailyDeductionApi()
                            startActivity(Intent(this@MyTaskActivity,
                                SearchReblissMerchantActivity::class.java))
                            sweetAlertDialog.dismissWithAnimation()
                        }.show()
                }
                if (response.body()?.status == 0 && !insuranceAmount.equals("0")) {
                    startActivity(Intent(this@MyTaskActivity,
                        SearchReblissMerchantActivity::class.java))
                } else {
                    Log.e("else", "onResponse: " + ">>>>>>>else part")
                    startActivity(Intent(this@MyTaskActivity,
                        SearchReblissMerchantActivity::class.java))
                }
            }

            override fun onFailure(call: Call<SuccessResponse>, t: Throwable) {
                kProgressHUD.dismiss();
                t.printStackTrace()
            }
        })

    }

    private fun callRupeeAPI() {
        kProgressHUD.show()
        val call = apiService.getInsuranceAmount()

        call.enqueue(object : Callback<InsuranceAmountResponse> {
            override fun onResponse(
                call: Call<InsuranceAmountResponse>,
                response: Response<InsuranceAmountResponse>,
            ) {
                kProgressHUD.dismiss();
                if (response.body()?.status == 1) {
                    insuranceAmount = response.body()?.data?.allGroups?.value.toString()
                }
            }

            override fun onFailure(call: Call<InsuranceAmountResponse>, t: Throwable) {
                kProgressHUD.dismiss();
                t.printStackTrace()
            }
        })


    }

    private fun callTodaysDeducation() {
        kProgressHUD.show()

        // val call = apiService.getTodaysDeduction(mySingleton.getData(Constant.USER_ID))
        val call = apiService.getTodaysDeduction(mySingleton.getData("id"))

        call.enqueue(object : Callback<SuccessResponse> {
            override fun onResponse(
                call: Call<SuccessResponse>,
                response: Response<SuccessResponse>,
            ) {
                Log.e("Today", "onResponse?????????>>>>>>>>: " + insuranceAmount)
                Log.e("status", "onResponse?????????>>>>>>>>: " + response.body()?.status)
                kProgressHUD.dismiss();
                if (response.body()?.status == 1 && !insuranceAmount.equals("0")) {
                    SweetAlertDialog(this@MyTaskActivity, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText(Constant.TITLE)
                        .setContentText("Congratulations, You will be insured today, An amount of \u20B9 " + insuranceAmount + " will deduct for today’s insurance cost from your today’s earning. Applicable only if you will complete any task.")
                        .setConfirmText("I agree")
                        .setConfirmClickListener { sweetAlertDialog ->
                            // callDailyDeductionApi()
                            startActivity(Intent(this@MyTaskActivity,
                                MyActivityFormActivity::class.java))
                            // startActivity(Intent(this@MyTaskActivity, SearchReblissMerchantActivity::class.java))
                            sweetAlertDialog.dismissWithAnimation()
                        }.show()


                }
                if (response.body()?.status == 0 && !insuranceAmount.equals("0")) {
                    startActivity(Intent(this@MyTaskActivity, MyActivityFormActivity::class.java))
                } else {
                    Log.e("else", "onResponse: " + ">>>>>>>else part")
                    startActivity(Intent(this@MyTaskActivity, MyActivityFormActivity::class.java))
                }

            }

            override fun onFailure(call: Call<SuccessResponse>, t: Throwable) {
                kProgressHUD.dismiss();
                t.printStackTrace()
            }
        })

    }

    private fun callTodaysDeducationforCudel() {
        kProgressHUD.show()
        val call = apiService.getTodaysDeduction(mySingleton.getData("id"))

        call.enqueue(object : Callback<SuccessResponse> {
            override fun onResponse(
                call: Call<SuccessResponse>,
                response: Response<SuccessResponse>,
            ) {
                Log.e("Today", "onResponsecudel?????????>>>>>>>>: " + insuranceAmount)
                Log.e("status", "onResponsecudel?????????>>>>>>>>: " + response.body()?.status)
                kProgressHUD.dismiss();
                if (response.body()?.status == 1 && !insuranceAmount.equals("0")) {
                    SweetAlertDialog(this@MyTaskActivity, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText(Constant.TITLE)
                        .setContentText("Congratulations, You will be insured today, An amount of \u20B9 " + insuranceAmount + " will deduct for today’s insurance cost from your today’s earning. Applicable only if you will complete any task.")
                        .setConfirmText("I agree")
                        .setConfirmClickListener { sweetAlertDialog ->
                            // callDailyDeductionApi()
                            startActivity(Intent(this@MyTaskActivity, MyActivityCudel::class.java))
                            sweetAlertDialog.dismissWithAnimation()
                        }.show()


                }
                if (response.body()?.status == 0 && !insuranceAmount.equals("0")) {
                    startActivity(Intent(this@MyTaskActivity, MyActivityCudel::class.java))
                } else {
                    Log.e("else", "onResponsecudel: " + ">>>>>>>else part")
                    startActivity(Intent(this@MyTaskActivity, MyActivityCudel::class.java))
                }

            }

            override fun onFailure(call: Call<SuccessResponse>, t: Throwable) {
                kProgressHUD.dismiss();
                t.printStackTrace()
            }
        })

    }

    private fun callDailyDeductionApi() {


        kProgressHUD.show()

        val call = apiService.getDailyDeduction(mySingleton.getData("id"))

        call.enqueue(object : Callback<SuccessResponse> {
            override fun onResponse(
                call: Call<SuccessResponse>,
                response: Response<SuccessResponse>,
            ) {

                Log.e("deductionAPI", "onResponse: " + insuranceAmount)
                kProgressHUD.dismiss();
                startActivity(Intent(this@MyTaskActivity, MyActivityFormActivity::class.java))
            }

            override fun onFailure(call: Call<SuccessResponse>, t: Throwable) {
                kProgressHUD.dismiss();
                t.printStackTrace()
            }
        })
    }


    /* private fun callActivityTask() {
         kProgressHUD.show()

        val call = apiService.getActivityTask(mySingleton.getData(Constant.USER_ID), district.toString().trim(), state.toString().trim())
     //    val call = apiService.getActivityTask(mySingleton.getData(Constant.USER_ID),"cuttack","odisha")
         call.enqueue(object : Callback<ActivityTaskResponse> {
             override fun onResponse(call: Call<ActivityTaskResponse>, response: Response<ActivityTaskResponse>) {

                 kProgressHUD.dismiss();
                 if (response.isSuccessful && response.code() == 200) {
                     if (response.body()?.status == 1 && response.body()?.data != null) {
                         val categoryList = response.body()?.data?.allGroups
                         (rvMyTask.adapter as MyTaskAdapter).updateAdapter(categoryList)
                     } else if (response.body()?.status == 0 && response.body()?.data != null) {
                         Toast.makeText(this@MyTaskActivity, "" + response.body()?.desc.toString(), Toast.LENGTH_SHORT).show()
                     }
                 }
             }

             override fun onFailure(call: Call<ActivityTaskResponse>, t: Throwable) {
                 Toast.makeText(this@MyTaskActivity, "" + t.message, Toast.LENGTH_SHORT).show()
                 kProgressHUD.dismiss();
                 t.printStackTrace()
             }
         })
     }*/

    private fun callActivityTaskTest() {
        kProgressHUD.show()

        val call = apiService.getActivityTaskTest(mySingleton.getData(Constant.USER_ID),
            district.toString().trim(),
            state.toString().trim(),
            zipcode)
        // val call = apiService.getActivityTask(mySingleton.getData(Constant.USER_ID),"Raipur","Chattisgarh")
        call.enqueue(object : Callback<ActivityTaskResponse> {
            override fun onResponse(
                call: Call<ActivityTaskResponse>,
                response: Response<ActivityTaskResponse>,
            ) {

                kProgressHUD.dismiss();
                if (response.isSuccessful && response.code() == 200) {
                    if (response.body()?.status == 1 && response.body()?.data != null) {
                        val categoryList = response.body()?.data?.allGroups
                        (rvMyTask.adapter as? MyTaskAdapter)?.updateAdapter(categoryList)
                    } else if (response.body()?.status == 0 && response.body()?.data != null) {
                        Toast.makeText(this@MyTaskActivity,
                            "" + response.body()?.desc.toString(),
                            Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<ActivityTaskResponse>, t: Throwable) {
                Toast.makeText(this@MyTaskActivity, "" + t.message, Toast.LENGTH_SHORT).show()
                kProgressHUD.dismiss();
                t.printStackTrace()
            }
        })
    }

    private fun checkUserLocation() {
        val apiService = ApiClient.getClient().create(ApiInterface::class.java)
        val call = apiService.updateUserCurrentLocation(mySingleton.getData(Constant.USER_ID),
            district,
            state,
            zipcode)
        call.enqueue(object : Callback<SuccessResponse> {
            override fun onResponse(
                call: Call<SuccessResponse>,
                response: Response<SuccessResponse>,
            ) {
                if (response.isSuccessful) {
                    if (response.code() >= 200 && response.code() < 700) {
                        if (response.code() == 200) {
                            if (response.body()!!.status == 1) {
                                Log.e("TAG", "onResponse: ")
                            }
                        }
                    }
                } else {
                    try {
                        var errorBody = ErrorBody()
                        val gson = Gson()
                        errorBody =
                            gson.fromJson(response.errorBody()!!.string(), ErrorBody::class.java)
                        ;

                    } catch (e: java.lang.Exception) {
                        e.printStackTrace()
                    }
                }
            }

            override fun onFailure(call: Call<SuccessResponse>, t: Throwable) {
                // kProgressHUD.dismiss();
                if (t != null && (t is IOException || t is SocketTimeoutException
                            || t is ConnectException || t is NoRouteToHostException
                            || t is SecurityException)
                ) {
                }
            }
        })
    }


    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, ActivityDashboard::class.java))
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (kProgressHUD.isShowing) {
            kProgressHUD.dismiss()
        }
    }
}