package com.shushant.messengercompose.extensions

import android.content.res.Resources
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import java.util.*

fun getRandomString(length: Int): String {
    val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
    return (1..length)
        .map { allowedChars.random() }
        .joinToString("")
}

fun getRandomBoolean(): Boolean {
    val random = Random()
    return random.nextBoolean()
}


fun Float.dp(): Float = this * density + 0.5f

val density: Float
    get() = Resources.getSystem().displayMetrics.density

@Composable
fun SnackbarDemo(text: String) {
    val scaffoldState = rememberScaffoldState() // this contains the `SnackbarHostState`
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        modifier = Modifier,
        scaffoldState = scaffoldState // attaching `scaffoldState` to the `Scaffold`
    ) {
        Button(
            onClick = {
                coroutineScope.launch { // using the `coroutineScope` to `launch` showing the snackbar
                    // taking the `snackbarHostState` from the attached `scaffoldState`
                    val snackbarResult = scaffoldState.snackbarHostState.showSnackbar(
                        message = text,
                        actionLabel = "Do something."
                    )
                    when (snackbarResult) {
                        SnackbarResult.Dismissed -> Log.d("SnackbarDemo", "Dismissed")
                        SnackbarResult.ActionPerformed -> Log.d(
                            "SnackbarDemo",
                            "Snackbar's button clicked"
                        )
                    }
                }
            }
        ) {
            Text(text = "A button that shows a Snackbar")
        }
    }
}

@Composable
fun SearchComposable(state: MutableState<TextFieldValue>) {
    TextField(
        value = state.value,
        onValueChange = { value ->
            state.value = value
        },
        textStyle = TextStyle(fontSize = 17.sp),
        leadingIcon = { Icon(Icons.Filled.Search, null, tint = Color(0XFF8E8E93)) },
        trailingIcon = {
            if (state.value != TextFieldValue("")) {
                IconButton(
                    onClick = {
                        state.value =
                            TextFieldValue("") // Remove text from TextField when you press the 'X' icon
                    }
                ) {
                    Icon(
                        Icons.Default.Close,
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
        placeholder = { Text(text = "Search") },
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            backgroundColor = Color.Transparent,
            cursorColor = Color.DarkGray
        )
    )
}