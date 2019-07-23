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

class MainViewModel(application: Application, networkConnection: Boolean) : AndroidViewModel(application) {

    private val viewModelJob = SupervisorJob()

    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val database = MoviesDatabase.getDatabase(application)
    val moviesRepository = MoviesRepository(database)

    init {
        sortMovies(SORT_POPULAR, networkConnection)
    }

    companion object {
        const val SORT_POPULAR = "popular"
        const val SORT_TOP = "top_rated"
        const val SORT_UPCOMING = "upcoming"
    }

    fun sortMovies(sort: String, networkConnection: Boolean) {
        if (networkConnection) {
            when (sort) {
                SORT_POPULAR -> {
                    viewModelScope.launch {
                        moviesRepository.refreshMovies(SORT_POPULAR)
                    }
                }
                SORT_TOP -> {
                    viewModelScope.launch {
                        moviesRepository.refreshMovies(SORT_TOP)
                    }
                }
                SORT_UPCOMING -> {
                    viewModelScope.launch {
                        moviesRepository.refreshMovies(SORT_UPCOMING)
                    }
                }
            }
        }
    }
}