package com.rappi.moviesdb.remote

import com.rappi.moviesdb.remote.categories.NetworkCategoryContainer
import com.rappi.moviesdb.remote.movies.NetworkMovieContainer
import com.rappi.moviesdb.remote.series.NetworkSerieContainer
import com.rappi.moviesdb.remote.videos.NetworkVideoContainer
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by Luis Vargas on 2019-07-22.
 */

interface Service {

    @GET("genre/movie/list")
    fun getMoviesCategories(@Query("api_key") api: String): Deferred<NetworkCategoryContainer>

    @GET("genre/tv/list")
    fun getSeriesCategories(@Query("api_key") api: String): Deferred<NetworkCategoryContainer>

    @GET("movie/{sort}")
    fun getMovies(@Path("sort") sort: String, @Query("api_key") api: String, @Query("page") page: Int): Deferred<NetworkMovieContainer>

    @GET("tv/{sort}")
    fun getSeries(@Path("sort") sort: String, @Query("api_key") api: String, @Query("page") page: Int): Deferred<NetworkSerieContainer>

    @GET("movie/{movie_id}/videos")
    fun getMovieVideos(@Path("movie_id") movie_id: Int, @Query("api_key") api: String): Deferred<NetworkVideoContainer>

    @GET("tv/{serie_id}/videos")
    fun getSerieVideos(@Path("serie_id") movie_id: Int, @Query("api_key") api: String): Deferred<NetworkVideoContainer>

}

