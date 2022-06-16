package com.rebliss.presenter.helper;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import com.rebliss.data.perf.MySingleton;
import com.rebliss.domain.constant.Constant;


public class Base64Encoded {

    private Context context;
    private String TAG = "Base64Encoded";
    private String encodedToken = "";
    private MySingleton mySingleton;

    public Base64Encoded(Context context) {
        this.context = context;
        this.mySingleton = new MySingleton(context);
    }


    public void EncodeToken() {
        String base64 = "";
        try {
            Log.i(TAG, mySingleton.getData(Constant.TOKEN));
            String username = mySingleton.getData(Constant.TOKEN);
            String credentials = username;
            base64 = "Basic " + removeLastChar(credentials.getBytes("UTF-8"));
          //  Log.i(TAG, removeLastChar(base64));
            mySingleton.saveData(Constant.TOKEN_BASE_64, removeLastChar(base64)+"6");
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public void EncodeFOSToken() {
        String base64 = "";
        try {
            Log.i(TAG, mySingleton.getData(Constant.FOSTOKEN));
            String username = mySingleton.getData(Constant.FOSTOKEN);
            String credentials = username;
            base64 = "Basic " + removeLastChar(credentials.getBytes("UTF-8"));
            mySingleton.saveData(Constant.TOKEN_BASE_64_FOS, removeLastChar(base64)+"6");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static String removeLastChar(byte[] byteArray) {
        String CHARSET_NAME = "UTF-8";
        String base64 = "";
        try {
            base64 = new String(
                    Base64.encode(byteArray, Base64.DEFAULT));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return base64.trim();
    }
    private static String removeLastChar(String str) {
        return str.substring(0, str.length() - 1);
    }
}
