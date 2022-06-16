package com.rebliss.view.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rebliss.R
import com.rebliss.domain.model.EarningSummaryResponse
import com.rebliss.utils.getDisplayFormatDate
import com.rebliss.utils.getDisplayFormatDate1
import kotlinx.android.synthetic.main.item_earning_summary.view.*

class PayoutAdapter : RecyclerView.Adapter<PayoutAdapter.MyTaskViewHolder>() {
    private  var TAG = "PayoutAdapter"

    private var categoryListing: List<EarningSummaryResponse.Data.AllGroup?>? = ArrayList()

    inner class MyTaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyTaskViewHolder {
        return MyTaskViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_earning_summary, parent, false))
    }

    override fun onBindViewHolder(holder: MyTaskViewHolder, position: Int) {
        val t = getDisplayFormatDate1(categoryListing?.get(position)?.date!!)
       val myName = t.substring(3, 4) + 2 + t.substring(3+1);
        Log.e(TAG, "onBindViewHolder:  "+t )
        Log.e(TAG, "onBindViewHolder: DSFSDFSD   "+myName )
        holder.itemView.tvDate.text = getDisplayFormatDate1(categoryListing?.get(position)?.date!!)
        holder.itemView.tvActivity.text = categoryListing?.get(position)?.earningTask?.taskFor?.categoryName
        holder.itemView.tvAmount.text = "\u20B9 " + categoryListing?.get(position)?.amount
        if (categoryListing?.get(position)?.payoutStatus == "1") {
            holder.itemView.tvPayoutStatus.text = "Completed"
        } else {
            holder.itemView.tvPayoutStatus.text = "In Progress"

        }
    }

    override fun getItemCount(): Int {
        return categoryListing?.size ?: 0
    }

    fun updateAdapter(categoryList: List<EarningSummaryResponse.Data.AllGroup?>?) {
        categoryListing = categoryList
        notifyDataSetChanged()
    }

}