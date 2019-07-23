package com.rappi.moviesdb.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

/**
 * Created by Luis Vargas on 2019-07-22.
 */
@Dao
interface MovieDao {
    @Query("SELECT * FROM DatabaseMovie")
    fun getMovies(): LiveData<List<DatabaseMovie>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg movies: DatabaseMovie)
}