package com.rebliss.view.activity

import android.os.Bundle
import com.rebliss.R
import com.rebliss.domain.constant.Constant
import com.rebliss.domain.model.opportunitylist.OpportunityListResponse
import com.rebliss.presenter.helper.DisplaySnackBar
import com.rebliss.view.adapter.OpportunityAdapter
import kotlinx.android.synthetic.main.activity_opportunity_list.*
import kotlinx.android.synthetic.main.layout_toolbar.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OpportunityListActivity : BaseActivity() {
    private var displaySnackBar: DisplaySnackBar? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_opportunity_list)
        toolbar.tvHeader.text = "Opportunity List"

        toolbar.ivBack.setOnClickListener { onBackPressed() }
        displaySnackBar = DisplaySnackBar(this)
        if (network.isNetworkConnected(this)) {
            getOpportunityList()
        }
        else
        {
            displaySnackBar!!.DisplaySnackBar(Constant.NETWIRK_ERROR, Constant.TYPE_ERROR)
        }
    }

    private fun getOpportunityList() {
        kProgressHUD.show()
        val call = apiService.getopportunity(mySingleton.getData(Constant.USER_ID))
        call.enqueue(object : Callback<OpportunityListResponse> {
            override fun onResponse(call: Call<OpportunityListResponse>, response: Response<OpportunityListResponse>) {
                kProgressHUD.dismiss()
                if (response.isSuccessful) {
                    if (response.code() == 200) {
                        val myAcivityAdapter = OpportunityAdapter(this@OpportunityListActivity, (response.body())?.data?.opportunity)
                        rvOpportunityList.adapter = myAcivityAdapter
                    }
                }
            }

            override fun onFailure(call: Call<OpportunityListResponse>, t: Throwable) {
                kProgressHUD.dismiss()
            }
        })
    }
}