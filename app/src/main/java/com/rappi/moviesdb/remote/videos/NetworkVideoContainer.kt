package com.rappi.moviesdb.remote.videos

import com.rappi.moviesdb.database.movies.DatabaseVideoMovie

/**
 * Created by Luis Vargas on 2019-07-28.
 */

data class NetworkVideoContainer(
    val id: Int,
    val results: List<NetworkVideo>
) {

    fun videosAsDatabaseModel(): Array<DatabaseVideoMovie> {
        return results.map {
            DatabaseVideoMovie(
                it.id,
                it.key,
                it.name,
                it.site,
                it.size,
                it.type,
                id
            )
        }.toTypedArray()
    }
}