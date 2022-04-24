package com.shushant.messengercompose.ui.screens

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.view.animation.OvershootInterpolator
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat.startActivity
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import com.google.firebase.auth.FirebaseAuth
import com.shushant.messengercompose.ui.composables.Placeholder
import com.shushant.messengercompose.ui.theme.MessengerComposeTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay


@SuppressLint("CustomSplashScreen")
@RequiresApi(Build.VERSION_CODES.N)
@AndroidEntryPoint
class LaunchScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MessengerComposeTheme(color = Color(0XFF310BE8)) {
                SplashScreens()
            }
        }
    }

}


@Composable
fun SplashScreens() {
    val context = LocalContext.current
    val scale = remember {
        Animatable(0f)
    }
    val imageLoader = ImageLoader.Builder(LocalContext.current)
        .components {
            if (SDK_INT >= 28) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }
        .build()


    // Animation
    LaunchedEffect(key1 = true) {
        scale.animateTo(
            targetValue = 0.7f,
            // tween Animation
            animationSpec = tween(
                durationMillis = 800,
                easing = {
                    OvershootInterpolator(4f).getInterpolation(it)
                })
        )

        // Customize the delay time
        delay(3000L)
        startActivity(
            context,
            Intent(
                context,
                if (FirebaseAuth.getInstance().currentUser != null) MainActivity::class.java else LoginActivity::class.java
            ),
            null
        )
        val activity = (context as? Activity)
        activity?.finish()
    }

    // Image
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(
                    listOf(
                        Color(0XFF310BE8),
                        Color(0XFF0027FE),
                        Color(0XFF8200FF),
                        Color(0XFFD703FF)
                    )
                )
            )
    ) {
        // Change the logo
        Placeholder(scale)
    }
}