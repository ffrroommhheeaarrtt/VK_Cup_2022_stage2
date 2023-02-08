package com.example.cup2.ui.screen.drag_and_drop

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cup2.ui.theme.Cup2Theme

@Composable
fun NumberGap(
    number: Int?,
    modifier: Modifier = Modifier,
    numberColor: Color = MaterialTheme.colorScheme.primary,
    bottomLineColor: Color = MaterialTheme.colorScheme.onBackground
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier) {
        Text(
            text = if (number == null) "" else "$number",
            style = MaterialTheme.typography.titleLarge,
            color = numberColor,
            fontWeight = FontWeight.Bold,
        )
        Spacer(modifier = Modifier.height(10.dp))
        Divider(
            color = bottomLineColor,
            thickness = 4.dp,
            modifier = Modifier.width(50.dp)
        )
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun NumberGapPreview() {
    Cup2Theme {
        Surface {
            NumberGap(number = 13,  modifier = Modifier.padding(12.dp))
        }
    }
}