package com.quickthought.domain.usecase

import com.quickthought.domain.model.ProcessingResult
import com.quickthought.domain.model.QuickThoughtItem
import com.quickthought.domain.model.SpeechResult
import com.quickthought.domain.repository.AiRepository
import com.quickthought.domain.repository.SpeechRepository
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class ProcessVoiceMemoUseCaseTest {

    private lateinit var speechRepository: SpeechRepository
    private lateinit var aiRepository: AiRepository
    private lateinit var useCase: ProcessVoiceMemoUseCase

    @Before
    fun setup() {
        speechRepository = mock()
        aiRepository = mock()
        useCase = ProcessVoiceMemoUseCase(speechRepository, aiRepository)
    }

    @Test
    fun `startListening delegates to repository`() = runTest {
        val expectedFlow = flowOf(SpeechResult.Success("test"))
        whenever(speechRepository.startListening()).thenReturn(expectedFlow)

        val result = useCase.startListening()

        assertEquals(expectedFlow, result)
        verify(speechRepository).startListening()
    }

    @Test
    fun `stopListening delegates to repository`() {
        useCase.stopListening()
        verify(speechRepository).stopListening()
    }

    @Test
    fun `processTranscription delegates to repository`() = runTest {
        val transcription = "Buy milk"
        val expectedResult = ProcessingResult.Success(
            listOf(QuickThoughtItem.Task("Buy milk", "Buy milk"))
        )
        whenever(aiRepository.processTranscription(transcription)).thenReturn(expectedResult)

        val result = useCase.processTranscription(transcription)

        assertEquals(expectedResult, result)
        verify(aiRepository).processTranscription(transcription)
    }

    @Test
    fun `hasPermission delegates to repository`() {
        whenever(speechRepository.hasPermission()).thenReturn(true)
        
        val result = useCase.hasPermission()
        
        assertEquals(true, result)
        verify(speechRepository).hasPermission()
    }
}
