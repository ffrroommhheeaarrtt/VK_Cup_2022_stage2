package com.example.cup2.ui.screen.filling_in_gaps

import android.content.res.Configuration
import androidx.compose.animation.*
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cup2.ui.theme.Cup2Theme

private enum class Sign(val sign: Char) {
    PLUS('+'),
    MINUS('-'),
    MULTIPLICATION('\u00d7')
}

private fun Int.sign(sign: Sign, other: Int): Int {
    return when (sign) {
        Sign.PLUS -> this + other
        Sign.MINUS -> this - other
        Sign.MULTIPLICATION -> this * other
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun FillingInGapsScreen(modifier: Modifier = Modifier) {
    Surface(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(40.dp),
        ) {
            items(100) {
                val numbers by rememberSaveable { mutableStateOf(List(3) { (0..10).random() }) }
                val signs by rememberSaveable { mutableStateOf(List(2) { Sign.values().random() }) }
                val answer = rememberSaveable(stateSaver = TextFieldValue.Saver) { mutableStateOf(TextFieldValue()) }
                var available by rememberSaveable { mutableStateOf(true) }

                fun isCorrectExpression(): Boolean {
                    return if (signs[1] == Sign.MULTIPLICATION) {
                        numbers[0].sign(signs[0], numbers[1].sign(signs[1], numbers[2])) == answer.value.text.toInt()
                    } else {
                        numbers[0].sign(signs[0], numbers[1]).sign(signs[1], numbers[2]) == answer.value.text.toInt()
                    }
                }

                val answerColor by animateColorAsState(
                    targetValue = when {
                        available -> MaterialTheme.colorScheme.primary
                        isCorrectExpression() -> MaterialTheme.colorScheme.tertiary
                        else -> MaterialTheme.colorScheme.error
                    },
                )
                val expressionColor by animateColorAsState(
                    targetValue = when {
                        available -> MaterialTheme.colorScheme.onBackground
                        isCorrectExpression() -> MaterialTheme.colorScheme.tertiary
                        else -> MaterialTheme.colorScheme.error
                    },
                )

                AnimatedContent(
                    targetState = !available,
                    transitionSpec = {
                        if (targetState && isCorrectExpression()) {
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
                        Text(
                            text = "${numbers[0]}",
                            style = MaterialTheme.typography.titleLarge,
                            color = expressionColor,
                            fontWeight = FontWeight.Bold,
                        )
                        Text(
                            text = "${signs[0].sign}",
                            style = MaterialTheme.typography.titleLarge,
                            color = expressionColor,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 12.dp)
                        )
                        Text(
                            text = "${numbers[1]}",
                            style = MaterialTheme.typography.titleLarge,
                            color = expressionColor,
                            fontWeight = FontWeight.Bold,
                        )
                        Text(
                            text = "${signs[1].sign}",
                            style = MaterialTheme.typography.titleLarge,
                            color = expressionColor,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 12.dp)
                        )
                        Text(
                            text = "${numbers[2]}",
                            style = MaterialTheme.typography.titleLarge,
                            color = expressionColor,
                            fontWeight = FontWeight.Bold,
                        )
                        Text(
                            text = "=",
                            style = MaterialTheme.typography.titleLarge,
                            color = expressionColor,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 12.dp)
                        )
                        Column {
                            BasicTextField(
                                value = answer.value,
                                onValueChange = { value ->
                                    if (value.text.length <= 4) {
                                        try {
                                            if (value.text != "" && value.text != "-") value.text.toInt()
                                            answer.value = value
                                        } catch (_: NumberFormatException) {}
                                    }
                                },
                                readOnly = !available,
                                keyboardOptions = KeyboardOptions(
                                    autoCorrect = false,
                                    keyboardType = KeyboardType.Decimal
                                ),
                                keyboardActions = KeyboardActions(
                                    onDone = {
                                        if (answer.value.text != "" && answer.value.text != "-") {
                                            available = false
                                        }
                                    }
                                ),
                                singleLine = true,
                                textStyle = MaterialTheme.typography.titleLarge.copy(
                                    color = answerColor,
                                    textAlign = TextAlign.Center,
                                    fontWeight = FontWeight.Bold,
                                ),
                                cursorBrush = SolidColor(MaterialTheme.colorScheme.onBackground),
                                modifier = Modifier.widthIn(max = 56.dp)
                            )
                            Divider(
                                color = expressionColor,
                                thickness = 2.dp,
                                modifier = Modifier.width(56.dp)
                            )
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
fun FillingInGapsScreenPreview() {
    Cup2Theme {
        FillingInGapsScreen()
    }
}