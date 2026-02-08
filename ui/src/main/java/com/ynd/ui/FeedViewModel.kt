package com.ynd.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ynd.domain.GetVideosUseCase
import com.ynd.domain.RecordVideoUseCase
import com.ynd.domain.entity.VideoEntry
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FeedViewModel(
    getVideosUseCase: GetVideosUseCase,
    private val recordVideoUseCase: RecordVideoUseCase
) : ViewModel() {

    val videos: StateFlow<List<VideoEntry>> =
        getVideosUseCase()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )

    fun addVideo(video: VideoEntry) {
        viewModelScope.launch {
            recordVideoUseCase(video)
        }
    }
}
