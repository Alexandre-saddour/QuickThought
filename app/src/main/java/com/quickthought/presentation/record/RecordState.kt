package com.quickthought.presentation.record

import com.quickthought.domain.model.QuickThoughtItem

/**
 * UI state for the Record screen
 */
data class RecordState(
    val isRecording: Boolean = false,
    val isProcessing: Boolean = false,
    val transcription: String = "",
    val items: List<QuickThoughtItem> = emptyList(),
    val error: String? = null,
    val hasPermission: Boolean = false
)
