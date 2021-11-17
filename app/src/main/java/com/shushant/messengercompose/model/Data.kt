package com.shushant.messengercompose.model


import androidx.annotation.Keep
import androidx.compose.runtime.Immutable
import androidx.room.Entity
import com.shushant.messengercompose.extensions.getRandomBoolean
import com.shushant.messengercompose.extensions.getRandomString

@Keep
@Immutable
@Entity(primaryKeys = [("id")])
data class Data(
    var firstName: String? = "", // Dylan
    var id: String = "", // 60d0fe4f5311236168a109fd
    var lastName: String? = "", // Vasquez
    var picture: String? = "", // https://randomuser.me/api/portraits/med/men/66.jpg
    var title: String? = "", // mr
    var page: Int? = 1,
    var isDelivered: Boolean? = getRandomBoolean(),
    var message:String?=getRandomString(50)
)