package com.rappi.moviesdb.presentation.movies_detail

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.rappi.moviesdb.R
import com.rappi.moviesdb.databinding.VideoItemBinding
import com.rappi.moviesdb.domain.Video
import com.squareup.picasso.Picasso

/**
 * Created by Luis Vargas on 2019-07-28.
 */

class VideosAdapter(val context: Context, var mMoviesList: List<Video>?) : RecyclerView.Adapter<VideosAdapter.VideosViewHolder>() {

    init {
        setHasStableIds(true)
    }

    fun setVideosList(movieList: List<Video>) {
        mMoviesList = movieList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideosViewHolder {
        val dataBinding: VideoItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.video_item,
            parent,
            false)
        return VideosViewHolder(dataBinding, context)
    }

    override fun onBindViewHolder(holder: VideosViewHolder, position: Int) {
        holder.bind(mMoviesList?.get(position))
    }

    override fun getItemCount(): Int {
        return mMoviesList?.size ?: 0
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    class VideosViewHolder(
        private val videoItemBinding: VideoItemBinding,
        private val context: Context
    ): RecyclerView.ViewHolder(videoItemBinding.root) {

        fun bind(video: Video?) {
            val videoThumbUrl = "https://img.youtube.com/vi/${video?.key}/0.jpg"
            Picasso.get()
                .load(videoThumbUrl)
                .error(R.drawable.ic_panorama)
                .into(videoItemBinding.video)
            videoItemBinding.video.setOnClickListener {
                val intent = Intent(context, VideoActivity::class.java)
                intent.putExtra("url", video?.key)
                startActivity(context, intent, null)
            }
        }
    }
}