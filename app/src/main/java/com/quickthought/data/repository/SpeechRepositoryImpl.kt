package com.quickthought.data.repository

import com.quickthought.data.source.SpeechDataSource
import com.quickthought.domain.model.SpeechResult
import com.quickthought.domain.repository.SpeechRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of SpeechRepository
 */
@Singleton
class SpeechRepositoryImpl @Inject constructor(
    private val speechDataSource: SpeechDataSource
) : SpeechRepository {
    
    override fun startListening(): Flow<SpeechResult> {
        return speechDataSource.startListening()
    }
    
    override fun stopListening() {
        speechDataSource.stopListening()
    }
    
    override fun hasPermission(): Boolean {
        return speechDataSource.hasPermission()
    }
}
