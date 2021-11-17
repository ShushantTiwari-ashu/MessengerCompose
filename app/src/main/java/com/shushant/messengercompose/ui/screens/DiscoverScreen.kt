package com.shushant.messengercompose.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shushant.messengercompose.R
import com.shushant.messengercompose.extensions.SearchComposable
import com.shushant.messengercompose.ui.screens.chat.ChatViewModel

@Composable
fun DiscoverScreen(viewModel: ChatViewModel) {
    val textState = remember { mutableStateOf(TextFieldValue("")) }
    Column {
        TopHeaderForDiscover()
        SearchComposable(textState)
    }
}

@Composable
fun TopHeaderForDiscover() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 30.dp)
            .background(Color.Transparent),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Column(modifier = Modifier.wrapContentSize(Alignment.CenterStart)) {
            Row {
                Spacer(modifier = Modifier.width(20.dp))
                Image(
                    painter = painterResource(id = R.drawable.oval),
                    contentDescription = "Person",
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .background(
                            Color.Cyan, CircleShape
                        ),
                )
                Spacer(modifier = Modifier.width(20.dp))
                Text(
                    text = "Discover",
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.align(Alignment.CenterVertically),
                    textAlign = TextAlign.Center,
                    fontSize = 25.sp
                )
            }
        }
    }
}
