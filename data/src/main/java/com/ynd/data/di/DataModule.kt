package com.ynd.data.di

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.ynd.data.db.JournalDatabase
import com.ynd.data.repository.VideoRepositoryImpl
import com.ynd.domain.repository.VideoRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideSqlDriver(
        @ApplicationContext context: Context
    ): SqlDriver =
        AndroidSqliteDriver(
            schema = JournalDatabase.Schema,
            context = context,
            name = "journal.db"
        )

    @Provides
    @Singleton
    fun provideDatabase(
        driver: SqlDriver
    ): JournalDatabase =
        JournalDatabase(driver)

    @Provides
    @Singleton
    fun provideVideoRepository(
        database: JournalDatabase
    ): VideoRepository =
        VideoRepositoryImpl(database.videosQueries)
}

