package com.shushant.messengercompose.extensions

import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import com.shushant.messengercompose.network.Api
import kotlinx.coroutines.flow.StateFlow

inline fun <T> LazyListScope.paging(
  items: List<T>,
  currentIndexFlow: StateFlow<Int>,
  threshold: Int = 4,
  pageSize: Int = Api.PAGING_SIZE,
  crossinline fetch: () -> Unit,
  crossinline itemContent: @Composable LazyItemScope.(item: T,index:Int) -> Unit,
) {
  itemsIndexed(items) { index, item ->

    itemContent(item,index)
    if ((index + threshold + 1) >= pageSize * (currentIndexFlow.value - 1)) {
      fetch()
    }
  }
}