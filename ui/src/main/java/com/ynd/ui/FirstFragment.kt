package com.ynd.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FirstFragment : Fragment() {

    private val viewModel: FeedViewModel by viewModels()


    override fun onCreateView(
        inflater: android.view.LayoutInflater,
        container: android.view.ViewGroup?,
        savedInstanceState: Bundle?
    ): android.view.View {
        return ComposeView(requireContext()).apply {
            setContent {
                MaterialTheme {
                    FeedScreen(viewModel)
                }
            }
        }
    }
}

@Composable
fun FeedScreen(viewModel: FeedViewModel) {
    val videos by viewModel.videos.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        Button(
            onClick = { viewModel.addVideoPlaceholder() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(text = "Add Video")
        }

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(videos) { video ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = video.description ?: "No description")
                        Text(text = video.fileUri)
                    }
                }
            }
        }
    }
}
