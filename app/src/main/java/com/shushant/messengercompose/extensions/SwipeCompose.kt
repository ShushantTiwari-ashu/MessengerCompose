package com.shushant.messengercompose.extensions

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shushant.messengercompose.R
import com.shushant.messengercompose.model.Data
import com.shushant.messengercompose.model.UsersData
import com.shushant.messengercompose.utils.Utility.getTimeAgo

@Composable
fun MessageItem(item: UsersData, detailScreen: (UsersData) -> Unit) {
    Row( // 1
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .fillMaxSize()
            .background(Color.White)
            .clickable { detailScreen.invoke(item) },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Box(modifier = Modifier.align(Alignment.CenterVertically)) {
            NetworkImage(
                networkUrl = item.name.getAvatar(),
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .fillMaxWidth(0.1F)
                    .background(Color(0X04000000), CircleShape)
            )

            if (item.online) {
                Image(
                    painter = painterResource(id = R.drawable.ic_oval_online),
                    contentDescription = "Online",
                    modifier = Modifier.align(Alignment.BottomEnd)
                )
            } else {
                Text(
                    text = item.currentTime.getTimeAgo(),
                    fontSize = 8.sp,
                    color = Color.Black,
                    modifier = Modifier
                        .background(
                            Color(0XFFC7F0BB),
                            RoundedCornerShape(18.dp)
                        )
                        .align(Alignment.BottomCenter)
                )
            }
        }

        Column(modifier = Modifier.fillMaxWidth(0.8F)) { // 3
            Text(
                text = item.name,
                style = TextStyle(fontWeight = FontWeight.Bold, color = Color.Black),
                fontSize = 18.sp,
            )

            val lastMessageText =
                if (item.messages?.message.isNullOrEmpty()) "Media" else item.messages?.message
                    ?: ""
            Text(
                text = if (item.messages?.checkIsRight() == true) "You:$lastMessageText ${item.messages?.getTime()}" else lastMessageText + " ${item.messages?.getTime()}",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = TextStyle(color = Color.Black),
                modifier = Modifier
                    .alpha(0.5F)
                    .padding(end = 10.dp)
            )
        }
        Image(
            painter = if (item.messages?.isDelivered == true) painterResource(id = R.drawable.ic_read) else painterResource(
                id = R.drawable.ic_shape
            ),
            contentDescription = "Read",
            alignment = Alignment.CenterStart,
            modifier = Modifier
                .align(Alignment.CenterVertically)
        )
    }
}