package com.rebliss.view.activity

import android.os.Bundle
import com.rebliss.R
import com.rebliss.domain.constant.Constant
import com.rebliss.domain.model.OpportunityDetailResponse
import com.rebliss.view.adapter.EditRbmAdapter
import com.rebliss.view.adapter.OpportunityDetailAdapter
import kotlinx.android.synthetic.main.activity_edit_rbm_detail.*
import kotlinx.android.synthetic.main.activity_opportunity_details.*
import kotlinx.android.synthetic.main.activity_opportunity_details.toolbar
import kotlinx.android.synthetic.main.layout_toolbar.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OpportunityDetailsActivity : BaseActivity() {

    private val opportunityTitle: String
        get() {
            return intent.getStringExtra(Constant.OPPORTUNITY_TITLE)!!
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_opportunity_details)

        toolbar.tvHeader.text = "Opportunity List"
        toolbar.ivBack.setOnClickListener { onBackPressed() }

        initAdapter()

        if (network.isNetworkConnected(this))
            getOpportunityDetail()
    }

    private fun initAdapter() {
        rvOpportunityDetailList.adapter = OpportunityDetailAdapter(this)
    }


    private fun getOpportunityDetail() {
        kProgressHUD.show()
        val call = apiService.getopportunityDetails(opportunityTitle)
        call.enqueue(object : Callback<OpportunityDetailResponse> {
            override fun onResponse(call: Call<OpportunityDetailResponse>, response: Response<OpportunityDetailResponse>) {
                kProgressHUD.dismiss()
                if (response.isSuccessful) {
                    if (response.code() == 200) {
                        (rvOpportunityDetailList.adapter as? OpportunityDetailAdapter)?.updateAdapter(response.body()?.data?.allGroups)
                    }
                }
            }
            override fun onFailure(call: Call<OpportunityDetailResponse>, t: Throwable) {
                kProgressHUD.dismiss()
            }
        })
    }
}