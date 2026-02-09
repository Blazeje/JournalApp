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
fun JournalScreen(state: State, onEvent: (Event) -> Unit) {
    var playingVideoId by remember { mutableStateOf<String?>(null) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { onEvent(Event.AddClicked) }) {
                Text("+")
            }
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.fillMaxSize().padding(padding)) {
            items(state.videos) { video ->
                Card(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                    Column {
                        VideoPlayer(
                            videoUri = video.fileUri,
                            modifier = Modifier.fillMaxWidth().height(200.dp),
                            isPlaying = playingVideoId == video.id,
                            onClick = {
                                playingVideoId = if (playingVideoId == video.id) null else video.id
                                onEvent(Event.VideoClicked(video.id))
                            }
                        )
                        Text(video.description ?: "No description", modifier = Modifier.padding(16.dp))
                    }
                }
            }
        }
    }
}

