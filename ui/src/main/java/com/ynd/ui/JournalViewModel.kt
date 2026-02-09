package com.ynd.ui

import androidx.lifecycle.viewModelScope
import com.ynd.domain.GetVideosUseCase
import com.ynd.shared.MviViewModel
import kotlinx.coroutines.launch
import com.ynd.ui.JournalContract.Event
import com.ynd.ui.JournalContract.State
import com.ynd.ui.JournalContract.Effect
import com.ynd.ui.JournalContract.InternalEvent

class JournalViewModel(
    private val getVideosUseCase: GetVideosUseCase
) : MviViewModel<State, Event, InternalEvent, Effect>(
    initialState = State()
) {

    init {
        observeVideos()
    }

    private fun observeVideos() {
        viewModelScope.launch {
            getVideosUseCase()
                .collect { videos ->
                    pushInternal(InternalEvent.VideosLoaded(videos))
                }
        }
    }

    override fun onHandleUiEvent(uiEvent:Event, state: State) {
        when (uiEvent) {
            Event.AddClicked -> emitEffect(Effect.OpenCamera)

            is Event.VideoClicked -> {
                val newId = if (state.playingVideoId == uiEvent.id) null else uiEvent.id
                pushInternal(InternalEvent.PlayingChanged(newId))
            }

            is Event.ShareVideo -> {
                emitEffect(Effect.ShareVideoIntent(uiEvent.video.fileUri))
            }
        }
    }

    override fun reduce(event: InternalEvent, state: State): State =
        when (event) {
            is InternalEvent.VideosLoaded -> state.copy(videos = event.videos)
            is InternalEvent.PlayingChanged -> state.copy(playingVideoId = event.id)
        }
}

