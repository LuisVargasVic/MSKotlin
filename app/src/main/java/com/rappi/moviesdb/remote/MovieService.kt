package com.rappi.moviesdb.remote

import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by Luis Vargas on 2019-07-22.
 */

interface MovieService {

    @GET("genre/movie/list")
    fun getMoviesCategories(@Query("api_key") api: String): Deferred<NetworkCategoryContainer>

    @GET("genre/tv/list")
    fun getSeriesCategories(@Query("api_key") api: String): Deferred<NetworkCategoryContainer>

    @GET("movie/{sort}")
    fun getMovies(@Path("sort") sort: String, @Query("api_key") api: String): Deferred<NetworkMovieContainer>

    @GET("tv/{sort}")
    fun getSeries(@Path("sort") sort: String, @Query("api_key") api: String): Deferred<NetworkSerieContainer>
}

