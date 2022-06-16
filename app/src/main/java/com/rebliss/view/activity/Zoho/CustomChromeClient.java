package com.rebliss.view.activity.Zoho;/*
 # @Author: Bhavesh Chand
 # @Date: 5/4/2022
 */

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Message;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.rebliss.view.activity.WebViewActivity;

class CustomChromeClient extends WebChromeClient {

    private WebView webViewPopUp;
    private AlertDialog builder;
    private Context globalContext;
    private WebView webView;
    private String userAgent;


    @Override
    public boolean onCreateWindow(WebView view, boolean isDialog,
                                  boolean isUserGesture, Message resultMsg) {
        webViewPopUp = new WebView(globalContext);
        webViewPopUp.setVerticalScrollBarEnabled(false);
        webViewPopUp.setHorizontalScrollBarEnabled(false);
        webViewPopUp.setWebChromeClient(new CustomChromeClient());
        webViewPopUp.getSettings().setJavaScriptEnabled(true);
        webViewPopUp.getSettings().setSaveFormData(true);
        webViewPopUp.getSettings().setEnableSmoothTransition(true);
        webViewPopUp.getSettings().setUserAgentString(userAgent + "yourAppName");

        // pop the  webview with alert dialog
        builder = new AlertDialog.Builder(globalContext).create();
        builder.setTitle("");
        builder.setView(webViewPopUp);

        builder.setButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                webViewPopUp.destroy();
                dialog.dismiss();
            }
        });

        builder.show();
        builder.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);

        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            cookieManager.setAcceptThirdPartyCookies(webViewPopUp, true);
            cookieManager.setAcceptThirdPartyCookies(webView, true);
        }

        WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
        transport.setWebView(webViewPopUp);
        resultMsg.sendToTarget();

        return true;
    }
}