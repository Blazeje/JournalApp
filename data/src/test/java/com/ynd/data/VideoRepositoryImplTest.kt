package com.ynd.data

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.ynd.data.db.JournalDatabase
import com.ynd.data.repository.VideoRepositoryImpl
import com.ynd.domain.entity.VideoEntry
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class VideoRepositoryImplTest {

    private val sut: VideoRepositoryImpl
    private val database: JournalDatabase

    init {
        val driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        
        JournalDatabase.Schema.create(driver)

        database = JournalDatabase(driver)
        sut = VideoRepositoryImpl(database.videosQueries)
    }

    @Test
    fun `GIVEN videos in database WHEN observeVideos is collected THEN it returns mapped VideoEntry`() = runTest {
        val fileUri = "file://video.mp4"
        val description = "Test video"
        val createdAt = 123L
        
        database.videosQueries.insertVideo(fileUri, description, createdAt)

        val result = sut.observeVideos().first()

        assertEquals(1, result.size)
        val video = result[0]
        assertEquals(fileUri, video.fileUri)
        assertEquals(description, video.description)
        assertEquals(createdAt, video.createdAt)
        assertEquals("1", video.id)
    }

    @Test
    fun `WHEN insertVideo is called THEN it inserts video`() = runTest {
        val video = VideoEntry(
            fileUri = "file://video.mp4",
            description = "New video",
            createdAt = 111L
        )

        sut.insertVideo(video)

        val videos = database.videosQueries.selectAll().executeAsList()
        assertEquals(1, videos.size)
        assertEquals("file://video.mp4", videos[0].fileUri)
    }

    @Test
    fun `WHEN clearVideos is called THEN it deletes all`() = runTest {
        database.videosQueries.insertVideo("uri", "desc", 0L)
        
        sut.clearVideos()

        val videos = database.videosQueries.selectAll().executeAsList()
        assertEquals(0, videos.size)
    }
}
