package com.quickthought.domain.model

/**
 * Result wrapper for AI processing operations
 */
sealed class ProcessingResult {
    data class Success(val items: List<QuickThoughtItem>) : ProcessingResult()
    data class Error(val message: String) : ProcessingResult()
}
