package com.rappi.moviesdb.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.rappi.moviesdb.domain.Category
import com.rappi.moviesdb.domain.Movie
import com.rappi.moviesdb.domain.MovieCategory
import com.rappi.moviesdb.domain.Serie

/**
 * Created by Luis Vargas on 2019-07-22.
 */

@Database(entities = [
    DatabaseCategory::class,
    DatabaseMovie::class,
    DatabaseMovieCategory::class,
    DatabaseSerie::class
], version = 1)
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

        fun categoriesAsDomainModel(list: List<DatabaseCategory>): List<Category> {
            return list.map {
                Category(
                    it.id,
                    it.name,
                    it.type)
            }
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

        fun moviesCategoriesAsDomainModel(list: List<DatabaseMovieCategory>): List<MovieCategory> {
            return list.map {
                MovieCategory(
                    it.id,
                    it.genreId,
                    it.movieId)
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



