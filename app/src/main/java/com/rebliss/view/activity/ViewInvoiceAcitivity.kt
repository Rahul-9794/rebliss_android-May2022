package com.rebliss.view.activity

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.dewinjm.monthyearpicker.MonthYearPickerDialogFragment
import com.rebliss.R
import com.rebliss.domain.constant.Constant
import com.rebliss.domain.model.ViewInvoiceResponse
import com.rebliss.utils.getCurrentMonthDate
import com.rebliss.view.adapter.MyEarningAdapter
import com.rebliss.view.adapter.ViewInvoiceAdapter
import kotlinx.android.synthetic.main.activity_my_earning.*
import kotlinx.android.synthetic.main.activity_view_invoice_acitivity.*
import kotlinx.android.synthetic.main.activity_view_invoice_acitivity.ivBack
import kotlinx.android.synthetic.main.layout_payout.*
import kotlinx.android.synthetic.main.layout_payout.tvNoPayoutData
import kotlinx.android.synthetic.main.layout_summary.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class ViewInvoiceAcitivity : BaseActivity() {
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var adapter: ViewInvoiceAdapter
    var catId :String =""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_invoice_acitivity)
        getSupportActionBar()?.hide();
        val bundle = intent.extras
        catId = bundle?.getString("categoryid").toString()

        val cal = Calendar.getInstance()
        val month = cal[Calendar.MONTH] + 1
        val year = cal[Calendar.YEAR]
        tvMonthInvoice.setText("Month: "+month+" ,"+year)


        callViewInvoiceApi()

        initClickListeners()
    }

    private fun initClickListeners() {

        ivBack.setOnClickListener { onBackPressed() }


        tvMonthInvoice.setOnClickListener {
                showMonthPicker(it as TextView)
            }
        }


    fun showMonthPicker(tv: TextView) {
        val yearSelected: Int
        val monthSelected: Int

        val calendar = Calendar.getInstance()
        yearSelected = calendar[Calendar.YEAR]
        monthSelected = calendar[Calendar.MONTH]

        val dialogFragment = MonthYearPickerDialogFragment
                .getInstance(monthSelected, yearSelected)

        dialogFragment.show(supportFragmentManager, null)

        dialogFragment.setOnDateSetListener { year, monthOfYear ->
            val month = monthOfYear + 1
            tv.text = "Month: " + month + ", " + year

            if (tv.id == R.id.tvMonthInvoice)
                callViewInvoiceApi("" + year + "-" + month + "-" + "01")

        }
    }

    private fun callViewInvoiceApi(date: String = "") {
        kProgressHUD.show()

        var monthDate = date
        if (monthDate.isBlank()) {
            monthDate = getCurrentMonthDate()
        }

        val call = apiService.getCheckPayoutbyCategory(mySingleton.getData(Constant.USER_ID), monthDate,catId)

        call.enqueue(object : Callback<ViewInvoiceResponse> {
            override fun onResponse(call: Call<ViewInvoiceResponse>, response: Response<ViewInvoiceResponse>) {
                kProgressHUD.dismiss();
                if (response.isSuccessful && response.code() == 200) {
                    if (response.body()?.status == 1 && response.body()?.data != null) {
                        rvViewInvoice.visibility = View.VISIBLE
                        tvNoPayoutData.visibility = View.GONE
                        linearLayoutManager = LinearLayoutManager(applicationContext)
                        rvViewInvoice.layoutManager = linearLayoutManager

                        val categoryList = response.body()?.data?.all_groups
                        adapter = ViewInvoiceAdapter(categoryList)
                        rvViewInvoice.adapter = adapter
                        (rvViewInvoice.adapter as? ViewInvoiceAdapter)?.updateAdapter(categoryList)
                        adapter.notifyDataSetChanged()
                    }
                    if(response.body()?.status == 0 && response.body()?.data != null)
                    {
                        linearLayoutManager = LinearLayoutManager(applicationContext)
                        rvViewInvoice.layoutManager = linearLayoutManager
                        val categoryList = response.body()?.data?.all_groups
                        adapter = ViewInvoiceAdapter(categoryList)
                        rvViewInvoice.adapter = adapter
                        (rvViewInvoice.adapter as? ViewInvoiceAdapter)?.updateAdapter(categoryList)
                        adapter.notifyDataSetChanged()
                        tvNoPayoutData.visibility = View.VISIBLE
                    }

                }
            }

            override fun onFailure(call: Call<ViewInvoiceResponse>, t: Throwable) {
                kProgressHUD.dismiss();
                t.printStackTrace()
            }
        })
    }
}