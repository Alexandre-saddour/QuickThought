package com.quickthought.domain.repository

import com.quickthought.domain.model.SpeechResult
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for speech recognition operations
 */
interface SpeechRepository {
    /**
     * Start listening for speech input
     * @return Flow emitting speech recognition results
     */
    fun startListening(): Flow<SpeechResult>
    
    /**
     * Stop listening for speech input
     */
    fun stopListening()
    
    /**
     * Check if microphone permission is granted
     */
    fun hasPermission(): Boolean
}
