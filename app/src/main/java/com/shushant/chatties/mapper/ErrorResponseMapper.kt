
package com.shushant.chatties.mapper

import com.skydoves.sandwich.ApiErrorModelMapper
import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.message

/**
 * A mapper for mapping [ApiResponse.Failure.Error] response as custom [MessengerErrorResponse] instance.
 *
 * @see [ApiErrorModelMapper](https://github.com/skydoves/sandwich#apierrormodelmapper)
 */
object ErrorResponseMapper : ApiErrorModelMapper<MessengerErrorResponse> {

  /**
   * maps the [ApiResponse.Failure.Error] to the [MessengerErrorResponse] using the mapper.
   *
   * @param apiErrorResponse The [ApiResponse.Failure.Error] error response from the network request.
   * @return A customized [MessengerErrorResponse] error response.
   */
  override fun map(apiErrorResponse: ApiResponse.Failure.Error<*>): MessengerErrorResponse {
    return MessengerErrorResponse(apiErrorResponse.statusCode.code, apiErrorResponse.message())
  }
}
