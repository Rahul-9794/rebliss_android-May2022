package com.rebliss.view.activity;

import android.graphics.Bitmap;
import android.os.Build;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.rebliss.R;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class WebViewBankItActivity extends AppCompatActivity {


    private WebView wv;
    private String redirectionUrl="";
    private    String agentId="",checksum="",emailId="",mdId="";
    private String postData="";
    private ImageView ic_back;


    class WebViewClass extends WebViewClient {
        KProgressHUD kProgressHUD;
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            if (kProgressHUD != null && kProgressHUD.isShowing()) {
                kProgressHUD.dismiss();
            }
            view.loadUrl(request.getUrl().toString());
            Log.d("webrdrctUrl", request.getUrl().toString());
            return true;
        }


        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            if (kProgressHUD != null && kProgressHUD.isShowing()) {
                kProgressHUD.dismiss();
            }
            kProgressHUD = KProgressHUD.create(WebViewBankItActivity.this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setCancellable(false).setWindowColor(getResources().getColor(R.color.colorPrimaryDark))
                    .setAnimationSpeed(2)
                    .setDimAmount(0.5f)
                    .show();
            // kProgressHUD.setAutoDismiss(true);
            Log.d("WebView", "your current url when webpage loading.." + url);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            Log.d("llgn", url + "");
            if (kProgressHUD != null && kProgressHUD.isShowing()) {
                kProgressHUD.dismiss();
            }
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view_bank_it);

        initView();

        if (getIntent() != null)
        {
            redirectionUrl = getIntent().getStringExtra("redirection_url_key");
            agentId = getIntent().getStringExtra("agentId_key");
            checksum = getIntent().getStringExtra("checksum_key");
            emailId = getIntent().getStringExtra("emailId_key");
            mdId = getIntent().getStringExtra("mdId_key");
            try {
                postData="agentId="+URLEncoder.encode(agentId, "UTF-8")+"&checksum="+URLEncoder.encode(checksum, "UTF-8")+"&emailId="+URLEncoder.encode(emailId, "UTF-8")+"&mdId="+URLEncoder.encode(mdId, "UTF-8");
            }
            catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        if (!redirectionUrl.isEmpty() && !redirectionUrl.equals("")) {
          //  kProgressHUD.show();
            wv.setWebViewClient(new WebViewClass());
            wv.postUrl(redirectionUrl,postData.getBytes());
        }
    }

    private void initView() {

        ic_back =  findViewById(R.id.ic_back);
        ic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        wv =  findViewById(R.id.webViewBankIt);
        wv.setWebViewClient(new WebViewClient());
        wv.getSettings().setLoadsImagesAutomatically(true);
        wv.getSettings().setJavaScriptEnabled(true);
        wv.setScrollBarStyle(View.VISIBLE);
        wv.getSettings().setBuiltInZoomControls(true);
        wv.getSettings().setSupportZoom(true);
        wv.getSettings().setLoadWithOverviewMode(true);
        wv.getSettings().setUseWideViewPort(true);
        wv.getSettings().setAllowContentAccess(true);

    }

    @Override
    public void onBackPressed()
    {
        Toast.makeText(this, "Button is Disable. Press Home to exit!",
                Toast.LENGTH_SHORT).show();
    }

}
