package com.shushant.chatties.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.gson.Gson
import com.shushant.chatties.extensions.NetworkImage
import com.shushant.chatties.model.UsersData
import com.shushant.chatties.persistence.SharedPrefs
import java.util.*
import com.shushant.chatties.R


@Composable
fun ProfileScreen(logout:()->Unit) {
    val userData = remember {
        mutableStateOf(SharedPrefs.read("User", ""))
    }
    val data = Gson().fromJson(userData.value, UsersData::class.java)

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(25.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Icon(
            Icons.Outlined.Logout,
            contentDescription = "",
            tint = Color(0XFF0584FE),
            modifier = Modifier.align(Alignment.End).padding(20.dp).clickable {
                logout.invoke()
            }
        )
        Box(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 60.dp)
        ) {
            Image(painter = painterResource(id = R.drawable.ic_code), contentDescription = "")
            NetworkImage(
                networkUrl = data?.profilePic,
                modifier = Modifier
                    .size(110.dp)
                    .padding(5.dp)
                    .clip(CircleShape)
                    .background(
                        Color.Cyan, CircleShape
                    )
                    .align(Alignment.Center)
            )
        }

        Text(text = data?.name?.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
            ?: "", fontStyle = FontStyle.Normal, fontWeight = FontWeight.Bold, fontSize = 20.sp)
    }
}

