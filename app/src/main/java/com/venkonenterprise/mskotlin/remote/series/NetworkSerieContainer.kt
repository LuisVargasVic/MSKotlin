package com.venkonenterprise.mskotlin.remote.series

import com.venkonenterprise.mskotlin.database.series.DatabaseSerie
import com.venkonenterprise.mskotlin.database.series.DatabaseSerieCategory

/**
 * Created by Luis Vargas on 2019-07-24.
 */

data class NetworkSerieContainer(
    val page: Int,
    val results: List<NetworkSerie>
) {

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
                it.firstAirDate
            )
        }.toTypedArray()
    }

    fun seriesCategoriesAsDatabaseModel(): Array<DatabaseSerieCategory> {
        val categories = mutableListOf<DatabaseSerieCategory>()
        results.map {
            it.genreIds.map { genre ->
                categories.add(
                    DatabaseSerieCategory(
                        genre,
                        it.id
                    )
                )
            }
        }
        return categories.toTypedArray()
    }
}