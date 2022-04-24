package com.shushant.messengercompose.utils

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class CustomObserver: LifecycleEventObserver {
    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_RESUME -> {
                FirebaseAuth.getInstance().currentUser?.let {user->
                    Firebase.database.reference.child("Users").child(user.uid).child("online")
                        .setValue(true).continueWith {
                            Firebase.database.reference.child("Users").child(user.uid)
                                .child("currentTime").setValue(System.currentTimeMillis())
                        }
                }
            }
            Lifecycle.Event.ON_PAUSE -> {
                FirebaseAuth.getInstance().currentUser?.let {user->
                    Firebase.database.reference.child("Users").child(user.uid).child("online")
                        .setValue(false).continueWith {
                            Firebase.database.reference.child("Users").child(user.uid)
                                .child("currentTime").setValue(System.currentTimeMillis())
                        }
                }
            }
            else -> {}
        }

    }
}