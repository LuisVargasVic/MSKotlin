package com.rappi.moviesdb.presentation

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.rappi.moviesdb.R
import com.rappi.moviesdb.databinding.ActivityMainBinding
import com.rappi.moviesdb.domain.Movie
import com.rappi.moviesdb.presentation.MainViewModel.Companion.SORT_POPULAR
import com.rappi.moviesdb.presentation.MainViewModel.Companion.SORT_TOP
import com.rappi.moviesdb.presentation.MainViewModel.Companion.SORT_UPCOMING
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity(), MovieAdapter.MovieClickListener {

    private lateinit var mBindingMainActivity: ActivityMainBinding
    private var typeSelected = SORT_POPULAR
    private lateinit var mAdapter: MovieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBindingMainActivity = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mBindingMainActivity.viewModel = MainViewModel(application, isNetworkAvailable())
        setSupportActionBar(mBindingMainActivity.toolbar)

        val layoutManager = GridLayoutManager(this, 4)
        mBindingMainActivity.rvMovies.layoutManager = layoutManager
        mBindingMainActivity.rvMovies.setHasFixedSize(true)

        mAdapter = MovieAdapter(mBindingMainActivity.viewModel?.moviesRepository?.movies?.value, this)
        mBindingMainActivity.rvMovies.adapter = mAdapter

        mBindingMainActivity.viewModel?.moviesRepository?.movies?.observe(this, Observer {
            // size = it.size
            when (typeSelected) {
                SORT_POPULAR -> mAdapter.setMoviesList(it.createSubList(SORT_POPULAR, 0,20))
                SORT_TOP -> mAdapter.setMoviesList(it.createSubList(SORT_TOP, 0,20))
                else -> mAdapter.setMoviesList(it.createSubList(SORT_UPCOMING, 0,20))
            }
            mAdapter.notifyDataSetChanged()
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.sort, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.action_popular -> {
                if (isNetworkAvailable()) {
                    mBindingMainActivity.viewModel?.sortMovies(SORT_POPULAR, isNetworkAvailable())
                } else {
                    mBindingMainActivity.viewModel?.moviesRepository?.movies?.value?.createSubList(SORT_POPULAR, 0,20)?.let {
                        mAdapter.setMoviesList(it)
                    }
                    mAdapter.notifyDataSetChanged()
                }
                typeSelected = SORT_POPULAR
                supportActionBar?.setTitle(R.string.popular)
                true
            }
            R.id.action_top -> {
                if (isNetworkAvailable()) {
                    mBindingMainActivity.viewModel?.sortMovies(SORT_TOP, isNetworkAvailable())
                } else {
                    mBindingMainActivity.viewModel?.moviesRepository?.movies?.value?.createSubList(SORT_TOP, 0,20)?.let {
                        mAdapter.setMoviesList(it)
                    }
                    mAdapter.notifyDataSetChanged()
                }
                typeSelected = SORT_TOP
                supportActionBar?.setTitle(R.string.top_rated)
                true
            }
            R.id.action_upcoming -> {
                if (isNetworkAvailable()) {
                    mBindingMainActivity.viewModel?.sortMovies(SORT_UPCOMING, isNetworkAvailable())
                } else {
                    mBindingMainActivity.viewModel?.moviesRepository?.movies?.value?.createSubList(SORT_UPCOMING, 0,20)?.let {
                        mAdapter.setMoviesList(it)
                    }
                    mAdapter.notifyDataSetChanged()
                }
                typeSelected = SORT_UPCOMING
                supportActionBar?.setTitle(R.string.upcoming)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun movieClicked(movie: Movie?) {

    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    private fun List<Movie>.createSubList(type: String, from: Int, to: Int): List<Movie> {
        return when (type) {
            SORT_POPULAR -> this.sortedByDescending {it.popularity}.subList(from, to)
            SORT_TOP -> this.sortedByDescending {it.popularity}.subList(from, to)
            else -> {
                val dateTimeStrToLocalDateTime: (Movie) -> Date = {
                    val format: DateFormat = SimpleDateFormat("yyyy-M-d", Locale.getDefault())
                    format.parse(it.releaseDate)!!
                }
                this.sortedByDescending(dateTimeStrToLocalDateTime).subList(from, to)
            }
        }
    }
}
