package com.ynd.domain

import com.ynd.domain.entity.VideoEntry
import com.ynd.domain.repository.VideoRepository
import javax.inject.Inject

class GetVideosUseCase @Inject constructor(
    private val videoRepository: VideoRepository
) {
    suspend operator fun invoke(): List<VideoEntry> {
        return videoRepository.getAllVideos().sortedByDescending { it.createdAt }
    }
}