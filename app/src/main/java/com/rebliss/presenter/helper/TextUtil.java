package com.rebliss.presenter.helper;

import android.text.TextUtils;


public class TextUtil {
    public static boolean isStringNullOrBlank(String string) {
        boolean isBlank;
        if (TextUtils.isEmpty(string) || string == null) {
            isBlank = true;
        }else{
            isBlank = false;
        }
        return isBlank;
    }


}
