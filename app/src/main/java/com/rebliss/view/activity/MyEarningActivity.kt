package com.rebliss.view.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import cn.pedant.SweetAlert.SweetAlertDialog
import com.github.dewinjm.monthyearpicker.MonthYearPickerDialogFragment
import com.rebliss.R
import com.rebliss.domain.constant.Constant
import com.rebliss.domain.model.EarningSummaryResponse
import com.rebliss.domain.model.MyEarningResponse
import com.rebliss.domain.model.payment.SuccessResponse
import com.rebliss.presenter.helper.DisplaySnackBar
import com.rebliss.utils.deselectButton
import com.rebliss.utils.getCurrentMonthDate
import com.rebliss.utils.selectButton
import com.rebliss.view.adapter.MyEarningAdapter
import com.rebliss.view.adapter.PayoutAdapter
import kotlinx.android.synthetic.main.activity_my_earning.*
import kotlinx.android.synthetic.main.layout_payout.*
import kotlinx.android.synthetic.main.layout_summary.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.Month
import java.time.Month.of
import java.util.*
import kotlinx.android.synthetic.main.layout_summary.tvTotalEarning as tvTotalEarning1


class MyEarningActivity : BaseActivity() {
    private var displaySnackBar: DisplaySnackBar? = null
    private var context: Context? = null

