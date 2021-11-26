package com.shushant.messengercompose.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shushant.messengercompose.R
import com.shushant.messengercompose.extensions.NetworkImage
import com.shushant.messengercompose.extensions.getAvatar
import com.shushant.messengercompose.model.UsersData

@Composable
fun MyLazyRow(
    users: MutableList<UsersData>
) {
    val list = users
    //list.add(0, UsersData(name = "Your Story"))
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        itemsIndexed(list) { index, item ->
            if (index == 0) {
                Spacer(
                    modifier = Modifier
                        .width(20.dp)
                )
            }
            ViewItem(itemText = item, index)
            Spacer(
                modifier = Modifier
                    .width(16.dp)
            )

        }
    }
}

@Composable
fun ViewItem(
    itemText: UsersData,
    index: Int
) {
    Column {
        Box(modifier = Modifier.align(Alignment.CenterHorizontally)) {
            NetworkImage(
                networkUrl = if (index > 0) itemText.name.getAvatar() else "https://i.ibb.co/1959D7F/Your-Story.png",
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(Color(0X04000000), CircleShape)
                    .align(Alignment.Center),
            )
            if (index > 0 && itemText.online) {
                Image(
                    painter = painterResource(id = R.drawable.ic_oval_online),
                    contentDescription = "Online",
                    modifier = Modifier.align(Alignment.BottomEnd)
                )
            }else if (!itemText.online && index >0){
                Image(
                    painter = painterResource(id = R.drawable.ic_vector_oval_offline),
                    contentDescription = "Online",
                    modifier = Modifier.align(Alignment.BottomEnd)
                )
            }
        }
        Text(
            text = itemText.name,
            modifier = Modifier
                .padding(8.dp)
                .alpha(0.35F),
            style = TextStyle(
                fontSize = 12.sp, color = Color.Black,
            ),
            textAlign = TextAlign.Center
        )
    }
}