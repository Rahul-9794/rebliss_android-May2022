package com.rebliss.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.rebliss.R
import com.rebliss.domain.model.CarouselResponse
import com.rebliss.domain.model.OpportunityDetailResponse
import com.rebliss.domain.model.assignment_review.AssignmentModel
import kotlinx.android.synthetic.main.activity_edit_rbm_detail.view.*
import kotlinx.android.synthetic.main.item_layout_edit_rbm.view.*
import kotlinx.android.synthetic.main.item_login_carousel.view.*
import kotlinx.android.synthetic.main.item_my_task.view.*
import kotlinx.android.synthetic.main.item_my_task.view.tvTaskName


class EditRbmAdapter(val mActivity: AppCompatActivity? = null) : RecyclerView.Adapter<EditRbmAdapter.MyTaskViewHolder>() {


    private var categoryListing: List<AssignmentModel.Data.All_groups?>? = ArrayList()

    inner class MyTaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyTaskViewHolder {
        return MyTaskViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_layout_edit_rbm, parent, false))
    }

    override fun onBindViewHolder(holder: MyTaskViewHolder, position: Int) {
     categoryListing?.get(position)?.apply {
      if(sub_category1_id.equals("59")){
          holder.itemView.txtEditActivityTypes.text = "reBLISS DPSO1 (Jio Mart)"
      }
         if(sub_category1_id.equals("52")){
             holder.itemView.txtEditActivityTypes.text = "reBLISS DPMO1 (Airtel)"
         }
        holder.itemView.txtEditStoreIds.text = shop_id ?: ""
        holder.itemView.txtEditActivityPhotos.text = inside_photo ?: ""
        holder.itemView.txtEditPancardNumbers.text = pan_number ?: ""
        holder.itemView.txtPancardPhotos.text = pan_photo ?: ""
        holder.itemView.txtEditGSTNumbers.text = gst_number ?: ""
        holder.itemView.txtEditGSTPhoto.text = gst_photo ?: ""


     }
    }

    override fun getItemCount(): Int {
        return categoryListing?.size ?: 0
    }

    fun updateAdapter(categoryList: List<AssignmentModel.Data.All_groups?>?) {
        categoryListing = categoryList
        notifyDataSetChanged()
    }
}