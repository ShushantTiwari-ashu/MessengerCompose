package com.shushant.messengercompose.model

import android.os.Parcelable
import com.shushant.messengercompose.extensions.getAvatar
import kotlinx.parcelize.Parcelize

@Parcelize
data class UsersData(
    var name: String="",
    var email: String="",
    var uid: String="",
    var password: String="",
    var online: Boolean= false,
    var currentTime: Long=0,
    var messages: Messages? = Messages(),
    var accountCreatedOn: Long = 0,
    var appVersion: String = "",
    var deviceId: String = "",
    var deviceModel: String = "",
    var deviceOs: String = "",
    var lastLoggedIn: Long = 0,
    var profilePic: String = name.getAvatar(),
    var status: String = "",
    var phoneNumber: String = "",
    var countryNameCode: String = "",
    var countryPhoneCode: String = "",
    var countryName: String = "",
    var isDetailsAdded: Boolean = false,
):Parcelable