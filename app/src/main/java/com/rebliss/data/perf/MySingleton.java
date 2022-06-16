package com.rebliss.data.perf;

import android.content.Context;
import android.content.SharedPreferences;
import com.rebliss.domain.constant.Constant;


public class MySingleton {
    private static MySingleton mySingleton;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor prefsEditor;
    public static MySingleton getInstance(Context context) {
        if (mySingleton == null) {
            mySingleton = new MySingleton(context);
        }
        return mySingleton;
    }

    public MySingleton(Context context) {
        sharedPreferences = context.getSharedPreferences(Constant.MYPREF, Context.MODE_PRIVATE);
    }


    public void saveData(String key, String value)
    {
        prefsEditor = sharedPreferences.edit();
        prefsEditor.putString(key, value);
        prefsEditor.commit();
    }

    public String getData(String key)
    {
        if (sharedPreferences!= null)
        {
            return sharedPreferences.getString(key, "");
        }
        return "";
    }

    public void saveInt(String key, int value)
    {
        prefsEditor = sharedPreferences.edit();
        prefsEditor.putInt(key, value);
        prefsEditor.commit();
    }

    public int getInt(String key)
    {
        if (sharedPreferences!= null)
        {
            return sharedPreferences.getInt(key, 0);
        }
        return 0;
    }

    public void saveBoolean(String key, boolean value)
    {
        prefsEditor = sharedPreferences.edit();
        prefsEditor.putBoolean(key, value);
        prefsEditor.commit();
    }

    public boolean getBoolean(String key)
    {
        if (sharedPreferences!= null)
        {
            return sharedPreferences.getBoolean(key, false);
        }
        return false;
    }


    public void clearData()
    {
        sharedPreferences.edit().clear().apply();
    }
}
