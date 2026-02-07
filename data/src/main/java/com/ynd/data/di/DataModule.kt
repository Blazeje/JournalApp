package com.ynd.data.di

import com.ynd.data.repository.VideoRepositoryImpl
import com.ynd.domain.GetVideosUseCase
import com.ynd.domain.RecordVideoUseCase
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
    fun provideVideoRepository(
        impl: VideoRepositoryImpl
    ): VideoRepository = impl

    @Provides
    fun provideGetVideosUseCase(
        repository: VideoRepository
    ): GetVideosUseCase = GetVideosUseCase(repository)

    @Provides
    fun provideRecordVideoUseCase(
        repository: VideoRepository
    ): RecordVideoUseCase = RecordVideoUseCase(repository)
}

