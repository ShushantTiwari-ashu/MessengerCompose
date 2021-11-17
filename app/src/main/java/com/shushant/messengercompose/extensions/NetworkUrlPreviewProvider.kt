package com.shushant.messengercompose.extensions

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.shushant.messengercompose.R

class NetworkUrlPreviewProvider : PreviewParameterProvider<Int> {
  override val count: Int
    get() = super.count
  override val values: Sequence<Int>
    get() = sequenceOf(R.drawable.ic_app)
}