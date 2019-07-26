package com.rappi.moviesdb.remote

import com.rappi.moviesdb.database.movies.DatabaseCategoryMovie
import com.rappi.moviesdb.database.series.DatabaseCategorySerie

/**
 * Created by Luis Vargas on 2019-07-24.
 */

data class NetworkCategoryContainer(val genres: List<NetworkCategory>) {

    fun moviesAsDatabaseModel(): Array<DatabaseCategoryMovie> {
        return genres.map {
            DatabaseCategoryMovie(
                it.id,
                it.name
            )
        }.toTypedArray()
    }

    fun seriesAsDatabaseModel(): Array<DatabaseCategorySerie> {
        return genres.map {
            DatabaseCategorySerie(
                it.id,
                it.name
            )
        }.toTypedArray()
    }
}