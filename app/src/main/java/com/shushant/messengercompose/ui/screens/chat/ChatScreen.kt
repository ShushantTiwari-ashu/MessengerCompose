package com.shushant.messengercompose.ui.screens.chat

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shushant.messengercompose.R
import com.shushant.messengercompose.extensions.*
import com.shushant.messengercompose.model.Data
import com.shushant.messengercompose.network.NetworkState
import com.shushant.messengercompose.network.onLoading
import com.shushant.messengercompose.ui.screens.MyLazyRow
import kotlinx.coroutines.flow.StateFlow


@ExperimentalMaterialApi
@Composable
fun ChatScreen(viewModel: ChatViewModel) {
    val textState = remember { mutableStateOf(TextFieldValue("")) }
    Column {
        TopHeader()
        SearchComposable(textState)
        MyChatLazyColumn(viewModel,textState)

    }
}



@ExperimentalMaterialApi
@Composable
fun MyChatLazyColumn(viewModel: ChatViewModel, textState: MutableState<TextFieldValue>) {
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
            if (index == 0) {
                MyLazyRow(
                    filteredUsers
                )
            } else {
                SwipeLeftRightCompose(item)
                //SwipeCompose(item)
            }
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
fun SwipeLeftRightCompose(item: Data) {
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
        MessageItem(item = item)
    }
}


@Composable
fun TopHeader() {
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



