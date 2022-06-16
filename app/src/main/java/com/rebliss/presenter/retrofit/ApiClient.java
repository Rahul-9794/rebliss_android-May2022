package com.rebliss.presenter.retrofit;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rebliss.domain.constant.Constant;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Initialise Retrofit Network lib
 */
public class ApiClient {
    private static Retrofit retrofit = null;
    private static Retrofit retrofit_IN = null;
    public static Retrofit getClient() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .build();
//        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(Constant.kBaseURL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(client)
                    .build();
//        }
        return retrofit;
    }

    public static Retrofit getRazorPayClient() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build();
//        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(Constant.kBaseURL_RazorPay)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(client)
                    .build();
//        }
        return retrofit;
    }

    public static Retrofit paymentDetail() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build();
//        if (retrofit == null) {
        retrofit = new Retrofit.Builder()
                .baseUrl(Constant.paymentdetail)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();
//        }
        return retrofit;
    }

}
