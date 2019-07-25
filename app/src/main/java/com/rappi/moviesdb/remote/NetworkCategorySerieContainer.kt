package com.rappi.moviesdb.remote

import com.rappi.moviesdb.database.DatabaseCategorySerie

/**
 * Created by Luis Vargas on 2019-07-24.
 */

data class NetworkCategorySerieContainer(val genres: List<NetworkCategory>) {

    fun asDatabaseModel(): Array<DatabaseCategorySerie> {
        return genres.map {
            DatabaseCategorySerie(
                it.id,
                it.name)
        }.toTypedArray()
    }
}