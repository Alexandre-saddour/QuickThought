package com.quickthought.domain.usecase

import com.quickthought.domain.model.ProcessingResult
import com.quickthought.domain.model.SpeechResult
import com.quickthought.domain.repository.AiRepository
import com.quickthought.domain.repository.SpeechRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Use case that orchestrates the process of capturing voice,
 * transcribing it, and processing it through AI to extract structured items.
 */
class ProcessVoiceMemoUseCase @Inject constructor(
    private val speechRepository: SpeechRepository,
    private val aiRepository: AiRepository
) {
    /**
     * Start listening for voice input
     */
    fun startListening(): Flow<SpeechResult> {
        return speechRepository.startListening()
    }
    
    /**
     * Stop listening for voice input
     */
    fun stopListening() {
        speechRepository.stopListening()
    }
    
    /**
     * Process transcribed text through AI
     */
    suspend fun processTranscription(transcription: String): ProcessingResult {
        return aiRepository.processTranscription(transcription = transcription)
    }
    
    /**
     * Check if microphone permission is granted
     */
    fun hasPermission(): Boolean {
        return speechRepository.hasPermission()
    }
}
