package com.ynd.video

import android.net.Uri

interface CameraContract {

    data class State(
        val isRecording: Boolean = false
    )

    sealed class Event {
        object StartRecording : Event()
        data class StopRecording(val uri: Uri, val description: String) : Event()
        object BackClicked : Event()
    }

    sealed class InternalEvent {
        object RecordingStarted : InternalEvent()
        object RecordingStopped : InternalEvent()
    }

    sealed class Effect {
        data class VideoRecorded(val uri: Uri, val description: String) : Effect()
        object NavigateBack : Effect()
    }
}