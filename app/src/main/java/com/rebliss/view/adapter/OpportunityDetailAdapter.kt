package com.rebliss.view.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.rebliss.R
import com.rebliss.domain.model.OpportunityDetailResponse
import com.rebliss.utils.getVideoIdFromYoutubeUrl
import com.rebliss.view.activity.YoutubeVideoPlayerActivity
import kotlinx.android.synthetic.main.item_my_task.view.tvTaskName
import kotlinx.android.synthetic.main.item_training.view.*


class OpportunityDetailAdapter(val activity: AppCompatActivity) : RecyclerView.Adapter<OpportunityDetailAdapter.MyTaskViewHolder>() {

    private var categoryListing: List<OpportunityDetailResponse.Data.AllGroup?>? = ArrayList()

    inner class MyTaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyTaskViewHolder {
        return MyTaskViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_training, parent, false))
    }

    override fun onBindViewHolder(holder: MyTaskViewHolder, position: Int) {
            holder.itemView.tvTaskName.text = categoryListing?.get(position)?.opportunityTitle ?: ""
        holder.itemView.tvDescription.text = categoryListing?.get(position)?.description ?: ""

        if (!categoryListing?.get(position)?.image.isNullOrEmpty()) {
            holder.itemView.videoPlayerView.visibility = View.GONE
            holder.itemView.ivTraining.visibility = View.VISIBLE

            Glide.with(holder.itemView.context).load(categoryListing?.get(position)?.image).into(holder.itemView.ivTraining)
        }

        if (!categoryListing?.get(position)?.videoLink.isNullOrEmpty()) {
            //holder.itemView.videoPlayerView.visibility = View.VISIBLE
            holder.itemView.ivVideoThumbnailss.visibility = View.VISIBLE
            Glide.with(holder.itemView.context).load(categoryListing?.get(position)?.image).into(holder.itemView.ivVideoThumbnailss)

            holder.itemView.ivTraining.visibility = View.GONE

            val videoId = getVideoIdFromYoutubeUrl(categoryListing?.get(position)?.videoLink)

            activity.lifecycle.addObserver(holder.itemView.videoPlayerView)

            holder.itemView.videoPlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    videoId?.let { youTubePlayer.cueVideo(it, 0f) }
                }
            })
            Log.e("TAG", "onBindViewHolder: "+ categoryListing?.get(position)?.image)


            holder.itemView.ivVideoThumbnailss.setOnClickListener(View.OnClickListener {
                val intent = Intent(activity,
                    YoutubeVideoPlayerActivity::class.java)
                intent.putExtra("videoId", videoId)
                Log.e("TAG", "onClick: $videoId")
                activity.startActivity(intent)

            })
        }

    }

    override fun getItemCount(): Int {
        return categoryListing?.size ?: 0
    }

    fun updateAdapter(categoryList: List<OpportunityDetailResponse.Data.AllGroup?>?) {
        categoryListing = categoryList
        notifyDataSetChanged()
    }


}