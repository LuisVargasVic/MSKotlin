package com.rappi.moviesdb.presentation.movies

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.rappi.moviesdb.R
import com.rappi.moviesdb.databinding.MovieItemBinding
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
    }

    fun setMoviesList(movieList: List<Movie>) {
        mMoviesList = movieList
    }

    fun setTypeSelected(typeSelected: String) {
        mTypeSelected = typeSelected
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesViewHolder {
        val dataBinding: MovieItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.movie_item,
            parent,
            false)
        return MoviesViewHolder(dataBinding, mMovieClickListener, mTypeSelected)
    }

    override fun onBindViewHolder(holder: MoviesViewHolder, position: Int) {
        holder.bind(mMoviesList?.get(position))
        if (position == itemCount - 1) {
            mMovieClickListener.bottom()
        }
    }

    override fun getItemCount(): Int {
        return mMoviesList?.size ?: 0
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    class MoviesViewHolder(
        private val movieItemBinding: MovieItemBinding,
        private val mMovieClickListener: MovieClickListener,
        private val mTypeSelected: String
    ) : RecyclerView.ViewHolder(movieItemBinding.root), View.OnClickListener {

        var mMovie: Movie? = null

        init {
            movieItemBinding.ivMovieImage.setOnClickListener(this)
        }

        fun bind(movie: Movie?) {
            mMovie = movie
            Picasso
                .get()
                .load(Uri.parse(BASE_URL + movie?.posterPath))
                .error(R.drawable.ic_photo)
                .into(movieItemBinding.ivMovieImage)
            when (mTypeSelected) {
                SORT_POPULAR -> movieItemBinding.tvMovieAverage.text = movie?.popularity.toString()
                SORT_TOP -> movieItemBinding.tvMovieAverage.text = movie?.voteAverage.toString()
                else -> movieItemBinding.tvMovieAverage.text = movie?.releaseDate.toString()
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