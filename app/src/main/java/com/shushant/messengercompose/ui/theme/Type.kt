package com.shushant.messengercompose.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.shushant.messengercompose.R

// Set of Material typography styles to start with
val Typography = Typography(
    body1 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 13.sp
    ),
    body2 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Light,
        fontSize = 12.sp
    ),
    h1 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp
    ),
    h3 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp
    ),
    subtitle1 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
)

val white30Bold = TextStyle(
    color = Color.White,
    fontSize = 30.sp,
    fontWeight = FontWeight.Bold,
)

val white20 = TextStyle(
    color = Color.White,
    fontSize = 20.sp
)

val black20 = TextStyle(
    color = Color.Black,
    fontSize = 20.sp
)

val black20Bold = TextStyle(
    color = Color.Black,
    fontSize = 20.sp,
    fontWeight = FontWeight.Bold,
    fontFamily = FontFamily(Font(R.font.playfairdisplay_semibold))
)

val black15Bold = TextStyle(
    color = Color.Black,
    fontSize = 15.sp,
    fontWeight = FontWeight.Bold,
)

val actionBold = TextStyle(
    fontWeight = FontWeight.Bold,
    color = Action
)

val gray15 = TextStyle(
    color = Color.Gray,
    fontSize = 15.sp,
    fontFamily = FontFamily(Font(R.font.playfairdisplay_medium))
)
val gray14 = TextStyle(
    color = Color.Gray,
    fontSize = 14.sp,
    fontFamily = FontFamily(Font(R.font.playfairdisplay_medium))
)

val gray12Italic = TextStyle(
    color = Color.Gray,
    fontSize = 15.sp,
    fontStyle = FontStyle.Italic
)

val black15 = TextStyle(
    color = Color.Black,
    fontSize = 15.sp,
)

val gray20Bold = TextStyle(
    color = Color.Gray,
    fontSize = 20.sp,
    fontWeight = FontWeight.Bold,
)

val white20Bold = TextStyle(
    color = Color.White,
    fontSize = 20.sp,
    fontWeight = FontWeight.Bold,
    fontFamily = FontFamily(Font(R.font.roboto_medium_numbers))
)

val white20Bold60 = TextStyle(
    color = Color.White.copy(alpha = 0.6f),
    fontSize = 20.sp,
    fontWeight = FontWeight.Bold,
)

val link15 = TextStyle(
    color = Color.Blue,
    fontSize = 20.sp,
)

val lightGray15 = TextStyle(
    color = Color.LightGray,
    fontSize = 15.sp,
)

val lightGray20 = TextStyle(
    color = Color.LightGray,
    fontSize = 20.sp,
)

val red30Bold = TextStyle(
    color = Color.Red,
    fontSize = 30.sp,
    fontWeight = FontWeight.Bold
)

val action25Bold = TextStyle(
    color = Action,
    fontSize = 25.sp,
    fontWeight = FontWeight.Bold
)