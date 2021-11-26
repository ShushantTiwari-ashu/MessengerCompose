package com.shushant.messengercompose.extensions

import android.os.Bundle
import androidx.navigation.NavType
import com.google.gson.Gson
import com.shushant.messengercompose.model.Data
import com.shushant.messengercompose.model.UsersData

class AssetParamType : NavType<UsersData>(isNullableAllowed = false) {
    override fun get(bundle: Bundle, key: String): UsersData? {
        return bundle.getParcelable(key)
    }

    override fun parseValue(value: String): UsersData {
        return Gson().fromJson(value, UsersData::class.java)
    }

    override fun put(bundle: Bundle, key: String, value: UsersData) {
        bundle.putParcelable(key, value)
    }
}