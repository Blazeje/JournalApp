package com.ynd.data.repository

import android.content.Context
import androidx.camera.video.*
import androidx.core.content.ContextCompat
import com.ynd.domain.repository.VideoRecorderRepository
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.first
import java.io.File

class VideoRecorderRepositoryImpl(
    private val context: Context,
    private val videoCapture: VideoCapture<Recorder>
) : VideoRecorderRepository {

    private var activeRecording: Recording? = null
    private val finalizeEventFlow = MutableSharedFlow<VideoRecordEvent.Finalize>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    override suspend fun startRecording(outputFile: File) {
        val outputOptions = FileOutputOptions.Builder(outputFile).build()
        activeRecording = videoCapture.output
            .prepareRecording(context, outputOptions)
            .start(ContextCompat.getMainExecutor(context)) { event ->
                if (event is VideoRecordEvent.Finalize) {
                    finalizeEventFlow.tryEmit(event)
                }
            }
    }

    override suspend fun stopRecording(): File? {
        activeRecording?.stop()
        activeRecording = null
        
        val finalizeEvent = finalizeEventFlow.first()
        return if (!finalizeEvent.hasError()) {
            val uri = finalizeEvent.outputResults.outputUri
            uri.path?.let { File(it) }
        } else {
            null
        }
    }
}
