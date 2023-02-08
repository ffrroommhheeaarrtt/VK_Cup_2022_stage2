package com.example.cup2.ui.screen.quiz

import android.content.res.Configuration
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cup2.ui.theme.Cup2Theme
import com.example.cup2.util.NoRippleInteractionSource
import com.example.cup2.util.formatNumberOfAnswers

private const val PROGRESS_DURATION = 2000
private const val PROGRESS_DELAY = 500

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun OptionChip(
    text: String,
    percent: Int,
    numberOfAnswers: Int,
    selected: Boolean,
    available: Boolean,
    isCorrect: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    val progress by animateFloatAsState(
        targetValue = if (available) 0f else percent.toFloat() / 100f,
        animationSpec = tween(durationMillis = PROGRESS_DURATION, delayMillis = PROGRESS_DELAY)
    )
    val percentChange by animateIntAsState(
        targetValue = if (available) 0 else percent,
        animationSpec = tween(durationMillis = PROGRESS_DURATION, delayMillis = PROGRESS_DELAY)
    )
    val selectedContainerColor by animateColorAsState(
        targetValue = when {
            !available && isCorrect -> MaterialTheme.colorScheme.tertiary
            !available -> MaterialTheme.colorScheme.error
            else -> MaterialTheme.colorScheme.background
        },
        animationSpec = spring(stiffness = Spring.StiffnessVeryLow)
    )
    val selectedLabelColor by animateColorAsState(
        targetValue = when {
            !available && isCorrect -> MaterialTheme.colorScheme.onTertiary
            !available -> MaterialTheme.colorScheme.onError
            else -> LocalContentColor.current
        },
        animationSpec = spring(stiffness = Spring.StiffnessVeryLow)
    )
    val progressIndicatorColor = if (selected) {
        if (isCorrect) MaterialTheme.colorScheme.onTertiary else MaterialTheme.colorScheme.onError
    } else ProgressIndicatorDefaults.linearColor
    val iconColor = when {
        !available && selected && isCorrect -> MaterialTheme.colorScheme.onTertiary
        !available && isCorrect -> MaterialTheme.colorScheme.onBackground
        !available && selected -> MaterialTheme.colorScheme.onError
        !available -> Color.Transparent
        else -> MaterialTheme.colorScheme.primary
    }

    AnimatedContent(
        targetState = !available,
        transitionSpec = {
            ContentTransform(
                initialContentExit = slideOutHorizontally { it } + scaleOut(),
                targetContentEnter = slideInHorizontally { -it } + scaleIn()
            )
        }
    ) {
        FilterChip(
            selected = selected,
            onClick = onClick,
            label = {
                Column(verticalArrangement = Arrangement.Center, modifier = Modifier.heightIn(min = 50.dp)) {
                    if (available) {
                        Text(text = text)
                    } else {
                        Row {
                            Text(text = text, modifier = Modifier.weight(1f))
                            Text(text = "$percentChange%")
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        LinearProgressIndicator(
                            progress = progress,
                            color = progressIndicatorColor,
                            trackColor = Color.Transparent,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Text(
                            text = if (selected) "${numberOfAnswers + 1} ${formatNumberOfAnswers(numberOfAnswers + 1)}"
                            else "$numberOfAnswers ${formatNumberOfAnswers(numberOfAnswers)}",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            },
            leadingIcon = {
                Icon(
                    imageVector = if (available) Icons.Filled.RadioButtonUnchecked
                    else if (isCorrect) Icons.Filled.CheckCircle else Icons.Filled.Cancel,
                    tint = iconColor,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
            },
            colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = selectedContainerColor,
                selectedLabelColor = selectedLabelColor
            ),
            interactionSource = NoRippleInteractionSource(),
            modifier = modifier.fillMaxWidth()
        )
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun OptionChipPreviewAvailable() {
    Cup2Theme {
        Surface {
            OptionChip(
                text = "Option",
                percent = 100,
                numberOfAnswers = 1,
                selected = false,
                available = true,
                isCorrect = false
            )
        }
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun OptionChipPreviewCorrect() {
    Cup2Theme {
        Surface {
            OptionChip(
                text = "Option",
                percent = 23,
                numberOfAnswers = 32,
                selected = false,
                available = false,
                isCorrect = true
            )
        }
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun OptionChipPreviewCorrectSelected() {
    Cup2Theme {
        Surface {
            OptionChip(
                text = "Option",
                percent = 32,
                numberOfAnswers = 22,
                selected = true,
                available = false,
                isCorrect = true
            )
        }
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun OptionChipPreviewWrong() {
    Cup2Theme {
        Surface {
            OptionChip(
                text = "Option",
                percent = 6,
                numberOfAnswers = 16,
                selected = true,
                available = false,
                isCorrect = false
            )
        }
    }
}