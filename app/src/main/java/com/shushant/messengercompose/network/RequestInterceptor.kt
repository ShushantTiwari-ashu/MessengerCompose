
package com.shushant.messengercompose.network

import com.shushant.messengercompose.network.Api.TOKEN
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
