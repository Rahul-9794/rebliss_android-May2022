package com.rebliss.view.activity.new_changes

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.PagerSnapHelper
import com.rebliss.R
import com.rebliss.domain.model.CarouselResponse
import com.rebliss.utils.AdapterItemDecorator
import com.rebliss.view.activity.ActivityLogin
import com.rebliss.view.activity.ActivitySignup
import com.rebliss.view.activity.BaseActivity
import com.rebliss.view.adapter.LoginCarouselAdapter
import kotlinx.android.synthetic.main.activity_login_signup_chooser.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class LoginSignupChooserActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_signup_chooser)

        setupImageSlider()
        callCarouselApi()
        initClicks()

    }

    private fun initClicks() {
        btnSignup.setOnClickListener {
            startActivity(Intent(this, ActivitySignup::class.java))
        }

        btnLogin.setOnClickListener {
            startActivity(Intent(this, ActivityLogin::class.java))
        }

    }


    private fun setupImageSlider() {
        rvCarousel.adapter = LoginCarouselAdapter(null, this)
        rvCarousel.addItemDecoration(AdapterItemDecorator())
        val snapHelper: PagerSnapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(rvCarousel)
    }


    private fun callCarouselApi() {
        kProgressHUD.show()
        val call = apiService.getCarousel("2", "signup")
        call.enqueue(object : Callback<CarouselResponse> {
            override fun onResponse(
                call: Call<CarouselResponse>,
                response: Response<CarouselResponse>,
            ) {
                kProgressHUD.dismiss();
                if (response.isSuccessful && response.code() == 200) {
                    (rvCarousel.adapter as? LoginCarouselAdapter)?.updateAdapter(response.body()?.data?.allGroups)

                    var count: Int = 0
                    Timer().scheduleAtFixedRate(object : TimerTask() {
                        override fun run() {
                            runOnUiThread {
                                if (!LoginCarouselAdapter.IS_VIDEO_PLAYING) {
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
                    }, 0, 80000)
                }
            }

            override fun onFailure(call: Call<CarouselResponse>, t: Throwable) {
                kProgressHUD.dismiss();
                t.printStackTrace()
            }
        })
    }



}