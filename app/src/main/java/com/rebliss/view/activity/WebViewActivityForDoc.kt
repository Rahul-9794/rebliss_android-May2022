package com.rebliss.view.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.webkit.*
import com.rebliss.R
import java.io.File
import java.io.IOException
import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import java.text.SimpleDateFormat
import java.util.*

class WebViewActivityForDoc : AppCompatActivity() {
    private var context: Context? = null
    private val TAG: String = WebViewActivity::class.java.getSimpleName()
    private var webView: WebView? = null
    private var webSettings: WebSettings? = null
    private val INPUT_FILE_REQUEST_CODE = 1
    private val FILECHOOSER_RESULTCODE = 1
    private var mFilePathCallback: ValueCallback<Array<Uri>>? = null
    private var mCameraPhotoPath: String? = null
    private var mUploadMessage: ValueCallback<Uri>? = null
    private var mCapturedImageURI: Uri? = null

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        context = this
        webView = findViewById(R.id.webView1)
        webSettings =webView?.getSettings()
        webSettings!!.setJavaScriptEnabled(true)
        webSettings!!.setLoadWithOverviewMode(true)
        webSettings!!.setAllowFileAccess(true)
        webView?.setWebViewClient(Clients())
        webView?.setWebChromeClient(WebChromeClient())
        if (Build.VERSION.SDK_INT >= 19) {
            webView?.setLayerType(View.LAYER_TYPE_HARDWARE, null)
        } else if (Build.VERSION.SDK_INT >= 11 && Build.VERSION.SDK_INT < 19) {
            webView?.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        }
        webView?.loadUrl("")

        webView?.webChromeClient = object : WebChromeClient() {
            override fun onShowFileChooser(
                webView: WebView, filePathCallback: ValueCallback<Array<Uri>>,
                fileChooserParams: WebChromeClient.FileChooserParams
            ): Boolean {
                if (mFilePathCallback != null) {
                    mFilePathCallback!!.onReceiveValue(null)
                }
                mFilePathCallback = filePathCallback
                var takePictureIntent: Intent? = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                if (takePictureIntent!!.resolveActivity(this@WebViewActivityForDoc.packageManager) != null) {
                    // Create the File where the photo should go
                    var photoFile: File? = null
                    try {
                        photoFile = createImageFile()
                        takePictureIntent.putExtra("PhotoPath", mCameraPhotoPath)
                    } catch (ex: IOException) {
                        // Error occurred while creating the File
                        Log.e(TAG, "Unable to create Image File", ex)
                    }
                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        mCameraPhotoPath = "file:" + photoFile.absolutePath
                        takePictureIntent.putExtra(
                            MediaStore.EXTRA_OUTPUT,
                            Uri.fromFile(photoFile)
                        )
                    } else {
                        takePictureIntent = null
                    }
                }

                val contentSelectionIntent = Intent(Intent.ACTION_GET_CONTENT)
                contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE)
                contentSelectionIntent.type = "image/*"
                val intentArray: Array<Intent?>
                if (takePictureIntent != null) {
                    intentArray = arrayOf(takePictureIntent)
                } else {
                    intentArray = arrayOfNulls(0)
                }
                val chooserIntent = Intent(Intent.ACTION_CHOOSER)
                chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent)
                chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser")
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray)
                startActivityForResult(chooserIntent, INPUT_FILE_REQUEST_CODE)
                return true
            }
        }
    }
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView?.canGoBack()!!) {
            webView?.goBack()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }
    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode != INPUT_FILE_REQUEST_CODE || mFilePathCallback == null) {
            super.onActivityResult(requestCode, resultCode, data)
            return
        }
        var results: Array<Uri>? = null
        // Check that the response is a good one
        if (resultCode == Activity.RESULT_OK) {
            if (data == null) {
                // If there is not data, then we may have taken a photo
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
        mFilePathCallback!!.onReceiveValue(results)
        mFilePathCallback = null
        return
    }
    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_PICTURES
        )
        return File.createTempFile(
            imageFileName,
            ".jpg",
            storageDir
        )
    }

}

class Clients : WebViewClient() {
    var progressDialog: ProgressDialog? = null
    override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
        // If url contains mailto link then open Mail Intent
        if (url.contains("mailto:")) {
            view.context.startActivity(
                Intent(Intent.ACTION_VIEW, Uri.parse(url))
            )
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
                Intent(Intent.ACTION_VIEW, Uri.parse(url))
            )
            return true
        }
        if (url.contains("https://www.youtube.com/cognitoabacus")) {
            val browserIntent =
                Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/cognitoabacus"))
            view.context.startActivity(browserIntent)
            return true
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
                Intent(Intent.ACTION_VIEW, Uri.parse(url))
            )
        }
        if (url.contains("comp-student-allocation/print-hall-ticket")) {
            var url1 = ""
            try {
                url1 = URLEncoder.encode(url, "utf-8")
            } catch (e: UnsupportedEncodingException) {
                e.printStackTrace()
            }
            val path = "http://docs.google.com/gview?embedded=true&url=$url1"
            view.context.startActivity(
                Intent(Intent.ACTION_VIEW, Uri.parse(url))
            )
        }
        if (url.contains("https://bit.ly")) {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            view.context.startActivity(browserIntent)
            return true
        }
        else {
            view.loadUrl(url)
            return true
        }
        return true
    }
}