package com.ynd.video

import android.net.Uri
import android.util.Log
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.video.*
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
    var description by remember { mutableStateOf("") }

    val videoCapture = remember { mutableStateOf<VideoCapture<Recorder>?>(null) }
    val videoRecorderRepository: VideoRecorderRepository? = remember(videoCapture.value) {
        videoCapture.value?.let { factory.create(context, it) }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Camera Preview
        Box(modifier = Modifier.weight(1f)) {
            AndroidView(factory = { ctx ->
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
            }, modifier = Modifier.fillMaxSize())

            if (state.isRecording) {
                Text(
                    "Recording...",
                    color = androidx.compose.ui.graphics.Color.Red,
                    modifier = Modifier.align(Alignment.TopCenter).padding(16.dp)
                )
            }
        }

        // Buttons
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = {
                coroutineScope.launch {
                    if (state.isRecording) {
                        val file = videoRecorderRepository?.stopRecording()
                        if (file != null) {
                            recordedUri = Uri.fromFile(file)
                            showDescriptionDialog = true
                        }
                        onEvent(Event.StartRecording) // update recording state
                    } else {
                        val videoFile = File(context.cacheDir, "${System.currentTimeMillis()}.mp4")
                        onEvent(Event.StartRecording)
                        videoRecorderRepository?.startRecording(videoFile)
                    }
                }
            }) {
                Text(if (state.isRecording) "Stop" else "Record")
            }

            Button(onClick = { onEvent(Event.BackClicked) }) { Text("Back") }
        }
    }

    // Description AlertDialog
    if (showDescriptionDialog) {
        AlertDialog(
            onDismissRequest = { showDescriptionDialog = false },
            title = { Text("Add Description") },
            text = {
                TextField(
                    value = description,
                    onValueChange = { description = it },
                    placeholder = { Text("Enter description...") }
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    recordedUri?.let { uri ->
                        onEvent(Event.StopRecording(uri, description))
                    }
                    showDescriptionDialog = false
                    description = ""
                }) { Text("Save") }
            },
            dismissButton = {
                TextButton(onClick = {
                    showDescriptionDialog = false
                    description = ""
                }) { Text("Cancel") }
            }
        )
    }
}
