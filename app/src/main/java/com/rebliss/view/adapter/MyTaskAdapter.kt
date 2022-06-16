package com.rebliss.view.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rebliss.R
import com.rebliss.domain.model.ActivityTaskResponse
import kotlinx.android.synthetic.main.item_my_earning.view.*
import kotlinx.android.synthetic.main.item_my_task.view.*
import kotlinx.android.synthetic.main.item_my_task.view.tvTaskName

class MyTaskAdapter(val sendDataBack: (DataModel) -> Unit) : RecyclerView.Adapter<MyTaskAdapter.MyTaskViewHolder>() {

    private var categoryListing: List<ActivityTaskResponse.Data.AllGroup?>? = ArrayList()

    inner class MyTaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyTaskViewHolder {
        return MyTaskViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_my_task, parent, false))
    }

    override fun onBindViewHolder(holder: MyTaskViewHolder, position: Int) {
        holder.itemView.tvTaskName.text = categoryListing?.get(position)?.taskFor?.categoryName
        if(categoryListing?.get(position)?.amount.equals("0"))
        {

            holder.itemView.tvTaskAmount.text =""
        }
        else {
            holder.itemView.tvTaskAmount.text = "\u20B9 " + categoryListing?.get(position)?.amount

        }
       // holder.itemView.tvTaskAmount.text = ("\u20B9 " + categoryListing?.get(position)?.amount).plus("/Activity")

        holder.itemView.btnTraining.setOnClickListener {
            sendDataBack(DataModel(TRAINING, categoryListing?.get(position)?.taskFor?.categoryId
                    ?: ""))
        }

        holder.itemView.btnStartNew.setOnClickListener {
            if (categoryListing?.get(position)?.taskFor?.categoryId == "65") {
                sendDataBack(DataModel(
                        START_CUDEL,
                        categoryListing?.get(position)?.taskFor?.categoryId ?: "",
                        categoryListing?.get(position)?.taskFor?.parentId ?: "",
                        categoryListing?.get(position)?.categoryDetail?.parentId ?: "",
                        categoryListing?.get(position)?.id ?: "",
                        categoryListing?.get(position)?.amount ?: ""))
            }
            else if (categoryListing?.get(position)?.taskFor?.categoryId == "69") {
                sendDataBack(DataModel(
                        START_DAILY_DSR,
                        categoryListing?.get(position)?.taskFor?.categoryId ?: "",
                        categoryListing?.get(position)?.taskFor?.parentId ?: "",
                        categoryListing?.get(position)?.categoryDetail?.parentId ?: "",
                        categoryListing?.get(position)?.id ?: "",
                        categoryListing?.get(position)?.amount ?: ""))
            }
           else if (categoryListing?.get(position)?.taskFor?.categoryId == "66") {
                sendDataBack(DataModel(
                        PINE_LABS,
                        categoryListing?.get(position)?.taskFor?.categoryId ?: "",
                        categoryListing?.get(position)?.taskFor?.parentId ?: "",
                        categoryListing?.get(position)?.categoryDetail?.parentId ?: "",
                        categoryListing?.get(position)?.id ?: "",
                        categoryListing?.get(position)?.amount ?: ""))
            }


            else if (categoryListing?.get(position)?.taskFor?.categoryId == "72") {
                sendDataBack(DataModel(
                        START_NEW_FORM,
                        categoryListing?.get(position)?.taskFor?.categoryId ?: "",
                        categoryListing?.get(position)?.taskFor?.parentId ?: "",
                        categoryListing?.get(position)?.categoryDetail?.parentId ?: "",
                        categoryListing?.get(position)?.id ?: "",
                        categoryListing?.get(position)?.amount ?: ""))
            }


            else {
                sendDataBack(DataModel(START_NEW,
                        categoryListing?.get(position)?.taskFor?.categoryId ?: "",
                        categoryListing?.get(position)?.taskFor?.parentId ?: "",
                        categoryListing?.get(position)?.categoryDetail?.parentId ?: "",
                        categoryListing?.get(position)?.id ?: "",
                        categoryListing?.get(position)?.amount ?: ""))
            }
        }

        holder.itemView.btnViewStatus.setOnClickListener {

            if (categoryListing?.get(position)?.taskFor?.categoryId == "72") {
                sendDataBack(
                    DataModel(
                        VIEW_STATUS,
                        categoryListing?.get(position)?.taskFor?.categoryId ?: "",
                        categoryListing?.get(position)?.taskFor?.parentId ?: "",
                        categoryListing?.get(position)?.categoryDetail?.parentId ?: "",
                        categoryListing?.get(position)?.id ?: "",
                        categoryListing?.get(position)?.amount ?: ""
                    )
                )
            } else {
                sendDataBack(
                    DataModel(
                        VIEW_STATUS, categoryListing?.get(position)?.taskFor?.categoryId
                            ?: ""
                    )
                )
            }
        }
    }

    override fun getItemCount(): Int {
        return categoryListing?.size ?: 0
    }

    fun updateAdapter(categoryList: List<ActivityTaskResponse.Data.AllGroup?>?) {
        categoryListing = categoryList
        notifyDataSetChanged()
    }

    companion object {
        const val TRAINING = "training"
        const val START_NEW = "start_new"
        const val START_CUDEL = "start_cudel"
        const val START_DAILY_DSR = "start_daily_dsr"
        const val PINE_LABS = "pine_labs"
        const val START_NEW_FORM = "new_form"
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