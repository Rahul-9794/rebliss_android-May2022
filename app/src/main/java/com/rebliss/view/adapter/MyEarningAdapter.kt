package com.rebliss.view.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rebliss.R
import com.rebliss.domain.model.MyEarningResponse
import com.rebliss.view.activity.ViewInvoiceAcitivity
import kotlinx.android.synthetic.main.item_my_earning.view.*

class MyEarningAdapter(val sendDataBack: (DataModel) -> Unit) : RecyclerView.Adapter<MyEarningAdapter.MyTaskViewHolder>() {

    private var categoryListing: List<MyEarningResponse.Data.AllGroup?>? = ArrayList()

    private var context : Context? = null


    inner class MyTaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyTaskViewHolder {
        context = parent.context
        return MyTaskViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_my_earning, parent, false))


    }

    override fun onBindViewHolder(holder: MyTaskViewHolder, position: Int) {
        holder.itemView.tvTaskName.text = categoryListing?.get(position)?.earningTask?.taskFor?.categoryName

        holder.itemView.btnViewSummary.setOnClickListener {
            sendDataBack(DataModel(VIEW_SUMMARY, categoryListing?.get(position)?.earningTask?.id
                    ?: ""))
        }

        holder.itemView.btnGenerateInvoice.setOnClickListener {
           /* sendDataBack(DataModel(GENERATE_INVOICE, categoryListing?.get(position)?.earningTask?.id
                    ?: ""))*/

            val intent = Intent(context, ViewInvoiceAcitivity::class.java)
            intent.putExtra("categoryid",categoryListing?.get(position)?.earningTask?.taskFor?.categoryId)
            context?.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return categoryListing?.size ?: 0
    }

    fun updateAdapter(categoryList: List<MyEarningResponse.Data.AllGroup?>?) {
        categoryListing = categoryList
        notifyDataSetChanged()
    }

    companion object {
        const val VIEW_SUMMARY = "view_summary"
        const val GENERATE_INVOICE = "generate_invoice"
    }

    data class DataModel(
            var clickType: String = VIEW_SUMMARY,
            var earningTaskId: String = "",
    )

}