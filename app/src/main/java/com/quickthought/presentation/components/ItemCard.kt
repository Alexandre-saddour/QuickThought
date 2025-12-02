package com.quickthought.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.quickthought.R
import com.quickthought.domain.model.QuickThoughtItem
import com.quickthought.presentation.theme.*

/**
 * Card component for displaying a structured item (Task, Appointment, or Idea)
 */
@Composable
fun ItemCard(
    item: QuickThoughtItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val (typeLabel, typeColor, typeIcon) = when (item) {
        is QuickThoughtItem.Task -> Triple(
            first = stringResource(id = R.string.type_task),
            second = TaskColor,
            third = Icons.Default.CheckCircle
        )
        is QuickThoughtItem.Appointment -> Triple(
            first = stringResource(id = R.string.type_appointment),
            second = AppointmentColor,
            third = Icons.Default.CalendarMonth
        )
        is QuickThoughtItem.Idea -> Triple(
            first = stringResource(id = R.string.type_idea),
            second = IdeaColor,
            third = Icons.Default.Lightbulb
        )
    }
    
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = SurfaceWhite
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(all = 16.dp)
        ) {
            // Type indicator
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(space = 8.dp)
                ) {
                    Icon(
                        imageVector = typeIcon,
                        contentDescription = null,
                        tint = typeColor,
                        modifier = Modifier.size(size = 20.dp)
                    )
                    Text(
                        text = typeLabel,
                        style = MaterialTheme.typography.labelMedium,
                        color = typeColor
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(height = 12.dp))
            
            // Title
            Text(
                text = item.title,
                style = MaterialTheme.typography.titleMedium,
                color = TextPrimary
            )
            
            Spacer(modifier = Modifier.height(height = 8.dp))
            
            // Description
            Text(
                text = item.description,
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary
            )
            
            // Type-specific details
            when (item) {
                is QuickThoughtItem.Task -> {
                    if (item.dueDate != null) {
                        Spacer(modifier = Modifier.height(height = 8.dp))
                        Text(
                            text = stringResource(
                                id = R.string.due_date_format,
                                item.dueDate
                            ),
                            style = MaterialTheme.typography.bodySmall,
                            color = TextTertiary
                        )
                    }
                }
                is QuickThoughtItem.Appointment -> {
                    Spacer(modifier = Modifier.height(height = 8.dp))
                    Text(
                        text = item.dateTime,
                        style = MaterialTheme.typography.bodySmall,
                        color = TextTertiary
                    )
                }
                is QuickThoughtItem.Idea -> {
                    if (item.tags.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(height = 8.dp))
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(space = 4.dp)
                        ) {
                            item.tags.take(n = 3).forEach { tag ->
                                SuggestionChip(
                                    onClick = { },
                                    label = {
                                        Text(
                                            text = tag,
                                            style = MaterialTheme.typography.labelSmall
                                        )
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
