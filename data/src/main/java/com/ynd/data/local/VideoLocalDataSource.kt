package com.ynd.data.local

import com.ynd.domain.entity.VideoEntry
import javax.inject.Inject

class VideoLocalDataSource @Inject constructor() {

    private val videoList = mutableListOf<VideoEntry>()

    suspend fun saveVideo(video: VideoEntry) {
        videoList.add(video)
    }

    suspend fun getAllVideos(): List<VideoEntry> {
        return videoList.toList()
    }

    suspend fun getVideoById(id: String): VideoEntry? {
        return videoList.find { it.id == id }
    }
}