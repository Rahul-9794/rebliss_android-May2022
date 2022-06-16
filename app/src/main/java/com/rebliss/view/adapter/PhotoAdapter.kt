package com.rebliss.view.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rebliss.utils.App
import com.rebliss.R
import com.rebliss.view.activity.DashboardDocUpload
import com.rebliss.view.activity.DashboardDocUploadEdit
import com.rebliss.view.activity.DashboardFOSDocUpload
import com.rebliss.view.activity.DashboardFOSDocUploadEdit
import kotlinx.android.synthetic.main.doc_upload_remove.view.*

class PhotoAdapter(var images: Array<String?>, var rowLayout: Int, var context: Context, var type: String?) : RecyclerView.Adapter<PhotoAdapter.MyTaskViewHolder>() {





    inner class MyTaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyTaskViewHolder {
        return MyTaskViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.doc_upload_remove, parent, false))
    }

    override fun onBindViewHolder(holder: MyTaskViewHolder, position: Int) {

            holder.itemView.textAdharFileName.text = images!![position]
            holder.itemView.textAdharFileName.setTypeface(App.LATO_REGULAR)
        holder.itemView.imgRemoveAdhar.setOnClickListener(View.OnClickListener {
            Log.i("uuu", position.toString() + "")
            val temp = arrayOfNulls<String?>(images!!.size - 1)
            for (i in images!!.indices) {
                if (i < position) {
                    temp[i] = images!![i]
                } else if (i > position) {
                    temp[i - 1] = images!![i]
                }
            }
            if (context is DashboardFOSDocUpload) {
                (context as DashboardFOSDocUpload).deleteUpload(type, images!![position])
            } else if (context is DashboardDocUpload) {
                (context as DashboardDocUpload).deleteUpload(type, images!![position])
            }
            if (context is DashboardFOSDocUploadEdit) {
                (context as DashboardFOSDocUploadEdit).deleteUpload(type, images!![position])
            } else if (context is DashboardDocUploadEdit) {
                (context as DashboardDocUploadEdit).deleteUpload(type, images!![position])
            }
            images = temp
            notifyDataSetChanged()
        })
    }

    fun UploadRemoveAdapter(images: Array<String?>?, rowLayout: Int, context: Context, type: String?) {
        this.images = images!!
        this.type = type
    }

    fun updateData(images: Array<String?>?) {
        this.images = images!!
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        Log.e("TAG", "getItemCount: " + images.size)

        return images.size ?: 0

    }
}