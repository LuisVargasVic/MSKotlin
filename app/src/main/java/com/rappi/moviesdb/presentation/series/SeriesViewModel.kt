package com.rappi.moviesdb.presentation.series

import android.content.Context
import androidx.lifecycle.ViewModel
import com.rappi.moviesdb.data.SeriesRepository
import com.rappi.moviesdb.database.MoviesDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

/**
 * Created by Luis Vargas on 2019-07-24.
 */

class SeriesViewModel(context: Context, networkConnection: Boolean) : ViewModel() {

    private val viewModelJob = SupervisorJob()

    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val database = MoviesDatabase.getDatabase(context)
    val seriesRepository = SeriesRepository(database)

    init {
        sortSeries(SORT_POPULAR, networkConnection)
    }

    companion object {
        const val SORT_POPULAR = "popular"
        const val SORT_TOP = "top_rated"
        const val SORT_UPCOMING = "upcoming"
    }

    fun sortSeries(sort: String, networkConnection: Boolean) {
        if (networkConnection) {
            when (sort) {
                SORT_POPULAR -> {
                    viewModelScope.launch {
                        seriesRepository.refreshSeries(SORT_POPULAR)
                    }
                }
                SORT_TOP -> {
                    viewModelScope.launch {
                        seriesRepository.refreshSeries(SORT_TOP)
                    }
                }
                SORT_UPCOMING -> {
                    viewModelScope.launch {
                        seriesRepository.refreshSeries(SORT_UPCOMING)
                    }
                }
            }
        }
    }
}