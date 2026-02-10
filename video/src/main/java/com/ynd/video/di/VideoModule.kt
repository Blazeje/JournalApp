package com.ynd.video.di

import com.ynd.domain.RecordVideoUseCase
import com.ynd.video.CameraViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val videoModule = module {
    factory { RecordVideoUseCase(get()) }
    viewModel { CameraViewModel(get(), get()) }
}