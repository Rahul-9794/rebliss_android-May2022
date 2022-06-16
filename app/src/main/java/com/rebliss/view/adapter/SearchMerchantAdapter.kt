package com.rebliss.view.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.rebliss.R
import com.rebliss.domain.model.SeachMerchantData
import com.rebliss.view.activity.MerchantActivity
import kotlinx.android.synthetic.main.item_merchant_detail.view.*
import kotlinx.android.synthetic.main.my_profile_layout.view.*

class SearchMerchantAdapter(val activity: AppCompatActivity) :
    RecyclerView.Adapter<SearchMerchantAdapter.MyTaskViewHolder>() {

    private var categoryListing: List<SeachMerchantData.Data.All_groups?>? = ArrayList()

    inner class MyTaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyTaskViewHolder {
        return MyTaskViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.item_merchant_detail, parent, false))
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: MyTaskViewHolder, position: Int) {
        categoryListing?.get(position)?.apply {
            holder.itemView.tvMerchantTitle.text = first_name.plus(" ").plus(last_name)

            holder.itemView.tvPhoneNo.text = phone_number
            holder.itemView.tvAddress.text = location_city.plus(",").plus(location_state)
            holder.itemView.tvPincode.text = location_zipcode.toString()
            if (profile_status.equals("0")) {
                holder.itemView.tvStatus.text = "InComplete"
                holder.itemView.llMerchantLayout.isEnabled = true
            } else if (profile_status.equals("1")) {
                holder.itemView.tvStatus.text = "Completed"
                holder.itemView.llMerchantLayout.isEnabled = false
            } else if (profile_status.equals("2")) {
                holder.itemView.tvStatus.text = "Initiated"
                holder.itemView.llMerchantLayout.isEnabled = true
            }
            if(fos_shop_name.equals("")||fos_shop_name.length==0)
            {
                holder.itemView.rlshop.visibility = View.GONE
            }
            else {
                holder.itemView.tvShopName.text = "Shop Name :- ".plus(fos_shop_name)
            }
            if(fos_shop_name.equals("")||fos_shop_name.length<1){
                holder.itemView.tvMerchantCode.text = (("rBS").plus(id))
                holder.itemView.llMerchantLayout.isEnabled = false
            }
            else{
                holder.itemView.tvMerchantCode.text = (("rBM").plus(id))
            }
          //  holder.itemView.tvMerchantTitle.text = first_name



            holder.itemView.llMerchantLayout.setOnClickListener {
                   val intent = Intent(activity, MerchantActivity::class.java)
                   intent.putExtra("sathi_id", id.toString())
                   intent.putExtra("first_name", first_name.toString())
                   intent.putExtra("phone_number", phone_number.toString())
                   activity.startActivity(intent)

            }
        }
    }

    override fun getItemCount(): Int {
        return categoryListing?.size ?: 0
    }

    fun updateAdapter(categoryList: List<SeachMerchantData.Data.All_groups?>?) {
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
        var amount: String = "",
    )

}