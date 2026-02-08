package com.ynd.ui

import android.Manifest
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import com.ynd.video.VideoPlayer
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class FirstFragment : Fragment() {

    private val viewModel: FeedViewModel by activityViewModel()

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions[Manifest.permission.CAMERA] == true) {
            openCamera()
        }
    }

    override fun onCreateView(
        inflater: android.view.LayoutInflater,
        container: android.view.ViewGroup?,
        savedInstanceState: Bundle?
    ): android.view.View {
        return ComposeView(requireContext()).apply {
            setContent {
                MaterialTheme {
                    FeedScreen(
                        viewModel = viewModel,
                        onAddClick = {
                            requestPermissionLauncher.launch(
                                arrayOf(
                                    Manifest.permission.CAMERA,
                                    Manifest.permission.RECORD_AUDIO
                                )
                            )
                        }
                    )
                }
            }
        }
    }

    private fun openCamera() {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, CameraFragment())
            .addToBackStack(null)
            .commit()
    }
}

@Composable
fun FeedScreen(
    viewModel: FeedViewModel,
    onAddClick: () -> Unit
) {
    val videos by viewModel.videos.collectAsState()
    var playingVideoId by remember { mutableStateOf<String?>(null) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onAddClick) {
                Text("+")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(videos) { video ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        Column {
                            VideoPlayer(
                                videoUri = video.fileUri,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp),
                                isPlaying = playingVideoId == video.id,
                                onClick = {
                                    playingVideoId = if (playingVideoId == video.id) null else video.id
                                }
                            )
                            
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = video.description ?: "No description",
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
