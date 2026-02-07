package com.ynd.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ynd.domain.GetVideosUseCase
import com.ynd.domain.RecordVideoUseCase
import com.ynd.domain.entity.VideoEntry

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val getVideosUseCase: GetVideosUseCase,
    private val recordVideoUseCase: RecordVideoUseCase
) : ViewModel() {

    private val _videos = MutableStateFlow<List<VideoEntry>>(emptyList())
    val videos: StateFlow<List<VideoEntry>> = _videos.asStateFlow()

    init {
        loadVideos()
    }

    fun loadVideos() {
        viewModelScope.launch {
            _videos.value = getVideosUseCase()
        }
    }

    fun addVideoPlaceholder() {
        // Dodajemy tymczasowy placeholder wideo
        val placeholder = VideoEntry(
            fileUri = "placeholder.mp4",
            description = "Test video"
        )
        viewModelScope.launch {
            recordVideoUseCase(placeholder)
            loadVideos()
        }
    }
}
