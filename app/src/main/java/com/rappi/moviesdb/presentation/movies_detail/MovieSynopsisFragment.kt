package com.rappi.moviesdb.presentation.movies_detail

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.rappi.moviesdb.R
import com.rappi.moviesdb.databinding.FragmentMovieSynopsisBinding
import com.rappi.moviesdb.domain.movies.Movie
import com.rappi.moviesdb.presentation.MainActivity
import com.rappi.moviesdb.presentation.movies.MoviesFragment
import com.squareup.picasso.Picasso


class MovieSynopsisFragment : Fragment() {

    private lateinit var viewDataBinding: FragmentMovieSynopsisBinding
    private lateinit var movie: Movie

    companion object {
        private const val BASE_URL_SMALL = "https://image.tmdb.org/t/p/w342"

        @JvmStatic
        fun newInstance(): MovieSynopsisFragment {
            val args = Bundle()
            val fragment = MovieSynopsisFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewDataBinding = FragmentMovieSynopsisBinding.inflate(inflater)
        return viewDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        movie = if (savedInstanceState != null) {
            savedInstanceState.getSerializable(MoviesFragment.MOVIE_KEY) as Movie
        } else {
            arguments?.getSerializable(MoviesFragment.MOVIE_KEY) as Movie
        }
        super.onViewCreated(view, savedInstanceState)
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
        outState.putSerializable(MoviesFragment.MOVIE_KEY, movie)
    }

}
