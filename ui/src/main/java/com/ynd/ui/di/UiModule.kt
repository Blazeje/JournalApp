package com.ynd.ui.di

import com.ynd.domain.GetVideosUseCase
import com.ynd.domain.RecordVideoUseCase
import com.ynd.ui.JournalViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val uiModule = module {
    factory { GetVideosUseCase(get()) }
    factory { RecordVideoUseCase(get()) }
    viewModel { JournalViewModel(get(), get()) }
}
