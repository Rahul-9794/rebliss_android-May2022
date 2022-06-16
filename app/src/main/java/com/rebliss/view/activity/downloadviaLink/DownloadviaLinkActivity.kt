package com.rebliss.view.activity.downloadviaLink

import android.os.Bundle
import com.rebliss.R
import com.rebliss.domain.constant.Constant
import com.rebliss.presenter.helper.DisplaySnackBar
import com.rebliss.view.activity.BaseActivity
import kotlinx.android.synthetic.main.activity_downloadvia_link.*
import kotlinx.android.synthetic.main.activity_opportunity_list.*
import kotlinx.android.synthetic.main.activity_opportunity_list.toolbar
import kotlinx.android.synthetic.main.layout_toolbar.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DownloadviaLinkActivity : BaseActivity() {
    private var displaySnackBar: DisplaySnackBar? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_downloadvia_link)
        displaySnackBar = DisplaySnackBar(this)
        toolbar.tvHeader.text = "reBLISS Opportunities"
        toolbar.ivBack.setOnClickListener { onBackPressed() }
        if (network.isNetworkConnected(this)) {
            getDownloadList()
        } else {
            displaySnackBar!!.DisplaySnackBar(Constant.NETWIRK_ERROR, Constant.TYPE_ERROR)
        }
    }

    private fun getDownloadList() {
        kProgressHUD.show()
        val call = apiService.getDemandPartnerAppLink()
        call.enqueue(object : Callback<DemandPartnerAppLinkModel> {
            override fun onResponse(
                call: Call<DemandPartnerAppLinkModel>,
                response: Response<DemandPartnerAppLinkModel>,
            ) {
                kProgressHUD.dismiss()
                if (response.isSuccessful) {
                    if (response.code() == 200) {
                        val myAcivityAdapter = DownloadLinkAdapter(this@DownloadviaLinkActivity,
                            (response.body())?.data?.allGroups!!)
                        rvDownloadviaLink.adapter = myAcivityAdapter
                    }
                }
            }

            override fun onFailure(call: Call<DemandPartnerAppLinkModel>, t: Throwable) {
                kProgressHUD.dismiss()
            }
        })
    }
}