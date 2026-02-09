package com.ynd.video

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material3.*
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import androidx.compose.runtime.collectAsState
import com.ynd.video.CameraContract.Effect
import com.ynd.video.compose.CameraScreen

class CameraFragment : Fragment() {

    private val viewModel: CameraViewModel by activityViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                MaterialTheme {
                    CameraScreen(
                        state = viewModel.state.collectAsState().value,
                        onEvent = viewModel::push
                    )
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.effects.collect { effect ->
                when (effect) {
                    is Effect.NavigateBack -> parentFragmentManager.popBackStack()
                    else -> parentFragmentManager.popBackStack()
                }
            }
        }
    }
}
