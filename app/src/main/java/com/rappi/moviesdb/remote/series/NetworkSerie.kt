package com.rappi.moviesdb.remote.series

import com.google.gson.annotations.SerializedName

/**
 * Created by Luis Vargas on 2019-07-24.
 */

data class NetworkSerie(
    val id: Int,
    @SerializedName("vote_count")
    val voteCount: Int,
    @SerializedName("vote_average")
    val voteAverage: Double,
    val name: String,
    val popularity: Double,
    @SerializedName("poster_path")
    val posterPath: String?,
    @SerializedName("original_language")
    val originalLanguage: String,
    @SerializedName("original_name")
    val originalName: String,
    @SerializedName("genre_ids")
    val genreIds: List<Int>,
    @SerializedName("backdrop_path")
    val backdropPath: String?,
    val overview: String,
    @SerializedName("first_air_date")
    val firstAirDate: String
)