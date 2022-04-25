package com.shushant.chatties.extensions

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.shushant.chatties.R

class NetworkUrlPreviewProvider : PreviewParameterProvider<Int> {
  override val count: Int
    get() = super.count
  override val values: Sequence<Int>
    get() = sequenceOf(R.drawable.ic_app)
}