package com.rappi.moviesdb.database.series

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by Luis Vargas on 2019-07-24.
 */

@Entity
data class DatabaseSerieCategory constructor(
    val genreId: Int,
    val serieId: Int
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}