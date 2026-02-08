package com.ynd.video

import android.net.Uri
import android.util.Log
import android.view.ViewGroup
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
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun CameraScreen(
    onVideoRecorded: (Uri, String) -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val coroutineScope = rememberCoroutineScope()
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    
    val factory: VideoCreatorRepository = koinInject()
    
    val videoCapture = remember { mutableStateOf<VideoCapture<Recorder>?>(null) }
    
    val videoRecorderRepository: VideoRecorderRepository? = remember(videoCapture.value) {
        videoCapture.value?.let { factory.create(context, it) }
    }

    var isRecording by remember { mutableStateOf(false) }
    var description by remember { mutableStateOf("") }
    var showDescriptionDialog by remember { mutableStateOf(false) }
    var recordedUri: Uri? by remember { mutableStateOf(null) }

    val mainExecutor = ContextCompat.getMainExecutor(context)

    Column(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.weight(1f)) {
            AndroidView(
                factory = { ctx ->
                    val previewView = PreviewView(ctx).apply {
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                    }

                    cameraProviderFuture.addListener({
                        val cameraProvider = cameraProviderFuture.get()
                        val preview = Preview.Builder().build().also {
                            it.surfaceProvider = previewView.surfaceProvider
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
                    }, mainExecutor)

                    previewView
                },
                modifier = Modifier.fillMaxSize()
            )

            if (isRecording) {
                Text(
                    "Recording...",
                    color = androidx.compose.ui.graphics.Color.Red,
                    modifier = Modifier.align(Alignment.TopCenter).padding(16.dp)
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = {
                    coroutineScope.launch {
                        if (isRecording) {
                            val recordedFile = videoRecorderRepository?.stopRecording()
                            isRecording = false
                            if (recordedFile != null) {
                                recordedUri = Uri.fromFile(recordedFile)
                                showDescriptionDialog = true
                            }
                        } else {
                            val videoFile = File(
                                context.cacheDir,
                                SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS", Locale.US)
                                    .format(System.currentTimeMillis()) + ".mp4"
                            )
                            isRecording = true
                            videoRecorderRepository?.startRecording(videoFile)
                        }
                    }
                }
            ) {
                Text(if (isRecording) "Stop" else "Record")
            }
            
            Button(onClick = onBack) {
                Text("Back")
            }
        }
    }

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
                TextButton(
                    onClick = {
                        recordedUri?.let { onVideoRecorded(it, description) }
                        showDescriptionDialog = false
                    }
                ) {
                    Text("Save")
                }
            }
        )
    }
}
