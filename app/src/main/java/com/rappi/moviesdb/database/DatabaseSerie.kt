package com.rappi.moviesdb.database

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by Luis Vargas on 2019-07-24.
 */

@Entity
data class DatabaseSerie constructor(
    @PrimaryKey
    val id: Int,
    val voteCount: Int,
    val voteAverage: Double,
    val name: String,
    val popularity: Double,
    val posterPath: String?,
    val originalLanguage: String,
    val originalName: String,
    val backdropPath: String?,
    val overview: String,
    val firstAirDate: String
)