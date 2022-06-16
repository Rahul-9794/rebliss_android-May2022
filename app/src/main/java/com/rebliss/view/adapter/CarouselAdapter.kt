package com.rebliss.view.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.rebliss.R
import com.rebliss.domain.model.CarouselResponse
import com.rebliss.domain.model.opportunitylist.Opportunity
import com.rebliss.utils.getVideoIdFromYoutubeUrl
import com.rebliss.view.activity.*
import com.rebliss.view.activity.notification.NotificationListActivity
import kotlinx.android.synthetic.main.item_carousel.view.*
import kotlinx.android.synthetic.main.item_carousel.view.ivCarousel
import kotlinx.android.synthetic.main.item_carousel.view.ivVideoThumbnail
import kotlinx.android.synthetic.main.item_login_carousel.view.*
import kotlinx.android.synthetic.main.item_training.view.videoPlayerView


class CarouselAdapter(var  context: Context, val fragment: Fragment?, val mActivity: AppCompatActivity? = null) : RecyclerView.Adapter<CarouselAdapter.MyTaskViewHolder>() {

    companion object {
        var IS_VIDEO_PLAYING: Boolean = false
    }
    private var categoryListing: List<CarouselResponse.Data.AllGroup?>? = ArrayList()

    inner class MyTaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyTaskViewHolder {
        return MyTaskViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_carousel, parent, false))
    }

    override fun onBindViewHolder(holder: MyTaskViewHolder, position: Int) {

        if (!categoryListing?.get(position)?.image.isNullOrEmpty()) {
            holder.itemView.videoPlayerView.visibility = View.INVISIBLE
            holder.itemView.ivCarousel.visibility = View.VISIBLE

            Glide.with(holder.itemView.context).load(categoryListing?.get(position)?.image).into(holder.itemView.ivCarousel)
            holder.itemView.ivCarousel.setOnClickListener {
                val num1:String = categoryListing?.get(position)?.redirection_type!!
                val screenRedirectValue:String = categoryListing?.get(position)?.screen_redirection_value!!
              //  val num1:String ="1"
                val showType:String = categoryListing?.get(position)?.show_type!!
                val img_redirectLink:String = categoryListing?.get(position)?.img_redirect_link!!
             //  val showType:String = "1"

                if(num1.equals("1")) {
                    val intent = Intent(context, ImageWebviewActivity::class.java)
                    //intent.putExtra("showType",showType)
                    intent.putExtra("showType",showType)
                    intent.putExtra("imgRedirectLink",img_redirectLink)
                    context.startActivity(intent)
                }
                else if(screenRedirectValue.equals("1")) {
                    val intent = Intent(context, MyTaskActivity::class.java)
                    context.startActivity(intent)
                }
                else if(screenRedirectValue.equals("2")) {
                    val intent = Intent(context, OpportunityListActivity::class.java)
                    context.startActivity(intent)
                }

                else if(screenRedirectValue.equals("3")) {
                    val intent = Intent(context, MyEarningActivity::class.java)
                    context.startActivity(intent)
                }
                else if(screenRedirectValue.equals("4")) {
                    val intent = Intent(context, NotificationListActivity::class.java)
                    context.startActivity(intent)
                }

                else if(screenRedirectValue.equals("5")) {
                    val intent = Intent(context, MyProfileActivity::class.java)
                    context.startActivity(intent)
                }
                else{
                    Log.e("CarouselAdapeter", "onBindViewHolder: else" )
                }
               }
        }

        if (!categoryListing?.get(position)?.videoLink.isNullOrEmpty()) {
            holder.itemView.ivVideoThumbnail.visibility = View.VISIBLE
            holder.itemView.ivCarousel.visibility = View.INVISIBLE

            val videoId = getVideoIdFromYoutubeUrl(categoryListing?.get(position)?.redirectLink)
            Glide.with(holder.itemView.context).load(categoryListing?.get(position)?.videoLink).into(holder.itemView.ivVideoThumbnail)
            if (fragment != null) {
                fragment.lifecycle.addObserver(holder.itemView.videoPlayerView)
            } else {
                mActivity?.lifecycle?.addObserver(holder.itemView.videoPlayerView)
            }

            holder.itemView.videoPlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    videoId?.let { youTubePlayer.cueVideo(it, 0f) }
                    youTubePlayer.pause()
                }

                override fun onStateChange(youTubePlayer: YouTubePlayer, state: PlayerConstants.PlayerState) {
                    when (state) {
                        PlayerConstants.PlayerState.BUFFERING -> {
                            IS_VIDEO_PLAYING = true
                        }
                        PlayerConstants.PlayerState.PLAYING -> {
                            IS_VIDEO_PLAYING = true
                        }
                        PlayerConstants.PlayerState.PAUSED -> {
                            IS_VIDEO_PLAYING = false
                        }
                        PlayerConstants.PlayerState.ENDED -> {
                            IS_VIDEO_PLAYING = false
                        }
                        PlayerConstants.PlayerState.VIDEO_CUED -> {
                            IS_VIDEO_PLAYING = false
                        }
                        PlayerConstants.PlayerState.UNSTARTED -> {
                            IS_VIDEO_PLAYING = false
                        }
                        PlayerConstants.PlayerState.UNKNOWN -> {
                            IS_VIDEO_PLAYING = false
                        }
                    }
                }

                override fun onError(youTubePlayer: YouTubePlayer, error: PlayerConstants.PlayerError) {
                    IS_VIDEO_PLAYING = false
                }
            })

            holder.itemView.ivVideoThumbnail.setOnClickListener {
                val intent = Intent(mActivity, YoutubeVideoPlayerActivity::class.java)
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