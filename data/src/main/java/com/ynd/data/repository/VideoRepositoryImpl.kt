package com.ynd.data.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.ynd.data.db.VideosQueries
import com.ynd.data.local.VideoLocalDataSource
import com.ynd.domain.entity.VideoEntry
import com.ynd.domain.repository.VideoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class VideoRepositoryImpl(
    private val queries: VideosQueries
) : VideoRepository {

    override fun observeVideos(): Flow<List<VideoEntry>> =
        queries.selectAll()
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { list ->
                list.map {
                    VideoEntry(
                        fileUri = it.fileUri,
                        description = it.description
                    )
                }
            }

    override suspend fun insertVideo(video: VideoEntry) {
        queries.insertVideo(
            fileUri = video.fileUri,
            description = video.description
        )
    }

    override suspend fun clearVideos() {
        queries.deleteAll()
    }
}

