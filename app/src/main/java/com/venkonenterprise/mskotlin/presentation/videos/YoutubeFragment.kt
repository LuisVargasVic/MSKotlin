package com.venkonenterprise.mskotlin.presentation.videos

import android.os.Bundle
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerSupportFragment
import com.venkonenterprise.mskotlin.BuildConfig

/**
 * Created by Luis Vargas on 2019-07-28.
 */

class YoutubeFragment : YouTubePlayerSupportFragment() {
    private var activePlayer: YouTubePlayer? = null
    var isFullScreen: Boolean = false
        private set

    private fun init() {
        initialize(BuildConfig.YT_API_KEY, object : YouTubePlayer.OnInitializedListener {

            override fun onInitializationFailure(arg0: YouTubePlayer.Provider, arg1: YouTubeInitializationResult) {}

            override fun onInitializationSuccess(provider: YouTubePlayer.Provider, player: YouTubePlayer, wasRestored: Boolean) {
                if (!wasRestored) {
                    activePlayer = player
                    activePlayer!!.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT)
                    activePlayer!!.setOnFullscreenListener { _isFullScreen -> isFullScreen = _isFullScreen }
                    activePlayer!!.loadVideo(arguments!!.getString("url"), 0)
                }
            }
        })
    }

    fun closeFullScreen() {
        activePlayer!!.setFullscreen(false)
    }

    companion object {

        fun newInstance(url: String): YoutubeFragment {
            val playerYouTubeFrag = YoutubeFragment()

            val bundle = Bundle()
            bundle.putString("url", url)

            playerYouTubeFrag.arguments = bundle

            playerYouTubeFrag.init()

            return playerYouTubeFrag
        }
    }
}