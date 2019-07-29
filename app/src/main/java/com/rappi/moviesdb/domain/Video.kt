package com.rappi.moviesdb.domain

import java.io.Serializable

/**
 * Created by Luis Vargas on 2019-07-28.
 */

data class Video(
    val id: String,
    val key: String,
    val name: String?,
    val site: String,
    val size: Int?,
    val type: String,
    val movieId: Int
): Serializable