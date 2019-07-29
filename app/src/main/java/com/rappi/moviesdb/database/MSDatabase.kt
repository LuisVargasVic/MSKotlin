package com.rappi.moviesdb.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.rappi.moviesdb.database.movies.*
import com.rappi.moviesdb.database.series.DatabaseCategorySerie
import com.rappi.moviesdb.database.series.DatabaseSerie
import com.rappi.moviesdb.database.series.DatabaseSerieCategory
import com.rappi.moviesdb.database.series.SerieDao
import com.rappi.moviesdb.domain.Video
import com.rappi.moviesdb.domain.movies.CategoryMovie
import com.rappi.moviesdb.domain.movies.Movie
import com.rappi.moviesdb.domain.movies.MovieCategory
import com.rappi.moviesdb.domain.series.CategorySerie
import com.rappi.moviesdb.domain.series.Serie
import com.rappi.moviesdb.domain.series.SerieCategory

/**
 * Created by Luis Vargas on 2019-07-22.
 */

@Database(entities = [
    DatabaseCategoryMovie::class,
    DatabaseMovie::class,
    DatabaseMovieCategory::class,
    DatabaseCategorySerie::class,
    DatabaseSerie::class,
    DatabaseSerieCategory::class,
    DatabaseVideoMovie::class
], version = 1)
abstract class MSDatabase : RoomDatabase() {
    abstract val movieDao: MovieDao
    abstract val serieDao: SerieDao

    companion object {

        private lateinit var INSTANCE: MSDatabase

        fun getDatabase(context: Context): MSDatabase {
            if (!::INSTANCE.isInitialized) {
                INSTANCE = Room.databaseBuilder(context,
                    MSDatabase::class.java, "movies").build()
            }
            return INSTANCE
        }

        fun categoryMovieAsDomainModel(list: List<DatabaseCategoryMovie>): List<CategoryMovie> {
            return list.map {
                CategoryMovie(
                    it.id,
                    it.name
                )
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
                    it.releaseDate
                )
            }
        }

        fun moviesCategoriesAsDomainModel(list: List<DatabaseMovieCategory>): List<MovieCategory> {
            return list.map {
                MovieCategory(
                    it.id,
                    it.genreId,
                    it.movieId
                )
            }
        }

        fun categorySerieAsDomainModel(list: List<DatabaseCategorySerie>): List<CategorySerie> {
            return list.map {
                CategorySerie(
                    it.id,
                    it.name
                )
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
                    it.firstAirDate
                )
            }
        }

        fun seriesCategoriesAsDomainModel(list: List<DatabaseSerieCategory>): List<SerieCategory> {
            return list.map {
                SerieCategory(
                    it.id,
                    it.genreId,
                    it.serieId
                )
            }
        }

        fun videosAsDomainModel(list: List<DatabaseVideoMovie>): List<Video> {
            return list.map {
                Video(
                    it.id,
                    it.key,
                    it.name,
                    it.site,
                    it.size,
                    it.type,
                    it.movieId
                )
            }
        }
    }
}



