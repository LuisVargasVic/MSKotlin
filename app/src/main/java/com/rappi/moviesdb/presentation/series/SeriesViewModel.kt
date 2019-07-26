package com.rappi.moviesdb.presentation.series

import android.content.Context
import androidx.lifecycle.ViewModel
import com.rappi.moviesdb.data.SeriesRepository
import com.rappi.moviesdb.database.MSDatabase
import com.rappi.moviesdb.domain.series.Serie
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

    private val database = MSDatabase.getDatabase(context)
    val seriesRepository = SeriesRepository(database)
    val categoriesSelected = mutableListOf<Int>()

    init {
        if (networkConnection) {
            viewModelScope.launch {
                seriesRepository.refreshCategories()
            }
        }
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

    fun select(category: Int) {
        categoriesSelected.add(category)
    }

    fun deselect(category: Int) {
        categoriesSelected.remove(category)
    }

    fun categoriesSelected(): List<Serie> {
        if (categoriesSelected.isNotEmpty()) {
            val moviesSelected = mutableListOf<Serie>()
            seriesRepository.seriesCategories.value?.forEach { movieCategory ->
                if (categoriesSelected.contains(movieCategory.genreId)) {
                    seriesRepository.series.value?.find {
                        it.id == movieCategory.serieId
                    }?.let {
                        moviesSelected.add(it)
                    }
                }
            }
            categoriesSelected.clear()
            return moviesSelected.distinct()
        } else {
            return seriesRepository.series.value!!
        }
    }
}