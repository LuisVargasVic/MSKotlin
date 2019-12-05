package com.venkonenterprise.mskotlin.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.venkonenterprise.mskotlin.BuildConfig
import com.venkonenterprise.mskotlin.database.MSDatabase
import com.venkonenterprise.mskotlin.domain.Video
import com.venkonenterprise.mskotlin.domain.movies.CategoryMovie
import com.venkonenterprise.mskotlin.domain.movies.Movie
import com.venkonenterprise.mskotlin.domain.movies.MovieCategory
import com.venkonenterprise.mskotlin.presentation.movies.MoviesViewModel.Companion.SORT_POPULAR
import com.venkonenterprise.mskotlin.remote.ApiStatus
import com.venkonenterprise.mskotlin.remote.Network
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Created by Luis Vargas on 2019-07-22.
 */

class MoviesRepository(private val database: MSDatabase) {

    val categories: LiveData<List<CategoryMovie>> =
        Transformations.map(database.movieDao.getCategories()) {
            MSDatabase.categoryMovieAsDomainModel(it)
        }

    val movies: LiveData<List<Movie>> =
        Transformations.map(database.movieDao.getMovies()) {
            MSDatabase.movieAsDomainModel(it)
        }

    val moviesCategories: LiveData<List<MovieCategory>> =
        Transformations.map(database.movieDao.getMoviesCategories()) {
            MSDatabase.moviesCategoriesAsDomainModel(it)
        }

    val videos: LiveData<List<Video>> =
        Transformations.map(database.movieDao.getMovieVideos()) {
            MSDatabase.movieVideosAsDomainModel(it)
        }

    private val _apiStatus = MutableLiveData<ApiStatus>()
    val apiStatus: LiveData<ApiStatus>
        get() = _apiStatus

    val page = MutableLiveData<Int>()

    suspend fun refreshCategories() {
        try {
            withContext(Dispatchers.IO) {
                val categories = Network.service.getMoviesCategories(BuildConfig.API_KEY).await()
                database.movieDao.insertAllCategories(*categories.moviesAsDatabaseModel())
                refreshMovies(SORT_POPULAR)
            }
        } catch (e: Exception) {
            Log.wtf("Errors", e.localizedMessage)
        }
    }

    suspend fun refreshMovies(sort: String) {
        try {
            withContext(Dispatchers.IO) {
                if (page.value == 1) {
                    _apiStatus.postValue(ApiStatus.LOADING)
                } else {
                    _apiStatus.postValue(ApiStatus.APPENDING)
                }
                val movies = Network.service.getMovies(sort, BuildConfig.API_KEY, page.value ?: 1).await()
                for (movie in movies.results) {
                    database.movieDao.deleteByMovieId(movie.id)
                }
                page.postValue(movies.page + 1)
                _apiStatus.postValue(ApiStatus.STOP)
                database.movieDao.insertAll(*movies.asDatabaseModel())
                database.movieDao.insertMoviesCategories(*movies.moviesCategoriesAsDatabaseModel())
            }
        } catch (e: Exception) {
            Log.wtf("Errors", e.localizedMessage)
            _apiStatus.value = ApiStatus.ERROR
        }
    }

    suspend fun refreshVideos(videoId: Int) {
        try {
            withContext(Dispatchers.IO) {
                val videos = Network.service.getMovieVideos(videoId, BuildConfig.API_KEY).await()
                database.movieDao.insertAllVideos(*videos.movieVideosAsDatabaseModel())
            }
        } catch (e: Exception) {
            Log.wtf("Errors", e.localizedMessage)
        }
    }
}