package com.ynd.video

import androidx.lifecycle.viewModelScope
import com.ynd.domain.RecordVideoUseCase
import com.ynd.domain.entity.VideoEntry
import com.ynd.video.CameraContract.Event
import com.ynd.video.CameraContract.Effect
import com.ynd.video.CameraContract.State
import com.ynd.video.CameraContract.InternalEvent
import com.ynd.shared.MviViewModel
import kotlinx.coroutines.launch

class CameraViewModel(
    private val recordVideoUseCase: RecordVideoUseCase
) : MviViewModel<State, Event, InternalEvent, Effect>(
    initialState = State()
) {

    override fun onHandleUiEvent(uiEvent: Event, state: State) {
        when (uiEvent) {
            Event.StartRecording -> pushInternal(InternalEvent.RecordingStarted)

            is Event.StopRecording -> {
                viewModelScope.launch {
                    recordVideoUseCase(
                        VideoEntry(
                            fileUri = uiEvent.uri.toString(),
                            description = uiEvent.description
                        )
                    )
                    emitEffect(Effect.VideoRecorded(uiEvent.uri, uiEvent.description))
                }
                pushInternal(InternalEvent.RecordingStopped)
            }

            Event.BackClicked -> emitEffect(Effect.NavigateBack)
        }
    }

    override fun reduce(event: InternalEvent, state: State): State =
        when (event) {
            InternalEvent.RecordingStarted -> state.copy(isRecording = true)
            InternalEvent.RecordingStopped -> state.copy(isRecording = false)
        }
}
