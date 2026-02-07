package com.ynd.domain.repository

import com.ynd.domain.entity.VideoEntry


interface VideoRepository {

    suspend fun saveVideo(video: VideoEntry)

    suspend fun getAllVideos(): List<VideoEntry>

    suspend fun getVideoById(id: String): VideoEntry?
}