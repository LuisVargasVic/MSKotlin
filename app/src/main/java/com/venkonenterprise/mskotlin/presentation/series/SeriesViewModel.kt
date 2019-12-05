package com.venkonenterprise.mskotlin.presentation.series

import android.content.Context
import androidx.lifecycle.ViewModel
import com.venkonenterprise.mskotlin.data.SeriesRepository
import com.venkonenterprise.mskotlin.database.MSDatabase
import com.venkonenterprise.mskotlin.domain.series.Serie
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
        seriesRepository.page.value = 1
        if (networkConnection) {
            viewModelScope.launch {
                seriesRepository.refreshCategories()
            }
        }
    }

    companion object {
        const val SORT_POPULAR = "popular"
        const val SORT_TOP = "top_rated"
        const val SORT_UPCOMING = "airing_today"
    }

    fun sortSeries(sort: String, networkConnection: Boolean, init: Boolean) {
        if (init) {
            seriesRepository.page.value = 1
        }
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

    fun categoriesSelected(type: String, from: Int, to: Int): List<Serie> {
        if (categoriesSelected.isNotEmpty()) {
            val seriesSelected = mutableListOf<Serie>()
            seriesRepository.seriesCategories.value?.forEach { serieCategory ->
                if (categoriesSelected.contains(serieCategory.genreId)) {
                    seriesRepository.series.value?.find {
                        it.id == serieCategory.serieId
                    }?.let {
                        seriesSelected.add(it)
                    }
                }
            }
            return seriesSelected.distinct().createSubList(type, from, to)
        } else {
            return seriesRepository.series.value?.createSubList(type, from, to) ?: mutableListOf()
        }
    }

    private fun List<Serie>.createSubList(type: String, from: Int, to: Int): List<Serie> {
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
                    this.sortedByDescending { it.firstAirDate }.subList(from, to)
                } else {
                    this.sortedByDescending { it.firstAirDate }
                }
            }
        }
    }
}