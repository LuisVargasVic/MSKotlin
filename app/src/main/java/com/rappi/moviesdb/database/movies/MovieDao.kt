package com.rappi.moviesdb.database.movies

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rappi.moviesdb.database.movies.DatabaseCategoryMovie
import com.rappi.moviesdb.database.movies.DatabaseMovie
import com.rappi.moviesdb.database.movies.DatabaseMovieCategory

/**
 * Created by Luis Vargas on 2019-07-22.
 */

@Dao
interface MovieDao {

    @Query("SELECT * FROM DatabaseCategoryMovie")
    fun getCategories(): LiveData<List<DatabaseCategoryMovie>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllCategories(vararg categoryMovies: DatabaseCategoryMovie)

    @Query("SELECT * FROM DatabaseMovie")
    fun getMovies(): LiveData<List<DatabaseMovie>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg movies: DatabaseMovie)

    @Query("SELECT * FROM DatabaseMovieCategory")
    fun getMoviesCategories(): LiveData<List<DatabaseMovieCategory>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMoviesCategories(vararg movies: DatabaseMovieCategory)

    @Query("DELETE FROM DatabaseMovieCategory WHERE movieId = :movieId")
    fun deleteByMovieId(movieId: Int)
}