package com.rappi.moviesdb.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.rappi.moviesdb.BuildConfig
import com.rappi.moviesdb.database.MoviesDatabase
import com.rappi.moviesdb.domain.CategoryMovie
import com.rappi.moviesdb.domain.Movie
import com.rappi.moviesdb.domain.MovieCategory
import com.rappi.moviesdb.presentation.movies.MoviesViewModel.Companion.SORT_POPULAR
import com.rappi.moviesdb.remote.Network
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Created by Luis Vargas on 2019-07-22.
 */

class MoviesRepository(private val database: MoviesDatabase) {

    val categories: LiveData<List<CategoryMovie>> =
        Transformations.map(database.movieDao.getCategories()) {
            MoviesDatabase.categoryMovieAsDomainModel(it)
        }

    val movies: LiveData<List<Movie>> =
        Transformations.map(database.movieDao.getMovies()) {
            MoviesDatabase.asDomainModel(it)
        }

    val moviesCategories: LiveData<List<MovieCategory>> =
        Transformations.map(database.movieDao.getMoviesCategories()) {
            MoviesDatabase.moviesCategoriesAsDomainModel(it)
        }

    suspend fun refreshCategories() {
        try {
            withContext(Dispatchers.IO) {
                val categories = Network.service.getMoviesCategories(BuildConfig.API_KEY).await()
                database.movieDao.insertAllCategories(*categories.asDatabaseModel())
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