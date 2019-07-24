package com.rappi.moviesdb.presentation.movies

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.rappi.moviesdb.R
import com.rappi.moviesdb.databinding.FragmentMoviesBinding
import com.rappi.moviesdb.domain.Movie
import com.rappi.moviesdb.presentation.MainActivity
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class MoviesFragment : Fragment(), MoviesAdapter.MovieClickListener {

    private lateinit var viewDataBinding: FragmentMoviesBinding
    private var typeSelected = MoviesViewModel.SORT_POPULAR
    private lateinit var mAdapter: MoviesAdapter
    private var title = R.string.popular

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    companion object {
        const val MOVIE_KEY = "movie"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewDataBinding = FragmentMoviesBinding.inflate(inflater)
        return viewDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (context as MainActivity).supportActionBar?.setTitle(title)
        viewDataBinding.viewModel =
            MoviesViewModel(context!!, isNetworkAvailable())

        val layoutManager = GridLayoutManager(context, 4)
        viewDataBinding.rvMovies.layoutManager = layoutManager
        viewDataBinding.rvMovies.setHasFixedSize(true)

        mAdapter = MoviesAdapter(
            viewDataBinding.viewModel?.moviesRepository?.movies?.value,
            this
        )
        viewDataBinding.rvMovies.adapter = mAdapter

        viewDataBinding.viewModel?.moviesRepository?.movies?.observe(this, Observer {
            // size = it.size
            when (typeSelected) {
                MoviesViewModel.SORT_POPULAR -> mAdapter.setMoviesList(it.createSubList(
                    MoviesViewModel.SORT_POPULAR, 0,20))
                MoviesViewModel.SORT_TOP -> mAdapter.setMoviesList(it.createSubList(
                    MoviesViewModel.SORT_TOP, 0,20))
                else -> mAdapter.setMoviesList(it.createSubList(MoviesViewModel.SORT_UPCOMING, 0,20))
            }
            mAdapter.notifyDataSetChanged()
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.action_popular -> {
                if (isNetworkAvailable()) {
                    viewDataBinding.viewModel?.sortMovies(MoviesViewModel.SORT_POPULAR, isNetworkAvailable())
                } else {
                    viewDataBinding.viewModel?.moviesRepository?.movies?.value?.createSubList(MoviesViewModel.SORT_POPULAR, 0,20)?.let {
                        mAdapter.setMoviesList(it)
                    }
                    mAdapter.notifyDataSetChanged()
                }
                typeSelected = MoviesViewModel.SORT_POPULAR
                (context as MainActivity).supportActionBar?.setTitle(R.string.popular)
                title = R.string.popular
                true
            }
            R.id.action_top -> {
                if (isNetworkAvailable()) {
                    viewDataBinding.viewModel?.sortMovies(MoviesViewModel.SORT_TOP, isNetworkAvailable())
                } else {
                    viewDataBinding.viewModel?.moviesRepository?.movies?.value?.createSubList(MoviesViewModel.SORT_TOP, 0,20)?.let {
                        mAdapter.setMoviesList(it)
                    }
                    mAdapter.notifyDataSetChanged()
                }
                typeSelected = MoviesViewModel.SORT_TOP
                (context as MainActivity).supportActionBar?.setTitle(R.string.top_rated)
                title = R.string.top_rated
                true
            }
            R.id.action_upcoming -> {
                if (isNetworkAvailable()) {
                    viewDataBinding.viewModel?.sortMovies(MoviesViewModel.SORT_UPCOMING, isNetworkAvailable())
                } else {
                    viewDataBinding.viewModel?.moviesRepository?.movies?.value?.createSubList(MoviesViewModel.SORT_UPCOMING, 0,20)?.let {
                        mAdapter.setMoviesList(it)
                    }
                    mAdapter.notifyDataSetChanged()
                }
                typeSelected = MoviesViewModel.SORT_UPCOMING
                (context as MainActivity).supportActionBar?.setTitle(R.string.upcoming)
                title = R.string.upcoming
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun movieClicked(movie: Movie?) {
        val bundle = bundleOf(MOVIE_KEY to movie)

        view?.findNavController()?.navigate(
            R.id.action_movies_to_movie_detail,
            bundle)
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = (context as MainActivity).getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    private fun List<Movie>.createSubList(type: String, from: Int, to: Int): List<Movie> {
        if (this.size >= to) {
            return when (type) {
                MoviesViewModel.SORT_POPULAR -> this.sortedByDescending {it.popularity}.subList(from, to)
                MoviesViewModel.SORT_TOP -> this.sortedByDescending {it.voteAverage}.subList(from, to)
                else -> {
                    val dateTimeStrToLocalDateTime: (Movie) -> Date = {
                        val format: DateFormat = SimpleDateFormat("yyyy-M-d", Locale.getDefault())
                        format.parse(it.releaseDate)!!
                    }
                    this.sortedByDescending(dateTimeStrToLocalDateTime).subList(from, to)
                }
            }
        }
        return this
    }

}
