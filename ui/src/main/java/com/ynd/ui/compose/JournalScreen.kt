package com.ynd.ui.compose

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ynd.ui.JournalContract.Event
import com.ynd.ui.JournalContract.State
import com.ynd.video.VideoPlayer

@Composable
fun FeedScreen(
    state: State,
    onEvent: (Event) -> Unit
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onEvent(Event.AddClicked) }
            ) {
                Text("+")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            items(state.videos) { video ->
                Card(
                    modifier = Modifier.padding(8.dp)
                ) {
                    Column {
                        VideoPlayer(
                            videoUri = video.fileUri,
                            isPlaying = state.playingVideoId == video.id,
                            onClick = {
                                onEvent(
                                    Event.VideoClicked(video.id)
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                        )

                        Text(
                            modifier = Modifier.padding(16.dp),
                            text = video.description ?: "No description"
                        )
                    }
                }
            }
        }
    }
}
