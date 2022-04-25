package com.shushant.chatties.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.shushant.chatties.R
import com.shushant.chatties.ui.theme.action25Bold

@Composable
fun SendHi(sendMessage: () -> Unit) {
    Text(
        text = stringResource(id = R.string.send_hi),
        style = action25Bold,
        modifier = Modifier.clickable {
            sendMessage()
        }
    )
}