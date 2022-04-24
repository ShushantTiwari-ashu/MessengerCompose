package com.shushant.messengercompose.ui.screens.chat

import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import com.airbnb.lottie.compose.*
import com.shushant.messengercompose.model.Message1
import java.io.File

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
    val color = remember {
        mutableStateOf(Color.Transparent)
    }
    val imageLoader = ImageLoader.Builder(LocalContext.current)
        .components {
            if (Build.VERSION.SDK_INT >= 28) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }
        .build()

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
        if (it.chatImage.isNullOrBlank()) {
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
            Image(
                painter = rememberAsyncImagePainter(
                    model = it.chatImage,
                    imageLoader = imageLoader,
                    onState = {
                        when (it) {
                            is AsyncImagePainter.State.Error -> {
                                color.value = Color.Blue
                            }
                            else -> {}
                        }
                    }
                ),
                modifier = Modifier
                    .size(150.dp)
                    .clip(RectangleShape)
                    .background(
                        color = color.value,
                        RoundedCornerShape(
                            topStart = 18.dp,
                            topEnd = 18.dp,
                            bottomStart = 18.dp,
                            bottomEnd = 18.dp
                        )
                    )
                    .scale(1F)
                    .align(Alignment.CenterHorizontally),
                contentDescription = null,
                contentScale = ContentScale.FillBounds
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