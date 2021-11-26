package com.shushant.messengercompose.model

import androidx.annotation.Keep
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.shushant.messengercompose.extensions.getRandomBoolean
import com.shushant.messengercompose.utils.Utility.getTimeAgo

@Keep
data class Message1(
    var date: Long? = 1406738574, // 1406738574
    var id: DatabaseReference?=null, // 1
    var sentBy: String? = "",
    var sentTo: String? = "",
    var msgId: Long = 1,
    var isLeft: Boolean? = false, // 1
    var message: String? = "", // Well I can't leave all that to Catelyn now can I!?
    var status: String? = "", // sent
    var userName: String? = "Shushant tiwari", // Rahul  bharadwaj
    var userImage: String? = "",
    var chatImage: String? = "",
    var isDelivered: Boolean? = getRandomBoolean(),
) {
    fun checkIsRight() = sentBy?.equals(FirebaseAuth.getInstance().currentUser?.uid)
    fun getTime() = date?.getTimeAgo()
}
