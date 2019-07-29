package com.rappi.moviesdb.presentation.movies_detail

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.rappi.moviesdb.databinding.FragmentMovieVideosBinding
import com.rappi.moviesdb.domain.movies.Movie
import com.rappi.moviesdb.presentation.MainActivity
import com.rappi.moviesdb.presentation.movies.MoviesFragment

class MovieVideosFragment : Fragment() {

    private lateinit var viewDataBinding: FragmentMovieVideosBinding
    private lateinit var mAdapter: VideosAdapter
    private lateinit var movie: Movie

    companion object {
        @JvmStatic
        fun newInstance(): MovieVideosFragment {
            val args = Bundle()
            val fragment = MovieVideosFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewDataBinding = FragmentMovieVideosBinding.inflate(inflater)
        return viewDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        movie = if (savedInstanceState != null) {
            savedInstanceState.getSerializable(MoviesFragment.MOVIE_KEY) as Movie
        } else {
            arguments?.getSerializable(MoviesFragment.MOVIE_KEY) as Movie
        }
        viewDataBinding.viewModel = VideosViewModel(movie.id, context!!, isNetworkAvailable())

        val layoutManager = LinearLayoutManager(context)
        viewDataBinding.rvVideos.layoutManager = layoutManager
        viewDataBinding.rvVideos.setHasFixedSize(true)

        mAdapter = VideosAdapter(
            context!!,
            viewDataBinding.viewModel?.videosSelected(movie.id)
        )
        viewDataBinding.rvVideos.adapter = mAdapter

        viewDataBinding.viewModel?.moviesRepository?.videos?.observe(this, Observer {
            viewDataBinding.viewModel?.videosSelected(movie.id)?.let { videos ->
                    mAdapter.setVideosList(videos)
            }
            mAdapter.notifyDataSetChanged()
        })
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = (context as MainActivity).getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable(MoviesFragment.MOVIE_KEY, movie)
    }
}
