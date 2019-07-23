package com.rappi.moviesdb.remote

import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by Luis Vargas on 2019-07-22.
 */

interface MovieService {
    @GET("movie/{sort}")
    fun getMovies(@Path("sort") sort: String, @Query("api_key") api: String): Deferred<NetworkMovieContainer>
}

