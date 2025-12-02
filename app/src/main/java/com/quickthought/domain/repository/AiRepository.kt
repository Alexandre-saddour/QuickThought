package com.quickthought.domain.repository

import com.quickthought.domain.model.ProcessingResult

/**
 * Repository interface for AI processing operations
 */
interface AiRepository {
    /**
     * Process transcribed text and extract structured items
     * @param transcription The text to process
     * @return Result containing extracted items or error
     */
    suspend fun processTranscription(transcription: String): ProcessingResult
}
