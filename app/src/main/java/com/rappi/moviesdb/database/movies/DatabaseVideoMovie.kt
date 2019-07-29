package com.rappi.moviesdb.database.movies

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by Luis Vargas on 2019-07-28.
 */

@Entity
data class DatabaseVideoMovie constructor(
    @PrimaryKey
    val id: String,
    val key: String,
    val name: String?,
    val site: String,
    val size: Int?,
    val type: String,
    val movieId: Int
)