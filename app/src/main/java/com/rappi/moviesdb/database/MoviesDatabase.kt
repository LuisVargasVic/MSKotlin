package com.rappi.moviesdb.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.rappi.moviesdb.domain.Movie
import com.rappi.moviesdb.domain.Serie

/**
 * Created by Luis Vargas on 2019-07-22.
 */

@Database(entities = [DatabaseMovie::class, DatabaseSerie::class], version = 1)
abstract class MoviesDatabase : RoomDatabase() {
    abstract val movieDao: MovieDao
    abstract val serieDao: SerieDao

    companion object {

        private lateinit var INSTANCE: MoviesDatabase

        fun getDatabase(context: Context): MoviesDatabase {
            if (!::INSTANCE.isInitialized) {
                INSTANCE = Room.databaseBuilder(context,
                    MoviesDatabase::class.java, "movies").build()
            }
            return INSTANCE
        }


        fun asDomainModel(list: List<DatabaseMovie>): List<Movie> {
            return list.map {
                Movie(
                    it.id,
                    it.voteCount,
                    it.video,
                    it.voteAverage,
                    it.title,
                    it.popularity,
                    it.posterPath,
                    it.originalLanguage,
                    it.originalTitle,
                    it.backdropPath,
                    it.adult,
                    it.overview,
                    it.releaseDate)
            }
        }

        fun serieAsDomainModel(list: List<DatabaseSerie>): List<Serie> {
            return list.map {
                Serie(
                    it.id,
                    it.voteCount,
                    it.voteAverage,
                    it.name,
                    it.popularity,
                    it.posterPath,
                    it.originalLanguage,
                    it.originalName,
                    it.backdropPath,
                    it.overview,
                    it.firstAirDate)
            }
        }
    }
}


