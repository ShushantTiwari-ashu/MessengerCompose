package com.shushant.messengercompose.model


import androidx.annotation.Keep

@Keep
data class UserData(
    var `data`: List<Data>? = listOf(),
    var limit: Int? = 0, // 50
    var page: Int? = 0, // 1
    var total: Int? = 0 // 99
)