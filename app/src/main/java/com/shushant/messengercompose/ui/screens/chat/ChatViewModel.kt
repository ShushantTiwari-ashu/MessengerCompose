package com.shushant.messengercompose.ui.screens.chat

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.ImageLoader
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.shushant.messengercompose.model.Messages
import com.shushant.messengercompose.model.UsersData
import com.shushant.messengercompose.network.NetworkState
import com.shushant.messengercompose.repository.MessengerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    val imageLoader: ImageLoader,
    private val messengerRepository: MessengerRepository,
) : ViewModel() {

    var id: String = "0"
    private var db: FirebaseDatabase = Firebase.database


    private val _movieLoadingState: MutableState<NetworkState> = mutableStateOf(NetworkState.NODATAFOUND)
    val movieLoadingState: State<NetworkState> get() = _movieLoadingState
    private val _latestMessages = MutableLiveData<MutableList<UsersData>>()
    val latestMessages: LiveData<MutableList<UsersData>> = _latestMessages

    private val _firebaseusers = MutableLiveData<MutableList<UsersData>>()

    private val _message = MutableLiveData("")
    val message: LiveData<String> = _message

    val messages = MutableStateFlow<MutableList<Messages>?>(null)

    fun fetchData() {
        Timber.e("FetchData")
        viewModelScope.launch(Dispatchers.IO) {
            messengerRepository.getUsers(
                db,
                success = {

                },
            ).zip(
                messengerRepository.getlatestMessages(
                    db,
                    success = {  },
                )
            ) { initial, next ->
                val messages = getUsersByLatestMessages(initial, next)
                _latestMessages.postValue(messages)
                if (messages.isNullOrEmpty()){
                    _movieLoadingState.value = NetworkState.NODATAFOUND
                }else{
                    _movieLoadingState.value = NetworkState.SUCCESS
                }
                _firebaseusers.postValue(initial)
                initial
            }.collectLatest {
                _firebaseusers.postValue(it)
            }
        }
    }

    private fun getUsersByLatestMessages(
        initial: MutableList<UsersData>,
        next: MutableList<Messages>
    ): MutableList<UsersData> {
        val listOfUSers = mutableListOf<UsersData>()
        next.forEach { model ->
            val chatPartnerId: String =
                if (model.sentBy == FirebaseAuth.getInstance().uid) {
                    model.sentTo.toString()
                } else {
                    model.sentBy.toString()
                }
            val getUserBYID = initial.find { it.uid == chatPartnerId }
            if (getUserBYID != null) {
                getUserBYID.messages = model
                listOfUSers.add(getUserBYID)
            }
        }
        return listOfUSers
    }


}