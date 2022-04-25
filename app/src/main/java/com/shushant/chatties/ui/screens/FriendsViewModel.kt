package com.shushant.chatties.ui.screens

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.*
import coil.ImageLoader
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.shushant.chatties.model.UsersData
import com.shushant.chatties.network.NetworkState
import com.shushant.chatties.repository.MessengerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
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