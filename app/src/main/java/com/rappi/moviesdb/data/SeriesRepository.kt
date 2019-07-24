package com.rappi.moviesdb.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.rappi.moviesdb.BuildConfig
import com.rappi.moviesdb.database.MoviesDatabase
import com.rappi.moviesdb.domain.Serie
import com.rappi.moviesdb.remote.Network
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Created by Luis Vargas on 2019-07-24.
 */

class SeriesRepository(private val database: MoviesDatabase) {

    val series: LiveData<List<Serie>> =
        Transformations.map(database.serieDao.getSeries()) {
            MoviesDatabase.serieAsDomainModel(it)
}

    suspend fun refreshSeries(sort: String) {
        try {
            withContext(Dispatchers.IO) {
                val series = Network.service.getSeries(sort, BuildConfig.API_KEY).await()
                database.serieDao.insertAll(*series.asDatabaseModel())
            }
        } catch (e: Exception) {
            Log.wtf("Errors", e.localizedMessage)
        }
    }
}