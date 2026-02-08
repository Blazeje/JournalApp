package com.ynd.data.di

import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.ynd.data.db.JournalDatabase
import com.ynd.data.repository.VideoRepositoryImpl
import com.ynd.domain.repository.VideoRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModule = module {
    single {
        val driver = AndroidSqliteDriver(JournalDatabase.Schema, androidContext(), "journal.db")
        JournalDatabase(driver)
    }

    single { get<JournalDatabase>().videosQueries }

    single<VideoRepository> { VideoRepositoryImpl(get()) }
}
