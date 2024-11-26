package com.example.youtubeplayerimplementation

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val youTubePlayerView = findViewById<YouTubePlayerView>(R.id.youtube_player_view)
        val videoUrlEditText = findViewById<EditText>(R.id.videoUrlEt)
        val loadBtn = findViewById<Button>(R.id.loadVideo)

        // Lifecycle management
        lifecycle.addObserver(youTubePlayerView)


        youTubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                loadBtn.setOnClickListener {
                    val videoUrl = videoUrlEditText.text.toString().trim()
                    val videoId = extractVideoId(videoUrl)

                    if (videoId.isNullOrEmpty()) {
                        Toast.makeText(this@MainActivity, "Invalid YouTube URL", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@MainActivity, "Loading Video: $videoId", Toast.LENGTH_SHORT).show()
                        youTubePlayer.loadVideo(videoId, 0f)
                    }
                }
            }
        })
    }

    private fun extractVideoId(youtubeUrl: String): String? {
        // Extended regex to handle various YouTube URL formats, including /live/
        val regex = "(?<=v=|youtu\\.be/|embed/|shorts/|live/)[^&?\\n]*".toRegex()

        // Find and return the match or null if no match is found
        return regex.find(youtubeUrl)?.value
    }
}
