
package com.shushant.chatties.network

import com.shushant.chatties.network.Api.TOKEN
import okhttp3.Interceptor
import okhttp3.Response

internal class RequestInterceptor : Interceptor {
  override fun intercept(chain: Interceptor.Chain): Response {
    val originalRequest = chain.request()
    val url = originalRequest.newBuilder()
      .addHeader("app-id", TOKEN)
      .build()

    val requestBuilder = url.newBuilder()
    val request = requestBuilder.build()
    return chain.proceed(request)
  }
}
