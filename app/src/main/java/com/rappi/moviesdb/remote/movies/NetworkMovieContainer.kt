package com.rappi.moviesdb.remote.movies

import com.rappi.moviesdb.database.movies.DatabaseMovie
import com.rappi.moviesdb.database.movies.DatabaseMovieCategory

/**
 * Created by Luis Vargas on 2019-07-22.
 */

data class NetworkMovieContainer(
    val page: Int,
    val results: List<NetworkMovie>
) {

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
                it.releaseDate
            )
        }.toTypedArray()
    }

    fun moviesCategoriesAsDatabaseModel(): Array<DatabaseMovieCategory> {
        val categories = mutableListOf<DatabaseMovieCategory>()
        results.map {
            it.genreIds.map { genre ->
                categories.add(
                    DatabaseMovieCategory(
                        genre,
                        it.id
                    )
                )
            }
        }
        return categories.toTypedArray()
    }
}