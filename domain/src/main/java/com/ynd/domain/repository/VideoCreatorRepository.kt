package com.ynd.domain.repository

interface VideoCreatorRepository {
    fun create(context: Any, captureObject: Any): VideoRecorderRepository
}
