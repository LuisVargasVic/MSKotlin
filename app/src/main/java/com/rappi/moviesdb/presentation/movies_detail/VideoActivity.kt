package com.rappi.moviesdb.presentation.movies_detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.rappi.moviesdb.R
import com.rappi.moviesdb.databinding.ActivityVideoBinding
import com.rappi.moviesdb.presentation.YoutubeFragment

class VideoActivity : AppCompatActivity() {

    private var myFragment: YoutubeFragment? = null
    private lateinit var viewDataBinding: ActivityVideoBinding
    private var url: String = ""

    companion object {
        const val URL = "url"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_video)
        url = if (savedInstanceState?.getSerializable(URL) != null) {
            savedInstanceState.getSerializable(URL) as String
        } else {
            intent.extras!!.getSerializable(URL) as String
        }
        setUpUI()
    }

    private fun setUpUI(){
        myFragment = YoutubeFragment.newInstance(url)
        supportFragmentManager.beginTransaction().replace(R.id.youtube_player, myFragment!!).commit()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState!!.putSerializable("url", url)
    }

    override fun onBackPressed() {
        if (myFragment != null){
            if (myFragment?.isFullScreen!!) {
                myFragment?.closeFullScreen()
            }
            else {
                super.onBackPressed()
            }
        } else {
            super.onBackPressed()
        }
    }
}
