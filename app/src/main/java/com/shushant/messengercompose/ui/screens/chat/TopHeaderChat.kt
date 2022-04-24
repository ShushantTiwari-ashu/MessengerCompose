package com.shushant.messengercompose.ui.screens.chat

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.*
import com.google.accompanist.insets.navigationBarsWithImePadding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.shushant.messengercompose.R
import com.shushant.messengercompose.extensions.NetworkImage
import com.shushant.messengercompose.extensions.getAvatar
import com.shushant.messengercompose.model.UsersData

@Composable
fun TopHeaderChat(
    device: UsersData?,
    onBackPressed: () -> Unit,
    viewModel: ChatDetailViewModel,
    onVideoCallAudioCall: (Triple<String, Boolean, Boolean>) -> Unit,
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
                    .clickable {
                        onVideoCallAudioCall.invoke(
                            Triple(
                                first = FirebaseAuth.getInstance().currentUser?.uid + "_" + device?.uid,
                                second = false,
                                third = false
                            )
                        )
                    }

            )
            Spacer(modifier = Modifier.width(20.dp))

            Image(
                painterResource(id = R.drawable.video_call),
                contentDescription = "New Message",
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .clickable {
                        onVideoCallAudioCall.invoke(
                            Triple(
                                first = FirebaseAuth.getInstance().currentUser?.uid + "_" + device?.uid,
                                second = true,
                                third = false
                            )
                        )
                    }
            )
            Spacer(modifier = Modifier.width(20.dp))

        }
    }
}