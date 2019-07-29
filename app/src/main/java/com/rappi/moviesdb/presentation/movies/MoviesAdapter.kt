package com.rappi.moviesdb.presentation.movies

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.rappi.moviesdb.R
import com.rappi.moviesdb.databinding.MsItemBinding
import com.rappi.moviesdb.domain.movies.Movie
import com.rappi.moviesdb.presentation.movies.MoviesViewModel.Companion.SORT_POPULAR
import com.rappi.moviesdb.presentation.movies.MoviesViewModel.Companion.SORT_TOP
import com.squareup.picasso.Picasso

/**
 * Created by Luis Vargas on 2019-07-22.
 */

class MoviesAdapter(var mMoviesList: List<Movie>?, var mMovieClickListener: MovieClickListener, var mTypeSelected: String) : RecyclerView.Adapter<MoviesAdapter.MoviesViewHolder>() {

    init {
        setHasStableIds(true)
    }

    interface MovieClickListener {
        fun movieClicked(movie: Movie?)
        fun bottom()
        fun hide()
    }

    fun setMoviesList(movieList: List<Movie>) {
        mMoviesList = movieList
    }

    fun setTypeSelected(typeSelected: String) {
        mTypeSelected = typeSelected
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesViewHolder {
        val dataBinding: MsItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.ms_item,
            parent,
            false)
        return MoviesViewHolder(dataBinding, mMovieClickListener, mTypeSelected)
    }

    override fun onBindViewHolder(holder: MoviesViewHolder, position: Int) {
        holder.bind(mMoviesList?.get(position))
        if (position == itemCount - 1) {
            mMovieClickListener.bottom()
        } else {
            mMovieClickListener.hide()
        }
    }

    override fun getItemCount(): Int {
        return mMoviesList?.size ?: 0
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    class MoviesViewHolder(
        private val msItemBinding: MsItemBinding,
        private val mMovieClickListener: MovieClickListener,
        private val mTypeSelected: String
    ) : RecyclerView.ViewHolder(msItemBinding.root), View.OnClickListener {

        var mMovie: Movie? = null

        init {
            msItemBinding.ivMovieImage.setOnClickListener(this)
        }

        fun bind(movie: Movie?) {
            mMovie = movie
            Picasso
                .get()
                .load(Uri.parse(BASE_URL + movie?.posterPath))
                .error(R.drawable.ic_photo)
                .into(msItemBinding.ivMovieImage)
            when (mTypeSelected) {
                SORT_POPULAR -> msItemBinding.tvMovieAverage.text = movie?.popularity.toString()
                SORT_TOP -> msItemBinding.tvMovieAverage.text = movie?.voteAverage.toString()
                else -> msItemBinding.tvMovieAverage.text = movie?.releaseDate.toString()
            }
        }

        override fun onClick(view: View) {
            mMovieClickListener.movieClicked(mMovie)
        }
    }

    companion object {
        private const val BASE_URL = "https://image.tmdb.org/t/p/w185"
    }
}