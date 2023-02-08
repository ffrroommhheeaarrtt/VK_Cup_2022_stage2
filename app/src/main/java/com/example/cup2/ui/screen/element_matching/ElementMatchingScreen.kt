package com.example.cup2.ui.screen.element_matching

import android.content.res.Configuration
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.boundsInParent
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cup2.data.SampleData
import com.example.cup2.ui.theme.Cup2Theme
import com.example.cup2.util.NoRippleInteractionSource
import com.example.cup2.util.isCorrectElementMatching

private fun List<Int>.isSelected(num: Int): Boolean {
    for (i in this) {
        if (i == num) return true
    }
    return false
}

private data class Border(val rect: Rect, val index: Int)

@Composable
fun ElementMatchingScreen(modifier: Modifier = Modifier) {
    val primaryColor = MaterialTheme.colorScheme.primary

    Surface(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(24.dp),
            verticalArrangement = Arrangement.spacedBy(40.dp),
        ) {
            items(count = 100) {
                val leftColumn by rememberSaveable {
                    mutableStateOf(SampleData.matchedElements.map { it.first }.shuffled())
                }
                val rightColumn by rememberSaveable {
                    mutableStateOf(SampleData.matchedElements.map { it.second.name }.shuffled())
                }
                var leftBorders by remember { mutableStateOf(listOf<Border>()) }
                var rightBorders by remember { mutableStateOf(listOf<Border>()) }
                var dragOffsets by remember { mutableStateOf<Pair<Offset, Offset>?>(null) }
                val lines = rememberSaveable(
                    saver = listSaver<MutableList<Pair<Offset, Offset>>, Pair<Pair<Float, Float>, Pair<Float, Float>>>(
                        save = { it.map { item -> (item.first.x to item.first.y) to (item.second.x to item.second.y) } },
                        restore = {
                            it.map { item ->
                                Offset(item.first.first, item.first.second) to Offset(item.second.first, item.second.second)
                            }.toMutableStateList()
                        }
                    )
                ) { mutableStateListOf() }
                val connections = rememberSaveable(
                    saver = listSaver<MutableList<Pair<Int, Int>>, Pair<Int, Int>>(
                        save = { it.toList() },
                        restore = { it.toMutableStateList() }
                    )
                ) { mutableStateListOf() }

                val contentColor by animateColorAsState(
                    targetValue = if (connections.size == SampleData.matchedElements.size) {
                        if (isCorrectElementMatching(leftColumn, rightColumn, connections)) {
                            MaterialTheme.colorScheme.onTertiary
                        } else {
                            MaterialTheme.colorScheme.onError
                        }
                    } else LocalContentColor.current,
                    animationSpec = spring(stiffness = Spring.StiffnessVeryLow)
                )

                val lineColor by animateColorAsState(
                    targetValue = if (connections.size == SampleData.matchedElements.size) {
                        if (isCorrectElementMatching(leftColumn, rightColumn, connections)) {
                            MaterialTheme.colorScheme.tertiary
                        } else {
                            MaterialTheme.colorScheme.error
                        }
                    } else MaterialTheme.colorScheme.primary,
                    animationSpec = spring(stiffness = Spring.StiffnessVeryLow)
                )

                @Composable
                fun getContainerColor(isSelected: Boolean): Color {
                    return animateColorAsState(
                        targetValue = if (connections.size == SampleData.matchedElements.size) {
                            if (isCorrectElementMatching(leftColumn, rightColumn, connections)) {
                                MaterialTheme.colorScheme.tertiary
                            } else {
                                MaterialTheme.colorScheme.error
                            }
                        } else {
                            if (isSelected) MaterialTheme.colorScheme.secondaryContainer
                            else MaterialTheme.colorScheme.background
                        },
                        animationSpec = spring(stiffness = Spring.StiffnessVeryLow)
                    ).value
                }

                @Composable
                fun getBorderColor(isSelected: Boolean): Color {
                    return animateColorAsState(
                        targetValue = if (connections.size == SampleData.matchedElements.size) {
                            if (isCorrectElementMatching(leftColumn, rightColumn, connections)) {
                                MaterialTheme.colorScheme.tertiary
                            } else {
                                MaterialTheme.colorScheme.error
                            }
                        } else {
                            if (isSelected) MaterialTheme.colorScheme.secondaryContainer
                            else MaterialTheme.colorScheme.onBackground
                        },
                        animationSpec = spring(stiffness = Spring.StiffnessVeryLow)
                    ).value
                }

                Row(
                    modifier = Modifier
                        .drawWithContent {
                            drawContent()
                            lines.forEach { pair ->
                                drawLine(
                                    start = pair.first,
                                    end = pair.second,
                                    color = lineColor,
                                    strokeWidth = 10f,
                                    cap = StrokeCap.Round,
                                )
                            }
                            dragOffsets?.let { pair ->
                                drawLine(
                                    start = pair.first,
                                    end = pair.second,
                                    color = primaryColor,
                                    strokeWidth = 10f,
                                    cap = StrokeCap.Round,
                                )
                            }
                        }
                ) {
                    Column(horizontalAlignment = Alignment.End) {
                        leftColumn.forEachIndexed { index, text ->
                            fun isSelected(): Boolean = connections.map { it.first }.isSelected(index)
                            var offset = Offset(0f, 0f)
                            var positionInParent = Offset(0f, 0f)

                            OutlinedButton(
                                onClick = {
                                    if (connections.size != SampleData.matchedElements.size) {
                                        lines.remove(lines.find { it.first == offset })
                                        connections.remove(connections.find { it.first == index })
                                    }
                                },
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = getContainerColor(isSelected()),
                                    contentColor = contentColor
                                ),
                                border = ButtonDefaults.outlinedButtonBorder.copy(
                                    brush = SolidColor(getBorderColor(isSelected()))
                                ),
                                interactionSource = NoRippleInteractionSource(),
                                modifier = Modifier
                                    .onPlaced { coords ->
                                        leftBorders = leftBorders + Border(
                                            rect = coords.boundsInParent(),
                                            index = index
                                        )
                                        offset = coords.boundsInParent().centerRight
                                        positionInParent = coords.positionInParent()
                                    }
                                    .pointerInput(Unit) {
                                        detectDragGesturesAfterLongPress(
                                            onDrag = { change, _ ->
                                                if (!isSelected()) {
                                                    dragOffsets = offset to change.position + positionInParent
                                                }
                                            },
                                            onDragEnd = {
                                                dragOffsets?.let { pair ->
                                                    for (border in rightBorders) {
                                                        if (pair.second in border.rect) {
                                                            if (!connections.map { it.second }.isSelected(border.index)
                                                            ) {
                                                                lines.add(pair.first to border.rect.centerLeft)
                                                                connections.add(index to border.index)
                                                            }
                                                            break
                                                        }
                                                    }
                                                }
                                                dragOffsets = null
                                            },
                                            onDragCancel = {
                                                dragOffsets = null
                                            }
                                        )
                                    }
                            ) {
                                Text(text = text)
                            }
                        }
                    }
                    Spacer(modifier = Modifier.width(60.dp))
                    Column {
                        rightColumn.forEachIndexed { index, iconName ->
                            fun isSelected(): Boolean = connections.map { it.second }.isSelected(index)
                            var offset = Offset(0f, 0f)
                            var positionInParent = Offset(0f, 0f)

                            OutlinedButton(
                                onClick = {
                                    if (connections.size != SampleData.matchedElements.size) {
                                        lines.remove(lines.find { it.second == offset })
                                        connections.remove(connections.find { it.second == index })
                                    }
                                },
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = getContainerColor(isSelected()),
                                    contentColor = contentColor
                                ),
                                border = ButtonDefaults.outlinedButtonBorder.copy(
                                    brush = SolidColor(getBorderColor(isSelected()))
                                ),
                                interactionSource = NoRippleInteractionSource(),
                                modifier = Modifier
                                    .onPlaced { coords ->
                                        rightBorders = rightBorders + Border(
                                            rect = Rect(
                                                topLeft = coords.boundsInParent().topLeft
                                                        + coords.parentCoordinates!!.positionInParent(),
                                                bottomRight = coords.boundsInParent().bottomRight
                                                        + coords.parentCoordinates!!.positionInParent()
                                            ),
                                            index = index
                                        )
                                        offset = coords.boundsInParent().centerLeft +
                                                coords.parentCoordinates!!.positionInParent()
                                        positionInParent = coords.positionInParent() +
                                                coords.parentCoordinates!!.positionInParent()
                                    }
                                    .pointerInput(Unit) {
                                        detectDragGesturesAfterLongPress(
                                            onDrag = { change, _ ->
                                                if (!isSelected()) {
                                                    dragOffsets = offset to change.position + positionInParent
                                                }
                                            },
                                            onDragEnd = {
                                                dragOffsets?.let { pair ->
                                                    for (border in leftBorders) {
                                                        if (pair.second in border.rect) {
                                                            if (!connections
                                                                    .map { it.first }
                                                                    .isSelected(border.index)
                                                            ) {
                                                                lines.add(border.rect.centerRight to pair.first)
                                                                connections.add(border.index to index)
                                                            }
                                                            break
                                                        }
                                                    }
                                                }
                                                dragOffsets = null
                                            },
                                            onDragCancel = {
                                                dragOffsets = null
                                            }
                                        )
                                    }
                            ) {
                                Icon(
                                    imageVector =
                                    SampleData.matchedElements.map { it.second }.first { it.name == iconName }.imageVector,
                                    contentDescription = null
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ElementMatchingScreenPreview() {
    Cup2Theme {
        ElementMatchingScreen()
    }
}