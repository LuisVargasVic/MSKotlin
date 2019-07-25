package com.rappi.moviesdb.domain

import java.io.Serializable

/**
 * Created by Luis Vargas on 2019-07-24.
 */

data class SerieCategory(
    val id: Int,
    val genreId: Int,
    val serieId: Int
): Serializable