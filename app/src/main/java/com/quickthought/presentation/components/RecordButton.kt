package com.quickthought.presentation.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.quickthought.R
import com.quickthought.presentation.theme.PrimaryIndigo
import com.quickthought.presentation.theme.RecordingActive
import com.quickthought.presentation.theme.RecordingPulse

/**
 * Animated record button with pulse effect when recording
 */
@Composable
fun RecordButton(
    isRecording: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        // Pulse animation when recording
        if (isRecording) {
            val infiniteTransition = rememberInfiniteTransition(label = "pulse")
            val pulseScale by infiniteTransition.animateFloat(
                initialValue = 1f,
                targetValue = 1.3f,
                animationSpec = infiniteRepeatable(
                    animation = tween(
                        durationMillis = 1000,
                        easing = EaseInOut
                    ),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "pulseScale"
            )
            
            val pulseAlpha by infiniteTransition.animateFloat(
                initialValue = 0.6f,
                targetValue = 0.1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(
                        durationMillis = 1000,
                        easing = EaseInOut
                    ),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "pulseAlpha"
            )
            
            Canvas(
                modifier = Modifier.size(size = 96.dp * pulseScale)
            ) {
                drawCircle(
                    color = RecordingPulse.copy(alpha = pulseAlpha),
                    radius = size.minDimension / 2
                )
            }
        }
        
        // Main button
        FloatingActionButton(
            onClick = onClick,
            modifier = Modifier.size(size = 72.dp),
            containerColor = if (isRecording) {
                RecordingActive
            } else {
                PrimaryIndigo
            },
            elevation = FloatingActionButtonDefaults.elevation(
                defaultElevation = 8.dp
            )
        ) {
            Icon(
                imageVector = Icons.Default.Mic,
                contentDescription = stringResource(id = R.string.record_button_description),
                tint = Color.White,
                modifier = Modifier.size(size = 32.dp)
            )
        }
    }
}
