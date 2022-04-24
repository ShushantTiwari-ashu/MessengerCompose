package com.shushant.messengercompose.ui.screens

import android.app.Activity
import android.text.style.AlignmentSpan
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.gson.Gson
import com.shushant.messengercompose.R
import com.shushant.messengercompose.extensions.NetworkImage
import com.shushant.messengercompose.extensions.getAvatar
import com.shushant.messengercompose.model.UsersData
import com.shushant.messengercompose.persistence.SharedPrefs
import java.util.*

@Composable
fun ProfileScreen(logout: () -> Unit, profileViewModel: AuthenticationViewModel = hiltViewModel()) {
    val userData = remember {
        mutableStateOf(SharedPrefs.read("User", ""))
    }
    val data = Gson().fromJson(userData.value, UsersData::class.java)
    val state = remember { mutableStateOf(TextFieldValue("")) }
    val enable = remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

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
            modifier = Modifier
                .align(Alignment.End)
                .padding(20.dp)
                .clickable {
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

        Text(text = "Contact no - " + data?.phoneNumber?.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(
                Locale.ROOT
            ) else it.toString()
        }, fontStyle = FontStyle.Normal, fontWeight = FontWeight.Bold, fontSize = 20.sp)
        TextField(
            value = state.value,
            onValueChange = { value ->
                state.value = value
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                    data.name = state.value.text
                    profileViewModel.updateUserDetails(data,
                        isNewUser = false,
                        home = { enable.value = false },
                        formFill = {})
                }
            ),
            textStyle = TextStyle(fontSize = 17.sp),
            enabled = enable.value,
            trailingIcon = {
                if (!enable.value) {
                    IconButton(
                        onClick = {
                            enable.value = true // Remove text from TextField when you press the 'X' icon
                        }
                    ) {
                        androidx.compose.material.Icon(
                            Icons.Default.Edit,
                            contentDescription = "", tint = Color(0XFF8E8E93),
                            modifier = Modifier
                                .padding(15.dp)
                                .size(24.dp)
                        )
                    }
                }
            },
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth()
                .background(Color(0xFFE7F1F1), RoundedCornerShape(16.dp)),
            placeholder = {
                Text(
                    text = "Name - " + data?.name?.replaceFirstChar {
                        if (it.isLowerCase()) it.titlecase(
                            Locale.ROOT
                        ) else it.toString()
                    },
                    fontStyle = FontStyle.Normal,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            },
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                backgroundColor = Color.Transparent,
                cursorColor = Color.DarkGray
            )
        )
    }
}

