package com.quickthought.presentation.record

import android.content.Context
import android.content.Intent
import android.provider.CalendarContract
import com.quickthought.domain.model.QuickThoughtItem
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun launchItemIntent(context: Context, item: QuickThoughtItem) {
    when (item) {
        is QuickThoughtItem.Appointment -> {
            val intent = Intent(Intent.ACTION_INSERT).apply {
                data = CalendarContract.Events.CONTENT_URI
                putExtra(CalendarContract.Events.TITLE, item.title)
                putExtra(CalendarContract.Events.DESCRIPTION, item.description)
                
                try {
                    val formatter = DateTimeFormatter.ISO_DATE_TIME
                    val startDate = LocalDateTime.parse(item.dateTime, formatter)
                    val startMillis = startDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
                    val endMillis = startMillis + (item.durationMinutes * 60 * 1000)
                    
                    putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startMillis)
                    putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endMillis)
                } catch (e: Exception) {
                    // Fallback if date parsing fails
                }
            }
            context.startActivity(intent)
        }
        
        is QuickThoughtItem.Task -> {
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_SUBJECT, item.title)
                val text = buildString {
                    append(item.title)
                    if (item.description.isNotBlank()) append("\n\n${item.description}")
                    item.dueDate?.let { append("\n\nDue: $it") }
                    append("\n\nPriority: ${item.priority}")
                }
                putExtra(Intent.EXTRA_TEXT, text)
            }
            context.startActivity(Intent.createChooser(intent, "Share Task"))
        }
        
        is QuickThoughtItem.Idea -> {
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_SUBJECT, item.title)
                val text = buildString {
                    append(item.title)
                    if (item.description.isNotBlank()) append("\n\n${item.description}")
                    if (item.tags.isNotEmpty()) append("\n\nTags: ${item.tags.joinToString(", ")}")
                }
                putExtra(Intent.EXTRA_TEXT, text)
            }
            context.startActivity(Intent.createChooser(intent, "Share Idea"))
        }
    }
}
