package com.shushant.chatties.network.service

import com.shushant.chatties.model.UserData
import com.skydoves.sandwich.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MessengerService {

    @GET("user")
    suspend fun getUsers(
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 100
    ): ApiResponse<UserData>
}
