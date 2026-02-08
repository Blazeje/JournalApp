package com.ynd.domain

import com.ynd.domain.entity.VideoEntry
import com.ynd.domain.repository.VideoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetVideosUseCase(
    private val videoRepository: VideoRepository
) {
    operator fun invoke(): Flow<List<VideoEntry>> =
        videoRepository.observeVideos()
            .map { list ->
                list.sortedByDescending { it.createdAt }
            }
}
