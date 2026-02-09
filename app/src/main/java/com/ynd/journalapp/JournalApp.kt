package com.ynd.journalapp

import android.app.Application
import com.ynd.data.di.dataModule
import com.ynd.ui.di.uiModule
import com.ynd.video.di.videoModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class JournalApp : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@JournalApp)
            modules(dataModule, uiModule, videoModule)
        }
    }
}
