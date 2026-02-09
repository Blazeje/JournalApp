package com.ynd.ui.di

import com.ynd.domain.GetVideosUseCase
import com.ynd.ui.JournalViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val uiModule = module {
    factory { GetVideosUseCase(get()) }
    viewModel { JournalViewModel(get()) }
}
