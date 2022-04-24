package com.shushant.messengercompose.ui.screens.chat

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.shushant.messengercompose.extensions.getAvatar
import com.shushant.messengercompose.model.Message1
import com.shushant.messengercompose.model.UsersData
import com.shushant.messengercompose.model.listOFLottieFile
import kotlinx.coroutines.launch

@ExperimentalMaterial3Api
@Composable
fun ChatDetailScreen(
    viewModel: ChatDetailViewModel,
    device: UsersData?,
    onVideoCallAudioCall:(Triple<String,Boolean,Boolean>)->Unit,
    onBackPressed: () -> Unit,
) {

    val messages =
        viewModel.state.messages.toSet().toMutableList().toMutableSet().distinctBy { it.msgId }
            .toMutableList()
    messages.sortByDescending { it.date }
    val clickState = remember { mutableStateOf(false) }

    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(key1 = device?.uid) {
        viewModel.getMessages(device?.uid ?: "")
    }

    Surface(color = MaterialTheme.colors.background) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Box(
                    contentAlignment = Alignment.BottomCenter,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Bottom
                    ) {
                        TopHeaderChat(device, onBackPressed, viewModel,onVideoCallAudioCall)
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(weight = 0.83f, fill = true),
                            contentPadding = PaddingValues(
                                top = 15.dp,
                                start = 15.dp,
                                bottom = 15.dp
                            ),
                            reverseLayout = true,
                            state = listState,
                        ) {
                            items(messages) {
                                if (it.checkIsRight() != true) {
                                    ChatItemsLeft(it)
                                } else {
                                    ChatItemsRight(it)
                                }
                            }
                        }
                        MessageInput(
                            viewModel,
                            device,
                            onScroll = {
                                coroutineScope.launch {
                                    listState.scrollToItem(0)
                                }
                            }
                        ) {
                            clickState.value = clickState.value.not()
                        }
                        if (clickState.value) {
                            LazyVerticalGrid(
                                columns = GridCells.Fixed(3),
                                Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight(0.6F)
                            ) {
                                items(listOFLottieFile) { items ->
                                    LottieRow(items, onItemClick = {
                                        clickState.value = clickState.value.not()
                                        val message = Message1(
                                            date = System.currentTimeMillis() + 500,
                                            // id = Firebase.database.reference,
                                            sentTo = device?.uid ?: "",
                                            sentBy = FirebaseAuth.getInstance().currentUser?.uid
                                                ?: "",
                                            msgId = java.util.Random().nextInt().toLong(),
                                            message = "Hit like",
                                            isLeft = false,
                                            chatImage = it,
                                            userName = device?.name ?: "Shushant tiwari",
                                            status = "sent",
                                            userImage = device?.name
                                                ?: "Shushant tiwari".getAvatar()
                                        )
                                        viewModel.saveMessage(
                                            message,
                                            onScroll = {
                                                coroutineScope.launch {
                                                    listState.scrollToItem(0)
                                                }
                                            },
                                            device?.uid,
                                            FirebaseAuth.getInstance().currentUser?.uid ?: ""
                                        )
                                    })
                                }
                            }

                        }
                    }
                }
            }
        }
    }


}








