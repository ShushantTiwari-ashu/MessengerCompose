package com.shushant.messengercompose.ui.screens

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.ImageLoader
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.shushant.messengercompose.model.UsersData
import com.shushant.messengercompose.network.NetworkState
import com.shushant.messengercompose.repository.MessengerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
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
    private val _firebaseusers = MutableLiveData<MutableList<UsersData>>()
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