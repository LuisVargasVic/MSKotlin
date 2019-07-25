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

    @Query("SELECT * FROM DatabaseCategorySerie")
    fun getCategories(): LiveData<List<DatabaseCategorySerie>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllCategories(vararg categorySeries: DatabaseCategorySerie)

    @Query("SELECT * FROM DatabaseSerie")
    fun getSeries(): LiveData<List<DatabaseSerie>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg series: DatabaseSerie)

    @Query("SELECT * FROM DatabaseSerieCategory")
    fun getSeriesCategories(): LiveData<List<DatabaseSerieCategory>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSeriesCategories(vararg movies: DatabaseSerieCategory)

    @Query("DELETE FROM DatabaseSerieCategory WHERE serieId = :serieId")
    fun deleteBySerieId(serieId: Int)
}