    private lateinit var MonthinString:Month


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_earning)

        initView()

        if (isNetworkConnected()) {

            callMyEarning()


        } else {
            displaySnackBar!!.DisplaySnackBar(Constant.NETWIRK_ERROR, Constant.TYPE_ERROR)
        }
        initClickListeners()
    }

    @SuppressLint("SetTextI18n")
    private fun initView() {
        displaySnackBar = DisplaySnackBar(this)
        context = this
        btnSummary.selectButton()
        btnPayout.deselectButton()
        val cal = Calendar.getInstance()
        val month = cal[Calendar.MONTH] + 1
        val year = cal[Calendar.YEAR]

         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
             MonthinString =  of(month)
             val months: String = MonthinString.toString().toLowerCase(Locale.getDefault())

             val monthsss = capitalizeString(months)
             tvMonth.setText("Month: $monthsss, $year")
        } else {
            tvMonth.setText("Month: $month , $year")
        }


    }

    @SuppressLint("SetTextI18n")
    private fun initClickListeners() {

        ivBack.setOnClickListener { onBackPressed() }

        btnSummary.setOnClickListener {
            txtEarningLabel.setText("Total Earning")
            btnSummary.selectButton()
            btnPayout.deselectButton()
            layoutSummary.visibility = View.VISIBLE
            layoutPayout.visibility = View.GONE

            val cal = Calendar.getInstance()
            val month = cal[Calendar.MONTH] + 1
            val year = cal[Calendar.YEAR]
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                MonthinString =  of(month)
                val months: String = MonthinString.toString().toLowerCase(Locale.getDefault())

                val monthsss = capitalizeString(months)
                tvMonth.setText("Month: $monthsss, $year")
            } else {
                tvMonth.setText("Month: $month , $year")
            }
            if (isNetworkConnected()) {
                callMyEarning()
            } else {
                displaySnackBar!!.DisplaySnackBar(Constant.NETWIRK_ERROR, Constant.TYPE_ERROR)
            }
        }

        btnPayout.setOnClickListener {
            txtEarningLabel.setText("Total Payout")
            btnSummary.deselectButton()
            btnPayout.selectButton()
            layoutSummary.visibility = View.GONE
            layoutPayout.visibility = View.VISIBLE

            val cal = Calendar.getInstance()
            val month = cal[Calendar.MONTH] + 1
            val year = cal[Calendar.YEAR]
            cal[Calendar.DAY_OF_WEEK]
            cal[Calendar.DAY_OF_MONTH]
            cal[Calendar.DAY_OF_YEAR]
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                MonthinString =  of(month)
                val months: String = MonthinString.toString().toLowerCase(Locale.getDefault())

                val monthsss = capitalizeString(months)
                tvMonthPayout.setText("Month: $monthsss, $year")
            } else {
                tvMonthPayout.setText("Month: $month , $year")
            }
            if (isNetworkConnected()) {
                callPayoutApi()
            } else {
                displaySnackBar!!.DisplaySnackBar(Constant.NETWIRK_ERROR, Constant.TYPE_ERROR)
            }
        }

        tvMonth.setOnClickListener {
            showMonthPicker(it as TextView)
        }

        tvMonthPayout.setOnClickListener {
            showMonthPicker(it as TextView)
        }

    }

    private fun initAdapter(date: String = "") {
        rvMyEarnings.adapter = MyEarningAdapter {
            when (it.clickType) {
                MyEarningAdapter.VIEW_SUMMARY -> {
                    startActivity(Intent(this, EarningSummaryActivity::class.java)
                        .putExtra(Constant.EARNING_TASK_ID, it.earningTaskId)
                        .putExtra("selected_date", date))
                }

                MyEarningAdapter.GENERATE_INVOICE -> {
                    if (isNetworkConnected()) {
                        callGenerateInvoiceApi(it.earningTaskId)
                    } else {
                        displaySnackBar!!.DisplaySnackBar(Constant.NETWIRK_ERROR,
                            Constant.TYPE_ERROR)
                    }
                }
            }
        }


    }

    private fun callGenerateInvoiceApi(earningTaskId: String) {
        kProgressHUD.show()

        val call = apiService.getWithdrawTaskAmount(mySingleton.getData(Constant.USER_ID),
            getCurrentMonthDate(),
            earningTaskId)
        call.enqueue(object : Callback<SuccessResponse> {
            override fun onResponse(
                call: Call<SuccessResponse>,
                response: Response<SuccessResponse>,
            ) {
                kProgressHUD.dismiss()
                if (response.isSuccessful && response.code() == 200) {
                    if (response.body()?.status == 1 && response.body() != null) {
                        val success = response.body()?.status == 1
                        if (success)
                            SweetAlertDialog(this@MyEarningActivity,
                                SweetAlertDialog.SUCCESS_TYPE).setTitleText("Invoice")
                                .setContentText("Invoice generated successfully!")
                                .setConfirmClickListener { sweetAlertDialog ->
                                    sweetAlertDialog.dismissWithAnimation()
                                    callMyEarning()
                                }
                                .show()
                    }

                }
            }

            override fun onFailure(call: Call<SuccessResponse>, t: Throwable) {
                kProgressHUD.dismiss()
                t.printStackTrace()
            }
        })
    }

    @SuppressLint("SetTextI18n")
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
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                MonthinString =  of(month)
                val months: String = MonthinString.toString().toLowerCase(Locale.getDefault())

                val monthsss = capitalizeString(months)
                tv.setText("Month: $monthsss, $year")
            } else {
                tv.setText("Month: $month , $year")
            }

            if (isNetworkConnected()) {

                if (tv.id == R.id.tvMonth)
                    initAdapter("" + year + "-" + month + "-" + "01")
                //` callMyEarning("" + year + "-" + month + "-" + "01")
                callPayoutApi("" + year + "-" + month + "-" + "01")
            } else {
                displaySnackBar!!.DisplaySnackBar(Constant.NETWIRK_ERROR, Constant.TYPE_ERROR)
            }
        }
    }

    fun capitalizeString(str: String): String? {
        var retStr = str
        try { // We can face index out of bound exception if the string is null
            retStr = str.substring(0, 1).toUpperCase() + str.substring(1)
        } catch (e: Exception) {
        }
        return retStr
    }

    private fun callMyEarning(date: String = "") {

        kProgressHUD.show()
        var monthDate = date
        if (monthDate.isBlank()) {
            monthDate = getCurrentMonthDate()
        }

        val call = apiService.getMyEarning(mySingleton.getData(Constant.USER_ID), monthDate)

        call.enqueue(object : Callback<MyEarningResponse> {
            override fun onResponse(
                call: Call<MyEarningResponse>,
                response: Response<MyEarningResponse>,
            ) {
                kProgressHUD.dismiss()
                if (response.isSuccessful && response.code() == 200) {
                    if (response.body()?.status == 1 && response.body()?.data != null) {
                        rvMyEarnings.visibility = View.VISIBLE
                        tvNoEarningData.visibility = View.GONE

                        val categoryList = response.body()?.data?.allGroups
                        (rvMyEarnings.adapter as? MyEarningAdapter)?.updateAdapter(categoryList)
                        showDataOnUi(response.body()?.data)
                    } else {
                        rvMyEarnings.visibility = View.GONE
                        tvNoEarningData.visibility = View.VISIBLE
                    }

                }
            }

            override fun onFailure(call: Call<MyEarningResponse>, t: Throwable) {
                kProgressHUD.dismiss()
                t.printStackTrace()
            }
        })
    }

    private fun callPayoutApi(date: String = "") {
        kProgressHUD.show()

        var monthDate = date
        if (monthDate.isBlank()) {
            monthDate = getCurrentMonthDate()
        }

        val call = apiService.getCheckPayout(mySingleton.getData(Constant.USER_ID), monthDate)
        Log.e("TAG", "callPayoutApi: " + monthDate)

        call.enqueue(object : Callback<EarningSummaryResponse> {
            override fun onResponse(
                call: Call<EarningSummaryResponse>,
                response: Response<EarningSummaryResponse>,
            ) {
                kProgressHUD.dismiss()
                if (response.isSuccessful && response.code() == 200) {
                    Log.e("TAG", "onResponse: " + response.body()?.data)
                    if (response.body()?.status == 1 && response.body()?.data != null) {
                        rvPayouts.visibility = View.VISIBLE
                        tvNoPayoutData.visibility = View.GONE
                        val categoryList = response.body()?.data?.allGroups
                        showDataOnUiforPayout(response.body()!!, categoryList)
                    } else {
                        rvPayouts.visibility = View.GONE
                        tvNoPayoutData.visibility = View.VISIBLE
                        val categoryList = response.body()?.data?.allGroups
                        Log.e("TAG", "onResponssssssse: " + response.body()?.data)
                        showDataOnUiforPayout(response.body()!!, categoryList)
                    }
                }
            }

            override fun onFailure(call: Call<EarningSummaryResponse>, t: Throwable) {
                kProgressHUD.dismiss()
                t.printStackTrace()
            }
        })
    }

    @SuppressLint("SetTextI18n")
    private fun showDataOnUiforPayout(
        earningSummary: EarningSummaryResponse,
        categoryList: List<EarningSummaryResponse.Data.AllGroup?>?,
    ) {

        if (earningSummary.status == 1) {
            if (!earningSummary.data?.total_amount?.get(0)?.totalAmount.isNullOrBlank()) {
                Log.e("TAG", "showDataOnUiforPayout>>>>>>>vvvvv: " + categoryList!![0]?.amount)
                tvTotalEarningsss.text =
                    "\u20B9 " + earningSummary.data?.total_amount?.get(0)?.totalAmount
                rvPayouts.adapter = PayoutAdapter()
                (rvPayouts.adapter as? PayoutAdapter)?.updateAdapter(categoryList)


            }
        }
        if (earningSummary.status == 0) {
            Log.e("TAG", "showDataOnUiforPayout 000000 ")
            tvTotalEarningsss.text = "\u20B9 " + 0
        }

    }

    @SuppressLint("SetTextI18n")
    private fun showDataOnUi(data: MyEarningResponse.Data?) {
        if (!data?.totalAmount?.get(0)?.totalAmount.isNullOrBlank()) {
            tvTotalAmount.text = "\u20B9 " + data?.totalAmount?.get(0)?.totalAmount
        } else {
            tvTotalAmount.text = "\u20B9 " + 0
        }

        if (!data?.allDebit?.get(0)?.totalAmount.isNullOrEmpty())
            tvInsuranceDeductionAmount.text = "\u20B9 " + data?.allDebit?.get(0)?.totalAmount
        else
            tvInsuranceDeductionAmount.text = "\u20B9 " + 0

        var total: Int = data?.totalAmount?.get(0)?.totalAmount?.toInt() ?: 0
        val debit: Int = data?.allDebit?.get(0)?.totalAmount?.toInt() ?: 0

        total -= debit
        tvTotalEarning1.text = "\u20B9 " + total
        tvTotalEarningsss.text = "\u20B9 " + total


        tvTotalAmounted.text = "\u20B9 " + total
    }

    fun isNetworkConnected(): Boolean {
        val connectivityManager =
            context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        var networkInfo: NetworkInfo? = null
        networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }
}