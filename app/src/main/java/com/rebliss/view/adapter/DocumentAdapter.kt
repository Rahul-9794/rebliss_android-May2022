package com.rebliss.view.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.rebliss.MainActivity
import com.rebliss.R
import com.rebliss.domain.model.MyEarningResponse
import com.rebliss.domain.model.document.DocumentModel
import com.rebliss.domain.model.documentByUser.Data
import com.rebliss.view.activity.ViewDocumentActivity
import kotlinx.android.synthetic.main.item_document_layout.view.*
import kotlinx.android.synthetic.main.item_my_task.view.*
import kotlinx.android.synthetic.main.item_my_task.view.tvTaskName

class DocumentAdapter(private var docList: ArrayList<Data>,private val activity:Context):RecyclerView.Adapter<DocumentAdapter.DocHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DocumentAdapter.DocHolder {
        return DocHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_document_layout, parent, false))
    }
    inner class DocHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    override fun onBindViewHolder(holder: DocumentAdapter.DocHolder, position: Int) {
        holder.itemView.tvDocType.text = docList.get(position).document_title
        holder.itemView.tvView.setOnClickListener {
            val path = docList.get(position).document_path
            val file_type = docList.get(position).file_type
            val intent = Intent(activity,ViewDocumentActivity::class.java)
            intent.putExtra("doc_path",path)
            intent.putExtra("file_type",file_type)
            activity.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return docList.size
    }
    fun updateAdapter(categoryList: List<Data>) {
        docList = categoryList as ArrayList<Data>
        notifyDataSetChanged()
    }
}