package com.example.cup2.ui.screen.article_rating

import android.content.res.Configuration
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.StarBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cup2.ui.theme.Cup2Theme
import com.example.cup2.util.NoRippleInteractionSource

@Composable
fun ArticleRatingScreen(modifier: Modifier = Modifier) {
    Surface(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(30.dp),
        ) {
            items(100) {
                var rate by rememberSaveable { mutableStateOf<Int?>(null) }
                var available by rememberSaveable { mutableStateOf(true) }

                Row {
                    repeat(5) { index ->
                        Icon(
                            imageVector =
                                if (rate == null || rate!! < index) Icons.Rounded.StarBorder
                                else Icons.Rounded.Star,
                            tint = MaterialTheme.colorScheme.primary,
                            contentDescription = null,
                            modifier = Modifier
                                .size(48.dp)
                                .rotate(
                                    animateFloatAsState(
                                        targetValue = if (rate != null && rate!! >= index) 360f else 0f,
                                        animationSpec = tween(durationMillis = (index + 1) * 300)
                                    ).value
                                )
                                .clickable(
                                    interactionSource = NoRippleInteractionSource(),
                                    indication = null
                                ) {
                                    if (available) {
                                        rate = index
                                        available = false
                                    }
                                }
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
fun ArticleRatingScreenPreview() {
    Cup2Theme {
        ArticleRatingScreen()
    }
}