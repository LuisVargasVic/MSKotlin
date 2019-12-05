package com.venkonenterprise.mskotlin.presentation.serie_detail

import android.content.Context
import androidx.lifecycle.ViewModel
import com.venkonenterprise.mskotlin.data.SeriesRepository
import com.venkonenterprise.mskotlin.database.MSDatabase
import com.venkonenterprise.mskotlin.domain.Video
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