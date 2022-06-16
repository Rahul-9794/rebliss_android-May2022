package com.rebliss.view.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.PagerSnapHelper
import com.kaopiz.kprogresshud.KProgressHUD
import com.rebliss.R
import com.rebliss.domain.constant.Constant
import com.rebliss.domain.model.CarouselResponse
import com.rebliss.domain.model.PartnerSelectionModel
import com.rebliss.domain.model.payment.SuccessResponse
import com.rebliss.presenter.retrofit.ApiClient
import com.rebliss.presenter.retrofit.ApiInterface
import com.rebliss.utils.AdapterItemDecorator
import com.rebliss.view.adapter.CarouselAdapter
import kotlinx.android.synthetic.main.activity_kyc.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class KycActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kyc)

        setupImageSlider()
        setDataOnViews()
        if (network.isNetworkConnected(this)) {
            callCarouselApi()
        }

        updateGroupId("2", "Join as reBLISS Sathi")
    }

    private fun updateGroupId(s: String, s1: String) {


        val apiService = ApiClient.getClient().create(ApiInterface::class.java)
        val call =
            apiService.updateGroupId(PartnerSelectionModel(mySingleton.getData(Constant.USER_ID),
                "2"))
        call.enqueue(object : Callback<SuccessResponse> {
            override fun onResponse(
                call: Call<SuccessResponse>,
                response: Response<SuccessResponse>,
            ) {
                if (response.isSuccessful) {
                    if (response.code() >= 200 && response.code() < 700) {
                        if (response.code() == 200) {

                        }
                    }
                }
            }

            override fun onFailure(call: Call<SuccessResponse>, t: Throwable) {
            }
        })
    }

    @SuppressLint("SetTextI18n")
    private fun setDataOnViews() {
        name.text =
            mySingleton.getData(Constant.USER_FIRST_NAME) + " " + mySingleton.getData(Constant.USER_LAST_NAME)

        btnSave.setOnClickListener {
            startActivity(Intent(this, DashboardFOSDocUpload2::class.java))
        }
    }

    private fun setupImageSlider() {
        rvCarousel.adapter = CarouselAdapter(this@KycActivity, null)
        rvCarousel.addItemDecoration(AdapterItemDecorator())
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(rvCarousel)
    }

    private fun callCarouselApi() {
        kProgressHUD.show()
        val call = apiService.getCarousel("2", "welcome")
        call.enqueue(object : Callback<CarouselResponse> {
            override fun onResponse(
                call: Call<CarouselResponse>,
                response: Response<CarouselResponse>,
            ) {
                kProgressHUD.dismiss()
                if (response.isSuccessful && response.code() == 200) {
                    (rvCarousel.adapter as? CarouselAdapter)?.updateAdapter(response.body()?.data?.allGroups)

                    var count = 0
                    Timer().scheduleAtFixedRate(object : TimerTask() {
                        override fun run() {
                            runOnUiThread {
                                if (CarouselAdapter.IS_VIDEO_PLAYING) {
                                    if (count >= rvCarousel.adapter?.itemCount ?: 0) {
                                        count = 0
                                        rvCarousel.scrollToPosition(count)
                                    } else {
                                        rvCarousel.smoothScrollToPosition(count)
                                    }
                                    count++
                                }
                            }
                        }
                    }, 0, 8000)
                }
            }

            override fun onFailure(call: Call<CarouselResponse>, t: Throwable) {
                kProgressHUD.dismiss()
                t.printStackTrace()
            }
        })
    }


}