package com.example.cup2.ui.screen.drag_and_drop

import android.content.res.Configuration
import androidx.compose.animation.*
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cup2.ui.theme.Cup2Theme
import com.example.cup2.util.NoRippleInteractionSource
import com.example.cup2.util.getRandomSum

private const val NUMBER_OF_OPTIONS = 6

private data class Border(val rect: Rect, val index: Int)

private data class Gap(val number: Int, val index: Int, val borderIndex: Int)

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun DragAndDropScreen(modifier: Modifier = Modifier) {
    Surface(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(40.dp),
        ) {
            items(count = 100) {
                val numbers by rememberSaveable { mutableStateOf(getRandomSum(NUMBER_OF_OPTIONS)) }
                var borders by remember { mutableStateOf(listOf<Border>()) }
                val gaps = rememberSaveable(
                    saver = listSaver<MutableList<Gap>, List<Int>>(
                        save = { list ->
                            list.map { listOf(it.number, it.index, it.borderIndex) }
                        },
                        restore = { list ->
                            list.map { Gap(it[0], it[1], it[2]) }.toMutableStateList()
                        }
                    )
                ) { mutableStateListOf() }

                fun isAvailable(): Boolean = gaps.size < 3

                fun isCorrectSum(): Boolean {
                    return gaps.sortedBy { it.borderIndex }.map { it.number }.let { it[0] + it[1] == it[2] }
                }

                val numberColor by animateColorAsState(
                    targetValue = when {
                        isAvailable() -> MaterialTheme.colorScheme.primary
                        isCorrectSum() -> MaterialTheme.colorScheme.tertiary
                        else -> MaterialTheme.colorScheme.error
                    },
                )
                val expressionColor by animateColorAsState(
                    targetValue = when {
                        isAvailable() -> MaterialTheme.colorScheme.onBackground
                        isCorrectSum() -> MaterialTheme.colorScheme.tertiary
                        else -> MaterialTheme.colorScheme.error
                    },
                )

                fun Modifier.getNumberGapModifier(index: Int): Modifier {
                    return this
                        .onGloballyPositioned { coords ->
                            borders = borders + Border(rect = coords.boundsInRoot(), index = index)
                        }
                        .clickable(
                            interactionSource = NoRippleInteractionSource(),
                            indication = null
                        ) {
                            if (isAvailable()) {
                                gaps.remove(gaps.find { it.borderIndex == index })
                            }
                        }
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    AnimatedContent(
                        targetState = !isAvailable(),
                        transitionSpec = {
                            if (targetState && isCorrectSum()) {
                                ContentTransform(
                                    initialContentExit = scaleOut(),
                                    targetContentEnter = scaleIn(spring(stiffness = Spring.StiffnessVeryLow))
                                )
                            } else {
                                ContentTransform(
                                    initialContentExit =
                                    slideOutHorizontally(spring(dampingRatio = Spring.DampingRatioHighBouncy)) { -20 }
                                            + fadeOut(spring(stiffness = Spring.StiffnessVeryLow)),
                                    targetContentEnter = fadeIn(spring(stiffness = Spring.StiffnessVeryLow))
                                )
                            }
                        }
                    ) {
                        Row {
                            NumberGap(
                                number = gaps.firstOrNull { it.borderIndex == 0 }?.number,
                                numberColor = numberColor,
                                bottomLineColor = expressionColor,
                                modifier = Modifier.getNumberGapModifier(0)
                            )
                            Text(
                                text = "+",
                                style = MaterialTheme.typography.titleLarge,
                                color = expressionColor,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 12.dp)
                            )
                            NumberGap(
                                number = gaps.firstOrNull { it.borderIndex == 1 }?.number,
                                numberColor = numberColor,
                                bottomLineColor = expressionColor,
                                modifier = Modifier.getNumberGapModifier(1)
                            )
                            Text(
                                text = "=",
                                style = MaterialTheme.typography.titleLarge,
                                color = expressionColor,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 12.dp)
                            )
                            NumberGap(
                                number = gaps.firstOrNull { it.borderIndex == 2 }?.number,
                                numberColor = numberColor,
                                bottomLineColor = expressionColor,
                                modifier = Modifier.getNumberGapModifier(2)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(40.dp))
                    Row {
                        numbers.forEachIndexed { index, number ->
                            var dragOffset by remember { mutableStateOf<Offset?>(null) }
                            var position = Offset(0f, 0f)
                            var scale by remember { mutableStateOf(1f) }
                            val animatedScale by animateFloatAsState(targetValue = scale)
                            val textColor by animateColorAsState(
                                targetValue =
                                    if (scale == 1f) MaterialTheme.colorScheme.onBackground
                                    else MaterialTheme.colorScheme.primary
                            )

                            Box(
                                modifier = Modifier
                                    .padding(20.dp)
                                    .alpha(if (gaps.map { it.index }.contains(index)) 0f else 1f)
                                    .onGloballyPositioned { coords ->
                                        position = coords.boundsInRoot().center
                                    }
                                    .graphicsLayer {
                                        dragOffset?.let { offset ->
                                            translationX = offset.x
                                            translationY = offset.y
                                            scaleX = animatedScale
                                            scaleY = animatedScale
                                        }
                                    }
                                    .pointerInput(Unit) {
                                        detectDragGesturesAfterLongPress(
                                            onDrag = { _, dragAmount ->
                                                if (isAvailable()) {
                                                    dragOffset = if (dragOffset != null) {
                                                        dragOffset!! + dragAmount
                                                    } else dragAmount

                                                    var flag = true
                                                    for (border in borders) {
                                                        if (position + dragOffset!! in border.rect) {
                                                            scale = 2f
                                                            flag = false
                                                            break
                                                        }
                                                    }
                                                    if (flag) scale = 1f
                                                }
                                            },
                                            onDragEnd = {
                                                dragOffset?.let { offset ->
                                                    for (border in borders) {
                                                        if (position + offset in border.rect) {
                                                            gaps
                                                                .indexOfFirst { it.borderIndex == border.index }
                                                                .also { i ->
                                                                    if (i == -1) gaps.add(
                                                                        Gap(
                                                                            number,
                                                                            index,
                                                                            border.index
                                                                        )
                                                                    )
                                                                    else gaps[i] = gaps[i].copy(
                                                                        number = number,
                                                                        index = index
                                                                    )
                                                                }
                                                            break
                                                        }
                                                    }
                                                }
                                                scale = 1f
                                                dragOffset = null
                                            },
                                            onDragCancel = {
                                                scale = 1f
                                                dragOffset = null
                                            }
                                        )
                                    }
                            ) {
                                Text(
                                    text = "$number",
                                    style = MaterialTheme.typography.titleLarge,
                                    color = textColor,
                                    fontWeight = FontWeight.Bold,
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
fun DragAndDropScreenPreview() {
    Cup2Theme {
        DragAndDropScreen()
    }
}