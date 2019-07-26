package com.rappi.moviesdb.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.rappi.moviesdb.BuildConfig
import com.rappi.moviesdb.database.MSDatabase
import com.rappi.moviesdb.domain.movies.CategoryMovie
import com.rappi.moviesdb.domain.movies.Movie
import com.rappi.moviesdb.domain.movies.MovieCategory
import com.rappi.moviesdb.presentation.movies.MoviesViewModel.Companion.SORT_POPULAR
import com.rappi.moviesdb.remote.Network
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
            MSDatabase.asDomainModel(it)
        }

    val moviesCategories: LiveData<List<MovieCategory>> =
        Transformations.map(database.movieDao.getMoviesCategories()) {
            MSDatabase.moviesCategoriesAsDomainModel(it)
        }

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
                val movies = Network.service.getMovies(sort, BuildConfig.API_KEY).await()
                for (movie in movies.results) {
                    database.movieDao.deleteByMovieId(movie.id)
                }
                database.movieDao.insertAll(*movies.asDatabaseModel())
                database.movieDao.insertMoviesCategories(*movies.moviesCategoriesAsDatabaseModel())
            }
        } catch (e: Exception) {
            Log.wtf("Errors", e.localizedMessage)
        }
    }
}