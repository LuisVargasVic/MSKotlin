package com.rappi.moviesdb.remote

import com.rappi.moviesdb.database.DatabaseCategoryMovie

/**
 * Created by Luis Vargas on 2019-07-24.
 */

data class NetworkCategoryMovieContainer(val genres: List<NetworkCategory>) {

    fun asDatabaseModel(): Array<DatabaseCategoryMovie> {
        return genres.map {
            DatabaseCategoryMovie(
                it.id,
                it.name
            )
        }.toTypedArray()
    }
}