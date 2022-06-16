package com.rebliss.view.fragment

import androidx.fragment.app.Fragment
import com.kaopiz.kprogresshud.KProgressHUD
import com.rebliss.R
import com.rebliss.data.perf.MySingleton
import com.rebliss.presenter.helper.Network
import com.rebliss.presenter.retrofit.ApiClient
import com.rebliss.presenter.retrofit.ApiInterface

open class BaseFrag : Fragment() {

    val apiService: ApiInterface by lazy { ApiClient.getClient().create(ApiInterface::class.java) }
    val mySingleton: MySingleton by lazy { MySingleton(requireContext()) }
    val network: Network by lazy { Network() }


    val kProgressHUD: KProgressHUD by lazy {
        KProgressHUD.create(requireContext())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .setWindowColor(getResources().getColor(R.color.progressbar_color))
    }

}