package com.shushant.messengercompose.ui.screens.chat

import android.app.Activity
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.*
import com.giphy.sdk.ui.views.GiphyDialogFragment
import com.google.accompanist.insets.navigationBarsWithImePadding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.shushant.messengercompose.R
import com.shushant.messengercompose.extensions.NetworkImage
import com.shushant.messengercompose.extensions.getAvatar
import com.shushant.messengercompose.model.*
import com.shushant.messengercompose.ui.screens.MainActivity
import kotlinx.coroutines.launch

@ExperimentalMaterial3Api
@ExperimentalFoundationApi
@Composable
fun ChatDetailScreen(
    openDocument: ActivityResultLauncher<Array<String>>,
    viewModel: ChatDetailViewModel,
    device: UsersData?,
    onBackPressed: () -> Unit
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
                        TopHeaderChat(device, onBackPressed, viewModel)
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
                            },
                            openDocument = openDocument, onAnimationOpen = {
                                clickState.value = clickState.value.not()
                            }
                        )
                        if (clickState.value) {
                            LazyVerticalGrid(
                                cells = GridCells.Fixed(3),
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

@Composable
fun LottieRow(items: LottieFiles, onItemClick: (String) -> Unit) {
    // to keep track if the animation is playing
    // and play pause accordingly
    val isPlaying by remember {
        mutableStateOf(true)
    }
    // for speed
    val speed by remember {
        mutableStateOf(1f)
    }

    // remember lottie composition ,which
    // accepts the lottie composition result
    val composition by rememberLottieComposition(
        LottieCompositionSpec
            .Url(items.fileJson)
    )


    // to control the animation
    val progress by animateLottieCompositionAsState(
        // pass the composition created above
        composition,

        // Iterates Forever
        iterations = LottieConstants.IterateForever,

        // pass isPlaying we created above,
        // changing isPlaying will recompose
        // Lottie and pause/play
        isPlaying = isPlaying,

        // pass speed we created above,
        // changing speed will increase Lottie
        speed = speed,

        // this makes animation to restart when paused and play
        // pass false to continue the animation at which is was paused
        restartOnPlay = false

    )
    Box(
        Modifier
            .background(Color.White, RoundedCornerShape(48.dp))
            .shadow(elevation = 1.dp)
    ) {
        LottieAnimation(
            composition,
            progress,
            modifier = Modifier
                .size(100.dp)
                .align(Alignment.Center)
                .clickable {
                    onItemClick.invoke(items.fileJson)
                }
        )
    }

}

@Composable
fun MessageInput(
    messageInputViewModel: ChatDetailViewModel,
    device: UsersData?,
    onScroll: () -> Unit,
    openDocument: ActivityResultLauncher<Array<String>>,
    onAnimationOpen: () -> Unit
) {
    var inputValue by remember { mutableStateOf("") } // 2
    val user = FirebaseAuth.getInstance().currentUser
    val userID = user?.uid
    val context = LocalContext.current

    fun sendMessage(chatImage: String) { // 3
        val message = Message1(
            date = System.currentTimeMillis() + 500,
            //id = Firebase.database.reference,
            sentTo = device?.uid ?: "",
            sentBy = userID ?: "",
            msgId = java.util.Random().nextInt().toLong(),
            message = inputValue,
            isLeft = false,
            chatImage = if (chatImage.isEmpty()) {
                ""
            } else chatImage,
            userName = user?.displayName ?: "Shushant tiwari",
            status = "sent",
            userImage = user?.displayName ?: "Shushant tiwari".getAvatar()
        )
        messageInputViewModel.saveMessage(
            message, onScroll, device?.uid, userID
        )
        inputValue = ""
    }

    fun sendLike() { // 3
        val message = Message1(
            date = System.currentTimeMillis() + 500,
            //id = Firebase.database.reference,
            sentTo = device?.uid ?: "",
            sentBy = userID ?: "",
            msgId = java.util.Random().nextInt().toLong(),
            message = "Hit like",
            isLeft = false,
            chatImage = "https://assets1.lottiefiles.com/packages/lf20_x99jHr.json",
            userName = user?.displayName ?: "Shushant tiwari",
            status = "sent",
            userImage = user?.displayName ?: "Shushant tiwari".getAvatar()
        )
        messageInputViewModel.saveMessage(
            message, onScroll, device?.uid, userID
        )
        inputValue = ""
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 20.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Image(
            painterResource(id = R.drawable.ic_collapse_input),
            contentDescription = "",
            modifier = Modifier
                .weight(0.1F)
                .align(Alignment.CenterVertically)
                .clickable {

                }
        )
        Box(
            modifier = Modifier
                .weight(0.8F)
                .align(Alignment.CenterVertically)
        ) {
            TextField(
                value = inputValue,
                onValueChange = { inputValue = it },
                shape = RoundedCornerShape(18),
                modifier = Modifier
                    .padding(10.dp)
                    .defaultMinSize(minHeight = 20.dp),
                colors = TextFieldDefaults.textFieldColors(
                    textColor = Color.Black,
                    disabledTextColor = Color.Transparent,
                    backgroundColor = Color(0XFFF2F2F2),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                ),
            )
            Image(painter = painterResource(id = R.drawable.ic_emoji),
                contentDescription = "",
                modifier = Modifier.padding(end = 20.dp)
                    .align(
                        Alignment.CenterEnd
                    )
                    .clickable {
                        onAnimationOpen.invoke()
                    })
        }
        /*keyboardOptions = KeyboardOptions(imeAction = ImeAction.O),
        keyboardActions = KeyboardActions {
            sendMessage()
        }*/

        if (inputValue.isBlank()) {
            Icon( // 6
                painterResource(id = R.drawable.ic_like), tint = Color(0XFF0584FE),
                contentDescription = "Send", modifier = Modifier
                    .weight(0.1F)
                    .clickable {
                        sendLike()
                        //openDocument.launch(arrayOf("image/*"))
                    }
                    .align(Alignment.CenterVertically)
            )
        } else {
            Icon( // 6
                imageVector = Icons.Default.Send, tint = Color(0XFF0584FE),
                contentDescription = "Send", modifier = Modifier
                    .weight(0.1F)
                    .clickable {
                        sendMessage("")
                    }
                    .align(Alignment.CenterVertically)
            )
        }
    }
}

@Composable
fun ChatItemsRight(it: Message1) {
    // to keep track if the animation is playing
    // and play pause accordingly
    var isPlaying by remember {
        mutableStateOf(true)
    }
    // for speed
    val speed by remember {
        mutableStateOf(1f)
    }

    // remember lottie composition ,which
    // accepts the lottie composition result
    val composition by rememberLottieComposition(
        LottieCompositionSpec
            .Url(it.chatImage ?: "")
    )


    // to control the animation
    val progress by animateLottieCompositionAsState(
        // pass the composition created above
        composition,

        // Iterates Forever
        iterations = LottieConstants.IterateForever,

        // pass isPlaying we created above,
        // changing isPlaying will recompose
        // Lottie and pause/play
        isPlaying = isPlaying,

        // pass speed we created above,
        // changing speed will increase Lottie
        speed = speed,

        // this makes animation to restart when paused and play
        // pass false to continue the animation at which is was paused
        restartOnPlay = false

    )


    Row(
        modifier = Modifier
            .fillMaxWidth(1F)
            .padding(end = 20.dp, top = 8.dp, start = 74.dp)
            .wrapContentSize(Alignment.CenterEnd),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End

    ) {
        if (it.chatImage.isNullOrBlank() || it.chatImage?.contains("lottie") != true) {
            Text(
                text = it.message ?: "",
                color = Color.White,
                fontStyle = FontStyle.Normal,
                fontWeight = FontWeight.Black,
                fontFamily = FontFamily.Monospace,
                modifier = Modifier
                    .background(
                        color = Color(0XFF0584FE),
                        RoundedCornerShape(
                            topStart = 18.dp,
                            topEnd = 18.dp,
                            bottomStart = 18.dp,
                            bottomEnd = 4.dp
                        )
                    )
                    .align(Alignment.CenterVertically)
                    .padding(10.dp)
            )

        } else if (it.chatImage?.isNotEmpty()!! && it.chatImage?.contains("lottie") != true) {
            NetworkImage(
                networkUrl = it.chatImage,
                modifier = Modifier
                    .size(150.dp)
                    .clip(RectangleShape)
                    .background(
                        color = Color(0XFF0584FE),
                        RoundedCornerShape(
                            topStart = 18.dp,
                            topEnd = 18.dp,
                            bottomStart = 18.dp,
                            bottomEnd = 18.dp
                        )
                    )
                    .align(Alignment.CenterVertically)
            )
        } else {
            Box(
                Modifier
                    .background(Color.White, RoundedCornerShape(48.dp))
                    .shadow(elevation = 1.dp)
            ) {
                LottieAnimation(
                    composition,
                    progress,
                    modifier = Modifier
                        .size(100.dp)
                        .clickable { isPlaying = !isPlaying }
                )
            }
        }

        Image(
            painter = if (it.status == "sent") painterResource(id = R.drawable.ic_sent) else painterResource(
                id = R.drawable.ic_shape__2_seen
            ),
            contentDescription = "sent",
            modifier = Modifier
                .align(Alignment.Bottom)
                .padding(start = 5.dp)
        )
    }
}

@Composable
fun ChatItemsLeft(it: Message1) {
    // to keep track if the animation is playing
    // and play pause accordingly
    var isPlaying by remember {
        mutableStateOf(true)
    }
    // for speed
    val speed by remember {
        mutableStateOf(1f)
    }

    // remember lottie composition ,which
    // accepts the lottie composition result
    val composition by rememberLottieComposition(
        LottieCompositionSpec
            .Url(it.chatImage ?: "")
    )


    // to control the animation
    val progress by animateLottieCompositionAsState(
        // pass the composition created above
        composition,

        // Iterates Forever
        iterations = LottieConstants.IterateForever,

        // pass isPlaying we created above,
        // changing isPlaying will recompose
        // Lottie and pause/play
        isPlaying = isPlaying,

        // pass speed we created above,
        // changing speed will increase Lottie
        speed = speed,

        // this makes animation to restart when paused and play
        // pass false to continue the animation at which is was paused
        restartOnPlay = false

    )
    Column(
        modifier = Modifier
            .fillMaxWidth(1F)
            .padding(start = 20.dp, top = 8.dp, end = 74.dp)
            .wrapContentSize(Alignment.CenterStart),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.SpaceAround,
    ) {
        if (it.chatImage.isNullOrBlank() || it.chatImage?.contains("lottie") != true) {
            Text(
                text = it.message ?: "",
                color = Color.Black,
                fontStyle = FontStyle.Normal,
                fontWeight = FontWeight.Black,
                fontFamily = FontFamily.Monospace,
                modifier = Modifier
                    .background(
                        color = Color(0XFFF0F0F0),
                        RoundedCornerShape(
                            topStart = 18.dp,
                            topEnd = 18.dp,
                            bottomStart = 18.dp,
                            bottomEnd = 4.dp
                        )
                    )
                    .padding(10.dp)
            )
        } else if (it.chatImage?.isNotEmpty()!! && it.chatImage?.contains("lottie") != true) {
            NetworkImage(
                networkUrl = it.chatImage,
                modifier = Modifier
                    .size(150.dp)
                    .clip(RectangleShape)
                    .background(
                        color = Color(0XFFF0F0F0),
                        RoundedCornerShape(
                            topStart = 18.dp,
                            topEnd = 18.dp,
                            bottomStart = 18.dp,
                            bottomEnd = 18.dp
                        )
                    )
                    .align(Alignment.CenterHorizontally)
            )
        } else {
            Box(
                Modifier
                    .background(Color.White, RoundedCornerShape(48.dp))
                    .shadow(elevation = 1.dp)
            ) {
                LottieAnimation(
                    composition,
                    progress,
                    modifier = Modifier
                        .size(100.dp)
                        .clickable { isPlaying = !isPlaying }
                )
            }
        }
    }
}

@Composable
fun TopHeaderChat(
    device: UsersData?,
    onBackPressed: () -> Unit,
    viewModel: ChatDetailViewModel,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 30.dp, bottom = 30.dp)
            .background(Color.Transparent)
            .navigationBarsWithImePadding(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Column(modifier = Modifier.wrapContentSize(Alignment.CenterStart)) {
            Row {
                Image(
                    painter = painterResource(id = R.drawable.ic_back),
                    contentDescription = "Back button", modifier = Modifier
                        .clickable {
                            onBackPressed()
                            viewModel.clearMessages()
                        }
                        .align(Alignment.CenterVertically)
                        .padding(start = 20.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                NetworkImage(
                    networkUrl = device?.name?.getAvatar(),
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color(0X04000000), CircleShape)
                        .align(Alignment.CenterVertically)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = device?.name ?: "",
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        modifier = Modifier.align(Alignment.Start),
                        textAlign = TextAlign.Start,
                        fontSize = 17.sp
                    )
                    Text(
                        text = "Active now",
                        fontWeight = FontWeight.Normal,
                        color = Color.Black,
                        modifier = Modifier
                            .align(Alignment.Start)
                            .alpha(0.5F),
                        textAlign = TextAlign.Start,
                        fontSize = 17.sp,
                    )
                }
            }
        }
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.align(Alignment.CenterVertically)
        ) {
            Image(
                painterResource(id = R.drawable.ic_call),
                contentDescription = "Person",

                modifier = Modifier
                    .align(Alignment.CenterVertically)

            )
            Spacer(modifier = Modifier.width(10.dp))

            Image(
                painterResource(id = R.drawable.video_call),
                contentDescription = "New Message",
                modifier = Modifier
                    .align(Alignment.CenterVertically)

            )
            Spacer(modifier = Modifier.width(20.dp))

        }
    }
}