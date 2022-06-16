package com.rebliss.view.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.PagerSnapHelper
import com.rebliss.R
import com.rebliss.domain.constant.Constant
import com.rebliss.domain.model.CarouselResponse
import com.rebliss.utils.AdapterItemDecorator
import com.rebliss.view.activity.DashboardFOSDocUpload2
import com.rebliss.view.adapter.CarouselAdapter
import kotlinx.android.synthetic.main.fragment_fos_dashboard.view.*
import kotlinx.android.synthetic.main.fragment_kyc.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class KycFragment : BaseFrag() {
    var context1: Context? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        context1 = activity
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_kyc, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupImageSlider()
        setDataOnViews()
        if (network.isNetworkConnected(requireContext())) {
            callCarouselApi()
        }
    }

    private fun setDataOnViews() {
        name.text = mySingleton.getData(Constant.USER_FIRST_NAME) + " " + mySingleton.getData(Constant.USER_LAST_NAME)

        btnSave.setOnClickListener {
            startActivity(Intent(requireActivity(), DashboardFOSDocUpload2::class.java))
        }
    }


    private fun setupImageSlider() {
        rvCarousel.adapter = CarouselAdapter(context1!!,this)
        rvCarousel.addItemDecoration(AdapterItemDecorator())
        val snapHelper: PagerSnapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(rvCarousel)
    }


    private fun callCarouselApi() {
        kProgressHUD.show()
        val call = apiService.getCarousel("2", "welcome")
        call.enqueue(object : Callback<CarouselResponse> {
            override fun onResponse(call: Call<CarouselResponse>, response: Response<CarouselResponse>) {
                kProgressHUD.dismiss();
                if (response.isSuccessful && response.code() == 200) {
                    (rvCarousel?.adapter as? CarouselAdapter)?.updateAdapter(response.body()?.data?.allGroups)

                    var count: Int = 0
                    Timer().scheduleAtFixedRate(object : TimerTask() {
                        override fun run() {
                            activity?.runOnUiThread {
                                if (CarouselAdapter.IS_VIDEO_PLAYING) {
                                    if (count >= rvCarousel.adapter?.itemCount ?: 0) {
                                        count = 0
                                        rvCarousel?.rvCarousel?.scrollToPosition(count)
                                    } else {
                                          rvCarousel?.rvCarousel?.smoothScrollToPosition(count)
                                    }
                                    count++
                                }
                            }
                        }
                    }, 0, 800000) //put here time 1000 milliseconds=1 second
                }
            }

            override fun onFailure(call: Call<CarouselResponse>, t: Throwable) {
                kProgressHUD.dismiss();
                t.printStackTrace()
            }
        })
    }
}