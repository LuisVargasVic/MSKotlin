package com.rappi.moviesdb.presentation.movies

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.rappi.moviesdb.R
import com.rappi.moviesdb.databinding.FragmentMovieDetailBinding
import com.rappi.moviesdb.domain.Movie
import com.rappi.moviesdb.presentation.MainActivity
import com.rappi.moviesdb.presentation.movies.MoviesFragment.Companion.MOVIE_KEY
import com.squareup.picasso.Picasso

class MovieDetailFragment : Fragment() {

    private lateinit var viewDataBinding: FragmentMovieDetailBinding
    private lateinit var movie: Movie

    companion object {
        private const val BASE_URL_SMALL = "https://image.tmdb.org/t/p/w342"
        private const val BASE_URL = "https://image.tmdb.org/t/p/w780"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewDataBinding = FragmentMovieDetailBinding.inflate(inflater)
        return viewDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        movie = if (savedInstanceState != null) {
            savedInstanceState.getSerializable(MOVIE_KEY) as Movie
        } else {
            arguments?.getSerializable(MOVIE_KEY) as Movie
        }
        setUpUI()
    }

    fun setUpUI() {
        assert(movie != null)
        Picasso
            .get()
            .load(Uri.parse(BASE_URL + movie.backdropPath))
            .error(R.drawable.ic_panorama)
            .into(viewDataBinding.ivDetailMovieBackdrop)
        Picasso
            .get()
            .load(Uri.parse(BASE_URL_SMALL + movie.posterPath))
            .error(R.drawable.ic_photo)
            .into(viewDataBinding.ivDetailMovieImage)
        (context as MainActivity).supportActionBar?.title = movie.title
        viewDataBinding.tvDetailMovieVoteAverage.text = movie.voteAverage.toString()
        viewDataBinding.tvDetailMovieReleaseDate.text = movie.releaseDate
        viewDataBinding.tvDetailMovieVoteCount.text = movie.voteCount.toString()
        viewDataBinding.tvDetailMoviePopularity.text = movie.popularity.toString()
        viewDataBinding.tvDetailMovieOriginalLanguage.text = movie.originalLanguage
        viewDataBinding.tvDetailMovieOverview.text = movie.overview
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable(MOVIE_KEY, movie)
    }


}
