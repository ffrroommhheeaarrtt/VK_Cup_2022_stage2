package com.example.cup2.ui.screen.main

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cup2.ui.NavigationMenu
import com.example.cup2.ui.theme.Cup2Theme

@Composable
fun MainScreen(modifier: Modifier = Modifier, onMenuButtonClick: (String) -> Unit = {}) {
    Surface(modifier = modifier.fillMaxSize()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            NavigationMenu.values().forEach { menu ->
                Button(
                    onClick = { onMenuButtonClick(menu.name) },
                    modifier = Modifier.padding(vertical = 16.dp, horizontal = 4.dp)
                ) {
                    Text(text = menu.text, style = MaterialTheme.typography.displaySmall)
                }
            }
        }
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun MainScreenPreview() {
    Cup2Theme {
        MainScreen()
    }
}