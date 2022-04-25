package com.shushant.chatties.model


import android.os.Parcelable
import androidx.annotation.Keep
import androidx.compose.runtime.Immutable
import androidx.room.Entity
import com.shushant.chatties.extensions.getRandomBoolean
import com.shushant.chatties.extensions.getRandomString
import kotlinx.parcelize.Parcelize

@Keep
@Immutable
@Parcelize
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
):Parcelable