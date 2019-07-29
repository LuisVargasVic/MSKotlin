package com.rappi.moviesdb.presentation.serie_detail

import android.content.Context
import androidx.lifecycle.ViewModel
import com.rappi.moviesdb.data.SeriesRepository
import com.rappi.moviesdb.database.MSDatabase
import com.rappi.moviesdb.domain.Video
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

/**
 * Created by Luis Vargas on 2019-07-28.
 */

class SerieVideosViewModel(serieId: Int, context: Context, networkConnection: Boolean) : ViewModel() {

    private val viewModelJob = SupervisorJob()

    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val database = MSDatabase.getDatabase(context)
    val seriesRepository = SeriesRepository(database)

    init {
        if (networkConnection) {
            viewModelScope.launch {
                seriesRepository.refreshVideos(serieId)
            }
        }
    }

    fun videosSelected(serieId: Int): List<Video> {
        val videosSelected = mutableListOf<Video>()
        seriesRepository.videos.value?.forEach { it ->
            if (it.typeId == serieId) {
                videosSelected.add(it)
            }
        }
        return videosSelected
    }
}