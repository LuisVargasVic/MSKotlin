package com.rappi.moviesdb.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.rappi.moviesdb.data.MoviesRepository
import com.rappi.moviesdb.database.MoviesDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

/**
 * Created by Luis Vargas on 2019-07-22.
 */
class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val viewModelJob = SupervisorJob()

    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val database = MoviesDatabase.getDatabase(application)
    private val moviesRepository = MoviesRepository(database)

    init {
        viewModelScope.launch {
            moviesRepository.refreshMovies(SORT_POPULAR)
        }
    }

    val movies = moviesRepository.movies

    companion object {
        const val SORT_POPULAR = "popular"
        const val SORT_TOP = "top_rated"
    }

}