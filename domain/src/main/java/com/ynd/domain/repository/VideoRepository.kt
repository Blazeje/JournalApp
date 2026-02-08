package com.ynd.domain.repository

import com.ynd.domain.entity.VideoEntry
import kotlinx.coroutines.flow.Flow


interface VideoRepository {
    fun observeVideos(): Flow<List<VideoEntry>>
    suspend fun insertVideo(video: VideoEntry)
    suspend fun clearVideos()
}
