package com.rebliss.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rebliss.R
import com.rebliss.domain.model.ViewInvoiceResponse
import com.rebliss.utils.getDisplayFormatDate
import kotlinx.android.synthetic.main.item_invoice_summary.view.*

class ViewInvoiceAdapter(categoryList: List<ViewInvoiceResponse.Data.All_groups>?) : RecyclerView.Adapter<ViewInvoiceAdapter.MyTaskViewHolder>() {

    private var categoryListing: List<ViewInvoiceResponse.Data.All_groups?>? = ArrayList()

    inner class MyTaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyTaskViewHolder {
        return MyTaskViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_invoice_summary, parent, false))
    }

    override fun onBindViewHolder(holder: MyTaskViewHolder, position: Int) {
        holder.itemView.tvDates.text = getDisplayFormatDate(categoryListing?.get(position)?.created_at!!)
        holder.itemView.tvActivitys.text = categoryListing?.get(position)?.earningTask?.taskFor?.category_name
        holder.itemView.tvAmounts.text = "\u20B9 " + categoryListing?.get(position)?.earningTask?.amount
    }

    override fun getItemCount(): Int {
        return categoryListing?.size ?: 0
    }

    fun updateAdapter(categoryList: List<ViewInvoiceResponse.Data.All_groups?>?) {
        categoryListing = categoryList
        notifyDataSetChanged()
    }



}