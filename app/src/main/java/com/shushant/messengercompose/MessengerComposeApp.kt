package com.shushant.messengercompose

import android.app.Application
import androidx.lifecycle.ProcessLifecycleOwner
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.shushant.messengercompose.persistence.SharedPrefs
import com.shushant.messengercompose.utils.CustomObserver
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MessengerComposeApp : Application() {

    override fun onCreate() {
        super.onCreate()
        Firebase.database.setPersistenceEnabled(true)
        ProcessLifecycleOwner.get().lifecycle.addObserver(CustomObserver())
        SharedPrefs.initSharedPrefs(this)

    }
}