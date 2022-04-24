package com.shushant.messengercompose.ui.screens.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.*
import com.shushant.messengercompose.model.LottieFiles

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