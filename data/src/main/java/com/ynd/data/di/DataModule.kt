package com.ynd.data.di

import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.ynd.data.db.JournalDatabase
import com.ynd.data.repository.VideoCreatorRepositoryImpl
import com.ynd.data.repository.VideoRepositoryImpl
import com.ynd.domain.repository.VideoCreatorRepository
import com.ynd.domain.repository.VideoRepository
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val dataModule = module {
    single {
        val driver = AndroidSqliteDriver(JournalDatabase.Schema, androidContext(), "journal.db")
        JournalDatabase(driver)
    }

    single { get<JournalDatabase>().videosQueries }

    singleOf(::VideoRepositoryImpl) bind VideoRepository::class
    
    singleOf(::VideoCreatorRepositoryImpl) bind VideoCreatorRepository::class
}
