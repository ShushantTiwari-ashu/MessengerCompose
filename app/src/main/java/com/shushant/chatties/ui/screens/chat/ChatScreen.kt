package com.shushant.chatties.ui.screens.chat

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.gson.Gson
import com.shushant.chatties.R
import com.shushant.chatties.extensions.*
import com.shushant.chatties.model.UsersData
import com.shushant.chatties.network.NetworkState
import com.shushant.chatties.network.onLoading
import com.shushant.chatties.network.onNoData
import com.shushant.chatties.network.onSuccess
import com.shushant.chatties.persistence.SharedPrefs


@ExperimentalMaterialApi
@Composable
fun ChatScreen(openProfile: () -> Unit, detailScreen: (UsersData) -> Unit) {
    val textState = remember { mutableStateOf(TextFieldValue("")) }
    val userData = remember {
        mutableStateOf(SharedPrefs.read("User",""))
    }

    Column {
        TopHeader(Gson().fromJson(userData.value,UsersData::class.java),openProfile)
        SearchComposable(textState)
        MyChatLazyColumn(textState = textState, detailScreen = detailScreen)
    }
}


@ExperimentalMaterialApi
@Composable
fun MyChatLazyColumn(
    viewModel: ChatViewModel = hiltViewModel(),
    textState: MutableState<TextFieldValue>,
    detailScreen: (UsersData) -> Unit
) {
    val networkState: NetworkState by viewModel.movieLoadingState
    val usersList by viewModel.latestMessages.observeAsState(
        initial = emptyList<UsersData>().toMutableList()
    )
    usersList.toSet().toMutableList()

    LaunchedEffect(key1 = "LatestMessages"){
        viewModel.fetchData()
    }

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
        LazyColumn(modifier = Modifier.padding(start = 10.dp, end = 10.dp)) {
            itemsIndexed(filteredUsers.toSet().toMutableList()) { index, item ->
                SwipeLeftRightCompose(item, detailScreen)
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
                painter = painterResource(id = R.drawable.no_message),
                contentDescription = "",
                contentScale = ContentScale.Inside,
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

@ExperimentalMaterialApi
@Composable
fun SwipeLeftRightCompose(item: UsersData, detailScreen: (UsersData) -> Unit) {
    RevealSwipe(
        modifier = Modifier.padding(vertical = 5.dp),
        directions = setOf(
            RevealDirection.StartToEnd,
            RevealDirection.EndToStart
        ),
        hiddenContentStart = {
            ActionsRow(
                onDelete = {
                },
                onEdit = {

                },
                onFavorite = {

                },
                actionIconSize = 40.dp,
                image1 = ImageVector.vectorResource(id = R.drawable.ic_photo_camera_black_24dp),
                color1 = Color(0XFF0084FE),
                tint1 = Color.White,
                image2 = Icons.Filled.Call,
                color2 = Color(0x1A000000),
                tint2 = Color.Black,
                image3 = ImageVector.vectorResource(id = R.drawable.ic_shape__1_),
                color3 = Color(0x1A000000),
                tint3 = Color.Black,
            )
        },
        hiddenContentEnd = {
            ActionsRow(onDelete = {

            }, onEdit = {

            }, onFavorite = {

            }, actionIconSize = 40.dp)
        }
    ) {
        MessageItem(item = item, detailScreen)
    }
}


@Composable
fun TopHeader(userData: UsersData?, openProfile: () -> Unit) {
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
                        .clickable { openProfile.invoke() }
                        .background(
                            Color.Cyan, CircleShape
                        )
                        .align(Alignment.CenterVertically)
                )
                Spacer(modifier = Modifier.width(20.dp))
                Text(
                    text = "Chats",
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
                painterResource(id = R.drawable.ic_take_a_photo),
                contentDescription = "Person",

                modifier = Modifier
                    .align(Alignment.CenterVertically)

            )
            Spacer(modifier = Modifier.width(10.dp))

            Image(
                painterResource(id = R.drawable.ic_new_message),
                contentDescription = "New Message",
                modifier = Modifier
                    .align(Alignment.CenterVertically)

            )
            Spacer(modifier = Modifier.width(20.dp))

        }
    }
}



