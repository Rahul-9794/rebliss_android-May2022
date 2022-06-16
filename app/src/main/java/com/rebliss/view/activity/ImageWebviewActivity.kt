package com.rebliss.view.activity

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.rebliss.R
import java.io.UnsupportedEncodingException
import java.net.URLEncoder

class ImageWebviewActivity : AppCompatActivity() {
    private var webSettings: WebSettings? = null
    private var webView: WebView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_webview)

        initView()

    }

    private fun initView() {
        webView = findViewById(R.id.webviewImage)
        webSettings =webView?.getSettings()
        webSettings!!.setJavaScriptEnabled(true)
        webSettings!!.setLoadWithOverviewMode(true)
        webSettings!!.setAllowFileAccess(true)
        val bundle = intent.extras
        val showType = bundle?.getString("showType")
        val imgRedirectLink = bundle?.getString("imgRedirectLink")
        webView?.setWebViewClient(Client(showType,imgRedirectLink))
        webView?.setWebChromeClient(WebChromeClient())


        if (Build.VERSION.SDK_INT >= 19) {
            webView?.setLayerType(View.LAYER_TYPE_HARDWARE, null)
        } else if (Build.VERSION.SDK_INT >= 11 && Build.VERSION.SDK_INT < 19) {
            webView?.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        }
        webView?.loadUrl(imgRedirectLink!!)
    }
}
class Client(showType: String?, imgRedirectLink: String?,) : WebViewClient() {
    var progressDialog: ProgressDialog? = null
    val show = showType
    val redirectLink = imgRedirectLink
    override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {

        // If url contains mailto link then open Mail Intent
        if (url.contains(redirectLink!!)) {
            if(show.equals("1")) {
                view.context.startActivity(
                    Intent(Intent.ACTION_VIEW, Uri.parse(url))
                )
                return true
            }
            else{
                view.context.startActivity(
                    Intent(Intent.ACTION_VIEW, Uri.parse(url))
                )
            }
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