package com.rappi.moviesdb.presentation.series

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.rappi.moviesdb.R
import com.rappi.moviesdb.databinding.MovieItemBinding
import com.rappi.moviesdb.domain.Serie
import com.squareup.picasso.Picasso

/**
 * Created by Luis Vargas on 2019-07-24.
 */

class SeriesAdapter(var mSerieList: List<Serie>?, var mSerieClickListener: SerieClickListener) : RecyclerView.Adapter<SeriesAdapter.SeriesViewHolder>() {

    interface SerieClickListener {
        fun serieClicked(serie: Serie?)
    }

    fun setSeriesList(serieList: List<Serie>) {
        mSerieList = serieList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeriesViewHolder {
        val dataBinding: MovieItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.movie_item,
            parent,
            false)
        return SeriesViewHolder(dataBinding, mSerieClickListener)
    }

    override fun onBindViewHolder(holder: SeriesViewHolder, position: Int) {
        holder.bind(mSerieList?.get(position))
    }

    override fun getItemCount(): Int {
        return mSerieList?.size ?: 0
    }

    class SeriesViewHolder(
        private val movieItemBinding: MovieItemBinding,
        private val mSerieClickListener: SerieClickListener
    ) : RecyclerView.ViewHolder(movieItemBinding.root), View.OnClickListener {

        var mSerie: Serie? = null

        init {
            movieItemBinding.ivMovieImage.setOnClickListener(this)
        }

        fun bind(serie: Serie?) {
            mSerie = serie
            Picasso
                .get()
                .load(Uri.parse(BASE_URL + serie?.posterPath))
                .into(movieItemBinding.ivMovieImage)
            movieItemBinding.tvMovieAverage.text = serie?.voteAverage.toString()
        }

        override fun onClick(view: View) {
            mSerieClickListener.serieClicked(mSerie)
        }
    }

    companion object {
        private const val BASE_URL = "https://image.tmdb.org/t/p/w185"
    }
}