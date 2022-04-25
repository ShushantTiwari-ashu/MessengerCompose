package com.shushant.chatties.ui.screens

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.animation.OvershootInterpolator
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import com.shushant.chatties.ui.theme.MessengerComposeTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import com.google.firebase.auth.FirebaseAuth
import androidx.compose.runtime.*
import com.shushant.chatties.R


@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class LaunchScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MessengerComposeTheme {
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
        delay(2000L)
        //navController.navigate("main_screen")
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
        modifier = Modifier.fillMaxSize()
    ) {
        // Change the logo
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo",
            modifier = Modifier
                .scale(scale.value)
                .size(200.dp)
        )
    }
}