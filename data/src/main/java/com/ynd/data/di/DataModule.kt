package com.ynd.data.di

import com.ynd.data.local.VideoLocalDataSource
import com.ynd.data.repository.VideoRepositoryImpl
import com.ynd.domain.repository.VideoRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideVideoLocalDataSource(): VideoLocalDataSource {
        return VideoLocalDataSource()
    }

    @Provides
    @Singleton
    fun provideVideoRepository(
        localDataSource: VideoLocalDataSource
    ): VideoRepository {
        return VideoRepositoryImpl(localDataSource)
    }
}
