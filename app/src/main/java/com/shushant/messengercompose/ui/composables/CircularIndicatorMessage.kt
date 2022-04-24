package com.shushant.messengercompose.ui.composables

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.shushant.messengercompose.ui.theme.black15

/**
 * A loading progress indicator.
 *
 * This will also take a [message] to show it with the progress indicator.
 */

@Composable
fun CircularIndicatorMessage(message: String) {
    Card(
        backgroundColor = Color.White,
        modifier = Modifier.fillMaxWidth(0.5f),
        elevation = 10.dp
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(15.dp),
        ) {
            CircularProgressIndicator()
            SizedBox(width = 15)
            Text(
                message,
                style = black15
            )
        }
    }
}