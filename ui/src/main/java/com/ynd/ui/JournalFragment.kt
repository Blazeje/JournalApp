package com.ynd.ui

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.ComposeView
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.ynd.ui.compose.JournalScreen
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import com.ynd.ui.JournalContract.Effect
import com.ynd.video.CameraFragment
import java.io.File
import androidx.core.net.toUri

class JournalFragment : Fragment() {

    private val viewModel: JournalViewModel by activityViewModel()

    private val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            if (permissions[Manifest.permission.CAMERA] == true) openCamera()
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        setContent {
            MaterialTheme {
                val state by viewModel.state.collectAsState()

                LaunchedEffect(Unit) {
                    viewModel.effects.collect { effect ->
                        when (effect) {
                            Effect.OpenCamera -> permissionLauncher.launch(
                                arrayOf(
                                    Manifest.permission.CAMERA,
                                    Manifest.permission.RECORD_AUDIO
                                )
                            )
                            is Effect.ShareVideoIntent -> shareVideo(effect.fileUri)
                        }
                    }
                }

                JournalScreen(
                    state = state,
                    onEvent = viewModel::push
                )
            }
        }
    }

    private fun shareVideo(fileUri: String) {
        val file = File(fileUri.toUri().path ?: return)
        if (!file.exists()) return

        val contentUri = FileProvider.getUriForFile(
            requireContext(),
            "${requireContext().packageName}.fileprovider",
            file
        )

        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_STREAM, contentUri)
            type = "video/mp4"
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        startActivity(Intent.createChooser(shareIntent, "Share Video"))
    }

    private fun openCamera() {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, CameraFragment())
            .addToBackStack(null)
            .commit()
    }
}
