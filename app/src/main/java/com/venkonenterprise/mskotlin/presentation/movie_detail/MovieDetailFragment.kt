package com.venkonenterprise.mskotlin.presentation.movie_detail

import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.venkonenterprise.mskotlin.R
import com.venkonenterprise.mskotlin.databinding.FragmentMovieDetailBinding
import com.venkonenterprise.mskotlin.domain.movies.Movie
import com.venkonenterprise.mskotlin.presentation.movies.MoviesFragment.Companion.MOVIE_KEY
import com.squareup.picasso.Picasso

class MovieDetailFragment : Fragment() {

    private lateinit var viewDataBinding: FragmentMovieDetailBinding
    private lateinit var movie: Movie

    companion object {
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
        if(resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE){
            viewDataBinding.ivDetailMovieBackdrop.visibility = View.GONE
        } else {
            Picasso
                .get()
                .load(Uri.parse(BASE_URL + movie.backdropPath))
                .error(R.drawable.ic_panorama)
                .into(viewDataBinding.ivDetailMovieBackdrop)
        }
        val adapter = MoviePagerAdapter(childFragmentManager, movie)
        adapter.addFragment(MovieSynopsisFragment.newInstance(), getString(R.string.synopsis))
        adapter.addFragment(MovieVideosFragment.newInstance(), getString(R.string.videos))
        viewDataBinding.viewPager.adapter = adapter
        viewDataBinding.tabLayout.setupWithViewPager(viewDataBinding.viewPager)
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable(MOVIE_KEY, movie)
    }

}
