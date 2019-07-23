package com.rappi.moviesdb.presentation

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.rappi.moviesdb.R
import com.rappi.moviesdb.databinding.ActivityMainBinding
import com.rappi.moviesdb.domain.Movie

class MainActivity : AppCompatActivity(), MovieAdapter.MovieClickListener {


    private lateinit var mBindingMainActivity: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBindingMainActivity = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mBindingMainActivity.viewModel = MainViewModel(application)

        val layoutManager = GridLayoutManager(this, 4)
        mBindingMainActivity.rvMovies.layoutManager = layoutManager
        mBindingMainActivity.rvMovies.setHasFixedSize(true)

        mBindingMainActivity.rvMovies.visibility = View.VISIBLE

        val mAdapter = MovieAdapter(mBindingMainActivity.viewModel?.movies?.value, this)
        mBindingMainActivity.rvMovies.adapter = mAdapter
    }

    override fun movieClicked(movie: Movie?) {

    }
}
