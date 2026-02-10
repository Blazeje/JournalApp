package com.ynd.ui

import com.ynd.domain.GetVideosUseCase
import com.ynd.domain.entity.VideoEntry
import com.ynd.domain.CoroutineDispatcherProvider
import com.ynd.ui.JournalContract.Effect
import com.ynd.ui.JournalContract.Event
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

@ExperimentalCoroutinesApi
class JournalViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private val dispatcherProvider = object : CoroutineDispatcherProvider {
        override val main: CoroutineDispatcher = testDispatcher
        override val io: CoroutineDispatcher = testDispatcher
        override val default: CoroutineDispatcher = testDispatcher
        override val unconfined: CoroutineDispatcher = testDispatcher
    }

    private val fakeVideos = listOf(
        VideoEntry(id = "1", fileUri = "file://video1.mp4", description = "Video 1", createdAt = 200L),
        VideoEntry(
            id = "2",
            fileUri = "file://video2.mp4",
            description = "Video 2",
            createdAt = 100L
        )
    )

    init {
        Dispatchers.setMain(testDispatcher)
    }

    private val getVideosUseCase: GetVideosUseCase = mockk {
        every { this@mockk() } returns flowOf(fakeVideos)
    }

    private val sut = JournalViewModel(
        getVideosUseCase = getVideosUseCase,
        dispatcherProvider = dispatcherProvider
    )


    @Test
    fun `GIVEN initial state WHEN initialized THEN videos are loaded`() = runTest(testDispatcher) {
        advanceUntilIdle()

        val state = sut.state.value
        assertEquals(fakeVideos, state.videos)
        assertEquals(null, state.playingVideoId)
    }

    @Test
    fun `WHEN AddClicked event THEN OpenCamera effect is emitted`() = runTest(testDispatcher) {
        sut.push(Event.AddClicked)
        
        val effect = sut.effects.first()
        assertTrue(effect is Effect.OpenCamera)
    }

    @Test
    fun `WHEN VideoClicked event THEN playingVideoId toggles`() = runTest(testDispatcher) {
        sut.push(Event.VideoClicked("1"))
        advanceUntilIdle()
        assertEquals("1", sut.state.value.playingVideoId)

        sut.push(Event.VideoClicked("1"))
        advanceUntilIdle()
        assertEquals(null, sut.state.value.playingVideoId)

        sut.push(Event.VideoClicked("2"))
        advanceUntilIdle()
        assertEquals("2", sut.state.value.playingVideoId)
    }

    @Test
    fun `WHEN ShareVideo event THEN ShareVideoIntent effect is emitted`() = runTest(testDispatcher) {
        val video = fakeVideos[0]
        sut.push(Event.ShareVideo(video))

        val effect = sut.effects.first()
        assertTrue(effect is Effect.ShareVideoIntent)
        assertEquals(video.fileUri, (effect as Effect.ShareVideoIntent).fileUri)
    }
}
