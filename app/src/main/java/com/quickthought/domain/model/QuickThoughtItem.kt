package com.quickthought.domain.model

import kotlinx.serialization.Serializable

/**
 * Sealed interface representing different types of structured items
 * that can be extracted from voice memos.
 */
@Serializable
sealed interface QuickThoughtItem {
    val title: String
    val description: String

    /**
     * Represents a task or to-do item
     */
    @Serializable
    data class Task(
        override val title: String,
        override val description: String,
        val dueDate: String? = null,
        val priority: Priority = Priority.MEDIUM
    ) : QuickThoughtItem {
        enum class Priority {
            LOW, MEDIUM, HIGH
        }
    }

    /**
     * Represents a calendar appointment or meeting
     */
    @Serializable
    data class Appointment(
        override val title: String,
        override val description: String,
        val dateTime: String,
        val durationMinutes: Int = 60
    ) : QuickThoughtItem

    /**
     * Represents a general idea or note
     */
    @Serializable
    data class Idea(
        override val title: String,
        override val description: String,
        val tags: List<String> = emptyList()
    ) : QuickThoughtItem
}
