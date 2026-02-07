package com.ynd.data.repository

import com.ynd.data.local.VideoLocalDataSource
import com.ynd.domain.entity.VideoEntry
import com.ynd.domain.repository.VideoRepository
import javax.inject.Inject

class VideoRepositoryImpl @Inject constructor(
    private val localDataSource: VideoLocalDataSource
) : VideoRepository {

    override suspend fun saveVideo(video: VideoEntry) {
        localDataSource.saveVideo(video)
    }

    override suspend fun getAllVideos(): List<VideoEntry> {
        return localDataSource.getAllVideos()
    }

    override suspend fun getVideoById(id: String): VideoEntry? {
        return localDataSource.getVideoById(id)
    }
}
