package com.quickthought.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.quickthought.presentation.record.RecordScreen
import com.quickthought.presentation.theme.QuickThoughtTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * Main activity hosting the Compose UI
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        setContent {
            QuickThoughtTheme {
                RecordScreen()
            }
        }
    }
}
