package com.venkonenterprise.mskotlin.database.series

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by Luis Vargas on 2019-07-24.
 */

@Entity
data class DatabaseCategorySerie constructor(
    @PrimaryKey
    val id: Int,
    val name: String
)