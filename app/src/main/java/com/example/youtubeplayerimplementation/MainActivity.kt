package com.example.youtubeplayerimplementation

import android.content.res.Configuration
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.loadOrCueVideo
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

class MainActivity : AppCompatActivity() {
    private var youTubePlayerView: YouTubePlayerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


//        youTubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
//            override fun onReady(youTubePlayer: YouTubePlayer) {
//                loadBtn.setOnClickListener {
//                    val videoUrl = videoUrlEditText.text.toString().trim()
//                    val videoId = extractVideoId(videoUrl)
//
//                    if (videoId.isNullOrEmpty()) {
//                        Toast.makeText(this@MainActivity, "Invalid YouTube URL", Toast.LENGTH_SHORT).show()
//                    } else {
//                        Toast.makeText(this@MainActivity, "Loading Video: $videoId", Toast.LENGTH_SHORT).show()
//                        youTubePlayer.loadVideo(videoId, 0f)
//                    }
//                }
//            }
//        })

        youTubePlayerView = findViewById(R.id.youtube_player_view)
        val videoUrlEditText = findViewById<EditText>(R.id.videoUrlEt)
        val loadBtn = findViewById<Button>(R.id.loadVideo)

        lifecycle.addObserver(youTubePlayerView!!)

        val customPlayerUi = youTubePlayerView!!.inflateCustomPlayerUi(R.layout.custom_player_ui)

        val listener = object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                val customPlayerUiController = CustomPlayerUiController(
                    baseContext,
                    customPlayerUi,
                    youTubePlayer,
                    youTubePlayerView!!
                )
                youTubePlayer.addListener(customPlayerUiController)

                loadBtn.setOnClickListener {
                    val videoUrl = videoUrlEditText.text.toString().trim()
                    val videoId = extractVideoId(videoUrl)

                    if (videoId.isNullOrEmpty()) {
                        Toast.makeText(this@MainActivity, "Invalid YouTube URL", Toast.LENGTH_SHORT).show()
                    } else {
                        youTubePlayer.loadVideo(videoId, 0f)
                    }
                }
            }
        }

        val playerOptions = IFramePlayerOptions.Builder()
            .controls(0) // Disable the default YouTube control view
            .build()

        youTubePlayerView?.initialize(listener, playerOptions)
    }

    private fun extractVideoId(youtubeUrl: String): String? {
        val regex = "(?<=v=|youtu\\.be/|embed/|shorts/|live/)[^&?\\n]*".toRegex()
        return regex.find(youtubeUrl)?.value
    }
}