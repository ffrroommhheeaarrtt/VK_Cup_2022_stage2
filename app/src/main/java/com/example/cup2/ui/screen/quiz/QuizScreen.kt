package com.example.cup2.ui.screen.quiz

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cup2.data.SampleData
import com.example.cup2.ui.theme.Cup2Theme
import com.example.cup2.util.getRandomNumberOfAnswers
import com.example.cup2.util.isCorrectQuizOption
import com.example.cup2.util.toPercents

@Composable
fun QuizScreen(modifier: Modifier = Modifier) {
    Surface(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(40.dp),
        ) {
            items(count = 100) {count ->
                val quizOptions by rememberSaveable { mutableStateOf(SampleData.quizOptions.shuffled()) }
                val numberOfAnswers by rememberSaveable {
                    mutableStateOf(getRandomNumberOfAnswers((0..10000).random(), SampleData.quizOptions.size))
                }
                var percents by rememberSaveable { mutableStateOf(List(4) { 0 }) }
                var quizAvailable by rememberSaveable { mutableStateOf(true) }

                Column {
                    Text(text = "Question ${count + 1}", style = MaterialTheme.typography.headlineSmall)
                    Spacer(modifier = Modifier.height(8.dp))
                    quizOptions.forEachIndexed { index, text ->
                        var selected by rememberSaveable { mutableStateOf(false) }

                        OptionChip(
                            text = text,
                            percent = percents[index],
                            numberOfAnswers = numberOfAnswers[index],
                            selected = selected,
                            available = quizAvailable,
                            isCorrect = isCorrectQuizOption(text),
                            onClick = {
                                if (quizAvailable) {
                                    percents = numberOfAnswers
                                        .mapIndexed { i, percent -> if (i == index) percent + 1 else percent }
                                        .toPercents()
                                    selected = true
                                }
                                quizAvailable = false
                            },
                            modifier = Modifier.padding(vertical = 2.dp)
                        )
                    }
                    AnimatedVisibility(visible = !quizAvailable, enter = slideInHorizontally { -it }) {
                        Text(
                            text = "Total answers: ${numberOfAnswers.sum() + 1}",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun QuizScreenPreview() {
    Cup2Theme {
        QuizScreen()
    }
}