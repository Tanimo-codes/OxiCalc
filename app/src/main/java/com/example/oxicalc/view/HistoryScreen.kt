package com.example.oxicalc.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.oxicalc.model.CalculationHistoryItem
import com.example.oxicalc.viewModel.HistoryViewModel
import com.example.oxicalc.ui.theme.OxiCalcTheme



@Composable
fun HistoryScreen(viewModel: HistoryViewModel = hiltViewModel()) {
    val historyItems by viewModel.historyItems.collectAsState()
    val colorScheme = MaterialTheme.colorScheme

    Scaffold(
        modifier = Modifier.fillMaxSize(),

        containerColor = colorScheme.surface
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            if (historyItems.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "No calculation history found",
                        style = MaterialTheme.typography.bodyMedium,
                        color = colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(items = historyItems, key = { it.id }) { item ->
                        SwipeableHistoryItem(
                            item = item,
                            onDelete = { viewModel.deleteHistory(item) }
                        )
                    }
                }
            }
        }
    }
}



@Composable
fun SwipeableHistoryItem(
    item: CalculationHistoryItem,
    onDelete: () -> Unit
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    var offsetX by remember { mutableFloatStateOf(0f) }
    val density = LocalDensity.current
    val isDarkMode = isSystemInDarkTheme()

    val dismissThreshold = (-120).dp
    val dismissThresholdPx = with(density) { dismissThreshold.toPx() }

    val rotationAngle by animateFloatAsState(
        targetValue = if (expanded) 90f else 0f,
        animationSpec = tween(200),
        label = "rotationAnimation"
    )

    val backgroundColor = if (expanded) Color(0xFF1565C0) else Color(0xFFBBDEFB)
    val contentColor = when {
        expanded -> Color.White
        isDarkMode -> Color.Black.copy(alpha = 0.87f)
        else -> MaterialTheme.colorScheme.onSurface
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
    ) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(Color.Red.copy(alpha = 0.8f))
                .padding(end = 16.dp),
            contentAlignment = Alignment.CenterEnd
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete",
                tint = Color.White
            )
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .offset(x = with(density) { offsetX.toDp() })
                .clip(RoundedCornerShape(8.dp))
                .draggable(
                    orientation = Orientation.Horizontal,
                    state = rememberDraggableState { delta ->
                        if (offsetX + delta <= 0) {
                            offsetX += delta
                        }
                    },
                    onDragStopped = {
                        if (offsetX < dismissThresholdPx) {
                            onDelete()
                        } else {
                            offsetX = 0f
                        }
                    }
                )
                .clickable { expanded = !expanded },
            colors = CardDefaults.cardColors(containerColor = backgroundColor),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = item.formula,
                        style = MaterialTheme.typography.titleMedium,
                        color = contentColor,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                        modifier = Modifier.weight(1f)
                    )

                    IconButton(
                        onClick = { expanded = !expanded },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = if (expanded) "Collapse" else "Expand",
                            modifier = Modifier.rotate(rotationAngle),
                            tint = contentColor
                        )
                    }
                }

                AnimatedVisibility(visible = expanded) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                    ) {
                        HorizontalDivider(color = Color.White.copy(alpha = 0.5f))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "Solution:",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium,
                            color = Color.White
                        )
                        Text(
                            text = "${item.targetElement} = ${item.oxidationState}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White
                        )
                        Text(
                            text = item.solution,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}
