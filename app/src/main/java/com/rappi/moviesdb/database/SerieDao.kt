package com.rappi.moviesdb.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

/**
 * Created by Luis Vargas on 2019-07-24.
 */

@Dao
interface SerieDao {
    @Query("SELECT * FROM DatabaseSerie")
    fun getSeries(): LiveData<List<DatabaseSerie>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg series: DatabaseSerie)
}