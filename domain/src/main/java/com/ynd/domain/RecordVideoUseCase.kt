package com.ynd.domain

import com.ynd.domain.entity.VideoEntry
import com.ynd.domain.repository.VideoRepository


class RecordVideoUseCase(
    private val videoRepository: VideoRepository
) {
    suspend operator fun invoke(videoEntry: VideoEntry) {
        videoRepository.saveVideo(videoEntry)
    }
}
