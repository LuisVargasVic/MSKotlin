package com.rappi.moviesdb.remote.videos

import java.io.Serializable

/**
 * Created by Luis Vargas on 2019-07-28.
 */

data class NetworkVideo(
    val id: String,
    val key: String,
    val name: String?,
    val site: String,
    val size: Int?,
    val type: String
): Serializable