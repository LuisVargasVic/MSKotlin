package com.rappi.moviesdb.presentation.movies

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.rappi.moviesdb.R
import com.rappi.moviesdb.databinding.MovieItemBinding
import com.rappi.moviesdb.domain.Movie
import com.squareup.picasso.Picasso

/**
 * Created by Luis Vargas on 2019-07-22.
 */

class MoviesAdapter(var mMoviesList: List<Movie>?, var mMovieClickListener: MovieClickListener) : RecyclerView.Adapter<MoviesAdapter.MoviesViewHolder>() {

    interface MovieClickListener {
        fun movieClicked(movie: Movie?)
    }

    fun setMoviesList(movieList: List<Movie>) {
        mMoviesList = movieList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesViewHolder {
        val dataBinding: MovieItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.movie_item,
            parent,
            false)
        return MoviesViewHolder(dataBinding, mMovieClickListener)
    }

    override fun onBindViewHolder(holder: MoviesViewHolder, position: Int) {
        holder.bind(mMoviesList?.get(position))
    }

    override fun getItemCount(): Int {
        return mMoviesList?.size ?: 0
    }

    class MoviesViewHolder(
        private val movieItemBinding: MovieItemBinding,
        private val mMovieClickListener: MovieClickListener
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
            movieItemBinding.tvMovieAverage.text = movie?.voteAverage.toString()
        }

        override fun onClick(view: View) {
            mMovieClickListener.movieClicked(mMovie)
        }
    }

    companion object {
        private const val BASE_URL = "https://image.tmdb.org/t/p/w185"
    }
}