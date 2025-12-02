package com.quickthought.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * DTO for Gemini API response items
 */
@Serializable
data class GeminiItemDto(
    @SerialName("type")
    val type: String,
    
    @SerialName("title")
    val title: String,
    
    @SerialName("description")
    val description: String,
    
    @SerialName("dueDate")
    val dueDate: String? = null,
    
    @SerialName("dateTime")
    val dateTime: String? = null,
    
    @SerialName("durationMinutes")
    val durationMinutes: Int? = null,
    
    @SerialName("priority")
    val priority: String? = null,
    
    @SerialName("tags")
    val tags: List<String>? = null
)

@Serializable
data class GeminiResponseDto(
    @SerialName("items")
    val items: List<GeminiItemDto>
)
