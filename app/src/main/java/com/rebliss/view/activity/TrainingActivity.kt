package com.rebliss.view.activity

import android.os.Bundle
import com.rebliss.R
import com.rebliss.domain.constant.Constant
import com.rebliss.domain.model.TrainingResponse
import com.rebliss.view.adapter.TrainingAdapter
import kotlinx.android.synthetic.main.activity_training.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TrainingActivity : BaseActivity() {

    private val subcategoryId: String?
    get() {
            return intent.getStringExtra(Constant.CATEGORY_ID)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_training)


        callGetTraining()
        initAdapter()
        initClickListeners()
    }

    private fun initClickListeners() {
        ivBack.setOnClickListener { onBackPressed() }
    }

    private fun initAdapter() {
        rvTraining.adapter = TrainingAdapter(this)
    }

    private fun callGetTraining() {
        kProgressHUD.show()

        val call = apiService.getTraining(subcategoryId)

        call.enqueue(object : Callback<TrainingResponse> {
            override fun onResponse(call: Call<TrainingResponse>, response: Response<TrainingResponse>) {
                kProgressHUD.dismiss();
                if (response.isSuccessful && response.code() == 200) {
                    if (response.body()?.status == 1 && response.body()?.data != null) {
                        (rvTraining.adapter as? TrainingAdapter)?.updateAdapter(response.body()?.data?.allGroups)
                    }
                }
            }

            override fun onFailure(call: Call<TrainingResponse>, t: Throwable) {
                kProgressHUD.dismiss();
                t.printStackTrace()
            }
        })
    }
}