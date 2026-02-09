package com.ynd.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.ynd.video.CameraScreen
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import com.ynd.ui.JournalContract.Event

class CameraFragment : Fragment() {

    private val viewModel: JournalViewModel by activityViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                MaterialTheme {
                    CameraScreen(
                        onVideoRecorded = { uri, description ->
                            viewModel.push(
                                Event.VideoRecorded(
                                    uri = uri.toString(),
                                    description = description
                                )
                            )
                        },
                        onBack = {
                            parentFragmentManager.popBackStack()
                        }
                    )

                }
            }
        }
    }
}
