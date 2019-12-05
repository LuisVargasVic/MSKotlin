package com.venkonenterprise.mskotlin.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.venkonenterprise.mskotlin.database.movies.*
import com.venkonenterprise.mskotlin.database.series.*
import com.venkonenterprise.mskotlin.domain.Video
import com.venkonenterprise.mskotlin.domain.movies.CategoryMovie
import com.venkonenterprise.mskotlin.domain.movies.Movie
import com.venkonenterprise.mskotlin.domain.movies.MovieCategory
import com.venkonenterprise.mskotlin.domain.series.CategorySerie
import com.venkonenterprise.mskotlin.domain.series.Serie
import com.venkonenterprise.mskotlin.domain.series.SerieCategory

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
    DatabaseVideoMovie::class,
    DatabaseVideoSerie::class
], version = 1, exportSchema = false)
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


        fun movieAsDomainModel(list: List<DatabaseMovie>): List<Movie> {
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

        fun movieVideosAsDomainModel(list: List<DatabaseVideoMovie>): List<Video> {
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

        fun serieVideosAsDomainModel(list: List<DatabaseVideoSerie>): List<Video> {
            return list.map {
                Video(
                    it.id,
                    it.key,
                    it.name,
                    it.site,
                    it.size,
                    it.type,
                    it.serieId
                )
            }
        }
    }
}



