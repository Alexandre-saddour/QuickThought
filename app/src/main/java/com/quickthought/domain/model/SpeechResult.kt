package com.quickthought.domain.model

/**
 * Result wrapper for speech recognition operations
 */
sealed class SpeechResult {
    data class Success(val text: String) : SpeechResult()
    data class Error(val message: String) : SpeechResult()
}
