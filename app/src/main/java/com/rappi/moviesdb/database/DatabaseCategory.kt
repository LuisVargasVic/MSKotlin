package com.rappi.moviesdb.database

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by Luis Vargas on 2019-07-24.
 */

@Entity
data class DatabaseCategory constructor(
    @PrimaryKey
    val id: Int,
    val name: String,
    val type: String
)