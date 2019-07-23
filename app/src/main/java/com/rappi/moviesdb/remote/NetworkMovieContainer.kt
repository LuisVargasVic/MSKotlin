package com.rappi.moviesdb.remote

import com.rappi.moviesdb.database.DatabaseMovie
import com.rappi.moviesdb.domain.Movie

/**
 * Created by Luis Vargas on 2019-07-22.
 */

data class NetworkMovieContainer(val results: List<NetworkMovie>) {
    fun asDomainModel(): List<Movie> {
        return results.map {
            Movie(
                it.id,
                it.voteCount,
                it.video,
                it.voteAverage,
                it.title,
                it.popularity,
                it.posterPath,
                it.originalLanguage,
                it.originalTitle,
                it.backdropPath,
                it.adult,
                it.overview,
                it.releaseDate)
        }
    }

    fun asDatabaseModel(): Array<DatabaseMovie> {
        return results.map {
            DatabaseMovie(
                it.id,
                it.voteCount,
                it.video,
                it.voteAverage,
                it.title,
                it.popularity,
                it.posterPath,
                it.originalLanguage,
                it.originalTitle,
                it.backdropPath,
                it.adult,
                it.overview,
                it.releaseDate)
        }.toTypedArray()
    }
}
