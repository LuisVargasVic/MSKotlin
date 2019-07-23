package com.rappi.moviesdb.database

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by Luis Vargas on 2019-07-22.
 */

@Entity
data class DatabaseMovie constructor(
    @PrimaryKey
    val id: Int,
    val voteCount: Int,
    val video: Boolean,
    val voteAverage: Double,
    val title: String,
    val popularity: Double,
    val posterPath: String?,
    val originalLanguage: String,
    val originalTitle: String,
    val backdropPath: String?,
    val adult: Boolean,
    val overview: String,
    val releaseDate: String
)

