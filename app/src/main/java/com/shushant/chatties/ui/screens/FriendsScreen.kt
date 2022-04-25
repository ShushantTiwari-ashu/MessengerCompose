package com.shushant.chatties.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.gson.Gson
import com.shushant.chatties.R
import com.shushant.chatties.extensions.NetworkImage
import com.shushant.chatties.extensions.SearchComposable
import com.shushant.chatties.extensions.getAvatar
import com.shushant.chatties.model.UsersData
import com.shushant.chatties.network.NetworkState
import com.shushant.chatties.network.onLoading
import com.shushant.chatties.network.onNoData
import com.shushant.chatties.network.onSuccess
import com.shushant.chatties.persistence.SharedPrefs
import com.shushant.chatties.utils.Utility.getTimeAgo

@Composable
fun FriendsScreen(
    openProfile: () -> Unit,
    opemChatPage: (UsersData) -> Unit,
    ) {
    val textState = remember { mutableStateOf(TextFieldValue("")) }
    val userData = remember {
        mutableStateOf(SharedPrefs.read("User", ""))
    }
    Column {
        TopHeaderForFriends(Gson().fromJson(userData.value, UsersData::class.java),openProfile)
        SearchComposable(textState)
        MyPeoplesColumn(textState = textState, opemChatPage = opemChatPage)
    }
}

@Composable
fun MyPeoplesColumn(
    viewModel: FriendsViewModel = hiltViewModel(),
    textState: MutableState<TextFieldValue>,
    opemChatPage: (UsersData) -> Unit
) {
    val networkState: NetworkState by viewModel.movieLoadingState
    val usersList by viewModel.firebaseusers.observeAsState(initial = emptyList<UsersData>().toMutableList())
    val searchedText = textState.value.text
    val filteredUsers = if (searchedText.isEmpty()) {
        usersList
    } else {
        val resultList = ArrayList<UsersData>()
        for (users in usersList) {
            if (users.name.contains(searchedText, true)
            ) {
                resultList.add(users)
            }
        }
        resultList
    }
    networkState.onSuccess {
        LazyColumn {
            itemsIndexed(filteredUsers.toSet().toMutableList()) { _, item ->
                UserItem(item = item, opemChatPage = opemChatPage)
            }
        }
    }
    networkState.onNoData {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(Alignment.Center)
        ) {
            Image(
                painter = painterResource(id = R.drawable.no),
                contentDescription = "",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.fillMaxSize()
            )
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
fun TopHeaderForFriends(userData: UsersData?, openProfile: () -> Unit) {
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
                NetworkImage(
                    networkUrl = userData?.profilePic,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(
                            Color.Cyan, CircleShape
                        )
                        .align(Alignment.CenterVertically).clickable { openProfile.invoke() }
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
fun UserItem(item: UsersData, opemChatPage: (UsersData) -> Unit) {
    Row( // 1
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .fillMaxSize()
            .background(Color.White)
            .clickable { opemChatPage(item) },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Box(modifier = Modifier.align(Alignment.CenterVertically)) {
            NetworkImage(
                networkUrl = item.name.getAvatar(),
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color(0X04000000), CircleShape)
                    .align(Alignment.Center),
            )

            if (item.online) {
                Image(
                    painter = painterResource(id = R.drawable.ic_oval_online),
                    contentDescription = "Online",
                    modifier = Modifier.align(Alignment.BottomEnd)
                )
            }else{
                Text(
                    text = item.currentTime.getTimeAgo(),
                    fontSize = 8.sp,
                    color = Color.Black,
                    modifier = Modifier.background(
                        Color(0XFFC7F0BB),
                        RoundedCornerShape(18.dp)
                    ).align(Alignment.BottomCenter)
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth(0.7F)
                .align(Alignment.CenterVertically), horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = item.name,
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