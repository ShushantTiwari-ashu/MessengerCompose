package com.shushant.chatties.ui.screens.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.ImageLoader
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.shushant.chatties.extensions.MESSAGES_CHILD
import com.shushant.chatties.model.ConversationUiState
import com.shushant.chatties.model.Message1
import com.shushant.chatties.model.Messages
import com.shushant.chatties.repository.MessengerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ChatDetailViewModel @Inject constructor(
    val imageLoader: ImageLoader,
    private val messengerRepository: MessengerRepository,
) : ViewModel() {
    val state = ConversationUiState()

    var id: String = "0"
    private var db: FirebaseDatabase = Firebase.database

    private val _message = MutableLiveData("")
    val message: LiveData<String> = _message

    val messages = MutableStateFlow<MutableList<Messages>?>(null)

    fun getMessages(id: String) {
        this.id = id
        viewModelScope.launch {
            messengerRepository.getMessages(
                id,
                db
            ).collectLatest {data->
                state.addList(data.toSet().toMutableList())
                updateStatus(id,FirebaseAuth.getInstance().uid)
            }
        }
    }

    fun clearMessages() {
        messages.value = null
    }


    fun saveMessage(messages: Message1, onScroll: () -> Unit, to: String?, from: String?) {
        if (messages.message?.isNotEmpty() == true) {
            val messagesRef =
                db.reference.child("/$MESSAGES_CHILD/${from}/${to}")
            val revmessagesRef =
                db.reference.child("/$MESSAGES_CHILD/${to}/${from}")
            messagesRef.push().setValue(messages).addOnCompleteListener {
                revmessagesRef.push().setValue(messages)
            }.addOnFailureListener {
                Timber.e("""ThrowError ${it.localizedMessage}""")
            }

            val latestMessageRef = db.reference.child("/latest-messages/$from/$to")
            latestMessageRef.setValue(messages)

            val latestMessageToRef = db.reference.child("/latest-messages/$to/$from")
            latestMessageToRef.setValue(messages)
            onScroll()
        }
    }
    private fun updateStatus(to: String?, from: String?){
        state.messages.forEach {
            if (it.status?.contains("sent") == true && it.sentBy != from ){
                it.id?.child("status")?.setValue("seen")
                db.reference.child("/latest-messages/$to/$from").child("status").setValue("seen")
            }
        }
    }

}