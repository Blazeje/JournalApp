package com.ynd.ui

import androidx.compose.runtime.Immutable
import com.ynd.domain.entity.VideoEntry

interface JournalContract{

    @Immutable
    data class State(
        val videos: List<VideoEntry> = emptyList(),
        val playingVideoId: String? = null,
        val isLoading: Boolean = false
    )

    sealed class Event {
        data object AddClicked : Event()
        data class VideoClicked(val id: String) : Event()
    }

    sealed class InternalEvent {
        data class VideosLoaded(val videos: List<VideoEntry>) : InternalEvent()
        data class PlayingChanged(val id: String?) : InternalEvent()
    }

    sealed class Effect {
        data object OpenCamera : Effect()
    }

}


