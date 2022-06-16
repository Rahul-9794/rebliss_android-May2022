package com.rebliss.view.activity

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.rebliss.R
import com.rebliss.domain.constant.Constant
import kotlinx.android.synthetic.main.activity_youtube_video_player.*

class YoutubeVideoPlayerActivity : YouTubeBaseActivity(), YouTubePlayer.OnInitializedListener {
    var videoId: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_youtube_video_player)


        videoId = intent.getStringExtra("videoId").toString()
        Log.e("TAG", "onCreate: " + videoId)

        youtubePlayerView.initialize(Constant.YOUTUBE_API, this)

    }

    override fun onInitializationSuccess(
        provider: YouTubePlayer.Provider?, youTubePlayer: YouTubePlayer?,
        wasRestored: Boolean,
    ) {
        Log.d("TAG", "onInitializationSuccess: provider is ${provider?.javaClass}")
        Log.d("TAG", "onInitializationSuccess: youTubePlayer is ${youTubePlayer?.javaClass}")

        youTubePlayer?.setPlayerStateChangeListener(playerStateChangeListener)
        youTubePlayer?.setPlaybackEventListener(playbackEventListener)

        if (!wasRestored) {
            youTubePlayer?.loadVideo(videoId)
        }
    }

    override fun onInitializationFailure(
        provider: YouTubePlayer.Provider?,
        youTubeInitializationResult: YouTubeInitializationResult?,
    ) {
        val REQUEST_CODE = 0

        if (youTubeInitializationResult?.isUserRecoverableError == true) {
            youTubeInitializationResult.getErrorDialog(this, REQUEST_CODE).show()
        } else {
            val errorMessage =
                "There was an error initializing the YoutubePlayer ($youTubeInitializationResult)"
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
        }
    }

    private val playbackEventListener = object : YouTubePlayer.PlaybackEventListener {
        override fun onSeekTo(p0: Int) {
        }

        override fun onBuffering(p0: Boolean) {
        }

        override fun onPlaying() {
        }

        override fun onStopped() {
        }

        override fun onPaused() {
        }
    }

    private val playerStateChangeListener = object : YouTubePlayer.PlayerStateChangeListener {
        override fun onAdStarted() {
            Toast.makeText(this@YoutubeVideoPlayerActivity,
                "Click Ad now, make the video creator rich!",
                Toast.LENGTH_SHORT).show()
        }

        override fun onLoading() {
        }

        override fun onVideoStarted() {
        }

        override fun onLoaded(p0: String?) {
        }

        override fun onVideoEnded() {
            onBackPressed()
        }

        override fun onError(p0: YouTubePlayer.ErrorReason?) {
        }
    }
}
