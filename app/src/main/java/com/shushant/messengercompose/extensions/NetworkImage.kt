package com.shushant.messengercompose.extensions

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.shushant.messengercompose.ui.theme.selectedColorNight
import com.skydoves.landscapist.CircularReveal
import com.skydoves.landscapist.ShimmerParams
import com.skydoves.landscapist.coil.CoilImage
import com.skydoves.landscapist.palette.BitmapPalette

/**
 * A wrapper around [CoilImage] setting a default [contentScale] and showing
 * an indicator when loading poster images.
 *
 * @see CoilImage https://github.com/skydoves/landscapist#coil
 */
@Preview
@Composable
fun NetworkImage(
  @PreviewParameter(NetworkUrlPreviewProvider::class) networkUrl: Any?,
  modifier: Modifier = Modifier,
  circularReveal: CircularReveal? = null,
  contentScale: ContentScale = ContentScale.FillBounds,
  bitmapPalette: BitmapPalette? = null,
  shimmerParams: ShimmerParams? = ShimmerParams(
    baseColor = Color.Transparent,
    highlightColor = selectedColorNight,
    dropOff = 0.65f
  ),
) {
  val url = networkUrl ?: return
  if (shimmerParams == null) {
    CoilImage(
      imageModel = url,
      modifier = modifier,
      contentScale = contentScale,
      circularReveal = circularReveal,
      bitmapPalette = bitmapPalette,
      failure = {
        Text(
          text = "image request failed.",
          textAlign = TextAlign.Center,
          style = MaterialTheme.typography.body2,
          modifier = Modifier.fillMaxSize()
        )
      }
    )
  } else {
    CoilImage(
      imageModel = url,
      modifier = modifier,
      contentScale = contentScale,
      circularReveal = circularReveal,
      bitmapPalette = bitmapPalette,
      shimmerParams = shimmerParams,
      failure = {
        Text(
          text = "image request failed.",
          textAlign = TextAlign.Center,
          style = MaterialTheme.typography.body2,
          modifier = Modifier.fillMaxSize()
        )
      }
    )
  }
}