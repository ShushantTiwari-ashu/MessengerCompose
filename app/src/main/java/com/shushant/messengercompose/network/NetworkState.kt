package com.shushant.messengercompose.network
import androidx.compose.runtime.Composable

enum class NetworkState {
  NODATAFOUND,
  LOADING,
  ERROR,
  SUCCESS
}

@Composable
fun NetworkState.onSuccess(block: @Composable () -> Unit): NetworkState {
  if (this == NetworkState.SUCCESS) {
    block()
  }
  return this
}

@Composable
fun NetworkState.onNoData(block: @Composable () -> Unit): NetworkState {
  if (this == NetworkState.NODATAFOUND) {
    block()
  }
  return this
}

@Composable
fun NetworkState.onLoading(block: @Composable () -> Unit): NetworkState {
  if (this == NetworkState.LOADING) {
    block()
  }
  return this
}
