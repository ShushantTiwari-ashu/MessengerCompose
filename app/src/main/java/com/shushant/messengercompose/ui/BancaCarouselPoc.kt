package com.shushant.messengercompose.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import coil.annotation.ExperimentalCoilApi
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.calculateCurrentOffsetForPage
import kotlin.math.absoluteValue

@OptIn(ExperimentalPagerApi::class)
@Composable
fun BancaCarouselPoc() {
    HorizontalPagerWithOffsetTransition(
        Modifier
            .height(180.dp)
    )
}

@OptIn(ExperimentalPagerApi::class, ExperimentalCoilApi::class)
@Composable
fun HorizontalPagerWithOffsetTransition(modifier: Modifier = Modifier) {
    val conf = LocalConfiguration.current
    val width = conf.screenWidthDp
    HorizontalPager(
        count = 10,
        // Add 32.dp horizontal padding to 'center' the pages
        contentPadding = PaddingValues(horizontal = 45.dp),
        modifier = modifier.fillMaxSize()
    ) { page ->
        Card(
            Modifier
                .graphicsLayer {
                    // Calculate the absolute offset for the current page from the
                    // scroll position. We use the absolute value which allows us to mirror
                    // any effects for both directions
                    val pageOffset = calculateCurrentOffsetForPage(page).absoluteValue
                    // We animate the scaleX + scaleY, between 85% and 100%
                    lerp(
                        start = 0.85f,
                        stop = 1.0f,
                        fraction = (if (width >= 600) 1.2f else 1.0f) - pageOffset.coerceIn(0f, 1f)
                    ).also { scale ->
                        scaleX = scale
                        scaleY = scale
                    }

                    // We animate the alpha, between 50% and 100%
                    alpha = lerp(
                        start = 0.09f,
                        stop = 1f,
                        fraction = 1f - pageOffset.coerceIn(0f, 1f)
                    )
                }
                .fillMaxWidth()
                .aspectRatio(1f)
        ) {
            Row(
                horizontalArrangement = Arrangement.Start, modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color(0XFF6AA1B4),
                                Color(0XFF15445B),
                            )
                        )
                    )
            ) {
                Text(text = "Allianz", color = Color.White)
            }

        }
    }
}

