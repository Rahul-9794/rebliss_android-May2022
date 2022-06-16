package com.rebliss.view.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.rebliss.R
import com.rebliss.domain.model.CarouselResponse
import com.rebliss.utils.getVideoIdFromYoutubeUrl
import com.rebliss.view.activity.ViewInvoiceAcitivity
import com.rebliss.view.activity.YoutubeVideoPlayerActivity
import com.rebliss.view.activity.youtubeplayerscreen.VideoPlayerActivity
import kotlinx.android.synthetic.main.item_login_carousel.view.*
import kotlinx.android.synthetic.main.item_training.view.videoPlayerView


class LoginCarouselAdapter(val fragment: Fragment?, val mActivity: AppCompatActivity? = null) : RecyclerView.Adapter<LoginCarouselAdapter.MyTaskViewHolder>() {

    companion object {
        var IS_VIDEO_PLAYING: Boolean = false
    }

    private var categoryListing: List<CarouselResponse.Data.AllGroup?>? = ArrayList()

    inner class MyTaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyTaskViewHolder {
        return MyTaskViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_login_carousel, parent, false))
    }

    override fun onBindViewHolder(holder: MyTaskViewHolder, position: Int) {

        if (!categoryListing?.get(position)?.image.isNullOrEmpty()) {
            holder.itemView.videoPlayerViews.visibility = View.INVISIBLE
            holder.itemView.ivCarousel.visibility = View.VISIBLE
            holder.itemView.ivPlay.visibility = View.INVISIBLE
            Glide.with(holder.itemView.context).load(categoryListing?.get(position)?.image).into(holder.itemView.ivCarousel)
        }

        if (!categoryListing?.get(position)?.videoLink.isNullOrEmpty()) {
            holder.itemView.ivVideoThumbnail.visibility = View.VISIBLE
            holder.itemView.ivCarousel.visibility = View.INVISIBLE

            val videoId = getVideoIdFromYoutubeUrl(categoryListing?.get(position)?.redirectLink)
         //   val videoId = "fSL9wGkOPI4"

            if (fragment != null) {
                fragment.lifecycle.addObserver(holder.itemView.videoPlayerViews)
            } else {
                mActivity?.lifecycle?.addObserver(holder.itemView.videoPlayerViews)
            }
            Glide.with(holder.itemView.context).load(categoryListing?.get(position)?.videoLink).into(holder.itemView.ivVideoThumbnail)

            holder.itemView.ivVideoThumbnail.setOnClickListener {
                val intent = Intent(mActivity, YoutubeVideoPlayerActivity::class.java)
               // intent.putExtra("videoId",categoryListing?.get(position)?.redirectLink)
                intent.putExtra("videoId",videoId)
                Log.e("TAG", "onBindViewHolder: >>>>>"+videoId )
                mActivity?.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int {
        return categoryListing?.size ?: 0
    }

    fun updateAdapter(categoryList: List<CarouselResponse.Data.AllGroup?>?) {
        categoryListing = categoryList
        notifyDataSetChanged()
    }



}