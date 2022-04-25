package com.shushant.chatties.model

import androidx.compose.runtime.mutableStateListOf

class ConversationUiState(
    initialMessages:List<Message1> = emptyList()
) {
    private val _messages: MutableList<Message1> =
        mutableStateListOf(*initialMessages.toTypedArray())
    val messages: List<Message1> = _messages

    fun addMessage(msg: Message1) {
        _messages.add(0, msg) // Add to the beginning of the list
    }
    fun addList(messages: MutableList<Message1>){
        _messages.addAll(messages)
        _messages.groupBy { it.msgId }.entries.map {
            it.value.firstOrNull()
        }
    }
}