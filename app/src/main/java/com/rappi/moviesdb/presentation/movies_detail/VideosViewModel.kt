package com.rappi.moviesdb.presentation.movies_detail

import android.content.Context
import androidx.lifecycle.ViewModel
import com.rappi.moviesdb.data.MoviesRepository
import com.rappi.moviesdb.database.MSDatabase
import com.rappi.moviesdb.domain.Video
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

/**
 * Created by Luis Vargas on 2019-07-28.
 */

class VideosViewModel(movieId: Int, context: Context, networkConnection: Boolean) : ViewModel() {

    private val viewModelJob = SupervisorJob()

    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val database = MSDatabase.getDatabase(context)
    val moviesRepository = MoviesRepository(database)

    init {
        if (networkConnection) {
            viewModelScope.launch {
                moviesRepository.refreshVideos(movieId)
            }
        }
    }

    fun videosSelected(movieId: Int): List<Video> {
        val videosSelected = mutableListOf<Video>()
        moviesRepository.videos.value?.forEach { it ->
            if (it.movieId == movieId) {
                videosSelected.add(it)
            }
        }
        return videosSelected
    }
}