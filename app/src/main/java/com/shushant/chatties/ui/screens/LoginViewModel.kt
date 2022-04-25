package com.shushant.chatties.ui.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.intl.Locale
import androidx.lifecycle.*
import coil.ImageLoader
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.shushant.chatties.model.UsersData
import com.shushant.chatties.network.NetworkState
import com.shushant.chatties.persistence.SharedPrefs
import com.shushant.chatties.repository.MessengerRepository
import com.shushant.chatties.utils.Utility
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    val imageLoader: ImageLoader,
    private val messengerRepository: MessengerRepository,
) : ViewModel() {
    private var auth = Firebase.auth
    private val _movieLoadingState: MutableState<NetworkState> =
        mutableStateOf(NetworkState.NODATAFOUND)
    val movieLoadingState: State<NetworkState> get() = _movieLoadingState
    fun login(
        context: Context,
        email: String,
        password: String,
        fullname: String,
        loginActivity: LoginActivity,
        updateUI: (UsersData?) -> Unit
    ) {
        _movieLoadingState.value = NetworkState.LOADING
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(loginActivity) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = auth.currentUser
                    val model = UsersData(
                        fullname,
                        email,
                        user?.uid ?: "",
                        password = password,
                        online = true,
                        currentTime = System.currentTimeMillis(),
                        lastLoggedIn = Utility.currentTimeStamp(),
                        appVersion = Utility.applicationVersion(),
                        deviceId = Utility.getDeviceId(),
                        deviceModel = Utility.deviceModel(),
                        deviceOs = Utility.systemOS(),
                        countryName = Locale.current.region
                    );
                    Firebase.database.reference.child("Users").child(user?.uid ?: "")
                        .setValue(model)
                    _movieLoadingState.value = NetworkState.SUCCESS
                    val data = Gson().toJson(model)
                    SharedPrefs.write("User", data)
                    updateUI(model)
                } else {
                    // If sign in fails, display a message to the user.
                    try {
                        Toast.makeText(
                            loginActivity, "Authentication failed.${task.result}",
                            Toast.LENGTH_SHORT
                        ).show()
                    } catch (e: Exception) {
                        signInUser(
                            context = context,
                            email,
                            password,
                            fullname,
                            loginActivity,
                            updateUI
                        )
                    }
                    _movieLoadingState.value = NetworkState.ERROR
                    updateUI(null)
                }
            }

    }

    private fun signInUser(
        context: Context,
        email: String,
        password: String,
        fullname: String,
        loginActivity: LoginActivity,
        updateUI: (UsersData?) -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(loginActivity) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = auth.currentUser
                    if (email.isNotEmpty()) {
                        val model = UsersData(
                            fullname,
                            email,
                            user?.uid ?: "",
                            password = password,
                            online = true,
                            currentTime = System.currentTimeMillis(),
                            lastLoggedIn = Utility.currentTimeStamp(),
                            appVersion = Utility.applicationVersion(),
                            deviceId = Utility.getDeviceId(),
                            deviceModel = Utility.deviceModel(),
                            deviceOs = Utility.systemOS(),
                            countryName = Locale.current.region
                        )
                        updateUI(model)
                    }
                    _movieLoadingState.value = NetworkState.SUCCESS
                } else {
                    // If sign in fails, display a message to the user.
                    try {
                        Toast.makeText(
                            loginActivity, "Authentication failed.${task.result}",
                            Toast.LENGTH_SHORT
                        ).show()
                    } catch (e: Exception) {
                        signInUser(
                            context = context,
                            email,
                            password,
                            fullname,
                            loginActivity,
                            updateUI
                        )
                    }
                    _movieLoadingState.value = NetworkState.ERROR
                    updateUI(null)
                }
            }
    }

    fun getUserByID(onSuccess:()->Unit) {
        viewModelScope.launch {
            messengerRepository.getUserById(db = Firebase.database).collectLatest {
                onSuccess.invoke()
            }
        }
    }
}