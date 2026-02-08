package com.ynd.domain.repository

import java.io.File

interface VideoRecorderRepository {
    suspend fun startRecording(outputFile: File)
    suspend fun stopRecording(): File?
}
