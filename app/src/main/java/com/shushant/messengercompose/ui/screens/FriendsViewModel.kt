package com.shushant.messengercompose.ui.screens

import android.content.Context
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
import com.shushant.messengercompose.model.Messages
import com.shushant.messengercompose.model.UsersData
import com.shushant.messengercompose.network.NetworkState
import com.shushant.messengercompose.persistence.StoreUserEmail
import com.shushant.messengercompose.repository.MessengerRepository
import com.shushant.messengercompose.ui.screens.MainActivity
import com.shushant.messengercompose.ui.screens.NavigationItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class FriendsViewModel @Inject constructor(
    val imageLoader: ImageLoader,
    private val messengerRepository: MessengerRepository,
) : ViewModel() {

    var id: String = "0"
    private var db: FirebaseDatabase = Firebase.database

    private val _movieLoadingState: MutableState<NetworkState> = mutableStateOf(NetworkState.LOADING)
    val movieLoadingState: State<NetworkState> get() = _movieLoadingState
    val _firebaseusers = MutableLiveData<MutableList<UsersData>>()
    val firebaseusers: LiveData<MutableList<UsersData>> = _firebaseusers

    init {
        viewModelScope.launch(Dispatchers.IO) {
            messengerRepository.getUsers(
                db,
                success = {
                    _movieLoadingState.value = NetworkState.SUCCESS
                },
            ).collectLatest {
                _firebaseusers.postValue(it)
                if (it.isEmpty()){
                    _movieLoadingState.value = NetworkState.NODATAFOUND
                }
            }
        }
    }
}