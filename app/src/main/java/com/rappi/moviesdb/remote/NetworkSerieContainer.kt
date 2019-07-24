package com.rappi.moviesdb.remote

import com.rappi.moviesdb.database.DatabaseSerie
import com.rappi.moviesdb.domain.Serie

/**
 * Created by Luis Vargas on 2019-07-24.
 */

data class NetworkSerieContainer(val results: List<NetworkSerie>) {
    fun asDomainModel(): List<Serie> {
        return results.map {
            Serie(
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
        }
    }

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