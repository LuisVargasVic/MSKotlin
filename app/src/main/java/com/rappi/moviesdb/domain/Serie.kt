package com.rappi.moviesdb.domain

import java.io.Serializable

/**
 * Created by Luis Vargas on 2019-07-24.
 */

data class Serie(
    val id: Int,
    val voteCount: Int,
    val voteAverage: Double,
    val name: String?,
    val popularity: Double,
    val posterPath: String?,
    val originalLanguage: String,
    val originalName: String,
    val backdropPath: String?,
    val overview: String,
    val firstAirDate: String
): Serializable