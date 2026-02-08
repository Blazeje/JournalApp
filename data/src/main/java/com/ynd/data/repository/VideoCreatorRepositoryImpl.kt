package com.ynd.data.repository

import android.content.Context
import androidx.camera.video.Recorder
import androidx.camera.video.VideoCapture
import com.ynd.domain.repository.VideoRecorderRepository
import com.ynd.domain.repository.VideoCreatorRepository

class VideoCreatorRepositoryImpl : VideoCreatorRepository {
    override fun create(context: Any, captureObject: Any): VideoRecorderRepository {
        return VideoRecorderRepositoryImpl(
            context as Context,
            captureObject as VideoCapture<Recorder>
        )
    }
}
