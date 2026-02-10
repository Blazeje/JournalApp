package com.ynd.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ynd.ui.JournalContract.Event
import com.ynd.ui.JournalContract.State
import com.ynd.video.compose.VideoPlayer
import com.ynd.shared.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JournalScreen(state: State, onEvent: (Event) -> Unit) {
    var playingVideoId by remember { mutableStateOf<String?>(null) }

    val backgroundGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF1e3c72),
            Color(0xFF2a5298),
            Color.Black
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundGradient)
    ) {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = stringResource(R.string.title),
                            color = Color.White,
                            style = MaterialTheme.typography.headlineMedium
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent
                    )
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { onEvent(Event.AddClicked) },
                    containerColor = Color(0xFF2a5298),
                    contentColor = Color.White
                ) {
                    Text(text = stringResource(R.string.add_video))
                }
            }
        ) { padding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                items(state.videos) { video ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        VideoPlayer(
                            videoUri = video.fileUri,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(300.dp)
                                .padding(horizontal = 96.dp)
                                .clip(RoundedCornerShape(24.dp)),
                            isPlaying = playingVideoId == video.id,
                            onClick = {
                                playingVideoId = if (playingVideoId == video.id) null else video.id
                                onEvent(Event.VideoClicked(video.id))
                            },
                            onLongClick = {
                                onEvent(Event.ShareVideo(video))
                            }
                        )
                        Text(
                            text = video.description ?: stringResource(R.string.no_description),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.headlineSmall,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}
