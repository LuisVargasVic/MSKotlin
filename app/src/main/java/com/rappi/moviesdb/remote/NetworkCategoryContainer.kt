package com.rappi.moviesdb.remote

import com.rappi.moviesdb.database.DatabaseCategory

/**
 * Created by Luis Vargas on 2019-07-24.
 */

data class NetworkCategoryContainer(val genres: List<NetworkCategory>) {

    fun asDatabaseModel(type: String): Array<DatabaseCategory> {
        return genres.map {
            DatabaseCategory(
                it.id,
                it.name,
                type
            )
        }.toTypedArray()
    }
}