package com.quickthought.data.source

import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.generationConfig
import com.quickthought.BuildConfig
import com.quickthought.data.dto.GeminiItemDto
import com.quickthought.data.dto.GeminiResponseDto
import com.quickthought.domain.model.ProcessingResult
import com.quickthought.domain.model.QuickThoughtItem
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Data source for AI processing using Google Gemini API
 */
@Singleton
class GeminiDataSource @Inject constructor() {
    
    companion object {
        private const val MODEL_NAME = "gemini-2.5-flash-lite"
        private const val TEMPERATURE = 0.7f
        private const val MAX_OUTPUT_TOKENS = 2048
        
        private const val SYSTEM_PROMPT = """You are an AI assistant that analyzes voice memo transcriptions and extracts structured information.

Your task is to identify and classify items from the transcription into three types:
1. TASK - To-do items, things to buy, actions to take
2. APPOINTMENT - Scheduled events, meetings, appointments with specific times
3. IDEA - General thoughts, notes, ideas, or observations

Extract ALL items from the transcription. For each item, provide:
- type: "TASK", "APPOINTMENT", or "IDEA"
- title: A concise title (max 50 characters)
- description: Full details from the transcription
- For TASK: dueDate (ISO 8601 format if mentioned, null otherwise), priority ("LOW", "MEDIUM", or "HIGH")
- For APPOINTMENT: dateTime (ISO 8601 format), durationMinutes (default 60)
- For IDEA: tags (array of relevant keywords)

Return ONLY valid JSON in this exact format:
{
  "items": [
    {
      "type": "TASK",
      "title": "example title",
      "description": "example description",
      "dueDate": "example date",
      "priority": "example priority"
    }
  ]
}

If no items are found, return: {"items": []}

Do not include any text before or after the JSON. Only output valid JSON."""
    }
    
    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        coerceInputValues = true
    }
    
    private val model: GenerativeModel by lazy {
        if (BuildConfig.GEMINI_API_KEY.isBlank() || BuildConfig.GEMINI_API_KEY == "your_api_key_here") {
            throw IllegalStateException("Gemini API key not configured. Please add GEMINI_API_KEY to local.properties")
        }
        
        GenerativeModel(
            modelName = MODEL_NAME,
            apiKey = BuildConfig.GEMINI_API_KEY,
            generationConfig = generationConfig {
                temperature = TEMPERATURE
                maxOutputTokens = MAX_OUTPUT_TOKENS
            }
        )
    }
    
    /**
     * Process transcription through Gemini API
     */
    suspend fun processTranscription(transcription: String): ProcessingResult {
        return try {
            val prompt = """$SYSTEM_PROMPT

Transcription: "$transcription"

JSON Response:"""
            
            val response = model.generateContent(prompt)
            val responseText = response.text ?: return ProcessingResult.Error(
                message = "Empty response from AI"
            )
            
            // Extract JSON from response (remove markdown code blocks if present)
            val jsonText = responseText
                .trim()
                .removePrefix("```json")
                .removePrefix("```")
                .removeSuffix("```")
                .trim()
            
            val geminiResponse = json.decodeFromString<GeminiResponseDto>(string = jsonText)
            val items = geminiResponse.items.mapNotNull { dto -> 
                convertDtoToDomain(dto = dto)
            }
            
            ProcessingResult.Success(items = items)
        } catch (e: IllegalStateException) {
            ProcessingResult.Error(message = e.message ?: "API key not configured")
        } catch (e: Exception) {
            ProcessingResult.Error(
                message = "Failed to process transcription: ${e.message}"
            )
        }
    }
    
    /**
     * Convert DTO to domain model
     */
    private fun convertDtoToDomain(dto: GeminiItemDto): QuickThoughtItem? {
        return when (dto.type.uppercase()) {
            "TASK" -> QuickThoughtItem.Task(
                title = dto.title,
                description = dto.description,
                dueDate = dto.dueDate,
                priority = when (dto.priority?.uppercase()) {
                    "HIGH" -> QuickThoughtItem.Task.Priority.HIGH
                    "LOW" -> QuickThoughtItem.Task.Priority.LOW
                    else -> QuickThoughtItem.Task.Priority.MEDIUM
                }
            )
            
            "APPOINTMENT" -> {
                val dateTime = dto.dateTime ?: return null
                QuickThoughtItem.Appointment(
                    title = dto.title,
                    description = dto.description,
                    dateTime = dateTime,
                    durationMinutes = dto.durationMinutes ?: 60
                )
            }
            
            "IDEA" -> QuickThoughtItem.Idea(
                title = dto.title,
                description = dto.description,
                tags = dto.tags ?: emptyList()
            )
            
            else -> null
        }
    }
}
