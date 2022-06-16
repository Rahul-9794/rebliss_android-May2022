package com.rebliss.view.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.rebliss.R
import com.rebliss.domain.constant.Constant
import com.rebliss.domain.model.CpPartnerSelectionModel
import com.rebliss.domain.model.payment.SuccessResponse
import com.rebliss.presenter.retrofit.ApiClient
import com.rebliss.presenter.retrofit.ApiInterface
import kotlinx.android.synthetic.main.activity_cp__partner_type.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ActivityCp_PartnerType : BaseActivity() {
    private var partnerType: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cp__partner_type)
        supportActionBar?.hide();

        textRetail.setOnClickListener {
            partnerType = ""
            if (network.isNetworkConnected(this)) {
                updatePartnerType(partnerType)
            }

        }

        textAlliances.setOnClickListener {
            partnerType = "rBT"
            if (network.isNetworkConnected(this)) {
                updatePartnerType(partnerType)
            }
        }


    }

    private fun updatePartnerType(partnerType: String) {

        val apiService = ApiClient.getClient().create(ApiInterface::class.java)
        val call =
            apiService.updatePartnerType(CpPartnerSelectionModel(mySingleton.getData(Constant.USER_ID),
                partnerType))
        call.enqueue(object : Callback<SuccessResponse> {
            override fun onResponse(
                call: Call<SuccessResponse>,
                response: Response<SuccessResponse>,
            ) {
                kProgressHUD.dismiss()
                if (response.isSuccessful) {
                    if (response.code() >= 200 && response.code() < 700) {
                        if (response.code() == 200) {
                            if (response.body()!!.status == 1) {

                                mySingleton.saveData(Constant.PARTNER_TYPE, partnerType)
                                startActivity(Intent(this@ActivityCp_PartnerType,
                                    DashboardCPDetails::class.java))

                            } else {
                                Toast.makeText(this@ActivityCp_PartnerType,
                                    "else part",
                                    Toast.LENGTH_SHORT).show()
                            }

                        }
                    } else {
                        Toast.makeText(this@ActivityCp_PartnerType,
                            "Something went wrong",
                            Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<SuccessResponse>, t: Throwable) {
                kProgressHUD.dismiss()
            }
        })

    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}