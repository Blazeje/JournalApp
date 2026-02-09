package com.ynd.ui

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.ynd.ui.compose.JournalScreen
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import com.ynd.ui.JournalContract.Effect
import com.ynd.video.CameraFragment

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

    private fun openCamera() {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, CameraFragment())
            .addToBackStack(null)
            .commit()
    }
}
