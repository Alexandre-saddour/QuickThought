package com.quickthought.presentation.record

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quickthought.domain.model.ProcessingResult
import com.quickthought.domain.model.SpeechResult
import com.quickthought.domain.usecase.ProcessVoiceMemoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Record screen
 */
@HiltViewModel
class RecordViewModel @Inject constructor(
    private val processVoiceMemoUseCase: ProcessVoiceMemoUseCase
) : ViewModel() {
    
    private val _state = MutableStateFlow(value = RecordState())
    val state: StateFlow<RecordState> = _state.asStateFlow()
    
    init {
        checkPermission()
    }
    
    /**
     * Check microphone permission status
     */
    fun checkPermission() {
        val hasPermission = processVoiceMemoUseCase.hasPermission()
        _state.update { currentState ->
            currentState.copy(hasPermission = hasPermission)
        }
    }
    
    /**
     * Start recording voice input
     */
    fun startRecording() {
        if (!_state.value.hasPermission) {
            _state.update { currentState ->
                currentState.copy(
                    error = "Microphone permission not granted"
                )
            }
            return
        }
        
        _state.update { currentState ->
            currentState.copy(
                isRecording = true,
                error = null,
                transcription = "",
                items = emptyList()
            )
        }
        
        viewModelScope.launch {
            processVoiceMemoUseCase.startListening().collect { result ->
                when (result) {
                    is SpeechResult.Success -> {
                        _state.update { currentState ->
                            currentState.copy(
                                isRecording = false,
                                isProcessing = true,
                                transcription = result.text
                            )
                        }
                        processTranscription(transcription = result.text)
                    }
                    is SpeechResult.Error -> {
                        _state.update { currentState ->
                            currentState.copy(
                                isRecording = false,
                                error = result.message
                            )
                        }
                    }
                }
            }
        }
    }
    
    /**
     * Stop recording voice input
     */
    fun stopRecording() {
        processVoiceMemoUseCase.stopListening()
        _state.update { currentState ->
            currentState.copy(isRecording = false)
        }
    }
    
    /**
     * Process transcription through AI
     */
    private suspend fun processTranscription(transcription: String) {
        val result = processVoiceMemoUseCase.processTranscription(
            transcription = transcription
        )
        
        when (result) {
            is ProcessingResult.Success -> {
                _state.update { currentState ->
                    currentState.copy(
                        isProcessing = false,
                        items = result.items
                    )
                }
            }
            is ProcessingResult.Error -> {
                _state.update { currentState ->
                    currentState.copy(
                        isProcessing = false,
                        error = result.message
                    )
                }
            }
        }
    }
    
    /**
     * Clear error message
     */
    fun clearError() {
        _state.update { currentState ->
            currentState.copy(error = null)
        }
    }
}
