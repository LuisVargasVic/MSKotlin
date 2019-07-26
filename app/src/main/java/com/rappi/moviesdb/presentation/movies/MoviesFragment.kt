package com.rappi.moviesdb.presentation.movies

import android.content.Context
import android.content.DialogInterface
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.rappi.moviesdb.R
import com.rappi.moviesdb.databinding.FragmentMoviesBinding
import com.rappi.moviesdb.domain.movies.Movie
import com.rappi.moviesdb.presentation.MainActivity
import com.rappi.moviesdb.remote.ApiStatus

class MoviesFragment : Fragment(), MoviesAdapter.MovieClickListener {

    private lateinit var viewDataBinding: FragmentMoviesBinding
    private var typeSelected = MoviesViewModel.SORT_POPULAR
    private lateinit var mAdapter: MoviesAdapter
    private var title = R.string.popular_movies

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
            this,
            typeSelected
        )
        viewDataBinding.rvMovies.adapter = mAdapter

        viewDataBinding.viewModel?.moviesRepository?.movies?.observe(this, Observer {
            // size = it.size
            if (viewDataBinding.viewModel?.moviesRepository?.page?.value ?: 1 == 1) {
                when (typeSelected) {
                    MoviesViewModel.SORT_POPULAR -> {
                        mAdapter.setTypeSelected(MoviesViewModel.SORT_POPULAR)
                        viewDataBinding.viewModel?.categoriesSelected(MoviesViewModel.SORT_POPULAR, 0, 20)?.let {
                            mAdapter.setMoviesList(it)
                        }
                    }
                    MoviesViewModel.SORT_TOP -> {
                        mAdapter.setTypeSelected(MoviesViewModel.SORT_TOP)
                        viewDataBinding.viewModel?.categoriesSelected(MoviesViewModel.SORT_TOP, 0, 20)?.let {
                            mAdapter.setMoviesList(it)
                        }
                    }
                    else -> {
                        mAdapter.setTypeSelected(MoviesViewModel.SORT_UPCOMING)
                        viewDataBinding.viewModel?.categoriesSelected(MoviesViewModel.SORT_UPCOMING, 0, 20)?.let {
                            mAdapter.setMoviesList(it)
                        }
                    }
                }
                mAdapter.notifyDataSetChanged()
            } else {
                Log.wtf("page", viewDataBinding.viewModel?.moviesRepository?.page?.value.toString())
                val to = 20 * (viewDataBinding.viewModel?.moviesRepository?.page?.value ?: 0)
                val oldSize = mAdapter.mMoviesList?.size
                val moviesSelected = viewDataBinding.viewModel?.categoriesSelected(typeSelected, 0, to)
                moviesSelected?.let {
                    mAdapter.setMoviesList(it)
                }
                Log.wtf("size", mAdapter.mMoviesList?.size.toString())
                if (((oldSize ?: 0) + 20) >= mAdapter.mMoviesList?.size ?: 0) {
                    viewDataBinding.rvMovies.post {
                        mAdapter.notifyItemRangeInserted(
                            (mAdapter.mMoviesList?.size ?: 0) - 20,
                            mAdapter.mMoviesList?.size ?: 0
                        )
                    }
                } else {
                    viewDataBinding.viewModel?.moviesRepository?.page?.value?.plus(1)
                    if (isNetworkAvailable()) {
                        viewDataBinding.viewModel?.sortMovies(typeSelected, isNetworkAvailable(), false)
                    }
                }
            }
        })

        viewDataBinding.viewModel?.moviesRepository?.moviesCategories?.observe(this, Observer {

        })

        viewDataBinding.viewModel?.moviesRepository?.categories?.observe(this, Observer {

        })

        viewDataBinding.viewModel?.moviesRepository?.page?.observe(this, Observer {

        })

        viewDataBinding.viewModel?.moviesRepository?.apiStatus?.observe(this, Observer {
            if (it == ApiStatus.LOADING) {
                viewDataBinding.pbLoad.visibility = View.VISIBLE
                viewDataBinding.pbLoadMore.visibility = View.GONE
            } else if (it == ApiStatus.APPENDING)  {
                viewDataBinding.pbLoadMore.visibility = View.VISIBLE
                viewDataBinding.pbLoad.visibility = View.GONE
            } else {
                viewDataBinding.pbLoadMore.visibility = View.GONE
                viewDataBinding.pbLoad.visibility = View.GONE
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        viewDataBinding.viewModel?.categoriesSelected?.clear()
        return when(item.itemId) {
            R.id.action_categories -> {
                val view = layoutInflater.inflate(R.layout.categories, null)
                val chipGroup = view.findViewById<ChipGroup>(R.id.chip_group)
                for (category in viewDataBinding.viewModel?.moviesRepository?.categories?.value ?: mutableListOf()) {
                    val chip = Chip(chipGroup.context, null, R.style.Choice)
                    chip.isCheckable = true
                    chip.setOnCheckedChangeListener { _, isChecked ->
                        if (isChecked) {
                            viewDataBinding.viewModel?.select(category.id)
                        } else {
                            viewDataBinding.viewModel?.deselect(category.id)
                        }
                    }
                    chip.setOnClickListener {}
                    chip.text = category.name
                    chipGroup.addView(chip)
                }
                val alertDialog = AlertDialog.Builder(context!!)
                alertDialog.setView(view)
                alertDialog.setPositiveButton("UPDATE") { dialogInterface: DialogInterface, i: Int ->
                    if (isNetworkAvailable()) {
                        viewDataBinding.viewModel?.sortMovies(typeSelected, isNetworkAvailable(), true)
                    }
                    val movies = viewDataBinding.viewModel?.categoriesSelected(typeSelected, 0, 20)!!
                    mAdapter = MoviesAdapter(
                        movies,
                        this,
                        typeSelected
                    )
                    viewDataBinding.rvMovies.adapter = mAdapter
                    mAdapter.notifyDataSetChanged()
                    dialogInterface.dismiss()
                }
                alertDialog.show()
                true
            }
            R.id.action_popular -> {
                if (isNetworkAvailable()) {
                    viewDataBinding.viewModel?.sortMovies(MoviesViewModel.SORT_POPULAR, isNetworkAvailable(), true)
                }
                typeSelected = MoviesViewModel.SORT_POPULAR
                mAdapter = MoviesAdapter(
                    viewDataBinding.viewModel?.categoriesSelected(typeSelected, 0,20),
                    this,
                    typeSelected
                )
                viewDataBinding.rvMovies.adapter = mAdapter
                mAdapter.notifyDataSetChanged()
                (context as MainActivity).supportActionBar?.setTitle(R.string.popular_movies)
                title = R.string.popular_movies
                true
            }
            R.id.action_top -> {
                if (isNetworkAvailable()) {
                    viewDataBinding.viewModel?.sortMovies(MoviesViewModel.SORT_TOP, isNetworkAvailable(), true)
                }
                typeSelected = MoviesViewModel.SORT_TOP
                mAdapter = MoviesAdapter(
                    viewDataBinding.viewModel?.categoriesSelected(typeSelected, 0,20),
                    this,
                    typeSelected
                )
                viewDataBinding.rvMovies.adapter = mAdapter
                mAdapter.notifyDataSetChanged()
                (context as MainActivity).supportActionBar?.setTitle(R.string.top_rated_movies)
                title = R.string.top_rated_movies
                true
            }
            R.id.action_upcoming -> {
                if (isNetworkAvailable()) {
                    viewDataBinding.viewModel?.sortMovies(MoviesViewModel.SORT_UPCOMING, isNetworkAvailable(), true)
                }
                typeSelected = MoviesViewModel.SORT_UPCOMING
                mAdapter = MoviesAdapter(
                    viewDataBinding.viewModel?.categoriesSelected(typeSelected, 0,20),
                    this,
                    typeSelected
                )
                viewDataBinding.rvMovies.adapter = mAdapter
                mAdapter.notifyDataSetChanged()
                (context as MainActivity).supportActionBar?.setTitle(R.string.upcoming_movies)
                title = R.string.upcoming_movies
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

    override fun bottom() {
        if (isNetworkAvailable()) {
            viewDataBinding.viewModel?.sortMovies(typeSelected, isNetworkAvailable(), false)
        } else {
            val oldSize = mAdapter.mMoviesList?.size
            if (oldSize ?: 0 >= 20) {
                val moviesSelected = viewDataBinding.viewModel?.categoriesSelected(typeSelected, 0, (oldSize ?: 0) + 20)
                moviesSelected?.let {
                    mAdapter.setMoviesList(it)
                }
                if (((oldSize ?: 0) + 20) == mAdapter.mMoviesList?.size) {
                    viewDataBinding.rvMovies.post {
                        mAdapter.notifyItemRangeInserted(
                            (mAdapter.mMoviesList?.size ?: 0) - 20,
                            mAdapter.mMoviesList?.size ?: 0
                        )
                    }
                }
            }
        }
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = (context as MainActivity).getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

}
