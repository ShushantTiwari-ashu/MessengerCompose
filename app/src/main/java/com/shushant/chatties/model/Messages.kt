package com.shushant.chatties.model

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.room.Entity
import com.google.firebase.auth.FirebaseAuth
import com.shushant.chatties.extensions.getRandomBoolean
import com.shushant.chatties.utils.Utility.getTimeAgo
import kotlinx.parcelize.Parcelize
import javax.annotation.concurrent.Immutable

@Keep
@Entity(primaryKeys = [("msgId")])
@Immutable
@Parcelize
data class Messages(
    var date: Long?=1406738574, // 1406738574
     var id: String?=null, // 1
    var sentBy:String?="",
    var sentTo:String?="",
    var msgId:Long=1,
    var isLeft: Boolean?=false, // 1
    var message: String?="", // Well I can't leave all that to Catelyn now can I!?
    var status: String?="", // sent
    var userName: String?="Shushant tiwari", // Rahul  bharadwaj
    var userImage: String?="",
    var chatImage:String?="",
    var isDelivered:Boolean?= getRandomBoolean(),
):Parcelable{
    fun checkIsRight()=sentBy?.equals(FirebaseAuth.getInstance().currentUser?.uid)
    fun getTime() = date?.getTimeAgo()
}


