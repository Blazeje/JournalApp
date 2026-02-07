package com.ynd.domain

import com.ynd.domain.entity.VideoEntry
import com.ynd.domain.repository.VideoRepository

class GetVideosUseCase(
    private val videoRepository: VideoRepository
) {
    suspend operator fun invoke(): List<VideoEntry> {
        return videoRepository.getAllVideos().sortedByDescending { it.createdAt }
    }
}