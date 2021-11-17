package com.shushant.messengercompose.model

import androidx.annotation.Keep
import javax.annotation.concurrent.Immutable

@Keep
@Immutable
data class Messages(
    var date: Int?=1406738574, // 1406738574
    var id: Int?=1, // 1
    var message: String?="Hi what's up?", // Well I can't leave all that to Catelyn now can I!?
    var status: String?="sent", // sent
    var userImage: String?="https://picsum.photos/200", // https://picsum.photos/200
    var userName: String?="Shushant tiwari" // Rahul  bharadwaj
)