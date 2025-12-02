package com.quickthought.data.repository

import com.quickthought.data.source.GeminiDataSource
import com.quickthought.domain.model.ProcessingResult
import com.quickthought.domain.repository.AiRepository
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of AiRepository
 */
@Singleton
class AiRepositoryImpl @Inject constructor(
    private val geminiDataSource: GeminiDataSource
) : AiRepository {
    
    override suspend fun processTranscription(transcription: String): ProcessingResult {
        return geminiDataSource.processTranscription(transcription = transcription)
    }
}
