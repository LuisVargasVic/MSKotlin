package com.venkonenterprise.mskotlin.database.movies

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by Luis Vargas on 2019-07-24.
 */

@Entity
data class DatabaseCategoryMovie constructor(
    @PrimaryKey
    val id: Int,
    val name: String
)