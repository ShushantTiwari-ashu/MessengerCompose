package com.shushant.messengercompose.ui.screens

import android.net.Uri
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.*
import coil.ImageLoader
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.google.gson.Gson
import com.shushant.messengercompose.extensions.MESSAGES_CHILD
import com.shushant.messengercompose.model.LottieFiles
import com.shushant.messengercompose.model.Messages
import com.shushant.messengercompose.model.UsersData
import com.shushant.messengercompose.model.listOFLottieFile
import com.shushant.messengercompose.network.NetworkState
import com.shushant.messengercompose.persistence.SharedPrefs
import com.shushant.messengercompose.repository.MessengerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    val imageLoader: ImageLoader,
    private val messengerRepository: MessengerRepository,
) : ViewModel() {

    var id: String = "0"
    private var db: FirebaseDatabase = Firebase.database


    private val _selectedTab: MutableState<NavigationItem> =
        mutableStateOf(NavigationItem.Chat)
    val selectedTab: State<NavigationItem> get() = _selectedTab
    private val _movieLoadingState: MutableState<NetworkState> =
        mutableStateOf(NetworkState.NODATAFOUND)
    val movieLoadingState: State<NetworkState> get() = _movieLoadingState
    val _firebaseusers = MutableLiveData<MutableList<UsersData>>()
    val firebaseusers: LiveData<MutableList<UsersData>> = _firebaseusers

    private val _message = MutableLiveData("")
    val message: LiveData<String> = _message

    val messages = MutableStateFlow<MutableList<Messages>?>(null)

    fun selectTab(tab: NavigationItem) {
        _selectedTab.value = tab
    }

    fun getUser() = liveData<UsersData> {
        viewModelScope.launch {
            try {
                val data = Gson().fromJson(SharedPrefs.read("User", ""), UsersData::class.java)
                emit(data)
            } catch (e: Exception) {
                emit(UsersData())
            }
        }
    }

    fun sendImage(uri: Uri, mainActivity: MainActivity, idUser: String) {
        val auth = FirebaseAuth.getInstance()
        val tempMessage = Messages(
            sentTo = idUser,
            sentBy = auth.currentUser?.uid,
            id = auth.currentUser?.uid ?: "",
            date = System.currentTimeMillis()
        )
        db.reference.child("/$MESSAGES_CHILD/${auth.currentUser?.uid}/${idUser}")
            .push()
            .setValue(
                tempMessage,
                DatabaseReference.CompletionListener { databaseError, databaseReference ->
                    if (databaseError != null) {
                        Timber.e(
                            """Unable to write message to database.${databaseError.toException()}"""
                        )
                        return@CompletionListener
                    }

                    // Build a StorageReference and then upload the file
                    val key = databaseReference.key
                    val storageReference = Firebase.storage
                        .getReference("${auth.currentUser?.uid}/${idUser}")
                        .child(key!!)
                        .child(uri.lastPathSegment!!)
                    putImageInStorage(storageReference, uri, key, mainActivity, idUser)
                })


    }

    private fun putImageInStorage(
        storageReference: StorageReference,
        uri: Uri,
        key: String?,
        mainActivity: MainActivity,
        idUser: String
    ) {
        // First upload the image to Cloud Storage
        storageReference.putFile(uri)
            .addOnSuccessListener(
                mainActivity
            ) { taskSnapshot -> // After the image loads, get a public downloadUrl for the image
                // and add it to the message.
                taskSnapshot.metadata!!.reference!!.downloadUrl
                    .addOnSuccessListener { uri ->
                        val friendlyMessage =
                            Messages(
                                sentTo = idUser,
                                sentBy = FirebaseAuth.getInstance().currentUser?.uid,
                                chatImage = uri.toString(),
                                date = System.currentTimeMillis()
                            )
                        db.reference.child("/$MESSAGES_CHILD/${FirebaseAuth.getInstance().currentUser?.uid}/${idUser}")
                            .child(key!!)
                            .setValue(friendlyMessage)

                        val latestMessageRef =
                            db.getReference("/latest-messages/${FirebaseAuth.getInstance().currentUser?.uid}/$idUser")
                        latestMessageRef.setValue(friendlyMessage)

                        val latestMessageToRef =
                            db.getReference("/latest-messages/$idUser/${FirebaseAuth.getInstance().currentUser?.uid}")
                        latestMessageToRef.setValue(friendlyMessage)

                    }
            }
            .addOnFailureListener(mainActivity) { e ->
                Timber.e(
                    "Image upload task was unsuccessful.$e"
                )
            }
    }

    init {
        viewModelScope.launch(Dispatchers.IO) {
            messengerRepository.getUsers(
                db,
                success = {
                    _movieLoadingState.value = NetworkState.SUCCESS
                },
            ).collectLatest {
                _firebaseusers.postValue(it)
            }
        }
    }
}