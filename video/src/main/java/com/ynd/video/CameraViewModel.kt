package com.ynd.video

import androidx.lifecycle.viewModelScope
import com.ynd.domain.RecordVideoUseCase
import com.ynd.domain.entity.VideoEntry
import com.ynd.domain.CoroutineDispatcherProvider
import com.ynd.video.CameraContract.Event
import com.ynd.video.CameraContract.Effect
import com.ynd.video.CameraContract.State
import com.ynd.video.CameraContract.InternalEvent
import com.ynd.shared.MviViewModel
import kotlinx.coroutines.launch

class CameraViewModel(
    private val recordVideoUseCase: RecordVideoUseCase,
    dispatcherProvider: CoroutineDispatcherProvider,
) : MviViewModel<State, Event, InternalEvent, Effect>(
    initialState = State(),
    eventsDispatcher = dispatcherProvider.unconfined,
    uiDispatcher = dispatcherProvider.main
) {

    override fun onHandleUiEvent(uiEvent: Event, state: State) {
        when (uiEvent) {

            Event.StartRecording ->
                pushInternal(InternalEvent.RecordingStarted)

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

            Event.BackClicked -> {
                if (state.isRecording) {
                    push(Event.CancelRecording)
                } else {
                    emitEffect(Effect.NavigateBack)
                }
            }

            Event.CancelRecording -> {
                pushInternal(InternalEvent.RecordingStopped)
                emitEffect(Effect.NavigateBack)
            }
        }
    }

    override fun reduce(event: InternalEvent, state: State): State =
        when (event) {
            InternalEvent.RecordingStarted -> state.copy(isRecording = true)
            InternalEvent.RecordingStopped -> state.copy(isRecording = false)
        }
}
