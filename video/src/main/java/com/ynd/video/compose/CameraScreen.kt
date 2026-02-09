package com.ynd.video.compose

import android.net.Uri
import android.util.Log
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.video.*
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.ynd.domain.repository.VideoRecorderRepository
import com.ynd.domain.repository.VideoCreatorRepository
import com.ynd.video.CameraContract.State
import com.ynd.video.CameraContract.Event
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CameraScreen(
    state: State,
    onEvent: (Event) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val coroutineScope = rememberCoroutineScope()
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }

    val factory: VideoCreatorRepository = koinInject()

    var showDescriptionDialog by remember { mutableStateOf(false) }
    var recordedUri by remember { mutableStateOf<Uri?>(null) }

    val videoCapture = remember { mutableStateOf<VideoCapture<Recorder>?>(null) }
    val videoRecorderRepository: VideoRecorderRepository? = remember(videoCapture.value) {
        videoCapture.value?.let { factory.create(context, it) }
    }

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
                            text = "My Journal",
                            color = Color.White,
                            style = MaterialTheme.typography.headlineMedium
                        )
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = Color.Transparent
                    )
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(16.dp)
                        .clip(RoundedCornerShape(24.dp))
                ) {
                    AndroidView(
                        factory = { ctx ->
                            val previewView = PreviewView(ctx)
                            previewView.layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT)

                            cameraProviderFuture.addListener({
                                val cameraProvider = cameraProviderFuture.get()
                                val preview = Preview.Builder().build().also {
                                    it.setSurfaceProvider(previewView.surfaceProvider)
                                }

                                val recorder = Recorder.Builder()
                                    .setQualitySelector(QualitySelector.from(Quality.HIGHEST))
                                    .build()
                                videoCapture.value = VideoCapture.withOutput(recorder)

                                try {
                                    cameraProvider.unbindAll()
                                    cameraProvider.bindToLifecycle(
                                        lifecycleOwner,
                                        CameraSelector.DEFAULT_BACK_CAMERA,
                                        preview,
                                        videoCapture.value
                                    )
                                } catch (e: Exception) {
                                    Log.e("CameraScreen", "Binding failed", e)
                                }
                            }, ContextCompat.getMainExecutor(context))

                            previewView
                        },
                        modifier = Modifier.fillMaxSize()
                    )

                    if (state.isRecording) {
                        Surface(
                            color = Color.Black.copy(alpha = 0.6f),
                            shape = MaterialTheme.shapes.medium,
                            modifier = Modifier
                                .align(Alignment.TopCenter)
                                .padding(16.dp)
                        ) {
                            Text(
                                "Recording...",
                                color = Color.Red,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 32.dp, start = 16.dp, end = 16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = {
                            coroutineScope.launch {
                                if (state.isRecording) {
                                    val file = videoRecorderRepository?.stopRecording()
                                    if (file != null) {
                                        recordedUri = Uri.fromFile(file)
                                        showDescriptionDialog = true
                                    }
                                    onEvent(Event.StartRecording)
                                } else {
                                    val videoFile = File(context.cacheDir, "${System.currentTimeMillis()}.mp4")
                                    onEvent(Event.StartRecording)
                                    videoRecorderRepository?.startRecording(videoFile)
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (state.isRecording) Color.Red else Color(0xFF2a5298),
                            contentColor = Color.White
                        ),
                        modifier = Modifier.height(56.dp).weight(1f).padding(horizontal = 8.dp)
                    ) {
                        Text(if (state.isRecording) "Stop" else "Record")
                    }

                    Button(
                        onClick = { onEvent(Event.BackClicked) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White.copy(alpha = 0.2f),
                            contentColor = Color.White
                        ),
                        modifier = Modifier.height(56.dp).weight(1f).padding(horizontal = 8.dp)
                    ) {
                        Text("Back")
                    }
                }
            }
        }
    }

    if (showDescriptionDialog && recordedUri != null) {
        AddVideoDialog(
            recordedUri = recordedUri!!,
            onSave = { description ->
                onEvent(Event.StopRecording(recordedUri!!, description))
                showDescriptionDialog = false
            },
            onDismiss = { showDescriptionDialog = false }
        )
    }
}
