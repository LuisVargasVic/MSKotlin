package com.rappi.moviesdb.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.rappi.moviesdb.BuildConfig
import com.rappi.moviesdb.database.MoviesDatabase
import com.rappi.moviesdb.domain.Movie
import com.rappi.moviesdb.remote.Network
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Created by Luis Vargas on 2019-07-22.
 */

class MoviesRepository(private val database: MoviesDatabase) {

    val movies: LiveData<List<Movie>> =
        Transformations.map(database.movieDao.getMovies()) {
            MoviesDatabase.asDomainModel(it)
        }

    suspend fun refreshMovies(sort: String) {
        withContext(Dispatchers.IO) {
            val movies = Network.movieService.getMovies(sort, BuildConfig.API_KEY).await()
            database.movieDao.insertAll(*movies.asDatabaseModel())
        }
    }
}