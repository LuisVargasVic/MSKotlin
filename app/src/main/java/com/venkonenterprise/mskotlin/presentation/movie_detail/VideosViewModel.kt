package com.venkonenterprise.mskotlin.presentation.movie_detail

import android.content.Context
import androidx.lifecycle.ViewModel
import com.venkonenterprise.mskotlin.data.MoviesRepository
import com.venkonenterprise.mskotlin.database.MSDatabase
import com.venkonenterprise.mskotlin.domain.Video
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
            if (it.typeId == movieId) {
                videosSelected.add(it)
            }
        }
        return videosSelected
    }
}