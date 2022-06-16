package com.rebliss.view.activity.Zoho

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Parcelable
import android.provider.MediaStore
import android.view.KeyEvent
import android.view.View
import android.webkit.ValueCallback
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.rebliss.R
import java.io.File
import java.io.IOException
import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import java.text.SimpleDateFormat
import java.util.*

class Zoho : AppCompatActivity() {
    private var webView: WebView? = null
    private var webSettings: WebSettings? = null
    private var mCapturedImageURI: Uri? = null
    private var mFilePathCallback: ValueCallback<Array<Uri>>? = null
    private val mCameraPhotoPath: String? = null
    private var mUploadMessage: ValueCallback<Uri>? = null
    private val INPUT_FILE_REQUEST_CODE = 1
    private val FILECHOOSER_RESULTCODE = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_zoho)
        initView();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initView() {
        webView = findViewById(R.id.wvZoho)

        webSettings = webView?.settings!!
        webSettings?.setJavaScriptEnabled(true)
        webSettings!!.javaScriptEnabled = true
        webSettings!!.loadWithOverviewMode = true
        webSettings!!.allowFileAccess = true
        webView!!.setWebViewClient(Client())

        webView!!.setLayerType(View.LAYER_TYPE_HARDWARE, null)
        webView!!.loadUrl("https://survey.zohopublic.com/zs/qSB3Dt")
    }

    @Throws(IOException::class)
    private fun createImageFile(): File? {
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",  /* suffix */
                storageDir /* directory */
        )
    }
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView!!.canGoBack()) {
            webView!!.goBack()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }
    class Client : WebViewClient() {
        var progressDialog: ProgressDialog? = null
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            if (url.contains("mailto:")) {
                view.context.startActivity(
                        Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                return true
            }
            if (url.contains("student-payment-history/print-receipt")) {
                var url1 = ""
                try {
                    url1 = URLEncoder.encode(url, "utf-8")
                } catch (e: UnsupportedEncodingException) {
                    e.printStackTrace()
                }
                val path = "http://docs.google.com/gview?embedded=true&url=$url1"
                view.context.startActivity(
                        Intent(Intent.ACTION_VIEW, Uri.parse(url)))
            }
            if (url.contains("student-result-master/report-card")) {
                var url1 = ""
                try {
                    url1 = URLEncoder.encode(url, "utf-8")
                } catch (e: UnsupportedEncodingException) {
                    e.printStackTrace()
                }
                val path = "http://docs.google.com/gview?embedded=true&url=$url1"
                view.context.startActivity(
                        Intent(Intent.ACTION_VIEW, Uri.parse(url)))
            }
            if (url.contains("user/logout")) {
            } else {
                view.loadUrl(url)
                return true
            }
            return true
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (requestCode != INPUT_FILE_REQUEST_CODE || mFilePathCallback == null) {
                super.onActivityResult(requestCode, resultCode, data)
                return
            }
            var results: Array<Uri>? = null

            if (resultCode == RESULT_OK) {
                if (data == null) {
                    if (mCameraPhotoPath != null) {
                        results = arrayOf(Uri.parse(mCameraPhotoPath))
                    }
                } else {
                    val dataString = data.dataString
                    if (dataString != null) {
                        results = arrayOf(Uri.parse(dataString))
                    }
                }
            }
            mFilePathCallback?.onReceiveValue(results)
            mFilePathCallback = null
        } else if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            if (requestCode != FILECHOOSER_RESULTCODE || mUploadMessage == null) {
                super.onActivityResult(requestCode, resultCode, data)
                return
            }
            if (requestCode == FILECHOOSER_RESULTCODE) {
                if (null == this.mUploadMessage) {
                    return
                }
                var result: Uri? = null
                try {
                    result = if (resultCode != RESULT_OK) {
                        null
                    } else {
                        if (data == null) mCapturedImageURI else data.data
                    }
                } catch (e: Exception) {
                    Toast.makeText(applicationContext, "activity :$e",
                            Toast.LENGTH_LONG).show()
                }
                mUploadMessage?.onReceiveValue(result)
                mUploadMessage = null
            }
        }
        return
    }

}


