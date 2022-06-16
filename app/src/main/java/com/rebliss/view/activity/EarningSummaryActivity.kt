package com.rebliss.view.activity

import android.os.Bundle
import android.util.Log
import com.rebliss.R
import com.rebliss.domain.constant.Constant
import com.rebliss.domain.model.EarningSummaryResponse
import com.rebliss.utils.getCurrentMonthDate
import com.rebliss.view.adapter.PayoutAdapter
import kotlinx.android.synthetic.main.activity_earning_summary.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EarningSummaryActivity : BaseActivity() {

    private val earningTaskId: String?
        get() {
            return intent.getStringExtra(Constant.EARNING_TASK_ID)
        }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_earning_summary)
        initAdapter()
        initClicks()
        callEarningSummary()
    }

    private fun initClicks() {
        ivBack.setOnClickListener { onBackPressed() }
    }

    private fun initAdapter() {
        rvEarningSummary.adapter = PayoutAdapter()
    }

    private fun callEarningSummary() {
        kProgressHUD.show()

        val call = apiService.getEariningSummary(mySingleton.getData(Constant.USER_ID), intent.getStringExtra("selected_date"), earningTaskId)
        call.enqueue(object : Callback<EarningSummaryResponse> {
            override fun onResponse(call: Call<EarningSummaryResponse>, response: Response<EarningSummaryResponse>) {
                kProgressHUD.dismiss()
                if (response.isSuccessful && response.code() == 200) {
                    Log.e("TAG", "onResponse: <<<<<status 1111" )
                    if (response.body()?.status == 1 && response.body()?.data != null) {
                        val categoryList = response.body()?.data?.allGroups
                        (rvEarningSummary.adapter as? PayoutAdapter)?.updateAdapter(categoryList)
                    }
                }
            }

            override fun onFailure(call: Call<EarningSummaryResponse>, t: Throwable) {
                kProgressHUD.dismiss()
                t.printStackTrace()
            }
        })
    }


}