package com.rappi.moviesdb.presentation.movies

import android.content.Context
import androidx.lifecycle.ViewModel
import com.rappi.moviesdb.data.MoviesRepository
import com.rappi.moviesdb.database.MSDatabase
import com.rappi.moviesdb.domain.movies.Movie
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

/**
 * Created by Luis Vargas on 2019-07-22.
 */

class MoviesViewModel(context: Context, networkConnection: Boolean) : ViewModel() {

    private val viewModelJob = SupervisorJob()

    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val database = MSDatabase.getDatabase(context)
    val moviesRepository = MoviesRepository(database)
    val categoriesSelected = mutableListOf<Int>()

    init {
        if (networkConnection) {
            viewModelScope.launch {
                moviesRepository.refreshCategories()
            }
        }
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

    fun select(category: Int) {
        categoriesSelected.add(category)
    }

    fun deselect(category: Int) {
        categoriesSelected.remove(category)
    }

    fun categoriesSelected(): List<Movie> {
        if (categoriesSelected.isNotEmpty()) {
            val moviesSelected = mutableListOf<Movie>()
            moviesRepository.moviesCategories.value?.forEach { movieCategory ->
                if (categoriesSelected.contains(movieCategory.genreId)) {
                    moviesRepository.movies.value?.find {
                        it.id == movieCategory.movieId
                    }?.let {
                        moviesSelected.add(it)
                    }
                }
            }
            categoriesSelected.clear()
            return moviesSelected.distinct()
        } else {
            return moviesRepository.movies.value!!
        }
    }
}