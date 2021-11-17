package com.shushant.messengercompose.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shushant.messengercompose.R
import com.shushant.messengercompose.extensions.NetworkImage
import com.shushant.messengercompose.extensions.SearchComposable
import com.shushant.messengercompose.extensions.paging
import com.shushant.messengercompose.model.Data
import com.shushant.messengercompose.network.NetworkState
import com.shushant.messengercompose.network.onLoading
import com.shushant.messengercompose.ui.screens.chat.ChatViewModel

@Composable
fun FriendsScreen(viewModel: ChatViewModel) {
    val textState = remember { mutableStateOf(TextFieldValue("")) }
    Column {
        TopHeaderForFriends()
        SearchComposable(textState)
        MyPeoplesColumn(viewModel, textState)

    }
}

@Composable
fun MyPeoplesColumn(viewModel: ChatViewModel, textState: MutableState<TextFieldValue>) {
    val networkState: NetworkState by viewModel.movieLoadingState
    val usersList by viewModel.users
    val searchedText = textState.value.text
    val filteredUsers = if (searchedText.isEmpty()) {
        usersList
    } else {
        val resultList = ArrayList<Data>()
        for (users in usersList) {
            if (users.firstName?.contains(searchedText,true) == true || users.lastName?.contains(
                    searchedText,true
                ) == true
            )
             {
                resultList.add(users)
            }
        }
        resultList
    }
    LazyColumn {
        paging(
            items = filteredUsers,
            currentIndexFlow = viewModel.moviePageStateFlow,
            fetch = { viewModel.fetchNextUserPage() }
        ) { item, index ->
            UserItem(item = item, index)
        }
    }
    networkState.onLoading {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(Alignment.Center)
        ) {

            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }

}

@Composable
fun TopHeaderForFriends() {
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
                    text = "People",
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.align(Alignment.CenterVertically),
                    textAlign = TextAlign.Center,
                    fontSize = 25.sp
                )
            }
        }
        Row(horizontalArrangement = Arrangement.SpaceBetween) {
            Image(
                painterResource(id = R.drawable.ic_requests),
                contentDescription = "Request",

                modifier = Modifier
                    .align(Alignment.CenterVertically)

            )
            Spacer(modifier = Modifier.width(10.dp))

            Image(
                painterResource(id = R.drawable.ic_add_contact),
                contentDescription = "Add Contacts",
                modifier = Modifier
                    .align(Alignment.CenterVertically)

            )
            Spacer(modifier = Modifier.width(20.dp))

        }
    }
}


@Composable
fun UserItem(item: Data, index: Int) {
    Row( // 1
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .fillMaxSize()
            .background(Color.White),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Box(modifier = Modifier.align(Alignment.CenterVertically)) {
            NetworkImage(
                networkUrl = item.picture,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color(0X04000000), CircleShape)
                    .align(Alignment.Center),
            )
            if (index > 0 && item.isDelivered != true) {
                Image(
                    painter = painterResource(id = R.drawable.ic_was_online),
                    contentDescription = "Online",
                    modifier = Modifier.align(Alignment.BottomCenter)
                )
            }
            if (index > 0 && item.isDelivered == true) {
                Image(
                    painter = painterResource(id = R.drawable.ic_oval_online),
                    contentDescription = "Online",
                    modifier = Modifier.align(Alignment.BottomEnd)
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth(0.7F)
                .align(Alignment.CenterVertically), horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "${item.firstName} ${item.lastName}",
                style = TextStyle(fontWeight = FontWeight.Bold, color = Color.Black),
                fontSize = 18.sp,
            )
        }
        Image(
            painter = painterResource(id = R.drawable.ic_wave),
            contentDescription = "Read",
            alignment = Alignment.CenterStart,
            modifier = Modifier
                .align(Alignment.CenterVertically)
        )
    }
}