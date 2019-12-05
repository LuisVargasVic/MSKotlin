package com.venkonenterprise.mskotlin.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.venkonenterprise.mskotlin.BuildConfig
import com.venkonenterprise.mskotlin.database.MSDatabase
import com.venkonenterprise.mskotlin.domain.Video
import com.venkonenterprise.mskotlin.domain.series.CategorySerie
import com.venkonenterprise.mskotlin.domain.series.Serie
import com.venkonenterprise.mskotlin.domain.series.SerieCategory
import com.venkonenterprise.mskotlin.presentation.series.SeriesViewModel
import com.venkonenterprise.mskotlin.remote.ApiStatus
import com.venkonenterprise.mskotlin.remote.Network
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

    val videos: LiveData<List<Video>> =
        Transformations.map(database.serieDao.getSerieVideos()) {
            MSDatabase.serieVideosAsDomainModel(it)
        }

    private val _apiStatus = MutableLiveData<ApiStatus>()
    val apiStatus: LiveData<ApiStatus>
        get() = _apiStatus

    val page = MutableLiveData<Int>()

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
                if (page.value == 1) {
                    _apiStatus.postValue(ApiStatus.LOADING)
                } else {
                    _apiStatus.postValue(ApiStatus.APPENDING)
                }
                val series = Network.service.getSeries(sort, BuildConfig.API_KEY, page.value ?: 1).await()
                for (serie in series.results) {
                    database.serieDao.deleteBySerieId(serie.id)
                }
                page.postValue(series.page + 1)
                _apiStatus.postValue(ApiStatus.STOP)
                database.serieDao.insertAll(*series.asDatabaseModel())
                database.serieDao.insertSeriesCategories(*series.seriesCategoriesAsDatabaseModel())
            }
        } catch (e: Exception) {
            Log.wtf("Errors", e.localizedMessage)
            _apiStatus.value = ApiStatus.ERROR
        }
    }

    suspend fun refreshVideos(videoId: Int) {
        try {
            withContext(Dispatchers.IO) {
                val videos = Network.service.getSerieVideos(videoId, BuildConfig.API_KEY).await()
                database.serieDao.insertAllVideos(*videos.serieVideosAsDatabaseModel())
            }
        } catch (e: Exception) {
            Log.wtf("Errors", e.localizedMessage)
        }
    }
}