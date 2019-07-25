package com.rappi.moviesdb.remote

import com.rappi.moviesdb.database.DatabaseSerie

/**
 * Created by Luis Vargas on 2019-07-24.
 */

data class NetworkSerieContainer(val results: List<NetworkSerie>) {

    fun asDatabaseModel(): Array<DatabaseSerie> {
        return results.map {
            DatabaseSerie(
                it.id,
                it.voteCount,
                it.voteAverage,
                it.name,
                it.popularity,
                it.posterPath,
                it.originalLanguage,
                it.originalName,
                it.backdropPath,
                it.overview,
                it.firstAirDate)
        }.toTypedArray()
    }
}