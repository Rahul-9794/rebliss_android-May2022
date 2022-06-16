package com.rebliss.utils;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;

import java.util.Locale;

public class LocaleHelper
{
    public static ContextWrapper changeLanguage(Context context, String languagecode) {
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        Locale systemLocale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            systemLocale = configuration.getLocales().get(0);
        } else {
            systemLocale = configuration.locale;
        }
        if (!languagecode.equals("") && !systemLocale.getLanguage().equals(languagecode)) {
            Locale locale = new Locale(languagecode);
            Locale.setDefault(locale);
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            {
                configuration.setLocale(locale);
            }
            else
            {
                configuration.locale = locale;
            }
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){
                context = context.createConfigurationContext(configuration);
            }
        }
        return new ContextWrapper(context);
    }
}
