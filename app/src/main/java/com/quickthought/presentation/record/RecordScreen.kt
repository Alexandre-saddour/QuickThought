package com.quickthought.presentation.record

import android.Manifest
import android.content.Context
import android.content.Intent
import android.provider.CalendarContract
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.quickthought.R
import com.quickthought.presentation.components.ItemCard
import com.quickthought.presentation.components.RecordButton

/**
 * Main record screen for capturing voice memos
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordScreen(
    viewModel: RecordViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current
    
    // Permission launcher
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            viewModel.checkPermission()
        }
    }
    
    // Check permission on first launch
    LaunchedEffect(key1 = Unit) {
        if (!state.hasPermission) {
            permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
        }
    }
    
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.app_name))
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(all = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Title and subtitle
                Text(
                    text = stringResource(id = R.string.record_title),
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
                
                Spacer(modifier = Modifier.height(height = 8.dp))
                
                Text(
                    text = when {
                        state.isRecording -> stringResource(id = R.string.recording_in_progress)
                        state.isProcessing -> stringResource(id = R.string.processing_audio)
                        else -> stringResource(id = R.string.record_subtitle)
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
                
                Spacer(modifier = Modifier.height(height = 48.dp))
                
                // Record button
                RecordButton(
                    isRecording = state.isRecording,
                    onClick = {
                        if (state.isRecording) {
                            viewModel.stopRecording()
                        } else {
                            viewModel.startRecording()
                        }
                    }
                )
                
                Spacer(modifier = Modifier.height(height = 32.dp))
                
                // Transcription
                if (state.transcription.isNotEmpty()) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(all = 16.dp)
                        ) {
                            Text(
                                text = stringResource(id = R.string.transcription_label),
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(height = 8.dp))
                            Text(
                                text = state.transcription,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(height = 16.dp))
                }
                
                // Loading indicator
                if (state.isProcessing) {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.height(height = 16.dp))
                }
                
                // Results
                if (state.items.isNotEmpty()) {
                    Text(
                        text = stringResource(id = R.string.results_title),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    )
                    
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(space = 12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(items = state.items) { item ->
                            ItemCard(
                                item = item,
                                onClick = { launchItemIntent(context, item) }
                            )
                        }
                    }
                }
            }
            
            // Error snackbar
            state.error?.let { errorMessage ->
                Snackbar(
                    modifier = Modifier
                        .align(alignment = Alignment.BottomCenter)
                        .padding(all = 16.dp),
                    action = {
                        TextButton(onClick = { viewModel.clearError() }) {
                            Text(text = stringResource(id = R.string.retry))
                        }
                    }
                ) {
                    Text(text = errorMessage)
                }
            }
        }
    }
}
