package com.rebliss.view.activity.downloadviaLink

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rebliss.R
import kotlinx.android.synthetic.main.item_opportunity.view.*


internal class DownloadLinkAdapter(
    private var context: Context,
    private var downloadLinkList: List<DemandPartnerAppLinkModel.AllGroup>,
) : RecyclerView.Adapter<DownloadLinkAdapter.DownloadLinkHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DownloadLinkHolder {

        return DownloadLinkHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_opportunity,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: DownloadLinkHolder, position: Int) {
        val downloadLinkModel: DemandPartnerAppLinkModel.AllGroup = downloadLinkList[position]

        holder.itemView.tv_text.text = downloadLinkModel.name
        holder.itemView.setOnClickListener {
            val uri: Uri = Uri.parse(downloadLinkModel.link)

            val intent = Intent(Intent.ACTION_VIEW, uri)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return downloadLinkList.size
    }

    inner class DownloadLinkHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


    }
}

