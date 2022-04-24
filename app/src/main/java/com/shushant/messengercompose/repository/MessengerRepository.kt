package com.shushant.messengercompose.repository

import androidx.annotation.WorkerThread
import androidx.compose.ui.platform.LocalContext
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.shushant.messengercompose.extensions.*
import com.shushant.messengercompose.model.Message1
import com.shushant.messengercompose.model.Messages
import com.shushant.messengercompose.model.UsersData
import com.shushant.messengercompose.network.service.MessengerService
import com.shushant.messengercompose.persistence.MessengerDao
import com.shushant.messengercompose.persistence.SharedPrefs
import com.skydoves.sandwich.onError
import com.skydoves.sandwich.onException
import com.skydoves.sandwich.suspendOnSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import timber.log.Timber

class MessengerRepository constructor(
    private val discoverService: MessengerService,
    private val messengerDao: MessengerDao,
) : Repository {
    val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db = Firebase.database

    init {
        Timber.d("Injection DiscoverRepository")
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @WorkerThread
    fun getMessages(id: String, db: FirebaseDatabase) = callbackFlow {
        val listOfMessages = mutableListOf<Message1>()
        db.reference.child("/$MESSAGES_CHILD/${getCurrentUserId()}/${id}").orderByChild("date")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(task: DataSnapshot) {
                    Timber.e(task.value.toString())
                    try {
                        for (item in task.children) {
                            val model = item.getValue(Message1::class.java)
                            if (model != null) {
                                if (!model.message.isNullOrBlank() || !model.chatImage.isNullOrEmpty()) {
                                    model.id = item.ref
                                    listOfMessages.add(model)
                                }
                            }
                        }
                    } catch (e: Exception) {
                    }
                    trySend(listOfMessages)
                }

                override fun onCancelled(error: DatabaseError) {
                    Timber.e(error.details)
                }
            })
        awaitClose {

        }
        Timber.e("items${listOfMessages.size}")
    }.flowOn(Dispatchers.IO)

    @OptIn(ExperimentalCoroutinesApi::class)
    @WorkerThread
    fun getUsers(db: FirebaseDatabase, success: () -> Unit) = callbackFlow {
        val listOfMessages = mutableListOf<UsersData>()
        db.reference.child(USERS)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(task: DataSnapshot) {
                    Timber.e(task.value.toString())
                    try {
                        for (item in task.children) {
                            val model = item.getValue(UsersData::class.java)
                            if (model != null) {
                                if (model.uid != FirebaseAuth.getInstance().currentUser?.uid) {
                                    listOfMessages.add(model)
                                } else {
                                    SharedPrefs.write("User", Gson().toJson(model))
                                }
                            }
                        }
                    } catch (e: Exception) {
                        Timber.e(e.localizedMessage)
                    }
                    success.invoke()
                    trySend(listOfMessages.toSet().toMutableList())
                }

                override fun onCancelled(error: DatabaseError) {
                    Timber.e(error.details)
                }
            })
        awaitClose {

        }
        Timber.e("items${listOfMessages.size}")
    }.flowOn(Dispatchers.IO)

    @OptIn(ExperimentalCoroutinesApi::class)
    @WorkerThread
    fun getUserById(db: FirebaseDatabase) = callbackFlow {
        db.reference.child(USERS).child(FirebaseAuth.getInstance().currentUser?.uid ?: "")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(task: DataSnapshot) {
                    Timber.e(task.value.toString())
                    try {
                        for (item in task.children) {
                            val model = item.getValue(UsersData::class.java)
                            if (model != null) {
                                SharedPrefs.write("User", Gson().toJson(model))
                            }
                        }
                    } catch (e: Exception) {
                    }
                    trySend(SharedPrefs.read("User", ""))
                }

                override fun onCancelled(error: DatabaseError) {
                    Timber.e(error.details)
                }
            })
        awaitClose {

        }
    }.flowOn(Dispatchers.IO)

    @OptIn(ExperimentalCoroutinesApi::class)
    @WorkerThread
    fun latestMessages(db: FirebaseDatabase, success: () -> Unit) = callbackFlow {
        val listOfMessages = mutableListOf<Messages>()
        db.reference.child("/latest-messages/${FirebaseAuth.getInstance().currentUser?.uid}")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(task: DataSnapshot) {
                    Timber.e(task.value.toString())
                    try {
                        for (item in task.children) {
                            val model = item.getValue(Messages::class.java)
                            if (model != null) {
                                listOfMessages.add(model)
                            }
                        }
                        trySend(listOfMessages)

                    } catch (e: Exception) {
                        Timber.e(e.localizedMessage)
                    }
                    success.invoke()
                }

                override fun onCancelled(error: DatabaseError) {
                    Timber.e(error.details)
                }
            })
        awaitClose {

        }
        Timber.e("items${listOfMessages.size}")
    }.flowOn(Dispatchers.IO)


    fun checksForUserDetails(callbacks: FirestoreCallbacks) {
        if (firebaseAuth.currentUser != null) {
            val userId = getCurrentUserId()
            db.reference.child(USERS).child(userId)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            val userDetails = snapshot.getValue(UsersData::class.java)
                            if (!userDetails?.uid.isNullOrBlank()) {
                                if (userDetails != null) {
                                    callbacks.userDetails(user = userDetails)
                                }
                            } else {
                                callbacks.isFalse()
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        callbacks.onError(error.message)
                    }
                })
        }
    }

    private fun getCurrentUserId(): String {
        return firebaseAuth.currentUser?.uid ?: ""
    }

    fun setUserDetails(data: UsersData, callbacks: FirestoreCallbacks) {
        if (firebaseAuth.currentUser != null) {
            val userId = getCurrentUserId()
            db.reference.child(USERS).child(userId).setValue(data)
                .addOnCompleteListener { task ->
                    if (task.exception != null) {
                        callbacks.onError(task.exception?.localizedMessage ?: "")
                        return@addOnCompleteListener
                    }
                    callbacks.isTrue()
                }
        }
    }
}
