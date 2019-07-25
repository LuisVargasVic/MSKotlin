package com.rappi.moviesdb.database

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by Luis Vargas on 2019-07-24.
 */

@Entity
data class DatabaseMovieCategory constructor(
    val genreId: Int,
    val movieId: Int
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}