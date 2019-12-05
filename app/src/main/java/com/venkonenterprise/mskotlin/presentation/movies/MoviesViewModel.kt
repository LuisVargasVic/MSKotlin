package com.venkonenterprise.mskotlin.presentation.movies

import android.content.Context
import androidx.lifecycle.ViewModel
import com.venkonenterprise.mskotlin.data.MoviesRepository
import com.venkonenterprise.mskotlin.database.MSDatabase
import com.venkonenterprise.mskotlin.domain.movies.Movie
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
    val categoriesSelected = mutableListOf<Int>()
    val moviesRepository = MoviesRepository(database)

    init {
        moviesRepository.page.value = 1
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

    fun sortMovies(sort: String, networkConnection: Boolean, init: Boolean) {
        if (init) {
            moviesRepository.page.value = 1
        }
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

    fun categoriesSelected(type: String, from: Int, to: Int): List<Movie> {
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
            return moviesSelected.distinct().createSubList(type, from, to)
        } else {
            return moviesRepository.movies.value?.createSubList(type, from, to) ?: mutableListOf()
        }
    }

    private fun List<Movie>.createSubList(type: String, from: Int, to: Int): List<Movie> {
        return when (type) {
            SORT_POPULAR -> {
                if (this.size >= to) {
                    this.sortedByDescending { it.popularity }.subList(from, to)
                } else {
                    this.sortedByDescending { it.popularity }
                }
            }
            SORT_TOP -> {
                if (this.size >= to) {
                    this.sortedByDescending { it.voteAverage }.subList(from, to)
                } else {
                    this.sortedByDescending { it.voteAverage }
                }
            }
            else -> {
                if (this.size >= to) {
                    this.sortedByDescending { it.releaseDate }.subList(from, to)
                } else {
                    this.sortedByDescending { it.releaseDate }
                }
            }
        }
    }
}