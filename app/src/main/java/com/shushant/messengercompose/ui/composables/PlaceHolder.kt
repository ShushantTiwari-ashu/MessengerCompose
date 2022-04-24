package com.shushant.messengercompose.ui.composables

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.shushant.messengercompose.R

@Composable
fun BoxScope.Placeholder(scale: Animatable<Float, AnimationVector1D>? = null, alpha: Float = 1.0f,alignment: Alignment = Alignment.Center) {
    Image(
        painter = rememberAsyncImagePainter(model = R.drawable.chatties_illustration),
        contentDescription = "Logo",
        modifier = Modifier
            .scale(scale?.value ?: 1.0f)
            .size(400.dp)
            .alpha(alpha)
            .align(alignment)
    )
}