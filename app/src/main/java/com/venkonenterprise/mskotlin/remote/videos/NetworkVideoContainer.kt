package com.venkonenterprise.mskotlin.remote.videos

import com.venkonenterprise.mskotlin.database.movies.DatabaseVideoMovie
import com.venkonenterprise.mskotlin.database.series.DatabaseVideoSerie

/**
 * Created by Luis Vargas on 2019-07-28.
 */

data class NetworkVideoContainer(
    val id: Int,
    val results: List<NetworkVideo>
) {

    fun movieVideosAsDatabaseModel(): Array<DatabaseVideoMovie> {
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

    fun serieVideosAsDatabaseModel(): Array<DatabaseVideoSerie> {
        return results.map {
            DatabaseVideoSerie(
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