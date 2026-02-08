package com.ynd.domain

import com.ynd.domain.entity.VideoEntry
import com.ynd.domain.repository.VideoRepository
import javax.inject.Inject


class RecordVideoUseCase @Inject constructor(
    private val videoRepository: VideoRepository
) {
    suspend operator fun invoke(videoEntry: VideoEntry) {
        videoRepository.insertVideo(videoEntry)
    }
}
