package com.shushant.messengercompose.extensions

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shushant.messengercompose.R
import com.shushant.messengercompose.model.Data
import kotlin.math.roundToInt

@ExperimentalMaterialApi
@Composable
fun SwipeCompose(item: Data) {
    val bgColor by remember { mutableStateOf(Color.White) }

    val color = animateColorAsState(
        targetValue = bgColor,
        animationSpec = tween(
            durationMillis = 2000
        )
    )

    val squareSize = 150.dp
    val swipeAbleState = rememberSwipeableState(initialValue = 0)
    val sizePx = with(LocalDensity.current) { squareSize.toPx() }
    val anchors = mapOf(0f to 0, sizePx to 1)

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White).align(Alignment.CenterHorizontally),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Transparent)
                    .align(Alignment.CenterHorizontally)
                    .swipeable(
                        state = swipeAbleState,
                        anchors = anchors,
                        thresholds = { _, _ ->
                            FractionalThreshold(0.3f)
                        },
                        orientation = Orientation.Horizontal,
                    ),
                contentAlignment = Alignment.CenterStart
            ) {
                ActionsRow(onDelete = {

                },onEdit = {

                },onFavorite = {

                },actionIconSize = 40.dp)

                Box(
                    modifier = Modifier
                        .offset {
                            IntOffset(
                                swipeAbleState.offset.value.roundToInt(), 0
                            )
                        }
                        .clip(RoundedCornerShape(15.dp))
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .background(color.value)
                        .align(Alignment.CenterStart)
                ) {
                    MessageItem(item =item )
                }
            }
        }
    }
}

@Composable
fun MessageItem(item: Data) {
    Row( // 1
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .fillMaxSize().background(Color.White),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Spacer(modifier = Modifier.width(1.dp))
        NetworkImage(
            networkUrl = item.picture,
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .fillMaxWidth(0.1F)
                .background(Color(0X04000000), CircleShape)
                .align(Alignment.CenterVertically),
        )
        Column(modifier = Modifier.fillMaxWidth(0.8F)) { // 3
            Text(
                text = "${item.firstName} ${item.lastName}",
                style = TextStyle(fontWeight = FontWeight.Bold,color = Color.Black),
                fontSize = 18.sp,
            )

            val lastMessageText = item.message ?: "..."
            Text(
                text = lastMessageText,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = TextStyle(color = Color.Black),
                modifier = Modifier
                    .alpha(0.5F)
                    .padding(end = 10.dp)
            )
        }
        Image(
            painter = if (item.isDelivered == true) painterResource(id = R.drawable.ic_read) else painterResource(
                id = R.drawable.ic_shape
            ),
            contentDescription = "Read",
            alignment = Alignment.CenterStart,
            modifier = Modifier
                .align(Alignment.CenterVertically)
        )
    }
}