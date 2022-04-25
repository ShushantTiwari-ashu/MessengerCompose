package com.shushant.chatties

import android.app.Application
import androidx.lifecycle.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.shushant.chatties.persistence.SharedPrefs
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MessengerComposeApp : Application(), LifecycleEventObserver {

    override fun onCreate() {
        super.onCreate()
        Firebase.database.setPersistenceEnabled(true)
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
        SharedPrefs.initSharedPrefs(this)

    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_RESUME -> {
                val user = FirebaseAuth.getInstance().currentUser
                if (user != null && FirebaseAuth.getInstance().uid !=null) {
                    Firebase.database.reference.child("Users").child(user.uid).child("online")
                        .setValue(true).continueWith {
                            Firebase.database.reference.child("Users").child(user.uid)
                                .child("currentTime").setValue(System.currentTimeMillis())
                        }
                }
            }
            Lifecycle.Event.ON_PAUSE -> {
                val user = FirebaseAuth.getInstance().currentUser
                if (user != null) {
                    Firebase.database.reference.child("Users").child(user.uid).child("online")
                        .setValue(false).continueWith {
                            Firebase.database.reference.child("Users").child(user.uid)
                                .child("currentTime").setValue(System.currentTimeMillis())
                        }
                }

            }
            else -> {

            }
        }
    }

}