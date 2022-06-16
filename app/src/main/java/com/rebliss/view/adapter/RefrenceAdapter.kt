package com.rebliss.view.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.rebliss.R
import com.rebliss.domain.model.getRefrenceDataResponse
import kotlinx.android.synthetic.main.item_merchant_detail.view.*

class RefrenceAdapter(val activity: AppCompatActivity) : RecyclerView.Adapter<RefrenceAdapter.MyTaskViewHolder>() {

    private var categoryListing: List<getRefrenceDataResponse.Data.All_groups?>? = ArrayList()

    inner class MyTaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyTaskViewHolder {
        return MyTaskViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_merchant_detail_list, parent, false))
    }

    override fun onBindViewHolder(holder: MyTaskViewHolder, position: Int) {
        categoryListing?.get(position)?.apply {
            holder.itemView.tvMerchantTitle.text = name
            holder.itemView.tvMerchantCode.text = user_id.toString()
            holder.itemView.tvPhoneNo.text = mobile.toString()
            holder.itemView.tvAddress.text = category
            holder.itemView.tvPincode.text = pincode.toString()

        }
    }

    override fun getItemCount(): Int {
        Log.e("TAG", "getItemCount: "+categoryListing?.size )

        return categoryListing?.size ?: 0

    }

    fun updateAdapter(categoryList: List<getRefrenceDataResponse.Data.All_groups?>?) {
        categoryListing = categoryList
        notifyDataSetChanged()
    }

    companion object {
        const val TRAINING = "training"
        const val START_NEW = "start_new"
        const val START_CUDEL = "start_cudel"
        const val START_DAILY_DSR = "start_daily_dsr"
        const val PINE_LABS = "pine_labs"
        const val VIEW_STATUS = "view_status"
    }

    data class DataModel(
            var clickType: String = TRAINING,
            var subcategoryId1: String = "",
            var subcategoryId: String = "",
            var categoryId1: String = "",
            var earningTaskId: String = "",
            var amount: String = ""
    )

}