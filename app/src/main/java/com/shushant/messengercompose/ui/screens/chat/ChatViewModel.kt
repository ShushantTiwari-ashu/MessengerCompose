package com.shushant.messengercompose.ui.screens.chat

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.ImageLoader
import com.shushant.messengercompose.model.Data
import com.shushant.messengercompose.network.NetworkState
import com.shushant.messengercompose.repository.MessengerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    val imageLoader: ImageLoader,
    private val messengerRepository: MessengerRepository,
) : ViewModel() {
    val moviePageStateFlow: MutableStateFlow<Int> = MutableStateFlow(1)
    private val _movieLoadingState: MutableState<NetworkState> = mutableStateOf(NetworkState.IDLE)
    val movieLoadingState: State<NetworkState> get() = _movieLoadingState
    val users: State<MutableList<Data>> = mutableStateOf(mutableListOf())

    private val newUsersFlow = moviePageStateFlow.flatMapLatest {
        _movieLoadingState.value = NetworkState.LOADING
        messengerRepository.loadUsers(
            page = it,
            success = { _movieLoadingState.value = NetworkState.SUCCESS },
            error = { _movieLoadingState.value = NetworkState.ERROR }
        )
    }.shareIn(viewModelScope, SharingStarted.WhileSubscribed(), replay = 1)

    fun fetchNextUserPage() {
        if (movieLoadingState.value != NetworkState.LOADING) {
            moviePageStateFlow.value++
        }
    }

    init {
        viewModelScope.launch(Dispatchers.IO) {
            newUsersFlow.collectLatest {
                users.value.addAll(it)
            }
        }
    }
}