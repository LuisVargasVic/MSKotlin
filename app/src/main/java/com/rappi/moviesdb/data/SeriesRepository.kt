package com.rappi.moviesdb.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.rappi.moviesdb.BuildConfig
import com.rappi.moviesdb.database.MSDatabase
import com.rappi.moviesdb.domain.series.CategorySerie
import com.rappi.moviesdb.domain.series.Serie
import com.rappi.moviesdb.domain.series.SerieCategory
import com.rappi.moviesdb.presentation.series.SeriesViewModel
import com.rappi.moviesdb.remote.Network
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Created by Luis Vargas on 2019-07-24.
 */

class SeriesRepository(private val database: MSDatabase) {


    val categories: LiveData<List<CategorySerie>> =
        Transformations.map(database.serieDao.getCategories()) {
            MSDatabase.categorySerieAsDomainModel(it)
        }

    val series: LiveData<List<Serie>> =
        Transformations.map(database.serieDao.getSeries()) {
            MSDatabase.serieAsDomainModel(it)
        }

    val seriesCategories: LiveData<List<SerieCategory>> =
        Transformations.map(database.serieDao.getSeriesCategories()) {
            MSDatabase.seriesCategoriesAsDomainModel(it)
        }

    suspend fun refreshCategories() {
        try {
            withContext(Dispatchers.IO) {
                val categories = Network.service.getSeriesCategories(BuildConfig.API_KEY).await()
                database.serieDao.insertAllCategories(*categories.seriesAsDatabaseModel())
                refreshSeries(SeriesViewModel.SORT_POPULAR)
            }
        } catch (e: Exception) {
            Log.wtf("Errors", e.localizedMessage)
        }
    }

    suspend fun refreshSeries(sort: String) {
        try {
            withContext(Dispatchers.IO) {
                val series = Network.service.getSeries(sort, BuildConfig.API_KEY).await()
                for (serie in series.results) {
                    database.serieDao.deleteBySerieId(serie.id)
                }
                database.serieDao.insertAll(*series.asDatabaseModel())
                database.serieDao.insertSeriesCategories(*series.seriesCategoriesAsDatabaseModel())
            }
        } catch (e: Exception) {
            Log.wtf("Errors", e.localizedMessage)
        }
    }
}