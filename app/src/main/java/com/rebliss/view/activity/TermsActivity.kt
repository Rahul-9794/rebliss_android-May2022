package com.rebliss.view.activity

import android.os.Bundle
import com.rebliss.R
import com.rebliss.domain.model.CarouselResponse
import com.rebliss.view.adapter.CarouselAdapter
import kotlinx.android.synthetic.main.activity_kyc.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class TermsActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_terms)
        getTermsDetails()
    }

    private fun getTermsDetails() {
        kProgressHUD.show()
        val call = apiService.getCarousel("2", "home")
        call.enqueue(object : Callback<CarouselResponse> {
            override fun onResponse(call: Call<CarouselResponse>, response: Response<CarouselResponse>) {
                kProgressHUD.dismiss();
                if (response.isSuccessful && response.code() == 200) {
                    (rvCarousel.adapter as? CarouselAdapter)?.updateAdapter(response.body()?.data?.allGroups)

                    var count: Int = 0
                    Timer().scheduleAtFixedRate(object : TimerTask() {
                        override fun run() {
                            runOnUiThread {
                                if (count >= rvCarousel.adapter?.itemCount ?: 0) {
                                    count = 0
                                }
                                rvCarousel.smoothScrollToPosition(count)
                                count++
                            }
                        }
                    }, 0, 5000) //put here time 1000 milliseconds=1 second
                }
            }

            override fun onFailure(call: Call<CarouselResponse>, t: Throwable) {
                kProgressHUD.dismiss();
                t.printStackTrace()
            }
        })
    }
}