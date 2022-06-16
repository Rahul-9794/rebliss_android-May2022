package com.rebliss.view.activity

import androidx.appcompat.app.AppCompatActivity
import com.kaopiz.kprogresshud.KProgressHUD
import com.rebliss.R
import com.rebliss.data.perf.MySingleton
import com.rebliss.presenter.helper.Network
import com.rebliss.presenter.retrofit.ApiClient
import com.rebliss.presenter.retrofit.ApiInterface

open class BaseActivity : AppCompatActivity() {

    val apiService: ApiInterface by lazy { ApiClient.getClient().create(ApiInterface::class.java) }
    val mySingleton: MySingleton by lazy { MySingleton(this) }
    val network: Network by lazy { Network() }

    val kProgressHUD: KProgressHUD by lazy {
        KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .setWindowColor(resources.getColor(R.color.progressbar_color))
    }
}