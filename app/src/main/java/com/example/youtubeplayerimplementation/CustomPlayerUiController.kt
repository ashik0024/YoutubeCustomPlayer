package com.example.youtubeplayerimplementation

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.SeekBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.YouTubePlayerTracker
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

internal class CustomPlayerUiController(
    private val context: Context,
    customPlayerUi: View,
    private val youTubePlayer: YouTubePlayer,
    private val youTubePlayerView: YouTubePlayerView
) : AbstractYouTubePlayerListener() {

    private val playerTracker = YouTubePlayerTracker()
    private var fullscreen = false

    private var panel: View? = null
    private var progressbar: View? = null
    private var videoCurrentTimeTextView: TextView? = null
    private var videoDurationTextView: TextView? = null
    private var videoSeekBar: SeekBar? = null

    init {
        youTubePlayer.addListener(playerTracker)
        initViews(customPlayerUi)
    }

    private fun initViews(playerUi: View) {
        panel = playerUi.findViewById(R.id.panel)
        videoCurrentTimeTextView = playerUi.findViewById(R.id.video_current_time)
        videoDurationTextView = playerUi.findViewById(R.id.video_duration)
        videoSeekBar = playerUi.findViewById(R.id.video_seekbar)

        val playPauseButton = playerUi.findViewById<ImageButton>(R.id.play_pause_button)
        val enterExitFullscreenButton = playerUi.findViewById<ImageButton>(R.id.enter_exit_fullscreen_button)

        // Initially hide all controls
        playPauseButton.visibility = View.GONE
        enterExitFullscreenButton.visibility = View.GONE
        videoSeekBar?.visibility = View.GONE
        videoCurrentTimeTextView?.visibility = View.GONE
        videoDurationTextView?.visibility = View.GONE

        // Toggle visibility of all controls on panel click
        panel?.setOnClickListener {
            // Show all controls
            playPauseButton.visibility = View.VISIBLE
            enterExitFullscreenButton.visibility = View.VISIBLE
            videoSeekBar?.visibility = View.VISIBLE
            videoCurrentTimeTextView?.visibility = View.VISIBLE
            videoDurationTextView?.visibility = View.VISIBLE

            // Use Handler to hide all controls after 5 seconds
            Handler(Looper.getMainLooper()).postDelayed({
                playPauseButton.visibility = View.GONE
                enterExitFullscreenButton.visibility = View.GONE
                videoSeekBar?.visibility = View.GONE
                videoCurrentTimeTextView?.visibility = View.GONE
                videoDurationTextView?.visibility = View.GONE
            }, 5000)  // 5000ms = 5 seconds
        }

        // Play/Pause button click behavior
        playPauseButton.setOnClickListener {
            if (playerTracker.state == PlayerConstants.PlayerState.PLAYING) youTubePlayer.pause()
            else youTubePlayer.play()
        }

        // Fullscreen button click behavior
        enterExitFullscreenButton.setOnClickListener {
            if (fullscreen) youTubePlayerView.wrapContent()
            else youTubePlayerView.matchParent()
            fullscreen = !fullscreen
        }

        // Seekbar change listener
        videoSeekBar?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    val videoDuration = playerTracker.videoDuration
                    val seekTo = (progress / 100.0 * videoDuration).toFloat()
                    youTubePlayer.seekTo(seekTo)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                youTubePlayer.pause()
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                youTubePlayer.play()
            }
        })
    }




    override fun onReady(youTubePlayer: YouTubePlayer) {
//        progressbar?.visibility = View.GONE
    }

    override fun onStateChange(youTubePlayer: YouTubePlayer, state: PlayerConstants.PlayerState) {
        if (state == PlayerConstants.PlayerState.PLAYING || state == PlayerConstants.PlayerState.PAUSED) {
            panel?.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent))
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onCurrentSecond(youTubePlayer: YouTubePlayer, second: Float) {
        videoCurrentTimeTextView?.text = second.toString()
        val progress = (second / playerTracker.videoDuration * 100).toInt()
        videoSeekBar?.progress = progress
    }

    @SuppressLint("SetTextI18n")
    override fun onVideoDuration(youTubePlayer: YouTubePlayer, duration: Float) {
        videoDurationTextView?.text = duration.toString()
    }
}

