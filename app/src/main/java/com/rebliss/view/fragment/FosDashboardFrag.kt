package com.rebliss.view.fragment

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.*
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.PagerSnapHelper
import cn.pedant.SweetAlert.SweetAlertDialog
import com.bumptech.glide.Glide
import com.rebliss.BuildConfig
import com.rebliss.R
import com.rebliss.domain.constant.Constant
import com.rebliss.domain.model.CarouselResponse
import com.rebliss.domain.model.profile.Data
import com.rebliss.domain.model.profile.ProfileResponce
import com.rebliss.utils.AdapterItemDecorator
import com.rebliss.utils.Utils
import com.rebliss.utils.disableCard
import com.rebliss.utils.enableCard
import com.rebliss.view.activity.*
import com.rebliss.view.activity.downloadviaLink.DownloadviaLinkActivity
import com.rebliss.view.adapter.CarouselAdapter
import kotlinx.android.synthetic.main.fragment_fos_dashboard.*
import kotlinx.android.synthetic.main.fragment_fos_dashboard.view.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class FosDashboardFrag : BaseFrag() {
    private val TAG = FosDashboardFrag::class.java.simpleName
    private var mImageUri: Uri? = null
    private lateinit var mCurrentPhotoPath: String
    private var profileVerified: String = ""
    private var profileData: Data? = null
    private lateinit var rootView: View
    private val pickImage = 100
    private var imageUri: Uri? = null
    private var context1: Context? = null
    private var count:Int =0;
    var myValue =""
    private var captureInProgress: Boolean = false
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_fos_dashboard, container, false)
        return rootView
    }


    private var cameFrom:String=""


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context1 = activity
        cameFrom = (arguments?.get("comefrom") as String?)?:""

       myValue = mySingleton.getData("navigatetodashboard")
        Log.e(TAG, "onViewCreated: ")
        Log.e(TAG, "onViewCreated: "+mySingleton.getData("navigatetodashboard") )

        initClickListeners()
        setupImageSlider()


        if (network.isNetworkConnected(requireContext())) {
            callProfileAPI()
            callCarouselApi()
        }


        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (shouldInterceptBackPress()) {

                    activity!!.finish()

                    // in here you can do logic when backPress is clicked
                } else {
                    isEnabled = false

                    activity?.finish()
                }
            }
        })
    }

    private fun shouldInterceptBackPress(): Boolean {
return  true
    }

    private fun initClickListeners() {

        cvProfile.setOnClickListener {
            val viewProfile = Intent(requireActivity(), MyProfileActivity::class.java)
            viewProfile.putExtra("call_from", "1")
            //viewProfile.putExtra("from_sathi_merchant",)
            startActivity(viewProfile)
        }

        cvMyEarnings.setOnClickListener {
            startActivity(Intent(requireActivity(), MyEarningActivity::class.java))
        }

        cvMyTask.setOnClickListener {
            startActivity(Intent(requireActivity(), MyTaskActivity::class.java))
        //    startActivity(Intent(requireActivity(), SearchReblissMerchantActivity::class.java))
        }

        cvOpportunities.setOnClickListener {
            startActivity(Intent(requireActivity(), OpportunityListActivity::class.java))
        }
        ivEditUserProfile.setOnClickListener {

            SweetAlertDialog(requireContext(), SweetAlertDialog.WARNING_TYPE).setTitleText("Image")
                    .setContentText("Select image from")
                    .setConfirmText("Gallery")
                    .setConfirmClickListener { sweetAlertDialog ->
                        sweetAlertDialog.dismissWithAnimation()

                        //TODO error coming there
                        // you need to get the permissions here before opening the gallery.- Tarun

                        if (isStoragePermissionGranted()) {
                            val gallery = Intent(Intent.ACTION_PICK,
                                MediaStore.Images.Media.INTERNAL_CONTENT_URI)
                            startActivityForResult(gallery, pickImage)
                        }
                        else{
                            Toast.makeText(activity, "Please allow permission", Toast.LENGTH_SHORT).show()
                        }
                    }
                    .setCancelText("Camera")
                    .setCancelClickListener { sweetAlertDialog ->
                        sweetAlertDialog.dismissWithAnimation()

                        if (isStoragePermissionGranted()) {
                            captureInProgress = true
                            dispatchTakePictureIntentReal(101)

                        }
                    }
                    .show()

        }

        btnDownload.setOnClickListener { startActivity(Intent(requireActivity(), DownloadviaLinkActivity::class.java)) }
        btnSearchMerchant.setOnClickListener { startActivity(Intent(requireActivity(), SearchReblissMerchantActivity::class.java)) }

    }



    lateinit var carouselAdapter: CarouselAdapter
    private fun setupImageSlider() {
        carouselAdapter = CarouselAdapter(context1!! ,this)
        rvCarousel.adapter = carouselAdapter
        rvCarousel.addItemDecoration(AdapterItemDecorator())
        val snapHelper: PagerSnapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(rvCarousel)
    }

    private fun callImagePost(imagePath: Uri?, imagePathReal: String) {
        kProgressHUD.show()

        var imageFile: File
        if (imagePath != null)
            imageFile = File(Utils.getRealPathFromUri(requireContext(), imagePath))
        else
            imageFile = File(imagePathReal)


        val call = apiService.postUserProfileImage(
                MultipartBody.Part.createFormData("profile_image", imageFile.name, RequestBody.create("image/*".toMediaTypeOrNull(), imageFile)),
                RequestBody.create("text/plain".toMediaTypeOrNull(), mySingleton.getData(Constant.USER_ID))
        )

        call.enqueue(object : Callback<ProfileResponce> {
            override fun onResponse(call: Call<ProfileResponce>, response: Response<ProfileResponce>) {
                kProgressHUD.dismiss();
                if (response.isSuccessful && response.code() == 200) {
                    if (response.body()?.status == 1 && response.body()?.data != null) {
                        profileData = response.body()?.data

                        mySingleton.saveData(Constant.USER_GROUP_ID, response.body()!!.data.group_id)
                        mySingleton.saveData(Constant.USER_GROUP_DETAIL_ID, response.body()!!.data.group_detail_id)
                        mySingleton.saveData(Constant.USER_UNIQUE_REF_CODE, response.body()!!.data.unique_ref_code)
                        mySingleton.saveData(Constant.USER_FOS_TYPE, response.body()!!.data.fos_type)
                        profileVerified = profileData?.profile_verified ?: "0"

                        profileData?.img_path?.let {
                            Glide.with(this@FosDashboardFrag).load(it).into(ivUserProfile)
                        }
                        Log.e(TAG, "navigated to dashboard: "+mySingleton.getData("navigatetodashboard") )
                        if (profileData?.profile_verified.equals("1", ignoreCase = true)) {
                            mySingleton.getData(Constant.USER_UNIQUE_REF_CODE)?.let {
                                textRefCode.text = it

                                liShareLayout.visibility = View.VISIBLE
                                llPendingReview.visibility = View.GONE

                                approvalPending.visibility = View.GONE
                                approvalmessage.visibility = View.GONE
                                Log.e(TAG, "123 " )
                                cvOpportunities.enableCard()
                                cvMyTask.enableCard()
                                cvMyEarnings.enableCard()
                            }

                        }

                        else if(mySingleton.getData("navigatetodashboard").length>0)
                        {
                            liShareLayout.visibility = View.GONE
                            llPendingReview.visibility = View.VISIBLE
                            Log.e(TAG, "456 " )
                            cvOpportunities.enableCard()
                            cvMyTask.disableCard()
                            cvMyEarnings.disableCard()
                            if (cameFrom=="directnav"){
                                rootView.cvOpportunities.enableCard()
                            }
                        }else {
                            liShareLayout.visibility = View.GONE
                            llPendingReview.visibility = View.VISIBLE
                            Log.e(TAG, "789 " )
                            cvOpportunities.disableCard()
                            cvMyTask.disableCard()
                            cvMyEarnings.disableCard()
                        }
                    }
                }
            }

            override fun onFailure(call: Call<ProfileResponce>, t: Throwable) {
                kProgressHUD.dismiss();
                t.printStackTrace()
            }
        })
    }


    fun callProfileAPI() {
        kProgressHUD.show()
        try{

        val call = apiService.getProfile(mySingleton.getData(Constant.TOKEN_BASE_64))
        call.enqueue(object : Callback<ProfileResponce> {
            override fun onResponse(call: Call<ProfileResponce>, response: Response<ProfileResponce>) {
                kProgressHUD.dismiss();
                if (response.isSuccessful && response.code() == 200) {
                    try {
                        if (response.body()?.status == 1 && response.body()?.data != null) {
                            profileData = response.body()?.data

                            rootView.name.text = profileData?.first_name + " " + profileData?.last_name

                            mySingleton.saveData(Constant.USER_GROUP_ID, response.body()!!.data.group_id)
                            mySingleton.saveData(Constant.USER_GROUP_DETAIL_ID, response.body()!!.data.group_detail_id)
                            mySingleton.saveData(Constant.USER_UNIQUE_REF_CODE, response.body()!!.data.unique_ref_code)
                            mySingleton.saveData(Constant.USER_FOS_TYPE, response.body()!!.data.fos_type)
                            profileVerified = profileData?.profile_verified ?: "0"

                            profileData?.img_path?.let {
                                Glide.with(requireActivity()).load(it).placeholder(R.drawable.placehold).into(rootView.ivUserProfile)
                            }

                            if (profileData?.img_path.equals("")&&count<2) {
                                count++
                                val builder = AlertDialog.Builder(activity)
                                builder.setTitle("Attention")
                                builder.setMessage(R.string.dialogMessage)
                                builder.setIcon(R.drawable.warning)
                                builder.setPositiveButton("Ok") { dialogInterface, _ ->

                                }


                                val alertDialog: AlertDialog = builder.create()
                                alertDialog.setCancelable(false)
                                alertDialog.show()
                            }

                            if (profileData?.profile_verified.equals("1", ignoreCase = true) && profileData?.is_parent.equals("0")) {
                                mySingleton.getData(Constant.USER_UNIQUE_REF_CODE)?.let {
                                    rootView.textRefCode.text = it
                                    rootView.liShareLayout.visibility = View.VISIBLE
                                    rootView.llPendingReview.visibility = View.GONE
                                    approvalPending.visibility = View.GONE
                                    approvalmessage.visibility = View.GONE
                                    Log.e(TAG, "10 11 12 " )
                                    rootView.cvOpportunities.enableCard()
                                    rootView.cvMyTask.enableCard()
                                    rootView.cvMyEarnings.enableCard()
                                }
                            }
                            if (profileData?.profile_verified.equals("1", ignoreCase = true) && profileData?.is_parent.equals("1")) {
                                mySingleton.getData(Constant.USER_UNIQUE_REF_CODE)?.let {
                                    rootView.textRefCode.text = it

                                    rootView.liShareLayout.visibility = View.VISIBLE
                                    rootView.llPendingReview.visibility = View.GONE

                                    approvalPending.visibility = View.GONE
                                    approvalmessage.visibility = View.GONE

                                    rootView.cvOpportunities.enableCard()
                                    rootView.cvMyTask.enableCard()
                                    rootView.cvMyEarnings.enableCard()
                                    Log.e(TAG, "13 14 15 " )
                                }
                            }
                            //TODO edited by bhavesh chand on june 3 here disability check added
                            /* if (profileData?.profile_verified.equals("1", ignoreCase = true)&& profileData?.is_parent.equals("1")) {

                                rootView.cvMyEarnings.disableCard()
                            }*/
                            if (profileData?.profile_verified.equals("0", ignoreCase = true) && response.body()!!.data.group_id.equals("2")) {
                                rootView.liShareLayout.visibility = View.GONE
                                rootView.llPendingReview.visibility = View.VISIBLE

                                approvalPending.visibility = View.VISIBLE
                                approvalmessage.visibility = View.VISIBLE

                                rootView.cvOpportunities.disableCard()
                                rootView.cvMyTask.disableCard()
                                rootView.cvMyEarnings.disableCard()
                                Log.e(TAG, "16 17 18 " )

                                startActivity(Intent(activity, KycActivity::class.java))
                            }

                            Log.e(TAG, "onResponse:>>>>>> "+profileData?.profile_verified )
                            if (profileData?.profile_verified.equals("2", ignoreCase = true)) {

                                rootView.liShareLayout.visibility = View.GONE
                                rootView.llPendingReview.visibility = View.VISIBLE

                                approvalPending.visibility = View.VISIBLE
                                approvalmessage.visibility = View.VISIBLE

                                rootView.cvOpportunities.disableCard()
                                rootView.cvMyTask.disableCard()
                                rootView.cvMyEarnings.disableCard()
                                rootView.cvOpportunities.enableCard()
                                Log.e(TAG, "19 20 21 "+mySingleton.getData("navigatetodashboard") )
                                Log.e(TAG, "19 20 21 " )
                                Log.e(TAG, "onResponse:>>>>>> "+cameFrom )
                                if (cameFrom=="directnav"){
                                    rootView.cvOpportunities.enableCard()
                                }

                            }

                            if (profileData!!.fos_type.equals("rBM", ignoreCase = true)) {
                                mySingleton.saveData("rebliss_type","rBM");
                                rootView.rebliss_sathi.text = "reBLISS Merchant"
                                btnDownload.visibility = View.VISIBLE
                            }

                            if (profileData!!.fos_type.equals("rBS", ignoreCase = true)) {
                                rootView.rebliss_sathi.text = "reBLISS Sathi"
                                btnDownload.visibility = View.GONE
                            }

                        }
                    } catch (e: java.lang.IllegalStateException) {
                        e.printStackTrace()
                    }
                }
            }

            override fun onFailure(call: Call<ProfileResponce>, t: Throwable) {
                kProgressHUD.dismiss();
                t.printStackTrace()
            }
        })
    }
        catch (e: IllegalStateException)
        {
            Log.e(TAG, "callProfileAPI: " + e.message)
        }

    }

    private fun callCarouselApi() {
        kProgressHUD.show()
        val call = apiService.getCarousel("2", "home")
        call.enqueue(object : Callback<CarouselResponse> {
            override fun onResponse(call: Call<CarouselResponse>, response: Response<CarouselResponse>) {
                kProgressHUD.dismiss();
                if (response.isSuccessful && response.code() == 200) {
                    carouselAdapter.updateAdapter(response.body()?.data?.allGroups)

                    var count: Int = 0
                    Timer().scheduleAtFixedRate(object : TimerTask() {
                        override fun run() {
                            activity?.runOnUiThread {
                               // if (CarouselAdapter.IS_VIDEO_PLAYING) {
                                    if (count >= carouselAdapter.itemCount) {
                                        count = 0
                                        rootView.rvCarousel.scrollToPosition(count)
                                    } else {
                                        rootView.rvCarousel.smoothScrollToPosition(count)
                                    }
                                    count++

                                }
                        }
                    }, 0, 5000) //put here time 1000 milliseconds=1 second
                }
            }

            override fun onFailure(call: Call<CarouselResponse>, t: Throwable) {
                kProgressHUD.dismiss();
                t.printStackTrace()
            }
        })
    }


    fun isStoragePermissionGranted(): Boolean {
        return if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                    checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                Log.v("permission", "Permission is granted")
                true
            } else {

                Log.v("permission", "Permission is revoked")
                ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA), 1)
                false
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v("permission", "Permission is granted")
            true
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.v("Permission: ", "was " + grantResults[0])
            //resume tasks needing this permission
        }
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
                val storageDir: File = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
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
            if (takePictureIntent.resolveActivity(requireActivity().getPackageManager()) != null) {
                if (file != null) {
                    val photoURI = FileProvider.getUriForFile(requireContext(),
                            BuildConfig.APPLICATION_ID + ".provider",
                            file)
                    mImageUri = Uri.fromFile(file)
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, type)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Handler().postDelayed(
                {
                    if (resultCode == Activity.RESULT_OK && requestCode == 101) {
                        ivUserProfile.setImageURI(mImageUri)
                        callImagePost(null, mCurrentPhotoPath)
                    }
                }, 500
        )
        if (resultCode == Activity.RESULT_OK && requestCode == pickImage) {
            imageUri = data?.data
            ivUserProfile.setImageURI(imageUri)
            val picturePath = getPath(requireActivity().applicationContext, imageUri)
            Log.d("Picture Path", picturePath!!)
            callImagePost(imageUri, picturePath!!)
        }


    }

    override fun onResume() {
        super.onResume()
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (shouldInterceptBackPress()) {
                    activity!!.finish()
                    // in here you can do logic when backPress is clicked
                } else {
                    isEnabled = false
                    activity?.onBackPressed()
                }
            }
        })
    }

    fun getPath(context: Context, uri: Uri?): String? {
        var result: String? = null
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val cursor: Cursor? = context.getContentResolver().query(uri!!, proj, null, null, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                val column_index: Int = cursor.getColumnIndexOrThrow(proj[0])
                result = cursor.getString(column_index)
            }
            cursor.close()
        }
        if (result == null) {
            result = "Not found"
        }
        return result
    }

    fun setLanguage(activity: Activity, language: String?) {
        val locale = Locale(language)
        val resources = activity.resources
        val configuration = resources.configuration
        configuration.setLocale(locale)
        resources.updateConfiguration(configuration, resources.displayMetrics)
    }
}