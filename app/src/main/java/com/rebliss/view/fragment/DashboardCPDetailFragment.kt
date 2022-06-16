package com.rebliss.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.kaopiz.kprogresshud.KProgressHUD
import com.rebliss.R
import com.rebliss.view.activity.DashboardCPDetails

class DashboardCPDetailFragment : Fragment() {
    private val kProgressHUD: KProgressHUD? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        kProgressHUD?.show()
        super.onCreate(savedInstanceState)

        val strtext = arguments!!.getString("Backtoleftpage")
        if (strtext != null || strtext != "") {
            kProgressHUD?.dismiss();
            startActivity(Intent(activity, DashboardCPDetails::class.java))
            activity?.finishAffinity()
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        kProgressHUD?.show()

        return inflater.inflate(R.layout.fragment_dashboard_c_p_detail, container, false)
    }



}